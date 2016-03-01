package controllers

import models.Point
import play.api.libs.json.{JsValue, Json, Writes}
import play.api.mvc._
import services.PointService

import scala.concurrent.ExecutionContext.Implicits.global

class JSONAPIGroupController extends Controller{

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
        "days" -> sortByDays(points)
      ))
  }

  def sortByDays(points: Seq[Point]): JsValue = {
    val setDays = (for {
      p <- points
    } yield p.day).toSet

    Json.obj("result" -> setDays.map(
      myDay => Json.obj(
        "day" -> myDay,
        "subjects" -> points.filter(_.day == myDay).map(point =>
          Json.arr(Json.toJson(point)))
      )
    ))
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
