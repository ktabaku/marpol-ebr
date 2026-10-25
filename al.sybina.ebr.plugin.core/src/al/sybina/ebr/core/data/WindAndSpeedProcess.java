/**
 * CVS markups
 * last update by : $Author: crosay $
 * $Date: 2009-11-02 00:10:37 $
 * $Header: /media/disk/capcodeCVSbackup/capcode.basics/src/capcode/data/WindAndSpeedProcess.java,v 1.20 2009-11-02 00:10:37 crosay Exp $
 * $Log: not supported by cvs2svn $
 * Revision 1.19  2009/09/03 21:49:20  crosay
 * Incomplete # change the logging of event
 * http://sourceforge.net/apps/trac/capcode/ticket/33
 *
 * Revision 1.18  2009/04/23 10:26:42  crosay
 * Incomplete - taskSourceForge.net: CapCode-software suite for sailors: Detail: 2705654 - NMEA data mapping (FR 2605966 Input Mapping)
 * https://sourceforge.net/tracker2/index.php?func=detail&aid=2705654&group_id=100698&atid=1045270
 * Incomplete - taskzygrib
 *
 * Revision 1.17  2009/03/31 07:42:19  crosay
 * Incomplete - taskSourceForge.net: CapCode-software suite for sailors: Detail: 2705654 - NMEA data mapping (FR 2605966 Input Mapping)
 * https://sourceforge.net/tracker2/index.php?func=detail&aid=2705654&group_id=100698&atid=1045270
 *
 * Revision 1.16  2009/03/04 10:15:55  crosay
 * Complete - taskdisplay the equipments connected to PC
 *
 * Complete - taskexternalise strings
 *
 * Incomplete - tasknew plugin: bearing-speed-and log
 *
 * Revision 1.15  2009/02/26 21:53:06  crosay
 * Complete - taskdisplay the equipments connected to PC
 *
 * Incomplete - tasktest case for plugins in general
 *
 * Incomplete - taskupgrade of simulator
 *
 * Revision 1.14  2009/02/20 22:28:51  crosay
 * Complete - task2611676 - take also MWV into account for computation of trueWind
 * https://sourceforge.net/tracker2/?func=detail&aid=2611676&group_id=100698&atid=1045270
 * Incomplete - tasktask 2577644 - SpeedPolar implement polar in tables
 * https://sourceforge.net/tracker2/?func=detail&aid=2577644&group_id=100698&atid=1045270
 *
 * Revision 1.13  2009/02/07 17:20:40  crosay
 * Incomplete - taskcheck correct behaviour of new speed capture mechanism
 * Complete - tasktask 2577326 - helmsman display: auto scale
 * https://sourceforge.net/tracker2/?func=detail&aid=2577326&group_id=100698&atid=1045270
 *
 * Revision 1.12  2009/01/29 10:31:28  crosay
 * Incomplete - task add gps hypbrid mode (in true wind computation) task 2533241 GPS speed spare mode
 * https://sourceforge.net/tracker2/?func=detail&aid=2533241&group_id=100698&atid=1045270
 * Complete - task save status and position of toolbars
 * https://sourceforge.net/tracker2/?func=detail&aid=2531530&group_id=100698&atid=1045270
 *
 * Revision 1.11  2009/01/24 20:52:36  crosay
 * Complete - taskRemoval of sqlite
 *
 * Complete - tasksuppression of debug display for preference saving
 *
 * Complete - tasktask 2531747 animation of the wearther
 * https://sourceforge.net/tracker2/?func=detail&aid=2024403&group_id=100698&atid=1045270
 *
 * Revision 1.10  2009/01/13 18:04:49  crosay
 * correction of code after unit test:
 * - SystemUtils: added exception in case of property not found
 * - SpeedPolar, SpeedPolarFile, WindandSpeedProcess: management of this exception
 *
 * Correction of a bug in Coords management (DMS function)
 *
 * Revision 1.9  2008/10/10 20:58:15  crosay
 * creation of speedpolar management with  xml files
 *
 * Revision 1.8  2008/09/11 15:23:59  crosay
 * implementation of  task 2081226 "preferences"
 *
 * Revision 1.7  2008/05/28 16:28:54  crosay
 * added CVS markups in source code
 *
 * $Revision: 1.20 $
 * $State: Exp $
 */
package al.sybina.ebr.core.data;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import al.sybina.ebr.core.Activator;
import al.sybina.ebr.core.maths.MeanValue;
import al.sybina.ebr.core.tools.Log;


