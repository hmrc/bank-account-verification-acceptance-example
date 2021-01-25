package uk.gov.hmrc.test.ui.pages

import org.openqa.selenium.support.ui.ExpectedConditions.titleIs
import uk.gov.hmrc.test.ui.conf.TestConfiguration

case class EntryPage() extends BasePage {
  val url: String = s"${TestConfiguration.url("bank-account-verification-frontend-example")}"

  private lazy val viewExampleButton = id("start")

  def viewExample(): Unit = {
    click on viewExampleButton
  }

  override def isOnPage: Boolean = {
    webDriverWillWait.until(titleIs("bank-account-verification-example-frontend"))
  }
}
