package com.oceanpro.core.entity.oceanManager.logbook.restHours;

import java.util.ArrayList;
import java.util.Calendar;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.oceanpro.core.entity.oceanManager.office.Ship;

/**
 * 
 * @author Gio
 *
 */
@Entity
@Table(name="ocean_logbook_rest_hours_data")
public class RestData {
	
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
	
	@OneToMany(targetEntity=RestDay.class, cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="data", fetch=FetchType.LAZY)
	private List<RestDay> days;

	
	@OneToOne (fetch = FetchType.EAGER) // No cascade
	private CrewForRestHours crew;
	
	
	
	
	
	
	
//	public List<RestDay> getMonthDays(Date month) {
//		
//		List<RestDay> days = new ArrayList<RestDay>();
//		Calendar c = Calendar.getInstance();
//		c.setTime(month);
//		c.set(Calendar.DAY_OF_MONTH, 1);
//		int endDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
//		//System.out.println("getMonthDays - last day of month:" + endDay);
//		int mese = c.get(Calendar.MONTH)+1;
//		int anno = c.get(Calendar.YEAR);
//		for (int i = 1; i <= endDay; i++) {
//			//System.out.println("Cerco: giorno:" + i + "   mese: " + mese + "   anno:" + anno);
//			days.add(getDay(anno, mese, i));
//		}
//		
//		return days;
//	}
//	

	public RestDay getDay(Date day) {
		Calendar c = Calendar.getInstance();
		c.setTime(day);
		int giorno = c.get(Calendar.DAY_OF_MONTH);
		int mese = c.get(Calendar.MONTH)+1;
		int anno = c.get(Calendar.YEAR);
		return getDay(anno, mese, giorno);
		
	}
	public RestDay getDay(int year,int month, int dayNumber) {
		
		try {
			if (days!=null && days.size()>0) {
				RestDay day = new RestDay();
				day.setDayNumber(dayNumber);
				day.setYear(year);
				day.setMonth(month);
				
				if (days.indexOf(day)>-1) return days.get(days.indexOf(day));
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	
	
	
	
	public int getRestHoursIn7days(Date day) {
		int tot =0;
		
		if (days!=null && days.size()>0) {
			int i=-6;
			do{
				Calendar c = Calendar.getInstance();
				c.setTime(day);
				c.add(Calendar.DAY_OF_MONTH, i);
				
				RestDay d = getDay(c.getTime());
				if (d!=null) tot+=d.getRestHoursIn24h();
				
				i++;
			} while(i<1);
		 
		}

		return tot;
	}
	
	public int getWorkHoursIn7days(Date day) {
		int tot =0;
		
		if (days!=null && days.size()>0) {
			int i=-6;
			do{
				Calendar c = Calendar.getInstance();
				c.setTime(day);
				c.add(Calendar.DAY_OF_MONTH, i);
				
				RestDay d = getDay(c.getTime());
				if (d!=null) tot+=d.getWorkHoursIn24h();
				
				i++;
			} while(i<1);
		 
		}

		return tot;
	}
	
	@Override
	public boolean equals(Object obj) {
		RestData rhs = (RestData) obj;
		if (getUuid()!=null && rhs.getUuid()!=null) return getUuid().equals(rhs.getUuid());
		boolean b = getUuid()==rhs.getUuid();
		//System.out.println("id1=" + id + " id2=" + rhs.id +  " b="+b);
		return b;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
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

	public CrewForRestHours getCrew() {
		return crew;
	}

	public void setCrew(CrewForRestHours crew) {
		this.crew = crew;
	}
	public List<RestDay> getDays() {
		return days;
	}
	public void setDays(List<RestDay> days) {
		this.days = days;
	}
	public Ship getShip() {
		return ship;
	}
	public void setShip(Ship ship) {
		this.ship = ship;
	}
}
