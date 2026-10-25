package com.oceanpro.core.entity.oceanManager.logbook.restHours;

import java.util.Calendar;
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
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * @author Gio
 *
 */
@Entity
@Table(name="ocean_logbook_rest_hours_hour")
public class RestHour {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long id ; // Unique ID
	
	@Basic
	private String uuid;
	
	@Basic
	private String name;
	@Basic
	private String value;
	
	@ManyToOne (fetch = FetchType.EAGER) // No cascade
	private RestDay day;
	
	@Transient
	private String notice;
	
	
	
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

	
	public String getAllowedValues() {
		// TODO !!!!!!!!
//		if(day.getManouvrings()!=null && day.getManouvrings().size()>0) {
//			
//			Calendar currentday = Calendar.getInstance();
//			currentday.set(Calendar.DAY_OF_MONTH, day.getDayNumber());
//			currentday.set(Calendar.MONTH, day.getMonth());
//			currentday.set(Calendar.YEAR, day.getYear());
//			
//			currentday.set(Calendar.HOUR_OF_DAY, Integer.parseInt(getName().substring(0, 2)));
//			currentday.set(Calendar.MINUTE, 0);
//			currentday.set(Calendar.SECOND, 0);
//			currentday.set(Calendar.MILLISECOND, 0);
//			
//			Date cd = currentday.getTime();
//			
//    		for (Manouvring man : day.getManouvrings()) {
//
//    			if ((cd.after(man.getStart()) || cd.equals(man.getStart())) && (cd.before(man.getEnd()) || cd.equals(man.getEnd()))) {
//    				return "R|W|A";
//    			}
//			}
//    	}
		
		return "R|W|A";
		//return "R|W";
	}
	
	@Transient
	private boolean disabled = false;
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RestHour other = (RestHour) obj;
		
		if (name != null && other.name != null) {
			if (other.name.equals(name) && other.day.equals(day)) return true;
			else return false;
		} else return false;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	public RestDay getDay() {
		return day;
	}


	public void setDay(RestDay day) {
		this.day = day;
	}


	public String getNotice() {
		return notice;
	}


	public void setNotice(String notice) {
		this.notice = notice;
	}

	


}
