scalaVersion := "3.3.6"
scalacOptions := Seq("-deprecation", "-release:21")

libraryDependencies ++= Seq(
  "com.madgag.play-git-hub" %% "core" % "10.0.0",
  "org.scalatest" %% "scalatest" % "3.2.19" % Test
)

Compile / run / fork := true
