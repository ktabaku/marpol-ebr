/**
 * Project: capcode.basics WaypointRecord.java created 18 Jun 2008 by rosaycy
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

package al.sybina.ebr.core.gpx;

import java.io.IOException;
import java.util.Calendar;

import javax.inject.Inject;

import org.eclipse.fx.ui.services.resources.ImageProvider;
import org.w3c.dom.Element;

import al.sybina.ebr.core.Activator;
import al.sybina.ebr.core.data.Height;
import al.sybina.ebr.core.geo.Position;
import al.sybina.ebr.core.maps.java2D.MapElement2DImage;
import al.sybina.ebr.core.services.Services;
import al.sybina.ebr.core.tools.Log;
import al.sybina.ebr.core.tools.PluginTools;
import al.sybina.ebr.core.xml.XMLParser;
import javafx.geometry.Dimension2D;
import javafx.scene.image.WritableImage;


/**
 * @author rosaycy class used to store waypoint data.
 */
public class GpxWaypointRecord{
	private String name = "";
	
	private String wpType;
	private String navType;
	private String speed;
	private String maxSpeed;
	private String minSpeed;
	private String runtime;
	private String intervalPosition;
	private String xte;
	private String bwc;
	private String engineStatus;
	private String turnRadius;
	private String wOver;
	private String rateOf;
	
	private Position pos = Position.ZERO;
	private Height altitude = Height.ZERO;
	private String symbolName = "";
	private String desc = "";
	private String comments = "";
	private MapElement2DImage mapElement2D;
	//leg information. (these are the information from the boat position or the previous wpt
	private Calendar eta;
	private boolean passedWaypoint;
	private int bearing;
	private double distance;
	private double cumulDistance;
	
	private String minDepth;
	private String tide;
	private String catzoc;
	private String availableDepth;
	private String dinamicDraft;
	private String maxHeight;
	private String obstacleHeight;
	private String clearance;
	private String safetyContour;
	private String attentionContour;

	public GpxWaypointRecord() {
		super();
		altitude = new Height(0, true);
		if(Services.getSymbolService() != null) {
			String imageFile = Services.getSymbolService().getdefaultSymbolForRoute();
			try {
				mapElement2D = new MapElement2DImage(imageFile, new Dimension2D(20, 20));
			} catch (IOException e) {
				Log.severe(Activator.getBundle(), "error loading image " + imageFile, e);
			}
		} else {
			mapElement2D = new MapElement2DImage(new WritableImage(20, 20));
		}
	}

	public GpxWaypointRecord(String name, Position pos) {
		this();
		this.name = name;
		mapElement2D.setName(name);
		mapElement2D.setPosition(pos);
		this.pos = pos;
	}


	/**
	 * set the position of a wayPoint (display and record)
	 * 
	 * @param pos
	 *            Position to set
	 */
	public void setPosition(Position pos) {
		this.pos = pos;
		mapElement2D.setPosition(pos);
	}
	public void setName(String text) {
		name = text;
		mapElement2D.setName(name);
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return String.format("%s: %s, %s [%s] (%s)", name, desc, pos, symbolName, comments);
	}

	public Position getPosition() {
		return pos;
	}

	public void setAltitude(Height a) {
		altitude = a;
	}

	public Height getAltitude() {
		return altitude;
	}

	/**
	 * @return the symbolName
	 */
	public String getSymbolName() {
		return symbolName;
	}

