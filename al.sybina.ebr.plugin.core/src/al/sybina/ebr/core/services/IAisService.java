package al.sybina.ebr.core.services;



import al.sybina.ebr.core.services.vessels.IVessel;
import javafx.scene.image.Image;


public interface IAisService extends IIbrCoreService{
	Image getVesselImage(int station, int tp);

	/**
	 * set the selected Vessel.
	 * @param vessel
	 */
	void setSelectedVessel(IVessel vessel);
}
