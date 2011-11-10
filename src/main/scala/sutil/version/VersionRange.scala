package sutil.version

sealed abstract class VersionRange {

  val start: VersionNumber

  val end: VersionNumber

  def contains(version: VersionNumber): Boolean

}

object VersionRange {

  case class Inclusive(start: VersionNumber, end: VersionNumber) extends VersionRange {

    def contains(version: VersionNumber) = start <= version && version <= end

  }

  case class Exclusive(start: VersionNumber, end: VersionNumber) extends VersionRange {

    def contains(version: VersionNumber) = start <= version && version < end

  }

}
