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
package controllers

import akka.actor.ActorSystem
import de.upb.cs.swt.delphi.webapp.BuildInfo
import javax.inject.Inject
import play.api.mvc.{BaseController, ControllerComponents}
import utils.AppLogging
import play.api.mvc._

class SettingsController @Inject()(val controllerComponents: ControllerComponents) extends BaseController  with AppLogging{
  implicit val system: ActorSystem = ActorSystem()

  def stop: Action[AnyContent] = Action { implicit request =>
    sys.addShutdownHook(this.shutDownHook)
    sys.exit(1)
  }


  def version: Action[AnyContent] = Action { implicit request =>
    val version = Ok(BuildInfo.version)
    version
  }

  def shutDownHook: Unit = {
    log.info("Webapp Stopped Successfully")
  }
}
