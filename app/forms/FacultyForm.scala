package forms

import models.Faculty
import play.api.data.Form
import play.api.data.Forms._

/**
  * Created by Kushik on 03.05.2016.
  */
object FacultyForm {
  val form = Form(
    mapping(
      "nameOfFaculty" -> nonEmptyText
    )(Faculty.apply)(Faculty.unapply)
  )
}
