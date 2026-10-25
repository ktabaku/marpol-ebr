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
 *  Consumi Machinary Inserimento Manuale
 *  
 * Class name: VoyageConsuptionManualEntry
 *
 * Description: 
 * 
 *
 */
@Entity
@javax.persistence.Table(name="ocean_voyage_consuption_manual_entry")
public class VoyageConsuptionManualEntry {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long id ; // Unique ID

	
	//Other Machinary Ins Manuale(in MT)
	private Double igsSeaConsMtHfo =0.0;
	private Double igsSeaConsMtLfo =0.0;
	private Double igsSeaConsMtMdo =0.0;
	private Double igsTransitConsMtHfo =0.0;
	private Double igsTransitConsMtLfo =0.0;
	private Double igsTransitConsMtMdo =0.0;
	private Double igsManouvreConsMtHfo =0.0;
	private Double igsManouvreConsMtLfo =0.0;
	private Double igsManouvreConsMtMdo =0.0;
	
	private Double compBoilerSeaConsMtHfo =0.0;
	private Double compBoilerSeaConsMtLfo =0.0;
	private Double compBoilerSeaConsMtMdo =0.0;
	private Double compBoilerTransitConsMtHfo =0.0;
	private Double compBoilerTransitConsMtLfo =0.0;
	private Double compBoilerTransitConsMtMdo =0.0;
	private Double compBoilerManouvreConsMtHfo =0.0;
	private Double compBoilerManouvreConsMtLfo =0.0;
	private Double compBoilerManouvreConsMtMdo =0.0;
	
	private Double powerPackSeaConsMtHfo =0.0;
	private Double powerPackSeaConsMtLfo =0.0;
	private Double powerPackSeaConsMtMdo =0.0;
	private Double powerPackTransitConsMtHfo =0.0;
	private Double powerPackTransitConsMtLfo =0.0;
	private Double powerPackTransitConsMtMdo =0.0;
	private Double powerPackManouvreConsMtHfo =0.0;
	private Double powerPackManouvreConsMtLfo =0.0;
	private Double powerPackManouvreConsMtMdo =0.0;
	
	/**
	 * Costruttore vuoto
	 */
	public VoyageConsuptionManualEntry() {
		super();
	}

	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	
	public Double getIgsSeaConsMtHfo() {
		return igsSeaConsMtHfo;
	}


	public void setIgsSeaConsMtHfo(Double igsSeaConsMtHfo) {
		this.igsSeaConsMtHfo = igsSeaConsMtHfo;
	}


	public Double getIgsSeaConsMtLfo() {
		return igsSeaConsMtLfo;
	}


	public void setIgsSeaConsMtLfo(Double igsSeaConsMtLfo) {
		this.igsSeaConsMtLfo = igsSeaConsMtLfo;
	}


	public Double getIgsSeaConsMtMdo() {
		return igsSeaConsMtMdo;
	}


	public void setIgsSeaConsMtMdo(Double igsSeaConsMtMdo) {
		this.igsSeaConsMtMdo = igsSeaConsMtMdo;
	}


	public Double getIgsTransitConsMtHfo() {
		return igsTransitConsMtHfo;
	}


	public void setIgsTransitConsMtHfo(Double igsTransitConsMtHfo) {
		this.igsTransitConsMtHfo = igsTransitConsMtHfo;
	}


	public Double getIgsTransitConsMtLfo() {
		return igsTransitConsMtLfo;
	}


	public void setIgsTransitConsMtLfo(Double igsTransitConsMtLfo) {
		this.igsTransitConsMtLfo = igsTransitConsMtLfo;
	}


	public Double getIgsTransitConsMtMdo() {
		return igsTransitConsMtMdo;
	}


	public void setIgsTransitConsMtMdo(Double igsTransitConsMtMdo) {
		this.igsTransitConsMtMdo = igsTransitConsMtMdo;
	}


	public Double getIgsManouvreConsMtHfo() {
		return igsManouvreConsMtHfo;
	}


	public void setIgsManouvreConsMtHfo(Double igsManouvreConsMtHfo) {
		this.igsManouvreConsMtHfo = igsManouvreConsMtHfo;
	}


	public Double getIgsManouvreConsMtLfo() {
		return igsManouvreConsMtLfo;
	}


	public void setIgsManouvreConsMtLfo(Double igsManouvreConsMtLfo) {
		this.igsManouvreConsMtLfo = igsManouvreConsMtLfo;
	}


	public Double getIgsManouvreConsMtMdo() {
		return igsManouvreConsMtMdo;
	}


	public void setIgsManouvreConsMtMdo(Double igsManouvreConsMtMdo) {
		this.igsManouvreConsMtMdo = igsManouvreConsMtMdo;
	}


	public Double getCompBoilerSeaConsMtHfo() {
		return compBoilerSeaConsMtHfo;
	}


	public void setCompBoilerSeaConsMtHfo(Double compBoilerSeaConsMtHfo) {
		this.compBoilerSeaConsMtHfo = compBoilerSeaConsMtHfo;
	}


	public Double getCompBoilerSeaConsMtLfo() {
		return compBoilerSeaConsMtLfo;
	}


	public void setCompBoilerSeaConsMtLfo(Double compBoilerSeaConsMtLfo) {
		this.compBoilerSeaConsMtLfo = compBoilerSeaConsMtLfo;
	}


