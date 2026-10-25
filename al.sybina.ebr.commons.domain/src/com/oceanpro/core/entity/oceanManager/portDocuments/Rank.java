package com.oceanpro.core.entity.oceanManager.portDocuments;


import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
//import org.hibernate.annotations.Cache;
//import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 
 * @author Gio
 *
 */
@Entity
@Table(name="ocean_documents_crew_rank")
//@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Rank {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long id ; // Unique ID
	
	@Basic
	private String uuid;
	@Basic
	private String name;
	@Basic
	private String code;
	@Basic
//	@Column(name = "`order`")
	@Column(name = "\"RANK_ORDER\"")
	private Integer order; // valore per ordinamento
	
	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinTable(name = "ocean_documents_rank_certificate_type", joinColumns = { @JoinColumn(name="rankId")}, inverseJoinColumns = { @JoinColumn(name="certtypeId") } )
	private List<CertificateType> certificates;
	
	/**
	 * Costruttore vuoto
	 */
	public Rank() {
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
		Rank rhs = (Rank) obj;
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

	@Override
	public int hashCode() {
		String[] excludeFields = {"certificates"};
		return HashCodeBuilder.reflectionHashCode(this,excludeFields);
	}
	
	@Override
	public String toString() {
		String res = "";
		res += "id=" + id;
		res += "\nuuid=" + uuid;
		res += "\nname=" + name;
		res += "\ncode=" + code;
		res += "\norder=" + order;
		return res;
		//return ToStringBuilder.reflectionToString(this);
	}



	public String getUuid() {
		return uuid;
	}




	public void setUuid(String uuid) {
		this.uuid = uuid;
	}




	public String getCode() {
		return code;
	}




	public void setCode(String code) {
		this.code = code;
	}




	public List<CertificateType> getCertificates() {
		return certificates;
	}




	public void setCertificates(List<CertificateType> certificates) {
		this.certificates = certificates;
	}




	public Integer getOrder() {
		return order;
	}




	public void setOrder(Integer order) {
		this.order = order;
	}



}