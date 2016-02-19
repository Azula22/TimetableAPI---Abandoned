package models

import play.api.Play
import play.api.data.Form
import play.api.data.Forms._
import play.api.db.slick.DatabaseConfigProvider
import scala.concurrent.Future
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._
import scala.concurrent.ExecutionContext.Implicits.global


case class Point(id: Long, name: String)

case class PointFormData(name: String)

object PointForm {
  val form = Form(
    mapping(
      "name" -> nonEmptyText
    )(PointFormData.apply)(PointFormData.unapply)
  )
}

class PointTableDef(tag: Tag) extends Table[Point](tag, "Point") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")

  override def * = (id, name) <>(Point.tupled, Point.unapply)
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
}