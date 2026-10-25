package al.sybina.ebr.logbook2.views;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.tools.resources.WorkbenchResourceProvider;
import org.eclipse.e4.tools.services.IResourceProviderService;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;
import org.eclipse.fx.ui.services.resources.ImageProvider;

import com.jfoenix.controls.JFXChipView;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.Tank;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.TankType;

import al.sybina.ebr.commons.utils.CoordinateUIUtils;
import al.sybina.ebr.commons.utils.JavaFXUtils;
import al.sybina.ebr.commons.utils.MathUtils;
import al.sybina.ebr.logbook.commons.Activator;
import al.sybina.ebr.logbook.commons.BallonMessage;
import al.sybina.ebr.logbook.dialogs.SelectTankerDialog;
import al.sybina.ebr.logbook.dialogs.TankUsageContext;
import al.sybina.ebr.logbook.dialogs.help.HelpCodeLetterDialog;
import al.sybina.ebr.logbook.domain.reladomo.OilLog;
import al.sybina.ebr.logbook2.views.OilRecordBookPart2ViewController.Validation;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jfxtras.scene.control.ImageViewButton;
import jidefx.scene.control.field.DoubleField;
import jidefx.scene.control.field.FormattedTextField;

public class OilRecordBookPart2CodeLetterGController implements Initializable {

	private Label label1 = new Label("Hand-hosing, machine washing and/or chemical cleaning. \r\n" + "Were chemically cleaned, the chemical concerned\r\n" + "and amount used should be stated." + "");

	private Label label2 = new Label(" Slop tank(s) or cargo tank(s) \r\n" + "designated as slop tanks" + "");

	private static final Logger logger = LoggerCreator.createLogger(OilRecordBookPart2CodeLetterGController.class);

	@Inject
	private IEventBroker eventBroker;
	@Inject
	private BallonMessage ballonMessage;
	@Inject
	private IResourceProviderService resourcesService;
	@Inject
	private ImageProvider imageProvider;
	@Inject
	private SelectTankerDialog selectTankerDialog;
	@Inject
	private HelpCodeLetterDialog helpCodeLetterDialog;
	@FXML
	private ImageViewButton infoLetterAImageViewButton;
	@FXML
	private ImageViewButton logook2LetterGMethodOfCleaningImageViewButton;
	@FXML
	private ImageViewButton logbook2LetterGReceptionFacilitiesImageViewButton;
	@FXML
	private ImageViewButton identityOfTanksG31ImageViewButton;
	@FXML
	private ImageViewButton identityOfTanksG27ImageViewButton;
	@FXML
	private ImageViewButton logbook2LetterGSlopTanksImageViewButton;
	@FXML
	private CheckBox indetityTanksCleanedCheckBox;
	@FXML
	private CheckBox logBook2CodeLetterG28PositionCheckBox;
	@FXML
	private CheckBox logBook2CodeLetterG28PortCheckBox;
	@FXML
	private CheckBox logBook2CodeLetterG28LatLongCheckBox;
	@FXML
	private TextField logBook2CodeLetterG28PortTextField;
	@FXML
	private FormattedTextField logBook2CodeLetterG28LatFormattedTextField;
	@FXML
	private FormattedTextField logBook2CodeLetterG28LongFormattedTextField;
	@FXML
	private CheckBox logBook2CodeLetterG29DurationCheckBox;
	@FXML
	private TextField logBook2CodeLetterG29DurationTextField;
	@FXML
	private CheckBox methodCleaningCheckBox;
	@FXML
	private ComboBox<String> methodCleaningComboBox;
	@FXML
	private CheckBox washingTransferdCheckBox;
	@FXML
	private CheckBox receptionFacilitiesCheckBox;
	@FXML
	private CheckBox slopCargoCheckBox;
	@FXML
	private TextField receptionFacilitiesTextField;
	@FXML
	private DoubleField receptionQuantityDoubleField;
	@FXML
	private TextField receptionBDNTextField;
	@FXML
	private DoubleField slopCargoQuantityDoubleField;
	@FXML
	private JFXChipView<String> identityOfTankJFXChipsView;
	@FXML
	private JFXChipView<String> identityOfTankSlopCargoJFXChipsView;
	@FXML
	private ImageViewButton uploadReceiptImageViewButton;
	@FXML
	private DoubleField methodOfCleaningQuantityDoubleField;
	@FXML
	private Label methodOfCleaningQuantityLabel;
	
