package al.sybina.ebr.logbook2.views;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

import al.sybina.ebr.commons.utils.JavaFXUtils;
import al.sybina.ebr.commons.utils.MathUtils;
import al.sybina.ebr.logbook.commons.Activator;
import al.sybina.ebr.logbook.commons.BallonMessage;
import al.sybina.ebr.logbook.dialogs.SelectTankerDialog;
import al.sybina.ebr.logbook.dialogs.TankUsageContext;
import al.sybina.ebr.logbook.dialogs.help.HelpCodeLetterDialog;
import al.sybina.ebr.logbook.domain.reladomo.OilLog;
import al.sybina.ebr.logbook2.views.OilRecordBookPart2ViewController.Validation;
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
import jidefx.scene.control.field.DoubleField;

public class OilRecordBookPart2CodeLetterAController implements Initializable {

	private Label label1 = new Label("Notes:\r\n" + 
			"The quantities should be recorded in accordance with the ship's ullage report.\r\n" + 
			"Il should be noted that under A.3, the responsible officer should record both the\r\n" +
			"\"quantity of oil loaded\" and the \"total contents of tanks\".\r\n"
			+ "Therefore, the following quantities should be recorded:\r\n" + 
			"\r\n" + 
			"I. Quantity loaded: Total Calculated Volume (TCV) - On Board Quantity {OBQ)\r\n" + 
			"2. Total contents of tank: Total\\ Calculated Volume (TCV)\r\n" + 
			"The type of oil loaded should be as per Appendix I \"List of oils\" to Annex I of MARPOL.\r\n" + 
			"");
	
	private static final Logger logger = LoggerCreator.createLogger(OilRecordBookPart2CodeLetterAController.class);

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
	private CheckBox placeOfLoadingCheckBox;
	@FXML
	private TextField placeOfLoadingTextField;
	@FXML
	private CheckBox typeOfOilLoadedCheckBox;
	@FXML
	private TextField typeOfOilLoadedTextField;
	@FXML
	private Button addOperationButton;
	@FXML
	private JFXChipView<String> identifyOfTanksJFXChipView;
	@FXML
	private CheckBox totalQuantityOfOilLoadedCheckBox;
	@FXML
	private DoubleField totalQuantityOfOilLoadedDoubleField;
	@FXML
	private DoubleField totalContentOfTanksDoubleField;
	@FXML
	private ImageViewButton infoLetterAImageViewButton;
	@FXML
	private ImageViewButton infoLetterA3ImageViewButton;
	@FXML
	private ImageViewButton identityOfTanksImageViewButton;
	@Inject
	private BallonMessage ballonMessage;
	@FXML
	private GridPane operationsGridPane;

