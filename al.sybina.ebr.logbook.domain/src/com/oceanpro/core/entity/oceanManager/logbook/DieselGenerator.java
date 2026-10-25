package com.oceanpro.core.entity.oceanManager.logbook;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.oceanpro.core.entity.oceanManager.office.Ship;


@Entity
@Table(name="ocean_logbook_diesel_generator")
public class DieselGenerator {

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
	
	@Basic
	private Date registrationDate;
	
	@Basic
	private String dieselGeneratorNro;
	
	//exhaust gas Temp(C)
	@Basic
	private Double cylinder1;
	
	@Basic
	private Double cylinder2;
	
	@Basic
	private Double cylinder3;
	
	@Basic
	private Double cylinder4;
	
	@Basic
	private Double cylinder5;
	
	@Basic
	private Double cylinder6;
	
	@Basic
	private Double cylinder7;
	
	@Basic
	private Double cylinder8;
	
	@Basic
	private Double tcIn;
	
	@Basic
	private Double tcOut;
	
	//Temperature(c)
	@Basic
	private Double loTemp;
	
	@Basic
	private Double foTemp;
	
	@Basic
	private Double airTemp;

	@Basic
	private Double cwTemp;
	
	
	//Pressure(BAR)
	@Basic
	private Double loBar;
	
	@Basic
	private Double foBar;
	
	@Basic
	private Double airBar;

	@Basic
	private Double cwBar;
	
	
	//
	@Basic
	private Double tcRpm;
	
	@Basic
	private Double kw;  
	

	
	//Duty Engineer
	
	@Basic
	private String dutyEngName;
	
	@Basic
	private String dutyEngSurname;
	
	@Basic
	private String dutyEngRank;
	
	
	
	@Basic
	private String compatibilityWithCheckList;

	
	@Column (length=5000) // 5000 caratteri
	private String remarks;
	

	/**
	 * Costruttore vuoto
	 */
	public DieselGenerator() {
		super();
	}
	
	public DieselGenerator(Date registrationTime) {
		this.setRegistrationDate(registrationTime);	

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


	public Double getCylinder1() {
		return cylinder1;
	}

	public void setCylinder1(Double cylinder1) {
		this.cylinder1 = cylinder1;
	}

	public Double getCylinder2() {
		return cylinder2;
	}

	public void setCylinder2(Double cylinder2) {
		this.cylinder2 = cylinder2;
	}

	public Double getCylinder3() {
		return cylinder3;
	}

	public void setCylinder3(Double cylinder3) {
		this.cylinder3 = cylinder3;
	}

	public Double getCylinder4() {
		return cylinder4;
	}

	public void setCylinder4(Double cylinder4) {
		this.cylinder4 = cylinder4;
	}

	public Double getCylinder5() {
		return cylinder5;
	}

	public void setCylinder5(Double cylinder5) {
		this.cylinder5 = cylinder5;
	}

	public Double getCylinder6() {
		return cylinder6;
	}

	public void setCylinder6(Double cylinder6) {
		this.cylinder6 = cylinder6;
	}

	public Double getCylinder7() {
		return cylinder7;
	}

	public void setCylinder7(Double cylinder7) {
		this.cylinder7 = cylinder7;
	}

	public Double getCylinder8() {
		return cylinder8;
	}

	public void setCylinder8(Double cylinder8) {
		this.cylinder8 = cylinder8;
	}

	public Double getTcRpm() {
		return tcRpm;
	}

	public void setTcRpm(Double tcRpm) {
		this.tcRpm = tcRpm;
	}

	public Double getLoTemp() {
		return loTemp;
	}

	public void setLoTemp(Double loTemp) {
		this.loTemp = loTemp;
	}

	public Double getFoTemp() {
		return foTemp;
	}

	public void setFoTemp(Double foTemp) {
		this.foTemp = foTemp;
	}

	public String getDutyEngName() {
		return dutyEngName;
	}

	public void setDutyEngName(String dutyEngName) {
		this.dutyEngName = dutyEngName;
	}

	public String getDutyEngSurname() {
		return dutyEngSurname;
	}

	public void setDutyEngSurname(String dutyEngSurname) {
		this.dutyEngSurname = dutyEngSurname;
	}

	public String getDutyEngRank() {
		return dutyEngRank;
	}

	public void setDutyEngRank(String dutyEngRank) {
		this.dutyEngRank = dutyEngRank;
	}

	public String getCompatibilityWithCheckList() {
		return compatibilityWithCheckList;
	}

	public void setCompatibilityWithCheckList(String compatibilityWithCheckList) {
		this.compatibilityWithCheckList = compatibilityWithCheckList;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Double getTcIn() {
		return tcIn;
	}

	public void setTcIn(Double tcIn) {
		this.tcIn = tcIn;
	}

	public Double getTcOut() {
		return tcOut;
	}

	public void setTcOut(Double tcOut) {
		this.tcOut = tcOut;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getDieselGeneratorNro() {
		return dieselGeneratorNro;
	}

	public void setDieselGeneratorNro(String dieselGeneratorNro) {
		this.dieselGeneratorNro = dieselGeneratorNro;
	}

	public Double getAirTemp() {
		return airTemp;
	}

	public void setAirTemp(Double airTemp) {
		this.airTemp = airTemp;
	}

	public Double getCwTemp() {
		return cwTemp;
	}

	public void setCwTemp(Double cwTemp) {
		this.cwTemp = cwTemp;
	}

	public Double getLoBar() {
		return loBar;
	}

	public void setLoBar(Double loBar) {
		this.loBar = loBar;
	}

	public Double getFoBar() {
		return foBar;
	}

	public void setFoBar(Double foBar) {
		this.foBar = foBar;
	}

	public Double getAirBar() {
		return airBar;
	}

	public void setAirBar(Double airBar) {
		this.airBar = airBar;
	}

	public Double getCwBar() {
		return cwBar;
	}

	public void setCwBar(Double cwBar) {
		this.cwBar = cwBar;
	}
	
	public Double getKw() {
		return kw;
	}

	public void setKw(Double kw) {
		this.kw = kw;
	}


	@Override
	public String toString() {
		return "DieselGenerator [id=" + id + ", uuid=" + uuid
				+ ", registrationDate=" + registrationDate
				+ ", dieselGeneratorNro=" + dieselGeneratorNro + ", cylinder1="
				+ cylinder1 + ", cylinder2=" + cylinder2 + ", cylinder3="
				+ cylinder3 + ", cylinder4=" + cylinder4 + ", cylinder5="
				+ cylinder5 + ", cylinder6=" + cylinder6 + ", cylinder7="
				+ cylinder7 + ", cylinder8=" + cylinder8 + ", tcIn=" + tcIn
				+ ", tcOut=" + tcOut + ", loTemp=" + loTemp + ", foTemp="
				+ foTemp + ", airTemp=" + airTemp + ", cwTemp=" + cwTemp
				+ ", loBar=" + loBar + ", foBar=" + foBar + ", airBar="
				+ airBar + ", cwBar=" + cwBar + ", tcRpm=" + tcRpm + ", Kw="
				+ kw + ", dutyEngName=" + dutyEngName + ", dutyEngSurname="
				+ dutyEngSurname + ", dutyEngRank=" + dutyEngRank
				+ ", compatibilityWithCheckList=" + compatibilityWithCheckList
				+ ", remarks=" + remarks + "]";
	}

	public Ship getShip() {
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}





}
