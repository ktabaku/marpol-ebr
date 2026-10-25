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
@Table(name="ocean_logbook_drug_test")
public class DrugTest implements Comparable<DrugTest>{

	
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
	private String placeOfTest;
	
	@Basic
	private String namePTested;
	
	@Basic
	private String surnamePTested;
	
	@Basic
	private String rankPTested;
	
	
	@Basic
	private String resultTest;
	
	
	
	@Basic
	private String nameWitness;
	
	@Basic
	private String surnameWitness;
	
	@Basic
	private String rankWitness;
	
	
	
	
	@Basic
	private String officerMasterName;

	
	@Column (length=5000) // 5000 caratteri
	private String remarks;	
	
	/**
	 * Costruttore vuoto
	 */
	public DrugTest() {
		super();
	}
	
	
	@Override
	public int compareTo(DrugTest comp) {
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


	public String getPlaceOfTest() {
		return placeOfTest;
	}


	public void setPlaceOfTest(String placeOfTest) {
		this.placeOfTest = placeOfTest;
	}


	public String getNamePTested() {
		return namePTested;
	}


	public void setNamePTested(String namePTested) {
		this.namePTested = namePTested;
	}


	public String getSurnamePTested() {
		return surnamePTested;
	}


	public void setSurnamePTested(String surnamePTested) {
		this.surnamePTested = surnamePTested;
	}


	public String getRankPTested() {
		return rankPTested;
	}


	public void setRankPTested(String rankPTested) {
		this.rankPTested = rankPTested;
	}


	public String getResultTest() {
		return resultTest;
	}


	public void setResultTest(String resultTest) {
		this.resultTest = resultTest;
	}


	public String getNameWitness() {
		return nameWitness;
	}


	public void setNameWitness(String nameWitness) {
		this.nameWitness = nameWitness;
	}


	public String getSurnameWitness() {
		return surnameWitness;
	}


	public void setSurnameWitness(String surnameWitness) {
		this.surnameWitness = surnameWitness;
	}


	public String getRankWitness() {
		return rankWitness;
	}


	public void setRankWitness(String rankWitness) {
		this.rankWitness = rankWitness;
	}


	public String getOfficerMasterName() {
		return officerMasterName;
	}


	public void setOfficerMasterName(String officerMasterName) {
		this.officerMasterName = officerMasterName;
	}


	public Ship getShip() {
		return ship;
	}


	public void setShip(Ship ship) {
		this.ship = ship;
	}






}
