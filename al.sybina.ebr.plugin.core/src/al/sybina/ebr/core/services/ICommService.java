package al.sybina.ebr.core.services;

import java.util.Vector;

import al.sybina.ebr.core.serialcomm.ISerialPortNMEA;

public interface ICommService extends IIbrCoreService{

	/**
	 * add a comm port with the specified parameter as string
	 * @param sp eg COM1,4800,8,1,0
	 * @throws Exception 
	 */
	public void addCommPort(String sp) throws Exception;

	public void freezeConfiguration();

	public String getConfiguration();

	public void scanAllPorts();

	public void validateConfiguration();

	public void removeCommPort(ISerialPortNMEA serialNMEA);

	public Vector<ISerialPortNMEA> getSerialPorts();

	/**
	 * define the startup mode of the service.<br>
	 * @param string
	 */
	public void setStartupMode(String string);

	/**
	 * set the startup configuration of the service.<br>
	 * @param string
	 */
	public void setStartupConfiguration(String string);

}
