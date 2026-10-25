package com.oceanpro.core.entity.oceanManager.logbook;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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


@Entity
@Table(name="ocean_logbook_enginelog_jack_cooling_water")
public class EnginelogJackCoolingWater implements Comparable<EnginelogJackCoolingWater>{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long id ; // Unique ID
	
	
	
	@ManyToOne (fetch = FetchType.EAGER) // No cascade
	private Enginelog engine;
	
	
	
	@Basic
	private String uuid;
	
	@Basic
	private Date registrationTime;
	
	
	//route
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
	public EnginelogJackCoolingWater() {
		super();
	}
	
	public EnginelogJackCoolingWater(Date registrationTime) {
		this.setRegistrationTime(registrationTime);	

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

	public Enginelog getEngine() {
		return engine;
	}

	public void setEngine(Enginelog engine) {
		this.engine = engine;
	}

	public Date getRegistrationTime() {
		return registrationTime;
	}

	public void setRegistrationTime(Date registrationTime) {
		this.registrationTime = registrationTime;
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

	@Override
	public String toString() {
		return "EnginelogJackCoolingWater [id=" + id + ", uuid=" + uuid
				+ ", registrationTime=" + registrationTime + ", cylinder1="
				+ cylinder1 + ", cylinder2=" + cylinder2 + ", cylinder3="
				+ cylinder3 + ", cylinder4=" + cylinder4 + ", cylinder5="
				+ cylinder5 + ", cylinder6=" + cylinder6 + ", cylinder7="
				+ cylinder7 + ", cylinder8=" + cylinder8 + ", dutyEngName="
				+ dutyEngName + ", dutyEngSurname=" + dutyEngSurname
				+ ", dutyEngRank=" + dutyEngRank
				+ ", compatibilityWithCheckList=" + compatibilityWithCheckList
				+ ", remarks=" + remarks + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(EnginelogJackCoolingWater comp) {
	  
	   SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm");
	   try {
	   Date noTime = ft.parse("1970-01-01 00:00");
	   
	 //caso data maggiore
	    if (getEngine().getRegistrationDate()!=null && comp.getEngine().getRegistrationDate()!=null &&
	      getEngine().getRegistrationDate().after(comp.getEngine().getRegistrationDate())) return -1;
	    
	    
	  //caso data uguale e ora maggiore
	    else if (getEngine().getRegistrationDate()!=null && comp.getEngine().getRegistrationDate()!=null && 
	      getRegistrationTime()!=null && comp.getRegistrationTime()!=null &&
	      getEngine().getRegistrationDate().equals(comp.getEngine().getRegistrationDate()) &&
	      getRegistrationTime().after(comp.getRegistrationTime())) return -1;


		//caso data uguale e ora diversa
		 else if ( (getEngine().getRegistrationDate()!=null && comp.getEngine().getRegistrationDate()!=null && 
				 getRegistrationTime()==null && comp.getRegistrationTime()!=null &&
				 getEngine().getRegistrationDate().equals(comp.getEngine().getRegistrationDate()) &&
				 noTime.after(comp.getRegistrationTime()) )||
				 (getEngine().getRegistrationDate()!=null && comp.getEngine().getRegistrationDate()!=null && 
				 getRegistrationTime()!=null && comp.getRegistrationTime()==null &&
				 getEngine().getRegistrationDate().equals(comp.getEngine().getRegistrationDate()) &&
				 getRegistrationTime().after(noTime)) ) return -1;
	    
		 //caso data uguale e ora uguale
		  else if (getEngine().getRegistrationDate()!=null && comp.getEngine().getRegistrationDate()!=null && 
			      getRegistrationTime()!=null && comp.getRegistrationTime()!=null &&
			      getEngine().getRegistrationDate().equals(comp.getEngine().getRegistrationDate()) &&
			      getRegistrationTime().equals(comp.getRegistrationTime())) return 0;
		    
		    
		//caso data uguale e ora uguale
		 else if (getEngine().getRegistrationDate()!=null && comp.getEngine().getRegistrationDate()!=null && 
			      getRegistrationTime()==null && comp.getRegistrationTime()==null &&
			      getEngine().getRegistrationDate().equals(comp.getEngine().getRegistrationDate())) return 0;
	    
	    
	    else return 1;
	    
	  } catch (ParseException e) {
	   // TODO Auto-generated catch block   
	   e.printStackTrace();
	   return 1;
	  }
	 }


}
