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
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import akka.util.ByteString
import javax.inject._
import play.api.Configuration
import play.api.mvc._
import utils.CommonHelper
import models.{InternalFeature, QueryRequestBody}
import org.parboiled2.ParseError
import de.upb.cs.swt.delphi.core.ql

import scala.util.{Failure, Success, Try}
import de.upb.cs.swt.delphi.core.ql._
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.{Await, Future}
import spray.json.DefaultJsonProtocol

import scala.concurrent.duration._

/**
  * Created by benhermann on 02.01.18.
  */
@Singleton
class HomeController @Inject()(assets: Assets,configuration: Configuration, cc: ControllerComponents) extends AbstractController(cc)
  with SprayJsonSupport with DefaultJsonProtocol {

  var featureList: List[InternalFeature] = List[InternalFeature]()
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

  def search: Action[AnyContent] = Action.async {
    implicit request => {
      val jsonBody: Option[JsValue]  = Some(request.body.asJson.get)
      val jsonEmptyBody = Json.toJson(QueryRequestBody("",Some(0)))
      val json : JsValue = jsonBody.getOrElse(jsonEmptyBody)

      val query = json.as[QueryRequestBody]
      val parser = new Syntax(query.query)
      val parsingResult: Try[ql.Query] = parser.QueryRule.run()
      parsingResult match {
        case Success(parsedQuery) =>
          val invalidFields = checkParsedQuery(parsedQuery, query.limit)
          if (invalidFields.size > 0) {
            // Future.failed(throw new IllegalArgumentException(s"Unknown field name(s) used. (${invalidFields.mkString(",")})"))
            Future.successful(new Status(EXPECTATION_FAILED)(s"Incorrect metric name(s) [${invalidFields.mkString(", ")}] entered"))
          }
          else{
            val searchResult = executeQuery(query.query, query.limit)
            searchResult
          }

        case Failure(e: ParseError) =>
          Future.successful(new Status(EXPECTATION_FAILED)(parser.formatError(e)))
          // Future.failed(throw new IllegalArgumentException(parser.formatError(e)))

        case Failure(_) => Future.failed(throw new IOException("Search query failed"))
      }
    }
  }

  private def checkParsedQuery(parsedQuery: ql.Query, limit: Option[Int]) : Set[String] = {
    val fields = collectFieldNames(parsedQuery.expr)
    val publicFieldNames = featureList.map(i => i.name)
    val invalidFields = fields.toSet.filter(f => !publicFieldNames.contains(f))
    invalidFields
  }

  private def executeQuery(query: String, limit: Option[Int]) :Future[Result] = {

    implicit val system = ActorSystem()
    implicit val ec = system.dispatcher
    implicit val materializer = ActorMaterializer()
    implicit val queryFormat = jsonFormat2(Query)

    val baseUri = Uri(CommonHelper.getDelphiServer)
    val prettyParam = Map("pretty" -> "")
    val searchUri = baseUri.withPath(baseUri.path + "/search").withQuery(akka.http.scaladsl.model.Uri.Query(prettyParam))
    val responseFuture = Marshal(Query(query, limit)).to[RequestEntity] flatMap { entity =>
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
        Future.failed(throw new IOException(code.defaultMessage()))
      }
    }
    val result = Await.result(resultFuture, Duration.Inf)
    val queryResponse: Future[Result] = Future.successful(Ok(result.toString))
    queryResponse
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
      implicit val queryFormat = jsonFormat2(InternalFeature)

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
          Future.failed(throw new IOException(code.defaultMessage()))
        }
      }
      val result = Await.result(resultFuture, Duration.Inf)
      val featureListFuture = Unmarshal(result).to[List[InternalFeature]]
      featureList = Await.result(featureListFuture, Duration.Inf)
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
          Future.failed(throw new IOException(code.defaultMessage()))
        }
      }

      val result = Await.result(resultFuture, Duration.Inf)
      val retrieveResponse: Future[Result] = Future.successful(Ok(result))
      retrieveResponse
    }
  }

  private def collectFieldNames(node: CombinatorialExpr): Seq[String] = {
    node match {
      case AndExpr(left, right) => collectFieldNames(left) ++ collectFieldNames(right)
      case OrExpr(left, right) => collectFieldNames(left) ++ collectFieldNames(right)
      case NotExpr(expr) => collectFieldNames(expr)
      case XorExpr(left, right) => collectFieldNames(left) ++ collectFieldNames(right)
      case EqualExpr(field, _) => Seq(field.fieldName)
      case NotEqualExpr(field, _) => Seq(field.fieldName)
      case GreaterThanExpr(field, _) => Seq(field.fieldName)
      case GreaterOrEqualExpr(field, _) => Seq(field.fieldName)
      case LessThanExpr(field, _) => Seq(field.fieldName)
      case LessOrEqualExpr(field, _) => Seq(field.fieldName)
      case LikeExpr(field, _) => Seq(field.fieldName)
      case IsTrueExpr(field) => Seq(field.fieldName)
      case FieldReference(name) => Seq(name)
      case _ => Seq()
    }
  }

  case class Query(query: String, limit: Option[Int] = Some(CommonHelper.defaultFetchSize))
}



