package sutil.control

class Every[T](some: T) {
  def on(f: T ⇒ Unit): T = {
    f apply some
    some
  }
}

class RichNumeric[N: Numeric](n: N) {
  def ifZero(alt: ⇒ N): N = if (implicitly[Numeric[N]].zero == n) alt else n
}

object Control extends ControlImports

trait ControlImports {

  implicit def every[T](some: T): Every[T] = new Every[T](some)

  implicit def numerics[T: Numeric](some: T): RichNumeric[T] = new RichNumeric[T](some)

  def closing[T <: { def close() }, R](closeable: T)(action: T ⇒ R): R =
    try action(closeable)
    finally closeable.close

}
