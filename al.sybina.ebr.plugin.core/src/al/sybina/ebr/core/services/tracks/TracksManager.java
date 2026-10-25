/**
 *
 */
package al.sybina.ebr.core.services.tracks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import al.sybina.ebr.core.Activator;
import al.sybina.ebr.core.data.Speed;
import al.sybina.ebr.core.data.Wind;
import al.sybina.ebr.core.events.NMEAEventManager;
import al.sybina.ebr.core.geo.Position;
import al.sybina.ebr.core.geo.transform.Orthodromy;
import al.sybina.ebr.core.gpx.TracksFileParser;
import al.sybina.ebr.core.nmea.NMEAEvent;
import al.sybina.ebr.core.nmea.NMEAEventListener;
import al.sybina.ebr.core.nmea.NMEAMessageCBD;
import al.sybina.ebr.core.nmea.NMEAMessageCSV;
import al.sybina.ebr.core.nmea.NMEAMessageCTW;
import al.sybina.ebr.core.services.IIbrCoreService;
import al.sybina.ebr.core.services.ITracksService;
import al.sybina.ebr.core.tools.Log;

/**
 * This class is used to record, store and display the different positions of the boat.
 * @author rosaycy
 */
public class TracksManager implements
		NMEAEventListener, ITracksService {
	private final Position boatPos;
	private final Position lastStoredBoatPos;
	private double boatSpeed = 0;
	private double targetSpeed = 0;
	private Wind trueWind = new Wind(0, 0);
	protected TracksFileParser boatTracks;
	private double triggerDistance;

	private static final double TRIGGER_INCREMENT = 1 / 100d;

	/**
	 * instantiates a BoatTracks writer/parser to store and read the data.
	 * @param fileName
	 * @throws TransformerException
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public TracksManager(String fileName){
		super();
		boatPos = new Position("", ""); //$NON-NLS-1$ //$NON-NLS-2$
		lastStoredBoatPos = new Position("", ""); //$NON-NLS-1$ //$NON-NLS-2$
		try {
			boatTracks = new TracksFileParser(fileName);
		} catch (Exception e) {
			Log.severe(Activator.getBundle(), fileName + "is damaged", e);
			File f = new File(fileName);
			if (f.exists()){
				f.delete();
			}
		}
		NMEAEventManager.addNMEAEventListener(this);
	}


	public void clearAllRecords() throws ParserConfigurationException, TransformerException, FileNotFoundException, UnsupportedEncodingException {
			// boatTracks.clear();
			boatTracks.clearGpx();
	}

	/**
	 * @return the boatTrace
	 */
	public TracksFileParser getBoatTrace() {
		return this.boatTracks;
	}


	@Override
	public void newNMEAEvent(NMEAEvent event) {
		//a new record in the file is needed (more than XX m)
		boolean newRecordNeeded = false;
		switch(event.message.getNMEACode()) {
		case CTW:
			NMEAMessageCTW ctw = (NMEAMessageCTW) event.message;
			trueWind = ctw.getTrueWind();
			break;
		case CBD:
			NMEAMessageCBD cbd = (NMEAMessageCBD) event.message;
			boatSpeed = cbd.getBoatGroundSpeed().getValue();
			// GPS information
			// set the boat position
			boatPos.set(cbd.getBoatLatitude(), cbd.getBoatLongitude());
			// display all the records of the day
			// check if we need to record a new boat position
			Orthodromy o = new Orthodromy(lastStoredBoatPos, boatPos);
			newRecordNeeded = (o.getDistance() > triggerDistance);
			if (newRecordNeeded && boatTracks != null) {
				try {
					boatTracks.addGpxRecord(
							boatPos,
							new Speed(boatSpeed),
							new Speed(targetSpeed),
							new Wind(trueWind));
				} catch (Exception e) {
					Log.severe(Activator.getBundle(), e.getLocalizedMessage(), e);
				}
				lastStoredBoatPos.set(boatPos.getLat(), boatPos.getLon());
			}
			break;
		case CSV:
			NMEAMessageCSV csv = (NMEAMessageCSV) event.message;
			targetSpeed = csv.getTargetSpeed().getValue();
			break;
		default:
		}
	}

	@Override
	public void noNMEATimeOut(NMEAEvent event) {

	}


	@Override
	public void terminate() {
		//nothing to do in particular
	}


	@Override
	public void openTrack(String fileName) {
		try {
			this.boatTracks.loadGpxFile(fileName);
		} catch (Exception e) {
			Log.severe(Activator.getBundle(), "error opening track " + fileName, e);
		}
	}


	@Override
	public String[] getFileExtensions() {
		return new String[]{"*.gpx", "*.kml"};
	}


	@Override
	public void saveAs(String fileName) {
		try {
			boatTracks.saveAs(fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
