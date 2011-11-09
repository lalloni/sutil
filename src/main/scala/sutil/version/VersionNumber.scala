package sutil.version
import scala.annotation.tailrec

case class VersionNumber(numbers: Int*) extends Ordered[VersionNumber] {

  require (!numbers.isEmpty, "Numbers must not be empty.")
  require (!numbers.exists(_ < 0), "Numbers must be positive or zero.")

  def compare(other: VersionNumber): Int =
    (numbers.view zipAll (other.numbers, 0, 0)) map { case (a, b) ⇒ a compare b } find (_ != 0) getOrElse 0

  override lazy val toString = numbers mkString "."

  def to(version: VersionNumber): VersionRange = VersionRange.Inclusive(this, version)
  def until(version: VersionNumber): VersionRange = VersionRange.Exclusive(this, version)

  def size: Int = numbers.size

  def incrementAt(position: Int, increment: Int): VersionNumber = {
    require (position >= 0 && position <= size, "Position must be between 1 and version number size.")
    new VersionNumber((numbers.take(position) :+ (numbers(position) + 1)) ++ numbers.drop(position + increment): _*)
  }

  def incrementBy(version: VersionNumber): VersionNumber =
    new VersionNumber(this.numbers zipAll (version.numbers, 0, 0) map { case (a, b) ⇒ a + b }: _*)

  def incrementAt(position: Int): VersionNumber = incrementAt(position, 1)
  def incrementAt(position: VersionNumberPosition.Value, increment: Int = 1): VersionNumber = incrementAt(position.id, increment)
  def incrementLast(increment: Int = 1): VersionNumber = incrementAt(size - 1, increment)
  def incrementLast: VersionNumber = incrementLast(1)
  
}

object VersionNumberPosition extends Enumeration {

  type VersionNumberPosition = Value

  val Major, Minor, Fix = Value

}

sealed abstract class VersionRange {
  val start: VersionNumber
  val end: VersionNumber
  def contains(version: VersionNumber): Boolean
}

object VersionRange {

  case class Inclusive(start: VersionNumber, end: VersionNumber) extends VersionRange {
    def contains(version: VersionNumber) =
      start <= version && version <= end
  }

  case class Exclusive(start: VersionNumber, end: VersionNumber) extends VersionRange {
    def contains(version: VersionNumber) =
      start <= version && version < end
  }

}
