package services

import models.{Group, Groups}

import scala.concurrent.Future

object GroupService {
  def addGroup(group: Group): Future[String] = Groups.add(group)

  def delete(id: Long): Future[Int] = Groups.delete(id)

  def getGroupById(id: Long): Future[Option[Group]] = Groups.get(id)

  def getGroupIDByName(name: String): Future[Option[Long]] = Groups.getGroupByName(name)

  def listAllGroups: Future[Seq[Group]] = Groups.listAll

  def getFacultyByGroupName(name: String): Future[Option[String]] = Groups.getFacultyByGroupName(name)
}
