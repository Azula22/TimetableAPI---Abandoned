package controllers


import models.{GroupForm, Point}
import play.api.mvc._
import services.PointService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class ScheduleController extends Controller {

  val days: Seq[String] = Seq("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")


  def index(seq: Seq[Point]) = Action.async {
    implicit request =>
      PointService.listAllPoints.map {
        points => Ok(views.html.showSchedule(GroupForm.form, points, days))
      }
  }

  def checkGroup = Action.async {
    implicit request =>
      GroupForm.form.bindFromRequest.fold(
        errorForm => Future.successful(Ok(views.html.showSchedule(errorForm, Seq.empty[Point], days))),
        data => {
          PointService.group(data.nameGroup).map(
            res => Redirect(routes.ScheduleController.index()))
        }
      )
  }
}
