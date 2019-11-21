import com.typesafe.config._

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
libraryDependencies ++= Seq(guice)
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.9"
libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.3.3"
libraryDependencies += "com.google.code.gson" % "gson" % "2.3.1"
libraryDependencies += "org.scalaj" % "scalaj-http_2.11" % "2.3.0"
libraryDependencies += ws
libraryDependencies += ehcache
libraryDependencies += "org.apache.httpcomponents" % "httpmime" % "4.5.10"
libraryDependencies += "org.apache.commons" % "commons-io" % "1.3.2"




resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test

libraryDependencies += "com.pauldijou" %% "jwt-core" % "1.0.0"


// Akka dependencies
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-core" % "10.0.14",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.5"
)

// Pinning secure versions of insecure transitive libraryDependencies
// Please update when updating dependencies above (including Play plugin)
libraryDependencies ++= Seq(
  "com.google.guava" % "guava" % "25.1-jre"
)

val conf = ConfigFactory.parseFile(new File("conf/application.conf")).resolve()
val appPortWebapp    = conf.getString("app.portWebapp")

PlayKeys.devSettings := Seq(
    "play.server.http.port" -> appPortWebapp
)
