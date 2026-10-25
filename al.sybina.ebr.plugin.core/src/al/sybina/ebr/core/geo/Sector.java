/**
 * Project: capcode.basics Sector.java created 21 Aug 2008 by rosaycy
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
/**
 * CVS markups
 * last update by : $Author: crosay $
 * $Date: 2009-07-19 09:29:57 $
 * $Header: /media/disk/capcodeCVSbackup/capcode.basics/src/capcode/maps/position/Sector.java,v 1.9 2009-07-19 09:29:57 crosay Exp $
 * $Log: not supported by cvs2svn $
 * Revision 1.8  2009/03/04 10:15:55  crosay
 * Complete - taskdisplay the equipments connected to PC
 *
 * Complete - taskexternalise strings
 *
 * Incomplete - tasknew plugin: bearing-speed-and log
 *
 * Revision 1.7  2009/01/29 10:18:20  crosay
 * Complete - tasktask 2531747 animation of the wearther
 * https://sourceforge.net/tracker2/?func=detail&aid=2024403&group_id=100698&atid=1045270
 *
 * Revision 1.6  2008/11/10 10:42:05  crosay
 * undocumented changes (code washup)
 *
 * Revision 1.5  2008/10/10 21:02:59  crosay
 * added intersection between 2 sectors
 *
 * Revision 1.4  2008/09/17 11:09:37  crosay
 * added functions:
 *  getIntersection, isAroundChangingDate, isInside
 *
 * Revision 1.3  2008/09/12 19:31:52  crosay
 * correction of a bug when longitude west
 *
 * Revision 1.2  2008/09/11 15:29:56  crosay
 * task 2042679 (BSB Maps format) continued
 * finalisation
 *
 * Revision 1.1  2008/08/23 19:32:46  crosay
 * task 2042679 (BSB Maps format) continued
 * added class Sector
 *
 * $Revision: 1.9 $
 * $State: Exp $
 */


package al.sybina.ebr.core.geo;

import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import al.sybina.ebr.core.data.util.Fields;

import java.awt.geom.Rectangle2D;



/**
 * a sector is a map sector comprise between 2 Position (north/west and south/east).
 * @author rosaycy
 *
 */
public class Sector {
	private static final String SECTOR_SEPARATOR = ";"; //$NON-NLS-1$
	public Position northWest= null;
	public Position southEast= null;
	
	public Sector clone(){
		return new Sector(northWest.clone(), southEast.clone());
	}

	public Sector (Position p1, Position p2){
		double maxLat = Math.max(p1.getLat().toDegrees(), p2.getLat().toDegrees());
		double minLat = Math.min(p1.getLat().toDegrees(), p2.getLat().toDegrees());
		this.northWest = new Position(maxLat, p1.getLon().toDegrees());
		this.southEast = new Position(minLat, p2.getLon().toDegrees());
	}

	public Sector(String s){
		Fields f = new Fields(s, SECTOR_SEPARATOR);
		northWest = new Position(f.atPos(0));
		southEast = new Position(f.atPos(1));
	}

	public Sector(double north, double west, double south, double east){
		//check if parameters are sent the correct way
		double n = north > south? north : south;
		double s = north > south? south : north;
		northWest = new Position(Math.min(90, n), Math.max(-180, west));
		southEast = new Position(Math.max(-90, s), Math.min(180, east));			
	}

	/**
	 * creates a Sector with the content of a Rectangle2D.
	 * rectangle2D must represent coordinates in degrees
	 * @param area
	 */
	public Sector(Rectangle2D area) {
		this(area.getMaxY(), area.getMinX(), area.getMinY(), area.getMaxX());
	}

	@Override
	public String toString(){
		return northWest+SECTOR_SEPARATOR+southEast;
	}

