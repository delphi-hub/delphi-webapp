
import com.typesafe.sbt.packager.MappingsHelper.directory


mappings in Universal ++= directory(baseDirectory.value / "public")
name := "delphi-webapp"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.13.1"

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
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test

libraryDependencies += "com.pauldijou" %% "jwt-core" % "4.3.0"

// Akka dependencies
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-core" % "10.1.11",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.11"
)
libraryDependencies += "de.upb.cs.swt.delphi" %% "delphi-core" % "0.9.2"
// Pinning secure versions of insecure transitive libraryDependencies
// Please update when updating dependencies above (including Play plugin)
libraryDependencies ++= Seq(
  "com.google.guava" % "guava" % "25.1-jre"
)
//Latest play sbt plugin in location project/plugins.sbt uses different jackson version that has security vulnerability as reported by snyk
//This dependency override can be removed once play updates its jackson version
dependencyOverrides ++= Seq(
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.10.2",
  "com.fasterxml.jackson.core" % "jackson-annotations" % "2.10.2",
  "com.fasterxml.jackson.core" % "jackson-core" % "2.10.2"
)

PlayKeys.playRunHooks += FrontEndBuilder(file("./frontend"))
// Production front-end build
// Play framework hooks for development
