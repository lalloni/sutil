package sutil.version

import sutil.Imports._

case class Version(number: VersionNumber, modifiers: VersionModifier*) extends Ordered[Version] {

  override lazy val toString = 
    number.toString + (if (modifiers.isEmpty) "" else "-" + modifiers.mkString("-"))

  def compare(other: Version): Int = 
    (number compare other.number) ifZero compareSeqs(modifiers, other.modifiers, M.Empty)

}

object Version {

  def apply(version: String): Version = 
    VersionParsers.version(version)

}
