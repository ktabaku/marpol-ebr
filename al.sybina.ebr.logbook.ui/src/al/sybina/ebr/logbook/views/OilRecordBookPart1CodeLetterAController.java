package al.sybina.ebr.logbook.views;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.tools.resources.WorkbenchResourceProvider;
import org.eclipse.e4.tools.services.IResourceProviderService;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;
import org.eclipse.fx.ui.services.resources.ImageProvider;
import org.osgi.service.event.Event;

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
import al.sybina.ebr.logbook.views.OilRecordBookPart1ViewController.Validation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

public class OilRecordBookPart1CodeLetterAController implements Initializable {

	private static final Label infoMessageLabel = new Label("Identify Tank(s) in which one or another \r\n" + 
			"method has been employed  (rinsing \r\n" + 
			"through,  steaming, cleaning with chemicals\r\n" + 
			"type."); 
	private static final Logger logger = LoggerCreator.createLogger(OilRecordBookPart1CodeLetterAController.class);
	
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
	@Inject
	private IEclipseContext context;
	@Inject
	private IEventBroker eventBroker;
	
	@FXML
	private AnchorPane anchorPaneEraseOperationInfo;		
	@FXML
	private AnchorPane anchorPane2;
	@FXML
	private ImageViewButton identifyTanksImageViewButton;
	@FXML
	private ImageViewButton identityOfTanksBallastedImageViewButton;
	@FXML
	private ImageViewButton identifyTanksCleaningWaterImageViewButton;
	@FXML
	private ImageViewButton imageViewIdentifyTanksInfo;
	@FXML
	private ImageViewButton imageViewButtonInfoLetterA;
	

	@FXML
	private CheckBox identityOfTanksBallastedCheckBox;
	@FXML
	private JFXChipView<String> tanksBallastedJFXChipView;
	@FXML
	private CheckBox whetherCleanedCheckBox;
	@FXML
	private CheckBox checkBoxCleaned;
	@FXML
	private CheckBox checkBoxNotCleaned;
	@FXML
	private TextField textFieldTypeOfOilPreviouslycarried;

	@FXML
	private CheckBox cleaningProcessCheckBox;

	@FXML
	private CheckBox positionOfShipLatCheckBox;
	@FXML
	private FormattedTextField textFieldPozitionOfShipLat;
	@FXML
	private FormattedTextField longTextField;	
	@FXML
	private LocalTimeTextField setTimeAtThestart;	
	@FXML
	private LocalTimeTextField setTimeEndOfCleaning;
	@FXML
	private CheckBox identifyTanksInwhichOneCheckBox;
	@FXML
	private JFXChipView<String> identifyTanksJFXChipView;
	@FXML
	private DoubleField qtyOfChemicalsDoubleField;
	@FXML
	private CheckBox identityOfTanksInToWhichCleaningWaterWasTransferredCheckBox;
	@FXML
	private JFXChipView<String> identityOfTanksIntoWhichCleaningWaterJFXChipView;	
	@FXML
	private DoubleField qtyInCmDoubleField;
	@FXML
	private CheckBox ballastingCheckBox;	
	@FXML
	private CheckBox pozitionOfShipLatitudeCheckBox;	
	@FXML
	private FormattedTextField pozitionOfShipLatitudeField;	
	@FXML
	private FormattedTextField longitudeField;	
	@FXML
	private LocalTimeTextField setTimeAtStart;	
	@FXML
	private LocalTimeTextField setTimeEnd;
	@FXML
	private CheckBox quantityOfBallastIfTanksAreNotCleanedCheckBox;
	@FXML
	private DoubleField quantityOfBallastIfTanksAreNotCleanedDoubleField;
	@FXML
	private TextField chemicalUsedTextField;
	@FXML
	public AnchorPane codeLetterContentAnchorPane;
	private ObservableList<Image> helpImageList = FXCollections.observableArrayList();
	private Label label1 = new Label("11. Collection of oil residues (sludge).\r\n" + 
			"\r\n" + 
			"Quantities of oil residues (sludge) retained on board.\r\n" +
			"The quantity must be recorded weekly 1^\r\n" + 
			"The quantity must be recorded weekly 1^\r\n" +
			"(This means that the quantity must be recorded once a \r\n" +
		    "week even if the voyage lasts more than one week).\r\n" + 
			"");
	private boolean cleaneNotCleanedListenerEnabled = true;

	private DateTimeFormatter dateTimeFormatter = EasyDateUtils.logbookPdfDateTimeFormatter;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MathUtils.setStringConverter(qtyOfChemicalsDoubleField);
		MathUtils.setStringConverter(qtyInCmDoubleField);
		MathUtils.setStringConverter(quantityOfBallastIfTanksAreNotCleanedDoubleField);
		setTimeAtThestart.setDateTimeFormatter(EasyDateUtils.logbookTimeFormatter);
		setTimeEndOfCleaning.setDateTimeFormatter(EasyDateUtils.logbookTimeFormatter);
		setTimeAtStart.setDateTimeFormatter(EasyDateUtils.logbookTimeFormatter);
		setTimeEnd.setDateTimeFormatter(EasyDateUtils.logbookTimeFormatter);
		textFieldTypeOfOilPreviouslycarried.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		chemicalUsedTextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		JavaFXUtils.addFilledPseudoClass(textFieldTypeOfOilPreviouslycarried);
		JavaFXUtils.addFilledPseudoClass(chemicalUsedTextField);
		JavaFXUtils.addFilledPseudoClass(textFieldPozitionOfShipLat);
		JavaFXUtils.addFilledPseudoClass(longTextField);
		JavaFXUtils.addFilledPseudoClass(qtyOfChemicalsDoubleField);
		JavaFXUtils.addFilledPseudoClass(qtyInCmDoubleField);
		JavaFXUtils.addFilledPseudoClass(pozitionOfShipLatitudeField);
		JavaFXUtils.addFilledPseudoClass(longitudeField);
		JavaFXUtils.addFilledPseudoClass(quantityOfBallastIfTanksAreNotCleanedDoubleField);
		JavaFXUtils.addFilledPseudoClass(setTimeAtThestart);
		JavaFXUtils.addFilledPseudoClass(setTimeEndOfCleaning);
		JavaFXUtils.addFilledPseudoClass(setTimeAtStart);
		JavaFXUtils.addFilledPseudoClass(setTimeEnd);

