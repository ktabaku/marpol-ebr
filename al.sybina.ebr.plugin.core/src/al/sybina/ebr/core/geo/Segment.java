package al.sybina.ebr.core.geo;

import java.awt.geom.Line2D;

import javafx.scene.shape.Line;

public class Segment {
	Position p1;
	Position p2;
	
	public Segment(Position ap1, Position ap2){
		p1 = ap1;
		p2 = ap2;
	}

	public Line toLine2D() {
		return new Line(
				p1.getLon().toDegrees(), 
				p1.getLat().toDegrees(),
				p2.getLon().toDegrees(),
				p2.getLat().toDegrees()
				);
	}
	
	public Line2D toAWTLine2D() {
		return new Line2D.Double(
				p1.getLon().toDegrees(), 
				p1.getLat().toDegrees(),
				p2.getLon().toDegrees(),
				p2.getLat().toDegrees()
				);
	}
	
}
