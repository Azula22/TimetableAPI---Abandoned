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

case class Point(id: Long, subject: String, day: String, groupName: String, kind: String, start: Time, teacher: String, auditorium: Int, pair: Boolean)

case class FormData(subject: Option[String], kind: Option[String],
                    teacher: Option[String], auditorium: Option[Int])

case class FormDataAllDays(groupName: String,
                           mon: FormDataOneDay,
                           tue: FormDataOneDay,
                           wed: FormDataOneDay,
                           thur: FormDataOneDay,
                           fri: FormDataOneDay,
                           sat: FormDataOneDay,
                           sund: FormDataOneDay) {
  def getDay(day: String): Option[FormDataOneDay] = {
    day match {
      case "Monday" => Some(this.mon)
      case "Tuesday" => Some(this.tue)
      case "Wednesday" => Some(this.wed)
      case "Thursday" => Some(this.thur)
      case "Friday" => Some(this.fri)
      case "Saturday" => Some(this.sat)
      case "Sunday" => Some(this.sund)
      case _ => None
    }
  }
}

case class FormDataOneDay(first: PairOddData,
                          second: PairOddData,
                          third: PairOddData,
                          fourth: PairOddData,
                          fifth: PairOddData,
                          sixth: PairOddData) {
  def getPair(pair: String): Option[PairOddData] = {
    pair match {
      case "8:00" => Some(this.first)
      case "9:35" => Some(this.second)
      case "11:10" => Some(this.third)
      case "12:50" => Some(this.fourth)
      case "14:25" => Some(this.fifth)
      case "16:00" => Some(this.sixth)
      case _ => None
    }
  }
}

case class PairOddData(pair: FormData, odd: FormData) {
  def getPairOrOdd(maVal: Int): Option[FormData]  = {
    maVal match {
      case 1 => Some(this.odd)
      case 2 => Some(this.pair)
      case _ => None
    }
  }
}


object FormAllDays {
  val form = Form(
    mapping(
      "groupName" -> nonEmptyText,
      "Monday" -> FormOneDay.form.mapping,
      "Tuesday" -> FormOneDay.form.mapping,
      "Wednesday" -> FormOneDay.form.mapping,
      "Thursday" -> FormOneDay.form.mapping,
      "Friday" -> FormOneDay.form.mapping,
      "Saturday" -> FormOneDay.form.mapping,
      "Sunday" -> FormOneDay.form.mapping
    )(FormDataAllDays.apply)(FormDataAllDays.unapply)
  )
}

object FormOneDay {
  val form = Form(
    mapping(
      "first" -> PairOddForm.form.mapping,
      "second" -> PairOddForm.form.mapping,
      "third" -> PairOddForm.form.mapping,
      "fourth" -> PairOddForm.form.mapping,
      "fifth" -> PairOddForm.form.mapping,
      "sixth" -> PairOddForm.form.mapping
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

object PairOddForm {
  val form = Form(
    mapping(
      "pair" -> PointForm.form.mapping,
      "odd" -> PointForm.form.mapping
    )(PairOddData.apply)(PairOddData.unapply)
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

  def pair = column[Boolean]("pair")

  override def * = (id, subject, day, groupName, kind, start, teacher, auditorium, pair) <>(Point.tupled, Point.unapply)
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