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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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
import com.oceanpro.utils.EasyDateUtils;

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
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import jfxtras.scene.control.ImageViewButton;
import jfxtras.scene.control.LocalDateTimeTextField;
import jidefx.scene.control.field.DoubleField;

public class OilRecordBookPart1CodeLetterHController implements Initializable {

	private Label label7 = new Label(
			"Notes:\r\n" + "Separate entries are required for each grade of \r\n" + "fuel oil being bunkered\r\n"+ "");
			
	private Label label8 = new Label(
			"Notes:\r\n" + "Separate entries are required for each grade of \r\n" + "fuel oil being bunkered\r\n"
	         + "Quantities should be recorded in metric tonnes.\r\n"
	         + "An entry in the ORB is NOT required when \r\n"
	         + "lubricating oils are delivered onboard in\r\n"
	         +"packaged form (55 gallon drum, etc.) \r\n"+ "");
	
	
	protected String CACHE_FILE_NAME = System.getProperty("user.home") + "/.ebr/caches/caches.properties";
	private static final Logger logger = LoggerCreator.createLogger(OilRecordBookPart1CodeLetterHController.class);
	
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
	private HelpCodeLetterDialog helpCodeLetterDialog;
	@Inject
	private OperationLogReladomoService operationLogReladomoService;
	@Inject
	private IEventBroker eventBroker;
	// codeLetter H
	@FXML
	private VBox mainVBox;
	@FXML
	private ImageViewButton identityOfTanksImageViewButton;
	@FXML
	private ImageViewButton identityOfTanks2ImageViewButton;
	@FXML
	private ImageViewButton imageViewButtonBunkeringInfo;
	@FXML
	private ImageViewButton imageViewButtonLubricatingOilInfo;
	@FXML
	private ImageViewButton imageViewButtonInfoLetterH;
	@FXML
	private CheckBox bunkeringCheckBox;
	@FXML
	private CheckBox placeOfBunkeringCheckBox;
	@FXML
	private TextField placeOfBunkeringTextField;
	@FXML
	private CheckBox timeOfBunkeringCheckBox;
	@FXML
	private LocalDateTimeTextField timeOfBunkeringStarttimeField;
	@FXML
	private LocalDateTimeTextField timeOfBunkeringStoptimeField;
	@FXML
	private CheckBox quantityOfFuelCheckBox;
	@FXML
	private DoubleField quantityOfFuelDoubleField;
	@FXML
	private TextField typeOfFuelTextField;
	@FXML
	private DoubleField tankContentDoubleField1;
	@FXML
	private TextField stateQuantityAddedInTonnesTextField2;
	@FXML
	private TextField stateQuantityAddedInTonnesTextField3;
	@FXML
	private JFXChipView<String> tankNameJFXChipView1;
	@FXML
	private TextField identityOfTankTextField2;
	@FXML
	private TextField identityOfTankTextField3;
	@FXML
	private DoubleField tankCapacityDoubleField1;
	@FXML
	private TextField totalContentOfTankTextField2;
	@FXML
	private TextField totalContentOfTankTextField3;
	@FXML
	private CheckBox quantityOfLubricatingOilCheckBox;
	@FXML
	private DoubleField quantityOfLubricatingOilTextField;
	@FXML
	private TextField typeOflubricatingOilTextField;
	@FXML
	private DoubleField tankContentOilDoubleField1;
	@FXML
	private JFXChipView<String> tankNameOilJFXChipView1;
	@FXML
	private DoubleField tankCapacityOilDoubleField1;
	@FXML
	private GridPane fuelGridPane;
	@FXML
	private GridPane oilGridPane;
	@FXML
	private Button addFuelTankButton;
	@FXML
	private Button addOilTankButton;
	@FXML
	private Button addFuelOperationButton;
	@FXML
	private Button addOilOperationButton;
	@FXML
	private GridPane fuelOperationGridPane;
	@FXML
	private GridPane oilOperationGridPane;
	@FXML
	private TextField bdn261TextField;
	@FXML
	private ImageViewButton uploadBdn261ImageViewButton;
	@FXML
	private TextField bdn264TextField;
	@FXML
	private ImageViewButton uploadBdn264ImageViewButton;
	@FXML
	public AnchorPane codeLetterContentAnchorPane;

	private Pattern pattern = Pattern.compile("(.{1,}) M3 ADDED TO (.{1,}) NOW CONTAINING (.{1,}) M3");
	private final StringTemplate template = new StringTemplate("$tankContent$ M3 ADDED TO $tankName$ NOW CONTAINING $tankCapacity$ M3");
	
	private final StringTemplate fuelTemplate = new StringTemplate("$quantity$ MT OF FUEL TYPE $type$ BUNKERED IN TANKS.");
	private final StringTemplate oilTemplate = new StringTemplate("$quantity$ MT OF OIL TYPE $type$ BUNKERED IN TANKS.");
	private Pattern fuelPattern = Pattern.compile("(.{1,}) MT OF FUEL TYPE (.{1,}) BUNKERED IN TANKS.");
	private Pattern oilPattern = Pattern.compile("(.{1,}) MT OF OIL TYPE (.{1,}) BUNKERED IN TANKS.");
	
	private TextField currentSelectedTextField;
	private Image addImageIdentityOfTanksLetterH;
	private ObservableList<Image> helpImageList = FXCollections.observableArrayList();
	private DateTimeFormatter dateTimeFormatter = EasyDateUtils.logbookPdfDateTimeFormatter;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MathUtils.setStringConverter(quantityOfFuelDoubleField);
		MathUtils.setStringConverter(tankContentDoubleField1);
		MathUtils.setStringConverter(tankCapacityDoubleField1);
		MathUtils.setStringConverter(quantityOfLubricatingOilTextField);
		MathUtils.setStringConverter(tankContentOilDoubleField1);
		MathUtils.setStringConverter(tankCapacityOilDoubleField1);
		
		MathUtils.setMinMaxValues(quantityOfFuelDoubleField, 0.0, null);
		MathUtils.setMinMaxValues(tankContentDoubleField1, 0.0, null);
		MathUtils.setMinMaxValues(tankCapacityDoubleField1, 0.0, null);
		MathUtils.setMinMaxValues(quantityOfLubricatingOilTextField, 0.0, null);
		MathUtils.setMinMaxValues(tankContentOilDoubleField1, 0.0, null);
		MathUtils.setMinMaxValues(tankCapacityOilDoubleField1, 0.0, null);
		
