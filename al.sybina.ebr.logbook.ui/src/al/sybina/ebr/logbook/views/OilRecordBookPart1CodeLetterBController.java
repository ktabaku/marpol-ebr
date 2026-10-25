package al.sybina.ebr.logbook.views;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.tools.resources.WorkbenchResourceProvider;
import org.eclipse.e4.tools.services.IResourceProviderService;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;
import org.eclipse.fx.ui.services.resources.ImageProvider;
import org.geotools.measure.Latitude;
import org.geotools.measure.Longitude;
import org.osgi.service.event.Event;

import com.jfoenix.controls.JFXChipView;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.Tank;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.TankType;
import com.oceanpro.core.services.OperationLogReladomoService;

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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jfxtras.scene.control.ImageViewButton;
import jidefx.scene.control.field.DoubleField;
import jidefx.scene.control.field.FormattedTextField;

public class OilRecordBookPart1CodeLetterBController implements Initializable {

	
	protected String CACHE_FILE_NAME = System.getProperty("user.home") + "/.ebr/caches/caches.properties";
	private static final Logger logger = LoggerCreator.createLogger(OilRecordBookPart1CodeLetterBController.class);
	
	@Inject
	private EHandlerService handlerService;
	@Inject
	private ECommandService commandService;
	@Inject
	private IEclipseContext context;
	@Inject
	private IResourceProviderService resourcesService;
	@Inject
	private ImageProvider imageProvider;
	@Inject
	private OperationLogReladomoService operationLogReladomoService;
	@Inject
	private SelectTankerDialog selectTankerDialog;
	@Inject
	private HelpCodeLetterDialog helpCodeLetterDialog;
	@Inject
	private BallonMessage ballonMessage;
	@Inject
	private IEventBroker eventBroker;
	
	// codeLetter B
	@FXML
	private ImageViewButton imageViewButtonInfoLetterB;
	@FXML
	private ImageViewButton identityOfTanksImageViewButton;
	@FXML
	private CheckBox identityOfTanksCheckBox;
	@FXML
	private JFXChipView<String> identityOfTanksJFXChipView;
	@FXML
	private CheckBox positionOfShipAtStartCheckBox;
	@FXML
	private FormattedTextField<Latitude> positionOfShipAtStartLatTextField;
	@FXML
	private CheckBox positionOfShipOnCompletionCheckBox;
	@FXML
	private FormattedTextField<Longitude> positionOfShipAtStartLongTextField;
	@FXML
	private FormattedTextField<Latitude> positionOfShipOnCompletionLatTextField;
	@FXML
	private FormattedTextField<Longitude> positionOfShipOnCompletionLongTextField;
	@FXML
	private CheckBox shipSpeedsDuringDischargeCheckBox;
	@FXML
	private DoubleField shipSpeedDuringDischargeDoubleField;
	@FXML
	private CheckBox methodOfDischargeCheckBox;
	@FXML
	private CheckBox through15PpmEquipmentCheckBox;
	@FXML
	private CheckBox toReceptionFacilitiesCheckBox;
	@FXML
	private CheckBox quantityDischargedCheckBox;
	@FXML
	private DoubleField quantityDischargedDoubleField;
	@FXML
	private TextField facilitiesNameTextField;
	@FXML
	private TextField receiptNoTextField;
	@FXML
	private ImageViewButton uploadReceiptImageViewButton;
	@FXML
	public AnchorPane codeLetterContentAnchorPane;
	private ObservableList<Image> helpImageList = FXCollections.observableArrayList();
	private OilLog oilLog1;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MathUtils.setStringConverter(shipSpeedDuringDischargeDoubleField);
		MathUtils.setStringConverter(quantityDischargedDoubleField);
		try {									
			Image helpImageLetterB1 = imageProvider
					.getImage(((WorkbenchResourceProvider) resourcesService)
							.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_HELPB1));  
			
