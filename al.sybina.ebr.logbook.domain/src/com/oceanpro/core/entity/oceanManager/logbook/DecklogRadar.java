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
@Table(name="ocean_logbook_decklog_radar")
public class DecklogRadar implements Comparable<DecklogRadar>{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long id ; // Unique ID
	
	
	@ManyToOne (fetch = FetchType.EAGER) // No cascade
	private Decklog deck;

	@Basic
	private String uuid;
	
	
	/*Radar S*/
	
	@Basic
	private Date timeOnRadarS;
	
	@Basic
	private Date timeOffRadarS;
	
	@Basic
	private String runnigHoursRadarS;
	
	@Basic
	private String totRunnigHoursRadarS;
	
	@Basic
	private String visibilityRadarS;
	
	@Basic
	private Integer performanceRadarS;
	
	@Column (length=5000) // 5000 caratteri
	private String remarksRadarS;
	
	
	/*Radar X*/
	
	@Basic
	private Date timeOnRadarX;
	
	@Basic
	private Date timeOffRadarX;
	
	@Basic
	private String runnigHoursRadarX;
	
	@Basic
	private String totRunnigHoursRadarX;
	
	@Basic
	private String visibilityRadarX;
	
	@Basic
	private Integer performanceRadarX;
	
	@Column (length=5000) // 5000 caratteri
	private String remarksRadarX;
	
	/**
	 * Costruttore vuoto
	 */
	public DecklogRadar() {
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

	public Date getTimeOnRadarS() {
		return timeOnRadarS;
	}

	public void setTimeOnRadarS(Date timeOnRadarS) {
		this.timeOnRadarS = timeOnRadarS;
	}

	public Date getTimeOffRadarS() {
		return timeOffRadarS;
	}

	public void setTimeOffRadarS(Date timeOffRadarS) {
		this.timeOffRadarS = timeOffRadarS;
	}

	public String getRunnigHoursRadarS() {
		return runnigHoursRadarS;
	}

	public void setRunnigHoursRadarS(String runnigHoursRadarS) {
		this.runnigHoursRadarS = runnigHoursRadarS;
	}

	public String getTotRunnigHoursRadarS() {
		return totRunnigHoursRadarS;
	}

	public void setTotRunnigHoursRadarS(String totRunnigHoursRadarS) {
		this.totRunnigHoursRadarS = totRunnigHoursRadarS;
	}

	public String getVisibilityRadarS() {
		return visibilityRadarS;
	}

	public void setVisibilityRadarS(String visibilityRadarS) {
		this.visibilityRadarS = visibilityRadarS;
	}

	public Integer getPerformanceRadarS() {
		return performanceRadarS;
	}

	public void setPerformanceRadarS(Integer performanceRadarS) {
		this.performanceRadarS = performanceRadarS;
	}

	public String getRemarksRadarS() {
		return remarksRadarS;
	}

	public void setRemarksRadarS(String remarksRadarS) {
		this.remarksRadarS = remarksRadarS;
	}

	public Date getTimeOnRadarX() {
		return timeOnRadarX;
	}

	public void setTimeOnRadarX(Date timeOnRadarX) {
		this.timeOnRadarX = timeOnRadarX;
	}

	public Date getTimeOffRadarX() {
		return timeOffRadarX;
	}

	public void setTimeOffRadarX(Date timeOffRadarX) {
		this.timeOffRadarX = timeOffRadarX;
	}

	public String getRunnigHoursRadarX() {
		return runnigHoursRadarX;
	}

	public void setRunnigHoursRadarX(String runnigHoursRadarX) {
		this.runnigHoursRadarX = runnigHoursRadarX;
	}

	public String getTotRunnigHoursRadarX() {
		return totRunnigHoursRadarX;
	}

	public void setTotRunnigHoursRadarX(String totRunnigHoursRadarX) {
		this.totRunnigHoursRadarX = totRunnigHoursRadarX;
	}

	public String getVisibilityRadarX() {
		return visibilityRadarX;
	}

	public void setVisibilityRadarX(String visibilityRadarX) {
		this.visibilityRadarX = visibilityRadarX;
	}

	public Integer getPerformanceRadarX() {
		return performanceRadarX;
	}

	public void setPerformanceRadarX(Integer performanceRadarX) {
		this.performanceRadarX = performanceRadarX;
	}

	public String getRemarksRadarX() {
		return remarksRadarX;
	}

	public void setRemarksRadarX(String remarksRadarX) {
		this.remarksRadarX = remarksRadarX;
	}

	@Override
	public int compareTo(DecklogRadar comp) {
		 if (getId()!=null && comp.getId()!=null && getId()>(comp.getId())) return -1; 
		 else if (getId()!=null && comp.getId()!=null && getId().equals(comp.getId())) return 0;
		 else return 1;
	}
	
	
	
	
	
	
}
