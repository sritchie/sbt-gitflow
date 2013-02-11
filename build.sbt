import sbt._
import Defaults._

organization := "com.twitter"

name := "sbt-gitflow"

version := "0.1.0"

resolvers += Resolver.url(
  "sbt-plugin-releases",
  new URL("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases/")
)(Resolver.ivyStylePatterns)

sbtPlugin := true

// Publishing options:
publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { x => false }

libraryDependencies += sbtPluginExtra(
  m = "com.github.gseitz" % "sbt-release" % "0.6", // Plugin module name and version
  sbtV = "0.12",    // SBT version
  scalaV = "2.9.2"    // Scala version compiled the plugin
)

publishTo <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("sonatype-snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("sonatype-releases"  at nexus + "service/local/staging/deploy/maven2")
}

pomExtra := (
  <url>https://github.com/sritchie/sbt-gitflow</url>
  <licenses>
    <license>
      <name>Apache 2</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
      <distribution>repo</distribution>
      <comments>A business-friendly OSS license</comments>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:sritchie/sbt-gitflow.git</url>
    <connection>scm:git:git@github.com:sritchie/sbt-gitflow.git</connection>
  </scm>
  <developers>
    <developer>
      <id>sritchie</id>
      <name>Sam Ritchie</name>
      <url>http://twitter.com/sritchie</url>
    </developer>
  </developers>)
