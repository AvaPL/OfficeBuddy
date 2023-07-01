ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.11"

val catsEffectVersion = "3.5.0"
val chimneyVersion = "0.7.5"
val circeVersion = "3.8.15"
val enumeratumVersion = "1.7.2"
val fly4sVersion = "0.0.18"
val http4sVersion = "0.23.21"
val mockitoScalaVersion = "1.17.14"
val quicklensVersion = "1.9.4"
val postgresqlVersion = "42.6.0"
val skunkVersion = "0.6.0"
val tapirVersion = "1.5.5"
val weaverCatsVersion = "0.8.3"

lazy val root = (project in file("."))
  .settings(
    name := "OfficeBuddy",
    idePackagePrefix := Some("io.github.avapl"),
    scalacOptions ++= Seq("-deprecation"),
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-effect" % catsEffectVersion,
      "io.scalaland" %% "chimney" % chimneyVersion,
      "com.beachape" %% "enumeratum" % enumeratumVersion,
      "com.github.geirolz" %% "fly4s-core" % fly4sVersion,
      "org.http4s" %% "http4s-ember-server" % http4sVersion,
      "org.mockito" %% "mockito-scala" % mockitoScalaVersion,
      "org.mockito" %% "mockito-scala-cats" % mockitoScalaVersion,
      "org.postgresql" % "postgresql" % postgresqlVersion,
      "org.tpolecat" %% "skunk-core" % skunkVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % tapirVersion,
      "com.disneystreaming" %% "weaver-cats" % weaverCatsVersion % Test,
      "com.softwaremill.sttp.tapir" %% "tapir-sttp-stub-server" % tapirVersion % Test,
      "com.softwaremill.sttp.client3" %% "circe" % circeVersion % Test
    ),
    testFrameworks += new TestFramework("weaver.framework.CatsEffect")
  )

lazy val integrationTests = (project in file("it"))
  .dependsOn(root) // your current subproject
  .settings(
    idePackagePrefix := Some("io.github.avapl"),
    publish / skip := true,
    Test / parallelExecution := false,
    libraryDependencies ++= Seq(
      "com.softwaremill.quicklens" %% "quicklens" % quicklensVersion % Test,
      "com.disneystreaming" %% "weaver-cats" % weaverCatsVersion % Test
    ),
    testFrameworks += new TestFramework("weaver.framework.CatsEffect")
  )
