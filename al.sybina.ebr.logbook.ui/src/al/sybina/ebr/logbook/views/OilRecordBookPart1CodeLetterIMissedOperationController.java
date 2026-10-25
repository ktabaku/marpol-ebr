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
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

import org.antlr.stringtemplate.StringTemplate;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.tools.resources.WorkbenchResourceProvider;
import org.eclipse.e4.tools.services.IResourceProviderService;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;
import org.eclipse.fx.ui.services.resources.ImageProvider;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXChipView;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.Tank;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.TankType;
import com.oceanpro.utils.EasyDateUtils;

import al.sybina.ebr.commons.utils.JavaFXUtils;
import al.sybina.ebr.commons.utils.MathUtils;
import al.sybina.ebr.logbook.commons.Activator;
import al.sybina.ebr.logbook.commons.BallonMessage;
import al.sybina.ebr.logbook.dialogs.SelectTankerDialog;
import al.sybina.ebr.logbook.dialogs.TankUsageContext;
import al.sybina.ebr.logbook.domain.reladomo.OilLog;
import al.sybina.ebr.logbook.views.OilRecordBookPart1ViewController.Validation;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import jfxtras.scene.control.ImageViewButton;
import jfxtras.scene.control.LocalDateTimeTextField;
import jidefx.scene.control.field.DoubleField;

public class OilRecordBookPart1CodeLetterIMissedOperationController implements Initializable {

	private Label label1 = new Label(" Notes:\r\n" + "This entry should be made only in exceptional cases\r\n" + "Date ‘1’ to be the date when the original operation was carried out.\r\n"
			+ "Date '2’ to be the current date i.e. the date when the entry is made.\r\n" + "Signed ‘1’:Signature of Officer making the “O” entry\r\n" + "Signed ‘2’:Signature of Officer making the missed entry\r\n" + "");
	private Label label2 = new Label("Note \r\n" + "Date ‘1’ to be the date when the original operation \r\n" + "was carried out\r\n" + "");
	private static final String option0 = "Bilge Water Separator Sealed";
	private static final String option1 = "Bilge Water Separator Unsealed";
	private static final String option2 = "De-bunkering of fuel oil (in case of out-of-spec fuel)";
//	private static final String option3 = "Entry pertaining to an earlier missed operational entry";// no needed in case we keep it the system goes in the cycle
	private static final String option4 = "Other";
	private static final String option5 = "Weekly Inventory of Bilge Water Tanks (listed under item 3.3";
	private static final Logger logger = LoggerCreator.createLogger(OilRecordBookPart1CodeLetterIController.class);
	protected String CACHE_FILE_NAME = System.getProperty("user.home") + "/.ebr/caches/caches.properties";
	private BallonMessage ballonMessage;
	private IResourceProviderService resourcesService;
	private ImageProvider imageProvider;
	private SelectTankerDialog selectTankerDialog;
	private IEventBroker eventBroker;
	@FXML
	private JFXChipView<String> tankNameJFXChipView;
	@FXML
	private DoubleField tankQuantityDoubleField;
	@FXML
	private DoubleField tankRetainedDoubleField;
	@FXML
	private Button IdentityOfTanksLetterI;
	@FXML
	private ImageView imageViewAddIdentityOfTanksLetterI;
	@FXML
	private Label selectOperation;
	@FXML
	private Label comment;
	@FXML
	private ImageView imageViewHelpI;
	@FXML
	private ImageView imageViewHelpI2;
	@FXML
	private ImageView imageViewHelpI3;
	@FXML
	private ImageView imageViewHelpI4;
	@FXML
	private ImageView imageViewHelpI5;
	@FXML
	private ImageView imageViewHelpI6;
	@FXML
	private ImageViewButton imageViewButtonInfoLetterI;
	@FXML
	private CheckBox selectOperationCheckBox;
	@FXML
	private TextField overboardValve;
	@FXML
	private TextField overboardEquipment;
	@FXML
	private TextField sealNumber;
	@FXML
	private TextField sealNumberEquipment;
	@FXML
	private TextField debunkeringToTextField;
	@FXML
	private TextField debunkeringPlaceOfTextField;
	@FXML
	private TextField receipNoTextField;
	@FXML
	private ImageViewButton uploadReceiptImageViewButton;
	@FXML
	private TextField tankFuelType;
	@FXML
	private TextArea commentTextArea;
	@FXML
	private TextField reasonTextField;
	@FXML
	private LocalDateTimeTextField setTimeAtStart;
	@FXML
	private LocalDateTimeTextField setTimeEnd;
	@FXML
	private ComboBox<String> comboBox;
	@FXML
	private VBox VBoxCaso0;
	@FXML
	private VBox VBoxCaso1;
	@FXML
	private VBox VBoxCaso2;
	@FXML
	private VBox VBoxCaso4;
	@FXML
	private VBox VBoxCaso5;
	@FXML
	private GridPane fuelTanksGridPane;
	@FXML
	private GridPane commentsGridPane;
	@FXML
	private ImageViewButton identityOfTanksImageViewButton;
	@FXML
	private ImageViewButton addIdentityOfTanksLetterI4ImageViewButton;
	private TextField currentSelectedTextField;
	private Image addImageIdentityOfTanksLetterI;
	@FXML
	private ImageViewButton helpLetterIImageViewButton1;
	@FXML
	private Button signatureMissedCodeLetterButton;
	@FXML
	private CheckBox weeklyInventoryCheckBox;
	@FXML
	private CheckBox identityOfTankCheckBox;
	@FXML
	private CheckBox capacityOfTanksCheckBox;
	@FXML
	private DoubleField capacityOfTanksInM3DoubleField;
	@FXML
	private CheckBox totalQuantityOfRetentionCheckBox;
	@FXML
	private DoubleField totalQuantityOfRetentionInM3DoubleField;
	@FXML
	private DoubleField quantityOfResidueCollectedByManualOperationInM3DoubleField;
	@FXML
	private ImageViewButton identityOfTanksLetterIImageViewButton;
	@FXML
	private JFXChipView<String> identityOfTanksLetterI1JFXChipView;
	@FXML
	private JFXChipView<String> identityOfTanksLetterI4JFXChipView;
	@FXML
	private CheckBox quantityOfResidueCollectedCheckBox;
	@FXML
	private AnchorPane missedIAnchorPane;

