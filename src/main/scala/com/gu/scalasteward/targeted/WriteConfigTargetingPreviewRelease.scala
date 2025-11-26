package com.gu.scalasteward.targeted

import cats.*
import cats.data.*
import cats.effect.{ExitCode, IO, Resource, ResourceApp}
import cats.implicits.*
import com.gu.scalasteward.targeted.model.{Artifact, Config}
import com.madgag.github.apps.GitHubAppJWTs
import com.madgag.scalagithub
import com.madgag.scalagithub.GitHub
import com.madgag.scalagithub.model.{Comment, PullRequest, RepoId}
import sttp.model.*

import java.nio.file.{Files, Path}
import scala.collection.immutable.SortedSet
import scala.concurrent.ExecutionContext.Implicits.global

object WriteConfigTargetingPreviewRelease extends ResourceApp {
  def run(args: List[String]): Resource[IO, ExitCode] = {
    val prId = PullRequest.Id.from(Uri.unsafeParse(args(0)))
    val outputFile = Path.of(args(1))
    for {
      gitHubFactory <- GitHub.Factory()
      clientWithAccess <- Resource.eval(gitHubFactory.accessSoleAppInstallation(GitHubAppJWTs.fromConfigMap(sys.env, "TARGETED_RELEASES")))
      exitCode <- Resource.eval(createTargetedReleaseConfigFor(prId, outputFile)(using clientWithAccess.gitHub))
    } yield exitCode
  }

  def createTargetedReleaseConfigFor(prId: PullRequest.Id, outputFile: Path)(using gitHub: GitHub): IO[ExitCode] = for {
    pr <- summon[GitHub].getPullRequest(prId).map(_.result)
    configOpt <- scalaStewardConfigForMostRecentPreviewReleaseOf(pr).value
    exitCode <- IO {
      configOpt.fold {
        Console.err.println(s"Could not generate config for ${pr.html_url}")
        ExitCode.Error
      } { config =>
        Files.writeString(outputFile, config.text)
        println(s"Wrote config to ${outputFile.toAbsolutePath} :\n\n${config.text}")
        ExitCode.Success
      }
    }
  } yield exitCode

  def scalaStewardConfigForMostRecentPreviewReleaseOf(pr: PullRequest)(using GitHub): OptionT[IO, Config] =
    findLatestPreviewVersionIn(pr).flatMap(version => scalaStewardConfigFor(pr, version))

  def findLatestPreviewVersionIn(pr: PullRequest)(using GitHub): OptionT[IO,String] =
    OptionT(pr.comments2.list().filter(_.user.login == "gu-scala-library-release[bot]").map(findPreviewVersionIn).unNone.compile.last)

  def findPreviewVersionIn(comment: Comment): Option[String] = comment.body.linesIterator.find(_.contains("-PREVIEW"))

  def tagMessageFor(repoId: RepoId, version: String)(using g: GitHub): IO[String] = for {
    _ <- IO.println(s"Looking for '${repoId.fullName}' release tag: $version")
    repo <- g.getRepo(repoId).map(_.result)
    ref <- repo.refs.get(s"tags/v$version").map(_.result)
    tag <- repo.tags.get(ref.objectId.name).map(_.result)
  } yield tag.message

  def artifactsFrom(tagMessage: String): Seq[Artifact] =
    tagMessage.linesIterator.filter(_.endsWith(".pom")).flatMap(Artifact.fromHashdeepPomLine).toSeq

  def scalaStewardConfigFor(pr: PullRequest, version: String)(using GitHub): OptionT[IO, Config] = OptionT(for {
    tagMessage <- tagMessageFor(pr.prId.repo, version)
  } yield for {
    artifacts <- NonEmptySet.fromSet(SortedSet.from(artifactsFrom(tagMessage)))
  } yield Config(pr, artifacts.map(_.withoutScalaVersion)))
}
