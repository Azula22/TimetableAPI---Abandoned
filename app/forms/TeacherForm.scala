package forms

import models.Teacher
import play.api.data.Form
import play.api.data.Forms._

object TeacherForm {
  val form = Form(
    mapping(
      "nameOfTeacher" -> nonEmptyText
    )(Teacher.apply)(Teacher.unapply)
  )
}
