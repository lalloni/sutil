package sutil.joda.time

import org.joda.time.base.AbstractPartial
import org.joda.time.chrono.ISOChronology
import org.joda.time.{ Partial, DateTimeFieldType, DateTimeField, Chronology }

case class Month(monthOfYear: Int, chronology: Chronology) extends AbstractPartial {

  def this(monthOfYear: Int) = this(monthOfYear, ISOChronology.getInstanceUTC)

  private val partial = new Partial(DateTimeFieldType.monthOfYear, monthOfYear, chronology)

  val size = partial.size

  def getField(field: Int, chrono: Chronology): DateTimeField = chrono.monthOfYear

  def getChronology: Chronology = partial.getChronology

  def getValue(field: Int) = partial.getValue(field)

  def getMonthOfYear = partial.get(DateTimeFieldType.monthOfYear)

  override def toString = monthOfYear.toString

}

object Month {
  def apply(monthOfYear: Int) = new Month(monthOfYear)
}
