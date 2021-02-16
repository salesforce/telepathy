lazy val scala212 = "2.12.13"
lazy val scala213 = "2.13.4"
lazy val supportedScalaVersions = List(scala212, scala213)

val circeVersion = "0.13.0"
val okHttpVersion = "4.9.0"

val scalaTestArtifact = "org.scalatest" %% "scalatest" % "3.2.3" % Test
// tool to simplify cross build https://docs.scala-lang.org/overviews/core/collections-migration-213.html
val collectionCompact = "org.scala-lang.modules" %% "scala-collection-compat" % "2.4.1"
val okhttpArtifact = "com.squareup.okhttp3" % "okhttp" % okHttpVersion

val circeArtifacts = Seq(
  "io.circe" %% "circe-parser",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-generic-extras"
).map(_ % circeVersion)

lazy val commonSettings = Seq(
  scalacOptions ++= Seq("-deprecation", "-feature", "-Xlint"),
  scalaVersion := scala212,
  crossScalaVersions := supportedScalaVersions,
  libraryDependencies += scalaTestArtifact,
  organization := "com.salesforce.mce",
  test in assembly := {}  // skip test during assembly
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "telepathy",
    libraryDependencies ++= Seq(
      okhttpArtifact,
      collectionCompact,
    ) ++ circeArtifacts
  )
