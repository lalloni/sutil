package sutil.path

import java.io.File
import util.matching.Regex

trait GlobImports {

  implicit def globBuilder(s: String) = new GlobBuilder(s)

}

trait FilePredicate extends (File ⇒ Boolean)

class GlobBuilder(s: String) {

  def g: Glob = Glob(s)

}

object Glob extends GlobImports {

  private val reserved = "\\+()^$.{}]|"

}

case class Glob(pattern: String) extends FilePredicate {

  lazy val regex = globToRegex(pattern)

  def apply(file: File): Boolean = matches(file)

  def matches(name: String): Boolean = regex.findPrefixMatchOf(name).isDefined

  def matches(file: File): Boolean = matches(file.getPath)

  private def globToRegex(glob: String): Regex = {

    def re(chars: List[Char]): List[Char] =
      chars match {
        case '*' :: cs ⇒ '.' :: '*' :: re(cs)
        case '?' :: cs ⇒ '.' :: re(cs)
        case '[' :: '!' :: c :: cs ⇒ '[' :: '^' :: c :: cla(cs)
        case '[' :: cs ⇒ '[' :: cla(cs)
        case '{' :: cs ⇒ '(' :: alt(cs)
        case c :: cs ⇒ esc(c) ++ re(cs)
        case Nil ⇒ Nil
      }

    def alt(chars: List[Char]): List[Char] =
      chars match {
        case '}' :: cs ⇒ ')' :: re(cs)
        case ',' :: cs ⇒ '|' :: alt(cs)
        case c :: cs ⇒ esc(c) ++ alt(cs)
        case Nil ⇒ sys.error("Unterminated alternatives")
      }

    def cla(chars: List[Char]): List[Char] =
      chars match {
        case ']' :: cs ⇒ ']' :: re(cs)
        case c :: cs ⇒ c :: cla(cs)
        case Nil ⇒ sys.error("Unterminated character class")
      }

    def esc(char: Char): List[Char] =
      if (Glob.reserved contains char)
        '\\' :: char :: Nil
      else
        char :: Nil

    ("^" + new String(re(glob.toCharArray.toList).toArray) + "$").r

  }

}
