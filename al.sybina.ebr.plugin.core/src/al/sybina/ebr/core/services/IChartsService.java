package al.sybina.ebr.core.services;



import org.eclipse.core.runtime.IProgressMonitor;
import org.osgeo.proj4j.proj.Projection;

import al.sybina.ebr.core.charts.IChartParser;
import al.sybina.ebr.core.events.IChartPaintListener;
import al.sybina.ebr.core.geo.Position;
import al.sybina.ebr.core.geo.Sector;
import al.sybina.ebr.core.geo.Segment;
import al.sybina.ebr.core.geo.transform.CoordsTranslator;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.MouseEvent;

public interface IChartsService extends IIbrCoreService{

	boolean isServiceReady();

	void setProjection(String value);

	IChartParser[] getChartParsers();

	Projection getProjection();

	CoordsTranslator getCoordsTranslator();

	void zoomIn();

	void zoomOut();

	void setProjectionScale(double scale);

	void terminate();

	Sector getVisibleSector();

	Position getLastKnownShipPosition();

	void gotoPosition(String position);

	void addMouseListener(int priority, EventHandler<MouseEvent> listener);

	void addMouseMotionListener(int priority, EventHandler<MouseEvent> listener);

	/**
	 * returns the time between two computations of boat position.
	 * This is used also by the polar display function to know the size of the polar to display.
	 * @return a long : the time in min
	 */
	double getTimeToNextPosition();


	/**
	 * get the name of a chart at the position. 
	 * The selected chart will be the one with the smallest scale amongs the possible ones.
	 * @param pos a Position 
	 * @return the full name (path + name) of a chart or null if no map found
	 */
	String getChartNameAtPosition(Position pos);

	boolean isAccessible(Position pos);

	boolean isAccessible(Sector sector);

	boolean isAccessible(Segment segment);

	void setFilterSector(Sector filterSector);

	/**
	 * returns the unit (depth) used by the top visible chart at a given position.
	 * @param position
	 */
	String getTopChartUnitAtPosition(Position position);

	/**
	 * create a chart parser based on the chart name.
	 * @param chartPath
	 * @param monitor
	 * @return
	 * @throws Exception
	 */
	IChartParser createChartParser(String chartPath, IProgressMonitor monitor) throws Exception;

	
	

	void repaintCharts();
	void repaintMapCanvas();
	Node addPaintPanel(int i, IChartPaintListener chartPaintListener);
	void removePaintPanel(Node chartPaintComponent);
	void repaintComponent(Node paintComponent);
//	/**
//	 * return if exists, the popup menu of the main charts display
//	 */
//	ContextMenu getPopupMenu();
//
//	/**
//	 * return the last pointed x,y position 
//	 * @return a Point or null
//	 */
//	Point2D getMousePointer();

	Node getChartsNode();

	Node getChartsLayer(IChartPaintListener chartPaintListener);

	public double getProjectionScale();
	
	public CoordsTranslator getTranslator();
	
}
