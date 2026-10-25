package al.sybina.ebr.core.dataBinding;


/**
 * This class provide the mechanism for data mapping.<br>
 * the aim is to link NMEA entries with capcode data fields.
 * @author c.rosay
 * @version 1.00 05/04/20
 */

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import al.sybina.ebr.core.Activator;
import al.sybina.ebr.core.data.Angle;
import al.sybina.ebr.core.data.Data;
import al.sybina.ebr.core.data.DataLink;
import al.sybina.ebr.core.data.DataLinkParser;
import al.sybina.ebr.core.data.Depth;
import al.sybina.ebr.core.data.EuclideanVector;
import al.sybina.ebr.core.data.Height;
import al.sybina.ebr.core.data.Speed;
import al.sybina.ebr.core.data.TrueWind;
import al.sybina.ebr.core.data.Wind;
import al.sybina.ebr.core.data.WindAndSpeedProcess;
import al.sybina.ebr.core.data.Angle.AngleOrigin;
import al.sybina.ebr.core.data.util.Fields;
import al.sybina.ebr.core.events.NMEAEventManager;
import al.sybina.ebr.core.exceptions.InvalidDataMapException;
import al.sybina.ebr.core.geo.Coords;
import al.sybina.ebr.core.geo.Latitude;
import al.sybina.ebr.core.geo.Longitude;
import al.sybina.ebr.core.geo.Position;
import al.sybina.ebr.core.nmea.E_NMEACodes;
import al.sybina.ebr.core.nmea.NMEAEvent;
import al.sybina.ebr.core.nmea.NMEAEventListener;
import al.sybina.ebr.core.nmea.NMEAMessageCBD;
import al.sybina.ebr.core.nmea.NMEAMessageCTW;
import al.sybina.ebr.core.nmea.NMEAEvent.EventType;
import al.sybina.ebr.core.services.IIbrCoreService;
import al.sybina.ebr.core.services.IDataService;
import al.sybina.ebr.core.services.IServiceListener;
import al.sybina.ebr.core.services.IWarningService;
import al.sybina.ebr.core.services.ServiceEventCreated;
import al.sybina.ebr.core.services.Services;
import al.sybina.ebr.core.tools.Log;
import al.sybina.ebr.core.warnings.CoreWarningMessage;
import al.sybina.ebr.core.xml.XMLParser;
/**
 * this class holds the last value for all boat information.<br>
 * the available information is:<br>
 * <b><u>boat:</B></u><br>
 * <li> position (longitude/latitude)
 * <li> surface speed<
 * <li> magnetic heading
 * <li> ground speed
 * <li> ground heading
 * <li> drift
 * <li> depth
 * <li> route log
 * <li> total log <br>
 * <b><u>wind:</B></u><br>
 * <li> relative force and direction
 * <li> true force and direction<br>
 * <b><u>other:</B></u><br>
 * <li> magnetic variation<br>
 * To get access to a data:
 *
 */
public class DataManager implements IDataService{
	private static final long MAX_AGE_FOR_DATA = 3000;

	private Vector<DataLink> dataLinks;

	/**
	 * @param dataLinks the dataLinks to set
	 */
	public void setDataLinks(Vector<DataLink> dataLinks) {
		this.dataLinks = dataLinks;
	}

	// --------------data mapped-----------------
	/*
	 * the data exist with 2 names for compatibility with older datamap files the
	 * 3 letters names is not used any more.
	 */
	/** Relative Wind Angle */
	public Angle rwa = new Angle(0);

	/** Relative Wind Speed */
	public Speed rws = new Speed(0);

	/** true Wind Angle */
	public Angle twa = new Angle(0);

	/** True Wind Speed */
	public Speed tws = new Speed(0);

	/** Boat Surface Speed */
	public Speed bss = new Speed(0);

	/** Boat Ground Speed */
	public Speed bgs = new Speed(0);

	/** Boat Magnetic Heading */
	public Angle bmh = new Angle(0);

	/** magnetic variation */
	public Angle magneticVariation = new Angle(0);

	/** Boat True Heading */
	public Angle bth = new Angle(0);

	/** Boat Ground Track */
	public Angle bgt = new Angle(0);

	/** boat position */
	private Position boatPosition = new Position(0,0);
	/** boat longitude */
	public Longitude blo = (Longitude) boatPosition.getLon();

	/** boat latitude */
	public Latitude bla = (Latitude) boatPosition.getLat();

	/** depth below boat */
	public Depth dpt = Depth.ZERO;

	/** altitude (extention for A/C)*/
	public Height alt = new Height(0);

	/** daily Log (distance since reset) */
	public Data dsr = new Data(0, "NM");
	/** total Log (total cumulative Distance) */
	public Data tcd = new Data(0, "NM");

	private Timer timer;

	protected IWarningService warningService;


