package controllers

import javax.inject._

import play.api.mvc._
import play.twirl.api.Html
import services.DropboxService

@Singleton
class DropboxController @Inject()(dropbox: DropboxService) extends Controller {

  def authRedirect = Action { response =>

    val userCode = response.queryString("code")
    userCode.map(dropbox.registerUserCodeForToken(_))

    Ok("Authorised" + response.queryString)
  }

  def listFiles = Action {
    val html = Html("hi")

    Ok(views.html.Application.main("Your data files")(html))
  }

}
