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
@Table(name="ocean_logbook_decklog_bell")
public class DecklogBellBook implements Comparable<DecklogBellBook>{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long id ; // Unique ID
	
	@ManyToOne (fetch = FetchType.EAGER) // No cascade
	private Decklog deck;

	
	@OneToMany(targetEntity=DecklogBellBookEngTelegraph.class, cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="bellBook", fetch=FetchType.LAZY)
	private List<DecklogBellBookEngTelegraph> bellBookEngTelegraph;
	
	
	
	@Basic
	private String uuid;
	
	@Basic
	private Date registrationDate;

	@Basic
	private String state;

	@Basic
	private String fromTo;
	
	@Basic
	@Column(name="\"DECKLOG_BELL_OF\"")
	private String of;	
	
	
	
	@Basic
	private String officerOnConning;	
	
	@Basic
	private Date atTime;
	
	@Basic
	private String helsmanName;
	
	@Basic
	private String bridgeCheckList;
	
	
	
	@Basic
	private Date reqTestAtTime;

	@Basic
	private Date masterOnConningAtTime;
	
	@Basic
	private String steeringCondition;
	

	
	//OPERATION
	@Basic
	private Date sbeDate;
	
	@Basic
	private Date fweDate;
	
	@Basic
	private Date readinessTenderedDate;
	
	@Basic
	private String pilot;
	
	@Basic
	private Date pilotTime;
	
	@Basic
	private Date dropAnchorTime;
	
	
	@Basic
	private Date anchorAwayTime;
	
	@Basic
	private Date firstLineAshoreTime;
	
	@Basic
	private Date gangwayAshoreTime;
	
	@Basic
	private Date allFastDate;
	
	
	
	@Basic
	private Date startSeaPassageDate;
	
	@Basic
	private Date endSeaPassageDate;
	
	@Basic
	private Date startHeavingUpDate;
	
	@Basic
	private Date tugFwdmfCastofTime;
	
	@Basic
	private Date tugMidmfCastofTime;
	
	@Basic
	private Date tugAftCastofTime;
	
	
	
	@Basic
	private String otherOperationADesc;
	
	@Basic
	private String otherOperationBDesc;
	
	@Basic
	private String otherOperationCDesc;
	
	@Basic
	private String otherOperationDDesc;
	
	@Basic
	private String otherOperationEDesc;
	
	@Basic
	private String otherOperationFDesc;
	
	@Basic
	private String otherOperationGDesc;
	
	
	@Basic
	private Date otherOperationATime;
	
	@Basic
	private Date otherOperationBTime;

	@Basic
	private Date otherOperationCTime;

	@Basic
	private Date otherOperationDTime;

	@Basic
	private Date otherOperationETime;

	@Basic
	private Date otherOperationFTime;

	@Basic
	private Date otherOperationGTime;
	
	
	
	//ROB - SBE e FWE
	
	@Basic
	private String robSbeFlag;

	
	@Basic
	private Double sbeLsfo;

	@Basic
	private Double sbeHsfo;
	
	@Basic
	private Double sbeDo;
	
	@Basic
	private Double sbeLo;
	
	@Basic
	private Double sbeFw;
	
	
	
	@Basic
	private String robFweFlag;
	
	
	@Basic
	private Double fweLsfo;

	@Basic
	private Double fweHsfo;
	
	@Basic
	private Double fweDo;
	
	@Basic
	private Double fweLo;
	
	@Basic
	private Double fweFw;

	
	
	//ENGINE TELEGRAPH
	
	//TODO
	
	
	
	
	
	@Column (length=5000) // 5000 caratteri
	private String remarks;
	
	
	
	/**
	 * Costruttore vuoto
	 */
	public DecklogBellBook() {
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



	public Date getRegistrationDate() {
		return registrationDate;
	}



	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}





	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(DecklogBellBook comp) {
		 if (getRegistrationDate()!=null && comp.getRegistrationDate()!=null && getRegistrationDate().after(comp.getRegistrationDate())) return -1; 
		 else if (getRegistrationDate()!=null && comp.getRegistrationDate()!=null && getRegistrationDate().equals(comp.getRegistrationDate())) return 0;
		 else return 1;
	}


