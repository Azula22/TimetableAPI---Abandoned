package controllers

import models._
import play.api.mvc._
import services.SubjectService
import controllers.DataHelper._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class SubjectController extends Controller {


  def index = Action.async {
    implicit request =>
      SubjectService.getAllSubjects map {
        points => Ok(views.html.index(SubjectForm.form, FormAllDays.form, points, days,
          timesForDisplaying, oddNot, timesForUsing))
      }
  }

  def addSubjects() = Action.async {
    implicit request =>
      FormAllDays.form.bindFromRequest.fold(
        errorForm => Future.successful(BadRequest(views.html.bad())),
        data => {

          //Cycles for getting to each point separately
          for (d <- days) {
            for (t <- timesForDisplaying) {
              for (oN <- oddNot) {

                if (idSubjectDefined(d, t, oN, data)) {
                  addThisSubject(data, d, t, oN)
                }
              }
            }
          }
        }
      )
      SubjectService.getAllSubjects.map(res => Redirect(routes.SubjectController.index()))
  }

  def getSubjectByDayTimeAndOddNot(d: String, t: String, oN: String, data: FormDataAllDays): FormData = {
    data.getDay(d).get.getPair(t).get.getPairOrOdd(oN).get
  }

  def idSubjectDefined(d: String, t: String, oN: String, data: FormDataAllDays): Boolean = {
    if (getSubjectByDayTimeAndOddNot(d, t, oN, data).subject.isDefined) true else false
  }

  def addThisSubject(data: FormDataAllDays, d: String, t: String, oN: String) = {
    val myLesson = getSubjectByDayTimeAndOddNot(d, t, oN, data)
    SubjectService.getOptionSubjectIfExists(data.groupName, d, java.sql.Time.valueOf(t + ":00"), reverseOddNotToBoolean(oN)).map(p =>
      SubjectService.alterSubject(p.get, myLesson.subject.get, myLesson.kind.getOrElse("-"), myLesson.teacher.getOrElse(),
        myLesson.auditorium.getOrElse(0))).recover {
      case _ => addSubject(data, d, t, oN)
    }
  }

  def addSubject(data: FormDataAllDays, d: String, t: String, oN: String): Unit = {
    val myLesson = getSubjectByDayTimeAndOddNot(d, t, oN, data)
    val newPoint = Subject(0, myLesson.subject.getOrElse("-"), d, data.groupName, myLesson.kind.getOrElse("-"),
      java.sql.Time.valueOf(t + ":00"), myLesson.teacher.getOrElse("-"), myLesson.auditorium.getOrElse(0), reverseOddNotToBoolean(oN))
    SubjectService.addSubject(newPoint)
  }

  def reverseOddNotToBoolean(oddNot: String): Boolean = {
    oddNot == "pair"
  }

  def deleteSubject(id: Long) = Action.async {
    implicit request =>
      SubjectService.deleteSubjectByID(id) map {
        res => Redirect(routes.SubjectController.index())
      }
  }

}