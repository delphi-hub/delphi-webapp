package controllers

import javax.inject._
import play.api.Configuration
import play.api.mvc._
import utils.BlockingHttpClient

import scala.concurrent.Future
import scala.util.{Failure, Success}


/**
  * Created by benhermann on 02.01.18.
  */
@Singleton
class HomeController @Inject()(configuration: Configuration, cc: ControllerComponents) extends AbstractController(cc) {

  /**
    * Create an Action to render an HTML page with a welcome message.
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index = Action {
    Ok(views.html.index("", "", false))
  }

  /**
    * Executes a given query and shows a result page
    * @param query query to execute
    * @return
    */
  def query(query : String) : Action[AnyContent] = Action.async {
    implicit request => {
      val server = configuration.underlying.getString("webapi.path")
      val getRequest = BlockingHttpClient.executeGet("search/"+query, server)
      getRequest match {
        case Success(response) => Future.successful(Ok(views.html.index(response, query, false)))
        case Failure(_) => Future.successful(Ok(views.html.index("ERROR: Failed to reach server at "+server, query, true)))
      }
    }
  }

}
