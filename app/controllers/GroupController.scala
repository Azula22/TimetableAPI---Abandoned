package controllers

import models.{Group, GroupForm}
import play.api.mvc._
import services.GroupService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class GroupController extends Controller {

  def index = Action.async{
    implicit request=>
    GroupService.listAllGroups.map{
      groups =>Ok(views.html.addGroup(GroupForm.form, groups))
    }
  }

  def addGroup = Action.async {
    implicit request =>
      GroupForm.form.bindFromRequest.fold(
        errorForm => Future.successful(Ok(views.html.addGroup(errorForm, Seq.empty[Group]))),
        data => {
          val newGroup = Group(0, data.nameGroup)
          GroupService.addGroup(newGroup).map(res => Redirect(routes.PointController.index()))
        }
      )
  }
}
