#!/usr/bin/env -S scala-cli shebang

//> using scala 3.3.6

//> using dep "com.madgag.play-git-hub::core:10.0.0-PREVIEW.examine-default-branch.2025-10-01T1252.5ed70273"

import cats.*
import cats.data.*
import cats.effect.unsafe.implicits.global
import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits.*
import cats.syntax.all.*
import com.madgag.github.apps.GitHubAppAuth
import com.madgag.scalagithub
import com.madgag.scalagithub.GitHub
import com.madgag.scalagithub.model.{Comment, PullRequest, RepoId}
import play.api.libs.json.Json.toJson
import play.api.libs.json.{Json, OWrites}
import sttp.model.*

import java.nio.file.{Files, Path}
import scala.collection.immutable.SortedSet
import scala.concurrent.ExecutionContext.Implicits.global

val gitHubAppAuth: GitHubAppAuth = GitHubAppAuth.fromConfigMap(sys.env, "TARGETED_RELEASES")

case class Config(
  pr: PullRequest,
  updates: NonEmptySet[Artifact]
) {
  val groupIds: NonEmptySet[String] = updates.map(_.groupId)
  val versions: NonEmptySet[String] = updates.map(_.version)

  val commitsMessage: String =  "Update ${artifactName} from ${currentVersion} to ${nextVersion}"

  val updatesJson: String = toJson(updates.toSortedSet.toSeq).toString

  val text =
    s"""
      |updates.allow = $updatesJson
      |
      |updates.allowPreReleases = $updatesJson
      |
      |pullRequests.draft = true
      |commits.message = "$commitsMessage"
      |pullRequests.grouping = [{
      |  name = "${pr.prId.slug}",
      |  title = "Update to `${pr.prId.repo.name}` PR #${pr.number} (`${pr.head.ref}`)",
      |  filter = [{"group" = "*"}]
      |}]
      |""".stripMargin
}

case class Artifact(groupId: String, artifactId: String, version: String) {
  val artifactIdWithoutScalaOrSbtVersion: String = artifactId.split('_').head

  lazy val withoutScalaVersion: Artifact = copy(artifactId = artifactIdWithoutScalaOrSbtVersion)
}

object Artifact {
  // c96c2303d065496792e7edb379b0e671f57edbb13cecb6b2d94deb22d1a504d4  ./com/gu/panda-hmac-core_3/11.0.0/panda-hmac-core_3-11.0.0.pom
  def fromSha256PomLine(pomLine: String): Option[Artifact] = {
    val segments = pomLine.split(' ').last.split('/').drop(1).dropRight(1).reverse.toList
    segments match {
      case version :: artifactId :: groupSegments =>
        Some(Artifact(groupSegments.reverse.mkString("."), artifactId, version))
      case _ => None
    }
  }

  given Order[Artifact] = Order.by(Tuple.fromProductTyped)

  given OWrites[Artifact] = Json.writes
}

object HelloWorld extends IOApp {

  def run(args: List[String]) = {
    val prId = PullRequest.Id.from(Uri.unsafeParse(args(0)))
    val outputFile = Path.of(args(1))
    for {
      installationAccess <- gitHubAppAuth.accessSoleInstallation()
      _ <- IO.println(s"Hello, ${installationAccess.installedOnAccount.atLogin}!")
      given GitHub = installationAccess.accountAccess().gitHub
      pr <- summon[GitHub].getPullRequest(prId).map(_.result)
      configOpt <- scalaStewardConfigForMostRecentPreviewReleaseOf(pr).value
      _ <- IO {
        configOpt.fold(println(s"Couldn't generate config for ${pr.html_url}")) { config =>
          Files.writeString(outputFile, config.text)
          println(s"Wrote config to ${outputFile.toAbsolutePath}")
        }
      }
    } yield ()
  }.as(ExitCode.Success)

  def scalaStewardConfigForMostRecentPreviewReleaseOf(pr: PullRequest)(using GitHub): OptionT[IO, Config] =
    findLatestPreviewVersionIn(pr).flatMap(version => scalaStewardConfigFor(pr, version))

  def findLatestPreviewVersionIn(pr: PullRequest)(using GitHub): OptionT[IO,String] =
    OptionT(pr.comments2.list().filter(_.user.login == "gu-scala-library-release[bot]").map(findPreviewVersionIn).unNone.compile.last)

  def findPreviewVersionIn(comment: Comment): Option[String] = comment.body.linesIterator.find(_.contains("-PREVIEW"))

  def tagMessageFor(repoId: RepoId, version: String)(using g: GitHub): IO[String] = for {
    repo <- g.getRepo(repoId).map(_.result)
    ref <- repo.refs.get(s"tags/v$version").map(_.result)
    tag <- repo.tags.get(ref.objectId.name).map(_.result)
  } yield tag.message

  def artifactsFrom(tagMessage: String): Seq[Artifact] =
    tagMessage.linesIterator.filter(_.endsWith(".pom")).flatMap(Artifact.fromSha256PomLine).toSeq

  def scalaStewardConfigFor(pr: PullRequest, version: String)(using GitHub): OptionT[IO, Config] = OptionT(for {
    tagMessage <- tagMessageFor(pr.prId.repo, version)
  } yield for {
    artifacts <- NonEmptySet.fromSet(SortedSet.from(artifactsFrom(tagMessage)))
  } yield Config(pr, artifacts.map(_.withoutScalaVersion)))
}
