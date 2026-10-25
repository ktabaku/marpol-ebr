/**
 * Project: capcode.basics SpeedPolar.java created 2 FEB. 2009 by cyrille
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
 * $Header: /media/disk/capcodeCVSbackup/capcode.basics/src/capcode/data/SpeedPolar.java,v 1.24 2009-11-02 00:10:37 crosay Exp $
 * $Log: not supported by cvs2svn $
 * Revision 1.23  2009/09/03 21:49:20  crosay
 * Incomplete # change the logging of event
 * http://sourceforge.net/apps/trac/capcode/ticket/33
 *
 * Revision 1.22  2009/03/04 10:15:55  crosay
 * Complete - taskdisplay the equipments connected to PC
 *
 * Complete - taskexternalise strings
 *
 * Incomplete - tasknew plugin: bearing-speed-and log
 *
 * Revision 1.21  2009/02/26 21:53:06  crosay
 * Complete - taskdisplay the equipments connected to PC
 *
 * Incomplete - tasktest case for plugins in general
 *
 * Incomplete - taskupgrade of simulator
 *
 * Revision 1.20  2009/02/22 11:39:38  crosay
 * Complete - tasktask 2577644 - SpeedPolar implement polar in tables
 * https://sourceforge.net/tracker2/?func=detail&aid=2577644&group_id=100698&atid=1045270
 *
 * Revision 1.19  2009/02/20 22:28:51  crosay
 * Complete - task2611676 - take also MWV into account for computation of trueWind
 * https://sourceforge.net/tracker2/?func=detail&aid=2611676&group_id=100698&atid=1045270
 * Incomplete - tasktask 2577644 - SpeedPolar implement polar in tables
 * https://sourceforge.net/tracker2/?func=detail&aid=2577644&group_id=100698&atid=1045270
 *
 * Revision 1.2  2009/02/13 13:24:34  crosay
 * Incomplete - taskcheck correct behaviour of new speed capture mechanism
 *
 * Complete - taskhelmsman display should not access directly the speed polar
 *
 * Revision 1.1  2009/02/07 17:20:40  crosay
 * Incomplete - taskcheck correct behaviour of new speed capture mechanism
 * Complete - tasktask 2577326 - helmsman display: auto scale
 * https://sourceforge.net/tracker2/?func=detail&aid=2577326&group_id=100698&atid=1045270
 *
 * $Revision: 1.24 $
 * $State: Exp $
 */


package al.sybina.ebr.core.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathException;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import al.sybina.ebr.core.data.util.Fields;
import al.sybina.ebr.core.maths.LinearInterpolation;
import al.sybina.ebr.core.xml.XMLParser;


/**
 * this class manages the speedPolar for a given boat.
 * mainly:<br>
 * 1) load the data from a xml file into tables<br>
 * 2) retrieves the target speed and best vmg for a given true wind<br>
 * @author cyrille
 *
 */
public class SpeedPolar {
	public enum E_Mode{
		UPWIND,
		DOWNWIND,
		TRACK,
		AUTO,
		INVALID
	}

	/**name of the root in the XML file*/
	private final static String ROOT = "polar"; //$NON-NLS-1$
	private static final String TARGET_SPEEDS = ROOT + "/targetSpeeds";//$NON-NLS-1$
	private static final String HEADER = ROOT + "/header";//$NON-NLS-1$
	private static final String VERSION = HEADER + "/version";//$NON-NLS-1$
	private static final String STEPWIND = HEADER  + "/stepWind";//$NON-NLS-1$
	private static final String STEPANGLE = HEADER  + "/stepAngle";//$NON-NLS-1$

	private String fileName = ""; //$NON-NLS-1$

	private class PolarInternalTable extends Hashtable<Integer, Double>{
		private static final long serialVersionUID = 1L;
	}
	private class PolarInternalRepresentation extends Hashtable<Double, PolarInternalTable>{
		private static final long serialVersionUID = 1L;
	};

