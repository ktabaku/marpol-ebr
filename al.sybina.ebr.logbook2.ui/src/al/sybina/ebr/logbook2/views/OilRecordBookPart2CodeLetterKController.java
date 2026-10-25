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
import jidefx.scene.control.field.FormattedTextField;

public class OilRecordBookPart2CodeLetterKController implements Initializable {

	private static final Logger logger = LoggerCreator.createLogger(OilRecordBookPart2CodeLetterKController.class);

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
	private CheckBox logbook2LetterK58CheckBox;
	@FXML
	private FormattedTextField logbook2LetterK58LatTextField;
	@FXML
	private FormattedTextField logbook2LetterK58LongTextField;
	@FXML
	private CheckBox logbook2LetterK59TanksCheckBox;
	@FXML
	private ImageViewButton identityOfTanksImageViewButton;
	@FXML
	private JFXChipView<String> identifyOfTanksJFXChipView;
	@FXML
	private CheckBox logbook2LetterK60CheckBox;
	@FXML
	private CheckBox logbook2LetterK60YESCheckBox;
	@FXML
	private CheckBox logbook2LetterK60NOTCheckBox;
	@FXML
	private TextField logbook2LetterK60TextField;
	@FXML
	private CheckBox logbook2LetterK61CheckBox;
	@FXML
	private FormattedTextField logbook2LetterK61LatTextField;
	@FXML
	private FormattedTextField logbook2LetterK61LongTextField;
	@FXML
	private CheckBox logbook2LetterK62CheckBox;
	@FXML
	private CheckBox logbook2LetterK62YESCheckBox;
	@FXML
	private CheckBox logbook2LetterK62NOTCheckBox;
	@FXML
	private TextField logbook2LetterK62TextField;
	@FXML
	private ImageViewButton infoLetterAImageViewButton;
	@FXML
	public AnchorPane codeLetterContentAnchorPane;
	private ObservableList<Image> helpImageList = FXCollections.observableArrayList();
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {

			Image helpImageLetterK2 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPK2));
			
			Image helpImageLetterK1 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPK1));
			
			Image helpImageLetterK3 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPK3));
			
			Image helpImageLetterK4 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPK4));
			
			helpImageList.addAll(helpImageLetterK4, helpImageLetterK1, helpImageLetterK2, helpImageLetterK3);
			
			Image addImageIdentityOfTanks = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_ADD));
			identityOfTanksImageViewButton.setImage(addImageIdentityOfTanks);

			Image infoIconImageInfoLetterA = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
			infoLetterAImageViewButton.setImage(infoIconImageInfoLetterA);
		} catch (IOException e) {
			e.printStackTrace();
		}

		logbook2LetterK58LatTextField.setDisable(true);
		logbook2LetterK58LongTextField.setDisable(true);
		CoordinateUIUtils.initializeLatitudeField(logbook2LetterK58LatTextField);
		CoordinateUIUtils.initializeLongitudeField(logbook2LetterK58LongTextField);

		identityOfTanksImageViewButton.setDisable(true);
		identifyOfTanksJFXChipView.setDisable(true);

		logbook2LetterK60YESCheckBox.setDisable(true);
		logbook2LetterK60NOTCheckBox.setDisable(true);
		logbook2LetterK60TextField.setDisable(true);

		logbook2LetterK61LatTextField.setDisable(true);
		logbook2LetterK61LongTextField.setDisable(true);
		CoordinateUIUtils.initializeLatitudeField(logbook2LetterK61LatTextField);
		CoordinateUIUtils.initializeLongitudeField(logbook2LetterK61LongTextField);

		logbook2LetterK62YESCheckBox.setDisable(true);
		logbook2LetterK62NOTCheckBox.setDisable(true);
		logbook2LetterK62TextField.setDisable(true);
		logbook2LetterK60TextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		logbook2LetterK62TextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		JavaFXUtils.addFilledPseudoClass(logbook2LetterK58LongTextField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterK58LatTextField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterK60TextField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterK61LongTextField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterK61LatTextField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterK62TextField);
		
		logbook2LetterK58CheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterK58LatTextField.setDisable(false);
				logbook2LetterK58LongTextField.setDisable(false);
				logbook2LetterK59TanksCheckBox.setSelected(true);
				logbook2LetterK60CheckBox.setSelected(true);
				logbook2LetterK62CheckBox.setSelected(true);

			} else {
				logbook2LetterK58LatTextField.setDisable(true);
				logbook2LetterK58LongTextField.setDisable(true);
				logbook2LetterK58LatTextField.setText("00°00.00'N");
				logbook2LetterK58LongTextField.setText("000°00.00'W");
				logbook2LetterK59TanksCheckBox.setSelected(false);
				logbook2LetterK60CheckBox.setSelected(false);
				logbook2LetterK62CheckBox.setSelected(false);

			}

		});
		logbook2LetterK59TanksCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				identityOfTanksImageViewButton.setDisable(false);
				identifyOfTanksJFXChipView.setDisable(false);

			} else {
				identityOfTanksImageViewButton.setDisable(true);
				identifyOfTanksJFXChipView.setDisable(true);
				identifyOfTanksJFXChipView.getChips().clear();

			}

		});
		TankUsageContext tankUsageContext = new TankUsageContext(Stream.of(TankType.BALLAST, TankType.CARGO, TankType.FUELOIL, TankType.OILYBILGE, TankType.SLOP, TankType.SLUDGE).collect(Collectors.toList()), false, true);
		identifyOfTanksJFXChipView.setUserData(tankUsageContext);
		addRemoveListener(identifyOfTanksJFXChipView);
		identifyOfTanksJFXChipView.getChips().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> change) {
				if (logbook2LetterK59TanksCheckBox.isSelected()) {
					Double totalUserTankQuantities = ((TankUsageContext) identifyOfTanksJFXChipView.getUserData()).getTotalUserTankQuantities();
					Double totalUserTankActualLevel = ((TankUsageContext) identifyOfTanksJFXChipView.getUserData()).getTotalUserTankActualLevel();
					// slopCargoQuantityDoubleField.setValue(totalUserTankQuantities);
				}
				JavaFXUtils.addTooltip(identifyOfTanksJFXChipView);
			}

		});
		logbook2LetterK60CheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterK60YESCheckBox.setDisable(false);
				logbook2LetterK60NOTCheckBox.setDisable(false);

			} else {
				logbook2LetterK60YESCheckBox.setDisable(true);
				logbook2LetterK60NOTCheckBox.setDisable(true);
				logbook2LetterK60YESCheckBox.setSelected(false);
				logbook2LetterK60NOTCheckBox.setSelected(false);

			}

		});

		logbook2LetterK60YESCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterK60NOTCheckBox.setSelected(false);

			} else {

			}

		});

		logbook2LetterK60NOTCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterK60YESCheckBox.setSelected(false);
				logbook2LetterK60TextField.setDisable(false);
			} else {
				logbook2LetterK60TextField.setDisable(true);
				logbook2LetterK60TextField.setText("");
			}

		});

		logbook2LetterK61CheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterK61LatTextField.setDisable(false);
				logbook2LetterK61LongTextField.setDisable(false);

			} else {
				logbook2LetterK61LatTextField.setDisable(true);
				logbook2LetterK61LongTextField.setDisable(true);
				logbook2LetterK61LatTextField.setText("00°00.00'N");
				logbook2LetterK61LongTextField.setText("000°00.00'W");

			}

		});
		logbook2LetterK62CheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterK62YESCheckBox.setDisable(false);
				logbook2LetterK62NOTCheckBox.setDisable(false);

			} else {
				logbook2LetterK62YESCheckBox.setDisable(true);
				logbook2LetterK62NOTCheckBox.setDisable(true);
				logbook2LetterK62YESCheckBox.setSelected(false);
				logbook2LetterK62NOTCheckBox.setSelected(false);
			}

		});

		logbook2LetterK62YESCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterK62NOTCheckBox.setSelected(false);

			} else {

			}

		});

		logbook2LetterK62NOTCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterK62YESCheckBox.setSelected(false);
				logbook2LetterK62TextField.setDisable(false);
			} else {
				logbook2LetterK62TextField.setDisable(true);
				logbook2LetterK62TextField.setText("");
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

	public void letterK(MouseEvent event) {
		helpCodeLetterDialog.showDialog((Stage)((ImageViewButton)event.getSource()).getScene().getWindow(),helpImageList);
	}

	public void openIdentifyOfTanksK59(Event e) {
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

	public void processK(OilLog logToInsert, List<OilLog> logToInsertList, Map<Validation, Object> validations) {
		validateFields(logToInsert.getBusinessDate(), validations);
		if (logbook2LetterK58CheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("58");
			log.setOrderInGroup("ZZN");
			log.setContent("LAT:" + logbook2LetterK58LatTextField.getText() + " LONG:" + logbook2LetterK58LongTextField.getText());
			logToInsertList.add(log);
		}
		if (logbook2LetterK59TanksCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("59");
			log.setOrderInGroup("ZZO");
			log.setContent("" + String.join(", ", identifyOfTanksJFXChipView.getChips()));
			log.setUserTankActualLevels(((TankUsageContext) identifyOfTanksJFXChipView.getUserData()).getUserTankActualLevels());
			logToInsertList.add(log);
		}
		if (logbook2LetterK60YESCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("60");
			log.setOrderInGroup("ZZP");
			log.setContent("YES");
			logToInsertList.add(log);
		}
		if (logbook2LetterK60NOTCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("60");
			log.setOrderInGroup("ZZQ");
			log.setContent("NOT " + logbook2LetterK60TextField.getText());
			logToInsertList.add(log);
		}
		if (logbook2LetterK61CheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("61");
			log.setOrderInGroup("ZZR");
			log.setContent("LAT:" + logbook2LetterK61LatTextField.getText() + " LONG:" + logbook2LetterK61LongTextField.getText());
			logToInsertList.add(log);
		}
		if (logbook2LetterK62YESCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("62");
			log.setOrderInGroup("ZZS");
			log.setContent("YES");
			logToInsertList.add(log);
		}
		if (logbook2LetterK62NOTCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("62");
			log.setOrderInGroup("ZZT");
			log.setContent("NOT " + logbook2LetterK62TextField.getText());
			logToInsertList.add(log);
		}
	}

	private void validateFields(Timestamp businessDate, Map<Validation, Object> validations) {
		if (!logbook2LetterK58CheckBox.isSelected()) {
			validations.put(Validation.K58_EMPTY_POSITION, null);
		}
		if (logbook2LetterK58CheckBox.isSelected() && (logbook2LetterK58LatTextField.getText().equals("00°00.00'N") || logbook2LetterK58LongTextField.getText().equals("000°00.00'W"))) {

			validations.put(Validation.K58_LAT_LON_LAT_LON_ZERO, null);
		}

		if (!logbook2LetterK59TanksCheckBox.isSelected() || identifyOfTanksJFXChipView.getChips().isEmpty()) {
			validations.put(Validation.K59_EMPTY_TANKS, null);
		}
		if (!logbook2LetterK60YESCheckBox.isSelected() && !logbook2LetterK60NOTCheckBox.isSelected()) {
			validations.put(Validation.K60_EMPTY, null);
		}
		if (logbook2LetterK60NOTCheckBox.isSelected() && logbook2LetterK60TextField.getText().isEmpty()) {
			validations.put(Validation.K60_EMPTY, null);
		}
		if (logbook2LetterK61CheckBox.isSelected() && (logbook2LetterK61LatTextField.getText().equals("00°00.00'N") || logbook2LetterK61LongTextField.getText().equals("000°00.00'W"))) {

			validations.put(Validation.K61_LAT_LON_LAT_LON_ZERO, null);
		}

		if (!logbook2LetterK62YESCheckBox.isSelected() && !logbook2LetterK62NOTCheckBox.isSelected()) {
			validations.put(Validation.K62_EMPTY, null);
		}
		if (logbook2LetterK62NOTCheckBox.isSelected() && logbook2LetterK62TextField.getText().isEmpty()) {
			validations.put(Validation.K62_EMPTY, null);
		}
	}

	public void clearFilledInfo() {
		logbook2LetterK58CheckBox.setSelected(false);
		logbook2LetterK59TanksCheckBox.setSelected(false);
		logbook2LetterK60CheckBox.setSelected(false);
		logbook2LetterK61CheckBox.setSelected(false);
		logbook2LetterK62CheckBox.setSelected(false);

	}
}
