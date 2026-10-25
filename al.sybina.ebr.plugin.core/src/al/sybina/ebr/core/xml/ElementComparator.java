/**
 * CVS markups
 * last update by : $Author: crosay $ 
 * $Date: 2009-03-04 10:15:55 $ 
 * $Header: /media/disk/capcodeCVSbackup/capcode.basics/src/capcode/xml/ElementComparator.java,v 1.2 2009-03-04 10:15:55 crosay Exp $
 * $Log: not supported by cvs2svn $
 * Revision 1.1  2009/02/07 17:20:40  crosay
 * Incomplete - taskcheck correct behaviour of new speed capture mechanism
 * Complete - tasktask 2577326 - helmsman display: auto scale
 * https://sourceforge.net/tracker2/?func=detail&aid=2577326&group_id=100698&atid=1045270
 *
 * Revision 1.2  2008/11/10 10:42:05  crosay
 * undocumented changes (code washup)
 *
 * Revision 1.1  2008/10/10 21:09:02  crosay
 * modifications needed for the speedpolar xml file
 *
 * $Revision: 1.2 $ 
 * $State: Exp $ 
 */
package al.sybina.ebr.core.xml;

import java.util.Comparator;

import org.w3c.dom.Element;

/**
 * compare two Elements in a DOM tree by some attribute.
 * the creator must be called with the attribute name.
 * eg: 
 * Comparators.sort(list, new ElementComparator("trueWindAngle");
 * @author rosaycy
 *
 */
public class ElementComparator implements Comparator<Element>{
	/** the name of the attribute to be used during the comparison */
	private String attributeName = ""; //$NON-NLS-1$
	private Class<?> as;
	private boolean asc = true;
	/** 
	 * create a comparator this the attribute name given in parameter 
	*/
	public ElementComparator(String attr, Class<?> c){
		super();
		attributeName = attr;
		as = c;
	}
	
	/** 
	 * create a comparator this the attribute name given in parameter 
	*/
	public ElementComparator(String attr, Class<?> c, boolean asc){
		this(attr, c);
		this.asc = asc;
	}
	/**
	 * compare the two elements by the attribute given in the creator.
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Element e1, Element e2) {
		int res = 0;
		if (as.isAssignableFrom(int.class)){
		    res = Integer.parseInt(e1.getAttribute(attributeName)) - 
		    Integer.parseInt(e2.getAttribute(attributeName));
		}
		else if (as.isAssignableFrom(double.class)){
		    double d = Double.parseDouble(e1.getAttribute(attributeName)) - 
		    Double.parseDouble(e2.getAttribute(attributeName));
		    res = (int)Math.signum(d);
		}
		else {
		    res = e1.getAttribute(attributeName).compareTo(e2.getAttribute(attributeName));
		}
		return asc ? res : -res;
  }
}
