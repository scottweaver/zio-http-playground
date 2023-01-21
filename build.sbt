val zioVersion = "2.0.5"
val zioHttpVersion = "0.0.3+68-d40d660c+20230121-1559-SNAPSHOT"

lazy val root = project
  .in(file("."))
  .settings(
    inThisBuild(
      List(
        name := "zio-http-playground",
        organization := "dev.zio",
        version := "0.0.1",
        scalaVersion := "3.2.1",
        Compile / run / fork := true,
        Global / cancelable := true
      )
    ),
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % zioVersion,
      "dev.zio" %% "zio-http" % zioHttpVersion,
      "dev.zio" %% "zio-test" % zioVersion % Test,
      "dev.zio" %% "zio-test-sbt" % zioVersion % Test,
      "dev.zio" %% "zio-test-junit" % zioVersion % Test,
      "dev.zio" %% "zio-test-magnolia" % zioVersion % Test
    ),
    testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework"))
  )
