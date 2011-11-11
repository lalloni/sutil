package sutil

package object version {

  type V = VersionNumber
  val V = VersionNumber
  val Major = VersionNumberPosition.Major
  val Minor = VersionNumberPosition.Minor
  val Fix = VersionNumberPosition.Fix

  type M = VersionModifier
  val M = VersionModifier

  implicit def parseVersionModifier(string: String): VersionModifier = VersionParsers.modifier(string)
  implicit def parseVersion(string: String): Version = VersionParsers.version(string)

}
