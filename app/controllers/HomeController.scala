package controllers

import javax.inject._
import play.api.mvc._

import scala.concurrent.Future

/**
  * Created by benhermann on 02.01.18.
  */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
    * Create an Action to render an HTML page with a welcome message.
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  /**
    * Executes a given query and shows a result page
    * @param query query to execute
    * @return
    */
  def query(query : String) : Action[AnyContent] = Action.async {
        //TODO: execute and show result
    implicit request => Future.successful(Ok(views.html.index("Query")))
  }

}
