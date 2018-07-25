import com.typesafe.config._

name := "delphi-webapp"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.12.4"

lazy val webapp = (project in file(".")).enablePlugins(PlayScala)
                                      .enablePlugins(BuildInfoPlugin).
                                        settings(
                                          buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
                                          buildInfoPackage := "de.upb.cs.swt.delphi.webapp"
                                        )

resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
libraryDependencies += "com.h2database" % "h2" % "1.4.197"

val conf = ConfigFactory.parseFile(new File("conf/application.conf")).resolve()
val appPortWebapp    = conf.getString("app.portWebapp")

PlayKeys.devSettings := Seq(
    "play.server.http.port" -> appPortWebapp
)                                 
