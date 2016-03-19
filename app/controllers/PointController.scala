package controllers

import models._
import play.api.mvc._
import services.PointService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class PointController extends Controller {

  val days: Seq[String] = Seq("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
  val times: Seq[String] = Seq("8:00", "9:35", "11:10", "12:50", "14:25", "16:00")

  def index = Action.async {
    implicit request =>
      PointService.listAllPoints map {
        points => Ok(views.html.index(PointForm.form, PointFormDays.form, points, days, times))
      }
  }

  def addPoints = Action.async {
    implicit request =>
      PointFormDays.form.bindFromRequest.fold(
        errorForm => Future.successful(BadRequest(views.html.bad())),
        data => {
          if (data.mon.subject.isDefined) {
            val newPoint = Point(0, data.mon.subject.get, "Monday", data.groupName, data.mon.kind.get, java.sql.Time.valueOf("8:00:00"), data.mon.teacher.get, data.mon.auditorium.get)
            PointService.addPoint(newPoint)
          }
          if (data.tue.subject.isDefined) {
            val newPoint = Point(0, data.tue.subject.get, "Tuesday", data.groupName, data.tue.kind.get, java.sql.Time.valueOf("8:00:00"), data.tue.teacher.get, data.tue.auditorium.get)
            PointService.addPoint(newPoint)
          }
          PointService.listAllPoints.map(res => Redirect(routes.PointController.index()))
        }
      )
  }

  def deletePoint(id: Long) = Action.async {
    implicit request =>
      PointService.deletePoint(id) map {
        res => Redirect(routes.PointController.index())
      }
  }

}