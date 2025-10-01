package com.gu.scalasteward.targeted.model

import cats.Order
import play.api.libs.json.{Json, OWrites}

object Artifact {
  /**
   * Parses a single line from the HashDeep (https://github.com/jessek/hashdeep) output included in
   * the annotated Git Tag message for a release by our Scala Library Release workflow:
   *
   * https://github.com/guardian/gha-scala-library-release-workflow/blob/fa09703460a2eae0fd7d47b3f3088b34c400f973/actions/sign/action.yml#L106-L110
   *
   * An example line would look like this:
   *
   * `c96c2303d065496792e7edb379b0e671f57edbb13cecb6b2d94deb22d1a504d4  ./com/gu/panda-hmac-core_3/11.0.0/panda-hmac-core_3-11.0.0.pom`
   *
   * We only want to extract the groupId, artifactId & version (not the hash).
   */
  def fromHashdeepPomLine(pomLine: String): Option[Artifact] = {
    val segments = pomLine.split(' ').last.split('/').drop(1).dropRight(1).reverse.toList
    segments match {
      case version :: artifactId :: groupSegments =>
        Some(Artifact(groupSegments.reverse.mkString("."), artifactId, version))
      case _ => None
    }
  }

  given Order[Artifact] = Order.by(Tuple.fromProductTyped) // required by NonEmptySet

  given OWrites[Artifact] = Json.writes
}

case class Artifact(groupId: String, artifactId: String, version: String) {
  val artifactIdWithoutScalaOrSbtVersion: String = artifactId.split('_').head

  lazy val withoutScalaVersion: Artifact = copy(artifactId = artifactIdWithoutScalaOrSbtVersion)
}