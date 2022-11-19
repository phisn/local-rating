package webapp.services

import colibri.*
import colibri.router.*
import colibri.router.Router
import org.scalajs.dom.*
import org.scalajs.dom
import outwatch.*
import outwatch.dsl.*
import rescala.default.*
import scala.reflect.Selectable.*
import scala.scalajs.js
import webapp.*
import webapp.application.{given, *}
import webapp.application.pages.*
import webapp.device.services.*

trait Page:
  def render(using services: Services): VNode

class RoutingService(services: {
  val logger: LoggerServiceInterface
  val window: WindowServiceInterface
}):
  private val page = Var[Page](Routes.fromPath(Path(services.window.routePath)))

  def render(using services: Services): Signal[VNode] =
    page.map(_.render)

  def to(newPage: Page, preventReturn: Boolean = false) =
    services.logger.trace(s"Routing to ${linkPath(newPage)}")
    services.window.routeTo(RoutingState(!preventReturn), linkPath(newPage))
    page.set(newPage)

  def toReplace(newPage: Page, preventReturn: Boolean = false) =
    services.logger.trace(s"Routing replace to ${linkPath(newPage)}")
    services.window.routeToInPlace(RoutingState(!preventReturn), linkPath(newPage))
    page.set(newPage)

  def link(newPage: Page) =
    URL(linkPath(newPage), window.location.href).toString

  def linkPath(newPage: Page) =
    Routes.toPath(newPage).pathString

  def back =
    services.logger.trace(s"Routing back")
    services.window.routeBack

  def state =
    services.window.routeState[RoutingState]

  services.logger.trace(s"Routing initial from ${window.location.pathname} to ${linkPath(page.now)}")

  // Ensure initial path is correctly set
  // Example: for path "/counter" and pattern "counter/{number=0}" the
  //          url should be "/counter/0" and not "/counter"
  services.window.routeToInPlace(RoutingState(false), linkPath(page.now))

  // Change path when url changes by user action
  services.window.eventFromName("popstate").observe(_ =>
    page.set(Routes.fromPath(Path(window.location.pathname)))
  )

  class RoutingState(
    // if canReturn is true then the page will show in mobile mode
    // an go back arrow in the top left corner
    val canReturn: Boolean

  ) extends js.Object
