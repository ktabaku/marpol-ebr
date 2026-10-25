package com.oceanpro.core.dto;

import java.text.NumberFormat;
import java.util.Locale;

import javax.sound.midi.SysexMessage;

public class MrvObservedData {	
	

	
	private String passage;
	
	private Double meDistanceMgHfo=0.0;
	private Double meDistanceMgLfo=0.0;
	private Double meDistanceMgMdo=0.0;
	
	private Long meRunningMinHfo;
	private Long meRunningMinLfo;
	private Long meRunningMinMdo;
	
	private Double meConsMtHfo=0.0;
	private Double meConsMtLfo=0.0;
	private Double meConsMtMdo=0.0;
	
	private Double dgConsMtHfo=0.0;
	private Double dgConsMtLfo=0.0;
	private Double dgConsMtMdo=0.0;
	
	private Double auxBoilerConsMcHfo=0.0;
	private Double auxBoilerConsMcLfo=0.0;
	private Double auxBoilerConsMcMdo=0.0;

	private Double avarageSpeed=0.0;

	private Double igsConsMtHfo=0.0;
	private Double igsConsMtLfo=0.0;
	private Double igsConsMtMdo=0.0;

	private Double compBoilerConsMtHfo=0.0;
	private Double compBoilerConsMtLfo=0.0;
	private Double compBoilerConsMtMdo=0.0;
	
	private Double powerPackConsMtHfo=0.0;
	private Double powerPackConsMtLfo=0.0;
	private Double powerPackConsMtMdo=0.0;


	//costruttore vuoto
	public MrvObservedData() {
	}

	
	//setta i valori iniziali 
	public void reset(String leg){
		setPassage(leg);
		setMeDistanceMgHfo(0.0); setMeDistanceMgMdo(0.0); setMeDistanceMgLfo(0.0);
		setMeRunningMinHfo((long) 0); setMeRunningMinMdo((long) 0); setMeRunningMinLfo((long) 0);
		
		setIgsConsMtHfo(0.0); setIgsConsMtLfo(0.0); setIgsConsMtMdo(0.0);
		setCompBoilerConsMtHfo(0.0); setCompBoilerConsMtLfo(0.0); setCompBoilerConsMtMdo(0.0);
		setPowerPackConsMtHfo(0.0); setPowerPackConsMtLfo(0.0); setPowerPackConsMtMdo(0.0);
	}
	
	
	protected  String convertMinutesToHours(long minutes) {
	    String label = "00:00";
	 
	    // il numero di ore
	    long diffHour = minutes / 60;
	    // i minuti restanti
	    long diffMinute = minutes % 60;
	 
	    // formattiamo i minuti con due cifre intere
	    NumberFormat formato = NumberFormat.getIntegerInstance();
	    formato.setMinimumIntegerDigits(2);
	    String minuteString = formato.format(diffMinute);
	 
	    // componiamo il tutto in una stringa
	    label = diffHour + ":" + minuteString;
	    return label;
	}
	
	
	public Double getTotalConsInsManualeHfo() {		
		return (getIgsConsMtHfo()+getCompBoilerConsMtHfo()+getPowerPackConsMtHfo());
	}
	public Double getTotalConsInsManualeMdo() {		
		return (getIgsConsMtMdo()+getCompBoilerConsMtMdo()+getPowerPackConsMtMdo());
	}	
	public Double getTotalConsInsManualeLfo() {		
		return (getIgsConsMtLfo()+getCompBoilerConsMtLfo()+getPowerPackConsMtLfo());
	}
	
	public String getTotalConsInsManualeHfoString() {
		return getDoubleToStringRoundN(getTotalConsInsManualeHfo(), 2);		
	}
	public String getTotalConsInsManualeMdoString() {		
		return getDoubleToStringRoundN(getTotalConsInsManualeMdo(), 2);	
	}	
	public String getTotalConsInsManualeLfoString() {		
		return getDoubleToStringRoundN(getTotalConsInsManualeLfo(), 2);	
	}
	
