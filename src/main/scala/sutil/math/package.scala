package sutil

package object math {

  def powers(base: BigInt, from: Int = 0, step: Int = 1): Stream[BigInt] =
    Stream.cons(base.pow(from), powers(base, from + step, step))

}