		timeOfBunkeringStarttimeField.setDateTimeFormatter(EasyDateUtils.logbookDateTimeFormatter);
		timeOfBunkeringStoptimeField.setDateTimeFormatter(EasyDateUtils.logbookDateTimeFormatter);
		placeOfBunkeringTextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		typeOfFuelTextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		typeOflubricatingOilTextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		JavaFXUtils.addFilledPseudoClass(placeOfBunkeringTextField);
		JavaFXUtils.addFilledPseudoClass(bdn261TextField);
		JavaFXUtils.addFilledPseudoClass(tankContentDoubleField1);
		JavaFXUtils.addFilledPseudoClass(tankCapacityDoubleField1);
		JavaFXUtils.addFilledPseudoClass(quantityOfFuelDoubleField);
		JavaFXUtils.addFilledPseudoClass(typeOfFuelTextField);
		JavaFXUtils.addFilledPseudoClass(tankContentOilDoubleField1);
		JavaFXUtils.addFilledPseudoClass(tankCapacityOilDoubleField1);
		JavaFXUtils.addFilledPseudoClass(quantityOfLubricatingOilTextField);
		JavaFXUtils.addFilledPseudoClass(typeOflubricatingOilTextField);
		JavaFXUtils.addFilledPseudoClass(bdn264TextField);
		JavaFXUtils.addFilledPseudoClass(timeOfBunkeringStarttimeField);
		JavaFXUtils.addFilledPseudoClass(timeOfBunkeringStoptimeField);
		try {
			Image helpImageLetterH1 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_HELPH1));
		
			Image helpImageLetterH2 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_HELPH2));
			
			Image helpImageLetterH3 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_HELPH3));
			
			helpImageList.addAll(helpImageLetterH1, helpImageLetterH2, helpImageLetterH3);
			
			Image infoImageLetterH = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
			imageViewButtonInfoLetterH.setImage(infoImageLetterH);

			Image infoImageBunkering = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
			imageViewButtonBunkeringInfo.setImage(infoImageBunkering);

			Image infoImageLubricatingOil = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
			imageViewButtonLubricatingOilInfo.setImage(infoImageLubricatingOil);
			
			addImageIdentityOfTanksLetterH = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_ADD));
			identityOfTanksImageViewButton.setImage(addImageIdentityOfTanksLetterH);
			identityOfTanks2ImageViewButton.setImage(addImageIdentityOfTanksLetterH);
			
			
			
			FileChooser fileChooser261 = new FileChooser();
			FileChooser fileChooser264 = new FileChooser();
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
	            fileChooser261.setInitialDirectory(new File(System.getProperty("user.home")));
	            fileChooser264.setInitialDirectory(new File(System.getProperty("user.home")));
	        } else {
	            fileChooser261.setInitialDirectory(new File(userInitialDirectory));
	            fileChooser264.setInitialDirectory(new File(userInitialDirectory));
	        }
			FileChooser.ExtensionFilter pdfFilesFilter = new FileChooser.ExtensionFilter("PDF files", "*.pdf");
			fileChooser261.getExtensionFilters().addAll(pdfFilesFilter);
			fileChooser264.getExtensionFilters().addAll(pdfFilesFilter);
			
	        uploadBdn261ImageViewButton.setImage(new Image(getClass().getResourceAsStream("/images/uploadReceipt.png")));
	        uploadBdn261ImageViewButton.setOnMouseClicked(me -> {
				File selectedFile = null;
				try {
					selectedFile = fileChooser261.showOpenDialog((Stage) ((ImageViewButton) me.getSource()).getScene().getWindow());
				} catch(IllegalArgumentException e) {
					fileChooser261.setInitialDirectory(new File(System.getProperty("user.home")));
					selectedFile = fileChooser261.showOpenDialog((Stage) ((ImageViewButton) me.getSource()).getScene().getWindow());
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
					bdn261TextField.setText(selectedFile.getName());
					bdn261TextField.setUserData(selectedFile);
				}
			});
	        uploadBdn264ImageViewButton.setImage(new Image(getClass().getResourceAsStream("/images/uploadReceipt.png")));
	        uploadBdn264ImageViewButton.setOnMouseClicked(me -> {
				File selectedFile = null;
				try {
					selectedFile = fileChooser264.showOpenDialog((Stage) ((ImageViewButton) me.getSource()).getScene().getWindow());
				} catch(IllegalArgumentException e) {
					fileChooser264.setInitialDirectory(new File(System.getProperty("user.home")));
					selectedFile = fileChooser264.showOpenDialog((Stage) ((ImageViewButton) me.getSource()).getScene().getWindow());
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
					bdn264TextField.setText(selectedFile.getName());
					bdn264TextField.setUserData(selectedFile);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		bunkeringCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					placeOfBunkeringCheckBox.setDisable(false);
					timeOfBunkeringCheckBox.setDisable(false);
					quantityOfFuelCheckBox.setDisable(false);
					quantityOfLubricatingOilCheckBox.setDisable(false);
					placeOfBunkeringCheckBox.setSelected(true);
					timeOfBunkeringCheckBox.setSelected(true);
				} else {
					placeOfBunkeringCheckBox.setDisable(true);
					timeOfBunkeringCheckBox.setDisable(true);
					quantityOfFuelCheckBox.setDisable(true);
					quantityOfLubricatingOilCheckBox.setDisable(true);
					placeOfBunkeringCheckBox.setSelected(false);
					timeOfBunkeringCheckBox.setSelected(false);
				}
				quantityOfFuelCheckBox.setSelected(false);
				quantityOfLubricatingOilCheckBox.setSelected(false);
			}
		});
		placeOfBunkeringCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					placeOfBunkeringTextField.setDisable(false);
					bdn261TextField.setDisable(false);
					uploadBdn261ImageViewButton.setDisable(false);
				} else {
					placeOfBunkeringTextField.setText("");
					placeOfBunkeringTextField.setDisable(true);
					bdn261TextField.clear();
					bdn261TextField.setDisable(true);
					uploadBdn261ImageViewButton.setDisable(true);
				}
			}

		});
		timeOfBunkeringCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					timeOfBunkeringStarttimeField.setDisable(false);
					timeOfBunkeringStoptimeField.setDisable(false);
				} else {
					timeOfBunkeringStarttimeField.setDisable(true);
					timeOfBunkeringStoptimeField.setDisable(true);
				}
				timeOfBunkeringStarttimeField.setPromptText("");
				timeOfBunkeringStoptimeField.setPromptText("");
			}
		});
		quantityOfFuelCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					quantityOfFuelDoubleField.setDisable(false);
					typeOfFuelTextField.setDisable(false);
					tankContentDoubleField1.setDisable(false);
					addFuelTankButton.setDisable(false);
					identityOfTanksImageViewButton.setDisable(false);
					tankNameJFXChipView1.setDisable(false);
					tankCapacityDoubleField1.setDisable(false);
					addFuelOperationButton.setDisable(false);
				} else {
					fuelGridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node)!=null && GridPane.getRowIndex(node) > 2);
					fuelGridPane.getRowConstraints().remove(3, fuelGridPane.getRowConstraints().size());
					fuelOperationGridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node)!=null && GridPane.getRowIndex(node) > 0);
					quantityOfFuelDoubleField.setDisable(true);
					typeOfFuelTextField.setDisable(true);
					tankContentDoubleField1.setDisable(true);
					addFuelTankButton.setDisable(true);
					identityOfTanksImageViewButton.setDisable(true);
					tankNameJFXChipView1.setDisable(true);
					tankCapacityDoubleField1.setDisable(true);
					addFuelOperationButton.setDisable(true);
				}
				quantityOfFuelDoubleField.setText("");
				typeOfFuelTextField.setText("");
				tankContentDoubleField1.setText("");
				tankNameJFXChipView1.getChips().clear();;
				tankCapacityDoubleField1.setText("");
			}
		});
		
		tankCapacityDoubleField1.valueProperty().addListener(new ChangeListener<Double>() {
			@Override
			public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
				Double totalUserTankCapacity = ((TankUsageContext)tankNameJFXChipView1.getUserData()).getTotalUserTankCapacity();
				Double totalUserTankActualLevel = ((TankUsageContext)tankNameJFXChipView1.getUserData()).getTotalUserTankActualLevel();
				if(!tankNameJFXChipView1.getChips().isEmpty()) {
					if(newValue!=null && newValue > totalUserTankCapacity) {
						OilRecordBookPart1ViewController.showWarning("Invalid value:" + newValue, "Valid value range:0.0-" + totalUserTankCapacity);
						tankContentDoubleField1.setValue(totalUserTankCapacity-totalUserTankActualLevel);
					} else {
						if(newValue!=null && newValue > totalUserTankCapacity*0.98) {
							OilRecordBookPart1ViewController.showWarning("Value:" + newValue, "Is greater than 98% of max value (" + totalUserTankCapacity +")!");
						}
					}
				}
			}
		});
		tankCapacityOilDoubleField1.valueProperty().addListener(new ChangeListener<Double>() {
			@Override
			public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
				Double totalUserTankCapacity = ((TankUsageContext)tankNameOilJFXChipView1.getUserData()).getTotalUserTankCapacity();
				Double totalUserTankActualLevel = ((TankUsageContext)tankNameOilJFXChipView1.getUserData()).getTotalUserTankActualLevel();
				if(!tankNameOilJFXChipView1.getChips().isEmpty()) {
					if(newValue!=null && newValue > totalUserTankCapacity) {
						OilRecordBookPart1ViewController.showWarning("Invalid value:" + newValue, "Valid value range:0.0-" + totalUserTankCapacity);
						tankContentOilDoubleField1.setValue(totalUserTankCapacity-totalUserTankActualLevel);
					} else {
						if(newValue!=null && newValue > totalUserTankCapacity*0.98) {
							OilRecordBookPart1ViewController.showWarning("Value:" + newValue, "Is greater than 98% of max value (" + totalUserTankCapacity +")!");
						}
					}
				}
			}
		});
		
		TankUsageContext tuc = new TankUsageContext(Stream.of(TankType.FUELOIL).collect(Collectors.toList()), true, false);
		tuc.setNoNeedForUserTankQuantities(true);
		tankNameJFXChipView1.setUserData(tuc);
		tankNameJFXChipView1.getChips().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> change) {
		        while (change.next()) {
		            for (String item : change.getAddedSubList()) {
		            	if(tankNameJFXChipView1.getChips().size()>1) {
		            		String oldChip = tankNameJFXChipView1.getChips().get(0);
		            		Platform.runLater(() -> tankNameJFXChipView1.getChips().remove(oldChip));
		            	}
		            }
		        }
				Double totalUserTankQuantities = ((TankUsageContext)tankNameJFXChipView1.getUserData()).getTotalUserTankActualLevel();
				tankCapacityDoubleField1.setValue(totalUserTankQuantities + Optional.ofNullable(tankContentDoubleField1.getValue()).orElse(0.0));
				JavaFXUtils.addTooltip(tankNameJFXChipView1);
			}
		});
		tankContentDoubleField1.valueProperty().addListener(new ChangeListener<Double>() {
			@Override
			public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
				Double totalUserTankQuantities = ((TankUsageContext)tankNameJFXChipView1.getUserData()).getTotalUserTankActualLevel();
				tankCapacityDoubleField1.setValue(totalUserTankQuantities + Optional.ofNullable(newValue).orElse(0.0));
			}
		});
		addRemoveListener(tankNameJFXChipView1);
		
		
		quantityOfLubricatingOilCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {	
				if(!newValue) {
					oilGridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node)!=null && GridPane.getRowIndex(node) > 2);
					oilGridPane.getRowConstraints().remove(3, oilGridPane.getRowConstraints().size());
					oilOperationGridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node)!=null && GridPane.getRowIndex(node) > 0);
				}
				if(newValue) {
					typeOflubricatingOilTextField.setDisable(false);
					quantityOfLubricatingOilTextField.setDisable(false);
					tankContentOilDoubleField1.setDisable(false);
					tankNameOilJFXChipView1.setDisable(false);
					tankCapacityOilDoubleField1.setDisable(false);
					addOilTankButton.setDisable(false);
					identityOfTanks2ImageViewButton.setDisable(false);
					addOilOperationButton.setDisable(false);
					bdn264TextField.setDisable(false);
					uploadBdn264ImageViewButton.setDisable(false);
				} else {
					typeOflubricatingOilTextField.setDisable(true);
					quantityOfLubricatingOilTextField.setDisable(true);
					tankContentOilDoubleField1.setDisable(true);
					tankNameOilJFXChipView1.setDisable(true);
					tankCapacityOilDoubleField1.setDisable(true);
					addOilTankButton.setDisable(true);
					identityOfTanks2ImageViewButton.setDisable(true);
					addOilOperationButton.setDisable(true);
					bdn264TextField.setDisable(true);
					uploadBdn264ImageViewButton.setDisable(true);
				}
				typeOflubricatingOilTextField.setText("");
				quantityOfLubricatingOilTextField.setText("");
				tankContentOilDoubleField1.setText("");
				tankNameOilJFXChipView1.getChips().clear();
				tankCapacityOilDoubleField1.clear();
				bdn264TextField.clear();
			}
		});
		TankUsageContext tuc2 = new TankUsageContext(Stream.of(TankType.FUELOIL).collect(Collectors.toList()), true, false);
		tuc2.setNoNeedForUserTankQuantities(true);
		tankNameOilJFXChipView1.setUserData(tuc2);
		tankNameOilJFXChipView1.getChips().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> change) {
		        while (change.next()) {
		            for (String item : change.getAddedSubList()) {
		            	if(tankNameOilJFXChipView1.getChips().size()>1) {
		            		String oldChip = tankNameOilJFXChipView1.getChips().get(0);
		            		Platform.runLater(() -> tankNameOilJFXChipView1.getChips().remove(oldChip));
		            	}
		            }
		        }
				Double totalUserTankQuantities = ((TankUsageContext)tankNameOilJFXChipView1.getUserData()).getTotalUserTankActualLevel();
				tankCapacityOilDoubleField1.setValue(totalUserTankQuantities + Optional.ofNullable(tankContentOilDoubleField1.getValue()).orElse(0.0));
				JavaFXUtils.addTooltip(tankNameOilJFXChipView1);
			}
		});
		tankContentOilDoubleField1.valueProperty().addListener(new ChangeListener<Double>() {
			@Override
			public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
				Double totalUserTankQuantities = ((TankUsageContext)tankNameOilJFXChipView1.getUserData()).getTotalUserTankActualLevel();
				tankCapacityOilDoubleField1.setValue(totalUserTankQuantities + Optional.ofNullable(newValue).orElse(0.0));
			}
		});
		addRemoveListener(tankNameOilJFXChipView1);
		
		
		imageViewButtonLubricatingOilInfo.setOnMouseEntered(mouseEvent -> {if(!mouseEvent.isPrimaryButtonDown()) imageViewButtonLubricatingOilInfo.setCursor(Cursor.HAND);});
		imageViewButtonLubricatingOilInfo.setOnMouseExited(mouseEvent -> {if(!mouseEvent.isPrimaryButtonDown()) imageViewButtonLubricatingOilInfo.setCursor(Cursor.DEFAULT);});
		
		addFuelTankButton.setOnMouseEntered(mouseEvent -> {if(!mouseEvent.isPrimaryButtonDown()) addFuelTankButton.setCursor(Cursor.HAND);});
		addFuelTankButton.setOnMouseExited(mouseEvent -> {if(!mouseEvent.isPrimaryButtonDown()) addFuelTankButton.setCursor(Cursor.DEFAULT);});

		addOilTankButton.setOnMouseEntered(mouseEvent -> {if(!mouseEvent.isPrimaryButtonDown()) addOilTankButton.setCursor(Cursor.HAND);});
		addOilTankButton.setOnMouseExited(mouseEvent -> {if(!mouseEvent.isPrimaryButtonDown()) addOilTankButton.setCursor(Cursor.DEFAULT);});
		
		addFuelOperationButton.setOnMouseEntered(mouseEvent -> {if(!mouseEvent.isPrimaryButtonDown()) addFuelOperationButton.setCursor(Cursor.HAND);});
		addFuelOperationButton.setOnMouseExited(mouseEvent -> {if(!mouseEvent.isPrimaryButtonDown()) addFuelOperationButton.setCursor(Cursor.DEFAULT);});
		
		addOilOperationButton.setOnMouseEntered(mouseEvent -> {if(!mouseEvent.isPrimaryButtonDown()) addOilOperationButton.setCursor(Cursor.HAND);});
		addOilOperationButton.setOnMouseExited(mouseEvent -> {if(!mouseEvent.isPrimaryButtonDown()) addOilOperationButton.setCursor(Cursor.DEFAULT);});
		

		GridPane.setRowIndex(fuelGridPane, 0);
		GridPane.setRowIndex(oilGridPane, 0);
		GridPane.setRowIndex(quantityOfFuelDoubleField, 0); GridPane.setRowIndex(typeOfFuelTextField, 0);
		GridPane.setRowIndex(quantityOfLubricatingOilTextField, 0); GridPane.setRowIndex(typeOflubricatingOilTextField, 0);
		
		eventBroker.subscribe(Activator.MARPOL_DETAILS_RELOADED, new org.osgi.service.event.EventHandler() {
			@Override
			public void handleEvent(org.osgi.service.event.Event event) {
				ArrayList<Long> changedTankIds = (ArrayList<Long>) event.getProperty(Activator.TANK_IDS);
    			if(changedTankIds==null) {
    				tankNameJFXChipView1.getChips().clear();
    				tankNameOilJFXChipView1.getChips().clear();
    			} else if(changedTankIds!=null && !changedTankIds.isEmpty()) {
    				Activator.removeChangedTanksFromJFXChipView(changedTankIds, tankNameJFXChipView1);
    				Activator.removeChangedTanksFromJFXChipView(changedTankIds, tankNameOilJFXChipView1);
    			}
			}
		});
	}
	

	public void processH(OilLog logToInsert, List<OilLog> logToInsertList, Map<Validation, Object> validations) {
		validateFields(logToInsert.getBusinessDate(), validations);
		
		if (placeOfBunkeringCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("26.1");
			log.setContent(placeOfBunkeringTextField.getText() + " - BDN FILE '" + bdn261TextField.getText() + "'");
			if(bdn261TextField.getUserData()!=null && ((File)bdn261TextField.getUserData()).exists()) {
				log.setAttachmentName(((File)bdn261TextField.getUserData()).getName());
				byte[] attachmentBytes = null;
				try {
					attachmentBytes = Files.readAllBytes(((File)bdn261TextField.getUserData()).toPath());
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
				log.setAttachmentData(attachmentBytes);
			}
			log.setOrderInGroup("ZL");
			logToInsertList.add(log);
		}
		if (timeOfBunkeringCheckBox.isSelected() && timeOfBunkeringStarttimeField.getLocalDateTime()!=null && timeOfBunkeringStoptimeField.getLocalDateTime()!=null) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("26.2");
			log.setContent("START " + timeOfBunkeringStarttimeField.getLocalDateTime().format(dateTimeFormatter) + " STOP " + timeOfBunkeringStoptimeField.getLocalDateTime().format(dateTimeFormatter));
			log.setOrderInGroup("ZM");
			logToInsertList.add(log);
		}
		if (quantityOfFuelCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("26.3");
			StringBuilder sb = new StringBuilder();
			Map<Integer, Pair<String, GridPane>> operations = getOperations(fuelOperationGridPane, true);
			
			Map<Tank, Double> userTankActualLevels = new HashMap<Tank, Double>();
			for(Integer operationIndex : operations.keySet()) {
				Pair<String, GridPane> operation = operations.get(operationIndex);
				if(sb.length()>0) {sb.append("\n");sb.append("\n");}
				sb.append(operation.getKey());
				for(Pair<String,Tank> tankPair : getTanks(operation.getValue())) {
					try {
						userTankActualLevels.put(tankPair.getValue(), MathUtils.decimalFormat.parse(parseTank(tankPair.getKey()).get("tankCapacity")).doubleValue());
					} catch (Exception e) {
						logger.debug(e.getMessage(), e);
					}
					sb.append("\n");
					sb.append(tankPair.getKey());
				}
			}
			log.setContent(sb.toString());
			log.setUserTankActualLevels(userTankActualLevels);
			log.setOrderInGroup("ZN");
			logToInsertList.add(log);
		}
		if (quantityOfLubricatingOilCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("26.4");
			StringBuilder sb = new StringBuilder();
			Map<Integer, Pair<String, GridPane>> operations = getOperations(oilOperationGridPane, false);
			
			Map<Tank, Double> userTankActualLevels = new HashMap<Tank, Double>();
			for(Integer operationIndex : operations.keySet()) {
				Pair<String, GridPane> operation = operations.get(operationIndex);
				if(sb.length()>0) {sb.append("\n");sb.append("\n");}
				sb.append(operation.getKey());
				for(Pair<String,Tank> tankPair : getTanks(operation.getValue())) {
					try {
						userTankActualLevels.put(tankPair.getValue(), MathUtils.decimalFormat.parse(parseTank(tankPair.getKey()).get("tankCapacity")).doubleValue());
					} catch (ParseException e) {
						logger.debug(e.getMessage(), e);
					}
					sb.append("\n");
					sb.append(tankPair.getKey());
				}
			}
			if(bdn264TextField.getUserData()!=null && ((File)bdn264TextField.getUserData()).exists()) {
				log.setAttachmentName(((File)bdn264TextField.getUserData()).getName());
				byte[] attachmentBytes = null;
				try {
					attachmentBytes = Files.readAllBytes(((File)bdn264TextField.getUserData()).toPath());
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
				log.setAttachmentData(attachmentBytes);
				
				sb.append("\n");
				sb.append("BDN FILE '" + bdn264TextField.getText() + "'");
			}
			log.setContent(sb.toString());
			log.setUserTankActualLevels(userTankActualLevels);
			log.setOrderInGroup("ZO");
			logToInsertList.add(log);
		}
	}




	private void validateFields(Date registrationDate, Map<Validation, Object> validations) {
		if (placeOfBunkeringCheckBox.isSelected() && placeOfBunkeringTextField.getText().trim().isEmpty()) {
			validations.put(Validation.H26_1_PLACEOFBUNKERING_EMPTY, null);
		}
		if((bdn261TextField.getText().trim().isEmpty() || bdn261TextField.getUserData()==null || !((File)bdn261TextField.getUserData()).exists())
				&& (placeOfBunkeringTextField.getText().trim().isEmpty() || bdn264TextField.getText().trim().isEmpty() || bdn264TextField.getUserData()==null || !((File)bdn264TextField.getUserData()).exists())) {
			validations.put(Validation.H26_1_26_4_BND_EMPTY, null);
		}
		if (!bunkeringCheckBox.isSelected() || (!placeOfBunkeringCheckBox.isSelected() && !timeOfBunkeringCheckBox.isSelected() && !quantityOfFuelCheckBox.isSelected() && !quantityOfLubricatingOilCheckBox.isSelected())) {
			validations.put(Validation.H26_REQUIRED, null);
		}
		if (!(placeOfBunkeringCheckBox.isSelected() && timeOfBunkeringCheckBox.isSelected() && (quantityOfFuelCheckBox.isSelected() || quantityOfLubricatingOilCheckBox.isSelected()))) {
			if(placeOfBunkeringCheckBox.isSelected() || timeOfBunkeringCheckBox.isSelected() || quantityOfFuelCheckBox.isSelected() || quantityOfLubricatingOilCheckBox.isSelected()) {
				validations.put(Validation.H26_1_26_2_26_3_OR_26_4_ALL_OR_NOTHING, null);
			}
		}
		if (timeOfBunkeringCheckBox.isSelected() && (timeOfBunkeringStarttimeField.getLocalDateTime()==null || timeOfBunkeringStoptimeField.getLocalDateTime()==null)) {
			validations.put(Validation.H26_2_EMPTY, null);
		} else if(timeOfBunkeringCheckBox.isSelected() && timeOfBunkeringStarttimeField.getLocalDateTime().isAfter(timeOfBunkeringStoptimeField.getLocalDateTime())) {
			validations.put(Validation.H26_2_START_AFTER_END, null);
		}
		if(registrationDate != null && timeOfBunkeringCheckBox.isSelected() && timeOfBunkeringStarttimeField.getLocalDateTime()!=null && timeOfBunkeringStoptimeField.getLocalDateTime()!=null) {
			LocalDateTime registrationLocalDateTime = Instant.ofEpochMilli(registrationDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime().plusDays(1);
			if(registrationLocalDateTime.isBefore(timeOfBunkeringStarttimeField.getLocalDateTime()) || registrationLocalDateTime.isBefore(timeOfBunkeringStoptimeField.getLocalDateTime())) {
				validations.put(Validation.H26_2_TIME_AFTER_REGISTRATION_DATE, null);
			}
		}
		
		if(quantityOfFuelCheckBox.isSelected()) {
			try {
				List<Pair<Double, Double>> quantities = new ArrayList<>();
				Map<Integer, Pair<String, GridPane>> operations = getOperations(fuelOperationGridPane, true);
				for(Integer operationIndex : operations.keySet()) {
					Pair<String, GridPane> operation = operations.get(operationIndex);
					String declaredQuantityString = parseFuelOperation(operation.getKey()).get("quantity");
					if(declaredQuantityString==null) {
						validations.put(Validation.H26_3_MISSING_QUANTITY_OR_TYPE, operation.getKey());
						continue;
					}
					Double declaredQuantity = MathUtils.decimalFormat.parse(declaredQuantityString).doubleValue();
					Double tanksQuantity = 0.0;
					List<Pair<String, Tank>> tankPairList = getTanks(operation.getValue());
					for(Pair<String,Tank> tankPair : tankPairList) {
						String currentTankQuantityString = parseTank(tankPair.getKey()).get("tankContent");
						if(currentTankQuantityString==null) {
							validations.put(Validation.H26_3_MISSING_TANK_DATA, tankPair.getKey());
							continue;
						}
						tanksQuantity = tanksQuantity + MathUtils.decimalFormat.parse(currentTankQuantityString).doubleValue();
					}
					quantities.add(new Pair<Double, Double>(declaredQuantity, tanksQuantity));
					
					List<Pair<String, Tank>> notNullTanksTankPairList = tankPairList.stream().filter(t -> t.getValue() != null).collect(Collectors.toList());
					List<Tank> duplicates = notNullTanksTankPairList.stream()
								  .collect(Collectors.groupingBy(p -> p.getValue(), Collectors.counting()))
								  .entrySet().stream()
								  .filter(n -> n.getValue() > 1)
								  .map(n -> n.getKey())
								  .collect(Collectors.toList());
					for(Tank duplicateTank : duplicates) {
						validations.put(Validation.H26_3_TANK_ALREADY_IN_LIST, duplicateTank.getName());
					}
					
				}
				for(Pair<Double, Double> quantity : quantities) {
					if(!(Math.abs(quantity.getKey() - quantity.getValue()) < MathUtils.DOUBLE_THRESHOLD)) {
						validations.put(Validation.H26_3_TRANSFERRED_QUANTITIES_NOT_EQUAL, new double[] {quantity.getKey(), quantity.getValue()});
					}
				}
			} catch(Exception e) {
				logger.error(e.getMessage(), e);
			}
		}

		if(quantityOfLubricatingOilCheckBox.isSelected()) {
//			List<Pair<Double, Double>> quantities = getOilOperationQuantities();
			List<Pair<Double, Double>> quantities = new ArrayList<>();
			Map<Integer, Pair<String, GridPane>> operations = getOperations(oilOperationGridPane, false);
			for(Integer operationIndex : operations.keySet()) {
				Pair<String, GridPane> operation = operations.get(operationIndex);
				String declaredQuantityString = parseOilOperation(operation.getKey()).get("quantity");
				if(declaredQuantityString==null) {
					validations.put(Validation.H26_4_MISSING_QUANTITY_OR_TYPE, operation.getKey());
					continue;
				}
				Double declaredQuantity = Double.parseDouble(declaredQuantityString);
				Double tanksQuantity = 0.0;
				List<Pair<String, Tank>> tankPairList = getTanks(operation.getValue());
				for(Pair<String,Tank> tankPair : tankPairList) {
					String currentTankQuantityString = parseTank(tankPair.getKey()).get("tankContent");
					if(currentTankQuantityString==null) {
						validations.put(Validation.H26_4_MISSING_TANK_DATA, tankPair.getKey());
						continue;
					}
					tanksQuantity = tanksQuantity + Double.parseDouble(currentTankQuantityString);
				}
				quantities.add(new Pair<Double, Double>(declaredQuantity, tanksQuantity));
				
				List<Pair<String, Tank>> notNullTanksTankPairList = tankPairList.stream().filter(t -> t.getValue() != null).collect(Collectors.toList());
				List<Tank> duplicates = notNullTanksTankPairList.stream()
						  .collect(Collectors.groupingBy(p -> p.getValue(), Collectors.counting()))
						  .entrySet().stream()
						  .filter(n -> n.getValue() > 1)
						  .map(n -> n.getKey())
						  .collect(Collectors.toList());
				for(Tank duplicateTank : duplicates) {
					validations.put(Validation.H26_4_TANK_ALREADY_IN_LIST, duplicateTank.getName());
				}
			}
			for(Pair<Double, Double> quantity : quantities) {
				if(!(Math.abs(quantity.getKey() - quantity.getValue()) < MathUtils.DOUBLE_THRESHOLD)) {
					validations.put(Validation.H26_4_TRANSFERRED_QUANTITIES_NOT_EQUAL, new double[] {quantity.getKey(), quantity.getValue()});
				}
			}
		}
	}

	public void showBalloon7(MouseEvent event) {
		ballonMessage.showGeneralBalloon((ImageViewButton) event.getTarget(), label7);
	}

	public void showBalloon8(MouseEvent event) {
		ballonMessage.showGeneralBalloon((ImageViewButton) event.getTarget(), label8);
	}

	public void actionPerformedH(MouseEvent event) {

		helpCodeLetterDialog.showDialog((Stage)((ImageViewButton)event.getSource()).getScene().getWindow(),helpImageList);

	}
	

	public void restoreLogGroup(List<OilLog> logGroup) {
		if(logGroup==null || logGroup.isEmpty()) return;
		List<OilLog> codeHOilLogs = logGroup.stream().filter(c -> c.getCodeLetter().equals("H")).collect(Collectors.toList());
		for (OilLog oilLog : codeHOilLogs) {
			if(oilLog.getCorrectionNote()!=null && oilLog.getCorrectionNote().equals("Corrected")) continue;
			String value;
			switch (oilLog.getCodeNumber()) {
				case "26.1":
					bunkeringCheckBox.setSelected(true);
					placeOfBunkeringCheckBox.setSelected(true);
					value = oilLog.getContent();
					placeOfBunkeringTextField.setText(value);
//					log.setContent(placeOfBunkeringTextField.getText());
					break;
				case "26.2":
					bunkeringCheckBox.setSelected(true);
					timeOfBunkeringCheckBox.setSelected(true);
					value = oilLog.getContent().replace("Start ", "");
					String startTime = value.substring(0, value.indexOf(" Stop "));
					value = value.replace(startTime+" Stop ", "");
					String stopTime = value;
					timeOfBunkeringStarttimeField.setLocalDateTime(LocalDateTime.parse(startTime, EasyDateUtils.logbookDateTimeFormatter));
					timeOfBunkeringStoptimeField.setLocalDateTime(LocalDateTime.parse(stopTime, EasyDateUtils.logbookDateTimeFormatter));
//					log.setContent("Start " + timeOfBunkeringStarttimeField.getLocalTime().toString() + " Stop " + timeOfBunkeringStoptimeField.getLocalTime().toString());
					break;
				case "26.3":
					bunkeringCheckBox.setSelected(true);
					quantityOfFuelCheckBox.setSelected(true);
					value = oilLog.getContent();
					String [] fuelOperations = value.split("\n\n");
					if(fuelOperations!=null && fuelOperations.length>0) fuelOperationGridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node)!=null && GridPane.getRowIndex(node) > 0);
					int fuelOperationIndex = 0;
					GridPane currentFuelGridPane = null;
					for(String operation : fuelOperations) {
						String fuelQuantity = operation.substring(0, operation.indexOf(" MT of fuel type "));
						operation = operation.replace(fuelQuantity+" MT of fuel type ", "");
						String fuelType = operation.substring(0, operation.indexOf(" bunkered in tanks.\n"));
						operation = operation.replaceFirst(fuelType+" bunkered in tanks.\n", "");
						String [] tanks = operation.split("\n");
						if(fuelOperationIndex==0) {
							quantityOfFuelDoubleField.setValue(Double.parseDouble(fuelQuantity));
							typeOfFuelTextField.setText(fuelType);
							currentFuelGridPane = fuelGridPane;
							fuelOperationIndex++;
						} else {
							boolean fuelOperation = true;
							Map<String, Node> nodes = addOperation(fuelOperationGridPane, fuelOperation);
							((DoubleField)nodes.get("quantity")).setValue(Double.parseDouble(fuelQuantity));
							((TextField)nodes.get("type")).setText(fuelType);
							currentFuelGridPane = (GridPane)nodes.get("gridPane");
							fuelOperationIndex++;
						}
						if(tanks!=null && tanks.length>0) currentFuelGridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node)!=null && GridPane.getRowIndex(node) > 2);
						Map<String, Node> firstRowNodesMap = getFirstRowNodes(currentFuelGridPane);
						int index2 = 0;
						for(String tank : tanks) {
							Map<String, String> map = parseTank(tank);
							if(index2==0) {
								List<Tank> userTanks = new ArrayList<Tank>();
								userTanks.add(getTankFromSettingsByName(map.get("tankName")));
								firstRowNodesMap.get("tankName").setUserData(userTanks);
								
								((JFXChipView<String>)firstRowNodesMap.get("tankName")).getChips().add(map.get("tankName"));
								((DoubleField)firstRowNodesMap.get("tankCapacity")).setValue(Double.parseDouble(map.get("tankCapacity")));
								((DoubleField)firstRowNodesMap.get("tankContent")).setValue(Double.parseDouble(map.get("tankContent")));
								index2++;
							} else {
								List<Tank> userTanks = (List<Tank>) firstRowNodesMap.get("tankName").getUserData();
								userTanks.add(getTankFromSettingsByName(map.get("tankName")));
								
								Map<String, Node> mapNodes = addTank(currentFuelGridPane);
								((JFXChipView<String>)mapNodes.get("tankName")).getChips().add(map.get("tankName"));
								((DoubleField)mapNodes.get("tankCapacity")).setValue(Double.parseDouble(map.get("tankCapacity")));
								((DoubleField)mapNodes.get("tankContent")).setValue(Double.parseDouble(map.get("tankContent")));
								index2++;
							}
						}
					}

//					log.setContent(quantityOfFuelDoubleField.getText() + " MT of fuel type " + typeOfFuelTextField.getText() + ...);
					break;
				case "26.4":
					bunkeringCheckBox.setSelected(true);
					quantityOfLubricatingOilCheckBox.setSelected(true);
					value = oilLog.getContent();
					String [] oilOperations = value.split("\n\n");
					if(oilOperations!=null && oilOperations.length>0) oilOperationGridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node)!=null && GridPane.getRowIndex(node) > 0);
					int oilOperationIndex = 0;
					GridPane currentOilGridPane = null;
					for(String operation : oilOperations) {
						String oilQuantity = operation.substring(0, operation.indexOf(" MT of oil type "));
						operation = operation.replace(oilQuantity+" MT of oil type ", "");
						String oilType = operation.substring(0, operation.indexOf("\n"));
						operation = operation.replaceFirst(oilType+"\n", "");
						String [] oilTanks = operation.split("\n");
						
						if(oilOperationIndex==0) {
							quantityOfLubricatingOilTextField.setValue(Double.parseDouble(oilQuantity));
							typeOflubricatingOilTextField.setText(oilType);
							currentOilGridPane = oilGridPane;
							oilOperationIndex++;
						} else {
							boolean fuelOperation = false;
							Map<String, Node> nodes = addOperation(oilOperationGridPane, fuelOperation);
							((DoubleField)nodes.get("quantity")).setValue(Double.parseDouble(oilQuantity));
							((TextField)nodes.get("type")).setText(oilType);
							currentOilGridPane = (GridPane)nodes.get("gridPane");
							oilOperationIndex++;
						}

						if(oilTanks!=null && oilTanks.length>0) currentOilGridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node)!=null && GridPane.getRowIndex(node) > 2);
						Map<String, Node> firstRowNodesMap = getFirstRowNodes(currentOilGridPane);
						int index3 = 0;
						for(String tank : oilTanks) {
							Map<String, String> map = parseTank(tank);
							if(index3==0) {
								List<Tank> userTanks = new ArrayList<Tank>();
								userTanks.add(getTankFromSettingsByName(map.get("tankName")));
								firstRowNodesMap.get("tankName").setUserData(userTanks);
								
								((JFXChipView<String>)firstRowNodesMap.get("tankName")).getChips().add(map.get("tankName"));
								((DoubleField)firstRowNodesMap.get("tankCapacity")).setValue(Double.parseDouble(map.get("tankCapacity")));
								((DoubleField)firstRowNodesMap.get("tankContent")).setValue(Double.parseDouble(map.get("tankContent")));
								index3++;
							} else {
								List<Tank> userTanks = (List<Tank>) firstRowNodesMap.get("tankName").getUserData();
								userTanks.add(getTankFromSettingsByName(map.get("tankName")));
								
								Map<String, Node> mapNodes = addTank(currentOilGridPane);
								((JFXChipView<String>)mapNodes.get("tankName")).getChips().add(map.get("tankName"));
								((DoubleField)mapNodes.get("tankCapacity")).setValue(Double.parseDouble(map.get("tankCapacity")));
								((DoubleField)mapNodes.get("tankContent")).setValue(Double.parseDouble(map.get("tankContent")));
								index3++;
							}
						}
					}

