/**
 *
 */
/**
 * CVS markups
 * last update by : $Author: crosay $
 * $Date: 2009-03-04 10:15:55 $
 * $Header: /media/disk/capcodeCVSbackup/capcode.basics/src/capcode/data/util/Date.java,v 1.10 2009-03-04 10:15:55 crosay Exp $
 * $Log: not supported by cvs2svn $
 * Revision 1.9  2009/02/20 22:29:14  crosay
 * Complete - task2611676 - take also MWV into account for computation of trueWind
 * https://sourceforge.net/tracker2/?func=detail&aid=2611676&group_id=100698&atid=1045270
 * Incomplete - tasktask 2577644 - SpeedPolar implement polar in tables
 * https://sourceforge.net/tracker2/?func=detail&aid=2577644&group_id=100698&atid=1045270
 *
 * Revision 1.8  2009/01/24 20:52:36  crosay
 * Complete - taskRemoval of sqlite
 *
 * Complete - tasksuppression of debug display for preference saving
 *
 * Complete - tasktask 2531747 animation of the wearther
 * https://sourceforge.net/tracker2/?func=detail&aid=2024403&group_id=100698&atid=1045270
 *
 * Revision 1.7  2008/09/12 19:25:13  crosay
 * changed the date format for boat records to yyyyMMdd
 *
 * Revision 1.6  2008/08/14 13:36:39  crosay
 * added method parseDate (required by grib test cases)
 *
 * Revision 1.5  2008/05/28 16:28:55  crosay
 * added CVS markups in source code
 *
 * $Revision: 1.10 $
 * $State: Exp $
 */
package al.sybina.ebr.core.data.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import al.sybina.ebr.core.common.Messages;


/**
 * class used to give the actual date/time for database purpose.
 *
 * @author rosaycy
 */
public class Date {

	/**
	 * used to create a date/time record understandable by a SQL Database
	 *
	 * @return a String with the format: "dd/MM/yyyy HH:mm:ss" ex: "01/01/2007
	 *         10:53:25"
	 */
	public static String getActualGMTDateTimeAsString() {
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT")); //$NON-NLS-1$
		return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(c.getTime()); //$NON-NLS-1$
	}

	/**
	 * returns a Calendar instance set to now in GMT
	 */
	public static Calendar getActualGMTDateTime() {
		return Calendar.getInstance(TimeZone.getTimeZone("GMT")); //$NON-NLS-1$
	}
	/**
	 * used to create a date record understandable by a SQL Database
	 *
	 * @return a String with the format: "yyyy?MM?dd" where ? is the separator.
	 *         used by process needing for example to create a file name with
	 *         the date. ex: "2007/02/01" for the first feb 2007.
	 */
	public static String getActualGMTDateAsString(String sep) {
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT")); //$NON-NLS-1$
		String formatDate = String.format("yyyy%sMM%sdd", sep, sep); //$NON-NLS-1$
		return new SimpleDateFormat(formatDate).format(c.getTime());
	}

	/**
	 * used to create a date string understandable by a NMEA Message
	 *
	 * @return a String with the format: "ddMMyy" ex: "010108"
	 */
	public static String getActualGMTDateAsNMEAString() {
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT")); //$NON-NLS-1$
		return new SimpleDateFormat("ddMMyy").format(c.getTime()); //$NON-NLS-1$
	}

	/**
	 * used to create a time string understandable by a NMEA Message
	 *
	 * @return a String with the format: "%HHmmss" ex: "121059"
	 */
	public static String getActualGMTTimeAsNMEAString() {
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT")); //$NON-NLS-1$
		String res = String.format("%02d%02d%02d",  //$NON-NLS-1$
				c.get(Calendar.HOUR_OF_DAY),
				c.get(Calendar.MINUTE),
				c.get(Calendar.SECOND));
		return res;
	}

