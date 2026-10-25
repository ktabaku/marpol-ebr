/**
 * project : capcode.basics
 * package : al.sybina.ebr.maps.transform
 * created by :cyrille rosay on 29 janv. 2006
 */
/**
 * CVS markups
 * last update by : $Author: crosay $
 * $Date: 2009-08-28 23:38:34 $
 * $Header: /media/disk/capcodeCVSbackup/capcode.basics/src/capcode/maps/position/CoordPattern.java,v 1.7 2009-08-28 23:38:34 crosay Exp $
 * $Log: not supported by cvs2svn $
 * Revision 1.6  2009/08/25 22:33:59  crosay
 * Complete # add reference points to maps
 * http://sourceforge.net/apps/trac/capcode/ticket/22
 * Incomplete # move the display of the VMG arrow from map display plugin to polar display plugin
 * http://sourceforge.net/apps/trac/capcode/ticket/29
 *
 * Revision 1.5  2009/03/04 10:15:55  crosay
 * Complete - taskdisplay the equipments connected to PC
 *
 * Complete - taskexternalise strings
 *
 * Incomplete - tasknew plugin: bearing-speed-and log
 *
 * Revision 1.4  2008/05/28 16:28:54  crosay
 * added CVS markups in source code
 *
 * $Revision: 1.7 $
 * $State: Exp $
 */

package al.sybina.ebr.core.geo;

import java.util.regex.Pattern;

import al.sybina.ebr.core.data.util.Fields;


/**
 * used to check if the string in input of a coords matches a known pattern<br>
 * evailable patterns are<br>
 * 0 : Xnn*nn'nn.nnnn where X can be N,E,S,W : <br>
 * means that the Coords in in deg*min'sec.decimal<br>
 * 1 : Xnn.nnnnnn where X can be N,E,S,W : <br>
 * means that the Coords in in deg.decimal<br>
 * 2 : Xnn�nn.nnnn where X can be N,E,S,W : <br>
 * means that the Coords in in deg�min.decimal<br>
 * 3 : Xnnnn.nnnn or Xnnnnn.nnnn where X can be N,E,S,W : <br>
 * means that the Coords in in degmin.decimal. there is no deg separator,
 * assuming is made on the fact that there are always 2 digits for minutes (eg
 * NMEA GPS format)<br>
 * 4 : Xnn*nn'nn.nn" where X can be N,E,S,W : <br>
 * means that the Coords in in deg*min'sec.dec
 *
 * @author cyrille Rosay
 *
 */
public class CoordPattern {
	private static final String PATTERN_DMD = "[NESW]\\d{1,3}°\\d{1,2}'\\d{1,2}\\.?\\d+"; //$NON-NLS-1$
	private static final String PATTERN_DMD_CAPTURE = "[NESW]|°|'"; //$NON-NLS-1$
	private static final String PATTERN_DMD2 = "\\d{1,3}°\\d{1,2}'\\d{1,2}\\.?\\d+[NESW]"; //$NON-NLS-1$
	private static final String PATTERN_DMD2_CAPTURE = "°|'"; //$NON-NLS-1$
	private static final String PATTERN_GOOGLE_EARTH1 = "\\d{1,3}°\\d{1,2}'\\d{1,2}\\.?\\d+\"[NESW]"; //$NON-NLS-1$
	private static final String PATTERN_GOOGLE_EARTH1_CAPTURE = "°|'|\""; //$NON-NLS-1$
	private static final String PATTERN_DEG = "[NESW]\\d{1,3}\\.\\d+"; //$NON-NLS-1$
	private static final String PATTERN_DEG_CAPTURE = "[NESW]"; //$NON-NLS-1$
	private static final String PATTERN_DEG2 = "\\d{1,3}\\.\\d+"; //$NON-NLS-1$
	private static final String PATTERN_DEG2_CAPTURE = ""; //$NON-NLS-1$
	private static final String PATTERN_DMM = "[NESW]\\d{1,3}°\\d{1,2}\\.\\d+"; //$NON-NLS-1$
	private static final String PATTERN_DMM_CAPTURE = "[NESW]|°"; //$NON-NLS-1$
	private static final String PATTERN_DMM_NO_DEGSEP = "[NESW]\\d{4,5}\\.\\d+"; //$NON-NLS-1$
	private static final String PATTERN_DMM_NO_DEGSEP_CAPTURE = "[NESW]"; //$NON-NLS-1$
	private static final String PATTERN_DMS = "[NESW]\\d{1,3}°\\d{1,2}'\\d{1,2}\""; //$NON-NLS-1$
	private static final String PATTERN_DMS_CAPTURE = "[NESW]|°|'|\""; //$NON-NLS-1$
	private static final String[] PATTERNS = {
		PATTERN_DMD,
		PATTERN_DMD2,
		PATTERN_GOOGLE_EARTH1,
		PATTERN_DEG,
			PATTERN_DEG2,
		PATTERN_DMM,
		PATTERN_DMM_NO_DEGSEP,
		PATTERN_DMS };
	private static final String[] PATTERNS_CAPTURE = {
		PATTERN_DMD_CAPTURE,
		PATTERN_DMD2_CAPTURE,
		PATTERN_GOOGLE_EARTH1_CAPTURE,
		PATTERN_DEG_CAPTURE,
			PATTERN_DEG2_CAPTURE,
		PATTERN_DMM_CAPTURE,
		PATTERN_DMM_NO_DEGSEP_CAPTURE,
		PATTERN_DMS_CAPTURE };
	public static final int RES_PATTERN_DMD = 0;
	public static final int RES_PATTERN_DMD2 = 1;
	public static final int RES_PATTERN_GOOGLE_EARTH1 = 2;
	public static final int RES_PATTERN_DEG = 3;
	public static final int RES_PATTERN_DEG2 = 4;
	public static final int RES_PATTERN_DMM = 5;
	public static final int RES_PATTERN_DMM_NO_DEGSEP = 6;
	public static final int RES_PATTERN_DMS = 7;
	public static final int RES_PATTERN_UNKNOWN = -1;

	/**
	 * @param text
	 * @return the index of the pattern for this text. 0: deg�min'sec.decimal<br>
	 *         1 : deg.decimal<br>
	 *         2:deg�min.decimal<br>
	 *         -1 : unknown pattern<br>
	 */
	public static int getPattern(String text) {
		for (int i = 0; i < PATTERNS.length; i++) {
			if (Pattern.matches(PATTERNS[i], text)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * split the string around the given code pattern<br>
	 * eg: N45�24'10.0010 has the pattern [NESW]\d(2,3)�\d(1,2)'d(1,2).\d?<br>
	 * this function will return the Fields: <empty>, 45, 24 and 10.0010<br>
	 *
	 * @see Fields
	 * @param text :
	 *            the string to be split around the pattern
	 * @param codePattern :
	 *            the pattern by its code (see constants)
	 * @return a Fields class
	 * @link CoordPatternTest.java
	 */
	public static String[] split(String text, int codePattern) {
		String[] res = { "", "" }; //$NON-NLS-1$ //$NON-NLS-2$
		if ((codePattern >= 0) && (codePattern < PATTERNS_CAPTURE.length)) {
			String capture = PATTERNS_CAPTURE[codePattern];
			res = text.split(capture);
		}
		return res;
	}
}
