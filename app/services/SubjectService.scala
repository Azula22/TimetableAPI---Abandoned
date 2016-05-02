package services

import java.sql.Time

import models.{Subjects, Subject}

import scala.concurrent.Future


object SubjectService {
  def alterSubject(subject: Subject, name: String, kind: String, teacher: String, auditorium: Int): Future[String] =
    Subjects.updateSubject(subject, name, kind, teacher, auditorium)

  def addSubject(subject: Subject): Future[String] = Subjects.add(subject)

  def deleteSubjectByID(id: Long): Future[Int] = Subjects.delete(id)

  def getSubjectByID(id: Long): Future[Option[Subject]] = Subjects.get(id)

  def getAllSubjects: Future[Seq[Subject]] = Subjects.listAll

  def getGroupByName(name: String): Future[Seq[Subject]] = Subjects.getGroup(name)

  def teacherByID(teacher: String): Future[Seq[Subject]] = Subjects.getTeacher(teacher)

  def getOptionSubjectIfExists(groupName: String, day: String, time: Time, pair: Boolean): Future[Option[Subject]] =
    Subjects.checkExistance(groupName, day, time, pair)

}
