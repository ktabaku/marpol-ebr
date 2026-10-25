package al.sybina.ebr.logbook.dialogs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import com.oceanpro.core.entity.oceanManager.logbook.operationLog.Tank;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.TankType;

public class TankUsageContext {

	private boolean noNeedForUserTankQuantities;

	private List<TankType> tankTypes;
	private boolean addition;
	private boolean multipleTanks;
	private boolean c11_1;
	private boolean c11_4;
	private Double maxC11_4=0.0;
	private Map<Tank, Double> userTankQuantities;
	
	public TankUsageContext() {
		userTankQuantities = new HashMap<Tank, Double>();
	}
	
	public TankUsageContext(List<TankType> list, boolean addition, boolean multipleTanks) {
		userTankQuantities = new HashMap<Tank, Double>();
		this.tankTypes = list;
		this.addition = addition;
		this.multipleTanks = multipleTanks;
	}


	public List<TankType> getTankTypes() {
		return tankTypes;
	}

	public void setTankTypes(List<TankType> tankTypes) {
		this.tankTypes = tankTypes;
	}

	public boolean isAddition() {
		return addition;
	}

	public void setAddition(boolean addition) {
		this.addition = addition;
	}

	public boolean isMultipleTanks() {
		return multipleTanks;
	}
	public void setMultipleTanks(boolean multipleTanks) {
		this.multipleTanks = multipleTanks;
	}

	public Map<Tank, Double> getUserTankQuantities() {
		return userTankQuantities;
	}
	public void setUserTankQuantities(Map<Tank, Double> userTankQuantities) {
		this.userTankQuantities = userTankQuantities;
	}
	public Map<Tank, Double> getUserTankActualLevels() {
		Map<Tank, Double> userTankActualLevels = new HashMap<Tank, Double>();
		for(Entry<Tank, Double> entry : userTankQuantities.entrySet()) {
			if(addition) {
				userTankActualLevels.put(entry.getKey(), Optional.ofNullable(entry.getKey().getActualLevel()).orElse(0.0) + Optional.ofNullable(entry.getValue()).orElse(0.0));
			} else {
				userTankActualLevels.put(entry.getKey(), Optional.ofNullable(entry.getKey().getActualLevel()).orElse(0.0) - Optional.ofNullable(entry.getValue()).orElse(0.0));
			}
		}
		return userTankActualLevels;
	}

	public Double getTotalUserTankQuantities() {
		Double total = userTankQuantities.values().stream().mapToDouble(i -> i).sum();
		return Math.round(total * 1e4) / 1e4;
	}
	public Double getTotalUserTankCapacity() {
		Double total = userTankQuantities.keySet().stream().mapToDouble(t -> Optional.ofNullable(t.getSize()).orElse(0.0)).sum();
		return Math.round(total * 1e4) / 1e4;
	}
	public Double getTotalUserTankActualLevel() {
		Double total = userTankQuantities.keySet().stream().mapToDouble(t -> Optional.ofNullable(t.getActualLevel()).orElse(0.0)).sum();
		return Math.round(total * 1e4) / 1e4;
	}
	public boolean isNoNeedForUserTankQuantities() {
		return noNeedForUserTankQuantities;
	}

	public void setNoNeedForUserTankQuantities(boolean noNeedForUserTankQuantities) {
		this.noNeedForUserTankQuantities = noNeedForUserTankQuantities;
	}
	public boolean isC11_1() {
		return c11_1;
	}

	public void setC11_1(boolean c11_1) {
		this.c11_1 = c11_1;
	}

	public boolean isC11_4() {
		return c11_4;
	}

	public void setC11_4(boolean c11_4) {
		this.c11_4 = c11_4;
	}

	public Double getMaxC11_4() {
		return maxC11_4;
	}

	public void setMaxC11_4(Double maxC11_4) {
		this.maxC11_4 = maxC11_4;
	}


}
