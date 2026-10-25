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

import com.oceanpro.utils.Coordinates;

/**
 * 
 * Class name: NoonPosition
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
@javax.persistence.Table(name="ocean_voyage_noonposition")
public class NoonPosition implements Comparable<NoonPosition>{


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long id ; // Unique ID
	
	@Basic
	private String uuid;

	@ManyToOne (fetch = FetchType.EAGER) // No cascade
	private VoyagePlan voyage;
	
	@Basic
	private Date registrationDate;
	
	@Basic
	private Date creationDate;
	

	@Basic
	private Date sbe; // Data partenza
	@Basic
	private Date fwe; // Data arrivo
	
	@Basic
	private String latitude; 
	@Basic
	private String longitude;
	
	@Basic
	private Byte dayHours; 
	@Basic
	private Byte dayMinutes;
	@Basic
	private Long totalRunningHours;
	
	@Basic
	private String timeZone;
	
	@Basic
	private Date eta;
	
	@Basic
	private Double distanceMg;
	@Basic
	private Double distanceToGo;
	@Basic
	private Double totalDistance;
	
	@Basic
	private Double averageSpeed;
	@Basic
	private Double totalAverageSpeed;
	
	@Basic
	private Double courseMadeGood;
	
	@Basic
	private String wind;
	@Basic
	private String sea;
	
	@Basic
	private String remark;
	
	
	
	/***************  In PORT *****************/
//	@Column(columnDefinition="INT(1)")
//	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Basic
	private boolean inPort;
	
	@Basic
	private Byte inPortOperationType; // 0=Manouvring; 1=PortOperation 
	
	@Basic
	private Date arrivalPortEndOfPassage;
	@Basic
	private Date arrivalPortNoticeOfReadinessTendered;
	@Basic
	private Date arrivalPortAnchored;
	@Basic
	private Date arrivalPortAnchorAweigh;
	
	@Basic
	private Date manouvringPilotOnBoard;
	@Basic
	private Date manouvringArrBerth;
	@Basic
	private Date manouvringFla;
	@Basic
	private Date manouvringMooringComplete;
	@Basic
	private Date manouvringFwe;
	
	@Basic
	private Date departurePortPilotOnBoard;
	@Basic
	private Date departurePortLastMooringLineCastOff;
	@Basic
	private Date departurePortPilotAway;
	@Basic
	private Date departurePortFullAwayOnPassage;
	@Basic
	private Date departurePortFweCosp;
	
	
	// CONSUMI
	
	// COSP ROB
	@Basic
	private Double cospRobHsfo =0.0;
	@Basic
	private Double cospRobLsfo =0.0;
	@Basic
	private Double cospRobHsdo =0.0;
	@Basic
	private Double cospRobLsdo =0.0;
	@Basic
	private Double cospRobLo =0.0;
	@Basic
	private Double cospRobFw =0.0;
	
	
	// Main Engine
	@Basic
	private Long mainEngineHours; 
	@Basic
	private Long mainEngineMinutes;
	@Basic
	private Long mainEngineTotalRunningHours;
	@Basic
	private Double mainEngineHsfo =0.0; // carburante consumato nella giornata
	@Basic
	private Double mainEngineLsfo =0.0;
	@Basic
	private Double mainEngineHsdo =0.0;
	@Basic
	private Double mainEngineLsdo =0.0;
	@Basic
	private Double mainEngineTotalHsfo =0.0; // Totale carburante consumato
	@Basic
	private Double mainEngineTotalLsfo =0.0;
	@Basic
	private Double mainEngineTotalHsdo =0.0;
	@Basic
	private Double mainEngineTotalLsdo =0.0;
	@Basic
	private String mainEngineRemarks;
	@Basic
	private Double mainEngineLo =0.0; // olio
	@Basic
	private Double mainEngineFw =0.0; // acqua
	@Basic
	private Double mainEngineTotalLo =0.0; // olio
	@Basic
	private Double mainEngineTotalFw =0.0; // acqua
	
	@Basic
	private Double mainEngineEngineDistance =0.0;
	@Basic
	private Double mainEngineTotalEngineDistance =0.0;
	@Basic
	private Double mainEngineAvEngineSpeed =0.0;
	@Basic
	private Double mainEngineTotalAvEngineSpeed =0.0;
	@Basic
	private Double mainEngineH24AvWk =0.0;
	@Basic
	private Double mainEngineRpm =0.0;
	@Basic
	private Double mainEngineSlip =0.0;
	@Basic
	private Double mainEngineTotalSlip =0.0;
	
	
	// Diesel Generator
	@OneToMany(targetEntity=NoonPositionDieselGenerator.class, cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="noonPosition", fetch=FetchType.LAZY)
	private List<NoonPositionDieselGenerator> dieselGenerators;
	@Basic
	private Double dieselGeneratorsTotalHsfo =0.0; // Totale carburante consumato da tutti i generatori
	@Basic
	private Double dieselGeneratorsTotalLsfo =0.0;
	@Basic
	private Double dieselGeneratorsTotalHsdo =0.0;
	@Basic
	private Double dieselGeneratorsTotalLsdo =0.0;
	
	
	// IGS
	@Basic
	private Integer igsStartHour;
	@Basic
	private Integer igsStartMinute;
	@Basic
	private Integer igsStopHour;
	@Basic
	private Integer igsStopMinute;
	@Basic
	private Byte igsHours; 
	@Basic
	private Byte igsMinutes;
	@Basic
	private Long igsTotalRunningHours;
	@Basic
	private Double igsHsfo =0.0; // carburante consumato nella giornata
	@Basic
	private Double igsLsfo =0.0;
	@Basic
	private Double igsHsdo =0.0;
	@Basic
	private Double igsLsdo =0.0;
	@Basic
	private Double igsTotalHsfo =0.0; // Totale carburante consumato
	@Basic
	private Double igsTotalLsfo =0.0;
	@Basic
	private Double igsTotalHsdo =0.0;
	@Basic
	private Double igsTotalLsdo =0.0;
	@Basic
	private String igsRemarks;
	
	// Boiler
	@Basic
	private Integer boilerStartHour;
	@Basic
	private Integer boilerStartMinute;
	@Basic
	private Integer boilerStopHour;
	@Basic
	private Integer boilerStopMinute;
	@Basic
	private Byte boilerHours; 
	@Basic
	private Byte boilerMinutes;
	@Basic
	private Long boilerTotalRunningHours;
	@Basic
	private Double boilerHsfo =0.0; // carburante consumato nella giornata
	@Basic
	private Double boilerLsfo =0.0;
	@Basic
	private Double boilerHsdo =0.0;
	@Basic
	private Double boilerLsdo =0.0;
	@Basic
	private Double boilerTotalHsfo =0.0; // Totale carburante consumato
	@Basic
	private Double boilerTotalLsfo =0.0;
	@Basic
	private Double boilerTotalHsdo =0.0;
	@Basic
	private Double boilerTotalLsdo =0.0;
	@Basic
	private String boilerRemarks;
	
	// Heating
	@Basic
	private Integer heatingStartHour;
	@Basic
	private Integer heatingStartMinute;
	@Basic
	private Integer heatingStopHour;
	@Basic
	private Integer heatingStopMinute;
	@Basic
	private Byte heatingHours; 
	@Basic
	private Byte heatingMinutes;
	@Basic
	private Long heatingTotalRunningHours;
	@Basic
	private Double heatingHsfo =0.0; // carburante consumato nella giornata
	@Basic
	private Double heatingLsfo =0.0;
	@Basic
	private Double heatingHsdo =0.0;
	@Basic
	private Double heatingLsdo =0.0;
	@Basic
	private Double heatingTotalHsfo =0.0; // Totale carburante consumato
	@Basic
	private Double heatingTotalLsfo =0.0;
	@Basic
	private Double heatingTotalHsdo =0.0;
	@Basic
	private Double heatingTotalLsdo =0.0;
	@Basic
	private String heatingRemarks;
		
	// Tank Cleaning
	@Basic
	private Integer tankCleaningStartHour;
	@Basic
	private Integer tankCleaningStartMinute;
	@Basic
	private Integer tankCleaningStopHour;
	@Basic
	private Integer tankCleaningStopMinute;
	@Basic
	private Byte tankCleaningHours; 
	@Basic
	private Byte tankCleaningMinutes;
	@Basic
	private Long tankCleaningTotalRunningHours;
	@Basic
	private Double tankCleaningHsfo =0.0; // carburante consumato nella giornata
	@Basic
	private Double tankCleaningLsfo =0.0;
	@Basic
	private Double tankCleaningHsdo =0.0;
	@Basic
	private Double tankCleaningLsdo =0.0;
	@Basic
	private Double tankCleaningTotalHsfo =0.0; // Totale carburante consumato
	@Basic
	private Double tankCleaningTotalLsfo =0.0;
	@Basic
	private Double tankCleaningTotalHsdo =0.0;
	@Basic
	private Double tankCleaningTotalLsdo =0.0;
	@Basic
	private String tankCleaningRemarks;
	
	// Other Machinery
	@Basic
	private Integer otherMachineryStartHour;
	@Basic
	private Integer otherMachineryStartMinute;
	@Basic
	private Integer otherMachineryStopHour;
	@Basic
	private Integer otherMachineryStopMinute;
	@Basic
	private Byte otherMachineryHours; 
	@Basic
	private Byte otherMachineryMinutes;
	@Basic
	private Long otherMachineryTotalRunningHours;
	@Basic
	private Double otherMachineryHsfo =0.0; // carburante consumato nella giornata
	@Basic
	private Double otherMachineryLsfo =0.0;
	@Basic
	private Double otherMachineryHsdo =0.0;
	@Basic
	private Double otherMachineryLsdo =0.0;
	@Basic
	private Double otherMachineryTotalHsfo =0.0; // Totale carburante consumato
	@Basic
	private Double otherMachineryTotalLsfo =0.0;
	@Basic
	private Double otherMachineryTotalHsdo =0.0;
	@Basic
	private Double otherMachineryTotalLsdo =0.0;
	@Basic
	private String otherMachineryRemarks;
	
	
	
	
	
	
	
	
	
	
	
	
	
