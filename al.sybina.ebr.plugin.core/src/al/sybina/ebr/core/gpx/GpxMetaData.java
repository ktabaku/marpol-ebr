package al.sybina.ebr.core.gpx;

import java.net.URL;
import java.util.Calendar;
import java.util.TimeZone;

import javax.xml.bind.DatatypeConverter;

import org.w3c.dom.Element;

import al.sybina.ebr.core.Activator;
import al.sybina.ebr.core.geo.Sector;
import al.sybina.ebr.core.tools.Log;
import al.sybina.ebr.core.xml.XMLParser;

/**
 * class for gpx meta data record.br>
 * Information about the GPX file, 
 * author, and copyright restrictions goes in the metadata section. 
 * Providing rich, meaningful information about your GPX files 
 * allows others to search for and use your GPS data.
 * http://www.topografix.com/gpx/1/1/#type_metadataType<br>
 * @author crosay
 */
public class GpxMetaData {
	/**
	 * schema	
	 * <name> xsd:string </name> [0..1]
	 * <desc> xsd:string </desc> [0..1]
	 * <author> personType </author> [0..1]
	 * <copyright> copyrightType </copyright> [0..1]
	 * <link> linkType </link> [0..*] 
	 * <time> xsd:dateTime </time> [0..1]
	 * <keywords> xsd:string </keywords> [0..1]
	 * <bounds> boundsType </bounds> [0..1]
	 * <extensions> extensionsType </extensions> [0..1]
	 */
	// The name of the GPX file.
	private String name = "";
	//A description of the contents of the GPX file.
	private String desc = "";
	//The person or organization who created the GPX file.
	private String author = System.getProperty("user.name") + " with capcode (http://capcode.sourceforge.net)";
	//Copyright and license information governing use of the file.
	private String copyright = "no copyright defined for this file";
	//URLs associated with the location described in the file.
	private URL link = null;
	//The creation date of the file.
	private Calendar time = null;
	//Keywords associated with the file. Search engines or databases can use this information to classify the data
	private String keywords = "";
	//Minimum and maximum coordinates which describe the extent of the coordinates in the file.
	private Sector bounds = null;
	//Allow any elements from a namespace other than this schema's namespace (lax validation). 
	private IGpxExtension extensions = null;
	
	/**
	 * Instantiate a meta data with the given name.<br>
	 * the time is set to now.
	 * the other fields have to be set up by setters.
	 * @param name
	 */
	public GpxMetaData(String name){
		this();
		this.name = name;
	}
	