	/**
	 * instanciate a DataManager class with default values.
	 */
	public DataManager() {
		super();
		// invalidate all the computation fields
		rwa.setValid(false);
		rwa.setAge(System.currentTimeMillis());
		rws.setValid(false);
		rws.setAge(System.currentTimeMillis());
		bss.setValid(false);
		bss.setAge(System.currentTimeMillis());
		bgs.setValid(false);
		bgs.setAge(System.currentTimeMillis());
		bmh.setValid(false);
		bmh.setAge(System.currentTimeMillis());
		bth.setValid(false);
		bth.setAge(System.currentTimeMillis());
		bgt.setValid(false);
		bgt.setAge(System.currentTimeMillis());
		blo.setValid(false);
		blo.setAge(System.currentTimeMillis());
		bla.setValid(false);
		bla.setAge(System.currentTimeMillis());
		dpt.setValid(false);
		dpt.setAge(System.currentTimeMillis());
		magneticVariation.setValid(false);
		magneticVariation.setAge(System.currentTimeMillis());
		dsr.setValid(false);
		dsr.setAge(System.currentTimeMillis());
		tcd.setValid(false);
		tcd.setAge(System.currentTimeMillis());


		// add a nmea event listener
		NMEAEventManager.addNMEAEventListener(new NMEAEventListener(){
			@Override
			public void newNMEAEvent(NMEAEvent event) {
				// IGNORE WRN MESSAGES
				E_NMEACodes code = event.message.getNMEACode();
				if (code != E_NMEACodes.WRN) {
					//remove any alert message for this com port
					if (warningService != null){
						warningService.removeMessage(event.commSource);
					}

					// CHECK IF THE MESSAGE IS AWAITED
					if (dataLinks != null){
						for (DataLink link : dataLinks) {
							if (link.nmeaCode.equalsIgnoreCase(code.toString())) {
								// YES, then set the associated field
								try {
									setValueToField(link, event.message.getOriginalText());
								} catch (Exception e) {
									Log.severe(Activator.getBundle(), e.getMessage(), e);
								}
							}
						}
					}
				}
			}

			@Override
			public void noNMEATimeOut(NMEAEvent event) {
				if (warningService != null){
					warningService.newMessage("DATAMAPPER", new CoreWarningMessage(CoreWarningMessage.ESeverity.message, event.commSource, "no data received on "+event.commSource));
				}

			}
		});

		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask(){


			@Override
			public void run() {
				// send the new computed information to the demultiplexer
				try {
					checkAges();
					Speed boatSpeed = bss.isValid()? bss : bgs.isValid() ? bgs : null;
					AngleOrigin origin = Angle.AngleOrigin.NONE;
					Angle boatAngle = Angle.ZERO;
					if (bth.isValid()){
						origin = AngleOrigin.GEOGRAPHIC;
						boatAngle = bth;
					}else if (bgt.isValid()){
						origin = AngleOrigin.HYBRID;					
						boatAngle = bgt;
					}else if (bmh.isValid()){
						origin = AngleOrigin.MAGNETIC;											
						boatAngle = bmh;
					}
					if (boatSpeed != null && rwa != null && rws != null){
						TrueWind computedTrueWind = new TrueWind(boatSpeed, new Wind(rwa, rws));
						computedTrueWind.compute();
						NMEAMessageCTW ctw = new NMEAMessageCTW("CC", computedTrueWind, boatAngle, origin);
						NMEAEventManager.fireNMEAEvent(DataManager.this,
								EventType.newNMEAMessage, "DataManager", ctw);
					}

					NMEAMessageCBD cbd = new NMEAMessageCBD("DF", //$NON-NLS-1$
							rwa, rws, bmh, bth, bss, bgt, bgs, blo, bla, dpt, alt,
							dsr, tcd, computeDrift(bth, bss, bgt, bgs));
					NMEAEventManager.fireNMEAEvent(DataManager.this,
							EventType.newNMEAMessage, "DataManager", cbd);
				} catch (Exception ex) {
					Log.warning(Activator.getBundle(), ex.getMessage(), ex);
				}
			}

		}, 1000, 1000);

