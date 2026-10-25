package al.sybina.ebr.logbook.views;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.apache.shiro.SecurityUtils;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.tools.resources.WorkbenchResourceProvider;
import org.eclipse.e4.tools.services.IResourceProviderService;
import org.eclipse.fx.core.di.Service;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;
import org.eclipse.fx.ui.services.resources.ImageProvider;
import org.eclipse.fx.ui.services.theme.ThemeManager;
import org.osgi.service.event.Event;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.Tank;
import com.oceanpro.core.entity.oceanManager.office.Ship;
import com.oceanpro.core.security.entities.User;
import com.oceanpro.core.services.OperationLogReladomoService;
import com.oceanpro.core.services.OperationLogService;
import com.oceanpro.utils.EasyDateUtils;

import al.sybina.ebr.commons.utils.JavaFXUtils;
import al.sybina.ebr.commons.utils.MathUtils;
import al.sybina.ebr.commons.utils.ViewPerspectiveHelper;
import al.sybina.ebr.jasperreports.BaseViewerFX;
import al.sybina.ebr.jasperreports.JasperReportDetails;
import al.sybina.ebr.jasperreports.JasperReportsService;
import al.sybina.ebr.logbook.commons.Activator;
import al.sybina.ebr.logbook.commons.BallonMessage;
import al.sybina.ebr.logbook.commons.ReportDataSourceBean;
import al.sybina.ebr.logbook.domain.reladomo.OilLog;
import al.sybina.ebr.logbook.domain.reladomo.TimestampProvider;
import al.sybina.ebr.logbook.signature.Signature;
import al.sybina.ebr.logbook.views.marpoldetail.MarpolBean;
import al.sybina.ebr.login.preferences.LoginPreferencePage;
import al.sybina.ebr.login.services.RemoteEventAdminService;
import al.sybina.ebr.core.tools.PluginTools;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jfxtras.scene.control.ImageViewButton;

public class OilRecordBookPart1ViewController implements Initializable {
	
	
	
	private Label label1 = new Label( );

	private static final Logger logger = LoggerCreator.createLogger(OilRecordBookPart1ViewController.class);
	
	@Inject
	private BallonMessage ballonMessage;
	@Inject
	private ViewPerspectiveHelper viewPerspectiveHelper;
	@Inject
	private static ThemeManager themeManager;
	@Inject
	private IResourceProviderService resourcesService;
	@Inject
	private ImageProvider imageProvider;
	@Inject
	@Service
	private OperationLogReladomoService operationLogReladomoService;
	@Inject
	@Service
	private OperationLogService operationLogService;
	@Inject
	private Signature signature;
	@Inject
	@Service
	private JasperReportsService jasperReportsService;
	@Inject
	private IEventBroker eventBroker;
	@Inject
	@Service
	private RemoteEventAdminService remoteEventAdminService;
	@Inject
	private LoginPreferencePage loginPreferencePage;
	@Inject
	private MarpolBean marpolBean;
	@FXML
	private Canvas canvasSignature;
	@FXML
	private TabPane mainTabPane;
	@FXML
	private AnchorPane anchorPane;
	@FXML
	private Label restoredLabel;
	@FXML
	private DatePicker compileDate;
	@FXML
	private HBox hBoxInsertDate;
	@FXML
	private AnchorPane anchorPaneInfoMessage;
	@FXML
	private TextField lastEntryTextField;
	@FXML
	private Button signatureButton;
	@FXML
	private ImageViewButton logbook1HelpImageViewButton;
	@FXML
	private Button saveLogsButton;

	public static Alert alert;
	private static JFXButton closeButton;
	
	private OilRecordBookPart1CodeLetterAController oilRecordBookPart1CodeLetterAController;
	private OilRecordBookPart1CodeLetterBController oilRecordBookPart1CodeLetterBController;
	private OilRecordBookPart1CodeLetterCController oilRecordBookPart1CodeLetterCController;
	private OilRecordBookPart1CodeLetterDController oilRecordBookPart1CodeLetterDController;
	private OilRecordBookPart1CodeLetterEController oilRecordBookPart1CodeLetterEController;
	private OilRecordBookPart1CodeLetterFController oilRecordBookPart1CodeLetterFController;
	private OilRecordBookPart1CodeLetterGController oilRecordBookPart1CodeLetterGController;
	private OilRecordBookPart1CodeLetterHController oilRecordBookPart1CodeLetterHController;
	private OilRecordBookPart1CodeLetterIController oilRecordBookPart1CodeLetterIController;
	
