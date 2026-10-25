package com.oceanpro.core.entity.oceanManager.logbook.restHours;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * @author Gio
 *
 */
@Entity
@Table(name="ocean_logbook_rest_hours_day")
public class RestDay {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long id ; // Unique ID
	
	@Basic
	private String uuid;
	
	@Basic
	@Column(name="\"RESTDAY_YEAR\"")
	private Integer year;
	@Basic
	private Integer month;
	@Basic
	private Integer dayNumber;
	@Basic
	private String dayName;
	

	@OneToMany(targetEntity=RestHour.class, cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="day", fetch=FetchType.EAGER)
	private List<RestHour> hours;
	
	@JoinTable(name="ocean_logbook_rest_hours_day_hours")
	@OneToMany(targetEntity=Manouvring.class, fetch=FetchType.LAZY) // no cascade
	private List<Manouvring> manouvrings;
	
	@ManyToOne (fetch = FetchType.EAGER) // No cascade
	private RestData data;
	
	
	@Basic
	private String remark;
	
	
	// ************* orario lavoro
	@Column
	private String workerType; // dayWorker, watchkeeping
	
	@Basic
	private Integer workFrom1;
	@Basic
	private Integer workTo1;
	@Basic
	private Integer workFrom2;
	@Basic
	private Integer workTo2;
	// ******************
	
	// At sea, In port
	@Basic
	private Integer place; // 0=atSea, 1=InPort
	
	
	@Transient
	private boolean disabled = false;
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	
	
	/**
	 * 
	 */
	public void createHours() {
		if (hours==null) hours = new ArrayList<RestHour>();
		for (int i = 0; i < 24; i++) {
			RestHour h = new RestHour();
			h.setDay(this);
			h.setName(String.format("%02d", i)+":00");
			h.setUuid(UUID.randomUUID().toString());
			hours.add(h);
		}
	}

	
	/**
	 * La prima ora della giornata lavorativa (di 24 ore) viene calcolata cos�:
	 * se il lavoratore per quel giorno � un day worker allora la prima ora � 00 (mezzanotte)
	 * se il lavoratore per quel giorno � un watchkeeping allora la prima ora � l'ora di inizio della prima guardia
	 * @return
	 */
	private int getStartHourFor24HoursCalculation() {
		if (getWorkerType()!=null && getWorkerType().equals("watchkeeping")) {
			return (getWorkFrom1()!=null?getWorkFrom1():0);
		}
		return 0;
	}
	
	
	public int getRestHoursIn24h() {
		int tot =0;
		
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.setTimeZone(TimeZone.getTimeZone("UTC"));
		tomorrow.set(Calendar.YEAR, year);
		tomorrow.set(Calendar.MONTH, month-1);
		tomorrow.set(Calendar.DAY_OF_MONTH, dayNumber+1);
		RestDay tom = data.getDay(tomorrow.getTime());
		
		boolean isTomorrow=false; 
		int start24Hour = getStartHourFor24HoursCalculation();
		for (int i = start24Hour; i < start24Hour+24; i++) {
			
			int h = i;
			if (i>=24) {
				h = i-24;
				isTomorrow=true;
			}
			
			RestHour hh = new RestHour();
			hh.setName(String.format("%02d", h)+":00");
			try {
				if (isTomorrow) {
					hh.setDay(tom);
					hh = tom.getHours().get(tom.getHours().indexOf(hh));
				}
				else {
					hh.setDay(this);
					hh = getHours().get(getHours().indexOf(hh));
				}
			} catch (Throwable e) {hh=null;}
			if (hh!=null && hh.getValue() !=null && hh.getValue().equals("R")) tot++;
			
			// SE GIORNO NON PRESENTE (quindi disabilitato) .....le ore le tratto come rest!!!
			else if (hh!=null && hh.getDay().isDisabled() && (hh.getValue()==null || hh.getValue().equals(""))) tot++;
			
		}

		return tot;
	}
	
