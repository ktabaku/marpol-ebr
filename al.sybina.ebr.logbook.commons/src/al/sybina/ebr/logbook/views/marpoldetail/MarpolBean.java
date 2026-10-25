package al.sybina.ebr.logbook.views.marpoldetail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;

import com.oceanpro.core.entity.oceanManager.logbook.operationLog.MarpleLogSettings;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.Tank;
import com.oceanpro.core.services.OperationLogService;

@Creatable
@Singleton
public class MarpolBean {

	private static final Logger logger = LoggerCreator.createLogger(MarpolBean.class);
	
	// @Autowired
	// private JasperReportsFactory jasperReportsFactory;
	@Inject
	private OperationLogService operationLogService;

	private MarpleLogSettings settings;

	@PostConstruct
	public void init() {
		List<MarpleLogSettings> li = operationLogService.getAll();
		if (li != null && li.size() > 0) {
			settings = li.get(0);
		} else {
			settings = new MarpleLogSettings();
		}
	}

	public void addCargoTanks() {
		if (settings.getCargoTanks() == null)
			settings.setCargoTanks(new ArrayList<Tank>());
		Tank a1 = new Tank();
		a1.setSizeMeasureUnit("cm");
		a1.setMarpleLogSettingsCargo(settings);
		settings.getCargoTanks().add(a1);
		Tank a2 = new Tank();
		a2.setSizeMeasureUnit("cm");
		a2.setMarpleLogSettingsCargo(settings);
		settings.getCargoTanks().add(a2);
		Tank a3 = new Tank();
		a3.setSizeMeasureUnit("cm");
		a3.setMarpleLogSettingsCargo(settings);
		settings.getCargoTanks().add(a3);
	}

	public void removeCargoTanks(int row) {
		int start = row - 2;
		settings.getCargoTanks().remove(start);
		settings.getCargoTanks().remove(start);
		settings.getCargoTanks().remove(start);
	}

	public void addSlopTanks() {
		if (settings.getSlopTanks() == null)
			settings.setSlopTanks(new ArrayList<Tank>());
		Tank a1 = new Tank();
		a1.setSizeMeasureUnit("cm");
		a1.setMarpleLogSettingsSlop(settings);
		settings.getSlopTanks().add(a1);
		Tank a2 = new Tank();
		a2.setSizeMeasureUnit("cm");
		a2.setMarpleLogSettingsSlop(settings);
		settings.getSlopTanks().add(a2);
		Tank a3 = new Tank();
		a3.setSizeMeasureUnit("cm");
		a3.setMarpleLogSettingsSlop(settings);
		settings.getSlopTanks().add(a3);
	}

	public void removeSlopTanks(int row) {
		int start = row - 2;
		settings.getSlopTanks().remove(start);
		settings.getSlopTanks().remove(start);
		settings.getSlopTanks().remove(start);
	}

	public void addFuelTanks() {
		if (settings.getFuelOilTanks() == null)
			settings.setFuelOilTanks(new ArrayList<Tank>());
		Tank a1 = new Tank();
		a1.setSizeMeasureUnit("cm");
		a1.setMarpleLogSettingsFuel(settings);
		settings.getFuelOilTanks().add(a1);
		Tank a2 = new Tank();
		a2.setSizeMeasureUnit("cm");
		a2.setMarpleLogSettingsFuel(settings);
		settings.getFuelOilTanks().add(a2);
	}

	public void removeFuelTanks(int row) {
		int start = row - 1;
		settings.getFuelOilTanks().remove(start);
		settings.getFuelOilTanks().remove(start);
	}

	public void addMgoTanks() {
		if (settings.getMgoTanks() == null)
			settings.setMgoTanks(new ArrayList<Tank>());
		Tank a1 = new Tank();
		a1.setSizeMeasureUnit("cm");
		a1.setMarpleLogSettingsMgo(settings);
		settings.getMgoTanks().add(a1);
		Tank a2 = new Tank();
		a2.setSizeMeasureUnit("cm");
		a2.setMarpleLogSettingsMgo(settings);
		settings.getMgoTanks().add(a2);
	}

	public void removeMgoTanks(int row) {
		int start = row - 1;
		settings.getMgoTanks().remove(start);
		settings.getMgoTanks().remove(start);
	}

	public void addBilgeTanks() {
		if (settings.getOilyBilgeTanks() == null)
			settings.setOilyBilgeTanks(new ArrayList<Tank>());
		Tank a1 = new Tank();
		a1.setSizeMeasureUnit("cm");
		a1.setMarpleLogSettingsBilge(settings);
		settings.getOilyBilgeTanks().add(a1);
	}

	public void removeBilgeTanks(int row) {
		int start = row - 0;
		settings.getOilyBilgeTanks().remove(start);
		settings.getOilyBilgeTanks().remove(start);
	}

