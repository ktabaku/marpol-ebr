package al.sybina.ebr.logbook.views;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.antlr.stringtemplate.StringTemplate;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.tools.resources.WorkbenchResourceProvider;
import org.eclipse.e4.tools.services.IResourceProviderService;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;
import org.eclipse.fx.ui.services.resources.ImageProvider;
import org.geotools.measure.Latitude;
import org.geotools.measure.Longitude;

import com.jfoenix.controls.JFXChipView;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.Tank;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.TankType;
import com.oceanpro.core.services.OperationLogReladomoService;
import com.oceanpro.core.services.OperationLogService;
import com.oceanpro.utils.EasyDateUtils;

import al.sybina.ebr.commons.utils.CoordinateUIUtils;
import al.sybina.ebr.commons.utils.JavaFXUtils;
import al.sybina.ebr.commons.utils.MathUtils;
import al.sybina.ebr.logbook.commons.Activator;
import al.sybina.ebr.logbook.commons.BallonMessage;
import al.sybina.ebr.logbook.dialogs.SelectTankerDialog;
import al.sybina.ebr.logbook.dialogs.TankUsageContext;
import al.sybina.ebr.logbook.dialogs.help.HelpCodeLetterDialog;
import al.sybina.ebr.logbook.domain.reladomo.OilLog;
import al.sybina.ebr.logbook.views.OilRecordBookPart1ViewController.Validation;
import al.sybina.ebr.logbook.views.marpoldetail.MarpolBean;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import jfxtras.scene.control.ImageViewButton;
import jfxtras.scene.control.LocalTimeTextField;
import jidefx.scene.control.field.DoubleField;
import jidefx.scene.control.field.FormattedTextField;

public class OilRecordBookPart1CodeLetterDController implements Initializable {

	private Label label6 = new Label("Notes:\r\n"
			+ "The identity of the oily bilge water holding tank  \r\n"
			+ "and its capacity recorded in this example under  \r\n"
			+ "D 15.3  should be in strict compliance with  \r\n"
			+ "section 3.3 of the supplement of the IOPPC\r\n" + "");
	private Label label5 = new Label("Notes:\r\n" + "The ship's Master should obtain from the operator of \r\n"
			+ "the reception facilities, which includes barges and tank \r\n"
			+ "trucks, a receipt or certificate detailing the quantity of \r\n" + "tank washings,\r\n"
			+ "dirty ballast, residues or 0ily mixtures transferred  \r\n"
			+ "together with the time and date of the transfer. This \r\n"
			+ "receipt or certificate, if attached to the Oil Record Book \r\n"
			+ "(ORB) Part I, may aid the Master of the ship in proving \r\n"
			+ "that the ship was not involved in an alleged pollution \r\n"
			+ "incident. The receipt or certificate should be kept \r\n" + "together with the ORB Part I.\r\n" + "");
	
	private static final Logger logger = LoggerCreator.createLogger(OilRecordBookPart1CodeLetterDController.class);
	
	protected String CACHE_FILE_NAME = System.getProperty("user.home") + "/.ebr/caches/caches.properties";
	
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
	@Inject
	private OperationLogReladomoService operationLogReladomoService;
	@Inject
	private OperationLogService operationLogService;
	@Inject
	private MarpolBean marpolBean;
	@Inject
	private IEventBroker eventBroker;
	
	// codeLetter D
	@FXML
	private ImageViewButton imageViewButtonInfoReceptionFacilities;
	@FXML
	private ImageViewButton imageViewButtonInfoStateQuantity15_1;
//	@FXML
//	private ImageViewButton imageViewButtonInfoStateQuantity15_2;
//	@FXML
//	private ImageViewButton imageViewButtonInfoStateQuantity15_3;
	@FXML
	private ImageViewButton imageViewButtonInfoLetterD;
	@FXML
	private CheckBox quantityDischargedTransferredCheckBox;
	@FXML
	private CheckBox engineRoomBigleWellsCheckBox;
	@FXML
	private CheckBox timeOfDischargeCheckBox;
	@FXML
	private DoubleField quantityDischargedTransferredOrDisposedDoubleField;
	@FXML
	private ImageViewButton quantityDischargedToTankImageViewButton;
	@FXML
	private LocalTimeTextField timeOfDischargeStartLocalTime;
	@FXML
	private LocalTimeTextField timeOfDischargeStopLocalTime;
	@FXML
	private CheckBox methodOfDischargeTransferCheckBox;
	@FXML
	private CheckBox positionAtStartDischargeCheckBox;
	@FXML
	private FormattedTextField<Latitude> positionAtStartDischargeLatTextField;
	@FXML
	private FormattedTextField<Longitude> positionAtStartDischargeLongTextField;
	@FXML
	private FormattedTextField<Latitude> positionAtEndDischargeLatTextField;
	@FXML
	private FormattedTextField<Longitude> positionAtEndDischargeLongTextField;
	@FXML
	private CheckBox toReceptionFacilitiesIdentifyCheckBox;
	@FXML
	private TextField toReceptionFacilitiesIdentifyPortTextField;
	@FXML
	private CheckBox toSlopTankCheckBox;
	@FXML
	private Button addBilgeTankButton;
	@FXML
	private Button addSlopTankButton;
	@FXML
	private ImageViewButton addSlopTankOrHoldingTankOrOtherTankImageViewButton;
	@FXML
	private JFXChipView<String> tankNameFromTanksJFXChipView;
	@FXML
	private JFXChipView<String> tankNameToTanksJFXChipView;
	@FXML
	private DoubleField tankQuantityFromBilgeDoubleField;
	@FXML
	private DoubleField tankRetainedBilgeTankDoubleField;
	@FXML
	private DoubleField tankRetainedSlopTankDoubleField;
	@FXML
	private DoubleField tankQuantityToSlopDoubleField;
	@FXML
	private ToggleGroup transferType;
	@FXML
	private Toggle dischargedOrDisposedToggle;
	@FXML
	private Toggle transferredToggle;
	@FXML
	private CheckBox bilgeTanksCheckBox;
	@FXML
	private GridPane bilgeGridPane;
	@FXML
	private GridPane dSlopGridPane;
	@FXML
	private Label bilgeWellsOfLabel;
	@FXML
	private Node bilgeWellsM3Label;
	@FXML
	private Label bilgeTanksRetainedLabel;
	@FXML
	private Label bilgeTanksM3Label;
	@FXML
	private Label bilgeTanksQuantityLabel;
	@FXML
	private Label bilgeTanksM3_2Label;
	@FXML
	private Label identifyTheTankLabel;
	@FXML
	private HBox identifyTheTankShellHBox;
	@FXML
	private TextField receiptTextField;
	@FXML
	private ImageViewButton uploadReceiptImageViewButton;
	@FXML
	public AnchorPane codeLetterContentAnchorPane;
	private ObservableList<Image> helpImageList = FXCollections.observableArrayList();
	private TextField currentSelectedTextField;
	private Image addImage;
	private StringTemplate bilgeTemplate = new StringTemplate("$tankQuantity$ M3 REMOVED FROM $tankName$ OF CAPACITY $tankCapacity$ M3, $tankRetained$ M3 RETAINED IN TANK");
	private StringTemplate slopTemplate = new StringTemplate("$tankQuantity$ M3 ADDED TO $tankName$ OF CAPACITY $tankCapacity$ M3, $tankRetained$ M3 RETAINED IN TANK");
	private Pattern bilgePattern = Pattern.compile("(.{1,}) M3 REMOVED FROM (.{1,}) OF CAPACITY (.{1,}) M3, (.{1,}) M3 RETAINED IN TANK");
	private Pattern slopPattern = Pattern.compile("(.{1,}) M3 ADDED TO (.{1,}) OF CAPACITY (.{1,}) M3, (.{1,}) M3 RETAINED IN TANK");

	private DateTimeFormatter dateTimeFormatter = EasyDateUtils.logbookPdfDateTimeFormatter;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MathUtils.setStringConverter(quantityDischargedTransferredOrDisposedDoubleField);
		MathUtils.setStringConverter(tankQuantityFromBilgeDoubleField);
		MathUtils.setStringConverter(tankRetainedBilgeTankDoubleField);
		MathUtils.setStringConverter(tankRetainedSlopTankDoubleField);
		MathUtils.setStringConverter(tankQuantityToSlopDoubleField);
		MathUtils.setMinMaxValues(quantityDischargedTransferredOrDisposedDoubleField, 0.0, null);
		MathUtils.setMinMaxValues(tankRetainedBilgeTankDoubleField, 0.0, null);
		MathUtils.setMinMaxValues(tankQuantityFromBilgeDoubleField, 0.0, null);
		MathUtils.setMinMaxValues(tankQuantityToSlopDoubleField, 0.0, null);
		MathUtils.setMinMaxValues(tankRetainedSlopTankDoubleField, 0.0, null);
		
