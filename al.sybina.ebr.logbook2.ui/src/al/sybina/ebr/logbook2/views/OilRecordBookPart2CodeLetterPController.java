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
import jidefx.scene.control.field.DoubleField;
import jidefx.scene.control.field.FormattedTextField;

public class OilRecordBookPart2CodeLetterPController implements Initializable {

	private static final Logger logger = LoggerCreator.createLogger(OilRecordBookPart2CodeLetterPController.class);

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
	private CheckBox logbook2LetterP77CheckBox;
	@FXML
	private FormattedTextField logbook2LetterP78LongTextField;
	@FXML
	private CheckBox logbook2LetterP78CheckBox;
	@FXML
	private FormattedTextField logbook2LetterP78LatTextField;
	@FXML
	private JFXChipView<String> identifyOfTanksJFXChipView;
	@FXML
	private CheckBox logbook2LetterP79CheckBox;
	@FXML
	private CheckBox logbook2LetterP80CheckBox;
	@FXML
	private DoubleField logbook2LetterP79DoubleField;
	@FXML
	private TextField logbook2LetterP80TextField;
	@FXML
	private ImageViewButton infoLetterAImageViewButton;
	@FXML
	private ImageViewButton identityOfTanksImageViewButton;
	@FXML
	public AnchorPane codeLetterContentAnchorPane;
	private ObservableList<Image> helpImageList = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MathUtils.setStringConverter(logbook2LetterP79DoubleField);
		try {

			Image helpImageLetterP2 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPP2));
			
			Image helpImageLetterP1 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPP1));
			
			Image helpImageLetterP3 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPP3));
			
			helpImageList.addAll(helpImageLetterP2, helpImageLetterP1, helpImageLetterP3);
			
			Image addImageIdentityOfTanks = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_ADD));
			identityOfTanksImageViewButton.setImage(addImageIdentityOfTanks);

			Image infoIconImageInfoLetterA = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
			infoLetterAImageViewButton.setImage(infoIconImageInfoLetterA);

		} catch (IOException e) {
			e.printStackTrace();
		}
		identityOfTanksImageViewButton.setDisable(true);
		identifyOfTanksJFXChipView.setDisable(true);
		CoordinateUIUtils.initializeLatitudeField(logbook2LetterP78LatTextField);
		CoordinateUIUtils.initializeLongitudeField(logbook2LetterP78LongTextField);
		logbook2LetterP79DoubleField.setDisable(true);
		logbook2LetterP79DoubleField.setText("");
		logbook2LetterP80TextField.setDisable(true);
		logbook2LetterP80TextField.setText("");
		logbook2LetterP80TextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		JavaFXUtils.addFilledPseudoClass(logbook2LetterP79DoubleField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterP80TextField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterP78LatTextField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterP78LongTextField);
		MathUtils.setStringConverter(logbook2LetterP79DoubleField);
		MathUtils.setMinMaxValues(logbook2LetterP79DoubleField, 0.0, null);
		logbook2LetterP77CheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				identityOfTanksImageViewButton.setDisable(false);
				identifyOfTanksJFXChipView.setDisable(false);
				logbook2LetterP78CheckBox.setSelected(true);
				logbook2LetterP79CheckBox.setSelected(true);
				logbook2LetterP80CheckBox.setSelected(true);
			} else {
				identityOfTanksImageViewButton.setDisable(true);
				identifyOfTanksJFXChipView.getChips().clear();
				identifyOfTanksJFXChipView.setDisable(true);
				logbook2LetterP78CheckBox.setSelected(false);
				logbook2LetterP79CheckBox.setSelected(false);
				logbook2LetterP80CheckBox.setSelected(false);
			}

		});
		logbook2LetterP78CheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterP78LatTextField.setDisable(false);
				logbook2LetterP78LongTextField.setDisable(false);
			} else {
				logbook2LetterP78LatTextField.setDisable(true);
				logbook2LetterP78LongTextField.setDisable(true);
				logbook2LetterP78LatTextField.setText("00°00.00'N");
				logbook2LetterP78LongTextField.setText("000°00.00'W");
			}

		});
		logbook2LetterP79CheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterP79DoubleField.setDisable(false);

			} else {
				logbook2LetterP79DoubleField.setDisable(true);
				logbook2LetterP79DoubleField.setText("");
			}

		});
		logbook2LetterP80CheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterP80TextField.setDisable(false);

			} else {
				logbook2LetterP80TextField.setDisable(true);
				logbook2LetterP80TextField.setText("");
			}

		});
		TankUsageContext tankUsageContext = new TankUsageContext(Stream.of(TankType.BALLAST, TankType.CARGO, TankType.FUELOIL, TankType.OILYBILGE, TankType.SLOP, TankType.SLUDGE).collect(Collectors.toList()), true, true);
		identifyOfTanksJFXChipView.setUserData(tankUsageContext);
		addRemoveListener(identifyOfTanksJFXChipView);
		identifyOfTanksJFXChipView.getChips().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> change) {
				if (logbook2LetterP77CheckBox.isSelected()) {
					Double totalUserTankQuantities = ((TankUsageContext) identifyOfTanksJFXChipView.getUserData()).getTotalUserTankQuantities();
					Double totalUserTankActualLevel = ((TankUsageContext) identifyOfTanksJFXChipView.getUserData()).getTotalUserTankActualLevel();
					logbook2LetterP79DoubleField.setValue(totalUserTankQuantities);
				}
				JavaFXUtils.addTooltip(identifyOfTanksJFXChipView);
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

	public void openIdentifyOfTanksP77(Event e) {
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

	public void letterP(MouseEvent event) {
		helpCodeLetterDialog.showDialog((Stage)((ImageViewButton)event.getSource()).getScene().getWindow(),helpImageList);
	}

	public void processP(OilLog logToInsert, List<OilLog> logToInsertList, Map<Validation, Object> validations) {
		validateFields(logToInsert.getBusinessDate(), validations);
		if (logbook2LetterP77CheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("77");
			log.setOrderInGroup("ZZZL");
			log.setContent("" + String.join(", ", identifyOfTanksJFXChipView.getChips()));
			log.setUserTankActualLevels(((TankUsageContext) identifyOfTanksJFXChipView.getUserData()).getUserTankActualLevels());
			logToInsertList.add(log);
		}
		if (logbook2LetterP78CheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("78");
			log.setOrderInGroup("ZZZM");
			log.setContent("LAT:" + logbook2LetterP78LatTextField.getText() + " LONG:" + logbook2LetterP78LongTextField.getText());
			logToInsertList.add(log);
		}
		if (logbook2LetterP79CheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("79");
			log.setOrderInGroup("ZZZN");
			log.setContent(logbook2LetterP79DoubleField.getText() + "M3");
			logToInsertList.add(log);
		}
		if (logbook2LetterP80CheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("80");
			log.setOrderInGroup("ZZZO");
			log.setContent(logbook2LetterP80TextField.getText());
			logToInsertList.add(log);
		}
	}

	private void validateFields(Timestamp businessDate, Map<Validation, Object> validations) {
		if (!logbook2LetterP77CheckBox.isSelected() || identifyOfTanksJFXChipView.getChips().isEmpty()) {
			validations.put(Validation.P77_EMPTY_TANKS, null);
		}
		if (logbook2LetterP78CheckBox.isSelected() && (logbook2LetterP78LatTextField.getText().equals("00°00.00'N") || logbook2LetterP78LongTextField.getText().equals("000°00.00'W"))) {

			validations.put(Validation.P78_LAT_LON_LAT_LON_ZERO, null);
		}

		if (!logbook2LetterP78CheckBox.isSelected()) {
			validations.put(Validation.P78_LAT_LON_LAT_LON_EMPTY, null);
		}
		if (!logbook2LetterP79CheckBox.isSelected() || logbook2LetterP79DoubleField.getText().isEmpty()) {
			validations.put(Validation.P79_EMPTY, null);
		}
		if (!logbook2LetterP80CheckBox.isSelected() || logbook2LetterP80TextField.getText().isEmpty()) {
			validations.put(Validation.P80_EMPTY, null);
		}
	}

	public void clearFilledInfo() {
		logbook2LetterP77CheckBox.setSelected(false);

	}
}
