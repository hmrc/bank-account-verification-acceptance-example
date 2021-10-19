/*
 * Copyright 2021 HM Revenue & Customs
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

case class DonePage() extends BasePage {

  private lazy val url: String = s"${TestConfiguration.url("bank-account-verification-frontend-example")}/done"

  def getUrl(journeyId: String): String = {
    s"$url/$journeyId"
  }

  override def isOnPage: Boolean = {
    webDriverWillWait.until(titleIs("bank-account-verification-example-frontend"))
  }

  private def getDataForSummaryListEntryCalled(entry: String): Option[Element] = {
    xpath(s"//dt[normalize-space()='$entry']/following-sibling::dd").findElement
  }

  def getAccountType: String = {
    getDataForSummaryListEntryCalled("Account type").get.text
  }

  def getAccountName: String = {
    getDataForSummaryListEntryCalled("Name on the account").get.text
  }

  def getSortCode: String = {
    getDataForSummaryListEntryCalled("Sort code").get.text
  }

  def getAccountNumber: String = {
    getDataForSummaryListEntryCalled("Account number").get.text
  }

  def getAddress: String = {
    getDataForSummaryListEntryCalled("Address").get.text
  }

  def getRollNumber: String = {
    getDataForSummaryListEntryCalled("Roll number").get.text
  }

  def getValidationResult: String = {
    getDataForSummaryListEntryCalled("Validation result").get.text
  }

  def getAccountExists: String = {
    getDataForSummaryListEntryCalled("Account exists").get.text
  }

  def getAccountNameMatched: String = {
    getDataForSummaryListEntryCalled("Account name matched").get.text
  }

  def getAccountAddressMatched: String = {
    getDataForSummaryListEntryCalled("Account address matched").get.text
  }

  def getAccountNonConsented: String = {
    getDataForSummaryListEntryCalled("Account non-consented").get.text
  }

  def getAccountOwnerDeceased: String = {
    getDataForSummaryListEntryCalled("Account owner deceased").get.text
  }

  def getCompanyName: String = {
    getDataForSummaryListEntryCalled("Company name").get.text
  }

  def getCompanyNameMatches: String = {
    getDataForSummaryListEntryCalled("Company name matches").get.text
  }

  def getCompanyPostcodeMatches: String = {
    getDataForSummaryListEntryCalled("Company postcode matches").get.text
  }

  def getBankName: String = {
    getDataForSummaryListEntryCalled("Bank name").get.text
  }

  def getDirectDebitSupported: String = {
    getDataForSummaryListEntryCalled("Direct debit supported").get.text
  }

  def getDirectCreditSupported: String = {
    getDataForSummaryListEntryCalled("Direct credit supported").get.text
  }

}
