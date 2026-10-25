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

public class OilRecordBookPart2CodeLetterIController implements Initializable {

	private Label label1 = new Label("Notes:\r\n" + "The time of settling depends upon the motion\r\n" + "of the ship as well as on the type of the \r\n" + "previous cargo. Oil settling in slop tank for at\r\n"
			+ "l east 36 hours should be carried out before \r\n" + "discharge of any water from the slop tanks \r\n" + "to the sea. \r\n" + "" + "");

	private Label label2 = new Label("The item 43 is used alternatively to item \r\n" + "42 in cases when the discharge follows a \r\n" + "previous discharge from the same tank \r\n" + "without in the meantime new washings to \r\n"
			+ "have been added to the tank.\r\n" + "" + "");

	private static final Logger logger = LoggerCreator.createLogger(OilRecordBookPart2CodeLetterIController.class);

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
	private ImageViewButton infoLetterIImageViewButton;
	@FXML
	private ImageViewButton infoLetterI3ImageViewButton;
	@FXML
	private ImageViewButton infoLetterI2ImageViewButton;
	@FXML
	private CheckBox identifyOfTanksCheckBox;
	@FXML
	private ImageViewButton identityOfTanksImageViewButton;
	@FXML
	private JFXChipView<String> identifyOfTanksJFXChipView;

	@FXML
	private CheckBox logBookLetterI42CheckBox;
	@FXML
	private CheckBox logBookLetterI43CheckBox;
	@FXML
	private DoubleField logBookLetterIHoursDoubleField;

	@FXML
	private CheckBox timeAndPositionAtStartCheckBox;
	@FXML
	private LocalTimeTextField timeAtStartLocalTimeTextField;
	@FXML
	private FormattedTextField latAtStartFormattedTextField;
	@FXML
	private FormattedTextField longAtStartFormattedTextField;

	@FXML
	private CheckBox logbookLetterI45CheckBox;
	@FXML
	private DoubleField totalContentsDoubleField;
	@FXML
	private CheckBox logbookLetterI46CheckBox;
	@FXML
	private DoubleField totalOilContentsDoubleField;
	@FXML
	private CheckBox logbookLetterI47CheckBox;
	@FXML
	private DoubleField bulkTotalQuantityDoubleField;
	@FXML
	private DoubleField bulkTotalQuantityForHoursDoubleField;
	@FXML
	private CheckBox logbookLetterI48CheckBox;
	@FXML
	private DoubleField finalTotalQuantityDoubleField;
	@FXML
	private DoubleField finalTotalQuantityForHoursDoubleField;

	@FXML
	private CheckBox timeAndPositionAtEndCheckBox;
	@FXML
	private LocalTimeTextField timeAtEndLocalTimeTextField;
	@FXML
	private FormattedTextField latAtEndFormattedTextField;
	@FXML
	private FormattedTextField longAtEndFormattedTextField;

	@FXML
	private CheckBox logbookLetterI50ControlCheckBox;
	@FXML
	private CheckBox logbookLetterI50YESCheckBox;
	@FXML
	private CheckBox logbookLetterI50NOTCheckBox;
	@FXML
	private TextField logbookLetterI50TextField;

	@FXML
	private CheckBox logbookLetterI51QuantityCheckBox;
	@FXML
	private DoubleField logbookLetterI51QuantityDoubleField;

	@FXML
	private CheckBox logbookLetterI52ShipSpeedCheckBox;
	@FXML
	private DoubleField logbookLetterI52ShipSpeedDoubleField;

	@FXML
	private CheckBox logbookLetterI53ControlCheckBox;
	@FXML
	private CheckBox logbookLetterI53YESCheckBox;
	@FXML
	private CheckBox logbookLetterI53NOTCheckBox;
	@FXML
	private TextField logbookLetterI53TextField;

