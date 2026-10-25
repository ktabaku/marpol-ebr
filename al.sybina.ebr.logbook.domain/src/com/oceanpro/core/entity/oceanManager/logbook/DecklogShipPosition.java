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

import com.oceanpro.core.entity.oceanManager.voyage.ShipPerformanceInstance;
import com.oceanpro.core.entity.oceanManager.voyage.VoyagePath;

@Entity
@Table(name="ocean_logbook_decklog_position")
public class DecklogShipPosition implements Comparable<DecklogShipPosition>{

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long id ; // Unique ID
	
	@ManyToOne (fetch = FetchType.EAGER) // No cascade
	private Decklog deck;

	@Basic
	private String uuid;
	
	@Basic
	private Date posDate;
	
	@Basic
	private String latitude;
	
	@Basic
	private String longitude;
	
	@Basic
	private Double course;
	
	@Basic
	private Double speed;
	
	@Basic
	private Double avSpeed;
	
	@Basic
	private Double distance;
	
	@Basic
	private Double avDistance;
	
	@Column (length=5000) // 5000 caratteri
	private String remarks;
	
	
	
	/**
	 * Costruttore vuoto
	 */
	public DecklogShipPosition() {
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



	public Date getPosDate() {
		return posDate;
	}



	public void setPosDate(Date posDate) {
		this.posDate = posDate;
	}



	public Double getCourse() {
		return course;
	}



	public void setCourse(Double course) {
		this.course = course;
	}



	public Double getSpeed() {
		return speed;
	}



	public void setSpeed(Double speed) {
		this.speed = speed;
	}



	public Double getAvSpeed() {
		return avSpeed;
	}



	public void setAvSpeed(Double avSpeed) {
		this.avSpeed = avSpeed;
	}



	public Double getDistance() {
		return distance;
	}



	public void setDistance(Double distance) {
		this.distance = distance;
	}



	public Double getAvDistance() {
		return avDistance;
	}



	public void setAvDistance(Double avDistance) {
		this.avDistance = avDistance;
	}



	public String getRemarks() {
		return remarks;
	}



	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}



	public String getLatitude() {
		return latitude;
	}



	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}



	public String getLongitude() {
		return longitude;
	}



	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}



	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(DecklogShipPosition comp) {
		 if (getPosDate()!=null && comp.getPosDate()!=null && getPosDate().after(comp.getPosDate())) return -1; 
		 else if (getPosDate()!=null && comp.getPosDate()!=null && getPosDate().equals(comp.getPosDate())) return 0;
		 else return 1;
	}



	@Override
	public String toString() {
		return "DecklogShipPosition [id=" + id + ", deck=" + deck + ", uuid="
				+ uuid + ", posDate=" + posDate + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", course=" + course
				+ ", speed=" + speed + ", avSpeed=" + avSpeed + ", distance="
				+ distance + ", avDistance=" + avDistance + ", remarks="
				+ remarks + "]";
	}
	
	

	

}
