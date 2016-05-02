package controllers

import models.{Group, GroupForm}
import play.api.mvc._
import services.GroupService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class GroupController extends Controller {

  def get = Action.async {
    implicit request =>
      GroupService.listAllGroups.map {
        groups => Ok(views.html.addGroup(GroupForm.form, groups))
      }
  }

  def addGroup() = Action.async {
    implicit request =>
      GroupForm.form.bindFromRequest.fold(
        errorForm => Future.successful(Ok(views.html.bad)),
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
