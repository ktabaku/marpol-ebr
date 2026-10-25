package al.sybina.ebr.logbook.domain.reladomo;

import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.oceanpro.core.entity.oceanManager.logbook.operationLog.Tank;

import javafx.util.Pair;
/**
 * 
 * @author Gio
 *
 */
@Entity
@Table(name="ocean_logbook_operation_oil_log")
public class OilLog extends OilLogAbstract implements Comparable<OilLog> {

	private List<Pair<String, Tank>> tankPairList;
	private Map<Tank, Double> userTankActualLevels;
	private String signatureTempFileName;
	
	public OilLog(Timestamp businessDate, Timestamp processingDate) {
		super(businessDate, processingDate);
		// You must not modify this constructor. Mithra calls this internally.
		// You can call this constructor. You can also add new constructors.
	}

	
	public OilLog(Timestamp businessDate) {
		super(businessDate);
	}
	
	
	public List<Pair<String, Tank>> getTankPairList() {
		return tankPairList;
	}
	public void setTankPairList(List<Pair<String, Tank>> tankPairList) {
		this.tankPairList = tankPairList;
	}


	public Map<Tank, Double> getUserTankActualLevels() {
		return userTankActualLevels;
	}
	public void setUserTankActualLevels(Map<Tank, Double> userTankActualLevels) {
		this.userTankActualLevels = userTankActualLevels;
	}

	public String getSignatureTempFileName() {
		return signatureTempFileName;
	}


