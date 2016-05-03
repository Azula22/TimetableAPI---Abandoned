package forms

import play.api.data.Form
import play.api.data.Forms._


case class FormData(subject: Option[String], kind: Option[String],
                    teacher: Option[String], auditorium: Option[Int])


case class FormDataAllDays(groupName: String,
                           mon: FormDataOneDay,
                           tue: FormDataOneDay,
                           wed: FormDataOneDay,
                           thur: FormDataOneDay,
                           fri: FormDataOneDay,
                           sat: FormDataOneDay,
                           sund: FormDataOneDay) {
  def getDay(day: String): Option[FormDataOneDay] = {
    day match {
      case "Monday" => Some(this.mon)
      case "Tuesday" => Some(this.tue)
      case "Wednesday" => Some(this.wed)
      case "Thursday" => Some(this.thur)
      case "Friday" => Some(this.fri)
      case "Saturday" => Some(this.sat)
      case "Sunday" => Some(this.sund)
      case _ => None
    }
  }
}

case class FormDataOneDay(first: PairOddData,
                          second: PairOddData,
                          third: PairOddData,
                          fourth: PairOddData,
                          fifth: PairOddData,
                          sixth: PairOddData) {
  def getPair(pair: String): Option[PairOddData] = {
    pair match {
      case "8:00" => Some(this.first)
      case "9:35" => Some(this.second)
      case "11:10" => Some(this.third)
      case "12:50" => Some(this.fourth)
      case "14:25" => Some(this.fifth)
      case "16:00" => Some(this.sixth)
      case _ => None
    }
  }
}

case class PairOddData(pair: FormData, odd: FormData) {
  def getPairOrOdd(maVal: String): Option[FormData] = {
    maVal match {
      case "odd" => Some(this.odd)
      case "pair" => Some(this.pair)
      case _ => None
    }
  }
}


object FormAllDays {
  val form = Form(
    mapping(
      "groupName" -> nonEmptyText,
      "Monday" -> FormOneDay.form.mapping,
      "Tuesday" -> FormOneDay.form.mapping,
      "Wednesday" -> FormOneDay.form.mapping,
      "Thursday" -> FormOneDay.form.mapping,
      "Friday" -> FormOneDay.form.mapping,
      "Saturday" -> FormOneDay.form.mapping,
      "Sunday" -> FormOneDay.form.mapping
    )(FormDataAllDays.apply)(FormDataAllDays.unapply)
  )
}

object FormOneDay {
  val form = Form(
    mapping(
      "first" -> PairOddForm.form.mapping,
      "second" -> PairOddForm.form.mapping,
      "third" -> PairOddForm.form.mapping,
      "fourth" -> PairOddForm.form.mapping,
      "fifth" -> PairOddForm.form.mapping,
      "sixth" -> PairOddForm.form.mapping
    )(FormDataOneDay.apply)(FormDataOneDay.unapply)
  )
}

object SubjectForm {
  val form = Form(
    mapping(
      "subject" -> optional(text),
      "kind" -> optional(text),
      "teacher" -> optional(text),
      "auditorium" -> optional(number)
    )(FormData.apply)(FormData.unapply)
  )
}

object PairOddForm {
  val form = Form(
    mapping(
      "pair" -> SubjectForm.form.mapping,
      "odd" -> SubjectForm.form.mapping
    )(PairOddData.apply)(PairOddData.unapply)
  )
}