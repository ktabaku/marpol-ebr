package al.sybina.ebr.core.charts;

import java.util.Vector;

import al.sybina.ebr.core.geo.Position;
import al.sybina.ebr.core.geo.Sector;
import al.sybina.ebr.core.geo.transform.CoordsTranslator;
import al.sybina.ebr.core.geo.transform.MapReference;
import javafx.scene.image.Image;

public interface IChartParser {

	/**
	 * load the map.<br>
	 * the image is not loaded. This is done with getImage(), getImage(sector) etc.
	 * @param fileName
	 * @throws Exception
	 */
	public abstract void load(String fileName) throws Exception;

	/**
	 * the translator is the object that transform for Lon/Lat to pixels
	 * and vice-versa.
	 * @return the translator.
	 * @see CoordsTranslator
	 */
	public abstract CoordsTranslator getTranslator();

	/**
	 * @return the name of the map (eg "CAPCODE BAY")
	 */
	public abstract String getName();


	/**
	 * returns the scale of the map.
	 * @return a int giving the scale of the map (number of cm on the screen for a cm on the chart).
	 */
	public abstract int getScale();

	/**
	 * return true if the map is ready to be displayed.
	 * @return
	 */
	public abstract boolean isChartReady();

	/**
	 * @return the zoomFactor
	 */
	public abstract double getZoomFactor();

	/**
	 * @return the position of the actual viewpoint.
	 */
	public abstract Position getViewPoint();

	/**
	 * @return a Rectangle defining the actual visible part of a chart (in pixels)
	 */
	public abstract java.awt.Rectangle getVisibleBounds();

	/**
	 * set the zoom factor on the map
	 * @param zf
	 */
	public abstract void setZoomFactor(double zf);

	/**
	 * The implementation shall return a path relative to the application (eg maps/BSB_ROOT/).
	 * @return the complete file name of the map.
	 */
	public abstract String getFileName();

	/**
	 * @return the sector as displayed (after a zoom or a viewpoint move for eg).
	 */
	public abstract Sector getVisibleSector();

	/**
	 * @return the map center as a Position (lon, lat)
	 */
	public abstract Position getMapCenter();

	/**
	 * get all the references used to georeference the chart.
	 * @return a Vector of MapReference
	 */
	public Vector<MapReference> getReferences();

	/**
	 * save the map file.
	 */
	public abstract void save();

	/**
	 * @return the visible sector as defined by the PLY informations.
	 */
	public Sector getMapUseableSector();

	/**
	 * @return the chart datum.
	 */
	public abstract String getDatum();

	/**
	 * set or modify the name of the chart.
	 * @param s
	 */
	public abstract void setName(String s);

	/**
	 * set or modify the scale of the chart.
	 * @param sc
	 */
	public abstract void setScale(int sc);

	/**
	 * set the Corrective longitude Phase.
	 * most of the case it is 0.
	 * but must be set to 180 when the chart overlaps the change of date line.
	 * @param d
	 */
	public abstract void setCph(double d);

	/**
	 * define the longitude shift to be used when the map is not Datum WGS84.
	 * it is usually defined on the map itself.
	 * @param d : in degrees of longitude.
	 */
	public abstract void setLongitudeShift(double d);

	/**
	 * define the latitude shift to be used when the map is not Datum WGS84.
	 * it is usually defined on the map itself.
	 * @param d : in degrees of latitude.
	 */
	public abstract void setLatitudeShift(double d);

	/**
	 * define the datum of the chart.
	 * eg: WGS84, EUROPEAN 1950, ...
	 * @param dt
	 */
	public abstract void setDatum(String dt);

	/**
	 * @return the boudaries of the map as define by the PLY information.
	 */
	public abstract Vector<Position> getBoudaries();

	/**
	 * define the file name of the map.
	 * @param fileName
	 */
	public abstract void setFileName(String fileName);

	/**
	 * define the image to be used with this chart.
	 * @param image
	 * @throws Exception 
	 */
	public abstract void setImage(Image image) throws Exception;

	/**
	 * @return the Corrective Longitude Phase. (180� when the chart covers the change of date line)
	 */
	public abstract double getCph();

	/**
	 * @return the longitude shift used to fit this chart with the WGS84 datum (in degrees).
	 */
	public abstract double getLonShift();

	/**
	 * @return the latitude shift used to fit this chart with the WGS84 datum (in degrees).
	 */
	public abstract double getLatShift();

	/**
	 * set the dot per inch information of the map.
	 * @param x
	 */
	public abstract void setDPI(int x);

	/**
	 * @return the dot per inch of the map.
	 */
	public abstract int getDPI();

	/**
	 * @return the unit of the sounding system (meter, fathom, feet...)
	 */
	public abstract String getSoundings();


	/**
	 * @return the the sounding reference (eg Medium Sea Level)
	 */
	public abstract String getSoundingReference();

	/**
	 * set the meaning of the sounding system for the chart.
	 * @param soundings
	 */
	public abstract void setSoundings(String soundings);

	/**
	 * set the meaning of the sounding system for the chart.
	 * @param soundingReference
	 */
	public abstract void setSoundingReference(String soundingReference);

	public abstract Image showSector(Sector viewSector);

	public abstract String getProjectionName();

	public abstract Object getProductionDate();

}