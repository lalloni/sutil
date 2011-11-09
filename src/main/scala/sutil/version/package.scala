package sutil

package object version {

  object V {

    def apply(numbers: Int*) = VersionNumber(numbers: _*)
    def unapplySeq(version: VersionNumber) = Some(version.numbers)

    val Major = VersionNumberPosition.Major
    val Minor = VersionNumberPosition.Minor
    val Fix = VersionNumberPosition.Fix

  }

  object VP {
    def unapply(version: VersionNumber): Option[List[Int]] = Some(List(version.numbers: _*))
  }

}
