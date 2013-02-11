package sbtgitflow

import sbt._
import Keys._
import complete.DefaultParsers._
import sbtrelease.{ ReleaseStep, Version, versionFormatError }
import sbtrelease.ReleaseStateTransformations._
import sbtrelease.{ ReleasePlugin => SBTReleasePlugin }
import SBTReleasePlugin.ReleaseKeys.{ nextVersion, releaseProcess, versions }

object ReleasePlugin extends Plugin {
  def execShell(s: String) = {
    List("sh", "-c", "(" + s + ")") ! new ProcessLogger {
      override def info(s: => String) { println(s) }
      override def error(s: => String) { sys.error(s) }
      override def buffer[T](t: => T) = t
    }
  }

  def execStep(fn: State => String): ReleaseStep = { st: State =>
    val command = fn(st)
    st.log.info("Executing '%s':" format command)
    execShell(command)
    st
  }

  def releaseV(st: State): String =
    st.get(versions).map { _._1 }.getOrElse(sys.error("No versions are set! Was this release part executed before inquireVersions?"))

  lazy val checkGitFlowExists: ReleaseStep = { st: State =>
    "command -v git-flow || echo" !! match {
      case "echo\n" => sys.error("git-flow is required for release. See https://github.com/nvie/gitflow for installation instructions.")
      case _ => st
    }
  }

  lazy val gfrStart = execStep { "git flow release start " + releaseV(_) }
  lazy val gfrFinish = execStep { st =>
    val rv = releaseV(st)
    List(
      "echo 'Releasing %s.' > .git/MY_TAGMSG".format(rv),
      """git config core.editor "mv .git/MY_TAGMSG"""",
      "git flow release finish " + rv,
      "git config --unset core.editor"
    ).mkString("; ")
  }
  lazy val releaseSettings =
    SBTReleasePlugin.releaseSettings ++ Seq(
      nextVersion := { ver =>
        Version(ver).map(_.bumpBugfix.asSnapshot.string).getOrElse(versionFormatError)
      },
      releaseProcess := Seq(
        checkSnapshotDependencies,
        checkGitFlowExists,
        inquireVersions,
        runTest,
        gfrStart,
        setReleaseVersion,
        commitReleaseVersion,
        publishArtifacts,
        gfrFinish,
        setNextVersion,
        commitNextVersion,
        pushChanges)
    )
}
