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
@Table(name="ocean_logbook_refrigerant")
public class Refrigerant implements Comparable<Refrigerant>{

	
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
	private String systemType;
	
	@Basic
	private String refrigerantType;

	
	@Basic
	private Double initSysCharge;
	
	@Basic
	private Double refrigerantAdd;

	@Basic
	private Double refrigerantRecovered;
	
	
	
	@Basic
	private Double sysOilAdd;
	
	@Basic
	private Double originalOil;
	
	
	//yes o no
	@Basic
	private String sysInspectionFlag;

	
	@Basic
	private String officerMasterName;

	
	@Column (length=5000) // 5000 caratteri
	private String remarks;	
	
	/**
	 * Costruttore vuoto
	 */
	public Refrigerant() {
		super();
	}
	
	
	@Override
	public int compareTo(Refrigerant comp) {
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

	public String getOfficerMasterName() {
		return officerMasterName;
	}


	public void setOfficerMasterName(String officerMasterName) {
		this.officerMasterName = officerMasterName;
	}


	public String getSystemType() {
		return systemType;
	}


	public void setSystemType(String systemType) {
		this.systemType = systemType;
	}


	public String getRefrigerantType() {
		return refrigerantType;
	}


	public void setRefrigerantType(String refrigerantType) {
		this.refrigerantType = refrigerantType;
	}


	public Double getInitSysCharge() {
		return initSysCharge;
	}


	public void setInitSysCharge(Double initSysCharge) {
		this.initSysCharge = initSysCharge;
	}


	public Double getRefrigerantAdd() {
		return refrigerantAdd;
	}


	public void setRefrigerantAdd(Double refrigerantAdd) {
		this.refrigerantAdd = refrigerantAdd;
	}


	public Double getRefrigerantRecovered() {
		return refrigerantRecovered;
	}


	public void setRefrigerantRecovered(Double refrigerantRecovered) {
		this.refrigerantRecovered = refrigerantRecovered;
	}


	public Double getSysOilAdd() {
		return sysOilAdd;
	}


	public void setSysOilAdd(Double sysOilAdd) {
		this.sysOilAdd = sysOilAdd;
	}


	public Double getOriginalOil() {
		return originalOil;
	}


	public void setOriginalOil(Double originalOil) {
		this.originalOil = originalOil;
	}


	public String getSysInspectionFlag() {
		return sysInspectionFlag;
	}


	public void setSysInspectionFlag(String sysInspectionFlag) {
		this.sysInspectionFlag = sysInspectionFlag;
	}


	public Ship getShip() {
		return ship;
	}


	public void setShip(Ship ship) {
		this.ship = ship;
	}






}
