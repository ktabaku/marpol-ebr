package com.oceanpro.core.entity.oceanManager.logbook.operationLog;
public enum TankType {
	
	BALLAST("Ballast"),
	CARGO("Cargo"),
	SLOP("Slop"),
	FUELOIL("FuelOil"),
	OILYBILGE("OilyBilge"),
	SLUDGE("Sludge");

	private String name;
	
	private TankType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}