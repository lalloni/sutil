package sutil.version

import scala.util.parsing.combinator.RegexParsers

object VersionParsers {

  private object Parsers extends RegexParsers {

    def number = """\d+""".r ^^ (_.toInt)

    def tag = """[^.0-9-]+""".r

    def numbers = rep1sep(number, ".") ^^ (VersionNumber(_: _*))

    def modifier = tag ~ opt(numbers) ^^ { case tag ~ num ⇒ VersionModifier(tag, num) }

    def modifiers = rep1sep(modifier, "-")

    def version = numbers ~ opt(("." | "-") ~> modifiers) ^^ { case nums ~ mods ⇒ Version(nums, mods.getOrElse(Nil): _*) }

    def get[T](parser: Parser[T], string: String): T =
      parseAll(parser, string) match {
        case Success(e, rest) ⇒ e
        case e: NoSuccess     ⇒ sys.error(e.toString)
      }

    def version(string: String): Version = get(version, string)

    def modifier(string: String): VersionModifier = get(modifier, string)

    def numbers(string: String): VersionNumber = get(numbers, string)

  }

  def numbers(string: String) = Parsers.numbers(string)

  def version(string: String) = Parsers.version(string)

  def modifier(string: String) = Parsers.modifier(string)

}
