package models

import play.api.data.Form
import play.api.data.Forms._

case class Point(id: BigInt, name: String)

case class PointFormData(name: String)

object PointForm{
  val form = Form(
    mapping(
      "name" -> nonEmptyText
    )(PointFormData.apply)(PointFormData.unapply)
  )
}