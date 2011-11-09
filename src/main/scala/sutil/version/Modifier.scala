package sutil.version

case class Modifier(tag: String, version: Option[Int]) extends Ordered[Modifier] { self ⇒
  
  import Modifier._
  
  lazy val order: Int = {
    val t = tag.toLowerCase
    require(orders.contains(t), "Unsupported version modifier: %s" format self)
    orders(t)
  }

  override lazy val toString = tag + version.getOrElse("")
  
  def compare(other: Modifier): Int =
    (self.order compare other.order) match {
      case 0 ⇒ self.version.getOrElse(0) compare other.version.getOrElse(0)
      case x ⇒ x
    }
  
}

object Modifier {
  
  val zero = Modifier("", None)
  
  private lazy val orders = Map(
    "snapshot" -> 10,
    "alpha" -> 20,
    "beta" -> 30,
    "pre" -> 40,
    "rc" -> 40,
    "" -> 45,
    "final" -> 50,
    "ga" -> 50
  )
  
}
