/**
 * CVS markups
 * last update by : $Author: crosay $ 
 * $Date: 2008-05-28 16:28:54 $ 
 * $Header: /media/disk/capcodeCVSbackup/capcode.basics/src/capcode/data/Stabilisator.java,v 1.4 2008-05-28 16:28:54 crosay Exp $
 * $Log: not supported by cvs2svn $
 * $Revision: 1.4 $ 
 * $State: Exp $ 
 */
package al.sybina.ebr.core.data;

/**
 * 
 */

/**
 * class used to detect the stabilisation of a numerical meaning. A data is
 * considered stabilised if during a defined number of iteration, its meaning did
 * not vary outside a defined margin.
 * 
 * @author rosaycy
 */
public class Stabilisator {
	// hold is used to store the last meaning when the data is unstable
	private double hold;
	// margin is the maximum absolute delta between two consecutives values
	private double margin;
	// cont the iteration of consecutives stable values
	private int loop;
	// criteria for acceptable consecutive iterations to declare the data stable
	private int validLoop;
	// true is the data is stabilised
	private boolean isStabilized;

	/**
	 * Creates a Stabilisator that will check if a meaning is stable during a
	 * specified loop iterations.<br>
	 * stability is true if the meaning does not change in a given margin during x
	 * iterations
	 * 
	 * @param margin
	 * @param loop
	 */
	public Stabilisator(double margin, int loop) {
		super();
		this.margin = margin;
		this.validLoop = loop;
		hold = 0;
		loop = 0;
		isStabilized = false;
	}

	/**
	 * set the new meaning and check if stabilised.
	 * 
	 * @param d :
	 *            the meaning of the data to check stability
	 */
	public void set(double d) {
		if (Math.abs(hold - d) < margin) {
			loop++;
		} else {
			loop = 1;
			hold = d;
		}
		isStabilized = (loop > validLoop);
	}

	/**
	 * @return true if the data was stable
	 */
	public final boolean isStabilized() {
		return isStabilized;
	}

	/**
	 * @return the number of stable iterations
	 */
	public final int getLoop() {
		return this.loop;
	}

}