		timeOfDischargeStartLocalTime.setDateTimeFormatter(EasyDateUtils.logbookTimeFormatter);
		timeOfDischargeStopLocalTime.setDateTimeFormatter(EasyDateUtils.logbookTimeFormatter);
		toReceptionFacilitiesIdentifyPortTextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		JavaFXUtils.addFilledPseudoClass(quantityDischargedTransferredOrDisposedDoubleField);
		JavaFXUtils.addFilledPseudoClass(tankQuantityFromBilgeDoubleField);
		JavaFXUtils.addFilledPseudoClass(tankRetainedBilgeTankDoubleField);
		JavaFXUtils.addFilledPseudoClass(positionAtStartDischargeLatTextField);
		JavaFXUtils.addFilledPseudoClass(positionAtStartDischargeLongTextField);
		JavaFXUtils.addFilledPseudoClass(positionAtEndDischargeLatTextField);
		JavaFXUtils.addFilledPseudoClass(positionAtEndDischargeLongTextField);
		JavaFXUtils.addFilledPseudoClass(toReceptionFacilitiesIdentifyPortTextField);
		JavaFXUtils.addFilledPseudoClass(receiptTextField);
		JavaFXUtils.addFilledPseudoClass(tankRetainedSlopTankDoubleField);
		JavaFXUtils.addFilledPseudoClass(tankQuantityToSlopDoubleField);
		JavaFXUtils.addFilledPseudoClass(timeOfDischargeStartLocalTime);
		JavaFXUtils.addFilledPseudoClass(timeOfDischargeStopLocalTime);
		try {
			Image helpImageLetterD1 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_HELPD1));
		
			Image helpImageLetterD2 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_HELPD2));
			
			Image helpImageLetterD3 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_HELPD3));
			
			Image helpImageLetterD4 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_HELPD4));
			
			Image helpImageLetterD5 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_HELPD5));
			 
			helpImageList.addAll(helpImageLetterD1, helpImageLetterD2, helpImageLetterD3, helpImageLetterD4, helpImageLetterD5);
			
			Image infoImageLetterD = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
			imageViewButtonInfoLetterD.setImage(infoImageLetterD);

			Image infoImageReceptionFacilities = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
			imageViewButtonInfoReceptionFacilities.setImage(infoImageReceptionFacilities);

			Image infoImageStateQuantity = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
			imageViewButtonInfoStateQuantity15_1.setImage(infoImageStateQuantity);
