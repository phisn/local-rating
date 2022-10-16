package webapp.components

import org.scalajs.dom
import outwatch.*
import outwatch.dsl.*
import rescala.default.*
import webapp.services.*
import webapp.store.aggregates.rating.{given, *}
import webapp.store.framework.*
import webapp.{*, given}

def titleComponent(title: String) =
  div(
    cls := "text-2xl md:text-4xl",
    h1(
      title
    )
  )
