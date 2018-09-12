package services

import javax.inject.{Singleton, _}
import play.api.inject.ApplicationLifecycle
import utils.Configuration
import utils.instancemanagement.InstanceRegistry

import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
  * functions that will be run during request
  */
@Singleton
class StartUpService @Inject()(appLifecycle: ApplicationLifecycle){
  private val configuration = new Configuration()

  def storeIpToInstanceReg(): Unit ={
    InstanceRegistry.getWebApiVersion(configuration) match {
      case Success(_) => {
        if(configuration.usingInstanceRegistry)
          InstanceRegistry.sendWebApiMatchingResult(true, configuration)
      }
      case Failure(_) => {
        if(configuration.usingInstanceRegistry)
          InstanceRegistry.sendWebApiMatchingResult(false, configuration)
      }
    }
  }
  
  appLifecycle.addStopHook { () =>
    InstanceRegistry.deregister(configuration)
    Future.successful(())
  }

  storeIpToInstanceReg()
}