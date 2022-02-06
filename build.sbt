ThisBuild / organization := "com.github.tototoshi"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.8"

lazy val `web` = project
  .in(file("web"))
  .enablePlugins(PlayScala)
  .settings(
    name := "web",
    libraryDependencies ++= Seq(
      guice,
      "org.scalatest" %% "scalatest" % "3.2.11" % "test"
    )
  )

lazy val `cache` = project
  .in(file("cache"))
  .settings(
    name := "cache"
  )
