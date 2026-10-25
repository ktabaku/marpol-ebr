package al.sybina.ebr.core.serialcomm;

import java.util.HashMap;

import al.sybina.ebr.core.nmea.NMEABasicMessage;

public interface ISerialPortNMEA {

	/**
	 * closes the port.<br>
	 */
	void close();

	/**
	 * @return true if the NMEA signature was identified in the 
	 * serial char flow (starts with $ end ends with LF/CR
	 */
	boolean isNMEA();

	/**
	 * return a string containing the parameter of the comm port but not the name.<br>
	 * the data are in this order: baudRate, dataBits, stopBit, parity, flowCtrl
	 * ex: "9600,8,1,1,0"
	 * @return the parameter of the comm port.
	 */
	String getParameter();

	/**
	 * get the port name. 
	 * eg COM 1<br>
	 * @return a string indicating the name of the comm port.
	 */
	String getPort();

	/**
	 * return as a NMEABasicMessage the last received NMEA message.
	 * @return the last NMEA message
	 */
	NMEABasicMessage getLastNmeaMessage();

	/**
	 * returns a hashmap of string and message NMEA.
	 * the key is the name of the equipment (2 letters) eg. GP (for GPS).
	 * the data is a NMEAMessage sent by this instrument.
	 * @return
	 */
	HashMap<String, NMEABasicMessage>  getEquipments();

	/**
	 * reconfigure the comm port with the parameter in the string
	 * @param parameter
	 * @throws Exception 
	 */
	void reconfigureWith(String parameter) throws Exception;

}
