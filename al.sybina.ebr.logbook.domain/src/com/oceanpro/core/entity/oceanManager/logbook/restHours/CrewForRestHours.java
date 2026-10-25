package com.oceanpro.core.entity.oceanManager.logbook.restHours;


import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;


import com.oceanpro.core.entity.oceanManager.office.Ship;
import com.oceanpro.core.entity.oceanManager.portDocuments.Rank;

/**
 * 
 * @author Gio
 *
 */
@Entity
@Table(name="ocean_logbook_rest_hours_crew")
public class CrewForRestHours {

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
	private String name;
	@Basic
	private String surname;
	@Basic
	private String nationality;

	

//	@OneToOne(fetch=FetchType.LAZY)
//	private RestData restData;
	
	@Basic
	private Date dateSignOn;
	@Basic
	private Date dateSignOff;
	
	@OneToOne(fetch=FetchType.EAGER)
	private Rank rank;
	
	// ************* Guardie
	@Column
	private String workerType; // dayWorker, watchkeeping
	
	@Basic
	private Integer workFrom1;
	@Basic
	private Integer workTo1;
	@Basic
	private Integer workFrom2;
	@Basic
	private Integer workTo2;
	// ******************
		
		
	
	/**
	 * Costruttore vuoto
	 */
	public CrewForRestHours() {
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



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getSurname() {
		return surname;
	}



	public void setSurname(String surname) {
		this.surname = surname;
	}



	public String getNationality() {
		return nationality;
	}



	public void setNationality(String nationality) {
		this.nationality = nationality;
	}



	public Date getDateSignOn() {
		return dateSignOn;
	}



	public void setDateSignOn(Date dateSignOn) {
		this.dateSignOn = dateSignOn;
	}



	public Date getDateSignOff() {
		return dateSignOff;
	}



	public void setDateSignOff(Date dateSignOff) {
		this.dateSignOff = dateSignOff;
	}



	public Rank getRank() {
		return rank;
	}



	public void setRank(Rank rank) {
		this.rank = rank;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CrewForRestHours other = (CrewForRestHours) obj;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}


//	public RestData getRestData() {
//		return restData;
//	}
//
//
//
//	public void setRestData(RestData restData) {
//		this.restData = restData;
//	}






	public String getWorkerType() {
		return workerType;
	}



	public void setWorkerType(String workerType) {
		this.workerType = workerType;
	}



	public Integer getWorkFrom1() {
		return workFrom1;
	}



	public void setWorkFrom1(Integer workFrom1) {
		this.workFrom1 = workFrom1;
	}



	public Integer getWorkTo1() {
		return workTo1;
	}



	public void setWorkTo1(Integer workTo1) {
		this.workTo1 = workTo1;
	}



	public Integer getWorkFrom2() {
		return workFrom2;
	}



	public void setWorkFrom2(Integer workFrom2) {
		this.workFrom2 = workFrom2;
	}



	public Integer getWorkTo2() {
		return workTo2;
	}



	public void setWorkTo2(Integer workTo2) {
		this.workTo2 = workTo2;
	}



	public Ship getShip() {
		return ship;
	}



	public void setShip(Ship ship) {
		this.ship = ship;
	}


	
	
}