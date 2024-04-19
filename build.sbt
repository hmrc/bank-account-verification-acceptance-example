lazy val testSuite = (project in file("."))
  .enablePlugins(SbtAutoBuildPlugin)
  .disablePlugins(JUnitXmlReportPlugin) //Required to prevent https://github.com/scalatest/scalatest/issues/1427
  .settings(
    organization := "uk.gov.hmrc",
    name := "bank-account-verification-acceptance-example",
    version := "0.1.0",
    scalaVersion := "2.13.13",
    scalacOptions ++= Seq("-feature"),
    libraryDependencies ++= Dependencies.test,
    (Compile / compile) := ((Compile / compile) dependsOn (Compile / scalafmtSbtCheck, Compile / scalafmtCheckAll)).value
  )
