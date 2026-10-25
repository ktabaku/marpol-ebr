package com.oceanpro.core.entity.oceanManager.voyage;


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.time.DateUtils;

import com.oceanpro.core.dto.MrvCargoData;
import com.oceanpro.core.dto.MrvObservedData;
import com.oceanpro.core.entity.oceanManager.office.Ship;
import com.oceanpro.core.utils.ScaleConversionUtils;
import com.oceanpro.utils.EasyDateUtils;

/**
 * 
 * Class name: VoyagePlan
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
@Table(name="ocean_voyage_voyageplan")
public class VoyagePlan {

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
	private boolean synchronizedWithRemote;
	
	
//	@Column(columnDefinition="INT(1)")
//	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Basic
	private boolean active; // Indica se il viaggio � attivo. Solo uno alla volta pu� essere attivo
	
	@Basic
	private String voyageNumber;
	
	/* Partenza */
	@Basic
	private Date departureDate;
	
	@Basic
	private String  departurePlace;
	
	@Basic
	private String arrivalPlace;
	
//	@Column(columnDefinition="INT(1)")
//	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Basic
	private boolean departurePlaceUe; // Indica se il porto e un porto UE o NON UE
	
//	@Column(columnDefinition="INT(1)")
//	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Basic
	private boolean arrivalPlaceUe; // Indica se il porto e un porto UE o NON UE
	
	
	

	
	
//	/* **************** */
//	
//	@Basic
//	private Double totalDistance; // in miglia nautiche
//	
//	/* Arrivo */
//	@Basic
//	private Date arrivalDate;

	
	/* **************** */
	
	@OneToMany(targetEntity=VoyagePath.class, cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="voyage", fetch=FetchType.LAZY)
	private List<VoyagePath> paths;
	
	@Column (length=5000) // 5000 caratteri
	private String remarks;
	
	@Basic
	private String  voyageType; // GC = Great Circle, RL = RumbLine 
	
	@OneToMany(targetEntity=WayPoint.class, cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="voyage", fetch=FetchType.LAZY)
	private List<WayPoint> wayPoints;
	
	@OneToMany(targetEntity=RoutePoint.class, cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="voyage", fetch=FetchType.EAGER)
	private List<RoutePoint> routePoints;
	
	@OneToMany(targetEntity=NoonPosition.class, cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="voyage", fetch=FetchType.LAZY)
	private List<NoonPosition> noonPositions;
	
