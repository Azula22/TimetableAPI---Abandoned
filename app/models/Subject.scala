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

case class Subject(id: Long, name: String, groupID: Long, faculty: String, teacher: String,
                   day: String, start: Time, oddNot: Boolean, kind: String, auditorium: Int)


class SubjectTableDef(tag: Tag) extends Table[Subject](tag, "SUBJECTS") {
  def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

  def name = column[String]("NAME")

  def groupID = column[Long]("GROUPID")

  def faculty = column[String]("FACULTY")

  def teacher = column[String]("TEACHER")

  def day = column[String]("DAY")

  def start = column[Time]("START")

  def isOdd = column[Boolean]("IS_ODD")

  def kind = column[String]("KIND")

  def auditorium = column[Int]("AUDITORIUM")

  override def * = (id, name, groupID, faculty, teacher, day, start, isOdd, kind, auditorium) <>(Subject.tupled, Subject.unapply)
}

object Subjects {

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  val subjects = TableQuery[SubjectTableDef]

  def add(point: Subject): Future[String] = {
    dbConfig.db.run(subjects += point).map(res => "Point successfully added").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def checkExistance(groupName: String, day: String, time: Time, pair: Boolean): Future[Option[Subject]] = {
    dbConfig.db.run(subjects.filter(p => p.name === groupName && p.day === day && p.start === time && p.isOdd === pair).result.headOption)
  }

  def updateSubject(subject: Subject, name: String, kind: String, teacher: String, auditorium: Int): Future[String] = {
    dbConfig.db.run(subjects.filter(_.id === subject.id).map(p => (p.name, p.kind, p.teacher, p.auditorium)).update((name, kind, teacher, auditorium)).map(res => "Point successfully updated"))
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

  def getTeacher(teacher: String): Future[Seq[Subject]] = {
    dbConfig.db.run(subjects.filter(_.teacher === teacher).result)
  }
}