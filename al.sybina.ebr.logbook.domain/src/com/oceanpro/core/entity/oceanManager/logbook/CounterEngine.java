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
@Table(name="ocean_logbook_counter_engine")
public class CounterEngine implements Comparable<CounterEngine>{

	
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
	private Double mePrevRunHours;
	
	@Basic
	private Double meRunHours;
	
	@Basic
	private Double meTotRunHours;
	
	@Basic
	private Double dg1PrevRunHours;
	
	@Basic
	private Double dg1RunHours;
	
	@Basic
	private Double dg1TotRunHours;
	
	@Basic
	private Double dg2PrevRunHours;
	
	@Basic
	private Double dg2RunHours;
	
	@Basic
	private Double dg2TotRunHours;
	
	@Basic
	private Double dg3PrevRunHours;
	
	@Basic
	private Double dg3RunHours;
	
	@Basic
	private Double dg3TotRunHours;
	
	@Basic
	private Double meRevPrevRunHours;

	@Basic
	private Double meRevRunHours;
	
	@Basic
	private Double meRevTotRunHours;
	
	@Basic
	private Double lBoilerPrevRunHours;
	
	@Basic
	private Double lBoilerRunHours;
	
	@Basic
	private Double lBoilerTotRunHours;
	
	@Basic
	private Double sBoilerPrevRunHours;
	
	@Basic
	private Double sBoilerRunHours;
	
	@Basic
	private Double sBoilerTotRunHours;
	
	@Basic
	private Double fwgPrevRunHours;
	
	@Basic
	private Double fwgRunHours;	
	
	@Basic
	private Double fwgTotRunHours;
	
	@Basic
	private Double igsPrevRunHours;
	
	@Basic
	private Double igsRunHours;
	
	@Basic
	private Double igsTotRunHours;
	
	@Basic
	private Double waterProductionPrevRunHours;
	
	@Basic
	private Double waterProductionRunHours;
	