			Image helpImageLetterB2 = imageProvider
					.getImage(((WorkbenchResourceProvider) resourcesService)
							.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_HELPB2));  
			
			Image helpImageLetterB3 = imageProvider
					.getImage(((WorkbenchResourceProvider) resourcesService)
							.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_HELPB3));  
			
			helpImageList.addAll(helpImageLetterB1, helpImageLetterB2, helpImageLetterB3);
			Image addImageIdentityOfTanksLetterB = imageProvider
					.getImage(((WorkbenchResourceProvider) resourcesService)
							.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_ADD));  
			identityOfTanksImageViewButton.setImage(addImageIdentityOfTanksLetterB);
			
			Image infoImageLetterB = imageProvider
					.getImage(((WorkbenchResourceProvider) resourcesService)
							.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));  
			imageViewButtonInfoLetterB.setImage(infoImageLetterB);

			
			
			FileChooser fileChooser = new FileChooser();
			Properties properties = new Properties();
			String userInitialDirectory = null;
	        try {
	            File cacheFile = new File(CACHE_FILE_NAME);
	            if (cacheFile.isFile()) {
	            	try (FileInputStream cacheIn = new FileInputStream(cacheFile)) {
	            		properties.load(cacheIn);
	            	}
	            }
	            userInitialDirectory = properties.getProperty("logbookReceiptDir");
	        } catch (Exception ex) {
	        	logger.error(ex.getMessage(), ex);
	        }
	        
	        if (userInitialDirectory == null) {
	            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
	        } else {
	            fileChooser.setInitialDirectory(new File(userInitialDirectory));
	        }
			FileChooser.ExtensionFilter pdfFilesFilter = new FileChooser.ExtensionFilter("PDF files", "*.pdf");
			fileChooser.getExtensionFilters().addAll(pdfFilesFilter);
			
	        uploadReceiptImageViewButton.setImage(new Image(getClass().getResourceAsStream("/images/uploadReceipt.png")));
			uploadReceiptImageViewButton.setOnMouseClicked(me -> {
				File selectedFile = null;
				try {
					selectedFile = fileChooser.showOpenDialog((Stage) ((ImageViewButton) me.getSource()).getScene().getWindow());
				} catch(IllegalArgumentException e) {
					fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
					selectedFile = fileChooser.showOpenDialog((Stage) ((ImageViewButton) me.getSource()).getScene().getWindow());
				}
				if(selectedFile!=null) {
					properties.setProperty("logbookReceiptDir", selectedFile.getParent());
					File f = new File(CACHE_FILE_NAME);
					if(!f.exists()) f.getParentFile().mkdirs();
					OutputStream out;
					try {
						out = new FileOutputStream(f);
						properties.store(out, "Last directory choosed by user");
						out.close();
					} catch (IOException ex) {
						logger.error("Last directory choosed by user " + ex.toString());
					}
					receiptNoTextField.setText(selectedFile.getName());
					receiptNoTextField.setUserData(selectedFile);
				}
			});
			
			CoordinateUIUtils.initializeLatitudeField(positionOfShipAtStartLatTextField);
			CoordinateUIUtils.initializeLongitudeField(positionOfShipAtStartLongTextField);
			CoordinateUIUtils.initializeLatitudeField(positionOfShipOnCompletionLatTextField);
			CoordinateUIUtils.initializeLongitudeField(positionOfShipOnCompletionLongTextField);
			facilitiesNameTextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
			JavaFXUtils.addFilledPseudoClass(positionOfShipAtStartLongTextField);
			JavaFXUtils.addFilledPseudoClass(positionOfShipAtStartLatTextField);
			JavaFXUtils.addFilledPseudoClass(positionOfShipOnCompletionLatTextField);
			JavaFXUtils.addFilledPseudoClass(positionOfShipOnCompletionLongTextField);
			JavaFXUtils.addFilledPseudoClass(shipSpeedDuringDischargeDoubleField);
			JavaFXUtils.addFilledPseudoClass(facilitiesNameTextField);
			JavaFXUtils.addFilledPseudoClass(receiptNoTextField);
			JavaFXUtils.addFilledPseudoClass(quantityDischargedDoubleField);
		} catch (IOException e) {    
			e.printStackTrace();         
		}

		identityOfTanksJFXChipView.setUserData(new TankUsageContext(Stream.of(TankType.BALLAST).collect(Collectors.toList()), false, true));
		identityOfTanksCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					identityOfTanksJFXChipView.setDisable(false);
					identityOfTanksImageViewButton.setDisable(false);
					positionOfShipAtStartCheckBox.setSelected(true);
					positionOfShipOnCompletionCheckBox.setSelected(true);
					shipSpeedsDuringDischargeCheckBox.setSelected(true);
					methodOfDischargeCheckBox.setSelected(true);
					through15PpmEquipmentCheckBox.setSelected(true);
					quantityDischargedCheckBox.setSelected(true);
					oilLog1 = operationLogReladomoService.getLastOil1CodeA1Log();
					if(oilLog1!=null) {
						String message = "The Ballast Tank refer to Code Letter A1 is\n" + oilLog1.getContent();
						ballonMessage.showGeneralBalloon(identityOfTanksImageViewButton, new Label(message));
					}
				} else {
					identityOfTanksJFXChipView.setDisable(true);
					identityOfTanksImageViewButton.setDisable(true);
					positionOfShipAtStartCheckBox.setSelected(false);
					positionOfShipOnCompletionCheckBox.setSelected(false);
					shipSpeedsDuringDischargeCheckBox.setSelected(false);
					methodOfDischargeCheckBox.setSelected(false);
					through15PpmEquipmentCheckBox.setSelected(false);
					quantityDischargedCheckBox.setSelected(false);
				}
				identityOfTanksJFXChipView.getChips().clear();
			}
		});
		addRemoveListener(identityOfTanksJFXChipView);
		identityOfTanksJFXChipView.getChips().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> c) {
				if(quantityDischargedCheckBox.isSelected()) {
					Double totalUserTankQuantities = ((TankUsageContext)identityOfTanksJFXChipView.getUserData()).getTotalUserTankQuantities();
					quantityDischargedDoubleField.setValue(totalUserTankQuantities);
				}
				JavaFXUtils.addTooltip(identityOfTanksJFXChipView);
			}
		});
		positionOfShipAtStartCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					positionOfShipAtStartLatTextField.setDisable(false);
					positionOfShipAtStartLongTextField.setDisable(false);
				} else {
					positionOfShipAtStartLatTextField.setDisable(true);
					positionOfShipAtStartLongTextField.setDisable(true);
				}
				positionOfShipAtStartLatTextField.setText("00°00.00'N");
				positionOfShipAtStartLongTextField.setText("000°00.00'W");
			}
		});
		positionOfShipOnCompletionCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					positionOfShipOnCompletionLatTextField.setDisable(false);
					positionOfShipOnCompletionLongTextField.setDisable(false);
				} else {
					positionOfShipOnCompletionLatTextField.setDisable(true);
					positionOfShipOnCompletionLongTextField.setDisable(true);
				}
				positionOfShipOnCompletionLatTextField.setText("00°00.00'N");
				positionOfShipOnCompletionLongTextField.setText("000°00.00'W");
			}
		});
		shipSpeedsDuringDischargeCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					shipSpeedDuringDischargeDoubleField.setDisable(false);
				} else {
					shipSpeedDuringDischargeDoubleField.setDisable(true);
				}
				shipSpeedDuringDischargeDoubleField.setValue(null);
			}
		});
		methodOfDischargeCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					through15PpmEquipmentCheckBox.setDisable(false);
					toReceptionFacilitiesCheckBox.setDisable(false);
				} else {
					through15PpmEquipmentCheckBox.setSelected(false);
					toReceptionFacilitiesCheckBox.setSelected(false);
					through15PpmEquipmentCheckBox.setDisable(true);
					toReceptionFacilitiesCheckBox.setDisable(true);
				}
			}
		});
		through15PpmEquipmentCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					toReceptionFacilitiesCheckBox.setSelected(false);
					toReceptionFacilitiesCheckBox.setDisable(true);
				} else {
					toReceptionFacilitiesCheckBox.setDisable(false);
				}
			}
		});
		toReceptionFacilitiesCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					through15PpmEquipmentCheckBox.setSelected(false);
					through15PpmEquipmentCheckBox.setDisable(true);
					facilitiesNameTextField.setDisable(false);
					receiptNoTextField.setDisable(false);
					uploadReceiptImageViewButton.setDisable(false);
					positionOfShipAtStartCheckBox.setSelected(false);
					positionOfShipAtStartCheckBox.setDisable(true);
					positionOfShipOnCompletionCheckBox.setSelected(false);
					positionOfShipOnCompletionCheckBox.setDisable(true);
					shipSpeedsDuringDischargeCheckBox.setSelected(false);
					shipSpeedsDuringDischargeCheckBox.setDisable(true);
				} else {
					through15PpmEquipmentCheckBox.setDisable(false);
					facilitiesNameTextField.setText("");
					facilitiesNameTextField.setDisable(true);
					receiptNoTextField.setText("");
					receiptNoTextField.setDisable(true);
					receiptNoTextField.setUserData(null);
					uploadReceiptImageViewButton.setDisable(true);
					positionOfShipAtStartCheckBox.setDisable(false);
					positionOfShipOnCompletionCheckBox.setDisable(false);
					shipSpeedsDuringDischargeCheckBox.setDisable(false);
				}
			}
		});
		quantityDischargedCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
