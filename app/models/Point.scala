package models

import java.sql.Time

import play.api.Play
import play.api.data.Form
import play.api.data.Forms._
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


case class Point(id: Long, subject: String, day: String, groupName: String, kind: String, start: Time, teacher: String, auditorium: Int)

case class PointFormData(subject: String, groupName: String, kind: String,
                         teacher: String, auditorium: Int)


object PointForm {
  val form = Form(
      mapping(
        "subject" -> text,
        "groupName" -> text,
        "kind" -> text,
        "teacher" -> text,
        "auditorium" -> number
      )(PointFormData.apply)(PointFormData.unapply)
  )
}

class PointTableDef(tag: Tag) extends Table[Point](tag, "points") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def subject = column[String]("subject")

  def day = column[String]("day")

  def groupName = column[String]("groupName")

  def kind = column[String]("kind")

  def start = column[Time]("start")

  def teacher = column[String]("teacher")

  def auditorium = column[Int]("auditorium")

  override def * = (id, subject, day, groupName, kind, start, teacher, auditorium) <>(Point.tupled, Point.unapply)
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

  def getTeacher(teacher: String): Future[Seq[Point]] = {
    dbConfig.db.run(points.filter(_.teacher === teacher).result)
  }

}