		try {
			Image helpImageLetterA1 = imageProvider
					.getImage(((WorkbenchResourceProvider) resourcesService)
							.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_HELPA1));  
			Image helpImageLetterA2 = imageProvider
					.getImage(((WorkbenchResourceProvider) resourcesService)
							.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_HELPA2));  
			
			Image helpImageLetterA3 = imageProvider
					.getImage(((WorkbenchResourceProvider) resourcesService)
							.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_HELPA3));  
			
			helpImageList.addAll(helpImageLetterA1, helpImageLetterA2, helpImageLetterA3);
			
			Image addImage = imageProvider
					.getImage(((WorkbenchResourceProvider) resourcesService)
							.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_ADD));  
			identityOfTanksBallastedImageViewButton.setImage(addImage);
			identifyTanksCleaningWaterImageViewButton.setImage(addImage);
			identifyTanksImageViewButton.setImage(addImage);
						
			Image infoIconImageInfoLetterA = imageProvider
					.getImage(((WorkbenchResourceProvider) resourcesService)
							.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));  
			imageViewButtonInfoLetterA.setImage(infoIconImageInfoLetterA);
			

			Image infoImageIdentityOfTanks = imageProvider
					.getImage(((WorkbenchResourceProvider) resourcesService)
							.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));  
			imageViewIdentifyTanksInfo.setImage(infoImageIdentityOfTanks);

				
			checkBoxCleaned.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
				if(cleaneNotCleanedListenerEnabled) {
					if(isNowSelected) {
						checkBoxNotCleaned.setSelected(false);
						textFieldTypeOfOilPreviouslycarried.setText("");
						textFieldTypeOfOilPreviouslycarried.setDisable(isNowSelected);
						ballastingCheckBox.setSelected(false);
						ballastingCheckBox.setDisable(true);
						cleaningProcessCheckBox.setDisable(false);
						cleaningProcessCheckBox.setSelected(true);
						positionOfShipLatCheckBox.setSelected(true);
						identityOfTanksInToWhichCleaningWaterWasTransferredCheckBox.setSelected(true);
					} else {
						checkBoxNotCleaned.setSelected(true);
						cleaningProcessCheckBox.setSelected(false);
						positionOfShipLatCheckBox.setSelected(false);
						identityOfTanksInToWhichCleaningWaterWasTransferredCheckBox.setSelected(false);
					}
				}
			});
			checkBoxNotCleaned.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
				if(cleaneNotCleanedListenerEnabled) {
					if(isNowSelected) {
						checkBoxCleaned.setSelected(false);
						textFieldTypeOfOilPreviouslycarried.setText("");
						textFieldTypeOfOilPreviouslycarried.setDisable(!isNowSelected);
						cleaningProcessCheckBox.setSelected(false);
						cleaningProcessCheckBox.setDisable(true);
						ballastingCheckBox.setDisable(false);
						ballastingCheckBox.setSelected(true);
						pozitionOfShipLatitudeCheckBox.setSelected(true);
						quantityOfBallastIfTanksAreNotCleanedCheckBox.setSelected(true);
					} else {
						checkBoxCleaned.setSelected(true);
						ballastingCheckBox.setSelected(false);
						pozitionOfShipLatitudeCheckBox.setSelected(false);
						quantityOfBallastIfTanksAreNotCleanedCheckBox.setSelected(false);
					}
				}
			});
			
					
			CoordinateUIUtils.initializeLatitudeField(textFieldPozitionOfShipLat);
			CoordinateUIUtils.initializeLongitudeField(longTextField);
			CoordinateUIUtils.initializeLatitudeField(pozitionOfShipLatitudeField);
			CoordinateUIUtils.initializeLongitudeField(longitudeField);
		} catch (IOException e) {    
			e.printStackTrace();         
		}
		
		tanksBallastedJFXChipView.setUserData(new TankUsageContext(Stream.of(TankType.BALLAST, TankType.CARGO, TankType.FUELOIL, TankType.OILYBILGE, TankType.SLOP, TankType.SLUDGE).collect(Collectors.toList()), true, true));
		identityOfTanksBallastedCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					identityOfTanksBallastedImageViewButton.setDisable(false);
					tanksBallastedJFXChipView.setDisable(false);
					whetherCleanedCheckBox.setSelected(true);
				} else {
					identityOfTanksBallastedImageViewButton.setDisable(true);
					tanksBallastedJFXChipView.setDisable(true);
					whetherCleanedCheckBox.setSelected(false);
				}
				tanksBallastedJFXChipView.getChips().clear();	
			}

		});
		addRemoveListener(tanksBallastedJFXChipView);
		tanksBallastedJFXChipView.getChips().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> c) {
				if(quantityOfBallastIfTanksAreNotCleanedCheckBox.isSelected()) {
					Double totalUserTankQuantities = ((TankUsageContext)tanksBallastedJFXChipView.getUserData()).getTotalUserTankQuantities();
					quantityOfBallastIfTanksAreNotCleanedDoubleField.setValue(totalUserTankQuantities);
				}
				JavaFXUtils.addTooltip(tanksBallastedJFXChipView);
			}
		});
		whetherCleanedCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(!oldValue && newValue) {
					checkBoxCleaned.setDisable(false);
					checkBoxNotCleaned.setDisable(false);
					textFieldTypeOfOilPreviouslycarried.setDisable(true);
				} else {
					cleaneNotCleanedListenerEnabled = false;
					checkBoxCleaned.setSelected(false);
					checkBoxNotCleaned.setSelected(false);
					textFieldTypeOfOilPreviouslycarried.setText("");
					checkBoxCleaned.setDisable(true);
					checkBoxNotCleaned.setDisable(true);
					textFieldTypeOfOilPreviouslycarried.setDisable(true);
					cleaningProcessCheckBox.setSelected(false);
					ballastingCheckBox.setSelected(false);
					cleaningProcessCheckBox.setDisable(true);
					ballastingCheckBox.setDisable(true);
					cleaneNotCleanedListenerEnabled = true;
				}
			}

		});
		cleaningProcessCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					positionOfShipLatCheckBox.setDisable(false);
					identifyTanksInwhichOneCheckBox.setDisable(false);
					identityOfTanksInToWhichCleaningWaterWasTransferredCheckBox.setDisable(false);
				} else {
					positionOfShipLatCheckBox.setDisable(true);
					identifyTanksInwhichOneCheckBox.setDisable(true);
					identityOfTanksInToWhichCleaningWaterWasTransferredCheckBox.setDisable(true);
				}
				positionOfShipLatCheckBox.setSelected(false);
				identifyTanksInwhichOneCheckBox.setSelected(false);
				identityOfTanksInToWhichCleaningWaterWasTransferredCheckBox.setSelected(false);
			}

		});
		positionOfShipLatCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					textFieldPozitionOfShipLat.setDisable(false);
					longTextField.setDisable(false);
					setTimeAtThestart.setDisable(false);
					setTimeEndOfCleaning.setDisable(false);
				} else {
					textFieldPozitionOfShipLat.setDisable(true);
					longTextField.setDisable(true);
					setTimeAtThestart.setDisable(true);
					setTimeEndOfCleaning.setDisable(true);
				}
				textFieldPozitionOfShipLat.setText("00°00.00'N");
				longTextField.setText("000°00.00'W");
				setTimeAtThestart.setLocalTime(null);
				setTimeEndOfCleaning.setLocalTime(null);
			}
		});
		MathUtils.setMinMaxValues(qtyOfChemicalsDoubleField, 0.0, null);
		MathUtils.setMinMaxValues(qtyInCmDoubleField, 0.0, null);
		qtyInCmDoubleField.valueProperty().addListener(new ChangeListener<Double>() {
			@Override
			public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
				if (newValue != null && identityOfTanksIntoWhichCleaningWaterJFXChipView.getUserData()!=null) {
					double totalCapacity = ((TankUsageContext)identityOfTanksIntoWhichCleaningWaterJFXChipView.getUserData()).getTotalUserTankCapacity();
					double totalActualLevel = ((TankUsageContext)identityOfTanksIntoWhichCleaningWaterJFXChipView.getUserData()).getTotalUserTankActualLevel();
					if (newValue > totalCapacity - totalActualLevel) {
						OilRecordBookPart1ViewController.showWarning("Invalid value:" + newValue, "Valid value range:0.0-" + (totalCapacity - totalActualLevel));
						qtyInCmDoubleField.setValue(totalCapacity - totalActualLevel);
						return;
					}  else if(newValue > totalCapacity*0.98 - totalActualLevel) {
							OilRecordBookPart1ViewController.showWarning("Value:" + newValue+totalActualLevel, "Is greater than 98% of max value (" + totalCapacity +")!");
					}
				}
			}
		});
		TankUsageContext tuc = new TankUsageContext();
		tuc.setNoNeedForUserTankQuantities(true);
		addRemoveListener(identifyTanksJFXChipView);
		identifyTanksJFXChipView.setUserData(tuc);
		identifyTanksInwhichOneCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					identifyTanksImageViewButton.setDisable(false);
					identifyTanksJFXChipView.setDisable(false);
					qtyOfChemicalsDoubleField.setDisable(false);
					chemicalUsedTextField.setDisable(false);
				} else {
					identifyTanksImageViewButton.setDisable(true);
					identifyTanksJFXChipView.setDisable(true);
					qtyOfChemicalsDoubleField.setDisable(true);
					chemicalUsedTextField.setDisable(true);
				}
				identifyTanksJFXChipView.getChips().clear();
				qtyOfChemicalsDoubleField.setValue(null);
				chemicalUsedTextField.setText("");
			}

		});
		addRemoveListener(identityOfTanksIntoWhichCleaningWaterJFXChipView);
		identityOfTanksIntoWhichCleaningWaterJFXChipView.setUserData(new TankUsageContext(Stream.of(TankType.BALLAST, TankType.CARGO, TankType.FUELOIL, TankType.OILYBILGE, TankType.SLOP, TankType.SLUDGE).collect(Collectors.toList()), true, true));
		identityOfTanksInToWhichCleaningWaterWasTransferredCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {				
				if(newValue) {
					identifyTanksCleaningWaterImageViewButton.setDisable(false);
					identityOfTanksIntoWhichCleaningWaterJFXChipView.setDisable(false);
					qtyInCmDoubleField.setDisable(false);
				} else {
					identifyTanksCleaningWaterImageViewButton.setDisable(true);
					identityOfTanksIntoWhichCleaningWaterJFXChipView.setDisable(true);
					qtyInCmDoubleField.setDisable(true);
				}
				identityOfTanksIntoWhichCleaningWaterJFXChipView.getChips().clear();
				qtyInCmDoubleField.setValue(null);
			}

		});
		identityOfTanksIntoWhichCleaningWaterJFXChipView.getChips().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> change) {
				while (change.next()) {
					Double totalUserTankQuantities = ((TankUsageContext)identityOfTanksIntoWhichCleaningWaterJFXChipView.getUserData()).getTotalUserTankQuantities();
					qtyInCmDoubleField.setValue(totalUserTankQuantities);
				}
				JavaFXUtils.addTooltip(identityOfTanksIntoWhichCleaningWaterJFXChipView);
			}
		});
		ballastingCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					pozitionOfShipLatitudeCheckBox.setDisable(false);
					quantityOfBallastIfTanksAreNotCleanedCheckBox.setDisable(false);
				} else {
					pozitionOfShipLatitudeCheckBox.setSelected(false);
					pozitionOfShipLatitudeCheckBox.setDisable(true);
					quantityOfBallastIfTanksAreNotCleanedCheckBox.setSelected(false);
					quantityOfBallastIfTanksAreNotCleanedCheckBox.setDisable(true);
				}
			}
		});
		pozitionOfShipLatitudeCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					pozitionOfShipLatitudeField.setDisable(false);
					longitudeField.setDisable(false);
					setTimeAtStart.setDisable(false);
					setTimeEnd.setDisable(false);
				} else {
					pozitionOfShipLatitudeField.setDisable(true);
					longitudeField.setDisable(true);
					setTimeAtStart.setDisable(true);
					setTimeEnd.setDisable(true);
				}
				pozitionOfShipLatitudeField.setText("00°00.00'N");
				longitudeField.setText("000°00.00'W");
				setTimeAtStart.setLocalTime(null);
				setTimeEnd.setLocalTime(null);
			}
		});
		quantityOfBallastIfTanksAreNotCleanedCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
