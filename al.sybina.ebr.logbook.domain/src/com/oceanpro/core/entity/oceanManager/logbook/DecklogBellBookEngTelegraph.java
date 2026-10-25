package com.oceanpro.core.entity.oceanManager.logbook;

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
@Table(name="ocean_logbook_decklog_bell_engteleg")
public class DecklogBellBookEngTelegraph implements Comparable<DecklogBellBookEngTelegraph>{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long id ; // Unique ID
	
	@ManyToOne (fetch = FetchType.EAGER) // No cascade
	private DecklogBellBook bellBook;
	
	@Basic
	private String uuid;
	
	@Basic
	private String fullAstern;

	@Basic
	private String halfAstern;
	
	@Basic
	private String slowAstern;

	@Basic
	private String deadAstern;
	
	@Basic
	private String stop;
	
	@Basic
	private String fullAhead;

	@Basic
	private String halfAhead;
	
	@Basic
	private String slowAhead;

	@Basic
	private String deadAhead;	
	
	
	
	
	
	@Column (length=5000) // 5000 caratteri
	private String remarks;
	
	
	
	/**
	 * Costruttore vuoto
	 */
	public DecklogBellBookEngTelegraph() {
		super();
	}



	public DecklogBellBookEngTelegraph(String fullAstern, String halfAstern,
			String slowAstern, String deadAstern, String stop,
			String fullAhead, String halfAhead, String slowAhead,
			String deadAhead) {
		super();

		this.fullAstern = fullAstern;
		this.halfAstern = halfAstern;
		this.slowAstern = slowAstern;
		this.deadAstern = deadAstern;
		this.stop = stop;
		this.fullAhead = fullAhead;
		this.halfAhead = halfAhead;
		this.slowAhead = slowAhead;
		this.deadAhead = deadAhead;
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








	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(DecklogBellBookEngTelegraph comp) {
		 if (getId()!=null && comp.getId()!=null && getId() > comp.getId()) return -1; 
		 else if (getId()!=null && comp.getId()!=null && getId().equals(comp.getId())) return 0;
		 else return 1;
	}






	public String getRemarks() {
		return remarks;
	}



	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getFullAstern() {
		return fullAstern;
	}



	public void setFullAstern(String fullAstern) {
		this.fullAstern = fullAstern;
	}



	public String getHalfAstern() {
		return halfAstern;
	}



	public void setHalfAstern(String halfAstern) {
		this.halfAstern = halfAstern;
	}



	public String getSlowAstern() {
		return slowAstern;
	}



	public void setSlowAstern(String slowAstern) {
		this.slowAstern = slowAstern;
	}



	public String getDeadAstern() {
		return deadAstern;
	}



	public void setDeadAstern(String deadAstern) {
		this.deadAstern = deadAstern;
	}



	public String getStop() {
		return stop;
	}



	public void setStop(String stop) {
		this.stop = stop;
	}



	public String getFullAhead() {
		return fullAhead;
	}



	public void setFullAhead(String fullAhead) {
		this.fullAhead = fullAhead;
	}



	public String getHalfAhead() {
		return halfAhead;
	}



	public void setHalfAhead(String halfAhead) {
		this.halfAhead = halfAhead;
	}



	public String getSlowAhead() {
		return slowAhead;
	}



	public void setSlowAhead(String slowAhead) {
		this.slowAhead = slowAhead;
	}



	public String getDeadAhead() {
		return deadAhead;
	}



	public void setDeadAhead(String deadAhead) {
		this.deadAhead = deadAhead;
	}



	public DecklogBellBook getBellBook() {
		return bellBook;
	}



	public void setBellBook(DecklogBellBook bellBook) {
		this.bellBook = bellBook;
	}










	
	
	
}
