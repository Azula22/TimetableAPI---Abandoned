package controllers

import models._
import play.api.mvc._
import services.PointService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class PointController extends Controller {

  val days: Seq[String] = Seq("mon", "tue", "wed", "thur", "fri", "sat", "sund")
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
          Helper.AddSubjectAllDays.addMonday(data)
          Helper.AddSubjectAllDays.addTuesday(data)
          Helper.AddSubjectAllDays.addWednesday(data)
          Helper.AddSubjectAllDays.addThursday(data)
          Helper.AddSubjectAllDays.addFriday(data)
          Helper.AddSubjectAllDays.addSaturday(data)
          Helper.AddSubjectAllDays.addSunday(data)
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