//					quantityOfBallastIfTanksAreNotCleanedDoubleField.setDisable(false);
					if(identityOfTanksBallastedCheckBox.isSelected()) {
						Double totalUserTankQuantities = ((TankUsageContext)tanksBallastedJFXChipView.getUserData()).getTotalUserTankQuantities();
						quantityOfBallastIfTanksAreNotCleanedDoubleField.setValue(totalUserTankQuantities);
					}
				} else {
					quantityOfBallastIfTanksAreNotCleanedDoubleField.clear();
//					quantityOfBallastIfTanksAreNotCleanedDoubleField.setDisable(true);
				}
			}
		});
		
//		identityOfTanksBallastedImageViewButton.setOnMouseEntered(mouseEvent -> {if(!mouseEvent.isPrimaryButtonDown()) identityOfTanksBallastedImageViewButton.setCursor(Cursor.HAND);});
//		identityOfTanksBallastedImageViewButton.setOnMouseExited(mouseEvent -> {if(!mouseEvent.isPrimaryButtonDown()) identityOfTanksBallastedImageViewButton.setCursor(Cursor.DEFAULT);});
		
//		buttonIdentifyTanks.setOnMouseEntered(mouseEvent -> {if(!mouseEvent.isPrimaryButtonDown()) buttonIdentifyTanks.setCursor(Cursor.HAND);});
//		buttonIdentifyTanks.setOnMouseExited(mouseEvent -> {if(!mouseEvent.isPrimaryButtonDown()) buttonIdentifyTanks.setCursor(Cursor.DEFAULT);});
//
//		buttonIdentifyOfTanksCleaningWater.setOnMouseEntered(mouseEvent -> {if(!mouseEvent.isPrimaryButtonDown()) buttonIdentifyOfTanksCleaningWater.setCursor(Cursor.HAND);});
//		buttonIdentifyOfTanksCleaningWater.setOnMouseExited(mouseEvent -> {if(!mouseEvent.isPrimaryButtonDown()) buttonIdentifyOfTanksCleaningWater.setCursor(Cursor.DEFAULT);});

		
		eventBroker.subscribe(Activator.MARPOL_DETAILS_RELOADED, new org.osgi.service.event.EventHandler() {
			@Override
			public void handleEvent(Event event) {
				ArrayList<Long> changedTankIds = (ArrayList<Long>) event.getProperty(Activator.TANK_IDS);
    			if(changedTankIds==null) {
    				identifyTanksJFXChipView.getChips().clear();
    				tanksBallastedJFXChipView.getChips().clear();
    				identityOfTanksIntoWhichCleaningWaterJFXChipView.getChips().clear();
    			} else if(changedTankIds!=null && !changedTankIds.isEmpty()) {
    				Activator.removeChangedTanksFromJFXChipView(changedTankIds, identifyTanksJFXChipView);
    				Activator.removeChangedTanksFromJFXChipView(changedTankIds, tanksBallastedJFXChipView);
    				Activator.removeChangedTanksFromJFXChipView(changedTankIds, identityOfTanksIntoWhichCleaningWaterJFXChipView);
    			}
			}
		});
	}
	

			



	public void openSelectTanksBalastedName(MouseEvent e) {
		selectTankerDialog.showDialog((Stage)((ImageViewButton)e.getSource()).getScene().getWindow(), tanksBallastedJFXChipView);
	}

	public void openIdentifyTanks(MouseEvent e) {
		selectTankerDialog.showDialog((Stage)((ImageViewButton)e.getSource()).getScene().getWindow(), identifyTanksJFXChipView);
	}

	public void openIdentiyTanksCleaningWater(MouseEvent e) {
		selectTankerDialog.showDialog((Stage)((ImageViewButton)e.getSource()).getScene().getWindow(), identityOfTanksIntoWhichCleaningWaterJFXChipView);
	}

			

			
			
	public void showBalloon(MouseEvent event) {
		ballonMessage.showGeneralBalloon((ImageViewButton) event.getTarget(), label1);
	}

	
	
	public void selectTanksBalastedName(ActionEvent e) {
		String value = ((Button) e.getSource()).getText();
	}
	
	public void letterA(MouseEvent event) {
		helpCodeLetterDialog.showDialog((Stage)((ImageViewButton)event.getSource()).getScene().getWindow(),helpImageList);
	}

	public void handleTaskBallasted(ActionEvent e) {
		anchorPane2.setVisible(true);
	}

	public void openInfoMessage(MouseEvent event) {
		ballonMessage.showGeneralBalloon((ImageViewButton) event.getTarget(), infoMessageLabel);
	}
	
	public void closeInfoMessageEraseOperation(ActionEvent e) {
		anchorPaneEraseOperationInfo.setVisible(false);
	}

	public void processA(OilLog logToInsert, List<OilLog> logToInsertList, Map<Validation, Object> validations) {
		validateFields(logToInsert.getBusinessDate(), validations);
		
		if (identityOfTanksBallastedCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("1");
			log.setContent(String.join(", ", tanksBallastedJFXChipView.getChips()));
			log.setUserTankActualLevels(((TankUsageContext)tanksBallastedJFXChipView.getUserData()).getUserTankActualLevels());
			log.setOrderInGroup("A");
			logToInsertList.add(log);
		}
		
		if (whetherCleanedCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("2");
			if (checkBoxCleaned.isSelected()) {
				log.setContent("YES " + textFieldTypeOfOilPreviouslycarried.getText());
			} else if (checkBoxNotCleaned.isSelected()) {
				log.setContent("NO " + textFieldTypeOfOilPreviouslycarried.getText());
			}
			log.setOrderInGroup("B");
			logToInsertList.add(log);
		}
		if (cleaningProcessCheckBox.isSelected() && positionOfShipLatCheckBox.isSelected() && setTimeAtThestart.getLocalTime()!=null && setTimeEndOfCleaning.getLocalTime()!=null) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("3.1");
			log.setContent("LAT: "+textFieldPozitionOfShipLat.getText()+" LON: "+longTextField.getText() + "\nSTART: "+setTimeAtThestart.getLocalTime().format(EasyDateUtils.logbookTimeFormatter) + " STOP: "+setTimeEndOfCleaning.getLocalTime().format(EasyDateUtils.logbookTimeFormatter));
			log.setOrderInGroup("C");
			logToInsertList.add(log);
		}
		
		if (identifyTanksInwhichOneCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("3.2");
			log.setContent(String.join(", ", identifyTanksJFXChipView.getChips()) + " CHEMICAL TYPE <<" + chemicalUsedTextField.getText() + ">>;Q.TY " + qtyOfChemicalsDoubleField.getValue() + " M3");
			log.setOrderInGroup("D");
			logToInsertList.add(log);
		}
		
		if (identityOfTanksInToWhichCleaningWaterWasTransferredCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("3.3");
			log.setContent(String.join(", ", identityOfTanksIntoWhichCleaningWaterJFXChipView.getChips()) + " " + qtyInCmDoubleField.getValue());
			log.setUserTankActualLevels(((TankUsageContext)identityOfTanksIntoWhichCleaningWaterJFXChipView.getUserData()).getUserTankActualLevels());
			log.setOrderInGroup("E");
			logToInsertList.add(log);
		}
		


		if (pozitionOfShipLatitudeCheckBox.isSelected() && setTimeAtStart.getLocalTime()!=null && setTimeEnd.getLocalTime()!=null) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("4.1");
			log.setContent("Lat: "+pozitionOfShipLatitudeField.getText()+" Lon: "+longitudeField.getText() + "\nStart: "+setTimeAtStart.getLocalTime().format(EasyDateUtils.logbookTimeFormatter) + " Stop: "+setTimeEnd.getLocalTime().format(EasyDateUtils.logbookTimeFormatter));
			log.setOrderInGroup("F");
			logToInsertList.add(log);
		}
		
		if (quantityOfBallastIfTanksAreNotCleanedCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("4.2");
			log.setContent(String.valueOf(quantityOfBallastIfTanksAreNotCleanedDoubleField.getValue()));
			log.setOrderInGroup("G");
			logToInsertList.add(log);
		}

	}






	private void validateFields(Date registrationDate, Map<Validation, Object> validations) {
		if (identityOfTanksBallastedCheckBox.isSelected() && tanksBallastedJFXChipView.getChips().isEmpty()) {
			validations.put(Validation.A1_EMPTY, null);
		}
		if (whetherCleanedCheckBox.isSelected() && checkBoxNotCleaned.isSelected() && textFieldTypeOfOilPreviouslycarried.getText().trim().isEmpty()) {
			validations.put(Validation.A2_EMPTY, null);
		}
		if (positionOfShipLatCheckBox.isSelected() && (setTimeAtThestart.getLocalTime()==null || setTimeEndOfCleaning.getLocalTime()==null)) {
			validations.put(Validation.A3_1_EMPTY, null);
		} else if(positionOfShipLatCheckBox.isSelected() && setTimeAtThestart.getLocalTime()!=null && setTimeEndOfCleaning.getLocalTime()!=null) {
//			if(registrationDate != null) {
//				LocalDateTime registrationLocalDateTime = Instant.ofEpochMilli(registrationDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime().plusDays(1);
//				if(setTimeAtThestart.getLocalTime().isAfter(registrationLocalDateTime) || setTimeEndOfCleaning.getLocalTime().isAfter(registrationLocalDateTime)) {
//					validations.put(Validation.A3_1_TIME_AFTER_REGISTRATION_DATE, null);
//				}
//			}
			if(setTimeAtThestart.getLocalTime().isAfter(setTimeEndOfCleaning.getLocalTime())) {
				validations.put(Validation.A3_1_START_AFTER_END, null);
			}
		}
		if (identifyTanksInwhichOneCheckBox.isSelected()) {
			if (identifyTanksJFXChipView.getChips().isEmpty()) {
				validations.put(Validation.A3_2_BALLAST_EMPTY, null);
			}
			if (qtyOfChemicalsDoubleField.getValue()==null) {
				validations.put(Validation.A3_2_CHEMICALS_EMPTY, null);
			}
			if (chemicalUsedTextField.getText().trim().isEmpty()) {
				validations.put(Validation.A3_2_CHEMICALS_NAME_EMPTY, null);
			}
//			if(!identifyTanksJFXChipView.getChips().isEmpty() && qtyOfChemicalsDoubleField.getValue()!=null) {
//				double sum = ((TankUsageContext) identifyTanksJFXChipView.getUserData()).getTotalUserTankCapacity();
//				if (qtyOfChemicalsDoubleField.getValue() > sum) {
//					validations.put(Validation.A3_2_CHEMICALS_SUM_ERROR, null);
//		        }
//			}
		}
		if (identityOfTanksInToWhichCleaningWaterWasTransferredCheckBox.isSelected()) {
			if (identityOfTanksInToWhichCleaningWaterWasTransferredCheckBox.isSelected() && identityOfTanksIntoWhichCleaningWaterJFXChipView.getChips().isEmpty()) {
				validations.put(Validation.A3_3_CLEANING_WATER_EMPTY, null);
			}
			if (identityOfTanksInToWhichCleaningWaterWasTransferredCheckBox.isSelected() && qtyInCmDoubleField.getValue()==null) {
				validations.put(Validation.A3_3_QUANTITY_EMPTY, null);
			}
			if (!identityOfTanksIntoWhichCleaningWaterJFXChipView.getChips().isEmpty() && qtyInCmDoubleField.getValue()!=null) {
				double totalCapacity = ((TankUsageContext) identityOfTanksIntoWhichCleaningWaterJFXChipView.getUserData()).getTotalUserTankCapacity();
				double totalActualLevel = ((TankUsageContext) identityOfTanksIntoWhichCleaningWaterJFXChipView.getUserData()).getTotalUserTankActualLevel();
				if (qtyInCmDoubleField.getValue() > ((totalCapacity*0.98)-totalActualLevel)) {
					validations.put(Validation.A3_3_WATER_SUM_0_98_WARNING, null);
		        }
			}
		}
		
		if (pozitionOfShipLatitudeCheckBox.isSelected() && (setTimeAtStart.getLocalTime()==null || setTimeEnd.getLocalTime()==null)) {
			validations.put(Validation.A4_1_EMPTY, null);
		}
		if(registrationDate != null && pozitionOfShipLatitudeCheckBox.isSelected() && setTimeAtStart.getLocalTime()!=null && setTimeEnd.getLocalTime()!=null) {
			if(setTimeAtStart.getLocalTime().isAfter(setTimeEnd.getLocalTime())) {
				validations.put(Validation.A4_1_START_TIME_AFTER_END_TIME, null);
			}
		}
		if (quantityOfBallastIfTanksAreNotCleanedCheckBox.isSelected() && quantityOfBallastIfTanksAreNotCleanedDoubleField.getValue()==null) {
			validations.put(Validation.A4_2_BALLAST_EMPTY, null);
		}

		
		
		if (!identityOfTanksBallastedCheckBox.isSelected()) {
			validations.put(Validation.A1_REQUIRED, null);
		}
		if (!whetherCleanedCheckBox.isSelected()) {
			validations.put(Validation.A2_REQUIRED, null);
		}
		if(checkBoxCleaned.isSelected()) {
			if(!positionOfShipLatCheckBox.isSelected()) {
				validations.put(Validation.A3_1_REQUIRED, null);
			}
//			if(!identifyTanksInwhichOneCheckBox.isSelected()) {
//				validations.put(Validation.A3_2_REQUIRED, null);
//			}
			if(!identityOfTanksInToWhichCleaningWaterWasTransferredCheckBox.isSelected()) {
				validations.put(Validation.A3_3_REQUIRED, null);
			}
			if(pozitionOfShipLatitudeCheckBox.isSelected()) {
				validations.put(Validation.A4_1_NOT_REQUIRED, null);
			}
			if(quantityOfBallastIfTanksAreNotCleanedCheckBox.isSelected()) {
				validations.put(Validation.A4_2_NOT_REQUIRED, null);
			}
		}
		if(checkBoxNotCleaned.isSelected()) {
			if(positionOfShipLatCheckBox.isSelected()) {
				validations.put(Validation.A3_1_NOT_REQUIRED, null);
			}
			if(identifyTanksInwhichOneCheckBox.isSelected()) {
				validations.put(Validation.A3_2_NOT_REQUIRED, null);
			}
			if(identityOfTanksInToWhichCleaningWaterWasTransferredCheckBox.isSelected()) {
				validations.put(Validation.A3_3_NOT_REQUIRED, null);
			}
			if(!pozitionOfShipLatitudeCheckBox.isSelected()) {
				validations.put(Validation.A4_1_REQUIRED, null);
			}
			if(!quantityOfBallastIfTanksAreNotCleanedCheckBox.isSelected()) {
				validations.put(Validation.A4_2_REQUIRED, null);
			}
		}
		if(positionOfShipLatCheckBox.isSelected()) {
			if(textFieldPozitionOfShipLat.getText().equals("00°00.00'N") || longTextField.getText().equals("000°00.00'W")) {
				validations.put(Validation.A3_1_LAT_LON_ZERO, null);
			}
		}
		if(pozitionOfShipLatitudeCheckBox.isSelected()) {
			if(pozitionOfShipLatitudeField.getText().equals("00°00.00'N") || longitudeField.getText().equals("000°00.00'W")) {
				validations.put(Validation.A4_1_LAT_LON_ZERO, null);
			}
		}
		
		if(identifyTanksInwhichOneCheckBox.isSelected()) {
			TankUsageContext tankUsageContext1 = (TankUsageContext) tanksBallastedJFXChipView.getUserData();
			TankUsageContext tankUsageContext3_2 = (TankUsageContext) identifyTanksJFXChipView.getUserData();
			
			Map<Tank, Double> tankQuantities1 = tankUsageContext1.getUserTankQuantities();
			Map<Tank, Double> tankQuantities3_2 = tankUsageContext3_2.getUserTankQuantities();
			List<Tank> tanksIn3_2NotIn_1 = tankQuantities3_2.keySet().stream().filter(t -> !tankQuantities1.keySet().contains(t)).collect(Collectors.toList());
			if(!tanksIn3_2NotIn_1.isEmpty()) {
				validations.put(Validation.A1_A3_2_NOT_SAME_TANK, tanksIn3_2NotIn_1.stream().map(Tank::getName).collect(Collectors.joining(",")));
			}
		}
		
		if (identityOfTanksInToWhichCleaningWaterWasTransferredCheckBox.isSelected()) {
			TankUsageContext tankUsageContext1 = (TankUsageContext) tanksBallastedJFXChipView.getUserData();
			TankUsageContext tankUsageContext3_3 = (TankUsageContext) identityOfTanksIntoWhichCleaningWaterJFXChipView.getUserData();
			
			Map<Tank, Double> tankQuantities1 = tankUsageContext1.getUserTankQuantities();
			Map<Tank, Double> tankQuantities3_3 = tankUsageContext3_3.getUserTankQuantities();
			List<Tank> intersect = tankQuantities1.keySet().stream().filter(tankQuantities3_3.keySet()::contains).collect(Collectors.toList());
			if(!intersect.isEmpty()) {
				validations.put(Validation.A1_A3_3_SAME_TANK, intersect.stream().map(Tank::getName).collect(Collectors.joining(",")));
			}
		}
	}


	public void restoreLogGroup(List<OilLog> logGroup) {
		if(logGroup==null || logGroup.isEmpty()) return;
		List<OilLog> codeAOilLogs = logGroup.stream().filter(c -> c.getCodeLetter().equals("A")).collect(Collectors.toList());
		for (OilLog oilLog : codeAOilLogs) {
			if(oilLog.getCorrectionNote()!=null && oilLog.getCorrectionNote().equals("Corrected")) continue;
			String value;
			switch (oilLog.getCodeNumber()) {
				case "1":
					value = oilLog.getContent().replace("CO Tank(s) Nr. ", "");
					identityOfTanksBallastedCheckBox.setSelected(true);
					tanksBallastedJFXChipView.getChips().addAll(Arrays.asList(value.split(", ")));
					break;
				case "2":
					whetherCleanedCheckBox.setSelected(true);
					if(oilLog.getContent().startsWith("Yes ")) {
						checkBoxCleaned.setSelected(true);
						textFieldTypeOfOilPreviouslycarried.setText(oilLog.getContent().replace("Yes ", ""));
					} else if(oilLog.getContent().startsWith("No ")) {
						checkBoxNotCleaned.setSelected(true);
						textFieldTypeOfOilPreviouslycarried.setText(oilLog.getContent().replace("No ", ""));
					};
					break;
				case "3.1":
					cleaningProcessCheckBox.setSelected(true);
					positionOfShipLatCheckBox.setSelected(true);
					value = oilLog.getContent().replace("Lat:", "");
					String lat = value.substring(0, value.indexOf(" Lon:"));
					value = value.replace(lat+" Lon:", "");
					String lon = value.substring(0, value.indexOf(" Time start:"));
					value = value.replace(lon+" Time start:", "");
					String startDate = value.substring(0, value.indexOf(" Time stop:"));
					value = value.replace(startDate+" Time stop:", "");
					String stopDate = value;
					textFieldPozitionOfShipLat.setText(lat);
					longTextField.setText(lon);
					setTimeAtThestart.setLocalTime(LocalTime.parse(startDate, EasyDateUtils.logbookTimeFormatter));
					setTimeEndOfCleaning.setLocalTime(LocalTime.parse(stopDate, EasyDateUtils.logbookTimeFormatter));
//					log.setContent("Lat:"+textFieldPozitionOfShipLat.getText()+" Lon:"+longTextField.getText() + " Time start:"+setTimeAtThestart.getLocalTime().toString() + " Time stop:"+setTimeEndOfCleaning.getLocalTime().toString());
					break;
				case "3.2":
					value = oilLog.getContent();
					String tanks = value.substring(0, value.indexOf(" Chemical Type <<"));
					value = value.replace(tanks+" Chemical Type <<", "");
					String chemicalName = value.substring(0, value.indexOf(">>;Q.ty "));
					value = value.replace(chemicalName+">>;Q.ty ", "");
					String quantity = value.substring(0, value.indexOf(" M3"));
					cleaningProcessCheckBox.setSelected(true);
					identifyTanksInwhichOneCheckBox.setSelected(true);
					identifyTanksJFXChipView.getChips().addAll(Arrays.asList(value.split(", ")));
					qtyOfChemicalsDoubleField.setValue(Double.parseDouble(quantity));
					chemicalUsedTextField.setText(chemicalName);
//					log.setContent(String.join(", ", identifyTanksJFXChipView.getChips()) + " Chemical Type <<" + chemicalUsedTextField.getText() + ">>;Q.ty " + qtyOfChemicalsDoubleField.getValue() + " M3");
					break;
				case "3.3":
					value = oilLog.getContent();
					String tanks2 = value.substring(0, value.lastIndexOf(" "));
					String quantity2 = value.substring(value.lastIndexOf(" "));
					cleaningProcessCheckBox.setSelected(true);
					identityOfTanksInToWhichCleaningWaterWasTransferredCheckBox.setSelected(true);
					identityOfTanksIntoWhichCleaningWaterJFXChipView.getChips().addAll(Arrays.asList(tanks2.split(", ")));
					qtyInCmDoubleField.setValue(Double.parseDouble(quantity2));
//					log.setContent(textFieldIdentityOfTanksIntoWhichCleaningWater.getText() + " " + textFieldQtyInCm.getText());
					break;
				case "4.1":
					ballastingCheckBox.setSelected(true);
					pozitionOfShipLatitudeCheckBox.setSelected(true);
					value = oilLog.getContent().replace("Lat:", "");
					String lat2 = value.substring(0, value.indexOf(" Lon:"));
					value = value.replace(lat2+" Lon:", "");
					String lon2 = value.substring(0, value.indexOf(" Time start:"));
					value = value.replace(lon2+" Time start:", "");
					String startDate2 = value.substring(0, value.indexOf(" Time stop:"));
					value = value.replace(startDate2+" Time stop:", "");
					String stopDate2 = value;
					pozitionOfShipLatitudeField.setText(lat2);
					longitudeField.setText(lon2);
					setTimeAtStart.setLocalTime(LocalTime.parse(startDate2, EasyDateUtils.logbookTimeFormatter));
					setTimeEnd.setLocalTime(LocalTime.parse(stopDate2, EasyDateUtils.logbookTimeFormatter));
//					log.setContent("Lat:"+pozitionOfShipLatitudeField.getText()+" Lon:"+longitudeField.getText() + " Time start:"+setTimeAtStart.getText() + " Time stop:"+setTimeEnd.getText());
					break;
				case "4.2":
					ballastingCheckBox.setSelected(true);
					quantityOfBallastIfTanksAreNotCleanedCheckBox.setSelected(true);
					quantityOfBallastIfTanksAreNotCleanedDoubleField.setValue(Double.parseDouble(oilLog.getContent()));
					break;
				default:
					break;
			}
		}
		

		
	}


	public void clearFilledInfo() {
		identityOfTanksBallastedCheckBox.setSelected(false);
		whetherCleanedCheckBox.setSelected(false);
		checkBoxCleaned.setSelected(false);
		checkBoxNotCleaned.setSelected(false);
		cleaningProcessCheckBox.setSelected(false);
		positionOfShipLatCheckBox.setSelected(false);
		identifyTanksInwhichOneCheckBox.setSelected(false);
		identityOfTanksInToWhichCleaningWaterWasTransferredCheckBox.setSelected(false);
		ballastingCheckBox.setSelected(false);
		pozitionOfShipLatitudeCheckBox.setSelected(false);
		quantityOfBallastIfTanksAreNotCleanedCheckBox.setSelected(false);
		
		tanksBallastedJFXChipView.getChips().clear();
		((TankUsageContext)tanksBallastedJFXChipView.getUserData()).getUserTankQuantities().clear();
		textFieldTypeOfOilPreviouslycarried.clear();
		textFieldPozitionOfShipLat.setText("00°00.00'N");
		longTextField.setText("000°00.00'W");
		setTimeAtThestart.setLocalTime(null);
		setTimeEndOfCleaning.setLocalTime(null);
		identifyTanksJFXChipView.getChips().clear();
		qtyOfChemicalsDoubleField.clear();
		identityOfTanksIntoWhichCleaningWaterJFXChipView.getChips().clear();
		qtyInCmDoubleField.clear();
		pozitionOfShipLatitudeField.setText("00°00.00'N");
		longitudeField.setText("000°00.00'W");
		setTimeAtStart.setLocalTime(null);
		setTimeEnd.setLocalTime(null);
		quantityOfBallastIfTanksAreNotCleanedDoubleField.clear();
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
