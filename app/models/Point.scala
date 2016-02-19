package models

import play.api.data.Form
import play.api.data.Forms._
import slick.driver.MySQLDriver.api._


case class Point(id: BigInt, name: String)

case class PointFormData(name: String)

object PointForm{
  val form = Form(
    mapping(
      "name" -> nonEmptyText
    )(PointFormData.apply)(PointFormData.unapply)
  )
}

class PointTableDef(tag: Tag) extends Table[Point](tag, "Point"){
  def id = column[BigInt]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")

  override def * = (id, name)<>(Point.tupled, Point.unapply)
}