	private Image addImageIdentityOfTanks;
	@FXML
	public AnchorPane codeLetterContentAnchorPane;
	private ObservableList<Image> helpImageList = FXCollections.observableArrayList();
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MathUtils.setStringConverter(totalQuantityOfOilLoadedDoubleField);
		MathUtils.setStringConverter(totalContentOfTanksDoubleField);
		try {
			Image helpImageLetterA1 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPA1));
			
			Image helpImageLetterA5 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_HELPA5));
			
			Image helpImageLetterA7 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPA7));
			
			Image helpImageLetterA8 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPA8));
			
			Image helpImageLetterA9 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPA9));
			
			helpImageList.addAll(helpImageLetterA5, helpImageLetterA1, helpImageLetterA7, helpImageLetterA8, helpImageLetterA9);
			
			addImageIdentityOfTanks = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_ADD));
			identityOfTanksImageViewButton.setImage(addImageIdentityOfTanks);

			Image infoIconImageInfoLetterA = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
			infoLetterAImageViewButton.setImage(infoIconImageInfoLetterA);
			infoLetterA3ImageViewButton.setImage(infoIconImageInfoLetterA);
		} catch (IOException e) {
			e.printStackTrace();
		}
		placeOfLoadingTextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		typeOfOilLoadedTextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		JavaFXUtils.addFilledPseudoClass(placeOfLoadingTextField);
		JavaFXUtils.addFilledPseudoClass(typeOfOilLoadedTextField);
		JavaFXUtils.addFilledPseudoClass(totalQuantityOfOilLoadedDoubleField);
		JavaFXUtils.addFilledPseudoClass(totalContentOfTanksDoubleField);
		MathUtils.setStringConverter(totalQuantityOfOilLoadedDoubleField);
		MathUtils.setStringConverter(totalContentOfTanksDoubleField);
		MathUtils.setMinMaxValues(totalContentOfTanksDoubleField, 0.0, null);
		MathUtils.setMinMaxValues(totalQuantityOfOilLoadedDoubleField, 0.0, null);
		placeOfLoadingCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					placeOfLoadingTextField.setDisable(false);
					typeOfOilLoadedCheckBox.setSelected(true);
					totalQuantityOfOilLoadedCheckBox.setSelected(true);
				} else {
					placeOfLoadingTextField.setDisable(true);
				}
				placeOfLoadingTextField.setText("");
			}
		});
		typeOfOilLoadedCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					identifyOfTanksJFXChipView.setDisable(false);
					typeOfOilLoadedTextField.setDisable(false);
					identityOfTanksImageViewButton.setDisable(false);
				} else {
					identifyOfTanksJFXChipView.setDisable(true);
					typeOfOilLoadedTextField.setDisable(true);
					identityOfTanksImageViewButton.setDisable(true);
				}
				identifyOfTanksJFXChipView.getChips().clear();
				typeOfOilLoadedTextField.setText("");
			}
		});
		totalQuantityOfOilLoadedCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					totalQuantityOfOilLoadedDoubleField.setDisable(false);
					totalContentOfTanksDoubleField.setDisable(false);
					addOperationButton.setDisable(false);
				} else {
					totalQuantityOfOilLoadedDoubleField.setDisable(true);
					totalContentOfTanksDoubleField.setDisable(true);
					addOperationButton.setDisable(true);
				}
				totalQuantityOfOilLoadedDoubleField.clear();
				totalContentOfTanksDoubleField.clear();
			}
		});

		identifyOfTanksJFXChipView.setUserData(new TankUsageContext(Stream.of(TankType.BALLAST, TankType.CARGO, TankType.FUELOIL, TankType.OILYBILGE, TankType.SLOP, TankType.SLUDGE).collect(Collectors.toList()), true, true));
		addRemoveListener(identifyOfTanksJFXChipView);
		identifyOfTanksJFXChipView.getChips().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> c) {
				if (totalQuantityOfOilLoadedCheckBox.isSelected()) {
					Double totalUserTankQuantities = ((TankUsageContext) identifyOfTanksJFXChipView.getUserData()).getTotalUserTankQuantities();
					Double totalUserTankActualLevel = ((TankUsageContext) identifyOfTanksJFXChipView.getUserData()).getTotalUserTankActualLevel();
					totalQuantityOfOilLoadedDoubleField.setValue(totalUserTankQuantities);
					totalContentOfTanksDoubleField.setValue(totalUserTankActualLevel + totalUserTankQuantities);
				}
				JavaFXUtils.addTooltip(identifyOfTanksJFXChipView);
			}
		});

		totalQuantityOfOilLoadedCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					if (typeOfOilLoadedCheckBox.isSelected()) {
						Double totalUserTankQuantities = ((TankUsageContext) identifyOfTanksJFXChipView.getUserData()).getTotalUserTankQuantities();
						Double totalUserTankActualLevel = ((TankUsageContext) identifyOfTanksJFXChipView.getUserData()).getTotalUserTankActualLevel();
						totalQuantityOfOilLoadedDoubleField.setValue(totalUserTankQuantities);
						totalContentOfTanksDoubleField.setValue(totalUserTankActualLevel + totalUserTankQuantities);
					}
				} else {
					operationsGridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 0);
				}
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

	public void letterA(MouseEvent event) {
		helpCodeLetterDialog.showDialog((Stage)((ImageViewButton)event.getSource()).getScene().getWindow(),helpImageList);
	}

	public void openSelectTanksOilCargoName(Event e) {
		selectTankerDialog.showDialog((Stage) ((ImageViewButton) e.getSource()).getScene().getWindow(), identifyOfTanksJFXChipView);
	}

	public void processA(OilLog logToInsert, List<OilLog> logToInsertList, Map<Validation, Object> validations) {
		validateFields(logToInsert.getBusinessDate(), validations);

		if (placeOfLoadingCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("1");
			log.setOrderInGroup("A");
			log.setContent(placeOfLoadingTextField.getText());
			logToInsertList.add(log);
		}
		if (typeOfOilLoadedCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("2");
			log.setOrderInGroup("B");
			log.setContent(typeOfOilLoadedTextField.getText() + " COTS NO. " + String.join(", ", identifyOfTanksJFXChipView.getChips()));
			log.setUserTankActualLevels(((TankUsageContext) identifyOfTanksJFXChipView.getUserData()).getUserTankActualLevels());
			logToInsertList.add(log);
		}
		if (totalQuantityOfOilLoadedCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("3");
			log.setOrderInGroup("C");
			log.setContent("QUANTITY LOADED " + totalQuantityOfOilLoadedDoubleField.getValue() + " M3 AT 15°C" + "\n" + "TOTAL CONTENT OF TANKS " + totalContentOfTanksDoubleField.getValue() + " M3 AT 15°C");
			logToInsertList.add(log);
		}
		int orderInGroup = 1;
		for (Node operationChildNode : operationsGridPane.getChildren()) {
			if (operationChildNode instanceof GridPane) {
				GridPane operationGridPane = (GridPane) operationChildNode;
				String typeTextField = null;
				JFXChipView<String> tankNameJFXChipView = null;
				Double totalTankQuantity = null;
				Double totalTankContent = null;
				for (Node childNode : operationGridPane.getChildren()) {
					if (childNode instanceof TextField && childNode.getId().startsWith("typeTextField")) {
						typeTextField = ((TextField) childNode).getText();
					}
					if (childNode instanceof JFXChipView && childNode.getId().startsWith("tankName") && !((JFXChipView) childNode).getChips().isEmpty()) {
						tankNameJFXChipView = (JFXChipView) childNode;
					}

					if (childNode instanceof DoubleField && childNode.getId().startsWith("totalTankQuantity")) {
						totalTankQuantity = ((DoubleField) childNode).getValue();
					}
					if (childNode instanceof DoubleField && childNode.getId().startsWith("totalTankContent")) {
						totalTankContent = ((DoubleField) childNode).getValue();
					}
				}
				if (typeTextField != null && tankNameJFXChipView != null && !tankNameJFXChipView.getChips().isEmpty() && totalTankQuantity != null && totalTankContent != null) {
					OilLog log2 = (OilLog) logToInsert.clone();
					log2.setCodeNumber("2");
					log2.setOrderInGroup("C" + String.format("%03d", orderInGroup));
					log2.setContent(typeTextField + " COTS NO. " + String.join(", ", tankNameJFXChipView.getChips()));
					log2.setUserTankActualLevels(((TankUsageContext) tankNameJFXChipView.getUserData()).getUserTankActualLevels());
					logToInsertList.add(log2);
					OilLog log3 = (OilLog) logToInsert.clone();
					log3.setCodeNumber("3");
					log3.setOrderInGroup("C" + String.format("%03d", orderInGroup));
					log3.setContent("QUANTITY LOADED " + totalTankQuantity + " M3 AT 15°C" + "\n" + "TOTAL CONTENT OF TANKS " + totalTankContent + " M3 AT 15°C");
					logToInsertList.add(log3);
					orderInGroup++;
				} else {
					logger.error("Validation not passed. Discarding group silently for the user.");
				}
			}
		}

	}

	private void validateFields(Date registrationDate, Map<Validation, Object> validations) {
		if (!placeOfLoadingCheckBox.isSelected() || placeOfLoadingTextField.getText().trim().isEmpty()) {
			validations.put(Validation.A1_EMPTY, null);
		}
		if (!typeOfOilLoadedCheckBox.isSelected() || (typeOfOilLoadedTextField.getText() == null || typeOfOilLoadedTextField.getText().trim().isEmpty())) {
			validations.put(Validation.A2_TYPE_OF_OIL_EMPTY, null);
		}
		if (!typeOfOilLoadedCheckBox.isSelected() || identifyOfTanksJFXChipView.getChips().isEmpty()) {
			validations.put(Validation.A2_TANKS_EMPTY, null);
		}
		if (!totalQuantityOfOilLoadedCheckBox.isSelected() || totalQuantityOfOilLoadedDoubleField.getValue() == null) {
			validations.put(Validation.A3_TOTAL_QUANTITY_EMPTY, null);
		}
		if (!totalQuantityOfOilLoadedCheckBox.isSelected() || totalContentOfTanksDoubleField.getValue() == null) {
			validations.put(Validation.A3_TOTAL_CONTENT_EMPTY, null);
		}

	}

	public void clearFilledInfo() {
		placeOfLoadingCheckBox.setSelected(false);
		typeOfOilLoadedCheckBox.setSelected(false);
		totalQuantityOfOilLoadedCheckBox.setSelected(false);
		identifyOfTanksJFXChipView.getChips().clear();
		((TankUsageContext) identifyOfTanksJFXChipView.getUserData()).getUserTankQuantities().clear();
	}

	public void showBalloon1(MouseEvent event) {
		ballonMessage.showGeneralBalloon((ImageViewButton) event.getTarget(), label1);
	}
	
	public Map<String, Node> addOperation(Event event) {
		try {

			GridPane newOperationGridPane = new GridPane();
			Method method = operationsGridPane.getClass().getDeclaredMethod("getNumberOfRows");
			method.setAccessible(true);
			Integer rows = (Integer) method.invoke(operationsGridPane);

			CheckBox typeOfOilCheckBox = new CheckBox();
			typeOfOilCheckBox.getStyleClass().add("logbookcheck-box");
			typeOfOilCheckBox.setSelected(true);
			typeOfOilCheckBox.setDisable(true);
			Label typeOfOilLoadedLabel = new Label("2. Type of oil loaded");
			typeOfOilLoadedLabel.getStyleClass().add("logbookOtherLabel");
			HBox typeOfOilLoadedHBox = new HBox(typeOfOilLoadedLabel);
			typeOfOilLoadedHBox.setAlignment(Pos.CENTER_LEFT);
			typeOfOilLoadedHBox.getStyleClass().add("logbook-label-hbox");
			Region arrowRegion = new Region();
			arrowRegion.getStyleClass().add("logbook-arrow-region");
			arrowRegion.getStyleClass().add("logbook-arrow-region-color-light-grey");
			HBox shellHBox = new HBox(typeOfOilCheckBox, typeOfOilLoadedHBox);
			shellHBox.getStyleClass().add("logbookLightGreyLabel");
			shellHBox.setAlignment(Pos.CENTER_LEFT);
			HBox typeOfOilLoadedWithCheckBoxHBox = new HBox(shellHBox, arrowRegion);
			typeOfOilLoadedWithCheckBoxHBox.setAlignment(Pos.CENTER_LEFT);
			GridPane.setColumnSpan(typeOfOilLoadedWithCheckBoxHBox, 2);


			TextField typeTextField = new TextField();
			typeTextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
			typeTextField.setId("typeTextField" + rows);
			typeTextField.getStyleClass().add("logbook-text-field");
			JavaFXUtils.addFilledPseudoClass(typeTextField);

			Label identifyTheTankLabel = new Label("Identity Tank(s)");
			identifyTheTankLabel.getStyleClass().add("logbookOtherLabel");
			HBox identifyTheTankHBox = new HBox(identifyTheTankLabel);
			identifyTheTankHBox.setAlignment(Pos.CENTER_LEFT);
			identifyTheTankHBox.setPadding(new Insets(0, 0, 0, 10));
			identifyTheTankHBox.getStyleClass().add("logbook-label-hbox");
			identifyTheTankHBox.getStyleClass().add("logbook2ShortLightGreyLabel");
			Region arrowRegion2 = new Region();
			arrowRegion2.getStyleClass().add("logbook-arrow-region");
			arrowRegion2.getStyleClass().add("logbook-arrow-region-color-light-grey");
			HBox shellHBox2 = new HBox(identifyTheTankHBox, arrowRegion2);
			shellHBox2.setAlignment(Pos.CENTER_LEFT);

			JFXChipView<String> tankNameJFXChipView = new JFXChipView<String>();
			TankUsageContext tuc = new TankUsageContext(Stream.of(TankType.BALLAST, TankType.CARGO, TankType.FUELOIL, TankType.OILYBILGE, TankType.SLOP, TankType.SLUDGE).collect(Collectors.toList()), true, true);
			tankNameJFXChipView.setUserData(tuc);
			tankNameJFXChipView.getStyleClass().add("logbookjfx-chip-view");
			GridPane.setColumnSpan(tankNameJFXChipView, 1);
			tankNameJFXChipView.setId("tankName" + rows);

			ImageViewButton addTankImageViewButton = new ImageViewButton(addImageIdentityOfTanks);
			addTankImageViewButton.getStyleClass().add("image-view-button");
			addTankImageViewButton.getStyleClass().add("logbookButtonAddTank");
			GridPane.setHalignment(addTankImageViewButton, HPos.CENTER);
			addTankImageViewButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					selectTankerDialog.showDialog((Stage) ((ImageViewButton) event.getSource()).getScene().getWindow(), tankNameJFXChipView);
				}
			});

			newOperationGridPane.addRow(0, typeOfOilLoadedWithCheckBoxHBox, getTransparentRegion(), getTransparentRegion(), typeTextField, getTransparentRegion(), shellHBox2, getTransparentRegion(), addTankImageViewButton, getTransparentRegion(), tankNameJFXChipView);

			CheckBox quantityCheckBox = new CheckBox();
			quantityCheckBox.setSelected(true);
			quantityCheckBox.setDisable(true);
			quantityCheckBox.getStyleClass().add("logbookcheck-box");
			Label totalQuantityLabel = new Label("3. Total quantity of oil loaded (state quantity\nadded)");
			totalQuantityLabel.getStyleClass().add("logbookOtherLabel");
			HBox totalQuantityHBox = new HBox(totalQuantityLabel);
			totalQuantityHBox.setAlignment(Pos.CENTER_LEFT);
			totalQuantityHBox.getStyleClass().add("logbook-label-hbox");
			Region arrowRegion3 = new Region();
			arrowRegion3.getStyleClass().add("logbook-arrow-region");
			arrowRegion3.getStyleClass().add("logbook-arrow-region-color-blue");
			HBox shellHBox3 = new HBox(quantityCheckBox, totalQuantityHBox);
			shellHBox3.getStyleClass().add("logbookColorBlueLabel");
			shellHBox3.setAlignment(Pos.CENTER_LEFT);
			HBox totalQuantityHBoxWithCheckBoxHBox = new HBox(shellHBox3, arrowRegion3);
			totalQuantityHBoxWithCheckBoxHBox.setAlignment(Pos.CENTER_LEFT);
			GridPane.setColumnSpan(totalQuantityHBoxWithCheckBoxHBox, 2);
			
			DoubleField quantityDoubleField = new DoubleField();
			MathUtils.setStringConverter(quantityDoubleField);
			quantityDoubleField.setId("totalTankQuantity" + rows);
			quantityDoubleField.getStyleClass().add("logbook-text-field");
			MathUtils.setMinMaxValues(quantityDoubleField, 0.0, null);
			Label mcLabel = new Label("MC at 15°C");
			mcLabel.getStyleClass().add("logbookOtherLabel");

			Button deleteButton = new Button("X");
			GridPane.setHalignment(deleteButton, HPos.CENTER);
			deleteButton.setStyle("-fx-background-color: #ef7f2e");
			deleteButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					Button deleteButton = (Button) event.getSource();
					GridPane operationGridPane = (GridPane) deleteButton.getParent();
					Integer rowIndex = GridPane.getRowIndex(operationGridPane);
					((GridPane) operationGridPane.getParent()).getChildren().removeIf(node -> GridPane.getRowIndex(node) == rowIndex);
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

			newOperationGridPane.addRow(1, totalQuantityHBoxWithCheckBoxHBox, getTransparentRegion(), getTransparentRegion(), quantityDoubleField, getTransparentRegion(), mcLabel, getTransparentRegion(), deleteButton);

			Label totalContentLabel = new Label("and the total content of tank(s), in cubic metres");
			totalContentLabel.getStyleClass().add("logbookOtherLabel");
			HBox totalContentHBox = new HBox(totalContentLabel);
			totalContentHBox.setAlignment(Pos.CENTER_LEFT);
			totalContentHBox.getStyleClass().add("logbookLightGreyLabel");
			totalContentHBox.getStyleClass().add("logbook-label-hbox");
			HBox.setHgrow(totalContentHBox, Priority.ALWAYS);
			Region arrowRegion4 = new Region();
			arrowRegion4.getStyleClass().add("logbook-arrow-region");
			arrowRegion4.getStyleClass().add("logbook-arrow-region-color-light-grey");
			HBox shellHBox4 = new HBox(totalContentHBox, arrowRegion4);
			shellHBox4.setAlignment(Pos.CENTER_LEFT);
			HBox.setHgrow(shellHBox4, Priority.ALWAYS);
			
			DoubleField contentDoubleField = new DoubleField();
			contentDoubleField.setId("totalTankContent" + rows);
			contentDoubleField.getStyleClass().add("logbook-text-field");
			JavaFXUtils.addFilledPseudoClass(contentDoubleField);
			MathUtils.setMinMaxValues(contentDoubleField, 0.0, null);
			Label mcLabel2 = new Label("MC at 15°C");
			mcLabel2.getStyleClass().add("logbookOtherLabel");

			newOperationGridPane.addRow(2, getTransparentRegion(), shellHBox4, getTransparentRegion(), contentDoubleField, getTransparentRegion(), mcLabel2);

			tankNameJFXChipView.getChips().addListener(new ListChangeListener<String>() {
				@Override
				public void onChanged(Change<? extends String> change) {
					Double totalUserTankQuantities = ((TankUsageContext) tankNameJFXChipView.getUserData()).getTotalUserTankQuantities();
					Double totalUserTankActualLevel = ((TankUsageContext) tankNameJFXChipView.getUserData()).getTotalUserTankActualLevel();
					quantityDoubleField.setValue(totalUserTankQuantities);
					contentDoubleField.setValue(totalUserTankActualLevel + totalUserTankQuantities);
					JavaFXUtils.addTooltip(tankNameJFXChipView);
				}
			});
			addRemoveListener(tankNameJFXChipView);