	@Override
	public String toString() {
		return "DecklogBellBook [id=" + id + ", uuid=" + uuid
				+ ", registrationDate=" + registrationDate + ", state=" + state
				+ ", fromTo=" + fromTo + ", of=" + of + ", officerOnConning="
				+ officerOnConning + ", atTime=" + atTime + ", helsmanName="
				+ helsmanName + ", bridgeCheckList=" + bridgeCheckList
				+ ", reqTestAtTime=" + reqTestAtTime
				+ ", masterOnConningAtTime=" + masterOnConningAtTime
				+ ", steeringCondition=" + steeringCondition + ", sbeDate="
				+ sbeDate + ", fweDate=" + fweDate + ", readinessTenderedDate="
				+ readinessTenderedDate + ", pilot=" + pilot + ", pilotTime="
				+ pilotTime + ", dropAnchorTime=" + dropAnchorTime
				+ ", firstLineAshoreTime=" + firstLineAshoreTime
				+ ", gangwayAshoreTime=" + gangwayAshoreTime + ", allFastDate="
				+ allFastDate + ", startSeaPassageDate=" + startSeaPassageDate
				+ ", endSeaPassageDate=" + endSeaPassageDate
				+ ", startHeavingUpDate=" + startHeavingUpDate
				+ ", tugFwdmfCastofTime=" + tugFwdmfCastofTime
				+ ", tugMidmfCastofTime=" + tugMidmfCastofTime
				+ ", tugAftCastofTime=" + tugAftCastofTime
				+ ", otherOperationADesc=" + otherOperationADesc
				+ ", otherOperationBDesc=" + otherOperationBDesc
				+ ", otherOperationCDesc=" + otherOperationCDesc
				+ ", otherOperationDDesc=" + otherOperationDDesc
				+ ", otherOperationEDesc=" + otherOperationEDesc
				+ ", otherOperationFDesc=" + otherOperationFDesc
				+ ", otherOperationGDesc=" + otherOperationGDesc
				+ ", otherOperationATime=" + otherOperationATime
				+ ", otherOperationBTime=" + otherOperationBTime
				+ ", otherOperationCTime=" + otherOperationCTime
				+ ", otherOperationDTime=" + otherOperationDTime
				+ ", otherOperationETime=" + otherOperationETime
				+ ", otherOperationFTime=" + otherOperationFTime
				+ ", otherOperationGTime=" + otherOperationGTime
				+ ", robSbeOrFwe=" + ", sbeLsfo=" + sbeLsfo
				+ ", sbeHsfo=" + sbeHsfo + ", sbeDo=" + sbeDo + ", sbeLo="
				+ sbeLo + ", sbeFw=" + sbeFw + ", fweLsfo=" + fweLsfo
				+ ", fweHsfo=" + fweHsfo + ", fweDo=" + fweDo + ", fweLo="
				+ fweLo + ", fweFw=" + fweFw + ", remarks=" + remarks + "]";
	}




	public String getState() {
		return state;
	}



	public void setState(String state) {
		this.state = state;
	}



	public String getFromTo() {
		return fromTo;
	}



	public void setFromTo(String fromTo) {
		this.fromTo = fromTo;
	}



	public String getOf() {
		return of;
	}



	public void setOf(String of) {
		this.of = of;
	}



	public String getOfficerOnConning() {
		return officerOnConning;
	}



	public void setOfficerOnConning(String officerOnConning) {
		this.officerOnConning = officerOnConning;
	}



	public Date getAtTime() {
		return atTime;
	}



	public void setAtTime(Date atTime) {
		this.atTime = atTime;
	}



	public String getHelsmanName() {
		return helsmanName;
	}



	public void setHelsmanName(String helsmanName) {
		this.helsmanName = helsmanName;
	}



	public String getBridgeCheckList() {
		return bridgeCheckList;
	}



	public void setBridgeCheckList(String bridgeCheckList) {
		this.bridgeCheckList = bridgeCheckList;
	}



	public Date getReqTestAtTime() {
		return reqTestAtTime;
	}



	public void setReqTestAtTime(Date reqTestAtTime) {
		this.reqTestAtTime = reqTestAtTime;
	}



	public Date getMasterOnConningAtTime() {
		return masterOnConningAtTime;
	}



	public void setMasterOnConningAtTime(Date masterOnConningAtTime) {
		this.masterOnConningAtTime = masterOnConningAtTime;
	}



	public String getSteeringCondition() {
		return steeringCondition;
	}



	public void setSteeringCondition(String steeringCondition) {
		this.steeringCondition = steeringCondition;
	}



