package controllers

import models.PointForm
import play.api.mvc._
import services.PointService
import scala.concurrent.ExecutionContext.Implicits.global

class Application extends Controller {

  def index = Action.async {
    implicit request=>
      PointService.listAllPoints map {
        points=>Ok(views.html.index(PointForm.form, points))
      }
  }

  def addUser = TODO
}