package controllers

import models.Subject
import play.api.libs.json.{JsValue, Json, Writes}
import play.api.mvc._
import services.SubjectService

import scala.concurrent.ExecutionContext.Implicits.global

class JSONAPIGroupController extends Controller{

  implicit val pointWrite = new Writes[Subject] {
    override def writes(subject: Subject) = Json.obj(
      "subj" -> subject.name,
      "type" -> subject.kind,
      "start" -> subject.start,
      "teacher" -> subject.teacherID,
      "auditorium" -> subject.auditorium
    )
  }

  implicit val seqPointWrite = new Writes[Seq[Subject]] {
    override def writes(points: Seq[Subject]) = Json.obj(
      "status" -> 0,
      "data" -> Json.obj(
        "group" -> points.head.groupID,
        "days" -> sortByDays(points)
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

  def getGroupJson(group: String) = Action.async {
    implicit request =>
      SubjectService.getGroupByName(group).map(
        res => res.headOption match {
          case Some(v) => Ok(Json.prettyPrint(Json.toJson(res)))
          case None => Ok(Json.prettyPrint(Json.obj("status" -> 1, "data" -> "Group doesn't exist")))
        }
      )
  }
}
