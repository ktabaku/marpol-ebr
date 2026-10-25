package com.oceanpro.core.entity.oceanManager.logbook;

import java.util.Collections;
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
import javax.persistence.Table;

import com.oceanpro.core.entity.oceanManager.office.Ship;
import com.oceanpro.core.entity.oceanManager.voyage.NoonPosition;
import com.oceanpro.core.entity.oceanManager.voyage.VoyagePath;
import com.oceanpro.core.entity.oceanManager.voyage.VoyagePlan;

@Entity
@Table(name="ocean_logbook_decklog")
public class Decklog {
	
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
	
	
	@ManyToOne (fetch = FetchType.EAGER) // No cascade
	private VoyagePlan voyage;
	
	@OneToMany(targetEntity=DecklogUms.class, cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="deck", fetch=FetchType.LAZY)
	private List<DecklogUms> ums;
	
	@OneToMany(targetEntity=DecklogRadar.class, cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="deck", fetch=FetchType.LAZY)
	private List<DecklogRadar> radar;
	
	@OneToMany(targetEntity=DecklogRouteMeteo.class, cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="deck", fetch=FetchType.LAZY)
	private List<DecklogRouteMeteo> routeMeteo;
	
	@OneToMany(targetEntity=DecklogShipPosition.class, cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="deck", fetch=FetchType.LAZY)
	private List<DecklogShipPosition> position;
	
	@OneToMany(targetEntity=DecklogCompass.class, cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="deck", fetch=FetchType.LAZY)
	private List<DecklogCompass> compass;
	
	@OneToMany(targetEntity=DecklogChrono.class, cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="deck", fetch=FetchType.LAZY)
	private List<DecklogChrono> chrono;
	
	@OneToMany(targetEntity=DecklogFirepatrol.class, cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="deck", fetch=FetchType.LAZY)
	private List<DecklogFirepatrol> firepatrol;
	
	
	
	
	@OneToMany(targetEntity=DecklogBellBook.class, cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="deck", fetch=FetchType.LAZY)
	private List<DecklogBellBook> bellBook;
	
	
	
	
	public DecklogFirepatrol getLastFirepatrol(){
		List<DecklogFirepatrol> lf = getFirepatrol();
		
		if (lf!=null) {

			Collections.sort(lf);
			
			if (lf.size()>0) {
				return lf.get(lf.size()-1);
			}
		}
		return null;
	}
	
	public DecklogUms getLastUms(){
		List<DecklogUms> lums = getUms();
		
		if (lums!=null) {

			Collections.sort(lums);
			
			if (lums.size()>0) {
				return lums.get(lums.size()-1);
			}
		}
		return null;
	}
	
	
	public DecklogShipPosition getLastShipPosition(){
		List<DecklogShipPosition> lsp = getPosition();
		
		if (lsp!=null) {

			Collections.sort(lsp);
			
			if (lsp.size()>0) {
				return lsp.get(lsp.size()-1);
			}
		}
		return null;
	}
	
	
	public DecklogCompass getLastCompass(){
		List<DecklogCompass> lc = getCompass();
		
		if (lc!=null) {

			Collections.sort(lc);
			
			if (lc.size()>0) {
				return lc.get(lc.size()-1);
			}
		}
		return null;
	}
	
	public DecklogChrono getLastChrono(){
		List<DecklogChrono> lch = getChrono();
		
		if (lch!=null) {

			Collections.sort(lch);
			
			if (lch.size()>0) {
				return lch.get(lch.size()-1);
			}
		}
		return null;
	}
	
	

	@Basic
	private Date registrationDate;
	
	
	/**
	 * Costruttore vuoto
	 */
	public Decklog() {
		super();
	}
	
	
	
	public VoyagePlan getVoyage() {
		return voyage;
	}

	public void setVoyage(VoyagePlan voyage) {
		this.voyage = voyage;
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

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}



	public List<DecklogUms> getUms() {
		return ums;
	}



	public void setUms(List<DecklogUms> ums) {
		this.ums = ums;
	}


	@Override
	public String toString() {
		return "Decklog [id=" + id + ", uuid=" + uuid + ", registrationDate="
				+ registrationDate 
				+ "]";
	}



	public List<DecklogShipPosition> getPosition() {
		return position;
	}



	public void setPosition(List<DecklogShipPosition> position) {
		this.position = position;
	}


	public List<DecklogCompass> getCompass() {
		return compass;
	}


	public void setCompass(List<DecklogCompass> compass) {
		this.compass = compass;
	}


	public List<DecklogChrono> getChrono() {
		return chrono;
	}


	public void setChrono(List<DecklogChrono> chrono) {
		this.chrono = chrono;
	}


	public List<DecklogFirepatrol> getFirepatrol() {
		return firepatrol;
	}


	public void setFirepatrol(List<DecklogFirepatrol> firepatrol) {
		this.firepatrol = firepatrol;
	}

	public Ship getShip() {
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}
}
