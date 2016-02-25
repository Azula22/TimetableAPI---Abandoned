package controllers

import models.{Point, PointForm}
import play.api.libs.json.{JsValue, Json, Writes}
import play.api.mvc._
import services.PointService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class Application extends Controller {

  implicit val pointWrite = new Writes[Seq[Point]] {
    override def writes(points: Seq[Point]) = Json.arr(points.map(point =>
      Json.obj(
        "subj" -> point.subject,
        "gName" -> point.groupName,
        "type" -> point.kind,
        "start" -> point.start,
        "end" -> point.ending,
        "teacher" -> point.teacher,
        "aa" -> point.auditorium
      )))
  }

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
          val newPoint = Point(0, data.subject, data.groupName, data.kind, formatTime(data.start), formatTime(data.ending), data.teacher, data.auditorium)
          PointService.addPoint(newPoint).map(res => Redirect(routes.Application.index()))
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


  //It's working wrong. Fix it
  def getGroupJson(group: String) = Action {
    val groups = PointService.group(group).value
    groups match {
      case Some(value) =>
        val json = Json.toJson(value.get)
        val readable = Json.prettyPrint(json)
        Ok(readable)
      case None => Ok("bad news")
    }
  }
}