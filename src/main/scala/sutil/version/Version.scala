package sutil.version

case class Version(numbers: VersionNumber, modifiers: VersionModifier*) extends Ordered[Version] {

  lazy val major = numbers.majorNumber

  lazy val minor = numbers.minorNumber

  lazy val fix = numbers.fixNumber

  lazy val normal = (major, minor.getOrElse(0), fix.getOrElse(0))

  lazy val extended = (normal, modifiers)

  override lazy val toString = numbers.toString + (if (modifiers.isEmpty) "" else "-" + modifiers.mkString("-"))

  def compare(other: Version): Int = {
    sys.error("unimplemented")
  }

}

object Version {

  def apply(version: String): Version = VersionParsers.version(version)

}
