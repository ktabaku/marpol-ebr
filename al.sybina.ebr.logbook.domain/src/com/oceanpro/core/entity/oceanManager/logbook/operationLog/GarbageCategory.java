package com.oceanpro.core.entity.oceanManager.logbook.operationLog;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * @author Gio
 *
 */
@Entity
@Table(name="ocean_logbook_operation_garbage_category")
public class GarbageCategory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long id ; // Unique ID
	
	@Basic
	private String uuid;
	
	// Cargo Tanks
	@Basic
	private String name;
	@Basic
	private String treatmentWhenShipOutsideSpecialAreas;
	@Basic
	private String treatmentWhenShipWithinSpecialAreas;
	@Basic
	private String treatmentForOfshorePlatforms;
	
	@Override
	public boolean equals(Object obj) {
		GarbageCategory rhs = (GarbageCategory) obj;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTreatmentWhenShipOutsideSpecialAreas() {
		return treatmentWhenShipOutsideSpecialAreas;
	}

	public void setTreatmentWhenShipOutsideSpecialAreas(
			String treatmentWhenShipOutsideSpecialAreas) {
		this.treatmentWhenShipOutsideSpecialAreas = treatmentWhenShipOutsideSpecialAreas;
	}

	public String getTreatmentWhenShipWithinSpecialAreas() {
		return treatmentWhenShipWithinSpecialAreas;
	}

	public void setTreatmentWhenShipWithinSpecialAreas(
			String treatmentWhenShipWithinSpecialAreas) {
		this.treatmentWhenShipWithinSpecialAreas = treatmentWhenShipWithinSpecialAreas;
	}

	public String getTreatmentForOfshorePlatforms() {
		return treatmentForOfshorePlatforms;
	}

	public void setTreatmentForOfshorePlatforms(String treatmentForOfshorePlatforms) {
		this.treatmentForOfshorePlatforms = treatmentForOfshorePlatforms;
	}


}
