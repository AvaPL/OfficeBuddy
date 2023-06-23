ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.11"

val enumeratumVersion = "1.7.2"

lazy val root = (project in file("."))
  .settings(
    name := "OfficeBuddy",
    idePackagePrefix := Some("io.github.avapl"),
    libraryDependencies ++= Seq(
      "com.beachape" %% "enumeratum" % enumeratumVersion
    )
  )
