package sutil.afip

import scala.util.parsing.combinator.RegexParsers
import sutil.math.Digit
import sutil.math.Digit._

case class CI(val prefix: Short, val id: Int, val verifier: Digit) {

  import CI.{ req, verifier ⇒ computeVerifier }

  assert(prefix > -1, req("prefix must be positive or zero", prefix))
  assert(prefix.toString.size == 2, req("prefix must be 2 digits", prefix))
  assert(id > -1, req("identifier must be positive or zero", id))
  assert(id.toString.size <= 8, req("identifier must be 8 digits or less", id))
  assert(computedVerifier == verifier, req("incorrect verifier digit", computedVerifier, verifier))

  lazy val computedVerifier = computeVerifier(this)

  override lazy val toString = format("%02d%08d%1d", prefix, id, verifier)

  lazy val toPrettyString = format("%02d-%08d-%1d", prefix, id, verifier)

  lazy val digits =
    (pad(prefix, 2).reverse ++ pad(id, 8).reverse :+ verifier).reverse

}

object CI {

  private def req(condition: String, received: Any): String =
    format("CI: %s but '%s' was received", condition, received)

  private def req(condition: String, expected: Any, received: Any): String =
    format("CI: %s '%s' was received while expecting '%s'", condition, received, expected)

  private object Parser extends RegexParsers {

    def ci = separated | joined

    def separated = pre ~ "-" ~ id ~ "-" ~ ver ^^ { case pre ~ _ ~ id ~ _ ~ ver ⇒ CI(pre, id, ver) }

    def joined = pre ~ id ~ ver ^^ { case pre ~ id ~ ver ⇒ CI(pre, id, ver) }

    def pre = "\\d{2}".r ^^ { _.toByte }
    def id = "\\d{8}".r ^^ { _.toInt }
    def ver = "\\d{1}".r ^^ { d ⇒ Digit(d.toByte) }

    def parse(code: String): CI = parse(ci, code) match {
      case Success(ci, _) ⇒ ci
      case n: NoSuccess   ⇒ throw error(n.toString)
    }

  }

  def apply(code: String): CI = Parser.parse(code)

  def apply(code: Long): CI = {
    val digits = Digit.digits(code)
    assert(digits.size == 11, req("must be 11 digits", code))
    CI(digits.slice(9, 11).reverse.mkString.toShort, digits.slice(1, 9).reverse.mkString.toInt, digits(0))
  }

  private val factors = Seq(5, 4, 3, 2, 7, 6, 5, 4, 3, 2)

  def verifier(ci: CI): Digit = {
    val digits = ci.digits.reverse
    0.until(factors.length).map(i ⇒ digits(i) * factors(i)).sum % 11 match {
      case 0 ⇒ Digit(0)
      case 1 ⇒ Digit(9)
      case x ⇒ Digit(11 - x)
    }
  }

}

object Test {

  def check(ci: CI, pre: Byte, id: Int, ver: Digit) = {
    assert(ci.prefix == pre)
    assert(ci.id == id)
    assert(ci.verifier == ver)
  }

  def main(args: Array[String]): Unit = {

    assert(CI.verifier(CI(20, 24264377, 2.toDigit)).toInt == 2)

    val cuit1 = CI(20242643772l)
    check(cuit1, 20, 24264377, 2.toDigit)

    val cuit2 = CI("20242643772")
    check(cuit2, 20, 24264377, 2.toDigit)

    val cuit3 = CI("20-24264377-2")
    check(cuit3, 20, 24264377, 2.toDigit)

    val cuit4 = CI(20, 24264377, 2.toDigit)
    check(cuit4, 20, 24264377, 2.toDigit)

    println(Seq(cuit1, cuit2, cuit3, cuit4, CI(20, 11111111, 2.toDigit), CI(20, 00000000, 1.toDigit)))

  }

}