	//array organized as:
	private PolarInternalTable polarOfAWind;
	private PolarInternalRepresentation completePolar;

	//last access to getTargetSpeed must have updated this
	private Speed actualVMG;
	//result of last call to getTargetSpeed
	private Speed lastTargetSpeed;

	private XMLParser parser;

	private Wind lastWindUsed = null;

	/**used to define the name of the polar (for info)*/
	private String name = ""; //$NON-NLS-1$

	private E_Mode mode = E_Mode.AUTO;

	/**the track is given as an angle relative to the boat bearing*/
	private Angle track = new Angle(0);

	/**	store the validity of the last computation */
	private boolean lastComputationValid = false;


	/**
	 * instantiates and load a speed polar file into the tables.
	 * @param fileName : path and name of the file to be loaded.
	 * @throws Exception
	 */
	public SpeedPolar(String fileName) throws Exception{
		this();
		this.fileName = fileName;
		//load the array with data founds in the polar file
		parser = new al.sybina.ebr.core.xml.XMLParser();
		File file = new File(fileName);
		if (file.exists() && file.isFile()){
			load(fileName);
		}
		else{
//			if (fileName.isEmpty()) {
//				fileName ="tempo.xml"; //$NON-NLS-1$
//			}
//			createXMLDocument(fileName,"default polar"); //$NON-NLS-1$
		}
	}

	/**
	 * instantiates an empty polar table.
	 * creates an empty table with winds every 5 knots and angle every 10DEG
	 */
	public SpeedPolar() {
		super();
		parser = new XMLParser();
		completePolar = new PolarInternalRepresentation();
	}

	/**
	 * create a XML document with the header (name, stepWind, stepAngle).
	 * @param fileName
	 * @param name
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	private void createXMLDocument(String fileName, String name) throws ParserConfigurationException, TransformerException, FileNotFoundException, UnsupportedEncodingException {
		this.fileName = fileName;
		parser.createDocument(fileName, ROOT);
		Element header = parser.createElement(HEADER);
		parser.createElement(VERSION);
		parser.setValue(VERSION, "2.0");
		header.setAttribute("name", name); //$NON-NLS-1$
		Element targetSpeedsElement = parser.createElement(TARGET_SPEEDS);
		SortedSet<Double> sortedWinds = new TreeSet<Double>(completePolar.keySet());
		for (double tws : sortedWinds){
			Element windElement = parser.createChildInElement(targetSpeedsElement, "wind");
			windElement.setAttribute("value", Double.toString(tws));
			PolarInternalTable polar = completePolar.get(tws);
			SortedSet<Integer> sortedAngles = new TreeSet<Integer>(polar.keySet());
			for (int twa : sortedAngles){
				Element angleElement  = parser.createChildInElement(windElement, "angle");
				angleElement.setAttribute("value", Double.toString(twa));
				Double ts = polar.get(twa);
				if (ts != null){
					Element targetSpeedElement = parser.createChildInElement(angleElement, "targetSpeed");
					targetSpeedElement.setAttribute("value", Double.toString(ts));
				}
			}
		}
		parser.saveAs(fileName);
	}

	private void setValue(double windForce, int trueWindAngle, double speed){
		polarOfAWind = completePolar.get(windForce);
		if (polarOfAWind == null){
			polarOfAWind = new PolarInternalTable();
		}
		polarOfAWind.put(trueWindAngle, speed);
		completePolar.put(windForce, polarOfAWind);
	}

	private double getValue(double windForce, int trueWindAngle){
		polarOfAWind = getInterpolledPolar(windForce);
		return getValue(polarOfAWind, trueWindAngle);
	}

	private double getValue(PolarInternalTable polar, int trueWindAngle){
		Double res = polar.get(trueWindAngle);
		if (res == null){
			SortedSet<Integer> sortedAngles = new TreeSet<Integer>(polar.keySet());
			if (sortedAngles != null && !sortedAngles.isEmpty()){
				int a1 = sortedAngles.first();
				for (int a : sortedAngles){
					if (a == trueWindAngle){
						break;
					}
					else if (a > trueWindAngle){
						res = interpolledValue(polar, a1, a, trueWindAngle);
						break;
					}
				}
			}
		}
		return res == null ? 0 : res;
	}

	private Double interpolledValue(PolarInternalTable polar, int a1, int a2, int a) {
		double s1 = polar.get(a1);
		double s2 = polar.get(a2);
		return new LinearInterpolation(a1, a2, s1, s2).getResult(a);
	}

	private PolarInternalTable getInterpolledPolar(double windForce) {
		PolarInternalTable pol = completePolar.get(windForce);
		if (pol == null){
			pol = new PolarInternalTable();
			SortedSet<Double> sortedWinds = new TreeSet<Double>(completePolar.keySet());
			if (sortedWinds != null && !sortedWinds.isEmpty()){
				double wind1 = sortedWinds.first();
				for (double bclWind : sortedWinds){
					if (bclWind > windForce){
						if (Data.equals(bclWind, wind1, 1)){
							return completePolar.get(wind1);
						}
						PolarInternalTable pol1 = completePolar.get(wind1);
						PolarInternalTable pol2 = completePolar.get(bclWind);
						for (int a1 : pol1.keySet()){
							Double speed1 = getValue(pol1, a1);
							Double speed2 = getValue(pol2, a1);
							if (speed1 != null){
								if (speed2 != null){
									double speed = new LinearInterpolation(wind1, bclWind, speed1, speed2).getResult(windForce);
									pol.put(a1, speed);
								}
								else{
									pol.put(a1, speed1);
								}
							}
						}
						return pol;
					}else{
						wind1 = bclWind;
					}
				}
				//if we are here means that the wind is higher than the strongest wind in the polar, lets take the last entry
				pol = completePolar.get(sortedWinds.last());
			}
		}
		return pol;
	}

	/* (non-Javadoc)
	 * @see al.sybina.ebr.data.I_SpeedPolar#getActualVMG()
	 */
	public Speed getActualVMG() {
		return actualVMG;
	}


