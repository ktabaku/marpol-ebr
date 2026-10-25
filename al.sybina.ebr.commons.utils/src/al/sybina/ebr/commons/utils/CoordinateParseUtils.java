package al.sybina.ebr.commons.utils;

import java.text.Format;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.geotools.measure.Angle;
import org.geotools.measure.AngleFormat;
import org.geotools.measure.Latitude;
import org.geotools.measure.Longitude;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Strings;

/**
 * Utilities for assisting in the parsing of latitude and longitude strings into Decimals.
 */
public class CoordinateParseUtils {
	private final static String DMS = "\\s*(\\d{1,3})\\s*(?:°|d|º| |g|o)"  // The degrees
              + "\\s*([0-6]?\\d)\\s*(?:'|m| |´|’|′)" // The minutes
              + "\\s*(?:"                            // Non-capturing group
              + "([0-6]?\\d(?:[,.]\\d+)?)"           // Seconds and optional decimal
              + "\\s*(?:\"|''|s|´´|″)?"
              + ")?\\s*";
	private final static String DM = "\\s*(\\d{1,3})\\s*(?:°|d|º| |g|o)" // The degrees
             + "\\s*(?:"                           // Non-capturing group
             + "([0-6]?\\d(?:[,.]\\d+)?)"          // Minutes and optional decimal
             + "\\s*(?:'|m| |´|’|′)?"
             + ")?\\s*";
	private final static String D = "\\s*(\\d{1,3}(?:[,.]\\d+)?)\\s*(?:°|d|º| |g|o|)\\s*"; // The degrees and optional decimal
	private final static Pattern DMS_SINGLE = Pattern.compile("^" + DMS + "$", Pattern.CASE_INSENSITIVE);
	private final static Pattern DM_SINGLE = Pattern.compile("^" + DM + "$", Pattern.CASE_INSENSITIVE);
	private final static Pattern D_SINGLE = Pattern.compile("^" + D + "$", Pattern.CASE_INSENSITIVE);
	private final static Pattern DMS_COORD = Pattern.compile("^" + DMS + "([NSEOW])" + "[ ,;/]?" + DMS + "([NSEOW])$",Pattern.CASE_INSENSITIVE);
	// private final static Pattern DM_COORD = Pattern.compile("^" + DM +
	// "([NSEOW])" + "[ ,;/]?" + DM + "([NSEOW])$", Pattern.CASE_INSENSITIVE);
	// private final static Pattern D_COORD = Pattern.compile("^" + D +
	// "([NSEOW])" + "[ ,;/]?" + D + "([NSEOW])$", Pattern.CASE_INSENSITIVE);
	private final static String POSITIVE = "NEO";
	private CoordinateParseUtils() {
		throw new UnsupportedOperationException("Can't initialize class");
	}


  /**
   * This parses string representations of latitude and longitude values. It tries its best to interpret the values and
   * indicates any problems in its result as {@link org.gbif.api.vocabulary.OccurrenceIssue}.
   * When the {@link ParseResult.STATUS} is FAIL the payload will be null and one or more issues should be set
   * in {@link org.gbif.common.parsers.core.OccurrenceParseResult#getIssues()}.
   *
   * Coordinate precision will be 6 decimals at most, any more precise values will be rounded.
   *
   * Supported standard formats are the following, with dots or optionally a comma as the decimal marker, and variations
   * on the units also accepted e.g. °, d, º, g, o.
   * <ul>
   *   <li>43.63871944444445</li>
   *   <li>N43°38'19.39"</li>
   *   <li>43°38'19.39"N</li>
   *   <li>43°38.3232'N</li>
   *   <li>43d 38m 19.39s N</li>
   *   <li>43 38 19.39</li>
   *   <li>433819N</li>
   * </ul>
   *
   * @param latitude  The decimal latitude
   * @param longitude The decimal longitude
   *
   * @return The parse result
   */
  
	public static LatLng parseLatLng(final String latitude, final String longitude) {
		if (Strings.isNullOrEmpty(latitude) || Strings.isNullOrEmpty(longitude)) {
			return null;
		}
		Double lat = Double.parseDouble(latitude);
		Double lng = Double.parseDouble(longitude);
		if (lat == null || lng == null) {
			// try degree minute seconds
			try {
				lat = parseDMS(latitude, true);
				lng = parseDMS(longitude, false);
			} catch (IllegalArgumentException e) {
				return null;
			}
		}

		return validateAndRound(lat, lng);
	}

