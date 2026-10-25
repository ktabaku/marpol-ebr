/**
 * Project: capcode.basics E_NMEACodes.java created 18-juil.-07 by rosaycy 
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
 * $Header: /media/disk/capcodeCVSbackup/capcode.basics/src/capcode/nmea/E_NMEACodes.java,v 1.8 2009-11-02 00:10:37 crosay Exp $
 * $Log: not supported by cvs2svn $
 * Revision 1.7  2009/04/23 10:26:42  crosay
 * Incomplete - taskSourceForge.net: CapCode-software suite for sailors: Detail: 2705654 - NMEA data mapping (FR 2605966 Input Mapping)
 * https://sourceforge.net/tracker2/index.php?func=detail&aid=2705654&group_id=100698&atid=1045270
 * Incomplete - taskzygrib
 *
 * Revision 1.6  2009/02/20 22:45:53  crosay
 * Complete - task2611676 - take also MWV into account for computation of trueWind
 * https://sourceforge.net/tracker2/?func=detail&aid=2611676&group_id=100698&atid=1045270
 *
 * Revision 1.5  2009/02/13 13:24:34  crosay
 * Incomplete - taskcheck correct behaviour of new speed capture mechanism
 *
 * Complete - taskhelmsman display should not access directly the speed polar
 *
 * Revision 1.4  2008/09/19 20:43:10  dersaint
 *  task 2024395  complement the NMEA messages
 *  added GSV and VTG
 *  modified E_NMEACodes and some others (minor changes)
 *
 * Revision 1.3  2008/05/28 16:28:54  crosay
 * added CVS markups in source code
 *
 * $Revision: 1.8 $ 
 * $State: Exp $ 
 */

package al.sybina.ebr.core.nmea;

/**
 * <u>This Enum lists all the NMEA message codes understood by Capcode</u><br>
 * see documentation on NMEA for all the evailable codes<br>
 * in bold are the code that are actualy implemented<br>
 * List of codes:<br>
 * <table border='1' cellpadding='3'>
 * <tr>
 * <th>code </th>
 * <th>Meaning</th>
 * </tr>
 * <tr>
 * <td><b>WRN</b></td>
 * <td>Warning message (status of the NMEA bus)</td>
 * </tr>
 * <tr>
 * <td>AIS</td>
 * <td>easyAIS maintenance message</td>
 * </tr>
 * <tr>
 * <tr>
 * <td>APB</td>
 * <td>Autopilot Format </td>
 * </tr>
 * <tr>
 * <td>BOD</td>
 * <td>Bearing Origin to Destination</td>
 * </tr>
 * <tr>
 * <td>BWC</td>
 * <td>Bearing and distance to waypoint - great circle</td>
 * </tr>
 * <tr>
 * <td>BWR</td>
 * <td>Bearing and distance to waypoint - rhumb line</td>
 * </tr>
 * <tr>
 * <td>DBT</td>
 * <td>Depth below transducer</td>
 * </tr>
 * <tr>
 * <td><b>GGA</b</td>
 * <td>Global Positioning System Fix Data </td>
 * </tr>
 * <tr>
 * <td<b>>GLL</b></td>
 * <td>Geographic position, Latitude and Longitude</td>
 * </tr>
 * <tr>
 * <td><b>GSA</b></td>
 * <td>GPS DOP and active satellites </td>
 * </tr>
 * <tr>
 * <td>GSV</td>
 * <td>Satellites in view</td>
 * </tr>
 * <tr>
 * <td>HDM</td>
 * <td>Heading, Magnetic</td>
 * </tr>
 * <tr>
 * <td>HSC</td>
 * <td>Command heading to steer</td>
 * </tr>
 * <tr>
 * <td>MTW</td>
 * <td>Water temperature, Celcius</td>
 * </tr>
 * <tr>
 * <td>MWV</td>
 * <td>Wind Speed and Angle</td>
 * </tr>
 * <tr>
 * <td>RMB</td>
 * <td>Recommended minimum navigation information</td>
 * </tr>
 * <tr>
 * <td><b>RMC</b></td>
 * <td>Recommended minimum specific GPS/Transit data</td>
 * </tr>
 * <tr>
 * <td>RTE</td>
 * <td>Waypoints in active route</td>
 * </tr>
* <tr>
 * <td><b>VDM</b></td>
 * <td>Vessel Data Message (IAS)</td>
 * </tr>
 * <tr>
 * <td><b>VHW</b></td>
 * <td>Water speed and heading</td>
 * </tr>
 * <tr>
 * <td><b>VLW</b></td>
 * <td>Distance travelled through water</td>
 * </tr>
 * <tr>
 * <td><b>VWR</b></td>
 * <td>Relative wind direction and speed</td>
 * </tr>
 * <tr>
 * <td><b>VTG</b></td>
 * <td>Track made good and ground speed</td>
 * </tr>
 * <tr>
 * <td>WCV</td>
 * <td>Waypoint Closure Velocity</td>
 * </tr>
 * <tr>
 * <td>WPL</td>
 * <td>waypoint location</td>
 * </tr>
 * <tr>
 * <td>XTE</td>
 * <td>Cross track error, measured</td>
 * </tr>
 * <tr>
 * <td>XTR</td>
 * <td>Cross-Track Error - Dead Reckoning</td>
 * </tr>
 * <tr>
 * <td><b>CDB</b></td>
 * <td>Capcode Basic Data</td>
 * </tr>
 * <tr>
 * <td><b>CTW</b></td>
 * <td>Computed True Wind</td>
 * </tr>
 * <tr>
 * <td><b>CSV</b></td>
 * <td>Computed Target Velocity</td>
 * </tr>
 * <tr>
 * <td><b>CLL</b></td>
 * <td>Computed Lay Lines</td>
 * </tr>
 * <tr>
 * <td><b>CDP</b></td>
 * <td>Computed Data Polar </td>
 * </tr>
 * <tr>
 * <td><b>CAS</b></td>
 * <td>Computed Crew Alerting System messages</td>
 * </tr>
 * </table>
 * 
 * @author rosaycy
 * 
 */
public enum E_NMEACodes {
	noCode, 
	AIS,
	APB, 
	BEC, BOD, BWC, BWR, 
	DBT, DBS, DBK,	
	GGA, GLL, GSA, GSV, 
	HDM, HSC, 
	MTW, MWV, 
	RMB, RMC, RTE, 
	VDM, VHW, VLW, VWR, VTG, 
	WCV, WPL, 
	XTE, XTR, 
	//created by capcode
	CTW, CSV, CBD, CLL, CDP, CAS, 
	WRN;
}
