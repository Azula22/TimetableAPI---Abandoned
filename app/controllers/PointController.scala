package controllers

import models.{GroupForm, Point, PointForm}
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
        points => Ok(views.html.index(PointForm.form, points, days, times))
      }
  }

  def addPoints = Action.async {
    implicit request =>
      PointForm.form.bindFromRequest.fold(
        errorForm => Future.successful(Ok(views.html.index(errorForm, Seq.empty[Point], days, times))),
        data => {
          for (d <- days) {
            for (t <- times) {
              if (data.subject != null) {
                val newPoint = Point(0, data.subject, d, data.groupName, data.kind, formatTime(t), data.teacher, data.auditorium)
                PointService.addPoint(newPoint)
              }
            }
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

  def formatTime(time: String): java.sql.Time = {
    val nTime = time + ":00"
    java.sql.Time.valueOf(nTime)
  }


}