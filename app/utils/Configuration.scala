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

import com.typesafe.config.ConfigFactory
import utils.instancemanagement.InstanceEnums.{ComponentType, InstanceState}
import utils.instancemanagement.{Instance, InstanceRegistry}

import scala.util.{Failure, Success, Try}

class Configuration(val bindPort: Int = ConfigFactory.load().getInt("app.portWebapp")) {

  val defaultWebApiPort : Int = ConfigFactory.load().getInt("webapi.port")
  val defaultWebApiHost : String = ConfigFactory.load().getString("webapi.host")
  val instanceName = "WebAppInstance"
  val instanceRegistryUri : String = sys.env.getOrElse("DELPHI_WEBAPI_URI", ConfigFactory.load().getString("instance.registry.path"))

  lazy val webApiUri:String =  webApiInstance.host + ":" + webApiInstance.portNumber

  lazy val webApiInstance : Instance = InstanceRegistry.retrieveWebApiInstance(this) match {
    case Success(instance) => instance
    case Failure(_) => Instance(
      None,
      fallbackWebApiHost,
      fallbackWebApiPort,
      "Default WebApi instance",
      ComponentType.WebApi,
      None,
      InstanceState.Running
    )

  }

  lazy val usingInstanceRegistry : Boolean = assignedID match {
    case Some(_) => true
    case None => false
  }
  lazy val assignedID : Option[Long] = InstanceRegistry.register(this) match {
    case Success(id) => Some(id)
    case Failure(_) => None
  }

  lazy val fallbackWebApiPort : Int = sys.env.get("DELPHI_WEBAPI_URI") match {
    case Some(hostString) => if(hostString.count(c => c == ':') == 2){
      Try(hostString.split(":")(2).toInt) match {
        case Success(port) => port
        case Failure(_) => defaultWebApiPort
      }
    } else {
      defaultWebApiPort
    }
    case None => defaultWebApiPort
  }

  lazy val fallbackWebApiHost : String = sys.env.get("DELPHI_WEBAPI_URI") match {
    case Some(hostString) =>
      if(hostString.count(c => c == ':') == 2){
        hostString.substring(0,hostString.lastIndexOf(":"))
      } else {
        defaultWebApiHost
      }
    case None => defaultWebApiHost

  }
}