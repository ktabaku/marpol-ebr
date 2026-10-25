/**
 * project : capcode file name : Position.java created : 3 janv. 2006 by :
 * cyrille
 */
/**
 * CVS markups
 * last update by : $Author: crosay $
 * $Date: 2009-12-15 12:24:22 $
 * $Header: /media/disk/capcodeCVSbackup/capcode.basics/src/capcode/maps/position/Position.java,v 1.19 2009-12-15 12:24:22 crosay Exp $
 * $Log: not supported by cvs2svn $
 * Revision 1.18  2009/11/02 00:10:37  crosay
 * Incomplete # add AIS capability
 * http://sourceforge.net/apps/trac/capcode/ticket/42
 * Complete # required by ticket 6: install a change waypoint event mechanism
 * http://sourceforge.net/apps/trac/capcode/ticket/37
 * Complete # when importing a polar, ask also to save it under capcode format.
 * http://sourceforge.net/apps/trac/capcode/ticket/40
 * Incomplete # wordwind plugin for capcode
 * http://sourceforge.net/apps/trac/capcode/ticket/6
 *
 * Revision 1.17  2009/08/15 15:13:11  crosay
 * Complete # add reference points to maps
 * http://sourceforge.net/apps/trac/capcode/ticket/22
 *
 * Revision 1.16  2009/07/28 10:55:38  crosay
 * Complete # update the dispay algorithm for the boat trace
 * http://sourceforge.net/apps/trac/capcode/ticket/12
 *
 * Revision 1.15  2009/03/16 17:13:05  crosay
 * Complete - taskexternalise strings
 *
 * Revision 1.14  2009/03/04 10:15:55  crosay
 * Complete - taskdisplay the equipments connected to PC
 *
 * Complete - taskexternalise strings
 *
 * Incomplete - tasknew plugin: bearing-speed-and log
 *
 * Revision 1.13  2009/01/13 18:04:49  crosay
 * correction of code after unit test:
 * - SystemUtils: added exception in case of property not found
 * - SpeedPolar, SpeedPolarFile, WindandSpeedProcess: management of this exception
 *
 * Correction of a bug in Coords management (DMS function)
 *
 * Revision 1.12  2008/12/19 16:04:59  crosay
 * Complete - task optimisation of map reading
 *
 * Revision 1.11  2008/10/10 21:03:45  crosay
 * added equal comparator between 2 positions
 *
 * Revision 1.10  2008/09/19 20:44:19  dersaint
 * added a Position creator with 2 doubles (easier for most of the cases)
 *
 * Revision 1.9  2008/09/12 19:31:52  crosay
 * correction of a bug when longitude west
 *
 * Revision 1.8  2008/09/11 15:29:56  crosay
 * task 2042679 (BSB Maps format) continued
 * finalisation
 *
 * Revision 1.7  2008/05/28 16:28:54  crosay
 * added CVS markups in source code
 *
 * $Revision: 1.19 $
 * $State: Exp $
 */
package al.sybina.ebr.core.geo;


import java.awt.geom.Point2D.Double;

import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.proj.Projection;

import al.sybina.ebr.core.common.Messages;
import al.sybina.ebr.core.data.Angle;
import al.sybina.ebr.core.data.EuclideanVector;
import al.sybina.ebr.core.data.Speed;
import al.sybina.ebr.core.data.util.Fields;
import al.sybina.ebr.core.geo.Coords.EStringFormat;
import al.sybina.ebr.core.geo.transform.Orthodromy;
import javafx.geometry.Point2D;


/**
 * @author cyrille This class implements a longitude and a latitude <br>
 * @see al.sybina.ebr.core.geo.Longitude
 * @see al.sybina.ebr.core.geo.Latitude
 */
public class Position {
	private static final String POSITION_SEPARATOR = ":"; //$NON-NLS-1$

	public static final Position ZERO = new Position(0, 0);

	public static final double DEGREE = 1.0;
	public static final double MINUTE = 1/60d;
	public static final double SECONDE = 1/3600d;

	/** longitude of the position */
	private Coords longitude;

	/** latitude of the position */
	private Coords latitude;

	/**
	 * Creates a position based on a latitude and a longitude<br>
	 *
	 * @param lat
	 * @param lon
	 */
	public Position(Coords lat, Coords lon) {
		if ((lat.isLatitude()) && (lon.isLongitude())) {
			setLat(lat);
			setLon(lon);
		} else {
			// its a mistake but we can correct it
			setLat(lon);
			setLon(lat);
		}
	}

	/**
	 * checks for user input error (lon for lat and lat for lon) reverse if
	 * needed.
	 */
	private void check() {
		// check for user error
		if (latitude.isLongitude() && longitude.isLatitude()) {
			String oldLat = latitude.toString();
			setLat(longitude);
			setLon(new Coords(oldLat));
		}
	}

