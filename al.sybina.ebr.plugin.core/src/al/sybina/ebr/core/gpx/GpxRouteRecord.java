/**
 * Project: capcode.basics WaypointRecord.java created 18 Jun 2008 by rosaycy
* Copyright (c) 2007 Capcode Inc. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * -Redistribution of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * -Redistribution in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * - Neither the name of Capcode, Inc. or the names of authors may be used to
 * endorse or promote products derived from this software without specific prior
 * written permission.
 *
 * - This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. CAPCODE AND ITS
 * LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A
 * RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 * IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT
 * OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR
 * PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY,
 * ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF CAPCODE HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * - You acknowledge that this software is not designed, licensed or intended for
 * use in the design, construction, operation or maintenance of any nuclear
 * facility or airborne software.
 * - You acknowledge that using this software for the purpose of navigation does not
 * engage the developer responsability in case of damage, injuries or casualities due
 * to the use of this software.
  */

package al.sybina.ebr.core.gpx;

import java.net.URL;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Vector;

import al.sybina.ebr.core.geo.Position;



/**
 * class used to store route data.<br>
 * a detail of the shema can be found at: http://www.topografix.com/gpx/1/1/#type_rteType
  * @author rosaycy
 */
public class GpxRouteRecord{
	private String name = "";
	private String comments = "";
	private String desc = "";
	private String source = "";
	private URL link = null;
	private int num = 0;
	private String type = "";
	private Vector<GpxWaypointRecord> points;
	private GpxWaypointRecord selected;
	private Calendar timeOfDeparture;
	boolean departed;
	
	private String departure_draft;
	private String arrival_draft;
	private String departure_Airdraft;
	private String arrival_Airdraft;
	
	private String departure_zone;
	private String arrival_zone;
	private String eta;
	private String depDate;
	
	private String fuelRequired;
	private String fuelRob;
	private String HSFO;
	private String HSFO1;
	private String LSFO;
	private String LSFO1;
	private String DO;
	private String DO1;
	

	public String getFuelRequired() {
		return fuelRequired;
	}

	public void setFuelRequired(String fuelRequired) {
		this.fuelRequired = fuelRequired;
	}

	public String getFuelRob() {
		return fuelRob;
	}

	public void setFuelRob(String fuelRob) {
		this.fuelRob = fuelRob;
	}

	public String getHSFO() {
		return HSFO;
	}

	public void setHSFO(String hSFO) {
		HSFO = hSFO;
	}

	public String getHSFO1() {
		return HSFO1;
	}

	public void setHSFO1(String hSFO1) {
		HSFO1 = hSFO1;
	}

	public String getLSFO() {
		return LSFO;
	}

	public void setLSFO(String lSFO) {
		LSFO = lSFO;
	}

	public String getLSFO1() {
		return LSFO1;
	}

	public void setLSFO1(String lSFO1) {
		LSFO1 = lSFO1;
	}

	public String getDO() {
		return DO;
	}

	public void setDO(String dO) {
		DO = dO;
	}

	public String getDO1() {
		return DO1;
	}

	public void setDO1(String dO1) {
		DO1 = dO1;
	}

	public String getArrival_draft() {
		return arrival_draft;
	}

	public void setArrival_draft(String arrival_draft) {
		this.arrival_draft = arrival_draft;
	}

	public String getDeparture_Airdraft() {
		return departure_Airdraft;
	}

	public void setDeparture_Airdraft(String departure_Airdraft) {
		this.departure_Airdraft = departure_Airdraft;
	}

	public String getArrival_Airdraft() {
		return arrival_Airdraft;
	}

	public void setArrival_Airdraft(String arrival_Airdraft) {
		this.arrival_Airdraft = arrival_Airdraft;
	}

	public String getDeparture_draft() {
		return departure_draft;
	}

	public void setDeparture_draft(String departure_draft) {
		this.departure_draft = departure_draft;
	}

	public GpxRouteRecord() {
		super();
		points = new Vector<GpxWaypointRecord>();
	}

	public void setName(String text) {
		name = text;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return String.format("%d %s: %s, %s [%s]", num, name, desc, comments, source);
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}


