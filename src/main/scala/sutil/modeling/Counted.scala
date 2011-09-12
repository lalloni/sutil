package sutil.modeling

import org.slf4j.LoggerFactory
import grizzled.slf4j.Logging
import scala.collection.mutable.ConcurrentMap
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.ConcurrentHashMap
import scala.collection.JavaConversions._

object Counted {

  private val counters: ConcurrentMap[Class[_], AtomicInteger] =
    new ConcurrentHashMap[Class[_], AtomicInteger]

  protected val defaultLoggingMessage: Int ⇒ String =
    "%s instances created" format _

  protected val defaultLoggingPeriod: Option[Int] = Some(1000)

}

trait Counted extends Logging {

  protected val countedLoggingPeriod: Option[Int] =
    Counted.defaultLoggingPeriod

  protected val countedLoggingMessage: Int ⇒ String =
    Counted.defaultLoggingMessage

  protected val countedInstanceNumber =
    Counted.counters
      .getOrElseUpdate(getClass, new AtomicInteger(0))
      .incrementAndGet

  countedLoggingPeriod foreach { period ⇒
    if (countedInstanceNumber % period == 0)
      info(countedLoggingMessage(countedInstanceNumber))
  }

}
