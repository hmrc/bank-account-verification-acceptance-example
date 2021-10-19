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

package uk.gov.hmrc.test.ui.specs

import bankaccountverification.api.{BusinessCompleteResponse, CompleteResponse, CompleteResponseAddress}
import bankaccountverification.connector.ReputationResponseEnum._
import bankaccountverification.web.AccountTypeRequestEnum
import org.assertj.core.api.Assertions.assertThat
import org.mockserver.model.{HttpRequest, HttpResponse}
import play.api.libs.json.Json
import uk.gov.hmrc.test.ui.models.InitResponse
import uk.gov.hmrc.test.ui.pages._
import uk.gov.hmrc.test.ui.utils.MockServer

class ServiceExampleSpec extends BaseSpec with MockServer {

  val DEFAULT_COMPANY_NAME = "P@cking & $orting"
  val DEFAULT_BANK_SORT_CODE = "40 47 84"
  val DEFAULT_BANK_ACCOUNT_NUMBER = "70872490"

  Scenario("Example acceptance test for services that use BAVFE to check company account details") {
    val initResponse: InitResponse = InitResponse()
    val credID = "Some-Cred-ID"
    val journeyId = initResponse.journeyId

    //Mock the init call that is made to BAVFE
    mockServer.when(
      HttpRequest.request()
        .withMethod("POST")
        .withPath("/api/init")
    ).respond(
      HttpResponse.response()
        .withHeader("Content-Type", "application/json")
        .withStatusCode(200)
        .withBody(initResponse.asJsonString())
    )

    //Create a mock that will emulate the handover from your service -> BAVFE, and then BAVFE -> your service
    mockServer.when(
      HttpRequest.request()
        .withMethod("GET")
        .withPath(initResponse.startUrl)
    ).respond(
      HttpResponse.response()
        .withHeader("Location", ExtraInformationPage().getUrl(journeyId))
        .withStatusCode(303)
    )

    //Create your expected BAVFE response using the models defined in BAVFE
    val expectedBAVFEResponse = Json.toJson(
      CompleteResponse(
        AccountTypeRequestEnum.Business,
        business = Some(BusinessCompleteResponse(
          address = Some(CompleteResponseAddress(
            lines = List("Line 1", "Line 2"),
            town = Some("Town"),
            postcode = Some("Postcode"),
          )),
          companyName = DEFAULT_COMPANY_NAME,
          sortCode = DEFAULT_BANK_SORT_CODE,
          accountNumber = DEFAULT_BANK_ACCOUNT_NUMBER,
          accountNumberWithSortCodeIsValid = Yes,
          accountExists = Some(Yes),
          companyNameMatches = Some(Yes),
          companyPostCodeMatches = Some(Inapplicable),
          companyRegistrationNumberMatches = Some(Inapplicable),
          nonStandardAccountDetailsRequiredForBacs = Some(No),
          sortCodeBankName = Some("Lloyds"),
          sortCodeSupportsDirectDebit = Some(Yes),
          sortCodeSupportsDirectCredit = Some(Yes)
        )),
        personal = None
      )
    )

    //Mock the request that collects the data that the user has entered in BAVFE using the above expected response
    mockServer.when(
      HttpRequest.request()
        .withMethod("GET")
        .withPath(initResponse.completeUrl)
    ).respond(
      HttpResponse.response()
        .withHeader("Content-Type", "application/json")
        .withBody(expectedBAVFEResponse.toString())
        .withStatusCode(200)
    )

    Given("I want to collect and validate some company bank account details for my service")

    go to EntryPage().url
    EntryPage().viewExample()
    AuthStubPage()
      .enterCredID(credID)
      .enterRedirectUrl(StartPage().url)
      .submit()

    When("I invoke BAVFE by clicking on 'View an Example'")

    StartPage().enterPetName("Bugs")
      .selectBunny()
      .enterPetAge("4")
      .clickContinue()

    Then("the BAVFE journey is bypassed and I am returned to my service with an expected BAVFE response")

    assertThat(webDriver.getCurrentUrl).isEqualTo(ExtraInformationPage().getUrl(journeyId))

    ExtraInformationPage()
      .clickContinue()

    CheckYourAnswersPage()
      .clickSubmit()

    assertThat(webDriver.getCurrentUrl).isEqualTo(DonePage().getUrl(journeyId))
    assertThat(DonePage().getAccountType).isEqualTo("business")
    assertThat(DonePage().getCompanyName).isEqualTo(DEFAULT_COMPANY_NAME)
    assertThat(DonePage().getSortCode).isEqualTo(DEFAULT_BANK_SORT_CODE)
    assertThat(DonePage().getAccountNumber).isEqualTo(DEFAULT_BANK_ACCOUNT_NUMBER)
    assertThat(DonePage().getValidationResult).isEqualTo("yes")
    assertThat(DonePage().getAccountExists).isEqualTo("yes")
    assertThat(DonePage().getCompanyNameMatches).isEqualTo("yes")
    assertThat(DonePage().getCompanyPostcodeMatches).isEqualTo("inapplicable")
    assertThat(DonePage().getBankName).isEqualTo("Lloyds")
    assertThat(DonePage().getDirectDebitSupported).isEqualTo("yes")
    assertThat(DonePage().getDirectCreditSupported).isEqualTo("yes")
  }

}
