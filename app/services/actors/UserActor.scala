package services.actors

import akka.actor.Actor
import play.api.Logger
import services.{CurrentDropboxUser, DropboxUser}

class NewUserActor extends Actor {

  def receive = {
    case newUser: DropboxUser => CurrentDropboxUser.update(newUser)
    case _ => Logger.error("NewUserActor received unknown message")
  }

}
