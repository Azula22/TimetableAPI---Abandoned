package models

import play.api.Play
import play.api.data.Form
import play.api.data.Forms._
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class Group(id: Long, nameGroup: String, faculty: String)

case class GroupFormData(nameGroup: String, faculty: String)

object GroupForm {
  val form = Form(
    mapping(
      "nameGroup" -> nonEmptyText,
      "faculty" -> nonEmptyText
    )(GroupFormData.apply)(GroupFormData.unapply)
  )
}

class GroupTableDef(tag: Tag) extends Table[Group](tag, "GROUPS") {
  def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

  def groupName = column[String]("GROUPNAME")

  def faculty = column[String]("FACULTY")

  override def * = (id, groupName, faculty) <>(Group.tupled, Group.unapply)
}

object Groups {

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  val groups = TableQuery[GroupTableDef]

  def add(group: Group): Future[String] =
    dbConfig.db.run(groups += group).map(res => "Point successfully added").recover {
      case ex: Exception => ex.getCause.getMessage
    }

  def delete(id: Long): Future[Int] = dbConfig.db.run(groups.filter(_.id === id).delete)

  def get(id: Long): Future[Option[Group]] = dbConfig.db.run(groups.filter(_.id === id).result.headOption)

  def listAll: Future[Seq[Group]] = dbConfig.db.run(groups.result)

  def getGroupByName(name: String): Future[Option[Long]] = dbConfig.db.run(groups.filter(_.groupName === name)
    .map(_.id).result.headOption)

  def getFacultyByGroupName(name: String): Future[Option[String]] = dbConfig.db.run(groups.filter(_.groupName === name)
    .map(_.faculty).result.headOption)
}