	/**
	 * Parse a NMEA GPS time (hour, min, sec) field into a Calendar.
	 *
	 * @param nmeaTime
	 *            a string to parse as a time (NMEA format : hhmmss)
	 * @return a Calendar with the expressed date
	 * @throws ParseException
	 */
	public static Calendar parseNMEATime(String nmeaTime) throws ParseException {
		java.util.Date d = new SimpleDateFormat("HHmmss").parse(nmeaTime); //$NON-NLS-1$
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d);
		// date is not set so put it to 0
		gc.set(0, 0, 0);
		return gc;
	}

	/**
	 * Parse a NMEA GPS date (day, month, year) field into a Calendar.
	 *
	 * @param nmeaDate
	 *            a string to parse as a date (NMEA format : ddMMyy)
	 * @return a Calendar with the expressed date
	 * @throws ParseException
	 */
	public static Calendar parseNMEADate(String nmeaDate) throws ParseException {
		java.util.Date d = new SimpleDateFormat("ddMMyy").parse(nmeaDate); //$NON-NLS-1$
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d);
		return gc;
	}

	/**
	 * Parse a NMEA GPS date and time (day, month, year, hour, min, sec) field
	 * into a Calendar.
	 *
	 * @param nmeaTime
	 *            a string to parse as a time (NMEA GPS format : ddmmyy and
	 *            hhmmss)
	 * @return a Calendar with the expressed date
	 * @throws ParseException
	 */
	public static Calendar parseNMEADateTime(String nmeaDateTime)
			throws ParseException {
		java.util.Date d = new SimpleDateFormat("ddMMyyHHmmss") //$NON-NLS-1$
		.parse(nmeaDateTime);
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d);
		return gc;
	}

	/**
	 * return a java.util.Date date from the format and string passed in parameters.
	 * the format must match the date time.
	 * ex:
	 * "yyyy/MM/dd HH:mm" "2008/08/10 00:00"
	 * @param format
	 * @param dateTime
	 * @return
	 * @throws ParseException
	 */
	public static Calendar parseDate(String format, String dateTime) throws ParseException {
		java.util.Date d = new SimpleDateFormat(format).parse(dateTime);
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d);
		return gc;
	}

	public static String toString(Calendar c) {
		if (c != null){
			return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(c.getTime()); //$NON-NLS-1$			
		}
		return "unknown";
	}

	public static String toISO8601(Calendar c) {
		return String.format("%tFT%tTZ", c, c); //$NON-NLS-1$
	}

	public static String toFileNameCompatible(Calendar c) {
		return String.format("yyyyMMddHHmmss", c, c); //$NON-NLS-1$
	}

	public static Calendar parseISO8601(String timeStr) {
		Calendar res = Calendar.getInstance();
		timeStr = timeStr.replace("T", " "); //$NON-NLS-1$ //$NON-NLS-2$
		timeStr = timeStr.replace("Z", ""); //$NON-NLS-1$ //$NON-NLS-2$
		java.util.Date date;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr); //$NON-NLS-1$
			res.setTime(date);
		} catch (ParseException e) {
		}
		return res;
	}

	/**
	 * @param time
	 * @return the elapsed time given in milliseconds as a String (month/day/hours/min/sec).
	 */
	public static String elapsedTime(long time) {
		double timeInHours = time / 3600000d;
		double days = timeInHours / 24;
		if (days >= 1) {
			timeInHours -= ((int) days) * 24;
		}
		int h = (int) timeInHours;
		int m = (int) ((timeInHours - h) * 60);
		if (days >= 1) {
			return String.format(Messages.Date_formatDuration1, (int) days, h, m);
		}
		return String.format("%02d:%02d", h, m); //$NON-NLS-1$
	}


	/**
	 * return the best readable time in string for a time given in hours.
	 * depending on the time, only the relevant information is returned (number of days, hours, min or sec)
	 * @param timeInHour
	 * @return 1d,2h or 1h30min or 35sec
	 */
	public static String getTimeInHoursAsHourOrMinOrSec(double timeInHour) {
		String res = "";
		if (timeInHour >= 24){
			int d = (int)(timeInHour/24);
			int h = (int) (timeInHour-(d*24));
			res = String.format("%dd,%dh", d, h);										

		}else if (timeInHour >= 1){
			int h = (int)timeInHour;
			int m = (int) ((timeInHour-h) * 60);
			res = String.format("%dh:%dmin", h, m);										
		}else if (timeInHour <= (1d/60d)){
			res = String.format("%dsec", (int)(timeInHour*3600));																				
		}else{
			res = String.format("%dmin", (int)(timeInHour * 60));																				
		}
		return res;
	}

	/**
	 * return the best readable time in string for a time given in milliseconds.
	 * depending on the time, only the relevant information is returned (number of days, hours, min or sec)
	 * @param timeInMs
	 * @return 1d,2h or 1h30min or 35sec
	 */
	public static String getTimeInMsAsHourOrMinOrSec(long timeInMs) {
		return getTimeInHoursAsHourOrMinOrSec((double)timeInMs / 3600000);
	}

	public static String etaAsString(Calendar eta) {
		if (eta != null){
			Calendar now = Calendar.getInstance();
			//if it is the same day, show only the time
			if (now.get(Calendar.DAY_OF_MONTH) == eta.get(Calendar.DAY_OF_MONTH)){
				if (now.get(Calendar.HOUR) == eta.get(Calendar.HOUR)){
					return new SimpleDateFormat("HH:mm:ss").format(eta.getTime()); //$NON-NLS-1$													
				}else{
					return new SimpleDateFormat("HH:mm").format(eta.getTime()); //$NON-NLS-1$								
				}
			}else if (now.get(Calendar.MONTH) == eta.get(Calendar.MONTH)){
				return new SimpleDateFormat("E HH:mm").format(eta.getTime()); //$NON-NLS-1$							
			}else{
				return new SimpleDateFormat("dd/MM HH").format(eta.getTime()); //$NON-NLS-1$							
			}

		}
		return "unknown";
	}

}
