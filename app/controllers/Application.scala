import javax.inject.{Inject, Singleton}

import scala.concurrent.Future
import scala.concurrent.duration._
import play.api.mvc._
import play.api.libs.ws._
import play.api.http.HttpEntity
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._
import akka.util.ByteString

import scala.concurrent.ExecutionContext
//@Singleton
//class Application @Inject()(ws: WSClient, val controllerComponents: ControllerComponents) extends BaseController {

//def abc(){

  //val request: WSRequest = ws.url("https://delphi.cs.uni-paderborn.de/api/search")

  //val complexResult: WSRequest = request.addHttpHeaders("Accept" -> "application/json")
    //.addQueryStringParameters("query" -> "[using KeyStore=10]")
    //.withRequestTimeout(10000.millis)

  //val futureResponse : Future[WSResponse] = complexResult.get()

  //println(futureResponse)
//}
//}