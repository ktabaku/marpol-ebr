/**
 *
 */
/**
 * CVS markups
 * last update by : $Author: crosay $
 * $Date: 2009-10-14 21:50:17 $
 * $Header: /media/disk/capcodeCVSbackup/capcode.basics/src/capcode/xml/XMLParser.java,v 1.28 2009-10-14 21:50:17 crosay Exp $
 * $Log: not supported by cvs2svn $
 * Revision 1.27  2009/09/03 21:49:20  crosay
 * Incomplete # change the logging of event
 * http://sourceforge.net/apps/trac/capcode/ticket/33
 *
 * Revision 1.26  2009/08/31 22:18:08  crosay
 * Incomplete # preparation for delivery to version 0.9.3
 * http://sourceforge.net/apps/trac/capcode/ticket/17
 *
 * Revision 1.25  2009/08/15 15:14:28  crosay
 * Complete # add reference points to maps
 * http://sourceforge.net/apps/trac/capcode/ticket/22
 *
 * Revision 1.24  2009/03/31 07:43:28  crosay
 * Incomplete - taskSourceForge.net: CapCode-software suite for sailors: Detail: 2705654 - NMEA data mapping (FR 2605966 Input Mapping)
 * https://sourceforge.net/tracker2/index.php?func=detail&aid=2705654&group_id=100698&atid=1045270
 *
 * Revision 1.23  2009/03/16 17:13:05  crosay
 * Complete - taskexternalise strings
 *
 * Revision 1.22  2009/03/04 10:15:55  crosay
 * Complete - taskdisplay the equipments connected to PC
 *
 * Complete - taskexternalise strings
 *
 * Incomplete - tasknew plugin: bearing-speed-and log
 *
 * Revision 1.21  2009/02/26 21:53:07  crosay
 * Complete - taskdisplay the equipments connected to PC
 *
 * Incomplete - tasktest case for plugins in general
 *
 * Incomplete - taskupgrade of simulator
 *
 * Revision 1.20  2009/02/20 22:48:20  crosay
 * Incomplete - taskimplement the possibility to change perspective
 *
 * Revision 1.18  2009/02/07 17:20:40  crosay
 * Incomplete - taskcheck correct behaviour of new speed capture mechanism
 * Complete - tasktask 2577326 - helmsman display: auto scale
 * https://sourceforge.net/tracker2/?func=detail&aid=2577326&group_id=100698&atid=1045270
 *
 * Revision 1.17  2008/11/10 10:42:05  crosay
 * undocumented changes (code washup)
 *
 * Revision 1.16  2008/10/10 21:09:02  crosay
 * modifications needed for the speedpolar xml file
 *
 * Revision 1.15  2008/09/17 11:10:26  crosay
 * added function createElementInDocument
 *
 * Revision 1.14  2008/09/11 15:23:59  crosay
 * implementation of  task 2081226 "preferences"
 *
 * Revision 1.13  2008/08/08 09:19:15  crosay
 * field filename changed from private to protected (used by implementing parsers)
 *
 * Revision 1.12  2008/06/18 19:46:24  crosay
 * complementing FR  1961649  	  plugin for race tracks
 * added the capability to:
 * - insert a waypoint
 * - delete a waypoint
 * - create and save a new track
 *
 * Revision 1.11  2008/05/28 16:28:55  crosay
 * added CVS markups in source code
 *
 * $Revision: 1.28 $
 * $State: Exp $
 */
package al.sybina.ebr.core.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import al.sybina.ebr.core.Activator;
import al.sybina.ebr.core.tools.Log;



/**
 * Provides methods to ease the access and search in a XML document, using a DOM structure.
 * to instantiate a XMLParser from a file : new XMLParser(String fileName)
 * @author rosaycy
 */
public class XMLParser {
	protected Document xmlDocument;
	protected String fileName;