	public Date getSbeDate() {
		return sbeDate;
	}



	public void setSbeDate(Date sbeDate) {
		this.sbeDate = sbeDate;
	}



	public Date getFweDate() {
		return fweDate;
	}



	public void setFweDate(Date fweDate) {
		this.fweDate = fweDate;
	}



	public Date getReadinessTenderedDate() {
		return readinessTenderedDate;
	}



	public void setReadinessTenderedDate(Date readinessTenderedDate) {
		this.readinessTenderedDate = readinessTenderedDate;
	}



	public String getPilot() {
		return pilot;
	}



	public void setPilot(String pilot) {
		this.pilot = pilot;
	}



	public Date getPilotTime() {
		return pilotTime;
	}



	public void setPilotTime(Date pilotTime) {
		this.pilotTime = pilotTime;
	}



	public Date getDropAnchorTime() {
		return dropAnchorTime;
	}



	public void setDropAnchorTime(Date dropAnchorTime) {
		this.dropAnchorTime = dropAnchorTime;
	}



	public Date getFirstLineAshoreTime() {
		return firstLineAshoreTime;
	}



	public void setFirstLineAshoreTime(Date firstLineAshoreTime) {
		this.firstLineAshoreTime = firstLineAshoreTime;
	}



	public Date getGangwayAshoreTime() {
		return gangwayAshoreTime;
	}



	public void setGangwayAshoreTime(Date gangwayAshoreTime) {
		this.gangwayAshoreTime = gangwayAshoreTime;
	}



	public Date getAllFastDate() {
		return allFastDate;
	}



	public void setAllFastDate(Date allFastDate) {
		this.allFastDate = allFastDate;
	}



	public Date getStartSeaPassageDate() {
		return startSeaPassageDate;
	}



	public void setStartSeaPassageDate(Date startSeaPassageDate) {
		this.startSeaPassageDate = startSeaPassageDate;
	}



	public Date getEndSeaPassageDate() {
		return endSeaPassageDate;
	}



	public void setEndSeaPassageDate(Date endSeaPassageDate) {
		this.endSeaPassageDate = endSeaPassageDate;
	}



	public Date getStartHeavingUpDate() {
		return startHeavingUpDate;
	}



	public void setStartHeavingUpDate(Date startHeavingUpDate) {
		this.startHeavingUpDate = startHeavingUpDate;
	}



	public Date getTugFwdmfCastofTime() {
		return tugFwdmfCastofTime;
	}



	public void setTugFwdmfCastofTime(Date tugFwdmfCastofTime) {
		this.tugFwdmfCastofTime = tugFwdmfCastofTime;
	}



	public Date getTugMidmfCastofTime() {
		return tugMidmfCastofTime;
	}



	public void setTugMidmfCastofTime(Date tugMidmfCastofTime) {
		this.tugMidmfCastofTime = tugMidmfCastofTime;
	}



	public Date getTugAftCastofTime() {
		return tugAftCastofTime;
	}



	public void setTugAftCastofTime(Date tugAftCastofTime) {
		this.tugAftCastofTime = tugAftCastofTime;
	}



	public String getOtherOperationADesc() {
		return otherOperationADesc;
	}



	public void setOtherOperationADesc(String otherOperationADesc) {
		this.otherOperationADesc = otherOperationADesc;
	}



	public String getOtherOperationBDesc() {
		return otherOperationBDesc;
	}



	public void setOtherOperationBDesc(String otherOperationBDesc) {
		this.otherOperationBDesc = otherOperationBDesc;
	}



	public String getOtherOperationCDesc() {
		return otherOperationCDesc;
	}



	public void setOtherOperationCDesc(String otherOperationCDesc) {
		this.otherOperationCDesc = otherOperationCDesc;
	}



	public String getOtherOperationDDesc() {
		return otherOperationDDesc;
	}



	public void setOtherOperationDDesc(String otherOperationDDesc) {
		this.otherOperationDDesc = otherOperationDDesc;
	}



	public String getOtherOperationEDesc() {
		return otherOperationEDesc;
	}



	public void setOtherOperationEDesc(String otherOperationEDesc) {
		this.otherOperationEDesc = otherOperationEDesc;
	}



	public String getOtherOperationFDesc() {
		return otherOperationFDesc;
	}



	public void setOtherOperationFDesc(String otherOperationFDesc) {
		this.otherOperationFDesc = otherOperationFDesc;
	}



