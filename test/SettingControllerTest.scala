import org.scalatestplus.play._
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.libs.json.Json
import play.api.mvc._
import play.api.test.Helpers._
import play.api.test.FakeRequest

class SettingControllerTest extends PlaySpec with Results with GuiceOneAppPerSuite{

  "SettingController version" should {
    "Show success for get request" in {
      val request = FakeRequest(GET, "/version")
      //val request = FakeRequest(GET, "/").withHeaders(HOST -> "localhost:9000").withCSRFToken
      //val home = route(app, request).get
      val Some(result) = route(app, request)
      status(result) mustEqual 200
    }
    "error for post request" in {
      val request = FakeRequest(POST, "/version").withJsonBody(Json.parse("""{ "field": "value" }"""))
      val Some(result) = route(app, request)
      status(result) mustEqual 404
    }
  }

  "SettingController stop" should {
    "show success for get request" in {
      val request = FakeRequest(GET, "/stop")
      val Some(result) = route(app, request)
      status(result) mustEqual 200
    }
    "give message during shutdown" in {
      val request = FakeRequest(GET, "/stop")
      val result = route(app, request).get
      contentAsString(result) mustEqual "Webapp is shutting down"
    }
    "show page not found error for post request" in {
      val request = FakeRequest(POST, "/stop").withJsonBody(Json.parse("""{ "field": "value" }"""))
      val Some(result) = route(app, request)
      status(result) mustEqual 404
    }
  }

}