	/**
	 * default constructor.
	 * Do nothing in particular.
	 * To create a new document call {@link #createDocument(String)} then save it with {@link #saveAs(String)}
	 * To load an existing document call {@link #load(String)}
	 */
	public XMLParser(){
		super();
	}
	/**
	 * Instantiate and load the xml file.
	 * @param fileName
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public XMLParser(String fileName) throws ParserConfigurationException, SAXException, IOException{
		super();
		load(fileName);
	}
	/**
	 * return a list {@link Vector} of {@link Element} in the node in parameter, given a
	 * key this function is mainly used by ccmap parser, to read subElements of
	 * an object for example example in the tree: <AAA> <BBB> <CCC/> </BBB>
	 * <CCC/> </AAA> let suppose that element is pointing to AAA.
	 * getElementKeys(element, "CCC") will return the node under AAA.
	 * getElementKeys(element, "//CCC") will return all the CCC nodes under AAA
	 *
	 * @param node: the node from where to look for elements
	 * @param key : the key leading to the element
	 * @return a Vector list of Elements
	 */
	public Vector<Element> getElementKeys(Element node, String key) {
		Vector<Element> res = new Vector<Element>();
		NodeList nl;
		try {
			XPath xpath = XPathFactory.newInstance().newXPath();
			nl = (NodeList) xpath.evaluate(key, node, XPathConstants.NODESET);
			for (int i = 0; i < nl.getLength(); i++) {
				res.add((Element) nl.item(i));
			}
		} catch (Exception e) {
		}
		return res;
	}

	/**
	 * return the first element of a Xpath this function is mainly used by ccmap
	 * parser, to read subElements of an object for example example in the tree:
	 * <AAA> <BBB> <CCC/> </BBB> <CCC/> </AAA> let suppose that element is
	 * pointing to AAA. getElementKey(element, "CCC") will return the first node
	 * under AAA. getElementKey(element, "//CCC") will return the first CCC node
	 * under AAA
	 *
	 * @param element
	 * @param key
	 * @return Element
	 */
	public Element getElementKey(Element element, String key) {
		Element res = null;
		try {
			XPath xpath = XPathFactory.newInstance().newXPath();
			res = (Element) xpath.evaluate(key, element, XPathConstants.NODE);
		} catch (Exception e) {
		}
		return res;
	}

	/**
	 * @return the fileName (path)
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * search in the xmlDocument an element with the Xpath search criteria the
	 * result must be unique
	 *
	 * @param key
	 * @return the first Element with the associated key example
	 *
	 * <pre>
	 * &lt;root&gt;
	 * &lt;process name=&quot;plugin1&quot;/&gt;
	 * &lt;process name=&quot;plugin2&quot;/&gt;
	 * &lt;process name=&quot;plugin3&quot;/&gt;
	 * &lt;other attr=&quot;something&quot;/&gt;
	 * &lt;/root&gt;
	 * Element res = getElement(&quot;process&quot;)
	 * res.getAttribute(&quot;name&quot;) should return &quot;plugins1&quot;
	 * res = getElementsList(&quot;unknown&quot;) should return null
	 * res = retElemetnsList(null) should raise a NullPointerException
	 * </pre>
	 */
	public Element getElement(String key) {
		Element res = null;
		try {
			XPath xpath = XPathFactory.newInstance().newXPath();
			res = (Element) xpath.evaluate(key, xmlDocument,
					XPathConstants.NODE);
		} catch (Exception e) {
		}
		return res;
	}

	/**
	 * search in the xmlDocument a list of elements with the Xpath search
	 * criteria the result will be all the elements having the Xpath key
	 *
	 * @param key
	 * @return a Vector of Elements example
	 *
	 * <pre>
	 * &lt;root&gt;
	 * &lt;process name=&quot;plugin1&quot;/&gt;
	 * &lt;process name=&quot;plugin2&quot;/&gt;
	 * &lt;process name=&quot;plugin3&quot;/&gt;
	 * &lt;other attr=&quot;something&quot;/&gt;
	 * &lt;root/&gt;
	 * </pre>
	 *
	 * Vector&lt;Element&gt; res = getElementsList("process") should
	 *         return a Vector of 3 Elements<br>
	 *         res.elementAt(0).getAttribute("name") should return "plugins1"<br>
	 *         Vector&lt;Element&gt; res = getElementsList("other") should
	 *         return a Vector of 1 Elements<br>
	 *         Vector&lt;Element&gt; res = getElementsList("unknown") should
	 *         return a Vector of 0 Elements<br>
	 *         if key is null then the result is an empty Vector
	 * @throws XPathExpressionException
	 */
	public Vector<Element> getElementsList(String key) throws XPathExpressionException {
		Vector<Element> res = new Vector<Element>();
		NodeList nl;
		XPath xpath = XPathFactory.newInstance().newXPath();
		if (key!=null && xmlDocument != null && xmlDocument.getChildNodes().getLength() > 0){
			nl = (NodeList) xpath.evaluate(key, xmlDocument, XPathConstants.NODESET);
			for (int i = 0; i < nl.getLength(); i++) {
				res.add((Element) nl.item(i));
			}
		}
		return res;
	}

