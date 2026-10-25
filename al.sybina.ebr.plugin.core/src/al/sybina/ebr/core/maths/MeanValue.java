/**
 * CVS markups
 * last update by : $Author: crosay $ 
 * $Date: 2009-09-26 23:02:09 $ 
 * $Header: /media/disk/capcodeCVSbackup/capcode.basics/src/capcode/maths/MeanValue.java,v 1.10 2009-09-26 23:02:09 crosay Exp $
 * $Log: not supported by cvs2svn $
 * Revision 1.9  2009/03/04 10:15:55  crosay
 * Complete - taskdisplay the equipments connected to PC
 *
 * Complete - taskexternalise strings
 *
 * Incomplete - tasknew plugin: bearing-speed-and log
 *
 * Revision 1.8  2009/02/26 21:53:06  crosay
 * Complete - taskdisplay the equipments connected to PC
 *
 * Incomplete - tasktest case for plugins in general
 *
 * Incomplete - taskupgrade of simulator
 *
 * Revision 1.7  2009/02/07 17:20:40  crosay
 * Incomplete - taskcheck correct behaviour of new speed capture mechanism
 * Complete - tasktask 2577326 - helmsman display: auto scale
 * https://sourceforge.net/tracker2/?func=detail&aid=2577326&group_id=100698&atid=1045270
 *
 * Revision 1.6  2008/05/28 16:28:55  crosay
 * added CVS markups in source code
 *
 * $Revision: 1.10 $ 
 * $State: Exp $ 
 */
package al.sybina.ebr.core.maths;

import java.nio.DoubleBuffer;

/**
 *Class used to compute floating mean meaning
 *n is the size of the buffer
 *setvalue adds the last meaning to the buffer
 *getValue compute the mean meaning 
 */
/**
 * Class used to implement the computation of a floating mean meaning for double
 * datas. usage: instantiate a class MeanValue with the number of points used to
 * mean <br>
 * ex: m = new MeanValue(10); <br>
 * each time a new data is ready, add it to the mean meaning:<br>
 * m.setValue(y); <br>
 * m.getValue() will retreive the sum of all last 10 y added, divided by 10
 * 
 * @author rosaycy
 * 
 */
public class MeanValue {
	private DoubleBuffer buffer;
	static final int MAX_NUMBER_OF_CHAR = 100;

	/**
	 * constructor
	 * 
	 * @param n :
	 *            size of the memorisarion buffer
	 * 
	 */
	public MeanValue(int n) {
		buffer = DoubleBuffer.allocate(n);
	}

	/**
	 * Method getValue
	 * 
	 * @return the mean meaning
	 */
	public double getValue() {
		int n = buffer.limit();
		double res = 0;
		for (int i = 0; i < n; i++) {
			res = res + buffer.array()[i];
		}
		res = res / n;
		return res;
	}

	/**
	 * Method setValue <br>
	 * add a meaning at the end of the memory buffer <br>
	 * when buffer size is reached, position goes to 0 and loop <br>
	 * 
	 * @param v
	 * 
	 */
	public void setValue(double v) {
		if (buffer.position() >= buffer.limit()) {
			buffer.rewind();
		}
		buffer.put(v);
	}

	/**
	 * clear the buffer
	 */
	public void clear() {
		buffer.clear();
	}

	/**
	 * set the new size of the buffer<br>
	 * The new size must be inferior to the previous size and must be positive.
	 * If not, the new size is keep
	 * 
	 * @param n :
	 *            size of the buffer,
	 */
	public void setN(int n) {
		if ((0 <= n) & (n <= buffer.limit())) {
			buffer.limit(n);
		}
	}

	/**
	 * show the buffer as text (but no more than 100 data) + size and pointers
	 */
	@Override
	public String toString() {
		String res;
		res = buffer.toString();
		return res;
	}

	/**
	 * @return the size of the buffer used to compute the mean meaning.
	 */
	public int getN() {
		return buffer.limit();
	}

}
