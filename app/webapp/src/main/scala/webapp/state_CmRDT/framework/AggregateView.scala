package webapp.state.framework

import core.framework.ecmrdt.*
import rescala.default.*
import scala.concurrent.*

// AggregateView exposes the manipulation or reading of the aggregate A
trait AggregateView[A, C]:
  def effect(event: EventWithContext[A, C]): Unit
  def listen: Signal[A]