		/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}


		/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}


	/**
	 * @return the num
	 */
	public int getNum() {
		return num;
	}


	/**
	 * @param num the num to set
	 */
	public void setNum(int num) {
		this.num = num;
	}

	/**
	 * @return a link to some published information on the route
	 */
	public URL getLink() {
		return link;
	}

	/**
	 * set the link to some published information about the route.
	 * (optional)
	 * @param link
	 */
	public void setLink(URL link) {
		this.link = link;
	}

		/**
	 * @return the waypointList
	 */
	public Vector<GpxWaypointRecord> getWaypointList() {
		return points;
	}


		/**
	 * @param waypointList the waypointList to set
	 */
	public void setWaypointList(Vector<GpxWaypointRecord> points) {
		this.points = points;
	}
	public GpxWaypointRecord getActiveWaypoint() {
		if (selected == null){
			if (points.size()>0){
				selected = points.firstElement();
			}
		}
		return selected;
	}

	public void selectNextActiveWaypoint() {
		int index = points.indexOf(selected)+1;
		if (index >= points.size()){
			selected = points.firstElement();
		}else{
			selected = points.elementAt(index);
		}
	}
	/**
	 * invert the list of waypoints. 
	 */
	public void invertWaypoints() {
		int size = points.size();
		Vector<GpxWaypointRecord> invertedList = new Vector<GpxWaypointRecord>(size);
		for (int i = 1; i <= size; i++){			
			invertedList.add(points.elementAt(size-i));
		}
		points = invertedList;
	}

	public void selectPreviousActiveWaypoint() {
		int index = points.indexOf(selected)-1;
		if (index < 0){
			selected = points.lastElement();
		}else{
			selected = points.elementAt(index);
		}
	}


	/**
	 * clear the list of waypoint.
	 * do not change the filename.
	 */
	public void clear() {
		selected = null;
		points.clear();
	}

	public void setActiveWaypoint(GpxWaypointRecord waypoint) {
		selected = waypoint;
	}

	/**
	 * insert a point at the given position, just after the selected (active) waypoint.
	 * if the file is empty or if the selected waypoint is the last of the file, the a waypoint
	 * is simply added at the end of the file.
	 * @param pos : the geo position 
	 * @return the created waypoint
	 */
	public GpxWaypointRecord createNewWaypointAfterSelection(Position pos) {
		//create a waypoint
		GpxWaypointRecord res = new GpxWaypointRecord(getNextName(), pos);
		insertAfterWaypoint(res, getActiveWaypoint());
		return res;
	}

	/**
	 * insert the wpt1 before wpt2.
	 * if wpt2 is null or the route is empty, wpt1 is added to the route, at the end.
	 * @param wpt1
	 * @param wpt2
	 */
	public void insertBeforeWaypoint(GpxWaypointRecord wpt1, GpxWaypointRecord wpt2) {
		//if the file is not empty
		if (points.size()>0){
			//get the index of the active waypoint
			int index = points.indexOf(wpt2);
			//if it is the last one then add a waypoint
			if (index == points.size()){
				points.add(wpt1);				
			}else{
				//insert a waypoint before the selected one
				points.insertElementAt(wpt1, index);							
			}
		}else{
			//first point, just add at the end of the file
			points.add(wpt1);
		}
	}

	/**
	 * insert the wpt1 after wpt2.
	 * if wpt2 is null or the route is empty, wpt1 is added to the route, at the end.
	 * @param wpt1
	 * @param wpt2
	 */
	public void insertAfterWaypoint(GpxWaypointRecord wpt1, GpxWaypointRecord wpt2) {
		//if the file is not empty and the wpt2 exists
		if (points.size()>0 && wpt2 != null){
			//get the index of the active waypoint
			int index = points.indexOf(wpt2);
			//if it is the last one then add a waypoint
			if (index == points.size()){
				points.add(wpt1);				
			}else{
				//insert a waypoint after the selected one
				points.insertElementAt(wpt1, index+1);							
			}
		}else{
			//first point, just add at the end of the file
			points.add(wpt1);
		}
	}

	/**
	 * @return the next name to be created.
	 * The logic is "point_" + the number of existing record +1
	 */
	private String getNextName() {
		return "point_" + (points.size()+1);
	}

	/**
	 * remove the selected waypoint from the route.
	 * @param waypoint
	 */
	public void removeWaypoint(GpxWaypointRecord waypoint) {
		points.remove(waypoint);
	}

	/**
	 * returns the waypoint at the given index in the route.
	 * @param index
	 * @return the selected waypoint 
	 * or the first element if index < 0 
	 * or the last element if index > size
	 */
	public GpxWaypointRecord getWaypointAt(int index) {
		try{
			return points.elementAt(index);			
		}catch(ArrayIndexOutOfBoundsException e){
			if (index >= points.size()){
				return points.lastElement();							
			}
			return points.firstElement();
		}
	}

	/**
	 * @return the selected (active) waypoint.
	 */
	public int getActiveWaypointIndex() {
		return points.indexOf(getActiveWaypoint());
	}

	/**
	 * move a waypoint from its original position to the position immediately before
	 * the target waypoint.
	 * @param gpx : the waypoint to move
	 * @param targetWpt : the place where to insert the waypoint before.
	 */
	public void moveWaypointBefore(GpxWaypointRecord gpx, GpxWaypointRecord targetWpt) {
		//remove the waypoint from the route
		removeWaypoint(gpx);
		//add the waypoint before the chosen target waypoint
		this.insertBeforeWaypoint(gpx, targetWpt);
	}

	public void addWaypoint(GpxWaypointRecord wpt) {
		this.insertAfterWaypoint(wpt, null);
	}

	/**
	 * define the time at wich the route is expected to start.
	 * @param time
	 */
	public void setTimeOfDeparture(Calendar time) {
		this.timeOfDeparture = time;		
	}

	/**
	 * define the time at wich the route is expected to start.
	 * @param time
	 */
	public Calendar getTimeOfDeparture() {
		return timeOfDeparture;
	}

	public void setDeparted(boolean b) {
		departed = b;
	}

	public boolean isDeparted() {
		return departed;
	}

	public String getDeparture_zone() {
		return departure_zone;
	}

	public void setDeparture_zone(String departure_zone) {
		this.departure_zone = departure_zone;
	}

	public String getArrival_zone() {
		return arrival_zone;
	}

	public void setArrival_zone(String arrival_zone) {
		this.arrival_zone = arrival_zone;
	}

	public String getEta() {
		return eta;
	}

	public void setEta(String eta) {
		this.eta = eta;
	}

	public String getDepDate() {
		return depDate;
	}

	public void setDepDate(String depDate) {
		this.depDate = depDate;
	}



}