	/**
	 * remove in the xmlDocument a list of elements with the Xpath search.
	 * @param key the key to the elements to remove.<br>
	 * <b>Example</b>
	 * <c>
	 * &lt;root&gt;
	 * &lt;process name=&quot;plugin1&quot;/&gt;
	 * &lt;process name=&quot;plugin2&quot;/&gt;
	 * &lt;process name=&quot;plugin3&quot;/&gt;
	 * &lt;other attr=&quot;something&quot;/&gt;
	 * &lt;root/&gt;
	 * </pre>
	 *
	 * clearElementsList("process") should
	 *         remove the first 3 elements<br>
	 *         Vector&lt;Element&gt; res = getElementsList("unknown") should do nothing
	 * @throws XPathExpressionException
	 */
	public void clearElementsList(String key) throws XPathExpressionException {
		NodeList nl;
		XPath xpath = XPathFactory.newInstance().newXPath();
		if (key!=null){
			nl = (NodeList) xpath.evaluate(key, xmlDocument,
					XPathConstants.NODESET);
			for (int i = 0; i < nl.getLength(); i++) {
				xmlDocument.removeChild(nl.item(i));
			}
		}
	}

	/**
	 * same as @link getElementList(String key) but sort on a specific attribute.
	 * @param key
	 * @param attr
	 * @return
	 * @throws XPathExpressionException
	 */
	public Vector<Element> getElementsListSortedByAttribute(String key, String attr, Class<?> as) throws XPathExpressionException {
		Vector<Element> res = getElementsList(key);
		if (res != null){
			Collections.sort(res, new ElementComparator(attr, as));
		}
		return res;
	}

	/**
	 * same as @link getElementList(String key) but sort on a specific attribute.
	 * @param key
	 * @param attr
	 * @param asc : true if ascending sort
	 * @return
	 * @throws XPathExpressionException
	 */
	public Vector<Element> getElementsListSortedByAttribute(String key, String attr, Class<?> as, boolean asc) throws XPathExpressionException {
		Vector<Element> res = getElementsList(key);
		if (res != null){
			Collections.sort(res, new ElementComparator(attr, as, asc));
		}
		return res;
	}


	/**
	 * search in the xml document the key given as a XPath key
	 *
	 * @param key
	 * @return the meaning of a node given its key example:
	 *
	 * <pre>
	 * &lt;element&gt;
	 * 10
	 * &lt;/element&gt;
	 * getValue(&quot;element&quot;) should return &quot;10&quot;
	 * in case of error (element not found) the function returns an empty string
	 * </pre>
	 * @throws XPathException
	 */
	public String getValue(String key) throws XPathException {
		String result = ""; //$NON-NLS-1$
		Element e = getElement(key);
		if (e != null) {
			result = e.getTextContent();
		}else{
			throw new XPathException(key + " not Found"); //$NON-NLS-1$
		}
		return result;
	}

	/**
	 * load and parse given a filename
	 *
	 * @param fileName
	 * @throws ParserConfigurationException  if a DocumentBuilder cannot be created
	 * which satisfies the configuration requested
	 * @throws IOException If any IO errors occur.
	 * @throws SAXException If any parse errors occur.
	 */
	public void load(String fileName) throws ParserConfigurationException,
			SAXException, IOException {
		this.fileName = fileName;
		// create a DocumentBuilderFactory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		// create a DocumentBuilder
		DocumentBuilder db;
		db = dbf.newDocumentBuilder();
		File file = new File(fileName);
		if (file.exists()){
			try{
				xmlDocument = db.parse(file);							
			}catch(SAXParseException e){
				Log.severe(Activator.getBundle(), fileName+" is damaged", e);
				al.sybina.ebr.core.io.IO.copyFile(fileName, fileName+".bak");
				throw new IOException(fileName + " is damaged");
			}
		}else{
			throw new IOException(fileName + " not found");
		}
	}

	/**
	 * Save the Document into its original filename
	 * @throws TransformerException
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	public void save() throws TransformerException, FileNotFoundException, UnsupportedEncodingException {
		saveAs(this.fileName);
	}

	/**
	 * Save the Document into a different fileName the Class fileName is then
	 * changed
	 *
	 * @param fileName
	 * @throws TransformerException
	 * @throws FileNotFoundException 
	 * @throws UnsupportedEncodingException 
	 */
	public void saveAs(String fileName) throws TransformerException, FileNotFoundException, UnsupportedEncodingException {
		this.fileName = fileName;
		OutputStream out = new FileOutputStream(fileName);
		Result r = new StreamResult(new OutputStreamWriter(out));
		transform(xmlDocument, r);
		try {
			out.close();
		} catch (IOException e) {
		}
	}

