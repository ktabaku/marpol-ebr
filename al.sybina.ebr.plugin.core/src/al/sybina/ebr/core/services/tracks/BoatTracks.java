/**
 * Project: capcode.basics BoatTrace.java created 23 Mar 2008 by rosaycy
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
 * $Date: 2009-11-02 00:25:07 $
 * $Header: /media/disk/capcodeCVSbackup/al.sybina.ebr.plugins.dataRecorder/src/capcode/dataRecorder/BoatTrace.java,v 1.7 2009-11-02 00:25:07 crosay Exp $
 * $Log: not supported by cvs2svn $
 * Revision 1.6  2009/09/03 21:49:29  crosay
 * Incomplete # change the logging of event
 * http://sourceforge.net/apps/trac/capcode/ticket/33
 *
 * Revision 1.5  2009/08/31 22:19:42  crosay
 * Incomplete # preparation for delivery to version 0.9.3
 * http://sourceforge.net/apps/trac/capcode/ticket/17
 *
 * Revision 1.4  2009/07/28 10:56:03  crosay
 * Complete # update the dispay algorithm for the boat trace
 * http://sourceforge.net/apps/trac/capcode/ticket/12
 *
 * Revision 1.3  2009/03/04 14:42:28  crosay
 * Complete - taskexternalise strings
 *
 * Revision 1.2  2008/10/10 21:11:40  crosay
 * added recorded fields
 *
 * Revision 1.1  2008/08/08 09:24:51  crosay
 * reorganisation of packages
 * BoatTrace is now in the plugin project
 *
 * Revision 1.5  2008/06/18 19:46:24  crosay
 * complementing FR  1961649  	  plugin for race tracks
 * added the capability to:
 * - insert a waypoint
 * - delete a waypoint
 * - create and save a new track
 *
 * Revision 1.4  2008/05/28 16:28:55  crosay
 * added CVS markups in source code
 *
 * $Revision: 1.7 $
 * $State: Exp $
 */

package al.sybina.ebr.core.services.tracks;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.eclipse.osgi.internal.debug.Debug;
import org.w3c.dom.Element;


import al.sybina.ebr.core.Activator;
import al.sybina.ebr.core.data.Speed;
import al.sybina.ebr.core.data.Wind;
import al.sybina.ebr.core.data.util.Date;
import al.sybina.ebr.core.geo.Position;
import al.sybina.ebr.core.geo.transform.CoordsTranslator;
import al.sybina.ebr.core.geo.transform.Path2DAffineTransform;
import al.sybina.ebr.core.tools.Log;
import al.sybina.ebr.core.xml.XMLParser;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;

/**
 * Manages the boat trace information. the boat trace information is the
 * periodic data stored on a record file during navigation the data stored are
 * (in <b>bold</b> is the information implemented:<br>
 * <b>- local time (dd/mm/yyyy hh:mm:ss)<br> - position (latitude/longitude)<br> -
 * boat surface speed (in knots)<br> - target speed (in knots)<br>
 * </b>- boat magnetic heading (in degrees)<br> - boat ground speed (in knots)<br> -
 * boat true heading (in degrees)<br>
 *
 * <pre>
 * &amp;lt?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot; standalone=&quot;no&quot;?&amp;gt
 * &amp;ltDataRecords&amp;gt
 * &amp;ltrecord date=&quot;22/02/2008 00:00:24&quot; position=&quot;N43�26'03.540/E003�41'11.580&quot; speed=&quot; 4.00&quot; targetSpeed=&quot; 0.00&quot;/&amp;gt
 * </pre>
 *
 * the general specifications are:
 *
 * <pre>
 * 1) the data are stored in a repository capcode/records
 * 2) file name is build using a prefix + the date .xml
 * the prefix is by default record but can be changed by configuration.
 * 3) the conditions to write a new point are:
 * 	- the boat has moved for more than 0.01 mile (this number is modifiable).
 * 	- the direction of the boat has changed for more than 10 degrees (modifiable).
 *  - the speed has significantly changed (modifiable parameter)
 * &#064;author rosaycy
 *
 */
