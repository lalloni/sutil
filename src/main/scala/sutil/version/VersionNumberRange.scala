package sutil.version

sealed abstract class VersionNumberRange {

  val start: VersionNumber

  val end: VersionNumber

  def contains(version: VersionNumber): Boolean

}

object VersionNumberRange {

  case class Inclusive(start: VersionNumber, end: VersionNumber) extends VersionNumberRange {

    def contains(version: VersionNumber) =
      start <= version && version <= end

  }

  case class Exclusive(start: VersionNumber, end: VersionNumber) extends VersionNumberRange {

    def contains(version: VersionNumber) =
      start <= version && version < end

  }

}
