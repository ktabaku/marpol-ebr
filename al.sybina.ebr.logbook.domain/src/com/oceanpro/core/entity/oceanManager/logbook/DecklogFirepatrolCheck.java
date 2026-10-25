package com.oceanpro.core.entity.oceanManager.logbook;

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
@Table(name="ocean_logbook_decklog_firepatrol_check")
public class DecklogFirepatrolCheck {

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long id ; // Unique ID
	
	@Basic
	private String uuid;
	
	@Basic
	private String bridge;
	
	@Basic
	private String aree;
	
	@ManyToOne (fetch = FetchType.LAZY, optional=false) // No cascade
    private Ship ship;
	
	/**
	 * Costruttore vuoto
	 */
	public DecklogFirepatrolCheck() {
		super();
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

	public String getBridge() {
		return bridge;
	}

	public void setBridge(String bridge) {
		this.bridge = bridge;
	}

	public String getAree() {
		return aree;
	}

	public void setAree(String aree) {
		this.aree = aree;
	}

	public Ship getShip() {
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}



	@Override
	public String toString() {
		return "DecklogFirepatrolCheck [id=" + id + ", uuid=" + uuid
				+ ", bridge=" + bridge + ", aree=" + aree 
				+ "]";
	}


}
