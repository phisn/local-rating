package webapp.application.usecases.ratable

import core.domain.aggregates.ratable.ecmrdt.*
import core.framework.{given, *}
import webapp.*
import webapp.state.framework.{given, *}

def createRatable(title: String, categories: List[String])(using services: Services) =
  services.logger.log(s"Creating ratable with title: $title")
  val id = services.state.uniqueID
  id

  /*
  services.state.ratables.create(id, Ratable(title, categories, services.config.replicaID))
  */
