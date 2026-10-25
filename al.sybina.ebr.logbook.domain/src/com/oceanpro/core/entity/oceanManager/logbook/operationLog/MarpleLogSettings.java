package com.oceanpro.core.entity.oceanManager.logbook.operationLog;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.oceanpro.core.entity.oceanManager.office.Ship;

/**
 * 
 * @author Gio
 *
 */
@Entity
@Table(name="ocean_logbook_operation_marple_settings")
public class MarpleLogSettings {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(nullable = false)
	private Long id ; // Unique ID
	
	@Basic
	private String uuid;
	
	/***** For office only *************/
	@ManyToOne (fetch = FetchType.LAZY, optional=true, cascade = CascadeType.PERSIST) // No cascade
    private Ship ship;
	/************************************/
	
	// Cargo Tanks
	@OneToMany(targetEntity=Tank.class, cascade={CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.REMOVE}, orphanRemoval=true, mappedBy="marpleLogSettingsCargo", fetch=FetchType.LAZY)
	private List<Tank> cargoTanks;
	@Basic
	private Double totalSizeCargoTanks;
	
	// Slop Tank
	@OneToMany(targetEntity=Tank.class, cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="marpleLogSettingsSlop", fetch=FetchType.LAZY)
	private List<Tank> slopTanks;
	@Basic
	private Double totalSizeSlopTanks;
	
	
	// Fuel Oil Tanks
	@OneToMany(targetEntity=Tank.class, cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="marpleLogSettingsFuel", fetch=FetchType.LAZY)
	private List<Tank> fuelOilTanks;
	@Basic
	private Double totalSizeFuelOilTanks;
	
	
	// MGO Tanks
	@OneToMany(targetEntity=Tank.class, cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="marpleLogSettingsMgo", fetch=FetchType.LAZY)
	private List<Tank> mgoTanks;
	@Basic
	private Double totalSizeMgoTanks;
	
	
	// Oily Bilge
	@OneToMany(targetEntity=Tank.class, cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="marpleLogSettingsBilge", fetch=FetchType.LAZY)
	private List<Tank> oilyBilgeTanks;
	@Basic
	private Double totalSizeBilgeTanks;
	
	
	// Sludge Tanks
	@OneToMany(targetEntity=Tank.class, cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="marpleLogSettingsSludge", fetch=FetchType.LAZY)
	private List<Tank> sludgeTanks;
	@Basic
	private Double totalSizeSludgeTanks;
	
	// MGO Tanks
	@OneToMany(targetEntity=Tank.class, cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="marpleLogSettingsBallast", fetch=FetchType.LAZY)
	private List<Tank> ballastTanks;
	@Basic
	private Double totalSizeBallastTanks;
	
	@Column
	private boolean incinerator;
	@Basic
	private Double incineratorCapacity;
	
	@Column
	private boolean auxBoiler;
	
	@Column
	private boolean mixingOil;
	@Basic
	private Double mixingOilCapacity;
	@Basic
	private Double bilgeWaterSeparatorThroughput;
	@Column
	private boolean otherMeansExist;
	@Basic
	private String otherMeans;
	
	
	
