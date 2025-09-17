import sbt._

object Dependencies {

  val scalatestVersion = "3.2.19"

  val test: Seq[ModuleID] = Seq(
    "org.scalatest"     %% "scalatest"                          % scalatestVersion,
    "org.scalatestplus" %% "selenium-4-21"                      % s"$scalatestVersion.0",
    "uk.gov.hmrc"       %% "ui-test-runner"                     % "0.49.0",
    "org.mock-server"    % "mockserver-netty"                   % "5.15.0",
    "org.assertj"        % "assertj-core"                       % "3.27.4",
    "uk.gov.hmrc"       %% "bank-account-verification-frontend" % "0.+"
  ).map(_ % Test)

}
