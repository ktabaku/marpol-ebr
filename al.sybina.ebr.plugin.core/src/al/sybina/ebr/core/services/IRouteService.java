package al.sybina.ebr.core.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.Calendar;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import al.sybina.ebr.core.gpx.GpxRouteRecord;

/**
 * implements the folowing services:<br>
 * getRoute <br>
 * open(filename)<br>
 * save()<br>
 * saveAs(filename)<br>
 * createNewRoute()<br>
 * setDepartureTile(Calendar)<br>
 * setDeparted(boolean)<br>
 * @author cyrille
 *
 */
public interface IRouteService extends IIbrCoreService{

	/**
	 * @return the actual route
	 */
	GpxRouteRecord getRoute();

	/**
	 * open a gpx route given its file name.
	 * @param fileName
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws XPathExpressionException 
	 */
	void open(String fileName) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException;

	/**
	 * save the route under it's fileName.
	 * @throws TransformerException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	void save() throws ParserConfigurationException, SAXException, TransformerException, FileNotFoundException, UnsupportedEncodingException;

	/**
	 * save the route under a new name.
	 * @throws TransformerException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	void saveAs(String fileName) throws ParserConfigurationException, SAXException, TransformerException, FileNotFoundException, UnsupportedEncodingException;

	/**
	 * create a new empty route.
	 * @throws ParserConfigurationException
	 */
	void createNewRoute() throws ParserConfigurationException;

	/**
	 * inverts the waypoints in the route
	 */
	void invertRoute();

	String getRouteDefaultPath();

	/**
	 * set the time of departure for the active route.
	 * @param time
	 */
	void setDepartureTime(Calendar time);

	/**
	 * set the route as departed or not depending on b.
	 * @param b 
	 */
	void setDeparted(boolean b);
	
	void saveRouteDatas(String departure_draft,String arrival_draft,String departure_airDraft,String arrival_airDraft,String departure_zone,String arrival_zone,String eta,String depDate,String fuelReq,String fuelRob,String hsfo,String hsfo1,String lsfo,String lsfo1,String doString,String do1);
}
