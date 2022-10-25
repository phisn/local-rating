package webapp.services

import com.github.plokhotnyuk.jsoniter_scala.core.*
import com.github.plokhotnyuk.jsoniter_scala.macros.*
import kofre.base.*
import kofre.decompose.containers.DeltaBufferRDT
import kofre.dotted.Dotted
import kofre.syntax.DottedName
import rescala.default.*
import webapp.services.*
import webapp.store.{*, given}
import webapp.store.framework.{*, given}

import scala.concurrent.Future
import scala.reflect.Selectable.*
import scala.util.{Failure, Success}
import webapp.Services

// Creates facades for aggregates and registers them for distribution
class StateDistributionService(services: {
  val backendApi: BackendApiServiceInterface
  val config: ApplicationConfigInterface
  val statePersistence: StatePersistanceServiceInterface
}):
  def registerAggregate[A : JsonValueCodec : Bottom : Lattice](
    id: String
  ): Facade[A] =
    val actionsEvt = Evt[A => A]()
    val deltaEvt = Evt[A]()

    val changes = services.statePersistence.storeAggregateSignal[DeltaContainer[A]](id, init =>
      Events.foldAll(init)(state => Seq(
        actionsEvt.act(action => state.mutate(action)),
        deltaEvt.act(delta => state.applyDelta(delta))
      ))
    ).map(_.inner)

    // actions.map(_(changes.now)).observe(pushDelta)

    Facade(
      actionsEvt,
      changes,
    )

  private def pushDelta[A](delta: A) =
    None


    /*
    val rdt = DeltaBufferRDT[A](services.config.replicaID)

    val deltaEvt = Evt[DottedName[A]]()
    val deltaPushedEvt = Evt[Unit]()

    val actions = Evt[A => A]()

    // current state consists only of all actions by usecases and deltas from other replicas
    val rdtSignal = Events.foldAll(rdt)(current =>
      Seq(
        deltaPushedEvt.act(_ => current.resetDeltaBuffer()),
        deltaEvt.act(current.applyDelta(_)),
        actions.act(mutator => current.applyDelta(DottedName(current.replicaID, Dotted(mutator(current.state.store)))))
      )
    )

    rdtSignal.observe { rdt =>
      val deltaStateList = rdt.deltaBuffer.collect {
        case DottedName(replicaID, deltaState) if replicaID == services.config.replicaID => deltaState
      }

      val combinedState = deltaStateList.reduceOption(DecomposeLattice[Dotted[A]].merge)
      combinedState.foreach { state =>
        pushDelta(state)
      }
    }

    // pack all into a single facade to expose minimal interface
    Facade[A](
      actions,
      rdStignal.map(_.state.store)
    )
    */

    /*
    registry.bindSbj(binding) { (remoteRef: RemoteRef, deltaState: Dotted[A]) =>
      deltaEvt.fire(DottedName(remoteRef.toString, deltaState))
    }

    var observers    = Map[RemoteRef, Disconnectable]()
    var resendBuffer = Map[RemoteRef, Dotted[A]]()

    def registerRemote(remoteRef: RemoteRef): Unit = {
      val remoteUpdate: Dotted[A] => Future[Unit] = registry.lookup(binding, remoteRef)

      // Send full state to initialize remote
      val currentState = signal.readValueOnce.state
      if (currentState != bottom.empty) remoteUpdate(currentState)

      // Whenever the crdt is changed propagate the delta
      // Praktisch wäre etwas wie crdt.observeDelta
      val observer = signal.observe { s =>
        val deltaStateList = s.deltaBuffer.collect {
          case DottedName(replicaID, deltaState) if replicaID != remoteRef.toString => deltaState
        } ++ resendBuffer.get(remoteRef).toList

        val combinedState = deltaStateList.reduceOption(DecomposeLattice[Dotted[A]].merge)

        combinedState.foreach { s =>
          val mergedResendBuffer = resendBuffer.updatedWith(remoteRef) {
            case None       => Some(s)
            case Some(prev) => Some(DecomposeLattice[Dotted[A]].merge(prev, s))
          }

          if (remoteRef.connected) {
            remoteUpdate(s).onComplete {
              case Success(_) =>
                resendBuffer = resendBuffer.removed(remoteRef)
              case Failure(_) =>
                resendBuffer = mergedResendBuffer
            }
          } else {
            resendBuffer = mergedResendBuffer
          }
        }
      }

      observers += (remoteRef -> observer)
    }
        
    registry.remoteJoined.monitor(registerRemote)
    registry.remotes.foreach(registerRemote)
    registry.remoteLeft.monitor(observers(_).disconnect())
    */
