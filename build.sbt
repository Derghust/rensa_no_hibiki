val scala3Version = "3.3.1"

val catsVersion             = "2.9.0"
val catsMTLVersion          = "1.3.0"
val catsEffectVersion       = "3.5.2"
val monocleVersion          = "3.2.0"
val refinedVersion          = "0.11.0"
val enumeratumVersion       = "1.7.3"
val circeVersion            = "0.14.6"
val chimneyVersion          = "0.8.2"
val doobieVersion           = "1.0.0-RC4"
val fs2Version              = "3.9.3"
val pekkoVersion            = "1.0.1"
val pekkoHttpVersion        = "1.0.0"
val pekkoHttpSessionVersion = "0.7.1"
val scalatestVersion        = "3.2.17"

lazy val root = project
  .in(file("."))
  .settings(
    name         := "Rensa no Hibiki",
    version      := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      // Extensions
      "org.typelevel" %% "cats-core"            % catsVersion,
      "org.typelevel" %% "cats-mtl"             % catsMTLVersion,
      "org.typelevel" %% "cats-effect"          % catsEffectVersion,
      "dev.optics"    %% "monocle-core"         % monocleVersion,
      "dev.optics"    %% "monocle-macro"        % monocleVersion,
      "eu.timepit"    %% "refined"              % refinedVersion,
      "eu.timepit"    %% "refined-cats"         % refinedVersion,
      "com.beachape"  %% "enumeratum"           % enumeratumVersion,
      "io.scalaland"  %% "chimney"              % chimneyVersion,
      "io.scalaland"  %% "chimney-cats"         % chimneyVersion,
      "co.fs2"        %% "fs2-core"             % fs2Version,
      "co.fs2"        %% "fs2-io"               % fs2Version,
      "co.fs2"        %% "fs2-reactive-streams" % fs2Version,
      "co.fs2"        %% "fs2-scodec"           % fs2Version,

      // Pekko
      "org.apache.pekko" %% "pekko-actor-typed"         % pekkoVersion,
      "org.apache.pekko" %% "pekko-slf4j"               % pekkoVersion,
      "org.apache.pekko" %% "pekko-stream"              % pekkoVersion,
      "org.apache.pekko" %% "pekko-actor-testkit-typed" % pekkoVersion % Test,
      "ch.qos.logback"    % "logback-classic"           % "1.1.3"      % Runtime,

      // HTTP
      "org.apache.pekko" %% "pekko-http"       % pekkoHttpVersion,
      "io.circe"         %% "circe-core"       % circeVersion,
      "com.beachape"     %% "enumeratum-circe" % enumeratumVersion,

      // Database
      "org.tpolecat" %% "doobie-core"      % doobieVersion,
      "org.tpolecat" %% "doobie-postgres"  % doobieVersion,
      "org.tpolecat" %% "doobie-refined"   % doobieVersion,
      "org.tpolecat" %% "doobie-scalatest" % doobieVersion % Test,

      // Authorization & Authentication
      // "com.softwaremill.pekko-http-session" %% "core" % pekkoHttpSessionVersion,
      // "com.softwaremill.pekko-http-session" %% "jwt"  % pekkoHttpSessionVersion,

      // Tests
      "org.scalatest" %% "scalatest" % scalatestVersion % Test,
      "org.scalameta" %% "munit"     % "0.7.29"         % Test,
    ),
  )

inThisBuild(
  List(
    scalaVersion      := scala3Version,
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
  )
)
