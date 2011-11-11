package sutil.control

trait ControlImports {

  implicit def every[T](some: T): Every[T] = new Every[T](some)

  implicit def numerics[T: Numeric](some: T): RichNumeric[T] = new RichNumeric[T](some)

  def closing[T <: { def close() }, R](closeable: T)(action: T ⇒ R): R =
    try action(closeable)
    finally closeable.close

}
