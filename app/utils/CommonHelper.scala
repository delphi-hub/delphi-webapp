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
package utils

import akka.http.scaladsl.model.headers.{Host}
import akka.http.scaladsl.model.{HttpMethod, HttpRequest}

import scala.collection.immutable.Seq

object CommonHelper {

  val configuration: Configuration = new Configuration()
  val server = sys.env.getOrElse("DELPHI_SERVER","https://delphi.cs.uni-paderborn.de/api")
  val  urls = configuration.webApiUri
  val defaultFetchSize = 50
  def getDelphiServer(): String = {
    if(!configuration.webApiUri.equals(configuration.defaultWebApiHost)) {
      configuration.webApiUri
    } else {
      server
    }
  }

    def addHttpProtocolIfNotExist(url: String): String = {
      val hasProtocol = url.startsWith("http://") || url.startsWith("https://")
      if(! hasProtocol) {
        "http://" + url //Default protocol is http
      } else {
        url
      }
    }

    def createWebApiRequest(path: String, method: HttpMethod): HttpRequest = {
      val insideContainer = sys.env.get("INSTANCE_ID").isDefined
      val apiInDocker = configuration.webApiInstance.traefikConfiguration.isDefined

      if(apiInDocker && !insideContainer) {
        val traefikHost = configuration.webApiInstance.traefikConfiguration.get.proxyUri
        val apiHostName = configuration.webApiInstance.traefikConfiguration.get.hostName
        //We are outside docker and target is inside -> Need to call traefik
        HttpRequest(method, addHttpProtocolIfNotExist(traefikHost) + path, Seq(Host(apiHostName)))
      } else {
        HttpRequest(method, addHttpProtocolIfNotExist(configuration.webApiUri) + path)
      }

    }

}
