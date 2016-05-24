package controllers

import javax.inject._

import akka.actor.ActorRef
import dao.UserDao
import play.api.Logger
import play.api.mvc._
import play.twirl.api.Html
import services.DropboxService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class WebhookController @Inject()(@Named("user-files-update-actor") updateActor: ActorRef) extends Controller {

  def webhookVerificationRequest = Action { request =>
    request.queryString.get("challenge") match {
      case Some(result) => Ok(result.mkString)
    }
  }

  def processWebhook = Action { request =>
    val jsonOptional = request.body.asJson
    Logger.info(jsonOptional.get.toString())

    val accountWithChangesOptional = jsonOptional.map( j => (j \ "list_folder" \ "accounts").as[List[String]])

    accountWithChangesOptional match {
      case Some(usersWithUpdates) => usersWithUpdates.foreach( updateActor ! _ )
      case _ => Logger.error("Optional fail "+ accountWithChangesOptional.get.mkString)
    }

    Ok
  }

}
