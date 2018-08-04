package com.gkatzioura.eager
import com.google.inject.AbstractModule
import app.StartUpService

/**
  * Run functions during request
  */
class EagerLoaderModule extends AbstractModule {
  override def configure() = {
    //startupservice will run during request
    bind(classOf[StartUpService]).asEagerSingleton
  }
}