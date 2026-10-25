package com.oceanpro.core.entity.oceanManager.voyage;

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
 * Class name: NoonPositionDieselGenerator
 *
 * Description: 
 * 
 *
 * Company: 
 *
 * @author 
 * @date 05/feb/2013
 *
 */
@Entity
@javax.persistence.Table(name="ocean_voyage_noonposition_dieselgenerator")
public class NoonPositionDieselGenerator {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long id ; // Unique ID

	@ManyToOne (fetch = FetchType.EAGER) // No cascade
	private NoonPosition noonPosition;

	@Basic
	private Byte number; 
	
	// Diesel Generator
	@Basic
	private Integer dieselGeneratorStartHour;
	@Basic
	private Integer dieselGeneratorStartMinute;
	@Basic
	private Integer dieselGeneratorStopHour;
	@Basic
	private Integer dieselGeneratorStopMinute;
	@Basic
	private Byte dieselGeneratorHours; 
	@Basic
	private Byte dieselGeneratorMinutes;
	@Basic
	private Long dieselGeneratorTotalRunningHours;
	@Basic
	private Double dieselGeneratorHsfo =0.0; // carburante consumato nella giornata
	@Basic
	private Double dieselGeneratorLsfo =0.0;
	@Basic
	private Double dieselGeneratorHsdo =0.0;
	@Basic
	private Double dieselGeneratorLsdo =0.0;
	@Basic
	private Double dieselGeneratorTotalHsfo =0.0; // Totale carburante consumato da questo generatore
	@Basic
	private Double dieselGeneratorTotalLsfo =0.0;
	@Basic
	private Double dieselGeneratorTotalHsdo =0.0;
	@Basic
	private Double dieselGeneratorTotalLsdo =0.0;
	@Basic
	private String dieselGeneratorRemarks;

	/**
	 * Costruttore vuoto non usare!!!!!
	 */
	public NoonPositionDieselGenerator() {
		super();
	}
	
	/**
	 * Costruttore vuoto
	 */
	public NoonPositionDieselGenerator(Byte number) {
		super();
		this.number=number;
	}
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	
	@Override
	public boolean equals(Object obj) {
		NoonPositionDieselGenerator rhs = (NoonPositionDieselGenerator) obj;
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


	public Byte getDieselGeneratorHours() {
		return dieselGeneratorHours;
	}



	public void setDieselGeneratorHours(Byte dieselGeneratorHours) {
		this.dieselGeneratorHours = dieselGeneratorHours;
	}



	public Byte getDieselGeneratorMinutes() {
		return dieselGeneratorMinutes;
	}



	public void setDieselGeneratorMinutes(Byte dieselGeneratorMinutes) {
		this.dieselGeneratorMinutes = dieselGeneratorMinutes;
	}



	public Long getDieselGeneratorTotalRunningHours() {
		return dieselGeneratorTotalRunningHours;
	}



	public void setDieselGeneratorTotalRunningHours(
			Long dieselGeneratorTotalRunningHours) {
		this.dieselGeneratorTotalRunningHours = dieselGeneratorTotalRunningHours;
	}



	public Double getDieselGeneratorHsfo() {
		return dieselGeneratorHsfo;
	}



	public void setDieselGeneratorHsfo(Double dieselGeneratorHsfo) {
		this.dieselGeneratorHsfo = dieselGeneratorHsfo;
	}



	public Double getDieselGeneratorLsfo() {
		return dieselGeneratorLsfo;
	}



	public void setDieselGeneratorLsfo(Double dieselGeneratorLsfo) {
		this.dieselGeneratorLsfo = dieselGeneratorLsfo;
	}



	public Double getDieselGeneratorHsdo() {
		return dieselGeneratorHsdo;
	}



	public void setDieselGeneratorHsdo(Double dieselGeneratorHsdo) {
		this.dieselGeneratorHsdo = dieselGeneratorHsdo;
	}



	public Double getDieselGeneratorLsdo() {
		return dieselGeneratorLsdo;
	}



	public void setDieselGeneratorLsdo(Double dieselGeneratorLsdo) {
		this.dieselGeneratorLsdo = dieselGeneratorLsdo;
	}



	public Double getDieselGeneratorTotalHsfo() {
		return dieselGeneratorTotalHsfo;
	}



	public void setDieselGeneratorTotalHsfo(Double dieselGeneratorTotalHsfo) {
		this.dieselGeneratorTotalHsfo = dieselGeneratorTotalHsfo;
	}



	public Double getDieselGeneratorTotalLsfo() {
		return dieselGeneratorTotalLsfo;
	}



	public void setDieselGeneratorTotalLsfo(Double dieselGeneratorTotalLsfo) {
		this.dieselGeneratorTotalLsfo = dieselGeneratorTotalLsfo;
	}



	public Double getDieselGeneratorTotalHsdo() {
		return dieselGeneratorTotalHsdo;
	}



	public void setDieselGeneratorTotalHsdo(Double dieselGeneratorTotalHsdo) {
		this.dieselGeneratorTotalHsdo = dieselGeneratorTotalHsdo;
	}



	public Double getDieselGeneratorTotalLsdo() {
		return dieselGeneratorTotalLsdo;
	}



	public void setDieselGeneratorTotalLsdo(Double dieselGeneratorTotalLsdo) {
		this.dieselGeneratorTotalLsdo = dieselGeneratorTotalLsdo;
	}



	public String getDieselGeneratorRemarks() {
		return dieselGeneratorRemarks;
	}



	public void setDieselGeneratorRemarks(String dieselGeneratorRemarks) {
		this.dieselGeneratorRemarks = dieselGeneratorRemarks;
	}

	public NoonPosition getNoonPosition() {
		return noonPosition;
	}

	public void setNoonPosition(NoonPosition noonPosition) {
		this.noonPosition = noonPosition;
	}

	public Byte getNumber() {
		return number;
	}

	public void setNumber(Byte number) {
		this.number = number;
	}

	public Integer getDieselGeneratorStartHour() {
		return dieselGeneratorStartHour;
	}

	public void setDieselGeneratorStartHour(Integer dieselGeneratorStartHour) {
		this.dieselGeneratorStartHour = dieselGeneratorStartHour;
	}

	public Integer getDieselGeneratorStartMinute() {
		return dieselGeneratorStartMinute;
	}

	public void setDieselGeneratorStartMinute(Integer dieselGeneratorStartMinute) {
		this.dieselGeneratorStartMinute = dieselGeneratorStartMinute;
	}

	public Integer getDieselGeneratorStopHour() {
		return dieselGeneratorStopHour;
	}

	public void setDieselGeneratorStopHour(Integer dieselGeneratorStopHour) {
		this.dieselGeneratorStopHour = dieselGeneratorStopHour;
	}

	public Integer getDieselGeneratorStopMinute() {
		return dieselGeneratorStopMinute;
	}

	public void setDieselGeneratorStopMinute(Integer dieselGeneratorStopMinute) {
		this.dieselGeneratorStopMinute = dieselGeneratorStopMinute;
	}

}