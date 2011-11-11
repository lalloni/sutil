package sutil.version

case class Version(number: VersionNumber, modifiers: VersionModifier*) extends Ordered[Version] {

  override lazy val toString = number.toString + (if (modifiers.isEmpty) "" else "-" + modifiers.mkString("-"))

  def compare(other: Version): Int = {
    sys.error("unimplemented")
  }

}

object Version {

  def apply(version: String): Version = VersionParsers.version(version)

}
