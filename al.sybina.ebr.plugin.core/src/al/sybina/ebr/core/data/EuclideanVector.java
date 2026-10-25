/**
 * Project: capcode.basics Vector.java created 17-juil.-07 by rosaycy 
 * Copyright (c) 2007 Capcode Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * -Redistribution of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 
 * -Redistribution in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * - Neither the name of Capcode, Inc. or the names of authors may be used to
 * endorse or promote products derived from this software without specific prior
 * written permission.
 * 
 * - This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. CAPCODE AND ITS
 * LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A
 * RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 * IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT
 * OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR
 * PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY,
 * ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF CAPCODE HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * - You acknowledge that this software is not designed, licensed or intended for
 * use in the design, construction, operation or maintenance of any nuclear
 * facility or airborne software.
 * - You acknowledge that using this software for the purpose of navigation does not
 * engage the developer responsability in case of damage, injuries or casualities due
 * to the use of this software.
 */
/**
 * CVS markups
 * last update by : $Author: crosay $ 
 * $Date: 2009-11-02 00:10:37 $ 
 * $Header: /media/disk/capcodeCVSbackup/capcode.basics/src/capcode/data/Vector.java,v 1.6 2009-11-02 00:10:37 crosay Exp $
 * $Log: not supported by cvs2svn $
 * Revision 1.5  2009/04/23 10:26:42  crosay
 * Incomplete - taskSourceForge.net: CapCode-software suite for sailors: Detail: 2705654 - NMEA data mapping (FR 2605966 Input Mapping)
 * https://sourceforge.net/tracker2/index.php?func=detail&aid=2705654&group_id=100698&atid=1045270
 * Incomplete - taskzygrib
 *
 * Revision 1.4  2009/01/24 20:52:36  crosay
 * Complete - taskRemoval of sqlite
 *
 * Complete - tasksuppression of debug display for preference saving
 *
 * Complete - tasktask 2531747 animation of the wearther
 * https://sourceforge.net/tracker2/?func=detail&aid=2024403&group_id=100698&atid=1045270
 *
 * Revision 1.3  2008/05/28 16:28:54  crosay
 * added CVS markups in source code
 *
 * $Revision: 1.6 $ 
 * $State: Exp $ 
 */

package al.sybina.ebr.core.data;


/**
 * used to store vector type datas. A vector is defined by a Speed and an Angle
 * 
 * @see Speed
 * @see Angle
 * @author rosaycy
 * 
 */
public class EuclideanVector {
	protected Speed speed;
	protected Angle angle;

	/**
	 * creates a vector with speed and angle null
	 */
	public EuclideanVector() {
		speed = new Speed(0);
		angle = new Angle(0);
	}

	/**
	 * creates a Vector with the following parameters
	 * 
	 * @param speed
	 * @param angle
	 */
	public EuclideanVector(Speed speed, Angle angle) {
		this.speed = speed;
		this.angle = angle;
	}

	/**
	 * creates a Vector with the following parameters
	 * 
	 * @param speed
	 *            as a double
	 * @param angle
	 *            as a double
	 */
	public EuclideanVector(double speed, double angle) {
		this.speed = new Speed(speed);
		this.angle = new Angle(angle);
	}

	public static EuclideanVector createFromUV(double u, double v) {
		//compute the speed = square root (u^2 + v^2)
		double speed = Math.sqrt(Math.pow(u, 2) + Math.pow(v, 2));
		// computation of the angle / north geographic
		// u- and v-components of vector quantities resolved
		// relative to easterly and northerly directions
		// u = speed x sin(dd)
		// v = speed x cos(dd)
		// where dd is the wind direction
		// dd = tg-1(u/v)
		double alpha = (v != 0)
				? Math.atan(u / v)
				: u > 0 ? 0 : Math.PI;
		// arcTan gives a result between -PI to PI
		// if v is < 0 (south) then we must add 180�
		if (v < 0) {
			alpha += Math.PI;
		}
		return new EuclideanVector(speed, Math.toDegrees(alpha));
	}
	
	/**
	 * @return the speed
	 */
	public Speed getSpeed() {
		return speed;
	}

	/**
	 * @param speed
	 *            the speed to set
	 */
	public void setSpeed(Speed speed) {
		this.speed = speed;
	}

	/**
	 * @return the angle
	 */
	public Angle getAngle() {
		return angle;
	}

	/**
	 * @param angle
	 *            the angle to set
	 */
	public void setAngle(Angle angle) {
		this.angle = angle;
	}

	/**
	 * affect the values of the vector with double values
	 * 
	 * @param angle :
	 *            angle in degrees (double)
	 * @param speed :
	 *            speed in knots (double)
	 */
	public void setVectorAngleSpeed(double angle, double speed) {
		this.angle.setValue(angle);
		this.speed.setValue(speed);
	}
	
	/**
	 * affect the values of the vector with double values
	 * 
	 * @param angle :angle in degrees (Angle)
	 * @param speed : speed in knots (Speed)
	 */
	public void setVectorAngleSpeed(Angle angle, Speed speed) {
		this.angle = angle;
		this.speed = speed;
	}
	
	/**
	 * force validation/invalidation of both speed and angle.
	 * @param valid (boolean)
	 */
	public void setValid(boolean valid) {
		angle.setValid(valid);
		speed.setValid(valid);
	}
	/**
	 * check if the angle and the speed are both valid.
	 * @return true if the Vector is valid (angle and speed are valid)
	 */
	public boolean isValid() {
		return angle.isValid() && speed.isValid();
	}

	@Override
	public String toString() {
		return (angle.toString() + "," + speed.toString()); //$NON-NLS-1$
	}

	public EuclideanVector substract(EuclideanVector v) {
		return v != null ? EuclideanVector.createFromUV(getU() - v.getU(), getV() - v.getV()) : this;
	}

	/**
	 * return the horizontal (x axis) component of the vector, in nautical miles.
    */
	public double getU() {
		return (speed.value * Math.sin(angle.toRadian()));
	}
	
	/**
	 * return the vertical (y axis) component of the vector in nautical miles.
    */
	public double getV() {
		return (speed.value * Math.cos(angle.toRadian()));
	}

	public EuclideanVector add(EuclideanVector v) {
		return v != null ? EuclideanVector.createFromUV(getU() - v.getU(), getV() - v.getV()) : this;
	}


}
