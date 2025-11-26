package com.gu.scalasteward.targeted.model

import org.scalatest.OptionValues
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ArtifactTest extends AnyFlatSpec with Matchers with OptionValues {
  it should "read artifact coordinates from a hashdeep line, as written by gha-scala-library-release-workflow" in {
    Artifact.fromHashdeepPomLine("c96c2303d065496792e7edb379b0e671f57edbb13cecb6b2d94deb22d1a504d4  ./com/gu/panda-hmac-core_3/11.0.0/panda-hmac-core_3-11.0.0.pom").value shouldBe
      Artifact("com.gu", "panda-hmac-core_3", "11.0.0")
  }

  it should "remove the Scala version from the artifact id" in {
    Artifact("com.gu", "panda-hmac-core_3", "11.0.0").artifactIdWithoutScalaOrSbtVersion shouldBe "panda-hmac-core"
  }

  it should "remove the Scala & sbt version from a sbt plugin artifact" in {
    Artifact("com.gu", "sbt-scrooge-typescript_2.12_1.0", "4.0.0").artifactIdWithoutScalaOrSbtVersion shouldBe "sbt-scrooge-typescript"
  }
}