	public EuclideanVector[] getSpeedPolar(Wind tw) {
		PolarInternalTable polar = getInterpolledPolar(tw.speed.value);
		EuclideanVector[] res = new EuclideanVector[polar.size()];
		SortedSet<Integer> sortedAngles = new TreeSet<Integer>(polar.keySet());
		int i = 0;
		for (int a : sortedAngles){
			res[i] = new EuclideanVector(polar.get(a), a);
			i++;
		}
		return res;
	}

	/**
	 * retrives the target speed for a given true wind.
	 * the direction of the true wind is relative to the boat course.
	 * @param tw
	 * @param sea
	 * @return
	 */
	public Speed getTargetSpeed(Wind tw, Sea sea) {
		lastWindUsed = tw;
		lastTargetSpeed = new Speed(getValue(tw.speed.value, tw.angle.asInt()));
		if (lastTargetSpeed != null){
			actualVMG = new Speed(lastTargetSpeed.getValue() * Math.cos(tw.getAngle().toRadian()));
			lastTargetSpeed.setValid(tw.isValid());
			actualVMG.setValid(tw.isValid());
		}
		return lastTargetSpeed;
	}

	/**
	 * @return the last target speed searched.
	 */
	public Speed getTargetSpeed() {
		return lastTargetSpeed;
	}


