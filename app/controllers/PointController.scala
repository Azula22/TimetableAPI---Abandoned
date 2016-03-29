package controllers

import models._
import play.api.mvc._
import services.PointService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class PointController extends Controller {

  val days: Seq[String] = Seq("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
  val timesForDisplaying: Seq[String] = Seq("8:00", "9:35", "11:10", "12:50", "14:25", "16:00")
  val oddNot: Seq[String] = Seq("odd", "pair")
  val timesForUsing: Seq[String] = Seq("first", "second", "third", "fourth", "fifth", "sixth")

  def index = Action.async {
    implicit request =>
      PointService.listAllPoints map {
        points => Ok(views.html.index(PointForm.form, FormAllDays.form, points, days, timesForDisplaying, oddNot, timesForUsing))
      }
  }

  def addPoints = Action.async {
    implicit request =>
      FormAllDays.form.bindFromRequest.fold(
        errorForm => Future.successful(BadRequest(views.html.bad())),
        data => {

          //Cycles for getting to each point separately
          for (d <- days) {
            for (t <- timesForDisplaying) {
              for (oN <- oddNot) {

                if (subjectIsDefined(d, t, oN, data)) {
                  addThisSubject(data, d, t, oN)
                }
              }
            }
          }
        }
      )
      PointService.listAllPoints.map(res => Redirect(routes.PointController.index()))
  }

  def pointData(d: String, t: String, oN: String, data: FormDataAllDays): FormData = {
    data.getDay(d).get.getPair(t).get.getPairOrOdd(oN).get
  }

  def subjectIsDefined(d: String, t: String, oN: String, data: FormDataAllDays): Boolean = {
    if (pointData(d, t, oN, data).subject.isDefined) true else false
  }

  def addThisSubject(data: FormDataAllDays, d: String, t: String, oN: String) = {
    val myLesson = pointData(d, t, oN, data)
    PointService.checkExistance(data.groupName, d, java.sql.Time.valueOf(t + ":00"), reverseOddNotToBoolean(oN)).map(p =>
      PointService.alterPoint(p.get, myLesson.subject.get, myLesson.kind.getOrElse("-"), myLesson.teacher.getOrElse("-"), myLesson.auditorium.getOrElse(0))).recover {
      case _ => addPoint(data, d, t, oN)
    }
  }

  def addPoint(data: FormDataAllDays, d: String, t: String, oN: String): Unit = {
    val myLesson = pointData(d, t, oN, data)
    val newPoint = Point(0, myLesson.subject.getOrElse("-"), d, data.groupName, myLesson.kind.getOrElse("-"), java.sql.Time.valueOf(t + ":00"), myLesson.teacher.getOrElse("-"), myLesson.auditorium.getOrElse(0), if (oN == "pair") true else false)
    PointService.addPoint(newPoint)
  }

  def reverseOddNotToBoolean(oddNot: String): Boolean = {
    if (oddNot == "odd") false else true
  }

  def deletePoint(id: Long) = Action.async {
    implicit request =>
      PointService.deletePoint(id) map {
        res => Redirect(routes.PointController.index())
      }
  }

}