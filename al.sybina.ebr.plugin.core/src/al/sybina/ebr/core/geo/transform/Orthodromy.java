/**
 * project : capcode.basics
 * package : al.sybina.ebr.maps.transform
 * created by :cyrille on 25 janv. 2006
 */
/**
 * CVS markups
 * last update by : $Author: crosay $ 
 * $Date: 2009-03-16 17:13:05 $ 
 * $Header: /media/disk/capcodeCVSbackup/capcode.basics/src/capcode/maps/transform/Orthodromy.java,v 1.10 2009-03-16 17:13:05 crosay Exp $
 * $Log: not supported by cvs2svn $
 * Revision 1.9  2009/03/04 10:15:55  crosay
 * Complete - taskdisplay the equipments connected to PC
 *
 * Complete - taskexternalise strings
 *
 * Incomplete - tasknew plugin: bearing-speed-and log
 *
 * Revision 1.8  2009/02/07 17:20:40  crosay
 * Incomplete - taskcheck correct behaviour of new speed capture mechanism
 * Complete - tasktask 2577326 - helmsman display: auto scale
 * https://sourceforge.net/tracker2/?func=detail&aid=2577326&group_id=100698&atid=1045270
 *
 * Revision 1.7  2008/10/10 21:05:24  crosay
 * robustness with NaN
 *
 * Revision 1.6  2008/09/17 11:11:48  crosay
 * change the side of computation (from , to)
 *
 * Revision 1.5  2008/05/28 16:28:55  crosay
 * added CVS markups in source code
 *
 * $Revision: 1.10 $ 
 * $State: Exp $ 
 */

package al.sybina.ebr.core.geo.transform;

import al.sybina.ebr.core.geo.Position;

/**
 * This class give the route and distance between 2 points on a great Earth circle<br>
 * @author cyrille
 */
public class Orthodromy {
	private final double distance;
	private double route;
	public Position vertex;

	/**
	 * compute the orthodromic distance and route between 2 Positions.<br>
	 * in case of error, the function gives a north for the initial route
	 * @param from : Position of origin
	 * @param to : Position of destination
	 */
	public Orthodromy(Position from, Position to) {
		// Orthodromical distance between 2 points
		double latA = from.getLat().toRadian();
		double latB = to.getLat().toRadian();
		double lonA = from.getLon().toRadian();
		double lonB = to.getLon().toRadian();
		double dLon = lonB - lonA;
		double dLat = latB - latA;
		double cosD = (Math.sin(latA) * Math.sin(latB))
				+ (Math.cos(latA) * Math.cos(latB) * Math.cos(dLon));
		distance = Math.toDegrees(Math.acos(cosD)) * 60.0d;
		if (dLon == 0){
			route = dLat > 0 ? 0 : 180;
		}
		else if (dLat == 0){
			route = dLon > 0 ? 90 : 270;
		}
		else{
			// initial route to destination
			double cotanV = (Math.sin(latA) / Math.tan(dLon))
					- ((Math.cos(latA) * Math.tan(latB)) / Math.sin(dLon));
			route = Math.atan(1 / cotanV);
			double latVertex = Math.acos(Math.sin(route)* Math.cos(latA));
			//initial route in degrees
			route = Math.abs(Math.toDegrees(route));
			//correction of the initial route, based on the vertex and initial E-W direction
			if (dLon>0){
				//rougthly going EAST
				if ((Math.abs(latVertex) < Math.PI/2)){
					route =  180 - route;
				}
				//else do nothing
			}
			else{
				//rougthly going WEST
				if ((Math.abs(latVertex) >= Math.PI/2)){
					route =  180 + route;
				}
				else{
					route = 360 - route;					
				}
			}
			//defensive code, route is set to north when impossible to compute
			if (Double.isNaN(route) || Double.isInfinite(route)){
				route = 0;
			}	
			double routeRd = Math.toRadians(route);
	        vertex = Position.computeDestination(from, routeRd, distance / 2);
		}
	}

	/**
	 * @return Returns the distance in nautical miles between the two points.
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * @return Returns the route.
	 */
	public double getRoute() {
		return route;
	}

}
