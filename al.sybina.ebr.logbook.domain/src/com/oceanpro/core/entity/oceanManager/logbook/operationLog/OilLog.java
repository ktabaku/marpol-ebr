package com.oceanpro.core.entity.oceanManager.logbook.operationLog;

import java.sql.Timestamp;
import java.time.ZoneOffset;
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

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.oceanpro.core.entity.oceanManager.office.Ship;

/**
 * 
 * @author Gio
 *
 */
@Entity
@Table(name="ocean_logbook_operation_oil_log")
public class OilLog implements Comparable<OilLog> {
	
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
	private String type; // OIL1, OIL2, OIL3
	
	@Basic
	private String codeLetter;
	@Basic
	private String codeNumber;
	
	@Column(name="content",columnDefinition="LONG VARCHAR")
	private String content;
	

	@Column(columnDefinition="LONG VARCHAR")
	private String signature;
	
	@Basic
	private String groupUuid; // serve per indicare il raggruppamento, cio�, tutti i log che hanno lo stesso groupUuid sono stati inseriti insieme
	@Basic
	private String orderInGroup; // ordine di inserimento nel gruppo
	
	@Basic
	private String signerRank;
	@Basic
	private String signerName;
	@Basic
	private String signerSurname;
	

	@Basic
	private String note;
	@Basic
	private String correctionNote;
	
	@Basic
	private String masterSignerName;
	@Basic
	private String masterSignerSurname;
	@Column(columnDefinition="LONG VARCHAR")
	private String masterSignature;
	@Basic
	private String signatureGroupUuid;

	@Basic
	private Date masterSigDate;
	
	@Column(name="FROM_Z",columnDefinition="TIMESTAMP")
	private Timestamp FROM_Z;
	@Id
	@Column(name="THRU_Z",columnDefinition="TIMESTAMP")
	private Timestamp THRU_Z;
	@Column(name="IN_Z",columnDefinition="TIMESTAMP")
	private Timestamp IN_Z;
	@Id
	@Column(name="OUT_Z",columnDefinition="TIMESTAMP")
	private Timestamp OUT_Z;
	
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

	public String getCodeLetter() {
		return codeLetter;
	}

	public void setCodeLetter(String codeLetter) {
		this.codeLetter = codeLetter;
	}

	public String getCodeNumber() {
		return codeNumber;
	}

	public void setCodeNumber(String codeNumber) {
		this.codeNumber = codeNumber;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getGroupUuid() {
		return groupUuid;
	}

	public void setGroupUuid(String groupUuid) {
		this.groupUuid = groupUuid;
	}

	public String getOrderInGroup() {
		return orderInGroup;
	}

	public void setOrderInGroup(String orderInGroup) {
		this.orderInGroup = orderInGroup;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Ship getShip() {
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OilLog other = (OilLog) obj;
		if (codeLetter == null) {
			if (other.codeLetter != null)
				return false;
		} else if (!codeLetter.equals(other.codeLetter))
			return false;
		if (codeNumber == null) {
			if (other.codeNumber != null)
				return false;
		} else if (!codeNumber.equals(other.codeNumber))
			return false;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
//		if (date == null) {
//			if (other.date != null)
//				return false;
//		} else if (!date.toInstant().atZone(ZoneOffset.UTC).toLocalDate().equals(other.date.toInstant().atZone(ZoneOffset.UTC).toLocalDate()))
//			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (orderInGroup == null) {
			if (other.orderInGroup != null)
				return false;
		} else if (!orderInGroup.equals(other.orderInGroup))
			return false;
		if (ship == null) {
			if (other.ship != null)
				return false;
		} else if (!ship.equals(other.ship))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (signerRank == null) {
			if (other.signerRank != null)
				return false;
		} else if (!signerRank.equals(other.signerRank))
			return false;
		if (signerName == null) {
			if (other.signerName != null)
				return false;
		} else if (!signerName.equals(other.signerName))
			return false;
		if (signerSurname == null) {
			if (other.signerSurname != null)
				return false;
		} else if (!signerSurname.equals(other.signerSurname))
			return false;
//		if (note == null) {
//			if (other.note != null)
//				return false;
//		} else if (!note.equals(other.note))
//			return false;
//		if (correctionNote == null) {
//			if (other.correctionNote != null)
//				return false;
//		} else if (!correctionNote.equals(other.correctionNote))
//			return false;
		return true;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	@Override
	public Object clone() {
		OilLog oilLog = new OilLog();
		oilLog.setCodeLetter(codeLetter);
		oilLog.setCodeNumber(codeNumber);
		oilLog.setContent(content);
//		oilLog.setDate(date);
		oilLog.setGroupUuid(groupUuid);
		oilLog.setId(id);
		oilLog.setOrderInGroup(orderInGroup);
		oilLog.setShip(ship);
		oilLog.setSignature(signature);
		oilLog.setType(type);
		oilLog.setUuid(groupUuid);
		oilLog.setSignerRank(signerRank);
		oilLog.setSignerName(signerName);
		oilLog.setSignerSurname(signerSurname);
		oilLog.setNote(note);
		oilLog.setCorrectionNote(correctionNote);
		
		oilLog.setSignatureGroupUuid(signatureGroupUuid);
		
		oilLog.setMasterSignerName(masterSignerName);
		oilLog.setMasterSignerSurname(masterSignerSurname);
		oilLog.setMasterSignature(masterSignature);
		oilLog.setMasterSigDate(masterSigDate);
		
		return oilLog;
	}

	@Override
	public int compareTo(OilLog o) {
		if(o==null) return -1;
		return (this.getCodeLetter()+this.getCodeNumber()).compareTo(o.getCodeLetter()+o.getCodeNumber());
	}

	public String getSignerName() {
		return signerName;
	}

	public void setSignerName(String name) {
		this.signerName = name;
	}

	public String getSignerSurname() {
		return signerSurname;
	}

	public void setSignerSurname(String surname) {
		this.signerSurname = surname;
	}

	public void setSignerRank(String rank) {
		this.signerRank = rank;
	}

	public String getSignerRank() {
		return signerRank;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getNote() {
		return note;
	}

	public void setCorrectionNote(String correction) {
		this.correctionNote = correction;
	}

	public String getCorrectionNote() {
		return correctionNote;
	}

	
	public String getMasterSignerName() {
		return masterSignerName;
	}

	public void setMasterSignerName(String masterSignerName) {
		this.masterSignerName = masterSignerName;
	}

	public String getMasterSignerSurname() {
		return masterSignerSurname;
	}

	public void setMasterSignerSurname(String masterSignerSurname) {
		this.masterSignerSurname = masterSignerSurname;
	}

	public String getMasterSignature() {
		return masterSignature;
	}

	public void setMasterSignature(String masterSignature) {
		this.masterSignature = masterSignature;
	}

	public void setSignatureGroupUuid(String signatureGroupUuid) {
		this.signatureGroupUuid = signatureGroupUuid;
	}

	public String getSignatureGroupUuid() {
		return signatureGroupUuid;
	}
	
	public void setMasterSigDate(Date masterSigDate) {
		this.masterSigDate = masterSigDate;
	}

	public Date getMasterSigDate() {
		return masterSigDate;
	}
	
}
