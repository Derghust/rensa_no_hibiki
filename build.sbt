val scala3Version = "3.3.1"

val pekkoVersion = "1.0.1"

lazy val root = project
  .in(file("."))
  .settings(
    name         := "Pekko Project Template",
    version      := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,

    // Pekko library
    libraryDependencies ++= Seq(
      "org.apache.pekko" %% "pekko-actor-typed"         % pekkoVersion,
      "org.apache.pekko" %% "pekko-slf4j"               % pekkoVersion,
      "org.apache.pekko" %% "pekko-actor-testkit-typed" % pekkoVersion % Test,
    ),

    // Enhancement
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core"     % "2.9.0",
      "org.typelevel" %% "cats-effect"   % "3.5.0",
      "org.typelevel" %% "cats-mtl"      % "1.3.0",
      "co.fs2"        %% "fs2-core"      % "3.7.0",
      "dev.optics"    %% "monocle-core"  % "3.2.0",
      "dev.optics"    %% "monocle-macro" % "3.2.0",
    ),

    // Refined
    libraryDependencies ++= Seq(
      "eu.timepit" %% "refined"      % "0.11.0",
      "eu.timepit" %% "refined-cats" % "0.11.0",// optional
      // "io.monix"      %% "newtypes-core" % "0.2.3",
      // "io.github.iltotore" %% "iron"         % "2.2.1",  // Lighweight
    ),

    // Other
    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test,
    scalacOptions ++= Seq(
      "-Ymacro-annotations",
      "-Wconf:cat=unused:info",
    ),
  )
