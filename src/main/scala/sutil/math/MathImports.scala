package sutil.math

trait MathImports {

  implicit def integralCanBeDigits[N: Integral](n: N) = new CanBeDigits(n)

  implicit def digitCanBeNumeric[N: Numeric](digit: Digit): N = implicitly[Numeric[N]].fromInt(digit.value)

  implicit def digitCanBeInt(digit: Digit): Int = digit.value

  def powers(base: BigInt, from: Int = 0, step: Int = 1): Stream[BigInt] =
    Stream.cons(base.pow(from), powers(base, from + step, step))

  def sequence(from: BigInt, step: BigInt = 1): Stream[BigInt] =
    Stream.cons(from, sequence(from + step, step))

}
