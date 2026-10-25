package al.sybina.ebr.logbook2.views;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.tools.resources.WorkbenchResourceProvider;
import org.eclipse.e4.tools.services.IResourceProviderService;
import org.eclipse.fx.ui.services.resources.ImageProvider;

import com.jfoenix.controls.JFXChipView;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.TankType;
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
import al.sybina.ebr.logbook2.CodeLetterDTankWashed;
import al.sybina.ebr.logbook2.views.OilRecordBookPart2ViewController.Validation;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import jfxtras.scene.control.ImageViewButton;
import jfxtras.scene.control.LocalTimeTextField;
import jidefx.scene.control.field.DoubleField;
import jidefx.scene.control.field.FormattedTextField;

public class OilRecordBookPart2CodeLetterDController implements Initializable {

	private Label label1 = new Label(" [1] When an individual tank has more machines that\r\n" + "can be operated simultaneously, as described in the\r\n" + "Operations and Equipment Manual, then the section\r\n"
			+ "being crude oil washed should be identified, e.g. No. 2\r\n" + "centre, forward section.\r\n" + "");
	private Label label2 = new Label("Enter ship position as Lat:__. Long:__\r\n" + "if COW operation carried out at sea.\r\n" + "");
	private Label label3 = new Label("[2] In accordance with the Operations and Equipment\r\n" + "Manual, enter whether single stage or multi-stage\r\n" + "method of washing is employed. If multi-stage method\r\n"
			+ "is used, give the vertical arc covered by the machines\r\n" + "and the number of times that arc is covered for that\r\n" + "particular stage of the programme.\r\n" + "");
	private Label label4 = new Label("[3] If the programmes given in the Operations and\r\n" + "Equipment Manual are not followed, then the \r\n" + "reasons must be given under Remarks.\r\n" + "");

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
	private ImageViewButton identityOfTanksImageViewButton;
	@FXML
	private ImageViewButton imageViewButtonInfoLetterD;
	@FXML
	private Button buttonAddTank;
	@FXML
	private CheckBox portWhereCrudeOilWashingCheckBox;
	@FXML
	private CheckBox shipPositionIfCarriedCheckBox;
	@FXML
	private CheckBox identityOfTankCheckBox;
	@FXML
	private CheckBox numberOfMachinesInUseCheckBox;
	@FXML
	private CheckBox timeOfStartOfWashingCheckBox;
	@FXML
	private CheckBox washing0PatternEmployedCheckBox;
	@FXML
	private CheckBox washingLinePressureCheckBox;
	@FXML
	private CheckBox timeWashingWasCompletedOrStoppedCheckBox;
	@FXML
	private CheckBox stateMethodOfEstablishingCheckBox;
	@FXML
	private CheckBox remarkCheckBox;
	@FXML
	private ImageViewButton shipPositionIfCarriedImageViewButton;
	@FXML
	private ImageViewButton identityOfTankWashedImageViewButton;
	@FXML
	private ImageViewButton washing0PatternEmployedImageViewButton;
	@FXML
	private ImageViewButton remarkImageViewButton;
	@FXML
	private TextField portWhereCrudeOilWashingTextField;
	@FXML
	private FormattedTextField shipPositionIfCarriedLatTextField;
	@FXML
	private FormattedTextField shipPositionIfCarriedLongTextField;
	@FXML
	private JFXChipView<String> identityOfTankJFXChipsView;
	@FXML
	private DoubleField numberOfMachinesInUseDoubleField;
	@FXML
	private LocalTimeTextField timeOfStartOfWashingTextField;
	@FXML
	private TextField washingPatternEmployedTextField;
	@FXML
	private ComboBox<String> washingPatternTypeComboBox;
	@FXML
	private ComboBox<String> identityTanksWashingComboBox;
	@FXML
	private DoubleField washingLinePressureValueDoubleField;
	@FXML
	private ComboBox<String> washingLinePressureTypeComboBox;
	@FXML
	private LocalTimeTextField timeWashingWasCompletedOrStoppedTextField;
	@FXML
	private TextField stateMethodOfEstablishingdTextField;
	@FXML
	private TextField remarkTextField;
	@FXML
	private GridPane tanksWashedGridPane;

