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

  checkInstanceRegistry()
  storeIpToInstanceRegistry()

  //check instance registry exist
  def checkInstanceRegistry() = {
    val url = ConfigFactory.load().getString("instance-registry-path")
    val result = BlockingHttpClient.doGet(url)
    result match {
      case Success(response) => println(response)
      case Failure(_) => println("Failed to connect with instance registry")
    }
  }

  //store ip to registry db
  def storeIpToInstanceRegistry(): Unit =
  {
    val instanceRegistryUrl = ConfigFactory.load().getString("instance-registry-path")
    val result = BlockingHttpClient.doGet(instanceRegistryUrl)
    result match {
      case Success(response) => {
        val postData = s"""{"ip":"$instanceRegistryUrl"}"""
        val urlIpStore = "http://0.0.0.1"
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

