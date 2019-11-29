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
package lib

import java.io.File

object WebpackBuildFile {
  private val d = new File("public/bundle")
  val jsBundle: String = if(d.exists && d.isDirectory) {
    d.listFiles.filter(_.isFile).toList.find(f => f.getName.contains("js.bundle.")).get.getName.replace(".gz", "")
  } else ""

  val cssBundle: String = if(d.exists && d.isDirectory) {
    d.listFiles.filter(_.isFile).toList.find(f => f.getName.contains("style.bundle.")).get.getName.replace(".gz", "")
  } else ""

}
