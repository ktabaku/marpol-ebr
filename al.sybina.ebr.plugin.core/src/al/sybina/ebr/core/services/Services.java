package al.sybina.ebr.core.services;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import org.eclipse.core.internal.runtime.InternalPlatform;
import org.eclipse.osgi.internal.debug.Debug;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

import al.sybina.ebr.core.Activator;
import al.sybina.ebr.core.dataBinding.DataManager;
import al.sybina.ebr.core.services.tracks.TracksManager;
import al.sybina.ebr.core.services.weather.IWeatherService;
import al.sybina.ebr.core.tools.Log;

@SuppressWarnings("restriction")
public class Services {
	public final static String WARNING_SERVICE_ID = "al.sybina.ebr.service.warning";
	public final static String DATA_CORE_SERVICE_ID = "al.sybina.ebr.service.data";
	public final static String COMM_CORE_SERVICE_ID = "al.sybina.ebr.service.comm";
	public final static String CHARTS_CORE_SERVICE_ID = "al.sybina.ebr.service.charts";
	public final static String TRACKS_CORE_SERVICE_ID = "al.sybina.ebr.service.tracks";
	public final static String WEATHER_CORE_SERVICE_ID = "al.sybina.ebr.service.weather";
	public final static String POLAR_SERVICE_ID = "al.sybina.ebr.service.polar";
	public final static String WAYPOINTS_SERVICE_ID = "al.sybina.ebr.service.waypoints";
	public final static String ROUTES_SERVICE_ID = "al.sybina.ebr.service.route";
	public static final String WORLDWIND_SERVICE_ID = "al.sybina.ebr.service.worldwind";
	public static final String AIS_SERVICE_ID = "al.sybina.ebr.service.ais";
	public static final String SYMBOLS_SERVICE_ID = "al.sybina.ebr.service.symbols";
	public static final String LOCATION_CORE_SERVICE_ID = "al.sybina.ebr.service.locations";
	public static final String ROUTER_SERVICE_ID = "al.sybina.ebr.service.router";
	public static final String PASSAGE_PLAN_SERVICE_ID = "al.sybina.ebr.service.passage_plan";
	
	public final static String CHARTS_SERVICE_INSTANCE_CREATED = "chartsServiceInstanceCreated";
	
	private static IChartsService chartsService;
	private static ICommService commService;
	private static IDataService dataService;
	private static ITracksService tracksService;
	private static IPolarService polarService;
	private static IRouteService routeService;
	private static IRouterService routerService;
	private static IWaypointsService waypointsService;
	private static I3DService worldWindService;
	private static IAisService aisService;
	private static IWarningService warningService;	
	private static ISymbolsService symbolsService;
	private static IWeatherService weatherService;
	private static ILocationService locationService;
	private static Hashtable<String, Vector<IServiceListener>> serviceListeners;
	
	/**
	 * add a service listener that will listen to state change on a service given by its ID.
	 * when the service is created, an ServiceEventCreated is fired.
	 * @param id
	 * @param listener
	 */
	public static void addServiceListener(String id, IServiceListener listener){
		//try to get the list of service listeners
		if (serviceListeners == null){
			//does not exist yet, create it
			serviceListeners = new Hashtable<String, Vector<IServiceListener>>();
		}
		//check if already existing listeners for this service
		Vector<IServiceListener> thisServiceListeners = serviceListeners.get(id);
		if (thisServiceListeners == null){
			//no, create the list
			thisServiceListeners = new Vector<IServiceListener>();
		}
		//add the listener to the list
		thisServiceListeners.add(listener);
		//put the listener list into the serviceListeners table
		serviceListeners.put(id, thisServiceListeners);
		//if the service is already started fire a ServiceEventCreated to the new listener, in case it missed it
		try {
			Object service = getIbrService(id);
			if (service != null){
				listener.serviceStarted(new ServiceEventCreated(id, service));			
			}
		} catch (InvalidSyntaxException e) {
			Log.severe(Activator.getBundle(), "error in service " + id, e);
		}
	}
	
	private static void fireServiceEventCreated(String id, Object service) {
		ServiceEventCreated event = new ServiceEventCreated(id, service);
		//try to get the list of service listeners
		if (serviceListeners != null){
			//check if already existing listeners for this service
			Vector<IServiceListener> thisServiceListeners = serviceListeners.get(id);
			if (thisServiceListeners != null){
				//fire the event
				for (IServiceListener l : thisServiceListeners){
					l.serviceStarted(event);
				}
			}
		}
	}
	

	
	/**
	 * @return the last registered IRouteService or null if none was registered.<br>
	 * a route service is able to provide the actual route (list of waypoints), load or save routes etc.<br>
	 */
	public static IRouteService getRouteService() {
		if (routeService == null){
			try {
				routeService = (IRouteService) getIbrService(ROUTES_SERVICE_ID);
			} catch (InvalidSyntaxException e) {
				return null;
			}
		}
		return routeService;
	}
	
