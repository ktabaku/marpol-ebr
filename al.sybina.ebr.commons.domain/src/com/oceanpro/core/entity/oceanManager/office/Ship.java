package com.oceanpro.core.entity.oceanManager.office;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * @author Gio
 *
 */
@Entity
@Table(name="ocean_office_ship")
public class Ship implements Serializable {

	private static final long serialVersionUID = -3537791800724332483L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long id ; // Unique ID
	
	@Basic
	private String uuid;
	@Basic
	private String name;
	@Basic
	private String description;
//	@Column(columnDefinition="INT(1)")
//	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Basic
	private boolean enabled;
	@Basic
	private ShipType type;
	
//	@Basic
//	private String adminEmail;
//	@Basic
//	private String mailHost;
//	@Basic
//	private String mailPassword;
//	@Basic
//	private int mailPort;
//	@Basic
//	private String mailProtocol;
//	@Basic
//	private String mailUserName;
//	@Column
//	@Type(type = "org.hibernate.type.NumericBooleanType")
//	private boolean mailSmtpSSLEnabled;
	
	@Basic
	private int communicationType; // 0=immediato; 1=on-demand, 2=a tempo
	@Basic
	private String url;
	@Basic
	private int port;
//	@Column(columnDefinition="INT(1)")
//	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Basic
	private boolean communicationCompressed;
	
	
	/* ********** Unimare ************** */
	@Basic
	private String classificazione;
	@Basic
	private String tipo;
//	@Column(columnDefinition="INT(1)")
//	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Basic
	private boolean internazionale;
//	@Column(columnDefinition="INT(1)")
//	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Basic
	private boolean stazzaMin5Ton;
	@Basic
	private String denominazione;
	@Basic
	private String numeroIscrizione;
	@Basic
	private String numeroIMO;
	@Basic
	private String luogoIscrizione;
	
	/* ********************************* */
	
	
	@Basic
	private String shipDistinctiveNumber;
	@Basic
	private String shipPortOfRegistry;
	@Basic
	private String shipGrossTonnage;
	@Basic
	private Double shipCarringCapacity;
	@Basic
	private Double shipDeadweight;
	@Basic
	private Double shipLength;
	@Basic
	private Date shipBuildingContractDate;
	@Basic
	private Date shipKeelLaidDate;
	@Basic
	private Date shipDeliveryDate;
	
	/**
	 * Costruttore vuoto
	 */
	public Ship() {
		super();
		enabled = true;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public boolean getEnabled() {
		return enabled;
	}



	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}




	
	
	
	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
	
	@Override
	public String toString() {
//		return  "documentId=" + documentId +
//				"\nname=" + name + 
//				"\ndescription=" + description +
//				"\npath=" + path+
//				"\nversion=" + version;
				
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE,false);
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


	public ShipType getType() {
		return type;
	}


	public void setType(ShipType type) {
		this.type = type;
	}


	public int getCommunicationType() {
		return communicationType;
	}


	public void setCommunicationType(int communicationType) {
		this.communicationType = communicationType;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public int getPort() {
		return port;
	}


	public void setPort(int port) {
		this.port = port;
	}


	public boolean isCommunicationCompressed() {
		return communicationCompressed;
	}


	public void setCommunicationCompressed(boolean communicationCompressed) {
		this.communicationCompressed = communicationCompressed;
	}


	public String getClassificazione() {
		return classificazione;
	}


	public void setClassificazione(String classificazione) {
		this.classificazione = classificazione;
	}


	public String getTipo() {
		return tipo;
	}


	public void setTipo(String tipo) {
		this.tipo = tipo;
	}


	public boolean isInternazionale() {
		return internazionale;
	}


	public void setInternazionale(boolean internazionale) {
		this.internazionale = internazionale;
	}


	public boolean isStazzaMin5Ton() {
		return stazzaMin5Ton;
	}


	public void setStazzaMin5Ton(boolean stazzaMin5Ton) {
		this.stazzaMin5Ton = stazzaMin5Ton;
	}


	public String getDenominazione() {
		return denominazione;
	}


	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}


	public String getNumeroIscrizione() {
		return numeroIscrizione;
	}


	public void setNumeroIscrizione(String numeroIscrizione) {
		this.numeroIscrizione = numeroIscrizione;
	}


	public String getNumeroIMO() {
		return numeroIMO;
	}


	public void setNumeroIMO(String numeroIMO) {
		this.numeroIMO = numeroIMO;
	}


	public String getLuogoIscrizione() {
		return luogoIscrizione;
	}


	public void setLuogoIscrizione(String luogoIscrizione) {
		this.luogoIscrizione = luogoIscrizione;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public String getShipDistinctiveNumber() {
		return shipDistinctiveNumber;
	}


	public void setShipDistinctiveNumber(String shipDistinctiveNumber) {
		this.shipDistinctiveNumber = shipDistinctiveNumber;
	}


	public String getShipPortOfRegistry() {
		return shipPortOfRegistry;
	}


	public void setShipPortOfRegistry(String shipPortOfRegistry) {
		this.shipPortOfRegistry = shipPortOfRegistry;
	}


	public String getShipGrossTonnage() {
		return shipGrossTonnage;
	}


	public void setShipGrossTonnage(String shipGrossTonnage) {
		this.shipGrossTonnage = shipGrossTonnage;
	}


	public Double getShipCarringCapacity() {
		return shipCarringCapacity;
	}


	public void setShipCarringCapacity(Double shipCarringCapacity) {
		this.shipCarringCapacity = shipCarringCapacity;
	}


	public Double getShipDeadweight() {
		return shipDeadweight;
	}


	public void setShipDeadweight(Double shipDeadweight) {
		this.shipDeadweight = shipDeadweight;
	}


	public Double getShipLength() {
		return shipLength;
	}


	public void setShipLength(Double shipLength) {
		this.shipLength = shipLength;
	}


	public Date getShipBuildingContractDate() {
		return shipBuildingContractDate;
	}


	public void setShipBuildingContractDate(Date shipBuildingContractDate) {
		this.shipBuildingContractDate = shipBuildingContractDate;
	}


	public Date getShipKeelLaidDate() {
		return shipKeelLaidDate;
	}


	public void setShipKeelLaidDate(Date shipKeelLaidDate) {
		this.shipKeelLaidDate = shipKeelLaidDate;
	}


	public Date getShipDeliveryDate() {
		return shipDeliveryDate;
	}


	public void setShipDeliveryDate(Date shipDeliveryDate) {
		this.shipDeliveryDate = shipDeliveryDate;
	}
	
	
}