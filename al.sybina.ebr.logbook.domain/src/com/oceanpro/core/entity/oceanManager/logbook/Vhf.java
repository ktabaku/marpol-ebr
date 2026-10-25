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

import com.oceanpro.core.entity.oceanManager.office.Ship;


@Entity
@Table(name="ocean_logbook_vhf")
public class Vhf implements Comparable<Vhf>{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long id ; // Unique ID
	
	/***** For office only *************/
	@ManyToOne (fetch = FetchType.LAZY, optional=true) // No cascade
    private Ship ship;
	/************************************/
	
	@Basic
	private String uuid;
	
	@Basic
	private Date registrationDate;
	
	@Basic
	private String watch; 
	
	@Basic
	private Byte timeHours; 
	@Basic
	private Byte timeMinutes;
	
	
	@Basic
	private String fromStation;
	
	@Basic
	private String toStation;
	
	
	@Basic
	private Integer channel; 
	
	@Basic
	private String performance; 
	
	@Column (length=5000) // 5000 caratteri
	private String remarks;
	
	
	
	/**
	 * Costruttore vuoto
	 */
	public Vhf() {
		super();
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

//
//	public Decklog getDeck() {
//		return deck;
//	}
//
//
//	public void setDeck(Decklog deck) {
//		this.deck = deck;
//	}


	public String getUuid() {
		return uuid;
	}


	public void setUuid(String uuid) {
		this.uuid = uuid;
	}


	public String getWatch() {
		return watch;
	}


	public void setWatch(String watch) {
		this.watch = watch;
	}


	public Byte getTimeHours() {
		return timeHours;
	}


	public void setTimeHours(Byte timeHours) {
		this.timeHours = timeHours;
	}


	public Byte getTimeMinutes() {
		return timeMinutes;
	}


	public void setTimeMinutes(Byte timeMinutes) {
		this.timeMinutes = timeMinutes;
	}


	public Integer getChannel() {
		return channel;
	}


	public void setChannel(Integer channel) {
		this.channel = channel;
	}


	public String getPerformance() {
		return performance;
	}


	public void setPerformance(String performance) {
		this.performance = performance;
	}


	public String getRemarks() {
		return remarks;
	}


	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	public String getFromStation() {
		return fromStation;
	}


	public void setFromStation(String fromStation) {
		this.fromStation = fromStation;
	}


	public String getToStation() {
		return toStation;
	}


	public void setToStation(String toStation) {
		this.toStation = toStation;
	}


	public Date getRegistrationDate() {
		return registrationDate;
	}


	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}


	@Override
	public int compareTo(Vhf comp) {
		 if (getRegistrationDate()!=null && comp.getRegistrationDate()!=null && getRegistrationDate().after(comp.getRegistrationDate())) return -1; 
		 else if (getRegistrationDate()!=null && comp.getRegistrationDate()!=null && getRegistrationDate().equals(comp.getRegistrationDate())) return 0;
		 else return 1;
	}


	public Ship getShip() {
		return ship;
	}


	public void setShip(Ship ship) {
		this.ship = ship;
	}


	
	
	
}
