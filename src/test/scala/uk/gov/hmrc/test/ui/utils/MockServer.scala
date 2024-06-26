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

package uk.gov.hmrc.test.ui.utils

import org.mockserver.integration.ClientAndServer
import org.mockserver.model.{HttpRequest, HttpResponse}
import org.scalatest.concurrent.Eventually
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import uk.gov.hmrc.test.ui.conf.TestConfiguration

trait MockServer extends AnyFeatureSpec with BeforeAndAfterAll with BeforeAndAfterEach with Eventually {

  val mockServerPort: Int              = TestConfiguration.mockServerPort()
  lazy val mockServer: ClientAndServer = ClientAndServer.startClientAndServer(mockServerPort)

  override def beforeAll(): Unit =
    super.beforeAll()

  override def beforeEach(): Unit = {
    mockServer
      .when(
        HttpRequest
          .request()
          .withMethod("POST")
          .withPath("/write/audit")
      )
      .respond(
        HttpResponse
          .response()
          .withStatusCode(200)
      )
    mockServer
      .when(
        HttpRequest
          .request()
          .withMethod("POST")
          .withPath("/write/audit/merged")
      )
      .respond(
        HttpResponse
          .response()
          .withStatusCode(200)
      )
  }

  override def afterEach(): Unit =
    mockServer.reset()

  override def afterAll(): Unit = {
    mockServer.stop()
    super.afterAll()
  }
}
