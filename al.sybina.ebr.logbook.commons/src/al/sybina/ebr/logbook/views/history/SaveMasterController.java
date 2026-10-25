package al.sybina.ebr.logbook.views.history;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
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
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.inject.Inject;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.fx.core.di.Service;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;
import org.eclipse.fx.ui.services.theme.ThemeManager;
import org.osgi.framework.Bundle;
import org.osgi.service.event.Event;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import com.oceanpro.core.entity.oceanManager.office.Ship;
import com.oceanpro.core.services.OperationLogReladomoService;

import al.sybina.ebr.commons.utils.ImageCell;
import al.sybina.ebr.commons.utils.JavaFXUtils;
import al.sybina.ebr.commons.utils.ViewPerspectiveHelper;
import al.sybina.ebr.jasperreports.BaseViewerFX;
import al.sybina.ebr.jasperreports.JasperReportDetails;
import al.sybina.ebr.jasperreports.JasperReportsService;
import al.sybina.ebr.logbook.commons.Activator;
import al.sybina.ebr.logbook.commons.ReportDataSourceBean;
import al.sybina.ebr.logbook.commons.ReportSignedDataSourceBean;
import al.sybina.ebr.logbook.domain.reladomo.OilLog;
import al.sybina.ebr.logbook.signature.SignatureMaster;
import al.sybina.ebr.logbook.views.marpoldetail.MarpolBean;
import al.sybina.ebr.login.preferences.LoginPreferencePage;
import al.sybina.ebr.login.services.RemoteEventAdminService;
import al.sybina.ebr.core.tools.PluginTools;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SaveMasterController implements Initializable {
	
	private static final Logger logger = LoggerCreator.createLogger(SaveMasterController.class);
	
	@Inject
	private IEventBroker eventBroker;
	@Inject
	@Service
	private RemoteEventAdminService remoteEventAdminService;
	@Inject
	private LoginPreferencePage loginPreferencePage;
	@Inject
	private static ThemeManager themeManager;
	@Inject
	@Service
	private JasperReportsService jasperReportsService;
	@Inject
	private MarpolBean marpolBean;
	@Inject
	@Service
	private OperationLogReladomoService operationLogReladomoService;
	@Inject
	private SignatureMaster signature;
	@Inject
	private ViewPerspectiveHelper viewPerspectiveHelper;
	@FXML
	private AnchorPane mainAnchorPane;
	@FXML
	private TableView<OilLog> logTableView;
	@FXML
	private TableColumn<OilLog, String> typeCol;
	@FXML
	private TableColumn<OilLog, String> dateCol;
	@FXML
	private TableColumn<OilLog, String> nameCreatorCol;
	@FXML
	private TableColumn<OilLog, ImageView> signatureCreatorCol;
	@FXML
	private TableColumn<OilLog, String> noteCol;
	
	@FXML
	private TableColumn<OilLog, String> codeLetterGroupCol;
	@FXML
	private TableColumn<OilLog, String> codeNumberGroupCol;
	@FXML
	private TableColumn<OilLog, String> contentGroupCol;
	@FXML
	private TableColumn<OilLog, String> correctionNoteCol;
	

    @FXML
    private Label masterNameLabel;
    @FXML
    private Label masterSurnameLabel;
    @FXML
    private Label masterSigDateLabel;
	@FXML
	private GridPane signaturesGridPane;
    
	private ImageCell masterSignatureImageView;
		
	@FXML
	private Button saveButton;
	@FXML
	private Button showPreviewButton;
	@FXML
	private ToggleGroup approvedRejectedToggleGroup;
	@FXML
	private ToggleButton approvedToggleButton;
	@FXML
	private ToggleButton rejectedToggleButton;
	@FXML
	private TextArea rejectedReasonTextArea;
	
	private static JFXButton closeButton;
	
	private Map<String, List<OilLog>> logGroupsMap;

	private Stage stage;

	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			SVGGlyph timesGlyph = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg.times-circle");
			closeButton = new JFXButton();
			closeButton.getStyleClass().add("closeDialogButton");
			closeButton.setMinWidth(40); closeButton.setPrefWidth(40); closeButton.setMaxWidth(40);
			closeButton.setMinHeight(32); closeButton.setPrefHeight(32); closeButton.setMaxHeight(32);
			closeButton.setGraphic(timesGlyph);
			closeButton.ripplerFillProperty().setValue(Color.LIGHTSALMON);
			closeButton.setOnAction(event -> {
				((Stage)((Button)event.getSource()).getScene().getWindow()).close();
	        });
	        Tooltip.install(closeButton, new Tooltip("Exit"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		masterSignatureImageView = new ImageCell();
		masterSignatureImageView.setMinWidth(300); masterSignatureImageView.setPrefWidth(300); masterSignatureImageView.setMaxWidth(300);
		GridPane.setColumnSpan(masterSignatureImageView, 1);
		signaturesGridPane.add(masterSignatureImageView, 5, 0);
		
		typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
		dateCol.setCellValueFactory(new PropertyValueFactory<>("businessDateFrom"));
		nameCreatorCol.setCellValueFactory(cellData -> {
			OilLog p = cellData.getValue();
			return new ReadOnlyStringWrapper(p.getSignerRank() + " " + p.getSignerName() + " " + p.getSignerSurname());
		});
		signatureCreatorCol.setCellValueFactory(cellData -> {
			OilLog p = cellData.getValue();
			byte[] signatureBytes = Base64.getDecoder().decode(p.getSignature());
			BufferedImage bImage;
			try {
				bImage = ImageIO.read(new ByteArrayInputStream(signatureBytes));
				WritableImage image = SwingFXUtils.toFXImage(bImage, null);
				ImageView imageView = new ImageView(image);
				imageView.setFitWidth(50);
				imageView.setFitHeight(30);
				return new SimpleObjectProperty(imageView);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		});
		noteCol.setCellValueFactory(new PropertyValueFactory<>("note"));
		
		codeLetterGroupCol.setCellValueFactory(new PropertyValueFactory<>("codeLetter"));
		codeNumberGroupCol.setCellValueFactory(new PropertyValueFactory<>("codeNumber"));
		contentGroupCol.setCellValueFactory(new PropertyValueFactory<>("content"));
		correctionNoteCol.setCellValueFactory(new PropertyValueFactory<>("correctionNote"));
		
		//logTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		logTableView.setSelectionModel(null);
		
		mainAnchorPane.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				Platform.runLater(() -> {
					customResize(logTableView);
				});
		}});
		

		masterNameLabel.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(newValue!=null && !newValue.equals("") && masterNameLabel.getText()!=null && !masterNameLabel.getText().equals("")) {
					saveButton.setDisable(false);
					showPreviewButton.setDisable(false);
				} else {
					saveButton.setDisable(true);
					showPreviewButton.setDisable(true);
				}
			}
		});
		
		
		approvedRejectedToggleGroup.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
		    if (newVal == null) {
		    	oldVal.setSelected(true);
		    } else {
		    	if(newVal.equals(rejectedToggleButton)) {
		    		rejectedReasonTextArea.setDisable(false);
		    	} else {
		    		rejectedReasonTextArea.clear();
		    		rejectedReasonTextArea.setDisable(true);
		    	}
		    }
		    
		});
	}
	
	public void customResize(TableView<?> view) {
        AtomicLong width = new AtomicLong();
        view.getColumns().forEach(col -> {
            width.addAndGet((long) col.getWidth());
        });
        double tableWidth = view.getWidth();

        if (tableWidth > width.get()) {
            view.getColumns().forEach(col -> {
                col.setPrefWidth(col.getWidth()+((tableWidth-width.get())/view.getColumns().size())-1);
            });
        }
    }
	

	@FXML
	private void masterSignature(ActionEvent event) {
		signature.showSignature((Stage)((Button)event.getSource()).getScene().getWindow(), logGroupsMap, masterNameLabel, masterSurnameLabel, (ImageCell)masterSignatureImageView, masterSigDateLabel);
	}
	
	@FXML
	public void saveLog() {
		List<OilLog> allOilLogList = new ArrayList<OilLog>();
		if(approvedRejectedToggleGroup.getSelectedToggle().equals(approvedToggleButton)) {
			for(List<OilLog> oilLogList : logGroupsMap.values()) {
				for(OilLog oilLog : oilLogList) {
					
					if (oilLog.getNote()!=null && !oilLog.getNote().contains("Missed Operation")) {
						oilLog.setNote(null);	
					}
					
				}
				allOilLogList.addAll(oilLogList);
			}
		} else {
			for(List<OilLog> oilLogList : logGroupsMap.values()) {
				for(OilLog oilLog : oilLogList) {
					
					if (oilLog.getNote()!=null && oilLog.getNote().contains("Missed Operation")) {
						oilLog.setNote(oilLog.getNote()+" REJECTED. Reason: " + rejectedReasonTextArea.getText());	
					}else {
						oilLog.setNote("REJECTED. Reason: " + rejectedReasonTextArea.getText());
					}
					
				}
				allOilLogList.addAll(oilLogList);
			}
		}
		operationLogReladomoService.editSignOil1Logs(allOilLogList);
		
		List<Long> updatedLogIds = new ArrayList<Long>();
		updatedLogIds.addAll(allOilLogList.stream().mapToLong(log -> log.getId()).boxed().collect(Collectors.toList()));
		Map<String, List<Long>> idsParameter = new HashMap<String, List<Long>>();
		idsParameter.put(Activator.LOG_IDS, updatedLogIds);
		remoteEventAdminService.getDistributedEventAdmin(loginPreferencePage.getMainInstallationIp().getValue()).postEvent(new Event(al.sybina.ebr.logbook.commons.Activator.LOGS_CHANGED_EVENT_TOPIC, idsParameter));
		showInformation("Log Saved", "Log saved successfully.");
		stage.close();
	}
	
	
	
	
	
	
	
	@FXML
	private void showPreview(ActionEvent event) {
		String signatureGroupUuid = UUID.randomUUID().toString();
		for(List<OilLog> oilLogList : logGroupsMap.values()) {
			for (OilLog log : oilLogList) {
				log.setSignatureGroupUuid(signatureGroupUuid);
			}
		}
		Map<String, Map<String, List<OilLog>>> logSignatureGroupsMap = new HashMap<>();
		logSignatureGroupsMap.put(signatureGroupUuid, logGroupsMap);
		
		
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
		List<ReportSignedDataSourceBean> reportSignedDataSourceBeanList = new ArrayList<ReportSignedDataSourceBean>();
		for(Map<String, List<OilLog>> logGroupsMap : logSignatureGroupsMap.values()) {
			OilLog oilLog00 = logGroupsMap.values().stream().findFirst().get().get(0);
			
			String masterSignerName = null;
			String masterSignerSurname = null;
			String masterSignature = null;
			Date masterSigDate = null;
			if(oilLog00.getMasterSignature()!=null) {
				masterSignerName = oilLog00.getMasterSignerName();
				masterSignerSurname = oilLog00.getMasterSignerSurname();
				masterSignature = createTempImageFile(oilLog00.getMasterSignature());
				masterSigDate = oilLog00.getMasterSigDate();
			}
			
			List<ReportDataSourceBean> reportDataSourceBeanList = new ArrayList<ReportDataSourceBean>();
			for (List<OilLog> oilLogList : logGroupsMap.values()) {
				OilLog oilLog0 = oilLogList.stream().filter(l -> l.getNote() == null || !l.getNote().contains("Missed Operation")).findFirst().orElse(null);
				OilLog missedLog = oilLogList.stream().filter(l -> l.getNote() != null && l.getNote().contains("Missed Operation")).findFirst().orElse(null);

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
		String filePath = PluginTools.getFileLocalToRuntime(Activator.RUNTIME_LOCATION_FOLDER_NAME+"/reports/LogbookPart1_History.jasper");
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
		//String periodFrom = "";
		//String periodTo = "";
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
	
	
	
	
	
	public static void showInformation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Information");
        alert.setHeaderText(title);
        alert.setContentText(message);
        JavaFXUtils.undecorate(alert, closeButton, themeManager);
        alert.showAndWait();
    }
	
	
	public Map<String, List<OilLog>> getLogGroupsMap() {
		return logGroupsMap;
	}

	public void setLogGroupsMap(Map<String, List<OilLog>> logGroupsMap) {
		this.logGroupsMap = logGroupsMap;
		logTableView.getItems().clear();
		for(List<OilLog> oilLogList : logGroupsMap.values()) {
			for (OilLog log : oilLogList) {
				logTableView.getItems().add(log);
			}
		}
		Comparator<OilLog> idComparator = Comparator.comparingLong(log -> log.getId());
		logTableView.getItems().sort(idComparator);
		
//		String masterName = oilLog.getMasterSignerName();
//		String masterSurname = oilLog.getMasterSignerSurname();
//		WritableImage masterSignatureImage = null;
//		if(oilLog.getMasterSignature()!=null) {
//			try {
//				byte[] signatureBytes = Base64.getDecoder().decode(oilLog.getMasterSignature());
//				BufferedImage bImage = ImageIO.read(new ByteArrayInputStream(signatureBytes));
//				masterSignatureImage = SwingFXUtils.toFXImage(bImage, null);
//			}catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		String masterDateString = "";
//		if(oilLog.getMasterSigDate() != null) {
//			masterDateString = new SimpleDateFormat("dd-MM-yyyy").format(oilLog.getMasterSigDate());
//		}
//		
//		masterNameLabel.setText(masterName);
//		masterSurnameLabel.setText(masterSurname);
//		masterSignatureImageView.setImage(masterSignatureImage);
//		masterSignatureImageView.layout();
//		masterSigDateLabel.setText(masterDateString);
		masterNameLabel.setText("");
		masterSurnameLabel.setText("");
		masterSigDateLabel.setText("");
		masterSignatureImageView.setImage(null);
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

}
