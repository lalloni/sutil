package sutil.version

import scala.util.parsing.combinator.RegexParsers

object VersionParsers {

  private object Parsers extends RegexParsers {

    def natural = """\d+""".r ^^ { _.toInt }

    def tag = """[^.0-9-]+""".r

    def number = rep1sep(natural, ".") ^^ { VersionNumber(_: _*) }

    def modifier = tag ~ opt(number) ^^ { case tag ~ num ⇒ VersionModifier(tag, num) }

    def modifiers = rep1sep(modifier, "-")

    def version = number ~ opt(("." | "-") ~> modifiers) ^^ { case nums ~ mods ⇒ Version(nums, mods.getOrElse(Nil): _*) }

    def get[T](parser: Parser[T], string: String): T =
      parseAll(parser, string) match {
        case Success(result, _) ⇒ result
        case failure: NoSuccess ⇒ sys.error(failure.toString)
      }

    def number(string: String): VersionNumber = get(number, string)

    def modifier(string: String): VersionModifier = get(modifier, string)

    def version(string: String): Version = get(version, string)

  }

  def number(string: String) = Parsers.number(string)

  def version(string: String) = Parsers.version(string)

  def modifier(string: String) = Parsers.modifier(string)

}
