/**
 * Project: capcode.basics NMEAMessageCTW.java created 23-juil.-07 by rosaycy 
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
 * $Header: /media/disk/capcodeCVSbackup/capcode.basics/src/capcode/nmea/NMEAMessageCTW.java,v 1.11 2009-04-23 10:26:42 crosay Exp $
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
import al.sybina.ebr.core.data.Wind;


/**
 * <b>Computed True Wind</b> CTW,060.5,B,330.2,G,25.3,N,46.86,K,13.02M<br>
 * 1-2 060.5,B : True wind direction relatively to the boat (B = data valid)<br>
 * 3-4 330.2,T : Geographic wind direction (T = geographic, M = magnetic, G = origin GPS)<br>
 * 5-6 25.3,N : True wind velocity in Knots<br>
 * 7-8 46.85,K: True wind velocity in km/h<br>
 * 9-10 13.02M: True wind velocity in m/s<br>
 * 
 * @author rosaycy
 * 
 */
public class NMEAMessageCTW extends NMEABasicMessage {
	private static final String CODE_INVALID = ""; //$NON-NLS-1$
	private static final String CODE_GEOGRAPHIC_VALID = "T"; //$NON-NLS-1$
	private static final String CODE_MAGNETIC_VALID = "M"; //$NON-NLS-1$
	private static final String CODE_HYBRID_VALID = "G"; //$NON-NLS-1$
	private static final String CODE_SPEED_VALID = "N"; //$NON-NLS-1$
	private static final String CODE_BEARING_VALID = "B"; //$NON-NLS-1$
	private Wind trueWind;
	private Angle bearing;
	private Angle trueWindGeographicDirection;
	private Angle.AngleOrigin boatBearingSource = Angle.AngleOrigin.NONE;

	/**
	 * <b>Computed True Wind</b> CTW,060.5,B,330.2,G,25.3,N,46.86,K,13.02M<br>
	 * 1-2 060.5,B : True wind direction relatively to the boat (B = data valid)<br>
	 * 3-4 330.2,G : Geographic wind direction (G = data valid)<br>
	 * 5-6 25.3,N : True wind velocity in Knots<br>
	 * 7-8 46.85,K: True wind velocity in km/h<br>
	 * 9-10 13.02M: True wind velocity in m/s<br>
	 * 
	 * @param message
	 */
	public NMEAMessageCTW(String message) {
		super(message);
		trueWind = new Wind(0, 0);
		double a = getFields().getAsDouble(1);
		double s = getFields().getAsDouble(5);
		trueWind.getAngle().setValid(
				getFields().getAsString(2).equals(CODE_BEARING_VALID));
		trueWind.getSpeed().setValid(
				getFields().getAsString(6).equals(CODE_SPEED_VALID));
		trueWind.set(a, s);
		trueWindGeographicDirection = new Angle(0);
		String code1 = getFields().getAsString(4);
		trueWindGeographicDirection.setValid(code1.equals(CODE_GEOGRAPHIC_VALID) 
				|| code1.equals(CODE_MAGNETIC_VALID)
				|| code1.equals(CODE_HYBRID_VALID));
		boatBearingSource =  code1.equals(CODE_MAGNETIC_VALID) ? Angle.AngleOrigin.MAGNETIC : 
			code1.equals(CODE_GEOGRAPHIC_VALID) ? Angle.AngleOrigin.GEOGRAPHIC :
				code1.equals(CODE_HYBRID_VALID) ? Angle.AngleOrigin.HYBRID: 
					Angle.AngleOrigin.NONE;

		if (trueWindGeographicDirection.isValid()) {
			trueWindGeographicDirection.setValue(getFields().getAsDouble(3));
		}
		if (trueWindGeographicDirection.isValid()
				&& trueWind.getAngle().isValid()) {
			bearing = new Angle(trueWindGeographicDirection.getValue()
					- trueWind.getDirection().getValue());
			bearing.setValid(true);
		} else {
			bearing = new Angle(0);
			bearing.setValid(false);
		}
	}

	/**
	 * 
	 * @param eq
	 *            equipment (eg "PC")
	 * @param tw :
	 *            true wind
	 * @param bearing1 :
	 *            direction followed by the boat
	 */
	public NMEAMessageCTW(String eq, Wind tw, Angle bearing1, Angle.AngleOrigin source) {
		super(eq, "CTW"); //$NON-NLS-1$
		trueWind = tw;
		this.bearing = bearing1;
		Angle geoWindDir = getTrueWindGeographicDirection();
		geoWindDir.setValid(bearing1.isValid() && tw.getDirection().isValid());
		// if geo wind valid then set meaning with true wind direction minus
		// bearing, else 0
		geoWindDir.setValue(geoWindDir.isValid() ? 
				(bearing1.getValue() + tw.getDirection().getValue()) % 360 : // valid: compute
				0.0); // invalid : default
		boatBearingSource = source;
	}
	
	public String getWindOrigin(){
		return trueWindGeographicDirection.isValid() ? 
				boatBearingSource == Angle.AngleOrigin.GEOGRAPHIC ? "Geo" :
					boatBearingSource == Angle.AngleOrigin.MAGNETIC ? "Mag" : 
						boatBearingSource == Angle.AngleOrigin.HYBRID ? "Comp" : 
							"invalid" 
							: "invalid"; 
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see al.sybina.ebr.nmea.NMEABasicMessage#buildString()
	 */
	@Override
	public String buildString() {
		// true wind geographic is valid if bearing is valid of true wind
		// geographic is valid
		return String.format(Locale.US,
				"%05.01f,%s,%05.01f,%s,%05.02f,N,%05.02f,K,%05.02f,M",  //$NON-NLS-1$
				trueWind.getDirection().getValue(), 
						trueWind.getDirection().isValid() ? 
								CODE_BEARING_VALID : 
									CODE_INVALID,
				trueWindGeographicDirection.getValue(),
				trueWindGeographicDirection.isValid() ? 
						boatBearingSource == Angle.AngleOrigin.GEOGRAPHIC ? CODE_GEOGRAPHIC_VALID :
							boatBearingSource == Angle.AngleOrigin.MAGNETIC ? CODE_MAGNETIC_VALID :
								boatBearingSource == Angle.AngleOrigin.HYBRID ? CODE_HYBRID_VALID :
									CODE_INVALID
									: CODE_INVALID, 
						trueWind.getSpeed().toKnots(), 
						trueWind.getSpeed().toKilometerPerHour(), 
						trueWind.getSpeed().toMeterPerSecond());
	}

	/**
	 * @return the trueWind
	 */
	public Wind getTrueWind() {
		return trueWind;
	}

	/**
	 * @param trueWind
	 *            the trueWind to set
	 */
	public void setTrueWind(Wind trueWind) {
		this.trueWind = trueWind;
	}

	/**
	 * @return the bearing
	 */
	public Angle getBearing() {
		return bearing;
	}

	/**
	 * @param bearing
	 *            the bearing to set
	 */
	public void setBearing(Angle bearing) {
		this.bearing = bearing;
	}

	/**
	 * @return the trueWindGeographicDirection
	 */
	public Angle getTrueWindGeographicDirection() {
		if (trueWindGeographicDirection == null) {
			trueWindGeographicDirection = new Angle(0);
		}
		return trueWindGeographicDirection;
	}

	/**
	 * @param trueWindGeographicDirection
	 *            the trueWindGeographicDirection to set
	 */
	public void setTrueWindGeographicDirection(Angle trueWindGeographicDirection) {
		this.trueWindGeographicDirection = trueWindGeographicDirection;
	}

}
