package controllers

import javax.inject._

import akka.actor.ActorRef
import dao.UserDao
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
    val accountWithChangesOptional = jsonOptional.map( _ \ "list_folder" \\ "accounts").map(_.map(_.as[String]))

    accountWithChangesOptional match {
      case Some(usersWithUpdates) => usersWithUpdates.foreach( updateActor ! _ )
    }

    Ok
  }

}
