package controllers

import forms.GroupForm
import models.Group
import play.api.mvc._
import services.GroupService

import scala.concurrent.ExecutionContext.Implicits.global

class GroupController extends Controller {

  def get = Action.async {
    implicit request =>
      GroupService.listAllGroups.map {
        groups => Ok(views.html.addGroup(groups))
      }
  }

  def addGroup() = Action.async {
    implicit request =>
      GroupForm.form.bindFromRequest.fold(
        errorForm => ???,
        data => {
          val newGroup = Group(0, data.nameGroup, data.faculty)
          GroupService.addGroup(newGroup).map(_ => Redirect(routes.GroupController.get()))
        }
      )
  }

  def delete(id: Long) = Action.async {
    implicit request =>
      GroupService.delete(id).map(_ => Redirect(routes.GroupController.get()))
  }
}