	/**
	 * @return the last registered IRouterService or null if none was registered.<br>
	 * a router service is able to compute a route from a point to a destination<br>
	 * or along an already existing route, depending on the weather and the boat polar.<br>
	 */
	public static IRouterService getRouterService() {
		if (routerService == null){
			try {
				routerService = (IRouterService) getIbrService(ROUTER_SERVICE_ID);
			} catch (InvalidSyntaxException e) {
				return null;
			}
		}
		return routerService;
	}
	
	/**
	 * @return the last registered IWaypointsService or null if none was registered.
	 */
	public static IWaypointsService getWaypointsService() {
		if (waypointsService == null){
			try {
				waypointsService = (IWaypointsService) getIbrService(WAYPOINTS_SERVICE_ID);
			} catch (InvalidSyntaxException e) {
				return null;
			}
		}
		return waypointsService;
	}
	
	
	/**
	 * @return the last registered IChartsService or null if none was registered.
	 */
	public static IChartsService getChartsService(){
		if (chartsService == null){
			try {
				chartsService = (IChartsService) getIbrService(CHARTS_CORE_SERVICE_ID);
			} catch (InvalidSyntaxException e) {
				return null;
			}
		}
		return chartsService;
	}

	/**
	 * @return the last registered IPolarService or null if none was registered.
	 */
	public static IPolarService getPolarService(){
		if (polarService == null){
			try {
				polarService = (IPolarService) getIbrService(POLAR_SERVICE_ID);
			} catch (InvalidSyntaxException e) {
				return null;
			}
		}
		return polarService;

	}

	/**
	 * @return the last registered ICommService or null if none was registered.
	 */
	public static ICommService getCommService(){
		if (commService == null){
			try {
				commService = (ICommService) getIbrService(COMM_CORE_SERVICE_ID);
			} catch (InvalidSyntaxException e) {
				return null;
			}
		}
		return commService;

	}

	/**
	 * @return the last registered IDataService or null if none was registered.
	 */
	public static IDataService getDataService() {
		if (dataService == null){
			try {
				dataService = (IDataService) getIbrService(DATA_CORE_SERVICE_ID);
			} catch (InvalidSyntaxException e) {
				return null;
			}
		}
		return dataService;
	}

	/**
	 * @return the last registered ITracksService or null if none was registered.
	 */
	public static ITracksService getTracksService() {
		if (tracksService == null){
			try {
				tracksService = (ITracksService) getIbrService(TRACKS_CORE_SERVICE_ID);
			} catch (InvalidSyntaxException e) {
				return null;
			}
		}
		return tracksService;
	}

	/**
	 * @return the last registered I3DService (worldwind) or null if none was registered.
	 */
	public static I3DService get3DService() {
		if (worldWindService == null){
			try {
				worldWindService = (I3DService) getIbrService(WORLDWIND_SERVICE_ID);
			} catch (InvalidSyntaxException e) {
				return null;
			}
		}
		return worldWindService;
	}

	/**
	 * @return the last registered IAisService or null if none was registered.
	 */
	public static IAisService getAisService() {
		if (aisService == null){
			try {
				aisService = (IAisService) getIbrService(AIS_SERVICE_ID);
			} catch (InvalidSyntaxException e) {
				return null;
			}
		}
		return aisService;
	}
	/**
	 * @return the service managing route and waypoints symbols
	 */
	public static ISymbolsService getSymbolService() {
		if (symbolsService == null){
			try {
				symbolsService = (ISymbolsService) getIbrService(SYMBOLS_SERVICE_ID);
			} catch (InvalidSyntaxException e) {
				return null;
			}
		}
		return symbolsService;
	}

	/**
	 * @return the service managing warnings
	 */
	public static IWarningService getWarningService() {
		if (warningService == null){
			try {
				warningService = (IWarningService) getIbrService(WARNING_SERVICE_ID);
			} catch (InvalidSyntaxException e) {
				return null;
			}
		}
		return warningService;
	}
	public static IWeatherService getWeatherService() {
		if (weatherService == null){
			try {
				weatherService = (IWeatherService) getIbrService(WEATHER_CORE_SERVICE_ID);
			} catch (InvalidSyntaxException e) {
				return null;
			}
		}
		return weatherService;
	}
	
