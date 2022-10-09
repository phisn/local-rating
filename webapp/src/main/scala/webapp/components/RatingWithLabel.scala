package webapp.components

import org.scalajs.dom
import outwatch.*
import outwatch.dsl.*
import rescala.default.*
import scala.util.*
import webapp.services.*
import webapp.store.aggregates.ratings.given
import webapp.store.aggregates.ratings.*
import webapp.store.framework.*
import webapp.{*, given}

private def starInputComponent(id: String, isChecked: Boolean) = 
  input(
    cls := "mask mask-star-2 bg-accent bg w-10 h-10",
    tpe := "radio",
    name := id,
    checked := isChecked
  )

private def starComponent(isChecked: Boolean) = 
  div(
    cls := "mask mask-star-2 bg-primary bg w-10 h-10",
    
    if isChecked then
      cls := "opacity-20"
    else
      cls := ""
  )

def ratingWithLabelComponent(label: String, defaultValue: Option[Int] = None, isReadOnly: Boolean = false, stars: Int = 5) =
  val name = Random.alphanumeric.take(16).mkString
  val defaultIndex: Int = defaultValue match {
    case None => (math.ceil(stars / 2.0) - 1).toInt
    case Some(value) => value - 1
  }

  div(
    cls := "flex flex-col space-y-2",
    div(
      cls := "text-2xl",
      label
    ),
    div(
      cls := "rating",
      Range(0, stars).map { i => 
        if isReadOnly then
          starComponent(i > defaultIndex)
        else
          starInputComponent(name, i == defaultIndex)
      }
    )
  )