public class BoatTracks {
	private static final String GPX_ROOT = "gpx"; //$NON-NLS-1$
	/** the parser to access the xml structure */
	private XMLParser parser;
	private String fileName;
	private double minlon;
	private double minlat;
	private double maxlon;
	private double maxlat;
	private Element root;
	private Element boundsElement;
	private Path path = new Path();

	public class Record {
		/** the date of the record */
		public Calendar date;
		/** the position of the boat */
		public Position boatPos;
		/** the boat speed */
		public Speed boatSpeed;
		/** the expected target speed */
		public Speed targetSpeed;

		/**
		 * create an empty record. date is set to local date/time<br>
		 */
		public Record() {
			super();
			date = Date.getActualGMTDateTime();
			boatPos = new Position("/"); //$NON-NLS-1$
			boatSpeed = new Speed(0);
			targetSpeed = new Speed(0);
		}

		/**
		 * createa a new Record.
		 *
		 * @param d
		 *            the date of the event as a Calendar (gmt)
		 * @param pos
		 *            Position of the boat
		 * @param speed :
		 *            Speed of the boat
		 * @param target:
		 *            target Speed of the boat
		 */
		public Record(Calendar d, Position pos, Speed speed, Speed target) {
			super();
			date = d;
			boatPos = pos;
			boatSpeed = speed;
			targetSpeed = target;
		}
	}

	/**
	 * basic creator. instantiates the parser.
	 */
	public BoatTracks() {
		super();
	}

