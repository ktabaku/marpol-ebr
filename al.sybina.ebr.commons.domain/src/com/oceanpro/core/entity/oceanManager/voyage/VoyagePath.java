package com.oceanpro.core.entity.oceanManager.voyage;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * Class name: VoyagePath
 *
 * Description: 
 * 
 *
 * Company: 
 *
 * @author Gio
 * @date 05/feb/2013
 *
 */
@Entity
@javax.persistence.Table(name="ocean_voyage_voyagepath")
public class VoyagePath implements Comparable<VoyagePath>{


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long id ; // Unique ID
	
	@Basic
	private String uuid;


	/* Partenza */
	@Basic
	private Date departureDate;
	@Basic
	private String  departurePlace;
	/* **************** */
	
	@Basic
	private Double totalDistance; // in miglia nautiche
	
	/* Arrivo */
	@Basic
	private Date arrivalDate;
	@Basic
	private String arrivalPlace;
	/* **************** */
	
	/* Indica il tipo di leg: SEA, TRANSIT, PORT */ 
	@Basic
	private String type;
	




	@ManyToOne (fetch = FetchType.LAZY, optional= false) // No cascade
	private VoyagePlan voyage;
	
	/**
	 * Costruttore vuoto
	 */
	public VoyagePath() {
		super();
	}
	

	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	@Override
	public boolean equals(Object obj) {
		VoyagePath rhs = (VoyagePath) obj;
		if (uuid!=null && rhs.uuid!=null) return uuid.equals(rhs.uuid);
		boolean b = uuid==rhs.uuid;
		//System.out.println("id1=" + id + " id2=" + rhs.id +  " b="+b);
		return b;
		/*
		return new EqualsBuilder()
        //.appendSuper(super.equals(obj))
        .append(id, rhs.id)
        .append(name, rhs.name)
        .append(description, rhs.description)
        .isEquals();
		*/
		//return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}


	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(VoyagePath comp) {
		 if (getDepartureDate()!=null && comp.getDepartureDate()!=null && getDepartureDate().before(comp.getDepartureDate())) return -1; 
		 else if (getDepartureDate()!=null && comp.getDepartureDate()!=null && getDepartureDate().equals(comp.getDepartureDate())) return 0;
		 else return 1;
	}

	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}



	public Date getDepartureDate() {
		return departureDate;
	}



	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}



	public String getDeparturePlace() {
		return departurePlace;
	}



	public void setDeparturePlace(String departurePlace) {
		this.departurePlace = departurePlace;
	}



	public Double getTotalDistance() {
		return totalDistance;
	}



	public void setTotalDistance(Double totalDistance) {
		this.totalDistance = totalDistance;
	}



	public Date getArrivalDate() {
		return arrivalDate;
	}



	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}



	public String getArrivalPlace() {
		return arrivalPlace;
	}



	public void setArrivalPlace(String arrivalPlace) {
		this.arrivalPlace = arrivalPlace;
	}



	public VoyagePlan getVoyage() {
		return voyage;
	}



	public void setVoyage(VoyagePlan voyage) {
		this.voyage = voyage;
	}

	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

}