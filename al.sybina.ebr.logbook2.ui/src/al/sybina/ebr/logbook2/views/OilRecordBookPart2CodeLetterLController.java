package al.sybina.ebr.logbook2.views;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
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
import al.sybina.ebr.logbook2.views.OilRecordBookPart2ViewController.Validation;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jfxtras.scene.control.ImageViewButton;
import jfxtras.scene.control.LocalTimeTextField;
import jidefx.scene.control.field.DoubleField;
import jidefx.scene.control.field.FormattedTextField;

public class OilRecordBookPart2CodeLetterLController implements Initializable {

	private Label label1 = new Label(" Ships· masters should obtain from the operator of the \r\n" + "reception facilities. which include barges and tank trucks a \r\n" + "receipt or certificate detailing the quantity or tank washings,\r\n"
			+ "dirty ballast, residues or oily mixtures transferred together \r\n" + "with the lime and date or the transfer. This receipt or \r\n" + "certificate, if attached to the Oil Record Book Part II may aid\r\n"
			+ "the master of the ship in proving  that his ship was not involved \r\n" + "in an alleged pollution incident The receipt or the certificate \r\n" + "should be kept together with the Oil Record Book Part II\r\n" + "");

	private static final Logger logger = LoggerCreator.createLogger(OilRecordBookPart2CodeLetterLController.class);

	@Inject
	private IEventBroker eventBroker;
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
	@FXML
	private CheckBox logbook2LetterL63CheckBox;
	@FXML
	private JFXChipView<String> identifyOfTanksJFXChipView;
	@FXML
	private ImageViewButton identityOfTanksImageViewButton;

	@FXML
	private CheckBox timeAndPositionAtStartCheckBox;
	@FXML
	private LocalTimeTextField timeAtStartLocalTimeTextField;
	@FXML
	private FormattedTextField latAtStartFormattedTextField;
	@FXML
	private FormattedTextField longAtStartFormattedTextField;

	@FXML
	private CheckBox timeAndPositionAtIntoSeaCheckBox;
	@FXML
	private LocalTimeTextField timeAtIntoSeaLocalTimeTextField;
	@FXML
	private FormattedTextField latAtIntoSeaFormattedTextField;
	@FXML
	private FormattedTextField longAtIntoSeaFormattedTextField;

	@FXML
	private CheckBox logbook2LetterL66CheckBox;
	@FXML
	private CheckBox logbook2LetterL66SEACheckBox;
	@FXML
	private DoubleField logbook2LetterL66SEADoubleField;
	@FXML
	private CheckBox logbook2LetterL66PORTCheckBox;
	@FXML
	private DoubleField logbook2LetterL66PORTDoubleField;
	@FXML
	private TextField logbook2LetterL66PORTTextField;
	@FXML
	private TextField logbook2LetterL66ReceptionTextField;
	@FXML
	private ImageViewButton uploadReceiptImageViewButton;
	@FXML
	private ImageViewButton infoLetterAImageViewButton;
	@FXML
	private ImageViewButton infoLetterL1ImageViewButton;

	@FXML
	private CheckBox logbook2LetterL67CheckBox;
	@FXML
	private CheckBox logbook2LetterL67YESCheckBox;
	@FXML
	private CheckBox logbook2LetterL67NOTCheckBox;
	@FXML
	private TextField logbook2LetterL67TextField;

	@FXML
	private CheckBox logbook2LetterL68CheckBox;
	@FXML
	private CheckBox logbook2LetterL68YESCheckBox;
	@FXML
	private CheckBox logbook2LetterL68NOTCheckBox;
	@FXML
	private TextField logbook2LetterL68TextField;

	@FXML
	private CheckBox timeAndPositionAtEndCheckBox;
	@FXML
	private LocalTimeTextField timeAtEndLocalTimeTextField;
	@FXML
	private FormattedTextField latAtEndFormattedTextField;
	@FXML
	private FormattedTextField longAtEndFormattedTextField;

