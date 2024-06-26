/*
 * Copyright 2024 HM Revenue & Customs
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

package uk.gov.hmrc.test.ui.specs

import bankaccountverification.api.{BusinessCompleteV3Response, CompleteV3Response}
import bankaccountverification.connector.ReputationResponseEnum._
import bankaccountverification.web.AccountTypeRequestEnum
import org.mockserver.model.{HttpRequest, HttpResponse}
import play.api.libs.json.Json
import uk.gov.hmrc.test.ui.models.InitResponse
import uk.gov.hmrc.test.ui.pages._
import uk.gov.hmrc.test.ui.utils.MockServer

class ServiceExampleSpec extends BaseSpec with MockServer {

  val DEFAULT_COMPANY_NAME        = "P@cking & $orting"
  val DEFAULT_BANK_SORT_CODE      = "40 47 84"
  val DEFAULT_BANK_ACCOUNT_NUMBER = "70872490"

  Scenario("Example acceptance test for services that use BAVFE to check company account details") {
    val initResponse: InitResponse = InitResponse()
    val credID                     = "Some-Cred-ID"
    val journeyId                  = initResponse.journeyId

    //Mock the init call that is made to BAVFE
    mockServer
      .when(
        HttpRequest
          .request()
          .withMethod("POST")
          .withPath("/api/v3/init")
      )
      .respond(
        HttpResponse
          .response()
          .withHeader("Content-Type", "application/json")
          .withStatusCode(200)
          .withBody(initResponse.asJsonString())
      )

    //Create a mock that will emulate the handover from your service -> BAVFE, and then BAVFE -> your service
    mockServer
      .when(
        HttpRequest
          .request()
          .withMethod("GET")
          .withPath(initResponse.startUrl)
      )
      .respond(
        HttpResponse
          .response()
          .withHeader("Location", ExtraInformationPage().getUrl(journeyId))
          .withStatusCode(303)
      )

    //Create your expected BAVFE response using the models defined in BAVFE
    val expectedBAVFEResponse = Json.toJson(
      CompleteV3Response(
        AccountTypeRequestEnum.Business,
        business = Some(
          BusinessCompleteV3Response(
            companyName = DEFAULT_COMPANY_NAME,
            sortCode = DEFAULT_BANK_SORT_CODE,
            accountNumber = DEFAULT_BANK_ACCOUNT_NUMBER,
            accountNumberIsWellFormatted = Yes,
            accountExists = Some(Yes),
            nameMatches = Some(Yes),
            nonStandardAccountDetailsRequiredForBacs = Some(No),
            sortCodeBankName = Some("Lloyds"),
            sortCodeSupportsDirectDebit = Some(Yes),
            sortCodeSupportsDirectCredit = Some(Yes)
          )
        ),
        personal = None
      )
    )

    //Mock the request that collects the data that the user has entered in BAVFE using the above expected response
    mockServer
      .when(
        HttpRequest
          .request()
          .withMethod("GET")
          .withPath(initResponse.completeUrl)
      )
      .respond(
        HttpResponse
          .response()
          .withHeader("Content-Type", "application/json")
          .withBody(expectedBAVFEResponse.toString())
          .withStatusCode(200)
      )

    Given("I want to collect and validate some company bank account details for my service")

    go to EntryPage()
    EntryPage().viewExample()
    AuthStubPage()
      .enterCredID(credID)
      .enterRedirectUrl(StartPage().url)
      .submit()

    When("I invoke BAVFE by clicking on 'View an Example'")

    StartPage()
      .enterPetName("Bugs")
      .selectBunny()
      .enterPetAge("4")
      .clickContinue()

    Then("the BAVFE journey is bypassed and I am returned to my service with an expected BAVFE response")

    driver.getCurrentUrl shouldBe ExtraInformationPage().getUrl(journeyId)

    ExtraInformationPage()
      .clickContinue()

    CheckYourAnswersPage()
      .clickSubmit()

    driver.getCurrentUrl                shouldBe DonePage().getUrl(journeyId)
    DonePage().getAccountType           shouldBe "business"
    DonePage().getCompanyName           shouldBe DEFAULT_COMPANY_NAME
    DonePage().getSortCode              shouldBe DEFAULT_BANK_SORT_CODE
    DonePage().getAccountNumber         shouldBe DEFAULT_BANK_ACCOUNT_NUMBER
    DonePage().getValidationResult      shouldBe "yes"
    DonePage().getAccountExists         shouldBe "yes"
    DonePage().getCompanyNameMatches    shouldBe "yes"
    DonePage().getBankName              shouldBe "Lloyds"
    DonePage().getDirectDebitSupported  shouldBe "yes"
    DonePage().getDirectCreditSupported shouldBe "yes"
  }

}
