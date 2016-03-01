package controllers

import models.{Point, PointForm}
import play.api.libs.json.{JsValue, Json, Writes}
import play.api.mvc._
import services.PointService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class Application extends Controller {

  val days: Seq[String] = Seq("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

  def index = Action.async {
    implicit request =>
      PointService.listAllPoints map {
        points => Ok(views.html.index(PointForm.form, points))
      }
  }

  def addUser = Action.async {
    implicit request =>
      PointForm.form.bindFromRequest.fold(
        errorForm => Future.successful(Ok(views.html.index(errorForm, Seq.empty[Point]))),
        data => {
          if (days.contains(data.day)) {
            val newPoint = Point(0, data.subject, data.day, data.groupName, data.kind, formatTime(data.start), formatTime(data.ending), data.teacher, data.auditorium)
            PointService.addPoint(newPoint).map(res => Redirect(routes.Application.index()))
          } else Future {
            //TODO make error message instead redirecting
            Redirect(routes.Application.index())
          }
        }
      )
  }

  def deletePoint(id: Long) = Action.async {
    implicit request =>
      PointService.deletePoint(id) map {
        res => Redirect(routes.Application.index())
      }
  }

  def formatTime(time: String): java.sql.Time = {
    val nTime = time + ":00"
    java.sql.Time.valueOf(nTime)
  }




}