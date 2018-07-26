// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.10")

// build management and packaging
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.7.0")
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.2")

// coverage
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.1")
addSbtPlugin("com.codacy" % "sbt-codacy-coverage" % "1.3.12")

// preparation for dependency checking
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.9.1")
