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
import javax.persistence.OneToMany;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *  Dati contratto
 *  
 * Class name: VoyageSettings
 *
 * Description: 
 * 
 *
 * Company: CrismaItalia
 *
 * @author Crisma
 * @date 05/feb/2013
 *
 */
@Entity
@javax.persistence.Table(name="ocean_voyage_settings")
public class VoyageSettings {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long id ; // Unique ID

	@Basic
	private Date layCan;
	@Basic
	private Double enginePower=0.0;
	@Basic
	private Double rpm=0.0;
	@Basic
	private Double warrantedShipSpeed=0.0;
	@Basic
	private Double warrantedFoConsumption=0.0;
	@Basic
	private Double warrantedDoConsumption=0.0;
	@Basic
	private Double maxWindBf=5.0;
	@Basic
	private Double maxSeaDg=4.0;
	
	//in MT
	@Basic
	private Double cargoQuantity =0.0;
	@Basic
	private Double cargoDraftFwd =0.0;
	@Basic
	private Double cargoDraftAft =0.0;
	@Basic
	private Double displacement = 0.0;
	
	//in M3
	@Basic
	private Double cargoQuantityMetrerCube =0.0;
	@Basic
	private String cargoType;	
	
	
	//cambiati per cambio tipo bunker da hsfo,lsfo,hsdo,lsdo a lfo,hfo,mdo
//	@Basic
//	private Double departureTotalHsfo =0.0; // Totale carburante a inizio viaggio
//	@Basic
//	private Double departureTotalLsfo =0.0;
//	@Basic
//	private Double departureTotalHsdo =0.0;
//	@Basic
//	private Double departureTotalLsdo =0.0;
		
	@Basic
	private Double departureTotalHfo =0.0; // Totale carburante a inizio viaggio
	@Basic
	private Double departureTotalLfo =0.0;
	@Basic
	private Double departureTotalMdo =0.0;	
	
	@Basic
	private Double departureTotalFw =0.0;
	@Basic
	private Double departureTotalLo =0.0;
	
//	@ElementCollection(fetch=FetchType.EAGER)
//    @MapKeyColumn(name="date")
//    @Column(nullable=true, name="shipPerformanceMatrix_id")
//    @CollectionTable( name="ocean_voyage_settings_ship_performance_matrix", joinColumns=@JoinColumn(name="voyage_settings_id"))
//	Map<Date,ShipPerformanceMatrix> shipPerformanceMatrix = new HashMap<Date, ShipPerformanceMatrix>();
	
	@OneToMany(targetEntity=ShipPerformanceInstance.class, cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="voyageSettings", fetch=FetchType.EAGER)
	private List<ShipPerformanceInstance> shipPerformances;
	
	
//	@Column(columnDefinition="INT(1)")
//	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Basic
	private boolean calculateCurrent=true;
	
	
	
