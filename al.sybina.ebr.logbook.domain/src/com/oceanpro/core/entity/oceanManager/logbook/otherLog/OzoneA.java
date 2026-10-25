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
@Table(name="ocean_logbook_ozone_a")
public class OzoneA implements Comparable<OzoneA>{

	
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
	private String refrigerantType;
	
	@Basic
	private Double amount;
	
	@Basic
	private String receivedLandedType;
	
	@Basic
	private String receivedLandedNote;
	
	@Basic
	private String supplierReceiverType;
	
	@Basic
	private String supplierReceiverNote;
	
	@Basic
	private String deliveryDisposalNroType;
	
	@Basic
	private String deliveryDisposalNroNote;
	
	
	
	@Basic
	private String masterCeInitialsName;

	
	@Column (length=5000) // 5000 caratteri
	private String remarks;	
	
	/**
	 * Costruttore vuoto
	 */
	public OzoneA() {
		super();
	}
	
	
	@Override
	public int compareTo(OzoneA comp) {
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


	public String getRefrigerantType() {
		return refrigerantType;
	}


	public void setRefrigerantType(String refrigerantType) {
		this.refrigerantType = refrigerantType;
	}


	public Double getAmount() {
		return amount;
	}


	public void setAmount(Double amount) {
		this.amount = amount;
	}


	public String getReceivedLandedNote() {
		return receivedLandedNote;
	}


	public void setReceivedLandedNote(String receivedLandedNote) {
		this.receivedLandedNote = receivedLandedNote;
	}

	public String getSupplierReceiverNote() {
		return supplierReceiverNote;
	}


	public void setSupplierReceiverNote(String supplierReceiverNote) {
		this.supplierReceiverNote = supplierReceiverNote;
	}


	public String getDeliveryDisposalNroNote() {
		return deliveryDisposalNroNote;
	}


	public void setDeliveryDisposalNroNote(String deliveryDisposalNroNote) {
		this.deliveryDisposalNroNote = deliveryDisposalNroNote;
	}


	public String getReceivedLandedType() {
		return receivedLandedType;
	}


	public void setReceivedLandedType(String receivedLandedType) {
		this.receivedLandedType = receivedLandedType;
	}


	public String getSupplierReceiverType() {
		return supplierReceiverType;
	}


	public void setSupplierReceiverType(String supplierReceiverType) {
		this.supplierReceiverType = supplierReceiverType;
	}


	public String getDeliveryDisposalNroType() {
		return deliveryDisposalNroType;
	}


	public void setDeliveryDisposalNroType(String deliveryDisposalNroType) {
		this.deliveryDisposalNroType = deliveryDisposalNroType;
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
