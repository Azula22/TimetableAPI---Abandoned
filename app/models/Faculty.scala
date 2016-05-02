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
case class Faculty(name: String)

object FacultyForm {
  val form = Form(
    mapping(
      "nameOfFaculty" -> nonEmptyText
    )(Faculty.apply)(Faculty.unapply)
  )
}

class FacultyTable(tag: Tag) extends Table[Faculty](tag, "FACULTY") {
  def name = column[String]("NAME", O.PrimaryKey)

  override def * = name <>(Faculty.apply, Faculty.unapply)
}

object Faculties {
  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  val faculties = TableQuery[FacultyTable]

  def add(faculty: Faculty): Future[String] = {
    dbConfig.db.run(faculties += faculty).map(res => "Faculty successfully added").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def delete(name: String): Future[Int] = {
    dbConfig.db.run(faculties.filter(_.name === name).delete)
  }

  def listAll: Future[Seq[Faculty]]={
    dbConfig.db.run(faculties.result)
  }
}

