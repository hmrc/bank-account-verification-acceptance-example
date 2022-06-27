import sbt._

object Dependencies {

  val test: Seq[ModuleID] = Seq(
    "org.scalatest"       %% "scalatest"                          % "3.2.12"   % Test,
    "org.scalatestplus"   %% "selenium-3-141"                     % "3.2.10.0" % Test,
    "com.vladsch.flexmark" % "flexmark-all"                       % "0.62.2"   % Test,
    "uk.gov.hmrc"         %% "webdriver-factory"                  % "0.+"      % Test,
    "com.squareup.okhttp3" % "okhttp"                             % "4.10.0"   % Test,
    "org.mock-server"      % "mockserver-netty"                   % "5.12.0"   % Test,
    "org.assertj"          % "assertj-core"                       % "3.23.1"   % Test,
    "uk.gov.hmrc"         %% "bank-account-verification-frontend" % "0.+"      % Test,
    "com.google.guava"     % "guava"                              % "31.1-jre" % Test
  )

}
