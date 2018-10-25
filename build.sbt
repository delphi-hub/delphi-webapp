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


resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test

// Akka dependencies
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-core" % "10.0.11",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.5",

  /*"net.codingwell" %% "scala-guice" % "4.1.0",
  "org.scalatest" % "scalatest_2.12" % "3.0.3",
  "org.mockito" % "mockito-core" % "2.7.22",*/
)

libraryDependencies += specs2 % Test

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
