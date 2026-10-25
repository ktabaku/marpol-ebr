package al.sybina.ebr.core.services;

import al.sybina.ebr.core.gpx.GpxRouteRecord;

public interface IRouterService extends IIbrCoreService{

	/**
	 * compute a route, based on actual weather grib file and selected boat polar, 
	 * along a route in parameter.<br>
	 * the route may add waypoints to get a better, faster route to destination.<br>
	 * @param route
	 */
	void computeRoute(GpxRouteRecord route);

}
