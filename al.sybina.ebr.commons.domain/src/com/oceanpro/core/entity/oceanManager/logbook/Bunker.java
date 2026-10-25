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
@Table(name="ocean_logbook_bunker")
public class Bunker implements Comparable<Bunker>{

	
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
	private String port;
	
	@Basic
	private String supplier;
	
	@Basic
	private String terminalName;
	
	@Basic
	private String samplingMethod;
	
	@Basic
	private String fuelGrade;
		
	@Basic
	private String seal;
	
	
	@Basic
	private String bunkerReceptRef;
	
	@Basic
	private String ceInitials;
		
	
	@Column (length=5000) // 5000 caratteri
	private String remarks;	
	
	/**
	 * Costruttore vuoto
	 */
	public Bunker() {
		super();
	}
	
	
	@Override
	public int compareTo(Bunker comp) {
		 if (getRegistrationDate()!=null && comp.getRegistrationDate()!=null && getRegistrationDate().after(comp.getRegistrationDate())) return -1; 
		 else if (getRegistrationDate()!=null && comp.getRegistrationDate()!=null && getRegistrationDate().equals(comp.getRegistrationDate())) return 0;
		 else return 1;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	public Date getRegistrationDate() {
		return registrationDate;
	}


	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}


	public String getPort() {
		return port;
	}


	public void setPort(String port) {
		this.port = port;
	}


	public String getSupplier() {
		return supplier;
	}


	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}


	public String getTerminalName() {
		return terminalName;
	}


	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}


	public String getSamplingMethod() {
		return samplingMethod;
	}


	public void setSamplingMethod(String samplingMethod) {
		this.samplingMethod = samplingMethod;
	}


	public String getFuelGrade() {
		return fuelGrade;
	}


	public void setFuelGrade(String fuelGrade) {
		this.fuelGrade = fuelGrade;
	}





	public String getBunkerReceptRef() {
		return bunkerReceptRef;
	}


	public void setBunkerReceptRef(String bunkerReceptRef) {
		this.bunkerReceptRef = bunkerReceptRef;
	}


	public String getCeInitials() {
		return ceInitials;
	}


	public void setCeInitials(String ceInitials) {
		this.ceInitials = ceInitials;
	}


	public String getSeal() {
		return seal;
	}


	public void setSeal(String seal) {
		this.seal = seal;
	}


	public Ship getShip() {
		return ship;
	}


	public void setShip(Ship ship) {
		this.ship = ship;
	}






}
