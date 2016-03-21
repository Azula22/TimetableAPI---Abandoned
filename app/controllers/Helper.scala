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
      if (data.first.pair.subject.isDefined) {
        val newPoint = Point(0, data.first.pair.subject.get, day, group, data.first.pair.kind.get, java.sql.Time.valueOf("8:00:00"), data.first.pair.teacher.get, data.first.pair.auditorium.get, pair = true)
        PointService.addPoint(newPoint)
      }

      if (data.first.odd.subject.isDefined) {
        val newPoint = Point(0, data.first.odd.subject.get, day, group, data.first.odd.kind.get, java.sql.Time.valueOf("8:00:00"), data.first.odd.teacher.get, data.first.odd.auditorium.get, pair = false)
        PointService.addPoint(newPoint)
      }
    }

    def addSecond(day: String, data: FormDataOneDay, group: String): Unit = {
      if (data.second.pair.subject.isDefined) {
        val newPoint = Point(0, data.second.pair.subject.get, day, group, data.second.pair.kind.get, java.sql.Time.valueOf("9:35:00"), data.second.pair.teacher.get, data.second.pair.auditorium.get, pair = true)
        PointService.addPoint(newPoint)
      }
      if (data.second.odd.subject.isDefined) {
        val newPoint = Point(0, data.second.odd.subject.get, day, group, data.second.odd.kind.get, java.sql.Time.valueOf("9:35:00"), data.second.odd.teacher.get, data.second.odd.auditorium.get, pair = false)
        PointService.addPoint(newPoint)
      }
    }

    def addThird(day: String, data: FormDataOneDay, group: String): Unit = {
      if (data.third.pair.subject.isDefined) {
        val newPoint = Point(0, data.third.pair.subject.get, day, group, data.third.pair.kind.get, java.sql.Time.valueOf("11:10:00"), data.third.pair.teacher.get, data.third.pair.auditorium.get, pair = true)
        PointService.addPoint(newPoint)
      }
      if (data.third.odd.subject.isDefined) {
        val newPoint = Point(0, data.third.odd.subject.get, day, group, data.third.odd.kind.get, java.sql.Time.valueOf("11:10:00"), data.third.odd.teacher.get, data.third.odd.auditorium.get, pair = false)
        PointService.addPoint(newPoint)
      }
    }

    def addFourth(day: String, data: FormDataOneDay, group: String): Unit = {
      if (data.fourth.pair.subject.isDefined) {
        val newPoint = Point(0, data.fourth.pair.subject.get, day, group, data.fourth.pair.kind.get, java.sql.Time.valueOf("12:50:00"), data.fourth.pair.teacher.get, data.fourth.pair.auditorium.get, pair = true)
        PointService.addPoint(newPoint)
      }
      if (data.fourth.odd.subject.isDefined) {
        val newPoint = Point(0, data.fourth.odd.subject.get, day, group, data.fourth.odd.kind.get, java.sql.Time.valueOf("12:50:00"), data.fourth.odd.teacher.get, data.fourth.odd.auditorium.get, pair = false)
        PointService.addPoint(newPoint)
      }
    }

    def addFifth(day: String, data: FormDataOneDay, group: String): Unit = {
      if (data.fifth.pair.subject.isDefined) {
        val newPoint = Point(0, data.fifth.pair.subject.get, day, group, data.fifth.pair.kind.get, java.sql.Time.valueOf("14:25:00"), data.fifth.pair.teacher.get, data.fifth.pair.auditorium.get, pair = true)
        PointService.addPoint(newPoint)
      }
      if (data.fifth.odd.subject.isDefined) {
        val newPoint = Point(0, data.fifth.pair.subject.get, day, group, data.fifth.pair.kind.get, java.sql.Time.valueOf("14:25:00"), data.fifth.pair.teacher.get, data.fifth.pair.auditorium.get, pair = false)
        PointService.addPoint(newPoint)
      }
    }

    def addSixth(day: String, data: FormDataOneDay, group: String): Unit = {
      if (data.sixth.pair.subject.isDefined) {
        val newPoint = Point(0, data.sixth.pair.subject.get, day, group, data.sixth.pair.kind.get, java.sql.Time.valueOf("16:00:00"), data.sixth.pair.teacher.get, data.sixth.pair.auditorium.get, pair = true)
        PointService.addPoint(newPoint)
      }
      if (data.sixth.odd.subject.isDefined) {
        val newPoint = Point(0, data.sixth.odd.subject.get, day, group, data.sixth.odd.kind.get, java.sql.Time.valueOf("16:00:00"), data.sixth.odd.teacher.get, data.sixth.odd.auditorium.get, pair = false)
        PointService.addPoint(newPoint)
      }
    }
  }

}
