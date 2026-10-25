/**
 * project : capcode.basics
 * file name : CoordsTranslator.java
 * created : 5 janv. 2006 by : cyrille
 */
/**
 * CVS markups
 * last update by : $Author: crosay $ 
 * $Date: 2009-08-31 22:18:08 $ 
 * $Header: /media/disk/capcodeCVSbackup/capcode.basics/src/capcode/maps/transform/CoordsTranslator.java,v 1.15 2009-08-31 22:18:08 crosay Exp $
 * $Log: not supported by cvs2svn $
 * Revision 1.14  2009/08/15 15:13:28  crosay
 * Complete # add reference points to maps
 * http://sourceforge.net/apps/trac/capcode/ticket/22
 *
 * Revision 1.13  2009/07/19 09:27:40  crosay
 * Incomplete task federate all maps directories
 *
 * Revision 1.12  2009/07/01 22:10:12  crosay
 * Complete - taskSourceForge.net: CapCode-software suite for sailors: Detail: 2798790 - solve bug 2798789 error with some BSB 2 maps
 * https://sourceforge.net/tracker/?func=detail&aid=2798790&group_id=100698&atid=1045270
 *
 * Revision 1.11  2008/09/17 11:12:15  crosay
 * added get scale (pixels per inc)
 *
 * Revision 1.10  2008/09/11 15:29:56  crosay
 * task 2042679 (BSB Maps format) continued
 * finalisation
 *
 * Revision 1.9  2008/08/14 13:39:23  crosay
 * task 2042679 (BSB Maps format) continued
 * new Coords transformers
 *
 * Revision 1.8  2008/07/14 17:18:08  crosay
 * added the computation of the scale for any map
 *
 * Revision 1.7  2008/06/20 20:03:06  crosay
 * update of computation of translators (using degrees instead of minutes)
 *
 * Revision 1.6  2008/05/28 16:28:55  crosay
 * added CVS markups in source code
 *
 * $Revision: 1.15 $ 
 * $State: Exp $ 
 */

package al.sybina.ebr.core.geo.transform;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;

import al.sybina.ebr.core.geo.Position;
import al.sybina.ebr.core.geo.Sector;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;


/**
 * interface used to define CoordsTranslator. Classes that extend this interface
 * must define 2 transformation functions:<br>
 * <b>Position getPositionFromPoint(Point p)</b><br>
 * returns the geographic position of a point on the map given by x, y in pixels
 * <b>Point getPointFromPosition(Position pos)</b><br>
 * returns the location on the map (x, y in pixels) of a geographic position
 * 
 * @author crosay
 * 
 */
public abstract class CoordsTranslator {
	protected double zoomFactor = 1;
	protected int offsetX = 0;
	protected int offsetY = 0;
	/** corrective longitude phase (case of maps crossing the day line */
	protected double cph = 0.0;
	
	/** latitude shift in seconds for some maps in other reference than WGS84*/
	protected double latShift = 0;
	/** longitude shift in seconds for some maps in other reference than WGS84*/
	protected double lonShift = 0;
	
	/**
	 * returns the geographic position of a point on the map given by x, y in
	 * pixels
	 * deprecated: must call getAbsolutePositionFromPoint
	 */
	@Deprecated
	public Position getPositionFromPoint(Point2D p){
		return getPositionFromLocalPoint(p);
	}

	/**
	 * returns the location on the map (x, y in pixels) of a geographic position.
	 * deprecated: must call getAbsolutePointFromPosition
	 */
	@Deprecated
	public Point2D getPointFromPosition(Position pos){
		return getLocalPointFromPosition(pos);
	}

	/** set the zoom factor applied on the map*/
	public void setZoomFactor(double zf){
		zoomFactor = zf;
	}

	/**
	 * @return the zoomFactor
	 */
	public double getZoomFactor() {
		return zoomFactor;
	}

	/**
	 * compute the scale of the map.
	 * take into account the zoom, when used.
	 * @return
	 */
	public int getScale() {
		int dpi = (Toolkit.getDefaultToolkit().getScreenResolution());
		return getScale(dpi);
	}

	public int getScale(int dpi) {
		int dpcm = (int) (dpi / 2.54);//digit per cm
		double minutes = Math.abs(getPositionFromLocalPoint(new Point2D (0, 0)).getLat().toMinutes()
				- getPositionFromLocalPoint(new Point2D (0, dpcm)).getLat().toMinutes());
		double scale = 100 * minutes * 1852; 
		return (int) (scale);		
	}
			
	/**
	 * @return the offsetX
	 */
	public int getOffsetX() {
		return offsetX;
	}

