/**
 *
 * @author c.rosay
 *
 */
/**
 * CVS markups
 * last update by : $Author: crosay $
 * $Date: 2009-11-02 00:10:37 $
 * $Header: /media/disk/capcodeCVSbackup/capcode.basics/src/capcode/maps/position/Coords.java,v 1.18 2009-11-02 00:10:37 crosay Exp $
 * $Log: not supported by cvs2svn $
 * Revision 1.17  2009/08/25 22:33:59  crosay
 * Complete # add reference points to maps
 * http://sourceforge.net/apps/trac/capcode/ticket/22
 * Incomplete # move the display of the VMG arrow from map display plugin to polar display plugin
 * http://sourceforge.net/apps/trac/capcode/ticket/29
 *
 * Revision 1.16  2009/03/31 07:43:12  crosay
 * Incomplete - taskSourceForge.net: CapCode-software suite for sailors: Detail: 2705654 - NMEA data mapping (FR 2605966 Input Mapping)
 * https://sourceforge.net/tracker2/index.php?func=detail&aid=2705654&group_id=100698&atid=1045270
 *
 * Revision 1.15  2009/03/04 10:15:55  crosay
 * Complete - taskdisplay the equipments connected to PC
 *
 * Complete - taskexternalise strings
 *
 * Incomplete - tasknew plugin: bearing-speed-and log
 *
 * Revision 1.14  2009/02/07 17:20:40  crosay
 * Incomplete - taskcheck correct behaviour of new speed capture mechanism
 * Complete - tasktask 2577326 - helmsman display: auto scale
 * https://sourceforge.net/tracker2/?func=detail&aid=2577326&group_id=100698&atid=1045270
 *
 * Revision 1.13  2009/01/13 18:04:49  crosay
 * correction of code after unit test:
 * - SystemUtils: added exception in case of property not found
 * - SpeedPolar, SpeedPolarFile, WindandSpeedProcess: management of this exception
 *
 * Correction of a bug in Coords management (DMS function)
 *
 * Revision 1.12  2008/09/12 19:31:52  crosay
 * correction of a bug when longitude west
 *
 * Revision 1.11  2008/09/11 15:29:56  crosay
 * task 2042679 (BSB Maps format) continued
 * finalisation
 *
 * Revision 1.10  2008/05/28 16:28:54  crosay
 * added CVS markups in source code
 *
 * $Revision: 1.18 $
 * $State: Exp $
 */

package al.sybina.ebr.core.geo;

import java.util.Locale;

import al.sybina.ebr.core.data.Data;
import al.sybina.ebr.core.data.util.Fields;


/**
 * this class embbed the logic to compute longitude and latitude transforms.<br>
 * any geographic point may be represented by a latitude/longitude:<br>
 * latitude : from -90 (south pole) 0 (equator) to +90 north pole<br>
 * longitude: from -180 (far west) O (Greenwich) to +180 (far east, but the same point than -180)<br>
 * this class manages both Latitude and longitude and in particular the way to read
 * and understand the data. Coords can be given in:<br>
 * degrees: 45.25<br>
 * minutes: 2700.25 (same than precedent)<br>
 * as a string with different format (see CoordPatterns)
 */
public class Coords extends Data{
	private static final long serialVersionUID = 3497414997278581268L;
	public enum EStringFormat {
		DEG, SHORT_DMS, LONG_DMS, NMEA,
	}
	public enum ERoundTo {
		DEG, MIN, SEC
	}

	private boolean latitude;
	private boolean longitude;
	private int sign = 1; // used to set a neg angle when south or west
	private class DMS{
		double deg;
		double min;
		double sec;
	};

