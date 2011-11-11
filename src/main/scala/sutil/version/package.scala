package sutil

package object version extends VersionImports {

  private[version] val adder =
    ((a: Int, b: Int) ⇒ a + b) tupled

  private[version] val not0 =
    (a: Int) ⇒ a != 0

  private[version] def comparator[T: Ordering]: ((T, T)) ⇒ Int =
    ((a: T, b: T) ⇒ implicitly[Ordering[T]].compare(a, b)) tupled

  private[version] def compareSeqs[T: Ordering](a: Seq[T], b: Seq[T], zero: T): Int =
    a zipAll (b, zero, zero) map comparator find not0 getOrElse 0

}