//					quantityDischargedDoubleField.setDisable(false);
					if(identityOfTanksCheckBox.isSelected()) {
						Double totalUserTankQuantities = ((TankUsageContext)identityOfTanksJFXChipView.getUserData()).getTotalUserTankQuantities();
						quantityDischargedDoubleField.setValue(totalUserTankQuantities);
					}
				} else {
//					quantityDischargedDoubleField.setDisable(true);
					quantityDischargedDoubleField.setValue(null);
				}
//				if(quantityDischargedDoubleField.isDisabled()) {
//					OilLog oilLog1 = operationLogReladomoService.getLastOil1CodeA3_3Log();
//					OilLog oilLog2 = operationLogReladomoService.getLastOil1CodeA4_2Log();
//					OilLog latestOilLog = null;
//					if(oilLog1!=null && oilLog2!=null) {
//						latestOilLog = oilLog1.getBusinessDate().after(oilLog2.getBusinessDate())?oilLog1:oilLog2;
//					} else if(oilLog1!=null) {
//						latestOilLog = oilLog1;
//					} else if(oilLog2!=null) {
//						latestOilLog = oilLog2;
//					}
//					if(latestOilLog!=null) {
//						String message = "The quantity of Ballast Tank refer to Code Letter A3.3 or A4.2 is " + latestOilLog.getContent();
//						ballonMessage.showGeneralBalloon(quantityDischargedDoubleField, new Label(message));
//					}
//				}
			}
		});
		
		identityOfTanksImageViewButton.setOnMouseEntered(mouseEvent -> {
			if(!mouseEvent.isPrimaryButtonDown()) {
				identityOfTanksImageViewButton.setCursor(Cursor.HAND);
				if(oilLog1!=null) {
					String message = "The Ballast Tank refer to Code Letter A1 is\n" + oilLog1.getContent();
					ballonMessage.showGeneralBalloon(identityOfTanksImageViewButton, new Label(message));
				}
			}
		});
		
		identityOfTanksImageViewButton.setOnMouseExited(mouseEvent -> {
			if(!mouseEvent.isPrimaryButtonDown()) {
				identityOfTanksImageViewButton.setCursor(Cursor.DEFAULT);
				ballonMessage.closeBalloon();
			}
		});

		
		eventBroker.subscribe(Activator.MARPOL_DETAILS_RELOADED, new org.osgi.service.event.EventHandler() {
			@Override
			public void handleEvent(Event event) {
				ArrayList<Long> changedTankIds = (ArrayList<Long>) event.getProperty(Activator.TANK_IDS);
    			if(changedTankIds==null) {
    				identityOfTanksJFXChipView.getChips().clear();
    			} else if(changedTankIds!=null && !changedTankIds.isEmpty()) {
    				Activator.removeChangedTanksFromJFXChipView(changedTankIds, identityOfTanksJFXChipView);
    			}
			}
		});
	}


	public void processB(OilLog logToInsert, List<OilLog> logToInsertList, Map<Validation, Object> validations) {
		validateFields(validations);
		
		if (identityOfTanksCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("5");
			log.setContent("CO TANK(S) NR. "+String.join(", ", identityOfTanksJFXChipView.getChips()));
			log.setUserTankActualLevels(((TankUsageContext)identityOfTanksJFXChipView.getUserData()).getUserTankActualLevels());
			log.setOrderInGroup("H");
			logToInsertList.add(log);
		}
		if (positionOfShipAtStartCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("6");
			log.setContent("START POSITION: LAT " + positionOfShipAtStartLatTextField.getText() + "; LONG:" + positionOfShipAtStartLongTextField.getText());
			log.setOrderInGroup("I");
			logToInsertList.add(log);
		}
		if (positionOfShipOnCompletionCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("7");
			log.setContent("STOP POSITION: LAT " + positionOfShipOnCompletionLatTextField.getText() + "; LONG:" + positionOfShipOnCompletionLongTextField.getText());
			log.setOrderInGroup("J");
			logToInsertList.add(log);
		}
		if (shipSpeedsDuringDischargeCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("8");
			log.setContent("SPEED: "+shipSpeedDuringDischargeDoubleField.getValue());
			log.setOrderInGroup("K");
			logToInsertList.add(log);
		}
		if (methodOfDischargeCheckBox.isSelected()) {
			if(through15PpmEquipmentCheckBox.isSelected()) {
				OilLog log = (OilLog) logToInsert.clone();
				log.setCodeNumber("9.1");
				log.setContent("DISCHARGED: THROUGH 15PPM EQUIPMENT");
				log.setOrderInGroup("L");
				logToInsertList.add(log);
			}else if(toReceptionFacilitiesCheckBox.isSelected()) {
				OilLog log = (OilLog) logToInsert.clone();
				log.setCodeNumber("9.2");
				log.setContent("TO RECEPTION FACILITIES " + facilitiesNameTextField.getText() + "- RECEIPT FILE '" + receiptNoTextField.getText() + "'");
				if(receiptNoTextField.getUserData()!=null && ((File)receiptNoTextField.getUserData()).exists()) {
					log.setAttachmentName(((File)receiptNoTextField.getUserData()).getName());
					byte[] attachmentBytes = null;
					try {
						attachmentBytes = Files.readAllBytes(((File)receiptNoTextField.getUserData()).toPath());
					} catch (IOException e) {
						logger.error(e.getMessage(), e);
					}
					log.setAttachmentData(attachmentBytes);
				}
				log.setOrderInGroup("M");
				logToInsertList.add(log);
			}
		}
		if (quantityDischargedCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("10");
			log.setContent("QUANTITY DISCHARGED: "+quantityDischargedDoubleField.getValue()+ " M3");
			log.setOrderInGroup("N");
			logToInsertList.add(log);
		}
	}

	private void validateFields(Map<Validation, Object> validations) {
		if (identityOfTanksCheckBox.isSelected() && identityOfTanksJFXChipView.getChips().isEmpty()) {
			validations.put(Validation.B5_EMPTY, null);
		}
		if (positionOfShipAtStartCheckBox.isSelected() && positionOfShipAtStartLatTextField.getText().trim().isEmpty()) {
			validations.put(Validation.B6_LATITUDE_EMPTY, null);
		}
		if (positionOfShipAtStartCheckBox.isSelected() && positionOfShipAtStartLongTextField.getText().trim().isEmpty()) {
			validations.put(Validation.B6_LONGITUDE_EMPTY, null);
		}
		if (positionOfShipOnCompletionCheckBox.isSelected() && positionOfShipOnCompletionLatTextField.getText().trim().isEmpty()) {
			validations.put(Validation.B7_LATITUDE_EMPTY, null);
		}
		if (positionOfShipOnCompletionCheckBox.isSelected() && positionOfShipOnCompletionLongTextField.getText().trim().isEmpty()) {
			validations.put(Validation.B7_LONGITUDE_EMPTY, null);
		}
		if (shipSpeedsDuringDischargeCheckBox.isSelected() && shipSpeedDuringDischargeDoubleField.getValue()==null) {
			validations.put(Validation.B8_EMPTY, null);
		}
		if(methodOfDischargeCheckBox.isSelected() && !through15PpmEquipmentCheckBox.isSelected() && !toReceptionFacilitiesCheckBox.isSelected()) {
			validations.put(Validation.B9_EMPTY, null);
		}
		if(toReceptionFacilitiesCheckBox.isSelected() && (facilitiesNameTextField.getText().trim().isEmpty() || receiptNoTextField.getText().trim().isEmpty() || receiptNoTextField.getUserData()==null || !((File)receiptNoTextField.getUserData()).exists())) {
			validations.put(Validation.B9_FACILITIES_OR_RECEIPT_EMPTY, null);
		}
		if (quantityDischargedCheckBox.isSelected() && quantityDischargedDoubleField.getValue()==null) {
			validations.put(Validation.B10_EMPTY, null);
		}

		
		if (!identityOfTanksCheckBox.isSelected()) {
			validations.put(Validation.B5_REQUIRED, null);
		} else {
			OilLog oilLog = operationLogReladomoService.getLastOil1CodeA1Log();
			if(oilLog!=null) {
				String value = oilLog.getContent().replace("CO Tank(s) Nr. ", "");
				HashSet<String> previousValueHashSet = new HashSet<>(Arrays.asList(value.split(", ")));
				HashSet<String> valueHashSet = new HashSet<>(identityOfTanksJFXChipView.getChips());
				if(!valueHashSet.equals(previousValueHashSet)) {
					validations.put(Validation.B5_VALUE_ERROR, value);
				}
			} else {
				validations.put(Validation.B5_DEPENDENCY_ERROR, null);
			}
		}
		if(!methodOfDischargeCheckBox.isSelected()) {
			validations.put(Validation.B9_REQUIRED, null);
		} else {
			if(through15PpmEquipmentCheckBox.isSelected()) {
				if(!positionOfShipAtStartCheckBox.isSelected() || !positionOfShipOnCompletionCheckBox.isSelected() || !shipSpeedsDuringDischargeCheckBox.isSelected()) {
					validations.put(Validation.B6_B7_B8_VALUE_REQUIRED, null);
				}
			} else if(toReceptionFacilitiesCheckBox.isSelected()) {
				if(positionOfShipAtStartCheckBox.isSelected() || positionOfShipOnCompletionCheckBox.isSelected() || shipSpeedsDuringDischargeCheckBox.isSelected()) {
					validations.put(Validation.B6_B7_B8_VALUE_NOT_REQUIRED, null);
				}
			}
		}
		if (!quantityDischargedCheckBox.isSelected()) {
			validations.put(Validation.B10_REQUIRED, null);
		} else {
			OilLog oilLog1 = operationLogReladomoService.getLastOil1CodeA3_3Log();
			OilLog oilLog2 = operationLogReladomoService.getLastOil1CodeA4_2Log();
			OilLog oilLog = null;
			if(oilLog1 == null) oilLog=oilLog2;
			if(oilLog2 == null) oilLog=oilLog1;
			if(oilLog1!=null && oilLog2!=null) {
				if(oilLog1.getBusinessDate().after(oilLog2.getBusinessDate())) oilLog=oilLog1;
				else oilLog=oilLog2;
			}
			if(oilLog!=null) {
				Double quantity = null;
				try {
					if(oilLog.getCodeNumber().equals("3.3")) {
						String quantityStr = oilLog.getContent().substring(oilLog.getContent().lastIndexOf(" "));
						quantity = MathUtils.decimalFormat.parse(quantityStr).doubleValue();
					} else if(oilLog.getCodeNumber().equals("4.2")) {
						quantity = MathUtils.decimalFormat.parse(oilLog.getContent()).doubleValue();
					}
				} catch (ParseException e) {
					logger.error(e.getMessage(), e);
				}
				if(quantityDischargedDoubleField.getValue()!=null && quantity!=null
						&& !(Math.abs(quantityDischargedDoubleField.getValue() - quantity) < MathUtils.DOUBLE_THRESHOLD)) {
					validations.put(Validation.B10_VALUE_ERROR, new Double[] {quantity, quantityDischargedDoubleField.getValue()});
				}
			} else {
				validations.put(Validation.B10_DEPENDENCY_ERROR, null);
			}
		}
		if(positionOfShipAtStartCheckBox.isSelected()) {
			if(positionOfShipAtStartLatTextField.getText().equals("00°00.00'N") || positionOfShipAtStartLongTextField.getText().equals("000°00.00'W")) {
				validations.put(Validation.B6_LAT_LON_ZERO, null);
			}
		}
		if(positionOfShipOnCompletionCheckBox.isSelected()) {
			if(positionOfShipOnCompletionLatTextField.getText().equals("00°00.00'N") || positionOfShipOnCompletionLongTextField.getText().equals("000°00.00'W")) {
				validations.put(Validation.B7_LAT_LON_ZERO, null);
			}
		}
	}
	
	public void openIdentifyTanksLetterB(MouseEvent e) {
		selectTankerDialog.showDialog((Stage) ((ImageViewButton) e.getSource()).getScene().getWindow(),
				identityOfTanksJFXChipView);
	}


	public void letterB(MouseEvent event) {
		helpCodeLetterDialog.showDialog((Stage)((ImageViewButton)event.getSource()).getScene().getWindow(),helpImageList);
	}


	public void restoreLogGroup(List<OilLog> logGroup) {
		if(logGroup==null || logGroup.isEmpty()) return;
		List<OilLog> codeBOilLogs = logGroup.stream().filter(c -> c.getCodeLetter().equals("B")).collect(Collectors.toList());
		for (OilLog oilLog : codeBOilLogs) {
			if(oilLog.getCorrectionNote()!=null && oilLog.getCorrectionNote().equals("Corrected")) continue;
			String value;
			switch (oilLog.getCodeNumber()) {
				case "5":
					identityOfTanksCheckBox.setSelected(true);
					value = oilLog.getContent().replace("CO Tank(s) Nr. ", "");
					identityOfTanksJFXChipView.getChips().addAll(Arrays.asList(value.split(", ")));
					break;
				case "6":
					positionOfShipAtStartCheckBox.setSelected(true);
					value = oilLog.getContent().replace("Start position: Lat ", "");
					String lat = value.substring(0, value.indexOf("; Long:"));
					value = value.replace(lat+"; Long:", "");
					String lon = value;
					positionOfShipAtStartLatTextField.setText(lat);
					positionOfShipAtStartLongTextField.setText(lon);
//					log.setContent("Start position: Lat " + positionOfShipAtStartLatTextField.getText() + "; Long:" + positionOfShipAtStartLongTextField.getText());
					break;
				case "7":
					positionOfShipOnCompletionCheckBox.setSelected(true);
					value = oilLog.getContent().replace("Stop position: Lat ", "");
					String lat2 = value.substring(0, value.indexOf("; Long:"));
					value = value.replace(lat2+"; Long:", "");
					String lon2 = value;
					positionOfShipOnCompletionLatTextField.setText(lat2);
					positionOfShipOnCompletionLongTextField.setText(lon2);
//					log.setContent("Stop position: Lat " + positionOfShipOnCompletionLatTextField.getText() + "; Long:" + positionOfShipOnCompletionLongTextField.getText());
					break;
				case "8":
					shipSpeedsDuringDischargeCheckBox.setSelected(true);
					value = oilLog.getContent().replace("Speed: ", "");
					shipSpeedDuringDischargeDoubleField.setValue(Double.parseDouble(value));
//					log.setContent("Speed: "+shipSpeedDuringDischargeTextField.getText());
					break;
				case "9.1":
					methodOfDischargeCheckBox.setSelected(true);
					if(oilLog.getContent().equals("Discharged: Through 15ppm equipment")) {
						through15PpmEquipmentCheckBox.setSelected(true);
					}
//					log.setContent("Discharged: Through 15ppm equipment");
					break;
				case "9.2":	
					if(oilLog.getContent().indexOf("To reception facilities")!=-1) {
						toReceptionFacilitiesCheckBox.setSelected(true);
						value= oilLog.getContent().replace("To reception facilities ", "");
						String facilitiesName = value.substring(0, value.indexOf(" receipt No. "));
						String receiptNumber = value.replace(facilitiesName + " receipt No. ", "");
						facilitiesNameTextField.setText(facilitiesName);
						receiptNoTextField.setText(receiptNumber);
//						log.setContent("To reception facilities " + facilitiesNameTextField.getText() + " receipt No. " + receiptNoTextField.getText());
					}
					break;
				case "10":
					quantityDischargedCheckBox.setSelected(true);
					value = oilLog.getContent().replace("Quantity discharged: ", "");
					value = value.substring(0, value.lastIndexOf(" M3"));
					quantityDischargedDoubleField.setValue(Double.parseDouble(value));
//					log.setContent("Quantity discharged: "+quantityDischargedTextField.getText()+ " M3");
					break;
				default:
					break;
			}
		}
	}

	public void clearFilledInfo() {
		identityOfTanksCheckBox.setSelected(false);
		positionOfShipAtStartCheckBox.setSelected(false);
		positionOfShipOnCompletionCheckBox.setSelected(false);
		shipSpeedsDuringDischargeCheckBox.setSelected(false);
		methodOfDischargeCheckBox.setSelected(false);
		through15PpmEquipmentCheckBox.setSelected(false);
		toReceptionFacilitiesCheckBox.setSelected(false);
		quantityDischargedCheckBox.setSelected(false);
		
		identityOfTanksJFXChipView.getChips().clear();
		((TankUsageContext)identityOfTanksJFXChipView.getUserData()).getUserTankQuantities().clear();
		positionOfShipAtStartLatTextField.setText("00°00.00'N");
		positionOfShipAtStartLongTextField.setText("000°00.00'W");
		positionOfShipOnCompletionLatTextField.setText("00°00.00'N");
		positionOfShipOnCompletionLongTextField.setText("000°00.00'W");
		shipSpeedDuringDischargeDoubleField.setValue(null);
		quantityDischargedDoubleField.setValue(null);
	}

	
	private void addRemoveListener(JFXChipView jFXChipView) {
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
