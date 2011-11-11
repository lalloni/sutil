package sutil.version

trait VersionImports {

  type N = VersionNumber
  val N = VersionNumber

  type M = VersionModifier
  val M = VersionModifier

  type V = Version
  val V = Version

  val Major = VersionNumberPosition.Major
  val Minor = VersionNumberPosition.Minor
  val Fix = VersionNumberPosition.Fix

  implicit def parseVersionModifier(string: String): VersionModifier = VersionParsers.modifier(string)
  implicit def parseVersionNumber(string: String): VersionNumber = VersionParsers.number(string)
  implicit def parseVersion(string: String): Version = VersionParsers.version(string)

}
