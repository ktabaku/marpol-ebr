package al.sybina.ebr.core.events;

import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import al.sybina.ebr.core.Activator;
import al.sybina.ebr.core.nmea.NMEABasicMessage;
import al.sybina.ebr.core.nmea.NMEAEvent;
import al.sybina.ebr.core.nmea.NMEAEventListener;
import al.sybina.ebr.core.tools.Log;

/**
 * Implements the mechanisms to fire NMEAMessages event and to add listeners.
 * a class throwing NMEA event should implement the WaypointChangeEventProducer interface.
 * @author cyrille
 *
 */
public class NMEAEventManager {
	//there is only one NMEA Event manager per application
	public final static Vector<NMEAEventListener> listeners = new Vector<NMEAEventListener>();

	public static void addNMEAEventListener(NMEAEventListener listener) {
		synchronized(listeners){
			listeners.add(listener);
		}
	}

	public static void fireNMEAEvent(Object source, NMEAEvent.EventType type, String comSource, NMEABasicMessage msg) {
		NMEAEvent evt = new NMEAEvent(source, type, comSource, msg);
		for (NMEAEventListener element : (Vector<NMEAEventListener>)listeners.clone()) {
			try{
				NMEAEventListener target = element;
				if (target != source) {
					switch(type) {
					case newNMEAMessage:
						target.newNMEAEvent(evt);
						break;
					case noNMEATimeout:
						target.noNMEATimeOut(evt);
						break;
					default:
					}
				}					
			}catch(Exception ex){
				Log.severe(Activator.getBundle(), "error during firing event", ex);					
			}
		}			
	}


}