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
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import jfxtras.scene.control.ImageViewButton;
import jidefx.scene.control.field.DoubleField;

public class OilRecordBookPart2CodeLetterBController implements Initializable {

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
	private ImageViewButton imageViewButtonInfoLetterB;
	@FXML
	private JFXChipView<String> fromTanksJFXChipView;
	@FXML
	private JFXChipView<String> toTanksJFXChipView;
	@FXML
	private DoubleField quantityTransferedDoubleField;
	@FXML
	private DoubleField totalQuantityOfTankDoubleField;
	@FXML
	private DoubleField quantityRetainedOnBoardDoubleField;
	@FXML
	private CheckBox codeletterBIdentityOfTanksCheckBox;
	@FXML
	private CheckBox codeLetterBFromTanksCheckBox;
	@FXML
	private CheckBox codeLetterBToTanksCheckBox;
	@FXML
	private CheckBox codeLetterBWasTheTanksCheckBox;
	@FXML
	private CheckBox emptiedLetterBCheckBox;
	@FXML
	private CheckBox notEmptiedLetterBCheckBox;
	@FXML
	private ImageViewButton fromTanksImageViewButton;
	@FXML
	private ImageViewButton toTanksImageViewButton;

	private boolean cleaneNotCleanedListenerEnabled = true;
	@FXML
	public AnchorPane codeLetterContentAnchorPane;
	private ObservableList<Image> helpImageList = FXCollections.observableArrayList();
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MathUtils.setStringConverter(quantityTransferedDoubleField);
		MathUtils.setStringConverter(totalQuantityOfTankDoubleField);
		MathUtils.setStringConverter(quantityRetainedOnBoardDoubleField);
		JavaFXUtils.addFilledPseudoClass(quantityTransferedDoubleField);
		JavaFXUtils.addFilledPseudoClass(totalQuantityOfTankDoubleField);
		JavaFXUtils.addFilledPseudoClass(quantityRetainedOnBoardDoubleField);
		MathUtils.setStringConverter(quantityTransferedDoubleField);
		MathUtils.setMinMaxValues(totalQuantityOfTankDoubleField, 0.0, null);
		MathUtils.setMinMaxValues(quantityRetainedOnBoardDoubleField, 0.0, null);
		MathUtils.setMinMaxValues(quantityTransferedDoubleField, 0.0, null);
		try {
			Image helpImageLetterB1 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPB1));
			
			Image helpImageLetterB2 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPB2));
			
			Image helpImageLetterB3 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPB3));
			
			helpImageList.addAll(helpImageLetterB1, helpImageLetterB2, helpImageLetterB3);
			
			Image addImageFromTanks = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_ADD));
			fromTanksImageViewButton.setImage(addImageFromTanks);
			toTanksImageViewButton.setImage(addImageFromTanks);

			Image infoIconImageInfoLetterB = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
			imageViewButtonInfoLetterB.setImage(infoIconImageInfoLetterB);
		} catch (IOException e) {
			e.printStackTrace();
		}

		emptiedLetterBCheckBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
			if (cleaneNotCleanedListenerEnabled) {
				if (isNowSelected) {
					notEmptiedLetterBCheckBox.setSelected(false);
				} else {
					notEmptiedLetterBCheckBox.setSelected(true);
				}
			}
		});
		notEmptiedLetterBCheckBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
			if (cleaneNotCleanedListenerEnabled) {
				if (isNowSelected) {
					emptiedLetterBCheckBox.setSelected(false);
					quantityRetainedOnBoardDoubleField.setDisable(false);
				} else {
					emptiedLetterBCheckBox.setSelected(true);
					quantityRetainedOnBoardDoubleField.setDisable(true);
					quantityRetainedOnBoardDoubleField.setValue(null);
				}
			}
		});

		codeletterBIdentityOfTanksCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				codeLetterBFromTanksCheckBox.setDisable(false);
				codeLetterBToTanksCheckBox.setDisable(false);
				quantityTransferedDoubleField.setDisable(false);
				totalQuantityOfTankDoubleField.setDisable(false);
				codeLetterBWasTheTanksCheckBox.setDisable(false);
			} else {
				codeLetterBFromTanksCheckBox.setDisable(true);
				codeLetterBToTanksCheckBox.setDisable(true);
				quantityTransferedDoubleField.setDisable(true);
				totalQuantityOfTankDoubleField.setDisable(true);
				codeLetterBWasTheTanksCheckBox.setDisable(true);
			}
			codeLetterBFromTanksCheckBox.setSelected(false);
			codeLetterBToTanksCheckBox.setSelected(false);
			quantityTransferedDoubleField.clear();
			totalQuantityOfTankDoubleField.clear();
			codeLetterBWasTheTanksCheckBox.setSelected(false);
		});

		codeLetterBFromTanksCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				fromTanksImageViewButton.setDisable(false);
				fromTanksJFXChipView.setDisable(false);
			} else {
				fromTanksImageViewButton.setDisable(true);
				fromTanksJFXChipView.setDisable(true);
			}
			fromTanksJFXChipView.getChips().clear();
		});

		codeLetterBToTanksCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				toTanksImageViewButton.setDisable(false);
				toTanksJFXChipView.setDisable(false);
			} else {
				toTanksImageViewButton.setDisable(true);
				toTanksJFXChipView.setDisable(true);
			}
			toTanksJFXChipView.getChips().clear();
		});

		codeLetterBWasTheTanksCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				emptiedLetterBCheckBox.setDisable(false);
				notEmptiedLetterBCheckBox.setDisable(false);
				quantityRetainedOnBoardDoubleField.setDisable(false);
				Double totalUserTankQuantities = ((TankUsageContext) fromTanksJFXChipView.getUserData()).getTotalUserTankQuantities();
				Double totalUserTankActualLevel = ((TankUsageContext) fromTanksJFXChipView.getUserData()).getTotalUserTankActualLevel();
				if (totalUserTankActualLevel - totalUserTankQuantities == 0.0) {
					emptiedLetterBCheckBox.setSelected(true);
					quantityRetainedOnBoardDoubleField.clear();
					quantityRetainedOnBoardDoubleField.setDisable(true);
					quantityRetainedOnBoardDoubleField.clear();
				} else {
					notEmptiedLetterBCheckBox.setSelected(true);
					quantityRetainedOnBoardDoubleField.setDisable(false);
					quantityRetainedOnBoardDoubleField.setValue(totalUserTankActualLevel - totalUserTankQuantities);
				}
			} else {
				cleaneNotCleanedListenerEnabled = false;
				emptiedLetterBCheckBox.setSelected(false);
				notEmptiedLetterBCheckBox.setSelected(false);
				emptiedLetterBCheckBox.setDisable(true);
				notEmptiedLetterBCheckBox.setDisable(true);
				quantityRetainedOnBoardDoubleField.setDisable(true);
				cleaneNotCleanedListenerEnabled = true;
				quantityRetainedOnBoardDoubleField.clear();
			}
		});

		TankUsageContext tuc = new TankUsageContext(Stream.of(TankType.BALLAST, TankType.CARGO, TankType.FUELOIL, TankType.OILYBILGE, TankType.SLOP, TankType.SLUDGE).collect(Collectors.toList()), false, true);
		fromTanksJFXChipView.setUserData(tuc);
		addRemoveListener(fromTanksJFXChipView);
		fromTanksJFXChipView.getChips().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> c) {
				Double totalUserTankQuantities = ((TankUsageContext) fromTanksJFXChipView.getUserData()).getTotalUserTankQuantities();
				quantityTransferedDoubleField.setValue(totalUserTankQuantities);
				if (!c.getList().isEmpty()) {
					if (codeLetterBWasTheTanksCheckBox.isSelected()) {
						Double totalUserTankActualLevel = ((TankUsageContext) fromTanksJFXChipView.getUserData()).getTotalUserTankActualLevel();
						if (totalUserTankActualLevel - totalUserTankQuantities == 0.0) {
							emptiedLetterBCheckBox.setSelected(true);
							quantityRetainedOnBoardDoubleField.clear();
							quantityRetainedOnBoardDoubleField.setDisable(true);
						} else {
							notEmptiedLetterBCheckBox.setSelected(true);
							quantityRetainedOnBoardDoubleField.setDisable(false);
							quantityRetainedOnBoardDoubleField.setValue(totalUserTankActualLevel - totalUserTankQuantities);
						}
						
					}
				} else {
					codeLetterBWasTheTanksCheckBox.setSelected(false);
				}
				JavaFXUtils.addTooltip(fromTanksJFXChipView);
			}
		});

		TankUsageContext tuc2 = new TankUsageContext(Stream.of(TankType.BALLAST, TankType.CARGO, TankType.FUELOIL, TankType.OILYBILGE, TankType.SLOP, TankType.SLUDGE).collect(Collectors.toList()), true, true);
		toTanksJFXChipView.setUserData(tuc2);
		addRemoveListener(toTanksJFXChipView);
		toTanksJFXChipView.getChips().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> c) {
				Double totalUserTankQuantities = ((TankUsageContext) toTanksJFXChipView.getUserData()).getTotalUserTankQuantities();
				Double totalUserTankActualLevel = ((TankUsageContext) toTanksJFXChipView.getUserData()).getTotalUserTankActualLevel();
				totalQuantityOfTankDoubleField.setValue(totalUserTankActualLevel + totalUserTankQuantities);
				
				JavaFXUtils.addTooltip(toTanksJFXChipView);
			}
			
		});

		eventBroker.subscribe(Activator.MARPOL_DETAILS_RELOADED, new org.osgi.service.event.EventHandler() {
			@Override
			public void handleEvent(org.osgi.service.event.Event event) {
				ArrayList<Long> changedTankIds = (ArrayList<Long>) event.getProperty(Activator.TANK_IDS);
				if (changedTankIds == null) {
					fromTanksJFXChipView.getChips().clear();
					toTanksJFXChipView.getChips().clear();
				} else if (changedTankIds != null && !changedTankIds.isEmpty()) {
					Activator.removeChangedTanksFromJFXChipView(changedTankIds, fromTanksJFXChipView);
					Activator.removeChangedTanksFromJFXChipView(changedTankIds, toTanksJFXChipView);
				}
				
			}
		});
	}

	public void processB(OilLog logToInsert, List<OilLog> logToInsertList, Map<Validation, Object> validations) {
		validateFields(logToInsert.getBusinessDate(), validations);

		if (codeLetterBFromTanksCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("4.1");
			log.setContent("FROM: " + String.join(", ", fromTanksJFXChipView.getChips()));
			log.setOrderInGroup("D");
			log.setUserTankActualLevels(((TankUsageContext) fromTanksJFXChipView.getUserData()).getUserTankActualLevels());
			logToInsertList.add(log);
		}
		if (codeLetterBToTanksCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("4.2");
			log.setContent("TO: " + String.join(", ", toTanksJFXChipView.getChips()) + " " + quantityTransferedDoubleField.getValue() + " M3 TRANSFERED, TOTAL QUANTITY OF TANK(S): " + totalQuantityOfTankDoubleField.getValue() + " M3");
			log.setOrderInGroup("E");
			log.setUserTankActualLevels(((TankUsageContext) toTanksJFXChipView.getUserData()).getUserTankActualLevels());
			logToInsertList.add(log);
		}
		if (codeLetterBWasTheTanksCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("5");
			if (emptiedLetterBCheckBox.isSelected()) {
				log.setContent("YES");
			} else if (notEmptiedLetterBCheckBox.isSelected()) {
				log.setContent("NO - QUANTITY RETAINED: " + quantityRetainedOnBoardDoubleField.getValue());
			}
			log.setOrderInGroup("F");
			logToInsertList.add(log);
		}
	}

	private void validateFields(Timestamp businessDate, Map<Validation, Object> validations) {
		if (!codeletterBIdentityOfTanksCheckBox.isSelected()) {
			validations.put(Validation.B4_EMPTY, null);
		}
		if (!codeLetterBFromTanksCheckBox.isSelected() || fromTanksJFXChipView.getChips().isEmpty()) {
			validations.put(Validation.B4_1_EMPTY, null);
		}
		if (!codeLetterBToTanksCheckBox.isSelected() || toTanksJFXChipView.getChips().isEmpty()) {
			validations.put(Validation.B4_2_EMPTY, null);
		}
		if (quantityTransferedDoubleField.getValue() == null) {
			validations.put(Validation.B4_2_QUANTITY_TRANSFERED_EMPTY, null);
		}
		if (totalQuantityOfTankDoubleField.getValue() == null) {
			validations.put(Validation.B4_2_TOTAL_QUANTITY_EMPTY, null);
		}
		if (!codeLetterBWasTheTanksCheckBox.isSelected()) {
			validations.put(Validation.B5_EMPTY, null);
		}
		if (notEmptiedLetterBCheckBox.isSelected() && quantityRetainedOnBoardDoubleField.getValue() == null) {
			validations.put(Validation.B5_QUANTITY_RETAINED_REQUIRED, null);
		}
	}

	public void letterB(MouseEvent event) {
		helpCodeLetterDialog.showDialog((Stage)((ImageViewButton)event.getSource()).getScene().getWindow(),helpImageList);
	}

	public void openSelectFromTanks(Event e) {
		selectTankerDialog.showDialog((Stage) ((ImageViewButton) e.getSource()).getScene().getWindow(), fromTanksJFXChipView);
	}

	public void openSelectToTanks(Event e) {
		selectTankerDialog.showDialog((Stage) ((ImageViewButton) e.getSource()).getScene().getWindow(), toTanksJFXChipView);
	}

	public void clearFilledInfo() {
		codeletterBIdentityOfTanksCheckBox.setSelected(false);
		codeLetterBWasTheTanksCheckBox.setSelected(false);
		fromTanksJFXChipView.getChips().clear();
		((TankUsageContext) fromTanksJFXChipView.getUserData()).getUserTankQuantities().clear();
		toTanksJFXChipView.getChips().clear();
		((TankUsageContext) toTanksJFXChipView.getUserData()).getUserTankQuantities().clear();
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