	/**
	 * set the attribute meaning to the Element e if e exist. if the attribute
	 * does not exist, it is created.
	 *
	 * @param e
	 * @param name
	 * @param meaning
	 * @return true if the function was succesfull
	 */
	public boolean setAttribute(Element e, String name, String value) {
		boolean valid = e != null;
		if (valid) {
			e.setAttribute(name, value);
		}
		return (valid);
	}

	/**
	 * recursive function. creates the tree given as a key. eg.:
	 * root/element/sub-element/sub-sub-element if the element does not already
	 * exist:
	 *
	 * @param key
	 * @return the Element created
	 */
	public Element createChildInElement(String key, String childName) {
		Element result = null;
		Element root = getElement(key);
		boolean valid = (root != null);
		if (!valid) {
			String upKey = key.substring(0, key.lastIndexOf("/")); //$NON-NLS-1$
			String newKey = key.substring(key.lastIndexOf("/") + 1, key //$NON-NLS-1$
					.length());
			result = createChildInElement(upKey, newKey);
			if (result != null) {
				result = createChildInElement(key, childName);
			}
		} else {
			result = xmlDocument.createElement(childName);
			root.appendChild(result);
		}
		return result;
	}

	/**
	 * set a meaning to an element reachable by a key in case the key point to no
	 * element, it is tentatively created
	 *
	 * @param key
	 * @param meaning
	 * @return true if the function was successful
	 */
	public boolean setValue(String key, String value) {
		Element element = getElement(key);
		if (element == null) {
			element = createElement(key);
		}
		if (element != null) {
			element.setTextContent(value);
		}
		return element != null;
	}

	/**
	 * create an element based on the key
	 */
	public Element createElement(String key) {
		Element res = null;
		if ((key != null) && (key.trim().length() > 0)) {
			String upKey = key.substring(0, key.lastIndexOf("/")); //$NON-NLS-1$
			String newKey = key.substring(key.lastIndexOf("/") + 1, key //$NON-NLS-1$
					.length());
			res = createChildInElement(upKey, newKey);
		}
		return res;
	}

	/**
	 * create an element based on the key
	 */
	public Element createElementInDocument(String key) {
		Element res= null;
		if (xmlDocument != null){
			res = xmlDocument.createElement(key);
		}
		return res;
	}

	/**
	 * return the full Document map as a String
	 */
	@Override
	public String toString() {
		return toString(xmlDocument);
	}

	/**
	 * read a Node and its children and transform it into a String
	 * @param n Node
	 * @return String : the node and its children
	 */
	public String toString(Node n) {
		StringWriter res = new StringWriter();
		try {
			transform(n, new StreamResult(res));
		} catch (TransformerException e) {
		}
		return res.toString();
	}

	/**
	 * @return the xmlDocument
	 */
	public Document getXmlDocument() {
		return xmlDocument;
	}