	public String getOtherOperationGDesc() {
		return otherOperationGDesc;
	}



	public void setOtherOperationGDesc(String otherOperationGDesc) {
		this.otherOperationGDesc = otherOperationGDesc;
	}



	public Date getOtherOperationATime() {
		return otherOperationATime;
	}



	public void setOtherOperationATime(Date otherOperationATime) {
		this.otherOperationATime = otherOperationATime;
	}



	public Date getOtherOperationBTime() {
		return otherOperationBTime;
	}



	public void setOtherOperationBTime(Date otherOperationBTime) {
		this.otherOperationBTime = otherOperationBTime;
	}



	public Date getOtherOperationCTime() {
		return otherOperationCTime;
	}



	public void setOtherOperationCTime(Date otherOperationCTime) {
		this.otherOperationCTime = otherOperationCTime;
	}



	public Date getOtherOperationDTime() {
		return otherOperationDTime;
	}



	public void setOtherOperationDTime(Date otherOperationDTime) {
		this.otherOperationDTime = otherOperationDTime;
	}



	public Date getOtherOperationETime() {
		return otherOperationETime;
	}



	public void setOtherOperationETime(Date otherOperationETime) {
		this.otherOperationETime = otherOperationETime;
	}



	public Date getOtherOperationFTime() {
		return otherOperationFTime;
	}



	public void setOtherOperationFTime(Date otherOperationFTime) {
		this.otherOperationFTime = otherOperationFTime;
	}



	public Date getOtherOperationGTime() {
		return otherOperationGTime;
	}



	public void setOtherOperationGTime(Date otherOperationGTime) {
		this.otherOperationGTime = otherOperationGTime;
	}


	public Double getSbeLsfo() {
		return sbeLsfo;
	}



	public void setSbeLsfo(Double sbeLsfo) {
		this.sbeLsfo = sbeLsfo;
	}



	public Double getSbeHsfo() {
		return sbeHsfo;
	}



	public void setSbeHsfo(Double sbeHsfo) {
		this.sbeHsfo = sbeHsfo;
	}



	public Double getSbeDo() {
		return sbeDo;
	}



	public void setSbeDo(Double sbeDo) {
		this.sbeDo = sbeDo;
	}



	public Double getSbeLo() {
		return sbeLo;
	}



	public void setSbeLo(Double sbeLo) {
		this.sbeLo = sbeLo;
	}



	public Double getSbeFw() {
		return sbeFw;
	}



	public void setSbeFw(Double sbeFw) {
		this.sbeFw = sbeFw;
	}



	public Double getFweLsfo() {
		return fweLsfo;
	}



	public void setFweLsfo(Double fweLsfo) {
		this.fweLsfo = fweLsfo;
	}



	public Double getFweHsfo() {
		return fweHsfo;
	}



	public void setFweHsfo(Double fweHsfo) {
		this.fweHsfo = fweHsfo;
	}



	public Double getFweDo() {
		return fweDo;
	}



	public void setFweDo(Double fweDo) {
		this.fweDo = fweDo;
	}



	public Double getFweLo() {
		return fweLo;
	}



	public void setFweLo(Double fweLo) {
		this.fweLo = fweLo;
	}



	public Double getFweFw() {
		return fweFw;
	}



	public void setFweFw(Double fweFw) {
		this.fweFw = fweFw;
	}



	public String getRemarks() {
		return remarks;
	}



	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}



	public Date getAnchorAwayTime() {
		return anchorAwayTime;
	}



	public void setAnchorAwayTime(Date anchorAwayTime) {
		this.anchorAwayTime = anchorAwayTime;
	}



	public String getRobSbeFlag() {
		return robSbeFlag;
	}



	public void setRobSbeFlag(String robSbeFlag) {
		this.robSbeFlag = robSbeFlag;
	}



	public String getRobFweFlag() {
		return robFweFlag;
	}



	public void setRobFweFlag(String robFweFlag) {
		this.robFweFlag = robFweFlag;
	}



	public List<DecklogBellBookEngTelegraph> getBellBookEngTelegraph() {
		return bellBookEngTelegraph;
	}



	public void setBellBookEngTelegraph(List<DecklogBellBookEngTelegraph> bellBookEngTelegraph) {
		this.bellBookEngTelegraph = bellBookEngTelegraph;
	}







	
	
	
}
