/**
 * CVS markups
 * last update by : $Author: crosay $ 
 * $Date: 2008-05-28 16:28:54 $ 
 * $Header: /media/disk/capcodeCVSbackup/capcode.basics/src/capcode/nmea/I_NMEAMessageBuilder.java,v 1.3 2008-05-28 16:28:54 crosay Exp $
 * $Log: not supported by cvs2svn $
 * $Revision: 1.3 $ 
 * $State: Exp $ 
 */
package al.sybina.ebr.core.nmea;

public interface I_NMEAMessageBuilder {

	/**
	 * must be overriden and parent-called by children creates the initial
	 * message
	 * 
	 * @return
	 */
	public abstract String buildString();

}