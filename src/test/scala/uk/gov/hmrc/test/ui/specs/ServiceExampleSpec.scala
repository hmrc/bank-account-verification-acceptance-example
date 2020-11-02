/*
 * Copyright 2020 HM Revenue & Customs
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

import org.assertj.core.api.Assertions.assertThat
import org.mockserver.model.{HttpRequest, HttpResponse}
import uk.gov.hmrc.test.ui.models.InitResponse
import uk.gov.hmrc.test.ui.pages.{DonePage, StartPage}
import uk.gov.hmrc.test.ui.utils.MockServer

class ServiceExampleSpec extends BaseSpec with MockServer {

  val DEFAULT_COMPANY_NAME = "P@cking & $orting"
  val DEFAULT_BANK_SORT_CODE = "40 47 84"
  val DEFAULT_BANK_ACCOUNT_NUMBER = "70872490"

  Scenario("Example Acceptance Test for services that use BAVFE") {
    val initResponse: InitResponse = InitResponse()
    val continueUrl = s"${DonePage().url}/${initResponse.journeyId}"
    val expectedBAVFEResponse =
      s"""{
         |    "accountType": "business",
         |    "business": {
         |        "address": {
         |            "lines": [
         |                "Line 1",
         |                "Line 2"
         |            ],
         |            "town": "Town",
         |            "postcode": "Postcode"
         |        },
         |        "companyName": "$DEFAULT_COMPANY_NAME",
         |        "sortCode": "$DEFAULT_BANK_SORT_CODE",
         |        "accountNumber": "$DEFAULT_BANK_ACCOUNT_NUMBER",
         |        "accountNumberWithSortCodeIsValid": "yes",
         |        "accountExists": "inapplicable",
         |        "companyNameMatches": "inapplicable",
         |        "companyPostCodeMatches": "inapplicable",
         |        "nonStandardAccountDetailsRequiredForBacs": "no"
         |    }
         |}""".stripMargin

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

    //Create a mock that will emulate the service passing over to BAVFE, and then handing back to the continue URL
    mockServer.when(
      HttpRequest.request()
        .withMethod("GET")
        .withPath(initResponse.startUrl)
    ).respond(
      HttpResponse.response()
        .withHeader("Location", continueUrl)
        .withStatusCode(303)
    )

    //Mock the request that collects data from BAVFE that the service will then use to continue its journey
    mockServer.when(
      HttpRequest.request()
        .withMethod("GET")
        .withPath(initResponse.completeUrl)
    ).respond(
      HttpResponse.response()
        .withHeader("Content-Type", "application/json")
        .withBody(expectedBAVFEResponse)
        .withStatusCode(200)
    )

    Given("I want to collect and validate a companies bank account details for my service")

    go to StartPage().url

    When("I invoke BAVFE by clicking on 'View an Example'")

    StartPage().clickStart()

    Then("the BAVFE journey is bypassed and I am returned to my service with an expected BAVFE response")

    assertThat(webDriver.getCurrentUrl).isEqualTo(continueUrl)
    assertThat(DonePage().getAccountType).isEqualTo("business")
    assertThat(DonePage().getCompanyName).isEqualTo(DEFAULT_COMPANY_NAME)
    assertThat(DonePage().getSortCode).isEqualTo(DEFAULT_BANK_SORT_CODE)
    assertThat(DonePage().getAccountNumber).isEqualTo(DEFAULT_BANK_ACCOUNT_NUMBER)
    assertThat(DonePage().getValidationResult).isEqualTo("yes")
    assertThat(DonePage().getAccountExists).isEqualTo("inapplicable")
    assertThat(DonePage().getCompanyNameMatches).isEqualTo("inapplicable")
    assertThat(DonePage().getCompanyPostcodeMatches).isEqualTo("inapplicable")
  }

}
