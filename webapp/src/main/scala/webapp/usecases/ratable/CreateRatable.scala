package webapp.usecases.ratable

import core.state.aggregates.ratable.*
import core.state.framework.{given, *}
import webapp.*
import webapp.state.framework.{given, *}

def createRatable(title: String, categories: List[String])(using services: Services) =
  services.logger.log(s"Creating ratable with title: $title")
  
  val id = "some-random-id"
  services.state.ratable(id) { ratable =>
    
  }

/*
  val id = services.state.ratables.now.uniqueID(services.config.replicaID)
  services.state.ratables(_.create(id, title, categories, services.config.replicaID))
  id
*/
