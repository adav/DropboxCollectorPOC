package controllers

import javax.inject._

import play.api.libs.json.JsLookupResult
import play.api.mvc._
import play.twirl.api.Html
import services.{CurrentDropboxUser, DropboxService}

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class DropboxController @Inject()(dropbox: DropboxService) extends Controller {

  def authRedirect = Action { response =>

    val userCode = response.queryString("code")
    userCode.map(dropbox.registerUserCodeForToken(_))

    Ok("Authorised" + response.queryString)
  }

  def listFiles = Action.async {
    dropbox.getFilesInFolder(CurrentDropboxUser.currentUser.token, "/").map{ json =>
      val files = json \\ "path"

      val listOfFiles = files
        .map(_.as[String])
        .filter(_.contains("."))
        .map("<li>" + _ + "</li>")

      val html = Html(json.toString() + listOfFiles.mkString("<ul>", "\n", "</ul>"))

      Ok(views.html.Application.main("Your data files")(html))

    }

  }

}
