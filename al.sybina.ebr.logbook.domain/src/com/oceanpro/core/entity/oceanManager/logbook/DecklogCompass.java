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
@Table(name="ocean_logbook_decklog_compass")
public class DecklogCompass implements Comparable<DecklogCompass>{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long id ; // Unique ID
	
	@ManyToOne (fetch = FetchType.EAGER) // No cascade
	private Decklog deck;

	@Basic
	private String uuid;
	
	@Basic
	private Date compaDate;
	
	@Basic
	private String latitude;
	
	@Basic
	private String longitude;
	
	
	
	//HEADING
	@Basic
	private Double headingGyro;
	
	@Basic
	private Double headingStandard;
	
	
	//OBJECT BEARING
	@Basic
	private String bearingObserved;
	
	@Basic
	private Double bearingGyro;
	
	@Basic
	private Double bearingStandard;

	@Basic
	private Double bearingTrue;
	
	
	//ERROR
	@Basic
	private Double errorGyro;
	
	@Basic
	private Double errorStandard;
	
	
	
	@Basic
	private Double variation;
	
	@Basic
	private Double deviation;
	
	@Basic
	private String repeater;
	
	
	
	
	
	
	@Column (length=5000) // 5000 caratteri
	private String remarks;
	
	
	
	/**
	 * Costruttore vuoto
	 */
	public DecklogCompass() {
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



	public Date getCompaDate() {
		return compaDate;
	}



	public void setCompaDate(Date compaDate) {
		this.compaDate = compaDate;
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



	public String getRemarks() {
		return remarks;
	}



	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}



	public Double getHeadingGyro() {
		return headingGyro;
	}



	public void setHeadingGyro(Double headingGyro) {
		this.headingGyro = headingGyro;
	}



	public Double getHeadingStandard() {
		return headingStandard;
	}



	public void setHeadingStandard(Double headingStandard) {
		this.headingStandard = headingStandard;
	}



	public String getBearingObserved() {
		return bearingObserved;
	}



	public void setBearingObserved(String bearingObserved) {
		this.bearingObserved = bearingObserved;
	}



	public Double getBearingGyro() {
		return bearingGyro;
	}



	public void setBearingGyro(Double bearingGyro) {
		this.bearingGyro = bearingGyro;
	}



	public Double getBearingStandard() {
		return bearingStandard;
	}



	public void setBearingStandard(Double bearingStandard) {
		this.bearingStandard = bearingStandard;
	}



	public Double getBearingTrue() {
		return bearingTrue;
	}



	public void setBearingTrue(Double bearingTrue) {
		this.bearingTrue = bearingTrue;
	}



	public Double getErrorGyro() {
		return errorGyro;
	}



	public void setErrorGyro(Double errorGyro) {
		this.errorGyro = errorGyro;
	}



	public Double getErrorStandard() {
		return errorStandard;
	}



	public void setErrorStandard(Double errorStandard) {
		this.errorStandard = errorStandard;
	}



	public Double getVariation() {
		return variation;
	}



	public void setVariation(Double variation) {
		this.variation = variation;
	}



	public Double getDeviation() {
		return deviation;
	}



	public void setDeviation(Double deviation) {
		this.deviation = deviation;
	}



	public String getRepeater() {
		return repeater;
	}



	public void setRepeater(String repeater) {
		this.repeater = repeater;
	}



	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(DecklogCompass comp) {
		 if (getCompaDate()!=null && comp.getCompaDate()!=null && getCompaDate().after(comp.getCompaDate())) return -1; 
		 else if (getCompaDate()!=null && comp.getCompaDate()!=null && getCompaDate().equals(comp.getCompaDate())) return 0;
		 else return 1;
	}



	@Override
	public String toString() {
		return "DecklogCompass [id=" + id + ", uuid=" + uuid + ", compaDate="
				+ compaDate + ", latitude=" + latitude + ", longitude="
				+ longitude + ", headingGyro=" + headingGyro
				+ ", headingStandard=" + headingStandard + ", bearingObserved="
				+ bearingObserved + ", bearingGyro=" + bearingGyro
				+ ", bearingStandard=" + bearingStandard + ", bearingTrue="
				+ bearingTrue + ", errorGyro=" + errorGyro + ", errorStandard="
				+ errorStandard + ", variation=" + variation + ", deviation="
				+ deviation + ", repeater=" + repeater + ", remarks=" + remarks
				+ "]";
	}
	

	
	
	
}
