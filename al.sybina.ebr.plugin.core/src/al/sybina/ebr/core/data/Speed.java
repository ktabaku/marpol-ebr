/**
 * project : capcode.toolbox file name : Speed.java created : 15 janv. 2006 by :
 * cyrille
 */
/**
 * CVS markups
 * last update by : $Author: crosay $ 
 * $Date: 2009-11-02 00:10:37 $ 
 * $Header: /media/disk/capcodeCVSbackup/capcode.basics/src/capcode/data/Speed.java,v 1.9 2009-11-02 00:10:37 crosay Exp $
 * $Log: not supported by cvs2svn $
 * Revision 1.8  2009/08/14 14:21:19  crosay
 * Complete # update the dispay algorithm for the boat trace
 * http://sourceforge.net/apps/trac/capcode/ticket/12
 *
 * Revision 1.7  2009/02/20 22:28:51  crosay
 * Complete - task2611676 - take also MWV into account for computation of trueWind
 * https://sourceforge.net/tracker2/?func=detail&aid=2611676&group_id=100698&atid=1045270
 * Incomplete - tasktask 2577644 - SpeedPolar implement polar in tables
 * https://sourceforge.net/tracker2/?func=detail&aid=2577644&group_id=100698&atid=1045270
 *
 * Revision 1.6  2009/01/24 20:52:36  crosay
 * Complete - taskRemoval of sqlite
 *
 * Complete - tasksuppression of debug display for preference saving
 *
 * Complete - tasktask 2531747 animation of the wearther
 * https://sourceforge.net/tracker2/?func=detail&aid=2024403&group_id=100698&atid=1045270
 *
 * Revision 1.5  2008/05/28 16:28:54  crosay
 * added CVS markups in source code
 *
 * $Revision: 1.9 $ 
 * $State: Exp $ 
 */
package al.sybina.ebr.core.data;


/**
 * this class implement a practical way to use speed. basically speed are in
 * knots, but can be retreived in km, ms and mph
 * 
 * @author cyrille
 */
public class Speed extends Data {
	/**
	 * long serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private static double CONV_MS_TO_KTS = 3.600 / 1.852;
	private static double CONV_KTS_TO_MS = 1.852 / 3.6;

	/**
	 * creator. creates a Data with v as a meaning and "KTS" as unit.
	 * 
	 * @param v :
	 *            speed in knots
	 */
	public Speed(double v) {
		super(v, "KTS"); //$NON-NLS-1$
	}

	/**
	 * create a speed and set the validity.
	 * @param f : the speed as float
	 * @param b : thue is speed is valid
	 */
	public Speed(float f, boolean b) {
		this(f);
		setValid(b);
	}

	/**
	 * retreive the speed in knots
	 * 
	 * @return
	 */
	public double toKnots() {
		return getValue();
	}

	/**
	 * retreive the speed in km/h
	 * 
	 * @return
	 */
	public double toKilometerPerHour() {
		return value * 1.852;
	}

	/**
	 * retreives the speed in m/s
	 * 
	 * @return
	 */
	public double toMeterPerSecond() {
		return value * CONV_KTS_TO_MS;
	}

	/**
	 * static transformation of speed from meter per second to knots.
	 * 
	 * @param ms
	 * @return
	 */
	public static double msToKts(double ms) {
		return ms * CONV_MS_TO_KTS;
	}

	/**
	 * static transformation of speed from km/h to knots.
	 * 
	 * @return
	 */
	public static double kmhToKts(double value) {
		return (value / 1.852);
	}
	
	/**
	 * static transformation of speed from mph to knots.
	 * 
	 * @return
	 */
	public static double mphToKts(double value){
		return value * 1.15078030303;
	}
	
	public Speed clone() {
		return new Speed(value);
	}

}
