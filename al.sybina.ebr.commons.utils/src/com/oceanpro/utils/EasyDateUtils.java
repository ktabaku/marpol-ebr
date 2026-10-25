package com.oceanpro.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.jfoenix.controls.JFXDatePicker;

import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;

/**
 * 
 * Class name: EasyDateUtils
 *
 * Description: 
 * NOTA : VEDI -----------------> http://web.mclink.it/MC4868/orologio/orautc.htm
 *
 * Company: CrismaItalia
 *
 * @author Crisma
 * @date 24/apr/2013
 *
 */
public class EasyDateUtils {

	public static String UTC_FORMAT = "dd/MM/yyyy hh:mm:ss a z";
	public static String UTC_FORMAT_DATE_ONLY = "dd/MM/yyyy";
	public static DateTimeFormatter logbookTimeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);
	public static DateTimeFormatter logbookSimpleDateTimeFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);
	public static DateTimeFormatter logbookDateTimeFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm", Locale.ENGLISH);
	public static DateFormat logbookDateFormatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm", Locale.ENGLISH);
	public static DateTimeFormatter logbookPdfDateTimeFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy 'at' HH:mm 'HRS'", Locale.ENGLISH);
	/**
	 * Data corrente in UTC
	 * @return
	 */
	public static Date getGMT0CurrentDate() {
		
		//System.out.println("getGMT0CurrentDate - new Date():" + (new Date()));
		DateTime utc = new DateTime(DateTimeZone.UTC);
		//System.out.println("getGMT0CurrentDate - utc:" + (utc.toDate()));
		return utc.toDate();
//		try {
//			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
//			cal.setTime(new Date());
//			return cal.getTime();
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
	}
	
	/**
	 * Crea data in UTC
	 * @param millis
	 * @return
	 */
	public static Date getGMT0Date(long millis) {
		
		try {
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			cal.setTimeInMillis(millis);
			return cal.getTime();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Formatta date and time in UTC
	 * @param date
	 * @return
	 */
	public static String getUTCFormattedDate (Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(EasyDateUtils.UTC_FORMAT);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		return sdf.format(date);
	}
	
	/**
	 * Formatta date in UTC
	 * @param date
	 * @return
	 */
	public static String getUTCFormattedDateOnly (Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(EasyDateUtils.UTC_FORMAT_DATE_ONLY);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		return sdf.format(date);
	}
	
	
	/**
	 * Confronta due date basandosi solo su giorno, mese e anno. ritorna true anche se le date sono entrambe null.
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean dateEquals(Date date1, Date date2) {
		if ((date1!=null && date2==null) || (date1==null && date2!=null)) return false;
		if (date1==null && date2==null) return true;
		
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		int day1 = cal1.get(Calendar.DAY_OF_MONTH);
		int mnt1 = cal1.get(Calendar.MONTH);
		int ye1 = cal1.get(Calendar.YEAR);
		
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		int day2 = cal2.get(Calendar.DAY_OF_MONTH);
		int mnt2 = cal2.get(Calendar.MONTH);
		int ye2 = cal2.get(Calendar.YEAR);
		
		//System.out.println("dateEquals: day1:" +day1 + "  mnt1:" +mnt1 + "  ye1:" + ye1 + "day2:" +day2 + "  mnt2:" +mnt2 + "  ye2:" + ye2);
		
		if (day1==day2 && mnt1==mnt2 && ye1==ye2) return true;
		
		return false;
	}
	
	
	/**
	 * Confronta due date basandosi solo su ora, giorno, mese e anno. ritorna true anche se le date sono entrambe null.
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean dateHourEquals(Date date1, Date date2) {
		if ((date1!=null && date2==null) || (date1==null && date2!=null)) return false;
		if (date1==null && date2==null) return true;
		
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		int hour1 = cal1.get(Calendar.HOUR_OF_DAY);
		int day1 = cal1.get(Calendar.DAY_OF_MONTH);
		int mnt1 = cal1.get(Calendar.MONTH);
		int ye1 = cal1.get(Calendar.YEAR);
		
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		int hour2 = cal2.get(Calendar.HOUR_OF_DAY);
		int day2 = cal2.get(Calendar.DAY_OF_MONTH);
		int mnt2 = cal2.get(Calendar.MONTH);
		int ye2 = cal2.get(Calendar.YEAR);
		
		//System.out.println("dateEquals: hour1:" +hour1 + " day1:" +day1 + "  mnt1:" +mnt1 + "  ye1:" + ye1 + "hour2:" +hour2 + "day2:" +day2 + "  mnt2:" +mnt2 + "  ye2:" + ye2);
		
		if (hour1==hour2 && day1==day2 && mnt1==mnt2 && ye1==ye2) return true;
		
		return false;
	}
	
	public static void setStringConverter(JFXDatePicker jfxDatePicker, DateTimeFormatter dateTimeFormatter) {
		jfxDatePicker.setConverter(new StringConverter<LocalDate>() {
			
			@Override
			public String toString(LocalDate localDate) {
				if (localDate == null)
					return "";
				return dateTimeFormatter.format(localDate);
			}
			
			@Override
			public LocalDate fromString(String dateString) {
				if (dateString == null || dateString.trim().isEmpty()) {
					return null;
				}
				return LocalDate.parse(dateString, dateTimeFormatter);
			}
		});
	}
	
	public static void setStringConverter(DatePicker datePicker, DateTimeFormatter dateTimeFormatter) {
		datePicker.setConverter(new StringConverter<LocalDate>() {

		    @Override
		    public String toString(LocalDate localDate)
		    {
		        if(localDate==null)
		            return "";
		        return dateTimeFormatter.format(localDate);
		    }

		    @Override
		    public LocalDate fromString(String dateString)
		    {
		        if(dateString==null || dateString.trim().isEmpty())
		        {
		            return null;
		        }
		        return LocalDate.parse(dateString,dateTimeFormatter);
		    }
		});
	}
}
