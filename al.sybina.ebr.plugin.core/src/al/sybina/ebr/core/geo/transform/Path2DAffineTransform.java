/**
 *
 */
package al.sybina.ebr.core.geo.transform;

import java.awt.Point;
import java.awt.geom.AffineTransform;

import javafx.geometry.Point2D;

/**
 * @author cyrille
 *
 */
@SuppressWarnings("serial")
public class Path2DAffineTransform extends AffineTransform {
	public CoordsTranslator translator;

	public Path2DAffineTransform(CoordsTranslator ct) {
		super();
		this.translator = ct;
	}

	@Override
	 public void transform(float[] srcPts, int srcOff, float[] dstPts, int dstOff, int numPts){
		 for (int i=0; i<2*numPts; i+=2){
			 float lon = srcPts[srcOff+i];
			 float lat = srcPts[srcOff+i+1];
			 Point2D p = translator.getLocalPointFromPosition(lon, lat);
			 dstPts[dstOff+i] = (float)p.getX();
			 dstPts[dstOff+i+1] = (float)p.getY();
		 }
	 }

	@Override
	 public void transform(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts){
		 for (int i=0; i<2*numPts; i+=2){
			 double lon = srcPts[srcOff+i];
			 double lat = srcPts[srcOff+i+1];
			 Point2D p = translator.getLocalPointFromPosition(lon, lat);
			 dstPts[dstOff+i] = (float)p.getX();
			 dstPts[dstOff+i+1] = (float)p.getY();
		 }
	 }

	@Override
	 public java.awt.geom.Point2D transform(java.awt.geom.Point2D srcPts, java.awt.geom.Point2D dstPts){
		if (dstPts == null) {
		    if (srcPts instanceof java.awt.geom.Point2D.Double) {
		    	dstPts = new java.awt.geom.Point2D.Double();
		    } else {
		    	dstPts = new java.awt.geom.Point2D.Float();
		    }
		}
		Point2D p = translator.getLocalPointFromPosition(srcPts.getX(), srcPts.getY());
		dstPts.setLocation(p.getX(), p.getY());
		return dstPts;
	 }

}