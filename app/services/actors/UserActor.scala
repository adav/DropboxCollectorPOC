package services.actors

import javax.inject.Inject

import akka.actor.Actor
import dao.UserDao
import models.DropboxUser
import play.api.Logger
import services.CurrentDropboxUser

class NewUserActor @Inject()(userDao: UserDao) extends Actor {

  def receive = {
    case newUser: DropboxUser => {
      userDao.insert(newUser)
      Logger.info("added " + newUser.toString + " to DB")
    }
    case _ => Logger.error("NewUserActor received unknown message")
  }

}