	// STATIC CREATORS-----------------------------------------------
	/**
	 * Set a Coords in degrees : DDD.ddmmm <br>
	 * where:<br>
	 * DDD degrees<br>
	 * dd minutes tenths og degree<br>
	 * mmm tenths of minute<br>
	 *
	 * @param aDeg
	 * @param u
	 *            Cardinal (N, S, E, W). If N is set and the meaning is negative,
	 *            then it will be switched to S same for longitude (E, W)
	 * @return a new Coords
	 */
	public static Coords createsFromDegrees(double aDeg, Cardinal u) {
		return new Coords(aDeg, u);
	}

	/**
	 * Set a Coords in minutes : MMMM.dddd <br>
	 * where:<br>
	 * MMMM minutes of angle<br>
	 * dddd tenths of minute<br>
	 *
	 * @param aDeg
	 * @param u
	 * @return a new Coords
	 */
	public static Coords createsFromMinutes(double aMin, Cardinal u) {
		return new Coords(aMin / 60.0d, u);
	}

	/**
	 * Create a longitude/latitude from a string given as DDMM.SS (lat) or
	 * DDDMM.SS (lon) as per Garmin format or as DD�MM'SS" or as DD�MM,mmmm mmmm
	 * are fraction of minutes
	 *
	 * @param dms
	 * @throws Exception
	 * @see #setAsString
	 */
	public static Coords createFromString(String dms) throws Exception {
		return new Coords(dms);
	}

	// DYNAMIC CREATORS-----------------------------------------------
	/**
	 * Creates an empty Coords with default meaning of:<br>
	 * 0 deg, 0 min, 0 sec North
	 */
	public Coords() {
		this(0.0d, Cardinal.N);
		valid = false;
	}

	/**
	 * creates a clone Coords from c
	 *
	 * @param c
	 */
	public Coords(Coords c) {
		this(c.value, c.getUnit());
		valid = c.isValid();
	}

	/**
	 * creates a Coords object given the meaning of the angle in degrees eg :
	 * N03�30'00 would be new Coords(3.5, N)
	 *
	 * @param d :
	 *            eg 3.45
	 * @param u :
	 *            eg N
	 */
	public Coords(double d, Cardinal u) {
		super();
		setUnit(u);
		setValue(d);
	}

	/**
	 * creates a Coords with deg = d, min = m, sec = s, unit = u <br>.
	 *
	 * @param d
	 * @param m
	 * @param s
	 * @param u
	 */
	public Coords(int d, int m, double s, Cardinal u) {
		super();
		setUnit(u);
		setAsDMS(d, m, s);
	}

	/**
	 * Create a longitude/latitude from a string given as DDMM.SS (lat) or
	 * DDDMM.SS (lon) as per Garmin format or as DD�MM'SS" or as DD�MM,mmmm mmmm
	 * are fraction of minutes
	 *
	 * @param dms
	 * @throws Exception
	 * @see #setAsString
	 */
	public Coords(String dms) {
		super();
		setAsString(dms);
	}


	/* (non-Javadoc)
	 * @see al.sybina.ebr.data.Data#set(java.lang.String)
	 */
	@Override
	public void set(String s) {
		setAsString(s);
	}

	/* (non-Javadoc)
	 * @see al.sybina.ebr.data.Data#setUnit(java.lang.String)
	 */
	@Override
	public void setUnit(String u) {
		setUnit(Cardinal.valueOf(u));
	}

	/**
	 * sets the Coords with deg = d, min = m, sec = s<br>
	 *
	 * @param d
	 * @param m
	 * @param s
	 */
	private void setAsDMS(int aDeg, int aMin, double aSec) {
		setValue(((aSec / 60)+aMin)/60 + aDeg);
	}

	/**
	 * add aDeg degrees to the Coordinates
	 *
	 * @param aDeg
	 */
	public void addDegrees(double moreDeg) {
		setValue(toDegrees() + moreDeg);
	}

	/**
	 * add aMin minutes to the Coordinates
	 *
	 * @param aMin
	 */
	public void addMinutes(double moreMin) {
		value = validate(value + (moreMin / 60.0d));
	}

