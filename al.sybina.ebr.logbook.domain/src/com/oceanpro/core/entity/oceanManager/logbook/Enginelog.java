package com.oceanpro.core.entity.oceanManager.logbook;

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
import javax.persistence.Table;

import com.oceanpro.core.entity.oceanManager.office.Ship;


@Entity
@Table(name="ocean_logbook_enginelog")
public class Enginelog {
	
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
	

	@OneToMany(targetEntity=EnginelogExaustGasTemp.class, cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="engine", fetch=FetchType.LAZY)
	private List<EnginelogExaustGasTemp> engineExausteGas;

	@OneToMany(targetEntity=EnginelogJackCoolingWater.class, cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="engine", fetch=FetchType.LAZY)
	private List<EnginelogJackCoolingWater> engineJackCooling;
	
	@OneToMany(targetEntity=EnginelogPistonCoolingTemp.class, cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="engine", fetch=FetchType.LAZY)
	private List<EnginelogPistonCoolingTemp> enginePistonCooling;
	
	@OneToMany(targetEntity=EnginelogScavangeAirTemp.class, cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="engine", fetch=FetchType.LAZY)
	private List<EnginelogScavangeAirTemp> engineScavingAir;
	
	
	@Basic
	private Date registrationDate;
	
	
	/**
	 * Costruttore vuoto
	 */
	public Enginelog() {
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


	public Date getRegistrationDate() {
		return registrationDate;
	}


	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}


	@Override
	public String toString() {
		return "Enginelog [id=" + id + ", uuid=" + uuid + ", registrationDate="
				+ registrationDate + "]";
	}


	public List<EnginelogExaustGasTemp> getEngineExausteGas() {
		return engineExausteGas;
	}


	public void setEngineExausteGas(List<EnginelogExaustGasTemp> engineExausteGas) {
		this.engineExausteGas = engineExausteGas;
	}


	public List<EnginelogJackCoolingWater> getEngineJackCooling() {
		return engineJackCooling;
	}


	public void setEngineJackCooling(
			List<EnginelogJackCoolingWater> engineJackCooling) {
		this.engineJackCooling = engineJackCooling;
	}


	public List<EnginelogPistonCoolingTemp> getEnginePistonCooling() {
		return enginePistonCooling;
	}


	public void setEnginePistonCooling(
			List<EnginelogPistonCoolingTemp> enginePistonCooling) {
		this.enginePistonCooling = enginePistonCooling;
	}


	public List<EnginelogScavangeAirTemp> getEngineScavingAir() {
		return engineScavingAir;
	}


	public void setEngineScavingAir(List<EnginelogScavangeAirTemp> engineScavingAir) {
		this.engineScavingAir = engineScavingAir;
	}


	public Ship getShip() {
		return ship;
	}


	public void setShip(Ship ship) {
		this.ship = ship;
	}
	
	
}
