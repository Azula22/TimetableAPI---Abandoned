package controllers

import models.{Point, PointForm}
import play.api.libs.json.{JsValue, Json, Writes}
import play.api.mvc._
import services.PointService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class Application extends Controller {

  val days: Seq[String] = Seq("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

  implicit val pointWrite = new Writes[Point] {
    override def writes(point: Point) = Json.obj(
      "subj" -> point.subject,
      "type" -> point.kind,
      "start" -> point.start,
      "end" -> point.ending,
      "teacher" -> point.teacher,
      "auditorium" -> point.auditorium
    )
  }

  implicit val seqPointWrite = new Writes[Seq[Point]] {
    override def writes(points: Seq[Point]) = Json.obj(
      "status" -> 0,
      "data" -> Json.obj(
        "group" -> points.head.groupName,
        "days"->sortByDays(points)
        ))
  }

  def sortByDays(points: Seq[Point]): JsValue = {
    val setDays = (for {
      p <- points
      if days.contains(p.day)
    } yield p.day).toSet

    println(setDays)

    Json.obj("result" -> setDays.map(
      myDay => Json.obj(
        "day"->myDay,
        "subjects"->points.filter(_.day==myDay).map(point=>
        Json.arr(Json.toJson(point)))
      )
    ))
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

  def getGroupJson(group: String) = Action.async {
    implicit request =>
      PointService.group(group).map(
        res => res.headOption match {
          case Some(v) => Ok(Json.prettyPrint(Json.toJson(res)))
          case None => Ok(Json.prettyPrint(Json.obj("status" -> 1, "data" -> "Group doesn't exist")))
        }
      )
  }
}