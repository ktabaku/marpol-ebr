package al.sybina.ebr.core.services;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import al.sybina.ebr.core.exceptions.InvalidDataMapException;

public interface IDataService extends IIbrCoreService{

	/**
	 * set the file used to link data and NMEA fields.
	 * calling this function will map the data sources with the information found in this file.
	 * @param newValue
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws XPathExpressionException
	 * @throws InvalidDataMapException
	 */
	void setDataMapFile(String newValue) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException, InvalidDataMapException;

	/**
	 * return a data given by its name.<br>
	 * available known data are:<br>
	 * "boatPos" or "boat pos" : returns a Position, the last known GPS position of the boat<br>
	 * "relativeWind" or "relative wind": returns a Wind, the last received relative wind<br>
	 * "trueWind" or "true wind": returns a Wind, the last received or computed true wind<br>
	 * "groundSpeed" or "ground speed": returns a Speed, the last received boat ground speed<br>
	 * "surfaceSpeed" or "surface speed": returns a Speed, the last received boat surface speed<br>
	 * "depth" return a Depth, the last depth below boat
	 * @param name
	 * @return the expected data if the name is found otherwise implementations should return null
	 */
	Object getDataByName(String name);

}
