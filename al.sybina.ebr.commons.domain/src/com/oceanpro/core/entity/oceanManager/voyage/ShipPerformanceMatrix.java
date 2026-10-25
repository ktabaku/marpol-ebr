package com.oceanpro.core.entity.oceanManager.voyage;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *  Dati performance della nave
 *  
 * Class name: ShipPerformanceMatrix
 *
 * Description: 
 * 
 *
 * Company: 
 *
 * @author Gio
 * @date 
 *
 */
@Entity
@javax.persistence.Table(name="ocean_voyage_ship_performance_matrix")
public class ShipPerformanceMatrix {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long id ; // Unique ID

	@Basic
	private String uuid;
	
	@Basic
	private String name;
	@Basic
	private Double speed;
	@Basic
	private Double enginePower=0.0;
	@Basic
	private Double rpm=0.0;
	@Basic
	private Double foConsumption=0.0;
	
	
	/**
	 * Costruttore vuoto
	 */
	public ShipPerformanceMatrix() {
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
		ShipPerformanceMatrix rhs = (ShipPerformanceMatrix) obj;
		if (uuid!=null && rhs.uuid!=null) return uuid.equals(rhs.uuid);
		boolean b = id==rhs.id;
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


	public Double getEnginePower() {
		return enginePower;
	}


	public void setEnginePower(Double enginePower) {
		this.enginePower = enginePower;
	}


	public Double getRpm() {
		return rpm;
	}


	public void setRpm(Double rpm) {
		this.rpm = rpm;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getUuid() {
		return uuid;
	}


	public void setUuid(String uuid) {
		this.uuid = uuid;
	}


	public Double getSpeed() {
		return speed;
	}


	public void setSpeed(Double speed) {
		this.speed = speed;
	}


	public Double getFoConsumption() {
		return foConsumption;
	}


	public void setFoConsumption(Double foConsumption) {
		this.foConsumption = foConsumption;
	}


}