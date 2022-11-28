package webapp

import webapp.services.*
import webapp.state.*
import webapp.state.services.*
import webapp.device.*
import webapp.device.services.*

// The Services trait contains core services used by usecases, components and pages
// Other Services traits like StateServices can by design only be accessed by other 
// Services. The idea is to abstract away complexity
trait Services:
  lazy val config: ApplicationConfigInterface
  lazy val logger: LoggerServiceInterface

  // State handling should be running from the start to setup connection to server
  // Instatiation starts from StateProvider
  val state: StateProvider
  
  lazy val routing: RoutingService
  lazy val popup: PopupService

object ServicesDefault extends Services, StateServices, DeviceServices:
  // Device
  val applicationInitializer = ApplicationInitializer(this)
  
  lazy val functionsHttpApi = FunctionsHttpApi(this)
  
  // We want socket connection to be established as soon as possible
  val functionsSocketApi = FunctionsSocketApi(this)

  lazy val storage = StorageService(this)

  lazy val window = WindowService(this)

  // Core
  lazy val config = ApplicationConfig(this)
  lazy val logger = LoggerService(this)

  val state = StateProvider(this)

  lazy val routing = RoutingService(this)
  lazy val popup = PopupService(this)
  
  // State
  lazy val aggregateFacadeProvider = AggregateFacadeProvider(this)
  lazy val aggregateFactory = AggregateFactory(this)
  lazy val applicationStateFactory = ApplicationStateFactory(this)
  
  lazy val stateDistribution = StateDistributionService(this)
  lazy val stateStorage = StateStorageService(this)
