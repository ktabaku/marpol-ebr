package com.oceanpro.core.entity.oceanManager.logbook;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="ocean_logbook_decklog_route_meteo")
public class DecklogRouteMeteo {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long id ; // Unique ID
	
	@ManyToOne (fetch = FetchType.EAGER) // No cascade
	private Decklog deck;
	
	@Basic
	private String uuid;
	
	@Basic
	private String watch;
	
	
	//route
	@Basic
	private Double gyroCompass;
	
	@Basic
	private Double steeringCompass;
	
	@Basic
	private Double courseMade;
	
	@Basic
	private Double drift;
	
	@Basic
	private Double gyroError;
	
	
	
	//meteo
	@Basic
	private String windDir;
	
	@Basic
	private String windForce;
	
	@Basic
	private String seaDir;
	
	@Basic
	private String seaForce;
	
	@Basic
	private String weather;
	
	@Basic
	private String visibility;
	
	@Basic
	private Integer barometer;
	
	@Basic
	private String trend;
	
	@Basic
	private Integer termometerDry;
	
	@Basic
	private Integer termometerWet;
	
	@Basic
	private Integer relativeUmidity;
	
	
	//Engine
	@Basic
	private Double rpm;
	
	@Basic
	private Double engineSpeed;
	
	@Basic
	private Double slip;
	
	
	//AB on watch
	@Basic
	private String abWatchName;
	
	@Basic
	private String abWatchSurname;
	
	
	//comply with checklist
	@Column (length=5000) // 5000 caratteri
	private String complyCheck;
	
	
	
	@Column (length=5000) // 5000 caratteri
	private String remarks;
	
	
	
	
	
	
	
	
	
	/**
	 * Costruttore vuoto
	 */
	public DecklogRouteMeteo() {
		super();
	}
	
	public DecklogRouteMeteo(String watch) {
		this.setWatch(watch);
		

	}
	




	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public Decklog getDeck() {
		return deck;
	}



	public void setDeck(Decklog deck) {
		this.deck = deck;
	}



	public String getUuid() {
		return uuid;
	}



	public void setUuid(String uuid) {
		this.uuid = uuid;
	}



	public String getVisibility() {
		return visibility;
	}



	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}



	public Integer getBarometer() {
		return barometer;
	}



	public void setBarometer(Integer barometer) {
		this.barometer = barometer;
	}



	public String getTrend() {
		return trend;
	}



	public void setTrend(String trend) {
		this.trend = trend;
	}



	public Integer getTermometerDry() {
		return termometerDry;
	}



	public void setTermometerDry(Integer termometerDry) {
		this.termometerDry = termometerDry;
	}



	public Integer getTermometerWet() {
		return termometerWet;
	}



	public void setTermometerWet(Integer termometerWet) {
		this.termometerWet = termometerWet;
	}



	public Integer getRelativeUmidity() {
		return relativeUmidity;
	}



	public void setRelativeUmidity(Integer relativeUmidity) {
		this.relativeUmidity = relativeUmidity;
	}



	public String getWatch() {
		return watch;
	}



	public void setWatch(String watch) {
		this.watch = watch;
	}

	@Override
	public String toString() {
		return "DecklogRouteMeteo [id=" + id  + ", uuid="
				+ uuid + ", watch=" + watch + ", gyroCompass=" + gyroCompass
				+ ", steeringCompass=" + steeringCompass + ", courseMade="
				+ courseMade + ", drift=" + drift + ", gyroError=" + gyroError
				+ ", windDir=" + windDir + ", windForce=" + windForce
				+ ", seaDir=" + seaDir + ", seaForce=" + seaForce + ", sky="
				+ weather + ", visibility=" + visibility + ", barometer="
				+ barometer + ", trend=" + trend + ", termometerDry="
				+ termometerDry + ", termometerWet=" + termometerWet
				+ ", relativeUmidity=" + relativeUmidity + "]";
	}

	public Double getDrift() {
		return drift;
	}

	public void setDrift(Double drift) {
		this.drift = drift;
	}

	public Double getGyroError() {
		return gyroError;
	}

	public void setGyroError(Double gyroError) {
		this.gyroError = gyroError;
	}

	public String getWindDir() {
		return windDir;
	}

	public void setWindDir(String windDir) {
		this.windDir = windDir;
	}

	public String getWindForce() {
		return windForce;
	}

	public void setWindForce(String windForce) {
		this.windForce = windForce;
	}

	public String getSeaDir() {
		return seaDir;
	}

	public void setSeaDir(String seaDir) {
		this.seaDir = seaDir;
	}

	public String getSeaForce() {
		return seaForce;
	}

	public void setSeaForce(String seaForce) {
		this.seaForce = seaForce;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public Double getRpm() {
		return rpm;
	}

	public void setRpm(Double rpm) {
		this.rpm = rpm;
	}

	public Double getEngineSpeed() {
		return engineSpeed;
	}

	public void setEngineSpeed(Double engineSpeed) {
		this.engineSpeed = engineSpeed;
	}

	public Double getSlip() {
		return slip;
	}

	public void setSlip(Double slip) {
		this.slip = slip;
	}

	public String getAbWatchName() {
		return abWatchName;
	}

	public void setAbWatchName(String abWatchName) {
		this.abWatchName = abWatchName;
	}

	public String getAbWatchSurname() {
		return abWatchSurname;
	}

	public void setAbWatchSurname(String abWatchSurname) {
		this.abWatchSurname = abWatchSurname;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getComplyCheck() {
		return complyCheck;
	}

	public void setComplyCheck(String complyCheck) {
		this.complyCheck = complyCheck;
	}

	public Double getGyroCompass() {
		return gyroCompass;
	}

	public void setGyroCompass(Double gyroCompass) {
		this.gyroCompass = gyroCompass;
	}

	public Double getSteeringCompass() {
		return steeringCompass;
	}

	public void setSteeringCompass(Double steeringCompass) {
		this.steeringCompass = steeringCompass;
	}

	public Double getCourseMade() {
		return courseMade;
	}

	public void setCourseMade(Double courseMade) {
		this.courseMade = courseMade;
	}


}
