package controllers

import models.{Point, PointForm}
import play.api.libs.json.{JsValue, Json, Writes}
import play.api.mvc._
import services.PointService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class Application extends Controller {

  val pointWrites = new Writes[Point] {
    override def writes(point: Point): JsValue = Json.obj(
      "id" -> point.id,
      "name" -> point.subject)
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
          val newPoint = Point(0, data.subject, data.groupName, data.kind, java.sql.Time.valueOf(data.start), java.sql.Time.valueOf(data.ending), data.teacher, data.auditorium)
          PointService.addPoint(newPoint).map(res => Redirect(routes.Application.index()))}
      )
  }

  def deletePoint(id: Long) = Action.async {
    implicit request =>
      PointService.deletePoint(id) map {
        res => Redirect(routes.Application.index())
      }
  }


}