import sbt._
import sbt.Keys._

object Dependencies {
  private def ver(for211: String, for212: String) = Def.setting {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, 11)) => for211
      case Some((2, 12)) => for212
      case _             => sys.error("not good")
    }
  }

  def catsVersion(module: String) = Def.setting {
    module match {
      case "core" =>
        "org.typelevel" %% s"cats-$module" % ver("1.6.1", "2.0.0").value force()
      case "effect" =>
        "org.typelevel" %% s"cats-$module" % ver("1.4.0", "2.0.0").value force()
    }
  }

  def circeVersion(module: String) = Def.setting {
    "io.circe" %% s"circe-$module" % ver("0.11.1", "0.12.2").value
  }

  def http4sVer(module: String) = Def.setting {
    "org.http4s" %% s"http4s-$module" % ver("0.20.15", "0.21.0-M6").value
  }

  val crossScalaVer = List("2.12.10", "2.11.12")
  val scalaVer = crossScalaVer.head

  val cats = catsVersion("core")
  val catsEffect = catsVersion("effect")
  val scaffeine = "com.github.blemale" %% "scaffeine" % "2.6.0"
  val concHashMap = "com.googlecode.concurrentlinkedhashmap" % "concurrentlinkedhashmap-lru" % "1.4.2"
  val geotrellisS3 = "org.locationtech.geotrellis" %% "geotrellis-s3" % "3.2.0"
  val geotrellisGdal = "org.locationtech.geotrellis" %% "geotrellis-gdal" % "3.2.0"
  val geotrellisServerOgc = "com.azavea.geotrellis" %% "geotrellis-server-ogc" % "4.1.0"
  val geotrellisPointcloud ="com.azavea.geotrellis" %% "geotrellis-pointcloud" % "0.3.4-9e63b5a"
  
  val decline = "com.monovore" %% "decline" % "0.5.0"
  val http4sBlazeClient = http4sVer("blaze-client")
  val http4sBlazeServer = http4sVer("blaze-server")
  val http4sCirce = http4sVer("circe")
  val http4sDsl = http4sVer("dsl")
  val http4sXml = http4sVer("scala-xml")

  val pureConfig = "com.github.pureconfig" %% "pureconfig" % "0.12.2"
  val pureConfigCatsEffect = "com.github.pureconfig" %% "pureconfig-cats-effect" % "0.12.2"
  val scalatest = "org.scalatest" %% "scalatest" % "3.1.1"
  
  val logback = "ch.qos.logback" % "logback-classic" % "1.2.3"
  val droste = "io.higherkindness" %% "droste-core" % "0.8.0"

  // This dependency differs between scala 2.11 and 2.12
  val ansiColors211 = "org.backuity" %% "ansi-interpolator" % "1.1"
  val ansiColors212 = "org.backuity" %% "ansi-interpolator" % "1.1.0"
}