	/**
	 * Costruttore vuoto
	 */
	public VoyageSettings() {
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
		VoyageSettings rhs = (VoyageSettings) obj;
		if (id!=null && rhs.id!=null) return id.equals(rhs.id);
		boolean b = id==rhs.id;
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

	public Double getEnginePower() {
		return enginePower;
	}


	public void setEnginePower(Double enginePower) {
		this.enginePower = enginePower;
	}


	public Double getRpm() {
		return rpm;
	}


	public void setRpm(Double rpm) {
		this.rpm = rpm;
	}


	public Date getLayCan() {
		return layCan;
	}


	public void setLayCan(Date layCan) {
		this.layCan = layCan;
	}


	public Double getWarrantedShipSpeed() {
		return warrantedShipSpeed;
	}


	public void setWarrantedShipSpeed(Double warrantedShipSpeed) {
		this.warrantedShipSpeed = warrantedShipSpeed;
	}


	public Double getWarrantedFoConsumption() {
		return warrantedFoConsumption;
	}


	public void setWarrantedFoConsumption(Double warrantedFoConsumption) {
		this.warrantedFoConsumption = warrantedFoConsumption;
	}


	public Double getWarrantedDoConsumption() {
		return warrantedDoConsumption;
	}


	public void setWarrantedDoConsumption(Double warrantedDoConsumption) {
		this.warrantedDoConsumption = warrantedDoConsumption;
	}


	public Double getMaxWindBf() {
		return maxWindBf;
	}


	public void setMaxWindBf(Double maxWindBf) {
		this.maxWindBf = maxWindBf;
	}


	public Double getMaxSeaDg() {
		return maxSeaDg;
	}


	public void setMaxSeaDg(Double maxSeaDg) {
		this.maxSeaDg = maxSeaDg;
	}


	public Double getCargoQuantity() {
		return cargoQuantity;
	}


	public void setCargoQuantity(Double cargoQuantity) {
		this.cargoQuantity = cargoQuantity;
	}


	public Double getCargoDraftFwd() {
		return cargoDraftFwd;
	}


	public void setCargoDraftFwd(Double cargoDraftFwd) {
		this.cargoDraftFwd = cargoDraftFwd;
	}


	public void setCargoDraftAft(Double cargoDraftAft) {
		this.cargoDraftAft = cargoDraftAft;
	}


	public Double getCargoDraftAft() {
		return cargoDraftAft;
	}


	public boolean isCalculateCurrent() {
		return calculateCurrent;
	}


	public void setCalculateCurrent(boolean calculateCurrent) {
		this.calculateCurrent = calculateCurrent;
	}


//	public Double getDepartureTotalHsfo() {
//		return departureTotalHsfo;
//	}
//
//
//	public void setDepartureTotalHsfo(Double departureTotalHsfo) {
//		this.departureTotalHsfo = departureTotalHsfo;
//	}
//
//
//	public Double getDepartureTotalLsfo() {
//		return departureTotalLsfo;
//	}
//
//
//	public void setDepartureTotalLsfo(Double departureTotalLsfo) {
//		this.departureTotalLsfo = departureTotalLsfo;
//	}
//
//
//	public Double getDepartureTotalHsdo() {
//		return departureTotalHsdo;
//	}
//
//
//	public void setDepartureTotalHsdo(Double departureTotalHsdo) {
//		this.departureTotalHsdo = departureTotalHsdo;
//	}
//
//
//	public Double getDepartureTotalLsdo() {
//		return departureTotalLsdo;
//	}
//
//
//	public void setDepartureTotalLsdo(Double departureTotalLsdo) {
//		this.departureTotalLsdo = departureTotalLsdo;
//	}



	public Double getDepartureTotalHfo() {
		return departureTotalHfo;
	}


	public void setDepartureTotalHfo(Double departureTotalHfo) {
		this.departureTotalHfo = departureTotalHfo;
	}


	public Double getDepartureTotalLfo() {
		return departureTotalLfo;
	}


	public void setDepartureTotalLfo(Double departureTotalLfo) {
		this.departureTotalLfo = departureTotalLfo;
	}


	public Double getDepartureTotalMdo() {
		return departureTotalMdo;
	}


	public void setDepartureTotalMdo(Double departureTotalMdo) {
		this.departureTotalMdo = departureTotalMdo;
	}


	
	public Double getDisplacement() {
		return displacement;
	}
	
	public void setDisplacement(Double displacement) {
		this.displacement = displacement;
	}


	public Double getDepartureTotalFw() {
		return departureTotalFw;
	}


	public void setDepartureTotalFw(Double departureTotalFw) {
		this.departureTotalFw = departureTotalFw;
	}


	public Double getDepartureTotalLo() {
		return departureTotalLo;
	}


	public void setDepartureTotalLo(Double departureTotalLo) {
		this.departureTotalLo = departureTotalLo;
	}


	public List<ShipPerformanceInstance> getShipPerformances() {
		return shipPerformances;
	}


	public void setShipPerformances(List<ShipPerformanceInstance> shipPerformances) {
		this.shipPerformances = shipPerformances;
	}
	
	public String getCargoType() {
		return cargoType;
	}
	
	public void setCargoType(String cargoType) {
		this.cargoType = cargoType;
	}


	public Double getCargoQuantityMetrerCube() {
		return cargoQuantityMetrerCube;
	}


	public void setCargoQuantityMetrerCube(Double cargoQuantityMetrerCube) {
		this.cargoQuantityMetrerCube = cargoQuantityMetrerCube;
	}
	
	
}