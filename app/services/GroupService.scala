package services

import models.{Groups, Group}

import scala.concurrent.Future

object GroupService {
  def addGroup(group: Group): Future[String] = {
    Groups.add(group)
  }

  def delete(id: Long): Future[Int] = {
    Groups.delete(id)
  }

  def get(id: Long): Future[Option[Group]] = {
    Groups.get(id)
  }
}
