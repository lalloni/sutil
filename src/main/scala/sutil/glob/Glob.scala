package sutil.glob

import java.io.File
import scala.util.matching.Regex

object Glob {
  private val reserved = "\\+()^$.{}]|"
}

case class Glob(pattern: String) extends (String ⇒ Boolean) {

  private lazy val r = globToRegex(pattern)

  def apply(string: String): Boolean = r.findPrefixMatchOf(string).isDefined
  def ~(string: String): Boolean = apply(string)

  private def globToRegex(glob: String): Regex = {

    def re(chars: List[Char]): List[Char] = chars match {
      case '*' :: cs             ⇒ '.' :: '*' :: re(cs)
      case '?' :: cs             ⇒ '.' :: re(cs)
      case '[' :: '!' :: c :: cs ⇒ '[' :: '^' :: c :: cla(cs)
      case '[' :: cs             ⇒ '[' :: cla(cs)
      case '{' :: cs             ⇒ '(' :: alt(cs)
      case c :: cs               ⇒ esc(c) ++ re(cs)
      case Nil                   ⇒ Nil
    }

    def alt(chars: List[Char]): List[Char] = chars match {
      case '}' :: cs ⇒ ')' :: re(cs)
      case ',' :: cs ⇒ '|' :: alt(cs)
      case c :: cs   ⇒ esc(c) ++ alt(cs)
      case Nil       ⇒ sys.error("Unterminated alternatives")
    }

    def cla(chars: List[Char]): List[Char] = chars match {
      case ']' :: cs ⇒ ']' :: re(cs)
      case c :: cs   ⇒ c :: cla(cs)
      case Nil       ⇒ sys.error("Unterminated character class")
    }

    def esc(char: Char): List[Char] = {
      if (Glob.reserved contains char) '\\' :: char :: Nil
      else char :: Nil
    }

    ('^' + new String(re(glob.toList).toArray) + '$').r

  }

}
