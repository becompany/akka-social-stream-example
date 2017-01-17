lazy val root = (project in file(".")).
  settings(
    organization := "ch.becompany",
    name := "akka-social-stream-example",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := "2.11.8",
    libraryDependencies ++= Seq(
      "ch.becompany" %% "akka-social-stream" % "0.1.1-SNAPSHOT",
      "ch.qos.logback" % "logback-classic" % "1.0.13"
    )
  )
