/**
 * 
 */
package al.sybina.ebr.core.maths;

import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Path2D.Double;

import al.sybina.ebr.core.geo.Sector;

/**
 * @author cyrille
 *
 */
public class PathIntersection extends Double {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int ALL_OUT = 1 + 2 + 4  + 8;


	public static Path2D getIntersection(final Path2D path, final Sector sector){
		boolean intersect = false;
		//used to check if all corners are set (which means they intersect, even if no point is inside)
		int corners = 0;
		//store here the path we will finally return
		final Path2D.Double finalPath = new Path2D.Double(); 
		//instantiate a Path iterator that will check the intersection of the path with the visiblesector
		PathIterator pi = path.getPathIterator(null);
		while (!pi.isDone()){
			double[] coords = new double[6];
			int code = pi.currentSegment(coords);
			 double x = coords[0];
			 double y = coords[1];
			 Point2D.Double p = new Point2D.Double(x,y);
			 if (sector.contains(p)){	
				 intersect = true;
			 }else{
				 //the point is outside the viewsector
				 //get the outcode of the point w.r.t the view rectangle
				 int outcode = sector.outcode(p);
				 corners = corners | outcode;
				 //first check the corners
				 if (outcode == (Rectangle2D.OUT_TOP + Rectangle2D.OUT_LEFT)){
				 		p.x = sector.getMinLon();
				 		p.y = sector.getMaxLat();
				 }else if(outcode == (Rectangle2D.OUT_TOP + Rectangle2D.OUT_RIGHT)){
				 		p.x = sector.getMaxLon();
				 		p.y = sector.getMaxLat();
				 }else if(outcode == (Rectangle2D.OUT_BOTTOM + Rectangle2D.OUT_LEFT)){
				 		p.x = sector.getMinLon();
				 		p.y = sector.getMinLat();
				 }else if(outcode == (Rectangle2D.OUT_BOTTOM + Rectangle2D.OUT_RIGHT)){
				 		p.x = sector.getMaxLon();
				 		p.y = sector.getMinLat();
				 }else{
					 switch (outcode){
					 	case Rectangle2D.OUT_TOP:
					 		p.y = sector.getMaxLat();
							 break;
					 	case Rectangle2D.OUT_BOTTOM:
					 		p.y = sector.getMinLat();
							 break;
					 	case Rectangle2D.OUT_LEFT:
					 		p.x = sector.getMinLon();
							 break;
					 	case Rectangle2D.OUT_RIGHT:
					 		p.x = sector.getMaxLon();
							 break;
					 }								 
				 }
			 }
			 if (code == PathIterator.SEG_MOVETO){
				 finalPath.moveTo(p.x, p.y);
			 }else{
				 finalPath.lineTo(p.x, p.y);
			 }
			pi.next();
		}
		return (intersect || corners == ALL_OUT) ? finalPath : null;
	}
		
	static Point2D.Double getLineLineIntersection(Line2D l1, Line2D l2){
		
		if (!l1.intersectsLine(l2)) return null;

		double  x1 = l1.getX1(), y1 = l1.getY1(),
		x2 = l1.getX2(), y2 = l1.getY2(),
		x3 = l2.getX1(), y3 = l2.getY1(),
		x4 = l2.getX2(), y4 = l2.getY2();

		double x = det(det(x1, y1, x2, y2), x1 - x2,
				det(x3, y3, x4, y4), x3 - x4)/
				det(x1 - x2, y1 - y2, x3 - x4, y3 - y4);
		double y = det(det(x1, y1, x2, y2), y1 - y2,
				det(x3, y3, x4, y4), y3 - y4)/
				det(x1 - x2, y1 - y2, x3 - x4, y3 - y4);

		return new Point2D.Double(x, y);
	}

	static double det(double a, double b, double c, double d)
	{
		return a * d - b * c;
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
