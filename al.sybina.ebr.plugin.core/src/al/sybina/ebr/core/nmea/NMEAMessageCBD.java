/**
 * Project: capcode.basics NMEAMessageCBD.java created 23-juil.-07 by rosaycy 
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
 * $Header: /media/disk/capcodeCVSbackup/capcode.basics/src/capcode/nmea/NMEAMessageCBD.java,v 1.5 2009-11-02 00:10:37 crosay Exp $
 * $Log: not supported by cvs2svn $
 * Revision 1.4  2009/08/31 22:18:08  crosay
 * Incomplete # preparation for delivery to version 0.9.3
 * http://sourceforge.net/apps/trac/capcode/ticket/17
 *
 * Revision 1.3  2009/08/29 19:24:26  crosay
 * Complete # move the display of the target VMG arrow to the polar on map plugin
 * http://sourceforge.net/apps/trac/capcode/ticket/29
 *
 * Revision 1.2  2009/08/15 23:43:49  crosay
 * Complete # add a close button on all internal frames
 * http://sourceforge.net/apps/trac/capcode/ticket/30
 * Complete # add reference points to maps
 * http://sourceforge.net/apps/trac/capcode/ticket/22
 * Incomplete # flight plugins
 * http://sourceforge.net/apps/trac/capcode/ticket/27
 *
 * Revision 1.1  2009/04/23 10:26:42  crosay
 * Incomplete - taskSourceForge.net: CapCode-software suite for sailors: Detail: 2705654 - NMEA data mapping (FR 2605966 Input Mapping)
 * https://sourceforge.net/tracker2/index.php?func=detail&aid=2705654&group_id=100698&atid=1045270
 * Incomplete - taskzygrib
 *
 * $Revision: 1.5 $ 
 * $State: Exp $ 
 */

package al.sybina.ebr.core.nmea;

import java.util.Locale;

import al.sybina.ebr.core.data.Angle;
import al.sybina.ebr.core.data.Data;
import al.sybina.ebr.core.data.EuclideanVector;
import al.sybina.ebr.core.data.Height;
import al.sybina.ebr.core.data.Speed;
import al.sybina.ebr.core.geo.Coords;
import al.sybina.ebr.core.geo.Latitude;
import al.sybina.ebr.core.geo.Longitude;


/**
 * this class gives the Capcode Basic Data.<br>
 * @author rosaycy
 * 
 */
public class NMEAMessageCBD extends NMEABasicMessage {
	private Angle rwa;
	private Speed rws;
	private Angle bmh;
	private Angle bth;
	private Angle bgt;
	private Speed bss;
	private Speed bgs;
	private EuclideanVector drift;
	private Longitude blo;
	private Latitude bla;
	private Height depth;
	private Height altitude;
	private Data totalLog;
	private Data dailyLog;

	/**
	 * CBD<br>
	 * <b>Capcode basic data</b><br>
	 */
	public NMEAMessageCBD(String message) {
		super(message);
		rwa = new Angle(getFields().getAsDouble(1));
		rws = new Speed(getFields().getAsDouble(2));
		bmh = new Angle(getFields().getAsDouble(3));
		bth = new Angle(getFields().getAsDouble(4));
		bss = new Speed(getFields().getAsDouble(5));
		bgt = new Angle(getFields().getAsDouble(6));
		bgs = new Speed(getFields().getAsDouble(7));		
		blo = new Longitude(getFields().atPos(8));// + getFields().atPos(8));
		bla = new Latitude(getFields().atPos(9));// + getFields().atPos(10));
		depth = new Height(getFields().atPos(10));
		altitude = new Height(getFields().atPos(11));
		dailyLog = new Data(getFields().getAsDouble(12), "NM");
		totalLog = new Data(getFields().getAsDouble(13), "NM");	
		drift = new EuclideanVector(getFields().getAsDouble(14), getFields().getAsDouble(15));
	}

