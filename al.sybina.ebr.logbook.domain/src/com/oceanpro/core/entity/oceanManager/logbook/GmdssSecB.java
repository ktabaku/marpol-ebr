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
@Table(name="ocean_logbook_gmdss_b")
public class GmdssSecB implements Comparable<GmdssSecB>{

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
	private Date signOnDate;
	
	@Basic
	private Date signOffDate;
	
	@Basic
	private String signOnPort;
	
	@Basic
	private String signOffPort;
	
	
	
	
	
	@Column (length=5000) // 5000 caratteri
	private String remarks;

	/**
	 * Costruttore vuoto
	 */
	public GmdssSecB() {
		super();
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

	public Date getSignOnDate() {
		return signOnDate;
	}

	public void setSignOnDate(Date signOnDate) {
		this.signOnDate = signOnDate;
	}

	public Date getSignOffDate() {
		return signOffDate;
	}

	public void setSignOffDate(Date signOffDate) {
		this.signOffDate = signOffDate;
	}

	public String getSignOnPort() {
		return signOnPort;
	}

	public void setSignOnPort(String signOnPort) {
		this.signOnPort = signOnPort;
	}

	public String getSignOffPort() {
		return signOffPort;
	}

	public void setSignOffPort(String signOffPort) {
		this.signOffPort = signOffPort;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public String toString() {
		return "GmdssSecB [id=" + id + ", uuid=" + uuid + ", signOnDate="
				+ signOnDate + ", signOffDate=" + signOffDate + ", signOnPort="
				+ signOnPort + ", signOffPort=" + signOffPort + ", remarks="
				+ remarks + "]";
	}

	@Override
	public int compareTo(GmdssSecB comp) {
		 if (getId()!=null && comp.getId()!=null && getId()>(comp.getId())) return -1; 
		 else if (getId()!=null && comp.getId()!=null && getId().equals(comp.getId())) return 0;
		 else return 1;
	}

	public Ship getShip() {
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}

}