	private String groupUUID;
	private boolean restored;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		JavaFXUtils.addFilledPseudoClass(lastEntryTextField);
		JavaFXUtils.addFilledPseudoClass(compileDate);
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
		try {
			Image infoIconImage = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
			logbook1HelpImageViewButton.setImage(infoIconImage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		EasyDateUtils.setStringConverter(compileDate, EasyDateUtils.logbookSimpleDateTimeFormatter);
		
		compileDate.valueProperty().addListener((observable, oldDate, newDate) -> {
			hBoxInsertDate.setVisible(false);
		});

		
		eventBroker.subscribe(al.sybina.ebr.login.Activator.LOGGED_IN_EVENT_TOPIC, new org.osgi.service.event.EventHandler() {
			@Override
	        public void handleEvent(Event event) {
				OilLog lastOilLog = operationLogReladomoService.getLastIncludingCodeIMissedOilLog(Activator.OIL_LOG_TYPE_1);
	            if(lastOilLog!=null) {
	            	lastEntryTextField.setText(lastOilLog.getCodeLetter()+" "+lastOilLog.getCodeNumber()+"  Date "+ new SimpleDateFormat("dd-MM-yyyy").format(lastOilLog.getBusinessDateFrom()));
	            }
			}
	    });

		eventBroker.subscribe(Activator.LOGS_CHANGED_EVENT_TOPIC, new org.osgi.service.event.EventHandler() {
			@Override
			public void handleEvent(Event event) {
				OilLog lastOilLog = operationLogReladomoService.getLastIncludingCodeIMissedOilLog(Activator.OIL_LOG_TYPE_1);
				if(lastOilLog!=null) {
					lastEntryTextField.setText(lastOilLog.getCodeLetter()+" "+lastOilLog.getCodeNumber()+"  Date "+ new SimpleDateFormat("dd-MM-yyyy").format(lastOilLog.getBusinessDateFrom()));
				}
			}
		});
		
		eventBroker.subscribe(al.sybina.ebr.login.Activator.LOGGED_IN_EVENT_TOPIC, new org.osgi.service.event.EventHandler() {
			@Override
			public void handleEvent(Event event) {
				if(SecurityUtils.getSubject().isPermitted(al.sybina.ebr.login.Activator.PERMISSION_INSERT_LOGS)) {
					signatureButton.setDisable(false);
					signatureButton.setTooltip(null);
					saveLogsButton.setDisable(false);
					saveLogsButton.setTooltip(null);
				} else {
					signatureButton.setDisable(true);
					signatureButton.setTooltip(new Tooltip("You do not have sufficient Permissions to perform this operation."));
					saveLogsButton.setDisable(true);
					saveLogsButton.setTooltip(new Tooltip("You do not have sufficient Permissions to perform this operation."));
				}
			}
		});

		eventBroker.subscribe(Activator.MARPOL_DETAILS_RELOADED, new org.osgi.service.event.EventHandler() {
			@Override
			public void handleEvent(Event event) {
				ArrayList<Long> changedTankIds = (ArrayList<Long>) event.getProperty(Activator.TANK_IDS);
				if(alert != null) alert.close();
			}
		});
		mainTabPane.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
			if (oilRecordBookPart1CodeLetterIController != null && !oilRecordBookPart1CodeLetterIController.logbook1LetterIEntryPertainingToAnEarlierMissedOperationalComboBox.getValue().isEmpty()) {
				// if a code letter from A-R is open in code letter O turn it back to same code
				// letter all "anchorpane childs"
				// please also refer to setOilRecordBookPart2ViewControllerToCodeLetterO
				// setOldCodLetterBackListNode and setAllCodeLetterAtO
				oilRecordBookPart1CodeLetterIController.logbook1LetterIEntryPertainingToAnEarlierMissedOperationalComboBox.setValue("");
				clearFilledInfo();
			}
		});
	}

	public void marpolDetailHandler() {
		viewPerspectiveHelper.showView("al.sybina.ebr.logbook.ui.part.marpolDetail");
	}

	public void showMapHandler() {
		viewPerspectiveHelper.goToPerspective("al.sybina.ebr.marpol.ui.perspective.Marpol");
	}

	


	public void saveLog() {
		Entry<Map<Validation, Object>, List<OilLog>> result = prepareLog();
		if (!checkShip(result)) return;
		if(!checkValidation(result)) return;
		List<OilLog> insertedLogList = null;
		if(restored) {
//			showInformation("Save info", "The Draft Log will be saved at the current date and time with a note that it has been modified.");
//			String groupUUID = result.getValue().get(0).getGroupUuid();
//			List<OilLog> oldOilLogList = operationLogReladomoService.getDraftOil1LogListByGroupUUID(groupUUID);
////			for(OilLog oilLog : oldOilLogList) oilLog.getDetachedCopy();
//			boolean atLeastOneSaved = false;
//			for (OilLog log : result.getValue()) {
//				if(!oldOilLogList.contains(log)) {
//					OilLog correctedOilLog = operationLogReladomoService.getDraftOil1Log(groupUUID, log.getCodeLetter(), log.getCodeNumber());
//					if(correctedOilLog!=null) {
//						correctedOilLog.setCorrectionNote("Corrected");
//						operationLogReladomoService.updateDraftOil1Log(correctedOilLog);
//					}
//					log.setCorrectionNote("Correction");
//					log.setBusinessDateFrom(TimestampProvider.createBusinessDate(new Date()));
//					operationLogReladomoService.addOil1Log(log);
//					atLeastOneSaved = true;
//				}
//			}
//			if(atLeastOneSaved) {
//				showInformation("Draft Log Saved", "Draft Log saved successfully.");
//				restored = false;
//				restoredLabel.setText("");
//			} else {
//				showInformation("Nothing to save", "No log saved because nothing has changed.");
//			}
		} else {
			String groupUuid = UUID.randomUUID().toString();
			for (OilLog log : result.getValue()) {
				log.setGroupUuid(groupUuid);
				log.setType("OIL1");

			}
			User user = (User) SecurityUtils.getSubject().getPrincipal();
			insertedLogList = operationLogReladomoService.addOil1Logs(result.getValue());
			logger.info("Start added logs:");
			logger.info("User: " + user.getRoles().stream().map(Object::toString).collect(Collectors.joining(", ")) + " " + user.getFirstName() + " " + user.getLastName());
			for(OilLog savedOilLog : result.getValue()) {
				logger.info(savedOilLog.toString());
			}
			logger.info("End added logs.");
			List<Long> updatedTankIds = new ArrayList<Long>();
			for (OilLog log : result.getValue()) {
//				if(log.getCodeLetter().equals("A") && (log.getCodeNumber().equals("1") || log.getCodeNumber().equals("3.3"))) {
//				if(log.getCodeLetter().equals("B") && log.getCodeNumber().equals("5")) {
//				if(log.getCodeLetter().equals("C") && (log.getCodeNumber().equals("11.1") || log.getCodeNumber().equals("11.4"))) {
//				if(log.getCodeLetter().equals("C") && (log.getCodeNumber().equals("12.1") || log.getCodeNumber().equals("12.2") || log.getCodeNumber().equals("12.3") || log.getCodeNumber().equals("12.4"))) {
//				if((log.getCodeLetter().equals("D") && (log.getCodeNumber().equals("13") || log.getCodeNumber().equals("15.3"))) || log.getCodeLetter().equals("E") && log.getCodeNumber().equals("17")) {
//				if(log.getCodeLetter().equals("H") && (log.getCodeNumber().equals("26.3") || log.getCodeNumber().equals("26.4"))) {
//				if(log.getCodeLetter().equals("I")) {
				
				if(log.getUserTankActualLevels()!=null) {
					for(Entry<Tank, Double> entry : log.getUserTankActualLevels().entrySet()) {
						entry.getKey().setActualLevel(entry.getValue());
						operationLogService.updateTank(entry.getKey());
						updatedTankIds.add(entry.getKey().getId());
					}
				}
			}
			Map<String, List<Long>> idsParameter = new HashMap<String, List<Long>>();
			idsParameter.put(Activator.TANK_IDS, updatedTankIds);
			try {
				remoteEventAdminService.getDistributedEventAdmin(loginPreferencePage.getMainInstallationIp().getValue()).postEvent(new Event(al.sybina.ebr.logbook.commons.Activator.MARPOL_DETAILS_CHANGED, idsParameter));
			} catch(Exception e) {
				logger.debug(e.getMessage(), e);
				remoteEventAdminService.getDistributedEventAdmin(loginPreferencePage.getMainInstallationIp().getValue()).postEvent(new Event(al.sybina.ebr.logbook.commons.Activator.MARPOL_DETAILS_CHANGED, idsParameter));
			}
			
			showInformation("Log(s) Saved", "Log(s) saved successfully.");
		}
		clearFilledInfo();
		List<Long> updatedLogIds = insertedLogList.stream().mapToLong(log -> log.getId()).boxed().collect(Collectors.toList());
		Map<String, List<Long>> idsParameter = new HashMap<String, List<Long>>();
		idsParameter.put(Activator.LOG_IDS, updatedLogIds);
		remoteEventAdminService.getDistributedEventAdmin(loginPreferencePage.getMainInstallationIp().getValue()).postEvent(new Event(al.sybina.ebr.logbook.commons.Activator.LOGS_CHANGED_EVENT_TOPIC, idsParameter));
	}


	/**
	 * Id of the ship configured in Marpol detail, or null when no ship has been set up yet.
	 */
	public Long getShipId() {
		if (marpolBean == null || marpolBean.getSettings() == null || marpolBean.getSettings().getShip() == null) {
			return null;
		}
		return marpolBean.getSettings().getShip().getId();
	}

	private boolean checkShip(Entry<Map<Validation, Object>, List<OilLog>> result) {
		if (result.getKey().containsKey(Validation.SHIP_MISSING)) {
			showWarning("Ship not configured", "Please set up the ship in Marpol detail (Ship Particulars) before saving or signing entries.");
			return false;
		}
		return true;
	}

	public SimpleEntry<Map<Validation, Object>, List<OilLog>> prepareLog() {
		Map<Validation, Object> validations = new HashMap<Validation, Object>();
		
		LocalDate compileLocalDate = compileDate.getValue();
		Timestamp compileTimestamp = TimestampProvider.getInfinityDate();
		boolean dateError = false;
		if (compileLocalDate == null) {
			validations.put(Validation.DATE_MISSING, null);
		} else {
			ZonedDateTime nowUTC = ZonedDateTime.now(ZoneOffset.UTC);
			LocalDateTime ldt = compileLocalDate.atTime(nowUTC.toLocalTime());
			ZonedDateTime utcZoned = ldt.atZone(ZoneId.of("UTC"));
			compileTimestamp = Timestamp.valueOf(utcZoned.toLocalDateTime());
			if(utcZoned.isAfter(nowUTC)) {
				validations.put(Validation.DATE_AFTER_NOW, null);
			} else {
				OilLog lastIncludingCodeIMissedOilLog = operationLogReladomoService.getLastIncludingCodeIMissedOilLog(Activator.OIL_LOG_TYPE_1);
				if(lastIncludingCodeIMissedOilLog!=null && compileTimestamp.before(lastIncludingCodeIMissedOilLog.getBusinessDateFrom())) {
					dateError = true;
					validations.put(Validation.DATE_BEFORE_LAST_LOG, null);
				}
			}
		}
		
		String encodedSignatureString = null;
		WritableImage signatureImage = signature.getSignatureController().getSignatureImage();
		if(signatureImage==null) {
			validations.put(Validation.SIGNATURE_MISSING, null);
		} else {
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(SwingFXUtils.fromFXImage(signatureImage, null), "png", baos);
				byte[] bytes = baos.toByteArray();
				encodedSignatureString = Base64.getEncoder().encodeToString(bytes);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		String rank = signature.getSignatureController().getRank();
		if(rank==null) {
			validations.put(Validation.SIGNER_RANK_MISSING, null);
		}
		String name = signature.getSignatureController().getName();
		if(name==null) {
			validations.put(Validation.SIGNER_NAME_MISSING, null);
		}
		String surname = signature.getSignatureController().getSurname();
		if(surname==null) {
			validations.put(Validation.SIGNER_SURNAME_MISSING, null);
		}
		if(groupUUID==null)
			groupUUID = UUID.randomUUID().toString();
		
		List<OilLog> allTabsLogToInsertList = new ArrayList<OilLog>();
		Tab tab = mainTabPane.getSelectionModel().getSelectedItem();
		String codeLetter = tab.getText();// mainTabPane.getSelectionModel().getSelectedItem().getText();
		OilLog logToInsert = new OilLog(compileTimestamp);
		logToInsert.setType("OIL1");
		Long shipId = getShipId();
		if (shipId != null) {
			logToInsert.setShip(shipId);
		} else {
			validations.put(Validation.SHIP_MISSING, null);
		}
		logToInsert.setGroupUuid(groupUUID);
		logToInsert.setCodeLetter(codeLetter);
//		logToInsert.setDate(compileDate);
		if(restored) {
			logToInsert.setNote(signature.getSignatureController().getNote());
		} else {
			if(dateError) logToInsert.setNote("Date error!");
		}
		logToInsert.setSignature(encodedSignatureString);
		logToInsert.setOrderInGroup("");
		logToInsert.setSignerRank(rank);
		logToInsert.setSignerName(name);
		logToInsert.setSignerSurname(surname);

		List<OilLog> logToInsertList = new ArrayList<OilLog>();

		if (codeLetter.equals("A")) {
			oilRecordBookPart1CodeLetterAController.processA(logToInsert, logToInsertList, validations);
		} else if (codeLetter.equals("B")) {
			oilRecordBookPart1CodeLetterBController.processB(logToInsert, logToInsertList, validations);
		} else if (codeLetter.equals("C")) {
			oilRecordBookPart1CodeLetterCController.processC(logToInsert, logToInsertList, validations);
		} else if (codeLetter.equals("D")) {
			oilRecordBookPart1CodeLetterDController.processD(logToInsert, logToInsertList, validations);
		} else if (codeLetter.equals("E")) {
			oilRecordBookPart1CodeLetterEController.processE(logToInsert, logToInsertList, validations);
		} else if (codeLetter.equals("F")) {
			oilRecordBookPart1CodeLetterFController.processF(logToInsert, logToInsertList, validations);
		} else if (codeLetter.equals("G")) {
			oilRecordBookPart1CodeLetterGController.processG(logToInsert, logToInsertList, validations);
		} else if (codeLetter.equals("H")) {
			oilRecordBookPart1CodeLetterHController.processH(logToInsert, logToInsertList, validations);
		} else if (codeLetter.equals("I")) {
			oilRecordBookPart1CodeLetterIController.processI(logToInsert, logToInsertList, validations);
		}
		allTabsLogToInsertList.addAll(logToInsertList);
		
		if(signature.getSignatureController().checkChanged(allTabsLogToInsertList)) {
			validations.put(Validation.SIGNATURE_INVALID, null);
		}
		return new SimpleEntry<Map<Validation, Object>, List<OilLog>>(validations, allTabsLogToInsertList);
	}
	
	
	public void clearFilledInfo() {
		restored = false;
		restoredLabel.setText("");
		compileDate.setValue(null);
		for(Tab tab : mainTabPane.getTabs()) {
			String codeLetter = tab.getText();
			if (codeLetter.equals("A")) {
				oilRecordBookPart1CodeLetterAController.clearFilledInfo();
	
			} else if (codeLetter.equals("B")) {
				oilRecordBookPart1CodeLetterBController.clearFilledInfo();
	
			} else if (codeLetter.equals("C")) {
				oilRecordBookPart1CodeLetterCController.clearFilledInfo();
	
			} else if (codeLetter.equals("D")) {
				oilRecordBookPart1CodeLetterDController.clearFilledInfo();
	
			} else if (codeLetter.equals("E")) {
				oilRecordBookPart1CodeLetterEController.clearFilledInfo();
	
			} else if (codeLetter.equals("F")) {
				oilRecordBookPart1CodeLetterFController.clearFilledInfo();
	
			} else if (codeLetter.equals("G")) {
				oilRecordBookPart1CodeLetterGController.clearFilledInfo();
	
			} else if (codeLetter.equals("H")) {
				oilRecordBookPart1CodeLetterHController.clearFilledInfo();
	
			} else if (codeLetter.equals("I")) {
				oilRecordBookPart1CodeLetterIController.clearFilledInfo();
	
			}
		}
	}


	public void showBalloon(MouseEvent event) {
		ballonMessage.showGeneralBalloon((ImageViewButton) event.getTarget(), label1);
	}
	
	public void openLogBookInfoPdf(MouseEvent event) {
		try {
			BaseViewerFX baseViewerFX = new BaseViewerFX();
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initOwner(mainTabPane.getScene().getWindow());
			stage.setTitle("LogBook Info");
			byte[] bytes = IOUtils.toByteArray(getClass().getResourceAsStream("/images/INTRODUCTION-ORB-PART-I.pdf"));
			baseViewerFX.start(stage, createTempPdfFile(bytes));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		logger.info("end showPreview");
	}
	private File createTempPdfFile(byte[] pdfFileBytes) {
		File tempPdfFile = null;
		File tempFolder = null;
		try {
			tempFolder = new File(PluginTools.getFileLocalToRuntime("reports"));
			tempFolder.mkdir();
			tempPdfFile = File.createTempFile("pdf", ".pdf", tempFolder);
			OutputStream os = new FileOutputStream(tempPdfFile);
			os.write(pdfFileBytes);
			os.flush();
			os.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		tempPdfFile.deleteOnExit();
		return tempPdfFile;
	}
	
	public void openSignature(ActionEvent e) {
		Entry<Map<Validation, Object>, List<OilLog>> result = prepareLog();
		if (!checkShip(result)) return;
		if(result.getKey().keySet().contains(Validation.DATE_MISSING)) {
			showWarning("Date error", "Please specify the compilation date.");
			return;
		}
		signature.showSignature((Stage)((Button)e.getSource()).getScene().getWindow(), result.getValue());
	}




	public void openInfoMessage(MouseEvent event) {
		anchorPaneInfoMessage.setVisible(true);
	}

	public void closeInfoMessage(ActionEvent e) {
		anchorPaneInfoMessage.setVisible(false);
	}

	public void clearAllOperation(ActionEvent e) {
		if(showClearAllConfirmation("Clear All", "Are you sure you want to clear all inserted data?")) {
			clearFilledInfo();
		}
	}


	                   
    public void restoreLogGroup(List<OilLog> logGroup) {
    	if(logGroup==null || logGroup.isEmpty()) return;
    	OilLog oilLog0 = logGroup.get(0);
    	LocalDateTime date = new java.sql.Timestamp(oilLog0.getBusinessDate().getTime()).toLocalDateTime();
    	compileDate.setValue(date.toLocalDate());
		
		try {
			byte[] signatureBytes = Base64.getDecoder().decode(oilLog0.getSignature());
			BufferedImage bImage = ImageIO.read(new ByteArrayInputStream(signatureBytes));
			signature.getSignatureController().setSignatureImage(SwingFXUtils.toFXImage(bImage, null));
		}catch (Exception e) {
			e.printStackTrace();
		}
    	groupUUID = oilLog0.getGroupUuid();

    	signature.getSignatureController().setOilLogList(logGroup);
    	signature.getSignatureController().setRank(oilLog0.getSignerRank());
    	signature.getSignatureController().setName(oilLog0.getSignerName());
    	signature.getSignatureController().setSurname(oilLog0.getSignerSurname());
    	signature.getSignatureController().setNote(oilLog0.getNote());
    	signature.getSignatureController().restoreSignature();
    	
    	oilRecordBookPart1CodeLetterAController.restoreLogGroup(logGroup);
    	oilRecordBookPart1CodeLetterBController.restoreLogGroup(logGroup);
    	oilRecordBookPart1CodeLetterCController.restoreLogGroup(logGroup);
    	oilRecordBookPart1CodeLetterDController.restoreLogGroup(logGroup);
    	oilRecordBookPart1CodeLetterEController.restoreLogGroup(logGroup);
    	oilRecordBookPart1CodeLetterFController.restoreLogGroup(logGroup);
    	oilRecordBookPart1CodeLetterGController.restoreLogGroup(logGroup);
    	oilRecordBookPart1CodeLetterHController.restoreLogGroup(logGroup);
    	oilRecordBookPart1CodeLetterIController.restoreLogGroup(logGroup);
    	
    	restoredLabel.setText("RESTORED!");
    	restored = true;
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
	public static void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Warning");
        alert.setHeaderText(title);
        alert.setContentText(message);
        JavaFXUtils.undecorate(alert, closeButton, themeManager);
        alert.showAndWait();
    }
	public static boolean showConfirmation(String title, String message) {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Confirmation");
        alert.setHeaderText(title);
        alert.setContentText(message);
        JavaFXUtils.undecorate(alert, closeButton, themeManager);
        Optional<ButtonType> optional = alert.showAndWait();
        if(optional.isPresent() && optional.get()==ButtonType.OK) return true;
        else return false;
    }
	public static boolean showClearAllConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Confirmation");
        alert.setHeaderText(title);
        alert.setContentText(message);
        JavaFXUtils.undecorate(alert, closeButton, themeManager);
        Optional<ButtonType> optional = alert.showAndWait();
        if(optional.isPresent() && optional.get()==ButtonType.OK) return true;
        else return false;
    }
	
	
	@FXML
	private void showPreview(ActionEvent event) {
		logger.info("showPreview");
		Entry<Map<Validation, Object>, List<OilLog>> result = prepareLog();
		if(!checkValidation(result)) return;
		
		
//		List<String> groupUIIDList = new ArrayList<String>();
//		for(OilLog oilLog : result.getValue()) {
//			groupUIIDList.add(oilLog.getUuid());
//		}
//		Map<String, List<OilLog>> logGroupsMap = operationLogService.getGroupsOil1LogList(groupUIIDList);
		Ship ship = marpolBean.getSettings().getShip();
		String shipName = "";
		String shipCode = "";
		if(ship!=null) {
			shipName = ship.getName();
			shipCode = ship.getShipDistinctiveNumber();
		}
		
		List<ReportDataSourceBean> reportDataSourceBeanList = new ArrayList<ReportDataSourceBean>();
//		for(List<OilLog> oilLogList : logGroupsMap.values()) {
		if(result.getValue()!=null && !result.getValue().isEmpty()) {
			List<OilLog> oilLogList = result.getValue();
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
			reportDataSourceBean.setOilLogList(oilLogList);
			reportDataSourceBean.setMissedRank(missedRank);
			reportDataSourceBean.setMissedName(missedName);
			reportDataSourceBean.setMissedSurname(missedSurname);
			reportDataSourceBean.setCompilationDateMissedLetter(missedOperationDate);
			reportDataSourceBean.setSignatureFileNameMissedLetter(signaturemMissedTempFileName);

			oilLogList.sort(Comparator.comparing(oilLog -> oilLog.getOrderInGroup()));
			reportDataSourceBean.setOilLogList(oilLogList);
			reportDataSourceBeanList.add(reportDataSourceBean);
		}
		
		
		
		JasperReportDetails det = new JasperReportDetails();
		det.setDescription("Oil Record Book Log Part 1");
		
		String filePath = PluginTools.getFileLocalToRuntime(Activator.RUNTIME_LOCATION_FOLDER_NAME+"/reports/LogbookPart1.jasper");
		File file = new File(filePath);
		det.setMasterReportUrl(file.getAbsolutePath());
		
		
		Map<String, Object> reportParams = new HashMap<String, Object>();
		reportParams.put("shipName", shipName);
		reportParams.put("shipCode", shipCode);
		reportParams.put("partAndVersion", "I  V 1.0.0");
		det.setReportParameters(reportParams);
		try {
			File pdffile = jasperReportsService.getJasperReportsFactory().complieReportAsPdfFile(new File(PluginTools.getFileLocalToRuntime("reports")+"/Logbook.pdf"), det, reportParams, reportDataSourceBeanList);
			BaseViewerFX baseViewerFX = new BaseViewerFX();
			Stage reportStage = new Stage();
			reportStage.initOwner(((Node)event.getSource()).getScene().getWindow());
			reportStage.initModality(Modality.APPLICATION_MODAL);
			reportStage.setTitle("Logbook Report");
			baseViewerFX.start(reportStage, pdffile);

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
	
	private boolean checkValidation(Entry<Map<Validation, Object>, List<OilLog>> result) {
		if(result.getKey().keySet().contains(Validation.DATE_MISSING)) {
			showWarning("Date error", "Please specify the compilation date.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.DATE_MISSING_CODE_LETTERI)) {
			showWarning("Date error", "Please specify the compilation date for missed operation code letter.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.DATE_AFTER_NOW)) {
			showWarning("Date error", "The compilation date cannot be after the system date.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.MISSED_DATE_AFTER_NOW)) {
			showWarning("Date error", "The compilation missed operation date cannot be after the compilation date of code letter I.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.A1_EMPTY)) {
			showWarning("Alert code Nr 1", "Please complete identity of tank(s) ballasted.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.A2_EMPTY)) {
			showWarning("Alert code Nr 2", "Please insert type of oil previously carried.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.A3_1_EMPTY)) {
			showWarning("Alert code Nr 3.1", "Please insert time at the start and end of cleaning.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.A3_1_START_AFTER_END)) {
			showWarning("Alert code Nr 3.1", "Start time is after end of cleaning time.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.A3_1_TIME_AFTER_REGISTRATION_DATE)) {
			showWarning("Alert code Nr 3.1", "Start Time or Stop Time is after Registration Time.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.A3_2_BALLAST_EMPTY)) {
			showWarning("Alert code Nr 3.2", "Identify tank(s) is empty.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.A3_2_CHEMICALS_EMPTY)) {
			showWarning("Alert code Nr 3.2", "Q.ty of chemicals in M3 is empty.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.A3_2_CHEMICALS_NAME_EMPTY)) {
			showWarning("Alert code Nr 3.2", "Name of chemicals is empty.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.A3_2_CHEMICALS_SUM_ERROR)) {
			showWarning("Alert code Nr 3.2", "The total sum of chemicals is more than the total capacity!");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.A3_3_CLEANING_WATER_EMPTY)) {
			showWarning("Alert code Nr 3.3", "Identity of tank(s) into which cleaning water was transfered is empty.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.A3_3_QUANTITY_EMPTY)) {
			showWarning("Alert code Nr 3.3", "Q.ty in M3 is empty.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.A4_1_EMPTY)) {
			showWarning("Alert code Nr 4.1", "Please insert time at start and end of ballasting.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.A4_1_START_TIME_AFTER_END_TIME)) {
			showWarning("Alert code Nr 4.1", "Start Time is after Stop Time.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.A4_2_BALLAST_EMPTY)) {
			showWarning("Alert code Nr 4.2", "Please insert quantity of ballast.");
			return false;
		}
		
		if(result.getKey().keySet().contains(Validation.B5_EMPTY)) {
			showWarning("Alert code Nr 5", "Identity of tank(s) is empty.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.B6_LATITUDE_EMPTY)) {
			showWarning("Alert code Nr 6", "Please complete latitude.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.B6_LONGITUDE_EMPTY)) {
			showWarning("Alert code Nr 6", "Please complete longitude.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.B7_LATITUDE_EMPTY)) {
			showWarning("Alert code Nr 7", "Please complete latitude.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.B7_LONGITUDE_EMPTY)) {
			showWarning("Alert code Nr 7", "Please complete longitude.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.B8_EMPTY)) {
			showWarning("Alert code Nr 8", "Ship's speed(s) during discharge is empty.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.B9_EMPTY)) {
			showWarning("Alert code Nr 9", "Please select a method of discharge.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.B9_FACILITIES_OR_RECEIPT_EMPTY)) {
			showWarning("Alert code Nr 9", "Please specify the reception facilities name and receipt file.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.B10_EMPTY)) {
			showWarning("Alert code Nr 10", "Quantity discharged is empty.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.C11_1_EMPTY)) {
			showWarning("Alert code Nr 11.1", "Identity of tank(s) is empty.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.C11_2_EMPTY)) {
			showWarning("Alert code Nr 11.2", "Capacity of tank(s) in m3 is empty.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.C11_3_EMPTY)) {
			showWarning("Alert code Nr 11.3", "Capacity of tank(s) in m3 is empty.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.C11_4_EMPTY)) {
			showWarning("Alert code Nr 11.4", "Quantity of residue collected by manual operation or source tank is empty.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.C12_1_C12_2_C12_3_C12_4_REQUIRED)) {
			showWarning("Alert code Nr 12.1, Nr 12.2, Nr 12.3, Nr 12.4", "Exactly one of Nr 12.1, Nr 12.2, Nr 12.3, Nr 12.4 should be compiled.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.C12_TANK_REQUIRED)) {
			showWarning("Alert code Nr 12", "Quantity transfered is empty or tank not selected.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.C12_SLUDGE_OR_WATER_REQUIRED)) {
			showWarning("Alert code Nr 12", "Either Sludge Transferred or Water Drained must be specified.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.C12_1_FACILITIES_OR_RECEIPT_EMPTY)) {
			showWarning("Alert code Nr 12.1", "Please specify the reception facilities name and receipt file.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.C12_2_EMPTY)) {
			showWarning("Alert code Nr 12.2", "To another (other) tank(s) (indicate tank(s) is empty.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.C12_2_EMPTY)) {
			showWarning("Alert code Nr 12.2", "Total content of tank(s) is empty.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.C12_3_EMPTY)) {
			showWarning("Alert code Nr 12.3", "Incinerated (indicate total time of operation) is empty.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.C12_4_EMPTY)) {
			showWarning("Alert code Nr 12.4", "Other method (state which) is empty.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.C12_TANK_ALREADY_IN_LIST)) {
			showWarning("Alert code Nr 12", "Tank already in list.\nTank name: " + (String)result.getKey().get(Validation.C12_TANK_ALREADY_IN_LIST));
			return false;
		}
		if(result.getKey().keySet().contains(Validation.D13_EMPTY)) {
			showWarning("Alert code Nr 13", "Quantity discharged, transferred or disposed is empty.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.D13_TANK_ALREADY_IN_LIST)) {
			showWarning("Alert code Nr 13", "Tank already in list.\nTank name: " + (String)result.getKey().get(Validation.D13_TANK_ALREADY_IN_LIST));
			return false;
		}
		if(result.getKey().keySet().contains(Validation.D14_EMPTY)) {
			showWarning("Alert code Nr 14", "Please insert time at the start and end of discharge, transfer or disposal.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.D14_START_AFTER_END)) {
			showWarning("Alert code Nr 14", "Start time is after end of discharge, transfer or disposal time.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.D14_TIME_AFTER_REGISTRATION_DATE)) {
			showWarning("Alert code Nr 14", "Time of discharge, transfer or disposal is after Registration Time.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.D15_2_FACILITIES_OR_RECEIPT_EMPTY)) {
			showWarning("Alert code Nr 15.2", "Please specify the reception facilities name and receipt file.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.D15_3_Tank_EMPTY)) {
			showWarning("Alert code Nr 15.3", "To slop tank or holding tank or other tank(s) is empty.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.D15_3_TANK_ALREADY_IN_LIST)) {
			showWarning("Alert code Nr 15.3", "Tank already in list.\nTank name: " + (String)result.getKey().get(Validation.D15_3_TANK_ALREADY_IN_LIST));
			return false;
		}
		if(result.getKey().keySet().contains(Validation.E17_EMPTY)) {
			showWarning("Alert code Nr 17", "To holding tank (identify tank) is empty.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.E17_TANK_ALREADY_IN_LIST)) {
			showWarning("Alert code Nr 17", "Tank already in list.\nTank name: " + (String)result.getKey().get(Validation.E17_TANK_ALREADY_IN_LIST));
			return false;
		}
		if(result.getKey().keySet().contains(Validation.F21_EMPTY)) {
			showWarning("Alert code Nr 21", "Reasons for failure is empty.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.G24_EMPTY)) {
			showWarning("Alert code Nr 24", "Approximate quantity and type of oil is empty.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.G24_TYPE_OIL_EMPTY)) {
			showWarning("Alert code Nr 24", "Type of oil is empty.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.G25_EMPTY)) {
			showWarning("Alert code Nr 25", "Circumstances of discharge or escape is empty.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.H26_1_PLACEOFBUNKERING_EMPTY)) {
			showWarning("Alert code Nr 26.1", "Please specify the place of bunkering.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.H26_1_26_4_BND_EMPTY)) {
			showWarning("Alert code Nr 26.1 or 26.4", "Please specify at least one BDN file, in 26.1 or 26.4.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.H26_3_TANK_ALREADY_IN_LIST)) {
			showWarning("Alert code Nr 26.3", "Tank already in list.\nTank name: " + (String)result.getKey().get(Validation.H26_3_TANK_ALREADY_IN_LIST));
			return false;
		}
		if(result.getKey().keySet().contains(Validation.H26_4_TANK_ALREADY_IN_LIST)) {
			showWarning("Alert code Nr 26.4", "Tank already in list.\nTank name: " + (String)result.getKey().get(Validation.H26_4_TANK_ALREADY_IN_LIST));
			return false;
		}
		if(result.getKey().keySet().contains(Validation.I3_TANK_ALREADY_IN_LIST)) {
			showWarning("Alert code Nr I.3", "Tank already in list.\nTank name: " + (String)result.getKey().get(Validation.I3_TANK_ALREADY_IN_LIST));
			return false;
		}
		if(result.getKey().keySet().contains(Validation.IO0_EMPTY)) {
			showWarning("Alert code Nr I.1", "Please insert overboard valve and seal no.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.IO1_EMPTY)) {
			showWarning("Alert code Nr I.2", "Please insert overboard valve, reason and seal no.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.IO2_EMPTY)) {
			showWarning("Alert code Nr I.3", "Please complete all fields.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.IO2_EMPTY_FUEL_TYPE)) {
			showWarning("Alert code Nr I.3", "Please insert fuel type.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.IO2_START_AFTER_END)) {
			showWarning("Alert code Nr I.3", "Start time is after end of de-bunkering time.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.IO2_TIME_AFTER_REGISTRATION_DATE)) {
			showWarning("Alert code Nr I.3", "Start Time or Stop Time is after Registration Time.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.IO4_EMPTY)) {
			showWarning("Alert code Nr I.4", "Please insert description.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.IO5_TANK_EMPTY)) {
			showWarning("Alert code Nr I.5", "Identity of tank(s) is required.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.I_1_I_4_SAME_TANK)) {
			showWarning("Alert code Nr I.1, Nr I.4", "The same tank(s) is/are specified in I.1 Identity of tank(s) and I.4 Quantity of residue collected by manual operation.\nTank name(s): " +  (String)result.getKey().get(Validation.I_1_I_4_SAME_TANK));
			return false;
		}
		if(result.getKey().keySet().contains(Validation.A1_REQUIRED)) {
			showWarning("Alert code Nr 1", "Identity of tank(s) ballasted is required.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.A2_REQUIRED)) {
			showWarning("Alert code Nr 2", "Whether cleaned since they last contained oil is required.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.A3_1_REQUIRED)) {
			showWarning("Alert code Nr 3.1", "Cleaning process, position of ship is required, if tanks are cleaned.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.A3_1_NOT_REQUIRED)) {
			showWarning("Alert code Nr 3.1", "Cleaning process, position of ship should not be compiled, if tanks are not cleaned.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.A3_2_REQUIRED)) {
			showWarning("Alert code Nr 3.2", "Cleaning process, identity of tank(s) is required, if tanks are cleaned.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.A3_2_NOT_REQUIRED)) {
			showWarning("Alert code Nr 3.2", "Cleaning process, identity of tank(s) should not be compiled, if tanks are not cleaned.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.A3_3_REQUIRED)) {
			showWarning("Alert code Nr 3.3", "Cleaning process, identity of tank(s) into which cleaning water was tranfered is required, if tanks are cleaned.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.A3_3_NOT_REQUIRED)) {
			showWarning("Alert code Nr 3.3", "Cleaning process, identity of tank(s) into which cleaning water was tranfered should not be compiled, if tanks are not cleaned.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.A4_1_REQUIRED)) {
			showWarning("Alert code Nr 4.1", "Ballasting, position of ship is required, if tanks are not cleaned.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.A4_1_NOT_REQUIRED)) {
			showWarning("Alert code Nr 4.1", "Ballasting, position of ship should not be compiled, if tanks are cleaned.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.A4_2_REQUIRED)) {
			showWarning("Alert code Nr 4.2", "Ballasting, quantity of ballast is required, if tanks are not cleaned.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.A4_2_NOT_REQUIRED)) {
			showWarning("Alert code Nr 4.2", "Ballasting, quantity of ballast should not be compiled, if tanks are cleaned.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.A1_A3_2_NOT_SAME_TANK)) {
			showWarning("Alert code Nr 1, Nr 3.2", "The tank(s) specified in 3.2 Cleaning process, identity of tank(s) is/are NOT contained in 1 Identity of tank(s) ballasted.\nTank name(s): " +  (String)result.getKey().get(Validation.A1_A3_2_NOT_SAME_TANK));
			return false;
		}
		if(result.getKey().keySet().contains(Validation.A1_A3_3_SAME_TANK)) {
			showWarning("Alert code Nr 1, Nr 3.3", "The same tank(s) is/are specified in 1 Identity of tank(s) ballasted and 3.3 Cleaning process, identity of tank(s) into which cleaning water was tranfered.\nTank name(s): " +  (String)result.getKey().get(Validation.A1_A3_3_SAME_TANK));
			return false;
		}
		
		if(result.getKey().keySet().contains(Validation.B5_REQUIRED)) {
			showWarning("Alert code Nr 5", "Identity of tank(s) is required.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.B5_DEPENDENCY_ERROR)) {
			showWarning("Alert code Nr 5", "Code letter A should be created before code letter B.");
			return false;
		}
		
		if(result.getKey().keySet().contains(Validation.B9_REQUIRED)) {
			showWarning("Alert code Nr 9", "Method of discharge is required.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.B6_B7_B8_VALUE_REQUIRED)) {
			showWarning("Alert code Nr 6, 7 and 8", "When method of discharge is through 15 ppm equipment, position of ship at start of discharge, position of ship at end of discharge and ship's speed during discharge are required.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.B6_B7_B8_VALUE_NOT_REQUIRED)) {
			showWarning("Alert code Nr 6, 7 and 8", "When method of discharge is to reception facilities, position of ship at start of discharge, position of ship at end of discharge and ship's speed during discharge should be empty.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.B10_REQUIRED)) {
			showWarning("Alert code Nr 10", "Quantity discharged is required.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.B10_DEPENDENCY_ERROR)) {
			showWarning("Alert code Nr 10", "Code letter A should be created before code letter B.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.C11_OR_C12_REQUIRED)) {
			showWarning("Alert code Nr 11, Nr 12", "Code Nr 11 of code Nr 12 is required.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.C11_1_REQUIRED)) {
			showWarning("Alert code Nr 11.1", "Identity of tank(s) is required.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.C11_2_REQUIRED)) {
			showWarning("Alert code Nr 11.2", "Capacity of tank(s) is required.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.C11_3_OR_C11_4_REQUIRED)) {
			showWarning("Alert code Nr 11.3, Nr 11.4", "11.3 Total quantity of retention or 11.4 Quantity of residue collected by manual operation should be compiled, but not both.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.C11_2_SUM_ERROR)) {
			showWarning("Alert code Nr 11.2", "The total sum is more than total!");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.C11_1_C11_4_SAME_TANK)) {
			showWarning("Alert code Nr 11.1, Nr 11.4", "The same tank(s) is/are specified in 11.1 Identity of tank(s) and 11.4 Quantity of residue collected by manual operation.\nTank name(s): " +  (String)result.getKey().get(Validation.C11_1_C11_4_SAME_TANK));
			return false;
		}
		if(result.getKey().keySet().contains(Validation.C12_2_SAME_TANK_IN_FROM_AND_TO)) {
			showWarning("Alert code Nr 12.2", "The same tank(s) is/are specified in the FROM and TO fields.\nTank name(s): " +  (String)result.getKey().get(Validation.C12_2_SAME_TANK_IN_FROM_AND_TO));
			return false;
		}
		


		if(result.getKey().keySet().contains(Validation.D13_QUANTITY_REQUIRED)) {
			showWarning("Alert code Nr 13", "Quantity discharged is required if from Engine Room Bilge Wells is selected.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.D13_BILGEWELLS_AND_BILGETANKS_EMPTY)) {
			showWarning("Alert code Nr 13", "Origin of discharge either from Bilge Wells or Bilge Tanks must be specified.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.D13_TANK_REQUIRED)) {
			showWarning("Alert code Nr 13", "Tank to discharge, transfer or dispose is required if from Bilge Tanks is selected.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.D14_REQUIRED)) {
			showWarning("Alert code Nr 14", "Time of discharge is required.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.D15_REQUIRED)) {
			showWarning("Alert code Nr 15", "Method of discharge is required.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.E16_REQUIRED)) {
			showWarning("Alert code Nr 16", "Time of automatic discharge is required.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.E16_TIME_AFTER_REGISTRATION_DATE)) {
			showWarning("Alert code Nr 16", "Time of automatic discharge is after Registration Date.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.E17_REQUIRED)) {
			showWarning("Alert code Nr 17", "Time of automatic bilge water transfer is required.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.E17_TIME_AFTER_REGISTRATION_DATE)) {
			showWarning("Alert code Nr 17", "Time of automatic bilge water transfer is after Registration Date.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.E18_REQUIRED)) {
			showWarning("Alert code Nr 18", "Time of manual operation is required.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.E17_TIME_AFTER_REGISTRATION_DATE)) {
			showWarning("Alert code Nr 18", "Time of manual operation is after Registration Date.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.F19_REQUIRED)) {
			showWarning("Alert code Nr 19", "Time of system failure is required.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.F19_EMPTY)) {
			showWarning("Alert code Nr 19", "Time of system failure is empty.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.F19_OR_F20_REQUIRED)) {
			showWarning("Alert code Nr 19", "Time of system failure or time when system has been made operational is required.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.F19_TIME_AFTER_REGISTRATION_DATE)) {
			showWarning("Alert code Nr 19", "Time of system failure is after Registration Date.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.F20_REQUIRED)) {
			showWarning("Alert code Nr 20", "Time when system has been made operational is required.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.F20_TIME_AFTER_REGISTRATION_DATE)) {
			showWarning("Alert code Nr 20", "Time when system has been made operational is after Registration Date.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.F21_REQUIRED)) {
			showWarning("Alert code Nr 21", "Reason for failure is required.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.G22_REQUIRED)) {
			showWarning("Alert code Nr 22", "Time of occurrence is required.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.G22_TIME_AFTER_REGISTRATION_DATE)) {
			showWarning("Alert code Nr 22", "Time of occurrence is after Registration Date.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.G23_REQUIRED)) {
			showWarning("Alert code Nr 23", "Place or position of ship at time of occurrence is required.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.G24_REQUIRED)) {
			showWarning("Alert code Nr 24", "Approximate quantity and type of oil is required.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.G25_REQUIRED)) {
			showWarning("Alert code Nr 25", "Circumstances of discharge or escape, the reasons therefore and general remarks is required.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.H26_REQUIRED)) {
			showWarning("Alert code Nr 26", "Bunkering is required.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.H26_1_26_2_26_3_OR_26_4_ALL_OR_NOTHING)) {
			showWarning("Alert code Nr 26", "Bunkering place, time and quantity should be compiled all or nothing.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.H26_2_EMPTY)) {
			showWarning("Alert code Nr 26.2", "Bunkering time should be compiled.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.H26_2_START_AFTER_END)) {
			showWarning("Alert code Nr 26.2", "Start time is after end of bunkering time.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.H26_2_TIME_AFTER_REGISTRATION_DATE)) {
			showWarning("Alert code Nr 26.2", "Bunkering start time or end time is after Registration Date.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.H26_3_MISSING_QUANTITY_OR_TYPE)) {
			showWarning("Alert code Nr 26.3", "Operation (" + (String)result.getKey().get(Validation.H26_3_MISSING_QUANTITY_OR_TYPE) + ") is missing quantity or type of fuel.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.H26_3_MISSING_TANK_DATA)) {
			showWarning("Alert code Nr 26.3", "Tank (" + (String)result.getKey().get(Validation.H26_3_MISSING_TANK_DATA) + ") is missing required data.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.H26_4_MISSING_QUANTITY_OR_TYPE)) {
			showWarning("Alert code Nr 26.4", "Operation (" + (String)result.getKey().get(Validation.H26_4_MISSING_QUANTITY_OR_TYPE) + ") is missing quantity or type of oil.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.H26_4_MISSING_TANK_DATA)) {
			showWarning("Alert code Nr 26.4", "Tank (" + (String)result.getKey().get(Validation.H26_4_MISSING_TANK_DATA) + ") is missing required data.");
			return false;
		}
		
		if(result.getKey().keySet().contains(Validation.SIGNATURE_MISSING) || result.getKey().keySet().contains(Validation.SIGNATURE_INVALID)) {
			showInformation("Signature Required", "Please sign the modifications.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.SIGNER_RANK_MISSING) || result.getKey().keySet().contains(Validation.SIGNER_NAME_MISSING) || result.getKey().keySet().contains(Validation.SIGNER_SURNAME_MISSING)) {
			showInformation("Rank, Name and Surname of signer required", "Please specify signer details.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.SIGNATURE_MISSING_CODE_LETTERI)) {
			showInformation("Signature Required", "Please sign missed operation code letter.");
			return false;
		}
		if(result.getKey().keySet().contains(Validation.B5_VALUE_ERROR)) {
			boolean toContinue = showConfirmation("Alert code Nr 5", "The value referes to code A1 is " + result.getKey().get(Validation.B5_VALUE_ERROR) + ". Do you want to continue?");
			if(!toContinue) return false;
		}
		if(result.getKey().keySet().contains(Validation.B10_VALUE_ERROR)) {
			boolean toContinue = showConfirmation("Alert code Nr 10", "The quantity of ballast refers to code letter A3.3 or A4.2 is " + MathUtils.decimalFormat.format(((Double[])result.getKey().get(Validation.B10_VALUE_ERROR))[0]) + " M3. Actual discharge is "+ MathUtils.decimalFormat.format(((Double[])result.getKey().get(Validation.B10_VALUE_ERROR))[1]) +" M3. Do you want to continue?");
			if(!toContinue) return false;
		}
		if(result.getKey().keySet().contains(Validation.A3_1_LAT_LON_ZERO)) {
			boolean toContinue = showConfirmation("Alert code Nr 3", "Lattitude or longitude is 0.\nDo you want to continue?");
			if(!toContinue) return false;
		}
		if(result.getKey().keySet().contains(Validation.A4_1_LAT_LON_ZERO)) {
			boolean toContinue = showConfirmation("Alert code Nr 4", "Lattitude or longitude is 0.\nDo you want to continue?");
			if(!toContinue) return false;
		}
		if(result.getKey().keySet().contains(Validation.A3_3_WATER_SUM_0_98_WARNING)) {
			boolean toContinue = showConfirmation("Alert code Nr 3.3", "The total sum of water is more than 98% of the total capacity!\nDo you want to continue?");
			if(!toContinue) return false;
		}
		if(result.getKey().keySet().contains(Validation.B6_LAT_LON_ZERO)) {
			boolean toContinue = showConfirmation("Alert code Nr 6", "Lattitude or longitude is 0.\nDo you want to continue?");
			if(!toContinue) return false;
		}
		if(result.getKey().keySet().contains(Validation.B7_LAT_LON_ZERO)) {
			boolean toContinue = showConfirmation("Alert code Nr 7", "Lattitude or longitude is 0.\nDo you want to continue?");
			if(!toContinue) return false;
		}
		if(result.getKey().keySet().contains(Validation.B15_1_LAT_LON_ZERO)) {
			boolean toContinue = showConfirmation("Alert code Nr 15", "Lattitude or longitude is 0.\nDo you want to continue?");
			if(!toContinue) return false;
		}
		if(result.getKey().keySet().contains(Validation.C11_3_SUM_WARNING)) {
			boolean toContinue = showConfirmation("Alert code Nr 11.3", "The value exceed the 98% of Maximun tank capacity!\nDo you want to continue?");
			if(!toContinue) return false;
		}
		if(result.getKey().keySet().contains(Validation.C11_1_SUM_DIFFERENT_C11_4_SUM_WARNING)) {
			boolean toContinue = showConfirmation("Alert code Nr 11.1, Nr 11.4", "The value added in the tanks in point Nr 11.1 (" + MathUtils.decimalFormat.format(((double[])result.getKey().get(Validation.C11_1_SUM_DIFFERENT_C11_4_SUM_WARNING))[0]) + " M3) is not equal to the quantity added in the tanks in point Nr 11.4 (" + MathUtils.decimalFormat.format(((double[])result.getKey().get(Validation.C11_1_SUM_DIFFERENT_C11_4_SUM_WARNING))[1]) + " M3)!\nDo you want to continue?");
			if(!toContinue) return false;
		}
		if(result.getKey().keySet().contains(Validation.C12_SUM_DIFFERENT_C12_2_SUM_WARNING)) {
			boolean toContinue = showConfirmation("Alert code Nr 12.2", "The value added in the tanks in point Nr 12 (" + MathUtils.decimalFormat.format(((double[])result.getKey().get(Validation.C12_SUM_DIFFERENT_C12_2_SUM_WARNING))[0]) + " M3) is not equal to the quantity added in the tanks in point Nr 12.2 (" + MathUtils.decimalFormat.format(((double[])result.getKey().get(Validation.C12_SUM_DIFFERENT_C12_2_SUM_WARNING))[1]) + " M3)!\nDo you want to continue?");
			if(!toContinue) return false;
		}
		if(result.getKey().keySet().contains(Validation.C12_4_PartII_J_MAY_BE_REQUIRED)) {
			showInformation("Information code Nr 12.4", "Requires an entry in the Oil Record Book – Part II using code (J).\r\n"
					+ "If sludge or bilge water is transferred from multiple tanks in engine-room a separate entry must be made in ORB Parts I & II for each transfer.");
		}
		if(result.getKey().keySet().contains(Validation.D14_TIME_INTERVAL_SHORT)) {
			boolean toContinue = showConfirmation("Alert code Nr 14", "The time interval is to short to discharge/dispose the specified quantity (" + MathUtils.decimalFormat.format(((double[])result.getKey().get(Validation.D14_TIME_INTERVAL_SHORT))[0]) + " M3) with the maximum throughput (" + MathUtils.decimalFormat.format(((double[])result.getKey().get(Validation.D14_TIME_INTERVAL_SHORT))[1]) + " M3) specified in settings!\nDo you want to continue?");
			if(!toContinue) return false;
		}
		if(result.getKey().keySet().contains(Validation.D15_TRANSFERRED_QUANTITIES_NOT_EQUAL)) {
			boolean toContinue = showConfirmation("Alert code Nr 15", "The value added in the tanks in point Nr 13 (" + MathUtils.decimalFormat.format(((double[])result.getKey().get(Validation.D15_TRANSFERRED_QUANTITIES_NOT_EQUAL))[0]) + " M3) is not equal to the quantity added in the tanks in point Nr 15.3 (" + MathUtils.decimalFormat.format(((double[])result.getKey().get(Validation.D15_TRANSFERRED_QUANTITIES_NOT_EQUAL))[1]) + " M3)!\nDo you want to continue?");
			if(!toContinue) return false;
		}
		if(result.getKey().keySet().contains(Validation.E16_LAT_LON_ZERO)) {
			boolean toContinue = showConfirmation("Alert code Nr 16", "Lattitude or longitude is 0.\nDo you want to continue?");
			if(!toContinue) return false;
		}
		if(result.getKey().keySet().contains(Validation.G23_LAT_LON_ZERO)) {
			boolean toContinue = showConfirmation("Alert code Nr 23", "Lattitude or longitude is 0.\nDo you want to continue?");
			if(!toContinue) return false;
		}
		if(result.getKey().keySet().contains(Validation.H26_3_TRANSFERRED_QUANTITIES_NOT_EQUAL)) {
			boolean toContinue = showConfirmation("Alert code Nr 26.3", "The total fuel quantity added in point Nr 26.3 (" + MathUtils.decimalFormat.format(((double[])result.getKey().get(Validation.H26_3_TRANSFERRED_QUANTITIES_NOT_EQUAL))[0]) + " MT) is not equal to the quantity added in the tanks in point Nr 26.3 (" + MathUtils.decimalFormat.format(((double[])result.getKey().get(Validation.H26_3_TRANSFERRED_QUANTITIES_NOT_EQUAL))[1]) + " MT)!\nDo you want to continue?");
			if(!toContinue) return false;
		}
		if(result.getKey().keySet().contains(Validation.H26_4_TRANSFERRED_QUANTITIES_NOT_EQUAL)) {
			boolean toContinue = showConfirmation("Alert code Nr 26.4", "The total oil quantity added in point Nr 26.4 (" + MathUtils.decimalFormat.format(((double[])result.getKey().get(Validation.H26_4_TRANSFERRED_QUANTITIES_NOT_EQUAL))[0]) + " MT) is not equal to the quantity added in the tanks in point Nr 26.4 (" + MathUtils.decimalFormat.format(((double[])result.getKey().get(Validation.H26_4_TRANSFERRED_QUANTITIES_NOT_EQUAL))[1]) + " MT)!\nDo you want to continue?");
			if(!toContinue) return false;
		}
		if(result.getKey().keySet().contains(Validation.D13_C11_4_MAY_BE_REQUIRED)) {
			showInformation("Information code Nr 13", "A code C.11.4 recording may be required if this operation is a manual operator initiated operation.");
		}
		if(result.getKey().keySet().contains(Validation.D15_2_PartII_J_MAY_BE_REQUIRED)) {
			showInformation("Information code Nr 15.2", "Requires an entry in the Oil Record Book – Part II using code (J).\r\n"
					+ "If sludge or bilge water is transferred from multiple tanks in engine-room a separate entry must be made in ORB Parts I & II for each transfer.");
		}
		
		if(result.getKey().keySet().contains(Validation.I_1_SUM_DIFFERENT_I_4_SUM_WARNING)) {
			boolean toContinue = showConfirmation("Alert code Nr I.1, Nr I.4", "The value added in the tanks in point Nr I.1 (" + MathUtils.decimalFormat.format(((double[])result.getKey().get(Validation.I_1_SUM_DIFFERENT_I_4_SUM_WARNING))[0]) + " M3) is not equal to the quantity added in the tanks in point Nr I.4 (" + MathUtils.decimalFormat.format(((double[])result.getKey().get(Validation.I_1_SUM_DIFFERENT_I_4_SUM_WARNING))[1]) + " M3)!\nDo you want to continue?");
			if(!toContinue) return false;
		}
		
		if(result.getKey().keySet().contains(Validation.DATE_BEFORE_LAST_LOG)) {
			showWarning("Date error", "The compilation date cannot be before the last log compilation date.");
			return false;
		}
		return true;
	}

	public void setOilRecordBookPart1CodeLetterAController(
			OilRecordBookPart1CodeLetterAController oilRecordBookPart1CodeLetterAController) {
		this.oilRecordBookPart1CodeLetterAController = oilRecordBookPart1CodeLetterAController;
	}

	public void setOilRecordBookPart1CodeLetterBController(
			OilRecordBookPart1CodeLetterBController oilRecordBookPart1CodeLetterBController) {
		this.oilRecordBookPart1CodeLetterBController = oilRecordBookPart1CodeLetterBController;
	}
	public void setOilRecordBookPart1CodeLetterCController(
			OilRecordBookPart1CodeLetterCController oilRecordBookPart1CodeLetterCController) {
		this.oilRecordBookPart1CodeLetterCController = oilRecordBookPart1CodeLetterCController;
	}
	public void setOilRecordBookPart1CodeLetterDController(
			OilRecordBookPart1CodeLetterDController oilRecordBookPart1CodeLetterDController) {
		this.oilRecordBookPart1CodeLetterDController = oilRecordBookPart1CodeLetterDController;
	}
	public void setOilRecordBookPart1CodeLetterEController(
			OilRecordBookPart1CodeLetterEController oilRecordBookPart1CodeLetterEController) {
		this.oilRecordBookPart1CodeLetterEController = oilRecordBookPart1CodeLetterEController;
	}
	public void setOilRecordBookPart1CodeLetterFController(
			OilRecordBookPart1CodeLetterFController oilRecordBookPart1CodeLetterFController) {
		this.oilRecordBookPart1CodeLetterFController = oilRecordBookPart1CodeLetterFController;
	}
	public void setOilRecordBookPart1CodeLetterGController(
			OilRecordBookPart1CodeLetterGController oilRecordBookPart1CodeLetterGController) {
		this.oilRecordBookPart1CodeLetterGController = oilRecordBookPart1CodeLetterGController;
	}
	public void setOilRecordBookPart1CodeLetterHController(
			OilRecordBookPart1CodeLetterHController oilRecordBookPart1CodeLetterHController) {
		this.oilRecordBookPart1CodeLetterHController = oilRecordBookPart1CodeLetterHController;
	}
	public void setOilRecordBookPart1CodeLetterIController(
			OilRecordBookPart1CodeLetterIController oilRecordBookPart1CodeLetterIController) {
		this.oilRecordBookPart1CodeLetterIController = oilRecordBookPart1CodeLetterIController;
	}
	public void setOilRecordBookPart1ViewControllerToCodeLetterI(OilRecordBookPart1ViewController oilRecordBookPart1ViewController) {
		oilRecordBookPart1CodeLetterIController.setController(oilRecordBookPart1ViewController);
	}
	public enum Validation {
		SHIP_MISSING, DATE_MISSING, DATE_MISSING_CODE_LETTERI, DATE_BEFORE_LAST_LOG, DATE_AFTER_NOW, MISSED_DATE_AFTER_NOW,
		SIGNATURE_MISSING,
		SIGNATURE_MISSING_CODE_LETTERI,
		SIGNATURE_INVALID,
		SIGNER_NAME_MISSING,
		SIGNER_SURNAME_MISSING,
		SIGNER_RANK_MISSING,
		A1_EMPTY, A1_REQUIRED,
		A2_EMPTY, A2_REQUIRED,
		A3_1_EMPTY, A3_1_START_AFTER_END, A3_1_REQUIRED, A3_1_NOT_REQUIRED, A3_1_TIME_AFTER_REGISTRATION_DATE, A3_1_LAT_LON_ZERO,
		A3_2_BALLAST_EMPTY, A3_2_CHEMICALS_EMPTY, A3_2_CHEMICALS_NAME_EMPTY, A3_2_CHEMICALS_SUM_ERROR, A3_2_REQUIRED, A3_2_NOT_REQUIRED,
		A3_3_CLEANING_WATER_EMPTY, A3_3_QUANTITY_EMPTY, A3_3_WATER_SUM_0_98_WARNING, A3_3_REQUIRED, A3_3_NOT_REQUIRED,
		A4_1_EMPTY, A4_1_REQUIRED, A4_1_NOT_REQUIRED, A4_1_START_TIME_AFTER_END_TIME, A4_1_LAT_LON_ZERO,
		A4_2_BALLAST_EMPTY, A4_2_REQUIRED, A4_2_NOT_REQUIRED,
		A1_A3_2_NOT_SAME_TANK, A1_A3_3_SAME_TANK,
		B5_EMPTY, B5_REQUIRED, B5_VALUE_ERROR, B5_DEPENDENCY_ERROR,
		B6_LATITUDE_EMPTY, B6_LONGITUDE_EMPTY, B6_LAT_LON_ZERO,
		B7_LATITUDE_EMPTY, B7_LONGITUDE_EMPTY, B7_LAT_LON_ZERO,
		B8_EMPTY,
		B9_EMPTY, B9_REQUIRED, B6_B7_B8_VALUE_REQUIRED, B6_B7_B8_VALUE_NOT_REQUIRED, B9_FACILITIES_OR_RECEIPT_EMPTY,
		B10_EMPTY, B10_REQUIRED, B10_VALUE_ERROR, B10_DEPENDENCY_ERROR,
		C11_OR_C12_REQUIRED,
		C11_1_EMPTY, C11_1_REQUIRED, C11_1_C11_4_SAME_TANK,
		C11_2_EMPTY, C11_2_SUM_ERROR, C11_2_REQUIRED,
		C11_3_EMPTY, C11_3_SUM_WARNING, C11_3_OR_C11_4_REQUIRED, C11_1_SUM_DIFFERENT_C11_4_SUM_WARNING,
		C11_4_EMPTY,
		C12_TANK_REQUIRED, C12_TANK_ALREADY_IN_LIST,
		C12_1_FACILITIES_OR_RECEIPT_EMPTY, C12_1_C12_2_C12_3_C12_4_REQUIRED, C12_SLUDGE_OR_WATER_REQUIRED,
		C12_2_EMPTY, C12_2_TOTAL_EMPTY, C12_2_SAME_TANK_IN_FROM_AND_TO,
		C12_3_EMPTY,
		C12_4_EMPTY, C12_4_PartII_J_MAY_BE_REQUIRED,
		C12_SUM_DIFFERENT_C12_2_SUM_WARNING,
		D13_EMPTY, D13_BILGEWELLS_AND_BILGETANKS_EMPTY, D13_QUANTITY_REQUIRED, D13_TANK_REQUIRED, D13_TANK_ALREADY_IN_LIST,
		D14_REQUIRED, D14_EMPTY, D14_START_AFTER_END, D14_TIME_INTERVAL_SHORT,
		D15_REQUIRED, D15_TRANSFERRED_QUANTITIES_NOT_EQUAL, B15_1_LAT_LON_ZERO,
		D15_2_FACILITIES_OR_RECEIPT_EMPTY, D14_TIME_AFTER_REGISTRATION_DATE,
		D15_3_Tank_EMPTY, D15_3_TANK_ALREADY_IN_LIST,
		D13_C11_4_MAY_BE_REQUIRED, D15_2_PartII_J_MAY_BE_REQUIRED,
		E16_REQUIRED, E16_TIME_AFTER_REGISTRATION_DATE, E16_LAT_LON_ZERO,
		E17_EMPTY, E17_REQUIRED, E17_TIME_AFTER_REGISTRATION_DATE, E17_TANK_ALREADY_IN_LIST,
		E18_REQUIRED, E18_TIME_AFTER_REGISTRATION_DATE,
		F19_REQUIRED, F19_EMPTY, F19_TIME_AFTER_REGISTRATION_DATE,
		F19_OR_F20_REQUIRED,
		F20_REQUIRED, F20_TIME_AFTER_REGISTRATION_DATE,
		F21_REQUIRED,
		F21_EMPTY,
		G22_REQUIRED, G22_TIME_AFTER_REGISTRATION_DATE,
		G23_REQUIRED, G23_LAT_LON_ZERO,
		G24_REQUIRED,
		G25_REQUIRED,
		G24_EMPTY, G24_TYPE_OIL_EMPTY,
		G25_EMPTY,
		H26_REQUIRED, H26_1_PLACEOFBUNKERING_EMPTY, H26_1_26_4_BND_EMPTY, H26_1_26_2_26_3_OR_26_4_ALL_OR_NOTHING, H26_2_EMPTY, H26_2_START_AFTER_END, H26_2_TIME_AFTER_REGISTRATION_DATE,
		H26_3_MISSING_QUANTITY_OR_TYPE, H26_3_MISSING_TANK_DATA, H26_3_TRANSFERRED_QUANTITIES_NOT_EQUAL, H26_3_TANK_ALREADY_IN_LIST,
		H26_4_MISSING_QUANTITY_OR_TYPE, H26_4_MISSING_TANK_DATA, H26_4_TRANSFERRED_QUANTITIES_NOT_EQUAL, H26_4_TANK_ALREADY_IN_LIST,
		I3_TANK_ALREADY_IN_LIST,IO0_EMPTY, IO1_EMPTY,IO2_EMPTY, IO4_EMPTY, IO5_TANK_EMPTY, IO2_START_AFTER_END, IO2_TIME_AFTER_REGISTRATION_DATE, IO2_EMPTY_FUEL_TYPE, I_1_I_4_SAME_TANK, I_1_SUM_DIFFERENT_I_4_SUM_WARNING
	}

	public void setOldCodLetterBackListNode(ObservableList<Node> nodeList, String codeLetter) {
		if (codeLetter.equals("A")) {
			oilRecordBookPart1CodeLetterAController.codeLetterContentAnchorPane.getChildren().addAll(nodeList);
		} else if (codeLetter.equals("B")) {
			oilRecordBookPart1CodeLetterBController.codeLetterContentAnchorPane.getChildren().addAll(nodeList);
		} else if (codeLetter.equals("C")) {
			oilRecordBookPart1CodeLetterCController.codeLetterContentAnchorPane.getChildren().addAll(nodeList);
		} else if (codeLetter.equals("D")) {
			oilRecordBookPart1CodeLetterDController.codeLetterContentAnchorPane.getChildren().addAll(nodeList);
		} else if (codeLetter.equals("E")) {
			oilRecordBookPart1CodeLetterEController.codeLetterContentAnchorPane.getChildren().addAll(nodeList);
		} else if (codeLetter.equals("F")) {
			oilRecordBookPart1CodeLetterFController.codeLetterContentAnchorPane.getChildren().addAll(nodeList);
		} else if (codeLetter.equals("G")) {
			oilRecordBookPart1CodeLetterGController.codeLetterContentAnchorPane.getChildren().addAll(nodeList);
		} else if (codeLetter.equals("H")) {
			oilRecordBookPart1CodeLetterHController.codeLetterContentAnchorPane.getChildren().addAll(nodeList);
		} 
	}
	
	public void setAllCodeLetterAtI(String codeLetter) {
		if (codeLetter.equals("A")) {
			oilRecordBookPart1CodeLetterIController.missedCodeLetterCodeLetterContentAnchorPane.getChildren().addAll(oilRecordBookPart1CodeLetterAController.codeLetterContentAnchorPane.getChildren());
		} else if (codeLetter.equals("B")) {
			oilRecordBookPart1CodeLetterIController.missedCodeLetterCodeLetterContentAnchorPane.getChildren().addAll(oilRecordBookPart1CodeLetterBController.codeLetterContentAnchorPane.getChildren());

		} else if (codeLetter.equals("C")) {
			oilRecordBookPart1CodeLetterIController.missedCodeLetterCodeLetterContentAnchorPane.getChildren().addAll(oilRecordBookPart1CodeLetterCController.codeLetterContentAnchorPane.getChildren());

		} else if (codeLetter.equals("D")) {
			oilRecordBookPart1CodeLetterIController.missedCodeLetterCodeLetterContentAnchorPane.getChildren().addAll(oilRecordBookPart1CodeLetterDController.codeLetterContentAnchorPane.getChildren());

		} else if (codeLetter.equals("E")) {
			oilRecordBookPart1CodeLetterIController.missedCodeLetterCodeLetterContentAnchorPane.getChildren().addAll(oilRecordBookPart1CodeLetterEController.codeLetterContentAnchorPane.getChildren());

		} else if (codeLetter.equals("F")) {
			oilRecordBookPart1CodeLetterIController.missedCodeLetterCodeLetterContentAnchorPane.getChildren().addAll(oilRecordBookPart1CodeLetterFController.codeLetterContentAnchorPane.getChildren());

		} else if (codeLetter.equals("G")) {
			oilRecordBookPart1CodeLetterIController.missedCodeLetterCodeLetterContentAnchorPane.getChildren().addAll(oilRecordBookPart1CodeLetterGController.codeLetterContentAnchorPane.getChildren());

		} else if (codeLetter.equals("H")) {
			oilRecordBookPart1CodeLetterIController.missedCodeLetterCodeLetterContentAnchorPane.getChildren().addAll(oilRecordBookPart1CodeLetterHController.codeLetterContentAnchorPane.getChildren());

		} 
	}
	
	public MarpolBean getMarpolBean() {
		return marpolBean;
	}

	public void setMarpolBean(MarpolBean marpolBean) {
		this.marpolBean = marpolBean;
	}

	public String getGroupUUID() {
		return groupUUID;
	}

	public void setGroupUUID(String groupUUID) {
		this.groupUUID = groupUUID;
	}

	public OilRecordBookPart1CodeLetterAController getOilRecordBookPart1CodeLetterAController() {
		return oilRecordBookPart1CodeLetterAController;
	}

	public OilRecordBookPart1CodeLetterBController getOilRecordBookPart1CodeLetterBController() {
		return oilRecordBookPart1CodeLetterBController;
	}

	public OilRecordBookPart1CodeLetterCController getOilRecordBookPart1CodeLetterCController() {
		return oilRecordBookPart1CodeLetterCController;
	}

	public OilRecordBookPart1CodeLetterDController getOilRecordBookPart1CodeLetterDController() {
		return oilRecordBookPart1CodeLetterDController;
	}

	public OilRecordBookPart1CodeLetterEController getOilRecordBookPart1CodeLetterEController() {
		return oilRecordBookPart1CodeLetterEController;
	}

	public OilRecordBookPart1CodeLetterFController getOilRecordBookPart1CodeLetterFController() {
		return oilRecordBookPart1CodeLetterFController;
	}

	public OilRecordBookPart1CodeLetterGController getOilRecordBookPart1CodeLetterGController() {
		return oilRecordBookPart1CodeLetterGController;
	}

	public OilRecordBookPart1CodeLetterHController getOilRecordBookPart1CodeLetterHController() {
		return oilRecordBookPart1CodeLetterHController;
	}

	public OilRecordBookPart1CodeLetterIController getOilRecordBookPart1CodeLetterIController() {
		return oilRecordBookPart1CodeLetterIController;
	}


}
