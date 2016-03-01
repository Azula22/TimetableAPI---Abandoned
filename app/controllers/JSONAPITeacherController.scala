package controllers

import models.Point
import play.api.libs.json.{JsValue, Json, Writes}
import play.api.mvc._
import services.PointService

import scala.concurrent.ExecutionContext.Implicits.global


class JSONAPITeacherController extends Controller{

  implicit val pointWrite = new Writes[Point] {
    override def writes(point: Point) = Json.obj(
      "group" -> point.groupName,
      "subj" -> point.subject,
      "type" -> point.kind,
      "start" -> point.start,
      "end" -> point.ending,
      "auditorium" -> point.auditorium
    )
  }

  implicit val seqPointWrite = new Writes[Seq[Point]] {
    override def writes(points: Seq[Point]) = Json.obj(
      "status" -> 0,
      "data" -> Json.obj(
        "teacher" -> points.head.teacher,
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

  def getTeacher(teacher: String) = Action.async{
    implicit request=>
      PointService.teacher(teacher).map(
        res =>res.headOption match {
          case Some(v) => Ok(Json.prettyPrint(Json.toJson(res)))
          case None => Ok(Json.prettyPrint(Json.obj("status" -> 1, "data" -> "Teacher doesn't exist")))
        }
      )
  }

}