	public Double getTotalConsHfo() {		
		return (getMeConsMtHfo()+getDgConsMtHfo()+getAuxBoilerConsMcHfo()+
				getIgsConsMtHfo()+getCompBoilerConsMtHfo()+getPowerPackConsMtHfo());
	}
	public Double getTotalConsMdo() {		
		return (getMeConsMtMdo()+getDgConsMtMdo()+getAuxBoilerConsMcMdo()+
				getIgsConsMtMdo()+getCompBoilerConsMtMdo()+getPowerPackConsMtMdo());
	}	
	public Double getTotalConsLfo() {		
		return (getMeConsMtLfo()+getDgConsMtLfo()+getAuxBoilerConsMcLfo()+
				getIgsConsMtLfo()+getCompBoilerConsMtLfo()+getPowerPackConsMtLfo());
	}
	
	
	public String getPassage() {
		return passage;
	}

	public void setPassage(String passage) {
		this.passage = passage;
	}

	
	public Double getAvarageSpeed() {
		return avarageSpeed;
	}
	public String getAvarageSpeedString() {
		return getDoubleToStringRoundN(getAvarageSpeed(), 2);
	}

	public void setAvarageSpeed(Double avarageSpeed) {
		this.avarageSpeed = avarageSpeed;
	}
	
	
	public Double getAuxBoilerConsMcTotal() {
		return auxBoilerConsMcHfo + auxBoilerConsMcMdo + auxBoilerConsMcLfo;
	}
	
	public Double getAuxBoilerConsMcHfo() {
		return auxBoilerConsMcHfo;
	}
	public String getAuxBoilerConsMcHfoString() {
		return getDoubleToStringRoundTwo(getAuxBoilerConsMcHfo());
	}

	public void setAuxBoilerConsMcHfo(Double auxBoilerConsMcHfo) {
		this.auxBoilerConsMcHfo = auxBoilerConsMcHfo;
	}


	public Double getAuxBoilerConsMcMdo() {
		return auxBoilerConsMcMdo;
	}
	public String getAuxBoilerConsMcMdoString() {
		return getDoubleToStringRoundTwo(getAuxBoilerConsMcMdo());
	}

	public void setAuxBoilerConsMcMdo(Double auxBoilerConsMcMdo) {
		this.auxBoilerConsMcMdo = auxBoilerConsMcMdo;
	}


	public Double getAuxBoilerConsMcLfo() {
		return auxBoilerConsMcLfo;
	}
	public String getAuxBoilerConsMcLfoString() {
		return getDoubleToStringRoundTwo(getAuxBoilerConsMcLfo());
	}

	public void setAuxBoilerConsMcLfo(Double auxBoilerConsMcLfo) {
		this.auxBoilerConsMcLfo = auxBoilerConsMcLfo;
	}

	
	public Double getDgConsMtTotal() {
		return dgConsMtHfo + dgConsMtMdo + dgConsMtLfo;
	}
	
	
	public Double getDgConsMtHfo() {
		return dgConsMtHfo;
	}
	public String getDgConsMtHfoString() {
		return getDoubleToStringRoundTwo(getDgConsMtHfo());
	}

	public void setDgConsMtHfo(Double dgConsMtHfo) {
		this.dgConsMtHfo = dgConsMtHfo;
	}


	public Double getDgConsMtMdo() {
		return dgConsMtMdo;
	}
	public String getDgConsMtMdoString() {
		return getDoubleToStringRoundTwo(getDgConsMtMdo());
	}

	public void setDgConsMtMdo(Double dgConsMtMdo) {
		this.dgConsMtMdo = dgConsMtMdo;
	}


	public Double getDgConsMtLfo() {
		return dgConsMtLfo;
	}
	public String getDgConsMtLfoString() {
		return getDoubleToStringRoundTwo(getDgConsMtLfo());
	}

	public void setDgConsMtLfo(Double dgConsMtLfo) {
		this.dgConsMtLfo = dgConsMtLfo;
	}



	public Double getMeConsMtTotal() {
		return meConsMtHfo + meConsMtMdo + meConsMtLfo;
	}
	
	public Double getMeConsMtHfo() {
		return meConsMtHfo;
	}
	public String getMeConsMtHfoString() {
		return getDoubleToStringRoundTwo(getMeConsMtHfo());
	}

	public void setMeConsMtHfo(Double meConsMtHfo) {
		this.meConsMtHfo = meConsMtHfo;
	}


	public Double getMeConsMtMdo() {
		return meConsMtMdo;
	}
	public String getMeConsMtMdoString() {
		return getDoubleToStringRoundTwo(getMeConsMtMdo());
	}

	public void setMeConsMtMdo(Double meConsMtMdo) {
		this.meConsMtMdo = meConsMtMdo;
	}


	public Double getMeConsMtLfo() {
		return meConsMtLfo;
	}
	public String getMeConsMtLfoString() {
		return getDoubleToStringRoundTwo(getMeConsMtLfo());
	}

