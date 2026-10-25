/**
 * project : capcode.basics file name : Fields.java 
 * created : 15 janv. 2006 by : crosay
 * modified 16 march 2008 by crosay to take into account FR 1915403
 */
/**
 * CVS markups
 * last update by : $Author: crosay $ 
 * $Date: 2009-09-13 12:10:39 $ 
 * $Header: /media/disk/capcodeCVSbackup/capcode.basics/src/capcode/data/util/Fields.java,v 1.15 2009-09-13 12:10:39 crosay Exp $
 * $Log: not supported by cvs2svn $
 * Revision 1.14  2009/03/16 17:13:05  crosay
 * Complete - taskexternalise strings
 *
 * Revision 1.13  2009/02/07 17:20:40  crosay
 * Incomplete - taskcheck correct behaviour of new speed capture mechanism
 * Complete - tasktask 2577326 - helmsman display: auto scale
 * https://sourceforge.net/tracker2/?func=detail&aid=2577326&group_id=100698&atid=1045270
 *
 * Revision 1.12  2009/01/24 20:52:36  crosay
 * Complete - taskRemoval of sqlite
 *
 * Complete - tasksuppression of debug display for preference saving
 *
 * Complete - tasktask 2531747 animation of the wearther
 * https://sourceforge.net/tracker2/?func=detail&aid=2024403&group_id=100698&atid=1045270
 *
 * Revision 1.11  2008/10/10 20:59:42  crosay
 * added method exist for a field
 *
 * Revision 1.10  2008/05/28 16:28:55  crosay
 * added CVS markups in source code
 *
 * $Revision: 1.15 $ 
 * $State: Exp $ 
 */
package al.sybina.ebr.core.data.util;

import java.util.Vector;

/**
 * used to selected the fields in a string with a separator char <br>
 * usage:
 * 
 * <pre>
 * Fields f = new Fields(string, separator);
 * for (int i=0, i &lt; f.length(), i++){
 * 		system.out.println(f.atPos(i));
 * 		system.out.println(f.getAsFloat(i));
 * }
 * </pre>
 * 
 * @author cyrille
 */
public class Fields {
	String[] fields;
	/** separator set by default to comma */
	private String separator = ","; //$NON-NLS-1$
	private boolean lastConvertValid = true;
	
	/**
	 * setup the fields attribute from a string splited on a pattern<br>
	 * 
	 * @param from
	 * @param separator
	 *            (pattern)
	 * @see String.split()
	 */
	public Fields(String from, String separator) {
		this.separator = separator;
		fields = from.split(separator);
	}
	
	/**
	 * creates a copy of f into Fields (allows easy parsing)<br>
	 * attention! use setSeparator(String) to define the separator used to
	 * separate those fields unless the default separator "," will be used this
	 * may be an issue when using toString()
	 * 
	 * @param f
	 */
	public Fields(String[] f) {
		fields = f;
	}
	
	/**
	 * @return the number of fields
	 */
	public int length() {
		return fields.length;
	}
	
	/**
	 * get the meaning of the field at i i must be within 0 and nb fields-1
	 * otherwise the result is "" and lastConvertValid is false
	 * 
	 * @param i
	 *            : position in the array of fields
	 * @return the String at pos i if within bounds or an empty string if out of
	 *         bound. The lastConvertValid is set to false
	 */
	public String atPos(int i) {
		String res;
		if ((i >= 0) && (i < fields.length)) {
			res = fields[i];
			lastConvertValid = true;
		} else {
			lastConvertValid = false;
			res = ""; //$NON-NLS-1$
		}
		return res;
		
	}
	
	/**
	 * same than atPos, but for code maintenance and readability
	 * 
	 * @param i
	 * @return the String at pos i
	 * @see atPos
	 */
	public String getAsString(int i) {
		return atPos(i);
	}
	
	/**
	 * transform the String field at i into a float meaning as per
	 * Float.parseFloat(String) if the string is not parseable the function
	 * returns 0 and set lastConvertValid to false
	 * 
	 * @param i
	 *            : position in the array of fields
	 * @return the String at pos i parsed to float
	 * @see Float.parseFloat(String)
	 */
	public float getAsFloat(int i) {
		float res = 0;
		lastConvertValid = true;
		try {
			res = Float.parseFloat(atPos(i));
		} catch (Exception e) {
			lastConvertValid = false;
		}
		return res;
	}
	
