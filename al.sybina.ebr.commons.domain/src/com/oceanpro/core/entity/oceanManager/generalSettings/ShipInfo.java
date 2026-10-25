package com.oceanpro.core.entity.oceanManager.generalSettings;


import javax.persistence.Basic;
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

import com.oceanpro.core.entity.oceanManager.office.Ship;

/**
 * 
 * @author Gio
 *
 */
@Entity
@Table(name="ocean_settings_ship_info")
public class ShipInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long id ; // Unique ID
	
	@Basic
	private String uuid;
	@Column(unique=true, nullable=false)
	private String name;
	@Basic
	private String value;
	
	@Basic
	private String nameLabel;
	@Basic
	private String description;
	
	@Basic
	private String section; // Sezione
	
	@Basic
	private int rank;
//	@Column(columnDefinition="INT(1)")
//	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Basic
	private boolean emiSettings;
	
	/***** For office only *************/
	@ManyToOne (fetch = FetchType.LAZY, optional=true) // No cascade
    private Ship ship;
	/************************************/

	/**
	 * Costruttore vuoto
	 */
	public ShipInfo() {
		super();
	}

	
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	@Override
	public boolean equals(Object obj) {
		ShipInfo rhs = (ShipInfo) obj;
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



	public String getUuid() {
		return uuid;
	}




	public void setUuid(String uuid) {
		this.uuid = uuid;
	}


	public String getValue() {
		return value;
	}




	public void setValue(String value) {
		this.value = value;
	}




	public int getRank() {
		return rank;
	}




	public void setRank(int rank) {
		this.rank = rank;
	}




	public Ship getShip() {
		return ship;
	}




	public void setShip(Ship ship) {
		this.ship = ship;
	}




	public boolean isEmiSettings() {
		return emiSettings;
	}




	public void setEmiSettings(boolean emiSettings) {
		this.emiSettings = emiSettings;
	}




	public String getDescription() {
		return description;
	}




	public void setDescription(String description) {
		this.description = description;
	}




	public String getNameLabel() {
		return nameLabel;
	}




	public void setNameLabel(String nameLabel) {
		this.nameLabel = nameLabel;
	}




	public String getSection() {
		return section;
	}




	public void setSection(String section) {
		this.section = section;
	}

}