package al.sybina.ebr.logbook.views;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
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

import com.jfoenix.controls.JFXChipView;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.Tank;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.TankType;
import com.oceanpro.core.services.OperationLogReladomoService;
import com.oceanpro.core.services.OperationLogService;

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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import jfxtras.scene.control.ImageViewButton;
import jidefx.scene.control.field.DoubleField;

public class OilRecordBookPart1CodeLetterCController implements Initializable {

	private Label label1 = new Label("11. Collection of oil residues (sludge).\r\n" + "\r\n"
			+ "Quantities of oil residues (sludge) retained on board.\r\n"
			+ "The quantity must be recorded weekly 1^\r\n"
			+ "(This means that the quantity must be recorded once a \r\n"
			+ "week even if the voyage lasts more than one week).\r\n" + "");
	private Label label2 = new Label("12. Methods of transfer or disposal of oil residues \r\n"
			+ " (sludge). State quantity of the oil residues\r\n"
			+ "   transferred or disposed of, the tank(s) emptied and \r\n"
			+ "the quantity of contents retained in m3: \r\n" + "");
	private Label label3 = new Label("Notes:\r\n" + "The ship's Master should obtain from the operator of \r\n"
			+ "the reception facilities, which includes barges and tank \r\n"
			+ "trucks, a receipt or certificate detailing the quantity of \r\n" + "tank washings,\r\n"
			+ "dirty ballast, residues or Oily mixtures transferred  \r\n"
			+ "together with the time and date of the transfer. This \r\n"
			+ "receipt or certificate, if attached to the Oil Record Book \r\n"
			+ "(ORB) Part I, may aid the Master of the ship in proving \r\n"
			+ "that the ship was not involved in an alleged pollution \r\n"
			+ "incident. The receipt or certificate should be kept \r\n" + "together with the ORB Part I.\r\n" + "");
	private Label label4 = new Label("(Operator initiated manual collections where oil residue \r\n"
			+ "(sludge) is transferred (transfer with a pump) into the \r\n"
			+ "oil residue (sludge) holding tank(s).)\r\n" + "Entries C11.1/C11.2/C11.3/C11.4 are related to pump \r\n"
			+ "transfers (manually tripped by operator) from any non \r\n"
			+ "IOPP section 3.1 tank in engine room (C11.4) to any \r\n"
			+ "IOPP section 3.1 sludge tank (C11.1) BUT ONLY PUMP \r\n" + "TRANSFERS.\r\n" + ""
			+ "The volume in 11.4 (transfer to the sludge tank) can no exceed\r\n" 
			+ "that retained in the sludge tank 11.3\r\n" + 
					"");
	private Label label5 = new Label("Entries C12.2 are related to ANY internal transfer (pump  \r\n"
			+ "or drain) of sludge from any IOPP 3.1 sludge tank to any  \r\n"
			+ "IOPP section 3.1 sludge tank or ANY internal transfer  \r\n"
			+ "drain of water from any IOPP section 3.1 sludge tank to \r\n"
			+ "any IOPP section 3.3 bilge holding tank. \r\n"
			+ "\r\n"
			+ "Requires an entry in the Oil Record Book –Part II using code (J).\r\n"
			+ "If sludge or bilge water is transferred from multiple tanks in engine-room a separate\r\n"
			+ "entry must be made in ORB Parts I & II for each transfer.");
	
	private static final Logger logger = LoggerCreator.createLogger(OilRecordBookPart1CodeLetterCController.class);
	
	protected String CACHE_FILE_NAME = System.getProperty("user.home") + "/.ebr/caches/caches.properties";
	
	@Inject
	private BallonMessage ballonMessage;
	@Inject
	private IResourceProviderService resourcesService;
	@Inject
	private ImageProvider imageProvider;
	@Inject
	private SelectTankerDialog selectTankerDialog;
	@Inject
	private OperationLogService operationLogService;
	@Inject
	private OperationLogReladomoService operationLogReladomoService;
	@Inject
	private IEventBroker eventBroker;
	@Inject
	private HelpCodeLetterDialog helpCodeLetterDialog;
	
	// codeLetter C
	@FXML
	private ImageViewButton imageViewButtonCollectionOfOilInfo;
	@FXML
	private ImageViewButton imageViewButtonQuantityOfResidueInfo;
	@FXML
	private ImageViewButton imageViewButtonMethodsOfTransferInfo;
	@FXML
	private ImageViewButton imageViewButtonInfoLetterC;
	@FXML
	private ImageViewButton identityOfTanksLetterCImageViewButton;
	@FXML
	private ImageViewButton addIdentityOfTanksLetterC4ImageViewButton;
	@FXML
	private ImageViewButton indicateTanksImageViewButton;
	@FXML
	private ImageView imageViewButtonReceptionFacilitiesInfo;
	@FXML
	private ImageView imageViewbuttonTotalContentInfo;
	@FXML
	private CheckBox collectionOfOilResiduesCheckBox;
	@FXML
	private CheckBox identityOfTankCheckBox;
	@FXML
	private JFXChipView<String> identityOfTanksLetterCJFXChipView;
	@FXML
	private JFXChipView<String> identityOfTanksLetterC4JFXChipView;
	@FXML
	private CheckBox capacityOfTanksCheckBox;
	@FXML
	private DoubleField capacityOfTanksInM3DoubleField;
	@FXML
	private CheckBox totalQuantityOfRetentionCheckBox;
	@FXML
	private DoubleField totalQuantityOfRetentionInM3DoubleField;
	@FXML
	private CheckBox quantityOfResidueCollectedCheckBox;
	@FXML
	private DoubleField quantityOfResidueCollectedByManualOperationInM3DoubleField;
	@FXML
	private CheckBox methodsOfTransferOrDisposalCheckBox;
	@FXML
	private CheckBox toReceptionFacilitiesIdentifyPortCheckBox;
	@FXML
	private TextField toReceptionFacilitiesTextFiled;
	@FXML
	private CheckBox toAnotherOtherTanksCheckBox;
	@FXML
	private JFXChipView<String> toAnotherOtherTanksJFXChipView;
	@FXML
	private DoubleField totalContentOfTanksDoubleField;
	@FXML
	private CheckBox incineratedCheckBox;
	@FXML
	private TextField incineratedIndicateTotalTimeOfOperationTextField;
	@FXML
	private CheckBox otherMethodCheckBox;
	@FXML
	private TextField otherMethodStateWhichTextField;
	
	@FXML
	private GridPane cSludgeGridPane;
	@FXML
	private ImageViewButton quantityDischargedToTanksButton;
	@FXML
	private JFXChipView<String> tankNameFromTanksJFXChipView;
	@FXML
	private DoubleField tankRetainedSludgeTankDoubleField;
	@FXML
	private DoubleField tankQuantityFromSludgeDoubleField;
	@FXML
	private Button addSludgeTankButton;
	
	@FXML
	private ToggleGroup sludgeWaterToggleGroup;
	@FXML
	private Toggle sludgeToggle;
	@FXML
	private Toggle waterToggle;
	@FXML
	private TextField receiptTextField;
	@FXML
	private ImageViewButton uploadReceiptImageViewButton;
	@FXML
	public AnchorPane codeLetterContentAnchorPane;
	private ObservableList<Image> helpImageList = FXCollections.observableArrayList();
	private Image addImage;

	private StringTemplate sludgeTemplate = new StringTemplate("$tankQuantity$ M3 $sludgeOrWater$ FROM $tankName$ OF CAPACITY $tankCapacity$ M3, $tankRetained$ M3 RETAINED IN TANK");
	private Pattern sludgePattern = Pattern.compile("(.{1,}) M3 (.{1,}) FROM (.{1,}) OF CAPACITY (.{1,}) M3, (.{1,}) M3 RETAINED IN TANK");
	private String sludgeTransferredString = "SLUDGE TRANSFERRED";
	private String waterDrainedString = "WATER DRAINED";
	protected boolean disableListenersFlag = false;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			MathUtils.setStringConverter(capacityOfTanksInM3DoubleField);
			MathUtils.setStringConverter(totalQuantityOfRetentionInM3DoubleField);
			MathUtils.setStringConverter(quantityOfResidueCollectedByManualOperationInM3DoubleField);
			MathUtils.setStringConverter(totalContentOfTanksDoubleField);
			MathUtils.setStringConverter(tankRetainedSludgeTankDoubleField);
			MathUtils.setStringConverter(tankQuantityFromSludgeDoubleField);
			MathUtils.setMinMaxValues(tankRetainedSludgeTankDoubleField, 0.0, null);
			MathUtils.setMinMaxValues(tankQuantityFromSludgeDoubleField, 0.0, null);
			MathUtils.setMinMaxValues(totalContentOfTanksDoubleField, 0.0, null);
			MathUtils.setMinMaxValues(quantityOfResidueCollectedByManualOperationInM3DoubleField, 0.0, null);

