package sutil.math

import scala.math.{ScalaNumericConversions, ScalaNumber}

import Stream._

abstract sealed class Digit(val value: Int)
  extends ScalaNumber
          with ScalaNumericConversions
          with Ordered[Digit] {

  assert(0 <= value && value <= 9, "A digit must be greater than or equal to 0 and lesser than or equal to 9")

  def intValue: Int = value
  def longValue: Long = value
  def floatValue: Float = value
  def doubleValue: Double = value
  def underlying = value.asInstanceOf[AnyRef]
  def isWhole = true
  def compare(that: Digit) = value.compare(that.value)
  override def toString = value.toString

}

object Digit0 extends Digit(0)
object Digit1 extends Digit(1)
object Digit2 extends Digit(2)
object Digit3 extends Digit(3)
object Digit4 extends Digit(4)
object Digit5 extends Digit(5)
object Digit6 extends Digit(6)
object Digit7 extends Digit(7)
object Digit8 extends Digit(8)
object Digit9 extends Digit(9)

object Digit {

  def apply[T: Numeric](value: T): Digit =
    value match {
      case 0 ⇒ Digit0
      case 1 ⇒ Digit1
      case 2 ⇒ Digit2
      case 3 ⇒ Digit3
      case 4 ⇒ Digit4
      case 5 ⇒ Digit5
      case 6 ⇒ Digit6
      case 7 ⇒ Digit7
      case 8 ⇒ Digit8
      case 9 ⇒ Digit9
    }
  
}
