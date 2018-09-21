package app
import com.google.inject.AbstractModule
import services.StartUpService

/**
  * Run functions during request
  */
class EagerLoaderModule extends AbstractModule {
  override def configure() = {
    //startupservice will run during request
    bind(classOf[StartUpService]).asEagerSingleton
  }
}