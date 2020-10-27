import sbt._

object Dependencies {

  val test = Seq(
    "uk.gov.hmrc"                %% "webdriver-factory"       % "0.13.0" % Test,
    "org.scalatest"              %% "scalatest"               % "3.0.7"  % Test,
      "org.pegdown"                %  "pegdown"                 % "1.2.1" % Test,
    "uk.gov.hmrc"                %% "zap-automation"          % "2.7.0" % Test,
    "com.typesafe"               %  "config"                  % "1.3.2" % Test
  )

}