//	@OneToMany(targetEntity=EmiWayPoint.class, cascade=CascadeType.ALL, mappedBy="voyage", fetch=FetchType.LAZY)
//	private List<EmiWayPoint> emiWayPoints;

	
	//AC per bunkering
	/*
	@OneToMany(targetEntity=Bunkering.class, cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="voyage", fetch=FetchType.LAZY)
	private List<Bunkering> bunkering;
	*/


	// Dati del contratto
	@OneToOne(fetch=FetchType.EAGER,cascade=CascadeType.ALL,orphanRemoval=true)
	private VoyageSettings voyageSettings;
	
	// Consumi Machinary Inserimento Manuale
	@OneToOne(fetch=FetchType.EAGER,cascade=CascadeType.ALL,orphanRemoval=true)
	private VoyageConsuptionManualEntry voyageConsuptionManualEntry;

	// Per Calcoli
	@Basic
	private Double testC;
	@Basic
	private Double testTotalPower;
	
	/**
	 * Costruttore vuoto
	 */
	public VoyagePlan() {
		super();
	}

	/**
	 * Corrisponde alla data della prima tratta
	 * @return
	 */
	public Date getVoyageCommenceDate() {
		VoyagePath vp = getFirstPath();
		if (vp!=null) {
				return vp.getDepartureDate();
		}
		return null;
	}

	public VoyagePath getLastPath(){
		List<VoyagePath> vps = getPaths();
		if (vps!=null) {
			Collections.sort(vps);
			if (vps.size()>0) {
				return vps.get(vps.size()-1);
			}
		}
		return null;
	}

	public double getTotalDistance(){
		double tot = 0.0;
		try {
			if (getPaths()!=null) {
				for (VoyagePath vp : getPaths()) {
					if (vp.getTotalDistance()!=null) {
						tot = tot + vp.getTotalDistance();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return tot;
	}
	
	
	
	
	public NoonPosition getLastNoonPositionForPath(VoyagePath vp){
		NoonPosition last = null;
		List<NoonPosition> vps = getNoonPositions();
		if (vps!=null) {
			Collections.sort(vps);
			for (NoonPosition np : vps) {
				if (np.getRegistrationDate().after(vp.getDepartureDate()) && np.getRegistrationDate().before(vp.getArrivalDate()))
					last = np;
			}
		}
		System.out.println("Last nP:" + last);
		return last;
	}
	
	public NoonPosition getLastNoonPosition(){
		List<NoonPosition> vps = getNoonPositions();
		if (vps!=null) {
			Collections.sort(vps);
			return vps.get(vps.size()-1);
		}
		return null;
	}
	
	public VoyagePath getFirstPath(){
		List<VoyagePath> vps = getPaths();
		if (vps!=null) {
			Collections.sort(vps);
			if (vps.size()>0) {
				return vps.get(0);
			}
		}
		return null;
	}
	
	
	
	public VoyagePath getActivePath() {
		return getActivePathForDate(EasyDateUtils.getGMT0CurrentDate());
	}
	public VoyagePath getActivePathForDate(Date date) {
		try {
			
			//System.out.println("AC voyage id:  " + this.getId());
			for (VoyagePath vp : getPaths()) {
				if (vp.getDepartureDate().before(date) && vp.getArrivalDate().after(date)) {
					return vp;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/****************************** CO2 **************************/
	public String getCo2TodayInKg() {
		return getDoubleToStringRoundTwo(getCo2Today()/1000);
	}
	/**
	 * In grammi
	 * @return
	 */
	public double getCo2Today() {
		
		if (getRoutePoints()!=null && getRoutePoints().size()>0) {
			List<RoutePoint> rpo = getRoutePoints();
			Collections.sort(rpo);
			List<RoutePoint> today = new ArrayList<RoutePoint>(); // Questo sar� ordinato per data crescente
			for (RoutePoint r : rpo) {
				if (DateUtils.isSameDay(r.getRegistrationDate(), EasyDateUtils.getGMT0CurrentDate())) {
					today.add(r);
				}
			}
			
			if (today.size()>0) { // TODO qui c'� da considerare il noon e non le 24 ore
				// Calcolo somma
				double cf = 0.0;
				for (RoutePoint rr : today) {
					cf+= (rr.getCo2Produced()!=null?rr.getCo2Produced():0.0);
				}
				return cf;
			}
			return 0.0;
		}
		return 0.0;
	}
	
	
	public String getCo2SavedTodayInKg() {
		return getDoubleToStringRoundTwo(getCo2SavedToday()/1000);
	}
	/**
	 * in grammi
	 * @return
	 */
	public double getCo2SavedToday() {
		
		if (getRoutePoints()!=null && getRoutePoints().size()>0) {
			List<RoutePoint> rpo = getRoutePoints();
			Collections.sort(rpo);
			List<RoutePoint> today = new ArrayList<RoutePoint>(); // Questo sar� ordinato per data crescente
			for (RoutePoint r : rpo) {
				if (DateUtils.isSameDay(r.getRegistrationDate(), EasyDateUtils.getGMT0CurrentDate())) {
					today.add(r);
				}
			}
			
			if (today.size()>0) { // TODO qui c'� da considerare il noon e non le 24 ore
				// Calcolo somma
				double cf = 0.0;
				for (RoutePoint rr : today) {
					cf+= (rr.getCo2Saved()!=null?rr.getCo2Saved():0.0);
				}
				return cf;
			}
			return 0.0;
		}
		return 0.0;
	}
	
	public String getCo2ForThisVoyageInKg() {
		return getDoubleToStringRoundTwo(getCo2ForThisVoyage()/1000);
	}
	/**
	 * in grammi
	 * @return
	 */
	public double getCo2ForThisVoyage() {
		if (getRoutePoints().size()>0) {
			// Calcolo somma
			double cf = 0.0;
			for (RoutePoint rr : getRoutePoints()) {
				cf+= (rr.getCo2Produced()!=null?rr.getCo2Produced():0.0);
			}
			return cf;
		}
		return 0.0;
	}
	
	public String getCo2SavedForThisVoyageInKg() {
		return getDoubleToStringRoundTwo(getCo2SavedForThisVoyage()/1000);
	}
	/**
	 * in grammi
	 * @return
	 */
	public double getCo2SavedForThisVoyage() {
		if (getRoutePoints().size()>0) {
			// Calcolo somma
			double cf = 0.0;
			for (RoutePoint rr : getRoutePoints()) {
				cf+= (rr.getCo2Saved()!=null?rr.getCo2Saved():0.0);
			}
			return cf;
		}
		return 0.0;
	}
	/*************************************************************/
	

	
	/**
	 * Calcola i running hours giornalieri o totali (basandosi sui route points registrati) prendendo come riferimento  quello passatogli  (compreso)
	 * @param rp punto
	 * @param daily se true calcola il giornaliero altrimenti il totale
	 * @return Total r.h nel giorno o totale
	 */
	protected String getRoutePointRunningHours(RoutePoint rp, boolean daily) {
		if (getRoutePoints()!=null && getRoutePoints().size()>0) {
			List<RoutePoint> rpo = getRoutePoints();
			Collections.sort(rpo);
			List<RoutePoint> in24H = new ArrayList<RoutePoint>(); // Questo sar� ordinato per data crescente
			for (RoutePoint r : rpo) {
				if (daily) {
					if (DateUtils.isSameDay(r.getRegistrationDate(), rp.getRegistrationDate())) {
						in24H.add(r);
						if (r.getUuid().equals(rp.getUuid())) break;
					}
				}
				else {
					in24H.add(r);
					if (r.getUuid().equals(rp.getUuid())) break;
				}
			}
			
			if (in24H.size()>0) {
				RoutePoint prev = null;
				long millis = 0;
				for (RoutePoint rr : in24H) {
					if (prev!=null) millis+= rr.getRegistrationDate().getTime() - prev.getRegistrationDate().getTime();
					prev = rr;
				}

				long diffHours = millis / (60 * 60 * 1000);
		        long diffMinutes = (millis-(diffHours*(60 * 60 * 1000)))  / (60 * 1000) % 60;   
		        
				return diffHours + ":" + diffMinutes;
			}
			return "00:00";
		}
		return "00:00";
	}

	
	//TODO
	/**
	 * Calcola i running hours dal current noon al precedente(basandosi sui route points registrati)
	 * @param type il tipo che puo essere h = hour oppure min = minutes
	 * @param start 
	 * @param stop 
	 */
	public long getRunningHoursDateToDate(Date start, Date stop, String type) {
		long res = 0;
		if (start!=null && stop!=null) {
			long millis = (start.getTime() - stop.getTime());
			
			//long diffSeconds = millis / 1000 % 60;  
			//long diffMinutes = millis / (60 * 1000) % 60;        
	        //long diffHours = millis / (60 * 60 * 1000);
	        //long diffDays = millis / (24 * 60 * 60 * 1000);
	        long diffHours = millis / (60 * 60 * 1000);
	        long diffMinutes = (millis-(diffHours*(60 * 60 * 1000)))  / (60 * 1000) % 60;   
	        
	        if (type=="h")
	        	res = diffHours;
	        else
	        	res = diffMinutes;
		}
		return res;
	}
	
	
	
	
	public String getTotalRunningHours() {
		String res = "00:00";
		if (getFirstPath()!=null && getFirstPath().getDepartureDate()!=null) {
			long millis = (new Date()).getTime() - getFirstPath().getDepartureDate().getTime();
			
			//long diffSeconds = millis / 1000 % 60;  
			//long diffMinutes = millis / (60 * 1000) % 60;        
	        //long diffHours = millis / (60 * 60 * 1000);
	        //long diffDays = millis / (24 * 60 * 60 * 1000);
	        long diffHours = millis / (60 * 60 * 1000);
	        long diffMinutes = (millis-(diffHours*(60 * 60 * 1000)))  / (60 * 1000) % 60;   
	        
//	        res = diffDays + "d " + diffHours + "h " + diffMinutes + "m " + diffSeconds + "s";
	        res = diffHours + ":" + diffMinutes;
		}
		return res;
	}
	/**
	 * Calcola le ore di navigazione (basandosi sui route points registrati) nelle 24 ore prendendo come riferimento la data del punto passatogli
	 * @param rp punto
	 * @return ore di navigazione nelle 24 ore 
	 */
	protected String getRoutePointTimeIn24Hours(RoutePoint rp) {
		if (getRoutePoints()!=null && getRoutePoints().size()>0) {
			List<RoutePoint> rpo = getRoutePoints();
			Collections.sort(rpo);
			List<RoutePoint> in24H = new ArrayList<RoutePoint>(); // Questo sar� ordinato per data crescente
			for (RoutePoint r : rpo) {
				if (DateUtils.isSameDay(r.getRegistrationDate(), rp.getRegistrationDate())) {
					in24H.add(r);
				}
			}
			
			if (in24H.size()>0) { // TODO qui c'� da considerare il noon e non le 24 ore
				RoutePoint prev = null;
				long millis = 0;
				for (RoutePoint rr : in24H) {
					if (prev!=null) millis+= rr.getRegistrationDate().getTime() - prev.getRegistrationDate().getTime();
					prev = rr;
				}

				long diffHours = millis / (60 * 60 * 1000);
		        long diffMinutes = (millis-(diffHours*(60 * 60 * 1000)))  / (60 * 1000) % 60;   
		        
				return diffHours + ":" + diffMinutes;
			}
			return "00:00";
		}
		return "00:00";
	}
	
	/**
	 * Calcola il total running hours sommando le ore inserite a ogni registrazione della noon position e restituisce il risultato in minuti
	 * 
	 * @param lastNoonPosition effettua il contenggio fino all'id lastNoonPosition ESCLUSO
	 * @return
	 */
	public long getNoonPositionTotalRunningMinutes(Long lastNoonPosition) {
		long minutes = 0;
		List<NoonPosition> np = getNoonPositions();
		if (np!=null) {
			Collections.sort(np, new NoonPositionSortByRegistrationDate());
			for (NoonPosition noon : getNoonPositions()) {
				if (lastNoonPosition!=null && lastNoonPosition==noon.getId()) break;
				
				if (noon.getDayHours()!=null) minutes += noon.getDayHours()*60;
				if (noon.getDayMinutes()!=null) minutes += noon.getDayMinutes();
			}
		}
		return minutes;
	}
	
	/**
	 * Calcola il total running hours per il tipo passatogli sommando le ore inserite a ogni registrazione della noon position e restituisce il risultato in minuti
	 * 
	 * @param lastNoonPosition effettua il contenggio fino all'id lastNoonPosition ESCLUSO
	 * @param type 1=main engine 2=diesel generator (aggiungere numero generatore) 3=igs
	 * @param dieselGeneratorNumber numero del generatore diesel, valido solo se type = 2
	 * @return
	 */
	public long getNoonPositionTotalRunningMinutesForType(Long lastNoonPosition, int type, byte dieselGeneratorNumber) {
		long minutes = 0;
		List<NoonPosition> np = getNoonPositions();
		if (np!=null) {
			Collections.sort(np, new NoonPositionSortByRegistrationDate());
			for (NoonPosition noon : getNoonPositions()) {
				if (lastNoonPosition!=null && lastNoonPosition==noon.getId()) break;
				
				if (type==1) { // main engine
					if (noon.getMainEngineHours()!=null) minutes += noon.getMainEngineHours()*60;
					if (noon.getMainEngineMinutes()!=null) minutes += noon.getMainEngineMinutes();
				}
				
				if (type==2) { // Diesel Generator
					for (NoonPositionDieselGenerator dg : noon.getDieselGenerators()) {
						if (dg.getNumber().byteValue()==dieselGeneratorNumber) {
							if (dg.getDieselGeneratorHours()!=null) minutes += dg.getDieselGeneratorHours()*60;
							if (dg.getDieselGeneratorMinutes()!=null) minutes += dg.getDieselGeneratorMinutes();
							break;
						}
					}
				}
				
				if (type==3) { // igs
					if (noon.getIgsHours()!=null) minutes += noon.getIgsHours()*60;
					if (noon.getIgsMinutes()!=null) minutes += noon.getIgsMinutes();
				}
				if (type==4) { // boiler
					if (noon.getBoilerHours()!=null) minutes += noon.getBoilerHours()*60;
					if (noon.getBoilerMinutes()!=null) minutes += noon.getBoilerMinutes();
				}
				if (type==5) { // Heating
					if (noon.getHeatingHours()!=null) minutes += noon.getHeatingHours()*60;
					if (noon.getHeatingMinutes()!=null) minutes += noon.getHeatingMinutes();
				}
				if (type==6) { // TankCleaning
					if (noon.getTankCleaningHours()!=null) minutes += noon.getTankCleaningHours()*60;
					if (noon.getTankCleaningMinutes()!=null) minutes += noon.getTankCleaningMinutes();
				}
				if (type==7) { // OtherMachinery
					if (noon.getOtherMachineryHours()!=null) minutes += noon.getOtherMachineryHours()*60;
					if (noon.getOtherMachineryMinutes()!=null) minutes += noon.getOtherMachineryMinutes();
				}
			}
		}
		return minutes;
	}
	
	/**
	 * Calcola la distanza totale percorsa sommando la distanza inserita a ogni registrazione della noon position e restituisce il risultato in miglia nautiche
	 * 
	 * @param lastNoonPosition effettua il contenggio fino all'id lastNoonPosition ESCLUSO
	 * @return
	 */
	public double getNoonPositionTotalDistance(Long lastNoonPosition) {
		double distance = 0;
		List<NoonPosition> np = getNoonPositions();
		if (np!=null) {
			Collections.sort(np, new NoonPositionSortByRegistrationDate());
			for (NoonPosition noon : np) {
				if (lastNoonPosition!=null && lastNoonPosition==noon.getId()) break;
				if (noon.getDistanceMg()!=null) distance += noon.getDistanceMg();
			}
		}
		return distance;
	}
	
	/**
	 * Calcola il consumo totale di carburante di un certo tipo sommando il consumo inserito a ogni registrazione della noon position e restituisce il risultato in tonnellate
	 * 
	 * @param Type: 
	 * 	0= HSFO
	 * 	1= LSFO
	 * 	2= HSDO
	 * 	3= LSDO
	 *  4= LO
	 *  5= FW
	 * 
	 * @param lastNoonPosition effettua il contenggio fino all'id lastNoonPosition ESCLUSO
	 * @return
	 */
	public double getNoonPositionTotalFuelConsumption(int type, int fuelType, Long lastNoonPosition) {
		double cons = 0;
		
		List<NoonPosition> np = getNoonPositions();
		if (np!=null) {
			Collections.sort(np, new NoonPositionSortByRegistrationDate());
			for (NoonPosition noon : np) {
				if (lastNoonPosition!=null && lastNoonPosition.equals(noon.getId())) break;
				
				
				if (type==0) {
					if (fuelType == 0 && noon.getMainEngineHsfo()!=null) cons += noon.getMainEngineHsfo();
					if (fuelType == 1 && noon.getMainEngineLsfo()!=null) cons += noon.getMainEngineLsfo();
					if (fuelType == 2 && noon.getMainEngineHsdo()!=null) cons += noon.getMainEngineHsdo();
					if (fuelType == 3 && noon.getMainEngineLsdo()!=null) cons += noon.getMainEngineLsdo();
					if (fuelType == 4 && noon.getMainEngineLo()!=null) cons += noon.getMainEngineLo();
					if (fuelType == 5 && noon.getMainEngineFw()!=null) cons += noon.getMainEngineFw();
				}
				
				if (type==2 && noon.getDieselGenerators()!=null) {
					// Totale di tutti i generatori
					for (NoonPositionDieselGenerator dg : noon.getDieselGenerators()) {
						if (fuelType == 0 && dg.getDieselGeneratorHsfo()!=null) cons += dg.getDieselGeneratorHsfo();
						if (fuelType == 1 && dg.getDieselGeneratorLsfo()!=null) cons += dg.getDieselGeneratorLsfo();
						if (fuelType == 2 && dg.getDieselGeneratorHsdo()!=null) cons += dg.getDieselGeneratorHsdo();
						if (fuelType == 3 && dg.getDieselGeneratorLsdo()!=null) cons += dg.getDieselGeneratorLsdo();
					}
				}
				
				if (type==3) {
					if (fuelType == 0 && noon.getIgsHsfo()!=null) cons += noon.getIgsHsfo();
					if (fuelType == 1 && noon.getIgsLsfo()!=null) cons += noon.getIgsLsfo();
					if (fuelType == 2 && noon.getIgsHsdo()!=null) cons += noon.getIgsHsdo();
					if (fuelType == 3 && noon.getIgsLsdo()!=null) cons += noon.getIgsLsdo();
				}
				if (type==4) {
					if (fuelType == 0 && noon.getBoilerHsfo()!=null) cons += noon.getBoilerHsfo();
					if (fuelType == 1 && noon.getBoilerLsfo()!=null) cons += noon.getBoilerLsfo();
					if (fuelType == 2 && noon.getBoilerHsdo()!=null) cons += noon.getBoilerHsdo();
					if (fuelType == 3 && noon.getBoilerLsdo()!=null) cons += noon.getBoilerLsdo();
				}
				if (type==5) {
					if (fuelType == 0 && noon.getHeatingHsfo()!=null) cons += noon.getHeatingHsfo();
					if (fuelType == 1 && noon.getHeatingLsfo()!=null) cons += noon.getHeatingLsfo();
					if (fuelType == 2 && noon.getHeatingHsdo()!=null) cons += noon.getHeatingHsdo();
					if (fuelType == 3 && noon.getHeatingLsdo()!=null) cons += noon.getHeatingLsdo();
				}
				if (type==6) {
					if (fuelType == 0 && noon.getTankCleaningHsfo()!=null) cons += noon.getTankCleaningHsfo();
					if (fuelType == 1 && noon.getTankCleaningLsfo()!=null) cons += noon.getTankCleaningLsfo();
					if (fuelType == 2 && noon.getTankCleaningHsdo()!=null) cons += noon.getTankCleaningHsdo();
					if (fuelType == 3 && noon.getTankCleaningLsdo()!=null) cons += noon.getTankCleaningLsdo();
				}
				if (type==7) {
					if (fuelType == 0 && noon.getOtherMachineryHsfo()!=null) cons += noon.getOtherMachineryHsfo();
					if (fuelType == 1 && noon.getOtherMachineryLsfo()!=null) cons += noon.getOtherMachineryLsfo();
					if (fuelType == 2 && noon.getOtherMachineryHsdo()!=null) cons += noon.getOtherMachineryHsdo();
					if (fuelType == 3 && noon.getOtherMachineryLsdo()!=null) cons += noon.getOtherMachineryLsdo();
				}
				
				
				
				
				if (type==-1) { // Totale per tutti i dispositivi
					if (fuelType == 0) {
						cons += noon.getMainEngineHsfo()!=null?noon.getMainEngineHsfo():0.0;
						cons += noon.getIgsHsfo()!=null?noon.getIgsHsfo():0.0;
						cons += noon.getTotalDailyDieselGeneratorHsfo();
						cons += noon.getBoilerHsfo()!=null?noon.getBoilerHsfo():0.0;
						cons += noon.getHeatingHsfo()!=null?noon.getHeatingHsfo():0.0;
						cons += noon.getTankCleaningHsfo()!=null?noon.getTankCleaningHsfo():0.0;
						cons += noon.getOtherMachineryHsfo()!=null?noon.getOtherMachineryHsfo():0.0;
					}
					if (fuelType == 1) {
						cons += noon.getMainEngineLsfo()!=null?noon.getMainEngineLsfo():0.0;
						cons += noon.getIgsLsfo()!=null?noon.getIgsLsfo():0.0;
						cons += noon.getTotalDailyDieselGeneratorLsfo();
						cons += noon.getBoilerLsfo()!=null?noon.getBoilerLsfo():0.0;
						cons += noon.getHeatingLsfo()!=null?noon.getHeatingLsfo():0.0;
						cons += noon.getTankCleaningLsfo()!=null?noon.getTankCleaningLsfo():0.0;
						cons += noon.getOtherMachineryLsfo()!=null?noon.getOtherMachineryLsfo():0.0;
					}
					if (fuelType == 2) {
						cons += noon.getMainEngineHsdo()!=null?noon.getMainEngineHsdo():0.0;
						cons += noon.getIgsHsdo()!=null?noon.getIgsHsdo():0.0;
						cons += noon.getTotalDailyDieselGeneratorHsdo();
						cons += noon.getBoilerHsdo()!=null?noon.getBoilerHsdo():0.0;
						cons += noon.getHeatingHsdo()!=null?noon.getHeatingHsdo():0.0;
						cons += noon.getTankCleaningHsdo()!=null?noon.getTankCleaningHsdo():0.0;
						cons += noon.getOtherMachineryHsdo()!=null?noon.getOtherMachineryHsdo():0.0;
					}
					if (fuelType == 3) {
						cons += noon.getMainEngineLsdo()!=null?noon.getMainEngineLsdo():0.0;
						cons += noon.getIgsLsdo()!=null?noon.getIgsLsdo():0.0;
						cons += noon.getTotalDailyDieselGeneratorLsdo();
						cons += noon.getBoilerLsdo()!=null?noon.getBoilerLsdo():0.0;
						cons += noon.getHeatingLsdo()!=null?noon.getHeatingLsdo():0.0;
						cons += noon.getTankCleaningLsdo()!=null?noon.getTankCleaningLsdo():0.0;
						cons += noon.getOtherMachineryLsdo()!=null?noon.getOtherMachineryLsdo():0.0;
					}
				}
				
			}
		}
		return cons;
	}
	
	/**
	 * Calcola il consumo totale di carburante di un certo tipo sommando il consumo inserito a ogni registrazione della noon position e restituisce il risultato in tonnellate
	 * 
	 * @param Type: 
	 * 	0= HSFO
	 * 	1= LSFO
	 * 	2= HSDO
	 * 	3= LSDO
	 *  4= LO
	 *  5= FW
	 * 
	 * @param lastNoonPosition effettua il contenggio fino all'id lastNoonPosition ESCLUSO
	 * @return
	 */
	public double getNoonPositionTotalFuelConsumptionByDieselGenerator(int fuelType, Long lastNoonPosition, byte dgNumber) {
		double cons = 0;
		
		List<NoonPosition> np = getNoonPositions();
		if (np!=null) {
			Collections.sort(np, new NoonPositionSortByRegistrationDate());
			for (NoonPosition noon : np) {
				if (lastNoonPosition!=null && lastNoonPosition==noon.getId()) break;

				if (noon.getDieselGenerators()!=null) {
					// Totale di tutti i generatori
					for (NoonPositionDieselGenerator dg : noon.getDieselGenerators()) {
						if (dgNumber==dg.getNumber() && fuelType == 0 && dg.getDieselGeneratorHsfo()!=null) cons += dg.getDieselGeneratorHsfo();
						if (dgNumber==dg.getNumber() && fuelType == 1 && dg.getDieselGeneratorLsfo()!=null) cons += dg.getDieselGeneratorLsfo();
						if (dgNumber==dg.getNumber() && fuelType == 2 && dg.getDieselGeneratorHsdo()!=null) cons += dg.getDieselGeneratorHsdo();
						if (dgNumber==dg.getNumber() && fuelType == 3 && dg.getDieselGeneratorLsdo()!=null) cons += dg.getDieselGeneratorLsdo();
					}
				}

			}
		}
		return cons;
	}
	
	
	/**
	 * Calcola il totale del bunkering di carburante di un certo tipo sommando gli inserimenti di ogni registrazione della noon position
	 * 
	 * @param Type: 
	 * 	0= HSFO
	 * 	1= LSFO
	 * 	2= HSDO
	 * 	3= LSDO
	 * 	4= LO
	 *  5= FW
	 * 
	 * @param lastNoonPosition effettua il contenggio fino all'id lastNoonPosition ESCLUSO
	 * @return
	 */
	public double getNoonPositionTotalBunkering(int fuelType, Long lastNoonPosition) {
		double cons = 0;
		
		List<NoonPosition> np = getNoonPositions();
		if (np!=null) {
			Collections.sort(np, new NoonPositionSortByRegistrationDate());
			for (NoonPosition noon : np) {
				if (lastNoonPosition!=null && lastNoonPosition==noon.getId()) break;
				
				if (fuelType == 0 && noon.getTotalBunkeringHsfo()!=null) cons += noon.getTotalBunkeringHsfo();
				if (fuelType == 1 && noon.getTotalBunkeringLsfo()!=null) cons += noon.getTotalBunkeringLsfo();
				if (fuelType == 2 && noon.getTotalBunkeringHsdo()!=null) cons += noon.getTotalBunkeringHsdo();
				if (fuelType == 3 && noon.getTotalBunkeringLsdo()!=null) cons += noon.getTotalBunkeringLsdo();
				if (fuelType == 4 && noon.getTotalBunkeringLo()!=null) cons += noon.getTotalBunkeringLo();
				if (fuelType == 5 && noon.getTotalBunkeringFw()!=null) cons += noon.getTotalBunkeringFw();
			}
		}
		return cons;
	}
	
	public double getNoonPositionMainEngineTotalDistance(Long lastNoonPosition) {
		double cons = 0;
		
		List<NoonPosition> np = getNoonPositions();
		if (np!=null) {
			Collections.sort(np, new NoonPositionSortByRegistrationDate());
			for (NoonPosition noon : np) {
				if (lastNoonPosition!=null && lastNoonPosition==noon.getId()) break;
					if (noon.getMainEngineEngineDistance()!=null) cons += noon.getMainEngineEngineDistance();
			}
		}
		return cons;
	}
	
	public boolean isRumbLine() {
		if (voyageType!=null && voyageType.equals("RL")) return true;
		else return false;
	}
	public boolean isGreatCircle() {
		if (voyageType!=null && voyageType.equals("GC")) return true;
		else return false;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getVoyageTotalPathDistance() {
		double tot =0.0; 
		if (getPaths()!=null) {
			for (VoyagePath rp : getPaths()) {
				tot = tot + (rp.getTotalDistance()!=null?rp.getTotalDistance():0.0); 
			}
		}
		return tot;
	}
	
	public String getTotalTime() {
		String res = "";
		
//		long millis =0; 
//		if (wayPoints!=null) {
//			for (WayPoint wp : wayPoints) {
//				if (wp.getDistance()!=null && wp.getSpeed()!=null && wp.getSpeed()>0) millis += (long)((wp.getDistance()/wp.getSpeed()) * (60 * 60 * 1000));
//			}
//		}
////		long diffSeconds = millis / 1000 % 60;  
//		long diffMinutes = millis / (60 * 1000) % 60;        
//        long diffHours = millis / (60 * 60 * 1000);
//        long diffDays = millis / (24 * 60 * 60 * 1000);
////        res = diffDays + "d " + diffHours + "h " + diffMinutes + "m " + diffSeconds + "s";
//        res = diffDays + "d " + diffHours + "h " + diffMinutes + "m ";
//		return res;
		
		long totTime=0;
		for (VoyagePath path : getPaths()) {
			if (path.getArrivalDate()!=null && path.getDepartureDate()!=null && path.getArrivalDate().after(path.getDepartureDate())) {
				totTime += path.getArrivalDate().getTime() - path.getDepartureDate().getTime();
			}
		}
		
		
		if(totTime>0)  {
			long diffMinutes = totTime / (60 * 1000) % 60;        
			long diffDays = totTime / (24 * 60 * 60 * 1000);
			long diffHours = (totTime / (60 * 60 * 1000))-(diffDays*24);
	        
//	        res = diffDays + "d " + diffHours + "h " + diffMinutes + "m " + diffSeconds + "s";
	        res = diffDays + "d " + diffHours + "h " + diffMinutes + "m ";
		}
		else {
			res = "n.a.";
		}
	     
		return res;
	}
	
	public double getTotalConsuptionDo() {
		double tot =0; 
		if (wayPoints!=null) {
			for (WayPoint wp : wayPoints) {
				if (wp.getEmiComsumptionDo()!=null) tot = tot + wp.getEmiComsumptionDo(); 
			}
		}
		return tot;
	}
	
	public double getTotalConsuptionIfo() {
		double tot =0; 
		if (wayPoints!=null) {
			for (WayPoint wp : wayPoints) {
				if (wp.getEmiComsumptionFo()!=null) tot = tot + wp.getEmiComsumptionFo(); 
			}
		}
		return tot;
	}
	
	public double getTotalDistanceInGoodWeather() {
		double tot =0; 
//		if (wayPoints!=null) {
//			for (WayPoint wp : wayPoints) {
//				if (wp.getEmiComsumptionFo()!=null) tot = tot + wp.getEmiComsumptionFo(); 
//			}
//		}
		return tot;
	}
	
	
	
	public double getAverageSpeedInGoodWeather() {
		double tot =0; 
//		if (wayPoints!=null) {
//			for (WayPoint wp : wayPoints) {
//				if (wp.getEmiComsumptionFo()!=null) tot = tot + wp.getEmiComsumptionFo(); 
//			}
//		}
		return tot;
	}
	public double getGoodWeatherConsuption() {
		double tot =0; 
//		if (wayPoints!=null) {
//			for (WayPoint wp : wayPoints) {
//				if (wp.getEmiComsumptionFo()!=null) tot = tot + wp.getEmiComsumptionFo(); 
//			}
//		}
		return tot;
	}
	public double getBadWeatherConsuption() {
		double tot =0; 
//		if (wayPoints!=null) {
//			for (WayPoint wp : wayPoints) {
//				if (wp.getEmiComsumptionFo()!=null) tot = tot + wp.getEmiComsumptionFo(); 
//			}
//		}
		return tot;
	}
	public double getAverageSpeedInBadWeather() {
		double tot =0; 
//		if (wayPoints!=null) {
//			for (WayPoint wp : wayPoints) {
//				if (wp.getEmiComsumptionFo()!=null) tot = tot + wp.getEmiComsumptionFo(); 
//			}
//		}
		return tot;
	}
	public double getTotalDistanceInBadWeather() {
		double tot =0; 
//		if (wayPoints!=null) {
//			for (WayPoint wp : wayPoints) {
//				if (wp.getEmiComsumptionFo()!=null) tot = tot + wp.getEmiComsumptionFo(); 
//			}
//		}
		return tot;
	}
	public double getTotalTimeInGoodWeather() {
		double tot =0; 
//		if (wayPoints!=null) {
//			for (WayPoint wp : wayPoints) {
//				if (wp.getEmiComsumptionFo()!=null) tot = tot + wp.getEmiComsumptionFo(); 
//			}
//		}
		return tot;
	}
	public double getTotalTimeInBadWeather() {
		double tot =0; 
//		if (wayPoints!=null) {
//			for (WayPoint wp : wayPoints) {
//				if (wp.getEmiComsumptionFo()!=null) tot = tot + wp.getEmiComsumptionFo(); 
//			}
//		}
		return tot;
	}
	public double getCurrentFactor() {
		double tot =0; 
//		if (wayPoints!=null) {
//			for (WayPoint wp : wayPoints) {
//				if (wp.getEmiComsumptionFo()!=null) tot = tot + wp.getEmiComsumptionFo(); 
//			}
//		}
		return tot;
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
	
	
	
	public String getAverageSpeedRoutePointString() {
		return getDoubleToStringRoundTwo(getAverageSpeedRoutePoint());
	}
	
	
	public double getAverageSpeedRoutePoint() {
		if (getRoutePoints()!=null && getRoutePoints().size()>0) {
			List<RoutePoint> rpo = getRoutePoints();
			double totSpeed = 0.0;
			for (RoutePoint pr : rpo) {
				totSpeed+=pr.getSpeed();
			}
			return totSpeed/(new Double(rpo.size()));
		}
		return 0.0;
	}
	
	public double getAverageSpeedRoutePointInGoodWheatherDays(RoutePoint rp) {
		List<RoutePoint> rps = getRoutePointsInGoodWheatherDays(rp);
		if (rps!=null && rps.size()>0) {
			double totSpeed = 0.0;
			for (RoutePoint pr : rps) {
				totSpeed+=pr.getSpeed();
			}
			return totSpeed/(new Double(rps.size()));
		}
		return 0.0;
	}
	public double getAverageSpeedRoutePointInGoodWheather() {
		List<RoutePoint> rps = getRoutePointsInGoodWheather();
		if (rps!=null && rps.size()>0) {
			double totSpeed = 0.0;
			for (RoutePoint pr : rps) {
				totSpeed+=pr.getSpeed();
			}
			return totSpeed/(new Double(rps.size()));
		}
		return 0.0;
	}
	
	public double getAverageSpeedRoutePointInGoodWheatherByLeg() {
		System.out.println("In getAverageSpeedRoutePointInGoodWheatherByLeg.");
		List<RoutePoint> rps = getRoutePointsInGoodWheatherByLeg();		
		System.out.println("In getAverageSpeedRoutePointInGoodWheatherByLeg null: "+ rps!=null);
		System.out.println("In getAverageSpeedRoutePointInGoodWheatherByLeg size: "+ rps.size());
		if (rps!=null && rps.size()>0) {
			double totSpeed = 0.0;
			for (RoutePoint pr : rps) {
				totSpeed+=pr.getSpeed();
			}
			return totSpeed/(new Double(rps.size()));
		}
		return 0.0;
	}
	
	public double getAverageSpeedRoutePointInGoodWheatherWithCurrentCorrection(boolean leg) {
		List<RoutePoint> rps = null;
		if(!leg){ rps = getRoutePointsInGoodWheather();}
		else{ rps = getRoutePointsInGoodWheatherByLeg();}
		
		if (rps!=null && rps.size()>0) {
			double totSpeed = 0.0;
			for (RoutePoint pr : rps) {
				
				double shipSpeed = 0.0;
				try {
					boolean positiva=false;
					// Calcolo se corrente � o meno favorevole
					// positiva se dir cur compresa tra direzione nave +- 45 gradi
					double startPosDir = Double.parseDouble(pr.getTrueCourse())-45.0;
					if (startPosDir<0.0) startPosDir = 360.0+startPosDir;
					double endPosDir = Double.parseDouble(pr.getTrueCourse())+45.0;
					if (endPosDir>=360.0) endPosDir=endPosDir-360.0;
					
					if (startPosDir<endPosDir) {
						if(pr.getCurrentDirirection().doubleValue()>=startPosDir && pr.getCurrentDirirection().doubleValue()<=endPosDir) positiva=true;
					}
					else {
						if((pr.getCurrentDirirection().doubleValue()>=startPosDir && pr.getCurrentDirirection().doubleValue()<=360.0) || (pr.getCurrentDirirection().doubleValue()>=0.0 && pr.getCurrentDirirection().doubleValue()<=endPosDir)) positiva=true;
					}
					
					double curSpeed = pr.getCurrentSpeed();
					
					//Fattore cdi correzione della velocit� nave in funzione della corrente
					if (positiva) shipSpeed = pr.getSpeed().doubleValue()+curSpeed;
					else shipSpeed = pr.getSpeed().doubleValue()-curSpeed;
					
					
				} catch (Throwable e) {
					e.printStackTrace();
					shipSpeed = pr.getSpeed().doubleValue(); // In caso di errore
				}
				
				
				totSpeed+=shipSpeed;
			}
			return totSpeed/(new Double(rps.size()));
		}
		return 0.0;
	}
	
	
	/**
	 * Calcola la velocit� (in nodi) tra il punto passatogli e quello prcedente
	 * @param rp punto
	 * @return velocit� media (in nodi)
	 */
	protected double getRoutePointOneHourAvSpeed(RoutePoint rp) {
		if (getRoutePoints()!=null && getRoutePoints().size()>0) {
			List<RoutePoint> rpo = getRoutePoints();
			Collections.sort(rpo);
			RoutePoint prev = null;
			for (RoutePoint r : rpo) {
				if (r.getUuid().equals(rp.getUuid())) break;
				prev = r;
			}
		
			if (prev!=null) {
				return (rp.getSpeed()+prev.getSpeed())/2.0;
			}
		}
		return 0.0;
	}
	
	
	
	/**
	 * Calcola la media della velocit� (basandosi sui route points registrati) basandosi sui route points precedenti a quello passatogli come riferimento (compreso)
	 * @param rp punto
	 * @return media della velocit� (in nodi)
	 */
	protected double getRoutePointAverageSpeed(RoutePoint rp) {
		if (getRoutePoints()!=null && getRoutePoints().size()>0) {
			List<RoutePoint> rpo = getRoutePoints();
			Collections.sort(rpo);
			List<RoutePoint> rpsPrev = new ArrayList<RoutePoint>(); // Questo sar� ordinato per data crescente
			for (RoutePoint r : rpo) {
				rpsPrev.add(r);
				if (r.getUuid().equals(rp.getUuid())) break;
			}
			
			if (rpsPrev.size()>0) {
				// Calcolo media
				double cf = 0.0;
				for (RoutePoint rr : rpsPrev) {
					cf+= rr.getSpeed();
				}
				cf = cf/rpsPrev.size(); // media
				return cf;
			}
			return 0.0;
		}
		return 0.0;
	}
	
	/**
	 * Calcola la media della velocit� (basandosi sui route points registrati) basandosi sui route points precedenti a quello passatogli come riferimento (compreso)
	 * La base di partenza per il calcolo � il primo routePoint del leg a cui appartiene il RoutePoint passato come riferimento.
	 * @param rp punto
	 * @return media della velocit� (in nodi)
	 */
	protected double getRoutePointAverageSpeedByLeg(RoutePoint rp) {
		try{
		
//			System.out.println("getRoutePointAverageSpeedByLeg " + rp.getActivePath().getId() + " - id rp: "+ rp.getId());
		
			if (getRoutePoints()!=null && getRoutePoints().size()>0) {
				List<RoutePoint> rpo = getRoutePoints();
				Collections.sort(rpo);
				List<RoutePoint> rpsPrev = new ArrayList<RoutePoint>(); // Questo sar� ordinato per data crescente
				for (RoutePoint r : rpo) {
//					System.out.println("getRoutePointAverageSpeedByLeg path: " + r.getActivePath().getId() + " - r.id: " + r.getId() + " - r.speed: " + r.getSpeed());
					if(r.getActivePath().equals(rp.getActivePath())) rpsPrev.add(r);
					if (r.getUuid().equals(rp.getUuid())) break;
				}
				
				if (rpsPrev.size()>0) {
					// Calcolo media
					double cf = 0.0;
					for (RoutePoint rr : rpsPrev) {
						cf+= rr.getSpeed();
					}
					cf = cf/rpsPrev.size(); // media
					return cf;
				}
				return 0.0;
			}
		} catch (Exception e) {			
			e.printStackTrace();
			return 0.0;			
		}
		return 0.0;
	}
	
	/**
	 * Calcola la distanza basandosi sui route points precedenti a quello passatogli come riferimento (compreso)
	 * @param rp punto
	 * @return distanza (in nm)
	 */
	public double getRoutePointTotalDistance(RoutePoint rp) {
		if (getRoutePoints()!=null && getRoutePoints().size()>0) {
			List<RoutePoint> rpo = getRoutePoints();
			Collections.sort(rpo);
			List<RoutePoint> rpsPrev = new ArrayList<RoutePoint>(); // Questo sar� ordinato per data crescente
			for (RoutePoint r : rpo) {
				rpsPrev.add(r);
				if (r.getUuid().equals(rp.getUuid())) break;
			}
			
			if (rpsPrev.size()>0) {
				
				RoutePoint preRoute = null;
				double totalDistance = 0.0;
				for (RoutePoint rr : rpsPrev) {
					if (preRoute != null) {
						// Verifico se entrambi sono numeri
						try {
							Double.parseDouble(preRoute.getLatitude());
							Double.parseDouble(rr.getLatitude());
							Double.parseDouble(preRoute.getLongitude());
							Double.parseDouble(rr.getLongitude());
						} catch (Exception e) {
							return 0.0;
						}
						
						// Calcolo distanza
						
						// Spherical Law of Cosines formula
						double R = 6371.0; // circonferenza terra in km
						double d = 0.0;
						try {
							d = Math.acos(Math.sin(Math.toRadians(Double.parseDouble(preRoute.getLatitude())))*Math.sin(Math.toRadians(Double.parseDouble(rr.getLatitude()))) + 
					                  Math.cos(Math.toRadians(Double.parseDouble(preRoute.getLatitude())))*Math.cos(Math.toRadians(Double.parseDouble(rr.getLatitude()))) *
					                  Math.cos(Math.toRadians(Double.parseDouble(rr.getLongitude()))-Math.toRadians(Double.parseDouble(preRoute.getLongitude())))) * R;
					
							d = d/1.852; // Converto in miglia nautiche
						} catch (Exception e) {
							System.out.println("Error: " + e.getMessage());
						}
						
						totalDistance+=d;
					}
					preRoute = rr;
				}
				
				return totalDistance;
			}
			return 0.0;
		}
		return 0.0;
	}
	
	
	/**
	 * Calcola la distanza (in miglia nautiche) tra il punto passatogli e quello prcedente
	 * @param rp punto
	 * @return distanza dal punto precedente (in miglia nautiche)
	 */
	protected double getRoutePointDistance(RoutePoint rp) {
		if (getRoutePoints()!=null && getRoutePoints().size()>0) {
			List<RoutePoint> rpo = getRoutePoints();
			Collections.sort(rpo);
			RoutePoint prev = null;
			for (RoutePoint r : rpo) {
				if (r.getUuid().equals(rp.getUuid())) break;
				prev = r;
			}
			
			// Prev se !=null � il punto precedente
			//System.out.println("Punto analizzato: " + rp);
			//System.out.println("Punto precedente trovato: " + prev);
			
			if (prev != null) {
				// Verifico se entrambi sono numeri
				try {
					Double.parseDouble(prev.getLatitude());
					Double.parseDouble(rp.getLatitude());
					Double.parseDouble(prev.getLongitude());
					Double.parseDouble(rp.getLongitude());
				} catch (Exception e) {
					return 0.0;
				}
				
				// Calcolo distanza
				
				// Spherical Law of Cosines formula
				double R = 6371.0; // circonferenza terra in km
				double d = 0.0;
				try {
					d = Math.acos(Math.sin(Math.toRadians(Double.parseDouble(prev.getLatitude())))*Math.sin(Math.toRadians(Double.parseDouble(rp.getLatitude()))) + 
			                  Math.cos(Math.toRadians(Double.parseDouble(prev.getLatitude())))*Math.cos(Math.toRadians(Double.parseDouble(rp.getLatitude()))) *
			                  Math.cos(Math.toRadians(Double.parseDouble(rp.getLongitude()))-Math.toRadians(Double.parseDouble(prev.getLongitude())))) * R;
//					d = Math.acos(Math.sin(Double.parseDouble(prev.getLatitude()))*Math.sin(Double.parseDouble(rp.getLatitude())) + 
//			                  Math.cos(Double.parseDouble(prev.getLatitude()))*Math.cos(Double.parseDouble(rp.getLatitude())) *
//			                  Math.cos(Double.parseDouble(rp.getLongitude())-Double.parseDouble(prev.getLongitude()))) * R;
			
					d = d/1.852; // Converto in miglia nautiche
				} catch (Exception e) {
					System.out.println("Error: " + e.getMessage());
				}
				
				return d;
			}
			return 0.0;
		}
		return 0.0;
	}
	
//	/**
//	 * Calcola la media del fattore corrente (basandosi sui route points registrati) nelle 24 ore prendendo come riferimento la data del punto passatogli
//	 * @param rp punto
//	 * @return media del fattore corrente nelle 24 ore 
//	 */
//	protected double getRoutePointAverageCurrentFactorIn24Hours(RoutePoint rp) {
//		if (getRoutePoints()!=null && getRoutePoints().size()>0) {
//			List<RoutePoint> rpo = getRoutePoints();
//			Collections.sort(rpo);
//			List<RoutePoint> in24H = new ArrayList<RoutePoint>(); // Questo sar� ordinato per data crescente
//			for (RoutePoint r : rpo) {
//				if (DateUtils.isSameDay(r.getRegistrationDate(), rp.getRegistrationDate())) {
//					in24H.add(r);
//				}
//			}
//			
//			if (in24H.size()>0) { // TODO qui c'� da considerare il noon e non le 24 ore
//				// Calcolo media
//				double cf = 0.0;
//				for (RoutePoint rr : in24H) {
//					
//					double currentSpeedWithSign = getCurrentSpeedWithSign(rr.getCurrentSpeed(), rr.getCurrentDirirection(), rr.getTrueCourse());
//					
//					cf+= rr.getCurrentSpeed(); // in nodi !!!!!!!!!!!!!!!!!!!!!!! errore verificare !!!!!!!!!!!!
//				}
//				cf = cf/in24H.size();
//				return cf;
//			}
//			return 0.0;
//		}
//		return 0.0;
//	}
	
	/**
	 * Restituisce la velocit� della corrente con il segno
	 *  Regola: Viene considerata positiva se  la direziene della corrente e compresa  tra un angolo Dir_nave+135 e Dir_nave+225
	 *  		negativa se non compresa
	 * @param currentSpeed
	 * @param currentDirirection
	 * @param shipDirection
	 * @return
	 */
	private double getCurrentSpeedWithSign(Double currentSpeed, Double currentDirirection, String shipDirection) {
		
		Double shipDirectionDouble = null;
		if (shipDirection!=null && !shipDirection.equals("")) try {shipDirectionDouble = Double.parseDouble(shipDirection);} catch (Exception e) {e.printStackTrace();}
		if (currentSpeed==null || currentDirirection==null || shipDirectionDouble==null) return currentSpeed;
		
		int sign = 1;
		Double shipDirectionDoubleMin = shipDirectionDouble + 135.0;
		Double shipDirectionDoubleMax = shipDirectionDouble + 225.0;
		if (shipDirectionDoubleMin>360.0) shipDirectionDoubleMin = shipDirectionDoubleMin - 360.0;
		if (shipDirectionDoubleMax>360.0) shipDirectionDoubleMax = shipDirectionDoubleMax - 360.0;
		
		if (shipDirectionDoubleMin>180.0 && shipDirectionDoubleMax<180.0) {
			if (currentDirirection>=shipDirectionDoubleMin && currentDirirection<=360 || currentDirirection>=0.0 && currentDirirection<=shipDirectionDoubleMax) sign = -1; 
		}
		else {
			if (currentDirirection>=shipDirectionDoubleMin && currentDirirection<=shipDirectionDoubleMax) sign = -1;
		}
		return currentSpeed * sign;
	}

	/**
	 * Calcola la media della velocit� (basandosi sui route points registrati) nelle 24 ore prendendo come riferimento la data del punto passatogli
	 * @param rp punto
	 * @return media del fattore corrente nelle 24 ore 
	 */
	protected double getRoutePointAverageSpeedIn24Hours(RoutePoint rp) {
		if (getRoutePoints()!=null && getRoutePoints().size()>0) {
			List<RoutePoint> rpo = getRoutePoints();
			Collections.sort(rpo);
			List<RoutePoint> in24H = new ArrayList<RoutePoint>(); // Questo sar� ordinato per data crescente
			for (RoutePoint r : rpo) {
				if (DateUtils.isSameDay(r.getRegistrationDate(), rp.getRegistrationDate())) {
					in24H.add(r);
				}
			}
			
			if (in24H.size()>0) { // TODO qui c'� da considerare il noon e non le 24 ore
				// Calcolo media
				double cf = 0.0;
				for (RoutePoint rr : in24H) {
					cf+= rr.getSpeed();
				}
				cf = cf/in24H.size(); // media
				return cf;
			}
			return 0.0;
		}
		return 0.0;
	}
	
	
	/**
	 * Calcola la media dei consumi del flussometro FM1  (basandosi sui route points registrati) nelle 24 ore prendendo come riferimento la data del punto passatogli
	 * @param rp punto
	 * @return media del fattore corrente nelle 24 ore 
	 */
	protected double getRoutePointAverageFm1In24Hours(RoutePoint rp) {
		if (getRoutePoints()!=null && getRoutePoints().size()>0) {
			List<RoutePoint> rpo = getRoutePoints();
			Collections.sort(rpo);
			List<RoutePoint> in24H = new ArrayList<RoutePoint>(); // Questo sar� ordinato per data crescente
			for (RoutePoint r : rpo) {
				if (DateUtils.isSameDay(r.getRegistrationDate(), rp.getRegistrationDate())) {
					in24H.add(r);
				}
			}
			
			if (in24H.size()>0) { // TODO qui c'� da considerare il noon e non le 24 ore
				// Calcolo media in kg per ora
				double cf = 0.0;
				for (RoutePoint rr : in24H) {
					cf+= rr.getFm1();
				}
				cf = cf/in24H.size(); // media
				return cf;
			}
			return 0.0;
		}
		return 0.0;
	}
	
	/**
	 * Calcola il totale dei consumi del flussometro main engine  (basandosi sui route points registrati) nelle 24 ore prendendo come riferimento la data del punto passatogli
	 * @param rp punto
	 * @return consumi nelle 24 ore 
	 */
	protected double getMainEngineFlowConsInTonsIn24Hours(RoutePoint rp) {
		if (getRoutePoints()!=null && getRoutePoints().size()>0) {
			List<RoutePoint> rpo = getRoutePoints();
			Collections.sort(rpo);
			List<RoutePoint> in24H = new ArrayList<RoutePoint>(); // Questo sar� ordinato per data crescente
			for (RoutePoint r : rpo) {
				if (DateUtils.isSameDay(r.getRegistrationDate(), rp.getRegistrationDate())) {
					in24H.add(r);
				}
			}
			
			if (in24H.size()>0) { // TODO qui c'� da considerare il noon e non le 24 ore
				// Calcolo somma
				double cf = 0.0;
				for (RoutePoint rr : in24H) {
					//cf+= (rr.getFm1()!=null?rr.getFm1():0.0);
					
					cf+= ( (rr.getFm1()!=null?rr.getFm1():0.0)-((rr.getFm2()!=null?rr.getFm2():0.0)-(rr.getFm3()!=null?rr.getFm3():0.0)) );					
				}
				return cf / 1000;
			}
			return 0.0;
		}
		return 0.0;
	}
	
	
	/**
	 * Calcola il totale dei consumi del flussometro main engine  (basandosi sui route points registrati) durante tutto il viaggio
	 * @param rp punto
	 * @return totale consumi nel viaggio
	 */
	protected double getMainEngineFlowConsPerVoyage(RoutePoint rp) {
		if (getRoutePoints()!=null && getRoutePoints().size()>0) {
			List<RoutePoint> rpo = getRoutePoints();
			Collections.sort(rpo);
			// Calcolo somma
			double cf = 0.0;
			System.out.println("getMainEngineFlowConsPerVoyage ");
			for (RoutePoint r : rpo) {
				//cf+= (r.getFm1()!=null?r.getFm1():0.0);
				
				cf+= ( (r.getFm1()!=null?r.getFm1():0.0)-((r.getFm2()!=null?r.getFm2():0.0)-(r.getFm3()!=null?r.getFm3():0.0)) );
//				System.out.println("getMainEngineFlowConsPerVoyage cf: " + cf);
			}
			return cf / 1000;
		}
		return 0.0;
	}
	
	
		
	/**
	 * Calcola il totale dei consumi dei flussometri diesel generator  (basandosi sui route points registrati) nelle 24 ore prendendo come riferimento la data del punto passatogli
	 * @param rp punto
	 * @return consumi nelle 24 ore 
	 */
	protected double getDgsFlowConsInTonsIn24Hours(RoutePoint rp) {
		if (getRoutePoints()!=null && getRoutePoints().size()>0) {
			List<RoutePoint> rpo = getRoutePoints();
			Collections.sort(rpo);
			List<RoutePoint> in24H = new ArrayList<RoutePoint>(); // Questo sar� ordinato per data crescente
			for (RoutePoint r : rpo) {
				if (DateUtils.isSameDay(r.getRegistrationDate(), rp.getRegistrationDate())) {
					in24H.add(r);
				}
			}
			
			if (in24H.size()>0) { // TODO qui c'� da considerare il noon e non le 24 ore
				// Calcolo somma
				double cf = 0.0;
				for (RoutePoint rr : in24H) {
					
					
					cf+= (rr.getFm2()!=null?rr.getFm2():0.0)-(rr.getFm3()!=null?rr.getFm3():0.0);
				}
				return cf / 1000;
			}
			return 0.0;
		}
		return 0.0;
	}
	
	
	/**
	 * Calcola il totale dei consumi del flussometro diesel generator  (basandosi sui route points registrati) durante tutto il viaggio
	 * @param rp punto
	 * @return totale consumi nel viaggio
	 */
	protected double getDgsFlowConsPerVoyage(RoutePoint rp) {
		if (getRoutePoints()!=null && getRoutePoints().size()>0) {
			List<RoutePoint> rpo = getRoutePoints();
			Collections.sort(rpo);
			// Calcolo somma
			double cf = 0.0;

			for (RoutePoint r : rpo) {
				cf+= (r.getFm2()!=null?r.getFm2():0.0)-(r.getFm3()!=null?r.getFm3():0.0);
			}
			return cf / 1000;
		}
		return 0.0;
	}
	
	
	
	
	/**
	 * Calcola il totale dei consumi del flussometro fm4(AuxBoiler)  (basandosi sui route points registrati) nelle 24 ore prendendo come riferimento la data del punto passatogli
	 * @param rp punto
	 * @return consumi nelle 24 ore 
	 */
	protected double getfm4FlowConsInTonsIn24Hours(RoutePoint rp) {
		if (getRoutePoints()!=null && getRoutePoints().size()>0) {
			List<RoutePoint> rpo = getRoutePoints();
			Collections.sort(rpo);
			List<RoutePoint> in24H = new ArrayList<RoutePoint>(); // Questo sar� ordinato per data crescente
			for (RoutePoint r : rpo) {
				if (DateUtils.isSameDay(r.getRegistrationDate(), rp.getRegistrationDate())) {
					in24H.add(r);
				}
			}
			
			if (in24H.size()>0) { // TODO qui c'� da considerare il noon e non le 24 ore
				// Calcolo somma
				double cf = 0.0;
				for (RoutePoint rr : in24H) {
					cf+= (rr.getFm4()!=null?rr.getFm4():0.0);
				}
				return cf / 1000;
			}
			return 0.0;
		}
		return 0.0;
	}
	
	
	/**
	 * Calcola il totale dei consumi del flussometro  fm4(AuxBoiler) (basandosi sui route points registrati) durante tutto il viaggio
	 * @param rp punto
	 * @return totale consumi nel viaggio
	 */
	protected double getFm4FlowConsPerVoyage(RoutePoint rp) {
		if (getRoutePoints()!=null && getRoutePoints().size()>0) {
			List<RoutePoint> rpo = getRoutePoints();
			Collections.sort(rpo);
			// Calcolo somma
			double cf = 0.0;

			for (RoutePoint r : rpo) {
				cf+= (r.getFm4()!=null?r.getFm4():0.0);
			}
			return cf / 1000;
		}
		return 0.0;
	}
	
	
	/**
	 * Calcola la media dei consumi del flussometro FM2  (basandosi sui route points registrati) nelle 24 ore prendendo come riferimento la data del punto passatogli
	 * @param rp punto
	 * @return media del fattore corrente nelle 24 ore 
	 */
	protected double getRoutePointAverageFm2In24Hours(RoutePoint rp) {
		if (getRoutePoints()!=null && getRoutePoints().size()>0) {
			List<RoutePoint> rpo = getRoutePoints();
			Collections.sort(rpo);
			List<RoutePoint> in24H = new ArrayList<RoutePoint>(); // Questo sar� ordinato per data crescente
			for (RoutePoint r : rpo) {
				if (DateUtils.isSameDay(r.getRegistrationDate(), rp.getRegistrationDate())) {
					in24H.add(r);
				}
			}
			
			if (in24H.size()>0) { // TODO qui c'� da considerare il noon e non le 24 ore
				// Calcolo media in kg per ora
				double cf = 0.0;
				for (RoutePoint rr : in24H) {
					cf+= rr.getFm2();
				}
				cf = cf/in24H.size(); // media
				return cf;
			}
			return 0.0;
		}
		return 0.0;
	}
	
	/**
	 * Calcola la media dei consumi del flussometro FM3  (basandosi sui route points registrati) nelle 24 ore prendendo come riferimento la data del punto passatogli
	 * @param rp punto
	 * @return media del fattore corrente nelle 24 ore 
	 */
	protected double getRoutePointAverageFm3In24Hours(RoutePoint rp) {
		if (getRoutePoints()!=null && getRoutePoints().size()>0) {
			List<RoutePoint> rpo = getRoutePoints();
			Collections.sort(rpo);
			List<RoutePoint> in24H = new ArrayList<RoutePoint>(); // Questo sar� ordinato per data crescente
			for (RoutePoint r : rpo) {
				if (DateUtils.isSameDay(r.getRegistrationDate(), rp.getRegistrationDate())) {
					in24H.add(r);
				}
			}
			
			if (in24H.size()>0) { // TODO qui c'� da considerare il noon e non le 24 ore
				// Calcolo media in kg per ora
				double cf = 0.0;
				for (RoutePoint rr : in24H) {
					cf+= rr.getFm3();
				}
				cf = cf/in24H.size(); // media
				return cf;
			}
			return 0.0;
		}
		return 0.0;
	}
	
	/**
	 * Calcola la media dei consumi del flussometro FM4  (basandosi sui route points registrati) nelle 24 ore prendendo come riferimento la data del punto passatogli
	 * @param rp punto
	 * @return media del fattore corrente nelle 24 ore 
	 */
	protected double getRoutePointAverageFm4In24Hours(RoutePoint rp) {
		if (getRoutePoints()!=null && getRoutePoints().size()>0) {
			List<RoutePoint> rpo = getRoutePoints();
			Collections.sort(rpo);
			List<RoutePoint> in24H = new ArrayList<RoutePoint>(); // Questo sar� ordinato per data crescente
			for (RoutePoint r : rpo) {
				if (DateUtils.isSameDay(r.getRegistrationDate(), rp.getRegistrationDate())) {
					in24H.add(r);
				}
			}
			
			if (in24H.size()>0) { // TODO qui c'� da considerare il noon e non le 24 ore
				// Calcolo media in kg per ora
				double cf = 0.0;
				for (RoutePoint rr : in24H) {
					cf+= rr.getFm4();
				}
				cf = cf/in24H.size(); // media
				return cf;
			}
			return 0.0;
		}
		return 0.0;
	}
	
	/**
	 * Calcola la media dei consumi del flussometro FM5  (basandosi sui route points registrati) nelle 24 ore prendendo come riferimento la data del punto passatogli
	 * @param rp punto
	 * @return media del fattore corrente nelle 24 ore 
	 */
	protected double getRoutePointAverageFm5In24Hours(RoutePoint rp) {
		if (getRoutePoints()!=null && getRoutePoints().size()>0) {
			List<RoutePoint> rpo = getRoutePoints();
			Collections.sort(rpo);
			List<RoutePoint> in24H = new ArrayList<RoutePoint>(); // Questo sar� ordinato per data crescente
			for (RoutePoint r : rpo) {
				if (DateUtils.isSameDay(r.getRegistrationDate(), rp.getRegistrationDate())) {
					in24H.add(r);
				}
			}
			
			if (in24H.size()>0) { // TODO qui c'� da considerare il noon e non le 24 ore
				// Calcolo media in kg per ora
				double cf = 0.0;
				for (RoutePoint rr : in24H) {
					cf+= rr.getFm5();
				}
				cf = cf/in24H.size(); // media
				return cf;
			}
			return 0.0;
		}
		return 0.0;
	}
	/**
	 * Calcola il totale dei consumi FO  (basandosi sui route points registrati) nelle 24 ore prendendo come riferimento la data del punto passatogli
	 * @param rp punto
	 * @return consumi nelle 24 ore 
	 */
	protected double getFoConsumptionIn24Hours(RoutePoint rp) {
		if (getRoutePoints()!=null && getRoutePoints().size()>0) {
			List<RoutePoint> rpo = getRoutePoints();
			Collections.sort(rpo);
			List<RoutePoint> in24H = new ArrayList<RoutePoint>(); // Questo sar� ordinato per data crescente
			for (RoutePoint r : rpo) {
				if (DateUtils.isSameDay(r.getRegistrationDate(), rp.getRegistrationDate())) {
					in24H.add(r);
				}
			}
			
			if (in24H.size()>0) { // TODO qui c'� da considerare il noon e non le 24 ore
				// Calcolo somma
				double cf = 0.0;
				for (RoutePoint rr : in24H) {
					cf+= (rr.getFoConsumption()!=null?rr.getFoConsumption():0.0);
				}
				return cf;
			}
			return 0.0;
		}
		return 0.0;
	}
	
	/**
	 * Calcola la distanza (basandosi sui route points registrati) percorsa nelle 24 ore prendendo come riferimento la data del punto passatogli
	 * @param rp punto
	 * @param dayStrict se false prende in considerazione anche il punto appena precedente al giorno in considerazione in modo da calcolare anche la distanza per il primo route point del giono. Se false prende in considerazione solo i route points del giono
	 * @return distanza nelle 24 ore (in miglia nautiche)
	 */
	protected double getRoutePointDistanceIn24Hours(RoutePoint rp, boolean dayStrict) {
		//System.out.println("\n\n\ngetRoutePointDistanceIn24Hours.....");
		if (getRoutePoints()!=null && getRoutePoints().size()>0) {
			List<RoutePoint> rpo = getRoutePoints();
			Collections.sort(rpo);
			List<RoutePoint> in24H = new ArrayList<RoutePoint>(); // Questo sar� ordinato per data crescente
			RoutePoint firstBefore = null;
			for (RoutePoint r : rpo) {
				if (DateUtils.isSameDay(r.getRegistrationDate(), rp.getRegistrationDate())) {
					//System.out.println("Route point " + r.getRegistrationDate() + "   and " + rp.getRegistrationDate() + "    are in same day!!!");
					if (!dayStrict && firstBefore!=null) in24H.add(firstBefore);
					in24H.add(r);
					firstBefore = null;
				}
				else if (in24H.size()==0) firstBefore = r;
			}
			
			//System.out.println("Total in same day: " + in24H.size());
			// Per sicurezza
			Collections.sort(in24H);
			
			if (in24H.size()>1) { // TODO qui c'� da considerare il noon e non le 24 ore
				// Calcolo distanza
				double d = 0.0;
				double R = 6371.0; // circonferenza terra in km
				RoutePoint prev = null;
				for (RoutePoint rr : in24H) {
					if (prev==null) prev = rr;
					else {
						try {
							// Spherical Law of Cosines formula
							d += Math.acos(Math.sin(Math.toRadians(Double.parseDouble(prev.getLatitude())))*Math.sin(Math.toRadians(Double.parseDouble(rr.getLatitude()))) + 
							                  Math.cos(Math.toRadians(Double.parseDouble(prev.getLatitude())))*Math.cos(Math.toRadians(Double.parseDouble(rr.getLatitude()))) *
							                  Math.cos(Math.toRadians(Double.parseDouble(rr.getLongitude()))-Math.toRadians(Double.parseDouble(prev.getLongitude())))) * R;
						} catch (Exception e) {
							System.out.println("Error: " + e.getMessage());
						}
						
					}
					//System.out.println("Distanza incremetale: " + d + "   considerando i punti: " + prev.getRegistrationDate() + "   and " + rr.getRegistrationDate());
					prev = rr;
				}
				d = d/1.852; // Converto in miglia nautiche
				return d;
			}
			return 0.0;
		}
		return 0.0;
	}
	
	
	
	/**
	 * Restituisce i RoutPoints che fanno parte dei giorni in Good Weather. Se diverso da null, prender� in considerazione tutti i route point precedenti a quello specificato (compreso)
	 * @return RoutPoints che fanno parte dei giorni in Good Weather
	 */
	public List<RoutePoint> getRoutePointsInGoodWheatherDays(RoutePoint rp) {
		return getRoutePointsForWheatherDays(true, rp);
	}
	/**
	 * Restituisce i RoutPoints che fanno parte dei giorni in Bad Weather. Se diverso da null, prender� in considerazione tutti i route point precedenti a quello specificato (compreso)
	 * @return RoutPoints che fanno parte dei giorni in Bad Weather
	 */
	public List<RoutePoint> getRoutePointsInBadWheatherDays(RoutePoint rp) {
		return getRoutePointsForWheatherDays(false, rp);
	}
	
	private List<RoutePoint> getRoutePointsForWheatherDays(boolean good, RoutePoint lastRoutePoint) {
		double windBeaufortForGoodWeather = 4.0; // Default 4 della scala Beaufort
		if (voyageSettings!=null && voyageSettings.getMaxWindBf()!=null) windBeaufortForGoodWeather = voyageSettings.getMaxWindBf();
		
		double waveDouglasForGoodWeather = 3.0; // Default 3 metri equivalente a circa il grado 4 della scala douglas
		if (voyageSettings!=null && voyageSettings.getMaxSeaDg()!=null) waveDouglasForGoodWeather = ScaleConversionUtils.fromDouglasToMaxWaveHeightInMeters(voyageSettings.getMaxSeaDg().intValue());
		
		List<RoutePoint> weatherPoints = new ArrayList<RoutePoint>();
		
		if (getRoutePoints()!=null && getRoutePoints().size()>0) {
			
			List<RoutePoint> rpDay = new ArrayList<RoutePoint>(); // Gruppo di route point che fanno parte di un giorno
//			List<RoutePoint> rpo = getRoutePoints();
//			Collections.sort(rpo);
			
			
			
			List<RoutePoint> rpoGlobal = getRoutePoints();
			Collections.sort(rpoGlobal);
			List<RoutePoint> rpo = new ArrayList<RoutePoint>(); // Questo sar� ordinato per data crescente
			for (RoutePoint r : rpoGlobal) {
				rpo.add(r);
				if (lastRoutePoint!=null && r.getUuid().equals(lastRoutePoint.getUuid())) break;
			}
			
			
			
			
			RoutePoint last = null;
			for (RoutePoint r : rpo) {
				if (last==null) {
					last = r;
					rpDay.add(r);
				}
				else if (DateUtils.isSameDay(r.getRegistrationDate(), last.getRegistrationDate())) {
					last = r;
					rpDay.add(r);
				}
				else {
					// Calcolo se il giorno � in good weather o bad weather
					int goodRootPoint =0;
					for (RoutePoint rd : rpDay) {
						if (rd.getWindBeaufort()<=windBeaufortForGoodWeather && (rd.getWaveHeight()==null || (rd.getWaveHeight()!=null && rd.getWaveHeight()<=waveDouglasForGoodWeather))) goodRootPoint++; 
					}
					if (good) {
						if (goodRootPoint>(rpDay.size()/2)) {
							// Giorno in good weather
							weatherPoints.addAll(rpDay);
						}
					}
					else {
						if (goodRootPoint<=(rpDay.size()/2)) {
							// Giorno in bad weather
							weatherPoints.addAll(rpDay);
						}
					}
					rpDay.clear();
					last = r;
					rpDay.add(r);
				}
			}
			if (rpDay.size()>0) {
				// Valuto gli ultimi
				int goodRootPoint =0;
				for (RoutePoint rd : rpDay) {
					if (rd.getWindBeaufort()<=windBeaufortForGoodWeather && (rd.getWaveHeight()==null || (rd.getWaveHeight()!=null && rd.getWaveHeight()<=waveDouglasForGoodWeather))) goodRootPoint++;
				}
				if (good) {
					if (goodRootPoint>(rpDay.size()/2)) {
						// Giorno in good weather
						weatherPoints.addAll(rpDay);
					}
				}
				else {
					if (goodRootPoint<=(rpDay.size()/2)) {
						// Giorno in bad weather
						weatherPoints.addAll(rpDay);
					}
				}
			}
		}
		
		System.out.println("Trovati " + weatherPoints.size() + "in good?" + good);
//		for (RoutePoint rppp : weatherPoints) {
//			System.out.println("Route point: " + rppp);
//		}
		
		return weatherPoints;
	}
	
	/**
	 * Restituisce tutti i RoutPoints in Good Weather
	 * @return RoutPoints in Good Weather
	 */
	public List<RoutePoint> getRoutePointsInGoodWheather() {
		return getAllRoutePointsForWheather(true, false);
	}
	/**
	 * Restituisce tutti i RoutPoints in Bad Weather
	 * @return RoutPoints in Bad Weather
	 */
	public List<RoutePoint> getRoutePointsInBadWheather() {
		return getAllRoutePointsForWheather(false, false);
	}
	
	
	/**
	 * Restituisce tutti i RoutPoints in Good Weather appartenenti a quel leg
	 * @return RoutPoints in Good Weather
	 */
	public List<RoutePoint> getRoutePointsInGoodWheatherByLeg() {
		System.out.println("In getRoutePointsInGoodWheatherByLeg. ");
		return getAllRoutePointsForWheather(true, true);
	}
	
	private List<RoutePoint> getAllRoutePointsForWheather(boolean good, boolean leg) {
		double windBeaufortForGoodWeather = 4.0; // Default 4 della scala Beaufort
		if (voyageSettings!=null && voyageSettings.getMaxWindBf()!=null) windBeaufortForGoodWeather = voyageSettings.getMaxWindBf();
		
		double waveDouglasForGoodWeather = 3.0; // Default 3 metri equivalente a circa il grado 4 della scala douglas
		if (voyageSettings!=null && voyageSettings.getMaxSeaDg()!=null) waveDouglasForGoodWeather = ScaleConversionUtils.fromDouglasToMaxWaveHeightInMeters(voyageSettings.getMaxSeaDg().intValue());
		
		
		List<RoutePoint> weatherPoints = new ArrayList<RoutePoint>(); // Questo sar� ordinato per data crescente
		
		System.out.println("In getAllRoutePointsForWheather.");
		if (getRoutePoints()!=null && getRoutePoints().size()>0) {			
			List<RoutePoint> rpo = getRoutePoints();
			
			Collections.sort(rpo);
			
			for (RoutePoint r : rpo) {
				
				if (r.getWindBeaufort()<=windBeaufortForGoodWeather && (r.getWaveHeight()==null || (r.getWaveHeight()!=null && r.getWaveHeight()<=waveDouglasForGoodWeather))) { 		
					if (good) {
//						System.out.println("In getAllRoutePointsForWheather r activePath: routeID: " + r.getId() + " - getActivePath: " + r.getActivePath());
//						System.out.println("In getAllRoutePointsForWheather getActivePath: " + getActivePath());
						//considero i leg
						if(r.getActivePath()!=null && getActivePath()!=null){
							if(!leg && r.getActivePath().equals(getActivePath())){
								// Punto in good weather
								weatherPoints.add(r);
							}
						}
					}
				}
				else if (!good){
					if(r.getActivePath()!=null && getActivePath()!=null){
						//considero i leg
						if(!leg && r.getActivePath().equals(getActivePath())){
							// Punto in bad weather
							weatherPoints.add(r);
						}
					}
				}
			}
		}
		
		System.out.println("Trovati " + weatherPoints.size() + "in good?" + good);
		
		return weatherPoints;
	}
	
	
	/**
	 * Restituisce il numero di ore coperte dai route points passatigli come argomento
	 * @param routePoints
	 * @return numero ore
	 */
	public int getHoursFromRoutePoints(List<RoutePoint> routePoints) {
		
		int hours =0;
		
		if (routePoints!=null && routePoints.size()>0) return routePoints.size()-1;
		
		return hours;
	}
	
	/**
	 * Restituisce il numero di giorni coperti dai route points passatigli come argomento
	 * @param routePoints
	 * @return numero giorni
	 */
	public int getDaysFromRoutePoints(List<RoutePoint> routePoints) {
		
		int days =0;
		
		if (routePoints!=null && routePoints.size()>0) {
			
			List<RoutePoint> rpDay = new ArrayList<RoutePoint>(); // Gruppo di route point che fanno parte di un giorno
			Collections.sort(routePoints);
			RoutePoint last = null;
			for (RoutePoint r : routePoints) {
				if (last==null) {
					last = r;
					rpDay.add(r);
					days++;
				}
				else if (DateUtils.isSameDay(r.getRegistrationDate(), last.getRegistrationDate())) {
					last = r;
					rpDay.add(r);
				}
				else {
					rpDay.clear();
					last = r;
					rpDay.add(r);
					days++;
				}
			}
		}
		return days;
	}
	
	public Double getTotalFoConsumptionInGoodWeader() {
		return getRoutePointsTotalConsumption(getRoutePointsInGoodWheather(), true);
	}
	public Double getTotalDoConsumptionInGoodWeader() {
		return getRoutePointsTotalConsumption(getRoutePointsInGoodWheather(), false);
	}
	public Double getTotalFoConsumptionInBadWeader() {
		return getRoutePointsTotalConsumption(getRoutePointsInBadWheather(), true);
	}
	public Double getTotalDoConsumptionInBadWeader() {
		return getRoutePointsTotalConsumption(getRoutePointsInBadWheather(), false);
	}
	
	public Double getTotalDistanceInGoodWeader() {
//		return getTotalDistanceRoutePointsGrouppedByDay(getRoutePointsInGoodWheather());
		double tot = 0.0;
		List<RoutePoint> ll = getRoutePointsInGoodWheather();
		if (ll==null || ll.size()==0) return 0.0;
		for (RoutePoint routePoint : ll) {
			tot += routePoint.getDistanceDouble();
		}
		return tot;
	}
	public int getTotalDaysInGoodWeader() {
		return getDaysFromRoutePoints(getRoutePointsInGoodWheatherDays(null));
	}
	public int getTotalHoursInGoodWeader() {
		return getHoursFromRoutePoints(getRoutePointsInGoodWheatherDays(null));
	}
	public Double getAverageCurrentFactorInGoodWeader() {
		return getRoutePointAverageCurrentFactor(getRoutePointsInGoodWheather());
	}
	public int getTotalHours() {
		return getHoursFromRoutePoints(getRoutePoints());
	}
	
	public Double getTotalDistanceInBadWeatherRoutePoint() {
//		return getTotalDistanceRoutePointsGrouppedByDay(getRoutePointsInBadWheather());
		double tot = 0.0;
		List<RoutePoint> ll = getRoutePointsInBadWheather();
		if (ll==null || ll.size()==0) return 0.0;
		for (RoutePoint routePoint : ll) {
			tot += routePoint.getDistanceDouble();
		}
		return tot;
	}
	public int getTotalDaysInBadWeader() {
		return getDaysFromRoutePoints(getRoutePointsInBadWheatherDays(null));
	}
	
	
	
	/**
	 * Restituisce la media del fattore corrente coperta dai route points
	 * @param routePoints
	 * @return numero giorni
	 */
	public double getRoutePointAverageCurrentFactor(List<RoutePoint> routePoints) {
		
		double tot =0.0;
		
		System.out.println("getRoutePointAverageCurrentFactor");
		if (routePoints!=null) System.out.println("numero route point da analizzare: " + routePoints.size());
		
		if (routePoints!=null && routePoints.size()>0) {
			for (RoutePoint rr : routePoints) {
				
				double currentSpeedWithSign = getCurrentSpeedWithSign(rr.getCurrentSpeed(), rr.getCurrentDirirection(), rr.getTrueCourse());
				
				tot+= currentSpeedWithSign;
			}
			tot = tot/routePoints.size();
		}
		return tot;
	}
	
	/**
	 * Restituisce la distanza totale coperta dai route points
	 * @param routePoints
	 * @return numero giorni
	 */
	public double getRoutePointsTotalConsumption(List<RoutePoint> routePoints, boolean fo) {
		double tot =0.0;
		
		System.out.println("getTotalConsumptionRoutePointsGrouppedByDay fo?" + fo);
		
		if (fo){ // FO Lo prendo dai route points
			if (routePoints!=null) System.out.println("numero route point da analizzare: " + routePoints.size());
			if (routePoints!=null && routePoints.size()>0) {
				for (RoutePoint r : routePoints) {
					// Calcolo
					tot += (r.getFoConsumption()!=null?r.getFoConsumption():0.0);
				}
			}
		}
		else { // DO Lo prendo dalle noon Position ----- qui le prende tutte!!!!! 
			List<NoonPosition> np = getNoonPositions();
			if (np!=null) {
				Collections.sort(np, new NoonPositionSortByRegistrationDate());
				if (np.size()>0) {
					// TODO !!!!!!!!!!!!! errore dovrebbe prendere solo quelle di questa tratta!!!!!!!!!
					double lsdo = np.get(np.size()-1).getTotalLsdo()!=null?np.get(np.size()-1).getTotalLsdo():0.0;
					double hsdo = np.get(np.size()-1).getTotalHsdo()!=null?np.get(np.size()-1).getTotalHsdo():0.0;
					tot = lsdo+hsdo; // TODO !!!! Da controllare
				}
			}
		}
			
		return tot;
	}
	
	
	/**
	 * Restituisce la distanza totale coperta dai route points
	 * @param routePoints
	 * @return numero giorni
	 */
	public double getTotalDistanceRoutePointsGrouppedByDay(List<RoutePoint> routePoints) {
		
		double tot =0.0;
		
		
		System.out.println("getTotalDistanceRoutePointsGrouppedByDay");
		if (routePoints!=null) System.out.println("numero route point da analizzare: " + routePoints.size());
		
		if (routePoints!=null && routePoints.size()>0) {
			Collections.sort(routePoints);
			RoutePoint last = null;
			for (RoutePoint r : routePoints) {
				if (last==null) {
					last = r;
				}
				else if (DateUtils.isSameDay(r.getRegistrationDate(), last.getRegistrationDate())) {
					last = r;
				}
				else {
					// Calcolo
					//System.out.println("Calcolo con il punto " + last.getId());
					tot += getRoutePointDistanceIn24Hours(last, false);
					//System.out.println("res calcolo: " + tot);
					last = r;
				}
			}
			
			if (last!=null) {
				// Calcolo ultimo
				System.out.println("Calcolo ultimo con il punto " + last.getId());
				tot += getRoutePointDistanceIn24Hours(last, false);
				System.out.println("res calcolo: " + tot);
			}
		}
		return tot;
	}
	
		
	/*
	 * 
	 */
	public long getRunningMinute(List<MachineriesBunkerStatus> mcn) {
		boolean stopUseFuelSet = true;
		long diff = 0;
		long runHour = 0;

		System.out.println("DataOdierna: " + EasyDateUtils.getGMT0CurrentDate().getTime());
		
		for (MachineriesBunkerStatus mbs : mcn) {
			
			stopUseFuelSet = true; 
			try{	mbs.getStopUseFuel().equals(null); }
			catch(Exception ex){
				stopUseFuelSet = false; }
			
			
			/******/
			System.out.println("ID MAchBunkStatus: " +  mbs.getId() + "-  GetStartUseFuel: " + mbs.getStartUseFuel().getTime());			
			if(stopUseFuelSet){	System.out.println("GetStopUseFuel" +  mbs.getStopUseFuel().getTime());	}
			/******/
			
			
			if(!stopUseFuelSet && EasyDateUtils.getGMT0CurrentDate().getTime() >= mbs.getStartUseFuel().getTime() ){
				
		        //calcolo il run hour e lo scrivo(se maggiore di 1h scrivo 60min) 

		        System.out.println("");
		        diff = EasyDateUtils.getGMT0CurrentDate().getTime() -  mbs.getStartUseFuel().getTime();
		        //se minore di 60min -> 
		        runHour = runHour + (diff<(1000*60*60)?(diff / (60 * 1000) % 60):60);
		        System.out.println("runHour: " + runHour);
   			}

			if (stopUseFuelSet && EasyDateUtils.getGMT0CurrentDate().getTime() >= mbs.getStartUseFuel().getTime() && 
					 mbs.getStopUseFuel().getTime() >= (EasyDateUtils.getGMT0CurrentDate().getTime() -  (1000*60*60) ) )
			{
				 //calcolo il run hour e lo scrivo 
				diff =  mbs.getStopUseFuel().getTime() -  mbs.getStartUseFuel().getTime();
				runHour = runHour + (diff<(1000*60*60)?(diff / (60 * 1000) % 60):60);
		        System.out.println("runHour2: " + runHour);
				
			}
			
		}
		
		return runHour;

    }
	
	
//	public List<MachineriesBunkerStatus> calculateComsuptionForMachinery(List<MachineriesBunkerStatus> machinaryFuelCalc, List<RoutePoint> rpo, List<Bunkering> bunk){
//		
//       	//calcolo i consumi dei diversi machinery per type of fuel dove calcCons=false e lo scrivo nelle relative righe
//    	System.out.println("************* Calcolo i consumi del bunker status dei diversi machinary ********BGN********.");
//    	
//    	try 
//    	{ 	
//    		boolean stopUseFuelSet = true;
//    		
//    		//COMMENTATO PER ORA: � stato cambiato il calcolo ed ora ci basiamo su ROB in voyage setting
//    		double consForTypeTotal = 0.0;
//    		double consTotStartFuelToVoyage = 0.0;
//    		double robPerFuel = 0.0;
//    		
//    		double cons = 0.0;  
//    		double bunker = 0.0;  
//    		double bunkerStartFuelToVoyage = 0.0;
//    		double bunkerVoyageToStoptFuel = 0.0;
//    		System.out.println("AC 01 ");        		    		
//    		System.out.println("AC 02 - machinaryFuelCalc null: " + machinaryFuelCalc==null + " ");
//    		System.out.println("AC 03 - machinaryFuelCalc.size(): " + machinaryFuelCalc.size());
//    		
//    		if(machinaryFuelCalc!=null){
//        		for (MachineriesBunkerStatus mfc : machinaryFuelCalc) {
//    		   		
//        			System.out.println("AC 04 - mfc: " + mfc.toString());
//
//        			bunker = 0.0;
//        			bunkerStartFuelToVoyage = 0.0;
//        			bunkerVoyageToStoptFuel = 0.0;
//        			//COMMENTATO PER ORA: � stato cambiato il calcolo ed ora ci basiamo su ROB in voyage setting
//        			consForTypeTotal = 0.0;
//        			cons = 0;
//        			robPerFuel = 0.0;
//        			consTotStartFuelToVoyage = 0.0;
//        			
//        			stopUseFuelSet = true; 
//        			try{	mfc.getStopUseFuel().equals(null); }
//        			catch(Exception ex){
//        				stopUseFuelSet = false; }	
//        			
//        			
//        			if (!mfc.isConsCalculated()){     				        				
//	        			//Calcolo i consumi per quel Machinary con regDate tra lo star e stop
//        				//consideraro anche il type
//	        			for (RoutePoint r : rpo) {        				
//	        				
//	            			//if(!stopUseFuelSet && r.getRegistrationDate().getTime() >= mfc.getStartUseFuel().getTime() ){
//	        				if(!stopUseFuelSet){
//	        					if(r.getRegistrationDate().getTime() >= mfc.getStartUseFuel().getTime()){
//	            				
//		            		        System.out.println("AC 04.f - getStopUseFuel e null -> calcolo sino alla data odierna");
//	
//		   	        				System.out.println("AC 07  + cons: " + cons +  " - RegDat " + r.getRegistrationDate() + " - ID " + r.getId() + " rpfc MEConsTons " + (r.getMainEngineFlowConsInTons()!=0.0?r.getMainEngineFlowConsInTons()/60:0.0) + 
//		   	        						" - rpfc dgConsTons " + (r.getDgsFlowConsInTons()!=0.0?r.getDgsFlowConsInTons()/60:0.0) + " - mfc.getTypeFuel(): " + mfc.getTypeFuel());
//		   	        				
//		   	        				//CONSUMI per quel machinary che ha quel fuel
//		   	        				if(mfc.getMachinary().equals("MAIN_ENGINE")){
//		   	        					if(mfc.getTypeFuel().equals("HSFO")) cons = cons + ( (r.getMainEngineFlowConsInTons()!=0.0?r.getMainEngineFlowConsInTons()/60:0.0)* (r.getMainEngineMinuteHsfo()!=null?r.getMainEngineMinuteHsfo():0) );
//		   	        					else if(mfc.getTypeFuel().equals("HSDO")) cons = cons + ( (r.getMainEngineFlowConsInTons()!=0.0?r.getMainEngineFlowConsInTons()/60:0.0)* (r.getMainEngineMinuteHsdo()!=null?r.getMainEngineMinuteHsdo():0) );
//		   	        					else if(mfc.getTypeFuel().equals("LSDO")) cons = cons + ( (r.getMainEngineFlowConsInTons()!=0.0?r.getMainEngineFlowConsInTons()/60:0.0)* (r.getMainEngineMinuteLsdo()!=null?r.getMainEngineMinuteLsdo():0) );
//		   	        					else if(mfc.getTypeFuel().equals("LSFO")) cons = cons + ( (r.getMainEngineFlowConsInTons()!=0.0?r.getMainEngineFlowConsInTons()/60:0.0)* (r.getMainEngineMinuteLsfo()!=null?r.getMainEngineMinuteLsfo():0) );
//		   	        				}
//		   	        				else if(mfc.getMachinary().equals("DIESEL_GENERATOR")){
//		   	        					if(mfc.getTypeFuel().equals("HSFO")) cons = cons + ( (r.getDgsFlowConsInTons()!=0.0?r.getDgsFlowConsInTons()/60:0.0)* (r.getDieselGenMinuteHsfo()!=null?r.getDieselGenMinuteHsfo():0) );
//		   	        					else if(mfc.getTypeFuel().equals("HSDO")) cons = cons + ( (r.getDgsFlowConsInTons()!=0.0?r.getDgsFlowConsInTons()/60:0.0)* (r.getDieselGenMinuteHsdo()!=null?r.getDieselGenMinuteHsdo():0) );
//		   	        					else if(mfc.getTypeFuel().equals("LSDO")) cons = cons + ( (r.getDgsFlowConsInTons()!=0.0?r.getDgsFlowConsInTons()/60:0.0)* (r.getDieselGenMinuteLsdo()!=null?r.getDieselGenMinuteLsdo():0) );
//		   	        					else if(mfc.getTypeFuel().equals("LSFO")) cons = cons + ( (r.getDgsFlowConsInTons()!=0.0?r.getDgsFlowConsInTons()/60:0.0)* (r.getDieselGenMinuteLsfo()!=null?r.getDieselGenMinuteLsfo():0) );		   	        					
//		   	        				}
//		   	        				else if(mfc.getMachinary().equals("AUX_BOILER")){
//		   	        					if(mfc.getTypeFuel().equals("HSFO")) cons = cons + ( (r.getFm4InTons()!=0.0?r.getFm4InTons()/60:0.0)* (r.getAuxBoilerMinuteHsfo()!=null?r.getAuxBoilerMinuteHsfo():0) );
//		   	        					else if(mfc.getTypeFuel().equals("HSDO")) cons = cons + ( (r.getFm4InTons()!=0.0?r.getFm4InTons()/60:0.0)* (r.getAuxBoilerMinuteHsdo()!=null?r.getAuxBoilerMinuteHsdo():0) );
//		   	        					else if(mfc.getTypeFuel().equals("LSDO")) cons = cons + ( (r.getFm4InTons()!=0.0?r.getFm4InTons()/60:0.0)* (r.getAuxBoilerMinuteLsdo()!=null?r.getAuxBoilerMinuteLsdo():0) );
//		   	        					else if(mfc.getTypeFuel().equals("LSFO")) cons = cons + ( (r.getFm4InTons()!=0.0?r.getFm4InTons()/60:0.0)* (r.getAuxBoilerMinuteLsfo()!=null?r.getAuxBoilerMinuteLsfo():0) );		   	        					
//		   	        				}
//	        					
//		        					//CONSUMI per tutti i machinary che hanno quel fuel
//		        					if(mfc.getTypeFuel().equals("HSFO")){
//		        						consForTypeTotal = consForTypeTotal + ( (  (r.getMainEngineFlowConsInTons()+r.getDgsFlowConsInTons()+r.getFm4InTons())!=0.0?(r.getMainEngineFlowConsInTons()+r.getDgsFlowConsInTons()+r.getFm4InTons())/60:0.0)*
//		        								( (r.getMainEngineMinuteHsfo()!=null && r.getDieselGenMinuteHsfo()!=null && r.getAuxBoilerMinuteHsfo()!=null)?(r.getMainEngineMinuteHsfo()+r.getDieselGenMinuteHsfo()+r.getAuxBoilerMinuteHsfo() ):0) );
//		        					}
//		        					if(mfc.getTypeFuel().equals("HSDO")){
//		        						consForTypeTotal = consForTypeTotal + ( (  (r.getMainEngineFlowConsInTons()+r.getDgsFlowConsInTons()+r.getFm4InTons())!=0.0?(r.getMainEngineFlowConsInTons()+r.getDgsFlowConsInTons()+r.getFm4InTons())/60:0.0)*
//		        								( (r.getMainEngineMinuteHsdo()!=null && r.getDieselGenMinuteHsdo()!=null && r.getAuxBoilerMinuteHsdo()!=null)?(r.getMainEngineMinuteHsdo()+r.getDieselGenMinuteHsdo()+r.getAuxBoilerMinuteHsdo() ):0) );
//		        					}
//		        					if(mfc.getTypeFuel().equals("LSDO")){
//		        						consForTypeTotal = consForTypeTotal + ( (  (r.getMainEngineFlowConsInTons()+r.getDgsFlowConsInTons()+r.getFm4InTons())!=0.0?(r.getMainEngineFlowConsInTons()+r.getDgsFlowConsInTons()+r.getFm4InTons())/60:0.0)*
//		        								( (r.getMainEngineMinuteLsdo()!=null && r.getDieselGenMinuteLsdo()!=null && r.getAuxBoilerMinuteLsdo()!=null)?(r.getMainEngineMinuteLsdo()+r.getDieselGenMinuteLsdo()+r.getAuxBoilerMinuteLsdo() ):0) );
//		        					}
//		        					if(mfc.getTypeFuel().equals("LSFO")){
//		        						consForTypeTotal = consForTypeTotal + ( (  (r.getMainEngineFlowConsInTons()+r.getDgsFlowConsInTons()+r.getFm4InTons())!=0.0?(r.getMainEngineFlowConsInTons()+r.getDgsFlowConsInTons()+r.getFm4InTons())/60:0.0)*
//		        								( (r.getMainEngineMinuteLsfo()!=null && r.getDieselGenMinuteLsfo()!=null && r.getAuxBoilerMinuteLsfo()!=null)?(r.getMainEngineMinuteLsfo()+r.getDieselGenMinuteLsfo()+r.getAuxBoilerMinuteLsfo() ):0) );
//		        					}
//		        					
//		        					
//		        					if(r.getRegistrationDate().getTime() <= mfc.getVoyageCommenceDate().getTime()){
//		        						
//		        						if(mfc.getTypeFuel().equals("HSFO")){
//		        							consTotStartFuelToVoyage = consTotStartFuelToVoyage + ( (  (r.getMainEngineFlowConsInTons()+r.getDgsFlowConsInTons()+r.getFm4InTons())!=0.0?(r.getMainEngineFlowConsInTons()+r.getDgsFlowConsInTons()+r.getFm4InTons())/60:0.0)*
//			        								( (r.getMainEngineMinuteHsfo()!=null && r.getDieselGenMinuteHsfo()!=null && r.getAuxBoilerMinuteHsfo()!=null)?(r.getMainEngineMinuteHsfo()+r.getDieselGenMinuteHsfo()+r.getAuxBoilerMinuteHsfo() ):0) );
//			        					}
//			        					if(mfc.getTypeFuel().equals("HSDO")){
//			        						consTotStartFuelToVoyage = consTotStartFuelToVoyage + ( (  (r.getMainEngineFlowConsInTons()+r.getDgsFlowConsInTons()+r.getFm4InTons())!=0.0?(r.getMainEngineFlowConsInTons()+r.getDgsFlowConsInTons()+r.getFm4InTons())/60:0.0)*
//			        								( (r.getMainEngineMinuteHsdo()!=null && r.getDieselGenMinuteHsdo()!=null && r.getAuxBoilerMinuteHsdo()!=null)?(r.getMainEngineMinuteHsdo()+r.getDieselGenMinuteHsdo()+r.getAuxBoilerMinuteHsdo() ):0) );
//			        					}
//			        					if(mfc.getTypeFuel().equals("LSDO")){
//			        						consTotStartFuelToVoyage = consTotStartFuelToVoyage + ( (  (r.getMainEngineFlowConsInTons()+r.getDgsFlowConsInTons()+r.getFm4InTons())!=0.0?(r.getMainEngineFlowConsInTons()+r.getDgsFlowConsInTons()+r.getFm4InTons())/60:0.0)*
//			        								( (r.getMainEngineMinuteLsdo()!=null && r.getDieselGenMinuteLsdo()!=null && r.getAuxBoilerMinuteLsdo()!=null)?(r.getMainEngineMinuteLsdo()+r.getDieselGenMinuteLsdo()+r.getAuxBoilerMinuteLsdo() ):0) );
//			        					}
//			        					if(mfc.getTypeFuel().equals("LSFO")){
//			        						consTotStartFuelToVoyage = consTotStartFuelToVoyage + ( (  (r.getMainEngineFlowConsInTons()+r.getDgsFlowConsInTons()+r.getFm4InTons())!=0.0?(r.getMainEngineFlowConsInTons()+r.getDgsFlowConsInTons()+r.getFm4InTons())/60:0.0)*
//			        								( (r.getMainEngineMinuteLsfo()!=null && r.getDieselGenMinuteLsfo()!=null && r.getAuxBoilerMinuteLsfo()!=null)?(r.getMainEngineMinuteLsfo()+r.getDieselGenMinuteLsfo()+r.getAuxBoilerMinuteLsfo() ):0) );
//			        					}
//		        						
//		        					}
//	        					
//	        					}
//	        									
//		           			}
//  
//
//	        				else if (stopUseFuelSet){
//	        					if(r.getRegistrationDate().getTime() >= mfc.getStartUseFuel().getTime() &&
//	        					r.getRegistrationDate().getTime() <=mfc.getStopUseFuel().getTime()){
//	        						System.out.println("AC 07  + cons: " + cons +  " - RegDat " + r.getRegistrationDate() + " - ID " + r.getId() + " rpfc MEConsTons " + (r.getMainEngineFlowConsInTons()!=0.0?r.getMainEngineFlowConsInTons()/60:0.0) + 
//		   	        						" - rpfc dgConsTons " + (r.getDgsFlowConsInTons()!=0.0?r.getDgsFlowConsInTons()/60:0.0) + " - mfc.getTypeFuel(): " + mfc.getTypeFuel());
//	        						
//	        						//CONSUMI per quel machinary che ha quel fuel
//			        				if(mfc.getMachinary().equals("MAIN_ENGINE")){
//		   	        					if(mfc.getTypeFuel().equals("HSFO")) cons = cons + ( (r.getMainEngineFlowConsInTons()!=0.0?r.getMainEngineFlowConsInTons()/60:0.0)* (r.getMainEngineMinuteHsfo()!=null?r.getMainEngineMinuteHsfo():0) );
//		   	        					else if(mfc.getTypeFuel().equals("HSDO")) cons = cons + ( (r.getMainEngineFlowConsInTons()!=0.0?r.getMainEngineFlowConsInTons()/60:0.0)* (r.getMainEngineMinuteHsdo()!=null?r.getMainEngineMinuteHsdo():0) );
//		   	        					else if(mfc.getTypeFuel().equals("LSDO")) cons = cons + ( (r.getMainEngineFlowConsInTons()!=0.0?r.getMainEngineFlowConsInTons()/60:0.0)* (r.getMainEngineMinuteLsdo()!=null?r.getMainEngineMinuteLsdo():0) );
//		   	        					else if(mfc.getTypeFuel().equals("LSFO")) cons = cons + ( (r.getMainEngineFlowConsInTons()!=0.0?r.getMainEngineFlowConsInTons()/60:0.0)* (r.getMainEngineMinuteLsfo()!=null?r.getMainEngineMinuteLsfo():0) );
//		   	        				}
//		   	        				else if(mfc.getMachinary().equals("DIESEL_GENERATOR")){
//		   	        					if(mfc.getTypeFuel().equals("HSFO")) cons = cons + ( (r.getDgsFlowConsInTons()!=0.0?r.getDgsFlowConsInTons()/60:0.0)* (r.getDieselGenMinuteHsfo()!=null?r.getDieselGenMinuteHsfo():0) );
//		   	        					else if(mfc.getTypeFuel().equals("HSDO")) cons = cons + ( (r.getDgsFlowConsInTons()!=0.0?r.getDgsFlowConsInTons()/60:0.0)* (r.getDieselGenMinuteHsdo()!=null?r.getDieselGenMinuteHsdo():0) );
//		   	        					else if(mfc.getTypeFuel().equals("LSDO")) cons = cons + ( (r.getDgsFlowConsInTons()!=0.0?r.getDgsFlowConsInTons()/60:0.0)* (r.getDieselGenMinuteLsdo()!=null?r.getDieselGenMinuteLsdo():0) );
//		   	        					else if(mfc.getTypeFuel().equals("LSFO")) cons = cons + ( (r.getDgsFlowConsInTons()!=0.0?r.getDgsFlowConsInTons()/60:0.0)* (r.getDieselGenMinuteLsfo()!=null?r.getDieselGenMinuteLsfo():0) );
//		   	        				}
//		   	        				else if(mfc.getMachinary().equals("AUX_BOILER")){
//		   	        					if(mfc.getTypeFuel().equals("HSFO")) cons = cons + ( (r.getFm4InTons()!=0.0?r.getFm4InTons()/60:0.0)* (r.getAuxBoilerMinuteHsfo()!=null?r.getAuxBoilerMinuteHsfo():0) );
//		   	        					else if(mfc.getTypeFuel().equals("HSDO")) cons = cons + ( (r.getFm4InTons()!=0.0?r.getFm4InTons()/60:0.0)* (r.getAuxBoilerMinuteHsdo()!=null?r.getAuxBoilerMinuteHsdo():0) );
//		   	        					else if(mfc.getTypeFuel().equals("LSDO")) cons = cons + ( (r.getFm4InTons()!=0.0?r.getFm4InTons()/60:0.0)* (r.getAuxBoilerMinuteLsdo()!=null?r.getAuxBoilerMinuteLsdo():0) );
//		   	        					else if(mfc.getTypeFuel().equals("LSFO")) cons = cons + ( (r.getFm4InTons()!=0.0?r.getFm4InTons()/60:0.0)* (r.getAuxBoilerMinuteLsfo()!=null?r.getAuxBoilerMinuteLsfo():0) );		   	        					
//		   	        				}	        					
//	        					
//		        					//CONSUMI per tutti i machinary che hanno quel fuel
//		        					if(mfc.getTypeFuel().equals("HSFO")){
//		        						consForTypeTotal = consForTypeTotal + ( (  (r.getMainEngineFlowConsInTons()+r.getDgsFlowConsInTons()+r.getFm4InTons())!=0.0?(r.getMainEngineFlowConsInTons()+r.getDgsFlowConsInTons()+r.getFm4InTons())/60:0.0)*
//		        								( (r.getMainEngineMinuteHsfo()!=null && r.getDieselGenMinuteHsfo()!=null && r.getAuxBoilerMinuteHsfo()!=null)?(r.getMainEngineMinuteHsfo()+r.getDieselGenMinuteHsfo()+r.getAuxBoilerMinuteHsfo() ):0) );
//		        					}
//		        					if(mfc.getTypeFuel().equals("HSDO")){
//		        						consForTypeTotal = consForTypeTotal + ( (  (r.getMainEngineFlowConsInTons()+r.getDgsFlowConsInTons()+r.getFm4InTons())!=0.0?(r.getMainEngineFlowConsInTons()+r.getDgsFlowConsInTons()+r.getFm4InTons())/60:0.0)*
//		        								( (r.getMainEngineMinuteHsdo()!=null && r.getDieselGenMinuteHsdo()!=null && r.getAuxBoilerMinuteHsdo()!=null)?(r.getMainEngineMinuteHsdo()+r.getDieselGenMinuteHsdo()+r.getAuxBoilerMinuteHsdo() ):0) );
//		        					}
//		        					if(mfc.getTypeFuel().equals("LSDO")){
//		        						consForTypeTotal = consForTypeTotal + ( (  (r.getMainEngineFlowConsInTons()+r.getDgsFlowConsInTons()+r.getFm4InTons())!=0.0?(r.getMainEngineFlowConsInTons()+r.getDgsFlowConsInTons()+r.getFm4InTons())/60:0.0)*
//		        								( (r.getMainEngineMinuteLsdo()!=null && r.getDieselGenMinuteLsdo()!=null && r.getAuxBoilerMinuteLsdo()!=null)?(r.getMainEngineMinuteLsdo()+r.getDieselGenMinuteLsdo()+r.getAuxBoilerMinuteLsdo() ):0) );
//		        					}
//		        					if(mfc.getTypeFuel().equals("LSFO")){
//		        						consForTypeTotal = consForTypeTotal + ( (  (r.getMainEngineFlowConsInTons()+r.getDgsFlowConsInTons()+r.getFm4InTons())!=0.0?(r.getMainEngineFlowConsInTons()+r.getDgsFlowConsInTons()+r.getFm4InTons())/60:0.0)*
//		        								( (r.getMainEngineMinuteLsfo()!=null && r.getDieselGenMinuteLsfo()!=null && r.getAuxBoilerMinuteLsfo()!=null)?(r.getMainEngineMinuteLsfo()+r.getDieselGenMinuteLsfo()+r.getAuxBoilerMinuteLsfo() ):0) );
//		        					}
//		        					
//		        					if(r.getRegistrationDate().getTime() <= mfc.getVoyageCommenceDate().getTime()){
//		        						
//		        						if(mfc.getTypeFuel().equals("HSFO")){
//		        							consTotStartFuelToVoyage = consTotStartFuelToVoyage + ( (  (r.getMainEngineFlowConsInTons()+r.getDgsFlowConsInTons()+r.getFm4InTons())!=0.0?(r.getMainEngineFlowConsInTons()+r.getDgsFlowConsInTons()+r.getFm4InTons())/60:0.0)*
//			        								( (r.getMainEngineMinuteHsfo()!=null && r.getDieselGenMinuteHsfo()!=null && r.getAuxBoilerMinuteHsfo()!=null)?(r.getMainEngineMinuteHsfo()+r.getDieselGenMinuteHsfo()+r.getAuxBoilerMinuteHsfo() ):0) );
//			        					}
//			        					if(mfc.getTypeFuel().equals("HSDO")){
//			        						consTotStartFuelToVoyage = consTotStartFuelToVoyage + ( (  (r.getMainEngineFlowConsInTons()+r.getDgsFlowConsInTons()+r.getFm4InTons())!=0.0?(r.getMainEngineFlowConsInTons()+r.getDgsFlowConsInTons()+r.getFm4InTons())/60:0.0)*
//			        								( (r.getMainEngineMinuteHsdo()!=null && r.getDieselGenMinuteHsdo()!=null && r.getAuxBoilerMinuteHsdo()!=null)?(r.getMainEngineMinuteHsdo()+r.getDieselGenMinuteHsdo()+r.getAuxBoilerMinuteHsdo() ):0) );
//			        					}
//			        					if(mfc.getTypeFuel().equals("LSDO")){
//			        						consTotStartFuelToVoyage = consTotStartFuelToVoyage + ( (  (r.getMainEngineFlowConsInTons()+r.getDgsFlowConsInTons()+r.getFm4InTons())!=0.0?(r.getMainEngineFlowConsInTons()+r.getDgsFlowConsInTons()+r.getFm4InTons())/60:0.0)*
//			        								( (r.getMainEngineMinuteLsdo()!=null && r.getDieselGenMinuteLsdo()!=null && r.getAuxBoilerMinuteLsdo()!=null)?(r.getMainEngineMinuteLsdo()+r.getDieselGenMinuteLsdo()+r.getAuxBoilerMinuteLsdo() ):0) );
//			        					}
//			        					if(mfc.getTypeFuel().equals("LSFO")){
//			        						consTotStartFuelToVoyage = consTotStartFuelToVoyage + ( (  (r.getMainEngineFlowConsInTons()+r.getDgsFlowConsInTons()+r.getFm4InTons())!=0.0?(r.getMainEngineFlowConsInTons()+r.getDgsFlowConsInTons()+r.getFm4InTons())/60:0.0)*
//			        								( (r.getMainEngineMinuteLsfo()!=null && r.getDieselGenMinuteLsfo()!=null && r.getAuxBoilerMinuteLsfo()!=null)?(r.getMainEngineMinuteLsfo()+r.getDieselGenMinuteLsfo()+r.getAuxBoilerMinuteLsfo() ):0) );
//			        					}
//		        						
//		        					}
//	        					
//	        					}
//	        					
//	        				}
//		
//	        			}	
//        			
//	        	
//	        			
//        				mfc.setConsuption(cons);
//        			
//        				
//	        			//Calcolo il totale dei bunker per quel FuelType con regDate minore di stop
//	        			System.out.println("AC 17.0  bunk null: " + bunk==null + " ");
//	        			for (Bunkering b : bunk) {
//
//	        				System.out.println("AC 17.ii : stopUseFuelSet = " + stopUseFuelSet + " - b.getRegistrationDate().getTime() = " + b.getRegistrationDate().getTime() + " - EasyDateUtils.getGMT0CurrentDate().getTime() = " + 
//	        						EasyDateUtils.getGMT0CurrentDate().getTime() + " - b.getType() = " + b.getType() + " - mfc.getTypeFuel() = " + mfc.getTypeFuel()
//	        						+ "- b.getType().equals(mfc.getTypeFuel()) = " + b.getType().equals(mfc.getTypeFuel()) + " - b.getType()==mfc.getTypeFuel() =" + b.getType()==mfc.getTypeFuel()
//	        						+ " - b.getQuantityMt() = " + b.getQuantityMt() );
//	        				
//	        				
//		        				if(!stopUseFuelSet && b.getType().equals(mfc.getTypeFuel()) && b.getRegistrationDate().getTime() >= mfc.getStartUseFuel().getTime() )
//		           				{
//		   	        				System.out.println("AC 17 " + bunker + " bunker for : " + b.getType() + " - RegDat " + b.getRegistrationDate() + " - ID " + b.getId());
//		   	        				bunker = bunker + b.getQuantityMt();  
//		   	        				
//		   	        				if(b.getRegistrationDate().getTime() <= mfc.getVoyageCommenceDate().getTime()){
//		   	        					bunkerStartFuelToVoyage  = bunkerStartFuelToVoyage + b.getQuantityMt();
//		   	        				}
//		           				}  
//		        				     
//		           				if(stopUseFuelSet && b.getType().equals(mfc.getTypeFuel()) && b.getRegistrationDate().getTime() >= mfc.getStartUseFuel().getTime() && b.getRegistrationDate().getTime() <= mfc.getStopUseFuel().getTime())
//			       				{
//				        				System.out.println("AC 17a " + bunker + " bunker for : " + b.getType() + " - RegDat " + b.getRegistrationDate() + " - ID " + b.getId());
//				        				bunker = bunker + b.getQuantityMt();
//				        				
//				        				if(b.getRegistrationDate().getTime() <= mfc.getVoyageCommenceDate().getTime()){
//				        					bunkerStartFuelToVoyage  = bunkerStartFuelToVoyage + b.getQuantityMt();
//			   	        				}
//			       				}
//	        			
//        					
//	        				}
//		        				
//	        					
//	        					
//
//	        					
//	        					
//	        					
//	        					
//	        					
//	        					
////	        					//caso start voyage dopo di start fuel
////		        				if(mfc.getStartUseFuel().getTime() <= mfc.getVoyageCommenceDate().getTime()){
////			        				if(!stopUseFuelSet && b.getType().equals(mfc.getTypeFuel()) && b.getRegistrationDate().getTime() >= mfc.getStartUseFuel().getTime() )
////			           				{
////			   	        				System.out.println("AC 17 " + bunker + " bunker for : " + b.getType() + " - RegDat " + b.getRegistrationDate() + " - ID " + b.getId());
////			   	        				bunker = bunker + b.getQuantityMt();  
////			   	        				
////			   	        				if(b.getRegistrationDate().getTime() <= mfc.getVoyageCommenceDate().getTime()){
////			   	        					bunkerStartFuelToVoyage  = bunkerStartFuelToVoyage + b.getQuantityMt();
////			   	        				}
////			           				}  
////			        				
////			        				
////			        				//if (stopUseFuelSet && b.getRegistrationDate().getTime() <= mfc.getStopUseFuel().getTime() && b.getType().equals(mfc.getTypeFuel()))     
////			           				if(stopUseFuelSet && b.getType().equals(mfc.getTypeFuel()) && b.getRegistrationDate().getTime() >= mfc.getStartUseFuel().getTime() && b.getRegistrationDate().getTime() <= mfc.getStopUseFuel().getTime())
////				       				{
////					        				System.out.println("AC 17a " + bunker + " bunker for : " + b.getType() + " - RegDat " + b.getRegistrationDate() + " - ID " + b.getId());
////					        				bunker = bunker + b.getQuantityMt();
////					        				
////					        				if(b.getRegistrationDate().getTime() <= mfc.getVoyageCommenceDate().getTime()){
////					        					bunkerStartFuelToVoyage  = bunkerStartFuelToVoyage + b.getQuantityMt();
////				   	        				}
////				       				}
////		        				}
////		        				//caso start voyage prima di start fuel
////		        				else{
////		        					if(!stopUseFuelSet && b.getType().equals(mfc.getTypeFuel())){
////			        					if(b.getRegistrationDate().getTime() >= mfc.getVoyageCommenceDate().getTime() )
////				           				{
////			        						bunkerVoyageToStoptFuel  = bunkerVoyageToStoptFuel + b.getQuantityMt();
////			        						
////				   	        				System.out.println("AC 17 " + bunker + " bunker for : " + b.getType() + " - RegDat " + b.getRegistrationDate() + " - ID " + b.getId());
////				   	        				bunker = bunker + b.getQuantityMt();  
////				   	        				
////				   	        				if(b.getRegistrationDate().getTime() <= mfc.getVoyageCommenceDate().getTime()){
////				   	        					bunkerStartFuelToVoyage  = bunkerStartFuelToVoyage + b.getQuantityMt();
////				   	        				}
////				           				}  
////		        					}
////			        				
////		        					
////		        					
//////		        					if(!stopUseFuelSet && b.getType().equals(mfc.getTypeFuel())){
//////			        					if(b.getRegistrationDate().getTime() >= mfc.getStartUseFuel().getTime() )
//////				           				{
//////				   	        				System.out.println("AC 17 " + bunker + " bunker for : " + b.getType() + " - RegDat " + b.getRegistrationDate() + " - ID " + b.getId());
//////				   	        				bunker = bunker + b.getQuantityMt();  
//////				   	        				
//////				   	        				if(b.getRegistrationDate().getTime() <= mfc.getVoyageCommenceDate().getTime()){
//////				   	        					bunkerStartFuelToVoyage  = bunkerStartFuelToVoyage + b.getQuantityMt();
//////				   	        				}
//////				           				}  
//////		        					}
//		        					
//		        					
//		        					
//		        					
//
//	        			}
//	        		
//        			
//			
//        			
//	        			 System.out.println("AC 04.g");
//	
//	        			 
//	
//	        			//Se la data di stop � diversa da null allora non deve pi� calcolarlo -> setto cansCalculated true 
//	        			if(stopUseFuelSet)	mfc.setConsCalculated(true); 
//	        				       			
//	        			
//	        			if(mfc.getTypeFuel().equals("HSFO")) robPerFuel = (mfc.getVoyDepartureTotalHsfo()!=null?mfc.getVoyDepartureTotalHsfo():0.0);
//	        			if(mfc.getTypeFuel().equals("HSDO")) robPerFuel = (mfc.getVoyDepartureTotalHsdo()!=null?mfc.getVoyDepartureTotalHsdo():0.0);
//	        			if(mfc.getTypeFuel().equals("LSFO")) robPerFuel = (mfc.getVoyDepartureTotalLsfo()!=null?mfc.getVoyDepartureTotalLsfo():0.0);
//	        			if(mfc.getTypeFuel().equals("LSDO")) robPerFuel = (mfc.getVoyDepartureTotalLsdo()!=null?mfc.getVoyDepartureTotalLsdo():0.0);
//	        			
//	        			
//	        			if(mfc.getStartUseFuel().getTime() <= mfc.getVoyageCommenceDate().getTime()){
//	        				//Qta allo start
//		        			mfc.setQob(robPerFuel - bunkerStartFuelToVoyage - consTotStartFuelToVoyage );
//		        			
//		        			//Qta allo stop
//		        			mfc.setRob(robPerFuel + bunker - bunkerStartFuelToVoyage - consForTypeTotal + consTotStartFuelToVoyage);
//	        			}
//	        			else{
//	        				//Qta allo start
//		        			mfc.setQob(robPerFuel - bunkerStartFuelToVoyage - consTotStartFuelToVoyage );
//		        			
//		        			//Qta allo stop
//		        			mfc.setRob(robPerFuel + bunker - bunkerStartFuelToVoyage - consForTypeTotal + consTotStartFuelToVoyage);
//	        			}
//	        			
//	        			
//	        			
//	        			
//	        			System.out.println("AC 10 - Consumi cons di mfc.getMachinary() " + mfc.getMachinary() + " = " + cons);
//	        			
//	        			System.out.println("AC 15 - Consumi robPerFuel di mfc.getMachinary() " + mfc.getMachinary() + " = " + robPerFuel);
//	        			
//	        			System.out.println("AC 20 - bunker per mfc.getTypeFuel() " + mfc.getTypeFuel() + " = " + bunker);
//	        		
//	        			System.out.println("AC 30 - bunker per mfc.isConsCalculated() " + mfc.isConsCalculated());
//        			}
//        			
//
//        			stopUseFuelSet = true;
//        			
//        			
//    			}	
//        		
//        	
//	   			
//    		}
//    		
//    		System.out.println("AC 10 ");
//  		
//    		
//    	} catch (Exception e1) {
//            e1.printStackTrace();
//            System.out.println("Ex. calcolo consumi dei machinary in bunker status");
//        }   
//    	
//    	
//    	System.out.println("************* Calcolo i consumi del bunker status dei diversi machinary ********END********.");
//    	
//    	return machinaryFuelCalc;
//    	
//	}
	
	
	public List<MachineriesBunkerStatus> calculateComsuptionForMachinery(List<MachineriesBunkerStatus> machinaryFuelCalc, List<RoutePoint> rpo, List<Bunkering> bunk){
		
       	//calcolo i consumi dei diversi machinery per type of fuel dove calcCons=false e lo scrivo nelle relative righe
    	System.out.println("************* Calcolo i consumi del bunker status dei diversi machinary ********BGN********.");
    	
    	try 
    	{ 	
    		
    		boolean stopUseFuelSet = true;
    		double robPerFuel = 0.0;
    		
    		//Consuption
    		double cons = 0.0;
    		double consStartStop = 0.0;
    		double consStartFuelToVoyage = 0.0;
    		double consVoyageToStopFuel = 0.0;
    		
    		 //Bunker 
    		double bunkerToAdd = 0.0;
    		double bunkerToSubs = 0.0;
    		
    		double bunkerStartStop = 0.0;  
    		double bunkerStartFuelToVoyage = 0.0;
    		double bunkerVoyageToStopFuel = 0.0;
    		
    		System.out.println("AC 01 ");        		    		
    		System.out.println("AC 02 - machinaryFuelCalc null: " + machinaryFuelCalc==null + " ");
    		System.out.println("AC 03 - machinaryFuelCalc.size(): " + machinaryFuelCalc.size());
    		
    		if(machinaryFuelCalc!=null){
        		for (MachineriesBunkerStatus mfc : machinaryFuelCalc) {
    		   		
        			System.out.println("AC 04 - mfc: " + mfc.toString());
        			stopUseFuelSet = true; 
        			robPerFuel = 0.0;
        			
        			cons = 0; 
        			
        			bunkerStartStop = 0;
        			bunkerStartFuelToVoyage = 0.0;
        			bunkerVoyageToStopFuel = 0.0;
        			
        			consVoyageToStopFuel = 0.0;
        			consStartStop = 0.0;
        			
        			consStartFuelToVoyage = 0.0;
        			
        			
        			try{	mfc.getStopUseFuel().equals(null); }
        			catch(Exception ex){
        				stopUseFuelSet = false; }	
        			
        			
        			if (!mfc.isConsCalculated()){     				        				
	        			//Calcolo i consumi per quel Machinary con regDate tra lo star e stop
        				//consideraro anche il type
	        			for (RoutePoint r : rpo) {        				
	        				
	        				/*****************************************   CONSUMI    ****************************BGN****/
	        				//Abbiamo 2 casi:
	        				//CASO 1: START MACHINARY -> START VOYAGE(ROB) -> STOP MACHINARY
	        				//CASO 2: START VOYAGE(ROB) -> START MACHINARY -> STOP MACHINARY
	        				
	        				if(!stopUseFuelSet){
//	        					&& r.getRegistrationDate().getTime() >= mfc.getStartUseFuel().getTime() ){
	        				
		            		        System.out.println("AC 04.f - getStopUseFuel e null -> calcolo sino alla data odierna");
	
		   	        				System.out.println("AC 07  + cons: " + cons +  " - RegDat " + r.getRegistrationDate() + " - ID " + r.getId() + " rpfc MEConsTons " + (r.getMainEngineFlowConsInTons()!=0.0?r.getMainEngineFlowConsInTons()/60:0.0) + 
		   	        						" - rpfc dgConsTons " + (r.getDgsFlowConsInTons()!=0.0?r.getDgsFlowConsInTons()/60:0.0) + " - mfc.getTypeFuel(): " + mfc.getTypeFuel());
		   	        				
		   	        				if(r.getRegistrationDate().getTime() >= mfc.getStartUseFuel().getTime() ){
		   	        				
			   	        				//CONSUMI per quel machinary che ha quel fuel
		   	        					if(mfc.getMachinary().equals("MAIN_ENGINE")){
			   	        					if(mfc.getTypeFuel().equals("HFO")) cons = cons + ( (r.getMainEngineFlowConsInTons()!=0.0?r.getMainEngineFlowConsInTons()/60:0.0)* (r.getMainEngineMinuteHfo()!=null?r.getMainEngineMinuteHfo():0) );
			   	        					else if(mfc.getTypeFuel().equals("LFO")) cons = cons + ( (r.getMainEngineFlowConsInTons()!=0.0?r.getMainEngineFlowConsInTons()/60:0.0)* (r.getMainEngineMinuteLfo()!=null?r.getMainEngineMinuteLfo():0) );
			   	        					else if(mfc.getTypeFuel().equals("MDO")) cons = cons + ( (r.getMainEngineFlowConsInTons()!=0.0?r.getMainEngineFlowConsInTons()/60:0.0)* (r.getMainEngineMinuteMdo()!=null?r.getMainEngineMinuteMdo():0) );		   	        								   	        					
			   	        				}
			   	        				else if(mfc.getMachinary().equals("DIESEL_GENERATOR")){
			   	        					if(mfc.getTypeFuel().equals("HFO")) cons = cons + ( (r.getDgsFlowConsInTons()!=0.0?r.getDgsFlowConsInTons()/60:0.0)* (r.getDieselGenMinuteHfo()!=null?r.getDieselGenMinuteHfo():0) );
			   	        					else if(mfc.getTypeFuel().equals("LFO")) cons = cons + ( (r.getDgsFlowConsInTons()!=0.0?r.getDgsFlowConsInTons()/60:0.0)* (r.getDieselGenMinuteLfo()!=null?r.getDieselGenMinuteLfo():0) );
			   	        					else if(mfc.getTypeFuel().equals("MDO")) cons = cons + ( (r.getDgsFlowConsInTons()!=0.0?r.getDgsFlowConsInTons()/60:0.0)* (r.getDieselGenMinuteMdo()!=null?r.getDieselGenMinuteMdo():0) );		   	        							   	        					
			   	        				}
			   	        				else if(mfc.getMachinary().equals("AUX_BOILER")){
			   	        					if(mfc.getTypeFuel().equals("HFO")) cons = cons + ( (r.getFm4InTons()!=0.0?r.getFm4InTons()/60:0.0)* (r.getAuxBoilerMinuteHfo()!=null?r.getAuxBoilerMinuteHfo():0) );
			   	        					else if(mfc.getTypeFuel().equals("LFO")) cons = cons + ( (r.getFm4InTons()!=0.0?r.getFm4InTons()/60:0.0)* (r.getAuxBoilerMinuteLfo()!=null?r.getAuxBoilerMinuteLfo():0) );
			   	        					else if(mfc.getTypeFuel().equals("MDO")) cons = cons + ( (r.getFm4InTons()!=0.0?r.getFm4InTons()/60:0.0)* (r.getAuxBoilerMinuteMdo()!=null?r.getAuxBoilerMinuteMdo():0) );
			   	        				}
		   	        					
		   	        					
		   	        					
		   	        					//cambiati per cambio tipo bunker da hsfo,lsfo,hsdo,lsdo a lfo,hfo,mdo
//			   	        				if(mfc.getMachinary().equals("MAIN_ENGINE")){
//			   	        					if(mfc.getTypeFuel().equals("HSFO")) cons = cons + ( (r.getMainEngineFlowConsInTons()!=0.0?r.getMainEngineFlowConsInTons()/60:0.0)* (r.getMainEngineMinuteHsfo()!=null?r.getMainEngineMinuteHsfo():0) );
//			   	        					else if(mfc.getTypeFuel().equals("HSDO")) cons = cons + ( (r.getMainEngineFlowConsInTons()!=0.0?r.getMainEngineFlowConsInTons()/60:0.0)* (r.getMainEngineMinuteHsdo()!=null?r.getMainEngineMinuteHsdo():0) );
//			   	        					else if(mfc.getTypeFuel().equals("LSDO")) cons = cons + ( (r.getMainEngineFlowConsInTons()!=0.0?r.getMainEngineFlowConsInTons()/60:0.0)* (r.getMainEngineMinuteLsdo()!=null?r.getMainEngineMinuteLsdo():0) );
//			   	        					else if(mfc.getTypeFuel().equals("LSFO")) cons = cons + ( (r.getMainEngineFlowConsInTons()!=0.0?r.getMainEngineFlowConsInTons()/60:0.0)* (r.getMainEngineMinuteLsfo()!=null?r.getMainEngineMinuteLsfo():0) );
//			   	        				}
//			   	        				else if(mfc.getMachinary().equals("DIESEL_GENERATOR")){
//			   	        					if(mfc.getTypeFuel().equals("HSFO")) cons = cons + ( (r.getDgsFlowConsInTons()!=0.0?r.getDgsFlowConsInTons()/60:0.0)* (r.getDieselGenMinuteHsfo()!=null?r.getDieselGenMinuteHsfo():0) );
//			   	        					else if(mfc.getTypeFuel().equals("HSDO")) cons = cons + ( (r.getDgsFlowConsInTons()!=0.0?r.getDgsFlowConsInTons()/60:0.0)* (r.getDieselGenMinuteHsdo()!=null?r.getDieselGenMinuteHsdo():0) );
//			   	        					else if(mfc.getTypeFuel().equals("LSDO")) cons = cons + ( (r.getDgsFlowConsInTons()!=0.0?r.getDgsFlowConsInTons()/60:0.0)* (r.getDieselGenMinuteLsdo()!=null?r.getDieselGenMinuteLsdo():0) );
//			   	        					else if(mfc.getTypeFuel().equals("LSFO")) cons = cons + ( (r.getDgsFlowConsInTons()!=0.0?r.getDgsFlowConsInTons()/60:0.0)* (r.getDieselGenMinuteLsfo()!=null?r.getDieselGenMinuteLsfo():0) );		   	        					
//			   	        				}
//			   	        				else if(mfc.getMachinary().equals("AUX_BOILER")){
//			   	        					if(mfc.getTypeFuel().equals("HSFO")) cons = cons + ( (r.getFm4InTons()!=0.0?r.getFm4InTons()/60:0.0)* (r.getAuxBoilerMinuteHsfo()!=null?r.getAuxBoilerMinuteHsfo():0) );
//			   	        					else if(mfc.getTypeFuel().equals("HSDO")) cons = cons + ( (r.getFm4InTons()!=0.0?r.getFm4InTons()/60:0.0)* (r.getAuxBoilerMinuteHsdo()!=null?r.getAuxBoilerMinuteHsdo():0) );
//			   	        					else if(mfc.getTypeFuel().equals("LSDO")) cons = cons + ( (r.getFm4InTons()!=0.0?r.getFm4InTons()/60:0.0)* (r.getAuxBoilerMinuteLsdo()!=null?r.getAuxBoilerMinuteLsdo():0) );
//			   	        					else if(mfc.getTypeFuel().equals("LSFO")) cons = cons + ( (r.getFm4InTons()!=0.0?r.getFm4InTons()/60:0.0)* (r.getAuxBoilerMinuteLsfo()!=null?r.getAuxBoilerMinuteLsfo():0) );		   	        					
//			   	        				}		        					
//		   	        					if(mfc.getTypeFuel().equals("HSFO")) consStartStop = consStartStop + getTotHsfo(r);
//		        						else if(mfc.getTypeFuel().equals("HSDO")) consStartStop = consStartStop + getTotHsdo(r); 						   
//		        						else if(mfc.getTypeFuel().equals("LSDO")) consStartStop = consStartStop + getTotLsdo(r);
//		        						else if(mfc.getTypeFuel().equals("LSFO")) consStartStop = consStartStop + getTotLsfo(r);
		   	        					
			        					//CONSUMI per tutti i machinary che hanno quel fuel
			   	        				if(mfc.getTypeFuel().equals("HFO")) consStartStop = consStartStop + getTotHfo(r);
			   	        				else if(mfc.getTypeFuel().equals("LFO")) consStartStop = consStartStop + getTotLfo(r);
		        						else if(mfc.getTypeFuel().equals("MDO")) consStartStop = consStartStop + getTotMdo(r); 						   		        						
			   	        				
		   	        				}
		   	        				
		   	        				//CASO 1
		   	        				if(mfc.getStartUseFuel().getTime()  <= mfc.getVoyageCommenceDate().getTime()
	        								&& r.getRegistrationDate().getTime() >= mfc.getStartUseFuel().getTime() && r.getRegistrationDate().getTime() <= mfc.getVoyageCommenceDate().getTime() ){
			        						if(mfc.getTypeFuel().equals("HFO")) consStartFuelToVoyage = consStartFuelToVoyage + getTotHfo(r);
			        						else if(mfc.getTypeFuel().equals("LFO")) consStartFuelToVoyage = consStartFuelToVoyage + getTotLfo(r);
			        						else if(mfc.getTypeFuel().equals("MDO")) consStartFuelToVoyage = consStartFuelToVoyage + getTotMdo(r); 						   		        							
			        				}
		   	        				//CASO 2
		   	        				else if(mfc.getStartUseFuel().getTime()  > mfc.getVoyageCommenceDate().getTime() 
		        							&&  r.getRegistrationDate().getTime() > mfc.getVoyageCommenceDate().getTime()){
		        						if(mfc.getTypeFuel().equals("HFO")) consVoyageToStopFuel = consVoyageToStopFuel + getTotHfo(r);
		        						else if(mfc.getTypeFuel().equals("LFO")) consVoyageToStopFuel = consVoyageToStopFuel + getTotLfo(r);
		        						else if(mfc.getTypeFuel().equals("MDO")) consVoyageToStopFuel = consVoyageToStopFuel + getTotMdo(r); 						   	        						
	        						}
			        				
		   	        				//cambiati per cambio tipo bunker da hsfo,lsfo,hsdo,lsdo a lfo,hfo,mdo
//	   	        					//CASO 1
//		   	        				if(mfc.getStartUseFuel().getTime()  <= mfc.getVoyageCommenceDate().getTime()
//	        								&& r.getRegistrationDate().getTime() >= mfc.getStartUseFuel().getTime() && r.getRegistrationDate().getTime() <= mfc.getVoyageCommenceDate().getTime() ){
//			        						if(mfc.getTypeFuel().equals("HSFO")) consStartFuelToVoyage = consStartFuelToVoyage + getTotHsfo(r);
//			        						else if(mfc.getTypeFuel().equals("HSDO")) consStartFuelToVoyage = consStartFuelToVoyage + getTotHsdo(r); 						   
//			        						else if(mfc.getTypeFuel().equals("LSDO")) consStartFuelToVoyage = consStartFuelToVoyage + getTotLsdo(r);
//			        						else if(mfc.getTypeFuel().equals("LSFO")) consStartFuelToVoyage = consStartFuelToVoyage + getTotLsfo(r);		        							
//			        				}
//		   	        				//CASO 2
//		   	        				else if(mfc.getStartUseFuel().getTime()  > mfc.getVoyageCommenceDate().getTime() 
//		        							&&  r.getRegistrationDate().getTime() > mfc.getVoyageCommenceDate().getTime()){
//		        						if(mfc.getTypeFuel().equals("HSFO")) consVoyageToStopFuel = consVoyageToStopFuel + getTotHsfo(r);
//		        						else if(mfc.getTypeFuel().equals("HSDO")) consVoyageToStopFuel = consVoyageToStopFuel + getTotHsdo(r); 						   
//		        						else if(mfc.getTypeFuel().equals("LSDO")) consVoyageToStopFuel = consVoyageToStopFuel + getTotLsdo(r);
//		        						else if(mfc.getTypeFuel().equals("LSFO")) consVoyageToStopFuel = consVoyageToStopFuel + getTotLsfo(r);		        						
//	        						}
		   	 					
		   	        		
				
		           			}
  
	        				else if(stopUseFuelSet){
	        						System.out.println("AC 07  + cons: " + cons +  " - RegDat " + r.getRegistrationDate() + " - ID " + r.getId() + " rpfc MEConsTons " + (r.getMainEngineFlowConsInTons()!=0.0?r.getMainEngineFlowConsInTons()/60:0.0) + 
		   	        						" - rpfc dgConsTons " + (r.getDgsFlowConsInTons()!=0.0?r.getDgsFlowConsInTons()/60:0.0) + " - mfc.getTypeFuel(): " + mfc.getTypeFuel());
		   	        				
	        						if(r.getRegistrationDate().getTime() >= mfc.getStartUseFuel().getTime() && r.getRegistrationDate().getTime() <= mfc.getStopUseFuel().getTime()){
				        				
	        							
	        							
	        							//CONSUMI per quel machinary che ha quel fuel
		   	        					if(mfc.getMachinary().equals("MAIN_ENGINE")){
			   	        					if(mfc.getTypeFuel().equals("HFO")) cons = cons + ( (r.getMainEngineFlowConsInTons()!=0.0?r.getMainEngineFlowConsInTons()/60:0.0)* (r.getMainEngineMinuteHfo()!=null?r.getMainEngineMinuteHfo():0) );
			   	        					else if(mfc.getTypeFuel().equals("LFO")) cons = cons + ( (r.getMainEngineFlowConsInTons()!=0.0?r.getMainEngineFlowConsInTons()/60:0.0)* (r.getMainEngineMinuteLfo()!=null?r.getMainEngineMinuteLfo():0) );
			   	        					else if(mfc.getTypeFuel().equals("MDO")) cons = cons + ( (r.getMainEngineFlowConsInTons()!=0.0?r.getMainEngineFlowConsInTons()/60:0.0)* (r.getMainEngineMinuteMdo()!=null?r.getMainEngineMinuteMdo():0) );		   	        								   	        					
			   	        				}
			   	        				else if(mfc.getMachinary().equals("DIESEL_GENERATOR")){
			   	        					if(mfc.getTypeFuel().equals("HFO")) cons = cons + ( (r.getDgsFlowConsInTons()!=0.0?r.getDgsFlowConsInTons()/60:0.0)* (r.getDieselGenMinuteHfo()!=null?r.getDieselGenMinuteHfo():0) );
			   	        					else if(mfc.getTypeFuel().equals("LFO")) cons = cons + ( (r.getDgsFlowConsInTons()!=0.0?r.getDgsFlowConsInTons()/60:0.0)* (r.getDieselGenMinuteLfo()!=null?r.getDieselGenMinuteLfo():0) );
			   	        					else if(mfc.getTypeFuel().equals("MDO")) cons = cons + ( (r.getDgsFlowConsInTons()!=0.0?r.getDgsFlowConsInTons()/60:0.0)* (r.getDieselGenMinuteMdo()!=null?r.getDieselGenMinuteMdo():0) );		   	        							   	        					
			   	        				}
			   	        				else if(mfc.getMachinary().equals("AUX_BOILER")){
			   	        					if(mfc.getTypeFuel().equals("HFO")) cons = cons + ( (r.getFm4InTons()!=0.0?r.getFm4InTons()/60:0.0)* (r.getAuxBoilerMinuteHfo()!=null?r.getAuxBoilerMinuteHfo():0) );
			   	        					else if(mfc.getTypeFuel().equals("LFO")) cons = cons + ( (r.getFm4InTons()!=0.0?r.getFm4InTons()/60:0.0)* (r.getAuxBoilerMinuteLfo()!=null?r.getAuxBoilerMinuteLfo():0) );
			   	        					else if(mfc.getTypeFuel().equals("MDO")) cons = cons + ( (r.getFm4InTons()!=0.0?r.getFm4InTons()/60:0.0)* (r.getAuxBoilerMinuteMdo()!=null?r.getAuxBoilerMinuteMdo():0) );
			   	        				}
		   	        					
		   	        					
		   	        					
		   	        					//cambiati per cambio tipo bunker da hsfo,lsfo,hsdo,lsdo a lfo,hfo,mdo
//			   	        				if(mfc.getMachinary().equals("MAIN_ENGINE")){
//			   	        					if(mfc.getTypeFuel().equals("HSFO")) cons = cons + ( (r.getMainEngineFlowConsInTons()!=0.0?r.getMainEngineFlowConsInTons()/60:0.0)* (r.getMainEngineMinuteHsfo()!=null?r.getMainEngineMinuteHsfo():0) );
//			   	        					else if(mfc.getTypeFuel().equals("HSDO")) cons = cons + ( (r.getMainEngineFlowConsInTons()!=0.0?r.getMainEngineFlowConsInTons()/60:0.0)* (r.getMainEngineMinuteHsdo()!=null?r.getMainEngineMinuteHsdo():0) );
//			   	        					else if(mfc.getTypeFuel().equals("LSDO")) cons = cons + ( (r.getMainEngineFlowConsInTons()!=0.0?r.getMainEngineFlowConsInTons()/60:0.0)* (r.getMainEngineMinuteLsdo()!=null?r.getMainEngineMinuteLsdo():0) );
//			   	        					else if(mfc.getTypeFuel().equals("LSFO")) cons = cons + ( (r.getMainEngineFlowConsInTons()!=0.0?r.getMainEngineFlowConsInTons()/60:0.0)* (r.getMainEngineMinuteLsfo()!=null?r.getMainEngineMinuteLsfo():0) );
//			   	        				}
//			   	        				else if(mfc.getMachinary().equals("DIESEL_GENERATOR")){
//			   	        					if(mfc.getTypeFuel().equals("HSFO")) cons = cons + ( (r.getDgsFlowConsInTons()!=0.0?r.getDgsFlowConsInTons()/60:0.0)* (r.getDieselGenMinuteHsfo()!=null?r.getDieselGenMinuteHsfo():0) );
//			   	        					else if(mfc.getTypeFuel().equals("HSDO")) cons = cons + ( (r.getDgsFlowConsInTons()!=0.0?r.getDgsFlowConsInTons()/60:0.0)* (r.getDieselGenMinuteHsdo()!=null?r.getDieselGenMinuteHsdo():0) );
//			   	        					else if(mfc.getTypeFuel().equals("LSDO")) cons = cons + ( (r.getDgsFlowConsInTons()!=0.0?r.getDgsFlowConsInTons()/60:0.0)* (r.getDieselGenMinuteLsdo()!=null?r.getDieselGenMinuteLsdo():0) );
//			   	        					else if(mfc.getTypeFuel().equals("LSFO")) cons = cons + ( (r.getDgsFlowConsInTons()!=0.0?r.getDgsFlowConsInTons()/60:0.0)* (r.getDieselGenMinuteLsfo()!=null?r.getDieselGenMinuteLsfo():0) );		   	        					
//			   	        				}
//			   	        				else if(mfc.getMachinary().equals("AUX_BOILER")){
//			   	        					if(mfc.getTypeFuel().equals("HSFO")) cons = cons + ( (r.getFm4InTons()!=0.0?r.getFm4InTons()/60:0.0)* (r.getAuxBoilerMinuteHsfo()!=null?r.getAuxBoilerMinuteHsfo():0) );
//			   	        					else if(mfc.getTypeFuel().equals("HSDO")) cons = cons + ( (r.getFm4InTons()!=0.0?r.getFm4InTons()/60:0.0)* (r.getAuxBoilerMinuteHsdo()!=null?r.getAuxBoilerMinuteHsdo():0) );
//			   	        					else if(mfc.getTypeFuel().equals("LSDO")) cons = cons + ( (r.getFm4InTons()!=0.0?r.getFm4InTons()/60:0.0)* (r.getAuxBoilerMinuteLsdo()!=null?r.getAuxBoilerMinuteLsdo():0) );
//			   	        					else if(mfc.getTypeFuel().equals("LSFO")) cons = cons + ( (r.getFm4InTons()!=0.0?r.getFm4InTons()/60:0.0)* (r.getAuxBoilerMinuteLsfo()!=null?r.getAuxBoilerMinuteLsfo():0) );		   	        					
//			   	        				}		        					
//		   	        					if(mfc.getTypeFuel().equals("HSFO")) consStartStop = consStartStop + getTotHsfo(r);
//		        						else if(mfc.getTypeFuel().equals("HSDO")) consStartStop = consStartStop + getTotHsdo(r); 						   
//		        						else if(mfc.getTypeFuel().equals("LSDO")) consStartStop = consStartStop + getTotLsdo(r);
//		        						else if(mfc.getTypeFuel().equals("LSFO")) consStartStop = consStartStop + getTotLsfo(r);
		   	        					
			        					//CONSUMI per tutti i machinary che hanno quel fuel
			   	        				if(mfc.getTypeFuel().equals("HFO")) consStartStop = consStartStop + getTotHfo(r);
			   	        				else if(mfc.getTypeFuel().equals("LFO")) consStartStop = consStartStop + getTotLfo(r);
		        						else if(mfc.getTypeFuel().equals("MDO")) consStartStop = consStartStop + getTotMdo(r); 	
			   	        					

	        						}	

	        						
	        						//CASO 1
	        						if(mfc.getStartUseFuel().getTime()  <= mfc.getVoyageCommenceDate().getTime()
	        								&& r.getRegistrationDate().getTime() >= mfc.getStartUseFuel().getTime() && r.getRegistrationDate().getTime() <= mfc.getVoyageCommenceDate().getTime() ){	        						
		        						if(mfc.getTypeFuel().equals("HFO")) consStartFuelToVoyage = consStartFuelToVoyage + getTotHfo(r);
		        						else if(mfc.getTypeFuel().equals("LFO")) consStartFuelToVoyage = consStartFuelToVoyage + getTotLfo(r);
		        						else if(mfc.getTypeFuel().equals("MDO")) consStartFuelToVoyage = consStartFuelToVoyage + getTotMdo(r); 						   		        								        						
		        					}
	        						//CASO 2
		        					else if(mfc.getStartUseFuel().getTime()  > mfc.getVoyageCommenceDate().getTime() 
		        							&&  r.getRegistrationDate().getTime() > mfc.getVoyageCommenceDate().getTime() && r.getRegistrationDate().getTime() < mfc.getStopUseFuel().getTime() ){	        						
		        						if(mfc.getTypeFuel().equals("HFO")) consVoyageToStopFuel = consVoyageToStopFuel + getTotHfo(r);
		        						else if(mfc.getTypeFuel().equals("LFO")) consVoyageToStopFuel = consVoyageToStopFuel + getTotLfo(r);
		        						else if(mfc.getTypeFuel().equals("MDO")) consVoyageToStopFuel = consVoyageToStopFuel + getTotMdo(r); 						   
	        						}
	        						
	        						
	        						//cambiati per cambio tipo bunker da hsfo,lsfo,hsdo,lsdo a lfo,hfo,mdo
			        				//CASO 1
//	        						if(mfc.getStartUseFuel().getTime()  <= mfc.getVoyageCommenceDate().getTime()
//	        								&& r.getRegistrationDate().getTime() >= mfc.getStartUseFuel().getTime() && r.getRegistrationDate().getTime() <= mfc.getVoyageCommenceDate().getTime() ){	        						
//		        						if(mfc.getTypeFuel().equals("HSFO")) consStartFuelToVoyage = consStartFuelToVoyage + getTotHsfo(r);
//		        						else if(mfc.getTypeFuel().equals("HSDO")) consStartFuelToVoyage = consStartFuelToVoyage + getTotHsdo(r); 						   
//		        						else if(mfc.getTypeFuel().equals("LSDO")) consStartFuelToVoyage = consStartFuelToVoyage + getTotLsdo(r);
//		        						else if(mfc.getTypeFuel().equals("LSFO")) consStartFuelToVoyage = consStartFuelToVoyage + getTotLsfo(r);		        						
//		        					}
//		        					//TODO non copre questo caso perche non pu� andare prima si star...quindi non pu� andare tra voy a start 
//	        						//CASO 2
//		        					else if(mfc.getStartUseFuel().getTime()  > mfc.getVoyageCommenceDate().getTime() 
//		        							&&  r.getRegistrationDate().getTime() > mfc.getVoyageCommenceDate().getTime() && r.getRegistrationDate().getTime() < mfc.getStopUseFuel().getTime() ){	        						
//		        						if(mfc.getTypeFuel().equals("HSFO")) consVoyageToStopFuel = consVoyageToStopFuel + getTotHsfo(r);
//		        						else if(mfc.getTypeFuel().equals("HSDO")) consVoyageToStopFuel = consVoyageToStopFuel + getTotHsdo(r); 						   
//		        						else if(mfc.getTypeFuel().equals("LSDO")) consVoyageToStopFuel = consVoyageToStopFuel + getTotLsdo(r);
//		        						else if(mfc.getTypeFuel().equals("LSFO")) consVoyageToStopFuel = consVoyageToStopFuel + getTotLsfo(r);
//	        						}

        					
	        				}
	        				/*****************************************   CONSUMI    ****************************END****/
		
	        			}	
        			
	        	

        				mfc.setConsuption(cons);
        			
	        			//Calcolo il totale dei bunker per quel FuelType con regDate minore di stop
	        			System.out.println("AC 17.0  bunk null: " + bunk==null + " ");
	        			for (Bunkering b : bunk) {

	        				System.out.println("AC 17.ii : stopUseFuelSet = " + stopUseFuelSet + " - b.getRegistrationDate().getTime() = " + b.getRegistrationDate().getTime() + " - EasyDateUtils.getGMT0CurrentDate().getTime() = " + 
	        						EasyDateUtils.getGMT0CurrentDate().getTime() + " - b.getType() = " + b.getType() + " - mfc.getTypeFuel() = " + mfc.getTypeFuel()
	        						+ "- b.getType().equals(mfc.getTypeFuel()) = " + b.getType().equals(mfc.getTypeFuel()) + " - b.getType()==mfc.getTypeFuel() =" + b.getType()==mfc.getTypeFuel()
	        						+ " - b.getQuantityMt() = " + b.getQuantityMt() );
	        				
	        				/*****************************************   BUNKER    ****************************BGN****/
	        				if(!stopUseFuelSet && b.getType().equals(mfc.getTypeFuel())){
	           				
	        					if(b.getRegistrationDate().getTime() >= mfc.getStartUseFuel().getTime() ){
		   	        				bunkerStartStop = bunkerStartStop + b.getQuantityMt();   	   	
		   	        				System.out.println("AC 17 " + bunkerStartStop + " bunker for : " + b.getType() + " - RegDat " + b.getRegistrationDate() + " - ID " + b.getId());	
	        					}
	   	        				//CASO 1
	        					if(mfc.getStartUseFuel().getTime()  <= mfc.getVoyageCommenceDate().getTime() 
	        							&& b.getRegistrationDate().getTime() >= mfc.getStartUseFuel().getTime() && b.getRegistrationDate().getTime() <= mfc.getVoyageCommenceDate().getTime())	
	   	        					bunkerStartFuelToVoyage  = bunkerStartFuelToVoyage + b.getQuantityMt();
	        					//CASO 2
	   	        				else if(mfc.getStartUseFuel().getTime()  > mfc.getVoyageCommenceDate().getTime()
	   	        						&& b.getRegistrationDate().getTime() > mfc.getVoyageCommenceDate().getTime()) 
	   	        					bunkerVoyageToStopFuel = bunkerVoyageToStopFuel + b.getQuantityMt();
	        					
	           				}  
 
	           				if(stopUseFuelSet && b.getType().equals(mfc.getTypeFuel()) ){
		       				
	           						if(b.getRegistrationDate().getTime() >= mfc.getStartUseFuel().getTime() && b.getRegistrationDate().getTime() <= mfc.getStopUseFuel().getTime()){ 
				        				bunkerStartStop = bunkerStartStop + b.getQuantityMt();
				        				System.out.println("AC 17a " + bunkerStartStop + " bunker for : " + b.getType() + " - RegDat " + b.getRegistrationDate() + " - ID " + b.getId());	
	           						}
			        			
	           						//CASO 1
	           						if(mfc.getStartUseFuel().getTime()  <= mfc.getVoyageCommenceDate().getTime() 
		        							&& b.getRegistrationDate().getTime() >= mfc.getStartUseFuel().getTime() && b.getRegistrationDate().getTime() <= mfc.getVoyageCommenceDate().getTime())
		   	        					bunkerStartFuelToVoyage  = bunkerStartFuelToVoyage + b.getQuantityMt();
	           						//CASO 2
	           						else if(mfc.getStartUseFuel().getTime()  > mfc.getVoyageCommenceDate().getTime()
		   	        						&& b.getRegistrationDate().getTime() > mfc.getVoyageCommenceDate().getTime() && b.getRegistrationDate().getTime() < mfc.getStopUseFuel().getTime())
		   	        					bunkerVoyageToStopFuel = bunkerVoyageToStopFuel + b.getQuantityMt();
	           						
		       				}	           				
	           				/*****************************************   BUNKER    ****************************END****/
	        			}
	        		
        			
			
        			
	        			 System.out.println("AC 04.g");
	
	        			 
	
	        			//Se la data di stop e diversa da null allora non deve piu calcolarlo -> setto cansCalculated true 
	        			if(stopUseFuelSet)	mfc.setConsCalculated(true); 
	        				       			
	        			
	        			//ROB preso dal voyage setting
	        			if(mfc.getTypeFuel().equals("HFO")) robPerFuel = (mfc.getVoyDepartureTotalHfo()!=null?mfc.getVoyDepartureTotalHfo():0.0);
	        			if(mfc.getTypeFuel().equals("LFO")) robPerFuel = (mfc.getVoyDepartureTotalLfo()!=null?mfc.getVoyDepartureTotalLfo():0.0);
	        			if(mfc.getTypeFuel().equals("MDO")) robPerFuel = (mfc.getVoyDepartureTotalMdo()!=null?mfc.getVoyDepartureTotalMdo():0.0);
	        			
	        			//cambiati per cambio tipo bunker da hsfo,lsfo,hsdo,lsdo a lfo,hfo,mdo
//	        			if(mfc.getTypeFuel().equals("HSFO")) robPerFuel = (mfc.getVoyDepartureTotalHsfo()!=null?mfc.getVoyDepartureTotalHsfo():0.0);
//	        			if(mfc.getTypeFuel().equals("HSDO")) robPerFuel = (mfc.getVoyDepartureTotalHsdo()!=null?mfc.getVoyDepartureTotalHsdo():0.0);
//	        			if(mfc.getTypeFuel().equals("LSFO")) robPerFuel = (mfc.getVoyDepartureTotalLsfo()!=null?mfc.getVoyDepartureTotalLsfo():0.0);
//	        			if(mfc.getTypeFuel().equals("LSDO")) robPerFuel = (mfc.getVoyDepartureTotalLsdo()!=null?mfc.getVoyDepartureTotalLsdo():0.0);
	        			

						
						//CASO 1
	        			if(mfc.getStartUseFuel().getTime() <= mfc.getVoyageCommenceDate().getTime()){	        				
	        				//Qta allo start - mod 06-07 h17
							mfc.setQob(robPerFuel - bunkerStartFuelToVoyage + consStartFuelToVoyage );		
							//mfc.setQob(robPerFuel - bunkerStartFuelToVoyage - consStartFuelToVoyage );	
							//Qta allo stop
							mfc.setRob(robPerFuel + bunkerStartStop - bunkerStartFuelToVoyage - consStartStop + consStartFuelToVoyage);
	        			}
						//CASO 2
	        			else{
							//Qta allo start
							mfc.setQob(robPerFuel + bunkerVoyageToStopFuel - bunkerStartStop + consVoyageToStopFuel - consStartStop );	
							
							//Qta allo stop - mod 06-07 h17
							mfc.setRob(robPerFuel + bunkerVoyageToStopFuel -  consVoyageToStopFuel );  
							//mfc.setRob(robPerFuel + bunkerVoyageToStopFuel - consStartStop + consStartFuelToVoyage );  // - mod 06-07 h17							
							//mfc.setRob(robPerFuel + bunkerVoyageToStopFuel - consStartStop );
	        			}
						
	        			
	        			System.out.println("AC 10 - RobPerFuel di mfc.getId() " + mfc.getId() + " = " + robPerFuel);	    
	        			
	        			System.out.println("AC 11 - bunkerVoyageToStopFuel " + bunkerVoyageToStopFuel);
	        			System.out.println("AC 12 - bunkerStartFuelToVoyage " + bunkerStartFuelToVoyage);
	        			System.out.println("AC 13 - bunkerStartStop " + bunkerStartStop);
	        			
	        			System.out.println("AC 14 - consStartStop " + consStartStop);
	        			System.out.println("AC 15 - consStartFuelToVoyage " + consStartFuelToVoyage);
	        			System.out.println("AC 16 - consVoyageToStopFuel " + consVoyageToStopFuel);
	        			System.out.println("AC 17 - Consumi cons di mfc.getMachinary() " + mfc.getMachinary() + " = " + cons);
	        				        		
	        			System.out.println("AC 30 - bunker per mfc.isConsCalculated() " + mfc.isConsCalculated());
        			}
        			

        			stopUseFuelSet = true;
        			
        			
    			}	
        		
        	
	   			
    		}
    		
    		System.out.println("AC 10 ");
  		
    		
    	} catch (Exception e1) {
            e1.printStackTrace();
            System.out.println("Ex. calcolo consumi dei machinary in bunker status");
        }   
    	
    	
    	System.out.println("************* Calcolo i consumi del bunker status dei diversi machinary ********END********.");
    	
    	return machinaryFuelCalc;
    	
	}
	
	//consStartFuelToVoyage
	private double getTotHfo(RoutePoint r){
		double totCons = 0.0;        						
		totCons =  (   (r.getMainEngineFlowConsInTons()!=0.0 && r.getMainEngineMinuteHfo()!=null?r.getMainEngineFlowConsInTons()/60*r.getMainEngineMinuteHfo():0.0)+(r.getDgsFlowConsInTons()!=0.0 && r.getDieselGenMinuteHfo()!=null?r.getDgsFlowConsInTons()/60*r.getDieselGenMinuteHfo():0.0)+ 
				(r.getFm4InTons()!=0.0 && r.getAuxBoilerMinuteHfo()!=null?r.getFm4InTons()/60*r.getAuxBoilerMinuteHfo():0.0) );			
		return totCons;
	}
	private double getTotLfo(RoutePoint r){
		double totCons = 0.0;        						
		totCons = (  (r.getMainEngineFlowConsInTons()!=0.0 && r.getMainEngineMinuteLfo()!=null?r.getMainEngineFlowConsInTons()/60*r.getMainEngineMinuteLfo():0.0)+(r.getDgsFlowConsInTons()!=0.0 && r.getDieselGenMinuteLfo()!=null?r.getDgsFlowConsInTons()/60*r.getDieselGenMinuteLfo():0.0)+ 
				(r.getFm4InTons()!=0.0 && r.getAuxBoilerMinuteLfo()!=null?r.getFm4InTons()/60*r.getAuxBoilerMinuteLfo():0.0) );		
		return totCons;
	}
	private double getTotMdo(RoutePoint r){
		double totCons = 0.0;        						
		totCons = (   (r.getMainEngineFlowConsInTons()!=0.0 && r.getMainEngineMinuteMdo()!=null?r.getMainEngineFlowConsInTons()/60*r.getMainEngineMinuteMdo():0.0)+(r.getDgsFlowConsInTons()!=0.0 && r.getAuxBoilerMinuteMdo()!=null?r.getDgsFlowConsInTons()/60*r.getAuxBoilerMinuteMdo():0.0)+ 
				(r.getFm4InTons()!=0.0 && r.getAuxBoilerMinuteMdo()!=null?r.getFm4InTons()/60*r.getAuxBoilerMinuteMdo():0.0) );			
		return totCons;
	}
	
	
	//cambiati per cambio tipo bunker da hsfo,lsfo,hsdo,lsdo a lfo,hfo,mdo
//	private double getTotHsfo(RoutePoint r){
//		double totCons = 0.0;        						
//		totCons =  (   (r.getMainEngineFlowConsInTons()!=0.0 && r.getMainEngineMinuteHsfo()!=null?r.getMainEngineFlowConsInTons()/60*r.getMainEngineMinuteHsfo():0.0)+(r.getDgsFlowConsInTons()!=0.0 && r.getDieselGenMinuteHsfo()!=null?r.getDgsFlowConsInTons()/60*r.getDieselGenMinuteHsfo():0.0)+ 
//				(r.getFm4InTons()!=0.0 && r.getAuxBoilerMinuteHsfo()!=null?r.getFm4InTons()/60*r.getAuxBoilerMinuteHsfo():0.0) );			
//		return totCons;
//	}
//	private double getTotHsdo(RoutePoint r){
//		double totCons = 0.0;        						
//		totCons = (   (r.getMainEngineFlowConsInTons()!=0.0 && r.getMainEngineMinuteHsdo()!=null?r.getMainEngineFlowConsInTons()/60*r.getMainEngineMinuteHsdo():0.0)+(r.getDgsFlowConsInTons()!=0.0 && r.getAuxBoilerMinuteHsdo()!=null?r.getDgsFlowConsInTons()/60*r.getAuxBoilerMinuteHsdo():0.0)+ 
//				(r.getFm4InTons()!=0.0 && r.getAuxBoilerMinuteHsdo()!=null?r.getFm4InTons()/60*r.getAuxBoilerMinuteHsdo():0.0) );			
//		return totCons;
//	}
//	private double getTotLsdo(RoutePoint r){
//		double totCons = 0.0;        						
//		totCons = (   (r.getMainEngineFlowConsInTons()!=0.0 && r.getMainEngineMinuteLsdo()!=null?r.getMainEngineFlowConsInTons()/60*r.getMainEngineMinuteLsdo():0.0)+(r.getDgsFlowConsInTons()!=0.0 && r.getDieselGenMinuteLsdo()!=null?r.getDgsFlowConsInTons()/60*r.getDieselGenMinuteLsdo():0.0)+ 
//				(r.getFm4InTons()!=0.0 && r.getAuxBoilerMinuteLsdo()!=null?r.getFm4InTons()/60*r.getAuxBoilerMinuteLsdo():0.0) );			
//		return totCons;
//	}
//	private double getTotLsfo(RoutePoint r){
//		double totCons = 0.0;        						
//		totCons = (  (r.getMainEngineFlowConsInTons()!=0.0 && r.getMainEngineMinuteLsfo()!=null?r.getMainEngineFlowConsInTons()/60*r.getMainEngineMinuteLsfo():0.0)+(r.getDgsFlowConsInTons()!=0.0 && r.getDieselGenMinuteLsfo()!=null?r.getDgsFlowConsInTons()/60*r.getDieselGenMinuteLsfo():0.0)+ 
//				(r.getFm4InTons()!=0.0 && r.getAuxBoilerMinuteLsfo()!=null?r.getFm4InTons()/60*r.getAuxBoilerMinuteLsfo():0.0) );		
//		return totCons;
//	}
	


	
	@Override
	public boolean equals(Object obj) {
		if(obj==null) return false;
		VoyagePlan rhs = (VoyagePlan) obj;
		if (uuid!=null && rhs!=null && rhs.uuid!=null) return uuid.equals(rhs.uuid);
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
		
		String res = "";

		res += "----- Voyage Plan Information -----\n\n\n";
		
		res += "Voyage number:" + voyageNumber + "\n\n";
		
//		res += "Departure :" + departurePlace+ "\n";
		res += "Departure date:" + departureDate+ "\n\n";

//		res += "Arrival:" + arrivalPlace + "\n";
//		res += "Arrival date:" + arrivalDate+ "\n";

		res += "Voyage type:" + voyageType+ "\n";
		
		res += "Remarks:" + remarks+ "\n";
		
//		if (wayPoints!=null) {
//			System.out.println("\nWAY POINTS \n");
//			for (WayPoint wp : wayPoints) {
//				System.out.println(wp);
//			}
//		}
		
//		if (emiWayPoints!=null) {
//			System.out.println("\nEMI WAY POINTS \n");
//			for (EmiWayPoint wp : emiWayPoints) {
//				System.out.println(wp);
//			}
//		}
		
		res += "------------------------------\n\n\n";

		return res;
		//return ToStringBuilder.reflectionToString(this);
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




	public boolean isSynchronizedWithRemote() {
		return synchronizedWithRemote;
	}




	public void setSynchronizedWithRemote(boolean synchronizedWithRemote) {
		this.synchronizedWithRemote = synchronizedWithRemote;
	}



	public Date getDepartureDate() {
		return departureDate;
	}




	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}




//	public Date getArrivalDate() {
//		return arrivalDate;
//	}
//
//
//
//
//	public void setArrivalDate(Date arrivalDate) {
//		this.arrivalDate = arrivalDate;
//	}





	public String getRemarks() {
		return remarks;
	}




	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}





	public List<WayPoint> getWayPoints() {
		return wayPoints;
	}




	public void setWayPoints(List<WayPoint> wayPoints) {
		this.wayPoints = wayPoints;
	}



//
//	public List<EmiWayPoint> getEmiWayPoints() {
//		return emiWayPoints;
//	}
//
//
//
//
//	public void setEmiWayPoints(List<EmiWayPoint> emiWayPoints) {
//		this.emiWayPoints = emiWayPoints;
//	}
//



	public String getVoyageNumber() {
		return voyageNumber;
	}




	public void setVoyageNumber(String voyageNumber) {
		this.voyageNumber = voyageNumber;
	}



	
	
	/**
	 * Restituisce l'oggetto MrvCargoData relativo al viaggio oppure totale oppure l'anno
	 * @return MrvCargoData 
	 */
	public List<MrvCargoData> getCargoData(Double cargoMaxQuantity98Perc, List<VoyagePlan> vp) {

		double CF_DIESEL_OIL = 3.206000; //CF di diesel gas Oil
		double CF_LFO = 3.151040; //CF di Light Fuel Oil(LFO)
		double CF_HFO = 3.114400; //CF di Heavy Fuel Oil(HFO)
		
		List<MrvCargoData> cargoData = new ArrayList<MrvCargoData>();
		
		MrvCargoData cargoItem = new MrvCargoData();

		double totConsMc = 0.0;
		double distance = 0.0; 
		double co2 = 0.0; 
		double eeeoi = 0.0;
		
		if (vp!=null) {
			for (VoyagePlan v : vp) {
				cargoItem = new MrvCargoData();
				
				cargoItem.setVoyageNumber(v.getVoyageNumber()); 
				cargoItem.setDeparturePlace(v.getDeparturePlace()); //from 
				cargoItem.setArrivalPlace(v.getArrivalPlace()); //to
				
				if(v.getVoyageSettings()!=null){					
					//System.out.println("Voyage Nr.: "+v.getVoyageNumber()+" Cargo Type: "+v.getVoyageSettings().getDisplacement() +     v.getVoyageSettings().getCargoType());
					//cargoItem.setDepartureDate(v.getDepartureDate()); cargoItem.setCargoType(v.getVoyageSettings().getCargoType());					
					cargoItem.setCargoQuantity(v.getVoyageSettings().getCargoQuantity() ); cargoItem.setCargoQuantityMetrerCube(v.getVoyageSettings().getCargoQuantityMetrerCube() );
					cargoItem.setDwt(v.getVoyageSettings().getDisplacement());
					
					if(v.getVoyageSettings().getCargoQuantityMetrerCube()!= null) cargoItem.setCargoFreeSpaceM3( cargoMaxQuantity98Perc -  (Double.isNaN(v.getVoyageSettings().getCargoQuantityMetrerCube())?0.0:v.getVoyageSettings().getCargoQuantityMetrerCube())  );
					else cargoItem.setCargoFreeSpaceM3(0.0);										
					if(v.getVoyageSettings().getCargoQuantityMetrerCube()!= null) cargoItem.setCargoFreeSpacePercent(100 -( (100 *(Double.isNaN(v.getVoyageSettings().getCargoQuantityMetrerCube())?0.0:v.getVoyageSettings().getCargoQuantityMetrerCube()) )/ cargoMaxQuantity98Perc ) ) ;
					else cargoItem.setCargoFreeSpacePercent(0.0);
					
				}
				
				if(v.getPaths()!=null){
					if (v.getPaths().size()>0){							
						//System.out.println("Voyage Nr.: "+v.getVoyageNumber()+" COSP: "+v.getPaths().get(0).getDepartureDate() + " - EOSP: " +   v.getPaths().get(v.getPaths().size()-1).getArrivalDate());
						cargoItem.setDepartureDate(v.getPaths().get(0).getDepartureDate());
						cargoItem.setArrivalDate(v.getPaths().get(v.getPaths().size()-1).getArrivalDate());
					}
				}
				
				//TODO 
				totConsMc = 0.0;
				distance = 0.0;
				co2 = 0.0;
				eeeoi = 0.0;
				for (RoutePoint r : new CopyOnWriteArrayList<RoutePoint>(v.getRoutePoints())) {
					//System.out.println("AC1 getCargoQuantity: "  ); 
					
					totConsMc = totConsMc + r.getTotMeConsMtHfo() + r.getTotMeConsMtLfo() + r.getTotMeConsMtMdo() + 
							r.getTotDgConsMtHfo() + r.getTotDgConsMtLfo() + r.getTotDgConsMtMdo() + 
							r.getTotAuxBoilerConsMtHfo() + r.getTotAuxBoilerConsMtLfo() + r.getTotAuxBoilerConsMtMdo();
			
					distance = distance + r.getDistanceDouble();
					
					co2 = co2 + ((r.getTotMeConsMtHfo()*CF_HFO)+(r.getTotMeConsMtLfo()*CF_LFO)+(r.getTotMeConsMtMdo()*CF_DIESEL_OIL)  );
					
				}
				
				cargoItem.setTotConsMc(totConsMc);				
				cargoItem.setTotDistance(distance);
				
				cargoItem.setCo2(getDoubleToStringRoundTwo(co2)); 
				
				if(totConsMc!=0.0){
					cargoItem.setFoPerMls(getDoubleToStringRoundTwo( (Double.isNaN(cargoItem.getCargoQuantity())?0.0:(cargoItem.getCargoQuantity()/ totConsMc) ) ) );
					cargoItem.setFoPerMtOfCargo(getDoubleToStringRoundTwo( (Double.isNaN(distance)?0.0:(distance / totConsMc ) ) ) );
				}
				else{
					cargoItem.setFoPerMls("0.0");
					cargoItem.setFoPerMtOfCargo("0.0");
				}
				
			
				
				if(co2 != 0.0 && (distance* cargoItem.getCargoQuantity()) != 0.0){
					eeeoi = co2/(distance* cargoItem.getCargoQuantity());
				}
				else eeeoi = 0.0;
				cargoItem.setEeoi(getDoubleToStringRoundTwo(eeeoi));
				
				
				
				cargoData.add(cargoItem);
			}
	
		}
		
//		for (MrvCargoData p : cargoData) {
//			System.out.println("AC1 getCargoQuantity: " + p.getVoyageNumber()); 
//		}
					
		return cargoData;
	}
	
	
	/**
	 * Restituisce l'oggetto MrvCargoData relativo atutti i viaggi
	 * @return MrvCargoData 
	 */
	public List<MrvCargoData> getTotalCargoData(Double cargoMaxQuantity98Perc, String totFoPerMls, String totFoPerMtOfCargo, String totCo2, String eeoi, List<MrvCargoData> cargoData, List<VoyagePlan> vp) {
		
		double cargoQuantity = 0.0; double cargoQuantityMetrerCube = 0.0;
		double dwt = 0.0;
		
		System.out.println("TotalCargoData");
		
		MrvCargoData cargoItem = new MrvCargoData();

		if (vp!=null) {
			for (VoyagePlan v : vp) {
				if(v.getVoyageSettings()!=null){
					
					System.out.println("TotalCargoData - Voyage Nr.: "+v.getVoyageNumber()+" Cargo qty m3: "+v.getVoyageSettings().getCargoQuantityMetrerCube());
					
					cargoQuantity += (v.getVoyageSettings().getCargoQuantity()!=null)?v.getVoyageSettings().getCargoQuantity():0.0; 
					cargoQuantityMetrerCube += (v.getVoyageSettings().getCargoQuantityMetrerCube()!=null)?v.getVoyageSettings().getCargoQuantityMetrerCube():0.0;
					dwt += (v.getVoyageSettings().getDisplacement()!=null)?v.getVoyageSettings().getDisplacement():0.0;
											
				}
				

			}
			cargoItem.setCargoQuantity(cargoQuantity); 
			cargoItem.setCargoQuantityMetrerCube(cargoQuantityMetrerCube);
			cargoItem.setDwt(dwt);
							
			cargoItem.setCargoFreeSpaceM3( (cargoMaxQuantity98Perc * vp.size()) - cargoQuantityMetrerCube);					
			cargoItem.setCargoFreeSpacePercent(100 -( (100 *cargoQuantityMetrerCube )/ (cargoMaxQuantity98Perc * vp.size()) ) );
			
			
			
			cargoItem.setEeoi(eeoi==null?"0.0000":eeoi);
			cargoItem.setCo2(totCo2);
			
			cargoItem.setFoPerMls(totFoPerMls);
			cargoItem.setFoPerMtOfCargo(totFoPerMtOfCargo);
			
			cargoData.add(cargoItem);
		}
		
					
		return cargoData;
	}
	
	/**
	 * Restituisce l'oggetto MrvObservedData relativo al viaggio oppure totale oppure l'anno
	 * @return MrvObservedData 
	 */
	public MrvObservedData getObservedData(MrvObservedData voyObsData, List<RoutePoint> rp, List<VoyagePlan> vp) {
		
		long totMeMinHfo = 0; long totMeMinMdo = 0; long totMeMinLfo = 0;		
		double totMeDistMgHfo = 0.0; double totMeDistMgMdo = 0.0; double totMeDistMgLfo = 0.0; 
		
		double totMeConsMtHfo = 0.0; double totMeConsMtMdo = 0.0; double totMeConsMtLfo = 0.0;
		double totDgConsMtHfo = 0.0; double totDgConsMtMdo = 0.0; double totDgConsMtLfo = 0.0;			
		double totAuxBoilerConsMcHfo = 0.0; double totAuxBoilerConsMcMdo = 0.0; double totAuxBoilerConsMcLfo = 0.0;
		
		double totAvSpeed = 0.0;
		double distance = 0.0; 
		int countRp = 0;
		
		System.out.println("getdistanceMgByPassageForTypeFuel- Passage: " + voyObsData.getPassage());
		
		if (rp!=null) {
			for (RoutePoint r : new CopyOnWriteArrayList<RoutePoint>(rp)) {

				if((r.getLegType()==null?voyObsData.getPassage().equals("SEA"):r.getLegType().equals(voyObsData.getPassage())))
				{			
					//cambiati per cambio tipo bunker da hsfo,lsfo,hsdo,lsdo a lfo,hfo,mdo
					//ME Distance MG
					distance = (r.getDistanceDouble()!=0.0?(r.getDistanceDouble()/60):0.0);
					totMeDistMgHfo = totMeDistMgHfo + (distance*(r.getMainEngineMinuteHfo()!=null?r.getMainEngineMinuteHfo():0) );
					totMeDistMgLfo = totMeDistMgLfo + (distance*(r.getMainEngineMinuteLfo()!=null?r.getMainEngineMinuteLfo():0) );
					totMeDistMgMdo = totMeDistMgMdo + (distance*(r.getMainEngineMinuteMdo()!=null?r.getMainEngineMinuteMdo():0) );
					//ME running hours
					totMeMinHfo = totMeMinHfo + (r.getMainEngineMinuteHfo()!=null?r.getMainEngineMinuteHfo():0);
					totMeMinLfo = totMeMinLfo + (r.getMainEngineMinuteLfo()!=null?r.getMainEngineMinuteLfo():0);
					totMeMinMdo = totMeMinMdo + (r.getMainEngineMinuteMdo()!=null?r.getMainEngineMinuteMdo():0);
					//ME Consuption MT
					totMeConsMtHfo = totMeConsMtHfo + r.getTotMeConsMtHfo();
					totMeConsMtLfo = totMeConsMtLfo + r.getTotMeConsMtLfo();
					totMeConsMtMdo = totMeConsMtMdo + r.getTotMeConsMtMdo();
					//DD GG Consuption MT
					totDgConsMtHfo = totDgConsMtHfo + r.getTotDgConsMtHfo();
					totDgConsMtLfo = totDgConsMtLfo + r.getTotDgConsMtLfo();
					totDgConsMtMdo = totDgConsMtMdo + r.getTotDgConsMtMdo();		
					//AUX BOILER Consuption MC
					totAuxBoilerConsMcHfo = totAuxBoilerConsMcHfo + r.getTotAuxBoilerConsMtHfo();
					totAuxBoilerConsMcLfo = totAuxBoilerConsMcLfo + r.getTotAuxBoilerConsMtLfo();
					totAuxBoilerConsMcMdo = totAuxBoilerConsMcMdo + r.getTotAuxBoilerConsMtMdo();				
					
					countRp++;					
					totAvSpeed = totAvSpeed +(r.getAverageSpeedInOneHour()!=0.0?r.getAverageSpeedInOneHour():0);					
				}
			
			}			
			voyObsData.setMeDistanceMgHfo(totMeDistMgHfo); voyObsData.setMeDistanceMgMdo(totMeDistMgMdo); voyObsData.setMeDistanceMgLfo(totMeDistMgLfo); 
			voyObsData.setMeRunningMinHfo(totMeMinHfo); voyObsData.setMeRunningMinMdo(totMeMinMdo); voyObsData.setMeRunningMinLfo(totMeMinLfo); 	
			voyObsData.setMeConsMtHfo(totMeConsMtHfo); voyObsData.setMeConsMtMdo(totMeConsMtMdo); voyObsData.setMeConsMtLfo(totMeConsMtLfo); 
			voyObsData.setDgConsMtHfo(totDgConsMtHfo); voyObsData.setDgConsMtMdo(totDgConsMtMdo); voyObsData.setDgConsMtLfo(totDgConsMtLfo);			
			voyObsData.setAuxBoilerConsMcHfo(totAuxBoilerConsMcHfo); voyObsData.setAuxBoilerConsMcMdo(totAuxBoilerConsMcMdo); voyObsData.setAuxBoilerConsMcLfo(totAuxBoilerConsMcLfo);
			voyObsData.setAvarageSpeed(totAvSpeed!=0.0 && countRp!=0?totAvSpeed/countRp:0.0);
		}
		
		
		//Popolo i consumi ad ins manuale
		if (vp!=null) {
			for (VoyagePlan v : vp) {
				if(v.getVoyageConsuptionManualEntry()!=null){
					if(voyObsData.getPassage().equals("SEA"))
					{									
						voyObsData.setIgsConsMtHfo( voyObsData.getIgsConsMtHfo() +  (Double.isNaN(v.getVoyageConsuptionManualEntry().getIgsSeaConsMtHfo())?0.0:v.getVoyageConsuptionManualEntry().getIgsSeaConsMtHfo()) );
						voyObsData.setIgsConsMtLfo( voyObsData.getIgsConsMtLfo() +  (Double.isNaN(v.getVoyageConsuptionManualEntry().getIgsSeaConsMtLfo())?0.0:v.getVoyageConsuptionManualEntry().getIgsSeaConsMtLfo()) );
						voyObsData.setIgsConsMtMdo( voyObsData.getIgsConsMtMdo() +  (Double.isNaN(v.getVoyageConsuptionManualEntry().getIgsSeaConsMtMdo())?0.0:v.getVoyageConsuptionManualEntry().getIgsSeaConsMtMdo()) );						
						
						voyObsData.setCompBoilerConsMtHfo( voyObsData.getCompBoilerConsMtHfo() + (Double.isNaN(v.getVoyageConsuptionManualEntry().getCompBoilerSeaConsMtHfo())?0.0:v.getVoyageConsuptionManualEntry().getCompBoilerSeaConsMtHfo()) );
						voyObsData.setCompBoilerConsMtLfo( voyObsData.getCompBoilerConsMtLfo() + (Double.isNaN(v.getVoyageConsuptionManualEntry().getCompBoilerSeaConsMtLfo())?0.0:v.getVoyageConsuptionManualEntry().getCompBoilerSeaConsMtLfo()) );
						voyObsData.setCompBoilerConsMtMdo( voyObsData.getCompBoilerConsMtMdo() + (Double.isNaN(v.getVoyageConsuptionManualEntry().getCompBoilerSeaConsMtMdo())?0.0:v.getVoyageConsuptionManualEntry().getCompBoilerSeaConsMtMdo()) );
						
						voyObsData.setPowerPackConsMtHfo( voyObsData.getPowerPackConsMtHfo() + (Double.isNaN(v.getVoyageConsuptionManualEntry().getPowerPackSeaConsMtHfo())?0.0:v.getVoyageConsuptionManualEntry().getPowerPackSeaConsMtHfo()) );
						voyObsData.setPowerPackConsMtLfo( voyObsData.getPowerPackConsMtLfo() + (Double.isNaN(v.getVoyageConsuptionManualEntry().getPowerPackSeaConsMtLfo())?0.0:v.getVoyageConsuptionManualEntry().getPowerPackSeaConsMtLfo()) );
						voyObsData.setPowerPackConsMtMdo( voyObsData.getPowerPackConsMtMdo() + (Double.isNaN(v.getVoyageConsuptionManualEntry().getPowerPackSeaConsMtMdo())?0.0:v.getVoyageConsuptionManualEntry().getPowerPackSeaConsMtMdo()) );						
					}
					if(voyObsData.getPassage().equals("TRANSIT"))
					{									
						voyObsData.setIgsConsMtHfo( voyObsData.getIgsConsMtHfo() +  (Double.isNaN(v.getVoyageConsuptionManualEntry().getIgsTransitConsMtHfo())?0.0:v.getVoyageConsuptionManualEntry().getIgsTransitConsMtHfo()) );
						voyObsData.setIgsConsMtLfo( voyObsData.getIgsConsMtLfo() +  (Double.isNaN(v.getVoyageConsuptionManualEntry().getIgsTransitConsMtLfo())?0.0:v.getVoyageConsuptionManualEntry().getIgsTransitConsMtLfo()) );
						voyObsData.setIgsConsMtMdo( voyObsData.getIgsConsMtMdo() +  (Double.isNaN(v.getVoyageConsuptionManualEntry().getIgsTransitConsMtMdo())?0.0:v.getVoyageConsuptionManualEntry().getIgsTransitConsMtMdo()) );						
						
						voyObsData.setCompBoilerConsMtHfo( voyObsData.getCompBoilerConsMtHfo() + (Double.isNaN(v.getVoyageConsuptionManualEntry().getCompBoilerTransitConsMtHfo())?0.0:v.getVoyageConsuptionManualEntry().getCompBoilerTransitConsMtHfo()) );
						voyObsData.setCompBoilerConsMtLfo( voyObsData.getCompBoilerConsMtLfo() + (Double.isNaN(v.getVoyageConsuptionManualEntry().getCompBoilerTransitConsMtLfo())?0.0:v.getVoyageConsuptionManualEntry().getCompBoilerTransitConsMtLfo()) );
						voyObsData.setCompBoilerConsMtMdo( voyObsData.getCompBoilerConsMtMdo() + (Double.isNaN(v.getVoyageConsuptionManualEntry().getCompBoilerTransitConsMtMdo())?0.0:v.getVoyageConsuptionManualEntry().getCompBoilerTransitConsMtMdo()) );
						
						voyObsData.setPowerPackConsMtHfo( voyObsData.getPowerPackConsMtHfo() + (Double.isNaN(v.getVoyageConsuptionManualEntry().getPowerPackTransitConsMtHfo())?0.0:v.getVoyageConsuptionManualEntry().getPowerPackTransitConsMtHfo()) );
						voyObsData.setPowerPackConsMtLfo( voyObsData.getPowerPackConsMtLfo() + (Double.isNaN(v.getVoyageConsuptionManualEntry().getPowerPackTransitConsMtLfo())?0.0:v.getVoyageConsuptionManualEntry().getPowerPackTransitConsMtLfo()) );
						voyObsData.setPowerPackConsMtMdo( voyObsData.getPowerPackConsMtMdo() + (Double.isNaN(v.getVoyageConsuptionManualEntry().getPowerPackTransitConsMtMdo())?0.0:v.getVoyageConsuptionManualEntry().getPowerPackTransitConsMtMdo()) );						
					}
					if(voyObsData.getPassage().equals("MANOUVRE"))
					{									
						voyObsData.setIgsConsMtHfo( voyObsData.getIgsConsMtHfo() +  (Double.isNaN(v.getVoyageConsuptionManualEntry().getIgsManouvreConsMtHfo())?0.0:v.getVoyageConsuptionManualEntry().getIgsManouvreConsMtHfo()) );
						voyObsData.setIgsConsMtLfo( voyObsData.getIgsConsMtLfo() +  (Double.isNaN(v.getVoyageConsuptionManualEntry().getIgsManouvreConsMtLfo())?0.0:v.getVoyageConsuptionManualEntry().getIgsManouvreConsMtLfo()) );
						voyObsData.setIgsConsMtMdo( voyObsData.getIgsConsMtMdo() +  (Double.isNaN(v.getVoyageConsuptionManualEntry().getIgsManouvreConsMtMdo())?0.0:v.getVoyageConsuptionManualEntry().getIgsManouvreConsMtMdo()) );						
						
						voyObsData.setCompBoilerConsMtHfo( voyObsData.getCompBoilerConsMtHfo() + (Double.isNaN(v.getVoyageConsuptionManualEntry().getCompBoilerManouvreConsMtHfo())?0.0:v.getVoyageConsuptionManualEntry().getCompBoilerManouvreConsMtHfo()) );
						voyObsData.setCompBoilerConsMtLfo( voyObsData.getCompBoilerConsMtLfo() + (Double.isNaN(v.getVoyageConsuptionManualEntry().getCompBoilerManouvreConsMtLfo())?0.0:v.getVoyageConsuptionManualEntry().getCompBoilerManouvreConsMtLfo()) );
						voyObsData.setCompBoilerConsMtMdo( voyObsData.getCompBoilerConsMtMdo() + (Double.isNaN(v.getVoyageConsuptionManualEntry().getCompBoilerManouvreConsMtMdo())?0.0:v.getVoyageConsuptionManualEntry().getCompBoilerManouvreConsMtMdo()) );
						
						voyObsData.setPowerPackConsMtHfo( voyObsData.getPowerPackConsMtHfo() + (Double.isNaN(v.getVoyageConsuptionManualEntry().getPowerPackManouvreConsMtHfo())?0.0:v.getVoyageConsuptionManualEntry().getPowerPackManouvreConsMtHfo()) );
						voyObsData.setPowerPackConsMtLfo( voyObsData.getPowerPackConsMtLfo() + (Double.isNaN(v.getVoyageConsuptionManualEntry().getPowerPackManouvreConsMtLfo())?0.0:v.getVoyageConsuptionManualEntry().getPowerPackManouvreConsMtLfo()) );
						voyObsData.setPowerPackConsMtMdo( voyObsData.getPowerPackConsMtMdo() + (Double.isNaN(v.getVoyageConsuptionManualEntry().getPowerPackManouvreConsMtMdo())?0.0:v.getVoyageConsuptionManualEntry().getPowerPackManouvreConsMtMdo()) );						
					}
				}
			
				
			}
			
			
		}
		
					
		return voyObsData;
	}
	
	/**
	 * Restituisce la distanza in MG relative al viaggio.
	 * Distance = 1h Dmg(mm) /60 * EstimetedRunningHours di quel fuel
	 * @param typeFuel , legType(SEA, TRANSIT, MANOUVRE)
	 * @return minuti 
	 */
	public String getDistanceMgByPassageForTypeFuel(String fuel, String passage) {
		
		double tot = 0.0;
		double distance = 0.0; 
		System.out.println("getdistanceMgByPassageForTypeFuel - fuel: " + fuel + " - Passage: " + passage);
		
		List<RoutePoint> rp = getRoutePoints();
		if (rp!=null) {
			for (RoutePoint r : rp) {

				System.out.println("getLegType: " + r.getLegType() + " - getDistance: " + r.getDistance());
				if(r.getLegType().equals(passage))
				{
					distance = (r.getDistanceDouble()!=0.0?(r.getDistanceDouble()/60):0.0);
					
					if(fuel.equals("HFO")){ tot = tot + (distance*(r.getMainEngineMinuteHfo()!=null?r.getMainEngineMinuteHfo():0) );}
					else if(fuel.equals("MDO")){ tot = tot + (distance*(r.getMainEngineMinuteMdo()!=null?r.getMainEngineMinuteMdo():0) );}
					else if(fuel.equals("LFO")){ tot = tot + (distance*(r.getMainEngineMinuteLfo()!=null?r.getMainEngineMinuteLfo():0) );}
					
					//cambiati per cambio tipo bunker da hsfo,lsfo,hsdo,lsdo a lfo,hfo,mdo
//					if(fuel.equals("HSFO")) tot = tot + (distance*(r.getMainEngineMinuteHsfo()!=null?r.getMainEngineMinuteHsfo():0) );
//					else if(fuel.equals("HSDO")){ tot = tot + (distance*(r.getMainEngineMinuteHsdo()!=null?r.getMainEngineMinuteHsdo():0) );}
//					else if(fuel.equals("LSFO")){ tot = tot + (distance*(r.getMainEngineMinuteLsfo()!=null?r.getMainEngineMinuteLsfo():0) );}
//					else if(fuel.equals("LSDO")){ tot = tot + (distance*(r.getMainEngineMinuteLsdo()!=null?r.getMainEngineMinuteLsdo():0) );}
				}
				System.out.println("TOT: " + tot + " - ID: " + r.getId() );
			}
			
		}
		
			
		return getDoubleToStringRoundTwo(tot);
	}
	
	
	/**
	 * Restituisce le running hours del main engine in min relative al viaggio 
	 * @param typeFuel , legType(SEA, TRANSIT, MANOUVRE)
	 * @return minuti 
	 */
	public Long getMeRunningHousByPassageForTypeFuel(String fuel, String passage) {
		
		Long tot = (long) 0;
		
		System.out.println("getRunningHousByPassageForTypeFuel - fuel: " + fuel + " - Passage: " + passage);
		
		List<RoutePoint> rp = getRoutePoints();
		if (rp!=null) {
			for (RoutePoint r : rp) {

				System.out.println("getLegType: " + r.getLegType() +  " - getMainEngineMinuteHfo: " + r.getMainEngineMinuteHfo());
				if(r.getLegType().equals(passage))
				{
					if(fuel.equals("HFO")){ tot = tot + (r.getMainEngineMinuteHfo()!=null?r.getMainEngineMinuteHfo():0);}
					else if(fuel.equals("MDO")){ tot = tot + (r.getMainEngineMinuteMdo()!=null?r.getMainEngineMinuteMdo():0);}
					else if(fuel.equals("LFO")){ tot = tot + (r.getMainEngineMinuteLfo()!=null?r.getMainEngineMinuteLfo():0);}
				}
				System.out.println("TOT: " + tot + " - ID: " + r.getId() +  " - HFO ME: " + r.getMainEngineMinuteHfo());
			}
			
		}

		return tot;
	}
	
	


	public String getVoyageType() {
		return voyageType;
	}




	public void setVoyageType(String voyageType) {
		this.voyageType = voyageType;
	}


	public boolean isActive() {
		return active;
	}


	public void setActive(boolean active) {
		this.active = active;
	}


	public List<RoutePoint> getRoutePoints() {
		return routePoints;
	}


	public void setRoutePoints(List<RoutePoint> routePoints) {
		this.routePoints = routePoints;
	}

	public Double getTestC() {
		return testC;
	}


	public void setTestC(Double testC) {
		this.testC = testC;
	}


	public Double getTestTotalPower() {
		return testTotalPower;
	}


	public void setTestTotalPower(Double testTotalPower) {
		this.testTotalPower = testTotalPower;
	}


	public VoyageSettings getVoyageSettings() {
		return voyageSettings;
	}


	public void setVoyageSettings(VoyageSettings voyageSettings) {
		this.voyageSettings = voyageSettings;
	}

	


	public VoyageConsuptionManualEntry getVoyageConsuptionManualEntry() {
		return voyageConsuptionManualEntry;
	}

	public void setVoyageConsuptionManualEntry(VoyageConsuptionManualEntry voyageConsuptionManualEntry) {
		this.voyageConsuptionManualEntry = voyageConsuptionManualEntry;
	}
	
	

	public List<NoonPosition> getNoonPositions() {
		return noonPositions;
	}



	public void setNoonPositions(List<NoonPosition> noonPositions) {
		this.noonPositions = noonPositions;
	}



//	public Double getTotalDistance() {
//		return totalDistance;
//	}
//
//
//
//	public void setTotalDistance(Double totalDistance) {
//		this.totalDistance = totalDistance;
//	}
//	
	private class NoonPositionSortByRegistrationDate implements Comparator<NoonPosition>{

		public int compare(NoonPosition o1, NoonPosition o2) {
			return o1.getRegistrationDate().compareTo(o2.getRegistrationDate());
		}
	}



	public List<VoyagePath> getPaths() {
		return paths;
	}



	public void setPaths(List<VoyagePath> paths) {
		this.paths = paths;
	}
/*	
	public List<Bunkering> getBunkering() {
		return bunkering;
	}

	public void setBunkering(List<Bunkering> bunkering) {
		this.bunkering = bunkering;
	}
	*/
	
	public String getDeparturePlace() {
		return departurePlace;
	}

	public void setDeparturePlace(String departurePlace) {
		this.departurePlace = departurePlace;
	}

	public String getArrivalPlace() {
		return arrivalPlace;
	}

	public void setArrivalPlace(String arrivalPlace) {
		this.arrivalPlace = arrivalPlace;
	}

	public boolean isDeparturePlaceUe() {
		return departurePlaceUe;
	}

	public void setDeparturePlaceUe(boolean departurePlaceUe) {
		this.departurePlaceUe = departurePlaceUe;
	}

	public boolean isArrivalPlaceUe() {
		return arrivalPlaceUe;
	}

	public void setArrivalPlaceUe(boolean arrivalPlaceUe) {
		this.arrivalPlaceUe = arrivalPlaceUe;
	}
	

}