	@Basic
	private Double waterProductionTotRunHours;
	
	
	
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
	public CounterEngine() {
		super();
	}
	
	
	@Override
	public int compareTo(CounterEngine comp) {
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


	public Double getMePrevRunHours() {
		return mePrevRunHours;
	}


	public void setMePrevRunHours(Double mePrevRunHours) {
		this.mePrevRunHours = mePrevRunHours;
	}


	public Double getMeRunHours() {
		return meRunHours;
	}


	public void setMeRunHours(Double meRunHours) {
		this.meRunHours = meRunHours;
	}


	public Double getMeTotRunHours() {
		return meTotRunHours;
	}


	public void setMeTotRunHours(Double meTotRunHours) {
		this.meTotRunHours = meTotRunHours;
	}


	public Double getDg1PrevRunHours() {
		return dg1PrevRunHours;
	}


	public void setDg1PrevRunHours(Double dg1PrevRunHours) {
		this.dg1PrevRunHours = dg1PrevRunHours;
	}


	public Double getDg1RunHours() {
		return dg1RunHours;
	}


	public void setDg1RunHours(Double dg1RunHours) {
		this.dg1RunHours = dg1RunHours;
	}


	public Double getDg1TotRunHours() {
		return dg1TotRunHours;
	}


	public void setDg1TotRunHours(Double dg1TotRunHours) {
		this.dg1TotRunHours = dg1TotRunHours;
	}


	public Double getDg2PrevRunHours() {
		return dg2PrevRunHours;
	}


	public void setDg2PrevRunHours(Double dg2PrevRunHours) {
		this.dg2PrevRunHours = dg2PrevRunHours;
	}


	public Double getDg2RunHours() {
		return dg2RunHours;
	}


	public void setDg2RunHours(Double dg2RunHours) {
		this.dg2RunHours = dg2RunHours;
	}


	public Double getDg2TotRunHours() {
		return dg2TotRunHours;
	}


	public void setDg2TotRunHours(Double dg2TotRunHours) {
		this.dg2TotRunHours = dg2TotRunHours;
	}


	public Double getDg3PrevRunHours() {
		return dg3PrevRunHours;
	}


	public void setDg3PrevRunHours(Double dg3PrevRunHours) {
		this.dg3PrevRunHours = dg3PrevRunHours;
	}


	public Double getDg3RunHours() {
		return dg3RunHours;
	}


	public void setDg3RunHours(Double dg3RunHours) {
		this.dg3RunHours = dg3RunHours;
	}


	public Double getDg3TotRunHours() {
		return dg3TotRunHours;
	}


	public void setDg3TotRunHours(Double dg3TotRunHours) {
		this.dg3TotRunHours = dg3TotRunHours;
	}


	public Double getMeRevPrevRunHours() {
		return meRevPrevRunHours;
	}


	public void setMeRevPrevRunHours(Double meRevPrevRunHours) {
		this.meRevPrevRunHours = meRevPrevRunHours;
	}


	public Double getMeRevRunHours() {
		return meRevRunHours;
	}


	public void setMeRevRunHours(Double meRevRunHours) {
		this.meRevRunHours = meRevRunHours;
	}


	public Double getMeRevTotRunHours() {
		return meRevTotRunHours;
	}


	public void setMeRevTotRunHours(Double meRevTotRunHours) {
		this.meRevTotRunHours = meRevTotRunHours;
	}


	public Double getlBoilerPrevRunHours() {
		return lBoilerPrevRunHours;
	}


	public void setlBoilerPrevRunHours(Double lBoilerPrevRunHours) {
		this.lBoilerPrevRunHours = lBoilerPrevRunHours;
	}


	public Double getlBoilerRunHours() {
		return lBoilerRunHours;
	}


	public void setlBoilerRunHours(Double lBoilerRunHours) {
		this.lBoilerRunHours = lBoilerRunHours;
	}


	public Double getlBoilerTotRunHours() {
		return lBoilerTotRunHours;
	}


	public void setlBoilerTotRunHours(Double lBoilerTotRunHours) {
		this.lBoilerTotRunHours = lBoilerTotRunHours;
	}


	public Double getsBoilerPrevRunHours() {
		return sBoilerPrevRunHours;
	}


	public void setsBoilerPrevRunHours(Double sBoilerPrevRunHours) {
		this.sBoilerPrevRunHours = sBoilerPrevRunHours;
	}


	public Double getsBoilerRunHours() {
		return sBoilerRunHours;
	}


	public void setsBoilerRunHours(Double sBoilerRunHours) {
		this.sBoilerRunHours = sBoilerRunHours;
	}


	public Double getsBoilerTotRunHours() {
		return sBoilerTotRunHours;
	}


	public void setsBoilerTotRunHours(Double sBoilerTotRunHours) {
		this.sBoilerTotRunHours = sBoilerTotRunHours;
	}


	public Double getFwgPrevRunHours() {
		return fwgPrevRunHours;
	}


	public void setFwgPrevRunHours(Double fwgPrevRunHours) {
		this.fwgPrevRunHours = fwgPrevRunHours;
	}


	public Double getFwgRunHours() {
		return fwgRunHours;
	}


	public void setFwgRunHours(Double fwgRunHours) {
		this.fwgRunHours = fwgRunHours;
	}


	public Double getFwgTotRunHours() {
		return fwgTotRunHours;
	}


	public void setFwgTotRunHours(Double fwgTotRunHours) {
		this.fwgTotRunHours = fwgTotRunHours;
	}


	public Double getIgsPrevRunHours() {
		return igsPrevRunHours;
	}


	public void setIgsPrevRunHours(Double igsPrevRunHours) {
		this.igsPrevRunHours = igsPrevRunHours;
	}


	public Double getIgsRunHours() {
		return igsRunHours;
	}


	public void setIgsRunHours(Double igsRunHours) {
		this.igsRunHours = igsRunHours;
	}


	public Double getIgsTotRunHours() {
		return igsTotRunHours;
	}


	public void setIgsTotRunHours(Double igsTotRunHours) {
		this.igsTotRunHours = igsTotRunHours;
	}


	public Double getWaterProductionPrevRunHours() {
		return waterProductionPrevRunHours;
	}


	public void setWaterProductionPrevRunHours(Double waterProductionPrevRunHours) {
		this.waterProductionPrevRunHours = waterProductionPrevRunHours;
	}


	public Double getWaterProductionRunHours() {
		return waterProductionRunHours;
	}


	public void setWaterProductionRunHours(Double waterProductionRunHours) {
		this.waterProductionRunHours = waterProductionRunHours;
	}


	public Double getWaterProductionTotRunHours() {
		return waterProductionTotRunHours;
	}


	public void setWaterProductionTotRunHours(Double waterProductionTotRunHours) {
		this.waterProductionTotRunHours = waterProductionTotRunHours;
	}


	public Ship getShip() {
		return ship;
	}


	public void setShip(Ship ship) {
		this.ship = ship;
	}




}
