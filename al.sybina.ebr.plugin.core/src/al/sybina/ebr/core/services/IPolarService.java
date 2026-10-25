package al.sybina.ebr.core.services;

import al.sybina.ebr.core.data.Sea;
import al.sybina.ebr.core.data.Speed;
import al.sybina.ebr.core.data.SpeedPolar;
import al.sybina.ebr.core.data.Wind;

public interface IPolarService extends IIbrCoreService{

	public SpeedPolar getSpeedPolar(double windForce, Sea sea);
	public Speed getTargetSpeed(Wind trueWind, Sea sea);
	/**
	 * turns on / off the recording of new speeds in the polar.
	 */
	public void toggleRecording();

}
