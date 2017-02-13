import sbt._
import Keys._

scalaVersion := "2.11.8"

lazy val root = project.in(file("."))

val circeVersion = "0.7.0"
val enumeratumVersion = "1.5.7"
libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser",
  "io.circe" %% "circe-generic-extras",
  "io.circe" %% "circe-literal"
).map(_ % circeVersion)