	public static ILocationService getLocationService() {
		if (locationService == null){
			try {
				locationService = (ILocationService) getIbrService(LOCATION_CORE_SERVICE_ID);
			} catch (InvalidSyntaxException e) {
				return null;
			}
		}
		return locationService;
	}


	/**
	 * @param id
	 * @return a registered service givben by its id, or null if the service was not registered.
	 * @throws InvalidSyntaxException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object getIbrService(String id) throws InvalidSyntaxException {		
		if (InternalPlatform.getDefault().isRunning()){
			String filter = String.format("(id=%s)", id);
			ServiceReference[] serviceReferences = InternalPlatform.getDefault().getBundleContext().getAllServiceReferences(null, filter);
			ServiceReference sr = null;
			if (serviceReferences != null && serviceReferences.length > 0){
				sr = serviceReferences[0];
			}
			//return the service
			if (sr != null){
				Bundle definingBundle = sr.getBundle();
				if (definingBundle != null){
					return definingBundle.getBundleContext().getService(sr);
				}			
			}			
		}
		return null;
	}
	
	/**
	 * search if the service is defined in the plugin meta-inf component.xml.
	 * @param id : the id of the service
	 * @return a ServiceReference. 
	 */
	public static ServiceReference<?> searchServiceInPlugins(String id){
		Debug.println("searching " + id + " in ibr plugins");
		Bundle[] bundles = InternalPlatform.getDefault().getBundleContext().getBundles();
		for (Bundle b : bundles) {
			if (b.getSymbolicName().contains("ibr")) {
				ServiceReference<?>[]  services = b.getRegisteredServices();
				if (services != null){
					for(ServiceReference<?> rs : services){
						String name = (String) rs.getProperty("component.name");
						if (name.equals(id)){
								Debug.println(id + " found in plugin " + b.getSymbolicName());
								if (b.getState() != Bundle.ACTIVE){
									try {
										Debug.println(" starting " + b.getSymbolicName());
										b.start();
									} catch (BundleException e) {
										Log.severe(Activator.getBundle(), "could not start plugin " + b.getSymbolicName(), e);
									}
								}
							return rs;								
						}
					}
				}
			}
		}
		return null;		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static boolean isServiceRunning(String id) throws InvalidSyntaxException {
		String filter = String.format("(id=%s)", id);
		ServiceReference[] serviceReferences = InternalPlatform.getDefault().getBundleContext().getAllServiceReferences(null, filter);
		return (serviceReferences != null && serviceReferences.length > 0);
	}

	/**
	 * register a service by its ID and its instance.
	 * @param serviceName : the name of the service (eg. "al.sybina.ebr.services.charts"
	 * @param service : the object implementing the service (must inherit from ICapcodeCoreService)
	 * @return
	 * @throws ServicesException: when the instance is not a ICapcodeCoreService
	 * @throws InvalidSyntaxException
	 */
	public static Object registerIbrService(String serviceName, Object service) throws ServicesException, InvalidSyntaxException{
		if (service instanceof IIbrCoreService){
			return registerService(serviceName, service.getClass(), service);							
		}else{
			throw new ServicesException(serviceName + " is not a " + IIbrCoreService.class.getName());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object registerService(String id, Class clazz, Object service) throws InvalidSyntaxException{
		Dictionary<String, String> dico = new Hashtable<String, String>();
		dico.put("id", id);
		BundleContext bc = InternalPlatform.getDefault().getBundleContext();
		bc.registerService(clazz, service, dico);
		fireServiceEventCreated(id, service);
		return getIbrService(id);
	}


	/**
	 * terminate all services.
	 * @throws InvalidSyntaxException
	 */
	public static void terminateAll() throws InvalidSyntaxException {
		BundleContext bc = InternalPlatform.getDefault().getBundleContext();
		ServiceReference<?>[] serviceReferences = bc.getAllServiceReferences(null, null);
		if (serviceReferences != null){
			for (ServiceReference<?> sr : serviceReferences){
				Bundle bundle = sr.getBundle();
				if (bundle != null){
					if (bundle.getSymbolicName().toLowerCase().contains("ibr")){
						BundleContext bundleContext = bundle.getBundleContext();
						if (bundleContext != null){
							Object service = bundleContext.getService(sr);
							if (service instanceof IIbrCoreService){
								IIbrCoreService capcodeService = (IIbrCoreService) service;
								capcodeService.terminate();
								bundleContext.ungetService(sr);
							}											
						}						
					}
				}
			}			
		}else{
			Debug.println("no service to terminate");	
		}		
	}



}
