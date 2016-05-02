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

case class Subject(id: Long, name: String, groupID: Long, facultyID: Long, teacherID: Long, day: String, start: Time, oddNot: Boolean, kind: String, auditorium: Int)

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
  def getPairOrOdd(maVal: String): Option[FormData] = {
    maVal match {
      case "odd" => Some(this.odd)
      case "pair" => Some(this.pair)
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

object SubjectForm {
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
      "pair" -> SubjectForm.form.mapping,
      "odd" -> SubjectForm.form.mapping
    )(PairOddData.apply)(PairOddData.unapply)
  )
}

class SubjectTableDef(tag: Tag) extends Table[Subject](tag, "SUBJECTS") {
  def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

  def name = column[String]("NAME")

  def groupID = column[Long]("GROUPID")

  def facultyID = column[Long]("FACULTYID")

  def teacherID = column[Long]("TEACHERID")

  def day = column[String]("DAY")

  def start = column[Time]("START")

  def oddNot = column[Boolean]("ODDNOT")

  def kind = column[String]("KIND")

  def auditorium = column[Int]("auditorium")

  override def * = (id, name, groupID, facultyID, teacherID, day, start, oddNot, kind, auditorium) <>(Subject.tupled, Subject.unapply)
}

object Points {

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  val subjects = TableQuery[SubjectTableDef]

  def add(point: Subject): Future[String] = {
    dbConfig.db.run(subjects += point).map(res => "Point successfully added").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def checkExistance(groupName: String, day: String, time: Time, pair: Boolean): Future[Option[Subject]] = {
    dbConfig.db.run(subjects.filter(p => p.name === groupName && p.day === day && p.start === time && p.oddNot === pair).result.headOption)
  }

  def updateSubject(subject: Subject, name: String, kind: String, teacherID: Long, auditorium: Int): Future[String] = {
    dbConfig.db.run(subjects.filter(_.id === subject.id).map(p => (p.name, p.kind, p.teacherID, p.auditorium)).update((name, kind, teacherID, auditorium)).map(res => "Point successfully updated"))
  }

  def delete(id: Long): Future[Int] = {
    dbConfig.db.run(subjects.filter(_.id === id).delete)
  }

  def get(id: Long): Future[Option[Subject]] = {
    dbConfig.db.run(subjects.filter(_.id === id).result.headOption)
  }

  def listAll: Future[Seq[Subject]] = {
    dbConfig.db.run(subjects.result)
  }

  def getGroup(group: String): Future[Seq[Subject]] = {
    dbConfig.db.run(subjects.filter(_.name === group).result)
  }

  def getTeacherByID(teacherID: Long): Future[Seq[Subject]] = {
    dbConfig.db.run(subjects.filter(_.teacherID === teacherID).result)
  }
}