package sutil.afip

import scala.util.parsing.combinator.RegexParsers

import CI.{ verifier ⇒ computeVerifier, req }
import sutil.math.Digit._
import sutil.math.Digit

case class CI(val prefix: Short, val id: Int, val verifier: Digit) {

  import CI.{ req, verifier ⇒ computeVerifier }

  assert(prefix > -1, req("prefix must be positive or zero", prefix))
  assert(prefix.toString.size == 2, req("prefix must be 2 digits", prefix))
  assert(id > -1, req("identifier must be positive or zero", id))
  assert(id.toString.size <= 8, req("identifier must be 8 digits or less", id))
  assert(computedVerifier == verifier, req("incorrect verifier digit", computedVerifier, verifier))

  lazy val computedVerifier = computeVerifier(this)

  override lazy val toString = "%02d%08d%1d" format (prefix, id, verifier)

  lazy val toPrettyString = "%02d-%08d-%1d" format (prefix, id, verifier)

  lazy val digits =
    (pad(prefix, 2).reverse ++ pad(id, 8).reverse :+ verifier).reverse

}

object CI {

  private def req(condition: String, received: Any): String =
    "CI: %s but '%s' was received" format (condition, received)

  private def req(condition: String, expected: Any, received: Any): String =
    "CI: %s '%s' was received while expecting '%s'" format (condition, received, expected)

  private object Parser extends RegexParsers {

    def ci = separated | joined

    def separated = pre ~ "-" ~ id ~ "-" ~ ver ^^ { case pre ~ _ ~ id ~ _ ~ ver ⇒ CI(pre, id, ver) }

    def joined = pre ~ id ~ ver ^^ { case pre ~ id ~ ver ⇒ CI(pre, id, ver) }

    def pre = "\\d{2}".r ^^ { _.toByte }
    def id = "\\d{8}".r ^^ { _.toInt }
    def ver = "\\d{1}".r ^^ { d ⇒ Digit(d.toByte) }

    def parse(code: String): CI = parse(ci, code) match {
      case Success(ci, _) ⇒ ci
      case n: NoSuccess   ⇒ sys.error(n.toString)
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
