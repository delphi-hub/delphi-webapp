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

import akka.http.scaladsl.model.HttpMethods
import javax.inject._
import play.api.Configuration
import play.api.mvc._
import models.QueryFormat
import play.api.libs.json._
import play.api.libs.json.Json._
import utils.{BlockingHttpClient, CommonHelper}
import scala.concurrent.Future
import scala.util.{Failure, Success}


/**
  * Created by benhermann on 02.01.18.
  */
@Singleton
class HomeController @Inject()(configuration: Configuration, cc: ControllerComponents) extends AbstractController(cc) {

  /**
    * Create an Action to render an HTML page with a welcome message.
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index : Action[AnyContent] = Action {
    Ok(views.html.index("", "", false))
  }

  /**
    * Executes a given query and shows a result page
    * @param query query to execute
    * @return
    */
  def query() : Action[AnyContent] = Action.async {
    implicit request => {
      val abc= "[using KeyStore]=25"
      val query= toJson(QueryFormat(abc))
      println(query.toString())

      val request = CommonHelper.createPostRequest("https://delphi.cs.uni-paderborn.de/api/search?pretty",
        HttpMethods.POST, query.toString() )

      val result = BlockingHttpClient.executeRequest(request)

      // Todo: How to get data out of an success object.
      val jsonString = result.toString().replace("Success([", "").replace("])", "")

      // Parse the string in json format into a json object
      val jsonObject = Json.parse(jsonString)

      // Get the id element of the json Object
      val idString = jsonObject("id")

      // Parse the relevant part out of the id string.
      val idStringManipulated = idString.toString().split("/:")(1)

      println("This is the damn URL: " + idStringManipulated)

      result match {
        case Success(response) => Future.successful(Ok(views.html.index(response, abc, false)))
        case Failure(_) => Future.successful(Ok(views.html.index("ERROR: Failed to reach server at " + request.uri, abc, true)))
      }
    }
  }
}
