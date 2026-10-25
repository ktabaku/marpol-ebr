/**
 * Project: capcode.basics Line.java created 25 oct. 2009 by cyrille 
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
 * $Date: 2009-12-01 22:28:15 $ 
 * $Header: /media/disk/capcodeCVSbackup/capcode.basics/src/capcode/maths/Line.java,v 1.2 2009-12-01 22:28:15 crosay Exp $
 * $Log: not supported by cvs2svn $
 * Revision 1.1  2009/11/02 00:10:37  crosay
 * Incomplete # add AIS capability
 * http://sourceforge.net/apps/trac/capcode/ticket/42
 * Complete # required by ticket 6: install a change waypoint event mechanism
 * http://sourceforge.net/apps/trac/capcode/ticket/37
 * Complete # when importing a polar, ask also to save it under capcode format.
 * http://sourceforge.net/apps/trac/capcode/ticket/40
 * Incomplete # wordwind plugin for capcode
 * http://sourceforge.net/apps/trac/capcode/ticket/6
 *
 * $Revision: 1.2 $ 
 * $State: Exp $ 
 */


package al.sybina.ebr.core.maths;


import al.sybina.ebr.core.data.EuclideanVector;
import javafx.geometry.Point2D;


/**
 * @author cyrille
 *
 */
public class Line extends javafx.scene.shape.Line {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Line(Point2D p1, Point2D p2) {
		super(p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}

	/**
	 * creates a Line from a point and vector.
	 * @param p
	 * @param v : a Vector(Angle / North, Speed)
	 */
	public Line(Point2D p, EuclideanVector v) {
		//getU return info in nautical miles, so in minute of angle. Needs to be / 60 to go to degree
		super(p.getX(), p.getY(), p.getX() + v.getU() / 60, p.getY() + v.getV() / 60);
	}
	

	/**
	 * compute a, b and c of the cartesian equation ax + by + c = 0
	 * returns the coefficient a, b and c of the cartesian equation of the line.
	 */
	public double[] getCartesianCoefficients() {
		double a = this.getStartY()-this.getEndY();
		double b = this.getEndX()-this.getStartX();
		double c = -this.getEndX()*this.getStartY()+this.getStartX()*this.getEndY();
		return new double[] {a,b,c};
		
	}
	
	/**
	 * compute a, b of the reduced equation y = ax + b
	 * returns the coefficient a, b of the reduced equation of the line.
	 */
	public double[] getReducedCoefficients() {
		double[] coefs = getCartesianCoefficients();
		double a = coefs[1] != 0 ? coefs[0] / coefs[1]
		                                            :0;
		double b = coefs[1] != 0 ? coefs[2] / coefs[1]
		                                            :0;
		return new double[] {a,b};
	}
		
	/**
	 * returns the intersection of this line with l1. 
	 * @param l1
	 * @return null if lines does not intersect
	 * @see http://mathworld.wolfram.com/Line-LineIntersection.html
	 */
	public Point2D getIntersection(Line l1){
		double[] coeffs1 = l1.getReducedCoefficients();
		double[] coeffs2 = this.getReducedCoefficients();
		double a1 = coeffs1[0];
		double b1 = coeffs1[1];
		double a2 = coeffs2[0];
		double b2 = coeffs2[1];
		if (a1 == a2) {
			return null;
		}
		return new Point2D(
				(b2 - b1) / (a1 - a2), 
				(a2 * b1 - a1 * b2) / (a1 - a2));
	}
	/**
	 * return the distance of the point p to the line.
	 *@see http://mathworld.wolfram.com/Point-LineDistance2-Dimensional.html
	 * @param p
	 * @return the distance as a double (same unit as line)
	 */
	public double getPointDistanceToLine(Point2D p) {
		double[] coefs = getCartesianCoefficients();		
		double a = coefs[0];
		double b = coefs[1];
		double c = coefs[2];
		return Math.abs(a * p.getX() +b * p.getY() + c) / Math.sqrt(a * a + b * b);
	}
	
}
