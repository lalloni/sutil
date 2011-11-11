package sutil.unit.binary

import sutil.Imports._

object BinaryUnit extends Enumeration {

  type BinaryUnit = Value

  private val expos = sequence(0, 10) iterator

  val B, KiB, MiB, GiB, TiB, PiB, EiB, ZiB, YiB = Value(expos.next.toInt)

  lazy val factors = values.toSeq.map(i ⇒ (BigDecimal(2).pow(i.id).toBigInt, i)).sorted

}