//			imageViewButtonInfoStateQuantity15_2.setImage(infoImageStateQuantity);
//			imageViewButtonInfoStateQuantity15_3.setImage(infoImageStateQuantity);
			
			
			addImage = imageProvider
					.getImage(((WorkbenchResourceProvider) resourcesService)
							.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_ADD));
			quantityDischargedToTankImageViewButton.setImage(addImage);
			addSlopTankOrHoldingTankOrOtherTankImageViewButton.setImage(addImage);
			
			
			
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
			uploadReceiptImageViewButton.setOnMouseClicked(me -> {
				File selectedFile = null;
				try {
					selectedFile = fileChooser.showOpenDialog((Stage) ((ImageViewButton) me.getSource()).getScene().getWindow());
				} catch(IllegalArgumentException e) {
					fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
					selectedFile = fileChooser.showOpenDialog((Stage) ((ImageViewButton) me.getSource()).getScene().getWindow());
				}
				if(selectedFile!=null) {
					properties.setProperty("logbookReceiptDir", selectedFile.getParent());
					File f = new File(CACHE_FILE_NAME);
					if(!f.exists()) f.getParentFile().mkdirs();
					OutputStream out;
					try {
						out = new FileOutputStream(f);
						properties.store(out, "Last directory choosed by user");
						out.close();
					} catch (IOException ex) {
						logger.error("Last directory choosed by user " + ex.toString());
					}
					receiptTextField.setText(selectedFile.getName());
					receiptTextField.setUserData(selectedFile);
				}
			});
			CoordinateUIUtils.initializeLatitudeField(positionAtStartDischargeLatTextField);
			CoordinateUIUtils.initializeLongitudeField(positionAtStartDischargeLongTextField);
			CoordinateUIUtils.initializeLatitudeField(positionAtEndDischargeLatTextField);
			CoordinateUIUtils.initializeLongitudeField(positionAtEndDischargeLongTextField);

		} catch (IOException e) {
			e.printStackTrace();
		}
		quantityDischargedTransferredCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					engineRoomBigleWellsCheckBox.setSelected(false);
					engineRoomBigleWellsCheckBox.setDisable(false);
					bilgeTanksCheckBox.setSelected(false);
					bilgeTanksCheckBox.setDisable(false);
					transferType.getToggles().forEach(toggle -> ((RadioButton)toggle).setDisable(false));
					timeOfDischargeCheckBox.setSelected(true);
					methodOfDischargeTransferCheckBox.setSelected(true);
				} else {
					methodOfDischargeTransferCheckBox.setSelected(false);
					timeOfDischargeCheckBox.setSelected(false);
					engineRoomBigleWellsCheckBox.setSelected(false);
					engineRoomBigleWellsCheckBox.setDisable(true);
					bilgeTanksCheckBox.setSelected(false);
					bilgeTanksCheckBox.setDisable(true);
					quantityDischargedToTankImageViewButton.setDisable(true);
					addBilgeTankButton.setDisable(true);
					tankQuantityFromBilgeDoubleField.setValue(null);
					tankQuantityFromBilgeDoubleField.setDisable(true);
					tankRetainedBilgeTankDoubleField.clear();
					tankRetainedBilgeTankDoubleField.setDisable(true);
					tankNameFromTanksJFXChipView.setDisable(true);
					transferType.getToggles().forEach(toggle -> {
						toggle.setSelected(false);
						((RadioButton)toggle).setDisable(true);
					});
					tankNameFromTanksJFXChipView.getChips().clear();
					
					bilgeGridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node)!=null && GridPane.getRowIndex(node) > 0);
					bilgeGridPane.getRowConstraints().removeIf(rowConstraint -> rowConstraint != bilgeGridPane.getRowConstraints().get(0));
					
					quantityDischargedTransferredOrDisposedDoubleField.setVisible(false);
					bilgeWellsOfLabel.setVisible(false);
					bilgeWellsM3Label.setVisible(false);
					quantityDischargedToTankImageViewButton.setVisible(false);
					tankNameFromTanksJFXChipView.setVisible(false);
					addBilgeTankButton.setVisible(false);
					tankQuantityFromBilgeDoubleField.setVisible(false);
					tankRetainedBilgeTankDoubleField.setVisible(false);
					bilgeTanksRetainedLabel.setVisible(false);
					bilgeTanksM3Label.setVisible(false);
					bilgeTanksQuantityLabel.setVisible(false);
					bilgeTanksM3_2Label.setVisible(false);
					identifyTheTankShellHBox.setVisible(false);
				}
			}

		});

		transferType.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				if(newValue != null) {
					if(newValue.equals(dischargedOrDisposedToggle)) {
						if(methodOfDischargeTransferCheckBox.isSelected()) {
							positionAtStartDischargeCheckBox.setDisable(false);
							toReceptionFacilitiesIdentifyCheckBox.setDisable(false);
							toSlopTankCheckBox.setDisable(true);
							toSlopTankCheckBox.setSelected(false);
						}
					} else if(newValue.equals(transferredToggle)) {
						if(methodOfDischargeTransferCheckBox.isSelected()) {
							toSlopTankCheckBox.setDisable(false);
							positionAtStartDischargeCheckBox.setDisable(true);
							positionAtStartDischargeCheckBox.setSelected(false);
							toReceptionFacilitiesIdentifyCheckBox.setDisable(true);
							toReceptionFacilitiesIdentifyCheckBox.setSelected(false);
						}
					}
				}
			}
		});


		bilgeTanksCheckBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
			if(quantityDischargedTransferredCheckBox.isSelected()) {
				if (isNowSelected) {
					quantityDischargedTransferredOrDisposedDoubleField.setDisable(true);
					quantityDischargedTransferredOrDisposedDoubleField.setValue(null);
					quantityDischargedToTankImageViewButton.setDisable(false);
					tankNameFromTanksJFXChipView.setDisable(false);
					addBilgeTankButton.setDisable(false);
					tankQuantityFromBilgeDoubleField.setDisable(false);
					tankRetainedBilgeTankDoubleField.setDisable(false);
					
					quantityDischargedTransferredOrDisposedDoubleField.setVisible(false);
					bilgeWellsOfLabel.setVisible(false);
					bilgeWellsM3Label.setVisible(false);
					quantityDischargedToTankImageViewButton.setVisible(true);
					tankNameFromTanksJFXChipView.setVisible(true);
					addBilgeTankButton.setVisible(true);
					tankQuantityFromBilgeDoubleField.setVisible(true);
					tankRetainedBilgeTankDoubleField.setVisible(true);
					bilgeTanksRetainedLabel.setVisible(true);
					bilgeTanksM3Label.setVisible(true);
					bilgeTanksQuantityLabel.setVisible(true);
					bilgeTanksM3_2Label.setVisible(true);
					identifyTheTankShellHBox.setVisible(true);
					Platform.runLater(() -> {
						tankRetainedBilgeTankDoubleField.clear();
					});
				}
				engineRoomBigleWellsCheckBox.setSelected(!isNowSelected);
			}
		});
		engineRoomBigleWellsCheckBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
			if(quantityDischargedTransferredCheckBox.isSelected()) {
				if (isNowSelected) {
					quantityDischargedTransferredOrDisposedDoubleField.setDisable(false);
					quantityDischargedToTankImageViewButton.setDisable(true);
					tankNameFromTanksJFXChipView.setDisable(true);
					tankNameFromTanksJFXChipView.getChips().clear();
					bilgeGridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node)!=null && GridPane.getRowIndex(node) > 0);
					bilgeGridPane.getRowConstraints().removeIf(rowConstraint -> rowConstraint != bilgeGridPane.getRowConstraints().get(0));
					addBilgeTankButton.setDisable(true);
					tankQuantityFromBilgeDoubleField.setValue(null);
					tankQuantityFromBilgeDoubleField.setDisable(true);
					Platform.runLater(() -> {
						tankRetainedBilgeTankDoubleField.clear();
					});
					tankRetainedBilgeTankDoubleField.setDisable(true);
					
					
					quantityDischargedTransferredOrDisposedDoubleField.setVisible(true);
					bilgeWellsOfLabel.setVisible(true);
					bilgeWellsM3Label.setVisible(true);
					quantityDischargedToTankImageViewButton.setVisible(false);
					tankNameFromTanksJFXChipView.setVisible(false);
					addBilgeTankButton.setVisible(false);
					tankQuantityFromBilgeDoubleField.setVisible(false);
					tankRetainedBilgeTankDoubleField.setVisible(false);
					bilgeTanksRetainedLabel.setVisible(false);
					bilgeTanksM3Label.setVisible(false);
					bilgeTanksQuantityLabel.setVisible(false);
					bilgeTanksM3_2Label.setVisible(false);
					identifyTheTankShellHBox.setVisible(false);
				}
				bilgeTanksCheckBox.setSelected(!isNowSelected);
			}
		});
		timeOfDischargeCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {	
				if(newValue) {
					timeOfDischargeStartLocalTime.setDisable(false);
					timeOfDischargeStopLocalTime.setDisable(false);
				} else {
					timeOfDischargeStartLocalTime.setDisable(true);
					timeOfDischargeStopLocalTime.setDisable(true);
				}
				timeOfDischargeStartLocalTime.setLocalTime(null);
				timeOfDischargeStopLocalTime.setLocalTime(null);
			}

		});
		methodOfDischargeTransferCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					if(dischargedOrDisposedToggle.isSelected()) {
						positionAtStartDischargeCheckBox.setDisable(false);
						toReceptionFacilitiesIdentifyCheckBox.setDisable(false);
					} else if(transferredToggle.isSelected()) {
						toSlopTankCheckBox.setDisable(false);
					}
				} else {
					positionAtStartDischargeCheckBox.setSelected(false);
					positionAtStartDischargeCheckBox.setDisable(true);
					toReceptionFacilitiesIdentifyCheckBox.setSelected(false);
					toReceptionFacilitiesIdentifyCheckBox.setDisable(true);
					toSlopTankCheckBox.setSelected(false);
					toSlopTankCheckBox.setDisable(true);
				}
			}

		});
		positionAtStartDischargeCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					if(methodOfDischargeTransferCheckBox.isSelected()) {
						toReceptionFacilitiesIdentifyCheckBox.setSelected(false);
						toReceptionFacilitiesIdentifyCheckBox.setDisable(true);
					}
					positionAtStartDischargeLatTextField.setDisable(false);
					positionAtStartDischargeLongTextField.setDisable(false);
					positionAtEndDischargeLatTextField.setDisable(false);
					positionAtEndDischargeLongTextField.setDisable(false);
				} else {
					if(methodOfDischargeTransferCheckBox.isSelected()) {
						toReceptionFacilitiesIdentifyCheckBox.setDisable(false);
					}
					positionAtStartDischargeLatTextField.setDisable(true);
					positionAtStartDischargeLongTextField.setDisable(true);
					positionAtEndDischargeLatTextField.setDisable(true);
					positionAtEndDischargeLongTextField.setDisable(true);
				}
				positionAtStartDischargeLatTextField.setText("00°00.00'N");
				positionAtStartDischargeLongTextField.setText("000°00.00'W");
				positionAtEndDischargeLatTextField.setText("00°00.00'N");
				positionAtEndDischargeLongTextField.setText("000°00.00'W");
			}

		});
		toReceptionFacilitiesIdentifyCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {				
				if(newValue) {
					if(methodOfDischargeTransferCheckBox.isSelected()) {
						positionAtStartDischargeCheckBox.setSelected(false);
						positionAtStartDischargeCheckBox.setDisable(true);
					}
					toReceptionFacilitiesIdentifyPortTextField.setDisable(false);
					receiptTextField.setDisable(false);
					uploadReceiptImageViewButton.setDisable(false);
				} else {
					if(methodOfDischargeTransferCheckBox.isSelected()) {
						positionAtStartDischargeCheckBox.setDisable(false);
					}
					toReceptionFacilitiesIdentifyPortTextField.setText("");
					toReceptionFacilitiesIdentifyPortTextField.setDisable(true);
					receiptTextField.clear();
					receiptTextField.setDisable(true);
					uploadReceiptImageViewButton.setDisable(true);
				}
			}

		});
		
		toSlopTankCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					tankNameToTanksJFXChipView.setDisable(false);
					addSlopTankOrHoldingTankOrOtherTankImageViewButton.setDisable(false);
					tankQuantityToSlopDoubleField.setDisable(false);
					tankRetainedSlopTankDoubleField.setDisable(false);
					addSlopTankButton.setDisable(false);

					tankNameToTanksJFXChipView.getChips().clear();
					tankQuantityToSlopDoubleField.clear();
					
					if(engineRoomBigleWellsCheckBox.isSelected()) {
						tankQuantityToSlopDoubleField.setValue(quantityDischargedTransferredOrDisposedDoubleField.getValue());
					} else if(bilgeTanksCheckBox.isSelected()){
						Double totalTankQuantity = 0.0;
						for(Node node : bilgeGridPane.getChildren()) {
							if(node instanceof DoubleField && ((DoubleField)node).getId().startsWith("tankQuantity")) {
								totalTankQuantity += Optional.ofNullable(((DoubleField)node).getValue()).orElse(0.0);
							}
						}
						tankQuantityToSlopDoubleField.setValue(totalTankQuantity);
					}
				} else {
					tankNameToTanksJFXChipView.setDisable(true);
					addSlopTankOrHoldingTankOrOtherTankImageViewButton.setDisable(true);
					tankQuantityToSlopDoubleField.setDisable(true);
					tankRetainedSlopTankDoubleField.setDisable(true);
					addSlopTankButton.setDisable(true);
					dSlopGridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node)!=null && GridPane.getRowIndex(node) > 0);
					dSlopGridPane.getRowConstraints().removeIf(rowConstraint -> rowConstraint != dSlopGridPane.getRowConstraints().get(0));
	
					tankNameToTanksJFXChipView.getChips().clear();
					tankQuantityToSlopDoubleField.clear();
					
			        Platform.runLater(() -> {
			    		tankRetainedSlopTankDoubleField.clear();
			        });
				}
			}
		});
		
		tankRetainedSlopTankDoubleField.valueProperty().addListener(new ChangeListener<Double>() {
			@Override
			public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
				Double totalUserTankCapacity = ((TankUsageContext)tankNameToTanksJFXChipView.getUserData()).getTotalUserTankCapacity();
				Double totalUserTankActualLevel = ((TankUsageContext)tankNameToTanksJFXChipView.getUserData()).getTotalUserTankActualLevel();
				if(!tankNameToTanksJFXChipView.getChips().isEmpty()) {
					if(newValue!=null && newValue > totalUserTankCapacity) {
						OilRecordBookPart1ViewController.showWarning("Invalid value:" + newValue, "Valid value range:0.0-" + totalUserTankCapacity);
						tankQuantityToSlopDoubleField.setValue(totalUserTankCapacity-totalUserTankActualLevel);
					} else {
						if(newValue!=null && newValue > totalUserTankCapacity*0.98) {
							OilRecordBookPart1ViewController.showWarning("Value:" + newValue, "Is greater than 98% of max value (" + totalUserTankCapacity +")!");
						}
					}
				}
			}
		});
//		toSlopTankOrHoldingTankOrOtherTanksJFXChipView.getChips().addListener(new ListChangeListener<String>() {
//			@Override
//			public void onChanged(Change<? extends String> c) {
//				Platform.runLater(() -> {
//					double sum = ((TankUsageContext) toSlopTankOrHoldingTankOrOtherTanksJFXChipView.getUserData()).getTotalUserTankCapacity();
//					forCapacityOfDoubleField.setValue(sum);
//				});
//			}
//		});
//		addRemoveListener(toSlopTankOrHoldingTankOrOtherTanksJFXChipView);
		
		
		
