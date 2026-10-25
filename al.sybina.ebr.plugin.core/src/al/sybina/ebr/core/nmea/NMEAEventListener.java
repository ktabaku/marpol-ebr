package al.sybina.ebr.core.nmea;
import java.util.EventListener;
/**
 * this interface calls for a class implementing it to have a set 
 * of methods managing NMEA events.<br>
 */
public interface NMEAEventListener extends EventListener{
	/**
	 * new NMEA message has been produced.
	 */
	public void newNMEAEvent(NMEAEvent event);
	/**
	 * no NMEA message has been received or produced for a while.
	 */
	public void noNMEATimeOut(NMEAEvent event);
}
