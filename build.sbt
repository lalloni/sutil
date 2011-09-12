organization := "plalloni"

name := "sutil"

version := "0.1"

scalaVersion := "2.9.1"

libraryDependencies ++= Seq(
    "org.clapper" %% "grizzled-slf4j" % "0.6.6",
    "org.scalaz" %% "scalaz-core" % "6.0.3",
    "org.scalaquery" %% "scalaquery" % "0.9.5-plalloni1",
    "joda-time" % "joda-time" % "2.0",
    //"org.scala-tools.time" %% "time" % "0.5",
    "ch.qos.logback" % "logback-classic" % "0.9.29" % "test")
