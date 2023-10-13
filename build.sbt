val scala3Version = "3.3.1"

val pekkoVersion = "1.0.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "Pekko Project Template",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "org.apache.pekko" %% "pekko-actor-typed" % pekkoVersion,
      "org.apache.pekko" %% "pekko-slf4j" % pekkoVersion,
      "org.apache.pekko" %% "pekko-actor-testkit-typed" % pekkoVersion % Test
    ),
    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test
  )