	protected String CACHE_FILE_NAME = System.getProperty("user.home") + "/.ebr/caches/caches.properties";
	@FXML
	public AnchorPane codeLetterContentAnchorPane;
	private ObservableList<Image> helpImageList = FXCollections.observableArrayList();
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MathUtils.setStringConverter(receptionQuantityDoubleField);
		MathUtils.setStringConverter(slopCargoQuantityDoubleField);
		MathUtils.setStringConverter(methodOfCleaningQuantityDoubleField);
		MathUtils.setMinMaxValues(receptionQuantityDoubleField, 0.0, null);
		MathUtils.setMinMaxValues(slopCargoQuantityDoubleField, 0.0, null);
		MathUtils.setMinMaxValues(methodOfCleaningQuantityDoubleField, 0.0, null);
		try {

			Image helpImageLetterG2 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPG2));
			
			Image helpImageLetterG1 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPG1));
			

			Image helpImageLetterG3 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPG3));
			
			helpImageList.addAll(helpImageLetterG2, helpImageLetterG1, helpImageLetterG3);
			
			Image addImageIdentityOfTanks = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_ADD));

			identityOfTanksG31ImageViewButton.setImage(addImageIdentityOfTanks);
			identityOfTanksG27ImageViewButton.setImage(addImageIdentityOfTanks);
			identityOfTanksG27ImageViewButton.setDisable(true);
			identityOfTanksG31ImageViewButton.setDisable(true);

			Image infoIconImageInfoLetterG = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
			infoLetterAImageViewButton.setImage(infoIconImageInfoLetterG);
			logook2LetterGMethodOfCleaningImageViewButton.setImage(infoIconImageInfoLetterG);
			logbook2LetterGSlopTanksImageViewButton.setImage(infoIconImageInfoLetterG);

			logBook2CodeLetterG28PortCheckBox.setDisable(true);
			logBook2CodeLetterG28LatLongCheckBox.setDisable(true);
			logBook2CodeLetterG28PortTextField.setDisable(true);
			logBook2CodeLetterG28LatFormattedTextField.setDisable(true);
			logBook2CodeLetterG28LongFormattedTextField.setDisable(true);
			CoordinateUIUtils.initializeLatitudeField(logBook2CodeLetterG28LatFormattedTextField);
			CoordinateUIUtils.initializeLongitudeField(logBook2CodeLetterG28LongFormattedTextField);
			logBook2CodeLetterG28PortTextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
			logBook2CodeLetterG29DurationTextField.setDisable(true);
			logBook2CodeLetterG29DurationTextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
			receptionFacilitiesTextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
			JavaFXUtils.addFilledPseudoClass(logBook2CodeLetterG28PortTextField);
			JavaFXUtils.addFilledPseudoClass(logBook2CodeLetterG28LatFormattedTextField);
			JavaFXUtils.addFilledPseudoClass(logBook2CodeLetterG28LongFormattedTextField);
			JavaFXUtils.addFilledPseudoClass(logBook2CodeLetterG29DurationTextField);
			JavaFXUtils.addFilledPseudoClass(receptionFacilitiesTextField);
			JavaFXUtils.addFilledPseudoClass(receptionQuantityDoubleField);
			JavaFXUtils.addFilledPseudoClass(receptionBDNTextField);
			JavaFXUtils.addFilledPseudoClass(slopCargoQuantityDoubleField);

			methodCleaningComboBox.getItems().addAll("HAND-HOSING", "HAND-HOSING, CHEMICAL CLEANING", "MACHINE WASHING", "MACHINE WASHING, CHEMICAL CLEANING");
			methodCleaningComboBox.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					String action = methodCleaningComboBox.getValue();
					if (action.equals("HAND-HOSING, CHEMICAL CLEANING") || action.equals("MACHINE WASHING, CHEMICAL CLEANING")) {
						methodOfCleaningQuantityDoubleField.clear();
						methodOfCleaningQuantityDoubleField.setVisible(true);
						methodOfCleaningQuantityLabel.setVisible(true);
					} else {
						methodOfCleaningQuantityDoubleField.clear();
						methodOfCleaningQuantityDoubleField.setVisible(false);
						methodOfCleaningQuantityLabel.setVisible(false);
					}
				}
			});
			receptionFacilitiesCheckBox.setDisable(true);
			slopCargoCheckBox.setDisable(true);
			receptionFacilitiesTextField.setDisable(true);
			receptionQuantityDoubleField.setDisable(true);
			receptionBDNTextField.setDisable(true);
			slopCargoQuantityDoubleField.setDisable(true);

			indetityTanksCleanedCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
				if (newValue) {
					identityOfTankJFXChipsView.setDisable(false);
					identityOfTanksG27ImageViewButton.setDisable(false);
					logBook2CodeLetterG28PositionCheckBox.setSelected(true);
					logBook2CodeLetterG29DurationCheckBox.setSelected(true);
					methodCleaningCheckBox.setSelected(true);
					washingTransferdCheckBox.setSelected(true);
				} else {
					identityOfTankJFXChipsView.setDisable(true);
					identityOfTanksG27ImageViewButton.setDisable(true);
					logBook2CodeLetterG28PositionCheckBox.setSelected(false);
					logBook2CodeLetterG29DurationCheckBox.setSelected(false);
					methodCleaningCheckBox.setSelected(false);
					washingTransferdCheckBox.setSelected(false);
				}
				identityOfTankJFXChipsView.getChips().clear();
			});
			TankUsageContext tankUsageContext = new TankUsageContext(Stream.of(TankType.BALLAST, TankType.CARGO, TankType.FUELOIL, TankType.OILYBILGE, TankType.SLOP, TankType.SLUDGE).collect(Collectors.toList()), false, true);
			// tankUsageContext.setNoNeedForUserTankQuantities(true);
			identityOfTankJFXChipsView.setUserData(tankUsageContext);
			addRemoveListener(identityOfTankJFXChipsView);
			identityOfTankJFXChipsView.getChips().addListener(new ListChangeListener<String>() {
				@Override
				public void onChanged(Change<? extends String> c) {
					if (indetityTanksCleanedCheckBox.isSelected()) {
//							Double totalUserTankQuantities = ((TankUsageContext)identityOfTankJFXChipsView.getUserData()).getTotalUserTankQuantities();
//							Double totalUserTankActualLevel = ((TankUsageContext)identityOfTankJFXChipsView.getUserData()).getTotalUserTankActualLevel();
//							//totalQuantityBallstRecivedDoubleField.setValue(totalUserTankQuantities);
						// totalQuantityBallastRecivedCheckBox.setSelected(true);
						// totalContentOfTanksDoubleField.setValue(totalUserTankActualLevel+totalUserTankQuantities);
					}
					JavaFXUtils.addTooltip(identityOfTankJFXChipsView);
				}
			});
			logBook2CodeLetterG28PositionCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
				if (newValue) {
					logBook2CodeLetterG28PortCheckBox.setDisable(false);
					logBook2CodeLetterG28LatLongCheckBox.setDisable(false);
				} else {
					logBook2CodeLetterG28PortCheckBox.setDisable(true);
					logBook2CodeLetterG28LatLongCheckBox.setDisable(true);
					logBook2CodeLetterG28PortCheckBox.setSelected(false);
					logBook2CodeLetterG28LatLongCheckBox.setSelected(false);
				}

			});

			logBook2CodeLetterG28PortCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
				if (newValue) {
					logBook2CodeLetterG28PortTextField.setDisable(false);
					logBook2CodeLetterG28LatLongCheckBox.setSelected(false);
				} else {
					logBook2CodeLetterG28PortTextField.setDisable(true);
					logBook2CodeLetterG28PortTextField.setText("");
				}

			});

			logBook2CodeLetterG28LatLongCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
				if (newValue) {
					logBook2CodeLetterG28LatFormattedTextField.setDisable(false);
					logBook2CodeLetterG28LongFormattedTextField.setDisable(false);
					logBook2CodeLetterG28PortCheckBox.setSelected(false);
				} else {
					logBook2CodeLetterG28LatFormattedTextField.setDisable(true);
					logBook2CodeLetterG28LongFormattedTextField.setDisable(true);
					logBook2CodeLetterG28LatFormattedTextField.setText("00°00.00'N");
					logBook2CodeLetterG28LongFormattedTextField.setText("000°00.00'W");
				}

			});
			logBook2CodeLetterG29DurationCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
				if (newValue) {
					logBook2CodeLetterG29DurationTextField.setDisable(false);
				} else {
					logBook2CodeLetterG29DurationTextField.setDisable(true);
					logBook2CodeLetterG29DurationTextField.setText("");
				}

			});
			methodCleaningCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
				if (newValue) {
					methodCleaningComboBox.setDisable(false);
				} else {
					methodCleaningComboBox.setDisable(true);
					methodCleaningComboBox.setValue("");
					;
				}

			});

			washingTransferdCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
				if (newValue) {
					receptionFacilitiesCheckBox.setDisable(false);
					slopCargoCheckBox.setDisable(false);
				} else {
					receptionFacilitiesCheckBox.setDisable(true);
					slopCargoCheckBox.setDisable(true);
				}

			});
			receptionFacilitiesCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
				if (newValue) {
					receptionFacilitiesTextField.setDisable(false);
					receptionQuantityDoubleField.setDisable(false);
					//receptionBDNTextField.setDisable(false);
					slopCargoCheckBox.setSelected(false);
					uploadReceiptImageViewButton.setDisable(false);
				} else {
					receptionFacilitiesTextField.setDisable(true);
					receptionQuantityDoubleField.setDisable(true);
					receptionBDNTextField.setDisable(true);
					uploadReceiptImageViewButton.setDisable(true);
					receptionFacilitiesTextField.setText("");
					receptionQuantityDoubleField.setText("");
					receptionBDNTextField.setText("");
				}

			});
			slopCargoCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
				if (newValue) {
					slopCargoQuantityDoubleField.setDisable(false);
					identityOfTanksG31ImageViewButton.setDisable(false);
					receptionFacilitiesCheckBox.setSelected(false);
				} else {
					slopCargoQuantityDoubleField.setDisable(true);
					slopCargoQuantityDoubleField.setText("");
					identityOfTanksG31ImageViewButton.setDisable(true);
				}
				identityOfTankSlopCargoJFXChipsView.getChips().clear();
			});
			TankUsageContext tankUsageContext1 = new TankUsageContext(Stream.of(TankType.BALLAST, TankType.CARGO, TankType.FUELOIL, TankType.OILYBILGE, TankType.SLOP, TankType.SLUDGE).collect(Collectors.toList()), true, true);
			identityOfTankSlopCargoJFXChipsView.setUserData(tankUsageContext1);
			addRemoveListener(identityOfTankSlopCargoJFXChipsView);
			identityOfTankSlopCargoJFXChipsView.getChips().addListener(new ListChangeListener<String>() {
				@Override
				public void onChanged(Change<? extends String> change) {
					if (slopCargoCheckBox.isSelected()) {
						Double totalUserTankQuantities = ((TankUsageContext) identityOfTankSlopCargoJFXChipsView.getUserData()).getTotalUserTankQuantities();
						Double totalUserTankActualLevel = ((TankUsageContext) identityOfTankSlopCargoJFXChipsView.getUserData()).getTotalUserTankActualLevel();
						slopCargoQuantityDoubleField.setValue(totalUserTankQuantities);
					}
					JavaFXUtils.addTooltip(identityOfTankSlopCargoJFXChipsView);
				}

			});
			FileChooser fileChooser = new FileChooser();
			Properties properties = new Properties();
			String userInitialDirectory = null;
			try {
				File cacheFile = new File(CACHE_FILE_NAME);
				if (cacheFile.isFile()) {
					try (FileInputStream cacheIn = new FileInputStream(cacheFile)) {
						properties.load(cacheIn);
					}
				}
				userInitialDirectory = properties.getProperty("logbookReceiptDir");
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
			}

			if (userInitialDirectory == null) {
				fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
			} else {
				fileChooser.setInitialDirectory(new File(userInitialDirectory));
			}
			FileChooser.ExtensionFilter pdfFilesFilter = new FileChooser.ExtensionFilter("PDF files", "*.pdf");
			fileChooser.getExtensionFilters().addAll(pdfFilesFilter);

			uploadReceiptImageViewButton.setImage(new Image(getClass().getResourceAsStream("/images/uploadReceipt.png")));
			uploadReceiptImageViewButton.setDisable(true);
			uploadReceiptImageViewButton.setOnMouseClicked(me -> {
				File selectedFile = null;
				try {
					selectedFile = fileChooser.showOpenDialog((Stage) ((ImageViewButton) me.getSource()).getScene().getWindow());
				} catch (IllegalArgumentException e) {
					fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
					selectedFile = fileChooser.showOpenDialog((Stage) ((ImageViewButton) me.getSource()).getScene().getWindow());
				}
				if (selectedFile != null) {
					properties.setProperty("logbookReceiptDir", selectedFile.getParent());
					File f = new File(CACHE_FILE_NAME);
					if (!f.exists())
						f.getParentFile().mkdirs();
					OutputStream out;
					try {
						out = new FileOutputStream(f);
						properties.store(out, "Last directory choosed by user");
						out.close();
					} catch (IOException ex) {
						logger.error("Last directory choosed by user " + ex.toString());
					}
					receptionBDNTextField.setText(selectedFile.getName());
					receptionBDNTextField.setUserData(selectedFile);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

		eventBroker.subscribe(Activator.MARPOL_DETAILS_RELOADED, new org.osgi.service.event.EventHandler() {
			@Override
			public void handleEvent(org.osgi.service.event.Event event) {
				ArrayList<Long> changedTankIds = (ArrayList<Long>) event.getProperty(Activator.TANK_IDS);
				if (changedTankIds == null) {
					identityOfTankJFXChipsView.getChips().clear();
					identityOfTankSlopCargoJFXChipsView.getChips().clear();
				} else if (changedTankIds != null && !changedTankIds.isEmpty()) {
					Activator.removeChangedTanksFromJFXChipView(changedTankIds, identityOfTankJFXChipsView);
					Activator.removeChangedTanksFromJFXChipView(changedTankIds, identityOfTankSlopCargoJFXChipsView);
				}
			}
		});
	}

	public void openIdentifyOfTanksG27(Event e) {
		selectTankerDialog.showDialog((Stage) ((ImageViewButton) e.getSource()).getScene().getWindow(), identityOfTankJFXChipsView);
	}

	public void openIdentifyOfTanksSlopCargo(Event e) {
		selectTankerDialog.showDialog((Stage) ((ImageViewButton) e.getSource()).getScene().getWindow(), identityOfTankSlopCargoJFXChipsView);
	}

	private void addRemoveListener(JFXChipView<String> jFXChipView) {
		jFXChipView.getChips().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> change) {
				while (change.next()) {
					for (String item : change.getRemoved()) {
						Map<Tank, Double> userTanks = ((TankUsageContext) jFXChipView.getUserData()).getUserTankQuantities();
						userTanks.entrySet().removeIf(entry -> entry.getKey().getName().equals(item));
					}
				}
			}
		});
	}

	public void letterG(MouseEvent event) {
		helpCodeLetterDialog.showDialog((Stage)((ImageViewButton)event.getSource()).getScene().getWindow(),helpImageList);
	}

	public void processG(OilLog logToInsert, List<OilLog> logToInsertList, Map<Validation, Object> validations) {
		validateFields(logToInsert.getBusinessDate(), validations);
		if (indetityTanksCleanedCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("27");
			log.setOrderInGroup("ZD");
			log.setContent("" + String.join(", ", identityOfTankJFXChipsView.getChips()));
			log.setUserTankActualLevels(((TankUsageContext) identityOfTankJFXChipsView.getUserData()).getUserTankActualLevels());
			logToInsertList.add(log);
		}
		if (logBook2CodeLetterG28PortCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("28");
			log.setOrderInGroup("ZE");
			log.setContent("" + logBook2CodeLetterG28PortTextField.getText());
			logToInsertList.add(log);
		}
		if (logBook2CodeLetterG28LatLongCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("28");
			log.setOrderInGroup("ZF");
			log.setContent("LAT:" + logBook2CodeLetterG28LatFormattedTextField.getText() + " LONG:" + logBook2CodeLetterG28LongFormattedTextField.getText());
			logToInsertList.add(log);
		}
		if (logBook2CodeLetterG29DurationCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("29");
			log.setOrderInGroup("ZG");
			log.setContent("" + logBook2CodeLetterG29DurationTextField.getText());
			logToInsertList.add(log);
		}
		if (methodCleaningCheckBox.isSelected() && methodCleaningComboBox.getValue()!=null) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("30");
			log.setOrderInGroup("ZH");
			log.setContent("" + methodCleaningComboBox.getValue().toString().toUpperCase() +" " +(!methodOfCleaningQuantityDoubleField.getText().isEmpty() ? methodOfCleaningQuantityDoubleField.getText() + " Ltrs":""));
			logToInsertList.add(log);
		}
		if (receptionFacilitiesCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("31.1");
			log.setOrderInGroup("ZI");
			// Map<Tank, Double> userTankActualLevels = new HashMap<Tank, Double>();
			StringBuilder sb = new StringBuilder();
			if (sb.length() > 0)
				sb.append("\n");
			sb.append(receptionFacilitiesTextField.getText());
			sb.append("" + receptionQuantityDoubleField.getText() + "M3");
			sb.append("" + receptionBDNTextField.getText());
			log.setContent(sb.toString());
			if (receptionBDNTextField.getUserData() != null && ((File) receptionBDNTextField.getUserData()).exists()) {
				log.setAttachmentName(((File) receptionBDNTextField.getUserData()).getName());
				byte[] attachmentBytes = null;
				try {
					attachmentBytes = Files.readAllBytes(((File) receptionBDNTextField.getUserData()).toPath());
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
				log.setAttachmentData(attachmentBytes);
			}
			// log.setUserTankActualLevels(userTankActualLevels);
			logToInsertList.add(log);
		} else if (slopCargoCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("31.2");
			log.setOrderInGroup("ZJ");
			log.setContent("" + String.join(", ", identityOfTankSlopCargoJFXChipsView.getChips()) + "\r\n" + "" + slopCargoQuantityDoubleField.getValue() + "M3");
			log.setUserTankActualLevels(((TankUsageContext) identityOfTankSlopCargoJFXChipsView.getUserData()).getUserTankActualLevels());
			logToInsertList.add(log);
		}

	}

	private void validateFields(Timestamp businessDate, Map<Validation, Object> validations) {
		if (!indetityTanksCleanedCheckBox.isSelected() || identityOfTankJFXChipsView.getChips().isEmpty()) {
			validations.put(Validation.G27_EMPTY_TANKS, null);
		}

		if (!logBook2CodeLetterG28LatLongCheckBox.isSelected() && !logBook2CodeLetterG28PortCheckBox.isSelected()) {
			validations.put(Validation.G28_EMPTY, null);
		}

		if (logBook2CodeLetterG28LatLongCheckBox.isSelected() && (logBook2CodeLetterG28LatFormattedTextField.getText().equals("00°00.00'N") || logBook2CodeLetterG28LongFormattedTextField.getText().equals("000°00.00'W"))) {

			validations.put(Validation.G28_LAT_LON_LAT_LON_ZERO, null);
		}
		if (logBook2CodeLetterG28PortCheckBox.isSelected() && (logBook2CodeLetterG28PortTextField.getText().isEmpty())) {
			validations.put(Validation.G28_EMPTY, null);
		}
		if (logBook2CodeLetterG29DurationCheckBox.isSelected() && (logBook2CodeLetterG29DurationTextField.getText().isEmpty())) {
			validations.put(Validation.G29_EMPTY, null);
		}
		if (!methodCleaningCheckBox.isSelected() || methodCleaningComboBox.getValue()==null || methodCleaningComboBox.getValue().isEmpty()) {
			validations.put(Validation.G30_EMPTY, null);
		}
		if(methodOfCleaningQuantityDoubleField.isVisible() && methodOfCleaningQuantityDoubleField.getValue()==null) {
			validations.put(Validation.G30_QUANTITY_EMPTY, null);
		}
		if (!slopCargoCheckBox.isSelected() && !receptionFacilitiesCheckBox.isSelected()) {
			validations.put(Validation.G31_EMPTY, null);
		}
		if (receptionFacilitiesCheckBox.isSelected() && (receptionFacilitiesTextField.getText().isEmpty() || receptionQuantityDoubleField.getText().isEmpty() || receptionBDNTextField.getText().isEmpty())) {
			validations.put(Validation.G31_EMPTY_RECEPTION, null);
		}
		if (slopCargoCheckBox.isSelected() && identityOfTankSlopCargoJFXChipsView.getChips().isEmpty()) {
			validations.put(Validation.G31_EMPTY_TANKS, null);
		}

	}

	public void showBalloon(MouseEvent event) {
		ballonMessage.showGeneralBalloon((ImageViewButton) event.getTarget(), label1);
	}

	public void showBalloon1(MouseEvent event) {
		ballonMessage.showGeneralBalloon((ImageViewButton) event.getTarget(), label2);
	}

	public void clearFilledInfo() {
		indetityTanksCleanedCheckBox.setSelected(false);
		logBook2CodeLetterG28PositionCheckBox.setSelected(false);
		logBook2CodeLetterG28PortCheckBox.setSelected(false);
		logBook2CodeLetterG28LatLongCheckBox.setSelected(false);
		logBook2CodeLetterG29DurationCheckBox.setSelected(false);
		methodCleaningCheckBox.setSelected(false);
		washingTransferdCheckBox.setSelected(false);
		receptionFacilitiesCheckBox.setSelected(false);
		slopCargoCheckBox.setSelected(false);
	}
}