//					log.setContent(quantityOfLubricatingOilTextField.getValue() + " MT of oil type " + typeOflubricatingOilTextField.getText() + ...);
					break;
				default:
					break;
			}
		}
	}
	
	public void addFuelTank(Event event) {
		Map<String, Node> map = addTank(fuelGridPane);
		TankUsageContext tuc = new TankUsageContext(Stream.of(TankType.FUELOIL).collect(Collectors.toList()), true, false);
		tuc.setNoNeedForUserTankQuantities(true);
		map.get("tankName").setUserData(tuc);
		
		JFXChipView<String> tankNameJFXChipView = (JFXChipView<String>) map.get("tankName");
		DoubleField tankFillVolume = (DoubleField) map.get("tankContent");
		DoubleField tankRetainedVolume = (DoubleField) map.get("tankCapacity");
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
		
		addMaxValueCheck(tankNameJFXChipView, tankFillVolume, tankRetainedVolume);
		
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
	public void addOilTank(Event event) {
		Map<String, Node> map = addTank(oilGridPane);
		TankUsageContext tuc = new TankUsageContext(Stream.of(TankType.FUELOIL).collect(Collectors.toList()), true, false);
		tuc.setNoNeedForUserTankQuantities(true);
		map.get("tankName").setUserData(tuc);
		
		JFXChipView<String> tankNameJFXChipView = (JFXChipView<String>) map.get("tankName");
		DoubleField tankFillVolume = (DoubleField) map.get("tankContent");
		DoubleField tankRetainedVolume = (DoubleField) map.get("tankCapacity");
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
		
		addMaxValueCheck(tankNameJFXChipView, tankFillVolume, tankRetainedVolume);
		
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
		GridPane.setColumnSpan(tankNameJFXChipView, 2);
		tankNameJFXChipView.getStyleClass().add("logbookjfx-chip-view");
		ImageViewButton addTankImageViewButton = new ImageViewButton(addImageIdentityOfTanksLetterH);
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
		Label measureLabel1 = new Label("MT");
		measureLabel1.getStyleClass().add("logbookOtherLabel");
		Label measureLabel2 = new Label("MT");
		measureLabel2.getStyleClass().add("logbookOtherLabel");
		DoubleField tankCapacity = new DoubleField();
		DoubleField tankFillVolume = new DoubleField();
		MathUtils.setStringConverter(tankCapacity);
		MathUtils.setStringConverter(tankFillVolume);
		MathUtils.setMinMaxValues(tankCapacity, 0.0, null);
		MathUtils.setMinMaxValues(tankFillVolume, 0.0, null);
		tankNameJFXChipView.setId("tankName"+rows);
		tankCapacity.setId("tankCapacity"+rows);
		tankFillVolume.setId("tankContent"+rows);
		tankCapacity.getStyleClass().add("logbook-text-field");
		tankFillVolume.getStyleClass().add("logbook-text-field");
		tankFillVolume.setMaxWidth(200);
		JavaFXUtils.addFilledPseudoClass(tankCapacity);
		JavaFXUtils.addFilledPseudoClass(tankFillVolume);
		Region mouseTransparentRegion = new Region();
		mouseTransparentRegion.setMouseTransparent(true);
		tankGridPane.addRow(rows, new Region(), tankFillVolume,measureLabel1,addTankImageViewButton,tankNameJFXChipView, mouseTransparentRegion, tankCapacity, measureLabel2, deleteButton);
		tankGridPane.getRowConstraints().add(rows, new RowConstraints(50));
		
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
				tankNameJFXChipView.getChips().addListener(new ListChangeListener<String>() {
					@Override
					public void onChanged(Change<? extends String> change) {
						Double totalUserTankQuantities = ((TankUsageContext)tankNameJFXChipView.getUserData()).getTotalUserTankActualLevel();
						tankCapacity.setValue(totalUserTankQuantities + Optional.ofNullable(tankFillVolume.getValue()).orElse(0.0));
					}
				});
				tankFillVolume.valueProperty().addListener(new ChangeListener<Double>() {
					@Override
					public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
						Double totalUserTankQuantities = ((TankUsageContext)tankNameJFXChipView.getUserData()).getTotalUserTankActualLevel();
						tankCapacity.setValue(totalUserTankQuantities + Optional.ofNullable(newValue).orElse(0.0));
					}
				});
				JavaFXUtils.addTooltip(tankNameJFXChipView);
			}
		});
		addRemoveListener(tankNameJFXChipView);
		
		Map<String, Node> map = new HashMap<>();
		map.put("tankName", tankNameJFXChipView);
		map.put("tankCapacity", tankCapacity);
		map.put("tankContent", tankFillVolume);
		return map;
	}
	
	private List<Pair<Double, Double>> getFuelOperationQuantities() throws ParseException {
		List<Pair<Double, Double>> result = new ArrayList<>();
		Map<Integer, Pair<String, GridPane>> operations = getOperations(fuelOperationGridPane, true);
		for(Integer operationIndex : operations.keySet()) {
			Pair<String, GridPane> operation = operations.get(operationIndex);
			String declaredQuantityString = parseFuelOperation(operation.getKey()).get("quantity");
			if(declaredQuantityString==null) continue;
			Double declaredQuantity = MathUtils.decimalFormat.parse(declaredQuantityString).doubleValue();
			Double tanksQuantity = 0.0;
			for(Pair<String,Tank> tankPair : getTanks(operation.getValue())) {
				String currentTankQuantityString = parseTank(tankPair.getKey()).get("tankContent");
				if(currentTankQuantityString==null) continue;
				tanksQuantity = tanksQuantity + MathUtils.decimalFormat.parse(currentTankQuantityString).doubleValue();
			}
			result.add(new Pair<Double, Double>(declaredQuantity, tanksQuantity));
		}
		return result;
	}

	private List<Pair<Double, Double>> getOilOperationQuantities() throws ParseException {
		List<Pair<Double, Double>> result = new ArrayList<>();
		Map<Integer, Pair<String, GridPane>> operations = getOperations(oilOperationGridPane, false);
		for(Integer operationIndex : operations.keySet()) {
			Pair<String, GridPane> operation = operations.get(operationIndex);
			String declaredQuantityString = parseOilOperation(operation.getKey()).get("quantity");
			if(declaredQuantityString==null) continue;
			Double declaredQuantity = MathUtils.decimalFormat.parse(declaredQuantityString).doubleValue();
			Double tanksQuantity = 0.0;
			for(Pair<String,Tank> tankPair : getTanks(operation.getValue())) {
				String currentTankQuantityString = parseTank(tankPair.getKey()).get("tankContent");
				if(currentTankQuantityString==null) continue;
				tanksQuantity = tanksQuantity + MathUtils.decimalFormat.parse(currentTankQuantityString).doubleValue();
			}
			result.add(new Pair<Double, Double>(declaredQuantity, tanksQuantity));
		}
		return result;
	}	
		
	private Map<Integer, Pair<String, GridPane>> getOperations(GridPane operationGridPane, boolean fuelOperation) {
		final StringTemplate template = fuelOperation?fuelTemplate:oilTemplate;
		Map<Integer, Pair<String, GridPane>> operationsMap = new TreeMap<Integer, Pair<String, GridPane>>();
		for(Node operationChildNode : operationGridPane.getChildren()) {
			if(operationChildNode instanceof GridPane) {
				GridPane tanksGridPane = (GridPane)operationChildNode;
				for(Node childNode : tanksGridPane.getChildren()) {
					if((GridPane.getRowIndex(childNode) != null && GridPane.getRowIndex(childNode) == 0) || (childNode!=null && childNode.getId()!=null && childNode.getId().equals("quantityType26_4Hbox"))) {
						if(childNode instanceof DoubleField) {
							Double quantity = ((DoubleField)childNode).getValue();
							template.setAttribute("quantity", quantity!=null?MathUtils.decimalFormat.format(quantity):null);
						}
						if(childNode instanceof TextField && !(childNode instanceof DoubleField)) {
							template.setAttribute("type", ((TextField)childNode).getText());
						}
						if(childNode instanceof HBox && childNode.getId().equals("quantityType26_4Hbox")) {
							Node quantityNode = childNode.lookup("#quantity26_4DoubleField");
							if(quantityNode!=null) {
								Double quantity = ((DoubleField)quantityNode).getValue();
								template.setAttribute("quantity", quantity!=null?MathUtils.decimalFormat.format(quantity):null);
							}
							Node typeNode = childNode.lookup("#type26_4TextField");
							if(typeNode!=null) {
								template.setAttribute("type", ((TextField)typeNode).getText());
							}
						}
					}
				}
				operationsMap.put(GridPane.getRowIndex(tanksGridPane), new Pair<String, GridPane>(template.toString(), tanksGridPane));
				template.reset();
			}
		}
		
		return operationsMap;
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
	
		List<Pair<String, Tank>> tankList = new ArrayList<Pair<String, Tank>>();
		for(List<Node> tankNodeList : tankNodesMap.values()) {
			String name = "";
			Double capacity = 0.0;
			Double content = 0.0;
			Tank tank = null;
			boolean rowContainsTank = false;
			for(Node node : tankNodeList) {
				if(node instanceof JFXChipView<?> && node.getId()!=null && node.getId().startsWith("tankName")) {
					List<String> chipsList = ((JFXChipView<String>)node).getChips();
					if(!chipsList.isEmpty()) name = chipsList.get(0);
					Set<Tank> userTanks = ((TankUsageContext) ((JFXChipView<String>)node).getUserData()).getUserTankQuantities().keySet();
					if(userTanks!=null && !userTanks.isEmpty()) {
						tank = (Tank)userTanks.iterator().next();
					}
					template.setAttribute("tankName", name);
					rowContainsTank = true;
				}
				if(node instanceof DoubleField && node.getId()!=null && node.getId().startsWith("tankCapacity")) {
					capacity = ((DoubleField)node).getValue();
					template.setAttribute("tankCapacity", capacity!=null?MathUtils.decimalFormat.format(capacity):null);
					rowContainsTank = true;
				}
				if(node instanceof DoubleField && node.getId()!=null && node.getId().startsWith("tankContent")) {
					content = ((DoubleField)node).getValue();
					template.setAttribute("tankContent", content!=null?MathUtils.decimalFormat.format(content):null);
					rowContainsTank = true;
				}
			}
			if(!rowContainsTank) continue;
			boolean rowInvalid = false;
			if(capacity==null) {rowInvalid = true; logger.error("quantity from tank " + name + " must have a value");};
			if(content==null) {rowInvalid = true; logger.error("retained in tank " + name + " must have a value");};
			if(capacity!=null && content!=null && content>capacity) {rowInvalid = true; logger.error("quantity from tank " + name + " must be less than tank capacity");};
			if(rowInvalid) continue;
			tankList.add(new Pair(template.toString(), tank));
			template.reset();
		}
		return tankList;
	}
	
	
	public void showSelectTanker(Event event) {
		selectTankerDialog.showDialog((Stage) ((ImageViewButton) event.getSource()).getScene().getWindow(), tankNameJFXChipView1);
	}
	
	public void showSelectTanker2(Event event) {
		selectTankerDialog.showDialog((Stage) ((ImageViewButton) event.getSource()).getScene().getWindow(), tankNameOilJFXChipView1);
	}
			
	// example input: added to tankNameee with capacity 20.0 which now contains 10.0 M3
	private Map<String, String> parseTank(String tankString) {
		Map<String, String> map = new HashMap<>();
		Matcher matcher = pattern.matcher(tankString);
		if (matcher.find()) {
			map.put("tankContent", matcher.group(1));
			map.put("tankName", matcher.group(2));
			map.put("tankCapacity", matcher.group(3));
			logger.debug(matcher.group(1));
			logger.debug(matcher.group(2));
			logger.debug(matcher.group(3));
		} else {
			logger.debug("could not parse: " + tankString);
		}
		return map;
	}
	
	// example input: 3.000 MT OF FUEL TYPE FUELTYPEEE BUNKERED IN TANKS.
	private Map<String, String> parseFuelOperation(String fuelOperationString) {
		Map<String, String> map = new HashMap<>();
		Matcher matcher = fuelPattern.matcher(fuelOperationString);
		if (matcher.find()) {
			map.put("quantity", matcher.group(1));
			map.put("type", matcher.group(2));
			logger.debug(matcher.group(1));
			logger.debug(matcher.group(2));
		} else {
			logger.error("could not parse: " + fuelOperationString);
		}
		return map;
	}

	// example input: 2.000 MT OF OIL TYPE OILTYPEEE BUNKERED IN TANKS.
	private Map<String, String> parseOilOperation(String oilOperationString) {
		Map<String, String> map = new HashMap<>();
		Matcher matcher = oilPattern.matcher(oilOperationString);
		if (matcher.find()) {
			map.put("quantity", matcher.group(1));
			map.put("type", matcher.group(2));
			logger.debug(matcher.group(1));
			logger.debug(matcher.group(2));
		} else {
			logger.error("could not parse: " + oilOperationString);
		}
		return map;
	}

	public void clearFilledInfo() {
		bunkeringCheckBox.setSelected(false);
		placeOfBunkeringCheckBox.setSelected(false);
		timeOfBunkeringCheckBox.setSelected(false);
		quantityOfFuelCheckBox.setSelected(false);
		quantityOfLubricatingOilCheckBox.setSelected(false);
		
		
		placeOfBunkeringTextField.clear();
		timeOfBunkeringStarttimeField.setLocalDateTime(null);
		timeOfBunkeringStoptimeField.setLocalDateTime(null);
		
		quantityOfFuelDoubleField.clear();
		typeOfFuelTextField.clear();

		
		quantityOfLubricatingOilTextField.clear();
		typeOflubricatingOilTextField.clear();

		bdn261TextField.clear();
		bdn261TextField.setUserData(null);
		bdn264TextField.clear();
		bdn264TextField.setUserData(null);
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
	
	
	
	
	public void addFuelOperation(Event event) {
		boolean fuelOperation = true;
		addOperation(fuelOperationGridPane, fuelOperation);
	}
	public void addOilOperation(Event event) {
		boolean fuelOperation = false;
		addOperation(oilOperationGridPane, fuelOperation);
	}
	

	private Map<String, Node> addOperation(GridPane operationGridPane, boolean fuelOperation) {
		try {

			GridPane newOperationGridPane = new GridPane();
			Method method = operationGridPane.getClass().getDeclaredMethod("getNumberOfRows");
			method.setAccessible(true);
			Integer rows = (Integer) method.invoke(operationGridPane);
			
			
			Label quantityLabel = fuelOperation?new Label("Quantity of fuel"):new Label("Quantity of lubricating oil");
			quantityLabel.getStyleClass().add("logbookOtherLabel");
			DoubleField quantityDoubleField = new DoubleField();
			MathUtils.setStringConverter(quantityDoubleField);
			MathUtils.setMinMaxValues(quantityDoubleField, 0.0, null);
			quantityDoubleField.getStyleClass().add("logbook-text-field");
			JavaFXUtils.addFilledPseudoClass(quantityDoubleField);
			Label mtLabel = new Label("MT");
			mtLabel.getStyleClass().add("logbookOtherLabel");
			Label typeLabel = fuelOperation?new Label("and Type of fuel"):new Label("Type of Lubricating Oil");
			typeLabel.getStyleClass().add("logbookOtherLabel");
			TextField typeTextField = new TextField();
			typeTextField.getStyleClass().add("logbook-text-field");
			typeTextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
			JavaFXUtils.addFilledPseudoClass(typeTextField);
			Button deleteButton = new Button("X");
			deleteButton.setStyle("-fx-background-color: #ef7f2e");
			deleteButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					Button deleteButton = (Button)event.getSource();
					GridPane operationGridPane = (GridPane)deleteButton.getParent();
					Integer rowIndex = GridPane.getRowIndex(operationGridPane);
					((GridPane)operationGridPane.getParent()).getChildren().removeIf(node -> GridPane.getRowIndex(node) == rowIndex);				
				}
			});
			deleteButton.setOnMouseEntered(mouseEvent -> {if(!mouseEvent.isPrimaryButtonDown()) deleteButton.setCursor(Cursor.HAND);});
			deleteButton.setOnMouseExited(mouseEvent -> {if(!mouseEvent.isPrimaryButtonDown()) deleteButton.setCursor(Cursor.DEFAULT);});
			
			newOperationGridPane.addRow(0, new Region(), quantityLabel, new Region(), quantityDoubleField, mtLabel, typeLabel, typeTextField, new Region(), deleteButton);
			

			
			Label quantityAddedLabel = new Label("(state quantity added, in tonnes");
			quantityAddedLabel.getStyleClass().add("logbookOtherLabel");
			Label identityOfTanksLabel = new Label("identity of tank(s");
			identityOfTanksLabel.getStyleClass().add("logbookOtherLabel");
			Label totalContentOfTanks = new Label("and total Content of tank(s)).");
			totalContentOfTanks.getStyleClass().add("logbookOtherLabel");
			Button addTankButton = new Button("ADD Tank");
			addTankButton.getStyleClass().add("logbookaddTankButton");
			addTankButton.setId("addTankButton");
			addTankButton.setOnAction(actionEvent -> {
				Map<String, Node> map = addTank(newOperationGridPane);
//				if(fuelOperation)
//				else
				TankUsageContext tuc = new TankUsageContext(Stream.of(TankType.FUELOIL).collect(Collectors.toList()), true, false);
				tuc.setNoNeedForUserTankQuantities(true);
				JFXChipView<String> tankNameJFXChipView = (JFXChipView<String>) map.get("tankName");
				DoubleField tankFillVolume = (DoubleField) map.get("tankContent");
				DoubleField tankRetainedVolume = (DoubleField) map.get("tankCapacity");
				tankNameJFXChipView.setUserData(tuc);
				
				addMaxValueCheck(tankNameJFXChipView, tankFillVolume, tankRetainedVolume);
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
			});
			addTankButton.setOnMouseEntered(mouseEvent -> {if(!mouseEvent.isPrimaryButtonDown()) addTankButton.setCursor(Cursor.HAND);});
			addTankButton.setOnMouseExited(mouseEvent -> {if(!mouseEvent.isPrimaryButtonDown()) addTankButton.setCursor(Cursor.DEFAULT);});
			
			newOperationGridPane.addRow(1, new Region(), quantityAddedLabel, new Region(), identityOfTanksLabel, new Region(), new Region(), totalContentOfTanks, new Region(), addTankButton);
			
			JFXChipView<String> tankNameJFXChipView = new JFXChipView<String>();
			tankNameJFXChipView.getStyleClass().add("logbookjfx-chip-view");
