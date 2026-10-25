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


@Entity
@Table(name="ocean_logbook_decklog_firepatrol_check_item")
public class DecklogFirepatrolCheckItem {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long id ; // Unique ID
	
	@Basic
	private String uuid;
	
	@ManyToOne (fetch = FetchType.EAGER) // No cascade
	private DecklogFirepatrol firepatrol;
	
	@ManyToOne (fetch = FetchType.EAGER) // No cascade
	private DecklogFirepatrolCheck ckeck;
	
	
	@Column
	private boolean value;
	
	
	
	/**
	 * Costruttore vuoto
	 */
	public DecklogFirepatrolCheckItem() {
		super();
	}



	public DecklogFirepatrol getFirepatrol() {
		return firepatrol;
	}



	public void setFirepatrol(DecklogFirepatrol firepatrol) {
		this.firepatrol = firepatrol;
	}



	public DecklogFirepatrolCheck getCkeck() {
		return ckeck;
	}



	public void setCkeck(DecklogFirepatrolCheck ckeck) {
		this.ckeck = ckeck;
	}



	public boolean isValue() {
		return value;
	}



	public void setValue(boolean value) {
		this.value = value;
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



	@Override
	public String toString() {
		return "DecklogFirepatrolCheckItem [id=" + id + ", uuid=" + uuid
				+ ", firepatrol=" + firepatrol + ", ckeck=" + ckeck
				+ ", value=" + value + "]";
	}




	
	
}