	private Image identityOfTanksImage;
    @FXML
	public AnchorPane codeLetterContentAnchorPane;
    private ObservableList<Image> helpImageList = FXCollections.observableArrayList();
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MathUtils.setStringConverter(numberOfMachinesInUseDoubleField);
		MathUtils.setStringConverter(washingLinePressureValueDoubleField);
		MathUtils.setMinMaxValues(numberOfMachinesInUseDoubleField, 0.0, null);
		MathUtils.setMinMaxValues(washingLinePressureValueDoubleField, 0.0, null);
		try {
			Image helpImageLetterD2 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPD2));
			
			Image helpImageLetterD4 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPD4));
			
			Image helpImageLetterD3 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPD3));
			
			helpImageList.addAll(helpImageLetterD4,helpImageLetterD2, helpImageLetterD3);
			
			Image infoIconImageInfoLetterD = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
			imageViewButtonInfoLetterD.setImage(infoIconImageInfoLetterD);

			identityOfTanksImage = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_ADD));
			identityOfTanksImageViewButton.setImage(identityOfTanksImage);

			Image infoImageShipPositionIfCarried = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
			shipPositionIfCarriedImageViewButton.setImage(infoImageShipPositionIfCarried);

			Image infoImageIdentityOfTankWashed = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
			identityOfTankWashedImageViewButton.setImage(infoImageIdentityOfTankWashed);

			Image infoImageWashing0PatternEmployed = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
			washing0PatternEmployedImageViewButton.setImage(infoImageWashing0PatternEmployed);

			Image infoImageRemark = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
			remarkImageViewButton.setImage(infoImageRemark);
		} catch (IOException e) {
			e.printStackTrace();
		}

		CoordinateUIUtils.initializeLatitudeField(shipPositionIfCarriedLatTextField);
		CoordinateUIUtils.initializeLongitudeField(shipPositionIfCarriedLongTextField);
		portWhereCrudeOilWashingTextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		washingPatternEmployedTextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		stateMethodOfEstablishingdTextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		remarkTextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		JavaFXUtils.addFilledPseudoClass(portWhereCrudeOilWashingTextField);
		JavaFXUtils.addFilledPseudoClass(shipPositionIfCarriedLatTextField);
		JavaFXUtils.addFilledPseudoClass(shipPositionIfCarriedLongTextField);
		JavaFXUtils.addFilledPseudoClass(washingPatternEmployedTextField);
		JavaFXUtils.addFilledPseudoClass(numberOfMachinesInUseDoubleField);
		JavaFXUtils.addFilledPseudoClass(washingLinePressureValueDoubleField);
		JavaFXUtils.addFilledPseudoClass(stateMethodOfEstablishingdTextField);
		JavaFXUtils.addFilledPseudoClass(remarkTextField);
		JavaFXUtils.addFilledPseudoClass(timeOfStartOfWashingTextField);
		JavaFXUtils.addFilledPseudoClass(timeWashingWasCompletedOrStoppedTextField);
		timeOfStartOfWashingTextField.setDateTimeFormatter(EasyDateUtils.logbookTimeFormatter);
		timeWashingWasCompletedOrStoppedTextField.setDateTimeFormatter(EasyDateUtils.logbookTimeFormatter);
		washingPatternTypeComboBox.getItems().addAll("MULTI STAGE", "SINGLE STATE", "OTHER");
		washingPatternTypeComboBox.setValue("");
		washingLinePressureTypeComboBox.getItems().addAll("MPA", "BAR", "IBS/FT2");
		washingLinePressureTypeComboBox.setValue("");
		identityTanksWashingComboBox.getItems().addAll("FWD", "MID", "AFT");
		identityTanksWashingComboBox.setValue("");
		GridPane.setRowIndex((GridPane) tanksWashedGridPane.getChildren().get(0), 0);

		portWhereCrudeOilWashingCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				shipPositionIfCarriedCheckBox.setDisable(false);
				portWhereCrudeOilWashingTextField.setDisable(false);

				identityOfTankCheckBox.setSelected(true);
				numberOfMachinesInUseCheckBox.setSelected(true);
				timeOfStartOfWashingCheckBox.setSelected(true);
				washing0PatternEmployedCheckBox.setSelected(true);
				washingLinePressureCheckBox.setSelected(true);
				timeWashingWasCompletedOrStoppedCheckBox.setSelected(true);
				stateMethodOfEstablishingCheckBox.setSelected(true);
				buttonAddTank.setDisable(false);
				
			} else {
				shipPositionIfCarriedCheckBox.setSelected(false);
				shipPositionIfCarriedCheckBox.setDisable(true);
				portWhereCrudeOilWashingTextField.setDisable(true);
				buttonAddTank.setDisable(true);
			}
			portWhereCrudeOilWashingTextField.setText("");
		});
		shipPositionIfCarriedCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				shipPositionIfCarriedLatTextField.setDisable(false);
				shipPositionIfCarriedLongTextField.setDisable(false);
				portWhereCrudeOilWashingTextField.clear();
				portWhereCrudeOilWashingTextField.setDisable(true);
			} else {
				shipPositionIfCarriedLatTextField.setDisable(true);
				shipPositionIfCarriedLongTextField.setDisable(true);
				portWhereCrudeOilWashingTextField.setDisable(false);
			}
			shipPositionIfCarriedLatTextField.setText("00°00.00'N");
			shipPositionIfCarriedLongTextField.setText("000°00.00'W");
		});
		identityOfTankCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				identityOfTankJFXChipsView.setDisable(false);
				identityOfTanksImageViewButton.setDisable(false);
				identityTanksWashingComboBox.setDisable(false);
			} else {
				identityOfTankJFXChipsView.setDisable(true);
				identityOfTanksImageViewButton.setDisable(true);
				identityTanksWashingComboBox.setDisable(true);
				identityTanksWashingComboBox.setValue("");
			}
			identityOfTankJFXChipsView.getChips().clear();
		});
		numberOfMachinesInUseCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				numberOfMachinesInUseDoubleField.setDisable(false);
			} else {
				numberOfMachinesInUseDoubleField.setDisable(true);
			}
			numberOfMachinesInUseDoubleField.clear();
		});
		timeOfStartOfWashingCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				timeOfStartOfWashingTextField.setDisable(false);
			} else {
				timeOfStartOfWashingTextField.setDisable(true);
			}
			timeOfStartOfWashingTextField.setLocalTime(null);
		});
		washing0PatternEmployedCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				washingPatternEmployedTextField.setDisable(false);
				washingPatternTypeComboBox.setDisable(false);
			} else {
				washingPatternEmployedTextField.setDisable(true);
				washingPatternTypeComboBox.setDisable(true);
			}
			washingPatternEmployedTextField.setText("");
			washingPatternTypeComboBox.getSelectionModel().clearSelection();
		});
		washingLinePressureCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				washingLinePressureTypeComboBox.setDisable(false);
				washingLinePressureValueDoubleField.setDisable(false);
			} else {
				washingLinePressureTypeComboBox.setDisable(true);
				washingLinePressureValueDoubleField.setDisable(true);
			}
			washingLinePressureTypeComboBox.getSelectionModel().clearSelection();
			washingLinePressureValueDoubleField.clear();
		});
		timeWashingWasCompletedOrStoppedCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				timeWashingWasCompletedOrStoppedTextField.setDisable(false);
			} else {
				timeWashingWasCompletedOrStoppedTextField.setDisable(true);
			}
			timeWashingWasCompletedOrStoppedTextField.setLocalTime(null);
		});
		stateMethodOfEstablishingCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				stateMethodOfEstablishingdTextField.setDisable(false);
			} else {
				stateMethodOfEstablishingdTextField.setDisable(true);
			}
			stateMethodOfEstablishingdTextField.setText("");
		});
		remarkCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				remarkTextField.setDisable(false);
			} else {
				remarkTextField.setDisable(true);
			}
			remarkTextField.setText("");
		});

		TankUsageContext tankUsageContext = new TankUsageContext(Stream.of(TankType.BALLAST, TankType.CARGO, TankType.FUELOIL, TankType.OILYBILGE, TankType.SLOP, TankType.SLUDGE).collect(Collectors.toList()), true, false);
		tankUsageContext.setNoNeedForUserTankQuantities(true);
		identityOfTankJFXChipsView.setUserData(tankUsageContext);
		identityOfTankJFXChipsView.getChips().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> change) {
				while (change.next()) {
					for (String item : change.getAddedSubList()) {
						if (identityOfTankJFXChipsView.getChips().size() > 1) {
							String oldChip = identityOfTankJFXChipsView.getChips().get(0);
							Platform.runLater(() -> identityOfTankJFXChipsView.getChips().remove(oldChip));
						}
					}
				}
				JavaFXUtils.addTooltip(identityOfTankJFXChipsView);
			}
		});

		eventBroker.subscribe(Activator.MARPOL_DETAILS_RELOADED, new org.osgi.service.event.EventHandler() {
			@Override
			public void handleEvent(org.osgi.service.event.Event event) {
				ArrayList<Long> changedTankIds = (ArrayList<Long>) event.getProperty(Activator.TANK_IDS);
				if (changedTankIds == null) {
					identityOfTankJFXChipsView.getChips().clear();
				} else if (changedTankIds != null && !changedTankIds.isEmpty()) {
					Activator.removeChangedTanksFromJFXChipView(changedTankIds, identityOfTankJFXChipsView);
				}
			}
		});
	}

	public void processD(OilLog logToInsert, List<OilLog> logToInsertList, Map<Validation, Object> validations) {
		List<CodeLetterDTankWashed> cldtwList = getTanksWashed();
		validateFields(logToInsert.getBusinessDate(), validations, cldtwList);

		if (portWhereCrudeOilWashingCheckBox.isSelected() && !shipPositionIfCarriedCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("9");
			log.setOrderInGroup("J");
			log.setContent(portWhereCrudeOilWashingTextField.getText());
			logToInsertList.add(log);
		} else if (portWhereCrudeOilWashingCheckBox.isSelected() && shipPositionIfCarriedCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("9");
			log.setOrderInGroup("J");
			log.setContent("LAT:" + shipPositionIfCarriedLatTextField.getText() + " LON:" + shipPositionIfCarriedLongTextField.getText());
			logToInsertList.add(log);
		}

		cldtwList.sort(Comparator.comparing(CodeLetterDTankWashed::getIndex));

		StringBuilder tanks = new StringBuilder();
		StringBuilder numberOfMachinesInUse = new StringBuilder();
		StringBuilder timeOfStartOfWashing = new StringBuilder();
		StringBuilder washingPattern = new StringBuilder();
		StringBuilder washingLinePressure = new StringBuilder();
		StringBuilder timeWashingWasCompletedOrStopped = new StringBuilder();
		boolean first = true;
		for (CodeLetterDTankWashed cldtw : cldtwList) {
			if (!cldtw.getIdentityOfTankJFXChipsView().getChips().isEmpty()) {
				if (!first)
					tanks.append(", ");
				tanks.append(String.join(", ", cldtw.getIdentityOfTankJFXChipsView().getChips() + cldtw.getIdentityTanksWashing()));
			}
			if (cldtw.getNumberOfMachinesInUse() != null && cldtw.getNumberOfMachinesInUse() != 0.0) {
				if (!first)
					numberOfMachinesInUse.append(", ");
				numberOfMachinesInUse.append(cldtw.getNumberOfMachinesInUse());
			}
			if (cldtw.getTimeOfStartOfWashing() != null) {
				if (!first)
					timeOfStartOfWashing.append(", ");
				timeOfStartOfWashing.append(cldtw.getTimeOfStartOfWashing().format(EasyDateUtils.logbookTimeFormatter));
			}

			if (cldtw.getWashingPatternType() != null && !cldtw.getWashingPatternType().equals("") && cldtw.getWashingPatternEmployed() != null && !cldtw.getWashingPatternEmployed().equals("")) {
				if (!first)
					washingPattern.append(", ");
				washingPattern.append(cldtw.getWashingPatternType() + " " + cldtw.getWashingPatternEmployed());
			}
			if (cldtw.getWashingLinePressureValue() != null && cldtw.getWashingLinePressureValue() != 0.0 && cldtw.getWashingLinePressureType() != null && !cldtw.getWashingLinePressureType().equals("")) {
				if (!first)
					washingLinePressure.append(", ");
				washingLinePressure.append(cldtw.getWashingLinePressureValue() + " " + cldtw.getWashingLinePressureType());
			}

			if (cldtw.getTimeWashingWasCompletedOrStopped() != null) {
				if (!first)
					timeWashingWasCompletedOrStopped.append(", ");
				timeWashingWasCompletedOrStopped.append(cldtw.getTimeWashingWasCompletedOrStopped().format(EasyDateUtils.logbookTimeFormatter));
			}
			first = false;
		}

		OilLog log10 = (OilLog) logToInsert.clone();
		log10.setCodeNumber("10");
		log10.setOrderInGroup("K");
		log10.setContent(tanks.toString());
		logToInsertList.add(log10);

		OilLog log11 = (OilLog) logToInsert.clone();
		log11.setCodeNumber("11");
		log11.setOrderInGroup("L");
		log11.setContent(numberOfMachinesInUse.toString());
		logToInsertList.add(log11);

		OilLog log12 = (OilLog) logToInsert.clone();
		log12.setCodeNumber("12");
		log12.setOrderInGroup("M");
		log12.setContent(timeOfStartOfWashing.toString());
		logToInsertList.add(log12);

		OilLog log13 = (OilLog) logToInsert.clone();
		log13.setCodeNumber("13");
		log13.setOrderInGroup("N");
		log13.setContent(washingPattern.toString());
		logToInsertList.add(log13);

		OilLog log14 = (OilLog) logToInsert.clone();
		log14.setCodeNumber("14");
		log14.setOrderInGroup("O");
		log14.setContent(washingLinePressure.toString());
		logToInsertList.add(log14);

		OilLog log15 = (OilLog) logToInsert.clone();
		log15.setCodeNumber("15");
		log15.setOrderInGroup("P");
		log15.setContent(timeWashingWasCompletedOrStopped.toString());
		logToInsertList.add(log15);

		if (stateMethodOfEstablishingCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("16");
			log.setOrderInGroup("Q");
			log.setContent(stateMethodOfEstablishingdTextField.getText());
			logToInsertList.add(log);
		}
		if (remarkCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("17");
			log.setOrderInGroup("R");
			log.setContent(remarkTextField.getText());
			logToInsertList.add(log);
		}
	}

	private List<CodeLetterDTankWashed> getTanksWashed() {
		List<CodeLetterDTankWashed> cldtwList = new ArrayList<CodeLetterDTankWashed>();
		for (Node tankWashedChildNode : tanksWashedGridPane.getChildren()) {
			if (tankWashedChildNode instanceof GridPane) {
				GridPane tanksWashedGridPane = (GridPane) tankWashedChildNode;
				CodeLetterDTankWashed cldtw = new CodeLetterDTankWashed();
				cldtw.setIndex(GridPane.getRowIndex(tanksWashedGridPane));
				for (Node childNode : tanksWashedGridPane.getChildren()) {
					if (childNode instanceof JFXChipView && childNode.getId().startsWith("identityOfTank")) {
						cldtw.setIdentityOfTankJFXChipsView((JFXChipView<String>) childNode);
					}
					if (childNode instanceof ComboBox && childNode.getId().startsWith("identityTanksWashing")) {
						cldtw.setIdentityTanksWashing(((ComboBox<String>) childNode).getValue());
					}
					if (childNode instanceof DoubleField && childNode.getId().startsWith("numberOfMachinesInUse")) {
						cldtw.setNumberOfMachinesInUse(((DoubleField) childNode).getValue());
					}
					if (childNode instanceof LocalTimeTextField && childNode.getId().startsWith("timeOfStartOfWashing")) {
						cldtw.setTimeOfStartOfWashing(((LocalTimeTextField) childNode).getLocalTime());
					}
					if (childNode instanceof ComboBox && childNode.getId().startsWith("washingPatternType")) {
						cldtw.setWashingPatternType(((ComboBox<String>) childNode).getValue());
					}
					if (childNode instanceof TextField && childNode.getId().startsWith("washingPatternEmployed")) {
						cldtw.setWashingPatternEmployed(((TextField) childNode).getText());
					}
					if (childNode instanceof DoubleField && childNode.getId().startsWith("washingLinePressureValue")) {
						cldtw.setWashingLinePressureValue(((DoubleField) childNode).getValue());
					}
					if (childNode instanceof ComboBox && childNode.getId().startsWith("washingLinePressureType")) {
						cldtw.setWashingLinePressureType(((ComboBox<String>) childNode).getValue());
					}
					if (childNode instanceof LocalTimeTextField && childNode.getId().startsWith("timeWashingWasCompletedOrStopped")) {
						cldtw.setTimeWashingWasCompletedOrStopped(((LocalTimeTextField) childNode).getLocalTime());
					}
				}
				cldtwList.add(cldtw);
			}
		}
		return cldtwList;
	}

	public void addTankWashed(Event event) {

		GridPane newTankWashedGridPane = new GridPane();
		Integer rows = null;
		try {
			Method method = tanksWashedGridPane.getClass().getDeclaredMethod("getNumberOfRows");
			method.setAccessible(true);
			rows = (Integer) method.invoke(tanksWashedGridPane);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}

		CheckBox identifyTheTankCheckBox = new CheckBox();
		identifyTheTankCheckBox.setSelected(true);
		identifyTheTankCheckBox.setDisable(true);
		identifyTheTankCheckBox.getStyleClass().add("logbookcheck-box");

		Label identifyTheTankLabel = new Label("10. Identity of tank(s) washed");
		identifyTheTankLabel.getStyleClass().add("logbookOtherLabel");
		HBox identifyTheTankHBox = new HBox(identifyTheTankLabel);
		identifyTheTankHBox.setAlignment(Pos.CENTER_LEFT);
		identifyTheTankHBox.getStyleClass().add("logbook-label-hbox");
		
		HBox identifyTheTankCheckBoxHBox = new HBox(identifyTheTankCheckBox, identifyTheTankHBox);
		identifyTheTankCheckBoxHBox.setAlignment(Pos.CENTER_LEFT);
		identifyTheTankCheckBoxHBox.getStyleClass().add("logbookColorBlueLabel");
		
		Region arrowRegion = new Region();
		arrowRegion.getStyleClass().add("logbook-arrow-region");
		arrowRegion.getStyleClass().add("logbook-arrow-region-color-blue");
		
		HBox shellHBox = new HBox(identifyTheTankCheckBoxHBox, arrowRegion);
		shellHBox.setAlignment(Pos.CENTER_LEFT);
//		GridPane.setColumnSpan(shellHBox, 2);

		JFXChipView<String> tankNameJFXChipView = new JFXChipView<String>();
		tankNameJFXChipView.getStyleClass().add("logbookjfx-chip-view");
		GridPane.setColumnSpan(tankNameJFXChipView, 3);
		tankNameJFXChipView.setId("identityOfTank" + rows);
		
		ComboBox<String> identityTanksWashingComboBox = new ComboBox<String>();
		identityTanksWashingComboBox.getItems().addAll("FWD", "MID", "AFT");
		identityTanksWashingComboBox.setValue("");
		identityTanksWashingComboBox.setMinWidth(80);
		identityTanksWashingComboBox.getStyleClass().add("logbookcombo-box");
		identityTanksWashingComboBox.getStyleClass().add("logbook-text-field");
		identityTanksWashingComboBox.setId("identityTanksWashing" + rows);
		identityTanksWashingComboBox.setDisable(false);
		GridPane.setColumnIndex(identityTanksWashingComboBox, 5);
		GridPane.setColumnSpan(identityTanksWashingComboBox, 1);
		TankUsageContext tankUsageContext = new TankUsageContext(Stream.of(TankType.BALLAST, TankType.CARGO, TankType.FUELOIL, TankType.OILYBILGE, TankType.SLOP, TankType.SLUDGE).collect(Collectors.toList()), true, false);
		tankUsageContext.setNoNeedForUserTankQuantities(true);
		tankNameJFXChipView.setUserData(tankUsageContext);
		tankNameJFXChipView.getChips().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> change) {
				while (change.next()) {
					for (String item : change.getAddedSubList()) {
						if (tankNameJFXChipView.getChips().size() > 1) {
							String oldChip = tankNameJFXChipView.getChips().get(0);
							Platform.runLater(() -> tankNameJFXChipView.getChips().remove(oldChip));
						}
					}
				}
				JavaFXUtils.addTooltip(tankNameJFXChipView);
			}
		});

		ImageViewButton addTankImageViewButton = new ImageViewButton(identityOfTanksImage);
		addTankImageViewButton.getStyleClass().add("logbookButtonAddTank");
		GridPane.setHalignment(addTankImageViewButton, HPos.CENTER);
		addTankImageViewButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				selectTankerDialog.showDialog((Stage) ((ImageViewButton) event.getSource()).getScene().getWindow(), tankNameJFXChipView);
			}
		});

		CheckBox nrMachinesInUseCheckBox = new CheckBox();
		nrMachinesInUseCheckBox.setSelected(true);
		nrMachinesInUseCheckBox.setDisable(true);
		nrMachinesInUseCheckBox.getStyleClass().add("logbookcheck-box");
		Label nrMachinesInUseLabel = new Label("11. Number of machines in use");
		nrMachinesInUseLabel.getStyleClass().add("logbookOtherLabel");
		HBox nrMachinesInUseHBox = new HBox(nrMachinesInUseLabel);
		nrMachinesInUseHBox.setAlignment(Pos.CENTER_LEFT);
		nrMachinesInUseHBox.getStyleClass().add("logbook-label-hbox");
		Region arrowRegion2 = new Region();
		arrowRegion2.getStyleClass().add("logbook-arrow-region");
		arrowRegion2.getStyleClass().add("logbook-arrow-region-color-light-grey");
		HBox shellHBox2 = new HBox(nrMachinesInUseCheckBox, nrMachinesInUseHBox);
		shellHBox2.getStyleClass().add("logbookLightGreyLabel");
		shellHBox2.setAlignment(Pos.CENTER_LEFT);
		HBox shellHBox3 = new HBox(shellHBox2, arrowRegion2);
		shellHBox3.setAlignment(Pos.CENTER_LEFT);
		GridPane.setColumnSpan(shellHBox3, 2);
		
		DoubleField nrOfMachinesInUseDoubleField = new DoubleField();
		MathUtils.setStringConverter(nrOfMachinesInUseDoubleField);
		nrOfMachinesInUseDoubleField.setId("numberOfMachinesInUse" + rows);
		nrOfMachinesInUseDoubleField.getStyleClass().add("logbook-text-field");
		JavaFXUtils.addFilledPseudoClass(nrOfMachinesInUseDoubleField);

		Button deleteButton = new Button("X");
		GridPane.setHalignment(deleteButton, HPos.CENTER);
		deleteButton.setStyle("-fx-background-color: #ef7f2e");
		deleteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Button deleteButton = (Button) event.getSource();
				GridPane tankWashedGridPane = (GridPane) deleteButton.getParent();
				Integer rowIndex = GridPane.getRowIndex(tankWashedGridPane);
				((GridPane) tankWashedGridPane.getParent()).getChildren().removeIf(node -> GridPane.getRowIndex(node) == rowIndex);
			}
		});
		deleteButton.setOnMouseEntered(mouseEvent -> {
			if (!mouseEvent.isPrimaryButtonDown())
				deleteButton.setCursor(Cursor.HAND);
		});
		deleteButton.setOnMouseExited(mouseEvent -> {
			if (!mouseEvent.isPrimaryButtonDown())
				deleteButton.setCursor(Cursor.DEFAULT);
		});
		
		newTankWashedGridPane.addRow(0, getTransparentRegion(), shellHBox, getTransparentRegion(), addTankImageViewButton, getTransparentRegion(), tankNameJFXChipView, getTransparentRegion(), getTransparentRegion(), getTransparentRegion(), identityTanksWashingComboBox, getTransparentRegion(), shellHBox3, getTransparentRegion(), getTransparentRegion(), getTransparentRegion(), getTransparentRegion(),
				nrOfMachinesInUseDoubleField, getTransparentRegion(), deleteButton);
		

		CheckBox timeOfStartOfWashingCheckBox = new CheckBox();
		timeOfStartOfWashingCheckBox.setSelected(true);
		timeOfStartOfWashingCheckBox.setDisable(true);
		timeOfStartOfWashingCheckBox.getStyleClass().add("logbookcheck-box");

		Label timeOfStartOfWashingLabel = new Label("12. Time of start of washing");
		timeOfStartOfWashingLabel.getStyleClass().add("logbookOtherLabel");
		HBox timeOfStartOfWashingHBox = new HBox(timeOfStartOfWashingLabel);
		timeOfStartOfWashingHBox.setAlignment(Pos.CENTER_LEFT);
		timeOfStartOfWashingHBox.getStyleClass().add("logbook-label-hbox");
		Region arrowRegion3 = new Region();
		arrowRegion3.getStyleClass().add("logbook-arrow-region");
		arrowRegion3.getStyleClass().add("logbook-arrow-region-color-light-grey");
		HBox shellHBox5 = new HBox(timeOfStartOfWashingCheckBox, timeOfStartOfWashingHBox);
		shellHBox5.getStyleClass().add("logbookLightGreyLabel");
		shellHBox5.setAlignment(Pos.CENTER_LEFT);
		HBox shellHBox6 = new HBox(shellHBox5, arrowRegion3);
		shellHBox6.setAlignment(Pos.CENTER_LEFT);
		GridPane.setColumnSpan(shellHBox6, 2);

		LocalTimeTextField timeOfStartOfWashingTextField = new LocalTimeTextField();
		timeOfStartOfWashingTextField.setId("timeOfStartOfWashing" + rows);
		timeOfStartOfWashingTextField.getStyleClass().add("logbook-LocalDateTimeTextFieldSkin");
		JavaFXUtils.addFilledPseudoClass(timeOfStartOfWashingTextField);
		timeOfStartOfWashingTextField.setDateTimeFormatter(EasyDateUtils.logbookTimeFormatter);
		timeOfStartOfWashingTextField.setMaxHeight(20.0);
		GridPane.setColumnSpan(timeOfStartOfWashingTextField, 7);

		CheckBox washingPatternEmployedCheckBox = new CheckBox();
		washingPatternEmployedCheckBox.setSelected(true);
		washingPatternEmployedCheckBox.setDisable(true);
		washingPatternEmployedCheckBox.getStyleClass().add("logbookcheck-box");

		Label washingPatternEmployedLabel = new Label("13. Washing pattern employed");
		washingPatternEmployedLabel.getStyleClass().add("logbookOtherLabel");
		HBox washingPatternEmployedLabelHBox = new HBox(washingPatternEmployedLabel);
		washingPatternEmployedLabelHBox.setAlignment(Pos.CENTER_LEFT);
		washingPatternEmployedLabelHBox.getStyleClass().add("logbook-label-hbox");
		Region arrowRegion4 = new Region();
		arrowRegion4.getStyleClass().add("logbook-arrow-region");
		arrowRegion4.getStyleClass().add("logbook-arrow-region-color-light-grey");
		HBox shellHBox7 = new HBox(washingPatternEmployedCheckBox, washingPatternEmployedLabelHBox);
		shellHBox7.getStyleClass().add("logbookLightGreyLabel");
		shellHBox7.setAlignment(Pos.CENTER_LEFT);
		HBox shellHBox8 = new HBox(shellHBox7, arrowRegion4);
		shellHBox8.setAlignment(Pos.CENTER_LEFT);
		GridPane.setColumnSpan(shellHBox8, 2);

		ComboBox<String> washingPatternComboBox = new ComboBox<String>();
		washingPatternComboBox.getItems().addAll("MULTI STAGE", "SINGLE STAGE", "OTHER");
		washingPatternComboBox.setValue("");
		washingPatternComboBox.setMinWidth(150);
		washingPatternComboBox.getStyleClass().add("logbookcombo-box");
		washingPatternComboBox.getStyleClass().add("logbook-text-field");
		washingPatternComboBox.setId("washingPatternType" + rows);

		TextField washingPatternEmployedTextField = new TextField();
		washingPatternEmployedTextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		washingPatternEmployedTextField.setId("washingPatternEmployed" + rows);
		washingPatternEmployedTextField.getStyleClass().add("logbook-text-field");
		JavaFXUtils.addFilledPseudoClass(washingPatternEmployedTextField);

		newTankWashedGridPane.addRow(1, shellHBox6, getTransparentRegion(), getTransparentRegion(), timeOfStartOfWashingTextField, getTransparentRegion(), getTransparentRegion(), getTransparentRegion(), getTransparentRegion(), getTransparentRegion(), getTransparentRegion(), getTransparentRegion(),
				shellHBox8, getTransparentRegion(), getTransparentRegion(), getTransparentRegion(), getTransparentRegion(), washingPatternComboBox, getTransparentRegion(), washingPatternEmployedTextField);

		CheckBox washingLinePressureCheckBox = new CheckBox();
		washingLinePressureCheckBox.setSelected(true);
		washingLinePressureCheckBox.setDisable(true);
		washingLinePressureCheckBox.getStyleClass().add("logbookcheck-box");

		Label washingLinePressureLabel = new Label("14. Washing line pressure");
		washingLinePressureLabel.getStyleClass().add("logbookOtherLabel");
		HBox washingLinePressureHBox = new HBox(washingLinePressureLabel);
		washingLinePressureHBox.setAlignment(Pos.CENTER_LEFT);
		washingLinePressureHBox.getStyleClass().add("logbook-label-hbox");
		Region arrowRegion5 = new Region();
		arrowRegion5.getStyleClass().add("logbook-arrow-region");
		arrowRegion5.getStyleClass().add("logbook-arrow-region-color-light-grey");
		HBox shellHBox9 = new HBox(washingLinePressureCheckBox, washingLinePressureHBox);
		shellHBox9.getStyleClass().add("logbookLightGreyLabel");
		shellHBox9.setAlignment(Pos.CENTER_LEFT);
		HBox shellHBox10 = new HBox(shellHBox9, arrowRegion5);
		shellHBox10.setAlignment(Pos.CENTER_LEFT);
		GridPane.setColumnSpan(shellHBox10, 2);

		DoubleField washingLinePressureValueDoubleField = new DoubleField();
		MathUtils.setStringConverter(washingLinePressureValueDoubleField);
		washingLinePressureValueDoubleField.setId("washingLinePressureValue" + rows);
		washingLinePressureValueDoubleField.getStyleClass().add("logbook-text-field");
		GridPane.setColumnSpan(washingLinePressureValueDoubleField, 3);
		JavaFXUtils.addFilledPseudoClass(washingLinePressureValueDoubleField);
		MathUtils.setMinMaxValues(washingLinePressureValueDoubleField, 0.0, null);
		ComboBox<String> washingLinePressureTypeComboBox = new ComboBox<String>();
		washingLinePressureTypeComboBox.getItems().addAll("MPA", "BAR", "IBS/FT2");
		washingLinePressureTypeComboBox.setValue("");
		washingLinePressureTypeComboBox.setMaxWidth(190);
		GridPane.setHalignment(washingLinePressureTypeComboBox, HPos.RIGHT);
		GridPane.setColumnSpan(washingLinePressureTypeComboBox, 3);
		washingLinePressureTypeComboBox.getStyleClass().add("logbookcombo-box");
		washingLinePressureTypeComboBox.getStyleClass().add("logbook-text-field");
		washingLinePressureTypeComboBox.setId("washingLinePressureType" + rows);

		CheckBox timeWashingWasCompletedOrStoppedCheckBox = new CheckBox();
		timeWashingWasCompletedOrStoppedCheckBox.setSelected(true);
		timeWashingWasCompletedOrStoppedCheckBox.setDisable(true);
		timeWashingWasCompletedOrStoppedCheckBox.getStyleClass().add("logbookcheck-box");

		Label timeWashingWasCompletedOrStoppedLabel = new Label("15. Time washing was completed or stopped");
		timeWashingWasCompletedOrStoppedLabel.getStyleClass().add("logbookOtherLabel");
		HBox timeWashingWasCompletedOrStoppedLabelHBox = new HBox(timeWashingWasCompletedOrStoppedLabel);
		timeWashingWasCompletedOrStoppedLabelHBox.setAlignment(Pos.CENTER_LEFT);
		timeWashingWasCompletedOrStoppedLabelHBox.getStyleClass().add("logbook-label-hbox");
		Region arrowRegion6 = new Region();
		arrowRegion6.getStyleClass().add("logbook-arrow-region");
		arrowRegion6.getStyleClass().add("logbook-arrow-region-color-light-grey");
		HBox shellHBox11 = new HBox(timeWashingWasCompletedOrStoppedCheckBox, timeWashingWasCompletedOrStoppedLabelHBox);
		shellHBox11.getStyleClass().add("logbookLightGreyLabel");
		shellHBox11.setAlignment(Pos.CENTER_LEFT);
		HBox shellHBox12 = new HBox(shellHBox11, arrowRegion6);
		shellHBox12.setAlignment(Pos.CENTER_LEFT);
		GridPane.setColumnSpan(shellHBox12, 2);

		LocalTimeTextField timeWashingWasCompletedOrStoppedTextField = new LocalTimeTextField();
		timeWashingWasCompletedOrStoppedTextField.setMaxHeight(20.0);
		GridPane.setColumnSpan(timeWashingWasCompletedOrStoppedTextField, 3);
		timeWashingWasCompletedOrStoppedTextField.setId("timeWashingWasCompletedOrStopped" + rows);
		timeWashingWasCompletedOrStoppedTextField.getStyleClass().add("logbook-LocalDateTimeTextFieldSkin");
		JavaFXUtils.addFilledPseudoClass(timeWashingWasCompletedOrStoppedTextField);
		timeWashingWasCompletedOrStoppedTextField.setDateTimeFormatter(EasyDateUtils.logbookTimeFormatter);
		newTankWashedGridPane.addRow(2, shellHBox10, getTransparentRegion(), getTransparentRegion(), washingLinePressureValueDoubleField, getTransparentRegion(), getTransparentRegion(), getTransparentRegion(), washingLinePressureTypeComboBox, getTransparentRegion(), getTransparentRegion(), getTransparentRegion(),
				shellHBox12, getTransparentRegion(), getTransparentRegion(), getTransparentRegion(), getTransparentRegion(), timeWashingWasCompletedOrStoppedTextField);

