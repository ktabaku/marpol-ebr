package al.sybina.ebr.core.services;

import java.util.Hashtable;

import al.sybina.ebr.core.events.IWarningEventListener;
import al.sybina.ebr.core.warnings.CoreWarningMessage;

public interface IWarningService extends IIbrCoreService {
	/**
	 * add a warning message to the list of messages
	 * @param origin : the originator of the message (free text: can be the plugin, the service, etc)
	 * @param message : the message to be transmitted to the warning service
	 */
	public void newMessage(String origin, CoreWarningMessage message);
	/**
	 * add a listener to the warning service. 
	 * this listener will be activated for every change in the warning system.
	 * @param iWarningEventListener
	 */
	public void addWarningListener(IWarningEventListener warningEventListener);
	/**
	 * returns the list of active messages.
	 * @return
	 */
	public Hashtable<String, CoreWarningMessage> getWarningList();
	/**
	 * return a code giving the warning status.
	 * 0 : no warning
	 * 1 : at least one message
	 * 2 : at least one caution
	 * 4 : at least one warning
	 * 3 and 5 to 7 : combination of the above.
	 * @return
	 */
	public int getWarningStatus();
	
	/**
	 * remove a message identified by its key
	 * @param key
	 */
	public void removeMessage(String key);
}
