resolvers += "HMRC-open-artefacts-maven" at "https://open.artefacts.tax.service.gov.uk/maven2"
resolvers += Resolver.url("HMRC-open-artefacts-ivy-local", url("https://open.artefacts.tax.service.gov.uk/ivy2"))(
  Resolver.ivyStylePatterns
)

addDependencyTreePlugin
addSbtPlugin("uk.gov.hmrc"   % "sbt-auto-build"  % "3.21.0")
addSbtPlugin("org.scalameta" % "sbt-scalafmt"    % "2.5.2")
addSbtPlugin("uk.gov.hmrc"   % "sbt-test-report" % "0.26.0")

logLevel := Level.Warn