//	// Lube Oil And Fresh Water
//	@Basic
//	private Byte lubeOilAndFreshWaterHours; 
//	@Basic
//	private Byte lubeOilAndFreshWaterMinutes;
//	@Basic
//	private Long lubeOilAndFreshWaterTotalRunningHours;
//	@Basic
//	private Double lubeOilAndFreshWaterLo =0.0; // carburante consumato nella giornata
//	@Basic
//	private Double lubeOilAndFreshWaterFw =0.0;
//	@Basic
//	private Double lubeOilAndFreshWaterTotalLo =0.0; // Totale carburante consumato
//	@Basic
//	private Double lubeOilAndFreshWaterTotalFw =0.0;
//	@Basic
//	private String lubeOilAndFreshWaterRemarks;
	
	
	
	
	
	
	
	
	
	// TOTAL Consumtion
	@Basic
	private Double totalHsfo =0.0; // Totale consumato
	@Basic
	private Double totalLsfo =0.0;
	@Basic
	private Double totalHsdo =0.0;
	@Basic
	private Double totalLsdo =0.0;
	@Basic
	private Double totalLo =0.0;
	@Basic
	private Double totalFw =0.0;
	
	// TOTAL Bunkering
	@Basic
	private Double totalBunkeringHsfo =0.0; // Totale 
	@Basic
	private Double totalBunkeringLsfo =0.0;
	@Basic
	private Double totalBunkeringHsdo =0.0;
	@Basic
	private Double totalBunkeringLsdo =0.0;
	@Basic
	private Double totalBunkeringLo =0.0;
	@Basic
	private Double totalBunkeringFw =0.0;
	
	// TOTAL ROB
	@Basic
	private Double totalRobHsfo =0.0; // Totale rimanenze carburante
	@Basic
	private Double totalRobLsfo =0.0;
	@Basic
	private Double totalRobHsdo =0.0;
	@Basic
	private Double totalRobLsdo =0.0;
	@Basic
	private Double totalRobLo =0.0;
	@Basic
	private Double totalRobFw =0.0;
	
	
	
	
	
	/**
	 * Costruttore vuoto
	 */
	public NoonPosition() {
		super();
	}
	
	
	
	public String getLatitudeDmsFormat() {
		try {
			return (new Coordinates(getLatitude(), getLongitude(), Coordinates.COOD_TYPE_NEGATIVE_DEGEE)).getLatitudeDms();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public String getLongitudeDmsFormat() {
		try {
			return (new Coordinates(getLatitude(), getLongitude(), Coordinates.COOD_TYPE_NEGATIVE_DEGEE)).getLongitudeDms();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	
	
	
	
	
	public double getTotalDailyDieselGeneratorHsfo() {
		return getTotalDailyDieselGenerator(0);
	}
	public double getTotalDailyDieselGeneratorLsfo() {
		return getTotalDailyDieselGenerator(1);
	}
	public double getTotalDailyDieselGeneratorLsdo() {
		return getTotalDailyDieselGenerator(2);
	}
	public double getTotalDailyDieselGeneratorHsdo() {
		return getTotalDailyDieselGenerator(3);
	}
	
	
	private double getTotalDailyDieselGenerator(int type) {
		double tot = 0.0;
		for (NoonPositionDieselGenerator dg : dieselGenerators) {
			if (type==0) tot += (dg.getDieselGeneratorHsfo()!=null?dg.getDieselGeneratorHsfo():0.0);
			if (type==1) tot += (dg.getDieselGeneratorLsfo()!=null?dg.getDieselGeneratorLsfo():0.0);
			if (type==2) tot += (dg.getDieselGeneratorLsdo()!=null?dg.getDieselGeneratorLsdo():0.0);
			if (type==3) tot += (dg.getDieselGeneratorHsdo()!=null?dg.getDieselGeneratorHsdo():0.0);
		}
		return tot;
	}
	
	
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	
	@Override
	public boolean equals(Object obj) {
		NoonPosition rhs = (NoonPosition) obj;
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


	
	private String getDoubleToStringRoundTwo(Double num) {
		if (num==null) return "";
		String res = num.toString();
		int ind = res.indexOf(".");
		if (ind+3 > res.length()) ind = res.length();
		else ind = ind +3;
		res = res.substring(0, ind);
		return res;
	}
	
	public String getLatitudeString() {
		try {
			return getDoubleToStringRoundTwo(Double.parseDouble(getLatitude()));
		} catch (Exception e) {
			e.printStackTrace();
			return "-";
		}
	}
	public String getLongitudeString() {
		try {
			return getDoubleToStringRoundTwo(Double.parseDouble(getLongitude()));
		} catch (Exception e) {
			e.printStackTrace();
			return "-";
		}
	}

	public VoyagePlan getVoyage() {
		return voyage;
	}



	public void setVoyage(VoyagePlan voyage) {
		this.voyage = voyage;
	}


	public Date getRegistrationDate() {
		return registrationDate;
	}


	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(NoonPosition comp) {
		 if (getRegistrationDate()!=null && comp.getRegistrationDate()!=null && getRegistrationDate().before(comp.getRegistrationDate())) return -1; 
		 else if (getRegistrationDate()!=null && comp.getRegistrationDate()!=null && getRegistrationDate().equals(comp.getRegistrationDate())) return 0;
		 else return 1;
	}



	public String getLatitude() {
		return latitude;
	}



	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}



	public Byte getDayHours() {
		return dayHours;
	}



	public void setDayHours(Byte dayHours) {
		this.dayHours = dayHours;
	}



	public Byte getDayMinutes() {
		return dayMinutes;
	}



	public void setDayMinutes(Byte dayMinutes) {
		this.dayMinutes = dayMinutes;
	}



	public Date getEta() {
		return eta;
	}



	public void setEta(Date eta) {
		this.eta = eta;
	}



	public Double getDistanceMg() {
		return distanceMg;
	}



	public void setDistanceMg(Double distanceMg) {
		this.distanceMg = distanceMg;
	}



	public Double getDistanceToGo() {
		return distanceToGo;
	}



	public void setDistanceToGo(Double distanceToGo) {
		this.distanceToGo = distanceToGo;
	}



	public Double getTotalDistance() {
		return totalDistance;
	}



	public void setTotalDistance(Double totalDistance) {
		this.totalDistance = totalDistance;
	}



	public Double getAverageSpeed() {
		return averageSpeed;
	}



	public void setAverageSpeed(Double averageSpeed) {
		this.averageSpeed = averageSpeed;
	}



	public Double getTotalAverageSpeed() {
		return totalAverageSpeed;
	}



	public void setTotalAverageSpeed(Double totalAverageSpeed) {
		this.totalAverageSpeed = totalAverageSpeed;
	}



	public Double getCourseMadeGood() {
		return courseMadeGood;
	}



	public void setCourseMadeGood(Double courseMadeGood) {
		this.courseMadeGood = courseMadeGood;
	}


	public Double getTotalHsfo() {
		return totalHsfo;
	}



	public void setTotalHsfo(Double totalHsfo) {
		this.totalHsfo = totalHsfo;
	}



	public Double getTotalLsfo() {
		return totalLsfo;
	}



	public void setTotalLsfo(Double totalLsfo) {
		this.totalLsfo = totalLsfo;
	}



	public Double getTotalHsdo() {
		return totalHsdo;
	}



	public void setTotalHsdo(Double totalHsdo) {
		this.totalHsdo = totalHsdo;
	}



	public Double getTotalLsdo() {
		return totalLsdo;
	}



	public void setTotalLsdo(Double totalLsdo) {
		this.totalLsdo = totalLsdo;
	}



	public Double getTotalRobHsfo() {
		return totalRobHsfo;
	}



	public void setTotalRobHsfo(Double totalRobHsfo) {
		this.totalRobHsfo = totalRobHsfo;
	}



	public Double getTotalRobLsfo() {
		return totalRobLsfo;
	}



	public void setTotalRobLsfo(Double totalRobLsfo) {
		this.totalRobLsfo = totalRobLsfo;
	}



	public Double getTotalRobHsdo() {
		return totalRobHsdo;
	}



	public void setTotalRobHsdo(Double totalRobHsdo) {
		this.totalRobHsdo = totalRobHsdo;
	}



	public Double getTotalRobLsdo() {
		return totalRobLsdo;
	}



	public void setTotalRobLsdo(Double totalRobLsdo) {
		this.totalRobLsdo = totalRobLsdo;
	}



	public String getWind() {
		return wind;
	}



	public void setWind(String wind) {
		this.wind = wind;
	}



	public String getSea() {
		return sea;
	}



	public void setSea(String sea) {
		this.sea = sea;
	}



	public String getLongitude() {
		return longitude;
	}



	public String getRemark() {
		return remark;
	}



	public void setRemark(String remark) {
		this.remark = remark;
	}



	public Date getSbe() {
		return sbe;
	}



	public void setSbe(Date sbe) {
		this.sbe = sbe;
	}



	public Date getFwe() {
		return fwe;
	}



	public void setFwe(Date fwe) {
		this.fwe = fwe;
	}



	public Long getTotalRunningHours() {
		return totalRunningHours;
	}



	public void setTotalRunningHours(Long totalRunningHours) {
		this.totalRunningHours = totalRunningHours;
	}



	public Double getCospRobHsfo() {
		return cospRobHsfo;
	}



	public void setCospRobHsfo(Double cospRobHsfo) {
		this.cospRobHsfo = cospRobHsfo;
	}



	public Double getCospRobLsfo() {
		return cospRobLsfo;
	}



	public void setCospRobLsfo(Double cospRobLsfo) {
		this.cospRobLsfo = cospRobLsfo;
	}



	public Double getCospRobHsdo() {
		return cospRobHsdo;
	}



	public void setCospRobHsdo(Double cospRobHsdo) {
		this.cospRobHsdo = cospRobHsdo;
	}



	public Double getCospRobLsdo() {
		return cospRobLsdo;
	}



	public void setCospRobLsdo(Double cospRobLsdo) {
		this.cospRobLsdo = cospRobLsdo;
	}



	public Double getCospRobLo() {
		return cospRobLo;
	}



	public void setCospRobLo(Double cospRobLo) {
		this.cospRobLo = cospRobLo;
	}



	public Double getCospRobFw() {
		return cospRobFw;
	}



	public void setCospRobFw(Double cospRobFw) {
		this.cospRobFw = cospRobFw;
	}



	public Long getMainEngineHours() {
		return mainEngineHours;
	}



	public void setMainEngineHours(Long mainEngineHours) {
		this.mainEngineHours = mainEngineHours;
	}



	public Long getMainEngineMinutes() {
		return mainEngineMinutes;
	}



	public void setMainEngineMinutes(Long mainEngineMinutes) {
		this.mainEngineMinutes = mainEngineMinutes;
	}



	public Long getMainEngineTotalRunningHours() {
		return mainEngineTotalRunningHours;
	}



	public void setMainEngineTotalRunningHours(Long mainEngineTotalRunningHours) {
		this.mainEngineTotalRunningHours = mainEngineTotalRunningHours;
	}



	public Double getMainEngineHsfo() {
		return mainEngineHsfo;
	}



	public void setMainEngineHsfo(Double mainEngineHsfo) {
		this.mainEngineHsfo = mainEngineHsfo;
	}



	public Double getMainEngineLsfo() {
		return mainEngineLsfo;
	}



	public void setMainEngineLsfo(Double mainEngineLsfo) {
		this.mainEngineLsfo = mainEngineLsfo;
	}



	public Double getMainEngineHsdo() {
		return mainEngineHsdo;
	}



	public void setMainEngineHsdo(Double mainEngineHsdo) {
		this.mainEngineHsdo = mainEngineHsdo;
	}



	public Double getMainEngineLsdo() {
		return mainEngineLsdo;
	}



	public void setMainEngineLsdo(Double mainEngineLsdo) {
		this.mainEngineLsdo = mainEngineLsdo;
	}



	public Double getMainEngineTotalHsfo() {
		return mainEngineTotalHsfo;
	}



	public void setMainEngineTotalHsfo(Double mainEngineTotalHsfo) {
		this.mainEngineTotalHsfo = mainEngineTotalHsfo;
	}



	public Double getMainEngineTotalLsfo() {
		return mainEngineTotalLsfo;
	}



	public void setMainEngineTotalLsfo(Double mainEngineTotalLsfo) {
		this.mainEngineTotalLsfo = mainEngineTotalLsfo;
	}



	public Double getMainEngineTotalHsdo() {
		return mainEngineTotalHsdo;
	}



	public void setMainEngineTotalHsdo(Double mainEngineTotalHsdo) {
		this.mainEngineTotalHsdo = mainEngineTotalHsdo;
	}



	public Double getMainEngineTotalLsdo() {
		return mainEngineTotalLsdo;
	}



	public void setMainEngineTotalLsdo(Double mainEngineTotalLsdo) {
		this.mainEngineTotalLsdo = mainEngineTotalLsdo;
	}



	public String getMainEngineRemarks() {
		return mainEngineRemarks;
	}



	public void setMainEngineRemarks(String mainEngineRemarks) {
		this.mainEngineRemarks = mainEngineRemarks;
	}


	public Byte getIgsHours() {
		return igsHours;
	}



	public void setIgsHours(Byte igsHours) {
		this.igsHours = igsHours;
	}



	public Byte getIgsMinutes() {
		return igsMinutes;
	}



	public void setIgsMinutes(Byte igsMinutes) {
		this.igsMinutes = igsMinutes;
	}



	public Long getIgsTotalRunningHours() {
		return igsTotalRunningHours;
	}



	public void setIgsTotalRunningHours(Long igsTotalRunningHours) {
		this.igsTotalRunningHours = igsTotalRunningHours;
	}



	public Double getIgsHsfo() {
		return igsHsfo;
	}



	public void setIgsHsfo(Double igsHsfo) {
		this.igsHsfo = igsHsfo;
	}



	public Double getIgsLsfo() {
		return igsLsfo;
	}



	public void setIgsLsfo(Double igsLsfo) {
		this.igsLsfo = igsLsfo;
	}



	public Double getIgsHsdo() {
		return igsHsdo;
	}



	public void setIgsHsdo(Double igsHsdo) {
		this.igsHsdo = igsHsdo;
	}



	public Double getIgsLsdo() {
		return igsLsdo;
	}



	public void setIgsLsdo(Double igsLsdo) {
		this.igsLsdo = igsLsdo;
	}



	public Double getIgsTotalHsfo() {
		return igsTotalHsfo;
	}



	public void setIgsTotalHsfo(Double igsTotalHsfo) {
		this.igsTotalHsfo = igsTotalHsfo;
	}



	public Double getIgsTotalLsfo() {
		return igsTotalLsfo;
	}



	public void setIgsTotalLsfo(Double igsTotalLsfo) {
		this.igsTotalLsfo = igsTotalLsfo;
	}



	public Double getIgsTotalHsdo() {
		return igsTotalHsdo;
	}



	public void setIgsTotalHsdo(Double igsTotalHsdo) {
		this.igsTotalHsdo = igsTotalHsdo;
	}



	public Double getIgsTotalLsdo() {
		return igsTotalLsdo;
	}



	public void setIgsTotalLsdo(Double igsTotalLsdo) {
		this.igsTotalLsdo = igsTotalLsdo;
	}



	public String getIgsRemarks() {
		return igsRemarks;
	}



	public void setIgsRemarks(String igsRemarks) {
		this.igsRemarks = igsRemarks;
	}



	public Byte getBoilerHours() {
		return boilerHours;
	}



	public void setBoilerHours(Byte boilerHours) {
		this.boilerHours = boilerHours;
	}



	public Byte getBoilerMinutes() {
		return boilerMinutes;
	}



	public void setBoilerMinutes(Byte boilerMinutes) {
		this.boilerMinutes = boilerMinutes;
	}



	public Long getBoilerTotalRunningHours() {
		return boilerTotalRunningHours;
	}



	public void setBoilerTotalRunningHours(Long boilerTotalRunningHours) {
		this.boilerTotalRunningHours = boilerTotalRunningHours;
	}



	public Double getBoilerHsfo() {
		return boilerHsfo;
	}



	public void setBoilerHsfo(Double boilerHsfo) {
		this.boilerHsfo = boilerHsfo;
	}



	public Double getBoilerLsfo() {
		return boilerLsfo;
	}



	public void setBoilerLsfo(Double boilerLsfo) {
		this.boilerLsfo = boilerLsfo;
	}



	public Double getBoilerHsdo() {
		return boilerHsdo;
	}



	public void setBoilerHsdo(Double boilerHsdo) {
		this.boilerHsdo = boilerHsdo;
	}



	public Double getBoilerLsdo() {
		return boilerLsdo;
	}



	public void setBoilerLsdo(Double boilerLsdo) {
		this.boilerLsdo = boilerLsdo;
	}



	public Double getBoilerTotalHsfo() {
		return boilerTotalHsfo;
	}



	public void setBoilerTotalHsfo(Double boilerTotalHsfo) {
		this.boilerTotalHsfo = boilerTotalHsfo;
	}



	public Double getBoilerTotalLsfo() {
		return boilerTotalLsfo;
	}



	public void setBoilerTotalLsfo(Double boilerTotalLsfo) {
		this.boilerTotalLsfo = boilerTotalLsfo;
	}



	public Double getBoilerTotalHsdo() {
		return boilerTotalHsdo;
	}



	public void setBoilerTotalHsdo(Double boilerTotalHsdo) {
		this.boilerTotalHsdo = boilerTotalHsdo;
	}



	public Double getBoilerTotalLsdo() {
		return boilerTotalLsdo;
	}



	public void setBoilerTotalLsdo(Double boilerTotalLsdo) {
		this.boilerTotalLsdo = boilerTotalLsdo;
	}



	public String getBoilerRemarks() {
		return boilerRemarks;
	}



	public void setBoilerRemarks(String boilerRemarks) {
		this.boilerRemarks = boilerRemarks;
	}



	public Byte getHeatingHours() {
		return heatingHours;
	}



	public void setHeatingHours(Byte heatingHours) {
		this.heatingHours = heatingHours;
	}



	public Byte getHeatingMinutes() {
		return heatingMinutes;
	}



	public void setHeatingMinutes(Byte heatingMinutes) {
		this.heatingMinutes = heatingMinutes;
	}



	public Long getHeatingTotalRunningHours() {
		return heatingTotalRunningHours;
	}



	public void setHeatingTotalRunningHours(Long heatingTotalRunningHours) {
		this.heatingTotalRunningHours = heatingTotalRunningHours;
	}



	public Double getHeatingHsfo() {
		return heatingHsfo;
	}



	public void setHeatingHsfo(Double heatingHsfo) {
		this.heatingHsfo = heatingHsfo;
	}



	public Double getHeatingLsfo() {
		return heatingLsfo;
	}



	public void setHeatingLsfo(Double heatingLsfo) {
		this.heatingLsfo = heatingLsfo;
	}



	public Double getHeatingHsdo() {
		return heatingHsdo;
	}



	public void setHeatingHsdo(Double heatingHsdo) {
		this.heatingHsdo = heatingHsdo;
	}



	public Double getHeatingLsdo() {
		return heatingLsdo;
	}



	public void setHeatingLsdo(Double heatingLsdo) {
		this.heatingLsdo = heatingLsdo;
	}



	public Double getHeatingTotalHsfo() {
		return heatingTotalHsfo;
	}



	public void setHeatingTotalHsfo(Double heatingTotalHsfo) {
		this.heatingTotalHsfo = heatingTotalHsfo;
	}



	public Double getHeatingTotalLsfo() {
		return heatingTotalLsfo;
	}



	public void setHeatingTotalLsfo(Double heatingTotalLsfo) {
		this.heatingTotalLsfo = heatingTotalLsfo;
	}



	public Double getHeatingTotalHsdo() {
		return heatingTotalHsdo;
	}



	public void setHeatingTotalHsdo(Double heatingTotalHsdo) {
		this.heatingTotalHsdo = heatingTotalHsdo;
	}



	public Double getHeatingTotalLsdo() {
		return heatingTotalLsdo;
	}



	public void setHeatingTotalLsdo(Double heatingTotalLsdo) {
		this.heatingTotalLsdo = heatingTotalLsdo;
	}



	public String getHeatingRemarks() {
		return heatingRemarks;
	}



	public void setHeatingRemarks(String heatingRemarks) {
		this.heatingRemarks = heatingRemarks;
	}



	public Byte getTankCleaningHours() {
		return tankCleaningHours;
	}



	public void setTankCleaningHours(Byte tankCleaningHours) {
		this.tankCleaningHours = tankCleaningHours;
	}



	public Byte getTankCleaningMinutes() {
		return tankCleaningMinutes;
	}



	public void setTankCleaningMinutes(Byte tankCleaningMinutes) {
		this.tankCleaningMinutes = tankCleaningMinutes;
	}



	public Long getTankCleaningTotalRunningHours() {
		return tankCleaningTotalRunningHours;
	}



	public void setTankCleaningTotalRunningHours(Long tankCleaningTotalRunningHours) {
		this.tankCleaningTotalRunningHours = tankCleaningTotalRunningHours;
	}



	public Double getTankCleaningHsfo() {
		return tankCleaningHsfo;
	}



	public void setTankCleaningHsfo(Double tankCleaningHsfo) {
		this.tankCleaningHsfo = tankCleaningHsfo;
	}



	public Double getTankCleaningLsfo() {
		return tankCleaningLsfo;
	}



	public void setTankCleaningLsfo(Double tankCleaningLsfo) {
		this.tankCleaningLsfo = tankCleaningLsfo;
	}



	public Double getTankCleaningHsdo() {
		return tankCleaningHsdo;
	}



	public void setTankCleaningHsdo(Double tankCleaningHsdo) {
		this.tankCleaningHsdo = tankCleaningHsdo;
	}



	public Double getTankCleaningLsdo() {
		return tankCleaningLsdo;
	}



	public void setTankCleaningLsdo(Double tankCleaningLsdo) {
		this.tankCleaningLsdo = tankCleaningLsdo;
	}



	public Double getTankCleaningTotalHsfo() {
		return tankCleaningTotalHsfo;
	}



	public void setTankCleaningTotalHsfo(Double tankCleaningTotalHsfo) {
		this.tankCleaningTotalHsfo = tankCleaningTotalHsfo;
	}



	public Double getTankCleaningTotalLsfo() {
		return tankCleaningTotalLsfo;
	}



	public void setTankCleaningTotalLsfo(Double tankCleaningTotalLsfo) {
		this.tankCleaningTotalLsfo = tankCleaningTotalLsfo;
	}



	public Double getTankCleaningTotalHsdo() {
		return tankCleaningTotalHsdo;
	}



	public void setTankCleaningTotalHsdo(Double tankCleaningTotalHsdo) {
		this.tankCleaningTotalHsdo = tankCleaningTotalHsdo;
	}



	public Double getTankCleaningTotalLsdo() {
		return tankCleaningTotalLsdo;
	}



	public void setTankCleaningTotalLsdo(Double tankCleaningTotalLsdo) {
		this.tankCleaningTotalLsdo = tankCleaningTotalLsdo;
	}



	public String getTankCleaningRemarks() {
		return tankCleaningRemarks;
	}



	public void setTankCleaningRemarks(String tankCleaningRemarks) {
		this.tankCleaningRemarks = tankCleaningRemarks;
	}



	public Byte getOtherMachineryHours() {
		return otherMachineryHours;
	}



	public void setOtherMachineryHours(Byte otherMachineryHours) {
		this.otherMachineryHours = otherMachineryHours;
	}



	public Byte getOtherMachineryMinutes() {
		return otherMachineryMinutes;
	}



	public void setOtherMachineryMinutes(Byte otherMachineryMinutes) {
		this.otherMachineryMinutes = otherMachineryMinutes;
	}



	public Long getOtherMachineryTotalRunningHours() {
		return otherMachineryTotalRunningHours;
	}



	public void setOtherMachineryTotalRunningHours(
			Long otherMachineryTotalRunningHours) {
		this.otherMachineryTotalRunningHours = otherMachineryTotalRunningHours;
	}



	public Double getOtherMachineryHsfo() {
		return otherMachineryHsfo;
	}



	public void setOtherMachineryHsfo(Double otherMachineryHsfo) {
		this.otherMachineryHsfo = otherMachineryHsfo;
	}



	public Double getOtherMachineryLsfo() {
		return otherMachineryLsfo;
	}



	public void setOtherMachineryLsfo(Double otherMachineryLsfo) {
		this.otherMachineryLsfo = otherMachineryLsfo;
	}



	public Double getOtherMachineryHsdo() {
		return otherMachineryHsdo;
	}



	public void setOtherMachineryHsdo(Double otherMachineryHsdo) {
		this.otherMachineryHsdo = otherMachineryHsdo;
	}



	public Double getOtherMachineryLsdo() {
		return otherMachineryLsdo;
	}



	public void setOtherMachineryLsdo(Double otherMachineryLsdo) {
		this.otherMachineryLsdo = otherMachineryLsdo;
	}



	public Double getOtherMachineryTotalHsfo() {
		return otherMachineryTotalHsfo;
	}



	public void setOtherMachineryTotalHsfo(Double otherMachineryTotalHsfo) {
		this.otherMachineryTotalHsfo = otherMachineryTotalHsfo;
	}



	public Double getOtherMachineryTotalLsfo() {
		return otherMachineryTotalLsfo;
	}



	public void setOtherMachineryTotalLsfo(Double otherMachineryTotalLsfo) {
		this.otherMachineryTotalLsfo = otherMachineryTotalLsfo;
	}



	public Double getOtherMachineryTotalHsdo() {
		return otherMachineryTotalHsdo;
	}



	public void setOtherMachineryTotalHsdo(Double otherMachineryTotalHsdo) {
		this.otherMachineryTotalHsdo = otherMachineryTotalHsdo;
	}



	public Double getOtherMachineryTotalLsdo() {
		return otherMachineryTotalLsdo;
	}



	public void setOtherMachineryTotalLsdo(Double otherMachineryTotalLsdo) {
		this.otherMachineryTotalLsdo = otherMachineryTotalLsdo;
	}



	public String getOtherMachineryRemarks() {
		return otherMachineryRemarks;
	}



	public void setOtherMachineryRemarks(String otherMachineryRemarks) {
		this.otherMachineryRemarks = otherMachineryRemarks;
	}



	public Double getTotalLo() {
		return totalLo;
	}



	public void setTotalLo(Double totalLo) {
		this.totalLo = totalLo;
	}



	public Double getTotalFw() {
		return totalFw;
	}



	public void setTotalFw(Double totalFw) {
		this.totalFw = totalFw;
	}



	public Double getTotalBunkeringHsfo() {
		return totalBunkeringHsfo;
	}



	public void setTotalBunkeringHsfo(Double totalBunkeringHsfo) {
		this.totalBunkeringHsfo = totalBunkeringHsfo;
	}



	public Double getTotalBunkeringLsfo() {
		return totalBunkeringLsfo;
	}



	public void setTotalBunkeringLsfo(Double totalBunkeringLsfo) {
		this.totalBunkeringLsfo = totalBunkeringLsfo;
	}



	public Double getTotalBunkeringHsdo() {
		return totalBunkeringHsdo;
	}



	public void setTotalBunkeringHsdo(Double totalBunkeringHsdo) {
		this.totalBunkeringHsdo = totalBunkeringHsdo;
	}



	public Double getTotalBunkeringLsdo() {
		return totalBunkeringLsdo;
	}



	public void setTotalBunkeringLsdo(Double totalBunkeringLsdo) {
		this.totalBunkeringLsdo = totalBunkeringLsdo;
	}



	public Double getTotalBunkeringLo() {
		return totalBunkeringLo;
	}



	public void setTotalBunkeringLo(Double totalBunkeringLo) {
		this.totalBunkeringLo = totalBunkeringLo;
	}



	public Double getTotalBunkeringFw() {
		return totalBunkeringFw;
	}



	public void setTotalBunkeringFw(Double totalBunkeringFw) {
		this.totalBunkeringFw = totalBunkeringFw;
	}



	public Double getTotalRobLo() {
		return totalRobLo;
	}



	public void setTotalRobLo(Double totalRobLo) {
		this.totalRobLo = totalRobLo;
	}



	public Double getTotalRobFw() {
		return totalRobFw;
	}



	public void setTotalRobFw(Double totalRobFw) {
		this.totalRobFw = totalRobFw;
	}



	public Integer getIgsStartHour() {
		return igsStartHour;
	}



	public void setIgsStartHour(Integer igsStartHour) {
		this.igsStartHour = igsStartHour;
	}



	public Integer getIgsStartMinute() {
		return igsStartMinute;
	}



	public void setIgsStartMinute(Integer igsStartMinute) {
		this.igsStartMinute = igsStartMinute;
	}



	public Integer getIgsStopHour() {
		return igsStopHour;
	}



	public void setIgsStopHour(Integer igsStopHour) {
		this.igsStopHour = igsStopHour;
	}



	public Integer getIgsStopMinute() {
		return igsStopMinute;
	}



	public void setIgsStopMinute(Integer igsStopMinute) {
		this.igsStopMinute = igsStopMinute;
	}



	public Double getMainEngineLo() {
		return mainEngineLo;
	}



	public void setMainEngineLo(Double mainEngineLo) {
		this.mainEngineLo = mainEngineLo;
	}



	public Double getMainEngineFw() {
		return mainEngineFw;
	}



	public void setMainEngineFw(Double mainEngineFw) {
		this.mainEngineFw = mainEngineFw;
	}



	public Double getMainEngineEngineDistance() {
		return mainEngineEngineDistance;
	}



	public void setMainEngineEngineDistance(Double mainEngineEngineDistance) {
		this.mainEngineEngineDistance = mainEngineEngineDistance;
	}



	public Double getMainEngineTotalEngineDistance() {
		return mainEngineTotalEngineDistance;
	}



	public void setMainEngineTotalEngineDistance(
			Double mainEngineTotalEngineDistance) {
		this.mainEngineTotalEngineDistance = mainEngineTotalEngineDistance;
	}



	public Double getMainEngineAvEngineSpeed() {
		return mainEngineAvEngineSpeed;
	}



	public void setMainEngineAvEngineSpeed(Double mainEngineAvEngineSpeed) {
		this.mainEngineAvEngineSpeed = mainEngineAvEngineSpeed;
	}



	public Double getMainEngineTotalAvEngineSpeed() {
		return mainEngineTotalAvEngineSpeed;
	}



	public void setMainEngineTotalAvEngineSpeed(Double mainEngineTotalAvEngineSpeed) {
		this.mainEngineTotalAvEngineSpeed = mainEngineTotalAvEngineSpeed;
	}



	public Double getMainEngineH24AvWk() {
		return mainEngineH24AvWk;
	}



	public void setMainEngineH24AvWk(Double mainEngineH24AvWk) {
		this.mainEngineH24AvWk = mainEngineH24AvWk;
	}



	public Double getMainEngineRpm() {
		return mainEngineRpm;
	}



	public void setMainEngineRpm(Double mainEngineRpm) {
		this.mainEngineRpm = mainEngineRpm;
	}



	public List<NoonPositionDieselGenerator> getDieselGenerators() {
		return dieselGenerators;
	}



	public void setDieselGenerators(
			List<NoonPositionDieselGenerator> dieselGenerators) {
		this.dieselGenerators = dieselGenerators;
	}



	public Double getDieselGeneratorsTotalHsfo() {
		return dieselGeneratorsTotalHsfo;
	}



	public void setDieselGeneratorsTotalHsfo(Double dieselGeneratorsTotalHsfo) {
		this.dieselGeneratorsTotalHsfo = dieselGeneratorsTotalHsfo;
	}



	public Double getDieselGeneratorsTotalLsfo() {
		return dieselGeneratorsTotalLsfo;
	}



	public void setDieselGeneratorsTotalLsfo(Double dieselGeneratorsTotalLsfo) {
		this.dieselGeneratorsTotalLsfo = dieselGeneratorsTotalLsfo;
	}



	public Double getDieselGeneratorsTotalHsdo() {
		return dieselGeneratorsTotalHsdo;
	}



	public void setDieselGeneratorsTotalHsdo(Double dieselGeneratorsTotalHsdo) {
		this.dieselGeneratorsTotalHsdo = dieselGeneratorsTotalHsdo;
	}



	public Double getDieselGeneratorsTotalLsdo() {
		return dieselGeneratorsTotalLsdo;
	}



	public void setDieselGeneratorsTotalLsdo(Double dieselGeneratorsTotalLsdo) {
		this.dieselGeneratorsTotalLsdo = dieselGeneratorsTotalLsdo;
	}



	public Integer getBoilerStartHour() {
		return boilerStartHour;
	}



	public void setBoilerStartHour(Integer boilerStartHour) {
		this.boilerStartHour = boilerStartHour;
	}



	public Integer getBoilerStartMinute() {
		return boilerStartMinute;
	}



	public void setBoilerStartMinute(Integer boilerStartMinute) {
		this.boilerStartMinute = boilerStartMinute;
	}



	public Integer getBoilerStopHour() {
		return boilerStopHour;
	}



	public void setBoilerStopHour(Integer boilerStopHour) {
		this.boilerStopHour = boilerStopHour;
	}



	public Integer getBoilerStopMinute() {
		return boilerStopMinute;
	}



	public void setBoilerStopMinute(Integer boilerStopMinute) {
		this.boilerStopMinute = boilerStopMinute;
	}



	public Integer getHeatingStartHour() {
		return heatingStartHour;
	}



	public void setHeatingStartHour(Integer heatingStartHour) {
		this.heatingStartHour = heatingStartHour;
	}



	public Integer getHeatingStartMinute() {
		return heatingStartMinute;
	}



	public void setHeatingStartMinute(Integer heatingStartMinute) {
		this.heatingStartMinute = heatingStartMinute;
	}



	public Integer getHeatingStopHour() {
		return heatingStopHour;
	}



	public void setHeatingStopHour(Integer heatingStopHour) {
		this.heatingStopHour = heatingStopHour;
	}



	public Integer getHeatingStopMinute() {
		return heatingStopMinute;
	}



	public void setHeatingStopMinute(Integer heatingStopMinute) {
		this.heatingStopMinute = heatingStopMinute;
	}



	public Integer getTankCleaningStartHour() {
		return tankCleaningStartHour;
	}



	public void setTankCleaningStartHour(Integer tankCleaningStartHour) {
		this.tankCleaningStartHour = tankCleaningStartHour;
	}



	public Integer getTankCleaningStartMinute() {
		return tankCleaningStartMinute;
	}



	public void setTankCleaningStartMinute(Integer tankCleaningStartMinute) {
		this.tankCleaningStartMinute = tankCleaningStartMinute;
	}



	public Integer getTankCleaningStopHour() {
		return tankCleaningStopHour;
	}



	public void setTankCleaningStopHour(Integer tankCleaningStopHour) {
		this.tankCleaningStopHour = tankCleaningStopHour;
	}



	public Integer getTankCleaningStopMinute() {
		return tankCleaningStopMinute;
	}



	public void setTankCleaningStopMinute(Integer tankCleaningStopMinute) {
		this.tankCleaningStopMinute = tankCleaningStopMinute;
	}



	public Integer getOtherMachineryStartHour() {
		return otherMachineryStartHour;
	}



	public void setOtherMachineryStartHour(Integer otherMachineryStartHour) {
		this.otherMachineryStartHour = otherMachineryStartHour;
	}



	public Integer getOtherMachineryStartMinute() {
		return otherMachineryStartMinute;
	}



	public void setOtherMachineryStartMinute(Integer otherMachineryStartMinute) {
		this.otherMachineryStartMinute = otherMachineryStartMinute;
	}



	public Integer getOtherMachineryStopHour() {
		return otherMachineryStopHour;
	}



	public void setOtherMachineryStopHour(Integer otherMachineryStopHour) {
		this.otherMachineryStopHour = otherMachineryStopHour;
	}



	public Integer getOtherMachineryStopMinute() {
		return otherMachineryStopMinute;
	}



	public void setOtherMachineryStopMinute(Integer otherMachineryStopMinute) {
		this.otherMachineryStopMinute = otherMachineryStopMinute;
	}



	public Double getMainEngineTotalLo() {
		return mainEngineTotalLo;
	}



	public void setMainEngineTotalLo(Double mainEngineTotalLo) {
		this.mainEngineTotalLo = mainEngineTotalLo;
	}



	public Double getMainEngineTotalFw() {
		return mainEngineTotalFw;
	}



	public void setMainEngineTotalFw(Double mainEngineTotalFw) {
		this.mainEngineTotalFw = mainEngineTotalFw;
	}



	public Double getMainEngineSlip() {
		return mainEngineSlip;
	}



	public void setMainEngineSlip(Double mainEngineSlip) {
		this.mainEngineSlip = mainEngineSlip;
	}



	public Double getMainEngineTotalSlip() {
		return mainEngineTotalSlip;
	}



	public void setMainEngineTotalSlip(Double mainEngineTotalSlip) {
		this.mainEngineTotalSlip = mainEngineTotalSlip;
	}



	public boolean isInPort() {
		return inPort;
	}



	public void setInPort(boolean inPort) {
		this.inPort = inPort;
	}



	public Date getArrivalPortEndOfPassage() {
		return arrivalPortEndOfPassage;
	}



	public void setArrivalPortEndOfPassage(Date arrivalPortEndOfPassage) {
		this.arrivalPortEndOfPassage = arrivalPortEndOfPassage;
	}



	public Date getArrivalPortNoticeOfReadinessTendered() {
		return arrivalPortNoticeOfReadinessTendered;
	}



	public void setArrivalPortNoticeOfReadinessTendered(
			Date arrivalPortNoticeOfReadinessTendered) {
		this.arrivalPortNoticeOfReadinessTendered = arrivalPortNoticeOfReadinessTendered;
	}



	public Date getArrivalPortAnchored() {
		return arrivalPortAnchored;
	}



	public void setArrivalPortAnchored(Date arrivalPortAnchored) {
		this.arrivalPortAnchored = arrivalPortAnchored;
	}



	public Date getArrivalPortAnchorAweigh() {
		return arrivalPortAnchorAweigh;
	}



	public void setArrivalPortAnchorAweigh(Date arrivalPortAnchorAweigh) {
		this.arrivalPortAnchorAweigh = arrivalPortAnchorAweigh;
	}



	public Date getManouvringPilotOnBoard() {
		return manouvringPilotOnBoard;
	}



	public void setManouvringPilotOnBoard(Date manouvringPilotOnBoard) {
		this.manouvringPilotOnBoard = manouvringPilotOnBoard;
	}



	public Date getManouvringArrBerth() {
		return manouvringArrBerth;
	}



	public void setManouvringArrBerth(Date manouvringArrBerth) {
		this.manouvringArrBerth = manouvringArrBerth;
	}



	public Date getManouvringFla() {
		return manouvringFla;
	}



	public void setManouvringFla(Date manouvringFla) {
		this.manouvringFla = manouvringFla;
	}



	public Date getManouvringMooringComplete() {
		return manouvringMooringComplete;
	}



	public void setManouvringMooringComplete(Date manouvringMooringComplete) {
		this.manouvringMooringComplete = manouvringMooringComplete;
	}



	public Date getManouvringFwe() {
		return manouvringFwe;
	}



	public void setManouvringFwe(Date manouvringFwe) {
		this.manouvringFwe = manouvringFwe;
	}



	public Date getDeparturePortPilotOnBoard() {
		return departurePortPilotOnBoard;
	}



	public void setDeparturePortPilotOnBoard(Date departurePortPilotOnBoard) {
		this.departurePortPilotOnBoard = departurePortPilotOnBoard;
	}



	public Date getDeparturePortLastMooringLineCastOff() {
		return departurePortLastMooringLineCastOff;
	}



	public void setDeparturePortLastMooringLineCastOff(
			Date departurePortLastMooringLineCastOff) {
		this.departurePortLastMooringLineCastOff = departurePortLastMooringLineCastOff;
	}



	public Date getDeparturePortPilotAway() {
		return departurePortPilotAway;
	}



	public void setDeparturePortPilotAway(Date departurePortPilotAway) {
		this.departurePortPilotAway = departurePortPilotAway;
	}



	public Date getDeparturePortFullAwayOnPassage() {
		return departurePortFullAwayOnPassage;
	}



	public void setDeparturePortFullAwayOnPassage(
			Date departurePortFullAwayOnPassage) {
		this.departurePortFullAwayOnPassage = departurePortFullAwayOnPassage;
	}



	public Date getDeparturePortFweCosp() {
		return departurePortFweCosp;
	}



	public void setDeparturePortFweCosp(Date departurePortFweCosp) {
		this.departurePortFweCosp = departurePortFweCosp;
	}



	public Byte getInPortOperationType() {
		return inPortOperationType;
	}



	public void setInPortOperationType(Byte inPortOperationType) {
		this.inPortOperationType = inPortOperationType;
	}



	public String getTimeZone() {
		return timeZone;
	}



	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

}