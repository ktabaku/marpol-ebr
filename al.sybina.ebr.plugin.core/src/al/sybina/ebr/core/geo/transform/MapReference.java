package al.sybina.ebr.core.geo.transform;


import al.sybina.ebr.core.geo.Position;
import javafx.geometry.Point2D;


/**
 * reference point of a map
 * position : is a longitude/latitude
 * point : is the associated X/Y on the map
 * @author rosaycy
 *
 */
public class MapReference{
    public Position position;
    public Point2D point;
    public MapReference(Position pos, Point2D p){
    	position = pos;
    	point = p;
    }
    public String toString() {
    	return String.format("%s : [%d, %d]", position, point.getX(), point.getY()); //$NON-NLS-1$
    }
}