package com.oceanpro.core.entity.oceanManager.voyage;

import java.util.Date;

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
 * Class name: WayPoint
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
@javax.persistence.Table(name="ocean_voyage_waypoint")
public class WayPoint {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long id ; // Unique ID
	
	@Basic
	private String uuid;

	@ManyToOne (fetch = FetchType.EAGER) // No cascade
	private VoyagePlan voyage;
	
	@Basic
	private int position; // Ordine di percorrenza
	
	@Basic
	private String latitudeStart; 
	@Basic
	private String longitudeStart;
	@Basic
	private String latitudeEnd; 
	@Basic
	private String longitudeEnd;
	@Basic
	private String trueCourse; // Rotta
	@Basic
	private Double distance; // Miglia nautiche
	@Basic
	private Double speed; // Knot = 1 miglio nautico/h
	@Basic
	private Double calculatedComsumption;
	@Basic
	private Double emiComsumptionFo;
	@Basic
	private Double emiComsumptionDo;
	
	@Basic
	private Date time;
	
	
	
	/***************** EMI **********************/
	@Basic
	private Date date;
//	@Basic
//	private String gm;
	@Basic
	private Double estimatedComsumption;
	@Basic
	private Double nsComsumption;
	
	// Wind
	@Basic
	private Double windDirection; 
	@Basic
	private Double windSpeed;
	
	@Basic
	private Double waveMeters;
	
	@Basic
	private Double seaCurrent;
	/********************************************/
	
	
	/**
	 * Costruttore vuoto
	 */
	public WayPoint() {
		super();
	}

	
	public String getDuration() {
		String res = "";
		if (distance!=null && speed!=null && speed>0) {
		
			long millis = (long)((distance/speed) * (60 * 60 * 1000));
//			long diffSeconds = millis / 1000 % 60;  
			long diffMinutes = millis / (60 * 1000) % 60;        
	        long diffHours = millis / (60 * 60 * 1000);
	        long diffDays = millis / (24 * 60 * 60 * 1000);
//	        res = diffDays + "d " + diffHours + "h " + diffMinutes + "m " + diffSeconds + "s";
	        res = diffDays + "d " + diffHours + "h " + diffMinutes + "m ";
		}
		else {
			res = "n.a.";
		}
	     
		return res;
	}
	

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	
	@Override
	public boolean equals(Object obj) {
		WayPoint rhs = (WayPoint) obj;
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

	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}



	public VoyagePlan getVoyage() {
		return voyage;
	}



	public void setVoyage(VoyagePlan voyage) {
		this.voyage = voyage;
	}



	public int getPosition() {
		return position;
	}



	public void setPosition(int position) {
		this.position = position;
	}
	


	public Date getTime() {
		return time;
	}



	public void setTime(Date time) {
		this.time = time;
	}



	public String getTrueCourse() {
		return trueCourse;
	}



	public void setTrueCourse(String trueCourse) {
		this.trueCourse = trueCourse;
	}



	public Double getDistance() {
		return distance;
	}



	public void setDistance(Double distance) {
		this.distance = distance;
	}



	public Double getSpeed() {
		return speed;
	}



	public void setSpeed(Double speed) {
		this.speed = speed;
	}



	public Double getCalculatedComsumption() {
		return calculatedComsumption;
	}



	public void setCalculatedComsumption(Double calculatedComsumption) {
		this.calculatedComsumption = calculatedComsumption;
	}



	public Double getEmiComsumptionFo() {
		return emiComsumptionFo;
	}



	public void setEmiComsumptionFo(Double emiComsumptionFo) {
		this.emiComsumptionFo = emiComsumptionFo;
	}



	public Double getEmiComsumptionDo() {
		return emiComsumptionDo;
	}



	public void setEmiComsumptionDo(Double emiComsumptionDo) {
		this.emiComsumptionDo = emiComsumptionDo;
	}



//	public String getGm() {
//		return gm;
//	}
//
//
//
//	public void setGm(String gm) {
//		this.gm = gm;
//	}



	public Double getEstimatedComsumption() {
		return estimatedComsumption;
	}



	public void setEstimatedComsumption(Double estimatedComsumption) {
		this.estimatedComsumption = estimatedComsumption;
	}



	public Double getNsComsumption() {
		return nsComsumption;
	}



	public void setNsComsumption(Double nsComsumption) {
		this.nsComsumption = nsComsumption;
	}



	public Double getWindDirection() {
		return windDirection;
	}



	public void setWindDirection(Double windDirection) {
		this.windDirection = windDirection;
	}



	public Double getWindSpeed() {
		return windSpeed;
	}



	public void setWindSpeed(Double windSpeed) {
		this.windSpeed = windSpeed;
	}



	public Double getWaveMeters() {
		return waveMeters;
	}



	public void setWaveMeters(Double waveMeters) {
		this.waveMeters = waveMeters;
	}



	public Double getSeaCurrent() {
		return seaCurrent;
	}



	public void setSeaCurrent(Double seaCurrent) {
		this.seaCurrent = seaCurrent;
	}



	public Date getDate() {
		return date;
	}



	public void setDate(Date date) {
		this.date = date;
	}



	public String getLatitudeStart() {
		return latitudeStart;
	}



	public void setLatitudeStart(String latitudeStart) {
		this.latitudeStart = latitudeStart;
	}



	public String getLongitudeStart() {
		return longitudeStart;
	}



	public void setLongitudeStart(String longitudeStart) {
		this.longitudeStart = longitudeStart;
	}



	public String getLatitudeEnd() {
		return latitudeEnd;
	}



	public void setLatitudeEnd(String latitudeEnd) {
		this.latitudeEnd = latitudeEnd;
	}



	public String getLongitudeEnd() {
		return longitudeEnd;
	}



	public void setLongitudeEnd(String longitudeEnd) {
		this.longitudeEnd = longitudeEnd;
	}



}