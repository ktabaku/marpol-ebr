/**
 *
 */
/**
 * CVS markups
 * last update by : $Author: crosay $
 * $Date: 2009-10-14 21:50:17 $
 * $Header: /media/disk/capcodeCVSbackup/capcode.basics/src/capcode/xml/XMLParser.java,v 1.28 2009-10-14 21:50:17 crosay Exp $
 * $Log: not supported by cvs2svn $
 * Revision 1.27  2009/09/03 21:49:20  crosay
 * Incomplete # change the logging of event
 * http://sourceforge.net/apps/trac/capcode/ticket/33
 *
 * Revision 1.26  2009/08/31 22:18:08  crosay
 * Incomplete # preparation for delivery to version 0.9.3
 * http://sourceforge.net/apps/trac/capcode/ticket/17
 *
 * Revision 1.25  2009/08/15 15:14:28  crosay
 * Complete # add reference points to maps
 * http://sourceforge.net/apps/trac/capcode/ticket/22
 *
 * Revision 1.24  2009/03/31 07:43:28  crosay
 * Incomplete - taskSourceForge.net: CapCode-software suite for sailors: Detail: 2705654 - NMEA data mapping (FR 2605966 Input Mapping)
 * https://sourceforge.net/tracker2/index.php?func=detail&aid=2705654&group_id=100698&atid=1045270
 *
 * Revision 1.23  2009/03/16 17:13:05  crosay
 * Complete - taskexternalise strings
 *
 * Revision 1.22  2009/03/04 10:15:55  crosay
 * Complete - taskdisplay the equipments connected to PC
 *
 * Complete - taskexternalise strings
 *
 * Incomplete - tasknew plugin: bearing-speed-and log
 *
 * Revision 1.21  2009/02/26 21:53:07  crosay
 * Complete - taskdisplay the equipments connected to PC
 *
 * Incomplete - tasktest case for plugins in general
 *
 * Incomplete - taskupgrade of simulator
 *
 * Revision 1.20  2009/02/20 22:48:20  crosay
 * Incomplete - taskimplement the possibility to change perspective
 *
 * Revision 1.18  2009/02/07 17:20:40  crosay
 * Incomplete - taskcheck correct behaviour of new speed capture mechanism
 * Complete - tasktask 2577326 - helmsman display: auto scale
 * https://sourceforge.net/tracker2/?func=detail&aid=2577326&group_id=100698&atid=1045270
 *
 * Revision 1.17  2008/11/10 10:42:05  crosay
 * undocumented changes (code washup)
 *
 * Revision 1.16  2008/10/10 21:09:02  crosay
 * modifications needed for the speedpolar xml file
 *
 * Revision 1.15  2008/09/17 11:10:26  crosay
 * added function createElementInDocument
 *
 * Revision 1.14  2008/09/11 15:23:59  crosay
 * implementation of  task 2081226 "preferences"
 *
 * Revision 1.13  2008/08/08 09:19:15  crosay
 * field filename changed from private to protected (used by implementing parsers)
 *
 * Revision 1.12  2008/06/18 19:46:24  crosay
 * complementing FR  1961649  	  plugin for race tracks
 * added the capability to:
 * - insert a waypoint
 * - delete a waypoint
 * - create and save a new track
 *
 * Revision 1.11  2008/05/28 16:28:55  crosay
 * added CVS markups in source code
 *
 * $Revision: 1.28 $
 * $State: Exp $
 */
package al.sybina.ebr.core.gpx;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import al.sybina.ebr.core.data.Height;
import al.sybina.ebr.core.geo.Position;
import al.sybina.ebr.core.geo.Sector;
import al.sybina.ebr.core.xml.XMLParser;



/**
 * Class used to access GPX xml formated files.
 * it is based on the {@link XMLParser} class.
 * to use this class: instanciate a new GPXParser either with a filename
 *
 * @author rosaycy
 *
 */
public class GpxParser extends XMLParser {
	private GpxMetaData metaData = null;

	public GpxParser(String fileName) throws ParserConfigurationException, SAXException, IOException{
		super(fileName);
	}

	public GpxParser() {
		super();
	}

