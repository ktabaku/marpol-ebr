package al.sybina.ebr.core.gpx;

import java.util.Calendar;

import al.sybina.ebr.core.data.Speed;
import al.sybina.ebr.core.data.util.Date;
import al.sybina.ebr.core.geo.Position;
import al.sybina.ebr.core.gpx.TracksFileParser.TrackSegment;

public class TrackRecord {
	/** the date of the record */
	public Calendar date;
	/** the position of the boat */
	public Position boatPos;
	/** the boat speed */
	public Speed boatSpeed;
	/** the expected target speed */
	public Speed targetSpeed;

	/**
	 * create an empty record. date is set to local date/time<br>
	 * @param segment TODO
	 */
	public TrackRecord() {
		super();
		date = Date.getActualGMTDateTime();
		boatPos = new Position("/"); //$NON-NLS-1$
		boatSpeed = new Speed(0);
		targetSpeed = new Speed(0);
	}

	/**
	 * createa a new Record.
	 *
	 * @param d
	 *            the date of the event as a Calendar (gmt)
	 * @param pos
	 *            Position of the boat
	 * @param speed :
	 *            Speed of the boat
	 * @param segment TODO
	 * @param target:
	 *            target Speed of the boat
	 */
	public TrackRecord(Calendar d, Position pos, Speed speed, Speed target) {
		super();
		date = d;
		boatPos = pos;
		boatSpeed = speed;
		targetSpeed = target;
	}	
}