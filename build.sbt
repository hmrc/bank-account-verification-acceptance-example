lazy val testSuite = (project in file("."))
  .enablePlugins(SbtAutoBuildPlugin)
  .disablePlugins(JUnitXmlReportPlugin) //Required to prevent https://github.com/scalatest/scalatest/issues/1427
  .settings(
    organization := "uk.gov.hmrc",
    name := "bank-account-verification-acceptance-example",
    version := "0.1.0",
    scalaVersion := "2.12.12",
    initialCommands in console := "import uk.gov.hmrc._",
    parallelExecution in Test := false,
    libraryDependencies ++= Dependencies.test,
    testOptions in Test := Seq(
      Tests.Argument(TestFrameworks.ScalaTest, "-u", "target/test-reports"),
      Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/test-reports/html-report"),
      Tests.Argument("-oD")
    ),
    resolvers ++= Seq(
      Resolver.bintrayRepo("hmrc", "releases"),
      Resolver.typesafeRepo("releases")
    )
  )
