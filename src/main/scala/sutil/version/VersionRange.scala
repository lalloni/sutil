package sutil.version

sealed abstract class VersionRange {

  val start: Version

  val end: Version

  def contains(version: Version): Boolean

}

object VersionRange {

  case class Inclusive(start: Version, end: Version) extends VersionRange {

    def contains(version: Version) =
      start <= version && version <= end

  }

  case class Exclusive(start: Version, end: Version) extends VersionRange {

    def contains(version: Version) =
      start <= version && version < end

  }

}
