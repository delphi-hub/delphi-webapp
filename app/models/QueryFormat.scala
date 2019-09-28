package models

import play.api.libs.json.Json

case class QueryFormat(query:String)
object QueryFormat {
  implicit val writes = Json.writes[QueryFormat]
}