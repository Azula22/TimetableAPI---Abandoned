package services

import models.{Teacher, Teachers}

import scala.concurrent.Future

/**
  * Created by Kushik on 02.05.2016.
  */
object TeacherService {
  def addTeacher(name: String): Future[String] = Teachers.add(Teacher(name))

  def getTeacher(name: String): Future[Option[Teacher]] = Teachers.getTeacher(name)

  def deleteTeacher(name: String): Future[Int] = Teachers.delete(name)

  def getAllTeachers: Future[Seq[Teacher]] = Teachers.listAll
}

