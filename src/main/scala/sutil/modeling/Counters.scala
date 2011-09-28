package sutil.modeling

import collection.mutable.ConcurrentMap
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.ConcurrentHashMap
import collection.JavaConversions._

object Counters {

  object Counters {

    type Tags = Set[String]

    private val counters: ConcurrentMap[Tags, AtomicLong] =
      new ConcurrentHashMap[Tags, AtomicLong]

    private def counter(tags: Tags) =
      counters.getOrElseUpdate(tags, new AtomicLong(0))

    def add(tags: Tags, value: Long): Long =
      counter(tags).addAndGet(value)

    def inc(tags: Tags): Long =
      counter(tags).incrementAndGet

    def dec(tags: Tags): Long =
      counter(tags).decrementAndGet

    def reset(tags: Tags): Unit =
      counter(tags).set(0)

    def get(tags: Tags): Option[Long] =
      counters.get(tags).map(_.get)

  }

}
