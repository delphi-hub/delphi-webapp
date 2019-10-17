package controllers

import akka.http.scaladsl.model.HttpMethods
import javax.inject.Inject
import models.ResultToJson
import play.api.libs.json.Json._
import play.api.mvc.InjectedController
import utils.{BlockingHttpClient, CommonHelper}

import scala.concurrent.Future
import scala.util.{Failure, Success}

class HelloWorldJSONController @Inject() extends InjectedController {

  def hello(query: String) = Action {

    val abc= "[using KeyStore]=10"
    println(abc)
    val query=toJson(ResultToJson(abc)).toString()
    println(query)

    Ok(toJson(ResultToJson(abc)))





  }

}
