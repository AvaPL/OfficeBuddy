ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.11"

val catsEffectVersion = "3.5.0"
val enumeratumVersion = "1.7.2"
val mockitoScalaVersion = "1.17.14"
val weaverCatsVersion = "0.8.3"

lazy val root = (project in file("."))
  .settings(
    name := "OfficeBuddy",
    idePackagePrefix := Some("io.github.avapl"),
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-effect" % catsEffectVersion,
      "com.beachape" %% "enumeratum" % enumeratumVersion,
      "org.mockito"%% "mockito-scala" % mockitoScalaVersion,
      "org.mockito"%% "mockito-scala-cats" % mockitoScalaVersion,
      "com.disneystreaming" %% "weaver-cats" % weaverCatsVersion % Test
    ),
    testFrameworks += new TestFramework("weaver.framework.CatsEffect")
  )
