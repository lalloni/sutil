package sutil.modeling

import grizzled.slf4j.Logging
import scala.collection.mutable.ConcurrentMap
import java.util.concurrent.ConcurrentHashMap
import scala.collection.JavaConversions._
import java.util.concurrent.atomic.AtomicLong

object Counted {

  private val counters: ConcurrentMap[Class[_], AtomicLong] =
    new ConcurrentHashMap[Class[_], AtomicLong]

  protected val defaultLoggingMessage: Long ⇒ String =
    "%s instances created" format _

  protected val defaultLoggingPeriod: Option[Long] = Some(1000)

}

trait Counted extends Logging {

  protected val countedLoggingPeriod: Option[Long] =
    Counted.defaultLoggingPeriod

  protected val countedLoggingMessage: Long ⇒ String =
    Counted.defaultLoggingMessage

  protected val countedInstanceNumber =
    Counted.counters
    .getOrElseUpdate(getClass, new AtomicLong(0))
    .incrementAndGet

  countedLoggingPeriod foreach {
    period ⇒
      if (countedInstanceNumber % period == 0)
        info(countedLoggingMessage(countedInstanceNumber))
  }

}
