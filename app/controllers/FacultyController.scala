package controllers

/**
  * Created by Kushik on 02.05.2016.
  */

import models.{Faculty, FacultyForm}
import play.api.mvc._
import services.FacultyService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class FacultyController extends Controller {


  def get() = Action.async {
    implicit request =>
      FacultyService.getAllFaculties.map(
        faculties => Ok(views.html.addFaculty(FacultyForm.form, faculties))
      )
  }

  def addFaculty() = Action.async {
    implicit request =>
      FacultyForm.form.bindFromRequest.fold(
        error => ???,
        data => FacultyService.addFaculty(data.name).map(_ => Redirect(routes.FacultyController.get()))
      )
  }

  def deleteFaculty(name: String) = Action.async {
    implicit request =>
      FacultyService.deleteFaculty(name).map(_ => Redirect(routes.FacultyController.get()))
  }
}
