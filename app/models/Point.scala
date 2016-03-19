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

case class FormData(subject: Option[String], kind: Option[String],
                    teacher: Option[String], auditorium: Option[Int])

case class FormDataAllDays(groupName: String,
                           mon: FormDataOneDay,
                           tue: FormDataOneDay,
                           wed: FormDataOneDay,
                           thur: FormDataOneDay,
                           fri: FormDataOneDay,
                           sat: FormDataOneDay,
                           sund:FormDataOneDay)

case class FormDataOneDay(first: FormData,
                          second: FormData,
                          third: FormData,
                          fourth: FormData,
                          fifth: FormData,
                          sixth: FormData)

object FormAllDays {
  val form = Form(
    mapping(
      "groupName" -> nonEmptyText,
      "mon" -> FormOneDay.form.mapping,
      "tue" -> FormOneDay.form.mapping,
      "wed" -> FormOneDay.form.mapping,
      "thur" -> FormOneDay.form.mapping,
      "fri" -> FormOneDay.form.mapping,
      "sat" -> FormOneDay.form.mapping,
      "sund" -> FormOneDay.form.mapping
    )(FormDataAllDays.apply)(FormDataAllDays.unapply)
  )
}

object FormOneDay {
  val form = Form(
    mapping(
      "first" -> PointForm.form.mapping,
      "second" -> PointForm.form.mapping,
      "third" -> PointForm.form.mapping,
      "fourth" -> PointForm.form.mapping,
      "fifth" -> PointForm.form.mapping,
      "sixth" -> PointForm.form.mapping
    )(FormDataOneDay.apply)(FormDataOneDay.unapply)
  )
}

object PointForm {
  val form = Form(
    mapping(
      "subject" -> optional(text),
      "kind" -> optional(text),
      "teacher" -> optional(text),
      "auditorium" -> optional(number)
    )(FormData.apply)(FormData.unapply)
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