	public void setSignatureTempFileName(String signatureTempFileName) {
		this.signatureTempFileName = signatureTempFileName;
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
		if (getCodeLetter() == null) {
			if (other.getCodeLetter() != null)
				return false;
		} else if (!getCodeLetter().equals(other.getCodeLetter()))
			return false;
		if (getCodeNumber() == null) {
			if (other.getCodeNumber() != null)
				return false;
		} else if (!getCodeNumber().equals(other.getCodeNumber()))
			return false;
		if (getContent() == null) {
			if (other.getContent() != null)
				return false;
		} else if (!getContent().equals(other.getContent()))
			return false;
		if (getBusinessDate() == null) {
			if (other.getBusinessDate() != null)
				return false;
		} else if (!(getBusinessDate().toInstant().truncatedTo( ChronoUnit.MINUTES ).equals(other.getBusinessDate().toInstant().truncatedTo( ChronoUnit.MINUTES ))))
			return false;
		if (getId() != other.getId())
			return false;
//		if (getOrderInGroup() == null) {
//			if (other.getOrderInGroup() != null)
//				return false;
//		} else if (!getOrderInGroup().equals(other.getOrderInGroup()))
//			return false;
//		if (ship == null) {
//			if (other.ship != null)
//				return false;
//		} else if (!ship.equals(other.ship))
//			return false;
//		if (type == null) {
//			if (other.type != null)
//				return false;
//		} else if (!type.equals(other.type))
//			return false;
		if (getSignerRank() == null) {
			if (other.getSignerRank() != null)
				return false;
		} else if (!getSignerRank().equals(other.getSignerRank()))
			return false;
		if (getSignerName() == null) {
			if (other.getSignerName() != null)
				return false;
		} else if (!getSignerName().equals(other.getSignerName()))
			return false;
		if (getSignerSurname() == null) {
			if (other.getSignerSurname() != null)
				return false;
		} else if (!getSignerSurname().equals(other.getSignerSurname()))
			return false;
//		if (getNote() == null) {
//			if (other.getNote() != null)
//				return false;
//		} else if (!getNote().equals(other.getNote()))
//			return false;
		if (getCorrectionNote() == null) {
			if (other.getCorrectionNote() != null)
				return false;
		} else if (!getCorrectionNote().equals(other.getCorrectionNote()))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this).
			       append("Type", this.getType()).
			       append("CodeLetter", this.getCodeLetter()).
			       append("CodeNumber", this.getCodeNumber()).
			       append("Content", this.getContent()).
			       toString();
	}
	
	@Override
	public Object clone() {
		OilLog oilLog = new OilLog(this.getBusinessDate());
		oilLog.setType(getType());
		oilLog.setCodeLetter(getCodeLetter());
		oilLog.setCodeNumber(getCodeNumber());
		oilLog.setContent(getContent());
		oilLog.setGroupUuid(getGroupUuid());
		oilLog.setId(getId());
		oilLog.setOrderInGroup(getOrderInGroup());
		oilLog.setShip(getShip());
		oilLog.setSignature(getSignature());
		oilLog.setUuid(getUuid());
		oilLog.setSignerRank(getSignerRank());
		oilLog.setSignerName(getSignerName());
		oilLog.setSignerSurname(getSignerSurname());
		oilLog.setNote(getNote());
		oilLog.setCorrectionNote(getCorrectionNote());
		
		oilLog.setSignatureGroupUuid(getSignatureGroupUuid());
		
//		oilLog.setChiefEngSignerName(getChiefEngSignerName());
//		oilLog.setChiefEngSignerSurname(getChiefEngSignerName());
//		oilLog.setChiefEngSignature(getChiefEngSignature());
//		oilLog.setChiefEngSigDate(getChiefEngSigDate());
		
		oilLog.setMasterSignerName(getMasterSignerName());
		oilLog.setMasterSignerSurname(getMasterSignerSurname());
		oilLog.setMasterSignature(getMasterSignature());
		oilLog.setMasterSigDate(getMasterSigDate());
		
		return oilLog;
	}

	@Override
	public int compareTo(OilLog o) {
		if(o==null) return -1;
		int idCompare = 0;
		int processingDateFromCompare = 0;
		int businessDateCompare = 0;
		int codeLetterCompare = 0;
		int orderInGroupCompare = 0;
		int codeNumberCompare = 0;
		int processingDateToCompare = 0;
		
		idCompare = Long.compare(this.getId(), o.getId());
		if(idCompare!=0) {
			return idCompare;
		} else {
			if(this.getProcessingDateFrom()!=null && o.getProcessingDateFrom()!=null) processingDateFromCompare = this.getProcessingDateFrom().toInstant().truncatedTo( ChronoUnit.SECONDS ).compareTo(o.getProcessingDateFrom().toInstant().truncatedTo( ChronoUnit.SECONDS ));
			if(processingDateFromCompare!=0) {
				return processingDateFromCompare;
			} else {
				orderInGroupCompare = (this.getOrderInGroup()).compareTo(o.getOrderInGroup());
				if(orderInGroupCompare!=0) {
					return orderInGroupCompare;
				} else {
					if(this.getBusinessDate()!=null && o.getBusinessDate()!=null) businessDateCompare = this.getBusinessDate().toInstant().truncatedTo( ChronoUnit.SECONDS ).compareTo(o.getBusinessDate().toInstant().truncatedTo( ChronoUnit.SECONDS ));
					if(businessDateCompare!=0) {
						return businessDateCompare;
					} else {
						codeLetterCompare = (this.getCodeLetter()).compareTo(o.getCodeLetter());
						if(codeLetterCompare!=0 && (!(this.getCodeLetter().equals("I") && this.getCodeNumber().equals("")) && !(o.getCodeLetter().equals("I") && o.getCodeNumber().equals("")))) {
							return codeLetterCompare;
						} else {
							codeNumberCompare = ((Double)Double.parseDouble(this.getCodeNumber())).compareTo((Double)Double.parseDouble(o.getCodeNumber()));
							if(codeNumberCompare!=0) {
								return codeNumberCompare;
							} else {
								if(this.getProcessingDateTo()!=null && o.getProcessingDateTo()!=null) processingDateToCompare = this.getProcessingDateTo().toInstant().truncatedTo( ChronoUnit.SECONDS ).compareTo(o.getProcessingDateTo().toInstant().truncatedTo( ChronoUnit.SECONDS ));
								if(processingDateToCompare!=0) {
									return processingDateToCompare;
								} else {
									return 0;
								}
							}
						}
					}
				}
			}
		}
	}

}
