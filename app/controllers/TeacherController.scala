package controllers

import forms.TeacherForm
import play.api.mvc.{Controller, _}
import services.TeacherService

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Kushik on 03.05.2016.
  */
class TeacherController extends Controller {


  def get() = Action.async {
    implicit request =>
      TeacherService.getAllTeachers.map(teachers =>
        Ok(views.html.addTeacher(teachers))
      )
  }

  def addTeacher() = Action.async {
    implicit request =>
      TeacherForm.form.bindFromRequest.fold(
        error => ???,
        data =>
          TeacherService.addTeacher(data.name).map(_ => Redirect(routes.TeacherController.get()))
      )
  }

  def delete(name: String) = Action.async {
    implicit request =>
      TeacherService.deleteTeacher(name).map(_ => Redirect(routes.TeacherController.get()))
  }
}
