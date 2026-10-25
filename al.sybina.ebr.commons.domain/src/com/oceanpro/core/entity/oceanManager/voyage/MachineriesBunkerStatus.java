package com.oceanpro.core.entity.oceanManager.voyage;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.oceanpro.core.entity.oceanManager.office.Ship;


/**
 * 
 * Class name: MachineriesBunkerStatus
 *
 * Description: 
 * 
 *
 * Company: 
 *
 * @author 
 * @date 24/feb/2016
 *
 */
@Entity
@javax.persistence.Table(name="ocean_voyage_machieries_bunker")
public class MachineriesBunkerStatus implements Comparable<MachineriesBunkerStatus>{

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

	
//	@Column(columnDefinition="INT(1)")
//	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Basic
	private boolean active;

	@Basic
	private String machinary;

	@Basic
	private String typeFuel;
	
	@Basic
	private Date startUseFuel;
	
	@Basic
	private Date stopUseFuel;	
	
	
	//Add consumi
	@Basic
	private Double consuption;	
	
	@Basic
	private Double qob;	

	@Basic
	private Double rob;	



	/*
	 * 
	 * 	
	 *  */
	//cambiati per cambio tipo bunker da hsfo,lsfo,hsdo,lsdo a lfo,hfo,mdo
//	@Basic
//	private Double voyDepartureTotalHsfo =0.0; // Totale carburante a inizio viaggio
//	@Basic
//	private Double voyDepartureTotalLsfo =0.0;
//	@Basic
//	private Double voyDepartureTotalHsdo =0.0;
//	@Basic
//	private Double voyDepartureTotalLsdo =0.0;
	@Basic
	private Double voyDepartureTotalHfo =0.0; // Totale carburante a inizio viaggio
	@Basic
	private Double voyDepartureTotalLfo =0.0;
	@Basic
	private Double voyDepartureTotalMdo =0.0;
	
	
	@Basic
	private Date voyageCommenceDate;
	
	
	






//	@Column(columnDefinition="INT(1)")
//	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Basic
	private boolean consCalculated;

	
	/**
	 * Costruttore vuoto
	 */
	public MachineriesBunkerStatus() {
		super();
	}
	
	public MachineriesBunkerStatus(boolean active, String machinary) {
		super();
		this.id = null;
		this.uuid = null;
		
		this.machinary = machinary;
		this.active = active; 
		
		this.typeFuel = null;
//		this.startUseFuel = EasyDateUtils.getGMT0CurrentDate();
//		this.stopUseFuel = EasyDateUtils.getGMT0CurrentDate();
		this.startUseFuel = null;
		this.stopUseFuel = null;
		
		
		this.consuption = 0.0;
		this.consCalculated = false;
		
		this.qob = 0.0;
		this.rob = 0.0;
	}
	
	public MachineriesBunkerStatus(MachineriesBunkerStatus mbs) {
		super();
		this.id = mbs.id;
		this.uuid = mbs.uuid;
		this.active = mbs.active; 
		this.machinary = mbs.machinary;
		this.typeFuel = mbs.typeFuel;
		this.startUseFuel = mbs.startUseFuel;
		this.stopUseFuel = mbs.stopUseFuel;
		
		this.consuption = mbs.consuption;
		this.consCalculated = mbs.consCalculated;
		
		this.qob = mbs.qob;
		this.rob = mbs.rob;
	}
	
