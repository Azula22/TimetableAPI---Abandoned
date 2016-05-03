package services

import models.{Faculties, Faculty}

import scala.concurrent.Future

/**
  * Created by Kushik on 02.05.2016.
  */
object FacultyService {

  def addFaculty(name: String): Future[String] = Faculties.add(Faculty(0, name))

  def deleteFaculty(name: String): Future[Int] = Faculties.delete(name)

  def getAllFaculties: Future[Seq[Faculty]] = Faculties.listAll
}