			addImage = imageProvider
					.getImage(((WorkbenchResourceProvider) resourcesService)
							.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_ADD));
			
			Image helpImageLetterC1 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_HELPC1));
			
			Image helpImageLetterC2 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_HELPC2));
			
			Image helpImageLetterC3 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_HELPC3));
			
			Image helpImageLetterC5 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_HELPC5));
			
			Image helpImageLetterC4 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_HELPC4));
			
			helpImageList.addAll(helpImageLetterC1, helpImageLetterC2, helpImageLetterC3, helpImageLetterC4, helpImageLetterC5);

			Image infoImageMethodsOfTransferInfo = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
			imageViewButtonMethodsOfTransferInfo.setImage(infoImageMethodsOfTransferInfo);
			
			Image infoImageReceptionFacilitiesInfo = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
			imageViewButtonReceptionFacilitiesInfo.setImage(infoImageReceptionFacilitiesInfo);

			Image infoImageQuantityOfResidue = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
			imageViewButtonQuantityOfResidueInfo.setImage(infoImageQuantityOfResidue);

			Image infoImageTotalContentInfo = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
			imageViewbuttonTotalContentInfo.setImage(infoImageTotalContentInfo);
			
			Image infoImageLetterC = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
			imageViewButtonInfoLetterC.setImage(infoImageLetterC);
			
			
			Image infoImageCollectionOfOil = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
			imageViewButtonCollectionOfOilInfo.setImage(infoImageCollectionOfOil);

			identityOfTanksLetterCImageViewButton.setImage(addImage);
			addIdentityOfTanksLetterC4ImageViewButton.setImage(addImage);
			indicateTanksImageViewButton.setImage(addImage);
			quantityDischargedToTanksButton.setImage(addImage);
			
			
			
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
					receiptTextField.setText(selectedFile.getName());
					receiptTextField.setUserData(selectedFile);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		toReceptionFacilitiesTextFiled.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		incineratedIndicateTotalTimeOfOperationTextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		otherMethodStateWhichTextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		JavaFXUtils.addFilledPseudoClass(capacityOfTanksInM3DoubleField);
		JavaFXUtils.addFilledPseudoClass(totalQuantityOfRetentionInM3DoubleField);
		JavaFXUtils.addFilledPseudoClass(quantityOfResidueCollectedByManualOperationInM3DoubleField);
		JavaFXUtils.addFilledPseudoClass(toReceptionFacilitiesTextFiled);
		JavaFXUtils.addFilledPseudoClass(incineratedIndicateTotalTimeOfOperationTextField);
		JavaFXUtils.addFilledPseudoClass(otherMethodStateWhichTextField);
		JavaFXUtils.addFilledPseudoClass(totalContentOfTanksDoubleField);
		JavaFXUtils.addFilledPseudoClass(tankQuantityFromSludgeDoubleField);
		JavaFXUtils.addFilledPseudoClass(tankRetainedSludgeTankDoubleField);
		JavaFXUtils.addFilledPseudoClass(receiptTextField);
		
		collectionOfOilResiduesCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					identityOfTankCheckBox.setDisable(false);
					capacityOfTanksCheckBox.setDisable(false);
					totalQuantityOfRetentionCheckBox.setDisable(false);
					//quantityOfResidueCollectedCheckBox.setDisable(false);
					identityOfTankCheckBox.setSelected(true);
					capacityOfTanksCheckBox.setSelected(true);
					totalQuantityOfRetentionCheckBox.setSelected(true);
				} else {
					identityOfTankCheckBox.setDisable(true);
					capacityOfTanksCheckBox.setDisable(true);
					totalQuantityOfRetentionCheckBox.setDisable(true);
					quantityOfResidueCollectedCheckBox.setDisable(true);
					identityOfTankCheckBox.setSelected(false);
					capacityOfTanksCheckBox.setSelected(false);
					totalQuantityOfRetentionCheckBox.setSelected(false);
					quantityOfResidueCollectedCheckBox.setSelected(false);
				}
			}
		});
		TankUsageContext tuc = new TankUsageContext(Stream.of(TankType.SLUDGE).collect(Collectors.toList()), false, true);
		tuc.setC11_1(true);
		identityOfTanksLetterCJFXChipView.setUserData(tuc);
		identityOfTankCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {	
				if(newValue) {
					identityOfTanksLetterCJFXChipView.setDisable(false);
					identityOfTanksLetterCImageViewButton.setDisable(false);
				} else {
					identityOfTanksLetterCJFXChipView.setDisable(true);
					identityOfTanksLetterCImageViewButton.setDisable(true);
				}
				identityOfTanksLetterCJFXChipView.getChips().clear();
			}

		});
		capacityOfTanksCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (identityOfTanksLetterCJFXChipView.getUserData()!=null && newValue) {
					double totalCapacity = ((TankUsageContext) identityOfTanksLetterCJFXChipView.getUserData()).getTotalUserTankCapacity();
					capacityOfTanksInM3DoubleField.setValue(totalCapacity);
				}else {
					capacityOfTanksInM3DoubleField.setValue(null);	
				}
			}
		});
		totalQuantityOfRetentionCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (identityOfTanksLetterCJFXChipView.getUserData()!=null && newValue) {
					double actualLevelSum = ((TankUsageContext) identityOfTanksLetterCJFXChipView.getUserData()).getTotalUserTankQuantities();
					totalQuantityOfRetentionInM3DoubleField.setValue(actualLevelSum);	
				}else {
					totalQuantityOfRetentionInM3DoubleField.setValue(null);	
				}
				
			}
		});

		TankUsageContext tuc3 = new TankUsageContext(Stream.of(TankType.SLUDGE).collect(Collectors.toList()), false, true);
		tuc3.setC11_4(true);
		TankUsageContext tankUsageContext11_1 = (TankUsageContext) identityOfTanksLetterCJFXChipView.getUserData();
		double sum11_1 = tankUsageContext11_1.getTotalUserTankQuantities()-tankUsageContext11_1.getTotalUserTankActualLevel();
		tuc3.setMaxC11_4(sum11_1);
		//tuc3.setMaxC11_4(totalQuantityOfRetentionInM3DoubleField.getValue());
		identityOfTanksLetterC4JFXChipView.setUserData(tuc3);
		addRemoveListener(identityOfTanksLetterC4JFXChipView);
		identityOfTanksLetterC4JFXChipView.getChips().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> c) {
				Double userTankQuantitiesSum = ((TankUsageContext) identityOfTanksLetterC4JFXChipView.getUserData()).getTotalUserTankQuantities();
				quantityOfResidueCollectedByManualOperationInM3DoubleField.setValue(userTankQuantitiesSum);
				JavaFXUtils.addTooltip(identityOfTanksLetterC4JFXChipView);
			}
		});
		
		
		
		quantityOfResidueCollectedCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
