val scala3Version = "3.3.1"

val catsVersion             = "2.9.0"
val catsMTLVersion          = "1.3.0"
val catsEffectVersion       = "3.5.2"
val monocleVersion          = "3.2.0"
val refinedVersion          = "0.11.0"
val enumeratumVersion       = "1.7.3"
val circeVersion            = "0.14.6"
val chimneyVersion          = "0.8.2"
val pekkoVersion            = "1.0.1"
val pekkoHttpVersion        = "1.0.0"
val pekkoHttpSessionVersion = "0.7.1"

lazy val root = project
  .in(file("."))
  .settings(
    name                                   := "Rensa no Hibiki",
    version                                := "0.1.0-SNAPSHOT",
    scalaVersion                           := scala3Version,
    libraryDependencies ++= Seq(
      // Extensions
      "org.typelevel" %% "cats-core"     % catsVersion,
      "org.typelevel" %% "cats-mtl"      % catsMTLVersion,
      "org.typelevel" %% "cats-effect"   % catsEffectVersion,
      "dev.optics"    %% "monocle-core"  % monocleVersion,
      "dev.optics"    %% "monocle-macro" % monocleVersion,
      "eu.timepit"    %% "refined"       % refinedVersion,
      "eu.timepit"    %% "refined-cats"  % refinedVersion,
      "com.beachape"  %% "enumeratum"    % enumeratumVersion,
      "io.scalaland"  %% "chimney"       % chimneyVersion,
      "io.scalaland"  %% "chimney-cats"  % chimneyVersion,

      // Pekko
      "org.apache.pekko" %% "pekko-actor-typed"         % pekkoVersion,
      "org.apache.pekko" %% "pekko-slf4j"               % pekkoVersion,
      "org.apache.pekko" %% "pekko-stream"              % pekkoVersion,
      "org.apache.pekko" %% "pekko-actor-testkit-typed" % pekkoVersion % Test,

      // HTTP
      "org.apache.pekko" %% "pekko-http"       % pekkoHttpVersion,
      "io.circe"         %% "circe-core"       % circeVersion,
      "com.beachape"     %% "enumeratum-circe" % enumeratumVersion,

      // Authorization & Authentication
      // "com.softwaremill.pekko-http-session" %% "core" % pekkoHttpSessionVersion,
      // "com.softwaremill.pekko-http-session" %% "jwt"  % pekkoHttpSessionVersion,
    ),
    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test,
  )

inThisBuild(
  List(
    scalaVersion      := scala3Version,
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
  )
)
