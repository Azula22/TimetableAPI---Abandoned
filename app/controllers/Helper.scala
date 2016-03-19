package controllers

import models.{Point, FormDataOneDay, FormDataAllDays}
import services.PointService


object Helper {

  object AddSubjectAllDays {
    def addMonday(data: FormDataAllDays): Unit = {
      AddSubjectOneDay.addFirst("Monday", data.mon, data.groupName)
      AddSubjectOneDay.addSecond("Monday", data.mon, data.groupName)
      AddSubjectOneDay.addThird("Monday", data.mon, data.groupName)
      AddSubjectOneDay.addFourth("Monday", data.mon, data.groupName)
      AddSubjectOneDay.addFifth("Monday", data.mon, data.groupName)
      AddSubjectOneDay.addSixth("Monday", data.mon, data.groupName)
    }

    def addTuesday(data: FormDataAllDays): Unit = {
      AddSubjectOneDay.addFirst("Tuesday", data.tue, data.groupName)
      AddSubjectOneDay.addSecond("Tuesday", data.tue, data.groupName)
      AddSubjectOneDay.addThird("Tuesday", data.tue, data.groupName)
      AddSubjectOneDay.addFourth("Tuesday", data.tue, data.groupName)
      AddSubjectOneDay.addFifth("Tuesday", data.tue, data.groupName)
      AddSubjectOneDay.addSixth("Tuesday", data.tue, data.groupName)
    }

    def addWednesday(data: FormDataAllDays): Unit = {
      AddSubjectOneDay.addFirst("Wednesday", data.wed, data.groupName)
      AddSubjectOneDay.addSecond("Wednesday", data.wed, data.groupName)
      AddSubjectOneDay.addThird("Wednesday", data.wed, data.groupName)
      AddSubjectOneDay.addFourth("Wednesday", data.wed, data.groupName)
      AddSubjectOneDay.addFifth("Wednesday", data.wed, data.groupName)
      AddSubjectOneDay.addSixth("Wednesday", data.wed, data.groupName)
    }

    def addThursday(data: FormDataAllDays): Unit = {
      AddSubjectOneDay.addFirst("Thursday", data.thur, data.groupName)
      AddSubjectOneDay.addSecond("Thursday", data.thur, data.groupName)
      AddSubjectOneDay.addThird("Thursday", data.thur, data.groupName)
      AddSubjectOneDay.addFourth("Thursday", data.thur, data.groupName)
      AddSubjectOneDay.addFifth("Thursday", data.thur, data.groupName)
      AddSubjectOneDay.addSixth("Thursday", data.thur, data.groupName)
    }

    def addFriday(data: FormDataAllDays): Unit = {
      AddSubjectOneDay.addFirst("Friday", data.fri, data.groupName)
      AddSubjectOneDay.addSecond("Friday", data.fri, data.groupName)
      AddSubjectOneDay.addThird("Friday", data.fri, data.groupName)
      AddSubjectOneDay.addFourth("Friday", data.fri, data.groupName)
      AddSubjectOneDay.addFifth("Friday", data.fri, data.groupName)
      AddSubjectOneDay.addSixth("Friday", data.fri, data.groupName)
    }

    def addSaturday(data: FormDataAllDays): Unit = {
      AddSubjectOneDay.addFirst("Saturday", data.sat, data.groupName)
      AddSubjectOneDay.addSecond("Saturday", data.sat, data.groupName)
      AddSubjectOneDay.addThird("Saturday", data.sat, data.groupName)
      AddSubjectOneDay.addFourth("Saturday", data.sat, data.groupName)
      AddSubjectOneDay.addFifth("Saturday", data.sat, data.groupName)
      AddSubjectOneDay.addSixth("Saturday", data.sat, data.groupName)
    }

    def addSunday(data: FormDataAllDays): Unit = {
      AddSubjectOneDay.addFirst("Sunday", data.sund, data.groupName)
      AddSubjectOneDay.addSecond("Sunday", data.sund, data.groupName)
      AddSubjectOneDay.addThird("Sunday", data.sund, data.groupName)
      AddSubjectOneDay.addFourth("Sunday", data.sund, data.groupName)
      AddSubjectOneDay.addFifth("Sunday", data.sund, data.groupName)
      AddSubjectOneDay.addSixth("Sunday", data.sund, data.groupName)
    }
  }

  object AddSubjectOneDay {
    def addFirst(day: String, data: FormDataOneDay, group: String): Unit = {
      if (data.first.subject.isDefined) {
        val newPoint = Point(0, data.first.subject.get, day, group, data.first.kind.get, java.sql.Time.valueOf("8:00:00"), data.first.teacher.get, data.first.auditorium.get)
      PointService.addPoint(newPoint)
    }}

    def addSecond(day: String, data: FormDataOneDay, group: String): Unit = {
      if (data.second.subject.isDefined) {
      val newPoint = Point(0, data.second.subject.get, day, group, data.second.kind.get, java.sql.Time.valueOf("9:35:00"), data.second.teacher.get, data.second.auditorium.get)
      PointService.addPoint(newPoint)
    }}

    def addThird(day: String, data: FormDataOneDay, group: String): Unit = {
      if (data.third.subject.isDefined) {
        val newPoint = Point(0, data.third.subject.get, day, group, data.third.kind.get, java.sql.Time.valueOf("11:10:00"), data.third.teacher.get, data.third.auditorium.get)
       PointService.addPoint(newPoint)
    }}

    def addFourth(day: String, data: FormDataOneDay, group: String): Unit = {
      if (data.fourth.subject.isDefined) {
        val newPoint = Point(0, data.fourth.subject.get, day, group, data.fourth.kind.get, java.sql.Time.valueOf("12:50:00"), data.fourth.teacher.get, data.fourth.auditorium.get)
      PointService.addPoint(newPoint)
    }}

    def addFifth(day: String, data: FormDataOneDay, group: String): Unit = {
      if (data.fifth.subject.isDefined) {
        val newPoint = Point(0, data.fifth.subject.get, day, group, data.fifth.kind.get, java.sql.Time.valueOf("14:25:00"), data.fifth.teacher.get, data.fifth.auditorium.get)
        PointService.addPoint(newPoint)
    }}

    def addSixth(day: String, data: FormDataOneDay, group: String): Unit = {
      if (data.sixth.subject.isDefined) {
        val newPoint = Point(0, data.sixth.subject.get, day, group, data.sixth.kind.get, java.sql.Time.valueOf("16:00:00"), data.sixth.teacher.get, data.sixth.auditorium.get)
        PointService.addPoint(newPoint)
    }}
  }

}