/**
 * XXX complete the specifications this process computes the following datas:<br> -
 * True Wind<br> - boat Target Speed<br> - Velocity Made Good<br>
 *
 * @author rosaycy
 */
public class WindAndSpeedProcess {

	/**
	 * E_BoatBearingSource boatBearingSource
	 */
	private Angle.AngleOrigin boatBearingSource = Angle.AngleOrigin.NONE;
	/** input : boat speed */
	private Speed boatSpeed;
	/** indication of the stabilised boat speed */
	private final Stabilisator speedStabilisator;
	/** input : relative wind */
	private Wind relativeWind;
	/** storage of the mean true wind speed */
	private final MeanValue meanWindSpeed;
	/** storage of the mean wind direction */
	private final MeanValue meanWindDirection;

	/** input : boat settings */
	BoatSettings boatSettings;
	/** input : sea status */
	Sea seaStatus;
	/** input: angle used as the source for computation of the geographic wind */
	private Angle boatBearing;

	/** tool : speedPolar is used to determine VMG and targetSpeed(angle)*/
	private SpeedPolar speedPolar = null;

	/** result : true wind */
	private TrueWind trueWind;
	/**result: the target Speed for a given boat angle*/
	private Speed targetSpeed;
	/**result: the vmg associated with the targetSpeed*/
	private Speed actualVMG;
	/** result : target velocity made good for this tack */
	private EuclideanVector targetVmg;
	/** result : target velocity made good for port tack */
	private EuclideanVector targetPortVmg;
	/** result : target velocity made good for starboard tack */
	private EuclideanVector targetStarboardVmg;
	/** result: computed geographic wind direction */
	private final Angle geographicWindDirection;

	/**
	 * used to store the switch select/unselect automatic speed validation when
	 * a boat speed is greater than the actual target speed and the automatic
	 * speed validation is set, then the new target speed is recorded as the new
	 * base
	 */
	private boolean automaticSpeedValidation = false;

	/** store the reactors to new target reached */
	private I_EventSpeedComputation onNewTargetSpeedAction;
	private I_EventSpeedComputation onNewVMGAction;

	/**
	 * instantiate a WindAndSpeed process.
	 * @param table
	 * @param database
	 * @param driver
	 *
	 * @param meanHistory
	 * @param stabilityFactor
	 */
	public WindAndSpeedProcess(int meanHistory, double stabilityFactor) {
		boatSpeed = new Speed(0);
		relativeWind = new Wind(0, 0);
		trueWind = new TrueWind(boatSpeed, relativeWind);
		boatSettings = new BoatSettings(0, ""); //$NON-NLS-1$
		seaStatus = new Sea(0);
		targetVmg = new EuclideanVector(0, 0);
		targetPortVmg = new EuclideanVector(0, 0);
		targetStarboardVmg = new EuclideanVector(0, 0);

		speedStabilisator = new Stabilisator(stabilityFactor, meanHistory);
		meanWindDirection = new MeanValue(meanHistory);
		meanWindSpeed = new MeanValue(meanHistory);
		geographicWindDirection = new Angle(0);
		geographicWindDirection.setValid(false);
	}

	/**
	 * @return the boatSettings
	 */
	public BoatSettings getBoatSettings() {
		return boatSettings;
	}

	/**
	 * @param boatSettings
	 *            the boatSettings to set
	 */
	public void setBoatSettings(BoatSettings boatSettings) {
		this.boatSettings = boatSettings;
	}

	/**
	 * @return the boatSpeed
	 */
	public Speed getBoatSpeed() {
		return boatSpeed;
	}

	/**
	 * @param boatSpeed
	 *            the boatSpeed to set
	 */
	public void setBoatSpeed(Speed boatSpeed) {
		this.boatSpeed = boatSpeed;

	}

	/**
	 * @return the relativeWind
	 */
	public Wind getRelativeWind() {
		return relativeWind;
	}

	/**
	 * @param relativeWind
	 *            the relativeWind to set
	 */
	public void setRelativeWind(Wind relativeWind) {
		this.relativeWind = relativeWind;
	}

	/**
	 * @return the seaStatus
	 */
	public Sea getSeaStatus() {
		return seaStatus;
	}

	/**
	 * @param seaStatus
	 *            the seaStatus to set
	 */
	public void setSeaStatus(Sea seaStatus) {
		this.seaStatus = seaStatus;
	}

