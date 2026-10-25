package com.oceanpro.core.entity.oceanManager.logbook.otherLog;

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
@Table(name="ocean_logbook_ozone_b")
public class OzoneB implements Comparable<OzoneB>{

	
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
	private String system;


	@Basic
	private String processRefrigerant;
	
	@Basic
	private Double amount;
	
	
	@Basic
	private String richargeFlag;
	
	@Basic
	private String richargeType;
	
	@Basic
	private Double richargeNote;
	
	
	@Basic
	private String repairEquipmentFlag;
	
	@Basic
	private String repairEquipmentNote;

	
	@Basic
	private String discargeAtmFlag;
	
	@Basic
	private String discargeAtmType;
	
	@Basic
	private Double discargeAtmNote;
	
	
	@Basic
	private String discargeReceptionFacilityFlag;
	
	@Basic
	private Double discargeReceptionFacilityNote;
	
	
	@Basic
	private String supplyShipFlag;
	
	@Basic
	private Double supplyShipNote;
	
	
	
	@Basic
	private String masterCeInitialsName;

	
	@Column (length=5000) // 5000 caratteri
	private String remarks;	
	
	/**
	 * Costruttore vuoto
	 */
	public OzoneB() {
		super();
	}
	
	
	@Override
	public int compareTo(OzoneB comp) {
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


	public Double getAmount() {
		return amount;
	}


	public void setAmount(Double amount) {
		this.amount = amount;
	}


	public String getSystem() {
		return system;
	}


	public void setSystem(String system) {
		this.system = system;
	}


	public String getProcessRefrigerant() {
		return processRefrigerant;
	}


	public void setProcessRefrigerant(String processRefrigerant) {
		this.processRefrigerant = processRefrigerant;
	}


	public String getRichargeFlag() {
		return richargeFlag;
	}


	public void setRichargeFlag(String richargeFlag) {
		this.richargeFlag = richargeFlag;
	}


	public String getRichargeType() {
		return richargeType;
	}


	public void setRichargeType(String richargeType) {
		this.richargeType = richargeType;
	}


	public Double getRichargeNote() {
		return richargeNote;
	}


	public void setRichargeNote(Double richargeNote) {
		this.richargeNote = richargeNote;
	}


	public String getRepairEquipmentFlag() {
		return repairEquipmentFlag;
	}


	public void setRepairEquipmentFlag(String repairEquipmentFlag) {
		this.repairEquipmentFlag = repairEquipmentFlag;
	}


	public String getRepairEquipmentNote() {
		return repairEquipmentNote;
	}


	public void setRepairEquipmentNote(String repairEquipmentNote) {
		this.repairEquipmentNote = repairEquipmentNote;
	}


	public String getDiscargeAtmFlag() {
		return discargeAtmFlag;
	}


	public void setDiscargeAtmFlag(String discargeAtmFlag) {
		this.discargeAtmFlag = discargeAtmFlag;
	}


	public String getDiscargeAtmType() {
		return discargeAtmType;
	}


	public void setDiscargeAtmType(String discargeAtmType) {
		this.discargeAtmType = discargeAtmType;
	}


	public Double getDiscargeAtmNote() {
		return discargeAtmNote;
	}


	public void setDiscargeAtmNote(Double discargeAtmNote) {
		this.discargeAtmNote = discargeAtmNote;
	}


	public String getDiscargeReceptionFacilityFlag() {
		return discargeReceptionFacilityFlag;
	}


	public void setDiscargeReceptionFacilityFlag(
			String discargeReceptionFacilityFlag) {
		this.discargeReceptionFacilityFlag = discargeReceptionFacilityFlag;
	}


	public Double getDiscargeReceptionFacilityNote() {
		return discargeReceptionFacilityNote;
	}


	public void setDiscargeReceptionFacilityNote(
			Double discargeReceptionFacilityNote) {
		this.discargeReceptionFacilityNote = discargeReceptionFacilityNote;
	}


	public String getSupplyShipFlag() {
		return supplyShipFlag;
	}


	public void setSupplyShipFlag(String supplyShipFlag) {
		this.supplyShipFlag = supplyShipFlag;
	}


	public Double getSupplyShipNote() {
		return supplyShipNote;
	}


	public void setSupplyShipNote(Double supplyShipNote) {
		this.supplyShipNote = supplyShipNote;
	}


	public String getMasterCeInitialsName() {
		return masterCeInitialsName;
	}


	public void setMasterCeInitialsName(String masterCeInitialsName) {
		this.masterCeInitialsName = masterCeInitialsName;
	}


	public Ship getShip() {
		return ship;
	}


	public void setShip(Ship ship) {
		this.ship = ship;
	}






}