	/**
	 * return the center position of the Sector.
	 * @return
	 */
	public Position getCenter() {
		double dLon = northWest.getDeltaLonInDeg(southEast);
		double dLat = northWest.getDeltaLatInDeg(southEast);
		double midLat = northWest.getLat().toDegrees() + dLat / 2;
		double midLon = northWest.getLon().toDegrees() + dLon / 2;
		return new Position(new Latitude(midLat), new Longitude(midLon));
	}

	/**
	 * return true if the 2 sectors are the same
	 */
	public boolean equals(Sector with){
		boolean res = false;
		res = this.northWest.getLat().toDegrees() == with.northWest.getLat().toDegrees()
		&& this.northWest.getLon().toDegrees() == with.northWest.getLon().toDegrees()
		&& this.southEast.getLat().toDegrees() == with.southEast.getLat().toDegrees()
		&& this.southEast.getLon().toDegrees() == with.southEast.getLon().toDegrees();
		return res;
	}


	/**
	 * return the intersection of this Sector with another one
	 * @param with
	 * @return
	 */
	public Sector getIntersection(Sector with){
		Sector res = null;
		if (with != null){
			res = getIntersection(this, with);
		}
		return res;
	}

	public static Sector getIntersection(Sector sectorA, Sector sectorB){
		if (sectorA == null) {
			return sectorB;
		}
		if (sectorB == null) {
			return sectorA;
		}

		// define latitudes
		double minLat, maxLat, minLon, maxLon;
		double minLatA = sectorA.southEast.getLat().toDegrees();
		double maxLatA = sectorA.northWest.getLat().toDegrees();
		double minLatB = sectorB.southEast.getLat().toDegrees();
		double maxLatB = sectorB.northWest.getLat().toDegrees();
		//check latitudes
		minLat = minLatA > minLatB ? minLatA : minLatB;
		maxLat = maxLatA < maxLatB ? maxLatA : maxLatB;

		if (minLat > maxLat){
			//no intersection possible
			return null;
		}

		//define longitudes
		double minLonA = sectorA.northWest.getLon().toDegrees();
		double maxLonA = sectorA.southEast.getLon().toDegrees();
		double minLonB = sectorB.northWest.getLon().toDegrees();
		double maxLonB = sectorB.southEast.getLon().toDegrees();

		//case 1 sector A is around changing date
		boolean isXingA = sectorA.isAroundChangingDate();
		boolean isXingB = sectorB.isAroundChangingDate();
		// is A crossing
		if (isXingA){
			//is B crossing too
			if (isXingB){
				minLon = Math.max(minLonA, minLonB);
				maxLon = Math.min(maxLonA, maxLonB);
			}else{
				//B is not Xing, check if B is East
				if (maxLonB>0){
					minLon = Math.max(minLonA, minLonB);
					maxLon = maxLonB;
				}else{
					//B is West
					minLon = minLonB;
					maxLon = Math.min(maxLonA, maxLonB);
				}
			}
		}else{
			//A is NOT crossing
			if (isXingB){
				//B is crossing
				if (maxLonA>0){
					// A is East
					minLon = Math.max(minLonA, minLonB);
					maxLon = maxLonA;
				}else{
					//A is West
					minLon = minLonA;
					maxLon = Math.min(maxLonA, maxLonB);
				}
			}else{
				//A and B are not Xing date changing line
				minLon = minLonA > minLonB ? minLonA : minLonB;
				maxLon = maxLonA < maxLonB ? maxLonA : maxLonB;
				if (minLon > maxLon){
					//no intersection possible
					return null;
				}
			}
		}
		return new Sector(new Position(new Latitude(maxLat), new Longitude(minLon)),
				new Position(new Latitude(minLat), new Longitude(maxLon)));
	}

	private static boolean isAroundChangingDate(Rectangle2D r){
		boolean res = false;
		double l1 = r.getMinX();
		double l2 = r.getMaxX();
        if (Math.signum(l1) != Math.signum(l2)){
            double delta = Math.abs(l2 - l1);
            res = delta > 180 && delta < 360;
        }
		return res;

	}

