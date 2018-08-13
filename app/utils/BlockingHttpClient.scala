package utils


import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpEntity, MediaTypes, HttpMethods, HttpRequest, HttpResponse, Uri}
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import akka.util.ByteString

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success, Try}

/***
  * A blocking http client implemented using Akka HTTP
  */
object BlockingHttpClient {

  def doGet(uri : Uri) : Try[String] = {
    implicit val system = ActorSystem()
    implicit val executionContext = system.dispatcher
    implicit val materializer: ActorMaterializer = ActorMaterializer(ActorMaterializerSettings(system))

    try {
      val req: Future[HttpResponse] = Http(system).singleRequest(HttpRequest(method = HttpMethods.GET, uri = uri))
      Await.result(req, Duration.Inf)

      val f = req.value.get.get.entity.dataBytes.runFold(ByteString(""))(_ ++ _)
      Await.result(f, Duration.Inf)

      Success(f.value.get.get.utf8String)
    } catch  {
      case e : Exception => Failure(e)
    } finally {
      system.terminate()
      Await.result(system.whenTerminated, Duration.Inf)
    }

  }

  // data parameter will be """{"name":"Hello"}"""
  def doPost(uri: Uri, data: String) = {
    implicit val system = ActorSystem()
    implicit val executionContext = system.dispatcher
    implicit val materializer: ActorMaterializer = ActorMaterializer(ActorMaterializerSettings(system))
    val bdata = ByteString(data)
    try {
      val req: Future[HttpResponse] = Http(system).singleRequest(HttpRequest(
        method = HttpMethods.POST,
        uri = uri,
        entity = bdata
      ))
      Await.result(req, Duration.Inf)

      val f = req.value.get.get.entity.dataBytes.runFold(ByteString(""))(_ ++ _)
      Await.result(f, Duration.Inf)

      Success(f.value.get.get.utf8String)
    } catch  {
      case e : Exception => Failure(e)
    } finally {
      system.terminate()
      Await.result(system.whenTerminated, Duration.Inf)
    }
  }

  def executeGet(target: String, server: String) : Try[String] = {

    val uri = Uri(server)
    doGet(uri.withPath(uri.path + target))

  }

  def executePost(target: String, server: String, data: String) : Try[String] = {

    val uri = Uri(server)
    doPost(uri.withPath(uri.path + target), data)

  }

}