	/**
	 * @param offsetX the offsetX to set
	 */
	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}

	/**
	 * @return the offsetY
	 */
	public int getOffsetY() {
		return offsetY;
	}

	/**
	 * @param offsetY the offsetY to set
	 */
	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}
	
	public Point2D getLocalPointFromPosition(Position pos){
		Point res = new Point(0, 0);
		if (pos != null){
			//get a point relative to the absolute map
			pos = toLocale(pos);
			Point2D res2D = getAbsolutePointFromPosition(pos);
			res = new Point((int)res2D.getX(), (int)res2D.getY());
			//make it relative to the view 
			res.x = res.x - offsetX;
			res.y = res.y - offsetY;
			res.x = (int) (res.x * zoomFactor);
			res.y = (int) (res.y * zoomFactor);
		}
		return new Point2D(res.getX(), res.getY());
	}

	public Point2D getLocalPointFromPosition(double x, double y) {
		Point res = new Point(0, 0);
		//get a point relative to the absolute map
		double lat = y - latShift;
		double lon = x - lonShift;
		Point2D res2D = getAbsolutePointFromPosition(lon, lat	);
		res = new Point((int)res2D.getX(), (int)res2D.getY());
		//make it relative to the view 
		res.x = res.x - offsetX;
		res.y = res.y - offsetY;
		res.x = (int) (res.x * zoomFactor);
		res.y = (int) (res.y * zoomFactor);
		return new Point2D(res.getX(), res.getY());
	}

	/**
	 * the chart transform is done in the local referential (eg EUR 50).
	 * normally, well defined charts have a longitude and latitude shift defined in the field DTM.
	 * returns the position in a WG84 referential.
	 * @param p
	 * @return
	 */
	public Position getPositionFromLocalPoint(Point2D p2d) {
		//add the offset.
		Point p = new Point((int)p2d.getX(), (int)p2d.getY());
		p.x = (int) (p.x / zoomFactor) + offsetX;
		p.y = (int) (p.y / zoomFactor) + offsetY;
		//return the Position computed from point relative to the whole map
		//the result is in chart referential
		Position res = getAbsolutePositionFromPoint(new Point2D(p.getX(), p.getY()));
		res = toWG84(res);
		return res;
	}
	/*
	 * return the position as in WG84 format 
	 */
	private Position toWG84(Position locale) {
		Position res = locale.clone();
		res.getLat().addSeconds(latShift); 
		res.getLon().addSeconds(lonShift);
		return res;
	}

	/*
	 * return a WG84 position in the chart locale system.
	 */
	private Position toLocale(Position pos) {
		Position res = pos.clone();
		res.getLat().addSeconds(-latShift);
		res.getLon().addSeconds(-lonShift);
		return res;
	}


	/**
	 * @return the latShift
	 */
	public double getLatShift() {
		return this.latShift;
	}

	/**
	 * @param latShift the latShift to set
	 */
	public void setLatShift(double latShift) {
		this.latShift = latShift;
	}

	/**
	 * @return the lonShift
	 */
	public double getLonShift() {
		return this.lonShift;
	}

	/**
	 * @param lonShift the lonShift to set
	 */
	public void setLonShift(double lonShift) {
		this.lonShift = lonShift;
	}

	/**
	 * returns a Rectangle from a Sector.
	 * @param s
	 * @return
	 */
	public Rectangle2D getAbsoluteRectangleFromSector(Sector s) {
		Point2D p12D = this.getAbsolutePointFromPosition(s.northWest);
		Point p1 = new Point((int)p12D.getX(), (int)p12D.getY());
		Point2D p22D = this.getAbsolutePointFromPosition(s.southEast);
		Point p2 = new Point((int)p22D.getX(), (int)p22D.getY());
		Rectangle r = new Rectangle(p1);
		r.width = p2.x - p1.x;
		r.height = p2.y - p1.y;
		return new Rectangle2D(r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight());
	}

	public Rectangle2D getLocalRectangleFromSector(Sector s) {
		if (s != null){
			Point2D p12D = this.getLocalPointFromPosition(s.northWest);
			Point p1 = new Point((int)p12D.getX(), (int)p12D.getY());
			Point2D p22D = this.getLocalPointFromPosition(s.southEast);
			Point p2 = new Point((int)p22D.getX(), (int)p22D.getY());
			Rectangle r = new Rectangle(p1);
			r.width = p2.x - p1.x;
			r.height = p2.y - p1.y;
			return new Rectangle2D(r.getMinX(), r.getMinY(), Math.abs(r.getWidth()), Math.abs(r.getHeight()));			
		}
		return null;
	}


	public String toString() {
		return String.format("zoom:%f; offsetX:%d; offsetY:%d;", zoomFactor, offsetX, offsetY); //$NON-NLS-1$
	}
	

	/**
	 * returns the x,y pixels as displayed from the pixels on the map.
	 * @param x
	 * @param y
	 * @return
	 */
	public Point2D getDisplayPointFromMapPixel(int x, int y) {
		Point res = new Point();
		res.x = x - offsetX;
		res.y = y - offsetY;
		res.x = (int) (res.x * zoomFactor);
		res.y = (int) (res.y * zoomFactor);
		return new Point2D(res.getX(), res.getY());
	}

	/**
	 * returns the x,y pixels on the map from the actual position.
	 * @param x
	 * @param y
	 * @return
	 */
	public Point2D getMapPixelFromMousePoint(Point2D mousePosition) {
		Point mPosition = new Point((int)mousePosition.getX(),(int)mousePosition.getY());
		Point res = new Point();
		res.x = (int) (mPosition.x / zoomFactor) + offsetX;
		res.y = (int) (mPosition.y / zoomFactor) + offsetY;
		return new Point2D(res.getX(), res.getY());
	}

	/**
	 * @return the cph
	 */
	public double getCph() {
		return this.cph;
	}

	/**
	 * @param cph the cph to set
	 */
	public void setCph(double cph) {
		this.cph = cph;
	}

	public Sector getSector(Rectangle2D cb) {
		return new Sector(this.getPositionFromLocalPoint(cb.getMinX(), cb.getMinY()), 
				this.getPositionFromLocalPoint(cb.getMaxX(), cb.getMaxY()));
	}

	private Position getPositionFromLocalPoint(double x, double y) {
		return getPositionFromLocalPoint(new Point2D(x, y));
	}

	

	public abstract Point2D getAbsolutePointFromPosition(Position pos);
	public abstract Point2D getAbsolutePointFromPosition(double lon, double lat);
	public abstract Position getAbsolutePositionFromPoint(Point2D centerXY);

}
