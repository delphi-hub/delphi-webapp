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
  val url = ConfigFactory.load().getString("instance-registry-path")
  getRequest(url)

  //get request with a url
  def getRequest(url: String) = {
    val result = BlockingHttpClient.doGet(url)
    result match {
      case Success(response) => println(response)
      case Failure(_) => println("Failed to connect with instance registry")
    }
  }
}

