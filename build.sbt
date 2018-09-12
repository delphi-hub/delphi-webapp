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
libraryDependencies += "org.parboiled" %% "parboiled" % "2.1.4"
libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.0.11"
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.12"
libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.1"
libraryDependencies += "io.spray" %%  "spray-json" % "1.3.3"
libraryDependencies += "org.parboiled" %% "parboiled" % "2.1.4"
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.4"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.4" % "test"

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