	/**
	 * creates a BoatTrace records file object and fills it with a XML file.
	 * tries to open the file, if failed, then the file is created from scratch.
	 * @throws TransformerException
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	public BoatTracks(String fileName) throws TransformerException, FileNotFoundException, UnsupportedEncodingException {
		this();
		loadGpxFile(fileName);
	}


	public Vector<Record> getAllGpxRecords() {
		Vector<Record> res = new Vector<Record>(25);
		try {
			for (Element e : parser.getElementsList("//trkpt")) {
				String dateStr = e.getAttribute("time");
				Calendar date = Date.parseISO8601(dateStr);
				Position pos = new Position(
						Double.parseDouble(e.getAttribute("lat")),
						Double.parseDouble(e.getAttribute("lon")));
				res.add(new Record(
						date,
						pos,
						new Speed(0),
						new Speed(0)));
			}
		} catch (Exception e) {
			Log.severe(Activator.getBundle(), "error reading records", e);
		}
		return res;
	}

	/**
	 *
	 * @param fileName
	 * @throws TransformerException
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	public void loadGpxFile(String fileName) throws TransformerException, FileNotFoundException, UnsupportedEncodingException {
		this.fileName = fileName;
		File file = new File(fileName);
		if (!file.exists()) {
			try {
				createGpxFile(fileName);
			} catch (ParserConfigurationException e) {
				Log.severe(Activator.getBundle(), e.getMessage(), e);
			}
		}
		if (parser == null) {
			parser = new XMLParser();
		}
		try {
			parser.load(fileName);
			root = parser.getRoot();
			boundsElement = parser.getElementKey(root, "bounds");
			if (boundsElement == null) {
				boundsElement = parser.createChildInElement(root, "bounds");
			}
		} catch (Exception e) {
			Log.severe(Activator.getBundle(), e.getMessage(), e);
		}
		Vector<Record> records = getAllGpxRecords();
		for (Record rec : records) {
			addToPath(rec.boatPos.getLon().toDegrees(), rec.boatPos.getLat().toDegrees());
		}
	}

	private void addToPath(double lon, double lat) {
		path.getElements().add(new LineTo(lon, lat));
//		if (path.getxCurrentPoint() == null) {
//			path.getElements().add(new MoveTo(lon, lat));
//		} else {
//			path.getElements().add(new LineTo(lon, lat));
//		}
	}

	public void clearGpx() throws ParserConfigurationException, TransformerException, FileNotFoundException, UnsupportedEncodingException {
		createGpxFile(fileName);
	}

	/**
	 * creates a gpx records file with the given fileName
	 *
	 * @param fileName
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	public void createGpxFile(String fileName) throws ParserConfigurationException, TransformerException, FileNotFoundException, UnsupportedEncodingException {
//		path.reset();
		parser = new XMLParser();
		File f = new File(fileName);
		if (!f.exists()) {
			File dir = new File(f.getPath().replace(f.getName(), ""));
			dir.mkdir();
		}
		parser.createDocument(fileName, GPX_ROOT);
		root = parser.getElement(GPX_ROOT);
		root.setAttribute("version", "1.1");
		root.setAttribute("name", this.fileName);
		root.setAttribute("creator", "capcode - http://sourceforge.net/projects/capcode/");
		Calendar now = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		Element time = parser.createChildInElement(root, "time");
		time.setTextContent(Date.toISO8601(now));
		minlon = Double.MAX_VALUE;
		minlat = Double.MAX_VALUE;
		maxlon = Double.MIN_VALUE;
		maxlat = Double.MIN_VALUE;
		boundsElement = parser.createChildInElement(root, "bounds");
		boundsElement.setAttribute("minlat", Double.toString(minlat));
		boundsElement.setAttribute("maxlat", Double.toString(maxlat));
		boundsElement.setAttribute("minlon", Double.toString(minlon));
		boundsElement.setAttribute("maxlon", Double.toString(maxlon));
		createGpxSegment();
	}

	private Element createGpxSegment() {
		Element trkElement = parser.createChildInElement(root, "trk");
		return parser.createChildInElement(trkElement, "trkseg");
	}

	private Element getGpxSegment() throws XPathExpressionException {
		Vector<Element> trkElements = parser.getElementsList("//trk");
		if (trkElements.isEmpty()) {
			return createGpxSegment();
		}
		return parser.getElementKey(trkElements.lastElement(), "trkseg");
	}

	public Shape getGpxPath(CoordsTranslator ct) {
//		path.getTransforms().add(new Path2DAffineTransform(ct));
		System.out.println("Fix this if you want to see correct GpxPath");
		return path;
	}

	public void addGpxRecord(Position boatPos, Speed boatSpeed, Speed targetSpeed, Wind tw) throws ParserConfigurationException,
			XPathExpressionException, TransformerException, FileNotFoundException, UnsupportedEncodingException {
		if (parser == null){
			loadGpxFile(fileName);
		}
		Element trkseg = getGpxSegment();

		if (trkseg == null) {
			trkseg = createGpxSegment();
		}
		double lat = boatPos.getLat().toDegrees();
		double lon = boatPos.getLon().toDegrees();
		if (lon < minlon) {
			minlon = lon;
			boundsElement.setAttribute("minlon", Double.toString(minlon));
		}
		if (lon > maxlon) {
			maxlon = lon;
			boundsElement.setAttribute("maxlon", Double.toString(maxlon));
		}
		if (lat < minlat) {
			minlat = lat;
			boundsElement.setAttribute("minlat", Double.toString(minlat));
		}
		if (lat > maxlon) {
			maxlat = lat;
			boundsElement.setAttribute("maxlat", Double.toString(maxlat));
		}

		// create a track point
		Debug.println("track point created");
		Element trkpt = parser.createChildInElement(trkseg, "trkpt");
		trkpt.setAttribute("lat", Double.toString(boatPos.getLat().toDegrees()));
		trkpt.setAttribute("lon", Double.toString(boatPos.getLon().toDegrees()));
		Element timeElement = parser.createChildInElement(trkpt, "time");
		Calendar c = Calendar.getInstance();
		timeElement.setTextContent(Date.toISO8601(c));
		addToPath(lon, lat);
		parser.save();
	}

}