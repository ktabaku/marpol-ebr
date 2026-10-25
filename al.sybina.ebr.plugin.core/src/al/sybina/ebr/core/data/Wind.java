/**
 * project : capcode.toolbox file name : Wind.java created : 15 janv. 2006 by :
 * cyrille
 */
/**
 * CVS markups
 * last update by : $Author: crosay $
 * $Date: 2009-11-02 00:10:37 $
 * $Header: /media/disk/capcodeCVSbackup/capcode.basics/src/capcode/data/Wind.java,v 1.6 2009-11-02 00:10:37 crosay Exp $
 * $Log: not supported by cvs2svn $
 * Revision 1.5  2009/02/20 22:28:51  crosay
 * Complete - task2611676 - take also MWV into account for computation of trueWind
 * https://sourceforge.net/tracker2/?func=detail&aid=2611676&group_id=100698&atid=1045270
 * Incomplete - tasktask 2577644 - SpeedPolar implement polar in tables
 * https://sourceforge.net/tracker2/?func=detail&aid=2577644&group_id=100698&atid=1045270
 *
 * Revision 1.4  2008/05/28 16:28:54  crosay
 * added CVS markups in source code
 *
 * $Revision: 1.6 $
 * $State: Exp $
 */
package al.sybina.ebr.core.data;

/**
 * a Wind class is composed of a direction (Angle) and speed (Speed) direction
 * is by default in degrees and speed in knots
 *
 * @author cyrille
 */
public class Wind extends EuclideanVector {

	/** constant used to select upwind best speed (french: pr�) = 0 */
	public static final int UPWIND = 0;

	/** constant used to select downwind best speed (french: largue) = 1 */
	public static final int DOWNWIND = 1;

	/** constant used to select reaching best speed (french: travers) = 2 */
	public static final int REACHING = 2;

	/**
	 * mask added to UPWIND/DOWNWIND/REACHING to select starboard cases (french:
	 * tribord) = 0
	 */
	public static final int MASK_STARBOARD = 0;

	/**
	 * mask added to UPWIND/DOWNWIND/REACHING to select port cases (french:
	 * babord) = 10
	 */
	public static final int MASK_PORT = 10;

	// TODO: put these values in SystemUtils and check consistency with values in
	// SpeedPolar
	private static final double LIMIT_UPWIND_LOW = 80;
	private static final double LIMIT_UPWIND_HIGH = 280;
	private static final double LIMIT_DOWNWIND_LOW = 100;
	private static final double LIMIT_DOWNWIND_HIGH = 260;

	public static final Wind NOWIND = new Wind(0, 0);

	/**
	 * creates a Wind given its direction in degrees and force in knots
	 *
	 * @param angleInDegrees
	 * @param speedInKnots
	 */
	public Wind(double angleInDegrees, double speedInKnots) {
		super(new Speed(speedInKnots), new Angle(angleInDegrees));
	}

	/**
	 * creates a wind from a copy of the wind in parameter
	 *
	 * @param wind
	 */
	public Wind(Wind wind) {
		this(wind.getAngle().getValue(), wind.getSpeed().getValue());
		speed.valid = wind.getSpeed().valid;
		angle.valid = wind.getAngle().valid;
	}

	/**
	 * creates a wind from a copy of the wind in parameter
	 *
	 * @param wind
	 */
	public Wind(EuclideanVector euclideanVector) {
		this(euclideanVector.getAngle().getValue(), euclideanVector.getSpeed().getValue());
		speed.valid = euclideanVector.getSpeed().valid;
		angle.valid = euclideanVector.getAngle().valid;
	}

	/**
	 * creates a Wind with angle and speed as object.
	 * @param a
	 * @param s
	 */
	public Wind(Angle a, Speed s) {
		super(s, a);
	}

	/**
	 * @return true if wind angle is between 0 and 80 and between 280 and 360
	 */
	public boolean isUpWind() {
		return (angle.value < LIMIT_UPWIND_LOW)
				|| (angle.value > LIMIT_UPWIND_HIGH);
	}

	/**
	 * @return true if then true wind angle is between 100 and 260
	 */
	public boolean isDownWind() {
		return (angle.value > LIMIT_DOWNWIND_LOW)
				&& (angle.value < LIMIT_DOWNWIND_HIGH);
	}

	/**
	 *
	 * @return a combination of {UPWIND, DOWNWIND, REACHING} and
	 *         {MASK_STARBOARD, MASK_PORT}
	 */
	public int getDirectionCode() {
		int code = 0;
		if (isUpWind()) {
			code = UPWIND;
		} else if (isDownWind()) {
			code = DOWNWIND;
		} else {
			code = REACHING;
		}
		if (angle.isLeft()) {
			code = code + MASK_PORT;
		} else {
			code = code + MASK_STARBOARD;
		}
		return code;
	}

	/**
	 * link the wind speed with a double meaning
	 */
	public void setSpeed(double v) {
		this.speed.setValue(v);
	}

	/**
	 * link the wind direction with a double meaning
	 *
	 * @uml.property name="direction"
	 */
	public void setDirection(double v) {
		this.angle.setValue(v);
	}

	/**
	 * @return the direction
	 */
	public Angle getDirection() {
		return angle;
	}

	/**
	 * @return the speed
	 */
	@Override
	public Speed getSpeed() {
		return speed;
	}

	/**
	 * Set the direction and force of the wind
	 *
	 * @param direction :
	 *            direction of the wind in degrees
	 * @param force :
	 *            force of the wind in knots
	 */
	public void set(double direction, double force) {
		this.speed.setValue(force);
		this.angle.setValue(direction);
	}

	public void copyfrom(Wind wind) {
		this.speed.setValue(wind.getSpeed().value);
		this.angle.setValue(wind.getDirection().value);
	}

	/**
	 * @param trueWind
	 * @return the wind express in a string giving direction in "E, NE,...) and force in beaufort scale
	 */
	public static String toBeaufortString(Wind wind) {
		String s = Integer.toString(wind.toBeaufort());
		int d = wind.getAngle().asInt();
		String dir = d < 23 ? "N" :  //$NON-NLS-1$
			d < 68 ? "NE" : //$NON-NLS-1$
				d < 113 ? "E" : //$NON-NLS-1$
					d < 158 ? "SE": //$NON-NLS-1$
						d < 203 ? "S" : //$NON-NLS-1$
							d < 248 ? "SW" : //$NON-NLS-1$
								d < 293 ? "W" : //$NON-NLS-1$
									d < 338 ? "NW" : "N"; //$NON-NLS-1$ //$NON-NLS-2$
		return dir + " " + s; //$NON-NLS-1$
	}

	public int toBeaufort() {
		int force = getSpeed().asInt();
		int res = (force < 1) ? 0: 
			(force < 4) ? 1: 
				(force < 7) ? 2:
					(force < 11) ? 3: 
						(force < 16) ? 4:
							(force < 21) ? 5:
								(force < 27) ? 6: 
									(force < 34) ? 7: 
										(force < 41) ? 8 :  
											(force < 48) ? 9 :
												(force < 55) ? 10 :  
													(force < 64) ? 11 : 12;
		return res;
	}

}
