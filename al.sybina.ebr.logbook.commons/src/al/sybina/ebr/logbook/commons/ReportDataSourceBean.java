package al.sybina.ebr.logbook.commons;

import java.util.Collections;
import java.util.List;

import al.sybina.ebr.logbook.domain.reladomo.OilLog;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.util.Date;

public class ReportDataSourceBean implements Comparable<ReportDataSourceBean> {

	private String shipName;
	private String shipCode;
	private String partAndVersion;
	private Date compilationDate;
	private String signatureFileName;
	private String rank;
	private String name;
	private String surname;
	private String missedRank;
	private String missedName;
	private String missedSurname;
	private Date compilationDateMissedLetter;
	private String signatureFileNameMissedLetter;
	
	private List<OilLog> oilLogList;

	public String getShipName() {
		return shipName;
	}

	public void setShipName(String shipName) {
		this.shipName = shipName;
	}

	public String getShipCode() {
		return shipCode;
	}

	public void setShipCode(String shipCode) {
		this.shipCode = shipCode;
	}

	public Date getCompilationDate() {
		return compilationDate;
	}

	public void setCompilationDate(Date compilationDate) {
		this.compilationDate = compilationDate;
	}

	public String getSignatureFileName() {
		return signatureFileName;
	}

	public void setSignatureFileName(String signatureFileName) {
		this.signatureFileName = signatureFileName;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public List<OilLog> getOilLogList() {
//		Collections.sort(this.oilLogList);
		return oilLogList;
	}

	public void setOilLogList(List<OilLog> oilLogList) {
		this.oilLogList = oilLogList;
	}
	
	public JRBeanCollectionDataSource getOilLogCollectionDataSource() {
		return new JRBeanCollectionDataSource(oilLogList);
	}

	@Override
	public int compareTo(ReportDataSourceBean o) {
		return this.getOilLogList().get(0).compareTo(o.getOilLogList().get(0));
	}

	public String getPartAndVersion() {
		return partAndVersion;
	}

	public void setPartAndVersion(String partAndVersion) {
		this.partAndVersion = partAndVersion;
	}

	public Date getCompilationDateMissedLetter() {
		return compilationDateMissedLetter;
	}

	public void setCompilationDateMissedLetter(Date compilationDateMissedLetter) {
		this.compilationDateMissedLetter = compilationDateMissedLetter;
	}

	public String getSignatureFileNameMissedLetter() {
		return signatureFileNameMissedLetter;
	}

	public void setSignatureFileNameMissedLetter(String signatureFileNameMissedLetter) {
		this.signatureFileNameMissedLetter = signatureFileNameMissedLetter;
	}

	public String getMissedRank() {
		return missedRank;
	}

	public void setMissedRank(String missedRank) {
		this.missedRank = missedRank;
	}

	public String getMissedName() {
		return missedName;
	}

	public void setMissedName(String missedName) {
		this.missedName = missedName;
	}

	public String getMissedSurname() {
		return missedSurname;
	}

	public void setMissedSurname(String missedSurname) {
		this.missedSurname = missedSurname;
	}
	
}
