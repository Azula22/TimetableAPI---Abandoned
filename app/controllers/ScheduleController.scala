package controllers


import models.{GroupForm, Point}
import play.api.mvc._
import services.PointService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class ScheduleController extends Controller {

  val days: Seq[String] = Seq("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")


  def indexDefault = Action.async {
    implicit request =>
      PointService.listAllPoints.map {
        points => Ok(views.html.showSchedule(GroupForm.form, null, days))
      }
  }

  def index(data: String) = Action.async {
    implicit request =>
      PointService.group(data).map {
        points => Ok(views.html.showSchedule(GroupForm.form, points.sortBy(_.start.toLocalTime), days))
      }
  }

  def checkGroup = Action.async {
    implicit request =>
      GroupForm.form.bindFromRequest.fold(
        errorForm => Future.successful(Ok(views.html.bad())),
        data => {
          PointService.listAllPoints.map(
            res => Redirect(routes.ScheduleController.index(data.nameGroup)))
        }
      )
  }
}