	/**
	 * create a XML document from scratch.
	 *
	 * @param fileName : fileName of the future XML document.
	 * @param root : name of the root Element of the DOM structure
	 * @return
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	public Element createDocument(String fileName, String root) throws ParserConfigurationException, TransformerException, FileNotFoundException, UnsupportedEncodingException {
		createDocument(root);
		this.saveAs(fileName);
		return getRoot();
	}

	/**
	 * create a xml document with a defined root, without saving into a file.
	 * @param root
	 * @throws ParserConfigurationException
	 */
	public Document createDocument(String root) throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		DOMImplementation impl = builder.getDOMImplementation();
		xmlDocument = impl.createDocument(null, root, null);
		return xmlDocument;
	}
	/**
	 *
	 * @param xsd
	 * @param fileName
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	public void createDocumentFromModel(String dtd, String id, String fileName, String root) throws ParserConfigurationException, TransformerException, FileNotFoundException, UnsupportedEncodingException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		DOMImplementation impl = builder.getDOMImplementation();
		DocumentType tp = impl.createDocumentType("xml", id, dtd); //$NON-NLS-1$
		xmlDocument = impl.createDocument(id, root, tp);
		this.saveAs(fileName);
	}

	public void validate(String schemaFileName, String fileName) throws SAXException, IOException{
	     // define the type of schema - we use W3C:
	      String schemaLang = "http://www.w3.org/2001/XMLSchema";
	      // get validation driver:
	      SchemaFactory factory = SchemaFactory.newInstance(schemaLang);
	      // create schema by reading it from an XSD file:
	      Schema schema = factory.newSchema(new StreamSource(schemaFileName));
	      Validator validator = schema.newValidator();
	      validator.validate(new StreamSource(fileName));

	}


	public boolean getAsBoolean(String key) throws XPathException {
		return Boolean.parseBoolean(getValue(key).trim());
	}
	public float getAsFloat(String key) throws NumberFormatException, XPathException {
		return Float.parseFloat(getValue(key).trim());
	}
	public double getAsDouble(String key) throws NumberFormatException, XPathException {
		return Double.parseDouble(getValue(key).trim());
	}
	public long getAsLong(String key) throws NumberFormatException, XPathException {
		return Long.parseLong(getValue(key).trim());
	}
	public int getAsInt(String key) throws NumberFormatException, XPathException {
		return Integer.parseInt(getValue(key).trim());
	}
	public short getAsShort(String key) throws NumberFormatException, XPathException {
		return Short.parseShort(getValue(key).trim());
	}
	public byte getAsByte(String key) throws NumberFormatException, XPathException {
		return Byte.parseByte(getValue(key).trim());
	}

	/**
	 * get the value of the child of Element given by its key.
	 * @param e
	 * @param key
	 * @return a String
	 */
	public String getValue(Element e, String key){
		Element el = getElementKey(e, key);
		return el != null ? el.getTextContent() : "";
	}

	/**
	 * get the value of the child of Element given by its key.
	 * @param e
	 * @param key
	 * @param defaultValue
	 * @return a boolean
	 * @throws XPathException
	 */
	public boolean getAsBoolean(Element e, String key) throws XPathException {
		return Boolean.parseBoolean(getValue(e, key).trim());
	}

	/**
	 * get the value of the child of Element given by its key.
	 * @param e
	 * @param key
	 * @param defaultValue
	 * @return a double
	 * @throws XPathException
	 */
	public double getAsDouble(Element e, String key) throws XPathException {
		String v = getValue(e, key);
		return v.isEmpty()? 0d : Double.parseDouble(v.trim());
	}

	/**
	 * get the value of the child of Element given by its key.
	 * @param e
	 * @param key
	 * @param defaultValue
	 * @return a long
	 * @throws XPathException
	 */
	public long getAsLong(Element e, String key) throws XPathException {
		String v = getValue(e, key);
		return v.isEmpty()? 0 : Long.parseLong(v.trim());
	}

	/**
	 * get the value of the child of Element given by its key.
	 * @param e
	 * @param key
	 * @param defaultValue
	 * @return an integer
	 * @throws XPathException
	 */
	public int getAsInt(Element e, String key) throws XPathException {
		String v = getValue(e, key);
		return v.isEmpty()? 0 : Integer.parseInt(v.trim());
	}

	/**
	 * @param key
	 * @return true if the element with the key exists. false in any other case.
	 */
	public boolean elementExists(String key) {
		return getElement(key)!= null;
	}

	/**
	 * return the root of the document.
	 * in case no root is found, returns null;
	 * @return the root of the xml document
	 */
	public Element getRoot() {
		return xmlDocument != null ? xmlDocument.getDocumentElement() : null;
	}

	/**
	 * Create a child in the parent given in parameter.
	 * The child will have the name as given in childName
	 * @param parent
	 * @param childName
	 * @return the created element
	 */
	public Element createChildInElement(Element parent, String childName) {
		Element child = xmlDocument.createElement(childName);
		parent.appendChild(child);
		return child;
	}

	// =====================private functions =================================

	/*
	 * uses a transformer a XML file (to a String or to a File for ex)
	 */
	private void transform(Node s, Result r) throws TransformerException {
			// Use a Transformer for output
			TransformerFactory tf = TransformerFactory.newInstance();
			tf.setAttribute("indent-number", new Integer(2));
			Transformer transformer = tf.newTransformer();
			// define transformer properties
			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$
			/*
			 * correction due to fault coding (changed to UTF-8) following
			 * implementation of @tag capcode.FR_1895363
			 */
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8"); //$NON-NLS-1$
			// transform
			transformer.transform(new DOMSource(s), r);
	}

}