	public void setMeConsMtLfo(Double meConsMtLfo) {
		this.meConsMtLfo = meConsMtLfo;
	}

	
	public Double getMeDistanceMgHfo() {
		return meDistanceMgHfo;
	}
	public String getMeDistanceMgHfoString() {
		return getDoubleToStringRoundTwo(getMeDistanceMgHfo());
	}
	
	public void setMeDistanceMgHfo(Double meDistanceMgHfo) {
		this.meDistanceMgHfo = meDistanceMgHfo;
	}
	
	public Double getMeDistanceMgMdo() {
		return meDistanceMgMdo;
	}
	public String getMeDistanceMgMdoString() {
		return getDoubleToStringRoundTwo(getMeDistanceMgMdo());
	}
	
	public void setMeDistanceMgMdo(Double meDistanceMgMdo) {
		this.meDistanceMgMdo = meDistanceMgMdo;
	}

	public Double getMeDistanceMgLfo() {
		return meDistanceMgLfo;
	}
	public String getMeDistanceMgLfoString() {
		return getDoubleToStringRoundTwo(getMeDistanceMgLfo());
	}

	public void setMeDistanceMgLfo(Double meDistanceMgLfo) {
		this.meDistanceMgLfo = meDistanceMgLfo;
	}
	
	public Double getMeDistanceMgTotal() {
		return meDistanceMgHfo + meDistanceMgMdo + meDistanceMgLfo;
	}
	public String getMeDistanceMgTotalString() {
		return getDoubleToStringRoundTwo(getMeDistanceMgTotal());
	}

	public Long getMeRunningMinTotal() {
		return meRunningMinHfo + meRunningMinMdo + meRunningMinLfo;
	}
	
	public Long getMeRunningMinHfo() {
		return meRunningMinHfo;
	}
	
	public void setMeRunningMinHfo(Long meRunningMinHfo) {
		this.meRunningMinHfo = meRunningMinHfo;
	}

	public Long getMeRunningMinMdo() {
		return meRunningMinMdo;
	}

	public void setMeRunningMinMdo(Long meRunningMinMdo) {
		this.meRunningMinMdo = meRunningMinMdo;
	}

	public Long getMeRunningMinLfo() {
		return meRunningMinLfo;
	}

	public void setMeRunningMinLfo(Long meRunningMinLfo) {
		this.meRunningMinLfo = meRunningMinLfo;
	}

	

	public Double getIgsConsMtHfo() {
		return igsConsMtHfo;
	}
	public void setIgsConsMtHfo(Double igsConsMtHfo) {
		this.igsConsMtHfo = igsConsMtHfo;
	}
	
	public Double getIgsConsMtLfo() {
		return igsConsMtLfo;
	}
	public void setIgsConsMtLfo(Double igsConsMtLfo) {
		this.igsConsMtLfo = igsConsMtLfo;
	}

	public Double getIgsConsMtMdo() {
		return igsConsMtMdo;
	}
	public void setIgsConsMtMdo(Double igsConsMtMdo) {
		this.igsConsMtMdo = igsConsMtMdo;
	}
	
	public Double getIgsConsMtTotal() {
		return igsConsMtHfo + igsConsMtMdo + igsConsMtLfo;
	}
	

	public Double getCompBoilerConsMtHfo() {
		return compBoilerConsMtHfo;
	}
	public void setCompBoilerConsMtHfo(Double compBoilerConsMtHfo) {
		this.compBoilerConsMtHfo = compBoilerConsMtHfo;
	}
	
	public Double getCompBoilerConsMtLfo() {
		return compBoilerConsMtLfo;
	}
	public void setCompBoilerConsMtLfo(Double compBoilerConsMtLfo) {
		this.compBoilerConsMtLfo = compBoilerConsMtLfo;
	}
	
	public Double getCompBoilerConsMtMdo() {
		return compBoilerConsMtMdo;
	}
	public void setCompBoilerConsMtMdo(Double compBoilerConsMtMdo) {
		this.compBoilerConsMtMdo = compBoilerConsMtMdo;
	}
	
	public Double getCompBoilerConsMtTotal() {
		return compBoilerConsMtHfo + compBoilerConsMtMdo + compBoilerConsMtLfo;
	}
	