	private static boolean inRange(double lat, double lon) {
		if (Double.compare(lat, 90) <= 0 && Double.compare(lat, -90) >= 0 && Double.compare(lon, 180) <= 0
				&& Double.compare(lon, -180) >= 0) {
			return true;
		}
		return false;
	}

	private static boolean isLat(String direction) {
		if ("NS".contains(direction.toUpperCase())) {
			return true;
		}
		return false;
	}

	private static int coordSign(String direction) {
		return POSITIVE.contains(direction.toUpperCase()) ? 1 : -1;
	}

  // 02° 49' 52" N	131° 47' 03" E
	public static LatLng parseVerbatimCoordinates(final String coordinates) {
		if (Strings.isNullOrEmpty(coordinates)) {
			return null;
		}
		Matcher m = DMS_COORD.matcher(coordinates);
		if (m.find()) {
			final String dir1 = m.group(4);
			final String dir2 = m.group(8);
			// first parse coords regardless whether they are lat or lon
			double c1 = coordFromMatcher(m, 1, 2, 3, dir1);
			double c2 = coordFromMatcher(m, 5, 6, 7, dir2);
			// now see what order the coords are in:
			if (isLat(dir1) && !isLat(dir2)) {
				return validateAndRound(c1, c2);

			} else if (!isLat(dir1) && isLat(dir2)) {
				return validateAndRound(c2, c1);

			} else {
				return null;
			}

		} else if (coordinates.length() > 4) {
			// try to split and then use lat/lon parsing
			for (final char delim : ",;/ ".toCharArray()) {
				int cnt = StringUtils.countMatches(coordinates, String.valueOf(delim));
				if (cnt == 1) {
					String[] latlon = StringUtils.split(coordinates, delim);
					if (latlon.length == 2) {
						return parseLatLng(latlon[0], latlon[1]);
					}
				}
			}
		}
		return null;
	}

  private static LatLng validateAndRound(double lat, double lon) {
    // if everything falls in range
    if (inRange(lat, lon)) {
      return new LatLng(lat, lon);
    }
    return null;

  }

  /**
   * Parses a single DMS coordinate
   * @param coord
   * @param lat
   * @return the converted decimal up to 6 decimals accuracy
   */
	@VisibleForTesting
	protected static double parseDMS(String coord, boolean lat) {
		final String DIRS = lat ? "NS" : "EOW";
		coord = coord.trim().toUpperCase();

		if (coord.length() > 3) {
			// preparse the direction and remove it from the string to avoid a
			// very complex regex
			char dir = 'n';
			if (DIRS.contains(String.valueOf(coord.charAt(0)))) {
				dir = coord.charAt(0);
				coord = coord.substring(1);
			} else if (DIRS.contains(String.valueOf(coord.charAt(coord.length() - 1)))) {
				dir = coord.charAt(coord.length() - 1);
				coord = coord.substring(0, coord.length() - 1);
			}
			// without the direction chuck it at the regex
			Matcher m = DMS_SINGLE.matcher(coord);
			if (m.find()) {
				return coordFromMatcher(m, 1, 2, 3, String.valueOf(dir));
			} else {
				m = DM_SINGLE.matcher(coord);
				if (m.find()) {
					return coordFromMatcher(m, 1, 2, String.valueOf(dir));
				} else {
					m = D_SINGLE.matcher(coord);
					if (m.find()) {
						return coordFromMatcher(m, 1, String.valueOf(dir));
					}
				}
			}
		}
		throw new IllegalArgumentException();
	}

	private static double coordFromMatcher(Matcher m, int idx1, int idx2, int idx3, String sign) {
		return roundTo6decimals(coordSign(sign) * dmsToDecimal(Double.parseDouble(m.group(idx1)),
				Double.parseDouble(m.group(idx2)), m.group(idx3)==null?0:Double.parseDouble(m.group(idx3))));
	}

	private static double coordFromMatcher(Matcher m, int idx1, int idx2, String sign) {
		return roundTo6decimals(coordSign(sign)
				* dmsToDecimal(Double.parseDouble(m.group(idx1)), Double.parseDouble(m.group(idx2)), 0.0));
	}

	private static double coordFromMatcher(Matcher m, int idx1, String sign) {
		return roundTo6decimals(coordSign(sign) * dmsToDecimal(Double.parseDouble(m.group(idx1)), 0.0, 0.0));
	}

	private static double dmsToDecimal(double degree, Double minutes, Double seconds) {
		minutes = minutes == null ? 0 : minutes;
		seconds = seconds == null ? 0 : seconds;
		return degree + (minutes / 60) + (seconds / 3600);
	}

