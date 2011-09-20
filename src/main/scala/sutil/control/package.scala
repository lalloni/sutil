package sutil

package control {

  class On[T](some: T) {
    def on(f: T ⇒ Unit): T = {
      f apply some
      some
    }
  }

}

package object control {

  implicit def using[T](some: T): On[T] = new On[T](some)

  def closing[T <: { def close() }, R](closeable: T)(action: T ⇒ R): R =
    try action(closeable)
    finally closeable.close

}
