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
@Table(name="ocean_logbook_master_order")
public class MasterNightOrder implements Comparable<MasterNightOrder>{

	
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
	private Date validUntil;
	
	@Lob
	@Column (length=40000) // 40000 caratteri
	private String masterOrder; 

	
	@Column
	private boolean active; // Indica se e attivo. Solo uno alla volta puo essere attivo
	
	
	@Column (length=5000) // 5000 caratteri
	private String remarks;	
	
	/**
	 * Costruttore vuoto
	 */
	public MasterNightOrder() {
		super();
	}
	
	
	@Override
	public int compareTo(MasterNightOrder comp) {
		 if (getId()!=null && comp.getId()!=null && getId() > comp.getId()) return -1; 
		 else if (getId()!=null && comp.getId()!=null && getId().equals(comp.getId())) return 0;
		 else return 1;
	}


	
	public String getMasterOrderTruncate() {
		String ord = masterOrder;
		ord = masterOrder.substring(0, Math.min(masterOrder.length(), 30));
		return ord;
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

	public Date getValidUntil() {
		return validUntil;
	}

	public void setValidUntil(Date validUntil) {
		this.validUntil = validUntil;
	}

	public String getMasterOrder() {
		return masterOrder;
	}

	public void setMasterOrder(String masterOrder) {
		this.masterOrder = masterOrder;
	}


	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	public Ship getShip() {
		return ship;
	}


	public void setShip(Ship ship) {
		this.ship = ship;
	}


	
}