	// round to 6 decimals (~1m precision) since no way we're getting anything
	// legitimately more precise
	private static Double roundTo6decimals(Double x) {
		return x == null ? null : Math.round(x * Math.pow(10, 6)) / Math.pow(10, 6);
	}
  
  
  
  
  
  
	public static Latitude parseLat(final String latitudeDMS) {
		if (Strings.isNullOrEmpty(latitudeDMS)) {
			return null;
		}
		Double lat = null;
		try {
			lat = Double.parseDouble(latitudeDMS);
		} catch (Exception ex) {
			try {
				lat = parseDMS(latitudeDMS, true);
			} catch (IllegalArgumentException e) {
				return null;
			}
		}
		return new Latitude(lat);
	}
	
	public static Longitude parseLon(final String longitude) {
		if (Strings.isNullOrEmpty(longitude)) {
			return null;
		}
		Double lon = null;
		try {
			lon = Double.parseDouble(longitude);
		} catch (Exception ex) {
			try {
				lon = parseDMS(longitude, false);
			} catch (IllegalArgumentException e) {
				return null;
			}
		}
		return new Longitude(lon);
	}
  
	
	
	private static Format format;  
	/**
	 Degrees and Decimal Minutes
	 DDD° MM.MMM'
	 32° 18.385' N 122° 36.875' W
	 This is the format most commonly used when working with electronic navigation equipment and hence also called NMEA (National Marine Electronics Association) format.
	**/ 
	public static String latitudeToString(final Latitude latitude) {
		StringBuffer buffer = new StringBuffer();
		synchronized (Angle.class) {
			final Format format = getAngleFormat();
			buffer = format.format(latitude, buffer, null);
		}
		return buffer.toString();
	}
	/**
	 Degrees and Decimal Minutes
	 DDD° MM.MMM'
	 32° 18.385' N 122° 36.875' W
	 This is the format most commonly used when working with electronic navigation equipment and hence also called NMEA (National Marine Electronics Association) format.
	**/ 	
	public static String longitudeToString(final Longitude longitude) {
		StringBuffer buffer = new StringBuffer();
		synchronized (Angle.class) {
			final Format format = getAngleFormat();
			buffer = format.format(longitude, buffer, null);
		}
		return buffer.toString();
	}
  
	private static Format getAngleFormat() {
        assert Thread.holdsLock(CoordinateParseUtils.class);
        if (format == null) {
            format = new AngleFormat("D°MM.mm'", Locale.US);
        }
        return format;
    }
  
  
  
  
  
  
  
