package sutil.version
import scala.annotation.tailrec

case class VersionNumber(numbers: Int*) extends Ordered[VersionNumber] {

  require (!numbers.isEmpty, "Numbers must not be empty.")
  require (!numbers.exists(_ < 0), "Numbers must be positive or zero.")

  def majorNumber = numbers(0) // size don't checked because at least one number required above 
  def minorNumber = if (numbers.size > 1) numbers(1) else 0
  def fixNumber = if (numbers.size > 2) numbers(2)

  def normalNumber = (majorNumber, minorNumber, fixNumber)

  import VersionNumber._

  def compare(other: VersionNumber): Int =
    (numbers.view zipAll (other.numbers, 0, 0)) map compared find not0 getOrElse 0

  override lazy val toString = numbers mkString "."

  def to(version: VersionNumber): VersionRange = VersionRange.Inclusive(this, version)
  def until(version: VersionNumber): VersionRange = VersionRange.Exclusive(this, version)

  def size: Int = numbers.size

  def incrementAt(position: Int, increment: Int): VersionNumber = {
    require (position >= 0 && position <= size, "Position must be between 1 and version number size.")
    new VersionNumber((numbers.take(position) :+ (numbers(position) + 1)) ++ numbers.drop(position + increment): _*)
  }

  def incrementBy(version: VersionNumber): VersionNumber =
    new VersionNumber(this.numbers zipAll (version.numbers, 0, 0) map added: _*)

  def incrementAt(position: Int): VersionNumber = incrementAt(position, 1)
  def incrementAt(position: VersionNumberPosition.Value, increment: Int = 1): VersionNumber = incrementAt(position.id, increment)

  def incrementLast(increment: Int = 1): VersionNumber = incrementAt(size - 1, increment)
  def incrementLast: VersionNumber = incrementLast(1)

}

object VersionNumber {
  private val added = ((a: Int, b: Int) ⇒ a + b) tupled
  private val compared = ((a: Int, b: Int) ⇒ a compare b) tupled
  private val not0 = (a: Int) ⇒ a != 0
}