	public int getWorkHoursIn24h() {
		int tot =0;
		
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.setTimeZone(TimeZone.getTimeZone("UTC"));
		tomorrow.set(Calendar.YEAR, year);
		tomorrow.set(Calendar.MONTH, month-1);
		tomorrow.set(Calendar.DAY_OF_MONTH, dayNumber+1);
		RestDay tom = data.getDay(tomorrow.getTime());
		
		boolean isTomorrow=false; 
		int start24Hour = getStartHourFor24HoursCalculation();
		for (int i = start24Hour; i < start24Hour+24; i++) {
			
			int h = i;
			if (i>=24) {
				h = i-24;
				isTomorrow=true;
			}
			
			RestHour hh = new RestHour();
			hh.setName(String.format("%02d", h)+":00");
			try {
				if (isTomorrow) {
					hh.setDay(tom);
					hh = tom.getHours().get(tom.getHours().indexOf(hh));
				}
				else {
					hh.setDay(this);
					hh = getHours().get(getHours().indexOf(hh));
				}
			} catch (Throwable e) {hh=null;}
			if (hh!=null && hh.getValue()!=null && (hh.getValue().equals("W") || hh.getValue().equals("A"))) tot++;
		}

		return tot;
	}
	
	
	
	public int getRestHoursIn7days() {
		
		Calendar today = Calendar.getInstance();
		today.setTimeZone(TimeZone.getTimeZone("UTC"));
		today.set(Calendar.YEAR, year);
		today.set(Calendar.MONTH, month-1);
		today.set(Calendar.DAY_OF_MONTH, dayNumber);
		
		return data.getRestHoursIn7days(today.getTime());
	}
	
