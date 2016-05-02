package controllers


import controllers.DataHelper.days
import models.GroupForm
import play.api.mvc._
import services.SubjectService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class ScheduleController extends Controller {

  def indexDefault = Action.async {
    implicit request =>
      SubjectService.getAllSubjects.map {
        points => Ok(views.html.showSchedule(GroupForm.form, null, days))
      }
  }

  def index(data: String) = Action.async {
    implicit request =>
      SubjectService.getGroupByName(data).map {
        points => Ok(views.html.showSchedule(GroupForm.form, points.sortBy(_.start.toLocalTime), days))
      }
  }

  def checkGroup = Action.async {
    implicit request =>
      GroupForm.form.bindFromRequest.fold(
        errorForm => Future.successful(Ok(views.html.bad())),
        data => {
          SubjectService.getAllSubjects.map(
            res => Redirect(routes.ScheduleController.index(data.nameGroup)))
        }
      )
  }
}
