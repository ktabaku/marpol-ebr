package com.oceanpro.core.entity.oceanManager.logbook;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.oceanpro.core.entity.oceanManager.office.Ship;

@Entity
@Table(name="ocean_logbook_gmdss_c")
public class GmdssSecC implements Comparable<GmdssSecC>{

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
	private Date registrationDate;
	
	
	
	@Basic
	private String toStation;
	
	@Basic
	private String fromStation;
	
	@Basic
	private String importantIncidentConnRadio;
	
	@Basic
	private Double radioFrequency =0.0;
	
	
	@Column (length=5000) // 5000 caratteri
	private String remarks;

	/**
	 * Costruttore vuoto
	 */
	public GmdssSecC() {
		super();
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

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getToStation() {
		return toStation;
	}

	public void setToStation(String toStation) {
		this.toStation = toStation;
	}

	public String getFromStation() {
		return fromStation;
	}

	public void setFromStation(String fromStation) {
		this.fromStation = fromStation;
	}

	public String getImportantIncidentConnRadio() {
		return importantIncidentConnRadio;
	}

	public void setImportantIncidentConnRadio(String importantIncidentConnRadio) {
		this.importantIncidentConnRadio = importantIncidentConnRadio;
	}

	public Double getRadioFrequency() {
		return radioFrequency;
	}

	public void setRadioFrequency(Double radioFrequency) {
		this.radioFrequency = radioFrequency;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public String toString() {
		return "GmdssSecC [id=" + id + ", uuid=" + uuid + ", registrationDate="
				+ registrationDate + ", toStation=" + toStation
				+ ", fromStation=" + fromStation
				+ ", importantIncidentConnRadio=" + importantIncidentConnRadio
				+ ", radioFrequency=" + radioFrequency + ", remarks=" + remarks
				+ "]";
	}

	@Override
	public int compareTo(GmdssSecC comp) {
		 if (getRegistrationDate()!=null && comp.getRegistrationDate()!=null && getRegistrationDate().after(comp.getRegistrationDate())) return -1; 
		 else if (getRegistrationDate()!=null && comp.getRegistrationDate()!=null && getRegistrationDate().equals(comp.getRegistrationDate())) return 0;
		 else return 1;
	}

	public Ship getShip() {
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}
	
	
	

}