	public int getWorkHoursIn7days() {
		
		Calendar today = Calendar.getInstance();
		today.setTimeZone(TimeZone.getTimeZone("UTC"));
		today.set(Calendar.YEAR, year);
		today.set(Calendar.MONTH, month-1);
		today.set(Calendar.DAY_OF_MONTH, dayNumber);
		
		return data.getWorkHoursIn7days(today.getTime());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	public int getRestPeriodsIn24h() {
//		int totPeriods =0;
//		Calendar tomorrow = Calendar.getInstance();
//		tomorrow.setTimeZone(TimeZone.getTimeZone("UTC"));
//		tomorrow.set(Calendar.YEAR, year);
//		tomorrow.set(Calendar.MONTH, month-1);
//		tomorrow.set(Calendar.DAY_OF_MONTH, dayNumber+1);
//		
//		int restConsecutivi =0;
//		boolean isTomorrow=false;  
//		int start24Hour = getStartHourFor24HoursCalculation();
//		for (int i = start24Hour; i < start24Hour+24; i++) {
//			
//			int h = i;
//			if (i>=24) {
//				h = i-24;
//				isTomorrow=true;
//			}
//			
//			RestHour hh = new RestHour();
//			hh.setName(String.format("%02d", h)+":00");
//			if (isTomorrow) {
//				RestDay tom = data.getDay(tomorrow.getTime());
//				hh.setDay(tom);
//				if (tom!=null && tom.getHours()!=null && tom.getHours().size()>0 && tom.getHours().get(tom.getHours().indexOf(hh))!=null && tom.getHours().get(tom.getHours().indexOf(hh)).getValue()!=null && tom.getHours().get(tom.getHours().indexOf(hh)).getValue().equals("R")) restConsecutivi++;
//				else {
//					if (restConsecutivi>0) totPeriods++;
//					restConsecutivi=0;
//				}
//			}
//			else {
//				hh.setDay(this);
//				if (getHours()!=null && getHours().size()>0 && getHours().get(getHours().indexOf(hh))!=null && getHours().get(getHours().indexOf(hh)).getValue()!=null && getHours().get(getHours().indexOf(hh)).getValue().equals("R")) restConsecutivi++;
//				else {
//					if (restConsecutivi>0) totPeriods++;
//					restConsecutivi=0;
//				}
//			}
//		}
//		if (restConsecutivi>0) totPeriods++;
//		//System.out.println("periodi trovati per il giorno " + year + "/" + (month) + "/" + dayNumber + ":  " + totPeriods);
//		return totPeriods;
//	}
//	
//	public int getMaxRestHoursForPeriodsIn24h() {
//		int maxHours =0;
//		Calendar tomorrow = Calendar.getInstance();
//		tomorrow.setTimeZone(TimeZone.getTimeZone("UTC"));
//		tomorrow.set(Calendar.YEAR, year);
//		tomorrow.set(Calendar.MONTH, month-1);
//		tomorrow.set(Calendar.DAY_OF_MONTH, dayNumber+1);
//		
//		int restConsecutivi =0;
//		boolean isTomorrow=false; 
//		int start24Hour = getStartHourFor24HoursCalculation();
//		for (int i = start24Hour; i < start24Hour+24; i++) {
//			
//			int h = i;
//			if (i>=24) {
//				h = i-24;
//				isTomorrow=true;
//			}
//			
//			RestHour hh = new RestHour();
//			hh.setName(String.format("%02d", h)+":00");
//			if (isTomorrow) {
//				RestDay tom = data.getDay(tomorrow.getTime());
//				hh.setDay(tom);
//				if (tom!=null && tom.getHours()!=null && tom.getHours().size()>0 && tom.getHours().get(tom.getHours().indexOf(hh))!=null && tom.getHours().get(tom.getHours().indexOf(hh)).getValue()!=null && tom.getHours().get(tom.getHours().indexOf(hh)).getValue().equals("R")) restConsecutivi++;
//				else {
//					if (restConsecutivi>0 && restConsecutivi>maxHours) maxHours =restConsecutivi;
//					restConsecutivi=0;
//				}
//			}
//			else {
//				hh.setDay(this);
//				if (getHours()!=null && getHours().size()>0 && getHours().get(getHours().indexOf(hh))!=null && getHours().get(getHours().indexOf(hh)).getValue()!=null && getHours().get(getHours().indexOf(hh)).getValue().equals("R")) restConsecutivi++;
//				else {
//					if (restConsecutivi>0 && restConsecutivi>maxHours) maxHours =restConsecutivi;
//					restConsecutivi=0;
//				}
//			}
//		}
//		if (restConsecutivi>0 && restConsecutivi>maxHours) maxHours =restConsecutivi;
//		//System.out.println("Max rest hours per il giorno " + year + "/" + (month) + "/" + dayNumber + ":  " + maxHours);
//		return maxHours;
//	}
//
//	
//	public int getMaxWorkHoursForPeriodIn24h() {
//		int maxHours =0;
//		Calendar tomorrow = Calendar.getInstance();
//		tomorrow.setTimeZone(TimeZone.getTimeZone("UTC"));
//		tomorrow.set(Calendar.YEAR, year);
//		tomorrow.set(Calendar.MONTH, month-1);
//		tomorrow.set(Calendar.DAY_OF_MONTH, dayNumber+1);
//		
//		int workConsecutivi =0;
//		boolean isTomorrow=false; 
//		int start24Hour = getStartHourFor24HoursCalculation();
//		for (int i = start24Hour; i < start24Hour+24; i++) {
//			
//			int h = i;
//			if (i>=24) {
//				h = i-24;
//				isTomorrow=true;
//			}
//			
//			RestHour hh = new RestHour();
//			hh.setName(String.format("%02d", h)+":00");
//			if (isTomorrow) {
//				RestDay tom = data.getDay(tomorrow.getTime());
//				hh.setDay(tom);
//				if (tom!=null && tom.getHours()!=null && tom.getHours().size()>0 && tom.getHours().get(tom.getHours().indexOf(hh))!=null && tom.getHours().get(tom.getHours().indexOf(hh)).getValue()!=null && (tom.getHours().get(tom.getHours().indexOf(hh)).getValue().equals("W") || tom.getHours().get(tom.getHours().indexOf(hh)).getValue().equals("A"))) workConsecutivi++;
//				else {
//					if (workConsecutivi>0 && workConsecutivi>maxHours) maxHours =workConsecutivi;
//					workConsecutivi=0;
//				}
//			}
//			else {
//				hh.setDay(this);
//				//System.out.println("hh:" + hh.getName());
//				if (getHours()!=null && getHours().size()>0 && getHours().get(getHours().indexOf(hh))!=null && getHours().get(getHours().indexOf(hh)).getValue()!=null && (getHours().get(getHours().indexOf(hh)).getValue().equals("W") || getHours().get(getHours().indexOf(hh)).getValue().equals("A"))) workConsecutivi++;
//				else {
//					if (workConsecutivi>0 && workConsecutivi>maxHours) maxHours =workConsecutivi;
//					workConsecutivi=0;
//				}
//			}
//		}
//		if (workConsecutivi>0 && workConsecutivi>maxHours) maxHours =workConsecutivi;
//
//		return maxHours;
//	}
//	
//	
//	public boolean isThereLunchBreakIn24h() {
//		Calendar tomorrow = Calendar.getInstance();
//		tomorrow.setTimeZone(TimeZone.getTimeZone("UTC"));
//		tomorrow.set(Calendar.YEAR, year);
//		tomorrow.set(Calendar.MONTH, month-1);
//		tomorrow.set(Calendar.DAY_OF_MONTH, dayNumber+1);
//		
//		int restConsecutivi =0;
//		boolean isTomorrow=false; 
//		int start24Hour = getStartHourFor24HoursCalculation();
//		for (int i = start24Hour; i < start24Hour+24; i++) {
//			
//			int h = i;
//			if (i>=24) {
//				h = i-24;
//				isTomorrow=true;
//			}
//			
//			RestHour hh = new RestHour();
//			hh.setName(String.format("%02d", h)+":00");
//			if (isTomorrow) {
//				RestDay tom = data.getDay(tomorrow.getTime());
//				hh.setDay(tom);
//				if (tom!=null && tom.getHours()!=null && tom.getHours().size()>0 && tom.getHours().get(tom.getHours().indexOf(hh))!=null && tom.getHours().get(tom.getHours().indexOf(hh)).getValue()!=null && tom.getHours().get(tom.getHours().indexOf(hh)).getValue().equals("R")) restConsecutivi++;
//				else {
//					if (restConsecutivi==1) {
//						break;
//					}
//					restConsecutivi=0;
//				}
//			}
//			else {
//				hh.setDay(this);
//				if (getHours()!=null && getHours().size()>0 && getHours().get(getHours().indexOf(hh))!=null && getHours().get(getHours().indexOf(hh)).getValue()!=null && getHours().get(getHours().indexOf(hh)).getValue().equals("R")) restConsecutivi++;
//				else {
//					if (restConsecutivi==1) {
//						break;
//					}
//					restConsecutivi=0;
//				}
//			}
//		}
//		if (restConsecutivi==1) return true;
//		//System.out.println("Max rest hours per il giorno " + year + "/" + (month) + "/" + dayNumber + ":  " + maxHours);
//		return false;
//	}
//	
	
//	public String getNotice() {
//		
//		if (getRestPeriodsIn24h()==3) {
//			// Verifico che uno sia di un ora (pausa pranzo)
//			if (!isThereLunchBreakIn24h()) return "More then 2 rest periods!";
//		}
//		else if (getRestPeriodsIn24h()>2) {
//			return "More then 2 rest periods!";
//		}
//		else {
//			if (getMaxRestHoursForPeriodsIn24h()<6) return "At last one rest period must be grather then or equars to 6 hours!";
//			if (getMaxWorkHoursForPeriodIn24h()>14) return "Interval between rest periods not to exceed 14 hours!"; // forse questo � superfluo, c'� gi� l'indicazione di quante ore consecutive lavora
//		}
//		
//		return "";
//	}
	
	
	public boolean isDayNoticed() {
		return setHoursNoticesForNonConformities();
	}
	
	@Transient
	private int workHoursIn24Hours = 0;
	@Transient
	private int restHoursIn24Hours = 0;
	
	private boolean setHoursNoticesForNonConformities() {
		
		workHoursIn24Hours = 0;
		restHoursIn24Hours = 0;
		
		// per ogni ora del giorno devo controllare che nelle passare 24 ore il marittimo abbia riposato almeno per 10 ore in massimo 2 periodi e che un periodo sia >= 6 ore
		boolean noticesInTheDay = false;
		
		Calendar yesterday = Calendar.getInstance();
		yesterday.setTimeZone(TimeZone.getTimeZone("UTC"));
		yesterday.set(Calendar.YEAR, year);
		yesterday.set(Calendar.MONTH, month-1);
		yesterday.set(Calendar.DAY_OF_MONTH, dayNumber-1);
		RestDay yesterdayDay = data.getDay(yesterday.getTime());
		
		int tempRestConsecutivi =0;
		
		int totRestHours=0;
		int totNotValueSettedHours = 0;
		int totRestPeriods =0;
		int maxRestHours =0;
		boolean isThereBreak=false;
		
		boolean isToday=false; 
		List<RestHour> lrhs = getHours();
		for (RestHour hor : lrhs) {
			tempRestConsecutivi =0;
			totRestHours=0;
			totRestPeriods =0;
			maxRestHours =0;
			totNotValueSettedHours = 0;
			isThereBreak=false;
			
			isToday=false;
			int end24Hour = Integer.parseInt(hor.getName().substring(0, 2));
//			int start24Hour = Integer.parseInt(hor.getName().substring(0, 2))-23;
			int start24Hour = end24Hour +1;
			if (start24Hour==24) start24Hour = 0;
			int previ=end24Hour;
			int iTemp =start24Hour;
			int i=0;
			while (i<24) {
				
				if (iTemp>23) iTemp = iTemp-24;
				
				if (!isToday && previ==23 && iTemp==0) {
					isToday=true;
				}
				previ = iTemp;
				
				RestHour hh = new RestHour();
				hh.setName(String.format("%02d", iTemp)+":00");
				
					
				try {
					if (!isToday) {
						hh.setDay(yesterdayDay);
						hh = yesterdayDay.getHours().get(yesterdayDay.getHours().indexOf(hh));
					}
					else {
						hh.setDay(this);
						hh = getHours().get(getHours().indexOf(hh));
					}
				} catch (Throwable e) {e.printStackTrace();hh=null;}
				
				if (hh==null) System.out.println("ora: " +iTemp + " day:" + getDayNumber() + "   isToday" + isToday +  "  NULL!!!!!!!!!!!!!!!!!");
				
				if (hh!=null && hh.getValue()!=null && hh.getValue().equals("R")) {
					tempRestConsecutivi++;
					totRestHours++;
				}
				else {
					
					// SE GIORNO NON PRESENTE (quindi disabilitato) .....le ore le tratto come rest!!!
					if (hh!=null && hh.getDay().isDisabled() && (hh.getValue()==null || hh.getValue().equals(""))) {
						tempRestConsecutivi++;
						totRestHours++;
					}
					else {
						if (tempRestConsecutivi==1) isThereBreak=true;
						if (tempRestConsecutivi>0 && tempRestConsecutivi>maxRestHours) maxRestHours =tempRestConsecutivi;
						if (tempRestConsecutivi>0) totRestPeriods++;
						
						if (hh!=null && (hh.getValue()==null || (hh.getValue()!=null && hh.getValue().equals("")))) totNotValueSettedHours++;
	
						tempRestConsecutivi=0;
					}
				}
				
				iTemp++;
				i++;
			}
			if (tempRestConsecutivi==1) isThereBreak=true;
			if (tempRestConsecutivi>0 && tempRestConsecutivi>maxRestHours) maxRestHours =tempRestConsecutivi;
			if (tempRestConsecutivi>0) totRestPeriods++;
			//System.out.println("Max rest hours per il giorno " + year + "/" + (month) + "/" + dayNumber + ":  " + maxHours);
			

			String notices = "";
//			if (totRestPeriods==3) {
//				// Verifico che uno sia di un ora (pausa pranzo)
//				if (!isThereBreak) notices += "More then 2 rest periods!; ";
//			}
//			else 
			if (totRestPeriods>2) {
				notices += "More then 2 rest periods!; ";
			}

			if (maxRestHours<6) notices += "At last one rest period must be grather then or equars to 6 hours!; ";
			if (totRestHours<10 && totNotValueSettedHours==0) notices += "Working hours in 24 hours must not to exceed 14 hours!";

			hor.setNotice(notices);
//			hor.setNotice("  totRestPeriods:" + totRestPeriods + "    maxRestHours:" + maxRestHours + "    totRestHours: " + totRestHours + "    totNotValueSettedHours:" + totNotValueSettedHours);
			
			if (!noticesInTheDay && !notices.equals("")) noticesInTheDay = true;
			
			
			
			// Calcolo ore lavorate nelle 24 ore
			if (hor.getValue()!=null && (hor.getValue().equals("W") || hor.getValue().equals("A"))) workHoursIn24Hours++;
			if (hor.getValue()!=null && hor.getValue().equals("R")) restHoursIn24Hours++;
			
		}
		
		return noticesInTheDay;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
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

	public Integer getDayNumber() {
		return dayNumber;
	}

	public void setDayNumber(Integer dayNumber) {
		this.dayNumber = dayNumber;
	}

	public String getDayName() {
		return dayName;
	}

	public void setDayName(String dayName) {
		this.dayName = dayName;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RestDay other = (RestDay) obj;
		
		if (year != null && month != null && dayNumber != null && other.year != null && other.month != null && other.dayNumber != null) {
			if (other.year.equals(year) && other.month.equals(month) && other.dayNumber.equals(dayNumber)) return true;
			else return false;
		} else return false;
	}


	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	public RestData getData() {
		return data;
	}
	public void setData(RestData data) {
		this.data = data;
	}

	

	public Integer getPlace() {
		return place;
	}

	public void setPlace(Integer place) {
		this.place = place;
	}

	public String getWorkerType() {
		return workerType;
	}

	public void setWorkerType(String workerType) {
		this.workerType = workerType;
	}

	
	public List<RestHour> getHours() {
		return hours;
	}

	public void setHours(List<RestHour> hours) {
		this.hours = hours;
	}


	public List<Manouvring> getManouvrings() {
		return manouvrings;
	}


	public void setManouvrings(List<Manouvring> manouvrings) {
		this.manouvrings = manouvrings;
	}


	public Integer getWorkFrom1() {
		return workFrom1;
	}


	public void setWorkFrom1(Integer workFrom1) {
		this.workFrom1 = workFrom1;
	}


	public Integer getWorkTo1() {
		return workTo1;
	}


	public void setWorkTo1(Integer workTo1) {
		this.workTo1 = workTo1;
	}


	public Integer getWorkFrom2() {
		return workFrom2;
	}


	public void setWorkFrom2(Integer workFrom2) {
		this.workFrom2 = workFrom2;
	}


	public Integer getWorkTo2() {
		return workTo2;
	}


	public void setWorkTo2(Integer workTo2) {
		this.workTo2 = workTo2;
	}
	public int getWorkHoursIn24Hours() {
		return workHoursIn24Hours;
	}
	public int getRestHoursIn24Hours() {
		return restHoursIn24Hours;
	}

	


}
