package com.oceanpro.core.entity.oceanManager.logbook;

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


@Entity
@Table(name="ocean_logbook_decklog_firepatrol")
public class DecklogFirepatrol implements Comparable<DecklogFirepatrol>{

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long id ; // Unique ID
	
	@ManyToOne (fetch = FetchType.EAGER) // No cascade
	private Decklog deck;

	@Basic
	private String uuid;
	
	@Basic
	private Date time;
	
	
	//watch man
	@Basic
	private String wmName;
	
	@Basic
	private String wmSurname;
	
	@Basic
	private String wmRank;
	
	
	
	
	@OneToMany(targetEntity=DecklogFirepatrolCheckItem.class, cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="firepatrol", fetch=FetchType.LAZY)
	private List<DecklogFirepatrolCheckItem> checkItem;
	
	
	@Column (length=5000) // 5000 caratteri
	private String remarks;


	
	
	/**
	 * Costruttore vuoto
	 */
	public DecklogFirepatrol() {
		super();
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


	public Date getTime() {
		return time;
	}


	public void setTime(Date time) {
		this.time = time;
	}


	public String getRemarks() {
		return remarks;
	}


	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	public List<DecklogFirepatrolCheckItem> getCheckItem() {
		return checkItem;
	}


	public void setCheckItem(List<DecklogFirepatrolCheckItem> checkItem) {
		this.checkItem = checkItem;
	}


	@Override
	public String toString() {
		return "DecklogFirepatrol [id=" + id + ", uuid="
				+ uuid + ", time=" + time + ", checkItem=" + checkItem
				+ ", remarks=" + remarks + "]";
	}


	public String getWmName() {
		return wmName;
	}


	public void setWmName(String wmName) {
		this.wmName = wmName;
	}


	public String getWmSurname() {
		return wmSurname;
	}


	public void setWmSurname(String wmSurname) {
		this.wmSurname = wmSurname;
	}


	public String getWmRank() {
		return wmRank;
	}


	public void setWmRank(String wmRank) {
		this.wmRank = wmRank;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(DecklogFirepatrol comp) {
		 if (getTime()!=null && comp.getTime()!=null && getTime().after(comp.getTime())) return -1; 
		 else if (getTime()!=null && comp.getTime()!=null && getTime().equals(comp.getTime())) return 0;
		 else return 1;
	}
	
}
