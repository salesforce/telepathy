import xerial.sbt.Sonatype.sonatypeCentralHost

lazy val scala213 = "2.13.17"
lazy val scala3 = "3.7.2"
lazy val supportedScalaVersions = List(scala213, scala3)

val circeVersion = "0.14.15"
val okHttpVersion = "5.3.0"

val scalaTestArtifact = "org.scalatest" %% "scalatest" % "3.2.19" % Test
val okhttpArtifact = "com.squareup.okhttp3" % "okhttp-jvm" % okHttpVersion

ThisBuild / sonatypeCredentialHost := sonatypeCentralHost

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
    "central.sonatype.org",
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
  "io.circe" %% "circe-generic"
).map(_ % circeVersion)

lazy val commonSettings = Seq(
  scalacOptions ++= {
    scalaVersion.value match {
      case sv if sv.startsWith("3.") =>
        Seq("-deprecation", "-feature", "-Wall")
      case _ =>
        Seq("-deprecation", "-feature", "-Xlint")
    }
  },
  scalaVersion := scala213,
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
    ) ++ circeArtifacts
  )