//					quantityOfResidueCollectedByManualOperationInM3DoubleField.setDisable(false);
					identityOfTanksLetterC4JFXChipView.setDisable(false);
					addIdentityOfTanksLetterC4ImageViewButton.setDisable(false);
				} else {
//					quantityOfResidueCollectedByManualOperationInM3DoubleField.setDisable(true);
					identityOfTanksLetterC4JFXChipView.setDisable(true);
					addIdentityOfTanksLetterC4ImageViewButton.setDisable(true);
				}
				identityOfTanksLetterC4JFXChipView.getChips().clear();
				quantityOfResidueCollectedByManualOperationInM3DoubleField.setValue(null);
			}
		});
		methodsOfTransferOrDisposalCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					toReceptionFacilitiesIdentifyPortCheckBox.setDisable(false);
					toAnotherOtherTanksCheckBox.setDisable(false);
					incineratedCheckBox.setDisable(false);
					otherMethodCheckBox.setDisable(false);
					quantityDischargedToTanksButton.setDisable(false);
					tankNameFromTanksJFXChipView.setDisable(false);
					addSludgeTankButton.setDisable(false);
					tankQuantityFromSludgeDoubleField.setDisable(false);
					tankRetainedSludgeTankDoubleField.setDisable(false);
					sludgeWaterToggleGroup.getToggles().forEach(toggle -> ((RadioButton)toggle).setDisable(false));
				} else {
					toReceptionFacilitiesIdentifyPortCheckBox.setDisable(true);
					toAnotherOtherTanksCheckBox.setDisable(true);
					incineratedCheckBox.setDisable(true);
					otherMethodCheckBox.setDisable(true);
					cSludgeGridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node)!=null && GridPane.getRowIndex(node) > 0);
					cSludgeGridPane.getRowConstraints().removeIf(rowConstraint -> rowConstraint != cSludgeGridPane.getRowConstraints().get(0));
					quantityDischargedToTanksButton.setDisable(true);
					addSludgeTankButton.setDisable(true);
					tankNameFromTanksJFXChipView.setDisable(true);
					tankNameFromTanksJFXChipView.getChips().clear();
					((TankUsageContext)tankNameFromTanksJFXChipView.getUserData()).getUserTankQuantities().clear();
					tankQuantityFromSludgeDoubleField.setValue(null);
					tankQuantityFromSludgeDoubleField.setDisable(true);
			        Platform.runLater(() -> {
			        	tankRetainedSludgeTankDoubleField.clear();
			        });
					tankRetainedSludgeTankDoubleField.setDisable(true);
					sludgeWaterToggleGroup.getToggles().forEach(toggle -> {
						toggle.setSelected(false);
						((RadioButton)toggle).setDisable(true);
					});
				}
				disableListenersFlag = true;
				toReceptionFacilitiesIdentifyPortCheckBox.setSelected(false);
				toAnotherOtherTanksCheckBox.setSelected(false);
				incineratedCheckBox.setSelected(false);
				otherMethodCheckBox.setSelected(false);
				disableListenersFlag = false;
			}

		});
		toReceptionFacilitiesIdentifyPortCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					toReceptionFacilitiesTextFiled.setDisable(false);
					receiptTextField.setDisable(false);
					uploadReceiptImageViewButton.setDisable(false);
				} else {
					toReceptionFacilitiesTextFiled.setText("");
					toReceptionFacilitiesTextFiled.setDisable(true);
					receiptTextField.clear();
					receiptTextField.setDisable(true);
					uploadReceiptImageViewButton.setDisable(true);
				}
			}

		});
		
		toReceptionFacilitiesIdentifyPortCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					toAnotherOtherTanksCheckBox.setDisable(true);
					incineratedCheckBox.setDisable(true);
					otherMethodCheckBox.setDisable(true);
					
					toAnotherOtherTanksCheckBox.setSelected(false);
					incineratedCheckBox.setSelected(false);
					otherMethodCheckBox.setSelected(false);
					
				} else {
					if(!disableListenersFlag) {
						toAnotherOtherTanksCheckBox.setDisable(false);
						incineratedCheckBox.setDisable(false);
						otherMethodCheckBox.setDisable(false);
					}
				}
			}

		});
		
		
		toAnotherOtherTanksCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					toAnotherOtherTanksJFXChipView.setDisable(false);
					totalContentOfTanksDoubleField.setDisable(false);
					indicateTanksImageViewButton.setDisable(false);
				} else {
					toAnotherOtherTanksJFXChipView.setDisable(true);
					totalContentOfTanksDoubleField.setDisable(true);
					indicateTanksImageViewButton.setDisable(true);
				}
				toAnotherOtherTanksJFXChipView.getChips().clear();
				totalContentOfTanksDoubleField.setValue(null);
			}

		});
		
		toAnotherOtherTanksCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					toReceptionFacilitiesIdentifyPortCheckBox.setDisable(true);
					incineratedCheckBox.setDisable(true);
					otherMethodCheckBox.setDisable(true);
					
					toReceptionFacilitiesIdentifyPortCheckBox.setSelected(false);
					incineratedCheckBox.setSelected(false);
					otherMethodCheckBox.setSelected(false);
					
				} else {
					if(!disableListenersFlag) {
						toReceptionFacilitiesIdentifyPortCheckBox.setDisable(false);
						incineratedCheckBox.setDisable(false);
						otherMethodCheckBox.setDisable(false);
					}
				}
			}

		});
		
		
		
		incineratedCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					incineratedIndicateTotalTimeOfOperationTextField.setDisable(false);
				} else {
					incineratedIndicateTotalTimeOfOperationTextField.setDisable(true);
				}
				incineratedIndicateTotalTimeOfOperationTextField.setText("");
			}

		});
		
		incineratedCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					toReceptionFacilitiesIdentifyPortCheckBox.setDisable(true);
					toAnotherOtherTanksCheckBox.setDisable(true);
					otherMethodCheckBox.setDisable(true);
					
					toReceptionFacilitiesIdentifyPortCheckBox.setSelected(false);
					toAnotherOtherTanksCheckBox.setSelected(false);
					otherMethodCheckBox.setSelected(false);
					
				} else {
					if(!disableListenersFlag ) {
						toReceptionFacilitiesIdentifyPortCheckBox.setDisable(false);
						toAnotherOtherTanksCheckBox.setDisable(false);
						otherMethodCheckBox.setDisable(false);
					}
				}
			}

		});
		
		otherMethodCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					otherMethodStateWhichTextField.setDisable(false);
				} else {
					otherMethodStateWhichTextField.setDisable(true);
				}
				otherMethodStateWhichTextField.setText("");
			}
		});
		
		otherMethodCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					toReceptionFacilitiesIdentifyPortCheckBox.setDisable(true);
					toAnotherOtherTanksCheckBox.setDisable(true);
					incineratedCheckBox.setDisable(true);
					
					toReceptionFacilitiesIdentifyPortCheckBox.setSelected(false);
					toAnotherOtherTanksCheckBox.setSelected(false);
					incineratedCheckBox.setSelected(false);
					
				} else {
					if(!disableListenersFlag ) {
						toReceptionFacilitiesIdentifyPortCheckBox.setDisable(false);
						toAnotherOtherTanksCheckBox.setDisable(false);
						incineratedCheckBox.setDisable(false);
					}
				}
			}

		});
		
		totalContentOfTanksDoubleField.valueProperty().addListener(new ChangeListener<Double>() {
			@Override
			public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
				Double totalUserTankCapacity = ((TankUsageContext)toAnotherOtherTanksJFXChipView.getUserData()).getTotalUserTankCapacity();
				if(newValue!=null && newValue > totalUserTankCapacity) {
					OilRecordBookPart1ViewController.showWarning("Invalid value:" + newValue, "Valid value range:0.0-" + totalUserTankCapacity);
					totalContentOfTanksDoubleField.setValue(totalUserTankCapacity);
				} else {
					if(newValue!=null && newValue > totalUserTankCapacity*0.98) {
						OilRecordBookPart1ViewController.showWarning("Value:" + newValue, "Is greater than 98% of max value (" + totalUserTankCapacity +")!");
					}
				}
			}
		});
		
