package models

import play.api.Play
import play.api.data.Form
import play.api.data.Forms._
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by Kushik on 02.05.2016.
  */
case class Teacher(name: String)

object TeacherForm {
  val form = Form(
    mapping(
      "nameOfFaculty" -> nonEmptyText
    )(Teacher.apply)(Teacher.unapply)
  )
}

class TeacherTable(tag: Tag) extends Table[Teacher](tag, "TEACHERS") {
  def name = column[String]("NAME", O.PrimaryKey)

  override def * = name <>(Teacher.apply, Teacher.unapply)
}

object Teachers {
  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  val teachers = TableQuery[TeacherTable]

  def add(teacher: Teacher): Future[String] = {
    dbConfig.db.run(teachers += teacher).map(res => "Teacher successfully added").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def delete(name: String): Future[Int] = {
    dbConfig.db.run(teachers.filter(_.name === name).delete)
  }

  def listAll: Future[Seq[Teacher]] = {
    dbConfig.db.run(teachers.result)
  }

  def getTeacher(teacherName: String): Future[Option[Teacher]] = {
    dbConfig.db.run(teachers.filter(_.name === teacherName).result.headOption)
  }
}
