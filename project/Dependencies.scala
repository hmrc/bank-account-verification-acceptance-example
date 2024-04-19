import sbt._

object Dependencies {

  val test: Seq[ModuleID] = Seq(
    "com.typesafe"         % "config"                             % "1.4.3"    % Test,
    "ch.qos.logback"       % "logback-classic"                    % "1.5.6"    % Test,
    "com.vladsch.flexmark" % "flexmark-all"                       % "0.64.8"   % Test,
    "org.scalatest"       %% "scalatest"                          % "3.2.18"   % Test,
    "org.scalatestplus"   %% "selenium-4-17"                      % "3.2.18.0" % Test,
    "uk.gov.hmrc"         %% "ui-test-runner"                     % "0.28.0"   % Test,
//    "com.squareup.okhttp3" % "okhttp"                             % "4.10.0"   % Test,
    "org.mock-server"      % "mockserver-netty"                   % "5.12.0"   % Test,
    "org.assertj"          % "assertj-core"                       % "3.23.1"   % Test,
    "uk.gov.hmrc"         %% "bank-account-verification-frontend" % "0.+"      % Test
//    "com.google.guava"     % "guava"                              % "31.1-jre" % Test
  )

}
