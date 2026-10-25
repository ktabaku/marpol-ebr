/**
 * 
 */
/**
 * CVS markups
 * last update by : $Author: crosay $ 
 * $Date: 2009-11-02 00:10:37 $ 
 * $Header: /media/disk/capcodeCVSbackup/capcode.basics/src/capcode/maths/LinearInterpolation.java,v 1.5 2009-11-02 00:10:37 crosay Exp $
 * $Log: not supported by cvs2svn $
 * Revision 1.4  2009/01/21 22:28:34  crosay
 * added the case where the 2 points of the interpolation are the same
 *
 * Revision 1.3  2008/05/28 16:28:55  crosay
 * added CVS markups in source code
 *
 * $Revision: 1.5 $ 
 * $State: Exp $ 
 */
package al.sybina.ebr.core.maths;

/**
 * used to implement linear interpolation calculus <br>
 * instantiate this class with x1, x2, y1 and y2 then ask for result y = a.x+b
 * 
 * @author rosaycy
 * 
 */
public class LinearInterpolation {
	// internal variables
	private double a = 1;
	private double b = 0;

	/**
	 * create and make the initial calculus for the line [x1, y1] - [x2, y2]
	 * computre a and b
	 * 
	 * @param x1
	 * @param x2
	 * @param y1
	 * @param y2
	 */
	public LinearInterpolation(double x1, double x2, double y1, double y2) {
		if (x2 - x1 != 0) {
			a = (y2 - y1) / (x2 - x1);
			b = ((x1 * y2) - (x2 * y1)) / (x1 - x2);
		}
		else{
			a = 0;
			b = y1;
		}
	}

	/**
	 * @param x :
	 *            the point between x1 and x2
	 * @return the y = a.x + b (point between y1 and y2)
	 */
	public double getResult(double x) {
		return a * x + b;
	}

	/**
	 * @return the a
	 */
	public double getA() {
		return this.a;
	}

	/**
	 * @return the b
	 */
	public double getB() {
		return this.b;
	}

}
