package controllers

import javax.inject._

import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.Configuration
import play.api.http.HttpErrorHandler
import play.api.mvc._

/**
  * Frontend controller managing all static resource associate routes.
  * @param assets Assets controller reference.
  * @param cc Controller components reference.
  */
@Singleton
class FrontController @Inject()(assets: Assets, errorHandler: HttpErrorHandler, config: Configuration, cc: ControllerComponents)
  extends AbstractController(cc) with I18nSupport {

  def startPoint: Action[AnyContent] = assets.at("index.html")

  def frontEndFile(resource: String): Action[AnyContent] =
    if (resource.contains(".")) assets.at(resource) else startPoint

}