/**
 * 
 */
/**
 * CVS markups
 * last update by : $Author: crosay $ 
 * $Date: 2008-05-28 16:28:54 $ 
 * $Header: /media/disk/capcodeCVSbackup/capcode.basics/src/capcode/data/BoatSettings.java,v 1.4 2008-05-28 16:28:54 crosay Exp $
 * $Log: not supported by cvs2svn $
 * $Revision: 1.4 $ 
 * $State: Exp $ 
 */
package al.sybina.ebr.core.data;

/**
 * This class implements the actual configuration of the boatSettings
 * 
 * @author rosaycy
 */
public class BoatSettings {
	/**
	 * Code for the sail. This as to be defined by the used. An example, taken
	 * for a sloop could be:<br>
	 * code 0: GV , SPI<br>
	 * code 1: GV , GEN<br>
	 * code 2: GV , GEN 2/3<br>
	 * code 3: GV 2/3, GEN 2/3 (or INTER)<br>
	 * code 4: GV 2/3, GEN 1/3 (or FOC 1)<br>
	 * code 5: GV 1/3, GEN 2/3 (or INTER)<br>
	 * code 6: GV 1/3, GEN 1/3 (or FOC 1)<br>
	 */
	private int sailCode;
	/**
	 * other relevant information<br>
	 * this can be a combination of fields, user defined that gives the settings
	 * of the boatSettings eg: "cunningham=3;
	 */
	private String other;

	/**
	 * create a record with the following parameters
	 * 
	 * @param sailCode :
	 *            An example, taken for a sloop could be:<br>
	 *            code 0: GV , SPI<br>
	 *            code 1: GV , GEN<br>
	 *            code 2: GV , GEN 2/3<br>
	 *            code 3: GV 2/3, GEN 2/3 (or INTER)<br>
	 *            code 4: GV 2/3, GEN 1/3 (or FOC 1)<br>
	 *            code 5: GV 1/3, GEN 2/3 (or INTER)<br>
	 *            code 6: GV 1/3, GEN 1/3 (or FOC 1)<br>
	 * @param other :
	 *            other relevant information<br>
	 *            this can be a combination of fields, user defined that gives
	 *            the settings of the boatSettings eg: "cunningham=3;
	 * 
	 */
	public BoatSettings(int sailCode, String other) {
		super();
		this.sailCode = sailCode;
		this.other = other;
	}

	/**
	 * @return the other
	 */
	public String getOther() {
		return other;
	}

	/**
	 * @return the sailCode
	 */
	public int getSailCode() {
		return sailCode;
	}

	/**
	 * @param other
	 *            the other to set
	 */
	public void setOther(String other) {
		this.other = other;
	}

	/**
	 * @param sailCode
	 *            the sailCode to set
	 */
	public void setSailCode(int sailCode) {
		this.sailCode = sailCode;
	}

}
