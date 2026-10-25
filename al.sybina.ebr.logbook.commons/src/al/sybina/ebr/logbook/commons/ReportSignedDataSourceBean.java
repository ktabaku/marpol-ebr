package al.sybina.ebr.logbook.commons;

import java.util.Date;
import java.util.List;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class ReportSignedDataSourceBean implements Comparable<ReportSignedDataSourceBean> {
	
	private String masterSignerName;
	private String masterSignerSurname;
	private String masterSignatureFilename;
	
	private Date masterSigDate;
	
	private String refusedToSignMsg;
	
	private List<ReportDataSourceBean> reportDataSourceBeanList;


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

	public List<ReportDataSourceBean> getReportDataSourceBeanList() {
		return reportDataSourceBeanList;
	}

	public void setReportDataSourceBeanList(List<ReportDataSourceBean> reportDataSourceBeanList) {
		this.reportDataSourceBeanList = reportDataSourceBeanList;
	}

	public JRBeanCollectionDataSource getReportDataSourceCollectionDataSource() {
		return new JRBeanCollectionDataSource(reportDataSourceBeanList);
	}

	public String getMasterSignatureFilename() {
		return masterSignatureFilename;
	}

	public void setMasterSignatureFilename(String masterSignatureFilename) {
		this.masterSignatureFilename = masterSignatureFilename;
	}

	public void setMasterSigDate(Date masterSigDate) {
		this.masterSigDate = masterSigDate;
	}

	public Date getMasterSigDate() {
		return masterSigDate;
	}

	
	public String getRefusedToSignMsg() {
		return refusedToSignMsg;
	}

	public void setRefusedToSignMsg(String refusedToSignMsg) {
		this.refusedToSignMsg = refusedToSignMsg;
	}

	@Override
	public int compareTo(ReportSignedDataSourceBean o) {
		return this.getReportDataSourceBeanList().get(0).compareTo(o.getReportDataSourceBeanList().get(0));
	}
	
	
}
