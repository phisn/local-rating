package webapp.pages.viewpage

import org.scalajs.dom
import outwatch.*
import outwatch.dsl.*
import rescala.default.*
import webapp.components.*
import webapp.components.layouts.*
import webapp.pages.ratepage.*
import webapp.services.*
import webapp.store.aggregates.ratings.given
import webapp.store.aggregates.ratings.*
import webapp.store.framework.*
import webapp.{*, given}

case class ViewPage(
  ratableId: String
) extends Page:
  def render(using services: Services): HtmlVNode =
    layoutComponent(
      div(
        cls := "flex-grow flex justify-center p-12",
        div(
          cls := "flex flex-col space-y-6",
          width := "36rem",
          
          titleComponent("Rating of this cool chinese restaurant we went to"),

          div(
            cls := "flex space-x-4",
            div(
              cls := "badge badge-outline p-4",
              "36 Submissions"
            ),
            div(
              cls := "badge badge-outline p-4",
              "20 Comments"
            ),
            div(
              cls := "badge badge-outline p-4",
              "Submission ends in 2 days"
            )
          ),

          div(
            cls := "pt-6",
            
            div(
              cls := "flex flex-col space-y-6",
              div(
                cls := "flex flex-col space-y-4",
                ratingWithLabelComponent("Overall", None, true),
                div(
                  cls := "divider"
                ),
                ratingWithLabelComponent("Taste", None, true),
              ),
              ratingWithLabelComponent("Ambiente", None, true),
              ratingWithLabelComponent("Price", None, true),
            )
          )
        )
      )
    )