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


import java.io.IOException

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.stream.ActorMaterializer
import akka.util.ByteString
import javax.inject._
import play.api.Configuration
import play.api.mvc._
import utils.CommonHelper
import org.parboiled2.ParseError
import de.upb.cs.swt.delphi.core.ql.Syntax

import scala.util.{Failure, Success, Try}
import de.upb.cs.swt.delphi.core.ql

import scala.concurrent.{Await, Future}
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

      val parser = new Syntax(query)
      val parsingResult: Try[ql.Query] = parser.QueryRule.run()
      parsingResult match {
        case Success(_) =>
          val baseUri = Uri(CommonHelper.getDelphiServer())
          val prettyParam = Map("pretty" -> "")
          val searchUri = baseUri.withPath(baseUri.path + "/search").withQuery(akka.http.scaladsl.model.Uri.Query(prettyParam))
          val responseFuture = Marshal(Query(query, CommonHelper.limit)).to[RequestEntity] flatMap { entity =>
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
              //Future("")
              val objectError:Future[String]=Future.failed(throw new IOException(code.defaultMessage()))
              objectError
            }
          }
          val result = Await.result(resultFuture, Duration.Inf)
          val queryResponse: Future[Result] = Future.successful(Ok(result.toString))
          queryResponse

        case Failure(e: ParseError) =>
          val errorResponse: Future[Result] = Future.successful(new Status(EXPECTATION_FAILED)(parser.formatError(e)))
          errorResponse
      }
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

      val featuresUri = CommonHelper.getDelphiServer() + "/features"

      val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = featuresUri, method = HttpMethods.GET))


      val response = Await.result(responseFuture, 10 seconds)

      val resultFuture: Future[String] = response match {
        case HttpResponse(StatusCodes.OK, headers, entity, _) =>
          entity.dataBytes.runFold(ByteString(""))(_ ++ _).map { body =>
            body.utf8String
          }
        case resp@HttpResponse(code, _, _, _) => {
          resp.discardEntityBytes()
          val objectError:Future[String]=Future.failed(throw new IOException(code.defaultMessage()))
          objectError
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

      val retrieveUri = CommonHelper.getDelphiServer() + "/retrieve/" + elementId
      val responseRetrieve: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = retrieveUri, method = HttpMethods.GET))

      val response = Await.result(responseRetrieve, 10 seconds)

      val resultFuture: Future[String] = response match {
        case HttpResponse(StatusCodes.OK, headers, entity, _) =>
          entity.dataBytes.runFold(ByteString(""))(_ ++ _).map { body =>
            body.utf8String
          }
        case resp@HttpResponse(code, _, _, _) => {
          resp.discardEntityBytes()
          val objectError:Future[String]=Future.failed(throw new IOException(code.defaultMessage()))
          objectError
        }
      }

      val result = Await.result(resultFuture, Duration.Inf)
      val retrieveResponse: Future[Result] = Future.successful(Ok(result))
      retrieveResponse
    }
  }

  case class Query(query: String, limit: Option[Int] = None)
}