	@FXML
	private CheckBox logbookLetterI54ControlCheckBox;
	@FXML
	private CheckBox logbookLetterI54YESCheckBox;
	@FXML
	private CheckBox logbookLetterI54NOTCheckBox;
	@FXML
	private TextField logbookLetterI54TextField;
	@FXML
	public AnchorPane codeLetterContentAnchorPane;
	private DateTimeFormatter timeFormatter = EasyDateUtils.logbookTimeFormatter;
	private ObservableList<Image> helpImageList = FXCollections.observableArrayList();
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MathUtils.setStringConverter(logBookLetterIHoursDoubleField);
		MathUtils.setStringConverter(totalContentsDoubleField);
		MathUtils.setStringConverter(totalOilContentsDoubleField);
		MathUtils.setStringConverter(bulkTotalQuantityDoubleField);
		MathUtils.setStringConverter(bulkTotalQuantityForHoursDoubleField);
		MathUtils.setStringConverter(finalTotalQuantityDoubleField);
		MathUtils.setStringConverter(finalTotalQuantityForHoursDoubleField);
		MathUtils.setStringConverter(logbookLetterI51QuantityDoubleField);
		MathUtils.setStringConverter(logbookLetterI52ShipSpeedDoubleField);
		MathUtils.setMinMaxValues(logBookLetterIHoursDoubleField, 0.0, null);
		MathUtils.setMinMaxValues(totalContentsDoubleField, 0.0, null);
		MathUtils.setMinMaxValues(totalOilContentsDoubleField, 0.0, null);
		MathUtils.setMinMaxValues(bulkTotalQuantityDoubleField, 0.0, null);
		MathUtils.setMinMaxValues(bulkTotalQuantityForHoursDoubleField, 0.0, null);
		MathUtils.setMinMaxValues(finalTotalQuantityDoubleField, 0.0, null);
		MathUtils.setMinMaxValues(finalTotalQuantityForHoursDoubleField, 0.0, null);
		MathUtils.setMinMaxValues(logbookLetterI51QuantityDoubleField, 0.0, null);
		MathUtils.setMinMaxValues(logbookLetterI52ShipSpeedDoubleField, 0.0, null);
		try {

			Image helpImageLetterI2 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPI2));
			
			Image helpImageLetterI1 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPI1));
			
			Image helpImageLetterI3 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPI3));
			
			Image helpImageLetterI4 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPI4));
			
			helpImageList.addAll(helpImageLetterI4, helpImageLetterI1, helpImageLetterI2, helpImageLetterI3);
			
			Image addImageIdentityOfTanks = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_ADD));
			identityOfTanksImageViewButton.setImage(addImageIdentityOfTanks);

			Image infoIconImageInfoLetterA = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
			infoLetterIImageViewButton.setImage(infoIconImageInfoLetterA);
			infoLetterI3ImageViewButton.setImage(infoIconImageInfoLetterA);
			infoLetterI2ImageViewButton.setImage(infoIconImageInfoLetterA);

		} catch (IOException e) {
			e.printStackTrace();
		}

		identityOfTanksImageViewButton.setDisable(true);
		identifyOfTanksJFXChipView.setDisable(true);
		logbookLetterI50TextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		logbookLetterI53TextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		logbookLetterI54TextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		JavaFXUtils.addFilledPseudoClass(logBookLetterIHoursDoubleField);
		JavaFXUtils.addFilledPseudoClass(latAtStartFormattedTextField);
		JavaFXUtils.addFilledPseudoClass(longAtStartFormattedTextField);
		JavaFXUtils.addFilledPseudoClass(totalContentsDoubleField);
		JavaFXUtils.addFilledPseudoClass(totalOilContentsDoubleField);
		JavaFXUtils.addFilledPseudoClass(bulkTotalQuantityDoubleField);
		JavaFXUtils.addFilledPseudoClass(bulkTotalQuantityForHoursDoubleField);
		JavaFXUtils.addFilledPseudoClass(finalTotalQuantityDoubleField);
		JavaFXUtils.addFilledPseudoClass(finalTotalQuantityForHoursDoubleField);
		JavaFXUtils.addFilledPseudoClass(latAtEndFormattedTextField);
		JavaFXUtils.addFilledPseudoClass(longAtEndFormattedTextField);
		JavaFXUtils.addFilledPseudoClass(logbookLetterI50TextField);
		JavaFXUtils.addFilledPseudoClass(logbookLetterI52ShipSpeedDoubleField);
		JavaFXUtils.addFilledPseudoClass(logbookLetterI51QuantityDoubleField);
		JavaFXUtils.addFilledPseudoClass(logbookLetterI53TextField);
		JavaFXUtils.addFilledPseudoClass(logbookLetterI54TextField);
		JavaFXUtils.addFilledPseudoClass(timeAtStartLocalTimeTextField);
		JavaFXUtils.addFilledPseudoClass(timeAtEndLocalTimeTextField);
		timeAtStartLocalTimeTextField.setDateTimeFormatter(EasyDateUtils.logbookTimeFormatter);
		timeAtEndLocalTimeTextField.setDateTimeFormatter(EasyDateUtils.logbookTimeFormatter);
		identifyOfTanksCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				identityOfTanksImageViewButton.setDisable(false);
				identifyOfTanksJFXChipView.setDisable(false);
				logBookLetterI42CheckBox.setSelected(true);
				timeAndPositionAtStartCheckBox.setSelected(true);
				logbookLetterI45CheckBox.setSelected(true);
				logbookLetterI46CheckBox.setSelected(true);
				logbookLetterI47CheckBox.setSelected(true);
				logbookLetterI48CheckBox.setSelected(true);
				timeAndPositionAtEndCheckBox.setSelected(true);
				logbookLetterI50ControlCheckBox.setSelected(true);
				logbookLetterI51QuantityCheckBox.setSelected(true);
				logbookLetterI52ShipSpeedCheckBox.setSelected(true);
				logbookLetterI53ControlCheckBox.setSelected(true);
				logbookLetterI54ControlCheckBox.setSelected(true);
			} else {
				identityOfTanksImageViewButton.setDisable(true);
				identifyOfTanksJFXChipView.getChips().clear();
				identifyOfTanksJFXChipView.setDisable(true);
				logBookLetterI42CheckBox.setSelected(false);
				timeAndPositionAtStartCheckBox.setSelected(false);
				logbookLetterI45CheckBox.setSelected(false);
				logbookLetterI46CheckBox.setSelected(false);
				logbookLetterI47CheckBox.setSelected(false);
				logbookLetterI48CheckBox.setSelected(false);
				timeAndPositionAtEndCheckBox.setSelected(false);
				logbookLetterI50ControlCheckBox.setSelected(false);
				logbookLetterI51QuantityCheckBox.setSelected(false);
				logbookLetterI52ShipSpeedCheckBox.setSelected(false);
				logbookLetterI53ControlCheckBox.setSelected(false);
				logbookLetterI54ControlCheckBox.setSelected(false);
			}

		});
		TankUsageContext tankUsageContext = new TankUsageContext(Stream.of(TankType.BALLAST, TankType.CARGO, TankType.FUELOIL, TankType.OILYBILGE, TankType.SLOP, TankType.SLUDGE).collect(Collectors.toList()), false, true);
		identifyOfTanksJFXChipView.setUserData(tankUsageContext);
		addRemoveListener(identifyOfTanksJFXChipView);
		identifyOfTanksJFXChipView.getChips().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> change) {
				if (identifyOfTanksCheckBox.isSelected()) {
					Double totalUserTankQuantities = ((TankUsageContext) identifyOfTanksJFXChipView.getUserData()).getTotalUserTankQuantities();
					Double totalUserTankActualLevel = ((TankUsageContext) identifyOfTanksJFXChipView.getUserData()).getTotalUserTankActualLevel();
					// slopCargoQuantityDoubleField.setValue(totalUserTankQuantities);
				}
				JavaFXUtils.addTooltip(identifyOfTanksJFXChipView);
			}

		});
		logBookLetterIHoursDoubleField.setDisable(true);
		logBookLetterI42CheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logBookLetterIHoursDoubleField.setDisable(false);
				logBookLetterI43CheckBox.setSelected(false);

			} else {
				if (!logBookLetterI42CheckBox.isSelected() && !logBookLetterI43CheckBox.isSelected()) {
					logBookLetterIHoursDoubleField.setDisable(true);
					logBookLetterIHoursDoubleField.setText("");
				}
			}

		});
		logBookLetterI43CheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logBookLetterIHoursDoubleField.setDisable(false);
				logBookLetterI42CheckBox.setSelected(false);

			} else {

				if (!logBookLetterI42CheckBox.isSelected() && !logBookLetterI43CheckBox.isSelected()) {
					logBookLetterIHoursDoubleField.setDisable(true);
					logBookLetterIHoursDoubleField.setText("");
				}

			}

		});
		timeAtStartLocalTimeTextField.setDisable(true);
		latAtStartFormattedTextField.setDisable(true);
		longAtStartFormattedTextField.setDisable(true);
		CoordinateUIUtils.initializeLatitudeField(latAtStartFormattedTextField);
		CoordinateUIUtils.initializeLongitudeField(longAtStartFormattedTextField);

		timeAndPositionAtStartCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				timeAtStartLocalTimeTextField.setDisable(false);
				latAtStartFormattedTextField.setDisable(false);
				longAtStartFormattedTextField.setDisable(false);

			} else {
				timeAtStartLocalTimeTextField.setDisable(true);
				latAtStartFormattedTextField.setDisable(true);
				longAtStartFormattedTextField.setDisable(true);
				timeAtStartLocalTimeTextField.setLocalTime(null);
				latAtStartFormattedTextField.setText("00°00.00'N");
				longAtStartFormattedTextField.setText("000°00.00'W");

			}

		});
		totalContentsDoubleField.setDisable(true);
		totalContentsDoubleField.setText("");

		logbookLetterI45CheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				totalContentsDoubleField.setDisable(false);
			} else {
				totalContentsDoubleField.setDisable(true);
				totalContentsDoubleField.setText("");

			}

		});
		totalOilContentsDoubleField.setDisable(true);
		totalOilContentsDoubleField.setText("");

		logbookLetterI46CheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				totalOilContentsDoubleField.setDisable(false);
			} else {
				totalOilContentsDoubleField.setDisable(true);
				totalOilContentsDoubleField.setText("");

			}

		});

		bulkTotalQuantityDoubleField.setDisable(true);
		bulkTotalQuantityForHoursDoubleField.setDisable(true);
		bulkTotalQuantityDoubleField.setText("");
		bulkTotalQuantityForHoursDoubleField.setText("");
		logbookLetterI47CheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				bulkTotalQuantityDoubleField.setDisable(false);
				bulkTotalQuantityForHoursDoubleField.setDisable(false);
			} else {
				bulkTotalQuantityDoubleField.setDisable(true);
				bulkTotalQuantityForHoursDoubleField.setDisable(true);
				bulkTotalQuantityDoubleField.setText("");
				bulkTotalQuantityForHoursDoubleField.setText("");

			}

		});

		finalTotalQuantityDoubleField.setDisable(true);
		finalTotalQuantityForHoursDoubleField.setDisable(true);
		finalTotalQuantityDoubleField.setText("");
		finalTotalQuantityForHoursDoubleField.setText("");
		logbookLetterI48CheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				finalTotalQuantityDoubleField.setDisable(false);
				finalTotalQuantityForHoursDoubleField.setDisable(false);
			} else {
				finalTotalQuantityDoubleField.setDisable(true);
				finalTotalQuantityForHoursDoubleField.setDisable(true);
				finalTotalQuantityDoubleField.setText("");
				finalTotalQuantityForHoursDoubleField.setText("");

			}

		});
		timeAtEndLocalTimeTextField.setDisable(true);
		latAtEndFormattedTextField.setDisable(true);
		longAtEndFormattedTextField.setDisable(true);
		CoordinateUIUtils.initializeLatitudeField(latAtEndFormattedTextField);
		CoordinateUIUtils.initializeLongitudeField(longAtEndFormattedTextField);

		timeAndPositionAtEndCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				timeAtEndLocalTimeTextField.setDisable(false);
				latAtEndFormattedTextField.setDisable(false);
				longAtEndFormattedTextField.setDisable(false);

			} else {
				timeAtEndLocalTimeTextField.setDisable(true);
				latAtEndFormattedTextField.setDisable(true);
				longAtEndFormattedTextField.setDisable(true);
				timeAtEndLocalTimeTextField.setLocalTime(null);
				latAtEndFormattedTextField.setText("00°00.00'N");
				longAtEndFormattedTextField.setText("000°00.00'W");

			}

		});

		logbookLetterI50YESCheckBox.setDisable(true);
		logbookLetterI50NOTCheckBox.setDisable(true);
		logbookLetterI50TextField.setDisable(true);

		logbookLetterI50ControlCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbookLetterI50YESCheckBox.setDisable(false);
				logbookLetterI50NOTCheckBox.setDisable(false);

			} else {
				logbookLetterI50YESCheckBox.setDisable(true);
				logbookLetterI50NOTCheckBox.setDisable(true);
				logbookLetterI50YESCheckBox.setSelected(false);
				logbookLetterI50NOTCheckBox.setSelected(false);
				logbookLetterI50TextField.setDisable(true);
				logbookLetterI50TextField.setText("");

			}

		});

		logbookLetterI50YESCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbookLetterI50NOTCheckBox.setSelected(false);
			} else {
			}
		});
		logbookLetterI50NOTCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbookLetterI50YESCheckBox.setSelected(false);
				logbookLetterI50TextField.setDisable(false);
			} else {
				logbookLetterI50TextField.setDisable(true);
				logbookLetterI50TextField.setText("");
			}
		});

		logbookLetterI51QuantityDoubleField.setDisable(true);
		logbookLetterI51QuantityDoubleField.setText("");

		logbookLetterI51QuantityCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbookLetterI51QuantityDoubleField.setDisable(false);
			} else {
				logbookLetterI51QuantityDoubleField.setDisable(true);
				logbookLetterI51QuantityDoubleField.setText("");

			}
		});
		logbookLetterI52ShipSpeedDoubleField.setDisable(true);
		logbookLetterI52ShipSpeedDoubleField.setText("");

		logbookLetterI52ShipSpeedCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbookLetterI52ShipSpeedDoubleField.setDisable(false);
			} else {
				logbookLetterI52ShipSpeedDoubleField.setDisable(true);
				logbookLetterI52ShipSpeedDoubleField.setText("");

			}
		});

		logbookLetterI53YESCheckBox.setDisable(true);
		logbookLetterI53NOTCheckBox.setDisable(true);
		logbookLetterI53TextField.setDisable(true);

		logbookLetterI53ControlCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbookLetterI53YESCheckBox.setDisable(false);
				logbookLetterI53NOTCheckBox.setDisable(false);

			} else {
				logbookLetterI53YESCheckBox.setDisable(true);
				logbookLetterI53NOTCheckBox.setDisable(true);
				logbookLetterI53YESCheckBox.setSelected(false);
				logbookLetterI53NOTCheckBox.setSelected(false);
				logbookLetterI53TextField.setDisable(true);
				logbookLetterI53TextField.setText("");

			}

		});

		logbookLetterI53YESCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbookLetterI53NOTCheckBox.setSelected(false);
			} else {
			}
		});
		logbookLetterI53NOTCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbookLetterI53YESCheckBox.setSelected(false);
				logbookLetterI53TextField.setDisable(false);
			} else {
				logbookLetterI53TextField.setDisable(true);
				logbookLetterI53TextField.setText("");
			}
		});

		logbookLetterI54YESCheckBox.setDisable(true);
		logbookLetterI54NOTCheckBox.setDisable(true);
		logbookLetterI54TextField.setDisable(true);

		logbookLetterI54ControlCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbookLetterI54YESCheckBox.setDisable(false);
				logbookLetterI54NOTCheckBox.setDisable(false);
			} else {
				logbookLetterI54YESCheckBox.setDisable(true);
				logbookLetterI54NOTCheckBox.setDisable(true);
				logbookLetterI54YESCheckBox.setSelected(false);
				logbookLetterI54NOTCheckBox.setSelected(false);
				logbookLetterI54TextField.setDisable(true);
				logbookLetterI54TextField.setText("");

			}

		});

		logbookLetterI54YESCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbookLetterI54NOTCheckBox.setSelected(false);
			} else {
			}
		});
		logbookLetterI54NOTCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbookLetterI54YESCheckBox.setSelected(false);
				logbookLetterI54TextField.setDisable(false);
			} else {
				logbookLetterI54TextField.setDisable(true);
				logbookLetterI54TextField.setText("");
			}
		});

		eventBroker.subscribe(Activator.MARPOL_DETAILS_RELOADED, new org.osgi.service.event.EventHandler() {
			@Override
			public void handleEvent(org.osgi.service.event.Event event) {
				ArrayList<Long> changedTankIds = (ArrayList<Long>) event.getProperty(Activator.TANK_IDS);
				if (changedTankIds == null) {
					identifyOfTanksJFXChipView.getChips().clear();
				} else if (changedTankIds != null && !changedTankIds.isEmpty()) {
					Activator.removeChangedTanksFromJFXChipView(changedTankIds, identifyOfTanksJFXChipView);
				}
			}
		});
	}

	public void openIdentifyOfTanksI41(Event e) {
		selectTankerDialog.showDialog((Stage) ((ImageViewButton) e.getSource()).getScene().getWindow(), identifyOfTanksJFXChipView);
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

	public void letterI(MouseEvent event) {
		helpCodeLetterDialog.showDialog((Stage)((ImageViewButton)event.getSource()).getScene().getWindow(),helpImageList);
	}

	public void processI(OilLog logToInsert, List<OilLog> logToInsertList, Map<Validation, Object> validations) {
		validateFields(logToInsert.getBusinessDate(), validations);
		if (identifyOfTanksCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("41");
			log.setOrderInGroup("ZT");
			log.setContent("" + String.join(", ", identifyOfTanksJFXChipView.getChips()));
			log.setUserTankActualLevels(((TankUsageContext) identifyOfTanksJFXChipView.getUserData()).getUserTankActualLevels());
			logToInsertList.add(log);
		}
		if (logBookLetterI42CheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("42");
			log.setOrderInGroup("ZU");
			log.setContent(logBookLetterIHoursDoubleField.getText() + " HOURS");
			logToInsertList.add(log);
		}
		if (logBookLetterI43CheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("43");
			log.setOrderInGroup("ZV");
			log.setContent(logBookLetterIHoursDoubleField.getText() + " HOURS");
			logToInsertList.add(log);
		}

		if (timeAndPositionAtStartCheckBox.isSelected()&&timeAtStartLocalTimeTextField.getLocalTime()!=null) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("44");
			log.setOrderInGroup("ZW");
			log.setContent("TIME " + timeAtStartLocalTimeTextField.getLocalTime().format(timeFormatter) + "  LAT:" + latAtStartFormattedTextField.getText() + " LONG:" + longAtStartFormattedTextField.getText());
			logToInsertList.add(log);
		}

		if (logbookLetterI45CheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("45");
			log.setOrderInGroup("ZX");
			log.setContent(totalContentsDoubleField.getText() + "MTRS");
			logToInsertList.add(log);
		}
		if (logbookLetterI46CheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("46");
			log.setOrderInGroup("ZY");
			log.setContent(totalOilContentsDoubleField.getText() + "MTRS");
			logToInsertList.add(log);
		}
		if (logbookLetterI47CheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("47");
			log.setOrderInGroup("ZZ");
			log.setContent(bulkTotalQuantityDoubleField.getText() + "M3 " + bulkTotalQuantityForHoursDoubleField.getText() + "M3/H ");
			logToInsertList.add(log);
		}
		if (logbookLetterI48CheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("48");
			log.setOrderInGroup("ZZA");
			log.setContent(finalTotalQuantityDoubleField.getText() + "M3 " + finalTotalQuantityForHoursDoubleField.getText() + "M3/H ");
			logToInsertList.add(log);
		}

		if (timeAndPositionAtEndCheckBox.isSelected()&&timeAtEndLocalTimeTextField.getLocalTime()!=null) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("49");
			log.setOrderInGroup("ZZB");
			log.setContent("TIME " + timeAtEndLocalTimeTextField.getLocalTime().format(timeFormatter) + "  LAT:" + latAtEndFormattedTextField.getText() + " LONG:" + longAtEndFormattedTextField.getText());
			logToInsertList.add(log);
		}
		if (logbookLetterI50YESCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("50");
			log.setOrderInGroup("ZZC");
			log.setContent("YES " + logbookLetterI50TextField.getText());
			logToInsertList.add(log);
		} else if (logbookLetterI50NOTCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("50");
			log.setOrderInGroup("ZZC");
			log.setContent("NOT " + logbookLetterI50TextField.getText());
			logToInsertList.add(log);
		}

		if (logbookLetterI51QuantityCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("51");
			log.setOrderInGroup("ZZD");
			log.setContent(logbookLetterI51QuantityDoubleField.getText() + " MTRS");
			logToInsertList.add(log);
		}
		if (logbookLetterI52ShipSpeedCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("52");
			log.setOrderInGroup("ZZE");
			log.setContent(logbookLetterI52ShipSpeedDoubleField.getText() + " KNTS");
			logToInsertList.add(log);
		}

		if (logbookLetterI53YESCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("53");
			log.setOrderInGroup("ZZF");
			log.setContent("YES " + logbookLetterI53TextField.getText());
			logToInsertList.add(log);
		} else if (logbookLetterI53NOTCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("53");
			log.setOrderInGroup("ZZF");
			log.setContent("NOT " + logbookLetterI53TextField.getText());
			logToInsertList.add(log);
		}

		if (logbookLetterI54YESCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("54");
			log.setOrderInGroup("ZZG");
			log.setContent("YES " + logbookLetterI54TextField.getText());
			logToInsertList.add(log);
		} else if (logbookLetterI53NOTCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("54");
			log.setOrderInGroup("ZZG");
			log.setContent("NOT " + logbookLetterI54TextField.getText());
			logToInsertList.add(log);
		}

	}

	private void validateFields(Timestamp businessDate, Map<Validation, Object> validations) {
		if (!identifyOfTanksCheckBox.isSelected() || identifyOfTanksJFXChipView.getChips().isEmpty()) {
			validations.put(Validation.I41_EMPTY_TANKS, null);
		}

		if (!logBookLetterI43CheckBox.isSelected()) {

			if (!logBookLetterI42CheckBox.isSelected() || logBookLetterIHoursDoubleField.getText().isEmpty()) {
				validations.put(Validation.I42_EMPTY, null);
			}
		}

		if (!logBookLetterI42CheckBox.isSelected()) {
			if (!logBookLetterI43CheckBox.isSelected() || logBookLetterIHoursDoubleField.getText().isEmpty()) {
				validations.put(Validation.I43_EMPTY, null);
			}
		}

		if (!timeAndPositionAtStartCheckBox.isSelected()) {
			validations.put(Validation.I44_EMPTY, null);
		}
		if (timeAndPositionAtStartCheckBox.isSelected() && (latAtStartFormattedTextField.getText().equals("00°00.00'N") || longAtStartFormattedTextField.getText().equals("000°00.00'W"))) {

			validations.put(Validation.I44_LAT_LON_LAT_LON_ZERO, null);
		}
		if (timeAndPositionAtStartCheckBox.isSelected() && timeAtStartLocalTimeTextField.getLocalTime() == null) {
			validations.put(Validation.I44_TIME_EMPTY, null);
		}
//		if (businessDate != null && timeAndPositionAtStartCheckBox.isSelected() && timeAtStartLocalTimeTextField.getLocalTime() != null) {
//			LocalDateTime registrationLocalDateTime = Instant.ofEpochMilli(businessDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime().plusDays(1);
//			if (timeAtStartLocalTimeTextField.getLocalTime().isAfter(registrationLocalDateTime)) {
//				validations.put(Validation.I44_TIME_AFTER_REGISTRATION_DATE, null);
//			}
//		}

		if (!logbookLetterI45CheckBox.isSelected() || totalContentsDoubleField.getText().isEmpty()) {
			validations.put(Validation.I45_EMPTY, null);
		}
		if (!logbookLetterI46CheckBox.isSelected() || totalOilContentsDoubleField.getText().isEmpty()) {
			validations.put(Validation.I46_EMPTY, null);
		}
		if (!logbookLetterI47CheckBox.isSelected() || bulkTotalQuantityDoubleField.getText().isEmpty() || bulkTotalQuantityForHoursDoubleField.getText().isEmpty()) {
			validations.put(Validation.I47_EMPTY, null);
		}
		if (!logbookLetterI48CheckBox.isSelected() || finalTotalQuantityDoubleField.getText().isEmpty() || finalTotalQuantityForHoursDoubleField.getText().isEmpty()) {
			validations.put(Validation.I48_EMPTY, null);
		}

		if (!timeAndPositionAtEndCheckBox.isSelected()) {
			validations.put(Validation.I49_EMPTY, null);
		}
		if (timeAndPositionAtEndCheckBox.isSelected() && (latAtEndFormattedTextField.getText().equals("00°00.00'N") || longAtEndFormattedTextField.getText().equals("000°00.00'W"))) {

			validations.put(Validation.I49_LAT_LON_LAT_LON_ZERO, null);
		}
		if (timeAndPositionAtEndCheckBox.isSelected() && timeAtEndLocalTimeTextField.getLocalTime() == null) {
			validations.put(Validation.I49_TIME_EMPTY, null);
		}
//		if (businessDate != null && timeAndPositionAtEndCheckBox.isSelected() && timeAtEndLocalTimeTextField.getLocalTime() != null) {
//			LocalDateTime registrationLocalDateTime = Instant.ofEpochMilli(businessDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime().plusDays(1);
//			if (timeAtEndLocalTimeTextField.getLocalTime().isAfter(registrationLocalDateTime)) {
//				validations.put(Validation.I49_TIME_AFTER_REGISTRATION_DATE, null);
//			}
//		}
		if ((timeAtStartLocalTimeTextField.getLocalTime()!=null && timeAtEndLocalTimeTextField.getLocalTime()!=null) && timeAtStartLocalTimeTextField.getLocalTime().isAfter(timeAtEndLocalTimeTextField.getLocalTime())) {
			validations.put(Validation.I44_TIME_AT_START_AFTER_TIME_AT_STOP, null);
		}
		if (!logbookLetterI50YESCheckBox.isSelected() && !logbookLetterI50NOTCheckBox.isSelected()) {
			validations.put(Validation.I50_EMPTY, null);
		}
		if (logbookLetterI50NOTCheckBox.isSelected() && logbookLetterI50TextField.getText().isEmpty()) {
			validations.put(Validation.I50_EMPTY, null);
		}
		if (!logbookLetterI51QuantityCheckBox.isSelected() || logbookLetterI51QuantityDoubleField.getText().isEmpty()) {
			validations.put(Validation.I51_EMPTY, null);
		}
		if (!logbookLetterI52ShipSpeedCheckBox.isSelected() || logbookLetterI52ShipSpeedDoubleField.getText().isEmpty()) {
			validations.put(Validation.I52_EMPTY, null);
		}
		if (!logbookLetterI53YESCheckBox.isSelected() && !logbookLetterI53NOTCheckBox.isSelected()) {
			validations.put(Validation.I53_EMPTY, null);
		}
		if (logbookLetterI53NOTCheckBox.isSelected() && logbookLetterI53TextField.getText().isEmpty()) {
			validations.put(Validation.I53_EMPTY, null);
		}
		if (!logbookLetterI54YESCheckBox.isSelected() && !logbookLetterI54NOTCheckBox.isSelected()) {
			validations.put(Validation.I54_EMPTY, null);
		}
		if (logbookLetterI54NOTCheckBox.isSelected() && logbookLetterI54TextField.getText().isEmpty()) {
			validations.put(Validation.I54_EMPTY, null);
		}
	}

	public void showBalloon(MouseEvent event) {
		ballonMessage.showGeneralBalloon((ImageViewButton) event.getTarget(), label1);
	}

	public void showBalloon1(MouseEvent event) {
		ballonMessage.showGeneralBalloon((ImageViewButton) event.getTarget(), label2);
	}

	public void clearFilledInfo() {
		identifyOfTanksCheckBox.setSelected(false);
		logBookLetterI42CheckBox.setSelected(false);
		logBookLetterI43CheckBox.setSelected(false);
		timeAndPositionAtStartCheckBox.setSelected(false);
		logbookLetterI45CheckBox.setSelected(false);
		logbookLetterI46CheckBox.setSelected(false);
		logbookLetterI47CheckBox.setSelected(false);
		logbookLetterI48CheckBox.setSelected(false);
		timeAndPositionAtEndCheckBox.setSelected(false);
		logbookLetterI50ControlCheckBox.setSelected(false);
		logbookLetterI51QuantityCheckBox.setSelected(false);
		logbookLetterI52ShipSpeedCheckBox.setSelected(false);
		logbookLetterI53ControlCheckBox.setSelected(false);
		logbookLetterI54ControlCheckBox.setSelected(false);

	}
}
