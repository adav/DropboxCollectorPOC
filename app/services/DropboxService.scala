package services


import javax.inject._

import akka.actor.ActorRef
import play.api.Logger
import play.api.libs.ws.{WSClient, WSResponse}

import scala.concurrent._
import ExecutionContext.Implicits.global

object DropboxService {
  val appKey = ""
  val appSecret = ""
}

@Singleton
class DropboxService @Inject()(ws: WSClient, @Named("new-user-actor") userActor: ActorRef) {

  var userToken = None

  def testDropboxApiConnection(): Future[WSResponse] = {
    ws.url("https://api.dropboxapi.com/").get()
  }

  Logger.info(s"DropboxService: Starting application with Dropbox credentials hardcoded.")
  testDropboxApiConnection map { response =>
    response.status match {
      case 200 => Logger.info("DropboxService: Successfully communicating with outside world")
      case _ => Logger.error("DropboxService: Error connecting to outside world. HTTP Code " + response.status)
    }
  }

  def registerUserCodeForToken(code: String) = {
    ws.url("https://api.dropboxapi.com/1/oauth2/token")
      .post(Map(
        "code" -> Seq(code),
        "grant_type" -> Seq("authorization_code"),
        "client_id" -> Seq(DropboxService.appKey),
        "client_secret" -> Seq(DropboxService.appSecret),
        "redirect_uri" -> Seq("http://localhost:9000/dropbox_redirect")
      )) onComplete {
      _ map { response =>

        val json = response.json
        val user = DropboxUser(
          (json \ "uid").as[String],
          (json \ "access_token").as[String]
        )

        userActor ! user

      }
    }

  }

  def getFilesInFolder = {
    ws.url("https://api.dropboxapi.com/1/oauth2/token")
      .post(Map(
        "code" -> Seq(code),
        "grant_type" -> Seq("authorization_code"),
        "client_id" -> Seq(DropboxService.appKey),
        "client_secret" -> Seq(DropboxService.appSecret),
        "redirect_uri" -> Seq("http://localhost:9000/dropbox_redirect")
      )) onComplete {
      _ map { response =>

        val json = response.json
        val user = DropboxUser(
          (json \ "uid").as[String],
          (json \ "access_token").as[String]
        )

        userActor ! user

      }
    }
  }
}