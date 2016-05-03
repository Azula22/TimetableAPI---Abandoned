package controllers

import models._
import play.api.mvc._
import services.{GroupService, SubjectService}
import controllers.DataHelper._
import forms.{FormAllDays, FormData, FormDataAllDays, SubjectForm}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class SubjectController extends Controller {


  def startPage = Action.async {
    implicit request =>
      SubjectService.getAllSubjects map {
        points => Ok(views.html.index(points, days,
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
      SubjectService.getAllSubjects.map(res => Redirect(routes.SubjectController.startPage()))
  }

  def getSubjectByDayTimeAndOddNot(d: String, t: String, oN: String, data: FormDataAllDays): FormData = {
    data.getDay(d).get.getPair(t).get.getPairOrOdd(oN).get
  }

  def idSubjectDefined(d: String, t: String, oN: String, data: FormDataAllDays): Boolean = {
    getSubjectByDayTimeAndOddNot(d, t, oN, data).subject.isDefined
  }

  def addThisSubject(data: FormDataAllDays, d: String, t: String, oN: String) = {
    val dataFromForm = getSubjectByDayTimeAndOddNot(d, t, oN, data)
    SubjectService.getOptionSubjectIfExists(data.groupName, d, java.sql.Time.valueOf(t + ":00"), reverseOddNotToBoolean(oN)).map(p =>
      SubjectService.alterSubject(p.get, dataFromForm.subject.get, dataFromForm.kind.getOrElse("-"), dataFromForm.teacher.orNull,
        dataFromForm.auditorium.getOrElse(0))).recover {
      case _ => addSubject(data, d, t, oN)
    }
  }

  def addSubject(data: FormDataAllDays, day: String, time: String, oDD: String) = {
    val myLesson = getSubjectByDayTimeAndOddNot(day, time, oDD, data)

    for {
      g <- GroupService.getGroupIDByName(data.groupName)
      f <- GroupService.getFacultyByGroupName(data.groupName)
    } yield {
      val newSubject = Subject(0, myLesson.subject.getOrElse("-"), g.getOrElse(-1),
        myLesson.teacher.getOrElse("-"), f.getOrElse("_"), day, java.sql.Time.valueOf(time + ":00"),
        reverseOddNotToBoolean(oDD), myLesson.kind.getOrElse("-"), myLesson.auditorium.getOrElse(0))
      SubjectService.addSubject(newSubject)
    }
  }

  def reverseOddNotToBoolean(oddNot: String): Boolean = {
    oddNot == "pair"
  }

  def deleteSubject(id: Long) = Action.async {
    implicit request =>
      SubjectService.deleteSubjectByID(id) map {
        res => Redirect(routes.SubjectController.startPage())
      }
  }

}