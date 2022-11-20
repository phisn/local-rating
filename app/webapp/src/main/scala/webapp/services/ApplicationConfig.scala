package webapp.services

import org.scalajs.dom

import java.util.concurrent.ThreadLocalRandom
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scalajs.js.Thenable.Implicits.thenable2future

import java.nio.file.{Files, Paths}
import scala.concurrent.*
import scala.concurrent.Await
import scala.concurrent.duration._
import org.scalajs.dom.*

import rescala.default._

trait ApplicationConfigInterface:
  def backendUrl: String
  def darkMode: Var[Boolean]
  def websocketReconnectInterval: Int
  def replicaID: String

class ApplicationConfig(services: {}) extends ApplicationConfigInterface:
  // Should use a config library but was unable to get it working with scalajs :(
  def backendUrl = if dom.window.location.hostname.contains("localhost") then
    "http://localhost:7071/api/"
  else
    "https://func-ratable-core.azurewebsites.net/api/"

  def darkMode = Var(false)

  def websocketReconnectInterval = 
    30.seconds.toMillis.toInt

  def replicaID: String = ThreadLocalRandom.current().nextLong().toHexString
