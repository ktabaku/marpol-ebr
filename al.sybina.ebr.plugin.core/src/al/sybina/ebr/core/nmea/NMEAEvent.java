package al.sybina.ebr.core.nmea;

import java.util.EventObject;

/**
 * the class NMEAEvent is used by capcode to propagate NMEA Events.
 * the possible events are:
 * newNMEAMessage : a new message has been produced 
 * noNMEATimeout : no message for a while
 * @see #eventType
 * @author crosay
 * 
 */
public class NMEAEvent extends EventObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public enum EventType {
		/** a new message has been produced */
		newNMEAMessage,
		/** no message for a while */
		noNMEATimeout
	}

	public EventType eventType;
	public NMEABasicMessage message;
	public String commSource;


	/**
	 * creates a NMEA event with the appropriate information
	 */
	public NMEAEvent(Object src, EventType type, String comm, NMEABasicMessage msg) {
		super(src);
		message = msg;
		eventType = type;
		commSource = comm;
	}
		
}
