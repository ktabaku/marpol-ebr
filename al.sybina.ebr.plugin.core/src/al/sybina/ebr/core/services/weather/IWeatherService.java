package al.sybina.ebr.core.services.weather;

import java.util.Calendar;
import java.util.Date;

import al.sybina.ebr.core.data.EuclideanVector;
import al.sybina.ebr.core.data.Wind;
import al.sybina.ebr.core.geo.Position;

public interface IWeatherService {
	/**
	 * returns the current as a EuclideanVector (direction, force).<br>
	 * data is valid only if something was found.
	 * @param pos : the required position 
	 * @param time : the requested time
	 * @return
	 */
	public EuclideanVector getCurrentAtLocationAndTime(Position pos, Date time);
	/**
	 * returns the wind at the specified position and time.
	 * data is valid only if something was found.
	 * @param pos
	 * @param time
	 * @return
	 */
	public Wind getWindAtLocationAndTime(Position pos, Date time);
	/**
	 * set the weather to the time in parameter
	 * @param time
	 */
	public void selectTime(Calendar time);
}