//		newTankWashedGridPane.setGridLinesVisible(true);
		GridPane.setFillWidth(newTankWashedGridPane, true);
		GridPane.setHgrow(newTankWashedGridPane, Priority.ALWAYS);
		GridPane.setVgrow(newTankWashedGridPane, Priority.ALWAYS);
//		newTankWashedGridPane.setPadding(new Insets(30,0,0,0));
//		newTankWashedGridPane.setHgap(10);
		newTankWashedGridPane.setVgap(10);
		ColumnConstraints cc0 = new ColumnConstraints(30.0, 30.0, 30.0);
		ColumnConstraints cc1 = new ColumnConstraints(330.0, 330.0, 330.0);
		ColumnConstraints cc2 = new ColumnConstraints(10.0, 10.0, 10.0);
		ColumnConstraints cc3 = new ColumnConstraints(30.0, 30.0, 30.0);
		ColumnConstraints cc4 = new ColumnConstraints(10.0, 10.0, 10.0);
		ColumnConstraints cc5 = new ColumnConstraints(50.0, 50.0, 50);
		ColumnConstraints cc6 = new ColumnConstraints(10.0, 10.0, 10.0);
		ColumnConstraints cc7 = new ColumnConstraints(50.0, 50.0, 50.0);
		ColumnConstraints cc8 = new ColumnConstraints(10.0, 10.0, 10.0);
		ColumnConstraints cc9 = new ColumnConstraints(80, 80, 80);
		ColumnConstraints cc10 = new ColumnConstraints(10.0, 10.0, 10.0);
		ColumnConstraints cc11 = new ColumnConstraints(30.0, 30.0, 30.0);
		ColumnConstraints cc12 = new ColumnConstraints(330.0, 330.0, 330.0);
		ColumnConstraints cc13 = new ColumnConstraints(10.0, 10.0, 10.0);
		ColumnConstraints cc14 = new ColumnConstraints(30.0, 30.0, 30.0);
		ColumnConstraints cc15 = new ColumnConstraints(10.0, 10.0, 10.0);
		ColumnConstraints cc16 = new ColumnConstraints(150.0, 150.0, 150.0);
		ColumnConstraints cc17 = new ColumnConstraints(10.0, 10.0, 10.0);
		ColumnConstraints cc18 = new ColumnConstraints(100.0, Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);