//		stateQuantity3DoubleField.focusedProperty().addListener(new ChangeListener<Boolean>() {
//			@Override
//			public void changed(ObservableValue observable, Boolean oldValue, Boolean newValue) {
//				if(newValue) {
//					Double lastEntry = 0.0;
//					OilLog oilLog1 = operationLogService.getLastOil1CodeC11_3Log();
//					OilLog oilLog2 = operationLogService.getLastOil1CodeC12_1Log();
//					if(oilLog1!=null) {
//						String quantityStr = oilLog1.getContent().substring(0, oilLog1.getContent().lastIndexOf(" M3"));
//						lastEntry =+ Double.parseDouble(quantityStr);
//					}
//					if(oilLog2!=null) {
//						String quantityStr = oilLog1.getContent().substring(0, oilLog2.getContent().lastIndexOf(" M3"));
//						lastEntry =+ Double.parseDouble(quantityStr);
//					}
//					String message = "Last entry\t\t\t\t\t= " + lastEntry + " M3\n" +
//									"Actual entry\t\t\t\t= " + (stateQuantity3DoubleField.getValue()!=null?stateQuantity3DoubleField.getValue():0.0) + " M3\n" +
//									"Total quantity of retention\t= " + (lastEntry + (stateQuantity3DoubleField.getValue()!=null?stateQuantity3DoubleField.getValue():0.0)) + " M3";
//					if(lastEntry>0)
//						ballonMessage.showGeneralBalloon(stateQuantity3DoubleField, new Label(message));
//				} else {
//					ballonMessage.closeBalloon();
//				}	
//			}
//		});
		
		addBilgeTankButton.setOnMouseEntered(mouseEvent -> {if(!mouseEvent.isPrimaryButtonDown()) addBilgeTankButton.setCursor(Cursor.HAND);});
		addBilgeTankButton.setOnMouseExited(mouseEvent -> {if(!mouseEvent.isPrimaryButtonDown()) addBilgeTankButton.setCursor(Cursor.DEFAULT);});

		addSlopTankButton.setOnMouseEntered(mouseEvent -> {if(!mouseEvent.isPrimaryButtonDown()) addSlopTankButton.setCursor(Cursor.HAND);});
		addSlopTankButton.setOnMouseExited(mouseEvent -> {if(!mouseEvent.isPrimaryButtonDown()) addSlopTankButton.setCursor(Cursor.DEFAULT);});

		TankUsageContext tuc = new TankUsageContext(Stream.of(TankType.OILYBILGE).collect(Collectors.toList()), false, false);
		tuc.setNoNeedForUserTankQuantities(true);
		tankNameFromTanksJFXChipView.setUserData(tuc);
		tankNameFromTanksJFXChipView.getChips().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> change) {
		        while (change.next()) {
		            for (String item : change.getAddedSubList()) {
		            	if(tankNameFromTanksJFXChipView.getChips().size()>1) {
		            		String oldChip = tankNameFromTanksJFXChipView.getChips().get(0);
		            		Platform.runLater(() -> tankNameFromTanksJFXChipView.getChips().remove(oldChip));
		            	}
		            }
		        }
		        Platform.runLater(() -> {	
		        	Double totalUserTankQuantities = ((TankUsageContext)tankNameFromTanksJFXChipView.getUserData()).getTotalUserTankActualLevel();
		        	tankRetainedBilgeTankDoubleField.setValue(totalUserTankQuantities - Optional.ofNullable(tankQuantityFromBilgeDoubleField.getValue()).orElse(0.0));
		        });
		        JavaFXUtils.addTooltip(tankNameFromTanksJFXChipView);
			}
		});
		tankQuantityFromBilgeDoubleField.valueProperty().addListener(new ChangeListener<Double>() {
			@Override
			public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
				Double totalUserTankQuantities = ((TankUsageContext)tankNameFromTanksJFXChipView.getUserData()).getTotalUserTankActualLevel();
				tankRetainedBilgeTankDoubleField.setValue(totalUserTankQuantities - Optional.ofNullable(newValue).orElse(0.0));
				if(totalUserTankQuantities - Optional.ofNullable(newValue).orElse(0.0) < 0) {
					OilRecordBookPart1ViewController.showWarning("Invalid value:" + newValue, "Valid value range:0.0-" + totalUserTankQuantities);
					tankQuantityFromBilgeDoubleField.setValue(totalUserTankQuantities);
				} else {
					tankRetainedBilgeTankDoubleField.setValue(totalUserTankQuantities - Optional.ofNullable(newValue).orElse(0.0));
				}
			}
		});
		addRemoveListener(tankNameFromTanksJFXChipView);
		
		TankUsageContext tuc2 = new TankUsageContext(Stream.of(TankType.OILYBILGE, TankType.SLOP).collect(Collectors.toList()), true, false);
		tuc2.setNoNeedForUserTankQuantities(true);
		tankNameToTanksJFXChipView.setUserData(tuc2);
		tankNameToTanksJFXChipView.getChips().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> change) {
		        while (change.next()) {
		            for (String item : change.getAddedSubList()) {
		            	if(tankNameToTanksJFXChipView.getChips().size()>1) {
		            		String oldChip = tankNameToTanksJFXChipView.getChips().get(0);
		            		Platform.runLater(() -> tankNameToTanksJFXChipView.getChips().remove(oldChip));
		            	}
		            }
		        }
		        Platform.runLater(() -> {
					Double totalUserTankQuantities = ((TankUsageContext)tankNameToTanksJFXChipView.getUserData()).getTotalUserTankActualLevel();
					tankRetainedSlopTankDoubleField.setValue(totalUserTankQuantities + Optional.ofNullable(tankQuantityToSlopDoubleField.getValue()).orElse(0.0));
		        });
		        JavaFXUtils.addTooltip(tankNameFromTanksJFXChipView);
			}
		});
		tankQuantityToSlopDoubleField.valueProperty().addListener(new ChangeListener<Double>() {
			@Override
			public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
				Double totalUserTankQuantities = ((TankUsageContext)tankNameToTanksJFXChipView.getUserData()).getTotalUserTankActualLevel();
				tankRetainedSlopTankDoubleField.setValue(totalUserTankQuantities + Optional.ofNullable(newValue).orElse(0.0));
			}
		});
		addRemoveListener(tankNameToTanksJFXChipView);
		
		
		eventBroker.subscribe(Activator.MARPOL_DETAILS_RELOADED, new org.osgi.service.event.EventHandler() {
			@Override
			public void handleEvent(org.osgi.service.event.Event event) {
				ArrayList<Long> changedTankIds = (ArrayList<Long>) event.getProperty(Activator.TANK_IDS);
    			if(changedTankIds==null) {
    				tankNameFromTanksJFXChipView.getChips().clear();
    				tankNameToTanksJFXChipView.getChips().clear();
    			} else if(changedTankIds!=null && !changedTankIds.isEmpty()) {
    				Activator.removeChangedTanksFromJFXChipView(changedTankIds, tankNameFromTanksJFXChipView);
    				Activator.removeChangedTanksFromJFXChipView(changedTankIds, tankNameToTanksJFXChipView);
    			}
			}
		});
		
		GridPane.setRowIndex(tankNameToTanksJFXChipView, 0);
		GridPane.setRowIndex(tankQuantityFromBilgeDoubleField, 0);
		GridPane.setRowIndex(tankRetainedBilgeTankDoubleField, 0);
		GridPane.setRowIndex(tankNameFromTanksJFXChipView, 0);
		GridPane.setRowIndex(tankQuantityToSlopDoubleField, 0);
		GridPane.setRowIndex(tankRetainedSlopTankDoubleField, 0);
	}

	public void processD(OilLog logToInsert, List<OilLog> logToInsertList, Map<Validation, Object> validations) {
		
		validateFields(logToInsert.getBusinessDate(), validations);
		
		if (quantityDischargedTransferredCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			Map<Tank, Double> userTankActualLevels = new HashMap<Tank, Double>();
			log.setCodeNumber("13");
			if(engineRoomBigleWellsCheckBox.isSelected()) log.setContent(quantityDischargedTransferredOrDisposedDoubleField.getValue() + " M3 BILGE WATER FROM ENGINE ROOM BILGE WELLS");
			else if(bilgeTanksCheckBox.isSelected()) {
				StringBuilder sb = new StringBuilder();
				List<Pair<String, Tank>> tankPairList = getTanks(bilgeGridPane, true);
				for(Pair<String, Tank> tank : tankPairList) {
					try {
						userTankActualLevels.put(tank.getValue(), MathUtils.decimalFormat.parse(parseTank(tank.getKey(), true).get("tankRetained")).doubleValue());
					} catch (ParseException e) {
						logger.debug(e.getMessage(), e);
					}
					if(sb.length()>0)sb.append("\n");
					sb.append(tank.getKey());
				}
				log.setContent(sb.toString());
				log.setUserTankActualLevels(userTankActualLevels);
			}
			log.setOrderInGroup("W");
			logToInsertList.add(log);
		}
		if (timeOfDischargeCheckBox.isSelected() && timeOfDischargeStartLocalTime.getLocalTime() != null && timeOfDischargeStopLocalTime.getLocalTime() != null) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("14");
			log.setContent("START: " + timeOfDischargeStartLocalTime.getLocalTime().format(EasyDateUtils.logbookTimeFormatter) + ", STOP: " + timeOfDischargeStopLocalTime.getLocalTime().format(EasyDateUtils.logbookTimeFormatter));
			log.setOrderInGroup("X");
			logToInsertList.add(log);
		}
		if (positionAtStartDischargeCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("15.1");
			log.setContent("POSITION AT START:" + positionAtStartDischargeLatTextField.getText() + " " + positionAtStartDischargeLongTextField.getText()+","
			+"\r\n"+ "POSITION AT STOP: " + positionAtEndDischargeLatTextField.getText() + " " + positionAtEndDischargeLongTextField.getText());
			log.setOrderInGroup("Y");
			logToInsertList.add(log);
		}
		if (toReceptionFacilitiesIdentifyCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("15.2");
			log.setContent(toReceptionFacilitiesIdentifyPortTextField.getText() + " - RECEIPT '" + receiptTextField.getText() + "'");
			if(receiptTextField.getUserData()!=null && ((File)receiptTextField.getUserData()).exists()) {
				log.setAttachmentName(((File)receiptTextField.getUserData()).getName());
				byte[] attachmentBytes = null;
				try {
					attachmentBytes = Files.readAllBytes(((File)receiptTextField.getUserData()).toPath());
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
				log.setAttachmentData(attachmentBytes);
			}
			log.setOrderInGroup("Z");
			logToInsertList.add(log);
		}
		if (toSlopTankCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("15.3");
			Map<Tank, Double> userTankActualLevels = new HashMap<Tank, Double>();
			StringBuilder sb = new StringBuilder();
			List<Pair<String, Tank>> tankPairList = getTanks(dSlopGridPane, false);
			for(Pair<String, Tank> tank : tankPairList) {
				try {
					userTankActualLevels.put(tank.getValue(), MathUtils.decimalFormat.parse(parseTank(tank.getKey(), false).get("tankRetained")).doubleValue());
				} catch (ParseException e) {
					logger.debug(e.getMessage(), e);
				}
				if(sb.length()>0) sb.append("\n");
				sb.append(tank.getKey());
			}
			log.setContent(sb.toString());
			log.setUserTankActualLevels(userTankActualLevels);
			log.setOrderInGroup("ZA");
			logToInsertList.add(log);
		}
	}

	private void validateFields(Date registrationDate, Map<Validation, Object> validations) {
		if (quantityDischargedTransferredCheckBox.isSelected()) {
			if(!engineRoomBigleWellsCheckBox.isSelected() && !bilgeTanksCheckBox.isSelected()) {
				validations.put(Validation.D13_BILGEWELLS_AND_BILGETANKS_EMPTY, null);
			} else if(engineRoomBigleWellsCheckBox.isSelected() && quantityDischargedTransferredOrDisposedDoubleField.getValue()==null) {
				validations.put(Validation.D13_QUANTITY_REQUIRED, null);
			} else if(bilgeTanksCheckBox.isSelected() && (tankQuantityFromBilgeDoubleField.getValue()==null || tankNameFromTanksJFXChipView.getChips().isEmpty())) {
				validations.put(Validation.D13_TANK_REQUIRED, null);
			}
		}
		if (timeOfDischargeCheckBox.isSelected() && (timeOfDischargeStartLocalTime.getLocalTime()==null || timeOfDischargeStopLocalTime.getLocalTime()==null)) {
			validations.put(Validation.D14_EMPTY, null);
		} else if(timeOfDischargeCheckBox.isSelected() && (timeOfDischargeStartLocalTime.getLocalTime().isAfter(timeOfDischargeStopLocalTime.getLocalTime()))) {
			validations.put(Validation.D14_START_AFTER_END, null);
		} else if(registrationDate != null && timeOfDischargeStartLocalTime.getLocalTime()!=null && timeOfDischargeStopLocalTime.getLocalTime()!=null) {
			LocalDateTime registrationLocalDateTime = Instant.ofEpochMilli(registrationDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime().plusDays(1);
//			if(registrationLocalDateTime.isBefore(timeOfDischargeStartLocalTime.getLocalTime()) || registrationLocalDateTime.isBefore(timeOfDischargeStopLocalTime.getLocalTime())) {
//				validations.put(Validation.D14_TIME_AFTER_REGISTRATION_DATE, null);
//			}
		}
		if (toReceptionFacilitiesIdentifyCheckBox.isSelected() && (toReceptionFacilitiesIdentifyPortTextField.getText().trim().isEmpty() || receiptTextField.getText().trim().isEmpty() || receiptTextField.getUserData()==null || !((File)receiptTextField.getUserData()).exists())) {
			validations.put(Validation.D15_2_FACILITIES_OR_RECEIPT_EMPTY, null);
		}
		if (toSlopTankCheckBox.isSelected() && (tankQuantityToSlopDoubleField.getValue()==null || tankNameToTanksJFXChipView.getChips().isEmpty())) {
			validations.put(Validation.D15_3_Tank_EMPTY, null);
		}
		
		if (!quantityDischargedTransferredCheckBox.isSelected()) {
			validations.put(Validation.D13_QUANTITY_REQUIRED, null);
		}
		if (!timeOfDischargeCheckBox.isSelected()) {
			validations.put(Validation.D14_REQUIRED, null);
		}
		if(!positionAtStartDischargeCheckBox.isSelected() && !toReceptionFacilitiesIdentifyCheckBox.isSelected() && !toSlopTankCheckBox.isSelected()) {
			validations.put(Validation.D15_REQUIRED, null);
		}

		if(bilgeTanksCheckBox.isSelected()) {
			validations.put(Validation.D13_C11_4_MAY_BE_REQUIRED, null);
			List<Pair<String, Tank>> bilgeTanks = getTanks(bilgeGridPane, true);
			List<Pair<String, Tank>> notNullTanksTankPairList = bilgeTanks.stream().filter(t -> t.getValue() != null).collect(Collectors.toList());
			List<Tank> duplicates = notNullTanksTankPairList.stream()
					  .collect(Collectors.groupingBy(p -> p.getValue(), Collectors.counting()))
					  .entrySet().stream()
					  .filter(n -> n.getValue() > 1)
					  .map(n -> n.getKey())
					  .collect(Collectors.toList());
			for(Tank duplicateTank : duplicates) {
				validations.put(Validation.D13_TANK_ALREADY_IN_LIST, duplicateTank.getName());
			}
		}
		
		if (toSlopTankCheckBox.isSelected()) {
			TankUsageContext tuc = ((TankUsageContext) tankNameToTanksJFXChipView.getUserData());
			if(tuc!=null && tuc.getUserTankQuantities()!=null && !tuc.getUserTankQuantities().isEmpty()) {
				Optional<Tank> tankOpt = tuc.getUserTankQuantities().keySet().stream().findFirst();
				tankOpt.ifPresent(tank -> {
					TankType type = tank.getType();
					if(type == TankType.CARGO || type == TankType.SLOP) {
						validations.put(Validation.D15_2_PartII_J_MAY_BE_REQUIRED, null);
					}
				});
			}
			
			List<Pair<String, Tank>> slopTank = getTanks(dSlopGridPane, false);
			List<Pair<String, Tank>> notNullTanksTankPairList = slopTank.stream().filter(t -> t.getValue() != null).collect(Collectors.toList());
			List<Tank> duplicates = notNullTanksTankPairList.stream()
					  .collect(Collectors.groupingBy(p -> p.getValue(), Collectors.counting()))
					  .entrySet().stream()
					  .filter(n -> n.getValue() > 1)
					  .map(n -> n.getKey())
					  .collect(Collectors.toList());
			for(Tank duplicateTank : duplicates) {
				validations.put(Validation.D15_3_TANK_ALREADY_IN_LIST, duplicateTank.getName());
			}
			
			Double quantityDischarged = 0.0;
			if (quantityDischargedTransferredCheckBox.isSelected()) {
				if(engineRoomBigleWellsCheckBox.isSelected()) {
					quantityDischarged = quantityDischargedTransferredOrDisposedDoubleField.getValue();
				} else if(bilgeTanksCheckBox.isSelected()) {
					quantityDischarged = getBilgeTanksQuantity();
				}
			}
			
			Double quantityTransferred = getSlopTanksQuantity();
			
			if(!(Math.abs(quantityDischarged - quantityTransferred) < MathUtils.DOUBLE_THRESHOLD)) {
				validations.put(Validation.D15_TRANSFERRED_QUANTITIES_NOT_EQUAL, new double[] {quantityDischarged, quantityTransferred});
			}
		}

		if(positionAtStartDischargeCheckBox.isSelected()) {
			Double throughput = marpolBean.getSettings().getBilgeWaterSeparatorThroughput();
			LocalTime startLocalTime = timeOfDischargeStartLocalTime.getLocalTime();
			LocalTime stopLocalTime = timeOfDischargeStopLocalTime.getLocalTime();
			if(throughput != null && startLocalTime != null && stopLocalTime != null) {
				Double quantityDischarged = 0.0;
				if (quantityDischargedTransferredCheckBox.isSelected()) {
					if(engineRoomBigleWellsCheckBox.isSelected()) {
						quantityDischarged = quantityDischargedTransferredOrDisposedDoubleField.getValue();
					} else if(bilgeTanksCheckBox.isSelected()) {
						quantityDischarged = getBilgeTanksQuantity();
					}
				}
				Double minHoursToDischarge = quantityDischarged/throughput;
				long hours = ChronoUnit.HOURS.between(startLocalTime, stopLocalTime);
				if(hours < minHoursToDischarge) {
					validations.put(Validation.D14_TIME_INTERVAL_SHORT, new double[] {quantityDischarged, throughput});
				}
			}
			if(positionAtStartDischargeLatTextField.getText().equals("00°00.00'N") || positionAtStartDischargeLongTextField.getText().equals("000°00.00'W")
					|| positionAtEndDischargeLatTextField.getText().equals("00°00.00'N") || positionAtEndDischargeLongTextField.getText().equals("000°00.00'W")) {
				validations.put(Validation.B15_1_LAT_LON_ZERO, null);
			}
		}
	}


	public void showBalloon5(MouseEvent event) {
		ballonMessage.showGeneralBalloon((ImageViewButton) event.getTarget(), label5);
	}

	public void showBalloon6(MouseEvent event) {
		ballonMessage.showGeneralBalloon((ImageViewButton) event.getTarget(), label6);
	}

	public void selectTanksBalastedName(ActionEvent e) {
		String value = ((Button) e.getSource()).getText();
		currentSelectedTextField.setText(value);
	}

	public void actionPerformedD(MouseEvent event) {
		helpCodeLetterDialog.showDialog((Stage)((ImageViewButton)event.getSource()).getScene().getWindow(),helpImageList);
	}

	

	public void restoreLogGroup(List<OilLog> logGroup) {
		if(logGroup==null || logGroup.isEmpty()) return;
		List<OilLog> codeBOilLogs = logGroup.stream().filter(c -> c.getCodeLetter().equals("D")).collect(Collectors.toList());
		for (OilLog oilLog : codeBOilLogs) {
			if(oilLog.getCorrectionNote()!=null && oilLog.getCorrectionNote().equals("Corrected")) continue;
			String value;
			switch (oilLog.getCodeNumber()) {
				case "13":
					quantityDischargedTransferredCheckBox.setSelected(true);
					value = oilLog.getContent();
					if(value.indexOf(" M3 Bilge Water from Engine Room Bilge Wells")>-1) {
						engineRoomBigleWellsCheckBox.setSelected(true);
						String quantity = value.substring(0, value.indexOf(" M3 Bilge Water from Engine Room Bilge Wells"));
						quantityDischargedTransferredOrDisposedDoubleField.setValue(Double.parseDouble(quantity));
					} else {
						bilgeTanksCheckBox.setSelected(true);
						String [] tanks = value.split("\n");
						int index = 0;
						for(String tank : tanks) {
							Map<String, String> map = parseTank(tank, true);
							if(index==0) {								
								Map<Tank, Double> userTanks = new HashMap<Tank, Double>();
								userTanks.put(getTankFromSettingsByName(map.get("tankName")), null);
								TankUsageContext tuc = new TankUsageContext();
								if(tankNameFromTanksJFXChipView.getUserData()==null) {
									tankNameFromTanksJFXChipView.setUserData(tuc);
								} else {
									tuc = (TankUsageContext) tankNameFromTanksJFXChipView.getUserData();
								}
								tuc.setNoNeedForUserTankQuantities(true);
								tuc.getUserTankQuantities().putAll(userTanks);
								
								tankNameFromTanksJFXChipView.getChips().add(map.get("tankName"));
								tankRetainedBilgeTankDoubleField.setValue(Double.parseDouble(map.get("tankRetained")));
								tankQuantityFromBilgeDoubleField.setValue(Double.parseDouble(map.get("tankQuantity")));
								index++;
							} else {
								Map<Tank, Double> userTanks = ((TankUsageContext) tankNameFromTanksJFXChipView.getUserData()).getUserTankQuantities();
								userTanks.put(getTankFromSettingsByName(map.get("tankName")), null);
								
								Map<String, Node> mapNodes = addTank(bilgeGridPane);
								((JFXChipView<String>)mapNodes.get("tankName")).getChips().add(map.get("tankName"));
								((DoubleField)mapNodes.get("tankRetained")).setValue(Double.parseDouble(map.get("tankRetained")));
								((DoubleField)mapNodes.get("tankQuantity")).setValue(Double.parseDouble(map.get("tankQuantity")));
								index++;
							}
						}
					}
					break;
				case "14":
					timeOfDischargeCheckBox.setSelected(true);
					value = oilLog.getContent().replace("Time start: ", "");
					String timeStart = value.substring(0, value.indexOf(" time stop:"));
					value = value.replace(timeStart+" time stop:", "");
					String timeStop = value;
					timeOfDischargeStartLocalTime.setLocalTime(LocalTime.parse(timeStart, EasyDateUtils.logbookTimeFormatter));
					timeOfDischargeStopLocalTime.setLocalTime(LocalTime.parse(timeStop, EasyDateUtils.logbookTimeFormatter));
//					log.setContent("Time start: " + timeOfDischargeStartLocalTime.getLocalTime().toString() + " time stop:" + timeOfDischargeStopLocalTime.getLocalTime().toString());
					break;
				case "15.1":
					dischargedOrDisposedToggle.setSelected(true);
					methodOfDischargeTransferCheckBox.setSelected(true);
					positionAtStartDischargeCheckBox.setSelected(true);
					value = oilLog.getContent().replace("Start position:", "");
					String lat = value.substring(0, value.indexOf(" "));
					value = value.replace(lat+" ", "");
					String lon = value.substring(0, value.indexOf(" Stop position: "));
					value = value.replace(lon+" Stop position: ", "");
					String lat2 = value.substring(0, value.indexOf(" "));
					String lon2 = value.replace(lat2+" ", "");
					positionAtStartDischargeLatTextField.setText(lat);
					positionAtStartDischargeLongTextField.setText(lon);
					positionAtEndDischargeLatTextField.setText(lat2);
					positionAtEndDischargeLongTextField.setText(lon2);
//					log.setContent("Start position:" + positionAtStartDischargeLatTextField.getText() + " " + positionAtStartDischargeLongTextField.getText()
//					+ " Stop position: " + positionAtEndDischargeLatTextField.getText() + " " + positionAtEndDischargeLongTextField.getText());
					break;
				case "15.2":
					dischargedOrDisposedToggle.setSelected(true);
					methodOfDischargeTransferCheckBox.setSelected(true);
					toReceptionFacilitiesIdentifyCheckBox.setSelected(true);
					value = oilLog.getContent();
					toReceptionFacilitiesIdentifyPortTextField.setText(value);
//					log.setContent(toReceptionFacilitiesIdentifyPortTextField.getText());
					break;
				case "15.3":
					transferredToggle.setSelected(true);
					methodOfDischargeTransferCheckBox.setSelected(true);
					toSlopTankCheckBox.setSelected(true);
					value = oilLog.getContent();
					String [] tanks = value.split("\n");
					int index = 0;
					for(String tank : tanks) {
						Map<String, String> map = parseTank(tank, false);
						if(index==0) {						
							tankNameToTanksJFXChipView.getChips().add(map.get("tankName"));
							tankRetainedSlopTankDoubleField.setValue(Double.parseDouble(map.get("tankRetained")));
							tankQuantityToSlopDoubleField.setValue(Double.parseDouble(map.get("tankQuantity")));
							
							Map<Tank, Double> userTanks = new HashMap<Tank, Double>();
							userTanks.put(getTankFromSettingsByName(map.get("tankName")), null);
							TankUsageContext tuc1 = new TankUsageContext();
							if(tankNameToTanksJFXChipView.getUserData()==null) {
								tankNameToTanksJFXChipView.setUserData(tuc1);
							} else {
								tuc1 = (TankUsageContext) tankNameToTanksJFXChipView.getUserData();
							}
							tuc1.setNoNeedForUserTankQuantities(true);
							tuc1.getUserTankQuantities().putAll(userTanks);
							index++;
						} else {							
							Map<String, Node> mapNodes = addTank(dSlopGridPane);
							((JFXChipView<String>)mapNodes.get("tankName")).getChips().add(map.get("tankName"));
							((DoubleField)mapNodes.get("tankRetained")).setValue(Double.parseDouble(map.get("tankRetained")));
							((DoubleField)mapNodes.get("tankQuantity")).setValue(Double.parseDouble(map.get("tankQuantity")));
							
							List<Tank> userTanks = new ArrayList<Tank>();
							userTanks.add(getTankFromSettingsByName(map.get("tankName")));
							((JFXChipView<String>)mapNodes.get("tankName")).setUserData(userTanks);
							index++;
						}
					}
//					log.setContent(toSlopTankOrHoldingTankOrOtherTanksTextField.getText()+ " " +forCapacityOfTextField.getText() + " M3"+ " " +stateQuantityTextField.getText() + " M3");
					break;
				default:
					break;
			}
		}
	}

	private Tank getTankFromSettingsByName(String tankName) {
		Tank tank = operationLogService.getTankByName(tankName);
		if(tank==null) logger.error("Not found SlopTank with name " + tankName);
		return tank;
	}


	public void clearFilledInfo() {
		quantityDischargedTransferredCheckBox.setSelected(false);
		timeOfDischargeCheckBox.setSelected(false);
		methodOfDischargeTransferCheckBox.setSelected(false);
		positionAtStartDischargeCheckBox.setSelected(false);
		toReceptionFacilitiesIdentifyCheckBox.setSelected(false);
		toSlopTankCheckBox.setSelected(false);
		
		quantityDischargedTransferredOrDisposedDoubleField.clear();
		timeOfDischargeStartLocalTime.setLocalTime(null);
		timeOfDischargeStopLocalTime.setLocalTime(null);
		positionAtStartDischargeLatTextField.setText("00°00.00'N");
		positionAtStartDischargeLongTextField.setText("000°00.00'W");
		positionAtEndDischargeLatTextField.setText("00°00.00'N");
		positionAtEndDischargeLongTextField.setText("000°00.00'W");
		toReceptionFacilitiesIdentifyPortTextField.clear();
		receiptTextField.clear();
		receiptTextField.setUserData(null);
		tankNameFromTanksJFXChipView.getChips().clear();
		((TankUsageContext)tankNameFromTanksJFXChipView.getUserData()).getUserTankQuantities().clear();
		tankNameToTanksJFXChipView.getChips().clear();
		((TankUsageContext)tankNameToTanksJFXChipView.getUserData()).getUserTankQuantities().clear();
		tankQuantityToSlopDoubleField.clear();
		tankQuantityFromBilgeDoubleField.clear();
        Platform.runLater(() -> {
    		tankRetainedBilgeTankDoubleField.clear();
    		tankRetainedSlopTankDoubleField.clear();
        });

	}

	public void openSelectQuantityDischargedToTanks(MouseEvent e) {
		selectTankerDialog.showDialog((Stage)((ImageViewButton)e.getSource()).getScene().getWindow(), tankNameFromTanksJFXChipView);
	}
	public void openSelectToSlopTankOrHoldingTankOrOtherTanks(MouseEvent e) {
		selectTankerDialog.showDialog((Stage)((ImageViewButton)e.getSource()).getScene().getWindow(), tankNameToTanksJFXChipView);
	}
	
	private void addRemoveListener(JFXChipView<String> jFXChipView) {
		jFXChipView.getChips().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> change) {
		        while (change.next()) {
		            for (String item : change.getRemoved()) {
		            	Map<Tank, Double> userTanks = ((TankUsageContext) jFXChipView.getUserData()).getUserTankQuantities();
		            	if(userTanks!=null) userTanks.entrySet().removeIf(entry -> entry.getKey().getName().equals(item));
		            }
		        }
			}
		});
	}
	
	public void addBilgeTank(Event event) {
		Map<String, Node> map = addTank(bilgeGridPane);
		TankUsageContext tuc = new TankUsageContext(Stream.of(TankType.OILYBILGE).collect(Collectors.toList()), false, false);
		tuc.setNoNeedForUserTankQuantities(true);
		map.get("tankName").setUserData(tuc);
		
		JFXChipView<String> tankNameJFXChipView = (JFXChipView<String>) map.get("tankName");
		DoubleField tankFillVolume = (DoubleField) map.get("tankQuantity");
		DoubleField tankRetainedVolume = (DoubleField) map.get("tankRetained");
		tankNameJFXChipView.getChips().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> change) {
				Double totalUserTankQuantities = ((TankUsageContext)tankNameJFXChipView.getUserData()).getTotalUserTankActualLevel();
				tankRetainedVolume.setValue(totalUserTankQuantities - Optional.ofNullable(tankFillVolume.getValue()).orElse(0.0));
			}
		});
		tankFillVolume.valueProperty().addListener(new ChangeListener<Double>() {
			@Override
			public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
				Double totalUserTankQuantities = ((TankUsageContext)tankNameJFXChipView.getUserData()).getTotalUserTankActualLevel();
				if(totalUserTankQuantities - Optional.ofNullable(newValue).orElse(0.0) < 0) {
					OilRecordBookPart1ViewController.showWarning("Invalid value:" + newValue, "Valid value range:0.0-" + totalUserTankQuantities);
					tankFillVolume.setValue(totalUserTankQuantities);
				} else {
					tankRetainedVolume.setValue(totalUserTankQuantities - Optional.ofNullable(newValue).orElse(0.0));
				}
				
			}
		});
		eventBroker.subscribe(Activator.MARPOL_DETAILS_RELOADED, new org.osgi.service.event.EventHandler() {
			@Override
			public void handleEvent(org.osgi.service.event.Event event) {
				ArrayList<Long> changedTankIds = (ArrayList<Long>) event.getProperty(Activator.TANK_IDS);
    			if(changedTankIds==null) {
    				tankNameJFXChipView.getChips().clear();
    			} else if(changedTankIds!=null && !changedTankIds.isEmpty()) {
    				Activator.removeChangedTanksFromJFXChipView(changedTankIds, tankNameJFXChipView);
    			}
			}
		});
	}
	public void addSlopTank(Event event) {
		Map<String, Node> map = addTank(dSlopGridPane);
		TankUsageContext tuc = new TankUsageContext(Stream.of(TankType.OILYBILGE, TankType.SLOP).collect(Collectors.toList()), true, false);
		tuc.setNoNeedForUserTankQuantities(true);
		map.get("tankName").setUserData(tuc);
		
		JFXChipView<String> tankNameJFXChipView = (JFXChipView<String>) map.get("tankName");
		DoubleField tankFillVolume = (DoubleField) map.get("tankQuantity");
		DoubleField tankRetainedVolume = (DoubleField) map.get("tankRetained");
		tankNameJFXChipView.getChips().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> change) {
				Double totalUserTankQuantities = ((TankUsageContext)tankNameJFXChipView.getUserData()).getTotalUserTankActualLevel();
				tankRetainedVolume.setValue(totalUserTankQuantities - Optional.ofNullable(tankFillVolume.getValue()).orElse(0.0));
			}
		});
		tankFillVolume.valueProperty().addListener(new ChangeListener<Double>() {
			@Override
			public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
				Double totalUserTankQuantities = ((TankUsageContext)tankNameJFXChipView.getUserData()).getTotalUserTankActualLevel();
				tankRetainedVolume.setValue(totalUserTankQuantities + Optional.ofNullable(newValue).orElse(0.0));
			}
		});
		tankRetainedVolume.valueProperty().addListener(new ChangeListener<Double>() {
			@Override
			public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
				Double totalUserTankCapacity = ((TankUsageContext)tankNameJFXChipView.getUserData()).getTotalUserTankCapacity();
				Double totalUserTankActualLevel = ((TankUsageContext)tankNameJFXChipView.getUserData()).getTotalUserTankActualLevel();
				if(!tankNameJFXChipView.getChips().isEmpty()) {
					if(newValue!=null && newValue > totalUserTankCapacity) {
						OilRecordBookPart1ViewController.showWarning("Invalid value:" + newValue, "Valid value range:0.0-" + totalUserTankCapacity);
						tankFillVolume.setValue(totalUserTankCapacity-totalUserTankActualLevel);
					} else {
						if(newValue!=null && newValue > totalUserTankCapacity*0.98) {
							OilRecordBookPart1ViewController.showWarning("Value:" + newValue, "Is greater than 98% of max value (" + totalUserTankCapacity +")!");
						}
					}
				}
			}
		});
		
		eventBroker.subscribe(Activator.MARPOL_DETAILS_RELOADED, new org.osgi.service.event.EventHandler() {
			@Override
			public void handleEvent(org.osgi.service.event.Event event) {
				ArrayList<Long> changedTankIds = (ArrayList<Long>) event.getProperty(Activator.TANK_IDS);
    			if(changedTankIds==null) {
    				tankNameJFXChipView.getChips().clear();
    			} else if(changedTankIds!=null && !changedTankIds.isEmpty()) {
    				Activator.removeChangedTanksFromJFXChipView(changedTankIds, tankNameJFXChipView);
    			}
			}
		});
	}
	
	public Map<String, Node> addTank(GridPane tankGridPane) {
		JFXChipView<String> tankNameJFXChipView = new JFXChipView<String>();
		tankNameJFXChipView.getStyleClass().add("logbookjfx-chip-view");
		ImageViewButton addTankImageViewButton = new ImageViewButton(addImage);
		addTankImageViewButton.getStyleClass().add("image-view-button");
		addTankImageViewButton.getStyleClass().add("logbookButtonAddTank");
		GridPane.setHalignment(addTankImageViewButton, HPos.CENTER);
		addTankImageViewButton.setOnMouseClicked(new EventHandler<MouseEvent>() {				
			@Override
			public void handle(MouseEvent event) {
				selectTankerDialog.showDialog((Stage) ((ImageViewButton) event.getSource()).getScene().getWindow(), tankNameJFXChipView);				
			}
		});
		Button deleteButton = new Button("X");
		deleteButton.setStyle("-fx-background-color: #ef7f2e");
		deleteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Button deleteButton = (Button)event.getSource();
				GridPane parent = (GridPane)deleteButton.getParent();
				Integer rowIndex = GridPane.getRowIndex(deleteButton);
				parent.getChildren().removeIf(node -> GridPane.getRowIndex(node) == rowIndex);
				parent.getChildren().filtered(node -> GridPane.getRowIndex(node)!=null && GridPane.getRowIndex(node) > rowIndex).forEach(node -> GridPane.setRowIndex(node, GridPane.getRowIndex(node) - 1));
				parent.getRowConstraints().remove(parent.getRowConstraints().size() - 1);
			}
		});
		deleteButton.setOnMouseEntered(mouseEvent -> {if(!mouseEvent.isPrimaryButtonDown()) deleteButton.setCursor(Cursor.HAND);});
		deleteButton.setOnMouseExited(mouseEvent -> {if(!mouseEvent.isPrimaryButtonDown()) deleteButton.setCursor(Cursor.DEFAULT);});
		
		Integer rows = null;
		try {
			Method method = tankGridPane.getClass().getDeclaredMethod("getNumberOfRows");
			method.setAccessible(true);
			rows = (Integer) method.invoke(tankGridPane);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		Label quantityLabel = new Label("quantity"); GridPane.setHalignment(quantityLabel, HPos.RIGHT);
		quantityLabel.getStyleClass().add("logbookOtherLabel");
		quantityLabel.setPadding(new Insets(0, 5, 0, 10));
		Label retainedLabel = new Label("retained"); GridPane.setHalignment(retainedLabel, HPos.RIGHT);
		retainedLabel.getStyleClass().add("logbookOtherLabel");
		retainedLabel.setPadding(new Insets(0, 5, 0, 10));
		Label measureLabel1 = new Label("M3");
		measureLabel1.getStyleClass().add("logbookOtherLabel");
		measureLabel1.setPadding(new Insets(0, 10, 0, 5));
		Label measureLabel2 = new Label("M3"); GridPane.setHalignment(retainedLabel, HPos.LEFT);
		measureLabel2.getStyleClass().add("logbookOtherLabel");
		measureLabel2.setPadding(new Insets(0, 10, 0, 5));

		DoubleField tankRetainedVolume = new DoubleField();
		DoubleField tankFillVolume = new DoubleField();
		MathUtils.setStringConverter(tankRetainedVolume);
		MathUtils.setStringConverter(tankFillVolume);
		MathUtils.setMinMaxValues(tankRetainedVolume, 0.0, null);
		MathUtils.setMinMaxValues(tankFillVolume, 0.0, null);
		tankNameJFXChipView.setId("tankName"+rows);
		tankRetainedVolume.setId("tankRetained"+rows);
		tankFillVolume.setId("tankQuantity"+rows);
		tankRetainedVolume.getStyleClass().add("logbook-text-field");
		tankFillVolume.getStyleClass().add("logbook-text-field");
		JavaFXUtils.addFilledPseudoClass(tankRetainedVolume);
		JavaFXUtils.addFilledPseudoClass(tankFillVolume);
		
		Label identifyTheTankLabel = new Label("identify the tank");
		identifyTheTankLabel.getStyleClass().add("logbookOtherLabel");
		HBox identifyTheTankHBox = new HBox(identifyTheTankLabel);
		identifyTheTankHBox.setAlignment(Pos.CENTER);
		identifyTheTankHBox.getStyleClass().add("logbookOrangeLabel");
		identifyTheTankHBox.getStyleClass().add("logbook-label-hbox");
		Region arrowRegion = new Region();
		arrowRegion.getStyleClass().add("logbook-arrow-region");
		arrowRegion.getStyleClass().add("logbook-arrow-region-color-orange");
		HBox shellHBox = new HBox(identifyTheTankHBox, arrowRegion);
		shellHBox.setAlignment(Pos.CENTER_LEFT);

		tankGridPane.addRow(rows, new Region(), shellHBox, new Region(), addTankImageViewButton, new Region(), tankNameJFXChipView, quantityLabel, tankFillVolume, measureLabel1, retainedLabel, tankRetainedVolume, measureLabel2, deleteButton);
		tankGridPane.getRowConstraints().add(rows, new RowConstraints(80));
		
		tankNameJFXChipView.getChips().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> change) {
		        while (change.next()) {
		            for (String item : change.getAddedSubList()) {
		            	if(tankNameJFXChipView.getChips().size()>1) {
		            		String oldChip = tankNameJFXChipView.getChips().get(0);
		            		Platform.runLater(() -> tankNameJFXChipView.getChips().remove(oldChip));
		            	}
		            }
		        }
				Platform.runLater(() -> {
//					double sum = ((TankUsageContext) identityOfTanksLetterCJFXChipView.getUserData()).getTotalUserTankCapacity();
//					tankCapacity.setValue(sum);
				});
				JavaFXUtils.addTooltip(tankNameJFXChipView);
			}
		});
		addRemoveListener(tankNameJFXChipView);
		
		Map<String, Node> map = new HashMap<>();
		map.put("tankName", tankNameJFXChipView);
		map.put("tankRetained", tankRetainedVolume);
		map.put("tankQuantity", tankFillVolume);
		return map;
	}
	
	private Double getSlopTanksQuantity() {
		Double tanksQuantity = 0.0;
		for(Pair<String, Tank> tank : getTanks(dSlopGridPane, false)) {
			try {
				tanksQuantity = tanksQuantity + MathUtils.decimalFormat.parse(parseTank(tank.getKey(), false).get("tankQuantity")).doubleValue();
			} catch (ParseException e) {
				logger.debug(e.getMessage(), e);
			}
		}
		return tanksQuantity;
	}
	private Double getBilgeTanksQuantity() {
		Double tanksQuantity = 0.0;
		for(Pair<String, Tank> tank : getTanks(bilgeGridPane, true)) {
			try {
				tanksQuantity = tanksQuantity + MathUtils.decimalFormat.parse(parseTank(tank.getKey(), true).get("tankQuantity")).doubleValue();
			} catch (ParseException e) {
				logger.debug(e.getMessage(), e);
			}
		}
		return tanksQuantity;
	}
	private List<Pair<String, Tank>> getTanks(GridPane tanksGridPane, boolean bilge) {
		Map<Integer, List<Node>> tankNodesMap = new TreeMap<Integer, List<Node>>();
		for(Node childNode : tanksGridPane.getChildren()) {
			if(GridPane.getRowIndex(childNode) != null) {
				if(tankNodesMap.get(GridPane.getRowIndex(childNode)) != null) {
					tankNodesMap.get(GridPane.getRowIndex(childNode)).add(childNode);
				} else {
					List<Node> nodeList = new ArrayList<Node>();
					nodeList.add(childNode);
					tankNodesMap.put(GridPane.getRowIndex(childNode), nodeList);
					
				}
				
			}
			
		}
	

		StringTemplate template = bilge?bilgeTemplate:slopTemplate;
		List<Pair<String, Tank>> tankList = new ArrayList<Pair<String, Tank>>();
		for(List<Node> tankNodeList : tankNodesMap.values()) {
			template.reset();
			String name = "";
			Double capacity = 0.0;
			Double retained = 0.0;
			Double quantity = 0.0;
			Tank tank = null;
			boolean rowContainsTank = false;
			for(Node node : tankNodeList) {
				if(node instanceof JFXChipView<?> && node.getId()!=null && node.getId().startsWith("tankName")) {
					List<String> chipsList = ((JFXChipView<String>)node).getChips();
					if(!chipsList.isEmpty()) name = chipsList.get(0);
					template.setAttribute("tankName", name);
					Set<Tank> userTanks = ((TankUsageContext) ((JFXChipView<String>)node).getUserData()).getUserTankQuantities().keySet();
					if(userTanks==null) continue;
					if(!userTanks.isEmpty()) {
						tank = (Tank)userTanks.iterator().next();
						capacity = tank.getSize();
					}
					template.setAttribute("tankCapacity", capacity!=null?MathUtils.decimalFormat.format(capacity):null);
					rowContainsTank = true;
				}
				if(node instanceof DoubleField && node.getId()!=null && node.getId().startsWith("tankRetained")) {
					retained = ((DoubleField)node).getValue();
					template.setAttribute("tankRetained", retained!=null?MathUtils.decimalFormat.format(retained):null);
					rowContainsTank = true;
				}
				if(node instanceof DoubleField && node.getId()!=null && node.getId().startsWith("tankQuantity")) {
					quantity = ((DoubleField)node).getValue();
					template.setAttribute("tankQuantity", quantity!=null?MathUtils.decimalFormat.format(quantity):null);
					rowContainsTank = true;
				}
			}
			if(!rowContainsTank) continue;
			boolean rowInvalid = false;
			if(quantity==null) {rowInvalid = true; logger.error("quantity from tank " + name + " must have a value");};
			if(retained==null) {rowInvalid = true; logger.error("retained in tank " + name + " must have a value");};
			if(quantity!=null && retained!=null && capacity!=null && quantity>capacity) {rowInvalid = true; logger.error("quantity from tank " + name + " must be less than tank capacity");};
			if(rowInvalid) continue;
			tankList.add(new Pair<String, Tank>(template.toString(), tank));
		}
		return tankList;
	}
	
	
	private Map<String, String> parseTank(String tankString, boolean bilge) {
		Map<String, String> map = new HashMap<>();
		Pattern pattern = bilge?bilgePattern:slopPattern;
		Matcher matcher = pattern.matcher(tankString);
		if (matcher.find()) {
			map.put("tankQuantity", matcher.group(1));
			map.put("tankName", matcher.group(2));
			map.put("tankCapacity", matcher.group(3));
			map.put("tankRetained", matcher.group(4));
			logger.debug(matcher.group(1));
			logger.debug(matcher.group(2));
			logger.debug(matcher.group(3));
			logger.debug(matcher.group(3));
		} else {
			logger.debug("could not parse: " + tankString);
		}
		return map;
	}
}
