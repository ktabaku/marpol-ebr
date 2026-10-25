/**
 * 
 */
package al.sybina.ebr.core.data;

import al.sybina.ebr.core.geo.Position;

/**
 * @author cyrille
 *
 */
public class Pressure extends Data{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Position position;
	/**
	 * create a new Pressure data with meaning in millibar and position.
	 * @param meaning
	 * @param pos
	 */
	public Pressure (double value, Position pos){
		super(value, "mb", true); //$NON-NLS-1$
		position = pos;
	}
	/**
	 * @return the position
	 */
	public Position getPosition() {
		return this.position;
	}
	/**
	 * @param position the position to set
	 */
	public void setPosition(Position position) {
		this.position = position;
	}
}
