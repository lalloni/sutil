package sutil.version

case class VersionNumber(numbers: Int*) extends Ordered[VersionNumber] {

  require (!numbers.isEmpty, "Numbers must not be empty.")
  require (!numbers.exists(_ < 0), "Numbers must be positive or zero.")

  lazy val majorNumber: Int = numbers(0) // size don't checked because at least one number required above 
  lazy val minorNumber: Int = if (numbers.size > 1) numbers(1) else 0
  lazy val fixNumber: Int = if (numbers.size > 2) numbers(2) else 0

  def normalNumber = (majorNumber, minorNumber, fixNumber)

  import VersionNumber._

  def compare(other: VersionNumber): Int = compareSeqs(numbers, other.numbers, 0)

  override lazy val toString = numbers mkString "."

  def to(version: VersionNumber): VersionNumberRange = VersionNumberRange.Inclusive(this, version)
  def until(version: VersionNumber): VersionNumberRange = VersionNumberRange.Exclusive(this, version)

  def size: Int = numbers.size

  def incrementAt(position: Int, increment: Int): VersionNumber = {
    require (position >= 0 && position <= size, "Position must be between 1 and version number size.")
    new VersionNumber((numbers.take(position) :+ (numbers(position) + 1)) ++ numbers.drop(position + increment): _*)
  }

  def incrementBy(version: VersionNumber): VersionNumber =
    new VersionNumber(this.numbers zipAll (version.numbers, 0, 0) map adder: _*)

  def incrementAt(position: Int): VersionNumber =
    incrementAt(position, 1)

  def incrementAt(position: VersionNumberPosition.Value, increment: Int = 1): VersionNumber =
    incrementAt(position.id, increment)

  def incrementLast(increment: Int = 1): VersionNumber =
    incrementAt(size - 1, increment)

  def incrementLast: VersionNumber =
    incrementLast(1)

}

object VersionNumber {

  val Zero = VersionNumber(0)
  val One = VersionNumber(1)

  def apply(number: String): VersionNumber =
    VersionParsers.number(number)

}
