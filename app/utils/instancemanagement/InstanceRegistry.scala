package utils.instancemanagement

import java.net.InetAddress

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import utils.instancemanagement.InstanceEnums.ComponentType
import utils.{AppLogging, Configuration}

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success, Try}

object InstanceRegistry extends JsonSupport with AppLogging
{

  implicit val system: ActorSystem = ActorSystem("delphi-webapp")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val ec  : ExecutionContext = system.dispatcher

  def register(configuration: Configuration) : Try[Long] = {
    val instance = createInstance(None,configuration.bindPort, configuration.instanceName)

    Await.result(postInstance(instance, configuration.instanceRegistryUri + "/register") map {response =>
      if(response.status == StatusCodes.OK){
        Await.result(Unmarshal(response.entity).to[String] map { assignedID =>
          val id = assignedID.toLong
          log.info(s"Successfully registered at Instance Registry, got ID $id.")
          Success(id)
        } recover { case ex =>
          log.warning(s"Failed to read assigned ID from Instance Registry, exception: $ex")
          Failure(ex)
        }, Duration.Inf)
      }
      else {
        val statuscode = response.status
        log.warning(s"Failed to register at Instance Registry, server returned $statuscode")
        Failure(new RuntimeException(s"Failed to register at Instance Registry, server returned $statuscode"))
      }

    } recover {case ex =>
      log.warning(s"Failed to register at Instance Registry, exception: $ex")
      Failure(ex)
    }, Duration.Inf)
  }

  def retrieveWebApiInstance(configuration: Configuration) : Try[Instance] = {
    if(!configuration.usingInstanceRegistry) {
      Failure(new RuntimeException("Cannot get WebApi instance from Instance Registry, no Instance Registry available."))
    } else {
      val request = HttpRequest(method = HttpMethods.GET, configuration.instanceRegistryUri + "/matchingInstance?ComponentType=WebApi")

      Await.result(Http(system).singleRequest(request) map {response =>
        val status = response.status
        if(status == StatusCodes.OK) {

          Await.result(Unmarshal(response.entity).to[Instance] map {instance =>
            val webApiIP = instance.host
            log.info(s"Instance Registry assigned WebApi instance at $webApiIP")
            Success(instance)
          } recover {case ex =>
            log.warning(s"Failed to read response from Instance Registry, exception: $ex")
            Failure(ex)
          }, Duration.Inf)
        }
        else{
          log.warning(s"Failed to read response from Instance Registry, server returned $status")
          Failure(new RuntimeException(s"Failed to read response from Instance Registry, server returned $status"))
        }
      } recover { case ex =>
        log.warning(s"Failed to request WebApi instance from Instance Registry, exception: $ex ")
        Failure(ex)
      }, Duration.Inf)
    }
  }

  def sendWebApiMatchingResult(isWebApiReachable : Boolean, configuration: Configuration) : Try[Unit] = {
    if(!configuration.usingInstanceRegistry) {
      Failure(new RuntimeException("Cannot post matching result to Instance Registry, no Instance Registry available."))
    } else {
      if(configuration.webApiInstance.iD.isEmpty) {
        Failure(new RuntimeException("Cannot post matching result to Instance Registry, assigned WebAPI instance has no ID."))
      } else {
        val IdToPost = configuration.webApiInstance.iD.getOrElse(-1L)
        val request = HttpRequest(
          method = HttpMethods.POST,
          configuration.instanceRegistryUri + s"/matchingResult?Id=$IdToPost&MatchingSuccessful=$isWebApiReachable")

        Await.result(Http(system).singleRequest(request) map {response =>
          if(response.status == StatusCodes.OK){
            log.info("Successfully posted matching result to Instance Registry.")
            Success()
          }
          else {
            val statuscode = response.status
            log.warning(s"Failed to post matching result to Instance Registry, server returned $statuscode")
            Failure(new RuntimeException(s"Failed to post matching result to Instance Registry, server returned $statuscode"))
          }

        } recover {case ex =>
          log.warning(s"Failed to post matching result to Instance Registry, exception: $ex")
          Failure(new RuntimeException(s"Failed to post matching result tot Instance Registry, exception: $ex"))
        }, Duration.Inf)
      }
    }

  }

  def deregister(configuration: Configuration) : Try[Unit] = {
    if(!configuration.usingInstanceRegistry){
      Failure(new RuntimeException("Cannot deregister from Instance Registry, no Instance Registry available."))
    } else {
      val id : Long = configuration.assignedID.getOrElse(-1L)

      val request = HttpRequest(method = HttpMethods.POST, configuration.instanceRegistryUri + s"/deregister?Id=$id")

      Await.result(Http(system).singleRequest(request) map {response =>
        if(response.status == StatusCodes.OK){
          log.info("Successfully deregistered from Instance Registry.")
          Success()
        }
        else {
          val statuscode = response.status
          log.warning(s"Failed to deregister from Instance Registry, server returned $statuscode")
          Failure(new RuntimeException(s"Failed to deregister from Instance Registry, server returned $statuscode"))
        }

      } recover {case ex =>
        log.warning(s"Failed to deregister to Instance Registry, exception: $ex")
        Failure(ex)
      }, Duration.Inf)
    }
  }

  def postInstance(instance : Instance, uri: String) () : Future[HttpResponse] =
    Marshal(instance).to[RequestEntity] flatMap { entity =>
      val request = HttpRequest(method = HttpMethods.POST, uri = uri, entity = entity)
      Http(system).singleRequest(request)
    }


  private def createInstance(id: Option[Long], controlPort : Int, name : String) : Instance =
    Instance(id, InetAddress.getLocalHost.getHostAddress, controlPort, name, ComponentType.WebApp)
}