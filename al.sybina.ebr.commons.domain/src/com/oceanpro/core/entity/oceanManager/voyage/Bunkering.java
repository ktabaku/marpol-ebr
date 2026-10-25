package com.oceanpro.core.entity.oceanManager.voyage;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
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
 * Class name: Bunker
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
@javax.persistence.Table(name="ocean_voyage_bunkering")
public class Bunkering implements Comparable<Bunkering>{


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
	
	/*
	@ManyToOne (fetch = FetchType.EAGER) // No cascade
	private VoyagePlan voyage;
	*/
	
	@Basic
	private Date registrationDate;
	
	
	@CollectionTable(name="ocean_voyage_bunkering_attachments")
	@ElementCollection(fetch=FetchType.EAGER, targetClass=String.class)
	@OneToMany(cascade={CascadeType.ALL, CascadeType.REMOVE})
	private List<String> attachments;
	
	
	public List<String> getAttachments() {
		return attachments;
	}




	public void setAttachments(List<String> attachments) {
		this.attachments = attachments;
	}

	@Basic
	private String type;
	
	@Basic
	private String place;
	
	@Basic
	private String bargeTerminalName;
	
	@Basic
	private String supplier;
	
	
	@Basic
	private String gradeName;
	
	@Basic
	private Double viscosity;
	
	@Basic
	private Double density;
	
	@Basic
	private Double poorPoint;
	
	
	@Basic
	private Double flashPoint;
	
	@Basic
	private Double solphur;
	
	@Basic
	private Double water;
	
	@Basic
	private String receptRefBunker;
	
	@Basic
	private String sealNro;
	
	@Basic
	private Double temperature;
	
	@Basic
	private Double quantityMtCube;
	
	@Basic
	private Double quantityMt;
	
	
	@Basic
	private Double robMt;
	
	


//	@Column(columnDefinition="INT(1)")
//	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Basic
	private boolean registeredInNoon;
	

	@Column (length=5000) // 5000 caratteri
	private String remark;
	
	

	
	/**
	 * Costruttore vuoto
	 */
	public Bunkering() {
		super();
	}
	
	

	
	@Override
	public boolean equals(Object obj) {
		Bunkering rhs = (Bunkering) obj;
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
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getBargeTerminalName() {
		return bargeTerminalName;
	}

	public void setBargeTerminalName(String bargeTerminalName) {
		this.bargeTerminalName = bargeTerminalName;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public Double getViscosity() {
		return viscosity;
	}

	public void setViscosity(Double viscosity) {
		this.viscosity = viscosity;
	}

	public Double getDensity() {
		return density;
	}

	public void setDensity(Double density) {
		this.density = density;
	}

	public Double getPoorPoint() {
		return poorPoint;
	}

	public void setPoorPoint(Double poorPoint) {
		this.poorPoint = poorPoint;
	}

	public Double getFlashPoint() {
		return flashPoint;
	}

	public void setFlashPoint(Double flashPoint) {
		this.flashPoint = flashPoint;
	}

	public Double getSolphur() {
		return solphur;
	}

	public void setSolphur(Double solphur) {
		this.solphur = solphur;
	}

	public Double getWater() {
		return water;
	}

	public void setWater(Double water) {
		this.water = water;
	}

	public String getReceptRefBunker() {
		return receptRefBunker;
	}

	public void setReceptRefBunker(String receptRefBunker) {
		this.receptRefBunker = receptRefBunker;
	}

	public String getSealNro() {
		return sealNro;
	}

	public void setSealNro(String sealNro) {
		this.sealNro = sealNro;
	}

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	public Double getQuantityMtCube() {
		return quantityMtCube;
	}

	public void setQuantityMtCube(Double quantityMtCube) {
		this.quantityMtCube = quantityMtCube;
	}

	public Double getQuantityMt() {
		return quantityMt;
	}

	public void setQuantityMt(Double quantityMt) {
		this.quantityMt = quantityMt;
	}
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public Ship getShip() {
		return ship;
	}


	public void setShip(Ship ship) {
		this.ship = ship;
	}
	
	/*
	public VoyagePlan getVoyage() {
		return voyage;
	}

	public void setVoyage(VoyagePlan voyage) {
		this.voyage = voyage;
	}
*/

	public boolean isRegisteredInNoon() {
		return registeredInNoon;
	}

	public void setRegisteredInNoon(boolean registeredInNoon) {
		this.registeredInNoon = registeredInNoon;
	}
	
	public Double getRobMt() {
		return robMt;
	}
	public void setRobMt(Double robMt) {
		this.robMt = robMt;
	}
	

	public double calculateBunkerConsuptionForType(List<MachineriesBunkerStatus> mbs){
		
		double totMbs = 0.0;
		
		for (MachineriesBunkerStatus m : mbs) {
			totMbs += m.getConsuption();
		}
		
		return totMbs;
	}
	
	public double calculateTotalBunker(List<Bunkering> bunks){
		
		double totBunk = 0.0;
		
		for (Bunkering b : bunks) {
			totBunk += b.getQuantityMt();
		}
		
		return totBunk;
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Bunkering comp) {
		 if (getRegistrationDate()!=null && comp.getRegistrationDate()!=null && getRegistrationDate().before(comp.getRegistrationDate())) return -1; 
		 else if (getRegistrationDate()!=null && comp.getRegistrationDate()!=null && getRegistrationDate().equals(comp.getRegistrationDate())) return 0;
		 else return 1;
	}













}