	private static final StringTemplate typeFillTemplate = new StringTemplate("$totalFillVolume$ M3 OF $tankFuelType$ DE-BUNKERED FROM TANKS");
	private static final StringTemplate tankFillTemplate = new StringTemplate("$tankFillVolume$ M3 REMOVED FROM $tankName$ WHICH NOW CONTAINS $tankContent$ M3");
	private static final StringTemplate debunkeringTemplate = new StringTemplate("DEBUNKERING TO THE $debunkeringTo$ AT PLACE OF $debunkeringPlaceOf$ - RECEIPT FILE '$receiptFile$'");
	private static final Pattern tankFillPattern = Pattern.compile("(.{1,}) M3 REMOVED FROM (.{1,}) WHICH NOW CONTAINS (.{1,}) M3");
	private static final Pattern overboardValvePattern = Pattern.compile("OVERBOARD VALVE (.{1,}) FROM 15PPM BILGE WATER SEPARATOR SEALED. SEAL NO (.{1,})");
	private static final Pattern overboardValveUnsealPattern = Pattern.compile("OVERBOARD VALVE (.{1,}) FROM WATER SEPARATOR WAS UNSEALED, FOR NORMAL OPERATION OF THE 15PPM BILGE WATER SEPARATOR. SEAL NO (.{1,})");
	private static final Pattern debunkeringPattern = Pattern.compile("DEBUNKERING TO THE (.{1,}) AT PLACE OF (.{1,}) - RECEIPT FILE '(.{1,})(?s:.{0,})'");
	private static JFXButton closeButton;
	private DateTimeFormatter dateTimeFormatter = EasyDateUtils.logbookPdfDateTimeFormatter;
	private boolean emptyFuelType=false;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MathUtils.setStringConverter(tankQuantityDoubleField);
		MathUtils.setStringConverter(tankRetainedDoubleField);
		MathUtils.setStringConverter(capacityOfTanksInM3DoubleField);
		MathUtils.setStringConverter(totalQuantityOfRetentionInM3DoubleField);
		MathUtils.setStringConverter(quantityOfResidueCollectedByManualOperationInM3DoubleField);
		MathUtils.setMinMaxValues(tankQuantityDoubleField, 0.0, null);
		MathUtils.setMinMaxValues(tankRetainedDoubleField, 0.0, null);
		MathUtils.setMinMaxValues(capacityOfTanksInM3DoubleField, 0.0, null);
		MathUtils.setMinMaxValues(totalQuantityOfRetentionInM3DoubleField, 0.0, null);
		MathUtils.setMinMaxValues(quantityOfResidueCollectedByManualOperationInM3DoubleField, 0.0, null);
		try {
			SVGGlyph timesGlyph = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg.times-circle");
			closeButton = new JFXButton();
			closeButton.getStyleClass().add("closeDialogButton");
			closeButton.setMinWidth(40);
			closeButton.setPrefWidth(40);
			closeButton.setMaxWidth(40);
			closeButton.setMinHeight(32);
			closeButton.setPrefHeight(32);
			closeButton.setMaxHeight(32);
			closeButton.setGraphic(timesGlyph);
			closeButton.ripplerFillProperty().setValue(Color.LIGHTSALMON);
			closeButton.setOnAction(event -> {
				((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
			});
			Tooltip.install(closeButton, new Tooltip("Exit"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setTimeAtStart.setDisable(false);
		setTimeEnd.setDisable(false);
		setTimeAtStart.setDateTimeFormatter(dateTimeFormatter);
		setTimeEnd.setDateTimeFormatter(dateTimeFormatter);
		overboardValve.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		overboardEquipment.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		sealNumber.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		sealNumberEquipment.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		tankFuelType.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		debunkeringToTextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		debunkeringPlaceOfTextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		commentTextArea.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		reasonTextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		JavaFXUtils.addFilledPseudoClass(overboardValve);
		JavaFXUtils.addFilledPseudoClass(overboardEquipment);
		JavaFXUtils.addFilledPseudoClass(sealNumber);
		JavaFXUtils.addFilledPseudoClass(sealNumberEquipment);
		JavaFXUtils.addFilledPseudoClass(tankQuantityDoubleField);
		JavaFXUtils.addFilledPseudoClass(tankFuelType);
		JavaFXUtils.addFilledPseudoClass(tankRetainedDoubleField);
		JavaFXUtils.addFilledPseudoClass(debunkeringToTextField);
		JavaFXUtils.addFilledPseudoClass(debunkeringPlaceOfTextField);
		JavaFXUtils.addFilledPseudoClass(receipNoTextField);
		JavaFXUtils.addFilledPseudoClass(commentTextArea);
		JavaFXUtils.addFilledPseudoClass(reasonTextField);
		JavaFXUtils.addFilledPseudoClass(capacityOfTanksInM3DoubleField);
		JavaFXUtils.addFilledPseudoClass(totalQuantityOfRetentionInM3DoubleField);
		JavaFXUtils.addFilledPseudoClass(quantityOfResidueCollectedByManualOperationInM3DoubleField);
		commentTextArea.setPrefRowCount(10);
		Platform.runLater(() -> {
		try {
			Image infoImageLetterI = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
			imageViewButtonInfoLetterI.setImage(infoImageLetterI);

			addImageIdentityOfTanksLetterI = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_ADD));
			identityOfTanksImageViewButton.setImage(addImageIdentityOfTanksLetterI);
			identityOfTanksLetterIImageViewButton.setImage(addImageIdentityOfTanksLetterI);
			addIdentityOfTanksLetterI4ImageViewButton.setImage(addImageIdentityOfTanksLetterI);
			identityOfTanksImageViewButton.setStyle("-fx-effect: dropshadow(three-pass-box, grey, 3, 0, 3, 3);");
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
					receipNoTextField.setText(selectedFile.getName());
					receipNoTextField.setUserData(selectedFile);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		});
		comboBox.getItems().addAll(option0, option1, option2, option4, option5);
		comboBox.setValue("");
		comboBox.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String output = (String) comboBox.getValue();
				String action = comboBox.getValue();
				overboardValve.setText("");
				sealNumber.setText("");
				overboardEquipment.setText("");
				sealNumberEquipment.setText("");
				commentTextArea.setText("");
				reasonTextField.setText("");
				tankFuelType.setText("");
				debunkeringToTextField.setText("");
				debunkeringPlaceOfTextField.setText("");
				setTimeAtStart.setLocalDateTime(null);
				setTimeEnd.setLocalDateTime(null);
				receipNoTextField.setText("");
				tankQuantityDoubleField.setValue(null);
				tankRetainedDoubleField.setValue(null);
				tankNameJFXChipView.getChips().clear();
				weeklyInventoryCheckBox.setSelected(false);
				identityOfTanksLetterI1JFXChipView.getChips().clear();
				identityOfTanksLetterI4JFXChipView.getChips().clear();
				fuelTanksGridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node)!=null && GridPane.getRowIndex(node) > 1);
				if (comboBox.getValue().equals(option0)) {
					VBoxCaso0.setVisible(true);
					VBoxCaso1.setVisible(false);
					VBoxCaso2.setVisible(false);
					VBoxCaso4.setVisible(false);
					VBoxCaso5.setVisible(false);
				}
				if (comboBox.getValue().equals(option1)) {
					VBoxCaso0.setVisible(false);
					VBoxCaso1.setVisible(true);
					VBoxCaso2.setVisible(false);
					VBoxCaso4.setVisible(false);
					VBoxCaso5.setVisible(false);
				}
				if (comboBox.getValue().equals(option2)) {
					VBoxCaso0.setVisible(false);
					VBoxCaso1.setVisible(false);
					VBoxCaso2.setVisible(true);
					VBoxCaso4.setVisible(false);
					VBoxCaso5.setVisible(false);
				}
				if (comboBox.getValue().equals(option4)) {
					VBoxCaso0.setVisible(false);
					VBoxCaso1.setVisible(false);
					VBoxCaso2.setVisible(false);
					VBoxCaso4.setVisible(true);
					VBoxCaso5.setVisible(false);
					commentTextArea.setDisable(false);
					commentTextArea.setPrefRowCount(5);
				}
				
				if (comboBox.getValue().equals(option5)) {
					VBoxCaso0.setVisible(false);
					VBoxCaso1.setVisible(false);
					VBoxCaso2.setVisible(false);
					VBoxCaso4.setVisible(false);
					VBoxCaso5.setVisible(true);
				} 
			}
		});
		
		selectOperationCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				// codeLetterI
				if (!comboBox.isDisabled()) {
					comboBox.setPromptText("");
					VBoxCaso0.setVisible(false);
					VBoxCaso1.setVisible(false);
					VBoxCaso2.setVisible(false);
					VBoxCaso4.setVisible(false);
					VBoxCaso5.setVisible(false);
				} else {
					switch(comboBox.getValue()) {
						case option0:
							VBoxCaso0.setVisible(true);
							break;
						case option1:
							VBoxCaso1.setVisible(true);
							break;
						case option2:
							VBoxCaso2.setVisible(true);
							break;
						case option4:
							VBoxCaso4.setVisible(true);
							break;
						case option5:
							VBoxCaso5.setVisible(true);
							break;
					}
				}
				if(newValue) {
					comboBox.setDisable(false);
				} else {
					comboBox.setDisable(true);
					clearFilledInfo();
					comboBox.getSelectionModel().select("");
				}
			}
		});
		
		weeklyInventoryCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					identityOfTankCheckBox.setDisable(false);
					capacityOfTanksCheckBox.setDisable(false);
					totalQuantityOfRetentionCheckBox.setDisable(false);
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
		TankUsageContext tucI = new TankUsageContext(Stream.of(TankType.OILYBILGE).collect(Collectors.toList()), false, true);
		tucI.setC11_1(true);//code letter I1.1 Weekly Inventory similar as code letter C11.1 but different tanks
		identityOfTanksLetterI1JFXChipView.setUserData(tucI);
		addRemoveListener(identityOfTanksLetterI1JFXChipView);
		identityOfTanksLetterI1JFXChipView.getChips().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> c) {
		        while (c.next()) {
		            for (String item : c.getAddedSubList()) {
		            	if(identityOfTanksLetterI1JFXChipView.getChips().size()>1) {
		            		String oldChip = identityOfTanksLetterI1JFXChipView.getChips().get(0);
		            		Platform.runLater(() -> identityOfTanksLetterI1JFXChipView.getChips().remove(oldChip));
		            	}
		            }
		        }
		        
				double totalCapacity = ((TankUsageContext) identityOfTanksLetterI1JFXChipView.getUserData()).getTotalUserTankCapacity();
				double actualLevelSum = ((TankUsageContext) identityOfTanksLetterI1JFXChipView.getUserData()).getTotalUserTankQuantities();
				
				if(actualLevelSum>0.0){
					quantityOfResidueCollectedCheckBox.setDisable(false);
					((TankUsageContext)identityOfTanksLetterI4JFXChipView.getUserData()).setMaxC11_4(actualLevelSum);
				}else {
					quantityOfResidueCollectedCheckBox.setDisable(true);
					quantityOfResidueCollectedCheckBox.setSelected(false);
					((TankUsageContext)identityOfTanksLetterI4JFXChipView.getUserData()).setMaxC11_4(0.0);
				}
				
				if(!identityOfTanksLetterI1JFXChipView.isDisable()) {
					capacityOfTanksCheckBox.setSelected(true);
					capacityOfTanksInM3DoubleField.setValue(totalCapacity);
					totalQuantityOfRetentionCheckBox.setSelected(true);
					totalQuantityOfRetentionInM3DoubleField.setValue(actualLevelSum);
				}
				
				JavaFXUtils.addTooltip(identityOfTanksLetterI1JFXChipView);
			}
		});
		TankUsageContext tuc3 = new TankUsageContext(Stream.of(TankType.OILYBILGE).collect(Collectors.toList()), false, true);
		tuc3.setC11_4(true);//code letter I1.4 Weekly Inventory similar as code letter C11.4 but different tanks
		tuc3.setMaxC11_4(totalQuantityOfRetentionInM3DoubleField.getValue());
		identityOfTanksLetterI4JFXChipView.setUserData(tuc3);
		addRemoveListener(identityOfTanksLetterI4JFXChipView);
		identityOfTanksLetterI4JFXChipView.getChips().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> c) {
				Double userTankQuantitiesSum = ((TankUsageContext) identityOfTanksLetterI4JFXChipView.getUserData()).getTotalUserTankQuantities();
				quantityOfResidueCollectedByManualOperationInM3DoubleField.setValue(userTankQuantitiesSum);
				JavaFXUtils.addTooltip(identityOfTanksLetterI4JFXChipView);
			}
		});
		quantityOfResidueCollectedCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					identityOfTanksLetterI4JFXChipView.setDisable(false);
					addIdentityOfTanksLetterI4ImageViewButton.setDisable(false);
				} else {
					identityOfTanksLetterI4JFXChipView.setDisable(true);
					addIdentityOfTanksLetterI4ImageViewButton.setDisable(true);
				}
				identityOfTanksLetterI4JFXChipView.getChips().clear();
				quantityOfResidueCollectedByManualOperationInM3DoubleField.setValue(null);
			}
		});
		identityOfTankCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {	
				if(newValue) {
					identityOfTanksLetterI1JFXChipView.setDisable(false);
					identityOfTanksLetterIImageViewButton.setDisable(false);
				} else {
					identityOfTanksLetterI1JFXChipView.setDisable(true);
					identityOfTanksLetterIImageViewButton.setDisable(true);
				}
				identityOfTanksLetterI1JFXChipView.getChips().clear();
			}

		});
		capacityOfTanksCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (identityOfTanksLetterI1JFXChipView.getUserData()!=null && newValue) {
					double totalCapacity = ((TankUsageContext) identityOfTanksLetterI1JFXChipView.getUserData()).getTotalUserTankCapacity();
					capacityOfTanksInM3DoubleField.setValue(totalCapacity);
				}else {
					capacityOfTanksInM3DoubleField.setValue(null);	
				}
			}
		});
		totalQuantityOfRetentionCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (identityOfTanksLetterI1JFXChipView.getUserData()!=null && newValue) {
					double actualLevelSum = ((TankUsageContext) identityOfTanksLetterI1JFXChipView.getUserData()).getTotalUserTankQuantities();
					totalQuantityOfRetentionInM3DoubleField.setValue(actualLevelSum);	
				}else {
					totalQuantityOfRetentionInM3DoubleField.setValue(null);	
				}
				
			}
		});
		TankUsageContext tuc = new TankUsageContext(Stream.of(TankType.FUELOIL).collect(Collectors.toList()), false, false);
		tuc.setNoNeedForUserTankQuantities(true);
		tankNameJFXChipView.setUserData(tuc);
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
					tankRetainedDoubleField.setValue(totalUserTankQuantities - Optional.ofNullable(tankQuantityDoubleField.getValue()).orElse(0.0));
				});
				JavaFXUtils.addTooltip(tankNameJFXChipView);
			}
		});
		tankQuantityDoubleField.valueProperty().addListener(new ChangeListener<Double>() {
			@Override
			public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
				Double totalUserTankQuantities = ((TankUsageContext)tankNameJFXChipView.getUserData()).getTotalUserTankActualLevel();
				if(totalUserTankQuantities - Optional.ofNullable(newValue).orElse(0.0) < 0) {
					OilRecordBookPart1ViewController.showWarning("Invalid value:" + newValue, "Valid value range:0.0-" + totalUserTankQuantities);
					tankQuantityDoubleField.setValue(totalUserTankQuantities);
				} else {
					tankRetainedDoubleField.setValue(totalUserTankQuantities - Optional.ofNullable(newValue).orElse(0.0));
				}
			}
		});
		addRemoveListener(tankNameJFXChipView);
		
		Platform.runLater(() -> {
			try {
				Image infoImageLetterI = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
						.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
				imageViewButtonInfoLetterI.setImage(infoImageLetterI);
				 addImageIdentityOfTanksLetterI = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
						.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_ADD));
				identityOfTanksImageViewButton.setImage(addImageIdentityOfTanksLetterI);
				identityOfTanksImageViewButton.setStyle("-fx-effect: dropshadow(three-pass-box, grey, 3, 0, 3, 3);");
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
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		Platform.runLater(() -> {
		eventBroker.subscribe(Activator.MARPOL_DETAILS_RELOADED, new org.osgi.service.event.EventHandler() {
			@Override
			public void handleEvent(org.osgi.service.event.Event event) {
				ArrayList<Long> changedTankIds = (ArrayList<Long>) event.getProperty(Activator.TANK_IDS);
    			if(changedTankIds==null) {
    				tankNameJFXChipView.getChips().clear();
    				identityOfTanksLetterI1JFXChipView.getChips().clear();
    				identityOfTanksLetterI4JFXChipView.getChips().clear();
    			} else if(changedTankIds!=null && !changedTankIds.isEmpty()) {
    				Activator.removeChangedTanksFromJFXChipView(changedTankIds, tankNameJFXChipView);
    				Activator.removeChangedTanksFromJFXChipView(changedTankIds, identityOfTanksLetterI1JFXChipView);
    				Activator.removeChangedTanksFromJFXChipView(changedTankIds, identityOfTanksLetterI4JFXChipView);
    			}
			}
		});
		});
	}

	public void showBalloon(MouseEvent event) {
		ballonMessage.showGeneralBalloon((ImageViewButton) event.getTarget(), label1);
	}
	public void showBalloon1(MouseEvent event) {
		ballonMessage.showGeneralBalloon((ImageViewButton) event.getTarget(), label2);
	}
	public void selectTanksBalastedName(ActionEvent e) {
		String value = ((Button) e.getSource()).getText();
		currentSelectedTextField.setText(value);
	}

	public void processI(OilLog logToInsert, List<OilLog> logToInsertList, Map<Validation, Object> validations) {
		validateFields(logToInsert.getBusinessDate(),validations);
		if (selectOperationCheckBox.isSelected()) {
			if(comboBox.getValue().equals(option0)) {
				OilLog log = (OilLog) logToInsert.clone();
				StringBuilder overboardVal = new StringBuilder();
				overboardVal.append("OVERBOARD VALVE ");
				overboardVal.append(overboardEquipment.getText());
				overboardVal.append(" FROM 15PPM BILGE WATER SEPARATOR UNIT SEALED");
				log.setContent(overboardVal.toString());
				log.setCodeNumber("");
				log.setOrderInGroup("ZP");
				logToInsertList.add(log);
				
				OilLog logSeal = (OilLog) logToInsert.clone();
				StringBuilder sealVal = new StringBuilder();
				sealVal.append("SEAL NO:");
				sealVal.append(sealNumberEquipment.getText());
				logSeal.setContent(sealVal.toString());
				logSeal.setCodeNumber("");
				logSeal.setOrderInGroup("ZQ");
				logToInsertList.add(logSeal);
			}
			
			
			if(comboBox.getValue().equals(option1)) {
				OilLog log = (OilLog) logToInsert.clone();
				StringBuilder overboardVal = new StringBuilder();
				overboardVal.append("OVERBOARD VALVE ");
				overboardVal.append(overboardValve.getText());
				overboardVal.append(" FROM 15PPM BILGE WATER SEPARATOR UNIT UNSEALED");
				log.setContent(overboardVal.toString());
				log.setCodeNumber("");
				log.setOrderInGroup("ZR");
				logToInsertList.add(log);
				
				OilLog logFor = (OilLog) logToInsert.clone();
				StringBuilder forVal = new StringBuilder();
				forVal.append("FOR ");
				forVal.append(reasonTextField.getText());
				logFor.setContent(forVal.toString());
				logFor.setCodeNumber("");
				logFor.setOrderInGroup("ZS");
				logToInsertList.add(logFor);
				
				OilLog logSeal = (OilLog) logToInsert.clone();
				StringBuilder sealVal = new StringBuilder();
				sealVal.append("SEAL NO:");
				sealVal.append(sealNumber.getText());
				logSeal.setContent(sealVal.toString());
				logSeal.setCodeNumber("");
				logSeal.setOrderInGroup("ZT");
				logToInsertList.add(logSeal);
			}
			
			if (comboBox.getValue().equals(option2) && (!tankFuelType.getText().isEmpty() && !tankNameJFXChipView.getChips().isEmpty()
					&& !debunkeringToTextField.getText().isEmpty() && !debunkeringPlaceOfTextField.getText().isEmpty() && setTimeAtStart.getLocalDateTime()!=null
					&& setTimeEnd.getLocalDateTime()!=null && !receipNoTextField.getText().isEmpty() && !tankQuantityDoubleField.getText().isEmpty() && !tankRetainedDoubleField.getText().isEmpty())) {
				
				OilLog logFuelType = (OilLog) logToInsert.clone();
				logFuelType.setContent(typeFillTemplate.toString());
				logFuelType.setCodeNumber("");
				logFuelType.setOrderInGroup("ZT");
				logToInsertList.add(logFuelType);
				
				OilLog log = (OilLog) logToInsert.clone();
				Map<Tank, Double> userTankActualLevels = new HashMap<Tank, Double>();
				StringBuilder sb = new StringBuilder();
				List<Pair<String, Tank>> tankPairList = getTanks(fuelTanksGridPane);
				for(Pair<String, Tank> tank : tankPairList) {
					try {
						if (!validations.containsKey(Validation.IO2_EMPTY)) {
							userTankActualLevels.put(tank.getValue(), MathUtils.decimalFormat.parse(parseTank(tank.getKey()).get("tankContent")).doubleValue());		
						}
					} catch (ParseException e) {
						logger.debug(e.getMessage(), e);
					}
					sb.append("\n");
					sb.append(tank.getKey());
				}
				log.setUserTankActualLevels(userTankActualLevels);
				log.setContent(sb.toString());
				log.setCodeNumber("");
				log.setOrderInGroup("ZU");
				logToInsertList.add(log);
				
				if(debunkeringTemplate.getAttributes()!=null) debunkeringTemplate.getAttributes().clear();
				debunkeringTemplate.setAttribute("debunkeringTo", debunkeringToTextField.getText());
				debunkeringTemplate.setAttribute("debunkeringPlaceOf", debunkeringPlaceOfTextField.getText());
				debunkeringTemplate.setAttribute("receiptFile", receipNoTextField.getText());
				OilLog logDebunkering = (OilLog) logToInsert.clone();
				logDebunkering.setContent(debunkeringTemplate.toString());
				logDebunkering.setCodeNumber("");
				logDebunkering.setOrderInGroup("ZV");
				if(receipNoTextField.getUserData()!=null && ((File)receipNoTextField.getUserData()).exists()) {
					logDebunkering.setAttachmentName(((File)receipNoTextField.getUserData()).getName());
					byte[] attachmentBytes = null;
					try {
						attachmentBytes = Files.readAllBytes(((File)receipNoTextField.getUserData()).toPath());
					} catch (IOException e) {
						logger.error(e.getMessage(), e);
					}
					logDebunkering.setAttachmentData(attachmentBytes);
				}
				logToInsertList.add(logDebunkering);
				
				OilLog logDate = (OilLog) logToInsert.clone();
				logDate.setContent("START " +setTimeAtStart.getLocalDateTime().format(dateTimeFormatter) + " STOP " + setTimeEnd.getLocalDateTime().format(dateTimeFormatter));
				logDate.setCodeNumber("");
				logDate.setOrderInGroup("ZW");
				logToInsertList.add(logDate);
			}
			
			if (comboBox.getValue().equals(option4)) {
				OilLog log = (OilLog) logToInsert.clone();
				log.setContent(commentTextArea.getText());
				log.setCodeNumber("");
				log.setOrderInGroup("ZV");
				logToInsertList.add(log);
			}
			if (comboBox.getValue().equals(option5)) {
				OilLog logIn = (OilLog) logToInsert.clone();
				logIn.setCodeNumber("");
				logIn.setContent("WEEKLY INVENTORY OF BILGE WATER TANKS (LISTED UNDER ITEM 3.3)");
				logIn.setOrderInGroup("ZW");
				logToInsertList.add(logIn);
				if (quantityOfResidueCollectedCheckBox.isSelected() && !identityOfTanksLetterI4JFXChipView.getChips().isEmpty()) {
					OilLog log = (OilLog) logToInsert.clone();
					log.setCodeNumber("");
					log.setContent(quantityOfResidueCollectedByManualOperationInM3DoubleField.getValue() + " M3, OILY BILGE WATER FROM  " + String.join("", identityOfTanksLetterI4JFXChipView.getChips())+" HOLDING TANK");
					log.setUserTankActualLevels(((TankUsageContext)identityOfTanksLetterI4JFXChipView.getUserData()).getUserTankActualLevels());
					log.setOrderInGroup("ZX");
					logToInsertList.add(log);
				}
				if (identityOfTankCheckBox.isSelected()) {
					OilLog log = (OilLog) logToInsert.clone();
					log.setCodeNumber("");
					log.setContent("TO " +String.join(", ", identityOfTanksLetterI1JFXChipView.getChips()));
					log.setUserTankActualLevels(((TankUsageContext)identityOfTanksLetterI1JFXChipView.getUserData()).getUserTankQuantities());
					log.setOrderInGroup("ZY");
					logToInsertList.add(log);
				}
				if (capacityOfTanksCheckBox.isSelected() && totalQuantityOfRetentionCheckBox.isSelected()) {
					OilLog log = (OilLog) logToInsert.clone();
					log.setCodeNumber("");
					log.setContent("CAPACITY "+capacityOfTanksInM3DoubleField.getValue() + " M3,  " +totalQuantityOfRetentionInM3DoubleField.getValue() + " M3 RETAINED");
					log.setOrderInGroup("ZZ");
					logToInsertList.add(log);
				}
			}
			
		}

		
	}
	private void validateFields(Date registrationDate, Map<Validation, Object> validations) {
		if (comboBox.getValue().equals(option0) && (overboardEquipment.getText().isEmpty() || sealNumberEquipment.getText().isEmpty())) {
			validations.put(Validation.IO0_EMPTY,null);
		}
		if (comboBox.getValue().equals(option1) && (overboardValve.getText().isEmpty() || sealNumber.getText().isEmpty() || reasonTextField.getText().isEmpty())) {
			validations.put(Validation.IO1_EMPTY,null);
		}
		if (comboBox.getValue().equals(option2) && (tankNameJFXChipView.getChips().isEmpty() || tankFuelType.getText().isEmpty()
		   || debunkeringToTextField.getText().isEmpty() || debunkeringPlaceOfTextField.getText().isEmpty() || setTimeAtStart.getLocalDateTime()==null
		   || setTimeEnd.getLocalDateTime()==null || receipNoTextField.getText().isEmpty() || tankQuantityDoubleField.getText().isEmpty() || tankRetainedDoubleField.getText().isEmpty())) {
			validations.put(Validation.IO2_EMPTY,null);
		}else if(comboBox.getValue().equals(option2) && setTimeAtStart.getLocalDateTime().isAfter(setTimeEnd.getLocalDateTime())) {
			validations.put(Validation.IO2_START_AFTER_END, null);
		}
		if(registrationDate != null && comboBox.getValue().equals(option2) && setTimeAtStart.getLocalDateTime()!=null && setTimeEnd.getLocalDateTime()!=null) {
			LocalDateTime registrationLocalDateTime = Instant.ofEpochMilli(registrationDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime().plusDays(1);
			if(registrationLocalDateTime.isBefore(setTimeAtStart.getLocalDateTime()) || registrationLocalDateTime.isBefore(setTimeEnd.getLocalDateTime())) {
				validations.put(Validation.IO2_TIME_AFTER_REGISTRATION_DATE, null);
			}
		}
		if (comboBox.getValue().equals(option4)&&commentTextArea.getText().isEmpty()) {
			validations.put(Validation.IO4_EMPTY,null);
		}
		if (comboBox.getValue().equals(option5)&&identityOfTanksLetterI1JFXChipView.getChips().isEmpty()) {
			validations.put(Validation.IO5_TANK_EMPTY,null);
		}
		if (comboBox.getValue().equals(option5) && identityOfTankCheckBox.isSelected() && quantityOfResidueCollectedCheckBox.isSelected()) {
			TankUsageContext tankUsageContext11_1 = (TankUsageContext) identityOfTanksLetterI1JFXChipView.getUserData();
			TankUsageContext tankUsageContext11_4 = (TankUsageContext) identityOfTanksLetterI4JFXChipView.getUserData();
			
			Map<Tank, Double> tankQuantities11_1 = tankUsageContext11_1.getUserTankQuantities();
			Map<Tank, Double> tankQuantities11_4 = tankUsageContext11_4.getUserTankQuantities();
			List<Tank> intersect = tankQuantities11_1.keySet().stream().filter(tankQuantities11_4.keySet()::contains).collect(Collectors.toList());
			if(!intersect.isEmpty()) {
				validations.put(Validation.I_1_I_4_SAME_TANK, intersect.stream().map(Tank::getName).collect(Collectors.joining(",")));
			}
			
			double sum11_1 = tankUsageContext11_1.getTotalUserTankQuantities()-tankUsageContext11_1.getTotalUserTankActualLevel();
			double sum11_4 = tankUsageContext11_4.getTotalUserTankQuantities();
			if (!(Math.abs(sum11_1 - sum11_4) < MathUtils.DOUBLE_THRESHOLD)) {
				validations.put(Validation.I_1_SUM_DIFFERENT_I_4_SUM_WARNING, new double[] {sum11_1, sum11_4});
			}
		}
		if(debunkeringToTextField.getText()!=null && !debunkeringToTextField.getText().equals("")) {
			List<Pair<String, Tank>> tankPairList = getTanks(fuelTanksGridPane);
			List<Pair<String, Tank>> notNullTanksTankPairList = tankPairList.stream().filter(t -> t.getValue() != null).collect(Collectors.toList());
			List<Tank> duplicates = notNullTanksTankPairList.stream()
					  .collect(Collectors.groupingBy(p -> p.getValue(), Collectors.counting()))
					  .entrySet().stream()
					  .filter(n -> n.getValue() > 1)
					  .map(n -> n.getKey())
					  .collect(Collectors.toList());
			for(Tank duplicateTank : duplicates) {
				validations.put(Validation.I3_TANK_ALREADY_IN_LIST, duplicateTank.getName());
			}
			for(Pair<String, Tank> tank : tankPairList) {
				if (comboBox.getValue().equals(option2) && (parseTank(tank.getKey()).get("tankFillVolume")==null ||
					parseTank(tank.getKey()).get("tankName")==null || parseTank(tank.getKey()).get("tankContent")==null)) {
					validations.put(Validation.IO2_EMPTY,null);
				}
			}
		}
		
		if (comboBox.getValue().equals(option2) && emptyFuelType) {
			validations.put(Validation.IO2_EMPTY_FUEL_TYPE,null);	
		}
	}
	
	public void addFuelTank(ActionEvent event) {
		Map<String, Node> map = addTank(fuelTanksGridPane);
		TankUsageContext tuc = new TankUsageContext(Stream.of(TankType.FUELOIL).collect(Collectors.toList()), false, false);
		tuc.setNoNeedForUserTankQuantities(true);
		map.get("tankName").setUserData(tuc);
		
		JFXChipView<String> tankNameJFXChipView = (JFXChipView<String>) map.get("tankName");
		DoubleField tankFillVolume = (DoubleField) map.get("tankFillVolume");
		DoubleField tankRetainedVolume = (DoubleField) map.get("tankContent");
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
					tankRetainedVolume.setValue(totalUserTankQuantities - Optional.ofNullable(tankFillVolume.getValue()).orElse(0.0));
				});
				JavaFXUtils.addTooltip(tankNameJFXChipView);
			}
		});
		tankFillVolume.valueProperty().addListener(new ChangeListener<Double>() {
			@Override
			public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
				Double totalUserTankQuantities = ((TankUsageContext)tankNameJFXChipView.getUserData()).getTotalUserTankActualLevel();
				if(totalUserTankQuantities - Optional.ofNullable(newValue).orElse(0.0) < 0) {
					OilRecordBookPart1ViewController.showWarning("Invalid value:" + newValue, "Valid value range:0.0-" + totalUserTankQuantities);
					tankFillVolume.setValue(totalUserTankQuantities);
				} else {
					tankRetainedVolume.setValue(totalUserTankQuantities - Optional.ofNullable(newValue).orElse(0.0));
				}
			}
		});
		addRemoveListener(tankNameJFXChipView);
		
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
		TankUsageContext tuc = new TankUsageContext(Stream.of(TankType.FUELOIL).collect(Collectors.toList()), false, false);
		tuc.setNoNeedForUserTankQuantities(true);
		tankNameJFXChipView.setUserData(tuc);
		tankNameJFXChipView.getStyleClass().add("logbookjfx-chip-view");
		
		ImageViewButton addImageViewButton = new ImageViewButton(addImageIdentityOfTanksLetterI);
		addImageViewButton.getStyleClass().add("image-view-button");
		addImageViewButton.getStyleClass().add("logbookButtonAddTank");
		GridPane.setHalignment(addImageViewButton, HPos.CENTER);
		addImageViewButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				selectTankerDialog.showDialog((Stage) ((ImageViewButton) event.getSource()).getScene().getWindow(),
						tankNameJFXChipView);