//			newOperationGridPane.setGridLinesVisible(true);
			GridPane.setFillWidth(newOperationGridPane, true);
			GridPane.setColumnSpan(newOperationGridPane, GridPane.REMAINING);
//			newOperationGridPane.setHgap(10);
//			newOperationGridPane.setVgap(10);
			ColumnConstraints cc0 = new ColumnConstraints(30.0, 30.0, 30.0);
			ColumnConstraints cc1 = new ColumnConstraints(330.0, Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
			ColumnConstraints cc2 = new ColumnConstraints(10.0, 10.0, 10.0);
			ColumnConstraints cc3 = new ColumnConstraints(150.0, 150.0, 150.0);
			ColumnConstraints cc4 = new ColumnConstraints(10.0, 10.0, 10.0);
			ColumnConstraints cc5 = new ColumnConstraints(150.0, Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
			ColumnConstraints cc6 = new ColumnConstraints(10.0, 10.0, 10.0);
			ColumnConstraints cc7 = new ColumnConstraints(30.0, 30.0, 30.0);
			ColumnConstraints cc8 = new ColumnConstraints(10.0, 10.0, 10.0);
			ColumnConstraints cc9 = new ColumnConstraints(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE, Double.MAX_VALUE);
			
//			cc1.setHalignment(HPos.CENTER); cc1.setHgrow(Priority.SOMETIMES);
//			cc2.setHgrow(Priority.SOMETIMES);
//			cc3.setHgrow(Priority.SOMETIMES);
//			cc4.setHgrow(Priority.SOMETIMES);
//			cc5.setHalignment(HPos.CENTER); cc5.setHgrow(Priority.SOMETIMES);
//			cc6.setHgrow(Priority.SOMETIMES);
//			cc7.setHgrow(Priority.SOMETIMES);
			cc9.setHgrow(Priority.ALWAYS);
			newOperationGridPane.getColumnConstraints().addAll(cc0, cc1, cc2, cc3, cc4, cc5, cc6, cc7, cc8, cc9);
			newOperationGridPane.getRowConstraints().addAll(new RowConstraints(80.0, 80.0, 80.0), new RowConstraints(80.0, 80.0, 80.0), new RowConstraints(80.0, 80.0, 80.0));

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
			

			operationsGridPane.addRow(rows, newOperationGridPane);
			Map<String, Node> map = new HashMap<>();
			map.put("gridPane", newOperationGridPane);
			map.put("quantity", quantityDoubleField);
			map.put("content", contentDoubleField);
			map.put("type", typeTextField);
			return map;
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private Region getTransparentRegion() {
		Region mouseTransparentRegion = new Region(); mouseTransparentRegion.setMouseTransparent(true);
		return mouseTransparentRegion;
	}
}



