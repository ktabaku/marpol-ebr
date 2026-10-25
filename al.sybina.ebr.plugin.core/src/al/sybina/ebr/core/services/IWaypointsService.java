package al.sybina.ebr.core.services;

import al.sybina.ebr.core.geo.waypoints.GpxWaypointsTable;
import al.sybina.ebr.core.gpx.GpxWaypointRecord;


public interface IWaypointsService extends IIbrCoreService{

	void open(String fileName);

	void save();

	void saveAs(String fileName);

	void deleteWaypoint(GpxWaypointRecord gpxRecord);

	GpxWaypointsTable getWaypoints();
	
	void saveSingleWp(String wpName,String wpType,String navType,String speed,String maxSpeed,String minSpeed,String runtime,String intPos,String xte,String bwc,String es,String position,String turnRadius,String wOver,String rateOf,String minDepthString,String tideString,String catzocString,String availableDepthString,String dinamicDraftString,String maxHeightString,
			String obstacleHeightString,String clearanceString,String safetyContourString,String attentionContourString);


}
