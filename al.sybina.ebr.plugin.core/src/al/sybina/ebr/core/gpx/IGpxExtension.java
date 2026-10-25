package al.sybina.ebr.core.gpx;

import org.w3c.dom.Element;

import al.sybina.ebr.core.xml.XMLParser;

public interface IGpxExtension {

	/**
	 * write the data in the <extentions/> markup. 
	 * the code should start with:
	 *<pre>
	 * Element extElement = parser.createChildInElement(parent, "extensions");
	 * extElement.addAttribute("key", "value");
	 * ...
	 *</pre>
	 * @param parser : the xmlParser to write in.
	 * @param parent : the parent to attache the extension to.
	 */
	void writeToElement(XMLParser parser, Element parent);
	
	/**
	 * read the data in the <extentions/> markup of parent. 
	 * @param parser : the xmlParser to write in.
	 * @param parent : the parent to attache the extension to.
	 */
	void readFromElement(XMLParser parser, Element parent);

}
