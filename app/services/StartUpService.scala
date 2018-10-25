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
package services

import java.util.concurrent.TimeUnit

import javax.inject.{Singleton, _}
import play.api.inject.ApplicationLifecycle
import utils.Configuration
import utils.instancemanagement.InstanceRegistry

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

/**
  * functions that will be run during request
  */
@Singleton
class StartUpService @Inject()(appLifecycle: ApplicationLifecycle){

  private val configuration = new Configuration()

  /**
    * Will register at the Instance Registry, get an matching WebApi instance and try to connect to it using the
    * /version endpoint. If successful, it will post the matching result true to the IR, otherwise false.
    */
  def doStartUpChecks(): Unit = {


    InstanceRegistry.getWebApiVersion(configuration) match {
      case Success(_) => {
        InstanceRegistry.sendWebApiMatchingResult(true, configuration)
      }
      case Failure(_) => {
        InstanceRegistry.sendWebApiMatchingResult(false, configuration)
        InstanceRegistry.handleInstanceFailure(configuration)
        //Cannot connect to WebApi on startup, so stop execution
        Await.ready(appLifecycle.stop(), Duration(5, TimeUnit.SECONDS))
        System.exit(1)
      }
    }
  }

  appLifecycle.addStopHook { () =>
    InstanceRegistry.handleInstanceStop(configuration)
    Future.successful(())
  }

  doStartUpChecks()
}