	@Override
	public boolean equals(Object obj) {
		MarpleLogSettings rhs = (MarpleLogSettings) obj;
		if (getUuid()!=null && rhs.getUuid()!=null) return getUuid().equals(rhs.getUuid());
		boolean b = getUuid()==rhs.getUuid();
		//System.out.println("id1=" + id + " id2=" + rhs.id +  " b="+b);
		return b;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public boolean isIncinerator() {
		return incinerator;
	}

	public void setIncinerator(boolean incinerator) {
		this.incinerator = incinerator;
	}

	public Double getIncineratorCapacity() {
		return incineratorCapacity;
	}

	public void setIncineratorCapacity(Double incineratorCapacity) {
		this.incineratorCapacity = incineratorCapacity;
	}

	public boolean isAuxBoiler() {
		return auxBoiler;
	}

	public void setAuxBoiler(boolean auxBoiler) {
		this.auxBoiler = auxBoiler;
	}

	public boolean isMixingOil() {
		return mixingOil;
	}

	public void setMixingOil(boolean mixingOil) {
		this.mixingOil = mixingOil;
	}

	public Double getMixingOilCapacity() {
		return mixingOilCapacity;
	}

	public void setMixingOilCapacity(Double mixingOilCapacity) {
		this.mixingOilCapacity = mixingOilCapacity;
	}

	public Double getBilgeWaterSeparatorThroughput() {
		return bilgeWaterSeparatorThroughput;
	}

	public void setBilgeWaterSeparatorThroughput(Double bilgeWaterSeparatorThroughput) {
		this.bilgeWaterSeparatorThroughput = bilgeWaterSeparatorThroughput;
	}

	public String getOtherMeans() {
		return otherMeans;
	}

	public void setOtherMeans(String otherMeans) {
		this.otherMeans = otherMeans;
	}

	public boolean isOtherMeansExist() {
		return otherMeansExist;
	}

	public void setOtherMeansExist(boolean otherMeansExist) {
		this.otherMeansExist = otherMeansExist;
	}

	public List<Tank> getCargoTanks() {
		return cargoTanks;
	}

	public void setCargoTanks(List<Tank> cargoTanks) {
		this.cargoTanks = cargoTanks;
	}

	public List<Tank> getSlopTanks() {
		return slopTanks;
	}

	public void setSlopTanks(List<Tank> slopTanks) {
		this.slopTanks = slopTanks;
	}

	public List<Tank> getFuelOilTanks() {
		return fuelOilTanks;
	}

	public void setFuelOilTanks(List<Tank> fuelOilTanks) {
		this.fuelOilTanks = fuelOilTanks;
	}

	public List<Tank> getMgoTanks() {
		return mgoTanks;
	}

	public void setMgoTanks(List<Tank> mgoTanks) {
		this.mgoTanks = mgoTanks;
	}

	public List<Tank> getOilyBilgeTanks() {
		return oilyBilgeTanks;
	}

	public void setOilyBilgeTanks(List<Tank> oilyBilgeTanks) {
		this.oilyBilgeTanks = oilyBilgeTanks;
	}

	public List<Tank> getSludgeTanks() {
		return sludgeTanks;
	}

	public void setSludgeTanks(List<Tank> sludgeTanks) {
		this.sludgeTanks = sludgeTanks;
	}

	public Double getTotalSizeCargoTanks() {
		return totalSizeCargoTanks;
	}

	public void setTotalSizeCargoTanks(Double totalSizeCargoTanks) {
		this.totalSizeCargoTanks = totalSizeCargoTanks;
	}

	public Double getTotalSizeSlopTanks() {
		return totalSizeSlopTanks;
	}

	public void setTotalSizeSlopTanks(Double totalSizeSlopTanks) {
		this.totalSizeSlopTanks = totalSizeSlopTanks;
	}

	public Double getTotalSizeFuelOilTanks() {
		return totalSizeFuelOilTanks;
	}

	public void setTotalSizeFuelOilTanks(Double totalSizeFuelOilTanks) {
		this.totalSizeFuelOilTanks = totalSizeFuelOilTanks;
	}

	public Double getTotalSizeMgoTanks() {
		return totalSizeMgoTanks;
	}

	public void setTotalSizeMgoTanks(Double totalSizeMgoTanks) {
		this.totalSizeMgoTanks = totalSizeMgoTanks;
	}

	public Double getTotalSizeBilgeTanks() {
		return totalSizeBilgeTanks;
	}

	public void setTotalSizeBilgeTanks(Double totalSizeBilgeTanks) {
		this.totalSizeBilgeTanks = totalSizeBilgeTanks;
	}

	public Double getTotalSizeSludgeTanks() {
		return totalSizeSludgeTanks;
	}

	public void setTotalSizeSludgeTanks(Double totalSizeSludgeTanks) {
		this.totalSizeSludgeTanks = totalSizeSludgeTanks;
	}

	public Ship getShip() {
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}

	public List<Tank> getBallastTanks() {
		return ballastTanks;
	}

	public void setBallastTanks(List<Tank> ballastTanks) {
		this.ballastTanks = ballastTanks;
	}

	public Double getTotalSizeBallastTanks() {
		return totalSizeBallastTanks;
	}

	public void setTotalSizeBallastTanks(Double totalSizeBallastTanks) {
		this.totalSizeBallastTanks = totalSizeBallastTanks;
	}
	

}
