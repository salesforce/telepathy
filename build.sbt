lazy val scala212 = "2.12.16"
lazy val scala213 = "2.13.8"
lazy val supportedScalaVersions = List(scala212, scala213)

val circeVersion = "0.14.2"
val okHttpVersion = "4.10.0"

val scalaTestArtifact = "org.scalatest" %% "scalatest" % "3.2.12" % Test
// tool to simplify cross build https://docs.scala-lang.org/overviews/core/collections-migration-213.html
val collectionCompact = "org.scala-lang.modules" %% "scala-collection-compat" % "2.7.0"
val okhttpArtifact = "com.squareup.okhttp3" % "okhttp" % okHttpVersion

lazy val publishSettings = Seq(
  publishMavenStyle := true,
  pomIncludeRepository := { _ => false },
  publishTo := sonatypePublishToBundle.value,
  licenses := Seq("Apache-2.0" -> url("http://opensource.org/licenses/Apache-2.0")),
  homepage := Some(url("https://github.com/salesforce/telepathy")),
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/salesforce/telepathy"),
      "scm:git:git@github.com:salesforce/telepathy.git"
    )
  ),
  credentials += Credentials(
    "Sonatype Nexus Repository Manager",
    "oss.sonatype.org",
    sys.env.getOrElse("SONATYPE_USERNAME",""),
    sys.env.getOrElse("SONATYPE_PASSWORD","")
  ),
  developers := List(
    Developer(
      id = "realstraw",
      name = "Kexin Xie",
      email = "kexin.xie@salesforce.com",
      url = url("http://github.com/realstraw")
    )
  ),
  useGpgPinentry := true
)

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
  headerLicense := Some(HeaderLicense.Custom(
  """|Copyright (c) 2021, salesforce.com, inc.
     |All rights reserved.
     |SPDX-License-Identifier: BSD-3-Clause
     |For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
     |""".stripMargin
  ))
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(publishSettings: _*).
  settings(
    name := "telepathy",
    libraryDependencies ++= Seq(
      okhttpArtifact,
      collectionCompact,
    ) ++ circeArtifacts
  )