	public static Rectangle2D getIntersection(Rectangle2D r1, Rectangle2D r2){
		if (r1 == null) {
			return r2;
		}
		if (r2 == null) {
			return r1;
		}

		// define latitudes
		double minLat, maxLat, minLon, maxLon;
		double minLatA = r1.getMinY();
		double maxLatA = r1.getMaxY();
		double minLatB = r2.getMinY();
		double maxLatB = r2.getMaxY();
		//check latitudes
		minLat = minLatA > minLatB ? minLatA : minLatB;
		maxLat = maxLatA < maxLatB ? maxLatA : maxLatB;

		if (minLat > maxLat){
			//no intersection possible
			return null;
		}

		//define longitudes
		double minLonA = r1.getMinX();
		double maxLonA = r1.getMaxX();
		double minLonB = r2.getMinX();
		double maxLonB = r2.getMaxX();

		//case 1 sector A is around changing date
		boolean isXingA = isAroundChangingDate(r1);
		boolean isXingB = isAroundChangingDate(r2);
		// is A crossing
		if (isXingA){
			//is B crossing too
			if (isXingB){
				minLon = Math.max(minLonA, minLonB);
				maxLon = Math.min(maxLonA, maxLonB);
			}else{
				//B is not Xing, check if B is East
				if (maxLonB>0){
					minLon = Math.max(minLonA, minLonB);
					maxLon = maxLonB;
				}else{
					//B is West
					minLon = minLonB;
					maxLon = Math.min(maxLonA, maxLonB);
				}
			}
		}else{
			//A is NOT crossing
			if (isXingB){
				//B is crossing
				if (maxLonA>0){
					// A is East
					minLon = Math.max(minLonA, minLonB);
					maxLon = maxLonA;
				}else{
					//A is West
					minLon = minLonA;
					maxLon = Math.min(maxLonA, maxLonB);
				}
			}else{
				//A and B are not Xing date changing line
				minLon = minLonA > minLonB ? minLonA : minLonB;
				maxLon = maxLonA < maxLonB ? maxLonA : maxLonB;
				if (minLon > maxLon){
					//no intersection possible
					return null;
				}
			}
		}
		return new Rectangle2D.Double(minLon, minLat, maxLon, maxLon);
	}

	public static boolean isPositionInsideSector(Sector sector, double lon, double lat){
		if (sector == null) {
			return false;
		}
		double minLatSector = sector.southEast.getLat().toDegrees();
		double maxLatSector = sector.northWest.getLat().toDegrees();
		if (lat < minLatSector || lat > maxLatSector){
			return false;
		}
		double minLonSector = sector.northWest.getLon().toDegrees();
		double maxLonSector = sector.southEast.getLon().toDegrees();
		//case 1 sector A is around changing date
		boolean isXingSector = sector.isAroundChangingDate();
		// is A crossing
		if (isXingSector){
			return (lon> minLonSector || lon < maxLonSector);
		}
		//A is NOT crossing
		return (lon > minLonSector && lon < maxLonSector);
	}

	public boolean isAroundChangingDate() {
		return northWest.isCrossingDayNightLine(southEast);
	}

	@Override
	public boolean equals(Object o){
		boolean res = false;
		if (o instanceof Sector){
			Sector s = (Sector)o;
			res = (this.northWest.getLat().toDegrees() == s.northWest.getLat().toDegrees()
					&& this.northWest.getLon().toDegrees() == s.northWest.getLon().toDegrees()
					&& this.southEast.getLat().toDegrees() == s.southEast.getLat().toDegrees()
					&& this.southEast.getLon().toDegrees() == s.southEast.getLon().toDegrees());
		}
		return res;
	}

	public boolean isInside(Sector container){
		Sector inter = getIntersection(container);
		return inter != null && inter.equals(this);
	}

	public boolean contains(Sector sector) {
		Sector inter = getIntersection(sector);
		return inter != null && inter.equals(sector);
	}

