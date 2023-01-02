package webapp.application.usecases.ratable

import core.domain.aggregates.ratable.ecmrdt.*
import core.framework.*
import scala.concurrent.*
import scala.concurrent.ExecutionContext.Implicits.global
import webapp.*
import webapp.state.framework.{given, *}

def rateRatable(id: String, ratingForCategory: Map[Int, Int])(using services: Services, crypt: Crypt) =
  services.logger.log(s"Rating ratable with id: $id")

  for
    replicaId <- services.config.replicaId
  yield
    services.state.ratables.effect(
      id,
      rateEvent(replicaId, ratingForCategory)
    )
    
  // services.state.ratables.mutate(id, _.rate(ratingForCategory, services.config.replicaID))
