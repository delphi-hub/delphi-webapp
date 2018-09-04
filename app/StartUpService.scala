package app
import com.typesafe.config.ConfigFactory
import javax.inject.Singleton
import utils.BlockingHttpClient

import scala.util.{Failure, Success}

/**
  * functions that will be run during request
  */
@Singleton
class StartUpService{

  storeIpToInstanceRegistry()

  //store ip to registry db
  def storeIpToInstanceRegistry(): Unit =
  {
    val instanceRegistryUrl = ConfigFactory.load().getString("instance-registry-path")
    val result = BlockingHttpClient.doGet(instanceRegistryUrl)
    result match {
      case Success(response) => {
        val portNumber = ConfigFactory.load().getString("app.portWebapp")
        val postData = s"""{"id":"0","IP":"string","portnumber":"${portNumber}","name":"string", "ComponentType"-> "Webapp"}"""

        val urlIpStore = instanceRegistryUrl+"/register"
        val instanceSaveRes = BlockingHttpClient.doPost(urlIpStore, postData)
        result match {
          case Success(response) => println("Instance save successfully")
          case Failure(_) => println(instanceSaveRes)
        }
      }
      case Failure(_) => println("Failed to connect with instance registry")
    }
  }
}