package services.actors

import akka.actor.Actor
import play.api.Logger

class FileUpdateActor extends Actor {

  def receive = {
    case _ => Logger.info("FileUpdateActor received")
  }

}
