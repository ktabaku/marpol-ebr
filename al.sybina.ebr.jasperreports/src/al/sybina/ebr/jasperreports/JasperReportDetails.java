/**
 * 
 */
package al.sybina.ebr.jasperreports;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class JasperReportDetails {
	private String description;
	private String masterReportUrl;
	private Properties subReportUrls;
	private Map<String, Object> reportParameters = new HashMap<String, Object>();

	public void setSubReportUrls(Properties subReports) {
		this.subReportUrls = subReports;
	}

	public String getMasterReportUrl() {
		return masterReportUrl;
	}

	public void setMasterReportUrl(String masterReportUrl) {
		this.masterReportUrl = masterReportUrl;
	}

	public Properties getSubReportUrls() {
		return subReportUrls;
	}
		
	public Map<String, Object> getReportParameters() {
		return reportParameters;
	}

	public void setReportParameters(Map<String, Object> reportParameters) {
		this.reportParameters = reportParameters;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
