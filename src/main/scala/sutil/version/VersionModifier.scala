package sutil.version

import sutil.Imports._

case class VersionModifier(tag: String, version: Option[VersionNumber]) extends Ordered[VersionModifier] {

  def compare(other: VersionModifier): Int =
    (tagOrder compare other.tagOrder) ifZero ((version getOrElse VersionNumber.Zero) compare (other.version getOrElse VersionNumber.Zero))

  override lazy val toString =
    tag + (if (version.isDefined) version.get.toString else "")

  private def tagOrder =
    VersionModifier.tagOrder(tag.toLowerCase)

}

object VersionModifier {

  private val NoTag = ""

  val Empty = VersionModifier(NoTag, None)

  private lazy val tags =
    Seq(
      Seq("snapshot"),
      Seq("alpha"),
      Seq("beta", "m"),
      Seq("pre", "rc", "cr", "ea"),
      Seq("final", "ga", NoTag),
      Seq("sp")
    )

  private lazy val tagOrder =
    (for ((group, index) ← tags zipWithIndex; tag ← group) yield tag -> index).toMap

  def apply(tag: String, version: VersionNumber): VersionModifier =
    VersionModifier(tag, Some(version))

  def apply(string: String): VersionModifier =
    VersionParsers.modifier(string)

}