	/**
	 * @return the trueWind
	 */
	public TrueWind getTrueWind() {
		return trueWind;
	}

	/**
	 * @param trueWind
	 *            the trueWind to set
	 */
	public void setTrueWind(TrueWind trueWind) {
		this.trueWind = trueWind;
	}

	/**
	 * @return the targetVmg
	 */
	public EuclideanVector getVmg() {
		return targetVmg;
	}

	/**
	 * updateComputations<br>
	 * shall be call upon every new change of wind or boat speed or sea data
	 */
	public void compute() {
		// compute the trueWind
 		trueWind.compute();
		geographicWindDirection.setValue(trueWind.getAngle().getValue()
				+ getBoatBearing().getValue());
		geographicWindDirection.setValid(trueWind.isValid()
				&& getBoatBearing().isValid());
		geographicWindDirection.setOrigin(boatBearingSource);
		if (boatBearingSource == Angle.AngleOrigin.NONE) {
			geographicWindDirection.setValue(0);
			geographicWindDirection.setValid(false);
		}
		if (speedPolar != null){
			// get the target speed for this wind
			targetSpeed = speedPolar.getTargetSpeed(trueWind, seaStatus);
			actualVMG = speedPolar.getActualVMG();

			// get the target VMG
			targetVmg = speedPolar.getTargetVMG(trueWind, seaStatus);
			Wind otherSideWind = new Wind(trueWind);
			otherSideWind.getDirection().changeSide();

			// get the laylines
			if (trueWind.getDirection().isLeft()) {
				// true wind is port
				targetPortVmg = targetVmg;
				targetStarboardVmg = speedPolar.getTargetVMG(otherSideWind, seaStatus);
			} else {
				// true wind is starboard
				targetStarboardVmg = targetVmg;
				targetPortVmg = speedPolar.getTargetVMG(otherSideWind, seaStatus);
			}
			// check for stability of the boat speed
			if (speedStabilisator.isStabilized()) {
				// check is new target speed
				if (targetSpeed != null && boatSpeed.getValue() > targetSpeed.getValue()) {
					// new Target Speed detected
					if (automaticSpeedValidation) {
						speedPolar.setNewTargetSpeed(trueWind,
								boatSpeed,
								seaStatus,
								boatSettings);
						Speed newTargetSpeed = speedPolar.getTargetSpeed(trueWind,seaStatus);
						targetVmg = speedPolar.getTargetVMG(trueWind, seaStatus);
					}
					// signal that a new target speed is available
					onNewTargetSpeed();
				}
				if (speedPolar.getActualVMG().getValue() > targetVmg.getSpeed().getValue()) {
					if (automaticSpeedValidation) {
						speedPolar.setNewTargetSpeed(trueWind,
								boatSpeed,
								seaStatus,
								boatSettings);
						speedPolar.getTargetSpeed(trueWind, seaStatus);
						targetVmg = speedPolar.getTargetVMG(trueWind, seaStatus);
						try {
							speedPolar.save();
						} catch (Exception e) {
							Log.severe(Activator.getBundle(), "error saving " + speedPolar.getFileName(), e);
						}
					}
					// signal that a new targetVmg is found
					onNewVMGSpeed();
				}
			}
		}
	}

	public void setRelativeWindValues(Wind w) {
		meanWindDirection.setValue(w.getDirection().getValue());
		meanWindSpeed.setValue(w.getSpeed().getValue());
		relativeWind.set(meanWindDirection.getValue(), meanWindSpeed.getValue());
		relativeWind.setValid(w.isValid());
	}


	/**
	 * @param s the new speed to set
	 */
	public void copyBoatSpeedValue(Speed s) {
		boatSpeed.setValue(s.value);
		boatSpeed.setValid(s.isValid());
		speedStabilisator.set(s.value);
	}

	/**
	 * @param meaning :
	 *            the meaning of the new VMG speed reached
	 */
	private void onNewVMGSpeed() {
		if (onNewVMGAction != null) {
			onNewVMGAction.perform(this);
		}

	}

	/**
	 * @param meaning
	 *            the new target speed reached
	 */
	private void onNewTargetSpeed() {
		if (onNewTargetSpeedAction != null) {
			onNewTargetSpeedAction.perform(this);
		}
	}

	/**
	 * @return speedStabilisator : the class that detect if the boat speed is
	 *         stabilised (can be used as reference)
	 */
	public Stabilisator getSpeedStabilisator() {
		return speedStabilisator;
	}

