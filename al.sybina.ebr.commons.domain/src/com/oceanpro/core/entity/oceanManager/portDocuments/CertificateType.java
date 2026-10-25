package com.oceanpro.core.entity.oceanManager.portDocuments;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

/**
 * 
 * @author Gio
 *
 */
@Entity
@javax.persistence.Table(name="ocean_documents_certificate_type")
public class CertificateType {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long id ; // Unique ID
	
	@Basic
	private String uuid;
	@Basic
	private String code;
	@Basic
	private String name;
	@Basic
	private String regulation;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name = "ocean_documents_rank_certificate_type", joinColumns = { @JoinColumn(name="certtypeId")}, inverseJoinColumns = { @JoinColumn(name="rankId") } )
	private List<Rank> ranks ;
   
	@Basic
	private int importance; // 0=optional 1=required 2=suggested
	
	/**
	 * Costruttore vuoto
	 */
	public CertificateType() {
		super();
	}

	

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}





	
	
	@Override
	public boolean equals(Object obj) {
		CertificateType rhs = (CertificateType) obj;
		boolean b = uuid==rhs.uuid;
		//System.out.println("id1=" + id + " id2=" + rhs.id +  " b="+b);
		return b;
		/*
		return new EqualsBuilder()
        //.appendSuper(super.equals(obj))
        .append(id, rhs.id)
        .append(name, rhs.name)
        .append(description, rhs.description)
        .isEquals();
		*/
		//return EqualsBuilder.reflectionEquals(this, obj);
	}
	// TODO da implementare...ci sono i lazy
	/*@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
	*/
	@Override
	public String toString() {
		return  "id=" + id +
				"\ncode=" + code +
				"\nname=" + name + 
				"\nregulation=" + regulation;
				
		//return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE,false);
	}

	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}



	public List<Rank> getRanks() {
		return ranks;
	}



	public void setRanks(List<Rank> ranks) {
		this.ranks = ranks;
	}



	public String getCode() {
		return code;
	}



	public void setCode(String code) {
		this.code = code;
	}



	public String getRegulation() {
		return regulation;
	}



	public void setRegulation(String regulation) {
		this.regulation = regulation;
	}



	public int getImportance() {
		return importance;
	}



	public void setImportance(int importance) {
		this.importance = importance;
	}





}