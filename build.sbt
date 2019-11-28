import java.io.File

import com.typesafe.config._
import com.typesafe.sbt.packager.MappingsHelper.directory

import scala.sys.process.Process
mappings in Universal ++= directory(baseDirectory.value / "public")
name := "delphi-webapp"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.12.4"

lazy val webapp = (project in file(".")).enablePlugins(PlayScala).
  enablePlugins(BuildInfoPlugin).
  enablePlugins(ScalastylePlugin).
  settings(
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "de.upb.cs.swt.delphi.webapp"
  )

scalastyleConfig := baseDirectory.value / "project" / "scalastyle-config.xml"


resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test

libraryDependencies += "com.pauldijou" %% "jwt-core" % "1.0.0"

// Akka dependencies
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-core" % "10.0.14",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.6"
)
libraryDependencies += "de.upb.cs.swt.delphi" %% "delphi-client" % "0.9.0"
// Pinning secure versions of insecure transitive libraryDependencies
// Please update when updating dependencies above (including Play plugin)
libraryDependencies ++= Seq(
  "com.google.guava" % "guava" % "25.1-jre"
)
//Latest play sbt plugin in location project/plugins.sbt uses different jackson version that has security vulnerability as reported by snyk
//This dependency override can be removed once play updates its jackson version
//dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-databind" % "2.9.10.1" not working

val conf = ConfigFactory.parseFile(new File("conf/application.conf")).resolve()
val appPortWebapp = conf.getString("app.portWebapp")

PlayKeys.devSettings := Seq(
  "play.server.http.port" -> appPortWebapp
)

val config: Config = ConfigFactory.parseFile(new File("conf/frontend.conf")).resolve()
val port = config.getInt("webpack.port")
PlayKeys.playRunHooks += WebpackServer(file("./frontend"))
// Production front-end build
// Play framework hooks for development


lazy val cleanFrontEndBuild = taskKey[Unit]("Remove the old front-end build")

cleanFrontEndBuild := {
  val d = file("public/js")
  if (d.exists()) {
    d.listFiles.foreach(f => {
      if(f.isFile) f.delete

    })
  }
}

lazy val frontEndBuild = taskKey[Unit]("Execute the npm build command to build the front-end")

frontEndBuild := {
  println(Process("npm install", file("frontend")).!!)
  println(Process("npm run build", file("frontend")).!!)
  /*println(Process("npm run dev", file("frontend")).!!)*/
}

frontEndBuild := (frontEndBuild dependsOn cleanFrontEndBuild).value

dist := (dist dependsOn frontEndBuild).value