	/**
	 * Instantiate a meta data with the predefined field.<br>
	 * the time is set to now.
	 * the other fields have to be set up by setters.
	 * @param name
	 */
	public GpxMetaData() {
		super();
		this.time = Calendar.getInstance();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}
	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}
	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}
	/**
	 * @return the copyright
	 */
	public String getCopyright() {
		return copyright;
	}
	/**
	 * @param copyright the copyright to set
	 */
	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}
	/**
	 * @return the link
	 */
	public URL getLink() {
		return link;
	}
	/**
	 * @param link the link to set
	 */
	public void setLink(URL link) {
		this.link = link;
	}
	/**
	 * @return the time
	 */
	public Calendar getTime() {
		return time;
	}
	/**
	 * @param time the time to set
	 */
	public void setTime(Calendar time) {
		this.time = time;
	}
	/**
	 * @return the keywords
	 */
	public String getKeywords() {
		return keywords;
	}
	/**
	 * @param keywords the keywords to set
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	/**
	 * @return the bounds
	 */
	public Sector getBounds() {
		return bounds;
	}
	/**
	 * @param bounds the bounds to set
	 */
	public void setBounds(Sector bounds) {
		this.bounds = bounds;
	}
	/**
	 * @return the extensions
	 */
	public IGpxExtension getExtensions() {
		return extensions;
	}
	/**
	 * @param extensions the extensions to set
	 */
	public void setExtensions(IGpxExtension extensions) {
		this.extensions = extensions;
	}
	
	public String toString(){
		return String.format("name:%s, desc:%s, author:%s, bounds : %s", 
				name,
				desc,
				author,
				bounds.toString());
	}
	
	/**
	 * create the meta data section in the element of the document.
	 * @param parser : the XMLParser where the section has to be added
	 * @param parent : the parent under which the section is added
	 */
	public void writeToElement(XMLParser parser, Element parent) {
		Element nameElement = parser.createChildInElement(parent, "name");
		nameElement.setTextContent(name);
		
		if (!desc.isEmpty()){
			Element descElement = parser.createChildInElement(parent, "desc");
			descElement.setTextContent(desc);
		}

		if (!author.isEmpty()){
			Element authorElement = parser.createChildInElement(parent, "author");
			authorElement.setTextContent(author);
		}
		
		if (!copyright.isEmpty()){
			Element copyRightElement = parser.createChildInElement(parent, "copyright");
			copyRightElement.setTextContent(copyright);
		}
		
		if (link != null){
			Element linkElement = parser.createChildInElement(parent, "link");
			linkElement.setTextContent(link.toString());			
		}
		
		time = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		Element timeElement = parser.createChildInElement(parent, "time");
		timeElement.setTextContent(String.format("%tFT%tTZ", time, time));

		if (!keywords.isEmpty()){
			Element keywordElement = parser.createChildInElement(parent, "keywords");
			keywordElement.setTextContent(keywords.toString());			
		}
		if (bounds != null){
			Element boundsElement = parser.createChildInElement(parent, "bounds");
			boundsElement.setAttribute("minlat", Double.toString(bounds.getMinLat()));
			boundsElement.setAttribute("maxlat", Double.toString(bounds.getMaxLat()));
			boundsElement.setAttribute("minlon", Double.toString(bounds.getMinLon()));
			boundsElement.setAttribute("maxlon", Double.toString(bounds.getMaxLon()));			
		}
		if (extensions != null){
			extensions.writeToElement(parser, parent);
		}
		
	}

	public static GpxMetaData readFromElement(XMLParser parser, Element parent) {
		GpxMetaData res = new GpxMetaData();
		Element nameElement = parser.getElementKey(parent, "name");
		if (nameElement != null){
			res.setName(nameElement.getTextContent());
		}
		
		Element descElement = parser.getElementKey(parent, "desc");
		if (descElement != null){
			res.setDesc(descElement.getTextContent());
		}

		Element authorElement = parser.getElementKey(parent, "author");
		if (authorElement != null){
			res.setAuthor(authorElement.getTextContent());
		}
		
		Element copyrightElement = parser.getElementKey(parent, "copyright");
		if (copyrightElement != null){
			res.setCopyright(copyrightElement.getTextContent());
		}
		
		Element linkElement = parser.getElementKey(parent, "link");
		if (linkElement != null){
			try {
				res.setLink(new URL(linkElement.getTextContent()));
			} catch (Exception e) {
				res.setLink(null);
				Log.severe(Activator.getBundle(), "error reading url for gpx", e);
			}
		}
		
		Element timeElement = parser.getElementKey(parent, "time");
		if (timeElement != null){
			res.setTime(DatatypeConverter.parseDateTime(timeElement.getTextContent()));
		}
		
		Element keywordsElement = parser.getElementKey(parent, "keywords");
		if (keywordsElement != null){
			res.setKeywords(keywordsElement.getTextContent());
		}
		
		Element boundsElement = parser.getElementKey(parent, "bounds");
		if (boundsElement != null){
			double minlat;
			try{
				minlat = Double.parseDouble(boundsElement.getAttribute("minlat"));
			}catch(NumberFormatException e){
				minlat = 0;
			}
			double minlon;
			try{
				minlon = Double.parseDouble(boundsElement.getAttribute("minlon"));
			}catch(NumberFormatException e){
				minlon = 0;
			}
			double maxlat;
			try{
				maxlat = Double.parseDouble(boundsElement.getAttribute("maxlat"));
			}catch(NumberFormatException e){
				maxlat = 0;
			}
			double maxlon;
			try{
				maxlon = Double.parseDouble(boundsElement.getAttribute("maxlon"));
			}catch(NumberFormatException e){
				maxlon = 0;
			}
			res.bounds = new Sector(maxlat, minlon, minlat, maxlon);
		}
		
		Element extensionsElement = parser.getElementKey(parent, "extensions");
		if (extensionsElement != null){
			res.extensions.readFromElement(parser, parent);
		}
		
		return res;
	}
}
