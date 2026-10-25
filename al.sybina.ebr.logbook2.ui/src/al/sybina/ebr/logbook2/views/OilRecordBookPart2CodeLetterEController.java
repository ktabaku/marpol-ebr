package al.sybina.ebr.logbook2.views;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
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
import al.sybina.ebr.logbook.commons.BallonMessage;
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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import jfxtras.scene.control.ImageViewButton;
import jfxtras.scene.control.LocalTimeTextField;
import jidefx.scene.control.field.DoubleField;
import jidefx.scene.control.field.FormattedTextField;

public class OilRecordBookPart2CodeLetterEController implements Initializable {

	private Label label1 = new Label(" Quantity of ballast received. Indicate total quantity \r\n" + "of ballast for each tank involved in the operation, in cubic metres" + "");

	private static final Logger logger = LoggerCreator.createLogger(OilRecordBookPart2CodeLetterEController.class);

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
	private CheckBox positionOfShipBallastCheckBox;
	@FXML
	private FormattedTextField positionOfShipAtStartOfBallastingLatTextField;
	@FXML
	private FormattedTextField positionOfShipAtStartOfBallastingLonTextField;
	@FXML
	private FormattedTextField positionOfShipAtEndOfBallastingLatTextField;
	@FXML
	private FormattedTextField positionOfShipAtEndOfBallastingTextLonField;
	@FXML
	private CheckBox ballastingProcessCheckBox;
	@FXML
	private CheckBox indetityTanksBallastCheckBox;
	@FXML
	private CheckBox timeAtStartBallastCheckBox;
	@FXML
	private CheckBox totalQuantityBallastRecivedCheckBox;
	@FXML
	private LocalTimeTextField timeAtStartBallastTextField;
	@FXML
	private LocalTimeTextField timeAtEndBallastTextField;
	@FXML
	private DoubleField totalQuantityBallstRecivedDoubleField;
//	@FXML
//	private TextField quantityBallstRecivedTextFIeld1;
	@FXML
	private Button addOperationButton;
	@FXML
	private TextField totalQuantityOfOilLoadedTextField;
	@FXML
	private TextField totalContentOfTanksTextField;
	@FXML
	private ImageViewButton infoLetterAImageViewButton;
	@FXML
	private ImageViewButton logbook2LetterEQuantityImageViewButton;
	@FXML
	private ImageViewButton identityOfTanksImageViewButton;
	@FXML
	private JFXChipView<String> identityOfTankJFXChipsView;
	@FXML
	public AnchorPane codeLetterContentAnchorPane;
	private ObservableList<Image> helpImageList = FXCollections.observableArrayList();
	private DateTimeFormatter dateTimeFormatter = EasyDateUtils.logbookPdfDateTimeFormatter;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MathUtils.setStringConverter(totalQuantityBallstRecivedDoubleField);
		try {
			Image helpImageLetterE2 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPE2));
			
			Image helpImageLetterE1 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPE1));
			
			Image helpImageLetterE3 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPE3));
			
			Image helpImageLetterE4 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPE4));
			
			helpImageList.addAll(helpImageLetterE4, helpImageLetterE1, helpImageLetterE3, helpImageLetterE2);
			
			Image addImageIdentityOfTanks = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_ADD));
			identityOfTanksImageViewButton.setImage(addImageIdentityOfTanks);

			Image infoIconImageInfoLetterE = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
			infoLetterAImageViewButton.setImage(infoIconImageInfoLetterE);
			logbook2LetterEQuantityImageViewButton.setImage(infoIconImageInfoLetterE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		CoordinateUIUtils.initializeLatitudeField(positionOfShipAtStartOfBallastingLatTextField);
		CoordinateUIUtils.initializeLongitudeField(positionOfShipAtStartOfBallastingLonTextField);
		CoordinateUIUtils.initializeLatitudeField(positionOfShipAtEndOfBallastingLatTextField);
		CoordinateUIUtils.initializeLongitudeField(positionOfShipAtEndOfBallastingTextLonField);
		JavaFXUtils.addFilledPseudoClass(positionOfShipAtStartOfBallastingLatTextField);
		JavaFXUtils.addFilledPseudoClass(positionOfShipAtStartOfBallastingLonTextField);
		JavaFXUtils.addFilledPseudoClass(positionOfShipAtEndOfBallastingLatTextField);
		JavaFXUtils.addFilledPseudoClass(positionOfShipAtEndOfBallastingTextLonField);
		JavaFXUtils.addFilledPseudoClass(totalQuantityBallstRecivedDoubleField);
		JavaFXUtils.addFilledPseudoClass(timeAtStartBallastTextField);
		JavaFXUtils.addFilledPseudoClass(timeAtEndBallastTextField);
		timeAtStartBallastTextField.setDateTimeFormatter(EasyDateUtils.logbookTimeFormatter);
		timeAtEndBallastTextField.setDateTimeFormatter(EasyDateUtils.logbookTimeFormatter);
		MathUtils.setStringConverter(totalQuantityBallstRecivedDoubleField);
		MathUtils.setMinMaxValues(totalQuantityBallstRecivedDoubleField, 0.0, null);
		positionOfShipAtStartOfBallastingLatTextField.setDisable(true);
		positionOfShipAtStartOfBallastingLonTextField.setDisable(true);
		positionOfShipAtEndOfBallastingLatTextField.setDisable(true);
		positionOfShipAtEndOfBallastingTextLonField.setDisable(true);
		timeAtStartBallastTextField.setDisable(true);
		timeAtEndBallastTextField.setDisable(true);

		indetityTanksBallastCheckBox.setDisable(true);
		timeAtStartBallastCheckBox.setDisable(true);
		totalQuantityBallastRecivedCheckBox.setDisable(true);
		totalQuantityBallstRecivedDoubleField.setDisable(true);
		// quantityBallstRecivedTextFIeld1.setDisable(true);
		identityOfTanksImageViewButton.setDisable(true);

		positionOfShipBallastCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				ballastingProcessCheckBox.setDisable(false);
				ballastingProcessCheckBox.setSelected(true);

				indetityTanksBallastCheckBox.setDisable(false);
				indetityTanksBallastCheckBox.setSelected(true);

				timeAtStartBallastCheckBox.setDisable(false);
				timeAtStartBallastCheckBox.setSelected(true);

				totalQuantityBallastRecivedCheckBox.setDisable(false);
				totalQuantityBallastRecivedCheckBox.setSelected(true);
				positionOfShipAtStartOfBallastingLatTextField.setDisable(false);
				positionOfShipAtStartOfBallastingLonTextField.setDisable(false);
				positionOfShipAtEndOfBallastingLatTextField.setDisable(false);
				positionOfShipAtEndOfBallastingTextLonField.setDisable(false);

				timeAtStartBallastTextField.setDisable(false);
				timeAtEndBallastTextField.setDisable(false);

			} else {
				ballastingProcessCheckBox.setSelected(false);
				indetityTanksBallastCheckBox.setDisable(true);
				indetityTanksBallastCheckBox.setSelected(false);
				timeAtStartBallastCheckBox.setDisable(true);
				timeAtStartBallastCheckBox.setSelected(false);
				totalQuantityBallastRecivedCheckBox.setDisable(true);
				totalQuantityBallastRecivedCheckBox.setSelected(false);
				positionOfShipAtStartOfBallastingLatTextField.setDisable(true);
				positionOfShipAtStartOfBallastingLonTextField.setDisable(true);
				positionOfShipAtEndOfBallastingLatTextField.setDisable(true);
				positionOfShipAtEndOfBallastingTextLonField.setDisable(true);
				timeAtStartBallastTextField.setDisable(true);
				timeAtEndBallastTextField.setDisable(true);
				identityOfTanksImageViewButton.setDisable(true);
				positionOfShipAtStartOfBallastingLatTextField.setText("00°00.00'N");
				positionOfShipAtStartOfBallastingLonTextField.setText("000°00.00'W");
				positionOfShipAtEndOfBallastingLatTextField.setText("00°00.00'N");
				positionOfShipAtEndOfBallastingTextLonField.setText("00°00.00'N");
			}
		});

		ballastingProcessCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				indetityTanksBallastCheckBox.setDisable(false);
				timeAtStartBallastCheckBox.setDisable(false);
				totalQuantityBallastRecivedCheckBox.setDisable(false);

			} else {
				indetityTanksBallastCheckBox.setDisable(true);
				timeAtStartBallastCheckBox.setDisable(true);
				totalQuantityBallastRecivedCheckBox.setDisable(true);
				indetityTanksBallastCheckBox.setSelected(false);
				timeAtStartBallastCheckBox.setSelected(false);
				totalQuantityBallastRecivedCheckBox.setSelected(false);

			}
		});
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
		timeAtStartBallastCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				timeAtStartBallastTextField.setDisable(false);
				timeAtEndBallastTextField.setDisable(false);
			} else {
				timeAtStartBallastTextField.setDisable(true);
				timeAtEndBallastTextField.setDisable(true);
				timeAtStartBallastTextField.setLocalTime(null);
				timeAtEndBallastTextField.setLocalTime(null);
			}
		});

		totalQuantityBallastRecivedCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				totalQuantityBallstRecivedDoubleField.setDisable(false);
			} else {
				totalQuantityBallstRecivedDoubleField.setDisable(true);
				totalQuantityBallstRecivedDoubleField.setText("");
			}
		});

		identityOfTankJFXChipsView.setUserData(new TankUsageContext(Stream.of(TankType.BALLAST, TankType.CARGO, TankType.FUELOIL, TankType.OILYBILGE, TankType.SLOP, TankType.SLUDGE).collect(Collectors.toList()), true, true));
		addRemoveListener(identityOfTankJFXChipsView);
		identityOfTankJFXChipsView.getChips().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> c) {
				if (indetityTanksBallastCheckBox.isSelected()) {
					Double totalUserTankQuantities = ((TankUsageContext) identityOfTankJFXChipsView.getUserData()).getTotalUserTankQuantities();
					Double totalUserTankActualLevel = ((TankUsageContext) identityOfTankJFXChipsView.getUserData()).getTotalUserTankActualLevel();
					totalQuantityBallstRecivedDoubleField.setValue(totalUserTankQuantities);
					totalQuantityBallastRecivedCheckBox.setSelected(true);
					// totalContentOfTanksDoubleField.setValue(totalUserTankActualLevel+totalUserTankQuantities);
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

	public void processE(OilLog logToInsert, List<OilLog> logToInsertList, Map<Validation, Object> validations) {
		validateFields(logToInsert.getBusinessDate(), validations);
		if (positionOfShipBallastCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("18");
			log.setOrderInGroup("S");
			log.setContent("LAT:" + positionOfShipAtStartOfBallastingLatTextField.getText() + " LONG:" + positionOfShipAtStartOfBallastingLonTextField.getText() + "\r\n" + "LAT:" + positionOfShipAtEndOfBallastingLatTextField.getText()
					+ " LONG:" + positionOfShipAtEndOfBallastingTextLonField.getText());
			logToInsertList.add(log);
		}
		if (indetityTanksBallastCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("19.1");
			log.setOrderInGroup("T");
			log.setContent("" + String.join(", ", identityOfTankJFXChipsView.getChips()));
			log.setUserTankActualLevels(((TankUsageContext) identityOfTankJFXChipsView.getUserData()).getUserTankActualLevels());
			logToInsertList.add(log);
		}
		if (timeAtStartBallastCheckBox.isSelected()  && (timeAtStartBallastTextField.getLocalTime()!=null && timeAtEndBallastTextField.getLocalTime()!=null)) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("19.2");
			log.setOrderInGroup("U");
			log.setContent("TIME:" + timeAtStartBallastTextField.getLocalTime().format(EasyDateUtils.logbookTimeFormatter) + " TIME:" + timeAtEndBallastTextField.getLocalTime().format(EasyDateUtils.logbookTimeFormatter));
			logToInsertList.add(log);
		}
		if (totalQuantityBallastRecivedCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("19.3");
			log.setOrderInGroup("V");
			log.setContent("" + totalQuantityBallstRecivedDoubleField.getText() + " M3");
			logToInsertList.add(log);
		}

	}

	private void validateFields(Timestamp businessDate, Map<Validation, Object> validations) {
		if (!positionOfShipBallastCheckBox.isSelected()) {
			validations.put(Validation.E18_EMPTY, null);
		}
		if (!ballastingProcessCheckBox.isSelected()) {
			validations.put(Validation.E19_EMPTY, null);
		}
		if (!indetityTanksBallastCheckBox.isSelected() || identityOfTankJFXChipsView.getChips().isEmpty()) {
			validations.put(Validation.E19_TANKS_EMPTY, null);
		}
		if (timeAtStartBallastCheckBox.isSelected() && (timeAtStartBallastTextField.getLocalTime()==null || timeAtEndBallastTextField.getLocalTime()==null)) {
			validations.put(Validation.E19_TIME_EMPTY, null);
		}
		if ((timeAtStartBallastTextField.getLocalTime()!=null && timeAtEndBallastTextField.getLocalTime()!=null) && timeAtStartBallastTextField.getLocalTime().isAfter(timeAtEndBallastTextField.getLocalTime())) {
			validations.put(Validation.E19_TIME_AT_START_AFTER_TIME_AT_STOP, null);
		}
		if (!totalQuantityBallastRecivedCheckBox.isSelected() || totalQuantityBallstRecivedDoubleField.getText().isEmpty()) {
			validations.put(Validation.E19_QUANTITY_EMPTY, null);

		}
		if (positionOfShipBallastCheckBox.isSelected() && (positionOfShipAtStartOfBallastingLatTextField.getText().equals("00°00.00'N") || positionOfShipAtStartOfBallastingLonTextField.getText().equals("000°00.00'W")
				|| positionOfShipAtEndOfBallastingLatTextField.getText().equals("00°00.00'N") || positionOfShipAtEndOfBallastingTextLonField.getText().equals("000°00.00'W"))) {
			validations.put(Validation.E18_LAT_LON_LAT_LON_ZERO, null);
		}
	}

	public void showBalloon(MouseEvent event) {
		ballonMessage.showGeneralBalloon((ImageViewButton) event.getTarget(), label1);
	}

	public void letterE(MouseEvent event) {
		helpCodeLetterDialog.showDialog((Stage)((ImageViewButton)event.getSource()).getScene().getWindow(),helpImageList);
	}

	public void openIdentifyOfTanksWashed(Event e) {
		selectTankerDialog.showDialog((Stage) ((ImageViewButton) e.getSource()).getScene().getWindow(), identityOfTankJFXChipsView);
	}

	public void clearFilledInfo() {
		ballastingProcessCheckBox.setSelected(false);
		positionOfShipBallastCheckBox.setSelected(false);
		indetityTanksBallastCheckBox.setDisable(true);
		indetityTanksBallastCheckBox.setSelected(false);
		timeAtStartBallastCheckBox.setDisable(true);
		timeAtStartBallastCheckBox.setSelected(false);
		totalQuantityBallastRecivedCheckBox.setDisable(true);
		totalQuantityBallastRecivedCheckBox.setSelected(false);
		positionOfShipAtStartOfBallastingLatTextField.setDisable(true);
		positionOfShipAtStartOfBallastingLonTextField.setDisable(true);
		positionOfShipAtEndOfBallastingLatTextField.setDisable(true);
		positionOfShipAtEndOfBallastingTextLonField.setDisable(true);
		timeAtStartBallastTextField.setDisable(true);
		timeAtEndBallastTextField.setDisable(true);
		identityOfTankJFXChipsView.getChips().clear();
		((TankUsageContext) identityOfTankJFXChipsView.getUserData()).getUserTankQuantities().clear();
	}
}