	public void addSludgeTanks() {
		if (settings.getSludgeTanks() == null)
			settings.setSludgeTanks(new ArrayList<Tank>());
		Tank a1 = new Tank();
		a1.setSizeMeasureUnit("cm");
		a1.setMarpleLogSettingsSludge(settings);
		settings.getSludgeTanks().add(a1);
	}

	public void removeSludgeTanks(int row) {
		int start = row - 0;
		settings.getSludgeTanks().remove(start);
		settings.getSludgeTanks().remove(start);
	}

	public void addBallastTanks() {
		if (settings.getBallastTanks() == null)
			settings.setBallastTanks(new ArrayList<Tank>());
		Tank a1 = new Tank();
		a1.setSizeMeasureUnit("cm");
		a1.setMarpleLogSettingsBallast(settings);
		settings.getBallastTanks().add(a1);
		Tank a2 = new Tank();
		a2.setSizeMeasureUnit("cm");
		a2.setMarpleLogSettingsBallast(settings);
		settings.getBallastTanks().add(a2);
	}

	public void removeBallastTanks(int row) {
		int start = row - 1;
		settings.getBallastTanks().remove(start);
		settings.getBallastTanks().remove(start);
	}
	
	// VOLUMES

	public Double getTotalCargoVolume() {
		if (settings.getCargoTanks() != null && settings.getCargoTanks().size() > 0) {
			double tot = 0.0;
			for (Tank t : settings.getCargoTanks()) {
				if (t.getSize() != null)
					tot += t.getSize();
			}
			return tot;
		} else
			return 0.0;
	}

	public Double getTotalSlopVolume() {
		if (settings.getSlopTanks() != null && settings.getSlopTanks().size() > 0) {
			double tot = 0.0;
			for (Tank t : settings.getSlopTanks()) {
				if (t.getSize() != null)
					tot += t.getSize();
			}
			return tot;
		} else
			return 0.0;
	}

	public Double getTotalFuelVolume() {
		if (settings.getFuelOilTanks() != null && settings.getFuelOilTanks().size() > 0) {
			double tot = 0.0;
			for (Tank t : settings.getFuelOilTanks()) {
				if (t.getSize() != null)
					tot += t.getSize();
			}
			return tot;
		} else
			return 0.0;
	}

	public Double getTotalMgoVolume() {
		if (settings.getMgoTanks() != null && settings.getMgoTanks().size() > 0) {
			double tot = 0.0;
			for (Tank t : settings.getMgoTanks()) {
				if (t.getSize() != null)
					tot += t.getSize();
			}
			return tot;
		} else
			return 0.0;
	}

	public Double getTotalBilgeVolume() {
		if (settings.getOilyBilgeTanks() != null && settings.getOilyBilgeTanks().size() > 0) {
			double tot = 0.0;
			for (Tank t : settings.getOilyBilgeTanks()) {
				if (t.getSize() != null)
					tot += t.getSize();
			}
			return tot;
		} else
			return 0.0;
	}

	public Double getTotalSludgeVolume() {
		if (settings.getSludgeTanks() != null && settings.getSludgeTanks().size() > 0) {
			double tot = 0.0;
			for (Tank t : settings.getSludgeTanks()) {
				if (t.getSize() != null)
					tot += t.getSize();
			}
			return tot;
		} else
			return 0.0;
	}

	public Double getTotalBallastVolume() {
		if (settings.getBallastTanks() != null && settings.getBallastTanks().size() > 0) {
			double tot = 0.0;
			for (Tank t : settings.getBallastTanks()) {
				if (t.getSize() != null)
					tot += t.getSize();
			}
			return tot;
		} else
			return 0.0;
	}
	
	public void saveSettings(Collection<String> addedTankNames, Collection<String> updatedTankNames, Collection<String> deletedTankNames) {
		logger.info("saveSettings");

		if (settings != null) {

			// Totals
			settings.setTotalSizeBilgeTanks(getTotalBilgeVolume());
			settings.setTotalSizeCargoTanks(getTotalCargoVolume());
			settings.setTotalSizeFuelOilTanks(getTotalFuelVolume());
			settings.setTotalSizeMgoTanks(getTotalMgoVolume());
			settings.setTotalSizeSlopTanks(getTotalSlopVolume());
			settings.setTotalSizeSludgeTanks(getTotalSludgeVolume());
			settings.setTotalSizeBallastTanks(getTotalBallastVolume());

			settings = operationLogService.update(settings, addedTankNames, updatedTankNames, deletedTankNames);

			logger.info("Settings updated!");
			// Invio a ufficio
			// sendToOffice(selectedMedicine);
		} else {
			logger.error("Error saving settings");
		}

	}

	public MarpleLogSettings getSettings() {
		return settings;
	}

	public void setSettings(MarpleLogSettings settings) {
		this.settings = settings;
	}
}
