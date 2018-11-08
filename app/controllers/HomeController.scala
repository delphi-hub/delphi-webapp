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

import javax.inject._
import play.api.Configuration
import play.api.mvc._
import utils.BlockingHttpClient
import utils.CommonHelper

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
  def query(query : String) : Action[AnyContent] = Action.async {
    implicit request => {
      val server = CommonHelper.addHttpProtocolIfNotExist(CommonHelper.configuration.webApiUri)
      val getRequest = BlockingHttpClient.executeGet("/search/" + query, server)
      getRequest match {
        case Success(response) => Future.successful(Ok(views.html.index(response, query, false)))
        case Failure(_) => Future.successful(Ok(views.html.index("ERROR: Failed to reach server at " + server, query, true)))
      }
    }
  }

}
