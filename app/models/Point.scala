package models

import java.sql.Time

import play.api.Play
import play.api.data.Form
import play.api.data.Forms._
import play.api.db.slick.DatabaseConfigProvider
import scala.concurrent.Future
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._
import scala.concurrent.ExecutionContext.Implicits.global


case class Point(id: Long, subject: String, groupName: String, kind: String, start: Time, ending: Time,
                 teacher: String, auditorium: Int)

case class PointFormData(subject: String, groupName: String, kind: String, start: String, ending: String,
                         teacher: String, auditorium: Int)

object PointForm {
  val form = Form(
    mapping(
      "subject" -> nonEmptyText,
      "groupName" -> nonEmptyText,
      "kind" -> nonEmptyText,
      "start" -> nonEmptyText,
      "ending" -> nonEmptyText,
      "teacher" -> nonEmptyText,
      "auditorium" -> number
    )(PointFormData.apply)(PointFormData.unapply)
  )
}

class PointTableDef(tag: Tag) extends Table[Point](tag, "points") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def subject = column[String]("subject")

  def groupName = column[String]("groupName")

  def kind = column[String]("kind")

  def start = column[Time]("start")

  def ending = column[Time]("ending")

  def teacher = column[String]("teacher")

  def auditorium = column[Int]("auditorium")

  override def * = (id, subject, groupName, kind, start, ending, teacher, auditorium) <>(Point.tupled, Point.unapply)
}

object Points {
  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  val points = TableQuery[PointTableDef]

  def add(point: Point): Future[String] = {
    dbConfig.db.run(points += point).map(res => "Point successfully added").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def delete(id: Long): Future[Int] = {
    dbConfig.db.run(points.filter(_.id === id).delete)
  }

  def get(id: Long): Future[Option[Point]] = {
    dbConfig.db.run(points.filter(_.id === id).result.headOption)
  }

  def listAll: Future[Seq[Point]] = {
    dbConfig.db.run(points.result)
  }

  def getGroup(group: String): Future[Seq[Point]] = {
    dbConfig.db.run(points.filter(_.groupName === group).result)
  }

}