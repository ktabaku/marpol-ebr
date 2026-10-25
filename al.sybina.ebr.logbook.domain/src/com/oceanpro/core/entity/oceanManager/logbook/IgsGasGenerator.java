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
@Table(name="ocean_logbook_igs_gas_generator")
public class IgsGasGenerator implements Comparable<IgsGasGenerator>{

	
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
	private Double loadD;
	
	@Basic
	private Double o2;
	
	@Basic
	private Double foPe;
	
	@Basic
	private Double instrumentAir;
	
	@Basic
	private Double swTemp;
	
	@Basic
	private Double swPpPress;
	
	@Basic
	private Double igTemp;
	
	@Basic
	private Double scrubber;
	
	@Basic
	private Double cwPe;
	
	@Basic
	private Double combustAirPe;
	
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
	public IgsGasGenerator() {
		super();
	}
	
	
	@Override
	public int compareTo(IgsGasGenerator comp) {
		 if (getId()!=null && comp.getId()!=null && getId() > comp.getId()) return -1; 
		 else if (getId()!=null && comp.getId()!=null && getId().equals(comp.getId())) return 0;
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

	public Double getO2() {
		return o2;
	}


	public void setO2(Double o2) {
		this.o2 = o2;
	}

	public Double getInstrumentAir() {
		return instrumentAir;
	}


	public void setInstrumentAir(Double instrumentAir) {
		this.instrumentAir = instrumentAir;
	}


	public Double getSwTemp() {
		return swTemp;
	}


	public void setSwTemp(Double swTemp) {
		this.swTemp = swTemp;
	}


	public Double getSwPpPress() {
		return swPpPress;
	}


	public void setSwPpPress(Double swPpPress) {
		this.swPpPress = swPpPress;
	}


	public Double getIgTemp() {
		return igTemp;
	}


	public void setIgTemp(Double igTemp) {
		this.igTemp = igTemp;
	}


	public Double getScrubber() {
		return scrubber;
	}


	public void setScrubber(Double scrubber) {
		this.scrubber = scrubber;
	}


	public Double getCwPe() {
		return cwPe;
	}


	public void setCwPe(Double cwPe) {
		this.cwPe = cwPe;
	}


	public Double getCombustAirPe() {
		return combustAirPe;
	}


	public void setCombustAirPe(Double combustAirPe) {
		this.combustAirPe = combustAirPe;
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


	public Double getFoPe() {
		return foPe;
	}


	public void setFoPe(Double foPe) {
		this.foPe = foPe;
	}


	public Double getLoadD() {
		return loadD;
	}


	public void setLoadD(Double loadD) {
		this.loadD = loadD;
	}


	public Ship getShip() {
		return ship;
	}


	public void setShip(Ship ship) {
		this.ship = ship;
	}



}
