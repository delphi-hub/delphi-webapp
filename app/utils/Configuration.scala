package utils

import com.typesafe.config.ConfigFactory
import utils.instancemanagement.InstanceEnums.ComponentType
import utils.instancemanagement.{Instance, InstanceRegistry}

import scala.util.{Failure, Success}

class Configuration(val bindPort: Int = ConfigFactory.load().getInt("app.portWebapp")) {
  val instanceName = "WebAppInstance"
  val instanceRegistryUri : String = ConfigFactory.load().getString("instance.registry.path")

  lazy val webApiInstance : Instance = InstanceRegistry.retrieveWebApiInstance(this) match {
    case Success(instance) => instance
    case Failure(_) => Instance(None, Some(ConfigFactory.load().getString("webapi.path")), None, Some("Default Webapi instance"), Some(ComponentType.WebApi) )
  }

  lazy val usingInstanceRegistry : Boolean = assignedID match {
    case Some(_) => true
    case None => false
  }
  lazy val assignedID : Option[Long] = InstanceRegistry.register(this) match {
    case Success(id) => Some(id)
    case Failure(_) => None
  }

}
