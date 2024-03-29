/*
 * Copyright 2022 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.test.ui.pages

import org.openqa.selenium.support.ui.ExpectedConditions.titleIs
import uk.gov.hmrc.test.ui.conf.TestConfiguration

case class EntryPage() extends BasePage {
  val url: String = s"${TestConfiguration.url("bank-account-verification-frontend-example")}"

  private lazy val viewExampleButton = id("start")

  def viewExample(): Unit =
    click on viewExampleButton

  override def isOnPage: Boolean =
    webDriverWillWait.until(titleIs("bank-account-verification-example-frontend"))
}
