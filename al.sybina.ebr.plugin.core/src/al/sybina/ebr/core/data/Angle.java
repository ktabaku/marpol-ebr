/**
 * project : capcode.basics file name : Angle.java created : 15 janv. 2006 by :
 * cyrille
 */
/**
 * CVS markups
 * last update by : $Author: crosay $
 * $Date: 2009-11-02 00:10:37 $
 * $Header: /media/disk/capcodeCVSbackup/capcode.basics/src/capcode/data/Angle.java,v 1.16 2009-11-02 00:10:37 crosay Exp $
 * $Log: not supported by cvs2svn $
 * Revision 1.15  2009/09/13 12:10:39  crosay
 * Incomplete # wordwind plugin for capcode
 * http://sourceforge.net/apps/trac/capcode/ticket/6
 *
 * Revision 1.14  2009/08/31 22:18:08  crosay
 * Incomplete # preparation for delivery to version 0.9.3
 * http://sourceforge.net/apps/trac/capcode/ticket/17
 *
 * Revision 1.13  2009/04/23 10:26:42  crosay
 * Incomplete - taskSourceForge.net: CapCode-software suite for sailors: Detail: 2705654 - NMEA data mapping (FR 2605966 Input Mapping)
 * https://sourceforge.net/tracker2/index.php?func=detail&aid=2705654&group_id=100698&atid=1045270
 * Incomplete - taskzygrib
 *
 * Revision 1.12  2009/03/31 07:42:19  crosay
 * Incomplete - taskSourceForge.net: CapCode-software suite for sailors: Detail: 2705654 - NMEA data mapping (FR 2605966 Input Mapping)
 * https://sourceforge.net/tracker2/index.php?func=detail&aid=2705654&group_id=100698&atid=1045270
 *
 * Revision 1.11  2009/03/04 10:15:55  crosay
 * Complete - taskdisplay the equipments connected to PC
 *
 * Complete - taskexternalise strings
 *
 * Incomplete - tasknew plugin: bearing-speed-and log
 *
 * Revision 1.10  2009/01/29 10:31:28  crosay
 * Incomplete - task add gps hypbrid mode (in true wind computation) task 2533241 GPS speed spare mode
 * https://sourceforge.net/tracker2/?func=detail&aid=2533241&group_id=100698&atid=1045270
 * Complete - task save status and position of toolbars
 * https://sourceforge.net/tracker2/?func=detail&aid=2531530&group_id=100698&atid=1045270
 *
 * Revision 1.9  2009/01/24 20:52:36  crosay
 * Complete - taskRemoval of sqlite
 *
 * Complete - tasksuppression of debug display for preference saving
 *
 * Complete - tasktask 2531747 animation of the wearther
 * https://sourceforge.net/tracker2/?func=detail&aid=2024403&group_id=100698&atid=1045270
 *
 * Revision 1.8  2008/10/10 20:58:15  crosay
 * creation of speedpolar management with  xml files
 *
 * Revision 1.7  2008/05/28 16:28:54  crosay
 * added CVS markups in source code
 *
 * $Revision: 1.16 $
 * $State: Exp $
 */
package al.sybina.ebr.core.data;

import java.util.Locale;

/**
 * class used to work with angles Angles are given in degrees only (no decimals)
 *
 * @author cyrille
 *
 */
public class Angle extends Data {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public static Angle ZERO = new Angle(0);

	public enum AngleOrigin{
		NONE, //no reason to know the origin of the angle
		RELATIVE, // the angle is relative to the boat for ex
		MAGNETIC, // origin is magnetic north
		GEOGRAPHIC, //origin is geographic north (true)
		HYBRID //eg coming from gps
	}

	private AngleOrigin origin = AngleOrigin.NONE;
	/**
	 * create an angle with a v meaning in degrees
	 *
	 * @param v :
	 *            double
	 */
	public Angle(double v) {
		super(0, "°"); //$NON-NLS-1$
		setValue(v);
	}

	public Angle(double v, AngleOrigin origin){
		super(0, "°"); //$NON-NLS-1$
		setOrigin(origin);
	}
	public Angle(double value, boolean valid) {
		this(value);
		this.valid = valid;
	}

	/**
	 * @return the angle in degrees
	 */
	public double toDegrees() {
		return getValue();
	}

	/**
	 * @return the angle in radian
	 */
	public double toRadian() {
		return Math.toRadians(getValue());
	}

