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
        points => Ok(views.html.index(PointForm.form, FormAllDays.form, points, days, times))
      }
  }

  def addPoints = Action.async {
    implicit request =>
      FormAllDays.form.bindFromRequest.fold(
        errorForm => Future.successful(BadRequest(views.html.bad())),
        data => {
          for (d <- days) {
            for (t <- times) {
              for (oddNot <- 1 to 2) {
                val myLesson = data.getDay(d).get.getPair(t).get.getPairOrOdd(oddNot).get
                if (myLesson.subject.isDefined) {
                  val newPoint = Point(0, myLesson.subject.get, d, data.groupName, myLesson.kind.get, java.sql.Time.valueOf(t+":00"), myLesson.teacher.get, myLesson.auditorium.get, if(oddNot==2) true else false)
                  PointService.addPoint(newPoint)
                }
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

}