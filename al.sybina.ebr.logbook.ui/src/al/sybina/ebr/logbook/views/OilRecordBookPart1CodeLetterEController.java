package al.sybina.ebr.logbook.views;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.util.Pair;
import jfxtras.scene.control.ImageViewButton;
import jfxtras.scene.control.LocalTimeTextField;
import jidefx.scene.control.field.DoubleField;
import jidefx.scene.control.field.FormattedTextField;

public class OilRecordBookPart1CodeLetterEController implements Initializable {
	
	private static final Logger logger = LoggerCreator.createLogger(OilRecordBookPart1CodeLetterEController.class);
	
	@Inject
	private BallonMessage ballonMessage;
	@Inject
	private IResourceProviderService resourcesService;
	@Inject
	private ImageProvider imageProvider;
	@Inject
	private SelectTankerDialog selectTankerDialog;
	@Inject
	private OperationLogReladomoService operationLogReladomoService;
	@Inject
	private HelpCodeLetterDialog helpCodeLetterDialog;
	@Inject
	private OperationLogService operationLogService;
	// codeLetter E
	@Inject
	private IEventBroker eventBroker;
	
	@FXML
	private ImageViewButton imageViewButtonInfoLetterE;
	@FXML
	private CheckBox timeCheckBox;
	@FXML
	private LocalTimeTextField localTimeTextField;
	@FXML
	private FormattedTextField<Latitude> positionLatTextField;
	@FXML
	private FormattedTextField<Longitude> positionLongTextField;
	@FXML
	private CheckBox systemHasBeenPutIntoAutomaticCheckBox;
	@FXML
	private LocalTimeTextField transferOfBilgeWaterTimeField;
	@FXML
	private CheckBox systemHasBeenPutIntoManualOperationCheckBox;
	@FXML
	private LocalTimeTextField systemHasBeenPutIntoManualOperationTimeField;

	private TextField currentSelectedTextField;
	
	
	
	@FXML
	private GridPane bilgeGridPane;
	@FXML
	private ImageViewButton quantityDischargedToTankImageViewButton;
	@FXML
	private JFXChipView<String> tankNameFromTanksJFXChipView;
	@FXML
	private DoubleField tankRetainedBilgeTankDoubleField;
	@FXML
	private DoubleField tankQuantityFromBilgeDoubleField;
	@FXML
	private Button addBilgeTankButton;
	@FXML
	public AnchorPane codeLetterContentAnchorPane;
	private ObservableList<Image> helpImageList = FXCollections.observableArrayList();
	private Image addImage;
	
	private StringTemplate bilgeTemplate = new StringTemplate("$tankQuantity$ M3 ADDED TO $tankName$ OF CAPACITY $tankCapacity$ M3, $tankRetained$ M3 RETAINED IN TANK");
	private Pattern bilgePattern = Pattern.compile("(.{1,}) M3 ADDED TO (.{1,}) OF CAPACITY (.{1,}) M3, (.{1,}) M3 RETAINED IN TANK");

