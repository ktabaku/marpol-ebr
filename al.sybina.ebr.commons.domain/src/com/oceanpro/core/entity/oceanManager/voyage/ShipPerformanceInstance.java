package com.oceanpro.core.entity.oceanManager.voyage;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * Class name: ShipPerformanceInstance
 *
 * Description: 
 * 
 *
 * Company: 
 *
 * @author Gio
 * @date 05/feb/2013
 *
 */
@Entity
@javax.persistence.Table(name="ocean_voyage_settings_ship_performance_instance")
public class ShipPerformanceInstance implements Comparable<ShipPerformanceInstance>{


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long id ; // Unique ID
	
	@Basic
	private String uuid;

	@Basic
	private Date registrationDate;
	@Basic
	private String voyageModeName;
	@Basic
	private Double enginePower=0.0;
	@Basic
	private Double rpm=0.0;
	@Basic
	private Double warrantedShipSpeed=0.0;
	@Basic
	private Double warrantedFoConsumption=0.0;
	
	@ManyToOne(fetch=FetchType.EAGER, optional=true )
	private ShipPerformanceMatrix shipPerformanceMatrix;
	
	@ManyToOne (fetch = FetchType.LAZY, optional= false) // No cascade
	private VoyageSettings voyageSettings;
	
	/**
	 * Costruttore vuoto
	 */
	public ShipPerformanceInstance() {
		super();
	}
	

	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	
	@Override
	public boolean equals(Object obj) {
		ShipPerformanceInstance rhs = (ShipPerformanceInstance) obj;
		if (uuid!=null && rhs.uuid!=null) return uuid.equals(rhs.uuid);
		boolean b = uuid==rhs.uuid;
		//System.out.println("id1=" + id + " id2=" + rhs.id +  " b="+b);
		return b;
		/*
		return new EqualsBuilder()
        //.appendSuper(super.equals(obj))
        .append(id, rhs.id)
        .append(name, rhs.name)
        .append(description, rhs.description)
        .isEquals();
		*/
		//return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}


	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ShipPerformanceInstance comp) {
		 if (getRegistrationDate()!=null && comp.getRegistrationDate()!=null && getRegistrationDate().before(comp.getRegistrationDate())) return -1; 
		 else if (getRegistrationDate()!=null && comp.getRegistrationDate()!=null && getRegistrationDate().equals(comp.getRegistrationDate())) return 0;
		 else return 1;
	}


	public Double getEnginePower() {
		return enginePower;
	}

	public void setEnginePower(Double enginePower) {
		this.enginePower = enginePower;
	}
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}



	public VoyageSettings getVoyageSettings() {
		return voyageSettings;
	}



	public void setVoyageSettings(VoyageSettings voyageSettings) {
		this.voyageSettings = voyageSettings;
	}



	public Double getRpm() {
		return rpm;
	}



	public void setRpm(Double rpm) {
		this.rpm = rpm;
	}



	public Double getWarrantedShipSpeed() {
		return warrantedShipSpeed;
	}



	public void setWarrantedShipSpeed(Double warrantedShipSpeed) {
		this.warrantedShipSpeed = warrantedShipSpeed;
	}



	public Double getWarrantedFoConsumption() {
		return warrantedFoConsumption;
	}



	public void setWarrantedFoConsumption(Double warrantedFoConsumption) {
		this.warrantedFoConsumption = warrantedFoConsumption;
	}



	public ShipPerformanceMatrix getShipPerformanceMatrix() {
		return shipPerformanceMatrix;
	}



	public void setShipPerformanceMatrix(ShipPerformanceMatrix shipPerformanceMatrix) {
		this.shipPerformanceMatrix = shipPerformanceMatrix;
	}



	public String getVoyageModeName() {
		return voyageModeName;
	}



	public void setVoyageModeName(String voyageModeName) {
		this.voyageModeName = voyageModeName;
	}


}