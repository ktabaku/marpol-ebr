/**
 * 
 */
package com.oceanpro.core.entity.oceanManager.office;

/**
 * Class name: ShipType
 *
 * Description:
 * 
 *
 * Company: CrismaItalia
 *
 * @author Crisma
 * @date 28/nov/2012
 *
 */
public enum ShipType {

	INDIAN_POINT("Indian Point"), GOLD_POINT("Gold Point"), ICE_POINT("Ice Point"), IRON_POINT("Iron Point"), SILVER_POINT("Silver Point"), GALATEA("Galatea"), NAVE_UFFICIO("Nave-Ufficio"), DEMO_TRAINING("demo_training"),
	BULK_CARRIER("Bulk Carrier"), 
	CHEMICAL_TANKER("Chemical Tanker"), 
	CONTAINER("Container"), 
	CRUDE_OIL_PRODUCT_TANKER("Crude Oil/Product Tanker"), 
	GAS_CARRIER("Gas Carrier"), 
	GENERAL_CARGO("General Cargo"), 
	OIL_CHEMICAL_TANKER("Oil/Chemical Tanker"), 
	PASSENGER("Passenger"),
	RO_RO_CARGO("Ro-Ro Cargo"), 
	RO_RO_PASSENGER("Ro-Ro Passenger");


	private String name;

	private ShipType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
