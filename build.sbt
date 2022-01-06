
lazy val commonSettings = Seq(
  scalaVersion := "2.13.6",
  scalacOptions ++= Seq(
    "-unchecked",
    "-deprecation"
  )
)

lazy val client = (project in file("client"))
  .enablePlugins(ScalaJSBundlerPlugin)
  .settings(commonSettings)
  .settings(
    scalaJSUseMainModuleInitializer := true,
    jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv(),
    testFrameworks += new TestFramework("utest.runner.Framework"),
    libraryDependencies ++= Seq(
      "org.scala-js"                      %%% "scalajs-dom"   % "1.1.0",
      "io.suzaku"                         %%% "diode-core"    % "1.1.13",
      "io.suzaku"                         %%% "diode-react"   % "1.1.13",
      "io.circe"                          %%% "circe-core"    % "0.13.0",
      "io.circe"                          %%% "circe-generic" % "0.13.0",
      "io.circe"                          %%% "circe-parser"  % "0.13.0",
      "com.github.japgolly.scalajs-react" %%% "core"          % "1.7.7",
      "com.github.japgolly.scalajs-react" %%% "extra"         % "1.7.7",
      "com.github.japgolly.scalacss"      %%% "core"          % "0.8.0-RC1",
      "com.github.japgolly.scalacss"      %%% "ext-react"     % "0.8.0-RC1"
    ),
    Compile / npmDependencies ++= Seq("react" -> "17.0.2", "react-dom" -> "17.0.2"),
    (fastOptJS / webpackBundlingMode) := BundlingMode.LibraryAndApplication(),
    Compile / fastOptJS / artifactPath := ((Compile / fastOptJS / crossTarget).value /
    ((fastOptJS / moduleName).value + "-opt.js"))
  )

val elastic4sVersion = "7.10.9"

lazy val server = (project in file("server"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "de.heikoseeberger" %% "akka-http-circe"  % "1.36.0",

      "com.typesafe.akka" %% "akka-http"        % "10.2.4",
      "com.typesafe.akka" %% "akka-stream"      % "2.6.15",
      "com.typesafe.akka" %% "akka-actor-typed" % "2.6.15",

      "io.circe"          %% "circe-core"       % "0.13.0",
      "io.circe"          %% "circe-generic"    % "0.13.0",
      "io.circe"          %% "circe-parser"     % "0.13.0",

      "com.sksamuel.elastic4s" %% "elastic4s-client-esjava" % elastic4sVersion,
      "com.sksamuel.elastic4s" %% "elastic4s-testkit" % elastic4sVersion % "test"
    )
  )

lazy val root = (project in file("."))
  .aggregate(server)
  .aggregate(client)