	private DateTimeFormatter dateTimeFormatter = EasyDateUtils.logbookPdfDateTimeFormatter;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MathUtils.setStringConverter(tankRetainedBilgeTankDoubleField);
		MathUtils.setStringConverter(tankQuantityFromBilgeDoubleField);
		MathUtils.setMinMaxValues(tankRetainedBilgeTankDoubleField, 0.0, null);
		MathUtils.setMinMaxValues(tankQuantityFromBilgeDoubleField, 0.0, null);
		localTimeTextField.setDateTimeFormatter(EasyDateUtils.logbookTimeFormatter);
		transferOfBilgeWaterTimeField.setDateTimeFormatter(EasyDateUtils.logbookTimeFormatter);
		systemHasBeenPutIntoManualOperationTimeField.setDateTimeFormatter(EasyDateUtils.logbookTimeFormatter);
		JavaFXUtils.addFilledPseudoClass(positionLatTextField);
		JavaFXUtils.addFilledPseudoClass(positionLongTextField);
		JavaFXUtils.addFilledPseudoClass(tankRetainedBilgeTankDoubleField);
		JavaFXUtils.addFilledPseudoClass(tankQuantityFromBilgeDoubleField);
		JavaFXUtils.addFilledPseudoClass(localTimeTextField);
		JavaFXUtils.addFilledPseudoClass(transferOfBilgeWaterTimeField);
		JavaFXUtils.addFilledPseudoClass(systemHasBeenPutIntoManualOperationTimeField);
		try {
			addImage = imageProvider
					.getImage(((WorkbenchResourceProvider) resourcesService)
							.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_ADD));
			Image helpImageLetterE1 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_HELPE1));
			
			Image helpImageLetterE2 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_HELPE2));
		
			Image helpImageLetterE3 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_HELPE3));
			
			helpImageList.addAll(helpImageLetterE1, helpImageLetterE2, helpImageLetterE3);
			
			Image infoImageLetterE = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
			imageViewButtonInfoLetterE.setImage(infoImageLetterE);
			
 
			quantityDischargedToTankImageViewButton.setImage(addImage);

			CoordinateUIUtils.initializeLatitudeField(positionLatTextField);
			CoordinateUIUtils.initializeLongitudeField(positionLongTextField);

		} catch (IOException e) {
			e.printStackTrace();
		}
		timeCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					localTimeTextField.setDisable(false);
					positionLatTextField.setDisable(false);
					positionLongTextField.setDisable(false);
					systemHasBeenPutIntoAutomaticCheckBox.setSelected(true);
					systemHasBeenPutIntoManualOperationCheckBox.setSelected(true);
				} else {
					localTimeTextField.setDisable(true);
					positionLatTextField.setDisable(true);
					positionLongTextField.setDisable(true);
					systemHasBeenPutIntoAutomaticCheckBox.setSelected(false);
					systemHasBeenPutIntoManualOperationCheckBox.setSelected(false);
				}
				localTimeTextField.setLocalTime(null);
				positionLatTextField.setText("00°00.00'N");
				positionLongTextField.setText("000°00.00'W");
			}

		});
		systemHasBeenPutIntoAutomaticCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {	
				if(newValue) {
					transferOfBilgeWaterTimeField.setDisable(false);
					quantityDischargedToTankImageViewButton.setDisable(false);
					tankNameFromTanksJFXChipView.setDisable(false);
					addBilgeTankButton.setDisable(false);
					tankQuantityFromBilgeDoubleField.setDisable(false);
					tankRetainedBilgeTankDoubleField.setDisable(false);
				} else {
					transferOfBilgeWaterTimeField.setDisable(true);
					bilgeGridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node)!=null && GridPane.getRowIndex(node) > 0);
					bilgeGridPane.getRowConstraints().removeIf(rowConstraint -> rowConstraint != bilgeGridPane.getRowConstraints().get(0));
					quantityDischargedToTankImageViewButton.setDisable(true);
					addBilgeTankButton.setDisable(true);
					tankNameFromTanksJFXChipView.setDisable(true);
					tankNameFromTanksJFXChipView.getChips().clear();
					tankQuantityFromBilgeDoubleField.setValue(null);
					tankQuantityFromBilgeDoubleField.setDisable(true);
			        Platform.runLater(() -> {
			        	tankRetainedBilgeTankDoubleField.clear();
			        });
					tankRetainedBilgeTankDoubleField.setDisable(true);
				}
				transferOfBilgeWaterTimeField.setLocalTime(null);
			}

		});
		systemHasBeenPutIntoManualOperationCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					systemHasBeenPutIntoManualOperationTimeField.setDisable(false);
				} else {
					systemHasBeenPutIntoManualOperationTimeField.setDisable(true);
				}
				systemHasBeenPutIntoManualOperationTimeField.setLocalTime(null);
			}

		});
		
		addBilgeTankButton.setOnMouseEntered(mouseEvent -> {if(!mouseEvent.isPrimaryButtonDown()) addBilgeTankButton.setCursor(Cursor.HAND);});
		addBilgeTankButton.setOnMouseExited(mouseEvent -> {if(!mouseEvent.isPrimaryButtonDown()) addBilgeTankButton.setCursor(Cursor.DEFAULT);});

		tankRetainedBilgeTankDoubleField.valueProperty().addListener(new ChangeListener<Double>() {
			@Override
			public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
				Double totalUserTankCapacity = ((TankUsageContext)tankNameFromTanksJFXChipView.getUserData()).getTotalUserTankCapacity();
				Double totalUserTankActualLevel = ((TankUsageContext)tankNameFromTanksJFXChipView.getUserData()).getTotalUserTankActualLevel();
				if(!tankNameFromTanksJFXChipView.getChips().isEmpty()) {
					if(newValue!=null && newValue > totalUserTankCapacity) {
						OilRecordBookPart1ViewController.showWarning("Invalid value:" + newValue, "Valid value range:0.0-" + totalUserTankCapacity);
						tankQuantityFromBilgeDoubleField.setValue(totalUserTankCapacity-totalUserTankActualLevel);
					} else {
						if(newValue!=null && newValue > totalUserTankCapacity*0.98) {
							OilRecordBookPart1ViewController.showWarning("Value:" + newValue, "Is greater than 98% of max value (" + totalUserTankCapacity +")!");
						}
					}
				}
			}
		});
		
		TankUsageContext tuc = new TankUsageContext(Stream.of(TankType.OILYBILGE).collect(Collectors.toList()), true, false);
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
					tankRetainedBilgeTankDoubleField.setValue(totalUserTankQuantities + Optional.ofNullable(tankQuantityFromBilgeDoubleField.getValue()).orElse(0.0));
		        });
		        JavaFXUtils.addTooltip(tankNameFromTanksJFXChipView);
			}
		});
		tankQuantityFromBilgeDoubleField.valueProperty().addListener(new ChangeListener<Double>() {
			@Override
			public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
				Double totalUserTankQuantities = ((TankUsageContext)tankNameFromTanksJFXChipView.getUserData()).getTotalUserTankActualLevel();
				tankRetainedBilgeTankDoubleField.setValue(totalUserTankQuantities + Optional.ofNullable(newValue).orElse(0.0));
			}
		});
		addRemoveListener(tankNameFromTanksJFXChipView);
		
		GridPane.setRowIndex(tankNameFromTanksJFXChipView, 0);
		GridPane.setRowIndex(tankQuantityFromBilgeDoubleField, 0);
		GridPane.setRowIndex(tankRetainedBilgeTankDoubleField, 0);
		
		eventBroker.subscribe(Activator.MARPOL_DETAILS_RELOADED, new org.osgi.service.event.EventHandler() {
			@Override
			public void handleEvent(org.osgi.service.event.Event event) {
				ArrayList<Long> changedTankIds = (ArrayList<Long>) event.getProperty(Activator.TANK_IDS);
    			if(changedTankIds==null) {
    				tankNameFromTanksJFXChipView.getChips().clear();
    			} else if(changedTankIds!=null && !changedTankIds.isEmpty()) {
    				Activator.removeChangedTanksFromJFXChipView(changedTankIds, tankNameFromTanksJFXChipView);
    			}
			}
		});
	}
	

	public void processE(OilLog logToInsert, List<OilLog> logToInsertList, Map<Validation, Object> validations) {
		validateFields(logToInsert.getBusinessDate(), validations);
		
		if (timeCheckBox.isSelected() && localTimeTextField.getLocalTime()!=null) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("16");
			log.setContent("TIME "+ localTimeTextField.getLocalTime().format(EasyDateUtils.logbookTimeFormatter) + " LAT " + positionLatTextField.getText() + " LONG " + positionLongTextField.getText());
			log.setOrderInGroup("ZB");
			logToInsertList.add(log);
		}
		if (systemHasBeenPutIntoAutomaticCheckBox.isSelected() && transferOfBilgeWaterTimeField.getLocalTime()!=null) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("17");
			StringBuilder sb = new StringBuilder();
			sb.append("START TIME: " + transferOfBilgeWaterTimeField.getLocalTime().format(EasyDateUtils.logbookTimeFormatter));
			List<Pair<String, Tank>> tankPairList = getTanks(bilgeGridPane);
			for(Pair<String, Tank> tank : tankPairList) {
				sb.append("\n");
				sb.append(tank.getKey());
			}
			log.setTankPairList(tankPairList);
			log.setContent(sb.toString());
			log.setOrderInGroup("ZC");
			logToInsertList.add(log);
		}
		if (systemHasBeenPutIntoManualOperationCheckBox.isSelected() && systemHasBeenPutIntoManualOperationTimeField.getLocalTime()!=null) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("18");
			log.setContent("STOP TIME: " + systemHasBeenPutIntoManualOperationTimeField.getLocalTime().format(EasyDateUtils.logbookTimeFormatter));
			log.setOrderInGroup("ZD");
			logToInsertList.add(log);
		}
	}
	private void validateFields(Date registrationDate, Map<Validation, Object> validations) {
		if (systemHasBeenPutIntoAutomaticCheckBox.isSelected() && tankNameFromTanksJFXChipView.getChips().isEmpty()) {
			validations.put(Validation.E17_EMPTY, null);
		}
		if(!timeCheckBox.isSelected()) {
			validations.put(Validation.E16_REQUIRED, null);
		}
		if(!systemHasBeenPutIntoAutomaticCheckBox.isSelected()) {
			validations.put(Validation.E17_REQUIRED, null);
		}
		if(!systemHasBeenPutIntoManualOperationCheckBox.isSelected()) {
			validations.put(Validation.E18_REQUIRED, null);
		}
//		if(registrationDate != null && timeCheckBox.isSelected() && localTimeTextField.getLocalTime()!=null) {
//			LocalDateTime registrationLocalDateTime = Instant.ofEpochMilli(registrationDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime().plusDays(1);
//			if(registrationLocalDateTime.isBefore(localTimeTextField.getLocalTime())) {
//				validations.put(Validation.E16_TIME_AFTER_REGISTRATION_DATE, null);
//			}
//		}
//		if(registrationDate != null && systemHasBeenPutIntoAutomaticCheckBox.isSelected() && transferOfBilgeWaterTimeField.getLocalTime()!=null) {
//			LocalDateTime registrationLocalDateTime = Instant.ofEpochMilli(registrationDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime().plusDays(1);
//			if(registrationLocalDateTime.isBefore(transferOfBilgeWaterTimeField.getLocalTime())) {
//				validations.put(Validation.E17_TIME_AFTER_REGISTRATION_DATE, null);
//			}
//		}
//		if(registrationDate != null && systemHasBeenPutIntoManualOperationCheckBox.isSelected() && systemHasBeenPutIntoManualOperationTimeField.getLocalTime()!=null) {
//			LocalDateTime registrationLocalDateTime = Instant.ofEpochMilli(registrationDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime().plusDays(1);
//			if(registrationLocalDateTime.isBefore(systemHasBeenPutIntoManualOperationTimeField.getLocalTime())) {
//				validations.put(Validation.E18_TIME_AFTER_REGISTRATION_DATE, null);
//			}
//		}
		if(timeCheckBox.isSelected()) {
			if(positionLatTextField.getText().equals("00°00.00'N") || positionLongTextField.getText().equals("000°00.00'W")) {
				validations.put(Validation.E16_LAT_LON_ZERO, null);
			}
		}
		if (systemHasBeenPutIntoAutomaticCheckBox.isSelected()) {
			List<Pair<String, Tank>> tankPairList = getTanks(bilgeGridPane);
			List<Pair<String, Tank>> notNullTanksTankPairList = tankPairList.stream().filter(t -> t.getValue() != null).collect(Collectors.toList());
			List<Tank> duplicates = notNullTanksTankPairList.stream()
					  .collect(Collectors.groupingBy(p -> p.getValue(), Collectors.counting()))
					  .entrySet().stream()
					  .filter(n -> n.getValue() > 1)
					  .map(n -> n.getKey())
					  .collect(Collectors.toList());
			for(Tank duplicateTank : duplicates) {
				validations.put(Validation.E17_TANK_ALREADY_IN_LIST, duplicateTank.getName());
			}
		}
	}
	public void actionPerformedE(MouseEvent event) {
		helpCodeLetterDialog.showDialog((Stage)((ImageViewButton)event.getSource()).getScene().getWindow(),helpImageList);
	}

	public void restoreLogGroup(List<OilLog> logGroup) {
		if(logGroup==null || logGroup.isEmpty()) return;
		List<OilLog> codeBOilLogs = logGroup.stream().filter(c -> c.getCodeLetter().equals("E")).collect(Collectors.toList());
		for (OilLog oilLog : codeBOilLogs) {
			if(oilLog.getCorrectionNote()!=null && oilLog.getCorrectionNote().equals("Corrected")) continue;
			String value;
			switch (oilLog.getCodeNumber()) {
				case "16":
					timeCheckBox.setSelected(true);
					value = oilLog.getContent().replace("Time ", "");
					String time = value.substring(0, value.indexOf(" Lat "));
					value = value.replace(time+" Lat ", "");
					String lat = value.substring(0, value.indexOf(" Long "));
					value = value.replace(lat+" Long ", "");
					String lon = value;
					localTimeTextField.setLocalTime(LocalTime.parse(time, EasyDateUtils.logbookTimeFormatter));
					positionLatTextField.setText(lat);
					positionLongTextField.setText(lon);
//					log.setContent("Time "+ localTimeTextField.getLocalTime().toString() + " Lat " + positionLatTextField.getText() + " Long " + positionLongTextField.getText());
					break;
				case "17":
					systemHasBeenPutIntoAutomaticCheckBox.setSelected(true);
					value = oilLog.getContent();
					String firstLine = value.substring(0, value.indexOf("\n"));
					LocalTime ldt = LocalTime.parse(firstLine.replace("Start time: ", ""), EasyDateUtils.logbookTimeFormatter);
					transferOfBilgeWaterTimeField.setLocalTime(ldt);
					
					value = value.replace(firstLine+"\n", "");
					String [] tanks = value.split("\n");
					int index = 0;
					for(String tank : tanks) {
						Map<String, String> map = parseTank(tank);
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
//					log.setContent(holdingTankTextField.getText() + " Start time: " + transferOfBilgeWaterTimeField.getLocalTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
					break;
				case "18":
					systemHasBeenPutIntoManualOperationCheckBox.setSelected(true);
					value = oilLog.getContent().replace("Stop time: ", "");
					systemHasBeenPutIntoManualOperationTimeField.setLocalTime(LocalTime.parse(value, EasyDateUtils.logbookTimeFormatter));
//					log.setContent("Stop time: " + systemHasBeenPutIntoManualOperationTimeField.getLocalTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
					break;
				default:
					break;
			}
		}
	}

	public void clearFilledInfo() {
		timeCheckBox.setSelected(false);
		systemHasBeenPutIntoAutomaticCheckBox.setSelected(false);
		systemHasBeenPutIntoManualOperationCheckBox.setSelected(false);
		
		localTimeTextField.setLocalTime(null);
		positionLatTextField.setText("00°00.00'N");
		positionLongTextField.setText("000°00.00'W");
		transferOfBilgeWaterTimeField.setLocalTime(null);
		tankNameFromTanksJFXChipView.getChips().clear();
		((TankUsageContext)tankNameFromTanksJFXChipView.getUserData()).getUserTankQuantities().clear();
		systemHasBeenPutIntoManualOperationTimeField.setLocalTime(null);
        Platform.runLater(() -> {
        	tankRetainedBilgeTankDoubleField.clear();
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
	
	private Tank getTankFromSettingsByName(String tankName) {
		Tank tank = operationLogService.getTankByName(tankName);
		if(tank==null) logger.error("Not found SlopTank with name " + tankName);
		return tank;
	}
	
	
	
	
	public void openSelectQuantityDischargedToTanks(MouseEvent e) {
		selectTankerDialog.showDialog((Stage)((ImageViewButton)e.getSource()).getScene().getWindow(), tankNameFromTanksJFXChipView);
	}
	
	public void addBilgeTank(Event event) {
		Map<String, Node> map = addTank(bilgeGridPane);
		TankUsageContext tuc = new TankUsageContext(Stream.of(TankType.OILYBILGE).collect(Collectors.toList()), true, false);
		tuc.setNoNeedForUserTankQuantities(true);
		map.get("tankName").setUserData(tuc);
		
		JFXChipView<String> tankNameJFXChipView = (JFXChipView<String>) map.get("tankName");
		DoubleField tankFillVolume = (DoubleField) map.get("tankQuantity");
		DoubleField tankRetainedVolume = (DoubleField) map.get("tankRetained");
		tankNameJFXChipView.getChips().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> change) {
				Double totalUserTankQuantities = ((TankUsageContext)tankNameJFXChipView.getUserData()).getTotalUserTankActualLevel();
				tankRetainedVolume.setValue(totalUserTankQuantities + Optional.ofNullable(tankFillVolume.getValue()).orElse(0.0));
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
		
		Label identifyTheTankLabel = new Label("to holding tank (identify tank).");
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
//					double sum = ((TankUsageContext) tankNameJFXChipView.getUserData()).getTotalUserTankCapacity();
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
	
	private List<Pair<String, Tank>> getTanks(GridPane tanksGridPane) {
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
	

		StringTemplate template = bilgeTemplate;
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
			tankList.add(new Pair(template.toString(), tank));
		}
		return tankList;
	}
	
	private Map<String, String> parseTank(String tankString) {
		Map<String, String> map = new HashMap<>();
		Pattern pattern = bilgePattern;
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
