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

@Entity
@Table(name="ocean_logbook_decklog_chrono")
public class DecklogChrono implements Comparable<DecklogChrono>{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long id ; // Unique ID
	
	@ManyToOne (fetch = FetchType.EAGER) // No cascade
	private Decklog deck;

	@Basic
	private String uuid;
	
	@Basic
	private Date gmtDate;
	
	@Basic
	private Date chronoDate;
	
	@Basic
	private String errorTime;
	
	@Basic
	private Double temperature;
	
	@Basic
	private String observerStation;
	
	@Basic
	private String observerName;
	
	@Basic
	private String observerSurname;
	
	@Basic
	private String observerRank;
	
	@Column (length=5000) // 5000 caratteri
	private String remarks;

	
	
	/**
	 * Costruttore vuoto
	 */
	public DecklogChrono() {
		super();
	}
	
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Decklog getDeck() {
		return deck;
	}

	public void setDeck(Decklog deck) {
		this.deck = deck;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Date getGmtDate() {
		return gmtDate;
	}

	public void setGmtDate(Date gmtDate) {
		this.gmtDate = gmtDate;
	}

	public Date getChronoDate() {
		return chronoDate;
	}

	public void setChronoDate(Date chronoDate) {
		this.chronoDate = chronoDate;
	}

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	public String getObserverName() {
		return observerName;
	}

	public void setObserverName(String observerName) {
		this.observerName = observerName;
	}

	public String getObserverSurname() {
		return observerSurname;
	}

	public void setObserverSurname(String observerSurname) {
		this.observerSurname = observerSurname;
	}

	public String getObserverRank() {
		return observerRank;
	}

	public void setObserverRank(String observerRank) {
		this.observerRank = observerRank;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}



	@Override
	public String toString() {
		return "DecklogChrono [id=" + id + ", uuid=" + uuid + ", gmtDate="
				+ gmtDate + ", chronoDate=" + chronoDate + ", temperature="
				+ temperature + ", observerName=" + observerName
				+ ", observerSurname=" + observerSurname + ", observerRank="
				+ observerRank + ", remarks=" + remarks + "]";
	}



	public String getErrorTime() {
		return errorTime;
	}



	public void setErrorTime(String errorTime) {
		this.errorTime = errorTime;
	}



	public String getObserverStation() {
		return observerStation;
	}



	public void setObserverStation(String observerStation) {
		this.observerStation = observerStation;
	}


	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(DecklogChrono comp) {
		 if (getGmtDate()!=null && comp.getGmtDate()!=null && getGmtDate().after(comp.getGmtDate())) return -1; 
		 else if (getGmtDate()!=null && comp.getGmtDate()!=null && getGmtDate().equals(comp.getGmtDate())) return 0;
		 else return 1;
	}

	
	
}
