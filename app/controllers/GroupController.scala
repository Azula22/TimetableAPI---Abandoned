package controllers

import forms.GroupForm
import models.Group
import play.api.mvc._
import services.{FacultyService, GroupService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class GroupController extends Controller {

  def get = Action.async {
    implicit request =>
      GroupService.getAllGroups.map {
        groups => Ok(views.html.addGroup(groups))
      }
  }

  def addGroup() = Action.async {
    implicit request =>
      GroupForm.form.bindFromRequest.fold(
        errorForm => ???,
        data => {
          (for {
            seqOfFaculties <- FacultyService.getAllFaculties
            fut <- if (seqOfFaculties.exists(_.name == data.faculty))
              GroupService.addGroup(Group(0, data.nameGroup, data.faculty))
            else
              Future((): Unit)
          } yield fut)
            .map(_ => Redirect(routes.GroupController.get()))
            .recoverWith { case _ =>
              Future(Redirect(routes.GroupController.get()))
            }
        })
  }

  def delete(id: Long) = Action.async {
    implicit request =>
      GroupService.delete(id).map(_ => Redirect(routes.GroupController.get()))
  }
}
