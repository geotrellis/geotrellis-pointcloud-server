import Dependencies._
import de.heikoseeberger.sbtheader._

name := "geotrellis-pointcloud-server"
version := "0.1.0-SNAPSHOT"
scalaVersion := "2.12.10"
crossScalaVersions := Seq("2.12.10", "2.11.12")
organization := "geotrellis"
scalacOptions ++= Seq(
  "-deprecation",
  "-unchecked",
  "-language:implicitConversions",
  "-language:reflectiveCalls",
  "-language:higherKinds",
  "-language:postfixOps",
  "-language:existentials",
  "-feature",
  "-Ypartial-unification"
)

shellPrompt := { s => Project.extract(s).currentProject.id + " > " }
run / fork := true
outputStrategy := Some(StdoutOutput)

headerLicense := Some(HeaderLicense.ALv2(java.time.Year.now.getValue.toString, "Azavea"))
headerMappings := Map(
  FileType.scala -> CommentStyle.cStyleBlockComment.copy(commentCreator = new CommentCreator() {
    val Pattern = "(?s).*?(\\d{4}(-\\d{4})?).*".r
    def findYear(header: String): Option[String] = header match {
      case Pattern(years, _) => Some(years)
      case _                 => None
    }
    def apply(text: String, existingText: Option[String]): String = {
      // preserve year of old headers
      val newText = CommentStyle.cStyleBlockComment.commentCreator.apply(text, existingText)
      existingText.flatMap(_ => existingText.map(_.trim)).getOrElse(newText)
    }
  })
)

assembly / test := {}
sources in (Compile, doc) := (sources in (Compile, doc)).value
assembly / assemblyMergeStrategy := {
  case "reference.conf"   => MergeStrategy.concat
  case "application.conf" => MergeStrategy.concat
  case PathList("META-INF", xs @ _*) =>
    xs match {
      case ("MANIFEST.MF" :: Nil) =>
        MergeStrategy.discard
      case ("services" :: _ :: Nil) =>
        MergeStrategy.concat
      case ("javax.media.jai.registryFile.jai" :: Nil) | ("registryFile.jai" :: Nil) | ("registryFile.jaiext" :: Nil) =>
        MergeStrategy.concat
      case (name :: Nil) if name.endsWith(".RSA") || name.endsWith(".DSA") || name.endsWith(".SF") =>
        MergeStrategy.discard
      case _ =>
        MergeStrategy.first
    }
  case _ => MergeStrategy.first
}

Global / cancelable := true
useCoursier := false
javaOptions ++= Seq("-Djava.library.path=/usr/local/lib")

resolvers += "GeoTrellis Bintray Repository" at "https://dl.bintray.com/azavea/geotrellis/"

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full)
addCompilerPlugin("org.scalamacros" %% "paradise" % "2.1.1" cross CrossVersion.full)

libraryDependencies ++= Seq(
  geotrellisS3,
  geotrellisGdal,
  geotrellisServerOgc,
  geotrellisPointcloud,
  http4sDsl.value,
  http4sBlazeServer.value,
  http4sBlazeClient.value,
  http4sCirce.value,
  http4sXml.value,
  logback % Runtime,
  pureConfig,
  pureConfigCatsEffect,
  scaffeine,
  decline,
  scalatest % Test
)

excludeDependencies ++= Seq(
  // log4j brought in via uzaygezen is a pain for us
  ExclusionRule("log4j", "log4j"),
  ExclusionRule("org.slf4j", "slf4j-log4j12"),
  ExclusionRule("org.slf4j", "slf4j-nop")
)

libraryDependencies += (CrossVersion.partialVersion(scalaVersion.value) match {
  case Some((2, scalaMajor)) if scalaMajor >= 12 => ansiColors212 % Provided
  case Some((2, scalaMajor)) if scalaMajor >= 11 => ansiColors211 % Provided
})

enablePlugins(DockerPlugin)

// extract docker org
def dockerize(org: String): String = org.split("\\.").last

imageNames in docker := Seq(
  // Sets the latest tag
  ImageName(s"${dockerize(organization.value)}/${name.value}:latest"),
  // Sets a name with a tag that contains the project version
  ImageName(
    namespace = Some(dockerize(organization.value)),
    repository = name.value,
    tag = Some("v" + version.value)
  )
)

dockerfile in docker := {
  // The assembly task generates a fat JAR file
  val artifact: File = assembly.value
  val artifactTargetPath = s"/app/${artifact.name}"

  new Dockerfile {
    from("daunnc/pdal-debian:2.0.1")
    add(artifact, artifactTargetPath)
    workDir("/app")
    entryPoint("java", "-jar", artifactTargetPath)
  }
}
