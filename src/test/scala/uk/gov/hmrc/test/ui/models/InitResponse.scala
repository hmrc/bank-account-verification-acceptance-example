package uk.gov.hmrc.test.ui.models

import java.util.UUID

import play.api.libs.json.{Json, OFormat}
import uk.gov.hmrc.test.ui.models.InitResponse.generatedJourneyID

object InitResponse {
  val generatedJourneyID: String = UUID.randomUUID().toString
  implicit val jsonFormat: OFormat[InitResponse] = Json.format[InitResponse]
}

case class InitResponse(journeyId: String = generatedJourneyID,
                        startUrl: String = s"/bank-account-verification/start/$generatedJourneyID",
                        completeUrl: String = s"/api/complete/$generatedJourneyID",
                        detailsUrl: Option[String] = None) {

  def asJsonString(): String = {
    Json.toJson(this).toString()
  }
}
