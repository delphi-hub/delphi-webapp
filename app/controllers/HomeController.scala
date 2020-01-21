// Copyright (C) 2018 The Delphi Team.
// See the LICENCE file distributed with this work for additional
// information regarding copyright ownership.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package controllers


import akka.actor.{ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.stream.ActorMaterializer
import akka.util.ByteString
import javax.inject._
import play.api.Configuration
import play.api.mvc._

import scala.concurrent.{Await, Future}
import de.upb.cs.swt.delphi.client.SearchResult
import de.upb.cs.swt.delphi.client.SearchResultJson._
import spray.json.DefaultJsonProtocol

import scala.concurrent.duration._

/**
  * Created by benhermann on 02.01.18.
  */
@Singleton
class HomeController @Inject()(assets: Assets,configuration: Configuration, cc: ControllerComponents) extends AbstractController(cc)
  with SprayJsonSupport with DefaultJsonProtocol {

  /**
    * Create an Action to render an HTML page with a welcome message.
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */

  /**
    * Executes a given query and shows a result page
    *
    * @param query query to execute
    * @return
    */

  def query(query: String): Action[AnyContent] = Action.async {
    implicit request => {

      implicit val system = ActorSystem()
      implicit val ec = system.dispatcher
      implicit val materializer = ActorMaterializer()
      implicit val queryFormat = jsonFormat2(Query)

      val baseUri = Uri(Config.server)
      val prettyParam = Map("pretty" -> "")
      val searchUri = baseUri.withPath(baseUri.path + "/search").withQuery(akka.http.scaladsl.model.Uri.Query(prettyParam))
      val responseFuture = Marshal(Query(query, Config.limit)).to[RequestEntity] flatMap { entity =>
        Http().singleRequest(HttpRequest(uri = searchUri, method = HttpMethods.POST, entity = entity))
      }


      val response = Await.result(responseFuture, 10 seconds)
      val resultFuture: Future[String] = response match {
        case HttpResponse(StatusCodes.OK, headers, entity, _) =>
          entity.dataBytes.runFold(ByteString(""))(_ ++ _).map { body =>
            body.utf8String
          }
        case resp@HttpResponse(code, _, _, _) => {
          resp.discardEntityBytes()
          Future("")
        }
      }

      val result = Await.result(resultFuture, Duration.Inf)
      val queryResponse: Future[Result] = Future.successful(Ok(result.toString))
      queryResponse
    }
  }

  /**
    * Get list of features
    *
    * @return
    */

  def features(): Action[AnyContent] = Action.async {
    implicit request => {

      implicit val system = ActorSystem()
      implicit val ec = system.dispatcher
      implicit val materializer = ActorMaterializer()

      val featuresUri = sys.env.getOrElse("DELPHI_WEBAPI_URL","https://delphi.cs.uni-paderborn.de/api") + "/features"

      val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = featuresUri, method = HttpMethods.GET))


      val response = Await.result(responseFuture, 10 seconds)

      val resultFuture: Future[String] = response match {
        case HttpResponse(StatusCodes.OK, headers, entity, _) =>
          entity.dataBytes.runFold(ByteString(""))(_ ++ _).map { body =>
            body.utf8String
          }
        case resp@HttpResponse(code, _, _, _) => {
          resp.discardEntityBytes()
          Future("")
        }
      }

      val result = Await.result(resultFuture, Duration.Inf)
      val queryResponse: Future[Result] = Future.successful(Ok(result))
      queryResponse
    }
  }

  /////////
  // you can test this method with the following URL: http://localhost:9000/retrieve/eu.eu-emi:emir-client:1.1.0
  // it sould display the information of eu.eu-emi:emir-client:1.1.0 in an abstract way the
  /////////
  def retrieve(elementId: String): Action[AnyContent] = Action.async {
    implicit request => {

      implicit val system = ActorSystem()
      implicit val ec = system.dispatcher
      implicit val materializer = ActorMaterializer()

      val retrieveUri = sys.env.getOrElse("DELPHI_WEBAPI_URL","https://delphi.cs.uni-paderborn.de/api") + "/retrieve/"+ elementId
      println(retrieveUri)
      val responseRetrieve: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = retrieveUri, method = HttpMethods.GET))

      val response = Await.result(responseRetrieve, 10 seconds)

      val resultFuture: Future[String] = response match {
        case HttpResponse(StatusCodes.OK, headers, entity, _) =>
          entity.dataBytes.runFold(ByteString(""))(_ ++ _).map { body =>
            body.utf8String
          }
        case resp@HttpResponse(code, _, _, _) => {
          resp.discardEntityBytes()
          Future("")
        }
      }

      val result = Await.result(resultFuture, Duration.Inf)
      val retrieveResponse: Future[Result] = Future.successful(Ok(result))
      retrieveResponse
    }
  }

  case class Query(query: String, limit: Option[Int] = None)
}