	protected String CACHE_FILE_NAME = System.getProperty("user.home") + "/.ebr/caches/caches.properties";
	@FXML
	public AnchorPane codeLetterContentAnchorPane;
	private DateTimeFormatter timeFormatter = EasyDateUtils.logbookTimeFormatter;
	private ObservableList<Image> helpImageList = FXCollections.observableArrayList();
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MathUtils.setStringConverter(logbook2LetterL66SEADoubleField);
		MathUtils.setStringConverter(logbook2LetterL66PORTDoubleField);
		MathUtils.setMinMaxValues(logbook2LetterL66SEADoubleField, 0.0, null);
		MathUtils.setMinMaxValues(logbook2LetterL66PORTDoubleField, 0.0, null);
		try {

			Image helpImageLetterL2 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPL2));
			
			Image helpImageLetterL1 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPL1));
			
			Image helpImageLetterL3 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPL3));
			
			Image helpImageLetterL4 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPL4));
			
			helpImageList.addAll(helpImageLetterL2,helpImageLetterL1, helpImageLetterL3, helpImageLetterL4);
			
			Image addImageIdentityOfTanks = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_ADD));
			identityOfTanksImageViewButton.setImage(addImageIdentityOfTanks);

			Image infoIconImageInfoLetterA = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
			infoLetterAImageViewButton.setImage(infoIconImageInfoLetterA);
			infoLetterL1ImageViewButton.setImage(infoIconImageInfoLetterA);
		} catch (IOException e) {
			e.printStackTrace();
		}
		identityOfTanksImageViewButton.setDisable(true);
		identifyOfTanksJFXChipView.setDisable(true);

		timeAtStartLocalTimeTextField.setDisable(true);
		latAtStartFormattedTextField.setDisable(true);
		longAtStartFormattedTextField.setDisable(true);
		CoordinateUIUtils.initializeLatitudeField(latAtStartFormattedTextField);
		CoordinateUIUtils.initializeLongitudeField(longAtStartFormattedTextField);

		timeAtIntoSeaLocalTimeTextField.setDisable(true);
		latAtIntoSeaFormattedTextField.setDisable(true);
		longAtIntoSeaFormattedTextField.setDisable(true);
		CoordinateUIUtils.initializeLatitudeField(latAtIntoSeaFormattedTextField);
		CoordinateUIUtils.initializeLongitudeField(longAtIntoSeaFormattedTextField);

		logbook2LetterL66SEACheckBox.setDisable(true);
		logbook2LetterL66PORTCheckBox.setDisable(true);

		logbook2LetterL66SEADoubleField.setDisable(true);
		logbook2LetterL66PORTDoubleField.setDisable(true);
		logbook2LetterL66PORTTextField.setDisable(true);
		logbook2LetterL66ReceptionTextField.setDisable(true);
		uploadReceiptImageViewButton.setDisable(true);

		logbook2LetterL66SEADoubleField.setText("");
		logbook2LetterL66PORTDoubleField.setText("");
		logbook2LetterL66PORTTextField.setText("");
		logbook2LetterL66ReceptionTextField.setText("");

		logbook2LetterL67YESCheckBox.setDisable(true);
		logbook2LetterL67NOTCheckBox.setDisable(true);
		logbook2LetterL67TextField.setDisable(true);

		logbook2LetterL68YESCheckBox.setDisable(true);
		logbook2LetterL68NOTCheckBox.setDisable(true);
		logbook2LetterL68TextField.setDisable(true);

		timeAtEndLocalTimeTextField.setDisable(true);
		latAtEndFormattedTextField.setDisable(true);
		longAtEndFormattedTextField.setDisable(true);
		CoordinateUIUtils.initializeLatitudeField(latAtEndFormattedTextField);
		CoordinateUIUtils.initializeLongitudeField(longAtEndFormattedTextField);
		logbook2LetterL66PORTTextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		logbook2LetterL67TextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		logbook2LetterL68TextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		
		JavaFXUtils.addFilledPseudoClass(latAtStartFormattedTextField);
		JavaFXUtils.addFilledPseudoClass(longAtStartFormattedTextField);
		JavaFXUtils.addFilledPseudoClass(latAtIntoSeaFormattedTextField);
		JavaFXUtils.addFilledPseudoClass(longAtIntoSeaFormattedTextField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterL66SEADoubleField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterL66PORTDoubleField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterL66PORTTextField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterL66ReceptionTextField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterL67TextField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterL68TextField);
		JavaFXUtils.addFilledPseudoClass(latAtEndFormattedTextField);
		JavaFXUtils.addFilledPseudoClass(longAtEndFormattedTextField);
		JavaFXUtils.addFilledPseudoClass(timeAtIntoSeaLocalTimeTextField);
		JavaFXUtils.addFilledPseudoClass(timeAtStartLocalTimeTextField);
		JavaFXUtils.addFilledPseudoClass(timeAtEndLocalTimeTextField);
		timeAtIntoSeaLocalTimeTextField.setDateTimeFormatter(EasyDateUtils.logbookTimeFormatter);
		timeAtStartLocalTimeTextField.setDateTimeFormatter(EasyDateUtils.logbookTimeFormatter);
		timeAtEndLocalTimeTextField.setDateTimeFormatter(EasyDateUtils.logbookTimeFormatter);
		
		logbook2LetterL63CheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
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
				if (logbook2LetterL63CheckBox.isSelected()) {
					Double totalUserTankQuantities = ((TankUsageContext) identifyOfTanksJFXChipView.getUserData()).getTotalUserTankQuantities();
					Double totalUserTankActualLevel = ((TankUsageContext) identifyOfTanksJFXChipView.getUserData()).getTotalUserTankActualLevel();
					if (logbook2LetterL66SEACheckBox.isSelected()) {
						logbook2LetterL66SEADoubleField.setValue(totalUserTankQuantities);
					}
					if (logbook2LetterL66PORTCheckBox.isSelected()) {
						logbook2LetterL66PORTDoubleField.setValue(totalUserTankQuantities);
					}
				}
				JavaFXUtils.addTooltip(identifyOfTanksJFXChipView);
			}

		});
		timeAndPositionAtStartCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				timeAtStartLocalTimeTextField.setDisable(false);
				latAtStartFormattedTextField.setDisable(false);
				longAtStartFormattedTextField.setDisable(false);

				timeAndPositionAtIntoSeaCheckBox.setSelected(true);
				logbook2LetterL66CheckBox.setSelected(true);
				logbook2LetterL66SEACheckBox.setSelected(true);
				logbook2LetterL67CheckBox.setSelected(true);
				logbook2LetterL68CheckBox.setSelected(true);
				timeAndPositionAtEndCheckBox.setSelected(true);
				
				

			} else {
				timeAtStartLocalTimeTextField.setDisable(true);
				latAtStartFormattedTextField.setDisable(true);
				longAtStartFormattedTextField.setDisable(true);
				timeAtStartLocalTimeTextField.setLocalTime(null);
				latAtStartFormattedTextField.setText("00°00.00'N");
				longAtStartFormattedTextField.setText("000°00.00'W");
				
				timeAndPositionAtIntoSeaCheckBox.setSelected(false);
				logbook2LetterL66SEACheckBox.setSelected(false);
				logbook2LetterL67CheckBox.setSelected(false);
				logbook2LetterL68CheckBox.setSelected(false);
				timeAndPositionAtEndCheckBox.setSelected(false);

			}

		});
		timeAndPositionAtIntoSeaCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				timeAtIntoSeaLocalTimeTextField.setDisable(false);
				latAtIntoSeaFormattedTextField.setDisable(false);
				longAtIntoSeaFormattedTextField.setDisable(false);

			} else {
				timeAtIntoSeaLocalTimeTextField.setDisable(true);
				latAtIntoSeaFormattedTextField.setDisable(true);
				longAtIntoSeaFormattedTextField.setDisable(true);
				timeAtIntoSeaLocalTimeTextField.setLocalTime(null);
				latAtIntoSeaFormattedTextField.setText("00°00.00'N");
				longAtIntoSeaFormattedTextField.setText("000°00.00'W");

			}

		});
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
		try {
			uploadReceiptImageViewButton.setImage(new Image(getClass().getResourceAsStream("/images/uploadReceipt.png")));

		} catch (Exception e) {
			System.out.print(e);
		}
		uploadReceiptImageViewButton.setDisable(true);
		uploadReceiptImageViewButton.setOnMouseClicked(me -> {
			File selectedFile = null;
			try {
				selectedFile = fileChooser.showOpenDialog((Stage) ((ImageViewButton) me.getSource()).getScene().getWindow());
			} catch (IllegalArgumentException e) {
				fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
				selectedFile = fileChooser.showOpenDialog((Stage) ((ImageViewButton) me.getSource()).getScene().getWindow());
			}
			if (selectedFile != null) {
				properties.setProperty("logbookReceiptDir", selectedFile.getParent());
				File f = new File(CACHE_FILE_NAME);
				if (!f.exists())
					f.getParentFile().mkdirs();
				OutputStream out;
				try {
					out = new FileOutputStream(f);
					properties.store(out, "Last directory choosed by user");
					out.close();
				} catch (IOException ex) {
					logger.error("Last directory choosed by user " + ex.toString());
				}
				logbook2LetterL66ReceptionTextField.setText(selectedFile.getName());
				logbook2LetterL66ReceptionTextField.setUserData(selectedFile);
			}
		});
		logbook2LetterL66CheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterL66SEACheckBox.setSelected(true);
				logbook2LetterL66PORTCheckBox.setDisable(false);
				logbook2LetterL66SEACheckBox.setDisable(false);

			} else {
				logbook2LetterL66SEACheckBox.setDisable(true);
				logbook2LetterL66PORTCheckBox.setDisable(true);
				logbook2LetterL66SEACheckBox.setSelected(false);
				logbook2LetterL66PORTCheckBox.setSelected(false);

			}

		});
		logbook2LetterL66SEACheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterL66PORTCheckBox.setSelected(false);
				logbook2LetterL66PORTCheckBox.setDisable(false);
				Double totalUserTankQuantities = ((TankUsageContext) identifyOfTanksJFXChipView.getUserData()).getTotalUserTankQuantities();
				logbook2LetterL66SEADoubleField.setValue(totalUserTankQuantities);

				timeAndPositionAtStartCheckBox.setSelected(true);
				timeAndPositionAtIntoSeaCheckBox.setSelected(true);
				logbook2LetterL67CheckBox.setSelected(true);
				logbook2LetterL68CheckBox.setSelected(true);
				timeAndPositionAtEndCheckBox.setSelected(true);
				
				timeAndPositionAtStartCheckBox.setDisable(false);
				timeAndPositionAtIntoSeaCheckBox.setDisable(false);
				logbook2LetterL67CheckBox.setDisable(false);
				logbook2LetterL68CheckBox.setDisable(false);
				timeAndPositionAtEndCheckBox.setDisable(false);

			} else {

				logbook2LetterL66SEADoubleField.setText("");
				timeAndPositionAtStartCheckBox.setSelected(false);
				timeAndPositionAtIntoSeaCheckBox.setSelected(false);
				logbook2LetterL67CheckBox.setSelected(false);
				logbook2LetterL68CheckBox.setSelected(false);
				timeAndPositionAtEndCheckBox.setSelected(false);
				
			}

		});

		logbook2LetterL66PORTCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterL66SEACheckBox.setSelected(false);
				logbook2LetterL66SEACheckBox.setDisable(false);
				logbook2LetterL66PORTTextField.setDisable(false);
				//logbook2LetterL66ReceptionTextField.setDisable(false);
				uploadReceiptImageViewButton.setDisable(false);
				Double totalUserTankQuantities = ((TankUsageContext) identifyOfTanksJFXChipView.getUserData()).getTotalUserTankQuantities();
				logbook2LetterL66PORTDoubleField.setValue(totalUserTankQuantities);

				timeAndPositionAtStartCheckBox.setSelected(false);
				timeAndPositionAtIntoSeaCheckBox.setSelected(false);
				logbook2LetterL67CheckBox.setSelected(false);
				logbook2LetterL68CheckBox.setSelected(false);
				timeAndPositionAtEndCheckBox.setSelected(false);

				timeAndPositionAtStartCheckBox.setDisable(true);
				timeAndPositionAtIntoSeaCheckBox.setDisable(true);
				logbook2LetterL67CheckBox.setDisable(true);
				logbook2LetterL68CheckBox.setDisable(true);
				timeAndPositionAtEndCheckBox.setDisable(true);

			} else {
				logbook2LetterL66PORTTextField.setDisable(true);
				logbook2LetterL66ReceptionTextField.setDisable(true);
				uploadReceiptImageViewButton.setDisable(true);
				logbook2LetterL66PORTTextField.setText("");
				logbook2LetterL66ReceptionTextField.setText("");
				logbook2LetterL66PORTDoubleField.setText("");

				timeAndPositionAtStartCheckBox.setDisable(false);
				timeAndPositionAtIntoSeaCheckBox.setDisable(false);
				logbook2LetterL67CheckBox.setDisable(false);
				logbook2LetterL68CheckBox.setDisable(false);
				timeAndPositionAtEndCheckBox.setDisable(false);
				
			}

		});

		logbook2LetterL67CheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterL67YESCheckBox.setDisable(false);
				logbook2LetterL67NOTCheckBox.setDisable(false);

			} else {
				logbook2LetterL67YESCheckBox.setDisable(true);
				logbook2LetterL67NOTCheckBox.setDisable(true);
				logbook2LetterL67YESCheckBox.setSelected(false);
				logbook2LetterL67NOTCheckBox.setSelected(false);

			}

		});

		logbook2LetterL67YESCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterL67NOTCheckBox.setSelected(false);

			} else {

			}

		});

		logbook2LetterL67NOTCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterL67YESCheckBox.setSelected(false);
				logbook2LetterL67TextField.setDisable(false);
			} else {
				logbook2LetterL67TextField.setDisable(true);
				logbook2LetterL67TextField.setText("");
			}

		});
		logbook2LetterL68CheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterL68YESCheckBox.setDisable(false);
				logbook2LetterL68NOTCheckBox.setDisable(false);

			} else {
				logbook2LetterL68YESCheckBox.setDisable(true);
				logbook2LetterL68NOTCheckBox.setDisable(true);
				logbook2LetterL68YESCheckBox.setSelected(false);
				logbook2LetterL68NOTCheckBox.setSelected(false);

			}

		});

		logbook2LetterL68YESCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterL68NOTCheckBox.setSelected(false);

			} else {

			}

		});

		logbook2LetterL68NOTCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterL68YESCheckBox.setSelected(false);
				logbook2LetterL68TextField.setDisable(false);
			} else {
				logbook2LetterL68TextField.setDisable(true);
				logbook2LetterL68TextField.setText("");
			}

		});

		timeAndPositionAtEndCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				timeAtEndLocalTimeTextField.setDisable(false);
				latAtEndFormattedTextField.setDisable(false);
				longAtEndFormattedTextField.setDisable(false);

			} else {
				timeAtEndLocalTimeTextField.setDisable(true);
				latAtEndFormattedTextField.setDisable(true);
				longAtEndFormattedTextField.setDisable(true);
				timeAtEndLocalTimeTextField.setLocalTime(null);
				latAtEndFormattedTextField.setText("00°00.00'N");
				longAtEndFormattedTextField.setText("000°00.00'W");

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

	public void openIdentifyOfTanksL63(Event e) {
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

	public void letterL(MouseEvent event) {
		helpCodeLetterDialog.showDialog((Stage)((ImageViewButton)event.getSource()).getScene().getWindow(),helpImageList);
	}

	public void processL(OilLog logToInsert, List<OilLog> logToInsertList, Map<Validation, Object> validations) {
		validateFields(logToInsert.getBusinessDate(), validations);
		if (logbook2LetterL63CheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("63");
			log.setOrderInGroup("ZZU");
			log.setContent("" + String.join(", ", identifyOfTanksJFXChipView.getChips()));
			log.setUserTankActualLevels(((TankUsageContext) identifyOfTanksJFXChipView.getUserData()).getUserTankActualLevels());
			logToInsertList.add(log);
		}
		if (timeAndPositionAtStartCheckBox.isSelected() && timeAtStartLocalTimeTextField.getLocalTime()!=null) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("64");
			log.setOrderInGroup("ZZV");
			log.setContent("TIME " + timeAtStartLocalTimeTextField.getLocalTime().format(timeFormatter) + " LAT:" + latAtStartFormattedTextField.getText() + " LONG:" + longAtStartFormattedTextField.getText());
			logToInsertList.add(log);
		}
		if (timeAndPositionAtIntoSeaCheckBox.isSelected() && timeAtIntoSeaLocalTimeTextField.getLocalTime()!=null) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("65");
			log.setOrderInGroup("ZZW");
			log.setContent("TIME " + timeAtIntoSeaLocalTimeTextField.getLocalTime().format(timeFormatter) + " LAT:" + latAtIntoSeaFormattedTextField.getText() + " LONG:" + longAtIntoSeaFormattedTextField.getText());
			logToInsertList.add(log);
		}
		if (logbook2LetterL66SEACheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("66.1");
			log.setOrderInGroup("ZZX");
			log.setContent(logbook2LetterL66SEADoubleField.getText() + "M3");
			logToInsertList.add(log);
		}

		if (logbook2LetterL66PORTCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("66.2");
			log.setOrderInGroup("ZZY");
			log.setContent(logbook2LetterL66PORTDoubleField.getText() + "M3 ," + logbook2LetterL66PORTTextField.getText() + ", " + logbook2LetterL66ReceptionTextField.getText());
			if (logbook2LetterL66ReceptionTextField.getUserData() != null && ((File) logbook2LetterL66ReceptionTextField.getUserData()).exists()) {
				log.setAttachmentName(((File) logbook2LetterL66ReceptionTextField.getUserData()).getName());
				byte[] attachmentBytes = null;
				try {
					attachmentBytes = Files.readAllBytes(((File) logbook2LetterL66ReceptionTextField.getUserData()).toPath());
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
				log.setAttachmentData(attachmentBytes);
			}
			logToInsertList.add(log);
		}
		if (logbook2LetterL67YESCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("67");
			log.setOrderInGroup("ZZZ");
			log.setContent("YES");
			logToInsertList.add(log);
		}
		if (logbook2LetterL67NOTCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("67");
			log.setOrderInGroup("ZZZA");
			log.setContent("NOT " + logbook2LetterL67TextField.getText());
			logToInsertList.add(log);
		}

		if (logbook2LetterL68YESCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("68");
			log.setOrderInGroup("ZZZB");
			log.setContent("YES");
			logToInsertList.add(log);
		}
		if (logbook2LetterL68NOTCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("68");
			log.setOrderInGroup("ZZZC");
			log.setContent("NOT " + logbook2LetterL68TextField.getText());
			logToInsertList.add(log);
		}
		if (timeAndPositionAtEndCheckBox.isSelected() && timeAtEndLocalTimeTextField.getLocalTime()!=null) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("69");
			log.setOrderInGroup("ZZZD");
			log.setContent("TIME " + timeAtEndLocalTimeTextField.getLocalTime().format(timeFormatter) + " LAT:" + latAtEndFormattedTextField.getText() + " LONG:" + longAtEndFormattedTextField.getText());
			logToInsertList.add(log);
		}
	}

	private void validateFields(Timestamp businessDate, Map<Validation, Object> validations) {
		if (!logbook2LetterL63CheckBox.isSelected() || identifyOfTanksJFXChipView.getChips().isEmpty()) {
			validations.put(Validation.L63_EMPTY_TANKS, null);
		}

		if (!timeAndPositionAtStartCheckBox.isSelected() && logbook2LetterL66SEACheckBox.isSelected()) {
			validations.put(Validation.L64_EMPTY, null);
		}
		if (timeAndPositionAtStartCheckBox.isSelected() && (latAtStartFormattedTextField.getText().equals("00°00.00'N") || longAtStartFormattedTextField.getText().equals("000°00.00'W"))) {

			validations.put(Validation.L64_LAT_LON_LAT_LON_ZERO, null);
		}
		if (timeAndPositionAtStartCheckBox.isSelected() && timeAtStartLocalTimeTextField.getLocalTime()==null) {

			validations.put(Validation.L64_EMPTY_TIME, null);
		}
		if ((timeAtStartLocalTimeTextField.getLocalTime()!=null && timeAtEndLocalTimeTextField.getLocalTime()!=null) && timeAtStartLocalTimeTextField.getLocalTime().isAfter(timeAtEndLocalTimeTextField.getLocalTime())) {
			validations.put(Validation.L64_TIME_AT_START_AFTER_TIME_AT_STOP, null);
		}
		if (!timeAndPositionAtIntoSeaCheckBox.isSelected() && logbook2LetterL66SEACheckBox.isSelected()) {
			validations.put(Validation.L65_EMPTY, null);
		}
		if (timeAndPositionAtIntoSeaCheckBox.isSelected() && (latAtIntoSeaFormattedTextField.getText().equals("00°00.00'N") || longAtIntoSeaFormattedTextField.getText().equals("000°00.00'W"))) {

			validations.put(Validation.L65_LAT_LON_LAT_LON_ZERO, null);
		}
		if (timeAndPositionAtIntoSeaCheckBox.isSelected() && timeAtIntoSeaLocalTimeTextField.getLocalTime()==null) {

			validations.put(Validation.L65_EMPTY_TIME, null);
		}
		if (!logbook2LetterL66SEACheckBox.isSelected() && !logbook2LetterL66PORTCheckBox.isSelected()) {
			validations.put(Validation.L66_EMPTY, null);
		}
		if (logbook2LetterL66PORTCheckBox.isSelected() && (logbook2LetterL66PORTTextField.getText().isEmpty() || logbook2LetterL66ReceptionTextField.getText().isEmpty())) {
			validations.put(Validation.L66_EMPTY_RECEPTION, null);
		}
		if (!logbook2LetterL67YESCheckBox.isSelected() && !logbook2LetterL67NOTCheckBox.isSelected() && logbook2LetterL66SEACheckBox.isSelected()) {
			validations.put(Validation.L67_EMPTY, null);
		}
		if (logbook2LetterL67NOTCheckBox.isSelected() && logbook2LetterL67TextField.getText().isEmpty()) {
			validations.put(Validation.L67_EMPTY, null);
		}
		
		if (!logbook2LetterL68YESCheckBox.isSelected() && !logbook2LetterL68NOTCheckBox.isSelected() && logbook2LetterL66SEACheckBox.isSelected()) {
			validations.put(Validation.L68_EMPTY, null);
		}
		if (logbook2LetterL68NOTCheckBox.isSelected() && logbook2LetterL68TextField.getText().isEmpty()) {
			validations.put(Validation.L68_EMPTY, null);
		}
		if (!timeAndPositionAtEndCheckBox.isSelected() && logbook2LetterL66SEACheckBox.isSelected()) {
			validations.put(Validation.L69_EMPTY, null);
		}
		if (timeAndPositionAtEndCheckBox.isSelected() && (latAtEndFormattedTextField.getText().equals("00°00.00'N") || longAtEndFormattedTextField.getText().equals("000°00.00'W"))) {

			validations.put(Validation.L69_LAT_LON_LAT_LON_ZERO, null);
		}
		if (timeAndPositionAtEndCheckBox.isSelected() && timeAtEndLocalTimeTextField.getLocalTime()==null) {

			validations.put(Validation.L69_EMPTY_TIME, null);
		}
	}

	public void showBalloon(MouseEvent event) {
		ballonMessage.showGeneralBalloon((ImageViewButton) event.getTarget(), label1);
	}

	public void clearFilledInfo() {
		logbook2LetterL63CheckBox.setSelected(false);
		timeAndPositionAtStartCheckBox.setSelected(false);
		timeAndPositionAtIntoSeaCheckBox.setSelected(false);
		logbook2LetterL66CheckBox.setSelected(false);
		logbook2LetterL67CheckBox.setSelected(false);
		logbook2LetterL68CheckBox.setSelected(false);
		timeAndPositionAtEndCheckBox.setSelected(false);

	}
}
