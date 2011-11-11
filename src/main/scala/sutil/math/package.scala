package sutil.math
import scala.annotation.tailrec

trait MathImports {

  def powers(base: BigInt, from: Int = 0, step: Int = 1): Stream[BigInt] =
    Stream.cons(base.pow(from), powers(base, from + step, step))

  def sequence(from: BigInt, step: BigInt = 1): Stream[BigInt] =
    Stream.cons(from, sequence(from + step, step))

}

object Math extends MathImports
