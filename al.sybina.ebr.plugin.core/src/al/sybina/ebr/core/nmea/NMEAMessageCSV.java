/**
 * Project: capcode.basics NMEAMessageCSV.java created 23-juil.-07 by rosaycy 
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
 * $Date: 2009-04-23 10:26:42 $ 
 * $Header: /media/disk/capcodeCVSbackup/capcode.basics/src/capcode/nmea/NMEAMessageCSV.java,v 1.11 2009-04-23 10:26:42 crosay Exp $
 * $Log: not supported by cvs2svn $
 * Revision 1.10  2009/03/16 17:13:05  crosay
 * Complete - taskexternalise strings
 *
 * Revision 1.9  2009/03/04 10:15:55  crosay
 * Complete - taskdisplay the equipments connected to PC
 *
 * Complete - taskexternalise strings
 *
 * Incomplete - tasknew plugin: bearing-speed-and log
 *
 * Revision 1.8  2009/02/20 22:45:53  crosay
 * Complete - task2611676 - take also MWV into account for computation of trueWind
 * https://sourceforge.net/tracker2/?func=detail&aid=2611676&group_id=100698&atid=1045270
 *
 * Revision 1.6  2009/01/29 10:31:28  crosay
 * Incomplete - task add gps hypbrid mode (in true wind computation) task 2533241 GPS speed spare mode
 * https://sourceforge.net/tracker2/?func=detail&aid=2533241&group_id=100698&atid=1045270
 * Complete - task save status and position of toolbars
 * https://sourceforge.net/tracker2/?func=detail&aid=2531530&group_id=100698&atid=1045270
 *
 * Revision 1.5  2008/09/19 20:43:10  dersaint
 *  task 2024395  complement the NMEA messages
 *  added GSV and VTG
 *  modified E_NMEACodes and some others (minor changes)
 *
 * Revision 1.4  2008/05/28 16:28:54  crosay
 * added CVS markups in source code
 *
 * $Revision: 1.11 $ 
 * $State: Exp $ 
 */

package al.sybina.ebr.core.nmea;

import java.util.Locale;

import al.sybina.ebr.core.data.Angle;
import al.sybina.ebr.core.data.EuclideanVector;
import al.sybina.ebr.core.data.Speed;
import al.sybina.ebr.core.data.SpeedPolar;


/**
 * this class gives the following cmputed informations:<br>
 * TargetSpeed at the actual boat angle <br>
 * VMG : bearing /true wind and expected speed <br>
 * actual boat speed and origin of the boat speed (speedometer/gps)
 * CSV<br>
 * <b>Computed target Speed and VMG</b><br>
 * <i>example:CSV,06.23,N,030.0,B,05.23,N,-<br>
 * 1-2 06.23,N target speed at this angle should be 6.23 knots <br>
 * (without regard to the best upwind or downwind speed)<br>
 * 3-4 030.0,B bearing to true wind should be 30.0 deg<br>
 * 5-6 05.23,N the best VMG upwind should be then 5.23 knots<br>
 * 7-8 07.25,G the actual boat speed 
 * 			(G = taken from GPS, S = taken from speedometer, blank if invalid)
 * 9 : the mode used for computation (UPWIND, DOWNWIND, TRACK)
 * </i>
 * @author rosaycy
 * 
 */
public class NMEAMessageCSV extends NMEABasicMessage {
	private static final String CODE_INVALID = ""; //$NON-NLS-1$
	private static final String CODE_SPEED_VALID = "N"; //$NON-NLS-1$
	private static final String CODE_BEARING_VALID = "B"; //$NON-NLS-1$
	private static final String CODE_SPEED_FROM_GPS = "G"; //$NON-NLS-1$

	private Speed targetSpeed = null;
	private EuclideanVector vmg;
	private Speed boatSpeed;
	private boolean speedFromGPS;
	private SpeedPolar.E_Mode mode;

