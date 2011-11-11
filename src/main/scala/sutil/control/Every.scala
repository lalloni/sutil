package sutil.control

class Every[T](some: T) {
  def on(f: T ⇒ Unit): T = {
    f apply some
    some
  }
}