	/**
	 * return the target VMG.
	 * will give the Vector (speed and angle of the best VMG UPWIND or DOWNWIND)
	 * @param tw : true wind with direction relative to the boat course.
	 * @param sea : seat status.
	 * @return the target VMG.
	 */
	public EuclideanVector getTargetVMG(Wind tw, Sea sea) {
		lastWindUsed = tw;
		EuclideanVector res = new EuclideanVector(0, 0);
		int angle = 0;
		double speed = 0;
		double vmg = 0;
		boolean validity = tw.isValid();
		E_Mode finalMode = E_Mode.UPWIND;
		if (completePolar != null){
			polarOfAWind = this.getInterpolledPolar(tw.speed.value);
			switch (mode){
			case AUTO:
				finalMode = E_Mode.TRACK != null ? E_Mode.TRACK : tw.isUpWind()? E_Mode.UPWIND : tw.isDownWind() ? E_Mode.DOWNWIND : E_Mode.TRACK;
				break;
			case UPWIND:
			case DOWNWIND:
			case TRACK:
				finalMode = mode;
			}
			int min = 0;
			int max = 360;
			vmg = Double.MIN_VALUE;
			SortedSet<Integer> sortedAngles = new TreeSet<Integer>(polarOfAWind.keySet());
			switch (finalMode){
			case UPWIND:
				if (tw.getDirection().isLeft()){
					min = 270;
					max = 360;
				} else{
					min = 0;
					max = 90;
				}
				break;
			case DOWNWIND:
				if (tw.getDirection().isLeft()){
					min = 180;
					max = 270;
				} else{
					min = 90;
					max = 180;
				}
				break;
			case TRACK:
				validity = validity && track.isValid();
				int a = track.asInt() - tw.getDirection().asInt();
				min = a - 45;
				max = a + 45;
			}
			for (int i : sortedAngles){
				if (i >= min && i <= max){
					double v = this.getValue(polarOfAWind, i);
					double vmg2 = v * Math.cos(Math.toRadians(i));
					if (vmg2 > vmg){
						vmg = vmg2;
						speed = v;
						angle = i;
					}
				}
			}

		}
		res.setVectorAngleSpeed(angle, speed);
		actualVMG = new Speed(vmg);
		actualVMG.setValid(validity);
		res.setValid(validity);
		return res;
	}

	/* (non-Javadoc)
	 * @see al.sybina.ebr.data.I_SpeedPolar#getValue()
	 */
	public Speed getValue() {
		return this.lastTargetSpeed;
	}

	/**
	 * load a file.
	 * if extension is .pol, or .xml then the program try to read as a xml polar file (see doc)<br>
	 * if extension is .csv or .txt, then the function try to read as a comma separated file.
	 * @param fileName
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws NumberFormatException
	 * @throws XPathException
	 */
	public void load(String fileName) throws ParserConfigurationException, SAXException, IOException, NumberFormatException, XPathException {
		this.fileName = fileName;
		if (isCsv(fileName)){
			loadFromCSVFile(fileName, "?");
		}else if (isMaxseaFormat(fileName)){
			loadFromCSVFile(fileName, "\t");
		}
		else{
			completePolar = new PolarInternalRepresentation();
			if (new File(fileName).exists()){
				parser.load(fileName);
				int version;
				try {
					version = parser.getAsInt(VERSION);
				} catch (XPathException e1) {
					version = -1;
				}
				if (version < 2){
					//old version,  keep compatibility
					int stepWind = parser.getAsInt(STEPWIND);
					int stepAngle = parser.getAsInt(STEPANGLE);
					String values = ""; //$NON-NLS-1$
					values = parser.getValue(TARGET_SPEEDS);
					String [] windSpeedArray = values.split("]"); //$NON-NLS-1$
					int tws = stepWind;
					for (String windSpeed : windSpeedArray){
						if (windSpeed.length()>0){
							windSpeed = windSpeed.substring(1, windSpeed.length());
							Fields tsFields = new Fields(windSpeed, ","); //$NON-NLS-1$
							int twa = stepAngle;
							polarOfAWind = new PolarInternalTable();
							for (int i = 0; i < tsFields.length(); i++){
								double targetSpeed = tsFields.getAsDouble(i);
								polarOfAWind.put(twa, targetSpeed);
								twa += stepAngle;
							}
							completePolar.put((double) tws, polarOfAWind);
							tws += stepWind;
						}
					}
				}
			}
		}
		System.out.println(completePolar);
	}