	/**
	 * @return the automaticSpeedValidation
	 */
	public boolean isAutomaticSpeedValidation() {
		return automaticSpeedValidation;
	}

	/**
	 * @param b
	 *            the automaticSpeedValidation to set
	 */
	public void setAutomaticSpeedValidation(boolean b) {
		this.automaticSpeedValidation = b;
	}

	/**
	 * @return the onNewTargetSpeedAction
	 */
	public I_EventSpeedComputation getOnNewTargetSpeedAction() {
		return onNewTargetSpeedAction;
	}

	/**
	 * @param onNewTargetSpeedAction
	 *            the onNewTargetSpeedAction to set
	 */
	public void setOnNewTargetSpeedAction(
			I_EventSpeedComputation onNewTargetSpeedAction) {
		this.onNewTargetSpeedAction = onNewTargetSpeedAction;
	}

	/**
	 * @return the onNewVMGAction
	 */
	public I_EventSpeedComputation getOnNewVMGAction() {
		return onNewVMGAction;
	}

	/**
	 * @param onNewVMGAction
	 *            the onNewVMGAction to set
	 */
	public void setOnNewVMGAction(I_EventSpeedComputation onNewVMGAction) {
		this.onNewVMGAction = onNewVMGAction;
	}

	/**
	 * define the way to compute the geographic wind (to be displayed on the
	 * map) geoWinCompMode can be:<br>
	 * NO_BEARING : no computation can be done: geographic wind must be set invalid<br>
	 * TRUE_BEARING : True bearing is valid: best choice<br>
	 * MAGNETIC_BEARING: when TRUE is invalid. but does not take into account the
	 * deviation.
	 * GPS_BEARING: the bearing is given by the GPS. but does not take
	 * into account the derive and deviation.
	 *
	 * @param heading
	 * @param geoWinCompMode
	 */
	public void setBoatHeading(Angle heading, Angle.AngleOrigin boatBearingSource) {
		boatBearing = heading;
		this.boatBearingSource = boatBearingSource;
	}

	/**
	 * @return the geographicWindDirection
	 */
	public Angle getGeographicWindDirection() {
		return geographicWindDirection;
	}

	/**
	 * @return the targetPortVmg
	 */
	public EuclideanVector getPortVmg() {
		return targetPortVmg;
	}

	/**
	 * @return the targetStarboardVmg
	 */
	public EuclideanVector getStarboardVmg() {
		return targetStarboardVmg;
	}

	/**
	 * @return the boatBearing
	 */
	public Angle getBoatBearing() {
		Angle res;
		if (boatBearing != null) {
			res = boatBearing;
		} else {
			res = new Angle(0);
			res.setValid(false);
		}
		return res;
	}

	/**
	 * set the target speed associated to the current conditions (wind, sea) to
	 * the actual boat speed. no parameter.
	 */
	public void forceTargetSpeedToActualBoatSpeed() {
		if (speedPolar != null){
			speedPolar.setNewTargetSpeed(trueWind,
					boatSpeed,
					seaStatus,
					boatSettings);
		}
	}

	/**
	 * @return the boatBearingSource
	 */
	public Angle.AngleOrigin getBoatBearingSource() {
		return this.boatBearingSource;
	}

	/**
	 * @param boatBearingSource the boatBearingSource to set
	 */
	public void setBoatBearingSource(Angle.AngleOrigin boatBearingSource) {
		this.boatBearingSource = boatBearingSource;
	}

	/**
	 * @return the targetSpeed
	 */
	public Speed getTargetSpeed() {
		return this.targetSpeed;
	}

	/**
	 * @return the speedPolar
	 */
	public SpeedPolar getSpeedPolar() {
		return this.speedPolar;
	}

	/**
	 * set the speed polar to this wind and speed processor.
	 * without this, speeds cannot be computed.
	 * @param sp
	 */
	public void setSpeedPolar(SpeedPolar sp){
		this.speedPolar = sp;
	}

	/**
	 * @return the actualVMG
	 */
	public Speed getActualVMG() {
		return actualVMG;
	}

	/**
	 * @return the meanWindSpeed
	 */
	public MeanValue getMeanWindSpeed() {
		return this.meanWindSpeed;
	}

	/**
	 * @return the meanWindDirection
	 */
	public MeanValue getMeanWindDirection() {
		return this.meanWindDirection;
	}

	public void setTrack(Angle trueBearing) {
		if (speedPolar != null){
			speedPolar.setTrack(trueBearing);
		}
	}
}