  /**
	 Degrees and Decimal Minutes
	 DDD° MM.MMM'
	 32° 18.385' N 122° 36.875' W
	 This is the format most commonly used when working with electronic navigation equipment and hence also called NMEA (National Marine Electronics Association) format. 

	 Decimal Degrees
	 DDD.DDDDD°
	 32.30642° N 122.61458° W
	 or +32.30642, -122.61458
	 This is the format you'll find most computer based mapping systems displaying. The coordinates are stored internally in a floating point data type, and no additional work is required to print them as a floating point number.
**/
	public static double[] DecimalToNMEAConverter(double lat, double lng) {
		String dcmLatString = null;
		int latDegree = 0;
		double latMinutes = 0, dcmLat = 0, absLat = 0;

		String dcmLngString = null;
		int lngDegree = 0;
		double lngMinutes = 0, dcmLng = 0, absLng = 0;

		// Log.i("Decimal to NMEA", "Decimal To NMEA: " + lat + " Longitude: " +
		// lng);
		if (lat < 0) {
			absLat = lat * -1;
		} else {
			absLat = lat;
		}

		latDegree = (int) absLat;
		latMinutes = (absLat - latDegree) * 60;
		dcmLatString = String.valueOf(latDegree) + String.valueOf(latMinutes);
		dcmLat = Double.parseDouble(dcmLatString);
		dcmLat = Math.round(dcmLat * 10000.0) / 10000.0;

		if (lat < 0) {
			dcmLat *= -1;
		}
		// ************************************************//
		if (lng < 0) {
			absLng = lng * -1;
		} else {
			absLng = lng;
		}

		lngDegree = (int) absLng;
		lngMinutes = (absLng - lngDegree) * 60;
		dcmLngString = String.valueOf(lngDegree) + String.valueOf(lngMinutes);
		dcmLng = Double.parseDouble(dcmLngString);
		dcmLng = Math.round(dcmLng * 10000.0) / 10000.0;

		if (lng < 0) {
			dcmLng *= -1;
		}
		// Log.i("Decimal to NMEA", "NMEA Lat: " + dcmLat + " Lng: " + dcmLng);
		return (new double[] { dcmLat, dcmLng });
	}

/**
	 Degrees and Decimal Minutes
	 DDD° MM.MMM'
	 32° 18.385' N 122° 36.875' W
	 This is the format most commonly used when working with electronic navigation equipment and hence also called NMEA (National Marine Electronics Association) format. 

	 Decimal Degrees
	 DDD.DDDDD°
	 32.30642° N 122.61458° W
	 or +32.30642, -122.61458
	 This is the format you'll find most computer based mapping systems displaying. The coordinates are stored internally in a floating point data type, and no additional work is required to print them as a floating point number.
**/	
	public double[] NMEAToDecimalConverter(double lat, double lng) {

		String latDegreeString = null, latMinutesString = null, nmeaLatString = null;
		double latDegree = 0, latMinutes = 0, nmeaLat = 0, absLat = 0;

		String lngDegreeString = null, lngMinutesString = null, nmeaLngString = null;
		double lngDegree = 0, lngMinutes = 0, nmeaLng = 0, absLng = 0;

		// Log.i("NMEA to Decimal", "Recieved Latitude: " + lat + " Longitude: "
		// + lng);

		if (lat < 0) {
			absLat = lat * -1;
		} else {
			absLat = lat;
		}

		Double d = absLat;
		String[] latSplitter = d.toString().split("\\.");

		if (latSplitter[0].length() <= 4) {
			latDegreeString = latSplitter[0].substring(0, 2);
			latMinutesString = d.toString().substring(2, d.toString().length());
		} else if (latSplitter[0].length() > 4) {
			latDegreeString = latSplitter[0].substring(0, 3);
			latMinutesString = d.toString().substring(3, d.toString().length());
		}

		latDegree = Integer.parseInt(latDegreeString);
		latMinutes = (Double.parseDouble(latMinutesString) / 60);
		// Log.i("NMEA to Decimal", "Degree: " + latDegree + " Minutes: " +
		// latMinutes);
		nmeaLat = latDegree + latMinutes;
		nmeaLat = Math.round(nmeaLat * 10000.0) / 10000.0;

		if (lat < 0) {
			nmeaLat *= -1;
		}
		// Log.i("NMEA to Decimal", "Decimal Value: " + nmeaLat);

		// *******************************************************//
		if (lng < 0) {
			absLng = lng * -1;
		} else {
			absLng = lng;
		}

		Double dlng = absLng;
		String[] lngSplitter = dlng.toString().split("\\.");

		if (lngSplitter[0].length() <= 4) {
			lngDegreeString = lngSplitter[0].substring(0, 2);
			lngMinutesString = dlng.toString().substring(2, dlng.toString().length());
		} else if (latSplitter[0].length() > 4) {
			lngDegreeString = lngSplitter[0].substring(0, 3);
			lngMinutesString = dlng.toString().substring(3, dlng.toString().length());
		}

		lngDegree = Integer.parseInt(lngDegreeString);
		lngMinutes = (Double.parseDouble(lngMinutesString) / 60);
		// Log.i("NMEA to Decimal", "Degree: " + lngDegree + " Minutes: " +
		// lngMinutes);
		nmeaLng = lngDegree + lngMinutes;
		nmeaLng = Math.round(nmeaLng * 10000.0) / 10000.0;

		if (lng < 0) {
			nmeaLng *= -1;
		}
		// Log.i("NMEA to Decimal", "NMEA Longitude: " + nmeaLat + " Longitude:
		// " + nmeaLng);
		return (new double[] { nmeaLat, nmeaLng });
	}
 
	
	
	
	
	
	
	
	
	/**
	 * Simple container class for an interpreted coordinate.
	 */
	public static class LatLng {

		private final Double lat;
		private final Double lng;

		public LatLng(double lat, double lng) {
			this.lat = lat;
			this.lng = lng;
		}

		public LatLng() {
			lat = null;
			lng = null;
		}

		public Double getLat() {
			return lat;
		}

		public Double getLng() {
			return lng;
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this).add("lat", lat).add("lng", lng).toString();
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(lat, lng);
		}

		@Override
		public boolean equals(Object object) {
			if (this == object) {
				return true;
			}
			if (!(object instanceof LatLng)) {
				return false;
			}

			LatLng that = (LatLng) object;
			return Objects.equal(this.lat, that.lat) && Objects.equal(this.lng, that.lng);
		}
	}
}