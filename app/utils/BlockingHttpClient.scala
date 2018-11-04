// Copyright (C) 2018 The Delphi Team.
// See the LICENCE file distributed with this work for additional
// information regarding copyright ownership.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

// http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package utils


import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpEntity, HttpMethods, HttpRequest, HttpResponse, MediaTypes, Uri}
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import akka.util.ByteString

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success, Try}
import MediaTypes._


/***
  * A blocking http client implemented using Akka HTTP
  */
object BlockingHttpClient {

  def doGet(uri : Uri) : Try[String] = {
    implicit val system: ActorSystem = ActorSystem()
    implicit val executionContext: ExecutionContext = system.dispatcher
    implicit val materializer: ActorMaterializer = ActorMaterializer(ActorMaterializerSettings(system))

    try {
      val req: Future[HttpResponse] = Http(system).singleRequest(HttpRequest(method = HttpMethods.GET, uri = uri))
      Await.result(req, Duration.Inf)

      val f = req.value.get.get.entity.dataBytes.runFold(ByteString(""))(_ ++ _)
      Await.result(f, Duration.Inf)

      Success(f.value.get.get.utf8String)
    } catch  {
      case e : Exception => Failure(e)
    } finally {
      system.terminate()
      Await.result(system.whenTerminated, Duration.Inf)
    }

  }

  // data parameter will be """{"name":"Hello"}"""
  def doPost(uri: Uri, data: String) : Try[String] = {
    implicit val system: ActorSystem = ActorSystem()
    implicit val executionContext: ExecutionContext = system.dispatcher
    implicit val materializer: ActorMaterializer = ActorMaterializer(ActorMaterializerSettings(system))
    val bdata = ByteString(data)
    try {
      val req: Future[HttpResponse] = Http(system).singleRequest(HttpRequest(
        method = HttpMethods.POST,
        uri = uri,
        entity = HttpEntity(`application/json`, bdata)
      ))
      Await.result(req, Duration.Inf)

      val f = req.value.get.get.entity.dataBytes.runFold(ByteString(""))(_ ++ _)
      Await.result(f, Duration.Inf)

      Success(f.value.get.get.utf8String)
    } catch  {
      case e : Exception => Failure(e)
    } finally {
      system.terminate()
      Await.result(system.whenTerminated, Duration.Inf)
    }
  }

  def executeGet(target: String, server: String) : Try[String] = {

    val uri = Uri(server)
    doGet(uri.withPath(uri.path + target))

  }

  def executePost(target: String, server: String, data: String) : Try[String] = {

    val uri = Uri(server)
    doPost(uri.withPath(uri.path + target), data)

  }

}