	/**
	 * add seconds of arc to the Coordinates.
	 * @param d
	 */
	public void addSeconds(double moreSec) {
		value = validate(value + (moreSec / 3600.0d));
	}

	/**
	 * add a Coords meaning to this Coords<br>
	 * Coords have to be both longitude, or latitude<br>
	 *
	 * @param c
	 */
	@Override
	public Data addTo(Data c) {
		value = validate(value + c.value);
		return this;
	}

	/**
	 * @return Returns the latitude.
	 */
	public boolean isLatitude() {
		return latitude;
	}

	/**
	 * @return Returns the longitude.
	 */
	public boolean isLongitude() {
		return longitude;
	}

	/**
	 * @return true if the Coord is EAST
	 */
	public boolean isEast() {
		return isLongitude() && (value >= 0);
	}

	/**
	 * @return true if the Coord is NORTH
	 */
	public boolean isNorth() {
		return isLatitude() && (value >= 0);
	}

	/**
	 * @return true if the Coord is SOUTH
	 */
	public boolean isSouth() {
		return isLatitude() && (value < 0);
	}

	/**
	 * @return true if the Coord is WEST
	 */
	public boolean isWest() {
		return isLongitude() && (value < 0);
	}

	/**
	 * Set a Coords in degrees : DDD.ddmmm <br>
	 * where:<br>
	 * DDD degrees<br>
	 * dd minutes tenths og degree<br>
	 * mmm tenths of minute<br>
	 *
	 * @param aDeg
	 */
	@Override
	public void setValue(double v) {
		value = sign * validate(v);
	}

	/**
	 * v is a float representation of DD.MMSSSS<br>
	 * DD = degrees <br>
	 * MM = minutes of angle <br>
	 * SSSS = seconds of angle <br>
	 *
	 * @param v
	 */
	public void setAsFloatDMS(double v) {
		double sign = Math.signum(v);
		double d = Math.abs((long) (v));
		double x = Math.abs(v) - d;
		x = toDecimal(x);
		d = sign * (d + x);
		setValue(d);
	}

	/**
	 * Set a Coords in degrees : DDD.DDDD <br>
	 * get the absolute meaning of aDeg and sign the result depending on aUnit<br>
	 * W and S -> negative <br>
	 * E and N : positive<br>
	 *
	 * @param aDeg
	 * @param aUnit
	 */
	public void setAsDegree(double aDeg, Cardinal u) {
		setUnit(u);
		setValue(Math.abs(aDeg));
	}

	/**
	 * Set a Coords in degrees : DDD.DDD <br>
	 *
	 * @param v
	 */
	public void setAsDegree(double v) {
		value = validate(v);
	}

	/**
	 * Set a Coords in minutes : MMMM.dddd <br>
	 * where:<br>
	 * MMMM minutes of angle<br>
	 * dddd tenths of minute<br>
	 *
	 * @param v
	 *            in minutes of angle
	 */
	public void setAsMinutes(double v) {
		setValue(v / 60.0d);
	}