	/**
	 * transform the String field at i into a boolean meaning as per
	 * Boolean.parseBoolean(String) if the string is not parseable the function
	 * returns false and set lastConvertValid to false example:
	 * 
	 * <pre>
	 * 	Fields f = new Fields(&quot;true/TRUE/false/FALSE/1/0/X&quot;.split(&quot;/&quot;));
	 *  f.getAsBoolean(0) and (1) ONLY shall return true
	 *  f.getAsBoolean(2), (3), (4), (5) and (6) shall return false
	 *  and set last convertValid to true
	 *  f.getAsBoolean(7) shall return false and set lastConvertValid to false
	 * </pre>
	 * 
	 * @param i
	 *            : position in the array of fields
	 * @return the String at pos i parsed to boolean (false if not parseable)
	 * @see Boolean.parseBoolean(String)
	 */
	public boolean getAsBoolean(int i) {
		boolean res = false;
		lastConvertValid = true;
		try {
			res = Boolean.parseBoolean(atPos(i));
		} catch (Exception e) {
			lastConvertValid = false;
		}
		return res;
	}
	
	/**
	 * transform the String field at i into a int meaning as per
	 * Integer.parseInteger(String) if the string is not parseable the function
	 * returns 0 and set lastConvertValid to false
	 * 
	 * @param i
	 *            : position in the array of fields
	 * @return the String at pos i parsed to Double, then casted to int (0 if
	 *         not parseable)
	 * @see Integer.parseInt(String)
	 */
	public int getAsInt(int i) {
		int res = 0;
		lastConvertValid = true;
		try {
			res = (int) Double.parseDouble(atPos(i));
		} catch (Exception e) {
			lastConvertValid = false;
		}
		return res;
	}
	
	public int getAsInt(int i, int def) {
		lastConvertValid = true;
		try {
			return (int) Double.parseDouble(atPos(i));
		} catch (Exception e) {
			lastConvertValid = false;
			return def;
		}
	}
	
	/**
	 * transform the String field at i into a int meaning as per
	 * Double.parseDouble(String) if the string is not parseable the function
	 * returns 0 and set lastConvertValid to false
	 * 
	 * @param i
	 *            : position in the array of fields
	 * @return the String at pos i parsed to double (0 if not parseable)
	 * @see Double.parseDouble(String)
	 */
	public double getAsDouble(int i) {
		double res = 0;
		lastConvertValid = true;
		try {
			res = Double.parseDouble(atPos(i));
		} catch (Exception e) {
			lastConvertValid = false;
		}
		return res;
	}
	
	public double getAsDouble(int i, double def) {
		lastConvertValid = true;
		try {
			return Double.parseDouble(atPos(i));
		} catch (Exception e) {
			lastConvertValid = false;
			return def;
		}
	}
	
	/**
	 * delete the field at index i if i is within the bound else do nothing
	 * 
	 * @param index
	 */
	public void deleteField(int index) {
		String s = ""; //$NON-NLS-1$
		if ((index >= 0) && (index < fields.length)) {
			for (int i = 0; i < index; i++) {
				s = s + fields[i] + separator;
			}
			for (int i = index + 1; i < fields.length; i++) {
				s = s + fields[i] + separator;
			}
			fields = s.split(separator);
		}
	}
	
	/**
	 * gives the content of the fields as a string with separators
	 */
	@Override
	public String toString() {
		String result = ""; //$NON-NLS-1$
		for (int i = 0; i < fields.length - 1; i++) {
			result = result + fields[i] + separator;
		}
		result = result + fields[fields.length - 1];
		return (result);
	}
	
	/**
	 * gives the content of the fields as a string with separator given in
	 * parameter
	 */
	public String toString(String sep) {
		setSeparator(sep);
		return (toString());
	}
	
	/**
	 * @return the lastConvertValid
	 */
	public boolean isLastConvertValid() {
		return lastConvertValid;
	}
	
	/**
	 * @return the separator
	 */
	public String getSeparator() {
		return separator;
	}
	
	/**
	 * @param separator
	 *            the separator to set
	 */
	public void setSeparator(String separator) {
		this.separator = separator;
	}
	
	public boolean exists(String v) {
		boolean res = false;
		for (String s : fields) {
			if (s.equals(v)) {
				res = true;
				break;
			}
		}
		return res;
	}
	
	/**
	 * @param meaning
	 * @return the position of meaning in the fields or -1 if not found.
	 */
	public int indexOf(String value) {
		int res = -1;
		for (int i = 0; i < length(); i++) {
			if (fields[i].equals(value)) {
				res = i;
				break;
			}
		}
		return res;
	}
	
	/**
	 * @return and array of string representing all the fields.
	 */
	public String[] toArray() {
		return fields;
	}
	
	public Vector<String> toVector() {
		Vector<String> res = new Vector<String>(fields.length);
		for (int i = 0; i < fields.length; i++) {
			res.add(this.atPos(i));
		}
		return res;
	}
	
}