	public NMEAMessageCBD(String eq, Angle rwa, Speed rws, Angle bmh, Angle bth, Speed bss,
			Angle bgt, Speed bgs, Longitude blo, Latitude bla, Height depth, Height alt,
			Data dailyLog, Data totalLog, EuclideanVector drift) {
		super(eq, "CBD"); //$NON-NLS-1$
		this.rwa = rwa;
		this.rws = rws;
		this.bmh = bmh;
		this.bth = bth;
		this.bss = bss;
		this.bgt = bgt;
		this.bgs = bgs;
		this.blo = blo;
		this.bla = bla;
		this.depth = depth;
		this.altitude = alt;
		this.dailyLog = dailyLog;
		this.totalLog = totalLog;
		this.drift = drift;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see al.sybina.ebr.nmea.NMEABasicMessage#buildString()
	 *      CSV,06.23,N,030.0,B,05.23,N,-<br>
	 */
	@Override
	public String buildString() {
		return String.format(Locale.US,
				"%06.02f,%05.02f,%06.02f,%06.02f,%05.02f,%06.02f,%05.02f,%s,%s,%05.01f,%05.0f,%05.0f,%05.1f,",  //$NON-NLS-1$
				rwa != null ? rwa.getValue() : 0,
				rws != null ? rws.getValue() : 0,
				bmh != null ? bmh.getValue() : 0,
				bth != null ? bth.getValue() : 0,
				bss != null ? bss.getValue() : 0,
				bgt != null ? bgt.getValue() : 0,
				bgs != null ? bgs.getValue() : 0,
				blo != null ? blo.toString(Coords.EStringFormat.DEG) : "00.000", //$NON-NLS-1$
				bla != null ? bla.toString(Coords.EStringFormat.DEG) : "00.000", //$NON-NLS-1$
				depth != null ? depth.getValue() : 0,
				altitude != null ? altitude.getValue() : 0,
				dailyLog != null ? dailyLog.getValue() : 0,
				totalLog != null ? totalLog.getValue() : 0,
				drift != null ? drift.getSpeed() : 0,
				drift != null ? drift.getAngle() : 0);		
	}

	/**
	 * @return the wind relative to the boat
	 */
	public Angle getRelativeWindAngle() {
		return this.rwa;
	}

	/**
	 * @param the relativeWindAngle to set
	 */
	public void setRelativeWindAngle(Angle relativeWindAngle) {
		this.rwa = relativeWindAngle;
	}

	/**
	 * @return the relative Wind speed
	 */
	public Speed getRelativeWindSpeed() {
		return this.rws;
	}

	/**
	 * @param rws the rws to set
	 */
	public void setRelativeWindSpeed(Speed relativeWindSpeed) {
		this.rws = relativeWindSpeed;
	}

	/**
	 * @return the boat magnetic heading (/magnetic North)
	 */
	public Angle getBoatMagneticHeading() {
		return this.bmh;
	}

	/**
	 * @param set the boat Magnetic Heading
	 */
	public void setBoatMagneticHeading(Angle boatMagneticHeading) {
		this.bmh = boatMagneticHeading;
	}

	/**
	 * @return the boat True Heading (/true North)
	 */
	public Angle getBoatTrueHeading() {
		return this.bth;
	}

	/**
	 * @param the True Heading to set
	 */
	public void setBoatTrueHeading(Angle boatTrueAngle) {
		this.bth = boatTrueAngle;
	}

	/**
	 * @return the boat True Heading (/true North)
	 */
	@Deprecated
	public Angle getBoatTrueAngle() {
		return this.bth;
	}

	/**
	 * @param the True Angle to set
	 */
	@Deprecated
	public void setBoatTrueAngle(Angle boatTrueAngle) {
		this.bth = boatTrueAngle;
	}

	/**
	 * @return the boat surface speed
	 */
	public Speed getBoatSurfaceSpeed() {
		return this.bss;
	}

	/**
	 * @param boatSurfaceSpeed the boat surface speed to set
	 */
	public void setBoatSurfaceSpeed(Speed boatSurfaceSpeed) {
		this.bss = boatSurfaceSpeed;
	}

	/**
	 * @return the boat geographic track
	 */
	public Angle getBoatGeographicTrack() {
		return this.bgt;
	}

	/**
	 * @param the boat Ground track ( course) to set
	 */
	public void setBoatGroundTrack(Angle boatGroundTrack) {
		this.bgt = boatGroundTrack;
	}

	/**
	 * @return the boat Ground Speed
	 */
	public Speed getBoatGroundSpeed() {
		return this.bgs;
	}

	/**
	 * @param the boat ground speed to set
	 */
	public void setBoatgroundSpeed(Speed boatgroundSpeed) {
		this.bgs = boatgroundSpeed;
	}

	/**
	 * @return the boat Longitude
	 */
	public Longitude getBoatLongitude() {
		return this.blo;
	}

	/**
	 * @param the longitude to set
	 */
	public void setBoatLongitude(Longitude lon) {
		this.blo = lon;
	}

	/**
	 * @return the boat latitude
	 */
	public Latitude getBoatLatitude() {
		return this.bla;
	}

	/**
	 * @param bla the bla to set
	 */
	public void setBoatLatitude(Latitude lat) {
		this.bla = lat;
	}

	public Height getDepth() {
		return this.depth;
	}
	
	public void setDepth(Height depth) {
		this.depth = depth;
	}

	public Height getAltitude() {
		return this.altitude;
	}

	public void setAltitude(Height alt) {
		this.altitude = alt;
	}

	public void setDailyLog(Data l){
		dailyLog = l;
	}
	
	public Data getDailyLog() {
		return this.dailyLog;
	}

	public Data getTotalLog() {
		return this.totalLog;
	}

	public void setTotalLog(Data l){
		totalLog = l;
	}
	
	public EuclideanVector getDrift(){
		return this.drift;
	}

	public void setDrift(EuclideanVector v){
		this.drift = v;
	}
}
