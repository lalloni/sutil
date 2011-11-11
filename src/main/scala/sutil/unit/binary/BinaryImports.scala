package sutil.unit.binary

import scala.math.{ BigDecimal ⇒ BigDec }

import BinaryUnit._
import scalaz.Scalaz._

trait BinaryImports {

  private val factors = BinaryUnit.factors.reverse.view

  def human(number: BigInt): (BigDec, BinaryUnit.Value) =
    factors
      .map(factor ⇒ (BigDec(number) / BigDec(factor._1), factor._2))
      .find(value ⇒ value._1 >= 1)
      .getOrElse(BigDec(0) -> B)

  def humans(number: BigInt, decimals: Int = 1): String =
    human(number) |> (value ⇒ ("%1." + decimals + "f %s") format (value._1, value._2))

}
