package sutil.control

class Every[T](some: T) {
  def on(f: T ⇒ Unit): T = {
    f apply some
    some
  }
}

object Control extends ControlImports

trait ControlImports {

  implicit def every[T](some: T): Every[T] = new Every[T](some)

  def closing[T <: {def close()}, R](closeable: T)(action: T ⇒ R): R =
    try action(closeable)
    finally closeable.close

}
