package controllers

import javax.inject._

import dao.UserDao
import play.api.libs.json.JsValue
import play.api.mvc._
import play.twirl.api.Html
import services.{CurrentDropboxUser, DropboxService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class DropboxController @Inject()(dropbox: DropboxService, userDao: UserDao) extends Controller {

  def authRedirect = Action { request =>

    val userCode = request.queryString("code")
    userCode.foreach(dropbox.registerUserCodeForToken(_, request.host))

    Ok("Authorised" + request.queryString)
  }

  def listFiles = Action.async { x =>

    val futureHtmlSeq = for {
      users <- userDao.all()
    } yield users map { user =>
      for {
        jsonSeq <- dropbox.getFilesInFolder(user.token) map (_ \\ "path")
        stringSeq <- Future(jsonSeq.map(_.as[String]))
        htmlSeq <- Future(stringSeq.map("<li>" + _ + "</li>"))
    } yield htmlSeq
    }

    val everyFileTogether = futureHtmlSeq.map(Future.sequence(_)).flatMap(identity)

    val everyUserTogether = everyFileTogether.map(_.flatMap(identity))

    everyUserTogether.map { p =>
      val htmlPaths = p.map(_.mkString("<ul>", "", "</ul>"))
      Ok(views.html.Application.main("Your data files")(Html(htmlPaths.mkString)))
    }
  }

}
