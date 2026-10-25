package al.sybina.ebr.s63;

import java.util.Date;

public class S63SerialEncRecord {
	
	private String dataServerId;
	private String weekOfIssue;
	private Date dateOfPublication;
	private String cdType;
	private double formatVersion;
	private String exchangeSetNumber;
	
	
	public String getDataServerId() {
		return dataServerId;
	}
	public void setDataServerId(String dataServerId) {
		this.dataServerId = dataServerId;
	}
	public String getWeekOfIssue() {
		return weekOfIssue;
	}
	public void setWeekOfIssue(String weekOfIssue) {
		this.weekOfIssue = weekOfIssue;
	}
	public Date getDateOfPublication() {
		return dateOfPublication;
	}
	public void setDateOfPublication(Date dateOfPublication) {
		this.dateOfPublication = dateOfPublication;
	}
	public String getCdType() {
		return cdType;
	}
	public void setCdType(String cdType) {
		this.cdType = cdType;
	}
	public double getFormatVersion() {
		return formatVersion;
	}
	public void setFormatVersion(double formatVersion) {
		this.formatVersion = formatVersion;
	}
	public String getExchangeSetNumber() {
		return exchangeSetNumber;
	}
	public void setExchangeSetNumber(String exchangeSetNumber) {
		this.exchangeSetNumber = exchangeSetNumber;
	}
}
