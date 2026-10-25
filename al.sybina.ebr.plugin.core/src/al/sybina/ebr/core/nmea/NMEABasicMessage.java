/**
 * project : capcode.basics
 * file name : NMEABasicSentMessage.java
 * created : 15 janv. 2006 by : cyrille
 */
package al.sybina.ebr.core.nmea;

import al.sybina.ebr.core.data.util.Fields;

/**
 * class used for definition of NMEA message received from and send to
 * equipments.<br>
 * basically the messages managed here are already primarily decoded by
 * NMEADecoder. The NMEADecoder remove the "$" marker at the beginning of the
 * message. this class add a "$" marker in its toString() method
 * 
 * @author cyrille
 */
public abstract class NMEABasicMessage implements I_NMEAMessageBuilder {
	private String workMessage;
	protected String equipment;
	protected String code;
	private Fields fields;
	private String messageChecksum;
	protected static final String SEP = ","; //$NON-NLS-1$
	private boolean checksumCorrect;
	private String computedChecksum;
	private E_NMEACodes NMEACode;
	private String message = ""; //$NON-NLS-1$
	
	/**
	 * extract the code of a NMEA message is valid. if not valid the code
	 * returned is "" (empty String)
	 * 
	 * @param msg
	 * @return
	 */
	private static String extractCode(String msg) {
		String s = new Fields(msg, SEP).atPos(0);
		if (s.length()>2){
			return s.substring(s.length()-3);			
		}else{
			return "???";
		}
	}
	
	/**
	 * extract the code of a NMEA message is valid. if not valid the code
	 * returned is "" (empty String)
	 * 
	 * @param msg
	 * @return
	 */
	private static String extractEquipment(String msg) {
		String s = new Fields(msg, SEP).atPos(0);
		if (s.length()>2){
			return s.substring(0, s.length()-3);			
		}else{
			return "??";
		}
	}
	
	/**
	 * creates from a string received from a NMEA equipment
	 * 
	 * @param message
	 */
	public NMEABasicMessage(String message) {
		super();
		// need to work with a clean message (without $ and *Checksum)
		this.message = message;
		workMessage = remove$(message);
		// first transform the "*" into a field so we can read the checksum
		setFields(new Fields(workMessage.replace("*", SEP), SEP)); //$NON-NLS-1$
		if (message.indexOf('*') != -1) {
			messageChecksum = getFields().atPos(getFields().length() - 1);
			if (messageChecksum.length() >= 2) {
				messageChecksum = messageChecksum.substring(0, 2);
			}
		} else {
			// no checksum but we can compute a checksum
			messageChecksum = computedChecksum(message);
		}
		workMessage = removeChecksum(workMessage);
		equipment = extractEquipment(workMessage);
		try {
			code = extractCode(workMessage);
			NMEACode = E_NMEACodes.valueOf(code);
		} catch (Exception ex) {
			code = "XXX";
			NMEACode = E_NMEACodes.noCode;
		}
		computedChecksum = computedChecksum(message);
		setChecksumCorrect(messageChecksum.equals(computedChecksum));
	}
	
	/**
	 * creates a NMEABAsic message, given its equipment and code the rest will
	 * have to be created by each NMEA children class
	 * 
	 * @param eq
	 * @param code
	 */
	public NMEABasicMessage(String eq, String code) {
		super();
		equipment = eq;
		this.code = code;
		NMEACode = E_NMEACodes.valueOf(code);
		setChecksumCorrect(true);
	}
	
	/*
	 * remove the $ of a NMEA message
	 */
	private static String remove$(String msg) {
		String res = ""; //$NON-NLS-1$
		if (msg.length() > 0) {
			res = msg.substring(1);
		}
		return res;
	}
	
	/*
	 * remove the CRC fields of a NMEA message
	 */
	private static String removeChecksum(String msg) {
		String res = msg;
		if (msg.indexOf('*') != -1) {
			res = msg.substring(0, msg.indexOf('*'));
		}
		return res;
		
	}
	
	/**
	 * Return the NMEA message as a string<br>
	 * this method creates the begining of the message then call buildString()
	 * that every subClass must implement. then the method add * plus the
	 * checksum.
	 */
	@Override
	public String toString() {
		String res = "$" + equipment + code + SEP + buildString(); //$NON-NLS-1$
		return (res + "*" + this.computedChecksum(res)); //$NON-NLS-1$
	}
	
	/**
	 * compute the checksum for the message passed in parameter. call the static
	 * method computeaNMEAChecksum.
	 * 
	 * @see #computeaNMEAChecksum(String)
	 */
	public String computedChecksum(String s) {
		return computeaNMEAChecksum(s);
	}
	
	/**
	 * This static method is used to compute a checksum from a string starting
	 * with a $ and finishing with a * (if no * is found the the end of line)
	 * the CRC is computed starting from the 2nd character ($ is ignored) but
	 * the * char is counted in the checksum. see NMEA norm
	 */
	public static String computeaNMEAChecksum(String s) {
		byte cs = 0;
		// start from position 1
		// stop when end of string or when char * is reached
		for (int i = 0; (i < s.length()) && (s.charAt(i) != '*'); i++) {
			if (s.charAt(i) != '$' && s.charAt(i) != '!') {
				cs ^= (byte) s.charAt(i);
			}
		}
		String res = String.format("%2X", cs).replace(' ', '0'); //$NON-NLS-1$
		return res;
	}
	
	/**
	 * compute the checksum of the message
	 * 
	 * @return the checksum of the message
	 */
	public String getComputedChecksum() {
		return computedChecksum(this.workMessage);
	}
	
	/**
	 * @return the messageChecksum found in the message
	 */
	public String getChecksum() {
		return messageChecksum;
	}
	
	/**
	 * returns true if during the analysis of the message, the Checksum was
	 * found correct
	 * 
	 * @return the valid field
	 */
	public boolean isChecksumCorrect() {
		return checksumCorrect;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see al.sybina.ebr.nmea.I_NMEAMessageBuilder#buildString()
	 */
	public abstract String buildString();
	
	/**
	 * @return the NMEACode
	 */
	public E_NMEACodes getNMEACode() {
		return NMEACode;
	}
	
	/**
	 * @return the equipment
	 */
	public String getEquipment() {
		return equipment;
	}
	
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * 
	 * @return the original message
	 */
	public String getOriginalText() {
		return message;
	}
	
	/**
	 * @param fields
	 *            the fields to set
	 */
	public void setFields(Fields fields) {
		this.fields = fields;
	}
	
	/**
	 * @return the fields
	 */
	public Fields getFields() {
		return fields;
	}
	
	/**
	 * @param valid
	 *            the valid to set
	 */
	public void setChecksumCorrect(boolean valid) {
		this.checksumCorrect = valid;
	}
	
	/**
	 * extract the Checksum from the message.
	 * 
	 * @param msg
	 * @return
	 */
	public static String extractCheckum(String msg) {
		String res = "**"; //$NON-NLS-1$
		int index = msg.indexOf('*');
		if (index != -1) {
			res = msg.substring(index + 1, index + 3);
		}
		return res;
	}
	
}
