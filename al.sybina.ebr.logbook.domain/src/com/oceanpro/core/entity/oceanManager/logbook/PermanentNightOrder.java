package com.oceanpro.core.entity.oceanManager.logbook;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


import com.oceanpro.core.entity.oceanManager.office.Ship;

@Entity
@Table(name="ocean_logbook_permanent_order")
public class PermanentNightOrder implements Comparable<PermanentNightOrder>{

	
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
	
	@Lob
	@Column (length=40000) // 40000 caratteri
	private String masterPermanentOrder;
	
	@Column
	private boolean active; // Indica se e attivo. Solo uno alla volta puo essere attivo
	
	@Column (length=5000) // 5000 caratteri
	private String remarks;

	
	/**
	 * Costruttore vuoto
	 */
	public PermanentNightOrder() {
		super();
	}
	
	
	@Override
	public int compareTo(PermanentNightOrder comp) {
		 if (getId()!=null && comp.getId()!=null && getId() > comp.getId()) return -1; 
		 else if (getId()!=null && comp.getId()!=null && getId().equals(comp.getId())) return 0;
		 else return 1;
	}

	
	public String getMasterPermanentOrderTruncate() {
		String perOrd = masterPermanentOrder;
		perOrd = masterPermanentOrder.substring(0, Math.min(masterPermanentOrder.length(), 30));
		return perOrd;
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

	public String getMasterPermanentOrder() {
		return masterPermanentOrder;
	}

	public void setMasterPermanentOrder(String masterPermanentOrder) {
		this.masterPermanentOrder = masterPermanentOrder;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "PermanentNightOrder [id=" + id + ", uuid=" + uuid
				+ ", registrationDate=" + registrationDate
				+ ", masterPermanentOrder=" + masterPermanentOrder
				+ ", active=" + active + ", remarks=" + remarks + "]";
	}


	public Ship getShip() {
		return ship;
	}


	public void setShip(Ship ship) {
		this.ship = ship;
	}
}
