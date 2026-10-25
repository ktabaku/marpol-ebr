package com.oceanpro.core.entity.oceanManager.logbook.operationLog;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
@Table(name="ocean_logbook_operation_garbage_log")
public class GarbageLog {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long id ; // Unique ID
	
	@Basic
	private String uuid;
	
	/***** For office only *************/
	@ManyToOne (fetch = FetchType.LAZY, optional=true) // No cascade
    private Ship ship;
	/************************************/
	
	@Basic
	private Date date; // Data
	
	@Basic
	private String portFacility;
	
	@Basic
	private String latitude1;
	@Basic
	private String latitude2;
	@Basic
	private String latitudeCardinalSign;
	
	@Basic
	private String longitude1;
	@Basic
	private String longitude2;
	@Basic
	private String longitudeCardinalSign;
	
	
	@Basic
	private String shipPositionMode;
	
	
	@OneToOne(fetch=FetchType.EAGER)
	private GarbageCategory category;
	
	@Basic
	private Double estimatedAmmountDischarged;
	@Basic
	private Double estimatedAmmountDischargedToSea;
	@Basic
	private String dischargedTo;
	
	@Basic
	private Double estimatedAmmountIncinerated;
	@Basic
	private Double incinerator;
	
	
	
	@Basic
	private String remarks;
	
	
	
	
	
	
	
	
	
	
	@Override
	public boolean equals(Object obj) {
		GarbageLog rhs = (GarbageLog) obj;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getPortFacility() {
		return portFacility;
	}

	public void setPortFacility(String portFacility) {
		this.portFacility = portFacility;
	}

	public String getLatitude1() {
		return latitude1;
	}

	public void setLatitude1(String latitude1) {
		this.latitude1 = latitude1;
	}

	public String getLatitude2() {
		return latitude2;
	}

	public void setLatitude2(String latitude2) {
		this.latitude2 = latitude2;
	}

	public String getLatitudeCardinalSign() {
		return latitudeCardinalSign;
	}

	public void setLatitudeCardinalSign(String latitudeCardinalSign) {
		this.latitudeCardinalSign = latitudeCardinalSign;
	}

	public String getLongitude1() {
		return longitude1;
	}

	public void setLongitude1(String longitude1) {
		this.longitude1 = longitude1;
	}

	public String getLongitude2() {
		return longitude2;
	}

	public void setLongitude2(String longitude2) {
		this.longitude2 = longitude2;
	}

	public String getLongitudeCardinalSign() {
		return longitudeCardinalSign;
	}

	public void setLongitudeCardinalSign(String longitudeCardinalSign) {
		this.longitudeCardinalSign = longitudeCardinalSign;
	}


	public GarbageCategory getCategory() {
		return category;
	}

	public void setCategory(GarbageCategory category) {
		this.category = category;
	}

	public Double getEstimatedAmmountDischarged() {
		return estimatedAmmountDischarged;
	}

	public void setEstimatedAmmountDischarged(Double estimatedAmmountDischarged) {
		this.estimatedAmmountDischarged = estimatedAmmountDischarged;
	}

	public Double getEstimatedAmmountDischargedToSea() {
		return estimatedAmmountDischargedToSea;
	}

	public void setEstimatedAmmountDischargedToSea(
			Double estimatedAmmountDischargedToSea) {
		this.estimatedAmmountDischargedToSea = estimatedAmmountDischargedToSea;
	}

	public Double getEstimatedAmmountIncinerated() {
		return estimatedAmmountIncinerated;
	}

	public void setEstimatedAmmountIncinerated(Double estimatedAmmountIncinerated) {
		this.estimatedAmmountIncinerated = estimatedAmmountIncinerated;
	}

	public Double getIncinerator() {
		return incinerator;
	}

	public void setIncinerator(Double incinerator) {
		this.incinerator = incinerator;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getShipPositionMode() {
		return shipPositionMode;
	}

	public void setShipPositionMode(String shipPositionMode) {
		this.shipPositionMode = shipPositionMode;
	}

	public String getDischargedTo() {
		return dischargedTo;
	}

	public void setDischargedTo(String dischargedTo) {
		this.dischargedTo = dischargedTo;
	}

	public Ship getShip() {
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}
}
