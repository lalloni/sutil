package sutil.version

import scala.util.parsing.combinator.RegexParsers

case class Version(numbers: List[Int], modifiers: List[Modifier] = Nil) extends Ordered[Version] {
  
  lazy val major = numbers.head
  
  lazy val minor = numbers.drop(1).headOption
  
  lazy val fix = numbers.drop(2).headOption
  
  lazy val normal = (major, minor.getOrElse(0), fix.getOrElse(0))
  
  lazy val extended = (normal, modifiers)
  
  override lazy val toString = numbers.mkString(".") + (if (modifiers.isEmpty) "" else "-" + modifiers.mkString("-"))
  
  def compare(other: Version): Int = {
    numbers
      .view
      .zipAll(other.numbers, 0, 0)
      .map(p ⇒ p._1 compare p._2)
      .find(_ != 0)
      .getOrElse(modifiers
        .view
        .zipAll(other.modifiers, Modifier.zero, Modifier.zero)
        .map(p ⇒ p._1 compare p._2)
        .find(_ != 0)
        .getOrElse(0))
  }
  
}

object Version {

  object parse extends RegexParsers {
    def number = """\d+""".r ^^ (_.toInt)
    def tag = """[^.0-9-]+""".r
    def numbers = rep1sep(number, ".")
    def modifiers = rep1sep(tag ~ opt(number) ^^ { case tag ~ num ⇒ Modifier(tag, num) }, "-")
    def version = numbers ~ opt(("." | "-") ~> modifiers) ^^ { case nums ~ mods ⇒ Version(nums, mods.getOrElse(Nil)) }
    def apply(string: String): Version =
      parseAll(version, string) match {
        case Success(ver, rest) ⇒ ver
        case e: NoSuccess       ⇒ sys.error(e.toString)
      }
  }

  def apply(version: String): Version = parse(version)

}
