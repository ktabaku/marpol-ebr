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
import org.eclipse.fx.ui.services.resources.ImageProvider;

import com.jfoenix.controls.JFXChipView;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.Tank;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.TankType;

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

public class OilRecordBookPart2CodeLetterCController implements Initializable {

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
	private ImageViewButton imageViewButtonInfoLetterC;
	@FXML
	private ImageViewButton identifyOfTanksUnloadedImageViewButton;
	@FXML
	private CheckBox placeOfUnloadingCheckBox;
	@FXML
	private CheckBox identityOfTanksUnloadedCheckBox;
	@FXML
	private CheckBox wasTheTanksEmptiedCheckBox;
	@FXML
	private TextField placeOfUnloadingTextField;
	@FXML
	private JFXChipView<String> identityOfTanksUnloadedJFXChipView;
	@FXML
	private DoubleField quantityRetainedOnBoardDoubleField;
	@FXML
	private CheckBox emptiedLetterCCheckBox;
	@FXML
	private CheckBox notEmptiedLetterCCheckBox;

	private boolean cleaneNotCleanedListenerEnabled;
	@FXML
	public AnchorPane codeLetterContentAnchorPane;
	private ObservableList<Image> helpImageList = FXCollections.observableArrayList();
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MathUtils.setStringConverter(quantityRetainedOnBoardDoubleField);
		try {


			Image helpImageLetterC2 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPC2));
			
			Image helpImageLetterC6 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPC6));
			
			Image helpImageLetterC1 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPC1));
			
			helpImageList.addAll(helpImageLetterC1, helpImageLetterC2, helpImageLetterC6);
			
			Image IdentifyOfTanksUnloaded = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_ADD));
			identifyOfTanksUnloadedImageViewButton.setImage(IdentifyOfTanksUnloaded);

			Image infoIconImageInfoLetterC = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
			imageViewButtonInfoLetterC.setImage(infoIconImageInfoLetterC);

		} catch (IOException e) {
			e.printStackTrace();
		}
		placeOfUnloadingTextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		JavaFXUtils.addFilledPseudoClass(placeOfUnloadingTextField);
		JavaFXUtils.addFilledPseudoClass(quantityRetainedOnBoardDoubleField);
		MathUtils.setMinMaxValues(quantityRetainedOnBoardDoubleField, 0.0, null);
		MathUtils.setStringConverter(quantityRetainedOnBoardDoubleField);
		emptiedLetterCCheckBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
			if (cleaneNotCleanedListenerEnabled) {
				if (isNowSelected) {
					notEmptiedLetterCCheckBox.setSelected(false);
				} else {
					notEmptiedLetterCCheckBox.setSelected(true);
				}
			}
		});
		notEmptiedLetterCCheckBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
			if (cleaneNotCleanedListenerEnabled) {
				if (isNowSelected) {
					emptiedLetterCCheckBox.setSelected(false);
					quantityRetainedOnBoardDoubleField.setDisable(false);
				} else {
					emptiedLetterCCheckBox.setSelected(true);
					quantityRetainedOnBoardDoubleField.setDisable(true);
					quantityRetainedOnBoardDoubleField.setValue(null);
				}
			}
		});

		placeOfUnloadingCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				placeOfUnloadingTextField.setDisable(false);
				identityOfTanksUnloadedCheckBox.setSelected(true);
				wasTheTanksEmptiedCheckBox.setSelected(true);
			} else {
				placeOfUnloadingTextField.setDisable(true);
				identityOfTanksUnloadedCheckBox.setSelected(false);
				wasTheTanksEmptiedCheckBox.setSelected(false);
			}
			placeOfUnloadingTextField.setText("");
		});
		identityOfTanksUnloadedCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				identityOfTanksUnloadedJFXChipView.setDisable(false);
				identifyOfTanksUnloadedImageViewButton.setDisable(false);
			} else {
				identityOfTanksUnloadedJFXChipView.setDisable(true);
				identifyOfTanksUnloadedImageViewButton.setDisable(true);
			}
			identityOfTanksUnloadedJFXChipView.getChips().clear();
			
		});
		wasTheTanksEmptiedCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				cleaneNotCleanedListenerEnabled = true;
				emptiedLetterCCheckBox.setDisable(false);
				notEmptiedLetterCCheckBox.setDisable(false);
				quantityRetainedOnBoardDoubleField.setDisable(false);
				Double totalUserTankQuantities = ((TankUsageContext) identityOfTanksUnloadedJFXChipView.getUserData()).getTotalUserTankQuantities();
				Double totalUserTankActualLevel = ((TankUsageContext) identityOfTanksUnloadedJFXChipView.getUserData()).getTotalUserTankActualLevel();
				if (totalUserTankActualLevel - totalUserTankQuantities == 0.0) {
					emptiedLetterCCheckBox.setSelected(true);
					quantityRetainedOnBoardDoubleField.clear();
					quantityRetainedOnBoardDoubleField.setDisable(true);
					quantityRetainedOnBoardDoubleField.clear();
				} else {
					notEmptiedLetterCCheckBox.setSelected(true);
					quantityRetainedOnBoardDoubleField.setDisable(false);
					quantityRetainedOnBoardDoubleField.setValue(totalUserTankActualLevel - totalUserTankQuantities);
				}
			} else {
				cleaneNotCleanedListenerEnabled = false;
				emptiedLetterCCheckBox.setSelected(false);
				notEmptiedLetterCCheckBox.setSelected(false);
				emptiedLetterCCheckBox.setDisable(true);
				notEmptiedLetterCCheckBox.setDisable(true);
				quantityRetainedOnBoardDoubleField.setDisable(true);
				cleaneNotCleanedListenerEnabled = true;
				quantityRetainedOnBoardDoubleField.clear();
			}
		});

		TankUsageContext tuc = new TankUsageContext(Stream.of(TankType.BALLAST, TankType.CARGO, TankType.FUELOIL, TankType.OILYBILGE, TankType.SLOP, TankType.SLUDGE).collect(Collectors.toList()), false, true);
		identityOfTanksUnloadedJFXChipView.setUserData(tuc);
		addRemoveListener(identityOfTanksUnloadedJFXChipView);
		identityOfTanksUnloadedJFXChipView.getChips().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> c) {
				if (!c.getList().isEmpty()) {
					if (wasTheTanksEmptiedCheckBox.isSelected()) {
						Double totalUserTankActualLevel = ((TankUsageContext) identityOfTanksUnloadedJFXChipView.getUserData()).getTotalUserTankActualLevel();
						Double totalUserTankQuantities = ((TankUsageContext) identityOfTanksUnloadedJFXChipView.getUserData()).getTotalUserTankQuantities();
						if (totalUserTankActualLevel - totalUserTankQuantities == 0.0) {
							emptiedLetterCCheckBox.setSelected(true);
							quantityRetainedOnBoardDoubleField.clear();
							quantityRetainedOnBoardDoubleField.setDisable(true);
						} else {
							notEmptiedLetterCCheckBox.setSelected(true);
							emptiedLetterCCheckBox.setSelected(false);
							quantityRetainedOnBoardDoubleField.setDisable(false);
							quantityRetainedOnBoardDoubleField.setValue(totalUserTankActualLevel - totalUserTankQuantities);
						}
					}
				} else {
					wasTheTanksEmptiedCheckBox.setSelected(false);
				}
				JavaFXUtils.addTooltip(identityOfTanksUnloadedJFXChipView);
			}
		});

		eventBroker.subscribe(Activator.MARPOL_DETAILS_RELOADED, new org.osgi.service.event.EventHandler() {
			@Override
			public void handleEvent(org.osgi.service.event.Event event) {
				ArrayList<Long> changedTankIds = (ArrayList<Long>) event.getProperty(Activator.TANK_IDS);
				if (changedTankIds == null) {
					identityOfTanksUnloadedJFXChipView.getChips().clear();
				} else if (changedTankIds != null && !changedTankIds.isEmpty()) {
					Activator.removeChangedTanksFromJFXChipView(changedTankIds, identityOfTanksUnloadedJFXChipView);
				}
			}
		});
	}

	public void processC(OilLog logToInsert, List<OilLog> logToInsertList, Map<Validation, Object> validations) {
		validateFields(logToInsert.getBusinessDate(), validations);

		if (placeOfUnloadingCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("6");
			log.setContent(placeOfUnloadingTextField.getText());
			log.setOrderInGroup("G");
			logToInsertList.add(log);
		}
		if (identityOfTanksUnloadedCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("7");
			log.setContent(String.join(", ", identityOfTanksUnloadedJFXChipView.getChips()));
			log.setOrderInGroup("H");
			log.setUserTankActualLevels(((TankUsageContext) identityOfTanksUnloadedJFXChipView.getUserData()).getUserTankActualLevels());
			logToInsertList.add(log);
		}
		if (wasTheTanksEmptiedCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("8");
			log.setOrderInGroup("I");
			if (emptiedLetterCCheckBox.isSelected()) {
				log.setContent("YES");
			} else if (notEmptiedLetterCCheckBox.isSelected()) {
				log.setContent("NO - QUANTITY RETAINED: " + quantityRetainedOnBoardDoubleField.getValue());
			}
			logToInsertList.add(log);
		}
	}

	private void validateFields(Timestamp businessDate, Map<Validation, Object> validations) {
		if (!placeOfUnloadingCheckBox.isSelected() || placeOfUnloadingTextField.getText().isEmpty()) {
			validations.put(Validation.C6_EMPTY, null);
		}
		if (!identityOfTanksUnloadedCheckBox.isSelected() || identityOfTanksUnloadedJFXChipView.getChips().isEmpty()) {
			validations.put(Validation.C7_EMPTY, null);
		}
		if (!wasTheTanksEmptiedCheckBox.isSelected()) {
			validations.put(Validation.C8_EMPTY, null);
		}
		if (notEmptiedLetterCCheckBox.isSelected() && quantityRetainedOnBoardDoubleField.getValue() == null) {
			validations.put(Validation.C8_QUANTITY_RETAINED_REQUIRED, null);
		}
	}

	public void letterC(MouseEvent event) {
		helpCodeLetterDialog.showDialog((Stage)((ImageViewButton)event.getSource()).getScene().getWindow(),helpImageList);
	}

	public void openIdentifyOfTanksUnloaded(Event e) {
		selectTankerDialog.showDialog((Stage) ((ImageViewButton) e.getSource()).getScene().getWindow(), identityOfTanksUnloadedJFXChipView);
	}

	public void clearFilledInfo() {
		placeOfUnloadingCheckBox.setSelected(false);
		identityOfTanksUnloadedCheckBox.setSelected(false);
		wasTheTanksEmptiedCheckBox.setSelected(false);
		identityOfTanksUnloadedJFXChipView.getChips().clear();
		((TankUsageContext) identityOfTanksUnloadedJFXChipView.getUserData()).getUserTankQuantities().clear();
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
}
