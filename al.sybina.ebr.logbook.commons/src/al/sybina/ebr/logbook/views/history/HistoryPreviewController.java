package al.sybina.ebr.logbook.views.history;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.inject.Inject;

import org.eclipse.fx.core.di.Service;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;
import org.osgi.framework.Bundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import com.jfoenix.validation.RequiredFieldValidator;
import com.oceanpro.core.entity.oceanManager.office.Ship;
import com.oceanpro.core.services.OperationLogReladomoService;
import com.oceanpro.utils.EasyDateUtils;

import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import al.sybina.ebr.commons.utils.ViewPerspectiveHelper;
import al.sybina.ebr.jasperreports.BaseViewerFX;
import al.sybina.ebr.jasperreports.JasperReportDetails;
import al.sybina.ebr.jasperreports.JasperReportsService;
import al.sybina.ebr.logbook.commons.Activator;
import al.sybina.ebr.logbook.commons.ReportDataSourceBean;
import al.sybina.ebr.logbook.commons.ReportSignedDataSourceBean;
import al.sybina.ebr.logbook.domain.reladomo.OilLog;
import al.sybina.ebr.logbook.views.marpoldetail.MarpolBean;
import al.sybina.ebr.core.tools.PluginTools;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HistoryPreviewController implements Initializable {

	private static final Logger logger = LoggerCreator.createLogger(HistoryPreviewController.class);
	
	@Inject
	@Service
	private JasperReportsService jasperReportsService;
	@Inject
	private MarpolBean marpolBean;
	@Inject
	@Service
	private OperationLogReladomoService operationLogReladomoService;
	@Inject
	private ViewPerspectiveHelper viewPerspectiveHelper;
	@Inject
	private HistoryController historyController;
	
	@FXML
	private AnchorPane anchorPane;
	@FXML
	private JFXDatePicker fromDatePicker;
	@FXML
	private JFXDatePicker toDatePicker;
	@FXML
	private Button okButton;
	@FXML
	private Button cancelButton;
	@FXML
	private ImageView eORBPart1LogoImageView;

	private DateTimeFormatter dateTimeFormatter = EasyDateUtils.logbookSimpleDateTimeFormatter; 
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		eORBPart1LogoImageView.setImage(new Image(getClass().getResourceAsStream("/images/eORBPart1Logo.png")));
		
		RequiredFieldValidator validator = new RequiredFieldValidator();
		validator.setMessage("Input Required");
		validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class).glyph(FontAwesomeIcon.WARNING).size("1em")
				.styleClass("error").build());

		EasyDateUtils.setStringConverter(fromDatePicker, EasyDateUtils.logbookSimpleDateTimeFormatter);
		fromDatePicker.getValidators().add(validator);
		fromDatePicker.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal) {
				fromDatePicker.validate();
			}
		});

		EasyDateUtils.setStringConverter(toDatePicker, EasyDateUtils.logbookSimpleDateTimeFormatter);
		toDatePicker.getValidators().add(validator);
		toDatePicker.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal) {
				toDatePicker.validate();
			}
		});
	}

	
	@FXML
	private void showPreview(ActionEvent event) {
		if(!(fromDatePicker.validate() && toDatePicker.validate())) return;
		String oilLogType = null;
		String currentActivePerspecticeId = viewPerspectiveHelper.getActivePerspectiveId();
		if(Activator.OIL_LOG_TYPE_1_PERSPECTIVE_ID.equals(currentActivePerspecticeId)) oilLogType = Activator.OIL_LOG_TYPE_1;
		if(Activator.OIL_LOG_TYPE_2_PERSPECTIVE_ID.equals(currentActivePerspecticeId)) oilLogType = Activator.OIL_LOG_TYPE_2;
		List<OilLog> selectedItems = null;
		if(historyController.getBusinessDateCheckBox().isSelected() && historyController.getProcessingDateCheckBox().isSelected()) {
			selectedItems = operationLogReladomoService.getValidFromToBusinessDateOil1Log(oilLogType, fromDatePicker.getValue().atStartOfDay(), toDatePicker.getValue().atStartOfDay());
		} else {
			if(historyController.getBusinessDateCheckBox().isSelected())
				selectedItems = operationLogReladomoService.getValidInDatesFromToBusinessDateOil1Log(oilLogType, null, historyController.getProcessingDatePicker().getLocalDateTime(), fromDatePicker.getValue().atStartOfDay(), toDatePicker.getValue().atStartOfDay());
			else if(historyController.getProcessingDateCheckBox().isSelected())
				selectedItems = operationLogReladomoService.getValidInDatesFromToBusinessDateOil1Log(oilLogType, historyController.getBusinessDatePicker().getLocalDateTime(), null, fromDatePicker.getValue().atStartOfDay(), toDatePicker.getValue().atStartOfDay());
			else
				selectedItems = operationLogReladomoService.getValidInDatesFromToBusinessDateOil1Log(oilLogType, historyController.getBusinessDatePicker().getLocalDateTime(), historyController.getProcessingDatePicker().getLocalDateTime(), fromDatePicker.getValue().atStartOfDay(), toDatePicker.getValue().atStartOfDay());
		}
		Map<String, Map<String, List<OilLog>>> logSignatureGroupsMap = groupBySignatureGroupsOil1LogMaps(selectedItems);
		logSignatureGroupsMap = addCorrectedHistoryLogs(logSignatureGroupsMap);
		
		Ship ship = marpolBean.getSettings().getShip();
		String shipName = "";
		String shipCode = "";
		String portRegistry = "";
		String grossTonnage = "";
		String periodFrom = "";
		String periodTo = "";
		String partNote = "";
		String partIntroduction = "";
		String partType="";
		String appVersion="V 1.0.0";
		try {
			InputStream resourceAsStream = al.sybina.ebr.rcp.Activator.class.getResourceAsStream("/version.properties");
			Properties prop = new Properties();
			prop.load(resourceAsStream);
			appVersion = "V " + prop.getProperty("releasedVersion.version");
		} catch (IOException e1) {
			logger.error(e1.getMessage(), e1);
		}
		if(ship!=null) {
			shipName = ship.getName();
			shipCode = ship.getShipDistinctiveNumber();
			portRegistry = ship.getShipPortOfRegistry();
			grossTonnage = ship.getShipGrossTonnage();
		}
		if (viewPerspectiveHelper.getActivePerspectiveId().equals("al.sybina.ebr.logbook.ui.perspective.logbook")) {
	    	partType="Part I – Machinery space operations";
	    	partNote="NOTE\r\n" + 
					"Oil Record Book Part I shall be provided to every oil tanker of 150 tons gross tonnage and above and every ship of 400 tons gross tonnage and above, other than oil tankers, to record relevant space operations. For oil tankers, Oil Record Book Part II shall also be provided to record relevant cargo/ballast operations.\r\n" + 
					"";
	    	partIntroduction="INTRODUCTION\r\n" + 
	    			"The following pages of this section show a comprehensive list of items of machinery space operations which are, when appropriate, to be recorded in the Oil Record Book, in accordance with Regulation 17 of Annex I of the International Convention for the Prevention of Pollution from Ships, 1973, as modified by the Protocol of 1978 relating thereto (MARPOL 73/ 78). The items have been grouped into operational sections, each of which is denoted by a letter code.\r\n" + 
	    			"When making entries in the Oil Record Book Part I, the date, operational code and item number shall be inserted in the appropriate columns and the required particulars shall be recorded chronologically in the blank spaces. Each completed operation shall be signed for and dated by the officer of officers in charge. The master of the ship shall sign each completed page.\r\n" + 
	    			"The Oil Record Book Part I contains many references to oil quantity. The limited accuracy of tank measurement devices, temperature variations and cling age will affect the accuracy of these readings. The entries in the Oil Record Book should be considered accordingly.\r\n" + 
	    			"In the event of accidental or other exceptional discharge of oil, statement shall be made in the Oil Record Book Part I of the circumstances of, and the reasons for, the discharge.\r\n" + 
	    			"Any failure of the oil filtering equipment shall be noted in the Oil Record Book Part I. \r\n" + 
	    			"The Entries in the Oil Record Book Part I, for ships holding an IOPP Certificate, shall be at least in English, French or Spanish. Where entries in official language of the State whose flag the ship is entitled to fly are also used, this shall prevail in case of a dispute or discrepancy.\r\n" + 
	    			"The Oil Record Book Part I shall be kept in such a place as to be readily available for inspection at all reasonable times and, except in the case of unmanned ships under tow, shall be kept on board the ship. It shall be preserved for a period of three years after the last entry has been made.\r\n" + 
	    			"The competent authority of the Government of a Party to the Convention may inspect the Oil Record Book Part I on board any ship to which this Annex applies while the ship is in its port or offshore terminals and may make a copy of any entry in that book and may require the master of the ship to certify that the copy is a true copy of such entry. Any copy so made which has been certified by the master of the ship as a true copy of an entry in the Oil Record Book Part I shall be made admissible in any juridical proceedings as evidence of the facts stated in the entry. The inspection of an Oil Record Book Part I and the taking of a certified copy by the competent authority under this paragraph shall be performed as expeditiously as possible without causing the ship to be unduly delayed.\r\n" + 
	    			"";
		}else if (viewPerspectiveHelper.getActivePerspectiveId().equals("al.sybina.ebr.logbook.ui.perspective.logbook2")){
			partType="Part II – Cargo and ballast operations";
			partNote="NOTE\r\n" + 
					"Oil Record Book Part I shall be provided to every oil tanker of 150 tons gross tonnage and above and every ship of 400 tons gross tonnage and above, other than oil tankers, to record relevant space operations. For oil tankers, Oil Record Book Part II shall also be provided to record relevant cargo/ballast operations.\r\n" + 
					"";
			partIntroduction="INTRODUCTION\r\n" + 
					"The following pages of this section show a comprehensive list of items of cargo and ballast operations which\r\n" + 
					"are, when appropriate, to be recorded in the Oil Record Book Part II, in accordance with Regulation 36 of\r\n" + 
					"Annex I of the International Convention for the Prevention\r\n" + 
					"of Pollution from Ships, 1973, as modified by the protocol of 1978 relating thereto (MARPOL 73/78). The\r\n" + 
					"items have been grouped into operational sections, each of which is denoted by a code letter.\r\n" + 
					"When making entries in the Oil Record Book Part II, the date, operational code and item number shall be\r\n" + 
					"inserted in the appropriate columns and the required particulars shall be recorded chronologically in the\r\n" + 
					"blank spaces.\r\n" + 
					"Each completed operation shall be signed for and dated by the officer or officers in charge. Each completed\r\n" + 
					"page shall be countersigned by the master of the ship.\r\n" + 
					"In respect of the oil tankers engaged in specific trades in accordance with Regulation 2.5 of Annex I of\r\n" + 
					"MARPOL 73/78, appropriate entry in the Oil Record Book part II shall be endorsed by the competent port\r\n" + 
					"State authority [*].\r\n" + 
					"The Oil Record Book Part II contains many references to oil quantity. The limited accuracy of tank\r\n" + 
					"measurement devices, temperature\r\n" + 
					"variations and clingage will affect the accuracy of these readings. The entries in the Oil Record Book Part II\r\n" + 
					"should be considered accordingly.\r\n" + 
					"In the event of accidental or other exceptional discharge of oil, a statement shall be made in the Oil Record\r\n" + 
					"Book Part II of the circumstances of, and the reasons for, the discharge.\r\n" + 
					"Any failure of the oil discharge monitoring and control system shall be noted in the Oil Record Book Part II.\r\n" + 
					"The entries in the Oil Record Book Part II, for ships holding an IOPP Certificate, shall be at least in English,\r\n" + 
					"French or Spanish. Where entries in an official language of the State whose flag the ship is entitled to fly\r\n" + 
					"are also used, this shall prevail in case of a dispute or discrepancy.\r\n" + 
					"The Oil Record Book Part II shall be kept in such a place as to be readily available for inspection at all\r\n" + 
					"reasonable times and, except in the case of unmanned ships under taw, shall be kept on board the ship. It\r\n" + 
					"shall be preserved for a period of three years after the last entry has been made.\r\n" + 
					"The competent authority of the Government of a Party to the Convention may inspect the Oil Record Book\r\n" + 
					"Part II on board the ship to which this Annex applies while the ship is in its port or offshore terminals and\r\n" + 
					"may make a copy of any entry in that book and may require the master of the ship to certify that the copy\r\n" + 
					"is a true copy of such entry. Any copy so made which has been certified by the master of the ship as a true\r\n" + 
					"copy of an entry in the Oil Record Book Part II shall be made admissible in any juridical proceedings as\r\n" + 
					"evidence of the facts stated in the entry.\r\n" + 
					"The inspection of an Oil Record Book Part II and the taking of a certified copy by the competent authority\r\n" + 
					"under this paragraph shall be performed as expeditiously as possible without causing the ship to be unduly\r\n" + 
					"delayed.";
		}
		periodFrom=fromDatePicker.getValue().atStartOfDay().format(dateTimeFormatter);
		periodTo=toDatePicker.getValue().atStartOfDay().format(dateTimeFormatter);
		
		List<ReportSignedDataSourceBean> reportSignedDataSourceBeanList = new ArrayList<ReportSignedDataSourceBean>();
		for(Map<String, List<OilLog>> logGroupsMap : logSignatureGroupsMap.values()) {
			OilLog oilLog00 = logGroupsMap.values().stream().findFirst().get().get(0);
			
			String masterSignerName = null;
			String masterSignerSurname = null;
			String masterSignature = null;
			Date masterSigDate = null;
			String refusedToSignMsg = null;
			if(oilLog00.getMasterSignature()!=null) {
				masterSignerName = oilLog00.getMasterSignerName();
				masterSignerSurname = oilLog00.getMasterSignerSurname();
				masterSignature = createTempImageFile(oilLog00.getMasterSignature());
				masterSigDate = oilLog00.getMasterSigDate();
				refusedToSignMsg = oilLog00.getNote()!=null && oilLog00.getNote().contains("REJECTED. Reason:")?"Master refused to sign the entries in this page.\n"+oilLog00.getNote().substring(oilLog00.getNote().indexOf("REJECTED. Reason:")):null;
			}
			
			List<ReportDataSourceBean> reportDataSourceBeanList = new ArrayList<ReportDataSourceBean>();
			for(List<OilLog> oilLogList : logGroupsMap.values()) {
				for(OilLog oilLog : oilLogList) {
					if(oilLog.getCorrectionNote()!=null) {
						oilLog.setSignatureTempFileName(createTempImageFile(oilLog.getSignature()));
					}
				}
				
				OilLog oilLog0 = oilLogList.stream().filter(l-> l.getNote()==null || !l.getNote().contains("Missed Operation")).findFirst().orElse(null);
				OilLog missedLog = oilLogList.stream().filter(l-> l.getNote()!=null && l.getNote().contains("Missed Operation")).findFirst().orElse(null);
	
				String rank = null;
				String name = null;
				String surname = null;
				Date date = null;
				String signatureTempFileName = null;
				String missedRank = (missedLog != null ? missedLog.getSignerRank() : null);
				String missedName = (missedLog != null ? missedLog.getSignerName() : null);
				String missedSurname = (missedLog != null ? missedLog.getSignerSurname() : null);
				Date missedOperationDate = (missedLog != null ? missedLog.getBusinessDate() : null);
				String signaturemMissedTempFileName = (missedLog != null ? createTempImageFile(missedLog.getSignature()) : null);
				
				if (oilLog0 != null) {
					rank = oilLog0.getSignerRank();
					name = oilLog0.getSignerName();
					surname = oilLog0.getSignerSurname();
					date = oilLog0.getBusinessDate();
					signatureTempFileName = createTempImageFile(oilLog0.getSignature());

				} else if (missedLog != null) {
					rank = missedLog.getSignerRank();
					name = missedLog.getSignerName();
					surname = missedLog.getSignerSurname();
				}
				
				ReportDataSourceBean reportDataSourceBean = new ReportDataSourceBean();
				reportDataSourceBean.setShipName(shipName);
				reportDataSourceBean.setShipCode(shipCode);
				reportDataSourceBean.setCompilationDate(date);
				reportDataSourceBean.setRank(rank);
				reportDataSourceBean.setName(name);
				reportDataSourceBean.setSurname(surname);
				reportDataSourceBean.setSignatureFileName(signatureTempFileName);
				reportDataSourceBean.setMissedRank(missedRank);
				reportDataSourceBean.setMissedName(missedName);
				reportDataSourceBean.setMissedSurname(missedSurname);
				reportDataSourceBean.setCompilationDateMissedLetter(missedOperationDate);
				reportDataSourceBean.setSignatureFileNameMissedLetter(signaturemMissedTempFileName);
				oilLogList.sort(Comparator.comparingLong(log -> log.getId()));
				reportDataSourceBean.setOilLogList(oilLogList);
				reportDataSourceBeanList.add(reportDataSourceBean);
			}
			
			
			ReportSignedDataSourceBean reportSignedDataSourceBean = new ReportSignedDataSourceBean();
			reportSignedDataSourceBean.setMasterSignerName(masterSignerName);
			reportSignedDataSourceBean.setMasterSignerSurname(masterSignerSurname);
			reportSignedDataSourceBean.setMasterSignatureFilename(masterSignature);
			reportSignedDataSourceBean.setMasterSigDate(masterSigDate);
			reportSignedDataSourceBean.setRefusedToSignMsg(refusedToSignMsg);
			reportSignedDataSourceBean.setReportDataSourceBeanList(reportDataSourceBeanList);
			reportSignedDataSourceBeanList.add(reportSignedDataSourceBean);
		}
		
		Collections.sort(reportSignedDataSourceBeanList);
		for(ReportSignedDataSourceBean reportSignedDataSourceBean : reportSignedDataSourceBeanList) {
			Collections.sort(reportSignedDataSourceBean.getReportDataSourceBeanList());
			for(ReportDataSourceBean reportDataSourceBean : reportSignedDataSourceBean.getReportDataSourceBeanList()) {
				Collections.sort(reportDataSourceBean.getOilLogList());
			}
		}
		
		logger.info("showPreview");
		JasperReportDetails det = new JasperReportDetails();
		det.setDescription("Oil Record Book Log Part 1");
		
		Bundle bundle = org.eclipse.core.runtime.Platform.getBundle(Activator.PLUGIN_ID);
		String filePath = PluginTools.getFileLocalToRuntime(Activator.RUNTIME_LOCATION_FOLDER_NAME+"/reports/LogbookPart1_HistoryFromTo.jasper");
		File file = new File(filePath);
		det.setMasterReportUrl(file.getAbsolutePath());
		
		
		Map<String, Object> reportParams = new HashMap<String, Object>();
		reportParams.put("shipName", shipName);
		reportParams.put("shipCode", shipCode);
		reportParams.put("partType", partType);
		reportParams.put("portRegistry", portRegistry);
		reportParams.put("grossTonnage", grossTonnage);
		reportParams.put("partNote", partNote);
		reportParams.put("partIntroduction", partIntroduction);
		reportParams.put("partIntroduction", partIntroduction);
		reportParams.put("appVersion", appVersion);
		reportParams.put("periodFrom", periodFrom);
		reportParams.put("periodTo", periodTo);
		
		det.setReportParameters(reportParams);
		try {
			
			File pdffile = jasperReportsService.getJasperReportsFactory().complieReportAsPdfFile(new File(PluginTools.getFileLocalToRuntime("reports")+"/Logbook_Signed.pdf"), det, reportParams, reportSignedDataSourceBeanList);
			BaseViewerFX baseViewerFX = new BaseViewerFX();
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Logbook Report");
			baseViewerFX.start(stage, pdffile);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("end showPreview");
	}
	private Map<String, Map<String, List<OilLog>>> addCorrectedHistoryLogs(Map<String, Map<String, List<OilLog>>> logSignatureGroupsMap) {
		for(String signatureUUID : logSignatureGroupsMap.keySet()) {
			Map<String, List<OilLog>> groupsOil1LogLists = logSignatureGroupsMap.get(signatureUUID);
			for(String groupUuid : groupsOil1LogLists.keySet()) {
				List<OilLog> list = groupsOil1LogLists.get(groupUuid);
				List<OilLog> simpleLogModificationsList = new ArrayList<OilLog>();
				for(OilLog oilLog : list) {
					List<OilLog> logModificationsList = operationLogReladomoService.getLogModificationHistory(oilLog);
					for(OilLog ol : logModificationsList) {
						if(!simpleLogModificationsList.contains(ol) && !ol.equals(oilLog)) {
							simpleLogModificationsList.add(ol);
						}
					}
				}
				list.addAll(simpleLogModificationsList);
			}
		}
		return logSignatureGroupsMap;
	}
	private String createTempImageFile(String signatureString) {
		WritableImage signatureImage = null;
		try {
			byte[] signatureBytes = Base64.getDecoder().decode(signatureString);
			BufferedImage bImage = ImageIO.read(new ByteArrayInputStream(signatureBytes));
			signatureImage = SwingFXUtils.toFXImage(bImage, null);
		}catch (Exception e) {
			e.printStackTrace();
		} 
		File tempSignatureFile = null;
		File tempFolder = null;
		try {
			tempFolder = new File(PluginTools.getFileLocalToRuntime("reports"));
			tempFolder.mkdir();
			tempSignatureFile = File.createTempFile("signature", ".png", tempFolder);
			OutputStream os = new FileOutputStream(tempSignatureFile);
			ImageIO.write(SwingFXUtils.fromFXImage(signatureImage, null), "png", os);
			os.flush();
			os.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		tempSignatureFile.deleteOnExit();
		return tempSignatureFile.getName();
	}
	private Map<String, Map<String, List<OilLog>>> groupBySignatureGroupsOil1LogMaps(List<OilLog> allOilLogList) {
		Map<String, List<OilLog>> signatureGroupedOil1Logs = groupBySignatureOil1Log(allOilLogList);
		Map<String, Map<String, List<OilLog>>> signatureAndGroupsGrouped = groupByGroupOil1LogMaps(signatureGroupedOil1Logs);
		return signatureAndGroupsGrouped;
	}
	
	private Map<String, List<OilLog>> groupBySignatureOil1Log(List<OilLog> allOilLogList) {
		Map<String, List<OilLog>> resultMap = new HashMap<String, List<OilLog>>();
		for(OilLog oilLog : allOilLogList) {
			List<OilLog> list = resultMap.get(oilLog.getSignatureGroupUuid());
			if(list==null) {
				List<OilLog> list2 = new ArrayList<OilLog>();
				list2.add(oilLog);
				resultMap.put(oilLog.getSignatureGroupUuid(), list2);
			} else {
				list.add(oilLog);
			}
		}
		return resultMap;
	}

	private Map<String, List<OilLog>> groupByGroupOil1Log(List<OilLog> allOilLogList) {
		Map<String, List<OilLog>> resultMap = new HashMap<String, List<OilLog>>();
		for(OilLog oilLog : allOilLogList) {
			List<OilLog> list = resultMap.get(oilLog.getGroupUuid());
			if(list==null) {
				List<OilLog> list2 = new ArrayList<OilLog>();
				list2.add(oilLog);
				resultMap.put(oilLog.getGroupUuid(), list2);
			} else {
				list.add(oilLog);
			}
		}
		return resultMap;
	}
	private Map<String, Map<String, List<OilLog>>> groupByGroupOil1LogMaps(Map<String, List<OilLog>> signatureGroupedOil1Logs) {
		Map<String, Map<String, List<OilLog>>> result = new HashMap<String, Map<String, List<OilLog>>>();
		for(String signatureUUID : signatureGroupedOil1Logs.keySet()) {
			List<OilLog> oilLogList = signatureGroupedOil1Logs.get(signatureUUID);
			Map<String, List<OilLog>> groupsOil1LogLists = new HashMap<String, List<OilLog>>();
			for(OilLog oilLog : oilLogList) {
				List<OilLog> list = groupsOil1LogLists.get(oilLog.getGroupUuid());
				if(list==null) {
					List<OilLog> list2 = new ArrayList<OilLog>();
					list2.add(oilLog);
					groupsOil1LogLists.put(oilLog.getGroupUuid(), list2);
				} else {
					list.add(oilLog);
				}
			}
			result.put(signatureUUID, groupsOil1LogLists);
		}
		return result;
	}
	

}
