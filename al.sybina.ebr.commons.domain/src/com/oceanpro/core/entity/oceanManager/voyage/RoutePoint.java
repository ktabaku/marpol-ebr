package com.oceanpro.core.entity.oceanManager.voyage;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
import org.apache.commons.lang.time.DateUtils;

import com.oceanpro.core.utils.ScaleConversionUtils;
import com.oceanpro.utils.Coordinates;

/**
 * 
 * Class name: RoutePoint
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
@javax.persistence.Table(name="ocean_voyage_routepoint")
public class RoutePoint implements Comparable<RoutePoint>{

	
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
	private Double pressure;
	
	@Basic
	private Double windSpeed;
	@Basic
	private Double windDirection;
	@Basic
	private Double windEffectOnShip;
	
	@Basic
	private Double waveHeight;
	@Basic
	private Double waveDirection;
	@Basic
	private Double wavePeriod; // secondi
	@Basic
	private Double waveEffectOnShip;
	
	@Basic
	private Double currentSpeed;
	@Basic
	private Double currentDirirection;
	@Basic
	private Double currentEffectOnShip; // effetto frizione acqua + corrente
	@Basic
	private Double onlyCurrentEffectOnShip; // effetto solo corrente
	
	@Basic
	private Double residualEffectOnShip; // potenza residua
	
	@Basic
	private Double realRpm;
	@Basic
	private Double emiRpm;
	
	@Basic
	private String latitude;
	@Basic
	private String longitude;
	@Basic
	private String trueCourse; // Rotta
	@Basic
	private Double foConsumption; // Consumo motore (in litri)
//	@Basic
//	private Double doConsumption; // Consumo generatore (in litri)
	@Basic
	private Double speed; // Knot = 1 miglio nautico/h
	@Basic
	private Double enginePower; // in kWh
	@Basic
	private Double shaftPower; // potenza all'albero in kWh
	
	@Basic
	private Double correctionFactor; // fattore di correzione che viene aggiornato a ogni iterazione
	
	@Basic
	private Double co2Produced; // Co2 Prodotto nell'intervallo tra un route point e l'altro
	@Basic
	private Double co2Saved; // Co2 risparmiato nell'intervallo tra un route point e l'altro rispetto alla potenza indicata nei dati del charter party
	
	@Basic
	private Double c; // Valore C
	
	@Basic
	private Double fm1; // Flowmeter1(MainEngine + DieselGen1 + DieselGen2 + DieselGen3) 	
	@Basic
	private Double fm2; // Flowmeter2 (in DieselGen1 + DieselGen2 + DieselGen3)	
	@Basic
	private Double fm3; // Flowmeter3 (out*di sottrazione* :  DieselGen1 + DieselGen2 + DieselGen3)	 
	@Basic
	private Double fm4; // Flowmeter4 (Aux Boiler)	
	@Basic
	private Double fm5; // Flowmeter5 (Comp. Boiler su F.O.)	
	@Basic
	private Double fm6; // Flowmeter6 (Comp. Boiler su MGO)
	
	@Basic
	private Double t1; // TemperatureSensor1 (Relativo ad fm1,fm2,fm3)	
	@Basic
	private Double t2; // TemperatureSensor2 (Non Utilizzato)	
	@Basic
	private Double t3; // TemperatureSensor3 (Non Utilizzato)	
	@Basic
	private Double t4; // TemperatureSensor4 (Relativo ad fm4)	
	@Basic
	private Double t5; // TemperatureSensor5 (Relativo ad fm5)	
	@Basic
	private Double t6; // TemperatureSensor6 (Relativo ad fm6)
	
	//cambiati per cambio tipo bunker da hsfo,lsfo,hsdo,lsdo a lfo,hfo,mdo
//	@Basic
//	private Long mainEngineMinuteHsfo ; 
//	@Basic
//	private Long mainEngineMinuteHsdo ;	
//	@Basic
//	private Long mainEngineMinuteLsfo ;	
//	@Basic
//	private Long mainEngineMinuteLsdo ;	
	
//	@Basic
//	private Long dieselGenMinuteHsfo ; 
//	@Basic
//	private Long dieselGenMinuteHsdo ;	
//	@Basic
//	private Long dieselGenMinuteLsfo ;	
//	@Basic
//	private Long dieselGenMinuteLsdo ;	
//	
//	@Basic
//	private Long auxBoilerMinuteHsfo ;
//	@Basic
//	private Long auxBoilerMinuteHsdo ;	
//	@Basic
//	private Long auxBoilerMinuteLsfo ;	
//	@Basic
//	private Long auxBoilerMinuteLsdo ;
	
	@Basic
	private Long mainEngineMinuteHfo ; 
	@Basic
	private Long mainEngineMinuteLfo ;	
	@Basic
	private Long mainEngineMinuteMdo ;	
	
	@Basic
	private Long dieselGenMinuteHfo ; 	
	@Basic
	private Long dieselGenMinuteLfo ;	
	@Basic
	private Long dieselGenMinuteMdo ;	
	
	@Basic
	private Long auxBoilerMinuteHfo ;	
	@Basic
	private Long auxBoilerMinuteLfo ;	
	@Basic
	private Long auxBoilerMinuteMdo ;	
	
	
	
	
	
	
	
	@Basic
	private String legType; // il tipo di leg che pu� essere SEA, TRANSIT, PORT 

	
	
	

//	@Column(columnDefinition="INT(1)")
//	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Basic
	private boolean forShipTracking; // Indica se il roure point � calcolato da emi o solo per tracking
	
	@Basic
	private Long shipId ; // Ship ID - for tracking only

	
	// DieselGenerator 1 2 e 3  in kg per ora
	public double getDgsFlowConsInTons() {
		return (fm2 - fm3)/1000;
	}	

	// Restituisce i minuti di moto del  Main Engine
	private double getMeRunMinute() {
		//cambiati per cambio tipo bunker da hsfo,lsfo,hsdo,lsdo a lfo,hfo,mdo
//		return ((getMainEngineMinuteHsfo()!=null?getMainEngineMinuteHsfo():0) + (getMainEngineMinuteHsdo()!=null?getMainEngineMinuteHsdo():0)+
//				(getMainEngineMinuteLsdo()!=null?getMainEngineMinuteLsdo():0) + (getMainEngineMinuteLsfo()!=null?getMainEngineMinuteLsfo():0));		 
				
		return ((getMainEngineMinuteHfo()!=null?getMainEngineMinuteHfo():0) + (getMainEngineMinuteLfo()!=null?getMainEngineMinuteLfo():0)+
				(getMainEngineMinuteMdo()!=null?getMainEngineMinuteMdo():0));
	}
	
	// Restituisce i consumi totali di fuel Hfo consumati dal Main Engine
	//in base alle running hous 
	public double getTotMeConsMtHfo() {
		//cambiati per cambio tipo bunker da hsfo,lsfo,hsdo,lsdo a lfo,hfo,mdo
//		return ( (getMainEngineFlowConsInTons()!=0.0 && getMeRunMinute()!= 0.0?getMainEngineFlowConsInTons()/getMeRunMinute():0.0)*(getMainEngineMinuteHsfo()!=null?getMainEngineMinuteHsfo():0) );
		
		return ( (getMainEngineFlowConsInTons()!=0.0 && getMeRunMinute()!= 0.0?getMainEngineFlowConsInTons()/getMeRunMinute():0.0)*(getMainEngineMinuteHfo()!=null?getMainEngineMinuteHfo():0) );
	}	

	// Restituisce i consumi totali di fuel Lfo consumati dal Main Engine
	//in base alle running hous 
	public double getTotMeConsMtLfo() {
		//cambiati per cambio tipo bunker da hsfo,lsfo,hsdo,lsdo a lfo,hfo,mdo
//		return ( (getMainEngineFlowConsInTons()!=0.0 && getMeRunMinute()!= 0.0?getMainEngineFlowConsInTons()/getMeRunMinute():0.0)*(getMainEngineMinuteLsfo()!=null?getMainEngineMinuteLsfo():0) );
		
		return ( (getMainEngineFlowConsInTons()!=0.0 && getMeRunMinute()!= 0.0?getMainEngineFlowConsInTons()/getMeRunMinute():0.0)*(getMainEngineMinuteLfo()!=null?getMainEngineMinuteLfo():0) );
	}
	
	// Restituisce i consumi totali di fuel Mdo consumati dal Main Engine
	//in base alle running hous 
	public double getTotMeConsMtMdo() {
		//cambiati per cambio tipo bunker da hsfo,lsfo,hsdo,lsdo a lfo,hfo,mdo
//		return ( (getMainEngineFlowConsInTons()!=0.0 && getMeRunMinute()!= 0.0?getMainEngineFlowConsInTons()/getMeRunMinute():0.0)*(getMainEngineMinuteHsdo()!=null?getMainEngineMinuteHsdo():0) );
		
		return ( (getMainEngineFlowConsInTons()!=0.0 && getMeRunMinute()!= 0.0?getMainEngineFlowConsInTons()/getMeRunMinute():0.0)*(getMainEngineMinuteMdo()!=null?getMainEngineMinuteMdo():0) );
	}	
	
	//cambiati per cambio tipo bunker da hsfo,lsfo,hsdo,lsdo a lfo,hfo,mdo
//	// Restituisce i consumi totali di fuel Lsdo consumati dal Main Engine
//	//in base alle running hous 
//	public double getTotMeConsMtLsdo() {
//		return ( (getMainEngineFlowConsInTons()!=0.0 && getMeRunMinute()!= 0.0?getMainEngineFlowConsInTons()/getMeRunMinute():0.0)*(getMainEngineMinuteLsdo()!=null?getMainEngineMinuteLsdo():0) );
//	}	
	

	
	// Restituisce i minuti di moto del  Diesel Generator
	private double getDgRunMinute() {
		//cambiati per cambio tipo bunker da hsfo,lsfo,hsdo,lsdo a lfo,hfo,mdo
//		return ((getDieselGenMinuteHsfo()!=null?getDieselGenMinuteHsfo():0) + (getDieselGenMinuteHsdo()!=null?getDieselGenMinuteHsdo():0)+
//				(getDieselGenMinuteLsdo()!=null?getDieselGenMinuteLsdo():0) + (getDieselGenMinuteLsfo()!=null?getDieselGenMinuteLsfo():0));	
		
		return ((getDieselGenMinuteHfo()!=null?getDieselGenMinuteHfo():0) + (getDieselGenMinuteLfo()!=null?getDieselGenMinuteLfo():0) +
				(getDieselGenMinuteMdo()!=null?getDieselGenMinuteMdo():0));	
	}
	
	// Restituisce i consumi totali di fuel Hfo consumati dal Diesel Generator
	//in base alle running hous 
	public double getTotDgConsMtHfo() {
		//cambiati per cambio tipo bunker da hsfo,lsfo,hsdo,lsdo a lfo,hfo,mdo
//		return ( (getDgsFlowConsInTons()!=0.0 && getDgRunMinute()!= 0.0?getDgsFlowConsInTons()/getDgRunMinute():0.0)*(getDieselGenMinuteHsfo()!=null?getDieselGenMinuteHsfo():0) );
		
		return ( (getDgsFlowConsInTons()!=0.0 && getDgRunMinute()!= 0.0?getDgsFlowConsInTons()/getDgRunMinute():0.0)*(getDieselGenMinuteHfo()!=null?getDieselGenMinuteHfo():0) );
	}	

	//cambiati per cambio tipo bunker da hsfo,lsfo,hsdo,lsdo a lfo,hfo,mdo
//	// Restituisce i consumi totali di fuel Hsdo consumati dal Diesel Generator
//	//in base alle running hous 
//	public double getTotDgConsMtHsdo() {		
//		return ( (getDgsFlowConsInTons()!=0.0 && getDgRunMinute()!= 0.0?getDgsFlowConsInTons()/getDgRunMinute():0.0)*(getDieselGenMinuteHsdo()!=null?getDieselGenMinuteHsdo():0) );
//	}	
	
	// Restituisce i consumi totali di fuel Lsfo consumati dal Diesel Generator
	//in base alle running hous 
	public double getTotDgConsMtLfo() {
		//cambiati per cambio tipo bunker da hsfo,lsfo,hsdo,lsdo a lfo,hfo,mdo
//		return ( (getDgsFlowConsInTons()!=0.0 && getDgRunMinute()!= 0.0?getDgsFlowConsInTons()/getDgRunMinute():0.0)*(getDieselGenMinuteLsfo()!=null?getDieselGenMinuteLsfo():0) );
		
		return ( (getDgsFlowConsInTons()!=0.0 && getDgRunMinute()!= 0.0?getDgsFlowConsInTons()/getDgRunMinute():0.0)*(getDieselGenMinuteLfo()!=null?getDieselGenMinuteLfo():0) );
	}
	
	// Restituisce i consumi totali di fuel Mdo consumati dal Diesel Generator
	//in base alle running hous 
	public double getTotDgConsMtMdo() {
		//cambiati per cambio tipo bunker da hsfo,lsfo,hsdo,lsdo a lfo,hfo,mdo
		return ( (getDgsFlowConsInTons()!=0.0 && getDgRunMinute()!= 0.0?getDgsFlowConsInTons()/getDgRunMinute():0.0)*(getDieselGenMinuteMdo()!=null?getDieselGenMinuteMdo():0) );
	}
	
	// Restituisce i minuti di moto del  Aux Boiler
	private double getAuxBoilerRunMinute() {
		//cambiati per cambio tipo bunker da hsfo,lsfo,hsdo,lsdo a lfo,hfo,mdo
//		return ((getAuxBoilerMinuteHsfo()!=null?getAuxBoilerMinuteHsfo():0) + (getAuxBoilerMinuteHsdo()!=null?getAuxBoilerMinuteHsdo():0)+
//				(getAuxBoilerMinuteLsdo()!=null?getAuxBoilerMinuteLsdo():0) + (getAuxBoilerMinuteLsfo()!=null?getAuxBoilerMinuteLsfo():0));
		
		return ((getAuxBoilerMinuteHfo()!=null?getAuxBoilerMinuteHfo():0) + (getAuxBoilerMinuteLfo()!=null?getAuxBoilerMinuteLfo():0)+
				(getAuxBoilerMinuteMdo()!=null?getAuxBoilerMinuteMdo():0));
	}
	
	// Restituisce i consumi totali di fuel Hfo consumati dal Aux Boiler
	//in base alle running hous 
	public double getTotAuxBoilerConsMtHfo() {
		//cambiati per cambio tipo bunker da hsfo,lsfo,hsdo,lsdo a lfo,hfo,mdo
//		return ( (getFm4InTons()!=0.0 && getAuxBoilerRunMinute()!= 0.0?getFm4InTons()/getAuxBoilerRunMinute():0.0)*(getAuxBoilerMinuteHsfo()!=null?getAuxBoilerMinuteHsfo():0) );
		
		return ( (getFm4InTons()!=0.0 && getAuxBoilerRunMinute()!= 0.0?getFm4InTons()/getAuxBoilerRunMinute():0.0)*(getAuxBoilerMinuteHfo()!=null?getAuxBoilerMinuteHfo():0) );
	}	

	//cambiati per cambio tipo bunker da hsfo,lsfo,hsdo,lsdo a lfo,hfo,mdo
//	// Restituisce i consumi totali di fuel Lsdo consumati dal Aux Boiler 
//	//in base alle running hous 
//	public double getTotAuxBoilerConsMtLsdo() {		
//		return ( (getFm4InTons()!=0.0 && getAuxBoilerRunMinute()!= 0.0?getFm4InTons()/getAuxBoilerRunMinute():0.0)*(getAuxBoilerMinuteLsdo()!=null?getAuxBoilerMinuteLsdo():0) );
//	}	
	
	// Restituisce i consumi totali di fuel Lfo consumati dal Aux Boiler
	//in base alle running hous 
	public double getTotAuxBoilerConsMtLfo() {
		//cambiati per cambio tipo bunker da hsfo,lsfo,hsdo,lsdo a lfo,hfo,mdo
		return ( (getFm4InTons()!=0.0 && getAuxBoilerRunMinute()!= 0.0?getFm4InTons()/getAuxBoilerRunMinute():0.0)*(getAuxBoilerMinuteLfo()!=null?getAuxBoilerMinuteLfo():0) );
	}
	
	// Restituisce i consumi totali di fuel Mdo consumati dal Aux Boiler
	//in base alle running hous 
	public double getTotAuxBoilerConsMtMdo() {
		//cambiati per cambio tipo bunker da hsfo,lsfo,hsdo,lsdo a lfo,hfo,mdo
		return ( (getFm4InTons()!=0.0 && getAuxBoilerRunMinute()!= 0.0?getFm4InTons()/getAuxBoilerRunMinute():0.0)*(getAuxBoilerMinuteMdo()!=null?getAuxBoilerMinuteMdo():0) );
	}
	
	public String getDgsFlowConsInTonsString() {
		return getDoubleToStringRoundThree((fm2 - fm3)/1000);
	}
	
	/**
	 * Restituisce i consumi media nelle 24 ore del DieselGenerator 1 2 e 3(basandosi sul punto corrente)
	 * @return consumi in Tons (Stringa troncata a 3 decimali)
	 */
	public String getAverageDGsFlowConsIn24Hours() {
		return getDoubleToStringRoundThree( (voyage.getRoutePointAverageFm2In24Hours(this) - voyage.getRoutePointAverageFm3In24Hours(this) )/1000);
	}
	
	// Main Engine in kg per ora
	public double getMainEngineFlowConsInTons() { 
		return (fm1 - fm2 + fm3)/1000 ;
	}
	

	public String getMainEngineFlowConsInTonsString() {
		return getDoubleToStringRoundThree((fm1 - fm2 + fm3)/(double)1000 );
	}
	
	/**
	 * Restituisce i consumi media nelle 24 ore del  Main Engine(basandosi sul punto corrente)
	 * @return consumi in Tons (Stringa troncata a 3 decimali)
	 */
	public String getAverageMainEngineFlowConsIn24Hours() {
		return getDoubleToStringRoundThree( voyage.getRoutePointAverageFm1In24Hours(this) /1000);
		//return getDoubleToStringRoundThree( (voyage.getRoutePointAverageFm1In24Hours(this) - (voyage.getRoutePointAverageFm2In24Hours(this) - voyage.getRoutePointAverageFm3In24Hours(this) ) )/1000);
	}
	
	
	public String getTotMainEngineFlowConsInTonsPerVoy() {
//		System.out.println("getTotMainEngineFlowConsInTonsPerVoy:  " + voyage.getMainEngineFlowConsPerVoyage(this));
//		System.out.println("getTotMainEngineFlowConsInTonsPerVoy round:  " +  getDoubleToStringRoundTwo( voyage.getMainEngineFlowConsPerVoyage(this)));
		return getDoubleToStringRoundTwo( voyage.getMainEngineFlowConsPerVoyage(this));
	}
	
	
	/*AC MOD*/
	
	 /**
	 * Restituisce il totale dei consumi del flussometro main engine nelle 24 ore (basandosi sul punto corrente)
	 * @return Tons (Stringa troncata a 2 decimali)
	 */
	public String getMainEngineFlowConsInTonsIn24HoursString() {
		return getDoubleToStringRoundTwo(voyage.getMainEngineFlowConsInTonsIn24Hours(this));
	}
	
	 /**
	 * Restituisce il totale dei consumi del flussometro Diesel Generator nelle 24 ore (basandosi sul punto corrente)
	 * @return Tons (Stringa troncata a 2 decimali)
	 */
	public String getDgsFlowConsInTonsIn24HoursString() {
		return getDoubleToStringRoundTwo(voyage.getDgsFlowConsInTonsIn24Hours(this));
	}
	
	
	
	public String getTotDgsFlowConsInTonsPerVoy() {
		return getDoubleToStringRoundTwo( voyage.getDgsFlowConsPerVoyage(this));
	}
	
	
	
	 /**
	 * Restituisce il totale dei consumi del FM4 (aux boiler) nelle 24 ore (basandosi sul punto corrente)
	 * @return Tons (Stringa troncata a 2 decimali)
	 */
	public String getFm4FlowConsInTonsIn24HoursString() {
		return getDoubleToStringRoundTwo(voyage.getfm4FlowConsInTonsIn24Hours(this));
	}
	
	
	 /**
	 * Restituisce nome del way point (basandosi sulla registration date del routepoint)
	 * @return string
	 */
	public VoyagePath getActivePath() {
			
		VoyagePath vp = null;
		vp = voyage.getActivePathForDate(this.getRegistrationDate());
		//vp = this.getVoyage().getActivePathForDate(this.getRegistrationDate());
		return vp;
	}
	
	 /**
	 * Restituisce nome del way point (basandosi sulla registration date del routepoint) e mai null
	 * @return string
	 */
	public long getActivePath2() {
			
		VoyagePath vp = null;
		vp = voyage.getActivePathForDate(this.getRegistrationDate());
		if(vp==null) return 0;
		
		return vp.getId();
	}	
	
	
	public String getTotFm4FlowConsInTonsPerVoy() {
		return getDoubleToStringRoundTwo( voyage.getFm4FlowConsPerVoyage(this));
	}
	
	
    //in kg per ora
	public Double getFm1() {
		return fm1;		
	}

	//in Tonnellate per ora
	public String getFm1InTons() {
		return getDoubleToStringRoundThree(fm1/1000);
	}

	public void setFm1(Double fm1) {
		this.fm1 = fm1;
	}

	public Double getFm2() {
		return fm2;
	}
	
	public String getFm2InTons() {
		return getDoubleToStringRoundThree(fm2/1000);
	}

	public void setFm2(Double fm2) {
		this.fm2 = fm2;
	}

	public Double getFm3() {
		return fm3;
	}
	
	public String getFm3InTons() {
		return getDoubleToStringRoundThree(fm3/1000);
	}
		
	public void setFm3(Double fm3) {
		this.fm3 = fm3;
	}
    
	//Caldaia Ausiliaria in kg per ora
	public Double getFm4() {
		return fm4;
	}
	
	//Caldaia Ausiliaria in Tonnellate per ora
	public Double getFm4InTons() {
		return fm4/1000;
	}
	
	public String getFm4InTonsString() {
		return getDoubleToStringRoundThree(fm4/1000);
	}

	public void setFm4(Double fm4) {
		this.fm4 = fm4;
	}
	
	//Caldaia Composita su F.O. in kg per ora
	public Double getFm5() {
		return fm5;
	}

	public String getFm5InTons() {
		return getDoubleToStringRoundThree(fm5/1000);
	}
	
	public void setFm5(Double fm5) {
		this.fm5 = fm5;
	}

	//Caldaia Composita su MGO in kg per ora
	public Double getFm6() {
		return fm6;
	}

	public void setFm6(Double fm6) {
		this.fm6 = fm6;
	}

	public Double getT1() {
		return t1;
	}

	public void setT1(Double t1) {
		this.t1 = t1;
	}

	public Double getT2() {
		return t2;
	}

	public void setT2(Double t2) {
		this.t2 = t2;
	}

	public Double getT3() {
		return t3;
	}

	public void setT3(Double t3) {
		this.t3 = t3;
	}

	public Double getT4() {
		return t4;
	}
	
	public String getT4Round() {
		return getDoubleToStringRoundTwo(t4);
	}

	public void setT4(Double t4) {
		this.t4 = t4;
	}

	public Double getT5() {
		return t5;
	}

	public void setT5(Double t5) {
		this.t5 = t5;
	}

	public Double getT6() {
		return t6;
	}

	public void setT6(Double t6) {
		this.t6 = t6;
	}
	
	/**
	 * Restituisce la velocit� media nelle 24 ore (basandosi sul punto corrente)
	 * @return consumi in Tons (Stringa troncata a 3 decimali)
	 */
	public String getAverageFm1In24Hours() {
		return getDoubleToStringRoundThree(voyage.getRoutePointAverageFm1In24Hours(this)/1000);
	}

	/**
	 * Restituisce la velocit� media nelle 24 ore (basandosi sul punto corrente)
	 * @return consumi in Tons (Stringa troncata a 3 decimali)
	 */
	public String getAverageFm2In24Hours() {
		return getDoubleToStringRoundThree(voyage.getRoutePointAverageFm2In24Hours(this)/1000);
	}
	
	/**
	 * Restituisce la velocit� media nelle 24 ore (basandosi sul punto corrente)
	 * @return consumi in Tons (Stringa troncata a 3 decimali)
	 */
	public String getAverageFm3In24Hours() {
		return getDoubleToStringRoundThree(voyage.getRoutePointAverageFm3In24Hours(this)/1000);
	}
	
	/**
	 * Restituisce la velocit� media nelle 24 ore (basandosi sul punto corrente)
	 * @return consumi in Tons (Stringa troncata a 3 decimali)
	 */
	public String getAverageFm4In24Hours() {
		return getDoubleToStringRoundThree(voyage.getRoutePointAverageFm4In24Hours(this)/1000);
	}
	
	/**
	 * Restituisce la velocit� media nelle 24 ore (basandosi sul punto corrente)
	 * @return consumi in Tons (Stringa troncata a 3 decimali)
	 */
	public String getAverageFm5In24Hours() {
		return getDoubleToStringRoundThree(voyage.getRoutePointAverageFm5In24Hours(this)/1000);
	}
	
	/**
	 * Restituisce la differenza tra i consumi del main engine e i consumi di emi 
	 * @return consumi in Tons (Stringa troncata a 3 decimali)
	 */
	public String getFoConsumptionDifferenceEmiVSFlow() {
//		return getDoubleToStringRoundThree(((fm1 -(fm2 - fm3))/1000)-foConsumption );				 
		return getDoubleToStringRoundThree( (fm1 -fm2 + fm3)!=0.0?((fm1 -fm2 + fm3)/1000)-foConsumption:(0.0-foConsumption));
	}
	
	/**
	 * Restituisce Mls/MT orari: consumi del main engine presi dai flussometri fratto speed 
	 * @return Mls/MT (Stringa troncata a 3 decimali)
	 */
	public String getMilePerTons() {		
//		return getDoubleToStringRoundThree(((fm1 -(fm2 - fm3))/1000)/speed);
		return getDoubleToStringRoundThree( ( (fm1 -fm2 + fm3)!=0.0 && speed!=0.0)?((fm1 -fm2 + fm3)/1000)/speed:0.0);
	}
	
	
	
	public void reset() {
		pressure = 0.0;
		windSpeed = 0.0;
		windDirection = 0.0;
		windEffectOnShip = 0.0;
		waveHeight = 0.0;
		waveDirection = 0.0;
		wavePeriod = 0.0;
		waveEffectOnShip = 0.0;
		currentSpeed = 0.0;
		currentDirirection = 0.0;
		currentEffectOnShip = 0.0;
		onlyCurrentEffectOnShip = 0.0;
		realRpm = 0.0;
		emiRpm = 0.0;
		latitude = "";
		longitude = "";
		trueCourse = "";
		foConsumption = 0.0;
//		doConsumption = 0.0;
		speed = 0.0;
		enginePower = 0.0;
		shaftPower = 0.0;
		co2Produced = 0.0;
		co2Saved = 0.0;
		
		fm1 = 0.0;
		fm2 = 0.0;
		fm3 = 0.0;
		fm4 = 0.0;
		fm5 = 0.0;
		fm6 = 0.0;
		
		t1 = 0.0;
		t2 = 0.0;
		t3 = 0.0;
		t4 = 0.0;
		t5 = 0.0;
		t6 = 0.0;
	}
	
	/**
	 * Costruttore vuoto
	 */
	public RoutePoint() {
		super();
	}
	

	
	public String getDailyRunningHours() {
//		return getVoyage().getRoutePointTimeIn24Hours(this);
		return getVoyage().getRoutePointRunningHours(this, true);
	}
	public String getTotalRunningHours() {
		return getVoyage().getRoutePointRunningHours(this, false);
	}
	
	
	
	public Coordinates getCoordinates() {
		if (getLatitude()==null || getLongitude()==null) return null;
		return new Coordinates(getLatitude().toString(), getLongitude().toString(), Coordinates.COOD_TYPE_NEGATIVE_DEGEE);
	}
	
	
	
	/**
	 * Restituisce la potenza del motore in KW
	 * @return potenza in KW (Stringa troncata a 2 decimali)
	 */
	public String getEnginePowerString() {
		return getDoubleToStringRoundTwo(getEnginePower());
	}
	/**
	 * Restituisce la potenza all'albero in KW
	 * @return potenza in KW (Stringa troncata a 2 decimali)
	 */
	public String getShaftPowerString() {
		return getDoubleToStringRoundTwo(getShaftPower());
	}
	
	/**
	 * Restituisce la velocit� media tra il punto corrente e quello precedente
	 * @return velocit� in nodi (Stringa troncata a 2 decimali)
	 */
	public String getAverageSpeedInOneHourString() {
		return getDoubleToStringRoundTwo(voyage.getRoutePointOneHourAvSpeed(this));
	}
	
	/**
	 * Restituisce la velocit� media tra il punto corrente e quello precedente
	 * @return velocit� in nodi
	 */
	public double getAverageSpeedInOneHour() {
		return voyage.getRoutePointOneHourAvSpeed(this);
	}
	/**
	 * Restituisce la velocit� media nelle 24 ore (basandosi sul punto corrente)
	 * @return velocit� in nodi (Stringa troncata a 2 decimali)
	 */
	public String getAverageSpeedIn24Hours() {
		return getDoubleToStringRoundTwo(voyage.getRoutePointAverageSpeedIn24Hours(this));
	}
	/**
	 * Restituisce la velocit� media considerando i giorni in good weather fino al punto corrente(basandosi sul punto corrente)
	 * @return velocit� in nodi (Stringa troncata a 2 decimali)
	 */
	public String getAverageSpeedInGoodWeatherUntilNow() {
		return getDoubleToStringRoundTwo(voyage.getAverageSpeedRoutePointInGoodWheatherDays(this));
	}
	
	
	/**
	 * Restituisce la velocit� media considerando i singoli punti che sono in good weather fino al punto corrente incluso (basandosi sul punto corrente)
	 * @return velocit� in nodi (Stringa troncata a 2 decimali)
	 */
	public String getAverageSpeedInGoodWeatherByRoutePointUntilNow() {
		double avSpeed = 0.0; // In nodi
		List<RoutePoint> rpgw = voyage.getRoutePointsInGoodWheather();
		if (rpgw!=null && rpgw.size()>0) {
			Collections.sort(rpgw);
			int i = 0;
			//System.out.println("getAverageSpeedInGoodWeatherByRoutePointUntilNow - punto corrente reg date: " + this.getRegistrationDate());
			for (RoutePoint rp : rpgw) {
				//System.out.println("getAverageSpeedInGoodWeatherByRoutePointUntilNow - punto analizzato reg date: " + rp.getRegistrationDate() + "   SPEED:" + rp.getSpeed());
				avSpeed += rp.getSpeed();
				i++;
				if (rp.getRegistrationDate()!=null && this.getRegistrationDate()!=null && rp.getRegistrationDate().after(this.getRegistrationDate())) break; // Questo serve perch� se il punto non � in good weater allora non sar� stra i punti ritornati
			}
			//System.out.println("getAverageSpeedInGoodWeatherByRoutePointUntilNow - calcolo velocit� media come "+ avSpeed + "/" + i);
			avSpeed = avSpeed/i;
			//System.out.println("getAverageSpeedInGoodWeatherByRoutePointUntilNow - risultato: "+ avSpeed);
		}

		return getDoubleToStringRoundTwo(new Double(avSpeed));
	}
	
	/**
	 * Restituisce la velocit� media calcolata fino al punto corrente
	 * @return velocit� in nodi (Stringa troncata a 2 decimali)
	 */
	public String getRoutePointTotalAverageSpeed() {
		return getDoubleToStringRoundTwo(voyage.getRoutePointAverageSpeed(this));
	}
	
	
	/**
	 * Restituisce la velocit� media calcolata dall inizio del leg
	 *  fino al punto corrente
	 * (relativamente solo a quel leg e non a tutto il viaggio)
	 * @return velocit� in nodi (Stringa troncata a 2 decimali)
	 */
	public String getRoutePointTotalAverageSpeedByLeg() {
		return getDoubleToStringRoundTwo(voyage.getRoutePointAverageSpeedByLeg(this));
	}
	
	/**
	 * Restituisce il totale dei consumi FO nelle 24 ore (basandosi sul punto corrente)
	 * @return Tons (Stringa troncata a 2 decimali)
	 */
	public String getFoConsumptionIn24HoursString() {
		return getDoubleToStringRoundTwo(voyage.getFoConsumptionIn24Hours(this));
	}

	/**
	 * Restituisce la distanza tra il punto precedente e questo (basandosi su lat,lon)
	 * @return distanza in miglia nautiche (Stringa troncata a 2 decimali)
	 */
	public String getDistance() {
//		return voyage.getRoutePointDistance(this);
		return getDoubleToStringRoundTwo(voyage.getRoutePointDistance(this));
	}
	public double getDistanceDouble() {
		return voyage.getRoutePointDistance(this);
	}
	/**
	 * Calcola la distanza (basandosi sui route points registrati) percorsa nelle 24 ore prendendo come riferimento la data di questo punto
	 * @return distanza in miglia nautiche (Stringa troncata a 2 decimali)
	 */
	public String getDistanceIn24Hours() {
//		return voyage.getRoutePointDistanceIn24Hours(this);
		return getDoubleToStringRoundTwo(voyage.getRoutePointDistanceIn24Hours(this, false));
	}
	/**
	 * Restituisce la distanza calcolata fino al punto corrente
	 * @return distanza in miglia nautiche (Stringa troncata a 2 decimali)
	 */
	public String getRoutePointTotalDistance() {
		return getDoubleToStringRoundTwo(voyage.getRoutePointTotalDistance(this));
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
	
	
	
	
	
	
	
	
	
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	
	@Override
	public boolean equals(Object obj) {
		RoutePoint rhs = (RoutePoint) obj;
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


	private String getDoubleToStringRoundFive(Double num) {
		if (num==null) return "";
		
		NumberFormat nf = NumberFormat.getInstance(Locale.ITALIAN);
		nf.setMaximumFractionDigits(10);		
		//String res = nf.format(num).replace(',', '.');
		String res = nf.format(num);
		
		int ind = res.indexOf(",");
		if(ind==-1)return res;
		
		if (ind+6 > res.length()) ind = res.length();
		else ind = ind +6;
		res = res.substring(0, ind);
		return res;
	}
	
	
	//Avevamo un bug: se abbiamo un valore come 5.925925925919983E-4 il dato esce sballato.
	private String getDoubleToStringRoundThree(Double num) {
		if (num==null) return "";
		
		NumberFormat nf = NumberFormat.getInstance(Locale.ITALIAN);
		nf.setMaximumFractionDigits(10);		
		//String res = nf.format(num).replace(',', '.');
		String res = nf.format(num);
		
		int ind = res.indexOf(",");
		if(ind==-1)return res;
		
		if (ind+4 > res.length()) ind = res.length();
		else ind = ind +4;
		res = res.substring(0, ind);
		return res;
	}
	
	//Avevamo un bug: se abbiamo un valore come 4472.337864365347 il dato esce 4.44 
	private String getDoubleToStringRoundTwo(Double num) {
		if (num==null) return "";
		if (num==0) return "0";
		
		//gli setto il formato 4.472,33
		NumberFormat nf = NumberFormat.getInstance(Locale.ITALIAN);
		nf.setMaximumFractionDigits(10);		
		//String res = nf.format(num).replace(',', '.');
		String res = nf.format(num);
		
		int ind = res.indexOf(",");
		if(ind==-1)return res;
		
		if (ind+3 > res.length()) ind = res.length();
		else ind = ind +3;
		res = res.substring(0, ind);
		return res;
	}
	
	
//	private String getDoubleToStringRoundThree(Double num) {
//		if (!num.isNaN()) return "";
//		Double res = (Double)arrotondaPerDifetto(num, 2);
//		return res.toString();
//	}
//	
//	public  double arrotondaPerDifetto (double value, int numCifreDecimali) {
//	      double temp = Math.pow(10, numCifreDecimali);
//	      return Math.floor(value * temp) / temp; 
//	}
	

	
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
	public String getRealRpmString() {
		return getDoubleToStringRoundTwo(getRealRpm());
	}
	public String getEmiRpmString() {
		return getDoubleToStringRoundTwo(getEmiRpm());
	}
	public String getCurrentSpeedString() {
		return getDoubleToStringRoundTwo(getCurrentSpeed());
	}
	public String getPressureString() {
		return (getPressure()!=null?getPressure().intValue():0)+"";
	}
	public String getWindDirectionString() {
		return getDoubleToStringRoundTwo(getWindDirection());
	}
	public String getWindSpeedString() {
		return getDoubleToStringRoundTwo(getWindSpeed());
	}
	public String getWindEffectOnShipString() {
		return getDoubleToStringRoundTwo(getWindEffectOnShip());
	}
	public String getWaveHeightString() {
		return getDoubleToStringRoundTwo(getWaveHeight());
	}
	public String getWaveDirectionString() {
		return getDoubleToStringRoundTwo(getWaveDirection());
	}
	public String getWavePeriodString() {
		return getDoubleToStringRoundTwo(getWavePeriod());
	}
	public String getWaveEffectOnShipString() {
		return getDoubleToStringRoundTwo(getWaveEffectOnShip());
	}
	public String getCurrentDirirectionString() {
		return getDoubleToStringRoundTwo(getCurrentDirirection());
	}
	public String getCurrentEffectOnShipString() {
		return getDoubleToStringRoundTwo(getCurrentEffectOnShip());
	}
	public String getOnlyCurrentEffectOnShipString() {
		return getDoubleToStringRoundTwo(getOnlyCurrentEffectOnShip());
	}
	
	
	public String getFoConsumptionRoundThreeString() {
		return getDoubleToStringRoundThree(getFoConsumption());
	}
	
	public String getFoConsumptionString() {
		return getDoubleToStringRoundTwo(getFoConsumption());
	}
//	public String getDoConsumptionString() {
//		return getDoubleToStringRoundTwo(getDoConsumption());
//	}
	public String getSpeedString() {
		return getDoubleToStringRoundTwo(getSpeed());
	}
	public String getTrueCourseString() {
		if (getTrueCourse()!=null && getTrueCourse().length()>0) {
			try {
				if (getTrueCourse().indexOf(".")>=0) {
				String res = getTrueCourse().substring(0, getTrueCourse().indexOf(".")+3);
				return res;
				}
				else return getTrueCourse();
			} catch (Exception e) {
				//e.printStackTrace();
			}
		}
		return getTrueCourse();
	}
	
	
	public int getWindBeaufort() {
		
		if (getWindSpeed()==null) return 0;
		else return ScaleConversionUtils.fromKnotsToBeaufort(getWindSpeed());
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


	public Double getPressure() {
		return pressure;
	}


	public void setPressure(Double pressure) {
		this.pressure = pressure;
	}


	public Double getWindSpeed() {
		return windSpeed;
	}


	public void setWindSpeed(Double windSpeed) {
		this.windSpeed = windSpeed;
	}


	public Double getWindDirection() {
		return windDirection;
	}


	public void setWindDirection(Double windDirection) {
		this.windDirection = windDirection;
	}


	public Double getWindEffectOnShip() {
		return windEffectOnShip;
	}


	public void setWindEffectOnShip(Double windEffectOnShip) {
		this.windEffectOnShip = windEffectOnShip;
	}


	public Double getWaveHeight() {
		return waveHeight;
	}


	public void setWaveHeight(Double waveHeight) {
		this.waveHeight = waveHeight;
	}


	public Double getWaveDirection() {
		return waveDirection;
	}


	public void setWaveDirection(Double waveDirection) {
		this.waveDirection = waveDirection;
	}


	public Double getWaveEffectOnShip() {
		return waveEffectOnShip;
	}


	public void setWaveEffectOnShip(Double waveEffectOnShip) {
		this.waveEffectOnShip = waveEffectOnShip;
	}


	public Double getCurrentSpeed() {
		return currentSpeed;
	}


	public void setCurrentSpeed(Double currentSpeed) {
		this.currentSpeed = currentSpeed;
	}


	public Double getCurrentDirirection() {
		return currentDirirection;
	}


	public void setCurrentDirirection(Double currentDirirection) {
		this.currentDirirection = currentDirirection;
	}


	public Double getCurrentEffectOnShip() {
		return currentEffectOnShip;
	}


	public void setCurrentEffectOnShip(Double currentEffectOnShip) {
		this.currentEffectOnShip = currentEffectOnShip;
	}


	public Double getRealRpm() {
		return realRpm;
	}


	public void setRealRpm(Double realRpm) {
		this.realRpm = realRpm;
	}


	public Double getEmiRpm() {
		return emiRpm;
	}


	public void setEmiRpm(Double emiRpm) {
		this.emiRpm = emiRpm;
	}


	public String getLatitude() {
		return latitude;
	}


	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}


	public String getLongitude() {
		return longitude;
	}


	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}


	public String getTrueCourse() {
		return trueCourse;
	}


	public void setTrueCourse(String trueCourse) {
		this.trueCourse = trueCourse;
	}


	public Double getSpeed() {
		return speed;
	}


	public void setSpeed(Double speed) {
		this.speed = speed;
	}


	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(RoutePoint comp) {
		 if (getRegistrationDate()!=null && comp.getRegistrationDate()!=null && getRegistrationDate().before(comp.getRegistrationDate())) return -1; 
		 else if (getRegistrationDate()!=null && comp.getRegistrationDate()!=null && getRegistrationDate().equals(comp.getRegistrationDate())) return 0;
		 else return 1;
	}


	public Double getFoConsumption() {
		return foConsumption;
	}


	public void setFoConsumption(Double foConsumption) {
		this.foConsumption = foConsumption;
	}


//	public Double getDoConsumption() {
//		return doConsumption;
//	}
//
//
//	public void setDoConsumption(Double doConsumption) {
//		this.doConsumption = doConsumption;
//	}

	/**
	 * Periodo in secondi
	 * @return periodo in secondi
	 */
	public Double getWavePeriod() {
		return wavePeriod;
	}

	/**
	 * Periodo in secondi
	 * @param wavePeriod in secondi
	 */
	public void setWavePeriod(Double wavePeriod) {
		this.wavePeriod = wavePeriod;
	}

	public Double getCorrectionFactor() {
		return correctionFactor;
	}

	public void setCorrectionFactor(Double correctionFactor) {
		this.correctionFactor = correctionFactor;
	}

	public Double getOnlyCurrentEffectOnShip() {
		return onlyCurrentEffectOnShip;
	}

	public void setOnlyCurrentEffectOnShip(Double onlyCurrentEffectOnShip) {
		this.onlyCurrentEffectOnShip = onlyCurrentEffectOnShip;
	}

	public Double getEnginePower() {
		return enginePower;
	}

	public void setEnginePower(Double enginePower) {
		this.enginePower = enginePower;
	}

	public Double getShaftPower() {
		return shaftPower;
	}

	public void setShaftPower(Double shaftPower) {
		this.shaftPower = shaftPower;
	}

	public Double getCo2Produced() {
		return co2Produced;
	}

	public void setCo2Produced(Double co2Produced) {
		this.co2Produced = co2Produced;
	}

	public Double getCo2Saved() {
		return co2Saved;
	}

	public void setCo2Saved(Double co2Saved) {
		this.co2Saved = co2Saved;
	}

	public Double getResidualEffectOnShip() {
		return residualEffectOnShip;
	}
	
	public String getResidualEffectOnShipString() {
		return getDoubleToStringRoundThree(residualEffectOnShip);
	}

	public void setResidualEffectOnShip(Double residualEffectOnShip) {
		this.residualEffectOnShip = residualEffectOnShip;
	}

	public Double getC() {
		return c;
	}

	public void setC(Double c) {
		this.c = c;
	}
	
	public String getCstring() {
		return getDoubleToStringRoundFive(getC());
	}

	public void setForShipTracking(boolean forTracking) {
		this.forShipTracking =forTracking;
	}

	public boolean isForShipTracking() {
		return forShipTracking;
	}

	public Long getShipId() {
		return shipId;
	}

	public void setShipId(Long shipId) {
		this.shipId = shipId;
	}

	public String getLegType() {
		return legType;
	}

	public void setLegType(String legType) {
		this.legType = legType;
	}

	public Long getMainEngineMinuteHfo() {
		return mainEngineMinuteHfo;
	}

	public void setMainEngineMinuteHfo(Long mainEngineMinuteHfo) {
		this.mainEngineMinuteHfo = mainEngineMinuteHfo;
	}

	public Long getMainEngineMinuteLfo() {
		return mainEngineMinuteLfo;
	}

	public void setMainEngineMinuteLfo(Long mainEngineMinuteLfo) {
		this.mainEngineMinuteLfo = mainEngineMinuteLfo;
	}

	public Long getMainEngineMinuteMdo() {
		return mainEngineMinuteMdo;
	}

	public void setMainEngineMinuteMdo(Long mainEngineMinuteMdo) {
		this.mainEngineMinuteMdo = mainEngineMinuteMdo;
	}

	public Long getDieselGenMinuteHfo() {
		return dieselGenMinuteHfo;
	}

	public void setDieselGenMinuteHfo(Long dieselGenMinuteHfo) {
		this.dieselGenMinuteHfo = dieselGenMinuteHfo;
	}

	public Long getDieselGenMinuteLfo() {
		return dieselGenMinuteLfo;
	}

	public void setDieselGenMinuteLfo(Long dieselGenMinuteLfo) {
		this.dieselGenMinuteLfo = dieselGenMinuteLfo;
	}

	public Long getDieselGenMinuteMdo() {
		return dieselGenMinuteMdo;
	}

	public void setDieselGenMinuteMdo(Long dieselGenMinuteMdo) {
		this.dieselGenMinuteMdo = dieselGenMinuteMdo;
	}

	public Long getAuxBoilerMinuteHfo() {
		return auxBoilerMinuteHfo;
	}

	public void setAuxBoilerMinuteHfo(Long auxBoilerMinuteHfo) {
		this.auxBoilerMinuteHfo = auxBoilerMinuteHfo;
	}

	public Long getAuxBoilerMinuteLfo() {
		return auxBoilerMinuteLfo;
	}

	public void setAuxBoilerMinuteLfo(Long auxBoilerMinuteLfo) {
		this.auxBoilerMinuteLfo = auxBoilerMinuteLfo;
	}

	public Long getAuxBoilerMinuteMdo() {
		return auxBoilerMinuteMdo;
	}

	public void setAuxBoilerMinuteMdo(Long auxBoilerMinuteMdo) {
		this.auxBoilerMinuteMdo = auxBoilerMinuteMdo;
	}	
	
//	public void setMainEngineMinuteHsfo(Long mainEngineMinuteHsfo) {
//		this.mainEngineMinuteHsfo = mainEngineMinuteHsfo;
//	}
//
//	public Long getMainEngineMinuteHsdo() {
//		return mainEngineMinuteHsdo;
//	}
//
//	public void setMainEngineMinuteHsdo(Long mainEngineMinuteHsdo) {
//		this.mainEngineMinuteHsdo = mainEngineMinuteHsdo;
//	}
//
//	public Long getMainEngineMinuteLsfo() {
//		return mainEngineMinuteLsfo;
//	}
//
//	public void setMainEngineMinuteLsfo(Long mainEngineMinuteLsfo) {
//		this.mainEngineMinuteLsfo = mainEngineMinuteLsfo;
//	}
//
//	public Long getMainEngineMinuteLsdo() {
//		return mainEngineMinuteLsdo;
//	}
//
//	public void setMainEngineMinuteLsdo(Long mainEngineMinuteLsdo) {
//		this.mainEngineMinuteLsdo = mainEngineMinuteLsdo;
//	}
//
//	public Long getDieselGenMinuteHsfo() {
//		return dieselGenMinuteHsfo;
//	}
//
//	public void setDieselGenMinuteHsfo(Long dieselGenMinuteHsfo) {
//		this.dieselGenMinuteHsfo = dieselGenMinuteHsfo;
//	}
//
//	public Long getDieselGenMinuteHsdo() {
//		return dieselGenMinuteHsdo;
//	}
//
//	public void setDieselGenMinuteHsdo(Long dieselGenMinuteHsdo) {
//		this.dieselGenMinuteHsdo = dieselGenMinuteHsdo;
//	}
//
//	public Long getDieselGenMinuteLsfo() {
//		return dieselGenMinuteLsfo;
//	}
//
//	public void setDieselGenMinuteLsfo(Long dieselGenMinuteLsfo) {
//		this.dieselGenMinuteLsfo = dieselGenMinuteLsfo;
//	}
//
//	public Long getDieselGenMinuteLsdo() {
//		return dieselGenMinuteLsdo;
//	}
//
//	public void setDieselGenMinuteLsdo(Long dieselGenMinuteLsdo) {
//		this.dieselGenMinuteLsdo = dieselGenMinuteLsdo;
//	}
//
//	public Long getAuxBoilerMinuteHsfo() {
//		return auxBoilerMinuteHsfo;
//	}
//
//	public void setAuxBoilerMinuteHsfo(Long auxBoilerMinuteHsfo) {
//		this.auxBoilerMinuteHsfo = auxBoilerMinuteHsfo;
//	}
//
//	public Long getAuxBoilerMinuteHsdo() {
//		return auxBoilerMinuteHsdo;
//	}
//
//	public void setAuxBoilerMinuteHsdo(Long auxBoilerMinuteHsdo) {
//		this.auxBoilerMinuteHsdo = auxBoilerMinuteHsdo;
//	}
//
//	public Long getAuxBoilerMinuteLsfo() {
//		return auxBoilerMinuteLsfo;
//	}
//
//	public void setAuxBoilerMinuteLsfo(Long auxBoilerMinuteLsfo) {
//		this.auxBoilerMinuteLsfo = auxBoilerMinuteLsfo;
//	}
//
//	public Long getAuxBoilerMinuteLsdo() {
//		return auxBoilerMinuteLsdo;
//	}
//
//	public void setAuxBoilerMinuteLsdo(Long auxBoilerMinuteLsdo) {
//		this.auxBoilerMinuteLsdo = auxBoilerMinuteLsdo;
//	}
//	public Long getMainEngineMinuteHsfo() {
//		return mainEngineMinuteHsfo;
//	}
	
	
	
}