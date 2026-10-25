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
@Table(name="ocean_logbook_boiler")
public class Boiler implements Comparable<Boiler>{

	
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
	private Date startTime;
	
	@Basic
	private Date endTime;
	
	@Basic
	private Double steamPe;
		
	@Basic
	private Double loadD;
	
	@Basic
	private Double foPe;
	
	@Basic
	private Double foTemp;
	
	@Basic
	private Double airFlow;
	
	@Basic
	private Double foFlow;
	
	@Basic
	private Double foPpPe;
		
	@Basic
	private Double waterLevel;
	
	@Basic
	private String dutyEngName;
	
	@Basic
	private String dutyEngSurname;
	
	@Basic
	private String dutyEngRank;
	
	
	@Column (length=5000) // 5000 caratteri
	private String remarks;	
	
	/**
	 * Costruttore vuoto
	 */
	public Boiler() {
		super();
	}
	
	
	@Override
	public int compareTo(Boiler comp) {
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


	public Date getStartTime() {
		return startTime;
	}


	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}


	public Date getEndTime() {
		return endTime;
	}


	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	
	public String getDutyEngName() {
		return dutyEngName;
	}


	public void setDutyEngName(String dutyEngName) {
		this.dutyEngName = dutyEngName;
	}


	public String getDutyEngSurname() {
		return dutyEngSurname;
	}


	public void setDutyEngSurname(String dutyEngSurname) {
		this.dutyEngSurname = dutyEngSurname;
	}


	public String getDutyEngRank() {
		return dutyEngRank;
	}


	public void setDutyEngRank(String dutyEngRank) {
		this.dutyEngRank = dutyEngRank;
	}


	public Double getSteamPe() {
		return steamPe;
	}


	public void setSteamPe(Double steamPe) {
		this.steamPe = steamPe;
	}


	public Double getFoPe() {
		return foPe;
	}


	public void setFoPe(Double foPe) {
		this.foPe = foPe;
	}


	public Double getFoTemp() {
		return foTemp;
	}


	public void setFoTemp(Double foTemp) {
		this.foTemp = foTemp;
	}


	public Double getAirFlow() {
		return airFlow;
	}


	public void setAirFlow(Double airFlow) {
		this.airFlow = airFlow;
	}


	public Double getFoFlow() {
		return foFlow;
	}


	public void setFoFlow(Double foFlow) {
		this.foFlow = foFlow;
	}


	public Double getWaterLevel() {
		return waterLevel;
	}


	public void setWaterLevel(Double waterLevel) {
		this.waterLevel = waterLevel;
	}


	public Double getLoadD() {
		return loadD;
	}


	public void setLoadD(Double loadD) {
		this.loadD = loadD;
	}


	public Double getFoPpPe() {
		return foPpPe;
	}


	public void setFoPpPe(Double foPpPe) {
		this.foPpPe = foPpPe;
	}


	public Ship getShip() {
		return ship;
	}


	public void setShip(Ship ship) {
		this.ship = ship;
	}



}
