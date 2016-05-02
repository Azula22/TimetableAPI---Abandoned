package services

import java.sql.Time

import models.{Points, Subject}

import scala.concurrent.Future


object SubjectService {
  def alterSubject(subject: Subject, name: String, kind: String, teacherID: Long, auditorium: Int): Future[String] =
    Points.updateSubject(subject, name, kind, teacherID, auditorium)

  def addSubject(subject: Subject): Future[String] = Points.add(subject)

  def deleteSubjectByID(id: Long): Future[Int] = Points.delete(id)

  def getSubjectByID(id: Long): Future[Option[Subject]] = Points.get(id)

  def getAllSubjects: Future[Seq[Subject]] = Points.listAll

  def getGroupByName(name: String): Future[Seq[Subject]] = Points.getGroup(name)

  def teacherByID(teacherID: Long): Future[Seq[Subject]] = Points.getTeacherByID(teacherID)

  def getOptionSubjectIfExists(groupName: String, day: String, time: Time, pair: Boolean): Future[Option[Subject]] =
    Points.checkExistance(groupName, day, time, pair)

}
