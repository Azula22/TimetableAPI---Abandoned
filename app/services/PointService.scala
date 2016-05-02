package services

import java.sql.Time

import models.{Points, Subject}

import scala.concurrent.Future


object PointService {
  def alterPoint(point: Subject, name: String, kind: String, teacherID: Long, auditorium: Int): Future[String] = {
    Points.updateSubject(point, name, kind, teacherID, auditorium)
  }


  def addPoint(point: Subject): Future[String] = {
    Points.add(point)
  }

  def deletePoint(id: Long): Future[Int] = {
    Points.delete(id)
  }

  def get(id: Long): Future[Option[Subject]] = {
    Points.get(id)
  }

  def listAllPoints: Future[Seq[Subject]] = {
    Points.listAll
  }

  def group(group: String): Future[Seq[Subject]] = {
    Points.getGroup(group)
  }

  def teacher(teacherID: Long): Future[Seq[Subject]] = {
    Points.getTeacher(teacherID)
  }

  def checkExistance(groupName: String, day: String, time: Time, pair: Boolean): Future[Option[Subject]] = {
    Points.checkExistance(groupName, day, time, pair)
  }
}