	/**
	 * default setter (in degree) limit the meaning to 0-360.
	 */
	@Override
	public void setValue(double v) {
		value = v;
		if (value < 0){
			if (value > -720){
				while (value < 0) {
					value = 360 + value;
				}
			}
			else{
				value = 0;
				valid = false;
			}
		}
	}

	/**
	 * set the meaning of the angle as string.
	 * 2 specific patterns are also allowed:
	 * xx.xxL (or xx.xxR) and Lxx.xx or Rxx.xx
	 * in that case it means that the angle is express from 0 to 180DEG
	 * with Left Right direction.
	 * this procedure will set the meaning to 0..360 in any case.
	 */
	@Override
	public void set(String s) {
		final String PATTERN1 = "[LR]\\d+\\.?\\d+"; //$NON-NLS-1$
		final String PATTERN2 = "\\d+\\.?\\d+[LR]"; //$NON-NLS-1$
		String[] fields;
		String valueAsString = ""; //$NON-NLS-1$
		boolean isLeftTag = false;
		if (s.matches(PATTERN1) || s.matches(PATTERN2)) {
			fields = s.split("[LR]"); //$NON-NLS-1$
			valueAsString = fields[0];
			String[] leftTag = s.split("\\d+\\.?\\d+"); //$NON-NLS-1$
			isLeftTag = leftTag.length > 1 && leftTag[1].equalsIgnoreCase("L"); //$NON-NLS-1$
		}
		else {
			valueAsString = s;
		}
		super.set(valueAsString);
		if (isLeftTag) {
			setValue(360 - value);
		}
	}

	/**
	 * default getter (in degrees)
	 */
	@Override
	public double getValue() {
		return value;
	}

	/**
	 * set the meaning in degrees
	 *
	 * @param v
	 */
	public void setValueInDegree(double v) {
		setValue(v);
	}

	/**
	 * set the meaning in radian
	 *
	 * @param v
	 *            the meaning in radian to set
	 */
	public void setValueInRadian(double v) {
		setValue(Math.toDegrees(v));
	}

	/**
	 * @return a string with the NMEA format of the angle ex 50.00 will produce
	 *         50.00R<br>
	 *         270 will produce 90L
	 */
	public String toNMEAString() {
		String res = ""; //$NON-NLS-1$
		if (value > 180) {
			res = String.format(Locale.US, "%05.1f,L", 360 - value); //$NON-NLS-1$
		} else {
			res = String.format(Locale.US, "%05.1f,R", value); //$NON-NLS-1$
		}
		return res;
	}

	/**
	 * @return true if meaning > 180 and < 360
	 */
	public boolean isLeft() {
		return ((value > 180) && (value < 360));
	}

	/**
	 * switch angle for board to board
	 */
	public double changeSide() {
		setValue(360 - value);
		return value;
	}

	/**
	 * @return the cosinus of the angle
	 */
	public double cos() {
		return Data.round(Math.cos(this.toRadian()), 10);
	}

	/**
	 * @return the sinus of the angle
	 */
	public double sin() {
		return Data.round(Math.sin(this.toRadian()), 10);
	}

	/**
	 * creates a new angle initiated by a radian meaning.
	 *
	 * @param r :
	 *            the angle in radian
	 * @return
	 */
	public static Angle createFromRadian(double r) {
		return new Angle(Math.toDegrees(r));
	}

	/**
	 * @param origin the origin to set
	 */
	public void setOrigin(AngleOrigin origin) {
		this.origin = origin;
	}

	/**
	 * @return the origin
	 */
	public AngleOrigin getOrigin() {
		return origin;
	}

	@Override
	public Angle clone() {
		Angle res = new Angle(this.value, this.valid);
		res.origin = origin;
		res.unit = unit;
		return res;
	}


	public static double toRadian(double angleInDegrees) {
		return Math.toRadians(angleInDegrees);
	}

	/**
	 * removes the value of a to the value of this angle.
	 * @param trueBearing
	 * @return
	 */
	public Angle minus(Angle a) {
		setValue(value - a.value);
		return this;
	}

	/**
	 * adds the value of a to the value of this angle.
	 * @param trueBearing
	 * @return
	 */
	public Angle plus(Angle a) {
		setValue(value = a.value);
		return this;
	}

	public static double changeSide(double angle) {
		return 360-angle;
	}

	public Angle minus(double a) {
		setValue(value - a);
		return this;
	}
}
