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
          if (data.mon.first.subject.isDefined) {
            val newPoint = Point(0, data.mon.first.subject.get, "Monday", data.groupName, data.mon.first.kind.get, java.sql.Time.valueOf("8:00:00"), data.mon.first.teacher.get, data.mon.first.auditorium.get)
            PointService.addPoint(newPoint)
          }
          if (data.mon.second.subject.isDefined) {
            val newPoint = Point(0, data.mon.second.subject.get, "Monday", data.groupName, data.mon.second.kind.get, java.sql.Time.valueOf("9:35:00"), data.mon.second.teacher.get, data.mon.second.auditorium.get)
            PointService.addPoint(newPoint)
          }
          if (data.mon.third.subject.isDefined) {
            val newPoint = Point(0, data.mon.third.subject.get, "Monday", data.groupName, data.mon.third.kind.get, java.sql.Time.valueOf("11:10:00"), data.mon.third.teacher.get, data.mon.third.auditorium.get)
            PointService.addPoint(newPoint)
          }
          if (data.mon.fourth.subject.isDefined) {
            val newPoint = Point(0, data.mon.fourth.subject.get, "Monday", data.groupName, data.mon.fourth.kind.get, java.sql.Time.valueOf("12:50:00"), data.mon.fourth.teacher.get, data.mon.fourth.auditorium.get)
            PointService.addPoint(newPoint)
          }
          if (data.mon.fifth.subject.isDefined) {
            val newPoint = Point(0, data.mon.fifth.subject.get, "Monday", data.groupName, data.mon.fifth.kind.get, java.sql.Time.valueOf("14:25:00"), data.mon.fifth.teacher.get, data.mon.fifth.auditorium.get)
            PointService.addPoint(newPoint)
          }
          if (data.mon.sixth.subject.isDefined) {
            val newPoint = Point(0, data.mon.sixth.subject.get, "Monday", data.groupName, data.mon.sixth.kind.get, java.sql.Time.valueOf("16:00:00"), data.mon.sixth.teacher.get, data.mon.sixth.auditorium.get)
            PointService.addPoint(newPoint)
          }

          /*
          if (data.tue.subject.isDefined) {
            val newPoint = Point(0, data.tue.subject.get, "Tuesday", data.groupName, data.tue.kind.get, java.sql.Time.valueOf("8:00:00"), data.tue.teacher.get, data.tue.auditorium.get)
            PointService.addPoint(newPoint)
          }

          */
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