	private boolean isCsv(String fileName) {
		return fileName.endsWith(".csv");
	}

	private boolean isMaxseaFormat(String fileName) throws FileNotFoundException {
		boolean res = fileName.endsWith(".pol");
		if (res){
			BufferedReader file = new BufferedReader(new FileReader(fileName));
			String firstLine = readLine(file);
			return firstLine.contains("TWA");
		}
		return false;
	}


	private String readLine(BufferedReader file){
		String res;
		try {
			res = file.readLine();
			if (res != null && !res.isEmpty()){
				//check if comma are used for decimal
				if (res.contains(",") && !res.contains(".")){
					res = res.replace(",", ".");
				}
			}
		} catch (IOException e) {
			//EXCEPTION CAPTURE because end of file is reached.
			res = ""; //$NON-NLS-1$
		}
		return res;
	}
	/**
	 *  erases all data associated with this polar of speeds.
	 *  loads it with a CVS format file.
	 * @param fileName
	 * @return the number of records created
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public int loadFromCSVFile(String fileName, String sep) throws IOException {
		int res = 0;
		String[] possibleSep = {";", ":", "\t", ","};
		// read the test file to fill in the table (eg sangria.csv)
		BufferedReader file = new BufferedReader(new FileReader(fileName));
		completePolar = new PolarInternalRepresentation();
		String msg = readLine(file);
		if (sep.equals("?")){
			for (String s : possibleSep){
				if (msg.contains(s)){
					sep = s;
					break;
				}
			}
		}
		// ignore title
		Fields names = new Fields(msg, sep);
		int indexTWS = names.indexOf("TWS"); //$NON-NLS-1$
		int indexTWA = names.indexOf("TWA"); //$NON-NLS-1$
		int indexTS = names.indexOf("TS"); //$NON-NLS-1$
		if (indexTWS != -1 && indexTWA != -1 && indexTS != -1){
			while (msg != null) {
				msg = readLine(file);
				if (msg != null) {
					res++;
					Fields f = new Fields(msg, sep);
					double tws = f.getAsDouble(indexTWS);
					int twa = f.getAsInt(indexTWA);
					double ts = f.getAsDouble(indexTS);
					setValue(tws, twa, ts);
				}
			}
		}
		//try a TWS/TWA format
		else if (names.atPos(0).matches("TWS.TWA")){ //$NON-NLS-1$
			//read the content
			while (msg != null) {
				msg = readLine(file);
				if (msg != null) {
					Fields f = new Fields(msg, sep);
					res++;
					double tws = f.getAsDouble(0);
					for (int i=1; i < f.length(); i++){
						int twa = names.getAsInt(i);
						double ts = f.getAsDouble(i);
						setValue(tws,twa, ts);
					}
				}
			}
		}
		else if (names.atPos(0).matches("TWA.TWS") || names.atPos(0).matches("TWA")){ //$NON-NLS-1$ //$NON-NLS-2$
			msg = readLine(file);
			//read all data and put then accordingly
			while (msg != null) {
				msg = readLine(file);
				if (msg != null) {
					Fields f = new Fields(msg, sep);
					res++;
					int twa = f.getAsInt(0);
					for (int i=1; i < names.length(); i++){
						double tws = names.getAsDouble(i);
						double ts = f.getAsDouble(i);
						setValue(tws, twa, ts);
					}
				}
			}
		}
		//check if only one side of the polar is defined
		SortedSet<Double> sortedWinds = new TreeSet<Double>(completePolar.keySet());
		for (double tws : sortedWinds){
			PolarInternalTable polar = completePolar.get(tws);
			SortedSet<Integer> sortedAngles = new TreeSet<Integer>(polar.keySet());
			int minWindInThisPolar = sortedAngles.first();
			if (minWindInThisPolar > 0){
				setValue(tws, 0, 0);
			}
			int maxWindInThisPolar = sortedAngles.last();
			if (maxWindInThisPolar <= 180){
				//if yes, then complement the polar
				for (int twa : sortedAngles){
					Double ts = polar.get(twa);
					if (ts != null){
						setValue(tws, 360 - twa, ts);
					}
				}
			}
		}
		file.close();
		return res;
	}


	/**
	 *  export the polar as a CVS format file.
	 * @param fileName
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void exportToCSV(String fileName) throws IOException {
		// read the test file to fill in the table (eg sangria.csv)
		BufferedWriter file = new BufferedWriter(new FileWriter(fileName));
		completePolar = new PolarInternalRepresentation();
		String sep = ";";
		String title = "TWA/TWS;";
		for (double wind: completePolar.keySet()){
			title+= String.format("%", wind);
		}
		file.write(title);
		file.close();
//		Fields names = new Fields(msg, sep);
//		int indexTWS = names.indexOf("TWS"); //$NON-NLS-1$
//		int indexTWA = names.indexOf("TWA"); //$NON-NLS-1$
//		int indexTS = names.indexOf("TS"); //$NON-NLS-1$
//		if (indexTWS != -1 && indexTWA != -1 && indexTS != -1){
//			while (msg != null) {
//				msg = readLine(file);
//				if (msg != null) {
//					res++;
//					Fields f = new Fields(msg, sep);
//					double tws = f.getAsDouble(indexTWS);
//					int twa = f.getAsInt(indexTWA);
//					double ts = f.getAsDouble(indexTS);
//					setValue(tws, twa, ts);
//				}
//			}
//		}
//		//try a TWS/TWA format
//		else if (names.atPos(0).matches("TWS.TWA")){ //$NON-NLS-1$
//			//read the content
//			while (msg != null) {
//				msg = readLine(file);
//				if (msg != null) {
//					Fields f = new Fields(msg, sep);
//					res++;
//					double tws = f.getAsDouble(0);
//					for (int i=1; i < f.length(); i++){
//						int twa = names.getAsInt(i);
//						double ts = f.getAsDouble(i);
//						setValue(tws,twa, ts);
//					}
//				}
//			}
//		}
//		else if (names.atPos(0).matches("TWA.TWS") || names.atPos(0).matches("TWA")){ //$NON-NLS-1$ //$NON-NLS-2$
//			msg = readLine(file);
//			//read all data and put then accordingly
//			while (msg != null) {
//				msg = readLine(file);
//				if (msg != null) {
//					Fields f = new Fields(msg, sep);
//					res++;
//					int twa = f.getAsInt(0);
//					for (int i=1; i < names.length(); i++){
//						double tws = names.getAsDouble(i);
//						double ts = f.getAsDouble(i);
//						setValue(tws, twa, ts);
//					}
//				}
//			}
//		}
//		//check if only one side of the polar is defined
//		SortedSet<Double> sortedWinds = new TreeSet<Double>(completePolar.keySet());
//		for (double tws : sortedWinds){
//			PolarInternalTable polar = completePolar.get(tws);
//			SortedSet<Integer> sortedAngles = new TreeSet<Integer>(polar.keySet());
//			int minWindInThisPolar = sortedAngles.first();
//			if (minWindInThisPolar > 0){
//				setValue(tws, 0, 0);
//			}
//			int maxWindInThisPolar = sortedAngles.last();
//			if (maxWindInThisPolar <= 180){
//				//if yes, then complement the polar
//				for (int twa : sortedAngles){
//					Double ts = polar.get(twa);
//					if (ts != null){
//						setValue(tws, 360 - twa, ts);
//					}
//				}
//			}
//		}
//
//		return res;
	}

	/**
	 * define a new target speed for the wind condition.
	 * search for an existing record. if not found then
	 * insert a new record.
	 *
	 * @param tw :
	 *            Wind : true wind direction and force
	 * @param s :
	 *            Speed: boat speed
	 * @param sea :
	 *            Sea : state of the sea
	 * @param bs:
	 *            boatSettings : diferent setting of the boat (sails, etc)
	 * @return true is the operation succeded
	 */
	public boolean setNewTargetSpeed(Wind tw, Speed s, Sea sea, BoatSettings bs) {
		setValue(tw.getSpeed().intValue(), tw.getAngle().asInt(), s.value);
		return true;
	}

