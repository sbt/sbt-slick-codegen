import scala.jdk.CollectionConverters._
import java.lang.management.ManagementFactory

lazy val scala212 = "2.12.21"
lazy val scala3 = "3.9.0"

ThisBuild / scalaVersion := scala212
ThisBuild / crossScalaVersions := Seq(scala212, scala3)
ThisBuild / organization := "com.github.sbt"
// Existing release tags use bare versions such as 2.2.0.
ThisBuild / dynverVTagPrefix := false

enablePlugins(SbtPlugin)

sbtPlugin := true

name := """sbt-slick-codegen"""

crossSbtVersions := Seq("1.12.9", "2.1.0-M3")

pluginCrossBuild / sbtVersion := {
  scalaBinaryVersion.value match {
    case "2.12" => "1.12.9"
    case _      => "2.1.0-M3"
  }
}

scalacOptions ++= {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, _)) => Seq("-Xsource:3")
    case _            => Seq.empty
  }
}

val slickVersion = SettingKey[String]("slickVersion")

slickVersion := "3.6.1"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % slickVersion.value,
  "com.typesafe.slick" %% "slick-codegen" % slickVersion.value
)

publishMavenStyle := true

pomIncludeRepository := { _ => false }

Test / publishArtifact := false

ThisBuild / homepage := Some(uri("https://github.com/sbt/sbt-slick-codegen"))
ThisBuild / description := "Generate Slick table mappings from an sbt build"
ThisBuild / licenses := Seq("Apache-2.0" -> uri("https://www.apache.org/licenses/LICENSE-2.0.html"))
ThisBuild / scmInfo := Some(
  ScmInfo(
    uri("https://github.com/sbt/sbt-slick-codegen"),
    "scm:git:git@github.com:sbt/sbt-slick-codegen.git"
  )
)
ThisBuild / developers := List(
  Developer(
    id = "tototoshi",
    name = "Toshiyuki Takahashi",
    email = "",
    url = uri("https://tototoshi.github.io")
  )
)

ThisBuild / githubWorkflowBuild := Seq(WorkflowStep.Sbt(List("test", "scripted")))
ThisBuild / githubWorkflowArtifactUpload := false
ThisBuild / githubWorkflowOSes := Seq("ubuntu-latest", "macos-latest")
ThisBuild / githubWorkflowScalaVersions := Seq(scala212, scala3)
ThisBuild / githubWorkflowJavaVersions := Seq(JavaSpec.temurin("17"), JavaSpec.temurin("21"), JavaSpec.temurin("25"))
ThisBuild / githubWorkflowTargetTags := Seq("*")
ThisBuild / githubWorkflowPublishTargetBranches := Seq(
  RefPredicate.StartsWith(Ref.Tag("")),
  RefPredicate.Equals(Ref.Branch("main"))
)
ThisBuild / githubWorkflowPublish := Seq(
  WorkflowStep.Sbt(
    commands = List("ci-release"),
    name = Some("Publish project"),
    env = Map(
      "PGP_PASSPHRASE" -> "${{ secrets.PGP_PASSPHRASE }}",
      "PGP_SECRET" -> "${{ secrets.PGP_SECRET }}",
      "SONATYPE_PASSWORD" -> "${{ secrets.SONATYPE_PASSWORD }}",
      "SONATYPE_USERNAME" -> "${{ secrets.SONATYPE_USERNAME }}"
    )
  )
)

scriptedBufferLog := false
scriptedLaunchOpts ++= ManagementFactory.getRuntimeMXBean.getInputArguments.asScala.toList.filter(a =>
  Seq("-Xmx", "-Xms", "-XX", "-Dsbt.log.noformat").exists(a.startsWith)
)
scriptedLaunchOpts ++= Seq(
  "-Dplugin.version=" + version.value,
  "-Dslick.version=" + slickVersion.value
)
