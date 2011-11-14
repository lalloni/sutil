package sutil.math

class CanBeDigits[N: Integral](n: N) {

  private lazy val integral = implicitly[Integral[N]]

  import integral._

  lazy val ten = fromInt(10)
  lazy val zero = fromInt(0)

  lazy val unitsDigit: Digit = digits(0)
  lazy val tensDigit: Digit = digits(1)
  lazy val hundredsDigit: Digit = digits(2)
  lazy val thousandsDigit: Digit = digits(3)
  lazy val millionsDigit: Digit = digits(6)
  lazy val trillionsDigit: Digit = digits(9)

  /** Returns the sequence of digits ordered from least significative to most significative (units first).
    * {{{
    * 123.digits == Seq(Digit(3), Digit(2), Digit(1))
    * }}}
    */
  lazy val digits: Seq[Digit] = {
    def digits(n: N): Stream[Digit] =
      Stream.cons(Digit(n % ten), if (n < ten) Stream.empty else digits(n / ten))
    digits(n)
  }

  def padLeft(size: Int): Seq[Digit] =
    digits padTo (size, Digit0) reverse

  def padRight(size: Int): Seq[Digit] =
    (digits reverse) padTo (size, Digit0)

}
