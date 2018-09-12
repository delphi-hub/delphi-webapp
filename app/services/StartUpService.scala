package services

import java.util.concurrent.TimeUnit

import javax.inject.{Singleton, _}
import play.api.inject.ApplicationLifecycle
import utils.Configuration
import utils.instancemanagement.InstanceRegistry

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

/**
  * functions that will be run during request
  */
@Singleton
class StartUpService @Inject()(appLifecycle: ApplicationLifecycle){

  private val configuration = new Configuration()

  /**
    * Will register at the Instance Registry, get an matching WebApi instance and try to connect to it using the
    * /version endpoint. If successful, it will post the matching result true to the IR, otherwise false.
    */
  def doStartUpChecks(): Unit = {
    InstanceRegistry.getWebApiVersion(configuration) match {
      case Success(_) => {
        InstanceRegistry.sendWebApiMatchingResult(true, configuration)
      }
      case Failure(_) => {
        InstanceRegistry.sendWebApiMatchingResult(false, configuration)
        //Cannot connect to WebApi on startup, so stop execution
        Await.ready(appLifecycle.stop(), Duration(5, TimeUnit.SECONDS))
        System.exit(1)
      }
    }
  }
  
  appLifecycle.addStopHook { () =>
    InstanceRegistry.deregister(configuration)
    Future.successful(())
  }

  doStartUpChecks()
}