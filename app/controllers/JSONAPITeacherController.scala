package controllers

import models.Subject
import play.api.libs.json.{JsValue, Json, Writes}
import play.api.mvc._
import services.PointService

import scala.concurrent.ExecutionContext.Implicits.global


class JSONAPITeacherController extends Controller{

  implicit val pointWrite = new Writes[Subject] {
    override def writes(subject: Subject) = Json.obj(
      "group" -> subject.groupID,
      "subj" -> subject.name,
      "type" -> subject.kind,
      "start" -> subject.start,
      "auditorium" -> subject.auditorium
    )
  }

  implicit val seqPointWrite = new Writes[Seq[Subject]] {
    override def writes(subjects: Seq[Subject]) = Json.obj(
      "status" -> 0,
      "data" -> Json.obj(
        "teacher" -> subjects.head.teacherID,
        "days" -> sortByDays(subjects)
      ))
  }

  def sortByDays(points: Seq[Subject]): JsValue = {
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

  def getTeacher(teacherId: Long) = Action.async{
    implicit request=>
      PointService.teacher(teacherId).map(
        res =>res.headOption match {
          case Some(v) => Ok(Json.prettyPrint(Json.toJson(res)))
          case None => Ok(Json.prettyPrint(Json.obj("status" -> 1, "data" -> "Teacher doesn't exist")))
        }
      )
  }
}
