/**
 * 
 */
package al.sybina.ebr.core.geo.waypoints;

import java.util.Hashtable;
import java.util.Vector;

import al.sybina.ebr.core.gpx.GpxWaypointRecord;

/**
 * @author crosay
 *
 */
public class GpxWaypointsTable extends Hashtable<String, GpxWaypointRecord>{
	private static final long serialVersionUID = 1L;
	private String name = "waypoints";

	public Vector<GpxWaypointRecord> getWaypointList() {
		Vector<GpxWaypointRecord>  res = new Vector<GpxWaypointRecord> ();
		for (java.util.Map.Entry<String, GpxWaypointRecord> entry : entrySet()){
			res.add(entry.getValue());
		}
		return res;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
