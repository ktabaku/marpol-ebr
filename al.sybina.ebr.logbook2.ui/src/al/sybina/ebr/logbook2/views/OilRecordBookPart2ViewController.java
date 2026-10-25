package al.sybina.ebr.logbook2.views;

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
import java.time.format.DateTimeFormatter;
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
import com.oceanpro.core.services.OperationLogReladomoService;
import com.oceanpro.core.services.OperationLogService;
import com.oceanpro.utils.EasyDateUtils;

import al.sybina.ebr.commons.utils.JavaFXUtils;
import al.sybina.ebr.commons.utils.ViewPerspectiveHelper;
import al.sybina.ebr.jasperreports.JasperReportDetails;
import al.sybina.ebr.jasperreports.JasperReportsService;
import al.sybina.ebr.logbook.commons.Activator;
import al.sybina.ebr.logbook.commons.ReportDataSourceBean;
import al.sybina.ebr.logbook.domain.reladomo.OilLog;
import al.sybina.ebr.logbook.domain.reladomo.TimestampProvider;
import al.sybina.ebr.logbook.signature.Signature;
import al.sybina.ebr.logbook.signature.SignatureLetterMissedOperation;
import al.sybina.ebr.logbook.views.marpoldetail.MarpolBean;
import al.sybina.ebr.login.preferences.LoginPreferencePage;
import al.sybina.ebr.login.services.RemoteEventAdminService;
import al.sybina.ebr.core.tools.PluginTools;
import al.sybina.ebr.rcp.BaseViewerFX;
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
import javafx.util.StringConverter;
import jfxtras.scene.control.ImageViewButton;

public class OilRecordBookPart2ViewController implements Initializable {

	private static final Logger logger = LoggerCreator.createLogger(OilRecordBookPart2ViewController.class);

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
	private SignatureLetterMissedOperation signatureLetterMissedOperation;
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
	public TabPane mainTabPane;
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
	private Button saveLogsButton;
	@FXML
	private ImageViewButton logbook2Help2ImageViewButton;

	public static Alert alert;
	private static JFXButton closeButton;

	private OilRecordBookPart2CodeLetterAController oilRecordBookPart2CodeLetterAController;
	private OilRecordBookPart2CodeLetterBController oilRecordBookPart2CodeLetterBController;
	private OilRecordBookPart2CodeLetterCController oilRecordBookPart2CodeLetterCController;
	private OilRecordBookPart2CodeLetterDController oilRecordBookPart2CodeLetterDController;
	private OilRecordBookPart2CodeLetterEController oilRecordBookPart2CodeLetterEController;
	private OilRecordBookPart2CodeLetterFController oilRecordBookPart2CodeLetterFController;
	private OilRecordBookPart2CodeLetterGController oilRecordBookPart2CodeLetterGController;
	private OilRecordBookPart2CodeLetterHController oilRecordBookPart2CodeLetterHController;
	private OilRecordBookPart2CodeLetterIController oilRecordBookPart2CodeLetterIController;
	private OilRecordBookPart2CodeLetterJController oilRecordBookPart2CodeLetterJController;
	private OilRecordBookPart2CodeLetterKController oilRecordBookPart2CodeLetterKController;
	private OilRecordBookPart2CodeLetterLController oilRecordBookPart2CodeLetterLController;
	private OilRecordBookPart2CodeLetterMController oilRecordBookPart2CodeLetterMController;
	private OilRecordBookPart2CodeLetterNController oilRecordBookPart2CodeLetterNController;
	private OilRecordBookPart2CodeLetterOController oilRecordBookPart2CodeLetterOController;
	private OilRecordBookPart2CodeLetterPController oilRecordBookPart2CodeLetterPController;
	private OilRecordBookPart2CodeLetterQController oilRecordBookPart2CodeLetterQController;
	private OilRecordBookPart2CodeLetterRController oilRecordBookPart2CodeLetterRController;