	/**
	 * Set a longitude or a latitude from a string given as an available
	 * CoordPattern
	 *
	 * @param sequence
	 * @see CoordPattern
	 */
	public void setAsString(String sequence) {
		valid = true;
		Cardinal u;
		sequence = sequence.trim().replaceAll(" ", "");
		int pattern = CoordPattern.getPattern(sequence);
		String[] s = CoordPattern.split(sequence, pattern);
		Fields fields = new Fields(s);
		char unit = sequence.isEmpty() ? 'N' : (pattern == CoordPattern.RES_PATTERN_DMD2)
					|| (pattern == CoordPattern.RES_PATTERN_GOOGLE_EARTH1)?
							sequence.charAt(sequence.length()-1):
								sequence.charAt(0);
		try {
			u = Cardinal.valueOf(String.valueOf(unit));
		} catch (IllegalArgumentException e) {
			u = Cardinal.N;
			valid = false;
		}
		setUnit(u);
		switch (pattern) {
		case CoordPattern.RES_PATTERN_DMD:
			setAsDMS(fields.getAsInt(1), fields.getAsInt(2), fields.getAsDouble(3));
			break;
		case CoordPattern.RES_PATTERN_DMD2:
		case CoordPattern.RES_PATTERN_GOOGLE_EARTH1:
			setAsDMS(fields.getAsInt(0), fields.getAsInt(1), fields.getAsDouble(2));
			break;
		case CoordPattern.RES_PATTERN_DEG2:
		case CoordPattern.RES_PATTERN_DEG:
			setValue(fields.getAsDouble(1));
			break;
		case CoordPattern.RES_PATTERN_DMM:
			setValue(fields.getAsDouble(1) + (fields.getAsDouble(2) / 60.0d));
			break;
		case CoordPattern.RES_PATTERN_DMM_NO_DEGSEP:
			double d = Math.floor(fields.getAsDouble(1) / 100.0d);
			double m = fields.getAsDouble(1) - 100 * d;
			setValue(d + (m / 60.0d));
			break;
		case CoordPattern.RES_PATTERN_DMS:
			setAsDMS(fields.getAsInt(1), fields.getAsInt(2), fields
					.getAsDouble(3));
			break;
		default: {
			this.setAsDMS(0, 0, 0);
			valid = false;
		}
		}
	}

	/**
	 * set the Coords to a latitude type<br>
	 * <unset the longitude type)<br>
	 *
	 * @param latitude
	 *            The latitude to set.
	 */
	public void setLatitude(boolean l) {
		this.latitude = l;
		this.longitude = !l;
	}

	/**
	 * set the Coords to a longitude type<br>
	 * <unset the latitudetype)<br>
	 *
	 * @param longitude
	 *            The longitude to set.
	 */
	public void setLongitude(boolean l) {
		this.latitude = !l;
		this.longitude = l;
	}

	private void setUnit(Cardinal c) {
		longitude = (c.equals( Cardinal.W )) || (c.equals( Cardinal.E ));
		latitude = (c.equals( Cardinal.N )) || (c.equals( Cardinal.S ));
		// angle is negative when W or south
		sign = ((c.equals( Cardinal.W )) || (c.equals( Cardinal.S ))) ? -1 : 1;
	}

	/**
	 * @tag capcode.coordinate.NESW
	 * @return
	 */
	@Override
	public Cardinal getUnit() {
		Cardinal res = Cardinal.E;
		// @tag capcode.derived: if angle = 0, by default is East
		if (isLongitude()) {
			res = (value >= 0) ? Cardinal.E : Cardinal.W;
		}
		// @tag capcode.derived: if angle = 0, by default is North
		else if (isLatitude()) {
			res = (value >= 0) ? Cardinal.N : Cardinal.S;
		}
		return res;
	}

	/**
	 * transform the coord into a float format in radian DDD.ddmmm where DDD is
	 * the radian ddmmm is a decimal fraction of radian
	 *
	 */
	public double toRadian() {
		return Math.toRadians(value);
	}

	public double toDegrees() {
		return value;
	}

	/**
	 * transform the coord into a float format in minutes of degrees: MMM.SS
	 */
	public double toMinutes() {
		return value * 60.0d;
	}

	/**
	 * transform the coord into a float format in minutes of degrees: MMM.SS
	 */
	public double toSeconds() {
		return value * 3600.0d;
	}

