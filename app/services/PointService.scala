package services

import models.{Points, Point}

import scala.concurrent.Future


object PointService {

  def addPoint(point: Point): Future[String] = {
    Points.add(point)
  }

  def deletePoint(id: Long): Future[Int] = {
    Points.delete(id)
  }

  def getPoint(id: Long): Future[Option[Point]] = {
    Points.get(id)
  }

  def listAllPoints: Future[Seq[Point]] = {
    Points.listAll
  }
}
