package com.oceanpro.core.dto;

import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;


public class MrvCargoData {	

	
	private String voyageNumber = ""; private String cargoType = ""; private String departurePlace = ""; private String arrivalPlace = ""; 
	private Date departureDate; private Date arrivalDate;
	private Double cargoQuantity = 0.0; private Double cargoQuantityMetrerCube = 0.0; private Double dwt = 0.0; //Displacement 
	private Double cargoFreeSpaceM3 = 0.0; private Double cargoFreeSpacePercent = 0.0;
	private String foPerMls = "";
	private String foPerMtOfCargo = "";
	private Double totConsMc = 0.0;
	private Double totDistance = 0.0;
	
	
	private String eeoi = "";
	private String co2 = "";
	
		
	//costruttore vuoto
	public MrvCargoData() {
	}

	
	//setta i valori iniziali 
	public void reset(){
	}
	

	
	private String getDoubleToStringRoundN(Double num, int cifre) {
		if (num==null) return "";
		if (num==0) return "0";

		NumberFormat nf = NumberFormat.getInstance(Locale.ITALIAN);
		nf.setMaximumFractionDigits(10);		
		//String res = nf.format(num).replace(',', '.');
		String res = nf.format(num);
		
		int ind = res.indexOf(",");
		if(ind==-1)return res;

		if (ind+cifre+1 > res.length()) ind = res.length();
		else ind = ind+cifre+1;
		res = res.substring(0, ind);
		return res;
	}


	public String getVoyageNumber() {
		return voyageNumber;
	}


	public void setVoyageNumber(String voyageNumber) {
		this.voyageNumber = voyageNumber;
	}


	public String getCargoType() {
		return cargoType;
	}


	public void setCargoType(String cargoType) {
		this.cargoType = cargoType;
	}


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


	public Date getDepartureDate() {
		return departureDate;
	}


	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}


	public Double getCargoQuantity() {
		return cargoQuantity;
	}


	public void setCargoQuantity(Double cargoQuantity) {
		this.cargoQuantity = cargoQuantity;
	}


	public Double getCargoQuantityMetrerCube() {
		return cargoQuantityMetrerCube;
	}


	public void setCargoQuantityMetrerCube(Double cargoQuantityMetrerCube) {
		this.cargoQuantityMetrerCube = cargoQuantityMetrerCube;
	}


	public Double getDwt() {
		return dwt;
	}


	public void setDwt(Double dwt) {
		this.dwt = dwt;
	}


	public Double getCargoFreeSpaceM3() {
		return cargoFreeSpaceM3;
	}


	public void setCargoFreeSpaceM3(Double cargoFreeSpaceM3) {
		this.cargoFreeSpaceM3 = cargoFreeSpaceM3;
	}


	public Double getCargoFreeSpacePercent() {
		return cargoFreeSpacePercent;
	}


	public void setCargoFreeSpacePercent(Double cargoFreeSpacePercent) {
		this.cargoFreeSpacePercent = cargoFreeSpacePercent;
	}


	public String getFoPerMls() {
		return foPerMls;
	}


	public void setFoPerMls(String foPerMls) {
		this.foPerMls = foPerMls;
	}


	public String getEeoi() {
		return eeoi;
	}


	public void setEeoi(String eeoi) {
		this.eeoi = eeoi;
	}


	public String getCo2() {
		return co2;
	}


	public void setCo2(String co2) {
		this.co2 = co2;
	}


	public String getFoPerMtOfCargo() {
		return foPerMtOfCargo;
	}


	public void setFoPerMtOfCargo(String foPerMtOfCargo) {
		this.foPerMtOfCargo = foPerMtOfCargo;
	}


	public Double getTotConsMc() {
		return totConsMc;
	}


	public void setTotConsMc(Double totConsMc) {
		this.totConsMc = totConsMc;
	}


	public Double getTotDistance() {
		return totDistance;
	}


	public void setTotDistance(Double totDistance) {
		this.totDistance = totDistance;
	}


	public Date getArrivalDate() {
		return arrivalDate;
	}


	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}









	

}