//				Button buttonIdentityOfTanksLetterI = (Button)event.getSource();
//				GridPane parent = (GridPane)buttonIdentityOfTanksLetterI.getParent();
//				Integer rowIndex = GridPane.getRowIndex(deleteButton);
//				parent.getChildren().removeIf(node -> GridPane.getRowIndex(node) == rowIndex);
			}
		});
		Button deleteButton = new Button("X");
		deleteButton.setStyle("-fx-effect: dropshadow(three-pass-box, grey, 3, 0, 3, 3); -fx-background-color: #ef7f2e");
		deleteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Button deleteButton = (Button)event.getSource();
				GridPane parent = (GridPane)deleteButton.getParent();
				Integer rowIndex = GridPane.getRowIndex(deleteButton);
				parent.getChildren().removeIf(node -> GridPane.getRowIndex(node) == rowIndex);
			}
		});
		deleteButton.setPickOnBounds(true);
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
		TextField fuelType = new TextField();
		fuelType.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		DoubleField tankContent = new DoubleField();
		DoubleField tankFillVolume = new DoubleField();
		MathUtils.setStringConverter(tankContent);
		MathUtils.setStringConverter(tankFillVolume);
		MathUtils.setMinMaxValues(tankFillVolume, 0.0, null);
		MathUtils.setMinMaxValues(tankContent, 0.0, null);
		tankContent.setValue(0.0);
		tankFillVolume.setValue(0.0);
		tankNameJFXChipView.setId("tankName"+rows);
		tankContent.setId("tankContent"+rows);
		tankFillVolume.setId("tankFillVolume"+rows);
		fuelType.setId("tankFuelType"+rows);
		tankContent.getStyleClass().add("logbook-text-field");
		tankFillVolume.getStyleClass().add("logbook-text-field");
		fuelType.getStyleClass().add("logbook-text-field");
		JavaFXUtils.addFilledPseudoClass(tankContent);
		JavaFXUtils.addFilledPseudoClass(tankFillVolume);
		JavaFXUtils.addFilledPseudoClass(fuelType);
		tankGridPane.addRow(rows, tankFillVolume, measureLabel1, fuelType,addImageViewButton,tankNameJFXChipView,tankContent, measureLabel2, deleteButton);
		Map<String, Node> map = new HashMap<>();
		map.put("tankName", tankNameJFXChipView);
		map.put("tankContent", tankContent);
		map.put("tankFillVolume", tankFillVolume);
		map.put("tankFuelType", fuelType);
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
		List<Pair<String, Tank>> tankList = new ArrayList<Pair<String, Tank>>();
		Double totalVolume = 0.0;
		List<String> allTypeOfFuel = new ArrayList<String>();
		emptyFuelType=false;
		if(typeFillTemplate.getAttributes()!=null) typeFillTemplate.getAttributes().clear();
		for(List<Node> tankNodeList : tankNodesMap.values()) {
			
			String name = "";
			String fuelType = "";
			Double tankFillVolume = 0.0;
			Double content = 0.0;
			Double capacity = 0.0;
			Tank tank = null;
			if(tankFillTemplate.getAttributes()!=null) tankFillTemplate.getAttributes().clear();
			boolean rowContainsTank = false;
			for(Node node : tankNodeList) {
				if(node instanceof JFXChipView && !(node instanceof DoubleField) && node.getId().startsWith("tankName")) {
					List<String> chipsList = ((JFXChipView<String>)node).getChips();
					if(!chipsList.isEmpty()) name = chipsList.get(0);
					tankFillTemplate.setAttribute("tankName", name);
					Set<Tank> userTanks = ((TankUsageContext) ((JFXChipView<String>)node).getUserData()).getUserTankQuantities().keySet();
					if(userTanks==null) continue;
					if(!userTanks.isEmpty()) {
						tank = (Tank)userTanks.iterator().next();
						capacity = tank.getSize();
					}
					rowContainsTank = true;
				}
				if(node instanceof TextField && !(node instanceof DoubleField) && node.getId().startsWith("tankFuelType")) {
					fuelType = ((TextField)node).getText();
					if (!((TextField)node).getText().isEmpty() && !allTypeOfFuel.contains(((TextField)node).getText())) {
						allTypeOfFuel.add(((TextField)node).getText());	
					}
					if (((TextField)node).getText().isEmpty()) {
					 emptyFuelType=true;
					}
					rowContainsTank = true;
				}
				if(node instanceof DoubleField && node.getId().startsWith("tankFillVolume")) {
					tankFillVolume = ((DoubleField)node).getValue();
					tankFillTemplate.setAttribute("tankFillVolume", tankFillVolume!=null?MathUtils.decimalFormat.format(tankFillVolume):null);
					rowContainsTank = true;
					totalVolume+=(tankFillVolume!=null?tankFillVolume:0.0);
				}
				if(node instanceof DoubleField && node.getId().startsWith("tankContent")) {
					content = ((DoubleField)node).getValue();
					tankFillTemplate.setAttribute("tankContent", content!=null?MathUtils.decimalFormat.format(content):null);
					rowContainsTank = true;
				}
			}

			if(!rowContainsTank) continue;
			boolean rowInvalid = false;
			if(tankFillVolume==null) {rowInvalid = true; logger.error("quantity from tank " + name + " must have a value");};
			if(content==null) {rowInvalid = true; logger.error("retained in tank " + name + " must have a value");};
			if(tankFillVolume!=null && content!=null && capacity!=null && tankFillVolume>capacity) {rowInvalid = true; logger.error("quantity from tank " + name + " must be less than tank capacity");};
			if(rowInvalid) continue;
			tankList.add(new Pair(tankFillTemplate.toString(), tank));
		}
		typeFillTemplate.setAttribute("totalFillVolume", totalVolume);
		typeFillTemplate.setAttribute("tankFuelType", allTypeOfFuel.toString());
		return tankList;
	}
	
	private static Map<String, String> parseTank(String tankString) {
		Map<String, String> map = new HashMap<>();
		Matcher matcher = tankFillPattern.matcher(tankString);
		if (matcher.find()) {
			map.put("tankFillVolume", matcher.group(1));
			map.put("tankName", matcher.group(2));
			map.put("tankContent", matcher.group(3));
			logger.debug(matcher.group(1));
			logger.debug(matcher.group(2));
			logger.debug(matcher.group(3));
		} else {
			logger.debug("could not parse: " + tankString);
		}
		return map;
	}
	
	private Map<String, String> parseOverBoardValve(String overBoardValveString) {
		Map<String, String> map = new HashMap<>();
		Matcher matcher = overboardValvePattern.matcher(overBoardValveString);
		if (matcher.find()) {
			map.put("overboardValve", matcher.group(1));
			map.put("sealNumber", matcher.group(2));
			logger.debug(matcher.group(1));
			logger.debug(matcher.group(2));
		} else {
			logger.debug("could not parse: " + overBoardValveString);
		}
		return map;
	}
	private Map<String, String> parseOverBoardValveUnseal(String overBoardValveUnsealString) {
		Map<String, String> map = new HashMap<>();
		Matcher matcher = overboardValveUnsealPattern.matcher(overBoardValveUnsealString);
		if (matcher.find()) {
			map.put("overboardValveUnseal", matcher.group(1));
			map.put("sealNumberUnseal", matcher.group(2));
			logger.debug(matcher.group(1));
			logger.debug(matcher.group(2));
		} else {
			logger.debug("could not parse: " + overBoardValveUnsealString);
		}
		return map;
	}
	private Map<String, String> parseDebunkering(String debunkeringString) {
		Map<String, String> map = new HashMap<>();
		Matcher matcher = debunkeringPattern.matcher(debunkeringString);
		if (matcher.find()) {
			map.put("debunkeringTo", matcher.group(1));
			map.put("debunkeringPlaceOf", matcher.group(2));
			map.put("receiptFile", matcher.group(3));
			map.put("rest", debunkeringString.substring(matcher.end(3)).trim());
			logger.debug(""+matcher.groupCount());
			logger.debug(matcher.group(1));
			logger.debug(matcher.group(2));
			logger.debug(matcher.group(3));
		} else {
			logger.debug("could not parse: " + debunkeringString);
		}
		return map;
	}

	public void clearFilledInfo() {
		selectOperationCheckBox.setSelected(false);
		overboardValve.clear();
		sealNumber.clear();
		VBoxCaso1.setVisible(false);
		VBoxCaso2.setVisible(false);
		weeklyInventoryCheckBox.setSelected(false);
		debunkeringToTextField.clear();
		debunkeringPlaceOfTextField.clear();
		receipNoTextField.clear();
		tankFuelType.clear();
		tankQuantityDoubleField.clear();
		tankNameJFXChipView.getChips().clear();
		((TankUsageContext)tankNameJFXChipView.getUserData()).getUserTankQuantities().clear();
		Platform.runLater(() -> {
			tankRetainedDoubleField.clear();
		});
		fuelTanksGridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node)!=null && GridPane.getRowIndex(node) > 1);
		VBoxCaso4.setVisible(false);
		quantityOfResidueCollectedByManualOperationInM3DoubleField.clear();
		totalQuantityOfRetentionCheckBox.setSelected(false);
	}
	
	public void showSelectTanker(Event event) {
		selectTankerDialog.showDialog((Stage) ((ImageViewButton) event.getSource()).getScene().getWindow(), tankNameJFXChipView);
	}
	public void openIdentifyTanksLetterI3(MouseEvent e) {
		selectTankerDialog.showDialog((Stage) ((ImageViewButton) e.getSource()).getScene().getWindow(), identityOfTanksLetterI1JFXChipView);
	}
	public void openIdentifyTanksLetterI4(MouseEvent e) {
		selectTankerDialog.showDialog((Stage) ((ImageViewButton) e.getSource()).getScene().getWindow(), identityOfTanksLetterI4JFXChipView);
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
	
	public static Double parseTankFillVolume(String value) {
		Map<String, String> map = parseTank(value);
		try {
			return MathUtils.decimalFormat.parse(map.get("tankFillVolume")).doubleValue();
		} catch (ParseException e) {
			logger.debug(e.getMessage(), e);
			return null;
		}
	}	
	public BallonMessage getBallonMessage() {
		return ballonMessage;
	}

	public void setBallonMessage(BallonMessage ballonMessage) {
		this.ballonMessage = ballonMessage;
	}

	public IResourceProviderService getResourcesService() {
		return resourcesService;
	}

	public void setResourcesService(IResourceProviderService resourcesService) {
		this.resourcesService = resourcesService;
	}

	public ImageProvider getImageProvider() {
		return imageProvider;
	}

	public void setImageProvider(ImageProvider imageProvider) {
		this.imageProvider = imageProvider;
	}

	public IEventBroker getEventBroker() {
		return eventBroker;
	}

	public void setEventBroker(IEventBroker eventBroker) {
		this.eventBroker = eventBroker;
	}
	public SelectTankerDialog getSelectTankerDialog() {
		return selectTankerDialog;
	}

	public void setSelectTankerDialog(SelectTankerDialog selectTankerDialog) {
		this.selectTankerDialog = selectTankerDialog;
	}
	public AnchorPane getMissedIAnchorPane() {
		return missedIAnchorPane;
	}
	
}