	/**
	 * Creates a position based on a latitude and a longitude given in String<br>
	 *
	 * @param lat
	 * @param lon
	 * @throws Exception
	 */
	public Position(String aLat, String aLon) {
		setLat(new Coords(aLat));
		setLon(new Coords(aLon));
		check();
	}

	/**
	 * Creates a position based on a latitude/longitude given in one String<br>
	 * the position string must have the following format: "latitude<b>:</b>longitude"
	 * eg : "N45.2345/E003,1234" in case of bad format, a default Position is
	 * created (N0.0, E0.0, somewhere along greenwitch and equator...
	 *
	 * @param position
	 *            String
	 */
	public Position(String aPos) {
		Fields f = new Fields(aPos, POSITION_SEPARATOR);
		if (f.length() == 2) {
			setLat(new Coords(f.atPos(0)));
			setLon(new Coords(f.atPos(1)));
			check();
		} else {
			setLat(new Latitude(0, false));
			setLon(new Longitude(0, false));
		}
	}

	public Position(double lat, double lon) {
		this(new Latitude(lat), new Longitude(lon));
	}

	public Position(Angle lat, Angle lon) {
		this(lat.toDegrees(), lon.toDegrees());
	}

	/**
	 * set the new values of the position
	 *
	 * @param lat
	 * @param lon
	 */
	public void set(Coords lat, Coords lon) {
		latitude.setAsDegree(lat.toDegrees());
		latitude.setValid(lat.isValid());
		longitude.setAsDegree(lon.toDegrees());
		longitude.setValid(lon.isValid());
	}

	/**
	 * set the new values of the position as string
	 *
	 * @param meaning
	 */
	public void set(String value) {
		Fields f = new Fields(value, POSITION_SEPARATOR);
		if (f.length() == 2) {
			setLat(new Coords(f.atPos(0)));
			setLon(new Coords(f.atPos(1)));
			check();
		} else {
			setLat(new Coords());
			setLon(new Coords());
		}
	}

	/**
	 *
	 * @return the latitude of the position
	 */
	public Coords getLat() {
		return latitude;
	}

	/**
	 * set the new lat of the position
	 *
	 * @param lat
	 */
	public void setLat(Coords lat) {
		this.latitude = lat;
	}

	/**
	 * @return the longitude of the Position
	 */
	public Coords getLon() {
		return longitude;
	}

	/**
	 * set the new longitude of the position
	 *
	 * @param lon
	 */
	public void setLon(Coords lon) {
		this.longitude = lon;
		// particular case where an empty string is passed as a longitude
		// it may be considered as a latitude (default behavior of Coords)
		// this code corrects it
		if (lon.isNorth() && (lon.value == 0)) {
			// force to a latitude
			longitude.setAsDegree(0, Cardinal.E);
		}
	}

	/**
	 * return a String of the position in longitude/latitude format
	 */
	@Override
	public String toString() {
		return latitude.toString() + POSITION_SEPARATOR + longitude.toString();
	}

	/**
	 *
	 * @param format (see STRING_FORMAT in Coords)
	 * @return a string with lat/lon as string, depending on the format
	 */
	public String toString(EStringFormat format) {
		return latitude.toString(format) + POSITION_SEPARATOR + longitude.toString(format);
	}

	/**
	 *
	 * @param from
	 *            position
	 * @return the latitude variation between the 2 positions
	 */
	public double getDeltaLatInDeg(Position from) {
		double l1 = latitude.toDegrees();
		double l2 = from.getLat().toDegrees();
		return l2 - l1;
	}

	/**
	 *
	 * @param from
	 *            position
	 * @return the longitude variation between the 2 positions
	 */
	public double getDeltaLonInDeg(Position from) {
		double d1 = longitude.toDegrees();
		double d2 = from.getLon().toDegrees();
		double res = d2 - d1;
		if (isCrossingDayNightLine(from)){
			res = 360 - Math.abs(res);
		}
		return res;
	}

	/**
	 * return true if between this position and from, the line cut the meridien 180�.
	 * @param from
	 * @return
	 */
	public boolean isCrossingDayNightLine(Position from){
		boolean res = false;
		double l1 = longitude.toDegrees();
		double l2 = from.getLon().toDegrees();
        if (Math.signum(l1) != Math.signum(l2)){
            double delta = Math.abs(l2 - l1);
            res = delta > 180 && delta < 360;
        }
		return res;
	}

	/**
	 *
	 * @param from
	 *            position
	 * @return the latitude variation between the 2 positions
	 */
	public double getDeltaLatInMinutes(Position from) {
		return getDeltaLatInDeg(from) * 60;
	}

	/**
	 *
	 * @param from
	 *            position
	 * @return the longitude variation between the 2 positions
	 */
	public double getDeltaLonInMinutes(Position from) {
		return getDeltaLonInDeg(from) * 60;
	}