	private String groupUUID;
	private boolean restored;
	private ObservableList<Node> addNewChildA;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		JavaFXUtils.addFilledPseudoClass(lastEntryTextField);
		JavaFXUtils.addFilledPseudoClass(compileDate);
		try {
			SVGGlyph timesGlyph = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg.times-circle");
			closeButton = new JFXButton();
			closeButton.getStyleClass().add("closeDialogButton");
			closeButton.setMinWidth(40);
			closeButton.setPrefWidth(40);
			closeButton.setMaxWidth(40);
			closeButton.setMinHeight(32);
			closeButton.setPrefHeight(32);
			closeButton.setMaxHeight(32);
			closeButton.setGraphic(timesGlyph);
			closeButton.ripplerFillProperty().setValue(Color.LIGHTSALMON);
			closeButton.setOnAction(event -> {
				((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
			});
			Tooltip.install(closeButton, new Tooltip("Exit"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Image infoIconImage = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
			logbook2Help2ImageViewButton.setImage(infoIconImage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		compileDate.setConverter(new StringConverter<LocalDate>() {
			private DateTimeFormatter dateTimeFormatter = EasyDateUtils.logbookSimpleDateTimeFormatter;

			@Override
			public String toString(LocalDate localDate) {
				if (localDate == null)
					return "";
				return dateTimeFormatter.format(localDate);
			}

			@Override
			public LocalDate fromString(String dateString) {
				if (dateString == null || dateString.trim().isEmpty()) {
					return null;
				}
				return LocalDate.parse(dateString, dateTimeFormatter);
			}
		});
		compileDate.valueProperty().addListener((observable, oldDate, newDate) -> {
			hBoxInsertDate.setVisible(false);
		});

		eventBroker.subscribe(al.sybina.ebr.login.Activator.LOGGED_IN_EVENT_TOPIC, new org.osgi.service.event.EventHandler() {
			@Override
			public void handleEvent(Event event) {
				OilLog lastOilLog = operationLogReladomoService.getLastIncludingCodeIMissedOilLog(Activator.OIL_LOG_TYPE_2);
				if (lastOilLog != null) {
					lastEntryTextField.setText(lastOilLog.getCodeLetter() + " " + lastOilLog.getCodeNumber() + "  Date " + new SimpleDateFormat("dd-MM-yyyy").format(lastOilLog.getBusinessDateFrom()));
				}
			}
		});

		eventBroker.subscribe(Activator.LOGS_CHANGED_EVENT_TOPIC, new org.osgi.service.event.EventHandler() {
			@Override
			public void handleEvent(Event event) {
				OilLog lastOilLog = operationLogReladomoService.getLastIncludingCodeIMissedOilLog(Activator.OIL_LOG_TYPE_2);
				if (lastOilLog != null) {
					lastEntryTextField.setText(lastOilLog.getCodeLetter() + " " + lastOilLog.getCodeNumber() + "  Date " + new SimpleDateFormat("dd-MM-yyyy").format(lastOilLog.getBusinessDateFrom()));
				}
			}
		});

		eventBroker.subscribe(al.sybina.ebr.login.Activator.LOGGED_IN_EVENT_TOPIC, new org.osgi.service.event.EventHandler() {
			@Override
			public void handleEvent(Event event) {
				if (SecurityUtils.getSubject().isPermitted(al.sybina.ebr.login.Activator.PERMISSION_INSERT_LOGS)) {
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
			if (oilRecordBookPart2CodeLetterOController != null && !oilRecordBookPart2CodeLetterOController.logbook2LetterOEntryPertainingToAnEarlierMissedOperationalComboBox.getValue().isEmpty()) {
				// if a code letter from A-R is open in code letter O turn it back to same code
				// letter all "anchorpane childs"
				// please also refer to setOilRecordBookPart2ViewControllerToCodeLetterO
				// setOldCodLetterBackListNode and setAllCodeLetterAtO
				oilRecordBookPart2CodeLetterOController.logbook2LetterOEntryPertainingToAnEarlierMissedOperationalComboBox.setValue("");
				clearFilledInfo();
			}
		});
	}

	public void marpolDetailHandler() {
		viewPerspectiveHelper.showView("al.sybina.ebr.logbook.ui.part.marpolDetail");
	}

	public void openLogBookInfoPdf(MouseEvent event) {
		try {
			BaseViewerFX baseViewerFX = new BaseViewerFX();
			Stage stage = new Stage();
			stage.initOwner(mainTabPane.getScene().getWindow());
			stage.setTitle("LogBook Info");
			byte[] bytes = IOUtils.toByteArray(getClass().getResourceAsStream("/images/INTRODUCTION-ORB-PART-II.pdf"));
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

	public void showMapHandler() {
		viewPerspectiveHelper.goToPerspective("al.sybina.ebr.marpol.ui.perspective.Marpol");
	}

	public void saveLog() {
		Entry<Map<Validation, Object>, List<OilLog>> result = prepareLog();
		if (!checkShip(result)) return;
		if (!checkValidation(result))
			return;
		List<OilLog> insertedLogList = null;
		if (restored) {
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
				log.setType("OIL2");

			}

			insertedLogList = operationLogReladomoService.addOil1Logs(result.getValue());
			List<Long> updatedTankIds = new ArrayList<Long>();
			for (OilLog log : result.getValue()) {
				if (log.getCodeLetter().equals("A") && log.getCodeNumber().equals("2")) {
					for (Entry<Tank, Double> entry : log.getUserTankActualLevels().entrySet()) {
						entry.getKey().setActualLevel(entry.getValue());
						operationLogService.updateTank(entry.getKey());
						updatedTankIds.add(entry.getKey().getId());
					}
				}
				if (log.getCodeLetter().equals("B") && (log.getCodeNumber().equals("4.1") || log.getCodeNumber().equals("4.2"))) {
					for (Entry<Tank, Double> entry : log.getUserTankActualLevels().entrySet()) {
						entry.getKey().setActualLevel(entry.getValue());
						operationLogService.updateTank(entry.getKey());
						updatedTankIds.add(entry.getKey().getId());
					}
				}
				if (log.getCodeLetter().equals("C") && log.getCodeNumber().equals("7")) {
					for (Entry<Tank, Double> entry : log.getUserTankActualLevels().entrySet()) {
						entry.getKey().setActualLevel(entry.getValue());
						operationLogService.updateTank(entry.getKey());
						updatedTankIds.add(entry.getKey().getId());
					}
				}
				if (log.getCodeLetter().equals("E") && log.getCodeNumber().equals("19.1")) {
					for (Entry<Tank, Double> entry : log.getUserTankActualLevels().entrySet()) {
						entry.getKey().setActualLevel(entry.getValue());
						operationLogService.updateTank(entry.getKey());
						updatedTankIds.add(entry.getKey().getId());
					}
				}
				if (log.getCodeLetter().equals("F") && log.getCodeNumber().equals("20")) {
					for (Entry<Tank, Double> entry : log.getUserTankActualLevels().entrySet()) {
						entry.getKey().setActualLevel(entry.getValue());
						operationLogService.updateTank(entry.getKey());
						updatedTankIds.add(entry.getKey().getId());
					}
				}
				if (log.getCodeLetter().equals("F") && log.getCodeNumber().equals("23")) {
					for (Entry<Tank, Double> entry : log.getUserTankActualLevels().entrySet()) {
						entry.getKey().setActualLevel(entry.getValue());
						operationLogService.updateTank(entry.getKey());
						updatedTankIds.add(entry.getKey().getId());
					}
				}
				if (log.getCodeLetter().equals("G") && log.getCodeNumber().equals("27")) {
					for (Entry<Tank, Double> entry : log.getUserTankActualLevels().entrySet()) {
						entry.getKey().setActualLevel(entry.getValue());
						operationLogService.updateTank(entry.getKey());
						updatedTankIds.add(entry.getKey().getId());
					}
				}
				if (log.getCodeLetter().equals("G") && log.getCodeNumber().equals("31.2")) {
					for (Entry<Tank, Double> entry : log.getUserTankActualLevels().entrySet()) {
						entry.getKey().setActualLevel(entry.getValue());
						operationLogService.updateTank(entry.getKey());
						updatedTankIds.add(entry.getKey().getId());
					}
				}
				if (log.getCodeLetter().equals("H") && log.getCodeNumber().equals("32")) {
					for (Entry<Tank, Double> entry : log.getUserTankActualLevels().entrySet()) {
						entry.getKey().setActualLevel(entry.getValue());
						operationLogService.updateTank(entry.getKey());
						updatedTankIds.add(entry.getKey().getId());
					}
				}
				if (log.getCodeLetter().equals("H") && log.getCodeNumber().equals("39")) {
					for (Entry<Tank, Double> entry : log.getUserTankActualLevels().entrySet()) {
						entry.getKey().setActualLevel(entry.getValue());
						operationLogService.updateTank(entry.getKey());
						updatedTankIds.add(entry.getKey().getId());
					}
				}
				if (log.getCodeLetter().equals("I") && log.getCodeNumber().equals("41")) {
					for (Entry<Tank, Double> entry : log.getUserTankActualLevels().entrySet()) {
						entry.getKey().setActualLevel(entry.getValue());
						operationLogService.updateTank(entry.getKey());
						updatedTankIds.add(entry.getKey().getId());
					}
				}
				if (log.getCodeLetter().equals("J") && log.getCodeNumber().equals("55")) {
					for (Entry<Tank, Double> entry : log.getUserTankActualLevels().entrySet()) {
						entry.getKey().setActualLevel(entry.getValue());
						operationLogService.updateTank(entry.getKey());
						updatedTankIds.add(entry.getKey().getId());
					}
				}
				if (log.getCodeLetter().equals("J") && log.getCodeNumber().equals("57.3")) {
					for (Entry<Tank, Double> entry : log.getUserTankActualLevels().entrySet()) {
						entry.getKey().setActualLevel(entry.getValue());
						operationLogService.updateTank(entry.getKey());
						updatedTankIds.add(entry.getKey().getId());
					}
				}
				if (log.getCodeLetter().equals("K") && log.getCodeNumber().equals("59")) {
					for (Entry<Tank, Double> entry : log.getUserTankActualLevels().entrySet()) {
						entry.getKey().setActualLevel(entry.getValue());
						operationLogService.updateTank(entry.getKey());
						updatedTankIds.add(entry.getKey().getId());
					}
				}
				if (log.getCodeLetter().equals("L") && log.getCodeNumber().equals("63")) {
					for (Entry<Tank, Double> entry : log.getUserTankActualLevels().entrySet()) {
						entry.getKey().setActualLevel(entry.getValue());
						operationLogService.updateTank(entry.getKey());
						updatedTankIds.add(entry.getKey().getId());
					}
				}
				if (log.getCodeLetter().equals("P") && log.getCodeNumber().equals("77")) {
					for (Entry<Tank, Double> entry : log.getUserTankActualLevels().entrySet()) {
						entry.getKey().setActualLevel(entry.getValue());
						operationLogService.updateTank(entry.getKey());
						updatedTankIds.add(entry.getKey().getId());
					}
				}

//				if(log.getCodeLetter().equals("C") && log.getCodeNumber().equals("11.4")) {
//					for(Entry<Tank, Double> entry : log.getUserTankActualLevels().entrySet()) {
//						entry.getKey().setActualLevel(entry.getValue());
//						operationLogService.updateTank(entry.getKey());
//					}
//				}
			}
			Map<String, List<Long>> idsParameter = new HashMap<String, List<Long>>();
			idsParameter.put(Activator.TANK_IDS, updatedTankIds);
			remoteEventAdminService.getDistributedEventAdmin(loginPreferencePage.getMainInstallationIp().getValue()).postEvent(new Event(al.sybina.ebr.logbook.commons.Activator.MARPOL_DETAILS_CHANGED, idsParameter));
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
			if (utcZoned.isAfter(nowUTC)) {
				validations.put(Validation.DATE_AFTER_NOW, null);
			} else {
				OilLog lastIncludingCodeIMissedOilLog = operationLogReladomoService.getLastIncludingCodeIMissedOilLog(Activator.OIL_LOG_TYPE_2);
				if (lastIncludingCodeIMissedOilLog != null && compileTimestamp.before(lastIncludingCodeIMissedOilLog.getBusinessDateFrom())) {
					dateError = true;
					validations.put(Validation.DATE_BEFORE_LAST_LOG, null);
				}
			}
		}

		String encodedSignatureString = null;
		WritableImage signatureImage = signature.getSignatureController().getSignatureImage();
		if (signatureImage == null) {
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
		if (rank == null) {
			validations.put(Validation.SIGNER_RANK_MISSING, null);
		}
		String name = signature.getSignatureController().getName();
		if (name == null) {
			validations.put(Validation.SIGNER_NAME_MISSING, null);
		}
		String surname = signature.getSignatureController().getSurname();
		if (surname == null) {
			validations.put(Validation.SIGNER_SURNAME_MISSING, null);
		}
		if (groupUUID == null)
			groupUUID = UUID.randomUUID().toString();

		List<OilLog> allTabsLogToInsertList = new ArrayList<OilLog>();
		Tab tab = mainTabPane.getSelectionModel().getSelectedItem();
		String codeLetter = tab.getText();// mainTabPane.getSelectionModel().getSelectedItem().getText();
		OilLog logToInsert = new OilLog(compileTimestamp);
		logToInsert.setType("OIL2");
		Long shipId = getShipId();
		if (shipId != null) {
			logToInsert.setShip(shipId);
		} else {
			validations.put(Validation.SHIP_MISSING, null);
		}
		logToInsert.setGroupUuid(groupUUID);
		logToInsert.setCodeLetter(codeLetter);
//		logToInsert.setDate(compileDate);
		if (restored) {
			logToInsert.setNote(signature.getSignatureController().getNote());
		} else {
			if (dateError)
				logToInsert.setNote("Date error!");
		}
		logToInsert.setSignature(encodedSignatureString);
		logToInsert.setOrderInGroup("");
		logToInsert.setSignerRank(rank);
		logToInsert.setSignerName(name);
		logToInsert.setSignerSurname(surname);

		List<OilLog> logToInsertList = new ArrayList<OilLog>();

		if (codeLetter.equals("A")) {
			oilRecordBookPart2CodeLetterAController.processA(logToInsert, logToInsertList, validations);
		} else if (codeLetter.equals("B")) {
			oilRecordBookPart2CodeLetterBController.processB(logToInsert, logToInsertList, validations);
		} else if (codeLetter.equals("C")) {
			oilRecordBookPart2CodeLetterCController.processC(logToInsert, logToInsertList, validations);
		} else if (codeLetter.equals("D")) {
			oilRecordBookPart2CodeLetterDController.processD(logToInsert, logToInsertList, validations);
		} else if (codeLetter.equals("E")) {
			oilRecordBookPart2CodeLetterEController.processE(logToInsert, logToInsertList, validations);
		} else if (codeLetter.equals("F")) {
			oilRecordBookPart2CodeLetterFController.processF(logToInsert, logToInsertList, validations);
		} else if (codeLetter.equals("G")) {
			oilRecordBookPart2CodeLetterGController.processG(logToInsert, logToInsertList, validations);
		} else if (codeLetter.equals("H")) {
			oilRecordBookPart2CodeLetterHController.processH(logToInsert, logToInsertList, validations);
		} else if (codeLetter.equals("I")) {
			oilRecordBookPart2CodeLetterIController.processI(logToInsert, logToInsertList, validations);
		} else if (codeLetter.equals("J")) {
			oilRecordBookPart2CodeLetterJController.processJ(logToInsert, logToInsertList, validations);
		} else if (codeLetter.equals("K")) {
			oilRecordBookPart2CodeLetterKController.processK(logToInsert, logToInsertList, validations);
		} else if (codeLetter.equals("L")) {
			oilRecordBookPart2CodeLetterLController.processL(logToInsert, logToInsertList, validations);
		} else if (codeLetter.equals("M")) {
			oilRecordBookPart2CodeLetterMController.processM(logToInsert, logToInsertList, validations);
		} else if (codeLetter.equals("N")) {
			oilRecordBookPart2CodeLetterNController.processN(logToInsert, logToInsertList, validations);
		} else if (codeLetter.equals("O")) {
			oilRecordBookPart2CodeLetterOController.processO(logToInsert, logToInsertList, validations);
		} else if (codeLetter.equals("P")) {
			oilRecordBookPart2CodeLetterPController.processP(logToInsert, logToInsertList, validations);
		} else if (codeLetter.equals("Q")) {
			oilRecordBookPart2CodeLetterQController.processQ(logToInsert, logToInsertList, validations);
		} else if (codeLetter.equals("R")) {
			oilRecordBookPart2CodeLetterRController.processR(logToInsert, logToInsertList, validations);
		}
		allTabsLogToInsertList.addAll(logToInsertList);

		if (signature.getSignatureController().checkChanged(allTabsLogToInsertList)) {
			validations.put(Validation.SIGNATURE_INVALID, null);
		}
		return new SimpleEntry<Map<Validation, Object>, List<OilLog>>(validations, allTabsLogToInsertList);
	}

	public void clearFilledInfo() {
		restored = false;
		restoredLabel.setText("");
		compileDate.setValue(null);
		for (Tab tab : mainTabPane.getTabs()) {
			String codeLetter = tab.getText();
			if (codeLetter.equals("A")) {
				oilRecordBookPart2CodeLetterAController.clearFilledInfo();
			} else if (codeLetter.equals("B")) {
				oilRecordBookPart2CodeLetterBController.clearFilledInfo();
			} else if (codeLetter.equals("C")) {
				oilRecordBookPart2CodeLetterCController.clearFilledInfo();
			} else if (codeLetter.equals("D")) {
				oilRecordBookPart2CodeLetterDController.clearFilledInfo();
			} else if (codeLetter.equals("E")) {
				oilRecordBookPart2CodeLetterEController.clearFilledInfo();
			} else if (codeLetter.equals("F")) {
				oilRecordBookPart2CodeLetterFController.clearFilledInfo();
			} else if (codeLetter.equals("G")) {
				oilRecordBookPart2CodeLetterGController.clearFilledInfo();
			} else if (codeLetter.equals("H")) {
				oilRecordBookPart2CodeLetterHController.clearFilledInfo();
			} else if (codeLetter.equals("I")) {
				oilRecordBookPart2CodeLetterIController.clearFilledInfo();
			} else if (codeLetter.equals("J")) {
				oilRecordBookPart2CodeLetterJController.clearFilledInfo();
			} else if (codeLetter.equals("K")) {
				oilRecordBookPart2CodeLetterKController.clearFilledInfo();
			} else if (codeLetter.equals("L")) {
				oilRecordBookPart2CodeLetterLController.clearFilledInfo();
			} else if (codeLetter.equals("M")) {
				oilRecordBookPart2CodeLetterMController.clearFilledInfo();
			} else if (codeLetter.equals("N")) {
				oilRecordBookPart2CodeLetterNController.clearFilledInfo();
			} else if (codeLetter.equals("O")) {
				oilRecordBookPart2CodeLetterOController.clearFilledInfo();
			} else if (codeLetter.equals("P")) {
				oilRecordBookPart2CodeLetterPController.clearFilledInfo();
			} else if (codeLetter.equals("Q")) {
				oilRecordBookPart2CodeLetterQController.clearFilledInfo();
			} else if (codeLetter.equals("R")) {
				oilRecordBookPart2CodeLetterRController.clearFilledInfo();
			}
		}
	}

	public void openSignature(ActionEvent e) {
		Entry<Map<Validation, Object>, List<OilLog>> result = prepareLog();
		if (!checkShip(result)) return;
		if (result.getKey().keySet().contains(Validation.DATE_MISSING)) {
			showWarning("Date error", "Please specify the compilation date.");
			return;
		}
		signature.showSignature((Stage) ((Button) e.getSource()).getScene().getWindow(), result.getValue());
	}

	public void openInfoMessage(MouseEvent event) {
		anchorPaneInfoMessage.setVisible(true);
	}

	public void closeInfoMessage(ActionEvent e) {
		anchorPaneInfoMessage.setVisible(false);
	}

	public void clearAllOperation(ActionEvent e) {
		if (showClearAllConfirmation("Clear All", "Are you sure you want to clear all inserted data?")) {
			clearFilledInfo();
		}
	}

	public void restoreLogGroup(List<OilLog> logGroup) {
		if (logGroup == null || logGroup.isEmpty())
			return;
		OilLog oilLog0 = logGroup.get(0);
		LocalDateTime date = new java.sql.Timestamp(oilLog0.getBusinessDate().getTime()).toLocalDateTime();
		compileDate.setValue(date.toLocalDate());

		try {
			byte[] signatureBytes = Base64.getDecoder().decode(oilLog0.getSignature());
			BufferedImage bImage = ImageIO.read(new ByteArrayInputStream(signatureBytes));
			signature.getSignatureController().setSignatureImage(SwingFXUtils.toFXImage(bImage, null));
		} catch (Exception e) {
			e.printStackTrace();
		}
		groupUUID = oilLog0.getGroupUuid();

		signature.getSignatureController().setOilLogList(logGroup);
		signature.getSignatureController().setRank(oilLog0.getSignerRank());
		signature.getSignatureController().setName(oilLog0.getSignerName());
		signature.getSignatureController().setSurname(oilLog0.getSignerSurname());
		signature.getSignatureController().setNote(oilLog0.getNote());
		signature.getSignatureController().restoreSignature();

//    	oilRecordBookPart2CodeLetterAController.restoreLogGroup(logGroup);
//    	oilRecordBookPart2CodeLetterBController.restoreLogGroup(logGroup);
//    	oilRecordBookPart2CodeLetterCController.restoreLogGroup(logGroup);
//    	oilRecordBookPart2CodeLetterDController.restoreLogGroup(logGroup);
//    	oilRecordBookPart2CodeLetterEController.restoreLogGroup(logGroup);
//    	oilRecordBookPart2CodeLetterFController.restoreLogGroup(logGroup);
//    	oilRecordBookPart2CodeLetterGController.restoreLogGroup(logGroup);
//    	oilRecordBookPart2CodeLetterHController.restoreLogGroup(logGroup);
//    	oilRecordBookPart2CodeLetterIController.restoreLogGroup(logGroup);

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
		if (optional.isPresent() && optional.get() == ButtonType.OK)
			return true;
		else
			return false;
	}

	public static boolean showClearAllConfirmation(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.initStyle(StageStyle.UTILITY);
		alert.setTitle("Confirmation");
		alert.setHeaderText(title);
		alert.setContentText(message);
		JavaFXUtils.undecorate(alert, closeButton, themeManager);
		Optional<ButtonType> optional = alert.showAndWait();
		if (optional.isPresent() && optional.get() == ButtonType.OK)
			return true;
		else
			return false;
	}

	@FXML
	private void showPreview(ActionEvent event) {
		logger.info("showPreview");
		Entry<Map<Validation, Object>, List<OilLog>> result = prepareLog();
		if (!checkValidation(result))
			return;

//		List<String> groupUIIDList = new ArrayList<String>();
//		for(OilLog oilLog : result.getValue()) {
//			groupUIIDList.add(oilLog.getUuid());
//		}
//		Map<String, List<OilLog>> logGroupsMap = operationLogService.getGroupsOil1LogList(groupUIIDList);
		Ship ship = marpolBean.getSettings().getShip();
		String shipName = "";
		String shipCode = "";
		if (ship != null) {
			shipName = ship.getName();
			shipCode = ship.getShipDistinctiveNumber();
		}

		List<ReportDataSourceBean> reportDataSourceBeanList = new ArrayList<ReportDataSourceBean>();
//		for(List<OilLog> oilLogList : logGroupsMap.values()) {
		if (result.getValue() != null && !result.getValue().isEmpty()) {
			    List<OilLog> oilLogList = result.getValue();
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
		det.setDescription("Oil Record Book Log Part @");

		String filePath = PluginTools.getFileLocalToRuntime(Activator.RUNTIME_LOCATION_FOLDER_NAME + "/reports/LogbookPart2.jasper");
		File file = new File(filePath);
		det.setMasterReportUrl(file.getAbsolutePath());

		Map<String, Object> reportParams = new HashMap<String, Object>();
		reportParams.put("shipName", shipName);
		reportParams.put("shipCode", shipCode);
		reportParams.put("partAndVersion", "II  V 1.0.0");
		det.setReportParameters(reportParams);
		try {
			File pdffile = jasperReportsService.getJasperReportsFactory().complieReportAsPdfFile(new File(PluginTools.getFileLocalToRuntime("reports") + "/Logbook.pdf"), det, reportParams, reportDataSourceBeanList);
			BaseViewerFX baseViewerFX = new BaseViewerFX();
			Stage reportStage = new Stage();
			reportStage.initOwner(((Node) event.getSource()).getScene().getWindow());
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
		} catch (Exception e) {
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
		if (result.getKey().keySet().contains(Validation.DATE_MISSING)) {
			showWarning("Date error", "Please specify the compilation date.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.DATE_MISSING_CODE_LETTERO)) {
			showWarning("Date error", "Please specify the compilation date for missed operation code letter.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.DATE_AFTER_NOW)) {
			showWarning("Date error", "The compilation date cannot be after the system date.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.MISSED_DATE_AFTER_NOW)) {
			showWarning("Date error", "The compilation missed operation date cannot be after the compilation date of code letter O.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.A1_EMPTY)) {
			showWarning("Alert code Nr 1", "Please complete place of loading.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.A2_TYPE_OF_OIL_EMPTY)) {
			showWarning("Alert code Nr 2", "Please insert type of oil loaded.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.A2_TANKS_EMPTY)) {
			showWarning("Alert code Nr 2", "Please insert identity of tanks.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.A3_TOTAL_QUANTITY_EMPTY)) {
			showWarning("Alert code Nr 3", "Please insert total quantity of oil loaded.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.A3_TOTAL_CONTENT_EMPTY)) {
			showWarning("Alert code Nr 3", "Please insert total content of tank(s), in cubic metres.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.B4_EMPTY)) {
			showWarning("Alert code Nr 4", "Please compile code letter 4, identity of tanks.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.B4_1_EMPTY)) {
			showWarning("Alert code Nr 4.1", "From tank(s) is empty.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.B4_2_EMPTY)) {
			showWarning("Alert code Nr 4.2", "To tank(s) is empty.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.B4_2_QUANTITY_TRANSFERED_EMPTY)) {
			showWarning("Alert code Nr 4.2", "Quantity transfered is empty.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.B4_2_TOTAL_QUANTITY_EMPTY)) {
			showWarning("Alert code Nr 4.2", "Total quantity Of tank(s) is empty.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.B5_EMPTY)) {
			showWarning("Alert code Nr 5", "Please compile code letter 5, tanks emptied.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.B5_QUANTITY_RETAINED_REQUIRED)) {
			showWarning("Alert code Nr 5", "Please state the quantity retained on board.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.C6_EMPTY)) {
			showWarning("Alert code Nr 6", "Place of unloading is empty.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.C7_EMPTY)) {
			showWarning("Alert code Nr 7", "Identity of tank(s) unloaded is empty.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.C8_EMPTY)) {
			showWarning("Alert code Nr 8", "Please compile code letter 8, tanks emptied.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.C8_QUANTITY_RETAINED_REQUIRED)) {
			showWarning("Alert code Nr 8", "Please state the quantity retained on board.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.D9_EMPTY)) {
			showWarning("Alert code Nr 9", "Port where crude oil washing was carried or ship’s position if carried out between two discharge ports is required.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.D9_PORT_EMPTY)) {
			showWarning("Alert code Nr 9", "Port where crude oil washing was carried out is empty.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.D9_LATITUDE_OR_LONGITUDE_EMPTY)) {
			showWarning("Alert code Nr 9", "If carried out between two discharges, ship’s position latitude and longitude are required.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.D10_TANKS_EMPTY)) {
			showWarning("Alert code Nr 10", "Please insert identity of tanks.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.D10_TANK_ALREADY_IN_LIST)) {
			showWarning("Alert code Nr 10", "Tank already in list.\nTank name: " + (String)result.getKey().get(Validation.D10_TANK_ALREADY_IN_LIST));
			return false;
		}
		if (result.getKey().keySet().contains(Validation.D10_WASHING_TANK_TYPE_EMPTY)) {
			showWarning("Alert code Nr 10", "Please select one of multi-selection.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.D11_NR_OF_MACHINES_IN_USE_EMPTY)) {
			showWarning("Alert code Nr 11", "Please specify nr of machines in use.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.D12_TIME_OF_START_OF_WASHING_EMPTY)) {
			showWarning("Alert code Nr 12", "Time of start of washing is empty.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.D13_WASHING_PATTERN_TYPE_EMPTY)) {
			showWarning("Alert code Nr 13", "Washing pattern type is empty.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.D13_WASHING_PATTERN_EMPLOYED_EMPTY)) {
			showWarning("Alert code Nr 13", "Washing pattern employed is empty.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.D14_WASHING_LINE_PRESSURE_VALUE_EMPTY)) {
			showWarning("Alert code Nr 14", "Washing line pressure value is empty.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.D14_WASHING_LINE_PRESSURE_TYPE_EMPTY)) {
			showWarning("Alert code Nr 14", "Washing line pressure type is empty.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.D15_TIME_OF_COMPLETION_OR_STOP_OF_WASHING_EMPTY)) {
			showWarning("Alert code Nr 15", "Time of completion or stop of washing is empty.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.D12_TIME_AT_START_OF_WASHING_AFTER_TIME_AT_STOP)) {
			showWarning("Alert code Nr 12", "Time at start of washing is after time at end of washing.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.D16_EMPTY)) {
			showWarning("Alert code Nr 16", "Method of establishing that tank(s) was (where) dry is empty.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.D17_EMPTY)) {
			showWarning("Alert code Nr 17", "Please insert remarks.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.E19_TANKS_EMPTY)) {
			showWarning("Alert code Nr 19.1", "Please insert identity of tanks.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.E19_QUANTITY_EMPTY)) {
			showWarning("Alert code Nr 19.3", "Total quantity reviced is empty.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.E19_TIME_EMPTY)) {
			showWarning("Alert code Nr 19.2", "Time at start or at end of ballast is empty.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.E19_TIME_AT_START_AFTER_TIME_AT_STOP)) {
			showWarning("Alert code Nr 19", "Time at start is after time at end of ballasting.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.E18_EMPTY)) {
			showWarning("Alert code Nr 18", "Please insert position!");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.E19_EMPTY)) {
			showWarning("Alert code Nr 19", "Please select checkbox!");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.F20_EMPTY_TANKS)) {
			showWarning("Alert code Nr 20", "Please insert identity of tanks.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.F21_EMPTY)) {
			showWarning("Alert code Nr 21", "Please insert position.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.F23_EMPTY_TANKS)) {
			showWarning("Alert code Nr 23", "Please insert identity of tanks.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.F25_TIME_EMPTY)) {
			showWarning("Alert code Nr 25", "Please insert Time.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.F26_EMPTY_QUANTITY)) {
			showWarning("Alert code Nr 26", "Please insert total quantity.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.G27_EMPTY_TANKS)) {
			showWarning("Alert code Nr 27", "Please insert identity of tanks.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.G28_EMPTY)) {
			showWarning("Alert code Nr 28", "Please insert position.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.G29_EMPTY)) {
			showWarning("Alert code Nr 29", "Please insert duration of cleaning.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.G30_EMPTY)) {
			showWarning("Alert code Nr 30", "Please insert method of cleaning.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.G30_QUANTITY_EMPTY)) {
			showWarning("Alert code Nr 30", "Please insert quantity.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.G31_EMPTY)) {
			showWarning("Alert code Nr 31", "Please insert code letter 31.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.G31_EMPTY_RECEPTION)) {
			showWarning("Alert code Nr 31.1", "Please complete all fields.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.G31_EMPTY_TANKS)) {
			showWarning("Alert code Nr 31.2", "Please insert identity of tanks.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.H32_EMPTY_TANKS)) {
			showWarning("Alert code Nr 32", "Please insert identity of tanks.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.H33_EMPTY)) {
			showWarning("Alert code Nr 33", "Please insert time and position at start.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.H33_TIME_EMPTY)) {
			showWarning("Alert code Nr 33", "Please select Time.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.H33_TIME_AFTER_REGISTRATION_DATE)) {
			showWarning("Alert code Nr 33", "Start Time is after Registration Time.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.H34_EMPTY)) {
			showWarning("Alert code Nr 34", "Please insert time and position at end.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.H34_TIME_EMPTY)) {
			showWarning("Alert code Nr 34", "Please insert Time.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.H33_TIME_AT_START_AFTER_TIME_AT_STOP)) {
			showWarning("Alert code Nr 33", "Time at the start of discharge after time at the end of the discharge.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.H34_TIME_AFTER_REGISTRATION_DATE)) {
			showWarning("Alert code Nr 34", "End Time is after Registration Time.");
			return false;
		}

		if (result.getKey().keySet().contains(Validation.H35_EMPTY)) {
			showWarning("Alert code Nr 35", "Please insert quantity discharged.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.H36_EMPTY)) {
			showWarning("Alert code Nr 36", "Please insert ship speed.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.H37_EMPTY)) {
			showWarning("Alert code Nr 37", "Please select YES or No and insert remarks.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.H38_EMPTY)) {
			showWarning("Alert code Nr 38", "Please select YES or No and insert remarks.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.H39_EMPTY_TANKS)) {
			showWarning("Alert code Nr 39", "Please insert identity of tanks.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.H40_EMPTY)) {
			showWarning("Alert code Nr 40", "Please complete code letter 40.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.I41_EMPTY_TANKS)) {
			showWarning("Alert code Nr 41", "Please insert identity of tanks.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.I42_EMPTY)) {
			showWarning("Alert code Nr 42", "Please insert hours.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.I43_EMPTY)) {
			showWarning("Alert code Nr 43", "Please insert hours.");
			return false;
		}

		if (result.getKey().keySet().contains(Validation.I44_EMPTY)) {
			showWarning("Alert code Nr 44", "Please insert time and position.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.I44_TIME_EMPTY)) {
			showWarning("Alert code Nr 44", "Please insert Time.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.I44_TIME_AFTER_REGISTRATION_DATE)) {
			showWarning("Alert code Nr 44", "Start Time is after Registration Time.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.I45_EMPTY)) {
			showWarning("Alert code Nr 45", "Please Select code letter 45.");
			return false;
		}

		if (result.getKey().keySet().contains(Validation.I46_EMPTY)) {
			showWarning("Alert code Nr 46", "Please Select code letter 46.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.I47_EMPTY)) {
			showWarning("Alert code Nr 47", "Please Select code letter 47.");
			return false;
		}

		if (result.getKey().keySet().contains(Validation.I48_EMPTY)) {
			showWarning("Alert code Nr 48", "Please Select code letter 48.");
			return false;
		}

		if (result.getKey().keySet().contains(Validation.I49_EMPTY)) {
			showWarning("Alert code Nr 49", "Please insert time and position.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.I49_TIME_EMPTY)) {
			showWarning("Alert code Nr 49", "Please insert Time.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.I49_TIME_AFTER_REGISTRATION_DATE)) {
			showWarning("Alert code Nr 49", "End Time is after Registration Time.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.I44_TIME_AT_START_AFTER_TIME_AT_STOP)) {
			showWarning("Alert code Nr 44", "Time at the start is after time at the end of discharge.");
			return false;
		}
		
		if (result.getKey().keySet().contains(Validation.I50_EMPTY)) {
			showWarning("Alert code Nr 50", "Please Complete Code Letter 50.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.I51_EMPTY)) {
			showWarning("Alert code Nr 51", "Please Complete Code Letter 51.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.I52_EMPTY)) {
			showWarning("Alert code Nr 52", "Please Insert Speed.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.I53_EMPTY)) {
			showWarning("Alert code Nr 53", "Please Complete Code Letter 53.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.I54_EMPTY)) {
			showWarning("Alert code Nr 54", "Please Complete Code Letter 54.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.J55_EMPTY_TANKS)) {
			showWarning("Alert code Nr 55", "Please insert identity of tanks.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.J56_EMPTY)) {
			showWarning("Alert code Nr 56", "Please select checkbox for quantity transfered or disposed.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.J57_EMPTY)) {
			showWarning("Alert code Nr 57.1", "Please insert disposal to reception facilities.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.J57_EMPTY_PORT)) {
			showWarning("Alert code Nr 57.1", "Please insert port name.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.J57_EMPTY_RECEPTION)) {
			showWarning("Alert code Nr 57.1", "Please insert reception.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.J57_EMPTY_QUANTITY)) {
			showWarning("Alert code Nr 57.2", "Please insert quantity.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.J57_EMPTY_TRANSFERED)) {
			showWarning("Alert code Nr 57.3", "Please insert transferred to or from (an) other tank(s).");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.J57_EMPTY_TANKS)) {
			showWarning("Alert code Nr 57.3", "Please insert identity of tanks.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.J57_EMPTY_QUANTITY1)) {
			showWarning("Alert code Nr 57.4", "Please insert quantity.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.K58_EMPTY_POSITION)) {
			showWarning("Alert code Nr 58", "Please Insert Position.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.K59_EMPTY_TANKS)) {
			showWarning("Alert code Nr 59", "Please insert identity of tanks.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.K60_EMPTY)) {
			showWarning("Alert code Nr 60", "Please Select Yes or No and insert remarks.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.K62_EMPTY)) {
			showWarning("Alert code Nr 62", "Please Select Yes or No and insert remarks.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.L63_EMPTY_TANKS)) {
			showWarning("Alert code Nr 63", "Please insert identity of tanks.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.L64_EMPTY)) {
			showWarning("Alert code Nr 64", "Please insert time and position.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.L64_TIME_AT_START_AFTER_TIME_AT_STOP)) {
			showWarning("Alert code Nr 64", "Time at start is after time at end of ballasting.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.L64_EMPTY_TIME)) {
			showWarning("Alert code Nr 64", "Please insert time.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.L65_EMPTY)) {
			showWarning("Alert code Nr 65", "Please insert time and position.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.L65_EMPTY_TIME)) {
			showWarning("Alert code Nr 65", "Please insert time.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.L66_EMPTY)) {
			showWarning("Alert code Nr 66", "Please select into sea or port.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.L66_EMPTY_RECEPTION)) {
			showWarning("Alert code Nr 66.2", "Please complete all fields.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.L67_EMPTY)) {
			showWarning("Alert code Nr 67", "Please Select YES or No and insert remarks");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.L68_EMPTY)) {
			showWarning("Alert code Nr 68", "Please Select YES or No and insert remarks.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.L69_EMPTY)) {
			showWarning("Alert code Nr 69", "Please insert time and position.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.L69_EMPTY_TIME)) {
			showWarning("Alert code Nr 69", "Please insert time.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.M70_EMPTY_TIME)) {
			showWarning("Alert code Nr 70", "Please Insert Time.");
			return false;
		}

		if (result.getKey().keySet().contains(Validation.M71_EMPTY_TIME)) {
			showWarning("Alert code Nr 71", "Please Insert Time.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.M72_EMPTY_REASON)) {
			showWarning("Alert code Nr 72", "Please Insert Reasons.");
			return false;
		}

		if (result.getKey().keySet().contains(Validation.N73_EMPTY_TIME)) {
			showWarning("Alert code Nr 73", "Please Insert Time.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.N74_EMPTY_POSITION)) {
			showWarning("Alert code Nr 74", "Please Insert Position.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.N75_EMPTY_QUANTITY)) {
			showWarning("Alert code Nr 75", "Please Insert Quantity.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.N76_EMPTY_REMARKS)) {
			showWarning("Alert code Nr 76", "Please Insert Remarks.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.O_EMPTY_OPERATION)) {
			showWarning("", "Please select operation.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.O_EMPTY_REMARKS)) {
			showWarning("", "Please insert remarks.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.O_EMPTY_MISSED_CODE_LETTER)) {
			showWarning("", "Please select  missed code letter.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.P77_EMPTY_TANKS)) {
			showWarning("Alert code Nr 77", "Please insert identity of tanks.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.P78_LAT_LON_LAT_LON_EMPTY)) {
			showWarning("Alert code Nr 78", "Please Insert Lattitude and Longitude.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.P79_EMPTY)) {
			showWarning("Alert code Nr 79", "Please Insert Quantity.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.P80_EMPTY)) {
			showWarning("Alert code Nr 80", "Please Insert Remarks.");
			return false;
		}

		if (result.getKey().keySet().contains(Validation.Q81_EMPTY_PORT)) {
			showWarning("Alert code Nr 81", "Please Insert Reasons for re-allocation.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.R82_EMPTY_PORT)) {
			showWarning("Alert code Nr 82", "Please Insert Port Name.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.R83_EMPTY_RECEPTION)) {
			showWarning("Alert code Nr 83", "Please Insert Reception Facility.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.R84_EMPTY_QUANTITY)) {
			showWarning("Alert code Nr 84", "Please Insert Quntity.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.R85_EMPTY_FILE)) {
			showWarning("Alert code Nr 85", "Please Insert File.");
			return false;
		}

		if (result.getKey().keySet().contains(Validation.SIGNATURE_MISSING) || result.getKey().keySet().contains(Validation.SIGNATURE_INVALID)) {
			showInformation("Signature Required", "Please sign the modifications.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.SIGNER_RANK_MISSING) || result.getKey().keySet().contains(Validation.SIGNER_NAME_MISSING) || result.getKey().keySet().contains(Validation.SIGNER_SURNAME_MISSING)) {
			showInformation("Rank, Name and Surname of signer required", "Please specify signer details.");
			return false;
		}
		if (result.getKey().keySet().contains(Validation.SIGNATURE_MISSING_CODE_LETTERO)) {
			showInformation("Signature Required", "Please sign missed operation code letter.");
			return false;
		}

		if (result.getKey().keySet().contains(Validation.D9_LAT_LON_ZERO)) {
			boolean toContinue = showConfirmation("Alert code Nr 23", "Lattitude or longitude is 0.\nDo you want to continue?");
			if (!toContinue)
				return false;
		}
		boolean contletterO = false;
		for (OilLog log : result.getValue()) {
			if (log.getCodeLetter().equals("O")) {
				contletterO = true;
			}
		}
		if (contletterO == false) {
			if (result.getKey().keySet().contains(Validation.DATE_BEFORE_LAST_LOG)) {
				return showConfirmation("Date error", "The compilation date cannot be before the last log compilation date. The record will be saved with the note 'Date error'. Do you want to continue?");
			}
		}

		if (result.getKey().keySet().contains(Validation.E18_LAT_LON_LAT_LON_ZERO)) {
			boolean toContinue = showConfirmation("Alert code Nr 23", "Lattitude or longitude is 0.\nDo you want to continue?");
			if (!toContinue)
				return false;
		}
		if (result.getKey().keySet().contains(Validation.F21_LAT_LON_LAT_LON_ZERO)) {
			boolean toContinue = showConfirmation("Alert code Nr 21", "Lattitude or longitude is 0.\nDo you want to continue?");
			if (!toContinue)
				return false;
		}
		if (result.getKey().keySet().contains(Validation.F22_LAT_LON_LAT_LON_ZERO)) {
			boolean toContinue = showConfirmation("Alert code Nr 22", "Lattitude or longitude is 0.\nDo you want to continue?");
			if (!toContinue)
				return false;
		}
		if (result.getKey().keySet().contains(Validation.F24_LAT_LON_LAT_LON_ZERO)) {
			boolean toContinue = showConfirmation("Alert code Nr 24", "Lattitude or longitude is 0.\nDo you want to continue?");
			if (!toContinue)
				return false;
		}
		if (result.getKey().keySet().contains(Validation.F25_LAT_LON_LAT_LON_ZERO)) {
			boolean toContinue = showConfirmation("Alert code Nr 25", "Lattitude or longitude is 0.\nDo you want to continue?");
			if (!toContinue)
				return false;
		}
		if (result.getKey().keySet().contains(Validation.G28_LAT_LON_LAT_LON_ZERO)) {
			boolean toContinue = showConfirmation("Alert code Nr 28", "Lattitude or longitude is 0.\nDo you want to continue?");
			if (!toContinue)
				return false;
		}
		if (result.getKey().keySet().contains(Validation.H33_LAT_LON_LAT_LON_ZERO)) {
			boolean toContinue = showConfirmation("Alert code Nr 33", "Lattitude or longitude is 0.\nDo you want to continue?");
			if (!toContinue)
				return false;
		}
		if (result.getKey().keySet().contains(Validation.H34_LAT_LON_LAT_LON_ZERO)) {
			boolean toContinue = showConfirmation("Alert code Nr 34", "Lattitude or longitude is 0.\nDo you want to continue?");
			if (!toContinue)
				return false;
		}
		if (result.getKey().keySet().contains(Validation.I44_LAT_LON_LAT_LON_ZERO)) {
			boolean toContinue = showConfirmation("Alert code Nr 44", "Lattitude or longitude is 0.\nDo you want to continue?");
			if (!toContinue)
				return false;
		}
		if (result.getKey().keySet().contains(Validation.I49_LAT_LON_LAT_LON_ZERO)) {
			boolean toContinue = showConfirmation("Alert code Nr 49", "Lattitude or longitude is 0.\nDo you want to continue?");
			if (!toContinue)
				return false;
		}

		if (result.getKey().keySet().contains(Validation.K58_LAT_LON_LAT_LON_ZERO)) {
			boolean toContinue = showConfirmation("Alert code Nr 58", "Lattitude or longitude is 0.\nDo you want to continue?");
			if (!toContinue)
				return false;
		}
		if (result.getKey().keySet().contains(Validation.K61_LAT_LON_LAT_LON_ZERO)) {
			boolean toContinue = showConfirmation("Alert code Nr 61", "Lattitude or longitude is 0.\nDo you want to continue?");
			if (!toContinue)
				return false;
		}
		if (result.getKey().keySet().contains(Validation.L64_LAT_LON_LAT_LON_ZERO)) {
			boolean toContinue = showConfirmation("Alert code Nr 64", "Lattitude or longitude is 0.\nDo you want to continue?");
			if (!toContinue)
				return false;
		}
		if (result.getKey().keySet().contains(Validation.L65_LAT_LON_LAT_LON_ZERO)) {
			boolean toContinue = showConfirmation("Alert code Nr 65", "Lattitude or longitude is 0.\nDo you want to continue?");
			if (!toContinue)
				return false;
		}
		if (result.getKey().keySet().contains(Validation.L69_LAT_LON_LAT_LON_ZERO)) {
			boolean toContinue = showConfirmation("Alert code Nr 69", "Lattitude or longitude is 0.\nDo you want to continue?");
			if (!toContinue)
				return false;
		}
		if (result.getKey().keySet().contains(Validation.N74_LAT_LON_LAT_LON_ZERO)) {
			boolean toContinue = showConfirmation("Alert code Nr 74", "Lattitude or longitude is 0.\nDo you want to continue?");
			if (!toContinue)
				return false;
		}
		if (result.getKey().keySet().contains(Validation.P78_LAT_LON_LAT_LON_ZERO)) {
			boolean toContinue = showConfirmation("Alert code Nr 78", "Lattitude or longitude is 0.\nDo you want to continue?");
			if (!toContinue)
				return false;
		}
		return true;
	}

	public void setOilRecordBookPart2CodeLetterAController(OilRecordBookPart2CodeLetterAController oilRecordBookPart2CodeLetterAController) {
		this.oilRecordBookPart2CodeLetterAController = oilRecordBookPart2CodeLetterAController;
	}

	public void setOilRecordBookPart2CodeLetterBController(OilRecordBookPart2CodeLetterBController oilRecordBookPart2CodeLetterBController) {
		this.oilRecordBookPart2CodeLetterBController = oilRecordBookPart2CodeLetterBController;
	}

	public void setOilRecordBookPart2CodeLetterCController(OilRecordBookPart2CodeLetterCController oilRecordBookPart2CodeLetterCController) {
		this.oilRecordBookPart2CodeLetterCController = oilRecordBookPart2CodeLetterCController;
	}

	public void setOilRecordBookPart2CodeLetterDController(OilRecordBookPart2CodeLetterDController oilRecordBookPart2CodeLetterDController) {
		this.oilRecordBookPart2CodeLetterDController = oilRecordBookPart2CodeLetterDController;
	}

	public void setOilRecordBookPart2CodeLetterEController(OilRecordBookPart2CodeLetterEController oilRecordBookPart2CodeLetterEController) {
		this.oilRecordBookPart2CodeLetterEController = oilRecordBookPart2CodeLetterEController;
	}

	public void setOilRecordBookPart2CodeLetterFController(OilRecordBookPart2CodeLetterFController oilRecordBookPart2CodeLetterFController) {
		this.oilRecordBookPart2CodeLetterFController = oilRecordBookPart2CodeLetterFController;
	}

	public void setOilRecordBookPart2CodeLetterGController(OilRecordBookPart2CodeLetterGController oilRecordBookPart2CodeLetterGController) {
		this.oilRecordBookPart2CodeLetterGController = oilRecordBookPart2CodeLetterGController;
	}

	public void setOilRecordBookPart2CodeLetterHController(OilRecordBookPart2CodeLetterHController oilRecordBookPart2CodeLetterHController) {
		this.oilRecordBookPart2CodeLetterHController = oilRecordBookPart2CodeLetterHController;
	}

	public void setOilRecordBookPart2CodeLetterIController(OilRecordBookPart2CodeLetterIController oilRecordBookPart2CodeLetterIController) {
		this.oilRecordBookPart2CodeLetterIController = oilRecordBookPart2CodeLetterIController;
	}

	public void setOilRecordBookPart2CodeLetterJController(OilRecordBookPart2CodeLetterJController oilRecordBookPart2CodeLetterJController) {
		this.oilRecordBookPart2CodeLetterJController = oilRecordBookPart2CodeLetterJController;
	}

	public void setOilRecordBookPart2CodeLetterKController(OilRecordBookPart2CodeLetterKController oilRecordBookPart2CodeLetterKController) {
		this.oilRecordBookPart2CodeLetterKController = oilRecordBookPart2CodeLetterKController;
	}

	public void setOilRecordBookPart2CodeLetterLController(OilRecordBookPart2CodeLetterLController oilRecordBookPart2CodeLetterLController) {
		this.oilRecordBookPart2CodeLetterLController = oilRecordBookPart2CodeLetterLController;
	}

	public void setOilRecordBookPart2CodeLetterMController(OilRecordBookPart2CodeLetterMController oilRecordBookPart2CodeLetterMController) {
		this.oilRecordBookPart2CodeLetterMController = oilRecordBookPart2CodeLetterMController;
	}

	public void setOilRecordBookPart2CodeLetterNController(OilRecordBookPart2CodeLetterNController oilRecordBookPart2CodeLetterNController) {
		this.oilRecordBookPart2CodeLetterNController = oilRecordBookPart2CodeLetterNController;
	}

	public void setOilRecordBookPart2CodeLetterOController(OilRecordBookPart2CodeLetterOController oilRecordBookPart2CodeLetterOController) {
		this.oilRecordBookPart2CodeLetterOController = oilRecordBookPart2CodeLetterOController;
	}

	public void setOilRecordBookPart2CodeLetterPController(OilRecordBookPart2CodeLetterPController oilRecordBookPart2CodeLetterPController) {
		this.oilRecordBookPart2CodeLetterPController = oilRecordBookPart2CodeLetterPController;
	}

	public void setOilRecordBookPart2CodeLetterQController(OilRecordBookPart2CodeLetterQController oilRecordBookPart2CodeLetterQController) {
		this.oilRecordBookPart2CodeLetterQController = oilRecordBookPart2CodeLetterQController;
	}

	public void setOilRecordBookPart2CodeLetterRController(OilRecordBookPart2CodeLetterRController oilRecordBookPart2CodeLetterRController) {
		this.oilRecordBookPart2CodeLetterRController = oilRecordBookPart2CodeLetterRController;
	}

	public void setOilRecordBookPart2ViewControllerToCodeLetterO(OilRecordBookPart2ViewController oilRecordBookPart2ViewController) {
		oilRecordBookPart2CodeLetterOController.setController(oilRecordBookPart2ViewController);
	}

	public void setOldCodLetterBackListNode(ObservableList<Node> nodeList, String codeLetter) {
		if (codeLetter.equals("A")) {
			oilRecordBookPart2CodeLetterAController.codeLetterContentAnchorPane.getChildren().addAll(nodeList);
		} else if (codeLetter.equals("B")) {
			oilRecordBookPart2CodeLetterBController.codeLetterContentAnchorPane.getChildren().addAll(nodeList);
		} else if (codeLetter.equals("C")) {
			oilRecordBookPart2CodeLetterCController.codeLetterContentAnchorPane.getChildren().addAll(nodeList);
		} else if (codeLetter.equals("D")) {
			oilRecordBookPart2CodeLetterDController.codeLetterContentAnchorPane.getChildren().addAll(nodeList);
		} else if (codeLetter.equals("E")) {
			oilRecordBookPart2CodeLetterEController.codeLetterContentAnchorPane.getChildren().addAll(nodeList);
		} else if (codeLetter.equals("F")) {
			oilRecordBookPart2CodeLetterFController.codeLetterContentAnchorPane.getChildren().addAll(nodeList);
		} else if (codeLetter.equals("G")) {
			oilRecordBookPart2CodeLetterGController.codeLetterContentAnchorPane.getChildren().addAll(nodeList);
		} else if (codeLetter.equals("H")) {
			oilRecordBookPart2CodeLetterHController.codeLetterContentAnchorPane.getChildren().addAll(nodeList);
		} else if (codeLetter.equals("I")) {
			oilRecordBookPart2CodeLetterIController.codeLetterContentAnchorPane.getChildren().addAll(nodeList);
		} else if (codeLetter.equals("J")) {
			oilRecordBookPart2CodeLetterJController.codeLetterContentAnchorPane.getChildren().addAll(nodeList);
		} else if (codeLetter.equals("K")) {
			oilRecordBookPart2CodeLetterKController.codeLetterContentAnchorPane.getChildren().addAll(nodeList);
		} else if (codeLetter.equals("L")) {
			oilRecordBookPart2CodeLetterLController.codeLetterContentAnchorPane.getChildren().addAll(nodeList);
		} else if (codeLetter.equals("M")) {
			oilRecordBookPart2CodeLetterMController.codeLetterContentAnchorPane.getChildren().addAll(nodeList);
		} else if (codeLetter.equals("N")) {
			oilRecordBookPart2CodeLetterNController.codeLetterContentAnchorPane.getChildren().addAll(nodeList);
		} else if (codeLetter.equals("P")) {
			oilRecordBookPart2CodeLetterPController.codeLetterContentAnchorPane.getChildren().addAll(nodeList);
		} else if (codeLetter.equals("Q")) {
			oilRecordBookPart2CodeLetterQController.codeLetterContentAnchorPane.getChildren().addAll(nodeList);
		} else if (codeLetter.equals("R")) {
			oilRecordBookPart2CodeLetterRController.codeLetterContentAnchorPane.getChildren().addAll(nodeList);
		}
	}

	public void setAllCodeLetterAtO(String codeLetter) {
		if (codeLetter.equals("A")) {
			oilRecordBookPart2CodeLetterOController.missedCodeLetterCodeLetterContentAnchorPane.getChildren().addAll(oilRecordBookPart2CodeLetterAController.codeLetterContentAnchorPane.getChildren());
		} else if (codeLetter.equals("B")) {
			oilRecordBookPart2CodeLetterOController.missedCodeLetterCodeLetterContentAnchorPane.getChildren().addAll(oilRecordBookPart2CodeLetterBController.codeLetterContentAnchorPane.getChildren());

		} else if (codeLetter.equals("C")) {
			oilRecordBookPart2CodeLetterOController.missedCodeLetterCodeLetterContentAnchorPane.getChildren().addAll(oilRecordBookPart2CodeLetterCController.codeLetterContentAnchorPane.getChildren());

		} else if (codeLetter.equals("D")) {
			oilRecordBookPart2CodeLetterOController.missedCodeLetterCodeLetterContentAnchorPane.getChildren().addAll(oilRecordBookPart2CodeLetterDController.codeLetterContentAnchorPane.getChildren());

		} else if (codeLetter.equals("E")) {
			oilRecordBookPart2CodeLetterOController.missedCodeLetterCodeLetterContentAnchorPane.getChildren().addAll(oilRecordBookPart2CodeLetterEController.codeLetterContentAnchorPane.getChildren());

		} else if (codeLetter.equals("F")) {
			oilRecordBookPart2CodeLetterOController.missedCodeLetterCodeLetterContentAnchorPane.getChildren().addAll(oilRecordBookPart2CodeLetterFController.codeLetterContentAnchorPane.getChildren());

		} else if (codeLetter.equals("G")) {
			oilRecordBookPart2CodeLetterOController.missedCodeLetterCodeLetterContentAnchorPane.getChildren().addAll(oilRecordBookPart2CodeLetterGController.codeLetterContentAnchorPane.getChildren());

		} else if (codeLetter.equals("H")) {
			oilRecordBookPart2CodeLetterOController.missedCodeLetterCodeLetterContentAnchorPane.getChildren().addAll(oilRecordBookPart2CodeLetterHController.codeLetterContentAnchorPane.getChildren());

		} else if (codeLetter.equals("I")) {
			oilRecordBookPart2CodeLetterOController.missedCodeLetterCodeLetterContentAnchorPane.getChildren().addAll(oilRecordBookPart2CodeLetterIController.codeLetterContentAnchorPane.getChildren());

		} else if (codeLetter.equals("J")) {
			oilRecordBookPart2CodeLetterOController.missedCodeLetterCodeLetterContentAnchorPane.getChildren().addAll(oilRecordBookPart2CodeLetterJController.codeLetterContentAnchorPane.getChildren());

		} else if (codeLetter.equals("K")) {
			oilRecordBookPart2CodeLetterOController.missedCodeLetterCodeLetterContentAnchorPane.getChildren().addAll(oilRecordBookPart2CodeLetterKController.codeLetterContentAnchorPane.getChildren());

		} else if (codeLetter.equals("L")) {
			oilRecordBookPart2CodeLetterOController.missedCodeLetterCodeLetterContentAnchorPane.getChildren().addAll(oilRecordBookPart2CodeLetterLController.codeLetterContentAnchorPane.getChildren());

		} else if (codeLetter.equals("M")) {
			oilRecordBookPart2CodeLetterOController.missedCodeLetterCodeLetterContentAnchorPane.getChildren().addAll(oilRecordBookPart2CodeLetterMController.codeLetterContentAnchorPane.getChildren());

		} else if (codeLetter.equals("N")) {
			oilRecordBookPart2CodeLetterOController.missedCodeLetterCodeLetterContentAnchorPane.getChildren().addAll(oilRecordBookPart2CodeLetterNController.codeLetterContentAnchorPane.getChildren());

		} else if (codeLetter.equals("P")) {
			oilRecordBookPart2CodeLetterOController.missedCodeLetterCodeLetterContentAnchorPane.getChildren().addAll(oilRecordBookPart2CodeLetterPController.codeLetterContentAnchorPane.getChildren());

		} else if (codeLetter.equals("Q")) {
			oilRecordBookPart2CodeLetterOController.missedCodeLetterCodeLetterContentAnchorPane.getChildren().addAll(oilRecordBookPart2CodeLetterQController.codeLetterContentAnchorPane.getChildren());

		} else if (codeLetter.equals("R")) {
			oilRecordBookPart2CodeLetterOController.missedCodeLetterCodeLetterContentAnchorPane.getChildren().addAll(oilRecordBookPart2CodeLetterRController.codeLetterContentAnchorPane.getChildren());

		}
	}

	public enum Validation {
		SHIP_MISSING, DATE_MISSING, DATE_MISSING_CODE_LETTERO, DATE_BEFORE_LAST_LOG, DATE_AFTER_NOW, MISSED_DATE_AFTER_NOW,SIGNATURE_MISSING, SIGNATURE_MISSING_CODE_LETTERO, SIGNATURE_INVALID, SIGNER_NAME_MISSING, SIGNER_SURNAME_MISSING, SIGNER_RANK_MISSING, A1_EMPTY,
		A2_TYPE_OF_OIL_EMPTY, A2_TANKS_EMPTY, A3_TOTAL_QUANTITY_EMPTY, A3_TOTAL_CONTENT_EMPTY,
		B4_EMPTY, B4_1_EMPTY, B4_2_EMPTY, B4_2_QUANTITY_TRANSFERED_EMPTY, B4_2_TOTAL_QUANTITY_EMPTY, B5_EMPTY, B5_QUANTITY_RETAINED_REQUIRED,
		C6_EMPTY, C7_EMPTY, C8_EMPTY, C8_QUANTITY_RETAINED_REQUIRED,
		D9_EMPTY, D9_PORT_EMPTY, D9_LATITUDE_OR_LONGITUDE_EMPTY, D9_LAT_LON_ZERO, D10_TANKS_EMPTY, D10_TANK_ALREADY_IN_LIST, D10_WASHING_TANK_TYPE_EMPTY, D11_NR_OF_MACHINES_IN_USE_EMPTY, D12_TIME_OF_START_OF_WASHING_EMPTY,
		D13_WASHING_PATTERN_TYPE_EMPTY, D13_WASHING_PATTERN_EMPLOYED_EMPTY, D14_WASHING_LINE_PRESSURE_VALUE_EMPTY, D14_WASHING_LINE_PRESSURE_TYPE_EMPTY, D15_TIME_OF_COMPLETION_OR_STOP_OF_WASHING_EMPTY, D16_EMPTY, D17_EMPTY,
		E19_EMPTY, E18_EMPTY, E18_LAT_LON_LAT_LON_ZERO, E19_TANKS_EMPTY, E19_QUANTITY_EMPTY, E19_TIME_EMPTY, E19_TIME_AT_START_AFTER_TIME_AT_STOP,
		F20_EMPTY_TANKS, F21_EMPTY, F21_LAT_LON_LAT_LON_ZERO, F22_LAT_LON_LAT_LON_ZERO, F23_EMPTY_TANKS, F24_LAT_LON_LAT_LON_ZERO, F25_LAT_LON_LAT_LON_ZERO, F25_TIME_EMPTY,F26_EMPTY_QUANTITY,
		G27_EMPTY_TANKS, G28_EMPTY, G29_EMPTY, G28_LAT_LON_LAT_LON_ZERO, G30_EMPTY, G30_QUANTITY_EMPTY, G31_EMPTY, G31_EMPTY_TANKS, G31_EMPTY_RECEPTION,
		H32_EMPTY_TANKS, H33_EMPTY, H34_EMPTY, H33_TIME_AT_START_AFTER_TIME_AT_STOP, H35_EMPTY, H36_EMPTY, H37_EMPTY, H38_EMPTY, H33_LAT_LON_LAT_LON_ZERO, H34_LAT_LON_LAT_LON_ZERO, H33_TIME_EMPTY, H34_TIME_EMPTY, H33_TIME_AFTER_REGISTRATION_DATE, H34_TIME_AFTER_REGISTRATION_DATE, H39_EMPTY_TANKS, H40_EMPTY,
		I41_EMPTY_TANKS, I42_EMPTY, I43_EMPTY, I44_EMPTY, I44_TIME_AT_START_AFTER_TIME_AT_STOP, I44_LAT_LON_LAT_LON_ZERO, I44_TIME_EMPTY, I44_TIME_AFTER_REGISTRATION_DATE, I45_EMPTY, I46_EMPTY, I47_EMPTY, I48_EMPTY, I49_EMPTY, I49_LAT_LON_LAT_LON_ZERO, I49_TIME_EMPTY, I49_TIME_AFTER_REGISTRATION_DATE, I50_EMPTY, I51_EMPTY, I52_EMPTY, I53_EMPTY, I54_EMPTY,
		J55_EMPTY_TANKS, J56_EMPTY, J57_EMPTY, J57_EMPTY_PORT, J57_EMPTY_RECEPTION, J57_EMPTY_QUANTITY, J57_EMPTY_QUANTITY1, J57_EMPTY_TRANSFERED, J57_EMPTY_TANKS,
		K58_EMPTY_POSITION, K58_LAT_LON_LAT_LON_ZERO, K59_EMPTY_TANKS, K60_EMPTY, K61_LAT_LON_LAT_LON_ZERO, K62_EMPTY, 
		L63_EMPTY_TANKS, L64_EMPTY, L64_EMPTY_TIME, L65_EMPTY_TIME, L65_EMPTY, L64_LAT_LON_LAT_LON_ZERO, L65_LAT_LON_LAT_LON_ZERO, L69_LAT_LON_LAT_LON_ZERO, L66_EMPTY, L66_EMPTY_RECEPTION, L67_EMPTY, L68_EMPTY, L69_EMPTY, L69_EMPTY_TIME,
		M70_EMPTY_TIME, M71_EMPTY_TIME, M72_EMPTY_REASON,
		N73_EMPTY_TIME, N74_EMPTY_POSITION, N74_LAT_LON_LAT_LON_ZERO, N75_EMPTY_QUANTITY, N76_EMPTY_REMARKS,
		O_EMPTY_OPERATION, O_EMPTY_REMARKS, O_EMPTY_MISSED_CODE_LETTER,
		P77_EMPTY_TANKS, P78_LAT_LON_LAT_LON_ZERO, P78_LAT_LON_LAT_LON_EMPTY, P79_EMPTY, P80_EMPTY, 
		Q81_EMPTY_PORT,
		R82_EMPTY_PORT, R83_EMPTY_RECEPTION, R84_EMPTY_QUANTITY, R85_EMPTY_FILE, D12_TIME_AT_START_OF_WASHING_AFTER_TIME_AT_STOP, L64_TIME_AT_START_AFTER_TIME_AT_STOP
	}

	public OilRecordBookPart2CodeLetterAController getOilRecordBookPart2CodeLetterAController() {
		return oilRecordBookPart2CodeLetterAController;
	}

	public OilRecordBookPart2CodeLetterBController getOilRecordBookPart2CodeLetterBController() {
		return oilRecordBookPart2CodeLetterBController;
	}

	public OilRecordBookPart2CodeLetterCController getOilRecordBookPart2CodeLetterCController() {
		return oilRecordBookPart2CodeLetterCController;
	}

	public OilRecordBookPart2CodeLetterDController getOilRecordBookPart2CodeLetterDController() {
		return oilRecordBookPart2CodeLetterDController;
	}

	public OilRecordBookPart2CodeLetterEController getOilRecordBookPart2CodeLetterEController() {
		return oilRecordBookPart2CodeLetterEController;
	}

	public OilRecordBookPart2CodeLetterFController getOilRecordBookPart2CodeLetterFController() {
		return oilRecordBookPart2CodeLetterFController;
	}

	public OilRecordBookPart2CodeLetterGController getOilRecordBookPart2CodeLetterGController() {
		return oilRecordBookPart2CodeLetterGController;
	}

	public OilRecordBookPart2CodeLetterHController getOilRecordBookPart2CodeLetterHController() {
		return oilRecordBookPart2CodeLetterHController;
	}

	public OilRecordBookPart2CodeLetterIController getOilRecordBookPart2CodeLetterIController() {
		return oilRecordBookPart2CodeLetterIController;
	}

	public OilRecordBookPart2CodeLetterJController getOilRecordBookPart2CodeLetterJController() {
		return oilRecordBookPart2CodeLetterJController;
	}

	public OilRecordBookPart2CodeLetterKController getOilRecordBookPart2CodeLetterKController() {
		return oilRecordBookPart2CodeLetterKController;
	}

	public OilRecordBookPart2CodeLetterLController getOilRecordBookPart2CodeLetterLController() {
		return oilRecordBookPart2CodeLetterLController;
	}

	public OilRecordBookPart2CodeLetterMController getOilRecordBookPart2CodeLetterMController() {
		return oilRecordBookPart2CodeLetterMController;
	}

	public OilRecordBookPart2CodeLetterNController getOilRecordBookPart2CodeLetterNController() {
		return oilRecordBookPart2CodeLetterNController;
	}

	public OilRecordBookPart2CodeLetterOController getOilRecordBookPart2CodeLetterOController() {
		return oilRecordBookPart2CodeLetterOController;
	}

	public OilRecordBookPart2CodeLetterPController getOilRecordBookPart2CodeLetterPController() {
		return oilRecordBookPart2CodeLetterPController;
	}

	public OilRecordBookPart2CodeLetterQController getOilRecordBookPart2CodeLetterQController() {
		return oilRecordBookPart2CodeLetterQController;
	}

	public OilRecordBookPart2CodeLetterRController getOilRecordBookPart2CodeLetterRController() {
		return oilRecordBookPart2CodeLetterRController;
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


}
