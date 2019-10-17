package models

import play.api.libs.json.Json

case class ResultToJson(result:String)
object ResultToJson {
  implicit val writes = Json.writes[ResultToJson]
}