//		totalQuantityOfRetentionInM3DoubleField.valueProperty().addListener(new ChangeListener<Double>() {
//			@Override
//			public void changed(ObservableValue observable, Double oldValue, Double newValue) {
//				ballonMessage.closeBalloon();
//				Double lastEntry = 0.0;
//				OilLog oilLog1 = operationLogReladomoService.getLastOil1CodeC11_3Log();
//				OilLog oilLog2 = operationLogReladomoService.getLastOil1CodeC11_4Log();
//				if(oilLog1!=null) {
//					String quantityStr = oilLog1.getContent().substring(0, oilLog1.getContent().lastIndexOf(" M3"));
//					lastEntry =+ Double.parseDouble(quantityStr);
//				}
//				if(oilLog2!=null) {
//					String quantityStr = oilLog1.getContent().substring(0, oilLog2.getContent().lastIndexOf(" M3"));
//					lastEntry =+ Double.parseDouble(quantityStr);
//				}
//				String message = "Last entry\t\t\t\t\t= " + lastEntry + " M3\n" +
//								"Actual entry\t\t\t\t= " + (totalQuantityOfRetentionInM3DoubleField.getValue()!=null?totalQuantityOfRetentionInM3DoubleField.getValue():0.0) + " M3\n" +
//								"Total quantity of retention\t= " + (lastEntry + (totalQuantityOfRetentionInM3DoubleField.getValue()!=null?totalQuantityOfRetentionInM3DoubleField.getValue():0.0)) + " M3";
//				if(lastEntry>0)
//					ballonMessage.showGeneralBalloon(totalQuantityOfRetentionInM3DoubleField, new Label(message));
//			}
//			
//		});
		addRemoveListener(identityOfTanksLetterCJFXChipView);
		identityOfTanksLetterCJFXChipView.getChips().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> c) {
		        while (c.next()) {
		            for (String item : c.getAddedSubList()) {
		            	if(identityOfTanksLetterCJFXChipView.getChips().size()>1) {
		            		String oldChip = identityOfTanksLetterCJFXChipView.getChips().get(0);
		            		Platform.runLater(() -> identityOfTanksLetterCJFXChipView.getChips().remove(oldChip));
		            	}
		            }
		        }
		        
				double totalCapacity = ((TankUsageContext) identityOfTanksLetterCJFXChipView.getUserData()).getTotalUserTankCapacity();
				double actualLevelSum = ((TankUsageContext) identityOfTanksLetterCJFXChipView.getUserData()).getTotalUserTankQuantities();
					
				if(actualLevelSum>0.0){
					quantityOfResidueCollectedCheckBox.setDisable(false);
					((TankUsageContext)identityOfTanksLetterC4JFXChipView.getUserData()).setMaxC11_4(actualLevelSum);
				}else {
					quantityOfResidueCollectedCheckBox.setDisable(true);
					quantityOfResidueCollectedCheckBox.setSelected(false);
					((TankUsageContext)identityOfTanksLetterC4JFXChipView.getUserData()).setMaxC11_4(0.0);
				}
					
				if(!identityOfTanksLetterCJFXChipView.isDisable()) {
					capacityOfTanksCheckBox.setSelected(true);
					capacityOfTanksInM3DoubleField.setValue(totalCapacity);
					totalQuantityOfRetentionCheckBox.setSelected(true);
					totalQuantityOfRetentionInM3DoubleField.setValue(actualLevelSum);
				}
				
				JavaFXUtils.addTooltip(identityOfTanksLetterCJFXChipView);
			}
		});
		
		addRemoveListener(toAnotherOtherTanksJFXChipView);
		toAnotherOtherTanksJFXChipView.setUserData(new TankUsageContext(Stream.of(TankType.SLUDGE).collect(Collectors.toList()), true, true));
		toAnotherOtherTanksJFXChipView.getChips().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> c) {
		        while (c.next()) {
		            for (String item : c.getAddedSubList()) {
		            	if(toAnotherOtherTanksJFXChipView.getChips().size()>1) {
		            		String oldChip = toAnotherOtherTanksJFXChipView.getChips().get(0);
		            		Platform.runLater(() -> toAnotherOtherTanksJFXChipView.getChips().remove(oldChip));
		            	}
		            }
		        }
		        
				Platform.runLater(() -> {
					double sumAdded = ((TankUsageContext) toAnotherOtherTanksJFXChipView.getUserData()).getTotalUserTankQuantities();
					double sumActual = ((TankUsageContext) toAnotherOtherTanksJFXChipView.getUserData()).getTotalUserTankActualLevel();
					if(!totalContentOfTanksDoubleField.isDisabled())	totalContentOfTanksDoubleField.setValue(sumAdded+sumActual);
				});
				JavaFXUtils.addTooltip(toAnotherOtherTanksJFXChipView);
			}
		});
		
		
		
		
		quantityDischargedToTanksButton.setOnMouseEntered(mouseEvent -> {if(!mouseEvent.isPrimaryButtonDown()) quantityDischargedToTanksButton.setCursor(Cursor.HAND);});
		quantityDischargedToTanksButton.setOnMouseExited(mouseEvent -> {if(!mouseEvent.isPrimaryButtonDown()) quantityDischargedToTanksButton.setCursor(Cursor.DEFAULT);});
		
		addSludgeTankButton.setOnMouseEntered(mouseEvent -> {if(!mouseEvent.isPrimaryButtonDown()) addSludgeTankButton.setCursor(Cursor.HAND);});
		addSludgeTankButton.setOnMouseExited(mouseEvent -> {if(!mouseEvent.isPrimaryButtonDown()) addSludgeTankButton.setCursor(Cursor.DEFAULT);});

		TankUsageContext tuc2 = new TankUsageContext(Stream.of(TankType.SLUDGE).collect(Collectors.toList()), false, true);
		tuc2.setNoNeedForUserTankQuantities(true);
		tankNameFromTanksJFXChipView.setUserData(tuc2);
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
					tankRetainedSludgeTankDoubleField.setValue(totalUserTankQuantities - Optional.ofNullable(tankQuantityFromSludgeDoubleField.getValue()).orElse(0.0));
		        });
		        JavaFXUtils.addTooltip(tankNameFromTanksJFXChipView);
			}
		});
		tankQuantityFromSludgeDoubleField.valueProperty().addListener(new ChangeListener<Double>() {
			@Override
			public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
				Double totalUserTankQuantities = ((TankUsageContext)tankNameFromTanksJFXChipView.getUserData()).getTotalUserTankActualLevel();
				if(totalUserTankQuantities - Optional.ofNullable(newValue).orElse(0.0) < 0) {
					OilRecordBookPart1ViewController.showWarning("Invalid value:" + newValue, "Valid value range:0.0-" + totalUserTankQuantities);
					tankQuantityFromSludgeDoubleField.setValue(totalUserTankQuantities);
				} else {
					tankRetainedSludgeTankDoubleField.setValue(totalUserTankQuantities - Optional.ofNullable(newValue).orElse(0.0));
				}
			}
		});
		addRemoveListener(tankNameFromTanksJFXChipView);
		
		GridPane.setRowIndex(tankNameFromTanksJFXChipView, 0);
		GridPane.setRowIndex(tankQuantityFromSludgeDoubleField, 0);
		GridPane.setRowIndex(tankRetainedSludgeTankDoubleField, 0);
		
		eventBroker.subscribe(Activator.MARPOL_DETAILS_RELOADED, new org.osgi.service.event.EventHandler() {
			@Override
			public void handleEvent(org.osgi.service.event.Event event) {
				ArrayList<Long> changedTankIds = (ArrayList<Long>) event.getProperty(Activator.TANK_IDS);
    			if(changedTankIds==null) {
    				identityOfTanksLetterCJFXChipView.getChips().clear();
    				identityOfTanksLetterC4JFXChipView.getChips().clear();
    				toAnotherOtherTanksJFXChipView.getChips().clear();
    				tankNameFromTanksJFXChipView.getChips().clear();
    			} else if(changedTankIds!=null && !changedTankIds.isEmpty()) {
    				Activator.removeChangedTanksFromJFXChipView(changedTankIds, identityOfTanksLetterCJFXChipView);
    				Activator.removeChangedTanksFromJFXChipView(changedTankIds, identityOfTanksLetterC4JFXChipView);
    				Activator.removeChangedTanksFromJFXChipView(changedTankIds, toAnotherOtherTanksJFXChipView);
    				Activator.removeChangedTanksFromJFXChipView(changedTankIds, tankNameFromTanksJFXChipView);
    			}
			}
		});
	}

	public void processC(OilLog logToInsert, List<OilLog> logToInsertList, Map<Validation, Object> validations) {
		validateFields(validations);
		
		if (identityOfTankCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("11.1");
			log.setContent(String.join(", ", identityOfTanksLetterCJFXChipView.getChips()));
			log.setUserTankActualLevels(((TankUsageContext)identityOfTanksLetterCJFXChipView.getUserData()).getUserTankQuantities());
			log.setOrderInGroup("O");
			logToInsertList.add(log);
		}
		if (capacityOfTanksCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("11.2");
			log.setContent(capacityOfTanksInM3DoubleField.getValue() + " M3");
			log.setOrderInGroup("P");
			logToInsertList.add(log);
		}
		if (totalQuantityOfRetentionCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("11.3");
			log.setContent(totalQuantityOfRetentionInM3DoubleField.getValue() + " M3");
			log.setOrderInGroup("Q");
			logToInsertList.add(log);
		}
		if (quantityOfResidueCollectedCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("11.4");
			log.setContent(quantityOfResidueCollectedByManualOperationInM3DoubleField.getValue() + " M3 COLLECTED FROM " + String.join(", ", identityOfTanksLetterC4JFXChipView.getChips()));
			log.setUserTankActualLevels(((TankUsageContext)identityOfTanksLetterC4JFXChipView.getUserData()).getUserTankActualLevels());
			log.setOrderInGroup("R");
			logToInsertList.add(log);
		}
		if (toReceptionFacilitiesIdentifyPortCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("12.1");
			Map<Tank, Double> userTankActualLevels = new HashMap<Tank, Double>();
			StringBuilder sb = new StringBuilder();
			for(Pair<String, Tank> tank : getTanks(cSludgeGridPane, sludgeToggle.isSelected()?sludgeTransferredString : waterDrainedString )) {
				try {
					userTankActualLevels.put(tank.getValue(), MathUtils.decimalFormat.parse(parseTank(tank.getKey()).get("tankRetained")).doubleValue());
				} catch (ParseException e) {
					logger.debug(e.getMessage(), e);
				}
				if(sb.length()>0) sb.append("\n");
				sb.append(tank.getKey());
			}
			if(sb.length()>0) sb.append("\n");
			sb.append("TO " + toReceptionFacilitiesTextFiled.getText() + " - RECEIPT FILE '" + receiptTextField.getText() + "'");
			log.setContent(sb.toString());
			if(receiptTextField.getUserData()!=null && ((File)receiptTextField.getUserData()).exists()) {
				log.setAttachmentName(((File)receiptTextField.getUserData()).getName());
				byte[] attachmentBytes = null;
				try {
					attachmentBytes = Files.readAllBytes(((File)receiptTextField.getUserData()).toPath());
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
				log.setAttachmentData(attachmentBytes);
			}
			log.setUserTankActualLevels(userTankActualLevels);
			log.setOrderInGroup("S");
			logToInsertList.add(log);
		}
		if (toAnotherOtherTanksCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("12.2");
			Map<Tank, Double> userTankActualLevels = new HashMap<Tank, Double>();
			StringBuilder sb = new StringBuilder();
			for(Pair<String, Tank> tank : getTanks(cSludgeGridPane, sludgeToggle.isSelected()?sludgeTransferredString : waterDrainedString)) {
//				try {
//					userTankActualLevels.put(tank.getValue(), MathUtils.decimalFormat.parse(parseTank(tank.getKey()).get("tankRetained")).doubleValue());
//				} catch (ParseException e) {
//					logger.debug(e.getMessage(), e);
//				}
				userTankActualLevels.put(tank.getValue(), totalContentOfTanksDoubleField.getValue().doubleValue());
				if(sb.length()>0) sb.append("\n");
				sb.append(tank.getKey());
			}
			if(sb.length()>0) sb.append("\n");
			sb.append("TO " + String.join(", ", toAnotherOtherTanksJFXChipView.getChips()) + " " +totalContentOfTanksDoubleField.getValue() + " M3 RETAINED IN THE TANK");
			log.setContent(sb.toString());
			userTankActualLevels.putAll(((TankUsageContext)toAnotherOtherTanksJFXChipView.getUserData()).getUserTankActualLevels());
			log.setUserTankActualLevels(userTankActualLevels);
			log.setOrderInGroup("T");
			logToInsertList.add(log);
		}
		if (incineratedCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("12.3");
			Map<Tank, Double> userTankActualLevels = new HashMap<Tank, Double>();
			StringBuilder sb = new StringBuilder();
			for(Pair<String, Tank> tank : getTanks(cSludgeGridPane, sludgeToggle.isSelected()?sludgeTransferredString : waterDrainedString)) {
				try {
					userTankActualLevels.put(tank.getValue(), MathUtils.decimalFormat.parse(parseTank(tank.getKey()).get("tankRetained")).doubleValue());
				} catch (ParseException e) {
					logger.debug(e.getMessage(), e);
				}
				if(sb.length()>0) sb.append("\n");
				sb.append(tank.getKey());
			}
			if(sb.length()>0) sb.append("\n");
			sb.append("BURNED IN INCENERATOR FOR "+incineratedIndicateTotalTimeOfOperationTextField.getText() + " HOURS");
			log.setContent(sb.toString());
			log.setUserTankActualLevels(userTankActualLevels);
			log.setOrderInGroup("U");
			logToInsertList.add(log);
		}
		if (otherMethodCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("12.4");
			Map<Tank, Double> userTankActualLevels = new HashMap<Tank, Double>();
			StringBuilder sb = new StringBuilder();
			for(Pair<String, Tank> tank : getTanks(cSludgeGridPane, sludgeToggle.isSelected()?sludgeTransferredString : waterDrainedString + ", " + otherMethodStateWhichTextField.getText())) {
				try {
					userTankActualLevels.put(tank.getValue(), MathUtils.decimalFormat.parse(parseTank(tank.getKey()).get("tankRetained")).doubleValue());
				} catch (ParseException e) {
					logger.debug(e.getMessage(), e);
				}
				if(sb.length()>0) sb.append("\n");
				sb.append(tank.getKey());
			}
			if(sludgeToggle.isSelected()) {
				if(sb.length()>0) sb.append("\n");
				sb.append(otherMethodStateWhichTextField.getText());
			}
			log.setContent(sb.toString());
			log.setUserTankActualLevels(userTankActualLevels);
			log.setOrderInGroup("V");
			logToInsertList.add(log);
		}
	}

	public void openIdentifyTanksLetterC(MouseEvent e) {
		selectTankerDialog.showDialog((Stage) ((ImageViewButton) e.getSource()).getScene().getWindow(), identityOfTanksLetterCJFXChipView);
	}
	public void openIdentifyTanksLetterC4(MouseEvent e) {
		selectTankerDialog.showDialog((Stage) ((ImageViewButton) e.getSource()).getScene().getWindow(), identityOfTanksLetterC4JFXChipView);
	}
	public void openIdentifyTanksIndicateTanks(MouseEvent e) {
		selectTankerDialog.showDialog((Stage) ((ImageViewButton) e.getSource()).getScene().getWindow(), toAnotherOtherTanksJFXChipView);
	}
	
           
     
	
	public void showBalloon(MouseEvent event) {
		ballonMessage.showGeneralBalloon((ImageViewButton) event.getTarget(), label1);
	}

	public void showBalloon1(MouseEvent event) {
		ballonMessage.showGeneralBalloon((ImageViewButton) event.getTarget(), label2);
	}

	public void showBalloon2(MouseEvent event) {
		ballonMessage.showGeneralBalloon((ImageViewButton) event.getTarget(), label3);
	}

	public void showBalloon3(MouseEvent event) {
		ballonMessage.showGeneralBalloon((ImageViewButton) event.getTarget(), label4);
	}

	public void showBalloon4(MouseEvent event) {
		ballonMessage.showGeneralBalloon((ImageViewButton) event.getTarget(), label5);
	}



	public void actionPerformedC(MouseEvent event) {
		helpCodeLetterDialog.showDialog((Stage)((ImageViewButton)event.getSource()).getScene().getWindow(),helpImageList);
	}

	public void restoreLogGroup(List<OilLog> logGroup) {
		if(logGroup==null || logGroup.isEmpty()) return;
		List<OilLog> codeBOilLogs = logGroup.stream().filter(c -> c.getCodeLetter().equals("C")).collect(Collectors.toList());
		for (OilLog oilLog : codeBOilLogs) {
			if(oilLog.getCorrectionNote()!=null && oilLog.getCorrectionNote().equals("Corrected")) continue;
			String value;
			switch (oilLog.getCodeNumber()) {
				case "11.1":
					collectionOfOilResiduesCheckBox.setSelected(true);
					identityOfTankCheckBox.setSelected(true);
					value = oilLog.getContent();
					List<String> tankNameList = Arrays.asList(value.split(", "));
					Map<Tank, Double> userTanks = new HashMap<Tank, Double>();
					for(String tankName : tankNameList) {
						userTanks.put(getTankFromSettingsByName(tankName), null);
					}
					TankUsageContext tuc = new TankUsageContext();
					if(identityOfTanksLetterCJFXChipView.getUserData()==null) {
						identityOfTanksLetterCJFXChipView.setUserData(tuc);
					} else {
						tuc = (TankUsageContext) identityOfTanksLetterCJFXChipView.getUserData();
					}
					tuc.getUserTankQuantities().putAll(userTanks);
					identityOfTanksLetterCJFXChipView.getChips().addAll(tankNameList);
					
//					log.setContent(identityOfTanksLetterCTextField.getText());
					break;
				case "11.2":
					collectionOfOilResiduesCheckBox.setSelected(true);
					capacityOfTanksCheckBox.setSelected(true);
					value = oilLog.getContent();
					value = value.substring(0, value.lastIndexOf(" M3"));
					capacityOfTanksInM3DoubleField.setValue(Double.parseDouble(value));
//					log.setContent(capacityOfTanksInM3TextField.getText() + " M3");
					break;
				case "11.3":
					collectionOfOilResiduesCheckBox.setSelected(true);
					totalQuantityOfRetentionCheckBox.setSelected(true);
					value = oilLog.getContent();
					value = value.substring(0, value.lastIndexOf(" M3"));
					totalQuantityOfRetentionInM3DoubleField.setValue(Double.parseDouble(value));
//					log.setContent(totalQuantityOfRetentionInM3TextField.getText() + " M3");
					break;
				case "11.4":
					collectionOfOilResiduesCheckBox.setSelected(true);
					quantityOfResidueCollectedCheckBox.setSelected(true);
					value = oilLog.getContent();
					String quantity11_4 = value.substring(0, value.indexOf(" M3 collected from "));
					String tanks11_4 = value.replace(quantity11_4+" M3 collected from ", "");
					quantityOfResidueCollectedByManualOperationInM3DoubleField.setValue(Double.parseDouble(quantity11_4));
					identityOfTanksLetterC4JFXChipView.getChips().addAll(Arrays.asList(tanks11_4.split(", ")));
//					log.setContent(quantityOfResidueCollectedByManualOperationInM3DoubleField.getValue() + " M3 collected from " + String.join(", ", identityOfTanksLetterC4JFXChipView.getChips()));
					break;
				case "12.1":
					methodsOfTransferOrDisposalCheckBox.setSelected(true);
					toReceptionFacilitiesIdentifyPortCheckBox.setSelected(true);
					value = oilLog.getContent();
					String facilityName = "";
					if(value.indexOf("\n") == -1) {
						facilityName = value;
					} else {
						String tanksString = "";
						tanksString = value.substring(0, value.lastIndexOf("\n"));
						value = value.replace(tanksString+"\n", "");
						facilityName = value;
						String [] tanks = value.split("\n");
						restoreTanks(tanks);
					}
					toReceptionFacilitiesTextFiled.setText(facilityName);
//					log.setContent(toReceptionFacilitiesTextFiled.getText());
					break;
				case "12.2":
					methodsOfTransferOrDisposalCheckBox.setSelected(true);
					toAnotherOtherTanksCheckBox.setSelected(true);
					String otherTanksString = "";
					String quantity;
					value = oilLog.getContent();
					if(value.indexOf("\n") == -1) {
						value = value.substring(3, value.lastIndexOf(" M3 retained in the tank"));
						otherTanksString = value.substring(0, value.lastIndexOf(" "));
						quantity = value.substring(value.lastIndexOf(" "));
					} else {
						String tanksString = value.substring(0, value.lastIndexOf("\n"));
						value = value.replace(tanksString+"\n", "");
						
						value = value.substring(3, value.lastIndexOf(" M3 retained in the tank"));
						otherTanksString = value.substring(0, value.lastIndexOf(" "));
						quantity = value.substring(value.lastIndexOf(" "));
						String [] tanks = tanksString.split("\n");
						restoreTanks(tanks);
					}
					List<String> tanksList = Arrays.asList(otherTanksString.split(", "));
					Map<Tank, Double> userObjectTanks = new HashMap<Tank, Double>();
					tanksList.stream().forEach(ut -> userObjectTanks.put(getTankFromSettingsByName(ut), null));
					TankUsageContext tuc2 = new TankUsageContext();
					if(toAnotherOtherTanksJFXChipView.getUserData()==null) {
						toAnotherOtherTanksJFXChipView.setUserData(tuc2);
					} else {
						tuc2 = (TankUsageContext) toAnotherOtherTanksJFXChipView.getUserData();
					}
					tuc2.getUserTankQuantities().putAll(userObjectTanks);
					toAnotherOtherTanksJFXChipView.getChips().addAll(tanksList);
					totalContentOfTanksDoubleField.setValue(Double.parseDouble(quantity));
//					log.setContent(toAnotherOtherTanksTextField.getText() + " " +totalContentOfTanksTextFiled.getText() + " M3");
					break;
				case "12.3":
					methodsOfTransferOrDisposalCheckBox.setSelected(true);
					incineratedCheckBox.setSelected(true);
					value = oilLog.getContent();
					String timeOfOperation = "";
					if(value.indexOf("\n") == -1) {
						timeOfOperation = value;
					} else {
						String tanksString = value.substring(0, value.lastIndexOf("\n"));
						value = value.replace(tanksString+"\n", "");
						value = value.replace("BURNED IN INCENERATOR FOR ", "");
						timeOfOperation = value.replace(" HOURS", "");
						String [] tanks = tanksString.split("\n");
						restoreTanks(tanks);
					}
					incineratedIndicateTotalTimeOfOperationTextField.setText(timeOfOperation);
//					log.setContent(incineratedIndicateTotalTimeOfOperationTextField.getText() + " M3");
					break;
				case "12.4":
					methodsOfTransferOrDisposalCheckBox.setSelected(true);
					otherMethodCheckBox.setSelected(true);
					value = oilLog.getContent();
					String otherMethod = "";
					if(value.indexOf("\n") == -1) {
						otherMethod = value;
					} else {
						String tanksString = value.substring(0, value.lastIndexOf("\n"));
						value = value.replace(tanksString+"\n", "");
						otherMethod = value;
						String [] tanks = tanksString.split("\n");
						restoreTanks(tanks);
					}
					otherMethodStateWhichTextField.setText(otherMethod);
//					log.setContent(otherMethodStateWhichTextField.getText());
					break;
				default:
					break;
			}
		}
		
	}

	private void restoreTanks(String[] tanks) {
		int index = 0;
		for(String tank : tanks) {
			Map<String, String> map = parseTank(tank);
			String sludgeOrWater = map.get("sludgeOrWater");
			if(sludgeOrWater.equalsIgnoreCase(sludgeTransferredString)) sludgeToggle.setSelected(true);
			else if(sludgeOrWater.equalsIgnoreCase(waterDrainedString)) waterToggle.setSelected(true);
			if(index==0) {								
				Map<Tank, Double> userTanks12_1 = new HashMap<Tank, Double>();
				userTanks12_1.put(getTankFromSettingsByName(map.get("tankName")), null);
				TankUsageContext tuc = new TankUsageContext();
				if(tankNameFromTanksJFXChipView.getUserData()==null) {
					tankNameFromTanksJFXChipView.setUserData(tuc);
				} else {
					tuc = (TankUsageContext) tankNameFromTanksJFXChipView.getUserData();
				}
				tuc.getUserTankQuantities().putAll(userTanks12_1);
				
				tankNameFromTanksJFXChipView.getChips().add(map.get("tankName"));
				tankRetainedSludgeTankDoubleField.setValue(Double.parseDouble(map.get("tankRetained")));
				tankQuantityFromSludgeDoubleField.setValue(Double.parseDouble(map.get("tankQuantity")));
				index++;
			} else {
				Map<Tank, Double> userTanks12_1 = ((TankUsageContext) tankNameFromTanksJFXChipView.getUserData()).getUserTankQuantities();
				userTanks12_1.put(getTankFromSettingsByName(map.get("tankName")), null);
				
				Map<String, Node> mapNodes = addTank(cSludgeGridPane);
				((JFXChipView<String>)mapNodes.get("tankName")).getChips().add(map.get("tankName"));
				((DoubleField)mapNodes.get("tankRetained")).setValue(Double.parseDouble(map.get("tankRetained")));
				((DoubleField)mapNodes.get("tankQuantity")).setValue(Double.parseDouble(map.get("tankQuantity")));
				index++;
			}
		}
	}

	private void validateFields(Map<Validation, Object> validations) {
		if (!collectionOfOilResiduesCheckBox.isSelected() && !methodsOfTransferOrDisposalCheckBox.isSelected()) {
			validations.put(Validation.C11_OR_C12_REQUIRED, null);
		}
		if (identityOfTankCheckBox.isSelected() && identityOfTanksLetterCJFXChipView.getChips().isEmpty()) {
			validations.put(Validation.C11_1_EMPTY, null);
		}
		if (capacityOfTanksCheckBox.isSelected() && capacityOfTanksInM3DoubleField.getValue()==null) {
			validations.put(Validation.C11_2_EMPTY, null);
		}
//		if(capacityOfTanksCheckBox.isSelected()) {
//			double sum = ((TankUsageContext) identityOfTanksLetterCJFXChipView.getUserData()).getTotalUserTankCapacity();
//			if (capacityOfTanksInM3DoubleField.getValue() > sum) {
//				validations.put(Validation.C11_2_SUM_ERROR, null);
//			}
//		}
		if(methodsOfTransferOrDisposalCheckBox.isSelected()) {
			int nrOfSelectedMethods = 0;
			if(toReceptionFacilitiesIdentifyPortCheckBox.isSelected()) nrOfSelectedMethods++;
			if(toAnotherOtherTanksCheckBox.isSelected()) nrOfSelectedMethods++;
			if(incineratedCheckBox.isSelected()) nrOfSelectedMethods++;
			if(otherMethodCheckBox.isSelected()) nrOfSelectedMethods++;
			if(nrOfSelectedMethods==0 || nrOfSelectedMethods>1) {
				validations.put(Validation.C12_1_C12_2_C12_3_C12_4_REQUIRED, null);
			}
		}
		if (totalQuantityOfRetentionCheckBox.isSelected() && totalQuantityOfRetentionInM3DoubleField.getValue()==null) {
			validations.put(Validation.C11_3_EMPTY, null);
		}
		if (quantityOfResidueCollectedCheckBox.isSelected() && (quantityOfResidueCollectedByManualOperationInM3DoubleField.getValue()==null || identityOfTanksLetterC4JFXChipView.getChips().isEmpty())) {
			validations.put(Validation.C11_4_EMPTY, null);
		}
		if(toReceptionFacilitiesIdentifyPortCheckBox.isSelected() && (tankQuantityFromSludgeDoubleField.getValue()==null || tankNameFromTanksJFXChipView.getChips().isEmpty())) {
			validations.put(Validation.C12_TANK_REQUIRED, null);
		}
		if (toReceptionFacilitiesIdentifyPortCheckBox.isSelected() && (toReceptionFacilitiesTextFiled.getText().trim().isEmpty() || receiptTextField.getText().trim().isEmpty() || receiptTextField.getUserData()==null || !((File)receiptTextField.getUserData()).exists())) {
			validations.put(Validation.C12_1_FACILITIES_OR_RECEIPT_EMPTY, null);
		}
		if (toAnotherOtherTanksCheckBox.isSelected() && toAnotherOtherTanksJFXChipView.getChips().isEmpty()) {
			validations.put(Validation.C12_2_EMPTY, null);
		}
		if (toAnotherOtherTanksCheckBox.isSelected() && totalContentOfTanksDoubleField.getValue()==null) {
			validations.put(Validation.C12_2_TOTAL_EMPTY, null);
		}
		if (incineratedCheckBox.isSelected() && incineratedIndicateTotalTimeOfOperationTextField.getText().trim().isEmpty()) {
			validations.put(Validation.C12_3_EMPTY, null);
		}
		if (otherMethodCheckBox.isSelected() && otherMethodStateWhichTextField.getText().trim().isEmpty()) {
			validations.put(Validation.C12_4_EMPTY, null);
		}
		
		if (collectionOfOilResiduesCheckBox.isSelected() && !identityOfTankCheckBox.isSelected()) {
			validations.put(Validation.C11_1_REQUIRED, null);
		}
		if (collectionOfOilResiduesCheckBox.isSelected() && !capacityOfTanksCheckBox.isSelected()) {
			validations.put(Validation.C11_2_REQUIRED, null);
		}
		if(methodsOfTransferOrDisposalCheckBox.isSelected() && !waterToggle.isSelected() && !sludgeToggle.isSelected()) {
			validations.put(Validation.C12_SLUDGE_OR_WATER_REQUIRED, null);
		}

		if(totalQuantityOfRetentionCheckBox.isSelected()) {
			double sum = ((TankUsageContext) identityOfTanksLetterCJFXChipView.getUserData()).getTotalUserTankCapacity();
			double sum1 = 98.0/100.0*sum;
			if (totalQuantityOfRetentionInM3DoubleField.getValue()!=null && totalQuantityOfRetentionInM3DoubleField.getValue() > sum1) {
				validations.put(Validation.C11_3_SUM_WARNING, null);
			}
        }
		if (identityOfTankCheckBox.isSelected() && quantityOfResidueCollectedCheckBox.isSelected()) {
			TankUsageContext tankUsageContext11_1 = (TankUsageContext) identityOfTanksLetterCJFXChipView.getUserData();
			TankUsageContext tankUsageContext11_4 = (TankUsageContext) identityOfTanksLetterC4JFXChipView.getUserData();
			
			Map<Tank, Double> tankQuantities11_1 = tankUsageContext11_1.getUserTankQuantities();
			Map<Tank, Double> tankQuantities11_4 = tankUsageContext11_4.getUserTankQuantities();
			List<Tank> intersect = tankQuantities11_1.keySet().stream().filter(tankQuantities11_4.keySet()::contains).collect(Collectors.toList());
			if(!intersect.isEmpty()) {
				validations.put(Validation.C11_1_C11_4_SAME_TANK, intersect.stream().map(Tank::getName).collect(Collectors.joining(",")));
			}
			
			double sum11_1 = tankUsageContext11_1.getTotalUserTankQuantities()-tankUsageContext11_1.getTotalUserTankActualLevel();
			double sum11_4 = tankUsageContext11_4.getTotalUserTankQuantities();
			if (!(Math.abs(sum11_1 - sum11_4) < MathUtils.DOUBLE_THRESHOLD)) {
				validations.put(Validation.C11_1_SUM_DIFFERENT_C11_4_SUM_WARNING, new double[] {sum11_1, sum11_4});
			}
		}
		if (methodsOfTransferOrDisposalCheckBox.isSelected()) {
			TankUsageContext tuc = ((TankUsageContext) toAnotherOtherTanksJFXChipView.getUserData());
			if(tuc!=null && tuc.getUserTankQuantities()!=null && !tuc.getUserTankQuantities().isEmpty()) {
				Optional<Tank> tankOpt = tuc.getUserTankQuantities().keySet().stream().findFirst();
				tankOpt.ifPresent(tank -> {
					TankType type = tank.getType();
					if(type == TankType.CARGO || type == TankType.SLOP) {
						validations.put(Validation.C12_4_PartII_J_MAY_BE_REQUIRED, null);
					}
				});
			}
			
			
			Map<Tank, Double> tankQuantities12_0 = new HashMap<Tank, Double>();
			for(Pair<String, Tank> tank : getTanks(cSludgeGridPane, sludgeToggle.isSelected()?sludgeTransferredString : waterDrainedString )) {
				try {
					if(tankQuantities12_0.containsKey(tank.getValue())) validations.put(Validation.C12_TANK_ALREADY_IN_LIST, tank.getValue().getName());
					tankQuantities12_0.put(tank.getValue(), MathUtils.decimalFormat.parse(parseTank(tank.getKey()).get("tankRetained")).doubleValue());
				} catch (ParseException e) {
					logger.debug(e.getMessage(), e);
				}
			}
			if(tankQuantities12_0.isEmpty()) {
				validations.put(Validation.C12_TANK_REQUIRED, null);
			}
			if(toAnotherOtherTanksCheckBox.isSelected()) {
				Map<Tank, Double> tankQuantities12_2 = ((TankUsageContext) toAnotherOtherTanksJFXChipView.getUserData()).getUserTankQuantities();
				List<Tank> intersect = tankQuantities12_0.keySet().stream().filter(tankQuantities12_2.keySet()::contains).collect(Collectors.toList());
				if(!intersect.isEmpty()) {
					validations.put(Validation.C12_2_SAME_TANK_IN_FROM_AND_TO, intersect.stream().map(Tank::getName).collect(Collectors.joining(",")));
				}
				double totalQuantity12_0 = getTanksQuantity();
				double totalQuantity12_2 = ((TankUsageContext) toAnotherOtherTanksJFXChipView.getUserData()).getTotalUserTankQuantities();
				if (!(Math.abs(totalQuantity12_0 - totalQuantity12_2) < MathUtils.DOUBLE_THRESHOLD)) {
					validations.put(Validation.C12_SUM_DIFFERENT_C12_2_SUM_WARNING, new double[] {totalQuantity12_0, totalQuantity12_2});
				}
			}
		}
	}
	
	
	public void clearFilledInfo() {
		collectionOfOilResiduesCheckBox.setSelected(false);
		identityOfTankCheckBox.setSelected(false);
		capacityOfTanksCheckBox.setSelected(false);
		totalQuantityOfRetentionCheckBox.setSelected(false);
		quantityOfResidueCollectedCheckBox.setSelected(false);
		methodsOfTransferOrDisposalCheckBox.setSelected(false);
		toReceptionFacilitiesIdentifyPortCheckBox.setSelected(false);
		toAnotherOtherTanksCheckBox.setSelected(false);
		incineratedCheckBox.setSelected(false);
		otherMethodCheckBox.setSelected(false);
		
		identityOfTanksLetterCJFXChipView.getChips().clear();
		((TankUsageContext)identityOfTanksLetterCJFXChipView.getUserData()).getUserTankQuantities().clear();
		capacityOfTanksInM3DoubleField.clear();
		totalQuantityOfRetentionInM3DoubleField.clear();
		identityOfTanksLetterC4JFXChipView.getChips().clear();
		((TankUsageContext)identityOfTanksLetterC4JFXChipView.getUserData()).getUserTankQuantities().clear();
		quantityOfResidueCollectedByManualOperationInM3DoubleField.clear();
		toReceptionFacilitiesTextFiled.clear();
		receiptTextField.clear();
		receiptTextField.setUserData(null);
		toAnotherOtherTanksJFXChipView.getChips().clear();
		((TankUsageContext)toAnotherOtherTanksJFXChipView.getUserData()).getUserTankQuantities().clear();
		totalContentOfTanksDoubleField.clear();
		incineratedIndicateTotalTimeOfOperationTextField.clear();
		otherMethodStateWhichTextField.clear();
		tankNameFromTanksJFXChipView.getChips().clear();
		((TankUsageContext)tankNameFromTanksJFXChipView.getUserData()).getUserTankQuantities().clear();
        Platform.runLater(() -> {
        	tankRetainedSludgeTankDoubleField.clear();
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
	
	public void addSludgeTank(Event event) {
		Map<String, Node> map = addTank(cSludgeGridPane);
		TankUsageContext tuc = new TankUsageContext(Stream.of(TankType.OILYBILGE).collect(Collectors.toList()), false, false);
		tuc.setNoNeedForUserTankQuantities(true);
		map.get("tankName").setUserData(tuc);
		
		JFXChipView<String> tankNameJFXChipView = (JFXChipView<String>) map.get("tankName");
		DoubleField tankFillVolume = (DoubleField) map.get("tankQuantity");
		DoubleField tankRetainedVolume = (DoubleField) map.get("tankRetained");
		MathUtils.setMinMaxValues(tankFillVolume, 0.0, null);
		MathUtils.setMinMaxValues(tankRetainedVolume, 0.0, null);
//		tankRetainedVolume.setDisable(true);
		tankNameJFXChipView.getChips().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> change) {
				Double totalUserTankQuantities = ((TankUsageContext)tankNameJFXChipView.getUserData()).getTotalUserTankActualLevel();
				tankRetainedVolume.setValue(totalUserTankQuantities - Optional.ofNullable(tankFillVolume.getValue()).orElse(0.0));
			}
		});
		tankFillVolume.valueProperty().addListener(new ChangeListener<Double>() {
			@Override
			public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
				Double totalUserTankQuantities = ((TankUsageContext)tankNameJFXChipView.getUserData()).getTotalUserTankActualLevel();
				tankRetainedVolume.setValue(totalUserTankQuantities - Optional.ofNullable(newValue).orElse(0.0));
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
			logger.error(e.getMessage(), e);
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
		tankNameJFXChipView.setId("tankName"+rows);
		tankRetainedVolume.setId("tankRetained"+rows);
		tankFillVolume.setId("tankQuantity"+rows);
		tankRetainedVolume.getStyleClass().add("logbook-text-field");
		tankFillVolume.getStyleClass().add("logbook-text-field");
		JavaFXUtils.addFilledPseudoClass(tankRetainedVolume);
		JavaFXUtils.addFilledPseudoClass(tankFillVolume);
		
		Label identifyTheTankLabel = new Label("identify the tank");
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
//					double sum = ((TankUsageContext) identityOfTanksLetterCJFXChipView.getUserData()).getTotalUserTankCapacity();
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
	
	private Double getTanksQuantity() {
		Double tanksQuantity = 0.0;
		for(Pair<String, Tank> tank : getTanks(cSludgeGridPane, sludgeToggle.isSelected()?sludgeTransferredString : waterDrainedString )) {
			try {
				tanksQuantity = tanksQuantity + MathUtils.decimalFormat.parse(parseTank(tank.getKey()).get("tankQuantity")).doubleValue();
			} catch (ParseException e) {
				logger.debug(e.getMessage(), e);
			}
		}
		return tanksQuantity;
	}
	private List<Pair<String, Tank>> getTanks(GridPane tanksGridPane, String sludgeOrWater) {
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
	

		StringTemplate template = sludgeTemplate;
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
					if(userTanks!=null && !userTanks.isEmpty()) {
						tank = (Tank)userTanks.iterator().next();
						capacity = tank.getSize();
					}
					template.setAttribute("tankCapacity", capacity!=null?MathUtils.decimalFormat.format(capacity):null);
					template.setAttribute("sludgeOrWater", sludgeOrWater);
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
		Pattern pattern = sludgePattern;
		Matcher matcher = pattern.matcher(tankString);
		if (matcher.find()) {
			map.put("tankQuantity", matcher.group(1));
			map.put("sludgeOrWater", matcher.group(2));
			map.put("tankName", matcher.group(3));
			map.put("tankCapacity", matcher.group(4));
			map.put("tankRetained", matcher.group(5));
			
			logger.debug(matcher.group(1));
			logger.debug(matcher.group(2));
			logger.debug(matcher.group(3));
			logger.debug(matcher.group(4));
			logger.debug(matcher.group(5));
		} else {
			logger.debug("could not parse: " + tankString);
		}
		return map;
	}
}
