// Copyright (C) 2018 The Delphi Team.
// See the LICENCE file distributed with this work for additional
// information regarding copyright ownership.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

// http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package utils.instancemanagement

import java.net.InetAddress

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import akka.util.ByteString
import utils.instancemanagement.InstanceEnums.{ComponentType, InstanceState}
import utils.{AppLogging, Configuration}

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}
import spray.json._

object InstanceRegistry extends JsonSupport with AppLogging
{

  implicit val system: ActorSystem = ActorSystem("delphi-webapp")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val ec  : ExecutionContext = system.dispatcher

  lazy val instanceIdFromEnv : Option[Long] = Try[Long](sys.env("INSTANCE_ID").toLong).toOption

  def handleInstanceStart(configuration: Configuration) : Option[Long] = {
    instanceIdFromEnv match {
      case Some(id) =>
        reportStart(configuration) match {
          case Success(_) => Some(id)
          case Failure(_) => None
        }
      case None =>
        register(configuration) match {
          case Success(id) => Some(id)
          case Failure(_) => None
        }
    }
  }

  def handleInstanceStop(configuration: Configuration): Try[Any] = {
    if(instanceIdFromEnv.isDefined) {
      reportStop(configuration)
    } else {
      deregister(configuration)
    }
  }

  def handleInstanceFailure(configuration: Configuration): Any = {
    if(instanceIdFromEnv.isDefined) {
      reportFailure(configuration)
    }
  }

  def reportStart(configuration: Configuration) : Try[Unit] = executeReportOperation(configuration, ReportOperationType.Start)

  def reportStop(configuration: Configuration) : Try[Unit] = {
    if(configuration.usingInstanceRegistry) {
      executeReportOperation(configuration, ReportOperationType.Stop)
    } else {
      Failure(new RuntimeException("Cannot report stop, no instance registry available."))
    }
  }

  def reportFailure(configuration: Configuration) : Try[Unit] = {
    if(configuration.usingInstanceRegistry){
      executeReportOperation(configuration, ReportOperationType.Failure)
    } else {
      Failure(new RuntimeException("Cannot report failure, no instance registry available."))
    }
  }

  private def executeReportOperation(configuration: Configuration, operationType: ReportOperationType.Value) : Try[Unit] = {
    instanceIdFromEnv match {
      case Some(id) =>
        val request = HttpRequest(
          method = HttpMethods.POST,
          configuration.instanceRegistryUri + ReportOperationType.toOperationUriString(operationType, id))

        Await.result(Http(system).singleRequest(request) map {response =>
          if(response.status == StatusCodes.OK){
            log.info(s"Successfully reported ${operationType.toString} to Instance Registry.")
            Success()
          }
          else {
            log.warning(s"Failed to report ${operationType.toString} to Instance Registry, server returned ${response.status}")
            Failure(new RuntimeException(s"Failed to report ${operationType.toString} to Instance Registry, server returned ${response.status}"))
          }

        } recover {case ex =>
          log.warning(s"Failed to report ${operationType.toString} to Instance Registry, exception: $ex")
          Failure(new RuntimeException(s"Failed to report ${operationType.toString} to Instance Registry, exception: $ex"))
        }, Duration.Inf)
      case None =>
        log.warning(s"Cannot report ${operationType.toString} to Instance Registry, no instance id is present in env var 'INSTANCE_ID'.")
        Failure(new RuntimeException(s"Cannot report ${operationType.toString} to Instance Registry, no instance id is present in env var 'INSTANCE_ID'."))
    }
  }

  def register(configuration: Configuration) : Try[Long] = {
    val instance = createInstance(None,configuration.bindPort, configuration.instanceName, None, InstanceState.Running)

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
        response.status match {
          case StatusCodes.OK =>
            try {
              val instanceString : String = Await.result(response.entity.dataBytes.runFold(ByteString(""))(_ ++ _).map(_.utf8String), 5 seconds)
              val esInstance = instanceString.parseJson.convertTo[Instance](instanceFormat)
              val webApiIP = esInstance.host
              log.info(s"Instance Registry assigned WebApi instance at $webApiIP")
              Success(esInstance)
            } catch {
              case px: spray.json.JsonParser.ParsingException =>
                log.warning(s"Failed to read response from Instance Registry, exception: $px")
                Failure(px)
            }
          case StatusCodes.NotFound =>
            log.warning(s"No matching instance of type 'WebApi' is present at the instance registry.")
            Failure(new RuntimeException(s"Instance Registry did not contain matching instance, server returned ${StatusCodes.NotFound}"))
          case _ =>
            val status = response.status
            log.warning(s"Failed to read matching instance from Instance Registry, server returned $status")
            Failure(new RuntimeException(s"Failed to read matching instance from Instance Registry, server returned $status"))
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
      if(configuration.webApiInstance.id.isEmpty) {
        Failure(new RuntimeException("The WebApi instance was not assigned by the Instance Registry, so no matching result will be posted."))
      } else {
        val idToPost = configuration.webApiInstance.id.getOrElse(-1L)
        val request = HttpRequest(
          method = HttpMethods.POST,
          configuration.instanceRegistryUri + s"/matchingResult?Id=$idToPost&MatchingSuccessful=$isWebApiReachable")

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

  def getWebApiVersion(configuration: Configuration) : Try[ResponseEntity]  = {
    val request = HttpRequest(method = HttpMethods.GET, configuration.webApiUri + "/version")

    Await.result(Http(system).singleRequest(request) map {response =>
      if(response.status == StatusCodes.OK){
        Success(response.entity)
      }
      else {
        val statuscode = response.status
        log.warning(s"Failed to get version of WebApi, server returned $statuscode")
        Failure(new RuntimeException(s"Failed to get version of WebApi, server returned $statuscode"))
      }

    } recover {case ex =>
      log.warning(s"Failed to get version of WebApi, server returned, exception: $ex")
      Failure(new RuntimeException(s"Failed to get version of WebApi, server returned, exception: $ex"))
    }, Duration.Inf)

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

  def postInstance(instance : Instance, uri: String) () : Future[HttpResponse] = {
    try {
      val request = HttpRequest(method = HttpMethods.POST, uri = uri, entity = instance.toJson(instanceFormat).toString())
      Http(system).singleRequest(request)
    } catch {
      case dx: DeserializationException =>
        log.warning(s"Failed to deregister to Instance Registry, exception: $dx")
        Future.failed(dx)
    }
  }


  private def createInstance(id: Option[Long], controlPort : Int, name : String, dockerId : Option[String], instanceState: InstanceEnums.State) : Instance =
    Instance(id, InetAddress.getLocalHost.getHostAddress, controlPort, name, ComponentType.WebApp, dockerId, instanceState)


  object ReportOperationType extends Enumeration {
    val Start : Value = Value("Start")
    val Stop : Value = Value("Stop")
    val Failure : Value = Value("Failure")

    def toOperationUriString(operation: ReportOperationType.Value, id: Long) : String = {
      operation match {
        case Start =>
          s"/reportStart?Id=$id"
        case Stop =>
          s"/reportStop?Id=$id"
        case _ =>
          s"/reportFailure?Id=$id"
      }
    }
  }
}