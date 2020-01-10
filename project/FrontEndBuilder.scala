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
import java.io.File
import java.net.InetSocketAddress

import com.typesafe.config._
import play.sbt.PlayRunHook

import scala.sys.process.Process

object FrontEndBuilder {
  def apply(base: File): PlayRunHook = {
    object FrontEndBuilderScript extends PlayRunHook {
      var process: Option[Process] = None
      val config: Config = ConfigFactory.parseFile(new File("conf/frontend.conf")).resolve()
      val isWin: Boolean = System.getProperty("os.name").toUpperCase().indexOf("WIN") >= 0
      override def afterStarted(add: InetSocketAddress): Unit = {
        process = if (isWin){
          Option(Process(s"cmd /c npm run build", base).run)}
        else{
          Option(Process(s"npm run build", base).run)}
      }
      override def afterStopped(): Unit = {
        process.foreach(p => { p.destroy() })
        process = None
      }
    }

    FrontEndBuilderScript
  }
}