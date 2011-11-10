package sutil

package object version {

  type V = VersionNumber
  val V = VersionNumber
  val Major = VersionNumberPosition.Major
  val Minor = VersionNumberPosition.Minor
  val Fix = VersionNumberPosition.Fix

  type M = VersionModifier
  val M = VersionModifier

  implicit def stringToVersionModifier(string: String): VersionModifier = Version.parsers.modifier(string)
  implicit def stringToVersion(string: String): Version = Version.parsers.version(string)

}
