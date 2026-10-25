/**
 * CVS markups
 * last update by : $Author: crosay $
 * $Date: 2009-08-31 22:18:08 $
 * $Header: /media/disk/capcodeCVSbackup/capcode.basics/src/capcode/data/Data.java,v 1.13 2009-08-31 22:18:08 crosay Exp $
 * $Log: not supported by cvs2svn $
 * Revision 1.12  2009/08/14 14:21:42  crosay
 * Complete # update the dispay algorithm for the boat trace
 * http://sourceforge.net/apps/trac/capcode/ticket/12
 *
 * Revision 1.11  2009/04/23 10:26:42  crosay
 * Incomplete - taskSourceForge.net: CapCode-software suite for sailors: Detail: 2705654 - NMEA data mapping (FR 2605966 Input Mapping)
 * https://sourceforge.net/tracker2/index.php?func=detail&aid=2705654&group_id=100698&atid=1045270
 * Incomplete - taskzygrib
 *
 * Revision 1.10  2009/03/31 07:42:19  crosay
 * Incomplete - taskSourceForge.net: CapCode-software suite for sailors: Detail: 2705654 - NMEA data mapping (FR 2605966 Input Mapping)
 * https://sourceforge.net/tracker2/index.php?func=detail&aid=2705654&group_id=100698&atid=1045270
 *
 * Revision 1.9  2009/02/20 22:28:51  crosay
 * Complete - task2611676 - take also MWV into account for computation of trueWind
 * https://sourceforge.net/tracker2/?func=detail&aid=2611676&group_id=100698&atid=1045270
 * Incomplete - tasktask 2577644 - SpeedPolar implement polar in tables
 * https://sourceforge.net/tracker2/?func=detail&aid=2577644&group_id=100698&atid=1045270
 *
 * Revision 1.8  2009/01/29 10:31:28  crosay
 * Incomplete - task add gps hypbrid mode (in true wind computation) task 2533241 GPS speed spare mode
 * https://sourceforge.net/tracker2/?func=detail&aid=2533241&group_id=100698&atid=1045270
 * Complete - task save status and position of toolbars
 * https://sourceforge.net/tracker2/?func=detail&aid=2531530&group_id=100698&atid=1045270
 *
 * Revision 1.7  2009/01/24 20:52:36  crosay
 * Complete - taskRemoval of sqlite
 *
 * Complete - tasksuppression of debug display for preference saving
 *
 * Complete - tasktask 2531747 animation of the wearther
 * https://sourceforge.net/tracker2/?func=detail&aid=2024403&group_id=100698&atid=1045270
 *
 * Revision 1.6  2008/05/28 16:28:54  crosay
 * added CVS markups in source code
 *
 * $Revision: 1.13 $
 * $State: Exp $
 */
package al.sybina.ebr.core.data;

import java.util.Locale;

/**
 * generic class for data used in capcode a data is a double meaning and a unit
 * (as text)
 *
 * @author rosaycy
 */
