package com.oceanpro.core.entity.oceanManager.logbook.operationLog;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * @author Gio
 *
 */
@Entity
@Table(name="ocean_logbook_operation_tank")
public class Tank {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(nullable = false)
	private Long id ; // Unique ID
	
	@Basic
	private String uuid;
	
	// Cargo Tanks
	@Basic
	private String name;
	@Basic
	private Double size;
	@Basic
	private String sizeMeasureUnit;
	@Basic
	private Double actualLevel;
	@Basic
	private TankPosition tankPosition;
	
	@ManyToOne (fetch = FetchType.EAGER) // No cascade
	private MarpleLogSettings marpleLogSettingsCargo;
	
	@ManyToOne (fetch = FetchType.EAGER) // No cascade
	private MarpleLogSettings marpleLogSettingsSlop;
	
	@ManyToOne (fetch = FetchType.EAGER) // No cascade
	private MarpleLogSettings marpleLogSettingsFuel;
	
	@ManyToOne (fetch = FetchType.EAGER) // No cascade
	private MarpleLogSettings marpleLogSettingsMgo;
	
	@ManyToOne (fetch = FetchType.EAGER) // No cascade
	private MarpleLogSettings marpleLogSettingsBilge;
	
	@ManyToOne (fetch = FetchType.EAGER) // No cascade
	private MarpleLogSettings marpleLogSettingsSludge;
	
	@ManyToOne (fetch = FetchType.EAGER) // No cascade
	private MarpleLogSettings marpleLogSettingsBallast;
	
//	@Override
//	public boolean equals(Object obj) {
//		Tank rhs = (Tank) obj;
//		if (getUuid()!=null && rhs.getUuid()!=null) return getUuid().equals(rhs.getUuid());
//		boolean b = getUuid()==rhs.getUuid();
//		//System.out.println("id1=" + id + " id2=" + rhs.id +  " b="+b);
//		return b;
//	}
	

//	@Override
//	public int hashCode() {
//		return HashCodeBuilder.reflectionHashCode(this);
//	}
	
	
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	public int changeTrackHashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actualLevel == null) ? 0 : actualLevel.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((size == null) ? 0 : size.hashCode());
		result = prime * result + ((sizeMeasureUnit == null) ? 0 : sizeMeasureUnit.hashCode());
		result = prime * result + ((tankPosition == null) ? 0 : tankPosition.hashCode());
		return result;
	}
	//Do not modify hashCode or equals because Tank is used as Key of Map which does not handle mutable keys.
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	//Do not modify hashCode or equals because Tank is used as Key of Map which does not handle mutable keys.
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tank other = (Tank) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getSize() {
		return size;
	}

	public void setSize(Double size) {
		this.size = size;
	}

	public String getSizeMeasureUnit() {
		return sizeMeasureUnit;
	}

	public void setSizeMeasureUnit(String sizeMeasureUnit) {
		this.sizeMeasureUnit = sizeMeasureUnit;
	}

	public MarpleLogSettings getMarpleLogSettingsCargo() {
		return marpleLogSettingsCargo;
	}

	public void setMarpleLogSettingsCargo(MarpleLogSettings marpleLogSettingsCargo) {
		this.marpleLogSettingsCargo = marpleLogSettingsCargo;
	}

	public MarpleLogSettings getMarpleLogSettingsSlop() {
		return marpleLogSettingsSlop;
	}

	public void setMarpleLogSettingsSlop(MarpleLogSettings marpleLogSettingsSlop) {
		this.marpleLogSettingsSlop = marpleLogSettingsSlop;
	}

	public MarpleLogSettings getMarpleLogSettingsFuel() {
		return marpleLogSettingsFuel;
	}

	public void setMarpleLogSettingsFuel(MarpleLogSettings marpleLogSettingsFuel) {
		this.marpleLogSettingsFuel = marpleLogSettingsFuel;
	}

	public MarpleLogSettings getMarpleLogSettingsMgo() {
		return marpleLogSettingsMgo;
	}

	public void setMarpleLogSettingsMgo(MarpleLogSettings marpleLogSettingsMgo) {
		this.marpleLogSettingsMgo = marpleLogSettingsMgo;
	}

	public MarpleLogSettings getMarpleLogSettingsBilge() {
		return marpleLogSettingsBilge;
	}

	public void setMarpleLogSettingsBilge(MarpleLogSettings marpleLogSettingsBilge) {
		this.marpleLogSettingsBilge = marpleLogSettingsBilge;
	}

	public MarpleLogSettings getMarpleLogSettingsSludge() {
		return marpleLogSettingsSludge;
	}

	public void setMarpleLogSettingsSludge(MarpleLogSettings marpleLogSettingsSludge) {
		this.marpleLogSettingsSludge = marpleLogSettingsSludge;
	}

	public Double getActualLevel() {
		return actualLevel;
	}

	public void setActualLevel(Double actualLevel) {
		this.actualLevel = actualLevel;
	}

	public TankPosition getTankPosition() {
		return tankPosition;
	}

	public void setTankPosition(TankPosition tankPosition) {
		this.tankPosition = tankPosition;
	}

	public MarpleLogSettings getMarpleLogSettingsBallast() {
		return marpleLogSettingsBallast;
	}

	public void setMarpleLogSettingsBallast(MarpleLogSettings marpleLogSettingsBallast) {
		this.marpleLogSettingsBallast = marpleLogSettingsBallast;
	}
	
	public TankType getType() {
		if(marpleLogSettingsCargo!=null && marpleLogSettingsCargo.getCargoTanks().contains(this))
			return TankType.CARGO;
		else if(marpleLogSettingsBallast!=null && marpleLogSettingsBallast.getBallastTanks().contains(this))
			return TankType.BALLAST;
		else if(marpleLogSettingsFuel!=null && marpleLogSettingsFuel.getFuelOilTanks().contains(this))
			return TankType.FUELOIL;
		else if(marpleLogSettingsSlop!=null && marpleLogSettingsSlop.getSlopTanks().contains(this))
			return TankType.SLOP;
		else if(marpleLogSettingsBilge!=null && marpleLogSettingsBilge.getOilyBilgeTanks().contains(this))
			return TankType.OILYBILGE;
		else if(marpleLogSettingsSludge!=null && marpleLogSettingsSludge.getSludgeTanks().contains(this))
			return TankType.SLUDGE;
		return null;
	}

}