	/**
	 * transform the coord into a string with the format:<br>
	 * [N,s,E,W]DDD.ddd were the angle is expressed in DEG<br>
	 * [N,s,E,W]DDD�mm'ss.ss were the angle is expressed in LONG_DMS<br>
	 */
	public String toString(EStringFormat format) {
		String res = "unknown format";
		DMS dms = this.toDMS();

		switch (format) {
		case DEG:
			res = String.format(Locale.US, "%s%07.04f", getUnit(), Math.abs(value)); //$NON-NLS-1$
			break;
		case SHORT_DMS:
			res = longitude ? String.format(Locale.US, "%s%03d°%02d'%02d", //$NON-NLS-1$
					getUnit(), (int) Math.abs(dms.deg), (int) dms.min, (int) dms.sec)
					: String.format(Locale.US, "%s%02d°%02d'%02d", getUnit(), //$NON-NLS-1$
							(int) Math.abs(dms.deg), (int) dms.min, (int) dms.sec);
			break;
		case LONG_DMS:
			res = longitude ? String.format(Locale.US, "%s%03d°%02d'%06.03f", //$NON-NLS-1$
					getUnit(), (int) Math.abs(dms.deg), (int) dms.min, dms.sec) : String
					.format(Locale.US, "%s%02d°%02d'%06.03f", getUnit(), //$NON-NLS-1$
							(int) Math.abs(dms.deg), (int) dms.min, dms.sec);
			break;
		case NMEA:
			res = longitude ? String.format(Locale.US, "%03d%06.3f,%s", //$NON-NLS-1$
					(int) Math.abs(dms.deg), this.getMinAsFloat(), getUnit())
					: String.format(Locale.US, "%02d%06.3f,%s", (int) Math //$NON-NLS-1$
							.abs(dms.deg), this.getMinAsFloat(), getUnit());
			break;
		}
		return res;
	}

	/**
	 * transform the coord into a string with the format:<br>
	 * [N,s,E,W]DDD.ddd were the angle is expressed in decimal<br>
	 */
	@Override
	public String toString() {
		return toString(EStringFormat.DEG);
	}

	private double validate(double v) {
		double res = 0;
		if (isLatitude()) {
			v = v % 180;
			res = v > 90 ? 180 - v : v < -90 ? -180 - v : v;
		}
		else if (isLongitude()) {
			v = v % 360;
	        res =  v > 180 ? v - 360 : v < -180 ? 360 + v : v;
		}
		return res;
	}

	/**
	 * @param x60
	 * @return x60 * 10 / 6
	 */
	private static double toDecimal(double x60) {
		return (x60 * 10.0 / 6.0);
	}

	private DMS toDMS() {
		DMS res = new DMS();
		res.deg = (int)value;
		double delta = value -  res.deg;
		// @tag capcode.derived: used to correct a bug introduced by the double
		// format (loss of precision): the precision is limited to 9th decimal
		double mind = Data.round(Math.abs(delta) * 60.0d, 9);
		res.min = (int)mind;
		res.sec = (mind - res.min) * 60;
		return res;
	};

	/**
	 * returns the degree part (int) of the coordinate.
	 *  (eg: 45,50 or 45°30' return 45)
	 * @return Returns the deg.
	 */
	public int getDeg() {
		return (int) Math.abs(toDMS().deg);
	}

	/**
	 * returns the minutes part (int) of the coordinate.
	 *  (eg: 45,50 or 45�30' return 30)
	 * @return Returns the min.
	 */
	public int getMin() {
		return (int) toDMS().min;
	}

	/**
	 * returns the minutes part (double) of the coordinate.
	 *  (eg: 45,50 or 45°30' return 30.0)
	 * @return Returns the min in double format
	 */
	public double getMinAsFloat() {
		double deg = (int) value;
		return (Math.abs(value - deg) * 60.0d);
	}

	/**
	 * @return Returns the sec.
	 */
	public double getSec() {
		return toDMS().sec;
	}

	/**
	 * set the validity of the coord.
	 * @param v
	 */
	@Override
	public void setValid(boolean v) {
		valid  = v;
	}

	/**
	 * @return true if the coord is valid.
	 */
	@Override
	public boolean isValid() {
		return valid;
	}

	@Override
	public long getAge(){
		return age;
	}
	@Override
	public void setAge(long time) {
		age = time;
	}


}