@SuppressWarnings("rawtypes")
public class Data extends Number implements Comparable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public String name = "";
	/** meaning of the data (double) */
	public double value;

	/** unit of the data (String) */
	protected String unit;

	/** valid of the data boolean */
	protected boolean valid = true;

	/** age of the data, in ms*/
	protected long age;

	/**
	 * creates a Data with fields v, units valid set true by default
	 *
	 * @param v :
	 *            can be set to 0 as default
	 * @param aUnit :
	 *            can be set to "???" as default
	 */
	public Data(double v, String aUnit) {
		setValue(v);
		unit = aUnit;
	}

	/**
	 * creates a Data with v; aUnit, isValid
	 *
	 * @param v
	 * @param aUnit
	 * @param isValid
	 */
	public Data(double v, String aUnit, boolean isValid) {
		this(v, aUnit);
		unit = aUnit;
		valid = isValid;
	}

	/**
	 * create a data with only
	 *
	 * @param v
	 * @param isValid
	 */
	public Data(double v, boolean isValid) {
		value = v;
		valid = isValid;
	}

	public Data() {
		super();
		value = 0;
		unit = ""; //$NON-NLS-1$
	}

	/**
	 * set the meaning with v
	 *
	 * @param v
	 */
	public void setValue(double v) {
		value = v;
	}

	/**
	 * set the meaning with a string
	 *
	 * @param s
	 */
	public void set(String s) {
		try {
			value = Double.parseDouble(s);
			valid = true;
		}catch(NumberFormatException e) {
			value = 0;
			valid = false;
		}
	}

	/**
	 * @return the meaning as double
	 */
	public double getValue() {
		return value;
	}

	/**
	 * @return the meaning as int
	 */
	public int asInt() {
		return (int) round(value, 0);
	}

	/**
	 * @return the unit as a String
	 */
	public Object getUnit() {
		return unit;
	}

	/**
	 * @param v1
	 * @param v2
	 * @param decimal
	 * @return true if v1 = v2, rounded to the decimal
	 */
	public static boolean equals(double v1, double v2, int decimal) {
		boolean res = (round(v1, decimal) == round(v2, decimal));
		return (res);
	}

	/**
	 * @param i :
	 *            the number of decimal to round to
	 * @return a rounded meaning with i decimals
	 */
	public double round(int i) {
		double x = Math.pow(10, i);
		return (Math.rint(value * x) / x);
	}

	/**
	 *
	 * @param v
	 * @param i
	 * @return v rounded at the i decimal
	 */
	public static double round(double v, int i) {
		double x = Math.pow(10, i);
		return (Math.rint(v * x) / x);
	}

	/**
	 * @param v
	 * @param i
	 * @return v rounded at the i decimal
	 */
	public static float round(float v, int i) {
		float x = (float) Math.pow(10, i);
		return ((float) Math.rint(v * x) / x);
	}

	/**
	 * return the floored meaning to the first next integer meaning defined with a
	 * step between values Examples: the wind shall be mesured every 5 knots the
	 * floor of this steps are the following * <table border='1'
	 * cellpadding='3'>
	 * <tr>
	 * <th>step </th>
	 * <th>Value</th>
	 * <th>Result</th>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>0</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>1</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>2</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>3</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>4</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>5</td>
	 * <td>5</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>6</td>
	 * <td>5</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>7</td>
	 * <td>5</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>8</td>
	 * <td>5</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>9</td>
	 * <td>5</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>10</td>
	 * <td>10</td>
	 * </tr>
	 * </table>
	 *
	 * @param step
	 * @return v rounded to the neared step
	 */
	public double floorStep(int step) {
		// defensive programming against division by 0
		// the smallest meaning of step is 1
		if (step == 0) {
			step = 1;
		}
		return Math.floor(round(value, 0) / step) * step;

	}

	/**
	 * return the floored meaning to the first next integer meaning defined with a
	 * step between values Examples: the wind shall be mesured every 5 knots the
	 * floor of this steps are the following * <table border='1'
	 * cellpadding='3'>
	 * <tr>
	 * <th>step </th>
	 * <th>Value</th>
	 * <th>Result</th>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>0</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>1</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>2</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>3</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>4</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>5</td>
	 * <td>5</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>6</td>
	 * <td>5</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>7</td>
	 * <td>5</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>8</td>
	 * <td>5</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>9</td>
	 * <td>5</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>10</td>
	 * <td>10</td>
	 * </tr>
	 * </table>
	 *
	 * @param step
	 * @return v rounded to the neared step
	 */
	public static double floorStep(double v, int step) {
		// defensive programming against division by 0
		// the smallest meaning of step is 1
		if (step == 0) {
			step = 1;
		}
		return Math.floor(round(v, 0) / step) * step;

	}

	/**
	 * return the ceiled meaning to the first next integer meaning defined with a
	 * step between values Examples: the wind shall be mesured every 5 knots the
	 * ceiling of this steps are the following * <table border='1'
	 * cellpadding='3'>
	 * <tr>
	 * <th>step </th>
	 * <th>Value</th>
	 * <th>Result</th>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>0</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>1</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>2</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>3</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>4</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>5</td>
	 * <td>5</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>6</td>
	 * <td>5</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>7</td>
	 * <td>5</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>8</td>
	 * <td>5</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>9</td>
	 * <td>5</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>10</td>
	 * <td>10</td>
	 * </tr>
	 * </table>
	 *
	 * @param step
	 * @return v rounded to the neared step
	 */
	public double ceilingStep(int step) {
		// defensive programming against division by 0
		// the smallest meaning of step is 1
		if (step == 0) {
			step = 1;
		}
		return Math.ceil(round(value, 0) / step) * step;
	}

	/**
	 * return the ceiled meaning to the first next integer meaning defined with a
	 * step between values Examples: the wind shall be mesured every 5 knots the
	 * ceiling of this steps are the following * <table border='1'
	 * cellpadding='3'>
	 * <tr>
	 * <th>step </th>
	 * <th>Value</th>
	 * <th>Result</th>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>0</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>1</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>2</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>3</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>4</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>5</td>
	 * <td>5</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>6</td>
	 * <td>5</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>7</td>
	 * <td>5</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>8</td>
	 * <td>5</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>9</td>
	 * <td>5</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>10</td>
	 * <td>10</td>
	 * </tr>
	 * </table>
	 *
	 * @param step
	 * @return v rounded to the neared step
	 */
	public static double ceilingStep(double v, int step) {
		// defensive programming against division by 0
		// the smallest meaning of step is 1
		if (step == 0) {
			step = 1;
		}
		return Math.ceil(round(v, 0) / step) * step;
	}

	/**
	 * Add a data to another data. if one is invalid, the result is invalid. if
	 * units are different, the result is invalid.
	 *
	 * @param d
	 *            a Data to add to the object
	 * @return a data
	 */
	public Data addTo(Data d) {
		Data res = new Data(this.value + d.value, this.unit);
		res.setValid(this.isValid() && d.isValid());
		return res;
	}

	/**
	 * return d.value - this data.value. if one data is invalid, the result is invalid. if
	 * units are different, the result is invalid.
	 *
	 * @param d
	 * @return a data
	 */
	public Data removeFrom(Data d) {
		Data res = new Data(d.value - this.value, this.unit);
		res.setValid(this.isValid() && d.isValid());
		return res;
	}
	
	/**
	 * return a string with the meaning and the unit
	 */
	@Override
	public String toString() {
		return String.format(Locale.US, "%.02f %s", getValue(), getUnit()); //$NON-NLS-1$
	}

	/**
	 * provide a string given a double format examples: toString("%01.02f") of
	 * 5.3 will give "05.30 UNIT"
	 *
	 * @param f :
	 *            format of the double meaning,eg %05.02f
	 * @return meaning + unit as a string
	 */
	public String toString(String format) {
		return String.format(Locale.US, format + " %s", getValue(), getUnit()); //$NON-NLS-1$
	}

	/**
	 * return a string with the meaning, the unit and a 1 if data is valid, 0 if
	 * not
	 */
	public String toFullString() {
		return String.format(Locale.US, "%s %f %s (valid: %B)",name, getValue(), getUnit(), isValid()); //$NON-NLS-1$
	}

	@Override
	public double doubleValue() {
		return value;
	}

	@Override
	public float floatValue() {
		return ((Double) value).floatValue();
	}

	@Override
	public int intValue() {
		return ((Double) value).intValue();
	}

	@Override
	public long longValue() {
		return ((Double) value).longValue();
	}

	/**
	 * @return the valid
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * @param valid
	 *            the valid to set
	 */
	public void setValid(boolean valid) {
		this.valid = valid;
	}

	@Override
	public Data clone() {
		return new Data(this.value, this.unit, this.valid);
	}

	public void setUnit(String u) {
		unit = u;
	}

	public long getAge(){
		return age;
	}

	public void setAge(long a){
		age = a;
	}

	@Override
	public boolean equals(Object o){
		if (o instanceof Data){
			Data d = (Data) o;
			return value == d.value;
		}
		return false;
	}

	@Override
	public int compareTo(Object o) {
		if (o instanceof Data){
			Data d = (Data) o;
			return value == d.value ? 0 : value > d.value ? 1 : -1;
		}
		return 0;
	}
}
