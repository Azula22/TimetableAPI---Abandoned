package forms

import play.api.data.Form
import play.api.data.Forms._

object GroupForm {
  val form = Form(
    mapping(
      "nameGroup" -> nonEmptyText,
      "faculty" -> nonEmptyText
    )(GroupFormData.apply)(GroupFormData.unapply)
  )
}

case class GroupFormData(nameGroup: String, faculty: String)