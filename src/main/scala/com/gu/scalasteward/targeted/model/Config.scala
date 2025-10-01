package com.gu.scalasteward.targeted.model

import cats.data.NonEmptySet
import com.gu.scalasteward.targeted.model.Artifact
import com.madgag.scalagithub.model.PullRequest
import play.api.libs.json.Json.toJson

case class Config(
  pr: PullRequest,
  updates: NonEmptySet[Artifact]
) {
  val commitsMessage: String = "Update ${artifactName} from ${currentVersion} to ${nextVersion}"

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
