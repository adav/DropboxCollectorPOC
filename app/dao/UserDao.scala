package dao

import javax.inject.Inject

import models.DropboxUser
import play.api.Logger
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.Future

class UserDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  private val Users = TableQuery[UserTable]

  def all(): Future[Seq[DropboxUser]] = db.run(Users.result)

  def insert(user: DropboxUser) = db.run(Users += user)

  private class UserTable(tag: Tag) extends Table[DropboxUser](tag, "USER") {
    def uid = column[String]("UID", O.PrimaryKey)
    def token = column[String]("TOKEN")

    override def * = (uid, token) <> (DropboxUser.tupled, DropboxUser.unapply)
  }
}
