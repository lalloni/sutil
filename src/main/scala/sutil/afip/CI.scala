package sutil.afip

import scala.util.parsing.combinator.RegexParsers

import CI._
import sutil.math._

case class CI(val prefix: Short, val id: Int, val verifier: Digit) {

  require(prefix > -1, req("prefix must be positive or zero", prefix))
  require(prefix.toString.size == 2, req("prefix must be 2 digits", prefix))
  require(id > -1, req("identifier must be positive or zero", id))
  require(id.toString.size <= 8, req("identifier must be 8 digits or less", id))
  require(computedVerifier == verifier, req("incorrect verifier digit", computedVerifier, verifier))

  lazy val computedVerifier = CI.verifier(prefix, id)

  override lazy val toString = "%02d%08d%1d" format (prefix, id, verifier)

  lazy val toPrettyString = "%02d-%08d-%1d" format (prefix, id, verifier)

  lazy val digits: Seq[Digit] =
    (prefix padLeft 2) ++ (id padLeft 8) :+ verifier

}

object CI {

  private def req(condition: String, received: Any): String =
    "CI: %s but '%s' was received" format (condition, received)

  private def req(condition: String, expected: Any, received: Any): String =
    "CI: %s '%s' was received while expecting '%s'" format (condition, received, expected)

  private object Parsers extends RegexParsers {

    def ci = separated | joined

    def separated = pre ~ "-" ~ id ~ "-" ~ ver ^^ { case pre ~ _ ~ id ~ _ ~ ver ⇒ CI(pre, id, ver) }

    def joined = pre ~ id ~ ver ^^ { case pre ~ id ~ ver ⇒ CI(pre, id, ver) }

    def pre = "\\d{2}".r ^^ { _.toByte }
    def id = "\\d{8}".r ^^ { _.toInt }
    def ver = "\\d{1}".r ^^ { d ⇒ Digit(d.toByte) }

    def ci(code: String): CI = parse(ci, code) match {
      case Success(ci, _) ⇒ ci
      case n: NoSuccess   ⇒ sys.error(n.toString)
    }

  }

  def apply(code: String): CI = Parsers.ci(code)

  def apply(code: Long): CI = {
    val digits = code.digits
    require(digits.size == 11, req("must be 11 digits", code))
    CI(digits.slice(9, 11).reverse.mkString.toShort, digits.slice(1, 9).reverse.mkString.toInt, digits(0))
  }

  private val factors = Seq(5, 4, 3, 2, 7, 6, 5, 4, 3, 2)

  /** Calculates verifier digit for a prefix and identification numbers */
  def verifier(prefix: Short, id: Int): Digit = {
    val digits = (prefix padLeft 2) ++ (id padLeft 8)
    (0 until (factors.length) map (i ⇒ digits(i) * factors(i)) sum) % 11 match {
      case 0 ⇒ Digit0
      case 1 ⇒ Digit9
      case x ⇒ Digit(11 - x)
    }
  }

}