	/**
	 * creates a gpx document on the base of a xml document.
	 * @param fileName : the name of the file to be created.
	 * @return a Document
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	public Element createGpxDocument(String fileName, String name) throws ParserConfigurationException, TransformerException, FileNotFoundException, UnsupportedEncodingException {
		Element root = super.createDocument(fileName, "gpx");
		this.setName(name);
		root.setAttribute("version", "1.1");
		root.setAttribute("name", name);
		root.setAttribute("creator", "capcode - http://sourceforge.net/projects/capcode/");
		if (metaData == null){
			metaData = new GpxMetaData(name);
		}			
		metaData.writeToElement(this, root);
		return root;
	}

	/**
	 * get the boundaries by looking for the max and min values in the list of waypoints.<br>
	 * @param waypointList
	 * @return a sector with the boundings values
	 */
	public Sector extractBoundFromWaypoints(Vector<GpxWaypointRecord> waypointList) {
		double minlon = Double.MAX_VALUE;
		double minlat = Double.MAX_VALUE;
		double maxlon = Double.MIN_VALUE;
		double maxlat = Double.MIN_VALUE;
		for (GpxWaypointRecord rec : waypointList) {
			double lat = rec.getPosition().getLat().toDegrees();
			double lon = rec.getPosition().getLon().toDegrees();
			if (lon < minlon) {
				minlon = lon;
			}
			if (lon > maxlon) {
				maxlon = lon;
			}
			if (lat < minlat) {
				minlat = lat;
			}
			if (lat > maxlat) {
				maxlat = lat;
			}
		}
		return new Sector(maxlat, minlon, minlat, maxlon);
	}

	/**
	 * get the bound as set in the metadata section.
	 * @return
	 */
	public Sector getBounds(){
		return metaData != null ?  metaData.getBounds() : null;
	}

	/**
	 * load the header part of the gpx file.
	 * the descendant class should overwrite this method to add spec
	 */
	@Override
	public void load(String fileName) throws ParserConfigurationException, SAXException, IOException{
		this.fileName = fileName;
		super.load(fileName);
		setName(getRoot().getAttribute("name"));
		metaData = GpxMetaData.readFromElement(this, getRoot());
	}

	/**
	 * add an extension element in the given element
	 * @param parent
	 * @return the created extension or null if the parent is null
	 */
	public Element createExtensionElement(Element parent) {
		if (parent != null){
			return createChildInElement(parent, "extension");			
		}
		return null;
	}
	
	/**
	 * get the extension of the element.
	 * @param parent the parent node
	 * @return the "extension" element or null if it does not exit
	 */
	public Element getExtentionElement(Element parent) {
		if (parent != null){
			return getElementKey(parent, "extension");			
		}
		return null;
	}
	
	/**
	 * add a route element in the file.
	 * this must be called before creating route points
	 * @return
	 */
	public Element createRouteElement() {
		return createChildInElement(getRoot(), "rte");
	}

	/**
	 * create a route point in the file.
	 * 	see the definition of the route record in http ://www.topografix.com/gpx/1/1/#type_rteType
	 * @return an Element
	 * @see Element
	 */
	public Element createRoutePoint(Element routeElement, GpxWaypointRecord wpt){
		Element rtept = createChildInElement(getRoot(), "rtept");
		routeElement.appendChild(rtept);
		wpt.writeToElement(this, rtept);
		return rtept;
	}


	/**
	 * add a waypoint element in the file, under root.
	 * the waypoint element are given in parameter.
	 * @return the Waypoint element
	 */
	public Element addWaypointElement(GpxWaypointRecord wpt){
		Element wptElement = createChildInElement(getRoot(), "wpt");
		wpt.writeToElement(this, wptElement);
		return wptElement;
	}

	public Vector<GpxWaypointRecord> getWaypointList() throws XPathExpressionException {
		Vector<GpxWaypointRecord> waypointList = new Vector<GpxWaypointRecord>();
		Vector<Element> wpts = getElementsList("//wpt");
		for (Element wptElement : wpts){
			GpxWaypointRecord wptRec = new GpxWaypointRecord();
			wptRec.readFromElement(this, wptElement);
			waypointList.add(wptRec);
		}
		return waypointList;
	}

	public Vector<GpxWaypointRecord> getRoutePoints() throws XPathExpressionException {
		Vector<GpxWaypointRecord> waypointList = new Vector<GpxWaypointRecord>();
		Vector<Element> rtePt = this.getElementKeys(getRouteElement(), "rtept");
		for (Element wptElement : rtePt){
			GpxWaypointRecord wptRec = new GpxWaypointRecord();
			wptRec.readFromElement(this, wptElement);
			waypointList.add(wptRec);
		}
		return waypointList;
	}
	
	public Element getRouteElement() {
		return this.getElementKey(getRoot(), "rte");
	}

	public String getName() {
		return getMetaData().getName();
	}

	public void setName(String name) {
		getMetaData().setName(name);
	}

	/**
	 * @return the metaData
	 */
	public GpxMetaData getMetaData() {
		if (metaData == null){
			metaData = new GpxMetaData();
		}
		return metaData;
	}

	/**
	 * @param metaData the metaData to set
	 */
	public void setMetaData(GpxMetaData metaData) {
		this.metaData = metaData;
	}

	public void setBounds(Sector bounds) {
		getMetaData().setBounds(bounds);
	}


}