//			if(fuelOperation)
//			else
			TankUsageContext tuc = new TankUsageContext(Stream.of(TankType.FUELOIL).collect(Collectors.toList()), true, false);
			tuc.setNoNeedForUserTankQuantities(true);
			tankNameJFXChipView.setUserData(tuc);
			GridPane.setColumnSpan(tankNameJFXChipView, 2);
			ImageViewButton addTankImageViewButton = new ImageViewButton(addImageIdentityOfTanksLetterH);
			addTankImageViewButton.getStyleClass().add("image-view-button");
			addTankImageViewButton.getStyleClass().add("logbookButtonAddTank");
			GridPane.setHalignment(addTankImageViewButton, HPos.CENTER);
			addTankImageViewButton.setOnMouseClicked(new EventHandler<MouseEvent>() {				
				@Override
				public void handle(MouseEvent event) {
					selectTankerDialog.showDialog((Stage) ((ImageViewButton) event.getSource()).getScene().getWindow(), tankNameJFXChipView);				
				}
			});
			Label measureLabel1 = new Label("MT");
			measureLabel1.getStyleClass().add("logbookOtherLabel");
			Label measureLabel2 = new Label("MT");
			measureLabel2.getStyleClass().add("logbookOtherLabel");
			
			

			DoubleField tankCapacity = new DoubleField();
			DoubleField tankFillVolume = new DoubleField();
			MathUtils.setStringConverter(tankCapacity);
			MathUtils.setStringConverter(tankFillVolume);
			MathUtils.setMinMaxValues(tankCapacity, 0.0, null);
			MathUtils.setMinMaxValues(tankFillVolume, 0.0, null);
			tankNameJFXChipView.setId("tankName"+rows);
			tankCapacity.setId("tankCapacity"+rows);
			tankFillVolume.setId("tankContent"+rows);
			tankCapacity.getStyleClass().add("logbook-text-field");
			tankFillVolume.getStyleClass().add("logbook-text-field");
			tankFillVolume.setMaxWidth(200);
			JavaFXUtils.addFilledPseudoClass(tankCapacity);
			JavaFXUtils.addFilledPseudoClass(tankFillVolume);
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
						Double totalUserTankQuantities = ((TankUsageContext)tankNameJFXChipView.getUserData()).getTotalUserTankActualLevel();
						tankCapacity.setValue(totalUserTankQuantities + Optional.ofNullable(tankFillVolume.getValue()).orElse(0.0));
					});
					JavaFXUtils.addTooltip(tankNameJFXChipView);
				}
			});
			tankFillVolume.valueProperty().addListener(new ChangeListener<Double>() {
				@Override
				public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
					Double totalUserTankQuantities = ((TankUsageContext)tankNameJFXChipView.getUserData()).getTotalUserTankActualLevel();
					tankCapacity.setValue(totalUserTankQuantities + Optional.ofNullable(newValue).orElse(0.0));
				}
			});
			
			addMaxValueCheck(tankNameJFXChipView, tankFillVolume, tankCapacity);
			
			addRemoveListener(tankNameJFXChipView);
			Region mouseTransparentRegion = new Region();
			mouseTransparentRegion.setMouseTransparent(true);
			newOperationGridPane.addRow(2, new Region(), tankFillVolume,measureLabel1,addTankImageViewButton,tankNameJFXChipView, mouseTransparentRegion, tankCapacity, measureLabel2, new Region());
			
			
			newOperationGridPane.setGridLinesVisible(false);
			newOperationGridPane.setPadding(new Insets(30,0,0,0));
			newOperationGridPane.setHgap(10);
			newOperationGridPane.setVgap(10);
			ColumnConstraints cc1 = new ColumnConstraints(330.0, Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
			ColumnConstraints cc2 = new ColumnConstraints(100.0, Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
			ColumnConstraints cc3 = new ColumnConstraints(100.0, Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
			ColumnConstraints cc4 = new ColumnConstraints(100.0, Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
			ColumnConstraints cc5 = new ColumnConstraints(150.0, Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
			ColumnConstraints cc6 = new ColumnConstraints(200.0, Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
			ColumnConstraints cc7 = new ColumnConstraints(60.0, Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
			ColumnConstraints cc8 = new ColumnConstraints(100.0, Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
			
			cc1.setHalignment(HPos.CENTER); cc1.setHgrow(Priority.SOMETIMES);
			cc2.setHgrow(Priority.SOMETIMES);
			cc3.setHalignment(HPos.CENTER); cc3.setHgrow(Priority.SOMETIMES);
			cc4.setHgrow(Priority.SOMETIMES);
			cc5.setHalignment(HPos.CENTER); cc5.setHgrow(Priority.SOMETIMES);
			cc6.setHgrow(Priority.SOMETIMES);
			cc7.setHgrow(Priority.SOMETIMES);
			cc8.setHgrow(Priority.SOMETIMES);
			newOperationGridPane.getColumnConstraints().addAll(new ColumnConstraints( 20 ),
															cc1,
															cc2,
															cc3,
															cc4,
															cc5,
															cc6,
															cc7,
															cc8
															);
			newOperationGridPane.getRowConstraints().addAll(new RowConstraints(30.0, 30, Control.USE_COMPUTED_SIZE),
															new RowConstraints(30.0, 30, Control.USE_COMPUTED_SIZE),
															new RowConstraints(50.0, 30, Control.USE_COMPUTED_SIZE));
			
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
			
			operationGridPane.addRow(rows, newOperationGridPane);
			Map<String, Node> map = new HashMap<>();
			map.put("gridPane", newOperationGridPane);
			map.put("quantity", quantityDoubleField);
			map.put("type", typeTextField);
			return map;
			
			
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
	}


	public void addMaxValueCheck(JFXChipView<String> tankNameJFXChipView, DoubleField tankFillVolume,
			DoubleField tankRetainedVolume) {
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
	}
	
	
	private Tank getTankFromSettingsByName(String tankName) {
		Tank tank = operationLogService.getTankByName(tankName);
		if(tank==null) logger.error("Not found SlopTank with name " + tankName);
		return tank;
	}
	
	private Map<String, Node> getFirstRowNodes(GridPane gridPane) {
		JFXChipView<String> tankNameJFXChipView = null;
		DoubleField tankCapacity = null;
		DoubleField tankFillVolume = null;
		for(Node childNode : gridPane.getChildren()) {
			if(GridPane.getRowIndex(childNode) != null) {
				if(childNode instanceof JFXChipView<?> && childNode.getId()!=null && childNode.getId().startsWith("tankName")) {
					tankNameJFXChipView = (JFXChipView<String>) childNode;
				}
				if(childNode instanceof DoubleField && childNode.getId()!=null && childNode.getId().startsWith("tankCapacity")) {
					tankCapacity = (DoubleField) childNode;
				}
				if(childNode instanceof DoubleField && childNode.getId()!=null && childNode.getId().startsWith("tankContent")) {
					tankFillVolume = (DoubleField) childNode;
				}
			}
		}
		Map<String, Node> map = new HashMap<>();
		map.put("tankName", tankNameJFXChipView);
		map.put("tankCapacity", tankCapacity);
		map.put("tankContent", tankFillVolume);
		return map;
	}
}