	public boolean contains(Rectangle2D areaBounds) {
		Rectangle2D inter = getIntersection(toRectangle2D(), areaBounds);
		return inter != null && inter.equals(areaBounds);
	}

	public boolean contains(Position point) {
		return isPositionInsideSector(this, point.getLon().value, point.getLat().value);
	}

	public boolean intersects(Rectangle2D areaBounds) {
		Rectangle2D inter = getIntersection(toRectangle2D(), areaBounds);
		return inter != null;
	}


	public boolean contains(Point2D point2d) {
		return isPositionInsideSector(this, point2d.getX(), point2d.getY());
	}

	public Rectangle2D toRectangle2D() {
		Path2D.Double s = new Path2D.Double();
		s.moveTo(this.northWest.getLon().toDegrees(),
				this.northWest.getLat().toDegrees());
		s.lineTo(this.southEast.getLon().toDegrees(),
				this.southEast.getLat().toDegrees());
		return s.getBounds2D();
	}
	public javafx.geometry.Rectangle2D toFxRectangle2D() {
		Path2D.Double s = new Path2D.Double();
		s.moveTo(this.northWest.getLon().toDegrees(),
				this.northWest.getLat().toDegrees());
		s.lineTo(this.southEast.getLon().toDegrees(),
				this.southEast.getLat().toDegrees());
		return new javafx.geometry.Rectangle2D(s.getBounds2D().getX(), s.getBounds2D().getY(), s.getBounds2D().getWidth(), s.getBounds2D().getHeight());
	}

	public void translate(Double deltaPos) {
		northWest.getLon().addDegrees(deltaPos.x);
		northWest.getLat().addDegrees(deltaPos.y);
		southEast.getLon().addDegrees(deltaPos.x);
		southEast.getLat().addDegrees(deltaPos.y);

	}

	public double getSize() {
		Rectangle2D rect = this.toRectangle2D();
		return rect.getWidth() * rect.getHeight();
	}

	public Rectangle2D toRectangle2D(AffineTransform at) {
		Path2D.Double s = new Path2D.Double();
		s.moveTo(this.northWest.getLon().toDegrees(),
				this.northWest.getLat().toDegrees());
		s.lineTo(this.southEast.getLon().toDegrees(),
				this.southEast.getLat().toDegrees());
		s.transform(at);
		return s.getBounds2D();
	}

	public Path2D toPath2D(AffineTransform at) {
		Path2D.Double s = new Path2D.Double();
		s.moveTo(this.northWest.getLon().toDegrees(),
				this.northWest.getLat().toDegrees());
		s.lineTo(this.southEast.getLon().toDegrees(),
				this.southEast.getLat().toDegrees());
		s.transform(at);
		return s;
	}

	public int outcode(Point2D p) {
		int res = 0;
		boolean xCDL = getMinLon() > getMaxLon();
		if (xCDL){
			if (p.getX() > getMinLon()){
				res += Rectangle2D.OUT_LEFT;
			}
			if (p.getX() < getMaxLon()){
				res += Rectangle2D.OUT_RIGHT;
			}
		}else{
			if (p.getX() < getMinLon()){
				res += Rectangle2D.OUT_LEFT;
			}
			if (p.getX() > getMaxLon()){
				res += Rectangle2D.OUT_RIGHT;
			}
		}
		if (p.getY() < getMinLat()){
			res += Rectangle2D.OUT_BOTTOM;
		}
		if (p.getY() > getMaxLat()){
			res += Rectangle2D.OUT_TOP;
		}
		return res;
	}

	public double getMinLon() {
		return northWest.getLon().toDegrees();
	}

	public double getMaxLon() {
		return southEast.getLon().toDegrees();
	}

	public double getMaxLat() {
		return northWest.getLat().toDegrees();
	}

	public double getMinLat() {
		return southEast.getLat().toDegrees();
	}


}
