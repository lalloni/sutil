package sutil.control

class RichNumeric[N: Numeric](n: N) {
  def ifZero(alt: ⇒ N): N = if (implicitly[Numeric[N]].zero == n) alt else n
}
