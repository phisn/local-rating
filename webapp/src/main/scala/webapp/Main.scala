package webapp

import cats.effect.SyncIO
import org.scalajs.dom
import outwatch.*
import outwatch.dsl.*
import rescala.default.*
import webapp.components.*
import webapp.services.*
import webapp.store.aggregates.ratings.*
import webapp.store.framework.{given, *}
import webapp.usecases.ratings.*

import webapp.store.given
import com.github.plokhotnyuk.jsoniter_scala.macros.*
import com.github.plokhotnyuk.jsoniter_scala.core.*

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

//TODO: https://github.com/scalacenter/scalajs-bundler/issues/414
// js.import("src/main/css/index.css")
@js.native
@JSImport("src/main/css/app.css", JSImport.Namespace)
object Css extends js.Object


object ServicesProduction extends Services:
  lazy val config = new ApplicationConfig()
  lazy val stateDistribution = new StateDistributionService(this)
  lazy val stateProvider = new StateProviderService(this)

@main
def main(): Unit =
  implicit val services = ServicesProduction
  Css

  Outwatch.renderInto[SyncIO]("#app", app).unsafeRunSync()

def app(using services: Services) =
  div(
    padding := "10px",
    connectionInput,
    clickCounter,
    createRating,
    ratings,
    jsonApplicationState,
    div(
      sys.props.map(i => div(i(0), " = ", i(1))).toList
    ),
    div(
      sys.env.map(i => div(i(0), " = ", i(1))).toList
    ),
    div(
      services.config.config
    )
  )

def jsonApplicationState(using services: Services) =
  div(
    services.stateProvider.state.toSignalDTO.map(dto => writeToString(dto))
  )
