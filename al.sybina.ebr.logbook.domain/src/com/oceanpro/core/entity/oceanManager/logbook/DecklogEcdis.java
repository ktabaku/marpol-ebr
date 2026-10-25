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
@Table(name="ocean_logbook_decklog_ecdis")
public class DecklogEcdis {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long id ; // Unique ID
	
	@ManyToOne (fetch = FetchType.EAGER) // No cascade
	private Decklog deck;
	
	@Basic
	private String uuid;
	
	@Basic
	private String watch;
	
	@Basic
	private String lastNtm;

	@Basic
	private Date ntmDate;
	
	@Basic
	private Integer wayPointNr;
	
	@Basic
	private Integer wayPointDistanceNext;
	
	@Basic
	private Date etaNextWayPoint;
	
	@Basic
	private Double crossTrackError =0.0;
	

	@Column
	private boolean performanceGood;
	
	@Column
	private boolean courseChecked;
	
	@Column
	private boolean trackWithAutoPilot;
	
	@Column
	private boolean positionCompared;
	
	@Column
	private boolean speedCompared;
	
	@Column
	private boolean dgps1Compare;
	
	@Column
	private boolean dgps2Compare;
	
	@Column
	private boolean wayPointControlled;

	@Column
	private boolean configurationCalibratic;
	
	@Column (length=5000) // 5000 caratteri
	private String remarks;
	
	
	/**
	 * Costruttore vuoto
	 */
	public DecklogEcdis() {
		super();
	}
	public DecklogEcdis(String watch) {
		this.setWatch(watch);
		

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


	public String getWatch() {
		return watch;
	}


	public void setWatch(String watch) {
		this.watch = watch;
	}


	public String getLastNtm() {
		return lastNtm;
	}


	public void setLastNtm(String lastNtm) {
		this.lastNtm = lastNtm;
	}


	public Date getNtmDate() {
		return ntmDate;
	}


	public void setNtmDate(Date ntmDate) {
		this.ntmDate = ntmDate;
	}


	public Integer getWayPointNr() {
		return wayPointNr;
	}


	public void setWayPointNr(Integer wayPointNr) {
		this.wayPointNr = wayPointNr;
	}


	public Integer getWayPointDistanceNext() {
		return wayPointDistanceNext;
	}


	public void setWayPointDistanceNext(Integer wayPointDistanceNext) {
		this.wayPointDistanceNext = wayPointDistanceNext;
	}


	public Date getEtaNextWayPoint() {
		return etaNextWayPoint;
	}


	public void setEtaNextWayPoint(Date etaNextWayPoint) {
		this.etaNextWayPoint = etaNextWayPoint;
	}


	public Double getCrossTrackError() {
		return crossTrackError;
	}


	public void setCrossTrackError(Double crossTrackError) {
		this.crossTrackError = crossTrackError;
	}




	public String getRemarks() {
		return remarks;
	}


	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	public boolean isCourseChecked() {
		return courseChecked;
	}
	public void setCourseChecked(boolean courseChecked) {
		this.courseChecked = courseChecked;
	}
	public boolean isTrackWithAutoPilot() {
		return trackWithAutoPilot;
	}
	public void setTrackWithAutoPilot(boolean trackWithAutoPilot) {
		this.trackWithAutoPilot = trackWithAutoPilot;
	}
	public boolean isPositionCompared() {
		return positionCompared;
	}
	public void setPositionCompared(boolean positionCompared) {
		this.positionCompared = positionCompared;
	}
	public boolean isSpeedCompared() {
		return speedCompared;
	}
	public void setSpeedCompared(boolean speedCompared) {
		this.speedCompared = speedCompared;
	}
	public boolean isDgps1Compare() {
		return dgps1Compare;
	}
	public void setDgps1Compare(boolean dgps1Compare) {
		this.dgps1Compare = dgps1Compare;
	}
	public boolean isDgps2Compare() {
		return dgps2Compare;
	}
	public void setDgps2Compare(boolean dgps2Compare) {
		this.dgps2Compare = dgps2Compare;
	}
	public boolean isWayPointControlled() {
		return wayPointControlled;
	}
	public void setWayPointControlled(boolean wayPointControlled) {
		this.wayPointControlled = wayPointControlled;
	}
	public boolean isConfigurationCalibratic() {
		return configurationCalibratic;
	}
	public void setConfigurationCalibratic(boolean configurationCalibratic) {
		this.configurationCalibratic = configurationCalibratic;
	}
	public void setPerformanceGood(boolean performanceGood) {
		this.performanceGood = performanceGood;
	}
	public boolean isPerformanceGood() {
		return performanceGood;
	}
	@Override
	public String toString() {
		return "DecklogEcdis [id=" + id + ", uuid=" + uuid + ", watch=" + watch
				+ ", lastNtm=" + lastNtm + ", ntmDate=" + ntmDate
				+ ", wayPointNr=" + wayPointNr + ", wayPointDistanceNext="
				+ wayPointDistanceNext + ", etaNextWayPoint=" + etaNextWayPoint
				+ ", crossTrackError=" + crossTrackError + ", performanceGood="
				+ performanceGood + ", courseChecked=" + courseChecked
				+ ", trackWithAutoPilot=" + trackWithAutoPilot
				+ ", positionCompared=" + positionCompared + ", speedCompared="
				+ speedCompared + ", dgps1Compare=" + dgps1Compare
				+ ", dgps2Compare=" + dgps2Compare + ", wayPointControlled="
				+ wayPointControlled + ", configurationCalibratic="
				+ configurationCalibratic + ", remarks=" + remarks + "]";
	}
	
	
	
}