	public Double getPowerPackConsMtHfo() {
		return powerPackConsMtHfo;
	}
	public void setPowerPackConsMtHfo(Double powerPackConsMtHfo) {
		this.powerPackConsMtHfo = powerPackConsMtHfo;
	}


	public Double getPowerPackConsMtLfo() {
		return powerPackConsMtLfo;
	}
	public void setPowerPackConsMtLfo(Double powerPackConsMtLfo) {
		this.powerPackConsMtLfo = powerPackConsMtLfo;
	}
	
	public Double getPowerPackConsMtMdo() {
		return powerPackConsMtMdo;
	}
	public void setPowerPackConsMtMdo(Double powerPackConsMtMdo) {
		this.powerPackConsMtMdo = powerPackConsMtMdo;
	}
	
	public Double getPowerPackConsMtTotal() {
		return powerPackConsMtHfo + powerPackConsMtMdo + powerPackConsMtLfo;
	}
	
	public String getIgsConsMtHfoString() {
		return getDoubleToStringRoundTwo(getIgsConsMtHfo());
	}
	public String getIgsConsMtLfoString() {
		return getDoubleToStringRoundTwo(getIgsConsMtLfo());
	}
	public String getIgsConsMtMdoString() {
		return getDoubleToStringRoundTwo(getIgsConsMtMdo());
	}
	public String getCompBoilerConsMtHfoString() {
		return getDoubleToStringRoundTwo(getCompBoilerConsMtHfo());
	}
	public String getCompBoilerConsMtLfoString() {
		return getDoubleToStringRoundTwo(getCompBoilerConsMtLfo());
	}
	public String getCompBoilerConsMtMdoString() {
		return getDoubleToStringRoundTwo(getCompBoilerConsMtMdo());
	}
	public String getPowerPackConsMtHfoString() {
		return getDoubleToStringRoundTwo(getPowerPackConsMtHfo());
	}
	public String getPowerPackConsMtLfoString() {
		return getDoubleToStringRoundTwo(getPowerPackConsMtLfo());
	}
	public String getPowerPackConsMtMdoString() {
		return getDoubleToStringRoundTwo(getPowerPackConsMtMdo());
	}


	public String getMeRunningMinHfoString() {
		return convertMinutesToHours(getMeRunningMinHfo());
	}
	public String getMeRunningMinMdoString() {
		return convertMinutesToHours(getMeRunningMinMdo());
	}
	public String getMeRunningMinLfoString() {
		return convertMinutesToHours(getMeRunningMinLfo());
	}
	public String getMeRunningMinTotalString() {
		return convertMinutesToHours(getMeRunningMinTotal());
	}
	
//	private String getDoubleToStringRoundTwo(Double num) {
//		if (num==null || num.isNaN() || num==0) return "0";
//
//		System.out.println("getDoubleToStringRoundTwo num:"+ num);
//		NumberFormat nf = NumberFormat.getInstance(Locale.ITALIAN);
//		nf.setMaximumFractionDigits(10);		
//		String res = nf.format(num);
//		System.out.println("getDoubleToStringRoundTwo res:"+ res);
//
//		int ind = res.indexOf(",");
//		if(ind==-1)return res;
//
//		if (ind+3 > res.length()) ind = res.length();
//		else ind = ind+3;
//		res = res.substring(0, ind);
//		return res;
//	}
	
	private String getDoubleToStringRoundTwo(Double num) {
		if (num==null || num.isNaN()) return "0";
		System.out.println("MrvObsData getDoubleToStringRoundTwo num:"+ num);
		String res = num.toString();
		
		System.out.println("MrvObsData  getDoubleToStringRoundTwo res:"+ res);
		
		int ind = res.indexOf(".");
		if (ind+3 > res.length()) ind = res.length();
		else ind = ind +3;
		res = res.substring(0, ind);
		return res;
	}
	
	private String getDoubleToStringRoundN(Double num, int cifre) {
		if (num==0 || num==null) return "0";
		
		System.out.println("MrvObsData getDoubleToStringRoundN num:"+ num);
		NumberFormat nf = NumberFormat.getInstance(Locale.ITALIAN);
		nf.setMaximumFractionDigits(10);		
		String res = nf.format(num);
		System.out.println("MrvObsData  getDoubleToStringRoundN res:"+ res);
		
		int ind = res.indexOf(",");
		if(ind==-1)return res;

		if (ind+cifre+1 > res.length()) ind = res.length();
		else ind = ind+cifre+1;
		res = res.substring(0, ind);
		return res;
	}
	
	

}