	/**
	 * @param symbolName
	 *            the symbolName to set
	 */
	public void setSymbolName(String symbolName) {
		this.symbolName = symbolName;
	}


	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}


	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc
	 *            the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}


	public MapElement2DImage getMapElement() {
		return mapElement2D;
	}

	/*
	 * set the values as specified in the gpx wptType scheme
	 */
	public void writeToElement(GpxParser parser, Element node){
		node.setAttribute("lat", Double.toString(getPosition().getLat().toDegrees()));
		node.setAttribute("lon", Double.toString(getPosition().getLon().toDegrees()));
		Element nameElement = parser.createChildInElement(node, "name");
		nameElement.setTextContent(getName());
		
		
		Element typeElement= parser.createChildInElement(node, "type");
		typeElement.setTextContent(getWpType());
		
		Element navTypeElement= parser.createChildInElement(node, "navType");
		navTypeElement.setTextContent(getNavType());
		
		Element speedElement= parser.createChildInElement(node, "speed");
		speedElement.setTextContent(getSpeed());
		
		Element maxSpeedElement= parser.createChildInElement(node, "maxSpeed");
		maxSpeedElement.setTextContent(getMaxSpeed());
		
		Element minSpeedElement= parser.createChildInElement(node, "minSpeed");
		minSpeedElement.setTextContent(getMinSpeed());
		
		Element rtElement= parser.createChildInElement(node, "runtime");
		rtElement.setTextContent(getRuntime());
		
		Element ipElement= parser.createChildInElement(node, "intervalPosition");
		ipElement.setTextContent(getIntervalPosition());
		
		Element bwcElement= parser.createChildInElement(node, "bwc");
		bwcElement.setTextContent(getBwc());
		
		Element xteElement= parser.createChildInElement(node, "xte");
		xteElement.setTextContent(getXte());
		
		Element esElement= parser.createChildInElement(node, "engineStatus");
		esElement.setTextContent(getEngineStatus());
		
		Element trElement= parser.createChildInElement(node, "turnRadius");
		trElement.setTextContent(getTurnRadius());
		
		Element woElement= parser.createChildInElement(node, "whellOver");
		woElement.setTextContent(getwOver());
		
		Element rotElement= parser.createChildInElement(node, "rateOfTurn");
		rotElement.setTextContent(getRateOf());
		//safety
		Element minDepthElement= parser.createChildInElement(node, "minDepth");
		minDepthElement.setTextContent(getMinDepth());
		
		Element tideElement= parser.createChildInElement(node, "tide");
		tideElement.setTextContent(getTide());
		
		Element catzocElement= parser.createChildInElement(node, "catzoc");
		catzocElement.setTextContent(getMinDepth());
		
		Element availableDepthElement= parser.createChildInElement(node, "availableDepth");
		availableDepthElement.setTextContent(getAvailableDepth());
		
		Element dinDraftElement= parser.createChildInElement(node, "dinamicDraft");
		dinDraftElement.setTextContent(getDinamicDraft());
		
		Element maxHeightElement= parser.createChildInElement(node, "maxHeight");
		maxHeightElement.setTextContent(getMaxHeight());
		
		Element obstacleHeightElement= parser.createChildInElement(node, "obstacleHeight");
		obstacleHeightElement.setTextContent(getObstacleHeight());
		
		Element clearanceElement= parser.createChildInElement(node, "clearance");
		clearanceElement.setTextContent(getClearance());
		
		Element attentionContourElement= parser.createChildInElement(node, "attentionContour");
		attentionContourElement.setTextContent(getAttentionContour());
		
		Element safetyContourElement= parser.createChildInElement(node, "safetyContour");
		safetyContourElement.setTextContent(getSafetyContour());
		
		
		Element cmtElement = parser.createChildInElement(node, "cmt");
		cmtElement.setTextContent(getComments());
		Element descElement = parser.createChildInElement(node, "desc");
		descElement.setTextContent(getDesc());
		Element elevElement = parser.createChildInElement(node, "ele");
		elevElement.setTextContent(Double.toString(getAltitude().doubleValue()));
		Element symbElement = parser.createChildInElement(node, "sym");
		symbElement.setTextContent(getSymbolName());
	}

	public void readFromElement(GpxParser parser, Element node) {
		double lon = Double.parseDouble(node.getAttribute("lon"));
		double lat = Double.parseDouble(node.getAttribute("lat"));
		setPosition(new Position(lat, lon));
		setName(parser.getValue(node, "name"));
		
		setWpType(parser.getValue(node,"type"));
		setNavType(parser.getValue(node,"navType"));
		setSpeed(parser.getValue(node,"speed"));
		setMaxSpeed(parser.getValue(node,"maxSpeed"));
		setMinSpeed(parser.getValue(node,"minSpeed"));
		setRuntime(parser.getValue(node,"runtime"));
		setXte(parser.getValue(node,"xte"));
		setIntervalPosition(parser.getValue(node,"intervalPosition"));
		setEngineStatus(parser.getValue(node,"engineStatus"));
		setBwc(parser.getValue(node,"bwc"));
		setTurnRadius(parser.getValue(node,"turnRadius"));
		setwOver(parser.getValue(node, "whellOver"));
		setRateOf(parser.getValue(node, "rateOfTurn"));
		//safety
		setMinDepth(parser.getValue(node, "minDepth"));
		setTide(parser.getValue(node, "tide"));
		setCatzoc(parser.getValue(node, "catzoc"));
		setAvailableDepth(parser.getValue(node, "minDepth"));
		setDinamicDraft(parser.getValue(node, "dinamicDraft"));
		setMaxHeight(parser.getValue(node, "maxHeight"));
		setObstacleHeight(parser.getValue(node, "obstacleHeight"));
		setClearance(parser.getValue(node, "clearance"));
		setAttentionContour(parser.getValue(node, "attentionContour"));
		setSafetyContour(parser.getValue(node, "safetyContour"));
		
		setSymbolName(parser.getValue(node, "sym"));
		setComments(parser.getValue(node, "cmt"));
		try {
			setAltitude(new Height(parser.getAsDouble(node, "ele")));
		} catch (Exception e) {
			setAltitude(Height.ZERO);
		}
		//		//get the icon fileName
		if (mapElement2D != null && Services.getSymbolService() != null){
			String imagePath = Services.getSymbolService().getImageFileName(getSymbolName());
			try {
				mapElement2D.setImageFilePathName(imagePath);
			} catch (IOException e) {
				Log.severe(Activator.getBundle(), "cannot set image "+imagePath, e);
			}
		}
	}

	public Calendar getEta() {
		return eta;
	}

	public void setEta(Calendar eta2) {
		eta = eta2;
	}

	public boolean isPassedWaypoint() {
		return passedWaypoint;
	}

	public void setPassedWaypoint(boolean passedWaypoint) {
		this.passedWaypoint = passedWaypoint;
	}

	public int getBearing() {
		return bearing;
	}

	public void setBearing(int bearing) {
		this.bearing = bearing;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getCumulDistance() {
		return cumulDistance;
	}

	public void setCumulDistance(double cumulDistance) {
		this.cumulDistance = cumulDistance;
	}

	public String getWpType() {
		return wpType;
	}

	public void setWpType(String wpType) {
		this.wpType = wpType;
	}

	public String getNavType() {
		return navType;
	}

	public void setNavType(String navType) {
		this.navType = navType;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getRuntime() {
		return runtime;
	}

	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}

	public String getIntervalPosition() {
		return intervalPosition;
	}

	public void setIntervalPosition(String intervalPosition) {
		this.intervalPosition = intervalPosition;
	}

	public String getXte() {
		return xte;
	}

	public void setXte(String xte) {
		this.xte = xte;
	}

	public String getBwc() {
		return bwc;
	}

	public void setBwc(String bwc) {
		this.bwc = bwc;
	}

	public String getEngineStatus() {
		return engineStatus;
	}

	public void setEngineStatus(String engineStatus) {
		this.engineStatus = engineStatus;
	}

	public String getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(String maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public String getMinSpeed() {
		return minSpeed;
	}

	public void setMinSpeed(String minSpeed) {
		this.minSpeed = minSpeed;
	}

	public String getTurnRadius() {
		return turnRadius;
	}

	public void setTurnRadius(String turnRadius) {
		this.turnRadius = turnRadius;
	}

	public String getwOver() {
		return wOver;
	}

	public void setwOver(String wOver) {
		this.wOver = wOver;
	}

	public String getRateOf() {
		return rateOf;
	}

	public void setRateOf(String rateOf) {
		this.rateOf = rateOf;
	}

	public String getMinDepth() {
		return minDepth;
	}

	public void setMinDepth(String minDepth) {
		this.minDepth = minDepth;
	}

	public String getTide() {
		return tide;
	}

	public void setTide(String tide) {
		this.tide = tide;
	}

	public String getCatzoc() {
		return catzoc;
	}

	public void setCatzoc(String catzoc) {
		this.catzoc = catzoc;
	}

	public String getAvailableDepth() {
		return availableDepth;
	}

	public void setAvailableDepth(String availableDepth) {
		this.availableDepth = availableDepth;
	}

	public String getDinamicDraft() {
		return dinamicDraft;
	}

	public void setDinamicDraft(String dinamicDraft) {
		this.dinamicDraft = dinamicDraft;
	}

	public String getMaxHeight() {
		return maxHeight;
	}

	public void setMaxHeight(String maxHeight) {
		this.maxHeight = maxHeight;
	}

	public String getObstacleHeight() {
		return obstacleHeight;
	}

	public void setObstacleHeight(String obstacleHeight) {
		this.obstacleHeight = obstacleHeight;
	}

	public String getClearance() {
		return clearance;
	}

	public void setClearance(String clearance) {
		this.clearance = clearance;
	}

	public String getSafetyContour() {
		return safetyContour;
	}

	public void setSafetyContour(String safetyContour) {
		this.safetyContour = safetyContour;
	}

	public String getAttentionContour() {
		return attentionContour;
	}

	public void setAttentionContour(String attentionContour) {
		this.attentionContour = attentionContour;
	}

}