	public MachineriesBunkerStatus(Long i, String uuid, boolean active, String machinary, String typeFuel, Date startUseFuel, Date stopUseFuel, Double consuption, boolean consCalculated, Double qob, Double rob) {
		super();
		this.id = i;
		this.uuid = uuid;
		this.active = active; 
		this.machinary = machinary;
		this.typeFuel = typeFuel;
		this.startUseFuel = startUseFuel;
		this.stopUseFuel = stopUseFuel;
		
		this.consuption = consuption;
		this.consCalculated = consCalculated;
		
		this.qob = qob;
		this.rob = rob;
	}

	
	@Override
	public boolean equals(Object obj) {
		MachineriesBunkerStatus rhs = (MachineriesBunkerStatus) obj;
		if (uuid!=null && rhs.uuid!=null) return uuid.equals(rhs.uuid);
		boolean b = uuid==rhs.uuid;
		//System.out.println("id1=" + id + " id2=" + rhs.id +  " b="+b);
		return b;
		
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
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

	public Ship getShip() {
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}


	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getMachinary() {
		return machinary;
	}
	public void setMachinary(String machinary) {
		this.machinary = machinary;
	}
	public String getTypeFuel() {
		return typeFuel;
	}
	public void setTypeFuel(String typeFuel) {
		this.typeFuel = typeFuel;
	}
	public Date getStartUseFuel() {
		return startUseFuel;
	}
	public void setStartUseFuel(Date startUseFuel) {
		this.startUseFuel = startUseFuel;
	}
	public Date getStopUseFuel() {
		return stopUseFuel;
	}
	public void setStopUseFuel(Date stopUseFuel) {
		this.stopUseFuel = stopUseFuel;
	}

	public Double getConsuption() {
		return consuption;
	}
	
	public void setConsuption(Double consuption) {
		this.consuption = consuption;
	}

	public boolean isConsCalculated() {
		return consCalculated;
	}

	public void setConsCalculated(boolean consCalculated) {
		this.consCalculated = consCalculated;
	}
	
	public Double getQob() {
		return qob;
	}

	public void setQob(Double qob) {
		this.qob = qob;
	}

//	public Double getVoyDepartureTotalHsfo() {
//		return voyDepartureTotalHsfo;
//	}
//
//	public void setVoyDepartureTotalHsfo(Double voyDepartureTotalHsfo) {
//		this.voyDepartureTotalHsfo = voyDepartureTotalHsfo;
//	}
//
//	public Double getVoyDepartureTotalLsfo() {
//		return voyDepartureTotalLsfo;
//	}
//
//	public void setVoyDepartureTotalLsfo(Double voyDepartureTotalLsfo) {
//		this.voyDepartureTotalLsfo = voyDepartureTotalLsfo;
//	}
//
//	public Double getVoyDepartureTotalHsdo() {
//		return voyDepartureTotalHsdo;
//	}
//
//	public void setVoyDepartureTotalHsdo(Double voyDepartureTotalHsdo) {
//		this.voyDepartureTotalHsdo = voyDepartureTotalHsdo;
//	}
//
//	public Double getVoyDepartureTotalLsdo() {
//		return voyDepartureTotalLsdo;
//	}
//
//	public void setVoyDepartureTotalLsdo(Double voyDepartureTotalLsdo) {
//		this.voyDepartureTotalLsdo = voyDepartureTotalLsdo;
//	}
	

	public Date getVoyageCommenceDate() {
		return voyageCommenceDate;
	}

	public Double getVoyDepartureTotalHfo() {
		return voyDepartureTotalHfo;
	}

	public void setVoyDepartureTotalHfo(Double voyDepartureTotalHfo) {
		this.voyDepartureTotalHfo = voyDepartureTotalHfo;
	}

	public Double getVoyDepartureTotalLfo() {
		return voyDepartureTotalLfo;
	}

	public void setVoyDepartureTotalLfo(Double voyDepartureTotalLfo) {
		this.voyDepartureTotalLfo = voyDepartureTotalLfo;
	}

	public Double getVoyDepartureTotalMdo() {
		return voyDepartureTotalMdo;
	}

	public void setVoyDepartureTotalMdo(Double voyDepartureTotalMdo) {
		this.voyDepartureTotalMdo = voyDepartureTotalMdo;
	}

	
	
	
	public void setVoyageCommenceDate(Date voyageCommenceDate) {
		this.voyageCommenceDate = voyageCommenceDate;
	}

	public Double getRob() {
		return rob;
	}

	public void setRob(Double rob) {
		this.rob = rob;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(MachineriesBunkerStatus comp) {
		 if (getId()!=null && comp.getId()!=null && getId()<comp.getId()) return -1; 
		 else if (getId()!=null && comp.getId()!=null && getId().equals(comp.getId())) return 0;
		 else return 1;
	}













}