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
@Table(name="ocean_logbook_visitor")
public class Visitor implements Comparable<Visitor>{

	
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
	private String securityLevel;
	
	@Basic
	private String shipSecurityLevel;
	
	@Column (length=5000) // 5000 caratteri
	private String addMesuredTaken;
	
	@Basic
	private String badge;
	
	@Basic
	private String visitorName;
	
	@Basic
	private String visitorOrganization;
	
	@Basic
	private String reasonOfVisit;
	
	@Basic
	private Date visitorDateIn;

	@Basic
	private Date visitorDateOut;
	
	
	@Basic
	private String badgeChaked;
	
	
	@Basic
	private String personInCharge;
	
	
	@Column (length=5000) // 5000 caratteri
	private String remarks;	
	
	/**
	 * Costruttore vuoto
	 */
	public Visitor() {
		super();
	}
	
	
	@Override
	public int compareTo(Visitor comp) {
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


	public String getSecurityLevel() {
		return securityLevel;
	}


	public void setSecurityLevel(String securityLevel) {
		this.securityLevel = securityLevel;
	}


	public String getShipSecurityLevel() {
		return shipSecurityLevel;
	}


	public void setShipSecurityLevel(String shipSecurityLevel) {
		this.shipSecurityLevel = shipSecurityLevel;
	}


	public String getAddMesuredTaken() {
		return addMesuredTaken;
	}


	public void setAddMesuredTaken(String addMesuredTaken) {
		this.addMesuredTaken = addMesuredTaken;
	}


	public String getBadge() {
		return badge;
	}


	public void setBadge(String badge) {
		this.badge = badge;
	}


	public String getVisitorName() {
		return visitorName;
	}


	public void setVisitorName(String visitorName) {
		this.visitorName = visitorName;
	}


	public String getVisitorOrganization() {
		return visitorOrganization;
	}


	public void setVisitorOrganization(String visitorOrganization) {
		this.visitorOrganization = visitorOrganization;
	}


	public String getReasonOfVisit() {
		return reasonOfVisit;
	}


	public void setReasonOfVisit(String reasonOfVisit) {
		this.reasonOfVisit = reasonOfVisit;
	}


	public Date getVisitorDateIn() {
		return visitorDateIn;
	}


	public void setVisitorDateIn(Date visitorDateIn) {
		this.visitorDateIn = visitorDateIn;
	}


	public Date getVisitorDateOut() {
		return visitorDateOut;
	}


	public void setVisitorDateOut(Date visitorDateOut) {
		this.visitorDateOut = visitorDateOut;
	}


	public String getBadgeChaked() {
		return badgeChaked;
	}


	public void setBadgeChaked(String badgeChaked) {
		this.badgeChaked = badgeChaked;
	}


	public String getPersonInCharge() {
		return personInCharge;
	}


	public void setPersonInCharge(String personInCharge) {
		this.personInCharge = personInCharge;
	}


	public Ship getShip() {
		return ship;
	}


	public void setShip(Ship ship) {
		this.ship = ship;
	}





}