	@Override
	public boolean equals(Object o){
		boolean res = false;
		if (o instanceof Position){
			Position p = (Position)o;
			res = (latitude.toDegrees() == p.getLat().toDegrees()
					&& longitude.toDegrees() == p.getLon().toDegrees());
		}
		return res;
	}

	public boolean equals(Object o, double precision){
		boolean res = false;
		if (o instanceof Position){
			Position p = (Position)o;
			res = (Math.abs(latitude.toDegrees() - p.getLat().toDegrees()) <= precision
					&& Math.abs(longitude.toDegrees() - p.getLon().toDegrees()) <= precision);
		}
		return res;
	}

	/**
	 * returns true if the position is inside the sector.
	 * @param wholeMapSector
	 * @return
	 */
	public boolean isInside(Sector sector) {
		return Sector.isPositionInsideSector(sector, getLon().toDegrees(), getLat().toDegrees());
	}

	/**
	 * compute the position of the boat at a given time on a orthodromy.
	 * @param position : the actual position of the boat
	 * @param t : the time in hour
	 * @param v : the boat vector (angle and speed)
	 * @return
	 */
	public static Position computeDestination(Position position, double t, EuclideanVector v) {
		return computeDestination(position, t, v.getSpeed(), v.getAngle());
	}

	/**
	/**
	 * compute the position of the boat at a given time on a orthodromy.
	 * @param position : the actual position of the boat
	 * @param t : the time in hour
	 * @param s : the speed of the mobile
	 * @param a : the bearing of the mobile
	 * @return
	 */
	public static Position computeDestination(Position position, double t, Speed s, Angle a) {
		//distance in nautical miles (or minute of angle on a great circle)
		double distance = s.getValue() * t;
		return computeDestination(position, a.toRadian(), distance);
	}

	/**
	 * numer of hours needed by a boat to goes from point 1 to point 2 at a given speed.
	 * It is supposed that the boat is aiming directly to point 2...
	 * @param p1 : origin
	 * @param p2 : destination
	 * @param speed : in knots
	 * @return the time in hours to reach the destination
	 */
	public static double timeToDestination(Position p1, Position p2, Speed speed) {
		return p1.getDistanceFrom(p2) / speed.getValue();
	}

	/**
	 * return the orthodromic distance in nautical miles between 2 this position and from.
	 * @param from
	 * @return
	 */
	public double getDistanceFrom(Position from) {
		return new Orthodromy(from, this).getDistance();
	}

	@Override
	public Position clone() {
		return new Position(latitude.value, longitude.value);
	}

	public boolean isValid() {
		return latitude.isValid() && longitude.isValid();
	}

	public void set(Position from) {
		set(from.latitude, from.longitude);
	}

	/**
	 * compute the position on a general mercantor earth projection.
	 * used in collision detector.
	 * @param position
	 * @return
	 * @throws Exception
	 */
	public static Point2D getPointOnProjection(Projection proj, Position position) {
		ProjCoordinate pj1 = new ProjCoordinate(position.getLon().value, position.getLat().value);
		ProjCoordinate pj2 = new ProjCoordinate();
		if (proj != null){
			proj.project(pj1, pj2);
			return new Point2D(pj2.x, pj2.y);
		}
		else{
			return null;
		}
	}

	public static Position getPositionFromProjectedPoint(Projection proj, Point2D collPoint){
		ProjCoordinate pj1 = new ProjCoordinate(collPoint.getX(), collPoint.getY());
		ProjCoordinate pj2 = new ProjCoordinate();
		if (proj != null){
			proj.inverseProject(pj1, pj2);
			return new Position(pj2.y, pj2.x);
		}
		return null;
	}


	public Point2D toPoint2D() {
		return new Point2D(longitude.value, latitude.value);
	}
	
	public Double toAWTPoint2D() {
		return new java.awt.geom.Point2D.Double(longitude.value, latitude.value);
	}

	public static Position computeDestination(Position from, double routeRd, double dst) {
        double lat = from.getLat().toRadian();
        double lon = from.getLon().toRadian();
        double distance = Math.toRadians(dst / 60);
        if (distance == 0){
            return from;
        }
        double endLatRadians = Math.asin(Math.sin(lat) * Math.cos(distance)
            + Math.cos(lat) * Math.sin(distance) * Math.cos(routeRd));
        double endLonRadians = lon + Math.atan2(
            Math.sin(distance) * Math.sin(routeRd),
            Math.cos(lat) * Math.cos(distance) - Math.sin(lat) * Math.sin(distance) * Math.cos(routeRd));
        return new Position(
            Math.toDegrees(endLatRadians),
            Math.toDegrees(endLonRadians));
	}
	
/**
 * round the value of the position to either the degree, the minute of the second.
 * @param at : Coords.ERoundTo 
 */
	public void round(Coords.ERoundTo roundTo) {
		switch (roundTo){
		case DEG:
			longitude.round(0);
			break;
		case MIN:
			longitude.round(2);
			break;
		case SEC:
			longitude.round(4);
			break;
			default:
		}
	}


}
