package al.sybina.ebr.logbook2.views;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import com.oceanpro.utils.EasyDateUtils;

import al.sybina.ebr.commons.utils.CoordinateUIUtils;
import al.sybina.ebr.commons.utils.JavaFXUtils;
import al.sybina.ebr.commons.utils.MathUtils;
import al.sybina.ebr.logbook.commons.Activator;
import al.sybina.ebr.logbook.dialogs.SelectTankerDialog;
import al.sybina.ebr.logbook.dialogs.TankUsageContext;
import al.sybina.ebr.logbook.dialogs.help.HelpCodeLetterDialog;
import al.sybina.ebr.logbook.domain.reladomo.OilLog;
import al.sybina.ebr.logbook2.views.OilRecordBookPart2ViewController.Validation;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import jfxtras.scene.control.ImageViewButton;
import jfxtras.scene.control.LocalTimeTextField;
import jidefx.scene.control.field.DoubleField;
import jidefx.scene.control.field.FormattedTextField;

public class OilRecordBookPart2CodeLetterFController implements Initializable {

	private static final Logger logger = LoggerCreator.createLogger(OilRecordBookPart2CodeLetterFController.class);

	@Inject
	private IEventBroker eventBroker;
	@Inject
	private IResourceProviderService resourcesService;
	@Inject
	private ImageProvider imageProvider;
	@Inject
	private SelectTankerDialog selectTankerDialog;
	@Inject
	private HelpCodeLetterDialog helpCodeLetterDialog;
	@FXML
	private ImageViewButton infoLetterFImageViewButton;
	@FXML
	private CheckBox indetityTanksBallastCheckBox;
	@FXML
	private CheckBox positionOfShipForFlushingCheckBox;
	@FXML
	private FormattedTextField positionOfShipForFlushingLatFormattedFormattedTextField;
	@FXML
	private FormattedTextField positionOfShipForFlushingLonFormattedFormattedTextField;
	@FXML
	private CheckBox portBallastCleanTanksCheckBox;
	@FXML
	private TextField portBallastCleanTanksTextField;
	@FXML
	private CheckBox positionOfShipForSlopTanksCheckBox;
	@FXML
	private FormattedTextField positionOfShipForSlopTanksLatFormattedFormattedTextField;
	@FXML
	private FormattedTextField positionOfShipForSlopTanksLonFormattedFormattedTextField;
	@FXML
	private ImageViewButton identityOfTanksImageViewButton;
	@FXML
	private CheckBox identityOfTanksTransferredCheckBox;
	@FXML
	private ImageViewButton identityOfTanksTransferredImageViewButton;
	@FXML
	private CheckBox identityOfTanksStoredCheckBox;
	@FXML
	private ImageViewButton identityOfTanksStoredImageViewButton;
	@FXML
	private JFXChipView<String> identityOfTankJFXChipsView;
	@FXML
	private JFXChipView<String> identityOfTankTransferredJFXChipsView;
	@FXML
	private JFXChipView<String> identityOfTankStoredJFXChipsView;
	@FXML
	private DoubleField totalQuantityBallstTransferredDoubleField;
	@FXML
	private DoubleField totalQuantityBallstStoredDoubleField;
	@FXML
	private CheckBox logbook2LetterF24CheckBox;
	@FXML
	private FormattedTextField logbook2LetterF24LatFormattedTextField;
	@FXML
	private FormattedTextField logbook2LetterF24LonFormattedTextField;
	@FXML
	private CheckBox logbook2LetterF25CheckBox;
	@FXML
	private FormattedTextField logbook2LetterF25LatFormattedTextField;
	@FXML
	private FormattedTextField logbook2LetterF25LonFormattedTextField;
	@FXML
	private LocalTimeTextField logbook2LetterF25TimeTextField;
	@FXML
	private CheckBox logbook2LetterF26CheckBox;
	@FXML
	private DoubleField logbook2LetterF26TotalQuantiDoubleField;
	@FXML
	public AnchorPane codeLetterContentAnchorPane;
	private ObservableList<Image> helpImageList = FXCollections.observableArrayList();
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MathUtils.setStringConverter(totalQuantityBallstTransferredDoubleField);
		MathUtils.setMinMaxValues(totalQuantityBallstTransferredDoubleField, 0.0, null);
		MathUtils.setStringConverter(totalQuantityBallstStoredDoubleField);
		MathUtils.setMinMaxValues(totalQuantityBallstStoredDoubleField, 0.0, null);
		MathUtils.setStringConverter(logbook2LetterF26TotalQuantiDoubleField);
		MathUtils.setMinMaxValues(logbook2LetterF26TotalQuantiDoubleField, 0.0, null);
		try {
			Image helpImageLetterF2 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPF2));
			
			Image helpImageLetterF1 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPF1));
			
			Image helpImageLetterF3 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPF3));
			
			helpImageList.addAll(helpImageLetterF2, helpImageLetterF1, helpImageLetterF3);
			
			Image addImageIdentityOfTanks = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_ADD));
			identityOfTanksImageViewButton.setImage(addImageIdentityOfTanks);
			identityOfTanksTransferredImageViewButton.setImage(addImageIdentityOfTanks);
			identityOfTanksStoredImageViewButton.setImage(addImageIdentityOfTanks);

			Image infoIconImageInfoLetterF = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
			infoLetterFImageViewButton.setImage(infoIconImageInfoLetterF);
		} catch (IOException e) {
			e.printStackTrace();
		}
		CoordinateUIUtils.initializeLatitudeField(positionOfShipForFlushingLatFormattedFormattedTextField);
		CoordinateUIUtils.initializeLongitudeField(positionOfShipForFlushingLonFormattedFormattedTextField);
		CoordinateUIUtils.initializeLatitudeField(positionOfShipForSlopTanksLatFormattedFormattedTextField);
		CoordinateUIUtils.initializeLongitudeField(positionOfShipForSlopTanksLonFormattedFormattedTextField);
		identityOfTanksImageViewButton.setDisable(true);
		identityOfTanksTransferredImageViewButton.setDisable(true);
		identityOfTanksStoredImageViewButton.setDisable(true);
		positionOfShipForFlushingLatFormattedFormattedTextField.setDisable(true);
		positionOfShipForFlushingLonFormattedFormattedTextField.setDisable(true);
		positionOfShipForSlopTanksLatFormattedFormattedTextField.setDisable(true);
		positionOfShipForSlopTanksLonFormattedFormattedTextField.setDisable(true);
		portBallastCleanTanksTextField.setDisable(true);
		totalQuantityBallstTransferredDoubleField.setDisable(true);
		totalQuantityBallstStoredDoubleField.setDisable(true);
		CoordinateUIUtils.initializeLatitudeField(logbook2LetterF24LatFormattedTextField);
		CoordinateUIUtils.initializeLongitudeField(logbook2LetterF24LonFormattedTextField);
		CoordinateUIUtils.initializeLatitudeField(logbook2LetterF25LatFormattedTextField);
		CoordinateUIUtils.initializeLongitudeField(logbook2LetterF25LonFormattedTextField);
		logbook2LetterF24LatFormattedTextField.setDisable(true);
		logbook2LetterF24LonFormattedTextField.setDisable(true);
		logbook2LetterF25LatFormattedTextField.setDisable(true);
		logbook2LetterF25LonFormattedTextField.setDisable(true);
		logbook2LetterF25TimeTextField.setDisable(true);
		logbook2LetterF26TotalQuantiDoubleField.setDisable(true);
		portBallastCleanTanksTextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		JavaFXUtils.addFilledPseudoClass(portBallastCleanTanksTextField);
		JavaFXUtils.addFilledPseudoClass(positionOfShipForFlushingLatFormattedFormattedTextField);
		JavaFXUtils.addFilledPseudoClass(positionOfShipForFlushingLonFormattedFormattedTextField);
		JavaFXUtils.addFilledPseudoClass(positionOfShipForSlopTanksLatFormattedFormattedTextField);
		JavaFXUtils.addFilledPseudoClass(positionOfShipForSlopTanksLonFormattedFormattedTextField);
		JavaFXUtils.addFilledPseudoClass(totalQuantityBallstStoredDoubleField);
		JavaFXUtils.addFilledPseudoClass(totalQuantityBallstTransferredDoubleField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterF24LatFormattedTextField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterF24LonFormattedTextField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterF25LatFormattedTextField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterF25LonFormattedTextField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterF26TotalQuantiDoubleField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterF25TimeTextField);
		logbook2LetterF25TimeTextField.setDateTimeFormatter(EasyDateUtils.logbookTimeFormatter);
		indetityTanksBallastCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				identityOfTankJFXChipsView.setDisable(false);
				identityOfTanksImageViewButton.setDisable(false);
			} else {
				identityOfTankJFXChipsView.setDisable(true);
				identityOfTanksImageViewButton.setDisable(true);
			}
			identityOfTankJFXChipsView.getChips().clear();
		});

		identityOfTanksTransferredCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				identityOfTankTransferredJFXChipsView.setDisable(false);
				identityOfTanksTransferredImageViewButton.setDisable(false);
				identityOfTanksStoredCheckBox.setSelected(false);
			} else {
				identityOfTankTransferredJFXChipsView.setDisable(true);
				identityOfTanksTransferredImageViewButton.setDisable(true);
				totalQuantityBallstTransferredDoubleField.clear();
			}
			identityOfTankTransferredJFXChipsView.getChips().clear();
		});

		identityOfTanksStoredCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				identityOfTankStoredJFXChipsView.setDisable(false);
				identityOfTanksStoredImageViewButton.setDisable(false);
				identityOfTanksTransferredCheckBox.setSelected(false);
			} else {
				identityOfTankStoredJFXChipsView.setDisable(true);
				identityOfTanksStoredImageViewButton.setDisable(true);
				totalQuantityBallstStoredDoubleField.clear();
			}
			identityOfTankStoredJFXChipsView.getChips().clear();
		});

		TankUsageContext tankUsageContext = new TankUsageContext(Stream.of(TankType.BALLAST, TankType.CARGO, TankType.FUELOIL, TankType.OILYBILGE, TankType.SLOP, TankType.SLUDGE).collect(Collectors.toList()), true, true);
		// tankUsageContext.setNoNeedForUserTankQuantities(true);
		identityOfTankJFXChipsView.setUserData(tankUsageContext);
		addRemoveListener(identityOfTankJFXChipsView);
		identityOfTankJFXChipsView.getChips().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> c) {
				if (indetityTanksBallastCheckBox.isSelected()) {
					// Double totalUserTankQuantities =
					// ((TankUsageContext)identityOfTankJFXChipsView.getUserData()).getTotalUserTankQuantities();
					// Double totalUserTankActualLevel =
					// ((TankUsageContext)identityOfTankJFXChipsView.getUserData()).getTotalUserTankActualLevel();
					// //totalQuantityBallstRecivedDoubleField.setValue(totalUserTankQuantities);
					// totalQuantityBallastRecivedCheckBox.setSelected(true);
					// totalContentOfTanksDoubleField.setValue(totalUserTankActualLevel+totalUserTankQuantities);
				}
				JavaFXUtils.addTooltip(identityOfTankJFXChipsView);
			}
		});
		TankUsageContext tankUsageContext1 = new TankUsageContext(Stream.of(TankType.BALLAST, TankType.CARGO, TankType.FUELOIL, TankType.OILYBILGE, TankType.SLOP, TankType.SLUDGE).collect(Collectors.toList()), true, true);
		identityOfTankTransferredJFXChipsView.setUserData(tankUsageContext1);
		addRemoveListener(identityOfTankTransferredJFXChipsView);
		identityOfTankTransferredJFXChipsView.getChips().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> change) {
				if (identityOfTanksTransferredCheckBox.isSelected()) {
					Double totalUserTankQuantities = ((TankUsageContext) identityOfTankTransferredJFXChipsView.getUserData()).getTotalUserTankQuantities();
					Double totalUserTankActualLevel = ((TankUsageContext) identityOfTankTransferredJFXChipsView.getUserData()).getTotalUserTankActualLevel();
					totalQuantityBallstTransferredDoubleField.setValue(totalUserTankQuantities);
				}
				JavaFXUtils.addTooltip(identityOfTankTransferredJFXChipsView);
			}
		});
		identityOfTankStoredJFXChipsView.setUserData(tankUsageContext1);
		addRemoveListener(identityOfTankStoredJFXChipsView);
		identityOfTankStoredJFXChipsView.getChips().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> change) {
				if (identityOfTanksStoredCheckBox.isSelected()) {
					Double totalUserTankQuantities = ((TankUsageContext) identityOfTankStoredJFXChipsView.getUserData()).getTotalUserTankQuantities();
					Double totalUserTankActualLevel = ((TankUsageContext) identityOfTankStoredJFXChipsView.getUserData()).getTotalUserTankActualLevel();
					totalQuantityBallstStoredDoubleField.setValue(totalUserTankQuantities);
				}
				JavaFXUtils.addTooltip(identityOfTankStoredJFXChipsView);
			}
		});
		positionOfShipForFlushingCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				positionOfShipForFlushingLatFormattedFormattedTextField.setDisable(false);
				positionOfShipForFlushingLonFormattedFormattedTextField.setDisable(false);
				portBallastCleanTanksCheckBox.setSelected(false);
			} else {
				positionOfShipForFlushingLatFormattedFormattedTextField.setDisable(true);
				positionOfShipForFlushingLonFormattedFormattedTextField.setDisable(true);
				positionOfShipForFlushingLatFormattedFormattedTextField.setText("00°00.00'N");
				positionOfShipForFlushingLonFormattedFormattedTextField.setText("000°00.00'W");

			}
		});
		portBallastCleanTanksCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				portBallastCleanTanksTextField.setDisable(false);
				positionOfShipForFlushingCheckBox.setSelected(false);
			} else {
				portBallastCleanTanksTextField.setDisable(true);
				portBallastCleanTanksTextField.clear();
				;
			}
		});
		positionOfShipForSlopTanksCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				positionOfShipForSlopTanksLatFormattedFormattedTextField.setDisable(false);
				positionOfShipForSlopTanksLonFormattedFormattedTextField.setDisable(false);
			} else {
				positionOfShipForSlopTanksLatFormattedFormattedTextField.setDisable(true);
				positionOfShipForSlopTanksLonFormattedFormattedTextField.setDisable(true);
				positionOfShipForSlopTanksLatFormattedFormattedTextField.setText("00°00.00'N");
				positionOfShipForSlopTanksLonFormattedFormattedTextField.setText("000°00.00'W");
			}
		});

		logbook2LetterF24CheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterF24LatFormattedTextField.setDisable(false);
				logbook2LetterF24LonFormattedTextField.setDisable(false);
			} else {
				logbook2LetterF24LatFormattedTextField.setDisable(true);
				logbook2LetterF24LonFormattedTextField.setDisable(true);
				logbook2LetterF24LatFormattedTextField.setText("00°00.00'N");
				logbook2LetterF24LonFormattedTextField.setText("000°00.00'W");
			}
		});
		logbook2LetterF25CheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterF25LatFormattedTextField.setDisable(false);
				logbook2LetterF25LonFormattedTextField.setDisable(false);
				logbook2LetterF25TimeTextField.setDisable(false);
			} else {
				logbook2LetterF25LatFormattedTextField.setDisable(true);
				logbook2LetterF25LonFormattedTextField.setDisable(true);
				logbook2LetterF25TimeTextField.setDisable(true);
				logbook2LetterF25TimeTextField.setLocalTime(null);
				logbook2LetterF25LatFormattedTextField.setText("00°00.00'N");
				logbook2LetterF25LonFormattedTextField.setText("000°00.00'W");
			}
		});
		logbook2LetterF26CheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterF26TotalQuantiDoubleField.setDisable(false);
			} else {
				logbook2LetterF26TotalQuantiDoubleField.setDisable(true);
				logbook2LetterF26TotalQuantiDoubleField.clear();
			}
		});

		eventBroker.subscribe(Activator.MARPOL_DETAILS_RELOADED, new org.osgi.service.event.EventHandler() {
			@Override
			public void handleEvent(org.osgi.service.event.Event event) {
				ArrayList<Long> changedTankIds = (ArrayList<Long>) event.getProperty(Activator.TANK_IDS);
				if (changedTankIds == null) {
					identityOfTankJFXChipsView.getChips().clear();
					identityOfTankTransferredJFXChipsView.getChips().clear();
					identityOfTankStoredJFXChipsView.getChips().clear();
				} else if (changedTankIds != null && !changedTankIds.isEmpty()) {
					Activator.removeChangedTanksFromJFXChipView(changedTankIds, identityOfTankJFXChipsView);
					Activator.removeChangedTanksFromJFXChipView(changedTankIds, identityOfTankTransferredJFXChipsView);
					Activator.removeChangedTanksFromJFXChipView(changedTankIds, identityOfTankStoredJFXChipsView);
				}
			}
		});
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

	public void letterF(MouseEvent event) {
		helpCodeLetterDialog.showDialog((Stage)((ImageViewButton)event.getSource()).getScene().getWindow(),helpImageList);
	}

	public void processF(OilLog logToInsert, List<OilLog> logToInsertList, Map<Validation, Object> validations) {
		validateFields(logToInsert.getBusinessDate(), validations);
		if (indetityTanksBallastCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("20");
			log.setOrderInGroup("W");
			log.setContent("" + String.join(", ", identityOfTankJFXChipsView.getChips()));
			log.setUserTankActualLevels(((TankUsageContext) identityOfTankJFXChipsView.getUserData()).getUserTankActualLevels());
			logToInsertList.add(log);
		}
		if (positionOfShipForFlushingCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("21");
			log.setOrderInGroup("X");
			log.setContent("LAT: " + positionOfShipForFlushingLatFormattedFormattedTextField.getText() + " LONG: " + positionOfShipForFlushingLonFormattedFormattedTextField.getText());
			logToInsertList.add(log);
		} else if (portBallastCleanTanksCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("21");
			log.setOrderInGroup("X");
			log.setContent("" + portBallastCleanTanksTextField.getText());
			logToInsertList.add(log);
		}
		if (positionOfShipForSlopTanksCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("22");
			log.setOrderInGroup("Y");
			log.setContent("LAT: " + positionOfShipForSlopTanksLatFormattedFormattedTextField.getText() + " LONG: " + positionOfShipForSlopTanksLonFormattedFormattedTextField.getText());
			logToInsertList.add(log);

		}
		if (identityOfTanksTransferredCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("23");
			log.setOrderInGroup("Z");
			log.setContent("" + String.join(", ", identityOfTankTransferredJFXChipsView.getChips()) + "\r\n" + "" + totalQuantityBallstTransferredDoubleField.getText() + "M3");
			log.setUserTankActualLevels(((TankUsageContext) identityOfTankTransferredJFXChipsView.getUserData()).getUserTankActualLevels());
			logToInsertList.add(log);
		} else if (identityOfTanksStoredCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("23");
			log.setOrderInGroup("Z");
			log.setContent("" + String.join(", ", identityOfTankStoredJFXChipsView.getChips()) + "\r\n" + "" + totalQuantityBallstStoredDoubleField.getText() + "M3");
			log.setUserTankActualLevels(((TankUsageContext) identityOfTankStoredJFXChipsView.getUserData()).getUserTankActualLevels());
			logToInsertList.add(log);
		}

		if (logbook2LetterF24CheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("24");
			log.setOrderInGroup("ZA");
			log.setContent("LAT: " + logbook2LetterF24LatFormattedTextField.getText() + " LONG: " + logbook2LetterF24LonFormattedTextField.getText());
			logToInsertList.add(log);

		}
		if (logbook2LetterF25CheckBox.isSelected() && logbook2LetterF25TimeTextField.getLocalTime()!=null) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("25");
			log.setOrderInGroup("ZB");
			log.setContent("TIME:" + logbook2LetterF25TimeTextField.getLocalTime().format(EasyDateUtils.logbookTimeFormatter) + " LAT:" + logbook2LetterF25LatFormattedTextField.getText() + " LONG:" + logbook2LetterF25LonFormattedTextField.getText());
			logToInsertList.add(log);

		}
		if (logbook2LetterF26CheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("26");
			log.setOrderInGroup("ZC");
			log.setContent(logbook2LetterF26TotalQuantiDoubleField.getText() + "M3");
			logToInsertList.add(log);

		}
	}

	private void validateFields(Timestamp businessDate, Map<Validation, Object> validations) {
		if (!indetityTanksBallastCheckBox.isSelected() || identityOfTankJFXChipsView.getChips().isEmpty()) {
			validations.put(Validation.F20_EMPTY_TANKS, null);
		}
		if (!positionOfShipForFlushingCheckBox.isSelected() && !portBallastCleanTanksCheckBox.isSelected()) {
			validations.put(Validation.F21_EMPTY, null);
		}
		if (positionOfShipForFlushingCheckBox.isSelected()
				&& (positionOfShipForFlushingLatFormattedFormattedTextField.getText().equals("00°00.00'N") || positionOfShipForFlushingLonFormattedFormattedTextField.getText().equals("000°00.00'W"))) {

			validations.put(Validation.F21_LAT_LON_LAT_LON_ZERO, null);
		}
		if (portBallastCleanTanksCheckBox.isSelected() && (portBallastCleanTanksTextField.getText().isEmpty())) {
			validations.put(Validation.F21_EMPTY, null);
		}

		if (positionOfShipForSlopTanksCheckBox.isSelected()
				&& (positionOfShipForSlopTanksLatFormattedFormattedTextField.getText().equals("00°00.00'N") || positionOfShipForSlopTanksLonFormattedFormattedTextField.getText().equals("000°00.00'W"))) {

			validations.put(Validation.F22_LAT_LON_LAT_LON_ZERO, null);
		}
		if ((!identityOfTanksTransferredCheckBox.isSelected() || identityOfTankTransferredJFXChipsView.getChips().isEmpty()) && (!identityOfTanksStoredCheckBox.isSelected() || identityOfTankStoredJFXChipsView.getChips().isEmpty())) {
			validations.put(Validation.F23_EMPTY_TANKS, null);
		}

		if (logbook2LetterF24CheckBox.isSelected() && (logbook2LetterF24LatFormattedTextField.getText().equals("00°00.00'N") || logbook2LetterF24LonFormattedTextField.getText().equals("000°00.00'W"))) {

			validations.put(Validation.F24_LAT_LON_LAT_LON_ZERO, null);
		}
		if (logbook2LetterF25CheckBox.isSelected() &&  logbook2LetterF25TimeTextField.getLocalTime()==null) {

			validations.put(Validation.F25_TIME_EMPTY, null);
		}
		if (logbook2LetterF25CheckBox.isSelected() && (logbook2LetterF25LatFormattedTextField.getText().equals("00°00.00'N") || logbook2LetterF25LonFormattedTextField.getText().equals("000°00.00'W"))) {

			validations.put(Validation.F25_LAT_LON_LAT_LON_ZERO, null);
		}
		if (!logbook2LetterF26CheckBox.isSelected() || logbook2LetterF26TotalQuantiDoubleField.getText().isEmpty()) {
			validations.put(Validation.F26_EMPTY_QUANTITY, null);
		}
	}

	public void openIdentifyOfTanksBallast(Event e) {
		selectTankerDialog.showDialog((Stage) ((ImageViewButton) e.getSource()).getScene().getWindow(), identityOfTankJFXChipsView);
	}

	public void openIdentifyOfTanksTankTransferredBallast(Event e) {
		selectTankerDialog.showDialog((Stage) ((ImageViewButton) e.getSource()).getScene().getWindow(), identityOfTankTransferredJFXChipsView);
	}

	public void openIdentifyOfTanksTankStoredBallast(Event e) {
		selectTankerDialog.showDialog((Stage) ((ImageViewButton) e.getSource()).getScene().getWindow(), identityOfTankStoredJFXChipsView);
	}
	
	public void clearFilledInfo() {
		indetityTanksBallastCheckBox.setSelected(false);
		positionOfShipForFlushingCheckBox.setSelected(false);
		portBallastCleanTanksCheckBox.setSelected(false);
		positionOfShipForSlopTanksCheckBox.setSelected(false);
		identityOfTanksTransferredCheckBox.setSelected(false);
		identityOfTanksStoredCheckBox.setSelected(false);
		logbook2LetterF24CheckBox.setSelected(false);
		logbook2LetterF25CheckBox.setSelected(false);
		logbook2LetterF26CheckBox.setSelected(false);

	}
}