	public void save() throws ParserConfigurationException, TransformerException, IOException {
		saveAs(fileName);
	}

	public void saveAs(String fileName) throws ParserConfigurationException, TransformerException, IOException {
		this.fileName = fileName;
		if (isCsv(fileName)){
			exportToCSV(fileName);			
		}else{
			createXMLDocument(fileName, name);			
		}
	}

	public String getName() {
		String res = name.isEmpty()? new File(fileName).getName().split("\\.")[0] : name; //$NON-NLS-1$
		if (lastWindUsed != null){
			res = String.format("%s (%d)", res, lastWindUsed.getSpeed().asInt()); //$NON-NLS-1$
		}
		return res;
	}

	/**
	 * this method renumerize a loaded polar into a full scale array.
	 *
	 */
	public void renumerize(){
		//check if only one side of the polar is defined
		SortedSet<Double> sortedWinds = new TreeSet<Double>(completePolar.keySet());
		double previousWind = 0;
		for (double tws : sortedWinds){
			if (tws != sortedWinds.first()){
				PolarInternalTable polar = completePolar.get(tws);
				SortedSet<Integer> sortedAngles = new TreeSet<Integer>(polar.keySet());
				double stepWind = tws - previousWind;
				int previousAngle = 0;
				for (int twa : sortedAngles){
					Double ts = polar.get(twa);
					if (ts != null){
						int stepAngle = twa - previousAngle;
						int wind1 = (int) Data.floorStep(tws, (int) stepWind);
						int wind2 = (int) Data.ceilingStep(tws, (int) stepWind);
						int angle1 = (int) Data.floorStep(twa, stepAngle);
						int angle2 = (int) Data.ceilingStep(twa, stepAngle);
						double speedWind1Angle1 = getValue(wind1,angle1);
						double speedWind1Angle2 = getValue(wind1, angle2);
						double speedWind2Angle1 = getValue(wind2, angle1);
						double speedWind2Angle2 = getValue(wind2, angle2);
						double speedWind1 = new LinearInterpolation(angle1, angle2, speedWind1Angle1, speedWind1Angle2).getResult(twa);
						double speedWind2 = new LinearInterpolation(angle1, angle2, speedWind2Angle1, speedWind2Angle2).getResult(twa);
						double speed = Data.round(new LinearInterpolation(wind1, wind2, speedWind1, speedWind2).getResult(tws), 2);
						this.setValue(tws, twa, speed);
					}
					previousAngle = twa;
				}
				previousWind = tws;
			}
		}

	}

	/**
	 * @return the mode
	 */
	public final E_Mode getMode() {
		return mode;
	}

	/**
	 * @param mode the mode to set
	 */
	public final void setMode(E_Mode mode) {
		this.mode = mode;
	}

	/**
	 * toggle the mode between the available modes
	 * @param mode the mode to set
	 */
	public final E_Mode toggleMode() {
		// loop from AUTO TO TRACK, but SKIP INVALID (length-1
		int x = (mode.ordinal() + 1);
		if (x > E_Mode.AUTO.ordinal()){
			x = 0;
		}
		mode = E_Mode.values()[x];
		return mode;
	}

	/**
	 * @param track: the track to the next waypoint, as an angle / boat bearing
	 */
	public void setTrack(Angle track) {
		this.track  = track;
	}


	/**
	 *
	 * @return true if the last computation was valid.
	 * false if no computation was done or if the last was invalid.
	 */
	public boolean isValid() {
		return lastComputationValid ;
	}

	public String getFileName() {
		return fileName;
	}

}
