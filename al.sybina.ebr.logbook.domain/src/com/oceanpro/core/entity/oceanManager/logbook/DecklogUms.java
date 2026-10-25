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
@Table(name="ocean_logbook_decklog_ums")
public class DecklogUms implements Comparable<DecklogUms>{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long id ; // Unique ID
	
	@ManyToOne (fetch = FetchType.EAGER) // No cascade
	private Decklog deck;
	
	
	@Basic
	private String uuid;
	
	@Basic
	private Byte engineUnmanndedHours; 
	@Basic
	private Byte engineUnmanndedMinutes;
	
	@Basic
	private Byte timeInHours; 
	@Basic
	private Byte timeInMinutes;
	
	@Basic
	private Byte timeOutHours; 
	@Basic
	private Byte timeOutMinutes;
	
	@Basic
	private String dutyEng; 
		
	@Basic
	private String reasonOf;	
	
	@Column (length=5000) // 5000 caratteri
	private String remarks;

	
	/**
	 * Costruttore vuoto
	 */
	public DecklogUms() {
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

	public Byte getEngineUnmanndedHours() {
		return engineUnmanndedHours;
	}

	public void setEngineUnmanndedHours(Byte engineUnmanndedHours) {
		this.engineUnmanndedHours = engineUnmanndedHours;
	}

	public Byte getEngineUnmanndedMinutes() {
		return engineUnmanndedMinutes;
	}

	public void setEngineUnmanndedMinutes(Byte engineUnmanndedMinutes) {
		this.engineUnmanndedMinutes = engineUnmanndedMinutes;
	}

	public Byte getTimeInHours() {
		return timeInHours;
	}

	public void setTimeInHours(Byte timeInHours) {
		this.timeInHours = timeInHours;
	}

	public Byte getTimeInMinutes() {
		return timeInMinutes;
	}

	public void setTimeInMinutes(Byte timeInMinutes) {
		this.timeInMinutes = timeInMinutes;
	}

	public Byte getTimeOutHours() {
		return timeOutHours;
	}

	public void setTimeOutHours(Byte timeOutHours) {
		this.timeOutHours = timeOutHours;
	}

	public Byte getTimeOutMinutes() {
		return timeOutMinutes;
	}

	public void setTimeOutMinutes(Byte timeOutMinutes) {
		this.timeOutMinutes = timeOutMinutes;
	}


	public String getReasonOf() {
		return reasonOf;
	}

	public void setReasonOf(String reasonOf) {
		this.reasonOf = reasonOf;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	public String getDutyEng() {
		return dutyEng;
	}


	public void setDutyEng(String dutyEng) {
		this.dutyEng = dutyEng;
	}


	public Decklog getDeck() {
		return deck;
	}


	public void setDeck(Decklog deck) {
		this.deck = deck;
	}


	@Override
	public int compareTo(DecklogUms comp) {
		 if (getId()!=null && comp.getId()!=null && getId()>(comp.getId())) return -1; 
		 else if (getId()!=null && comp.getId()!=null && getId().equals(comp.getId())) return 0;
		 else return 1;
	}
}
