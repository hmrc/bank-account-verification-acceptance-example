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

case class StartPage() extends BasePage {
  val url: String = s"${TestConfiguration.url("bank-account-verification-frontend-example")}/start"
  private lazy val petName: TextField = textField(id("petName"))
  private lazy val bunny: RadioButton = radioButton(id("bunny"))
  private lazy val cat: RadioButton = radioButton(id("cat"))
  private lazy val dog: RadioButton = radioButton(id("dog"))
  private lazy val other: RadioButton = radioButton(id("other"))
  private lazy val petAge: TextField = textField(id("age"))
  private lazy val continueButton: NameQuery = name("continue")

  override def isOnPage: Boolean = {
    webDriverWillWait.until(titleIs("bank-account-verification-example-frontend"))
  }

  def enterPetName(name: String): StartPage = {
    petName.value = name
    this
  }

  def enterPetAge(age: String): StartPage = {
    petAge.value = age
    this
  }

  def selectBunny(): StartPage = {
    bunny.underlying.click()
    this
  }

  def selectCat(): StartPage = {
    cat.underlying.click()
    this
  }

  def selectDog(): StartPage = {
    dog.underlying.click()
    this
  }

  def selectOther(): StartPage = {
    other.underlying.click()
    this
  }

  def clickContinue(): Unit = {
    click on continueButton
  }
}
