package utils

import com.typesafe.config.ConfigFactory
import utils.instancemanagement.InstanceEnums.ComponentType
import utils.instancemanagement.{Instance, InstanceRegistry}

import scala.util.{Failure, Success, Try}

class Configuration(val bindPort: Int = ConfigFactory.load().getInt("app.portWebapp")) {

  val defaultWebApiPort : Int = ConfigFactory.load().getInt("webapi.port")
  val defaultWebApiHost : String = ConfigFactory.load().getString("webapi.host")
  val instanceName = "WebAppInstance"
  val instanceRegistryUri : String = sys.env.getOrElse("DELPHI_WEBAPI_URI", ConfigFactory.load().getString("instance.registry.path"))

  lazy val WebApiUri =  webApiInstance.host + ":" + webApiInstance.portNumber

  lazy val webApiInstance : Instance = InstanceRegistry.retrieveWebApiInstance(this) match {
    case Success(instance) => instance
    case Failure(_) => Instance(
      None,
      fallbackWebApiHost,
      fallbackWebApiPort,
      "Default WebApi instance",
      ComponentType.WebApi)

  }

  lazy val usingInstanceRegistry : Boolean = assignedID match {
    case Some(_) => true
    case None => false
  }
  lazy val assignedID : Option[Long] = InstanceRegistry.register(this) match {
    case Success(id) => Some(id)
    case Failure(_) => None
  }

  lazy val fallbackWebApiPort : Int = sys.env.get("DELPHI_WEBAPI_URI") match {
    case Some(hostString) => if(hostString.count(c => c == ':') == 2){
      Try(hostString.split(":")(2).toInt) match {
        case Success(port) => port
        case Failure(_) => defaultWebApiPort
      }
    } else {
      defaultWebApiPort
    }
    case None => defaultWebApiPort
  }

  lazy val fallbackWebApiHost : String = sys.env.get("DELPHI_WEBAPI_URI") match {
    case Some(hostString) =>
      if(hostString.count(c => c == ':') == 2){
        hostString.substring(0,hostString.lastIndexOf(":"))
      } else {
        defaultWebApiHost
      }
    case None => defaultWebApiHost

  }
}