		Services.addServiceListener(Services.WARNING_SERVICE_ID, new IServiceListener(){

			@Override
			public void serviceStarted(ServiceEventCreated event) {
				warningService = (IWarningService)(event.service);
			}

		});
	}

	protected EuclideanVector computeDrift(Angle bth, Speed bss, Angle bgt, Speed bgs) {
		EuclideanVector res = new EuclideanVector(bss.removeFrom(bgs).value, bth.removeFrom(bgt).value);
		return res;
	}

	public void terminate(){
		timer.purge();
		timer.cancel();
	}

	public DataManager(String fileName) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		this();
		DataLinkParser dataLinkParser = new DataLinkParser(fileName);
		dataLinks = dataLinkParser.getLinks();
	}

	public DataManager(Vector<DataLink> dl){
		this();
		dataLinks = dl;
	}


	/**
	 * set the field having the name specified in link (link.dataName) with the
	 * value found in the message at position {@link DataLink#fieldNum}
	 *
	 * @param link
	 * @param NMEAmsg
	 * @throws NoSuchFieldException
	 *             when the field does not exist (or is not public) in this
	 *             class
	 * @throws SecurityException
	 *             (when field modification is not allowed)
	 */
	private void setValueToField(DataLink link, String msg)
			throws SecurityException, NoSuchFieldException {
		// get the data field of the link (for example: this.blo)
		Field f = this.getClass().getField(link.dataName);
		// parse the nmea message into string fields
		Fields nmeaMessageFields = new Fields(msg, ","); //$NON-NLS-1$
		String valueAsString = link.getValuesFromMessage(nmeaMessageFields);
		try {
			if (f != null) {
				Object o = f.get(this);
				if (o instanceof Data) {
					Data data = (Data) o;
					data.set(valueAsString);
					data.setValid(!valueAsString.isEmpty()
							&& link.isValid(nmeaMessageFields));
					data.setAge(System.currentTimeMillis());
				}
			}
		} catch (IllegalArgumentException e) {
			Log.severe(Activator.getBundle(), e.getMessage(), e);
		} catch (IllegalAccessException e) {
			Log.severe(Activator.getBundle(), e.getMessage(), e);
		} catch (SecurityException e) {
			Log.severe(Activator.getBundle(), e.getMessage(), e);
		}
	}

	/**
	 * set the data map file.<br>
	 * The data map file makes the connection between the data and the NMEA fields.
	 * for example the boat speed over ground can be read in RMC field 7.
	 * @param fn : the filename of the data map.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws XPathExpressionException
	 * @throws InvalidDataMapException : when the file is not a data map file
	 */
	public void setDataMapFile(String fn) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException, InvalidDataMapException {
		String dataMapFile = fn;
		if (isDataMapFile(fn)) {
			DataLinkParser dataLinkParser = new DataLinkParser(dataMapFile);
			dataLinks = dataLinkParser.getLinks();
		} else {
			throw new InvalidDataMapException(fn);
		}
	}

	public Data getData(String fieldName) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException{
		return checkAge(fieldName);
	}

	private void checkAges() {
		long t = System.currentTimeMillis();
		for (Field f : this.getClass().getFields()) {
			Object o;
			try {
				o = f.get(this);
				if (o instanceof Data) {
					Data data = (Data) o;
					if (t - data.getAge() > MAX_AGE_FOR_DATA) {
						data.setValid(false);
					}
				} else if (o instanceof Coords) {
					Coords coord = (Coords) o;
					if (t - coord.getAge() > MAX_AGE_FOR_DATA) {
						coord.setValid(false);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// TODO Auto-generated method stub

	}

	private Data checkAge(String fieldName) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		long t = System.currentTimeMillis();
		Field f = this.getClass().getField(fieldName);
		if (f != null){
			Object o;
			o = f.get(this);
			if (o instanceof Data) {
				Data data = (Data) o;
				if (t - data.getAge() > MAX_AGE_FOR_DATA) {
					data.setValid(false);
					return data;
				}
			}
		}
		return null;
	}

	/**
	 * @param fileName
	 * @return true if the file is a dataParameter file
	 */
	public static boolean isDataMapFile(String fileName) {
		boolean res = false;
		if (fileName != null && !fileName.isEmpty()) {
			XMLParser parser = new XMLParser();
			try {
				parser.load(fileName);
				res = parser.elementExists("/map/dataMap"); //$NON-NLS-1$
			} catch (Exception e) {
				Log.warning(Activator.getBundle(), e.getMessage(), e);
			}
		}
		return res;
	}

	@Override
	public Object getDataByName(String name) {
		String dataName = name.trim().toLowerCase();
		dataName.replace(" ", "");
		
		if (dataName.equals("boatpos")){
			try {
				Coords lon = (Coords) checkAge("blo");
				Coords lat = (Coords) checkAge("bla");
				return new Position(lat, lon);
			} catch (Exception e) {
				return null;
			}
		}else if (dataName.equals("relativewind")){
			try {
				Speed rws = (Speed) checkAge("rws");
				Angle rwa = (Angle) checkAge("rwa");
				return new Wind(rwa, rws);
			} catch (Exception e) {
				return null;
			}
		}else if (dataName.equals("truewind")){
			try {
				Speed tws = (Speed) checkAge("tws");
				Angle twa = (Angle) checkAge("twa");
				return new Wind(twa, tws);
			} catch (Exception e) {
				return null;
			}
		}else if (dataName.equals("groundspeed")){
			try {
				return (Speed) checkAge("bgs");
			} catch (Exception e) {
				return null;
			}
		}else if (dataName.equals("surfacespeed")){
			try {
				return (Speed) checkAge("bss");
			} catch (Exception e) {
				return null;
			}
		}else if (dataName.equals("depth")){
			try {
				return (Depth) checkAge("dpt");
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

}
