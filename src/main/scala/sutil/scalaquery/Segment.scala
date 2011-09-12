package sutil.scalaquery

import scala.actors.Actor

import org.scalaquery.ql.extended.{ ExtendedTable ⇒ Table }
import org.scalaquery.ql.Query
import org.scalaquery.session.Database.threadLocalSession

case class Segment[T](val table: Table[T], val position: Int, val size: Int)(implicit dbp: DatabaseProfile) {

  import dbp.profile.Implicit._

  def query =
    Query(table).drop(position).take(size)

  def reader(handler: T ⇒ Unit): Actor =
    new Actor {
      def act =
        dbp.database.withSession {
          for (t ← query)
            handler(t)
        }
    }

  def read(handler: T ⇒ Unit): Unit =
    reader(handler).start

}

object Segment {

  def table[T](table: Table[T], segments: Int)(implicit dbp: DatabaseProfile): Seq[Segment[T]] = {
    import dbp.profile.Implicit._
    val totalSize = dbp.database.withSession(Query(table.count).first)
    val segmentSize = totalSize / segments
    for (currentSegment ← 0 until segments)
      yield Segment(table, currentSegment * segmentSize, segmentSize)
  }

}
