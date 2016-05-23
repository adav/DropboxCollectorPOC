package services

import models.DropboxUser
import play.api.Logger

object CurrentDropboxUser {
  private var user: DropboxUser = DropboxUser("Made up", "OMG")

  def update(newUser: DropboxUser): Unit = {
    user = newUser
    Logger.info("Welcome " + user.uid + " (Token: " + user.token + ")")
  }

  def currentUser: DropboxUser = user.copy()
}
