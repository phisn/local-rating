package webapp.pages.ratepage

import org.scalajs.dom
import outwatch.*
import outwatch.dsl.*
import rescala.default.*
import webapp.components.*
import webapp.components.layouts.*
import webapp.pages.viewpage.*
import webapp.services.*
import webapp.store.aggregates.ratings.given
import webapp.store.aggregates.ratings.*
import webapp.store.framework.*
import webapp.{*, given}

case class RatePage(
  ratableId: String
) extends Page:
  def render(using services: Services): HtmlVNode =
    layoutComponent(
      div(
        cls := "flex-grow flex justify-center p-12",
        div(
          cls := "flex flex-col space-y-8",
          width := "36rem",
          
          titleComponent("Rating of this cool chinese restaurant we went to"),
          
          div(
            cls := "flex flex-col space-y-6",
            ratingWithLabelComponent("Taste"),
            ratingWithLabelComponent("Ambiente"),
            ratingWithLabelComponent("Price"),
          ),

          div(
            cls := "space-x-4",
            button(
              cls := "btn btn-primary mt-8",
              "Submit",
              onClick.foreach(_ => services.routing.to(ViewPage(ratableId)))
            ),
            button(
              cls := "btn btn-outline mt-8",
              "Cancel and view submissions",
              onClick.foreach(_ => services.routing.to(ViewPage(ratableId)))
            )
          )
        )
      )
    )
