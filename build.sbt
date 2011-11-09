organization := "plalloni"

name := "sutil"

version := "1-SNAPSHOT"

scalaVersion := "2.9.1"

libraryDependencies ++= Seq (
    "org.clapper" %% "grizzled-slf4j" % "0.6.6" % "provided",
    "org.scalaz" %% "scalaz-core" % "6.0.3" % "provided",
    "org.scalaquery" %% "scalaquery" % "0.9.5-plalloni1" % "provided",
    "org.scala-tools.time" %% "time" % "0.5" % "provided",
    "joda-time" % "joda-time" % "2.0" % "provided",
    "org.joda" % "joda-convert" % "1.1" % "provided",
    "org.scalatest" %% "scalatest" % "1.6.1" % "test",
    "ch.qos.logback" % "logback-classic" % "1.0.0" % "test",
    "junit" % "junit" % "4.8" % "test")

publishMavenStyle := true

publishTo <<= version { ver ⇒ Some(
    Resolver
        .file("Local Repository", file(Path.userHome 
            + "/projects/artifacts/maven-" 
            + (if (ver endsWith "-SNAPSHOT") "snapshots" else "releases")))
        .mavenStyle)}
