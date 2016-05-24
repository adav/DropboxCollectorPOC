package services.actors

import akka.actor.Actor
import play.api.Logger

class UserFilesUpdateActor extends Actor {

  def receive = {
    case a => Logger.info("FileUpdateActor received: " + a.toString)
  }

}