	/**
	 * create a CSV message from text<br>
	 * <b>Computed target Speed and VMG</b><br>
	 * CSV,06.23,N,030.0,B,05.23,N,-<br>
	 * 1-2 06.23, N target speed at this angle should be 6.23 knots <br>
	 * (without regard to the best upwind or downwind speed)<br>
	 * 3-4 030.0, B bearing to true wind should be 30.0 deg<br>
	 * 5-6 05.23, N the best VMG upwind should be then 5.23 knots<br>
	 * 
	 */
	public NMEAMessageCSV(String message) {
		super(message);
		targetSpeed = new Speed(getFields().getAsDouble(1));
		targetSpeed.setValid(getFields().atPos(2).equals("N")); //$NON-NLS-1$
		vmg = new EuclideanVector(new Speed(getFields().getAsDouble(5)), new Angle(getFields()
				.getAsDouble(3)));
		vmg.getAngle().setValid(getFields().atPos(4).equals("B")); //$NON-NLS-1$
		vmg.getSpeed().setValid(getFields().atPos(6).equals("N")); //$NON-NLS-1$
		boatSpeed = new Speed(getFields().getAsDouble(7));
		boatSpeed.setValid(!getFields().atPos(8).isEmpty());
		setSpeedFromGPS(getFields().atPos(8).equalsIgnoreCase("G")); //$NON-NLS-1$
		try{
			mode = SpeedPolar.E_Mode.valueOf(getFields().atPos(9));
		}catch(Exception e){
			mode = SpeedPolar.E_Mode.INVALID;

		}
	}


	/**
	 * create a CSV message from values<br>
	 * @param eq : equipment origin of the message (should be PC)
	 * @param target target speed (at this angle to true wind)
	 * @param vmg : best vmg (angle and speed)
	 * @param boatSpeed : the actual boat speed
	 * @param fromGPS : true if boat speed comes from the gps
	 */
	public NMEAMessageCSV(String eq, Speed target, EuclideanVector vmg, Speed boatSpeed, boolean fromGPS, SpeedPolar.E_Mode mode) {
		super(eq, "CSV"); //$NON-NLS-1$
		this.targetSpeed = target;
		this.vmg = vmg;
		this.boatSpeed = boatSpeed;
		this.speedFromGPS= fromGPS;
		this.mode = mode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see al.sybina.ebr.nmea.NMEABasicMessage#buildString()
	 *      CSV,06.23,N,030.0,B,05.23,N,-<br>
	 */
	@Override
	public String buildString() {
		return String.format(Locale.US, "%05.02f,%s,%05.01f,%s,%05.02f,%s,%05.02f,%s,%s", //$NON-NLS-1$
				targetSpeed.getValue(),
				targetSpeed.isValid() ? CODE_SPEED_VALID : CODE_INVALID, vmg
						.getAngle().getValue(),
				vmg.getAngle().isValid() ? CODE_BEARING_VALID : CODE_INVALID,
				vmg.getSpeed().getValue(),
				vmg.getSpeed().isValid() ? CODE_SPEED_VALID : CODE_INVALID,
				boatSpeed.getValue(),
				speedFromGPS ? 
						CODE_SPEED_FROM_GPS : 
							boatSpeed.isValid() ? 
									CODE_SPEED_VALID : CODE_INVALID,
				mode.toString()
			);
	}

	/**
	 * @return the targetSpeed
	 */
	public Speed getTargetSpeed() {
		return targetSpeed;
	}

	/**
	 * @param targetSpeed
	 *            the targetSpeed to set
	 */
	public void setTargetSpeed(Speed targetSpeed) {
		this.targetSpeed = targetSpeed;
	}

	/**
	 * @return the vmg
	 */
	public EuclideanVector getVmg() {
		return vmg;
	}

	/**
	 * @param vmg
	 *            the vmg to set
	 */
	public void setVmg(EuclideanVector vmg) {
		this.vmg = vmg;
	}

	/**
	 * @param boatSpeed the boatSpeed to set
	 */
	public void setBoatSpeed(Speed boatSpeed) {
		this.boatSpeed = boatSpeed;
	}

	/**
	 * @return the boatSpeed
	 */
	public Speed getBoatSpeed() {
		return boatSpeed;
	}

	/**
	 * @param speedFromGPS the speedFromGPS to set
	 */
	public void setSpeedFromGPS(boolean speedFromGPS) {
		this.speedFromGPS = speedFromGPS;
	}

	/**
	 * @return the speedFromGPS
	 */
	public boolean isSpeedFromGPS() {
		return speedFromGPS;
	}


	/**
	 * @return the mode
	 */
	public final SpeedPolar.E_Mode getMode() {
		return mode;
	}


	/**
	 * @param mode the mode to set
	 */
	public final void setMode(SpeedPolar.E_Mode mode) {
		this.mode = mode;
	}

	
}
