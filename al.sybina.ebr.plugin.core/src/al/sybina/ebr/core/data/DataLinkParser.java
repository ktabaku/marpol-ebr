/**
 * Project: capcode.basics DataLinkParser.java created 23 mars 2009 by cyrille 
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
 * $Date: 2009-12-06 00:01:59 $ 
 * $Header: /media/disk/capcodeCVSbackup/capcode.basics/src/capcode/data/DataLinkParser.java,v 1.4 2009-12-06 00:01:59 crosay Exp $
 * $Log: not supported by cvs2svn $
 * Revision 1.3  2009/10/14 21:50:17  crosay
 * Complete # when importing a polar, ask also to save it under capcode format.
 * http://sourceforge.net/apps/trac/capcode/ticket/40
 *
 * Revision 1.2  2009/08/31 22:18:08  crosay
 * Incomplete # preparation for delivery to version 0.9.3
 * http://sourceforge.net/apps/trac/capcode/ticket/17
 *
 * Revision 1.1  2009/03/31 07:42:19  crosay
 * Incomplete - taskSourceForge.net: CapCode-software suite for sailors: Detail: 2705654 - NMEA data mapping (FR 2605966 Input Mapping)
 * https://sourceforge.net/tracker2/index.php?func=detail&aid=2705654&group_id=100698&atid=1045270
 *
 * $Revision: 1.4 $ 
 * $State: Exp $ 
 */


package al.sybina.ebr.core.data;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import al.sybina.ebr.core.data.DataLink.DataKind;
import al.sybina.ebr.core.xml.XMLParser;


/**
 * this class parser xml file for datalink (data<->NMEA field) 
 *
 */
public class DataLinkParser extends XMLParser{
	/**
	 * default constructor.
	 * @throws ParserConfigurationException 
	 */
	public DataLinkParser() throws ParserConfigurationException {
		super();
		createDocument("map"); //$NON-NLS-1$
	}
	/**
	 * default constructor.
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public DataLinkParser(String fileName) throws ParserConfigurationException, SAXException, IOException {
		this();
		load(fileName);
	}
	
	/**
	 * 
	 * @return
	 * @throws XPathExpressionException
	 */
	public 	java.util.Vector<DataLink> getLinks() throws XPathExpressionException{
		java.util.Vector<DataLink> res = new java.util.Vector<DataLink>(10);
		for (Element e : getElementsList("//dataMap")) { //$NON-NLS-1$
			String name = e.getAttribute("name"); //$NON-NLS-1$
			String origin = e.getAttribute("origin"); //$NON-NLS-1$
			if (origin.equalsIgnoreCase("nmea") //$NON-NLS-1$
					|| origin.equalsIgnoreCase(DataKind.nmeaLink.toString())) { //$NON-NLS-1$
				Element message = this.getElementKey(e, "message"); //$NON-NLS-1$
				if (message != null) {
					String nmeaCode = message.getAttribute("code"); //$NON-NLS-1$
					Element validity = getElementKey(message, "validity"); //$NON-NLS-1$
					String validityFields = ""; //$NON-NLS-1$
					String validityValues = ""; //$NON-NLS-1$
					if (validity != null) {
						validityFields = validity.getAttribute("field"); //$NON-NLS-1$
						validityValues = validity.getAttribute("values"); //$NON-NLS-1$
					}
					res.add(
							new DataLink(
									name, 
									nmeaCode, 
									message.getAttribute("field"),  //$NON-NLS-1$
									validityFields,
									validityValues));
				}
			}else {
				Element computationParam = this.getElementKey(e, "computation"); //$NON-NLS-1$
				String param = (computationParam != null)? computationParam.getAttribute("information") : ""; //$NON-NLS-1$ //$NON-NLS-2$
				res.add(
						new DataLink(
								name, 
								param
								));				
			}
		}
		return res;
	}
	
	/**
	 * return true if the field is computed (and not read in nmea message).
	 * @param dataName
	 * @return
	 */
	public boolean isByComputation(String dataName) {
		Element e = getElement(String.format("//dataMap[@name='%s']", dataName)); //$NON-NLS-1$
		return e != null && e.getAttribute("origin").equalsIgnoreCase("internal"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * return the meaning of the header summary attribute.
	 * @return
	 */
	public String getSummary() {
		String summary = ""; //$NON-NLS-1$
		Element element = getElement("//header"); //$NON-NLS-1$
		if (element != null) {
			summary = element.getAttribute("summary"); //$NON-NLS-1$
		}
		return summary;
	}
	/**
	 * set the header summary with the meaning in text.
	 * @param text
	 */
	public void setSummary(String text) {
		Element element = getElement("//header"); //$NON-NLS-1$
		if (element == null) {
			element = this.createElement("/map/header"); //$NON-NLS-1$
		}
		element.setAttribute("summary", text); //$NON-NLS-1$
	}
	
	/**
	 * set the dataLink in the element tree.
	 * If an element is found with the same name, then it is modified
	 * otherwise it is created.
	 * @param dl : DataLink
	 * @throws XPathExpressionException 
	 */
	public void setDataLink(DataLink dl) throws XPathExpressionException {
		Element dataMapElement = null;
		for (Element e : getElementsList("//dataMap")) { //$NON-NLS-1$
			String name = e.getAttribute("name"); //$NON-NLS-1$
			if (name.equalsIgnoreCase(dl.dataName)) {
				dataMapElement = e;
				break;
			}
		}
		if (dataMapElement == null) {
			dataMapElement = this.createElement("/map/dataMap"); //$NON-NLS-1$
			dataMapElement.setAttribute("name", dl.dataName); //$NON-NLS-1$
		}
		dataMapElement.setAttribute("origin", dl.kind.name()); //$NON-NLS-1$
		//erase the previous values
		Element message = this.getElementKey(dataMapElement, "message"); //$NON-NLS-1$
		if (message != null) {
			dataMapElement.removeChild(message);
		}

		if (dl.kind == DataKind.nmeaLink) {
			message = this.getXmlDocument().createElement("message"); //$NON-NLS-1$
			dataMapElement.appendChild(message);
			message.setAttribute("code", dl.nmeaCode); //$NON-NLS-1$
			message.setAttribute("field", dl.getFields());			 //$NON-NLS-1$
			Element validity = this.getXmlDocument().createElement("validity"); //$NON-NLS-1$
			message.appendChild(validity);
			validity.setAttribute("field", dl.getValidityFields()); //$NON-NLS-1$
			validity.setAttribute("values", dl.getValidityValues()); //$NON-NLS-1$
		}
		else {
			message = this.getXmlDocument().createElement("computation"); //$NON-NLS-1$
			dataMapElement.appendChild(message);
			message.setAttribute("information", dl.computationInfo); //$NON-NLS-1$
		}
	}
	/**
	 * erase the content of the xmlDocument by creating a new root.
	 * @throws ParserConfigurationException
	 */
	public void eraseDocument() throws ParserConfigurationException {
		String rootname = this.getRoot().getNodeName();
		super.createDocument(rootname);
		
	}

}
