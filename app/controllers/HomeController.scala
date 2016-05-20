package controllers

import javax.inject._

import play.api._
import play.api.mvc._
import play.twirl.api.Html
import views.html

@Singleton
class HomeController @Inject() extends Controller {

  def index = Action {
    Ok(views.html.Application.dropboxregister("Your data"))
  }

}
