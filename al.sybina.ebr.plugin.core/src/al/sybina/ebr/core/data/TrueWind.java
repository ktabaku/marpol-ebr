/**
 * CVS markups
 * last update by : $Author: crosay $ 
 * $Date: 2009-03-16 17:13:05 $ 
 * $Header: /media/disk/capcodeCVSbackup/capcode.basics/src/capcode/data/TrueWind.java,v 1.7 2009-03-16 17:13:05 crosay Exp $
 * $Log: not supported by cvs2svn $
 * Revision 1.6  2008/05/28 16:28:54  crosay
 * added CVS markups in source code
 *
 * $Revision: 1.7 $ 
 * $State: Exp $ 
 */
package al.sybina.ebr.core.data;

/**
 * TrueWind for capCode Computation of the True Wind, given Relative Wind
 * Velocity and Direction and Boat Speed usage : implement a TrueWindClass then
 * set the wind velocity and direction and boat speed then get the result
 * 
 * @author c.rosay
 * @version 1.00 02/08/2005
 */
public class TrueWind extends Wind {
	/** relative wind (relative to the boat, ie measured) */
	private Wind relativeWind;
	/** speed of the boat in knots */
	private Speed boatSpeed;

	/**
	 * creates a Class TrueWind with the following parameter
	 * 
	 * @param boatSpeed
	 * @param relativeWind
	 */
	public TrueWind(Speed boatSpeed, Wind relativeWind) {
		super(0, 0);
		this.boatSpeed = boatSpeed;
		this.relativeWind = relativeWind;
	}

	/**
	 * Method Compute : called each time one of the following attribute is
	 * modified: boatSpeed, relativeSpeed, relativeDirection. Compute the true
	 * wind direction and velocity. see the callBack mechanism.
	 * 
	 */

	public void compute() {
		double VA = relativeWind.getSpeed().getValue();
		double a = relativeWind.getDirection().toRadian(); // relative wind
															// angle in radian
		double Ya = VA * Math.sin(a);
		double Xa = VA * Math.cos(a);
		double Vb = -boatSpeed.value; // Boat wind = - boat speed on surface
		// result
		double Xr = Xa + Vb;
		double Yr = Ya;
		// VR = SQR(Xr^2 + Yr^2)
		double VR = Math.sqrt(Xr * Xr + Yr * Yr); // true wind speed
		double b = 0;
		if (VR != 0) {
			b = Math.acos(Xr / VR);
		}
		speed.setValue(VR);
		getDirection().setValueInRadian(b);
		// Ya <0 means angle is port
		if (Ya < 0) {
			getDirection().changeSide();
		}
		setValid(relativeWind.isValid() && boatSpeed.isValid());
	}

	/**
	 * this method is used by the simulator. Given a true wind, and depending on
	 * the boat, this gives the relative wind to be send on the NMEA This
	 * computation provides the relative wind given a true wind and a boat
	 * speed. This feature is meant mainly for the simulator, to simulate a
	 * relative wind given a true wind geographic and a boat speed and bearing.
	 * (see capcode_SDD_Simulator)
	 * 
	 */
	public void reverseComputation() {
		double VR = this.getSpeed().getValue();
		double b = this.getDirection().toRadian(); // true wind angle in radian
		double Yr = VR * Math.sin(b);
		double Xr = VR * Math.cos(b);
		double Vb = -boatSpeed.value; // Boat wind = - boat speed on surface
		// result
		double Xa = Xr - Vb;
		double Ya = Yr;
		// VA = SQR(Xa^2 + Ya^2)
		double VA = Math.sqrt(Xa * Xa + Ya * Ya); // relative wind speed
		double a = 0;
		if (VA != 0) {
			a = Math.acos(Xa / VA);
		}
		relativeWind.getSpeed().setValue(VA);
		relativeWind.getDirection().setValueInRadian(a);
		// Ya <0 means angle is port
		if (Yr < 0) {
			relativeWind.getDirection().changeSide();
		}
	}

	/**
	 * used to simplify the sequences. usually the wind is always received with
	 * the 2 values direction and speed at the same time. Usefull to have this
	 * function to set the relative wind values binded to this true wind.
	 * Modification of the values will launch the computation of the true wind.
	 * 
	 * @param d
	 *            wind direction in DEG
	 * @param s
	 *            speed of the wind in KTS
	 */
	public void modifyRelativeWindValues(double d, double s) {
		// in order to launch only one computation per cycle, the direction is
		// set directly
		// and the speed is set through the setter (this will ask for a
		// computation)
		relativeWind.getDirection().setValue(d);
		relativeWind.speed.setValue(s);
	}

	/**
	 * used for testing purpose. force the true wind direction and speed to
	 * specific values
	 * 
	 * @param d
	 * @param s
	 */
	public void forceToDirectionAndSpeed(double d, double s) {
		getDirection().setValue(d);
		getSpeed().setValue(s);
	}

}