	public Double getCompBoilerSeaConsMtMdo() {
		return compBoilerSeaConsMtMdo;
	}


	public void setCompBoilerSeaConsMtMdo(Double compBoilerSeaConsMtMdo) {
		this.compBoilerSeaConsMtMdo = compBoilerSeaConsMtMdo;
	}


	public Double getCompBoilerTransitConsMtHfo() {
		return compBoilerTransitConsMtHfo;
	}


	public void setCompBoilerTransitConsMtHfo(Double compBoilerTransitConsMtHfo) {
		this.compBoilerTransitConsMtHfo = compBoilerTransitConsMtHfo;
	}


	public Double getCompBoilerTransitConsMtLfo() {
		return compBoilerTransitConsMtLfo;
	}


	public void setCompBoilerTransitConsMtLfo(Double compBoilerTransitConsMtLfo) {
		this.compBoilerTransitConsMtLfo = compBoilerTransitConsMtLfo;
	}


	public Double getCompBoilerTransitConsMtMdo() {
		return compBoilerTransitConsMtMdo;
	}


	public void setCompBoilerTransitConsMtMdo(Double compBoilerTransitConsMtMdo) {
		this.compBoilerTransitConsMtMdo = compBoilerTransitConsMtMdo;
	}


	public Double getCompBoilerManouvreConsMtHfo() {
		return compBoilerManouvreConsMtHfo;
	}


	public void setCompBoilerManouvreConsMtHfo(Double compBoilerManouvreConsMtHfo) {
		this.compBoilerManouvreConsMtHfo = compBoilerManouvreConsMtHfo;
	}


	public Double getCompBoilerManouvreConsMtLfo() {
		return compBoilerManouvreConsMtLfo;
	}


	public void setCompBoilerManouvreConsMtLfo(Double compBoilerManouvreConsMtLfo) {
		this.compBoilerManouvreConsMtLfo = compBoilerManouvreConsMtLfo;
	}


	public Double getCompBoilerManouvreConsMtMdo() {
		return compBoilerManouvreConsMtMdo;
	}


	public void setCompBoilerManouvreConsMtMdo(Double compBoilerManouvreConsMtMdo) {
		this.compBoilerManouvreConsMtMdo = compBoilerManouvreConsMtMdo;
	}


	public Double getPowerPackSeaConsMtHfo() {
		return powerPackSeaConsMtHfo;
	}


	public void setPowerPackSeaConsMtHfo(Double powerPackSeaConsMtHfo) {
		this.powerPackSeaConsMtHfo = powerPackSeaConsMtHfo;
	}


	public Double getPowerPackSeaConsMtLfo() {
		return powerPackSeaConsMtLfo;
	}


	public void setPowerPackSeaConsMtLfo(Double powerPackSeaConsMtLfo) {
		this.powerPackSeaConsMtLfo = powerPackSeaConsMtLfo;
	}


	public Double getPowerPackSeaConsMtMdo() {
		return powerPackSeaConsMtMdo;
	}


	public void setPowerPackSeaConsMtMdo(Double powerPackSeaConsMtMdo) {
		this.powerPackSeaConsMtMdo = powerPackSeaConsMtMdo;
	}


	public Double getPowerPackTransitConsMtHfo() {
		return powerPackTransitConsMtHfo;
	}


	public void setPowerPackTransitConsMtHfo(Double powerPackTransitConsMtHfo) {
		this.powerPackTransitConsMtHfo = powerPackTransitConsMtHfo;
	}


	public Double getPowerPackTransitConsMtLfo() {
		return powerPackTransitConsMtLfo;
	}


	public void setPowerPackTransitConsMtLfo(Double powerPackTransitConsMtLfo) {
		this.powerPackTransitConsMtLfo = powerPackTransitConsMtLfo;
	}


	public Double getPowerPackTransitConsMtMdo() {
		return powerPackTransitConsMtMdo;
	}


	public void setPowerPackTransitConsMtMdo(Double powerPackTransitConsMtMdo) {
		this.powerPackTransitConsMtMdo = powerPackTransitConsMtMdo;
	}


	public Double getPowerPackManouvreConsMtHfo() {
		return powerPackManouvreConsMtHfo;
	}


	public void setPowerPackManouvreConsMtHfo(Double powerPackManouvreConsMtHfo) {
		this.powerPackManouvreConsMtHfo = powerPackManouvreConsMtHfo;
	}


	public Double getPowerPackManouvreConsMtLfo() {
		return powerPackManouvreConsMtLfo;
	}


	public void setPowerPackManouvreConsMtLfo(Double powerPackManouvreConsMtLfo) {
		this.powerPackManouvreConsMtLfo = powerPackManouvreConsMtLfo;
	}


	public Double getPowerPackManouvreConsMtMdo() {
		return powerPackManouvreConsMtMdo;
	}


	public void setPowerPackManouvreConsMtMdo(Double powerPackManouvreConsMtMdo) {
		this.powerPackManouvreConsMtMdo = powerPackManouvreConsMtMdo;
	}


	@Override
	public boolean equals(Object obj) {
		VoyageConsuptionManualEntry rhs = (VoyageConsuptionManualEntry) obj;
		if (id!=null && rhs.id!=null) return id.equals(rhs.id);
		boolean b = id==rhs.id;
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


	
}