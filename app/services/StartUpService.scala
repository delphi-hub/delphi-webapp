package services

import javax.inject.{Singleton, _}
import play.api.inject.ApplicationLifecycle
import utils.Configuration
import utils.instancemanagement.InstanceRegistry

import scala.concurrent.Future

/**
  * functions that will be run during request
  */
@Singleton
class StartUpService @Inject()(appLifecycle: ApplicationLifecycle){
  private val configuration = new Configuration()

  def storeIpToInstanceReg(): Unit ={
    InstanceRegistry.sendWebApiMatchingResult(true, configuration)
  }
  
  appLifecycle.addStopHook { () =>
    InstanceRegistry.deregister(configuration)
    Future.successful(())
  }

  storeIpToInstanceReg()
}