package com.oceanpro.core.entity.oceanManager.logbook.operationLog;

public enum TankPosition {
	
	PORTSIDE("Port Side"),
	CENTER("Center"),
	CENTER1("Center1"),
	CENTER2("Center2"),
	SBDSIDE("Sbd Side"),
	FORE_PEAK("Fore Peak"),
	AFT_PEAK("Aft Peak");

	private String name;
	
	private TankPosition(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
