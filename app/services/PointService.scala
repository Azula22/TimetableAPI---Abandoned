package services

import java.sql.Time

import models.{Points, Point}

import scala.concurrent.Future


object PointService {
  def alterPoint(point: Point, name: String, kind: String, teacher: String, auditorium: Int): Future[String] = {
    Points.alterPoint(point, name, kind, teacher, auditorium)
  }


  def addPoint(point: Point): Future[String] = {
    Points.add(point)
  }

  def deletePoint(id: Long): Future[Int] = {
    Points.delete(id)
  }

  def get(id: Long): Future[Option[Point]] = {
    Points.get(id)
  }

  def listAllPoints: Future[Seq[Point]] = {
    Points.listAll
  }

  def group(group: String): Future[Seq[Point]] = {
    Points.getGroup(group)
  }

  def teacher(teacher: String): Future[Seq[Point]] = {
    Points.getTeacher(teacher)
  }

  def checkExistance(groupName: String, day: String, time: Time, pair: Boolean): Future[Option[Point]] = {
    Points.checkExistance(groupName, day, time, pair)
  }
}