//		cc0.setHalignment(HPos.CENTER);
//		cc2.setHgrow(Priority.SOMETIMES);
//		cc3.setHgrow(Priority.SOMETIMES);
//		cc4.setHgrow(Priority.SOMETIMES);
		cc5.setHgrow(Priority.SOMETIMES);
//		cc6.setHgrow(Priority.SOMETIMES);
		cc7.setHgrow(Priority.SOMETIMES);
//		cc8.setHgrow(Priority.SOMETIMES);
		cc11.setHgrow(Priority.SOMETIMES);
		newTankWashedGridPane.getColumnConstraints().addAll(cc0, cc1, cc2, cc3, cc4, cc5, cc6, cc7, cc8, cc9, cc10, cc11, cc12, cc13, cc14, cc15, cc16, cc17, cc18);
		newTankWashedGridPane.getRowConstraints().addAll(new RowConstraints(50.0, 50.0, 50.0),
														new RowConstraints(50.0, 50.0, 50.0),
														new RowConstraints(50.0, 50.0, 50.0));
		eventBroker.subscribe(Activator.MARPOL_DETAILS_RELOADED, new org.osgi.service.event.EventHandler() {
			@Override
			public void handleEvent(org.osgi.service.event.Event event) {
				ArrayList<Long> changedTankIds = (ArrayList<Long>) event.getProperty(Activator.TANK_IDS);
				if (changedTankIds == null) {
					tankNameJFXChipView.getChips().clear();
				} else if (changedTankIds != null && !changedTankIds.isEmpty()) {
					Activator.removeChangedTanksFromJFXChipView(changedTankIds, tankNameJFXChipView);
				}
			}
		});

		tanksWashedGridPane.addRow(rows, newTankWashedGridPane);
	}

	public void letterD(MouseEvent event) {
		helpCodeLetterDialog.showDialog((Stage)((ImageViewButton)event.getSource()).getScene().getWindow(),helpImageList);
	}

	public void openIdentifyOfTanksWashed(Event e) {
		selectTankerDialog.showDialog((Stage) ((ImageViewButton) e.getSource()).getScene().getWindow(), identityOfTankJFXChipsView);
	}

	public void showBalloon(MouseEvent event) {
		ballonMessage.showGeneralBalloon((ImageViewButton) event.getTarget(), label1);
	}

	public void showBalloon1(MouseEvent event) {
		ballonMessage.showGeneralBalloon((ImageViewButton) event.getTarget(), label2);
	}

	public void showBalloon2(MouseEvent event) {
		ballonMessage.showGeneralBalloon((ImageViewButton) event.getTarget(), label3);
	}

	public void showBalloon3(MouseEvent event) {
		ballonMessage.showGeneralBalloon((ImageViewButton) event.getTarget(), label4);
	}

	public void clearFilledInfo() {
		portWhereCrudeOilWashingCheckBox.setSelected(false);
		identityOfTankCheckBox.setSelected(false);
		numberOfMachinesInUseCheckBox.setSelected(false);
		timeOfStartOfWashingCheckBox.setSelected(false);
		washing0PatternEmployedCheckBox.setSelected(false);
		washingLinePressureCheckBox.setSelected(false);
		timeWashingWasCompletedOrStoppedCheckBox.setSelected(false);
		stateMethodOfEstablishingCheckBox.setSelected(false);
		remarkCheckBox.setSelected(false);
		identityOfTankJFXChipsView.getChips().clear();
		((TankUsageContext) identityOfTankJFXChipsView.getUserData()).getUserTankQuantities().clear();
		tanksWashedGridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 0);
	}

	private void validateFields(Date registrationDate, Map<Validation, Object> validations, List<CodeLetterDTankWashed> cldtwList) {
		if (!portWhereCrudeOilWashingCheckBox.isSelected() && !shipPositionIfCarriedCheckBox.isSelected()) {
			validations.put(Validation.D9_EMPTY, null);
		}
		if (portWhereCrudeOilWashingCheckBox.isSelected() && !shipPositionIfCarriedCheckBox.isSelected() && (portWhereCrudeOilWashingTextField.getText() == null || portWhereCrudeOilWashingTextField.getText().equals(""))) {
			validations.put(Validation.D9_PORT_EMPTY, null);
		} else if (portWhereCrudeOilWashingCheckBox.isSelected() && shipPositionIfCarriedCheckBox.isSelected()) {
			if (shipPositionIfCarriedLatTextField.getText() == null || shipPositionIfCarriedLatTextField.getText().equals("") || shipPositionIfCarriedLongTextField.getText() == null
					|| shipPositionIfCarriedLongTextField.getText().equals("")) {
				validations.put(Validation.D9_LATITUDE_OR_LONGITUDE_EMPTY, null);
			} else if (shipPositionIfCarriedLatTextField.getText().equals("00°00.00'N") || shipPositionIfCarriedLongTextField.getText().equals("000°00.00'W")) {
				validations.put(Validation.D9_LAT_LON_ZERO, null);
			}
		}
		
		for (CodeLetterDTankWashed cldtw : cldtwList) {
			if (cldtw.getIdentityOfTankJFXChipsView().getChips().size()==0 || cldtw.getIdentityOfTankJFXChipsView().getChips().isEmpty()) {
				validations.put(Validation.D10_TANKS_EMPTY, null);
			}
			if (cldtw.getIdentityTanksWashing()== null || cldtw.getIdentityTanksWashing().equals("")) {
				validations.put(Validation.D10_WASHING_TANK_TYPE_EMPTY, null);
			}
			if (cldtw.getNumberOfMachinesInUse() == null || cldtw.getNumberOfMachinesInUse() == 0.0) {
				validations.put(Validation.D11_NR_OF_MACHINES_IN_USE_EMPTY, null);
			}
			if (cldtw.getTimeOfStartOfWashing() == null) {
				validations.put(Validation.D12_TIME_OF_START_OF_WASHING_EMPTY, null);
			}
			if (cldtw.getWashingPatternType() == null || cldtw.getWashingPatternType().equals("")) {
				validations.put(Validation.D13_WASHING_PATTERN_TYPE_EMPTY, null);
			}
			if (cldtw.getWashingPatternEmployed() == null || cldtw.getWashingPatternEmployed().equals("")) {
				validations.put(Validation.D13_WASHING_PATTERN_EMPLOYED_EMPTY, null);
			}
			if (cldtw.getWashingLinePressureValue() == null || cldtw.getWashingLinePressureValue() == 0.0) {
				validations.put(Validation.D14_WASHING_LINE_PRESSURE_VALUE_EMPTY, null);
			}
			if (cldtw.getWashingLinePressureType() == null || cldtw.getWashingLinePressureType().equals("")) {
				validations.put(Validation.D14_WASHING_LINE_PRESSURE_TYPE_EMPTY, null);
			}
			if (cldtw.getTimeWashingWasCompletedOrStopped() == null) {
				validations.put(Validation.D15_TIME_OF_COMPLETION_OR_STOP_OF_WASHING_EMPTY, null);
			}
			if ((cldtw.getTimeOfStartOfWashing()!=null && cldtw.getTimeWashingWasCompletedOrStopped()!=null) && cldtw.getTimeOfStartOfWashing().isAfter(cldtw.getTimeWashingWasCompletedOrStopped())) {
				validations.put(Validation.D12_TIME_AT_START_OF_WASHING_AFTER_TIME_AT_STOP, null);
			}
		}

		if (!stateMethodOfEstablishingCheckBox.isSelected() || stateMethodOfEstablishingdTextField.getText() == null || stateMethodOfEstablishingdTextField.getText().equals("")) {
			validations.put(Validation.D16_EMPTY, null);
		}
		if (remarkCheckBox.isSelected() && remarkTextField.getText().isEmpty()) {
			validations.put(Validation.D17_EMPTY, null);
		}
		if (!validations.containsKey(Validation.D10_TANKS_EMPTY)) {
		List<String> duplicates = cldtwList.stream()
				  .collect(Collectors.groupingBy(p -> p.getIdentityOfTankJFXChipsView().getChips().get(0), Collectors.counting()))
				  .entrySet().stream()
				  .filter(n -> n.getValue() > 1)
				  .map(n -> n.getKey())
				  .collect(Collectors.toList());
		for(String duplicateTank : duplicates) {
			validations.put(Validation.D10_TANK_ALREADY_IN_LIST, duplicateTank);
		}
	  }
	}
	
	private Region getTransparentRegion() {
		Region mouseTransparentRegion = new Region(); mouseTransparentRegion.setMouseTransparent(true);
		return mouseTransparentRegion;
	}
}
