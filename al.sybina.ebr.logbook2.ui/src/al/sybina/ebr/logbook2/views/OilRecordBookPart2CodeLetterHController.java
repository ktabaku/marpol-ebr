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

public class OilRecordBookPart2CodeLetterHController implements Initializable {

	private Label label1 = new Label("Ships' master's should obtain from the operator of the reception\r\n" + "facilities which include barges and tank trucks. A receipt or certificates \r\n"
			+ "detailing the Quantity  or tank washings dirty ballast, residues or oily mixtures\r\n" + "transferred together with the time and date of the transfer. This receipt or certificate,\r\n "
			+ "if attached to the Oil Record Book Part II, may aid the master of the ship in proving that his\r\n " + "ship was not involved in an alleged pollution incident.\r\n"
			+ "The receipt or the certificates should be keept together with the Oil record book\r\n" + "" + "");

	private static final Logger logger = LoggerCreator.createLogger(OilRecordBookPart2CodeLetterHController.class);

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
	private CheckBox logbook2LetterH32CheckBox;
	@FXML
	private FormattedTextField logbook2LetterH34LatTextField;
	@FXML
	private CheckBox logbook2LetterH33CheckBox;
	@FXML
	private FormattedTextField logbook2LetterH34LongTextField;
	@FXML
	private JFXChipView<String> logbook2LetterHIdentifyOfTanksJFXChipView;
	@FXML
	private CheckBox logbook2LetterH34CheckBox;
	@FXML
	private FormattedTextField logbook2LetterH33LongTextField;
	@FXML
	private FormattedTextField logbook2LetterH33LatTextField;
	@FXML
	private ImageViewButton infoLetterAImageViewButton;
	@FXML
	private ImageViewButton infoLetterA3ImageViewButton;
	@FXML
	private ImageViewButton identityOfTanksImageViewButton;
	@FXML
	private LocalTimeTextField logbook2LetterH33TimeTextField;
	@FXML
	private LocalTimeTextField logbook2LetterH34TimeTextField;
	@FXML
	private CheckBox logbook2LetterH35CheckBox;
	@FXML
	private DoubleField logbook2LetterH35DoubleField;
	@FXML
	private CheckBox logbook2LetterH36CheckBox;
	@FXML
	private DoubleField logbook2LetterH36DoubleField;
	@FXML
	private CheckBox logbook2LetterH37CheckBox;
	@FXML
	private CheckBox logbook2LetterH37YESCheckBox;
	@FXML
	private CheckBox logbook2LetterH37NOTCheckBox;

	@FXML
	private CheckBox logbook2LetterH38CheckBox;
	@FXML
	private CheckBox logbook2LetterH38YESCheckBox;
	@FXML
	private CheckBox logbook2LetterH38NOTCheckBox;

	@FXML
	private CheckBox logbook2LetterH39CheckBox;
	@FXML
	private ImageViewButton logbook2LetterH39ImageViewButton;
	@FXML
	private JFXChipView<String> logbook2LetterH39IdentifyOfTanksJFXChipView;
	@FXML
	private DoubleField logbook2LetterH39DoubleField;

	@FXML
	private CheckBox logbook2LetterH40CheckBox;
	@FXML
	private TextField logbook2LetterH40TextField;
	@FXML
	private DoubleField logbook2LetterH40DoubleField;
	@FXML
	private TextField logbook2LetterH40ReceptionFacilitiesTextField;
	@FXML
	private ImageViewButton uploadReceiptImageViewButton;
	@FXML
	private Label remarks37Label;
	@FXML
	private TextField remarks37TextField;
	@FXML
	private Label remarks38Label;
	@FXML
	private TextField remarks38TextField;

	protected String CACHE_FILE_NAME = System.getProperty("user.home") + "/.ebr/caches/caches.properties";
	@FXML
	public AnchorPane codeLetterContentAnchorPane;
	private DateTimeFormatter timeFormatter = EasyDateUtils.logbookTimeFormatter;
	private ObservableList<Image> helpImageList = FXCollections.observableArrayList();
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MathUtils.setStringConverter(logbook2LetterH35DoubleField);
		MathUtils.setStringConverter(logbook2LetterH36DoubleField);
		MathUtils.setStringConverter(logbook2LetterH39DoubleField);
		MathUtils.setStringConverter(logbook2LetterH40DoubleField);
		MathUtils.setMinMaxValues(logbook2LetterH35DoubleField, 0.0, null);
		MathUtils.setMinMaxValues(logbook2LetterH36DoubleField, 0.0, null);
		MathUtils.setMinMaxValues(logbook2LetterH39DoubleField, 0.0, null);
		MathUtils.setMinMaxValues(logbook2LetterH40DoubleField, 0.0, null);
		try {

			Image helpImageLetterH2 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPH2));
			
			Image helpImageLetterH1 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPH1));
			
			Image helpImageLetterH3 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPH3));
			
			Image helpImageLetterH4 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPH4));
			
			helpImageList.addAll(helpImageLetterH4, helpImageLetterH1, helpImageLetterH3, helpImageLetterH2);
			
			Image addImageIdentityOfTanks = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_ADD));
			identityOfTanksImageViewButton.setImage(addImageIdentityOfTanks);
			logbook2LetterH39ImageViewButton.setImage(addImageIdentityOfTanks);

			Image infoIconImageInfoLetterA = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
			infoLetterAImageViewButton.setImage(infoIconImageInfoLetterA);
			infoLetterA3ImageViewButton.setImage(infoIconImageInfoLetterA);
		} catch (IOException e) {
			e.printStackTrace();
		}
		identityOfTanksImageViewButton.setDisable(true);
		logbook2LetterHIdentifyOfTanksJFXChipView.setDisable(true);
		TankUsageContext tankUsageContext = new TankUsageContext(Stream.of(TankType.BALLAST, TankType.CARGO, TankType.FUELOIL, TankType.OILYBILGE, TankType.SLOP, TankType.SLUDGE).collect(Collectors.toList()), false, true);
		logbook2LetterHIdentifyOfTanksJFXChipView.setUserData(tankUsageContext);
		addRemoveListener(logbook2LetterHIdentifyOfTanksJFXChipView);
		logbook2LetterHIdentifyOfTanksJFXChipView.getChips().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> change) {
				if (logbook2LetterH32CheckBox.isSelected()) {
					Double totalUserTankQuantities = ((TankUsageContext) logbook2LetterHIdentifyOfTanksJFXChipView.getUserData()).getTotalUserTankQuantities();
					Double totalUserTankActualLevel = ((TankUsageContext) logbook2LetterHIdentifyOfTanksJFXChipView.getUserData()).getTotalUserTankActualLevel();
					// slopCargoQuantityDoubleField.setValue(totalUserTankQuantities);
				}
				JavaFXUtils.addTooltip(logbook2LetterHIdentifyOfTanksJFXChipView);
			}

		});
		CoordinateUIUtils.initializeLatitudeField(logbook2LetterH33LatTextField);
		CoordinateUIUtils.initializeLongitudeField(logbook2LetterH33LongTextField);

		logbook2LetterH33LatTextField.setDisable(true);
		logbook2LetterH33LongTextField.setDisable(true);
		logbook2LetterH33TimeTextField.setDisable(true);
		logbook2LetterH33TimeTextField.setDateTimeFormatter(EasyDateUtils.logbookTimeFormatter);
		logbook2LetterH34TimeTextField.setDateTimeFormatter(EasyDateUtils.logbookTimeFormatter);
		remarks37TextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		remarks38TextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		CoordinateUIUtils.initializeLatitudeField(logbook2LetterH34LatTextField);
		CoordinateUIUtils.initializeLongitudeField(logbook2LetterH34LongTextField);
		logbook2LetterH34LatTextField.setDisable(true);
		logbook2LetterH34LongTextField.setDisable(true);
		logbook2LetterH34TimeTextField.setDisable(true);

		logbook2LetterH35DoubleField.setDisable(true);
		logbook2LetterH36DoubleField.setDisable(true);

		logbook2LetterH37YESCheckBox.setDisable(true);
		logbook2LetterH37NOTCheckBox.setDisable(true);

		logbook2LetterH38YESCheckBox.setDisable(true);
		logbook2LetterH38NOTCheckBox.setDisable(true);

		logbook2LetterH39ImageViewButton.setDisable(true);
		logbook2LetterH39IdentifyOfTanksJFXChipView.setDisable(true);
		logbook2LetterH39DoubleField.setDisable(true);

		logbook2LetterH40TextField.setDisable(true);
		logbook2LetterH40DoubleField.setDisable(true);
		logbook2LetterH40ReceptionFacilitiesTextField.setDisable(true);
		logbook2LetterH40TextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		JavaFXUtils.addFilledPseudoClass(logbook2LetterH33LatTextField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterH33LongTextField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterH34LatTextField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterH34LongTextField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterH35DoubleField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterH36DoubleField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterH39DoubleField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterH40TextField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterH40DoubleField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterH40ReceptionFacilitiesTextField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterH33TimeTextField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterH34TimeTextField);
		
		logbook2LetterH32CheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				identityOfTanksImageViewButton.setDisable(false);
				logbook2LetterHIdentifyOfTanksJFXChipView.setDisable(false);
				if (!logbook2LetterH40CheckBox.isSelected()) {
					logbook2LetterH33CheckBox.setSelected(true);
					logbook2LetterH34CheckBox.setSelected(true);
					logbook2LetterH35CheckBox.setSelected(true);
					logbook2LetterH36CheckBox.setSelected(true);
					logbook2LetterH37CheckBox.setSelected(true);
					logbook2LetterH38CheckBox.setSelected(true);
				}

			} else {
				identityOfTanksImageViewButton.setDisable(true);
				logbook2LetterHIdentifyOfTanksJFXChipView.getChips().clear();
				logbook2LetterHIdentifyOfTanksJFXChipView.setDisable(true);

				logbook2LetterH33CheckBox.setSelected(false);
				logbook2LetterH34CheckBox.setSelected(false);
				logbook2LetterH35CheckBox.setSelected(false);
				logbook2LetterH36CheckBox.setSelected(false);
				logbook2LetterH37CheckBox.setSelected(false);
				logbook2LetterH38CheckBox.setSelected(false);
			}

		});
		logbook2LetterH33CheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterH33LatTextField.setDisable(false);
				logbook2LetterH33LongTextField.setDisable(false);
				logbook2LetterH33TimeTextField.setDisable(false);
			} else {
				logbook2LetterH33LatTextField.setDisable(true);
				logbook2LetterH33LongTextField.setDisable(true);
				logbook2LetterH33LatTextField.setText("00°00.00'N");
				logbook2LetterH33LongTextField.setText("000°00.00'W");
				logbook2LetterH33TimeTextField.setDisable(true);
				logbook2LetterH33TimeTextField.setLocalTime(null);
			}

		});

		logbook2LetterH34CheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterH34LatTextField.setDisable(false);
				logbook2LetterH34LongTextField.setDisable(false);
				logbook2LetterH34TimeTextField.setDisable(false);
			} else {
				logbook2LetterH34LatTextField.setDisable(true);
				logbook2LetterH34LongTextField.setDisable(true);
				logbook2LetterH34LatTextField.setText("00°00.00'N");
				logbook2LetterH34LongTextField.setText("000°00.00'W");
				logbook2LetterH34TimeTextField.setDisable(true);
				logbook2LetterH34TimeTextField.setLocalTime(null);
			}

		});

		logbook2LetterH35CheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterH35DoubleField.setDisable(false);

			} else {
				logbook2LetterH35DoubleField.setDisable(true);
				logbook2LetterH35DoubleField.setText("");
			}

		});
		logbook2LetterH36CheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterH36DoubleField.setDisable(false);

			} else {
				logbook2LetterH36DoubleField.setDisable(true);
				logbook2LetterH36DoubleField.setText("");
			}

		});
		logbook2LetterH37CheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterH37YESCheckBox.setDisable(false);
				logbook2LetterH37NOTCheckBox.setDisable(false);

			} else {
				logbook2LetterH37YESCheckBox.setDisable(true);
				logbook2LetterH37NOTCheckBox.setDisable(true);
				logbook2LetterH37YESCheckBox.setSelected(false);
				logbook2LetterH37NOTCheckBox.setSelected(false);

			}

		});

		logbook2LetterH37YESCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterH37NOTCheckBox.setSelected(false);
			} else {

			}

		});

		logbook2LetterH37NOTCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterH37YESCheckBox.setSelected(false);
				remarks37Label.setVisible(true);
				remarks37TextField.setVisible(true);
			} else {
				remarks37TextField.clear();
				remarks37Label.setVisible(false);
				remarks37TextField.setVisible(false);
			}

		});

		logbook2LetterH38CheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterH38YESCheckBox.setDisable(false);
				logbook2LetterH38NOTCheckBox.setDisable(false);

			} else {
				logbook2LetterH38YESCheckBox.setDisable(true);
				logbook2LetterH38NOTCheckBox.setDisable(true);
				logbook2LetterH38YESCheckBox.setSelected(false);
				logbook2LetterH38NOTCheckBox.setSelected(false);

			}

		});

		logbook2LetterH38YESCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterH38NOTCheckBox.setSelected(false);
			} else {

			}

		});

		logbook2LetterH38NOTCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterH38YESCheckBox.setSelected(false);
				remarks38Label.setVisible(true);
				remarks38TextField.setVisible(true);
			} else {
				remarks38TextField.clear();
				remarks38Label.setVisible(false);
				remarks38TextField.setVisible(false);
			}
		});
		TankUsageContext tankUsageContext1 = new TankUsageContext(Stream.of(TankType.BALLAST, TankType.CARGO, TankType.FUELOIL, TankType.OILYBILGE, TankType.SLOP, TankType.SLUDGE).collect(Collectors.toList()), true, true);
		logbook2LetterH39IdentifyOfTanksJFXChipView.setUserData(tankUsageContext1);
		addRemoveListener(logbook2LetterH39IdentifyOfTanksJFXChipView);
		logbook2LetterH39IdentifyOfTanksJFXChipView.getChips().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> change) {
				if (logbook2LetterH32CheckBox.isSelected()) {
					Double totalUserTankQuantities = ((TankUsageContext) logbook2LetterH39IdentifyOfTanksJFXChipView.getUserData()).getTotalUserTankQuantities();
					Double totalUserTankActualLevel = ((TankUsageContext) logbook2LetterH39IdentifyOfTanksJFXChipView.getUserData()).getTotalUserTankActualLevel();
					logbook2LetterH39DoubleField.setValue(totalUserTankQuantities);
				}
				JavaFXUtils.addTooltip(logbook2LetterH39IdentifyOfTanksJFXChipView);
			}

		});
		logbook2LetterH39CheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterH39ImageViewButton.setDisable(false);
				logbook2LetterH39IdentifyOfTanksJFXChipView.setDisable(false);
				logbook2LetterH39DoubleField.setDisable(false);

			} else {
				logbook2LetterH39ImageViewButton.setDisable(true);
				logbook2LetterH39IdentifyOfTanksJFXChipView.setDisable(true);
				logbook2LetterH39DoubleField.setDisable(true);
				logbook2LetterH39DoubleField.setText("");
				logbook2LetterH39IdentifyOfTanksJFXChipView.getChips().clear();
			}

		});

		logbook2LetterH40CheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterH40TextField.setDisable(false);
				logbook2LetterH40DoubleField.setDisable(false);
				uploadReceiptImageViewButton.setDisable(false);
                logbook2LetterH33CheckBox.setSelected(false);
				logbook2LetterH34CheckBox.setSelected(false);
				logbook2LetterH35CheckBox.setSelected(false);
				logbook2LetterH36CheckBox.setSelected(false);
				logbook2LetterH37CheckBox.setSelected(false);
				logbook2LetterH38CheckBox.setSelected(false);

			} else {
				logbook2LetterH40TextField.setDisable(true);
				logbook2LetterH40DoubleField.setDisable(true);
				logbook2LetterH40ReceptionFacilitiesTextField.setDisable(true);
				logbook2LetterH40TextField.setText("");
				logbook2LetterH40ReceptionFacilitiesTextField.setText("");
				logbook2LetterH40DoubleField.setText("");
				uploadReceiptImageViewButton.setDisable(true);
				if (logbook2LetterH32CheckBox.isSelected()) {
					logbook2LetterH33CheckBox.setSelected(true);
					logbook2LetterH34CheckBox.setSelected(true);
					logbook2LetterH35CheckBox.setSelected(true);
					logbook2LetterH36CheckBox.setSelected(true);
					logbook2LetterH37CheckBox.setSelected(true);
					logbook2LetterH38CheckBox.setSelected(true);
				}
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
				logbook2LetterH40ReceptionFacilitiesTextField.setText(selectedFile.getName());
				logbook2LetterH40ReceptionFacilitiesTextField.setUserData(selectedFile);
			}
		});

		eventBroker.subscribe(Activator.MARPOL_DETAILS_RELOADED, new org.osgi.service.event.EventHandler() {
			@Override
			public void handleEvent(org.osgi.service.event.Event event) {
				ArrayList<Long> changedTankIds = (ArrayList<Long>) event.getProperty(Activator.TANK_IDS);
				if (changedTankIds == null) {
					logbook2LetterHIdentifyOfTanksJFXChipView.getChips().clear();
					logbook2LetterH39IdentifyOfTanksJFXChipView.getChips().clear();
				} else if (changedTankIds != null && !changedTankIds.isEmpty()) {
					Activator.removeChangedTanksFromJFXChipView(changedTankIds, logbook2LetterHIdentifyOfTanksJFXChipView);
					Activator.removeChangedTanksFromJFXChipView(changedTankIds, logbook2LetterH39IdentifyOfTanksJFXChipView);
				}
			}
		});
	}

	public void letterH(MouseEvent event) {
		helpCodeLetterDialog.showDialog((Stage)((ImageViewButton)event.getSource()).getScene().getWindow(),helpImageList);
	}

	public void openIdentifyOfTanksH32(Event e) {
		selectTankerDialog.showDialog((Stage) ((ImageViewButton) e.getSource()).getScene().getWindow(), logbook2LetterHIdentifyOfTanksJFXChipView);
	}

	public void openIdentifyOfTanksH39(Event e) {
		selectTankerDialog.showDialog((Stage) ((ImageViewButton) e.getSource()).getScene().getWindow(), logbook2LetterH39IdentifyOfTanksJFXChipView);
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

	public void processH(OilLog logToInsert, List<OilLog> logToInsertList, Map<Validation, Object> validations) {
		validateFields(logToInsert.getBusinessDate(), validations);
		if (logbook2LetterH32CheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("32");
			log.setOrderInGroup("ZK");
			log.setContent("" + String.join(", ", logbook2LetterHIdentifyOfTanksJFXChipView.getChips()));
			log.setUserTankActualLevels(((TankUsageContext) logbook2LetterHIdentifyOfTanksJFXChipView.getUserData()).getUserTankActualLevels());
			logToInsertList.add(log);
		}
		if (logbook2LetterH33CheckBox.isSelected() && logbook2LetterH33TimeTextField.getLocalTime()!=null) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("33");
			log.setOrderInGroup("ZL");
			log.setContent("TIME " + logbook2LetterH33TimeTextField.getLocalTime().format(timeFormatter) + " LAT:" + logbook2LetterH33LatTextField.getText() + " LONG:" + logbook2LetterH33LongTextField.getText());
			logToInsertList.add(log);
		}

		if (logbook2LetterH34CheckBox.isSelected() && logbook2LetterH34TimeTextField.getLocalTime()!=null) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("34");
			log.setOrderInGroup("ZM");
			log.setContent("TIME " + logbook2LetterH34TimeTextField.getLocalTime().format(timeFormatter) + " LAT:" + logbook2LetterH34LatTextField.getText() + " LONG:" + logbook2LetterH34LongTextField.getText());
			logToInsertList.add(log);
		}
		if (logbook2LetterH35CheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("35");
			log.setOrderInGroup("ZN");
			log.setContent(logbook2LetterH35DoubleField.getText() + "M3");
			logToInsertList.add(log);
		}
		if (logbook2LetterH36CheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("36");
			log.setOrderInGroup("ZO");
			log.setContent(logbook2LetterH36DoubleField.getText() + "KNTS");
			logToInsertList.add(log);
		}
		if (logbook2LetterH37CheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("37");
			log.setOrderInGroup("ZP");
			if (logbook2LetterH37YESCheckBox.isSelected()) {
				log.setContent("YES");
			} else if (logbook2LetterH37NOTCheckBox.isSelected()) {
				log.setContent("NOT" + remarks37TextField.getText()==null || remarks37TextField.getText().equals("") ? "" : " " + remarks37TextField.getText());
			}

			logToInsertList.add(log);
		}
		if (logbook2LetterH38CheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("38");
			log.setOrderInGroup("ZQ");
			if (logbook2LetterH38YESCheckBox.isSelected()) {
				log.setContent("YES");
			} else if (logbook2LetterH38NOTCheckBox.isSelected()) {
				log.setContent("NOT" + remarks38TextField.getText()==null || remarks38TextField.getText().equals("") ? "" : " " + remarks38TextField.getText());
			}

			logToInsertList.add(log);
		}

		if (logbook2LetterH39CheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("39");
			log.setOrderInGroup("ZR");
			log.setContent("" + String.join(", ", logbook2LetterH39IdentifyOfTanksJFXChipView.getChips() + " Total Quantity " + logbook2LetterH39DoubleField.getText() + "MC"));
			log.setUserTankActualLevels(((TankUsageContext) logbook2LetterH39IdentifyOfTanksJFXChipView.getUserData()).getUserTankActualLevels());
			logToInsertList.add(log);
		}
		if (logbook2LetterH40CheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("40");
			log.setOrderInGroup("ZS");
			// Map<Tank, Double> userTankActualLevels = new HashMap<Tank, Double>();
			StringBuilder sb = new StringBuilder();
			if (sb.length() > 0)
				sb.append("\n");
			sb.append(logbook2LetterH40TextField.getText());
			sb.append(logbook2LetterH40DoubleField.getText() + "M3");
			sb.append(logbook2LetterH40ReceptionFacilitiesTextField.getText());
			log.setContent(sb.toString());
			if (logbook2LetterH40ReceptionFacilitiesTextField.getUserData() != null && ((File) logbook2LetterH40ReceptionFacilitiesTextField.getUserData()).exists()) {
				log.setAttachmentName(((File) logbook2LetterH40ReceptionFacilitiesTextField.getUserData()).getName());
				byte[] attachmentBytes = null;
				try {
					attachmentBytes = Files.readAllBytes(((File) logbook2LetterH40ReceptionFacilitiesTextField.getUserData()).toPath());
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
				log.setAttachmentData(attachmentBytes);
			}
			// log.setUserTankActualLevels(userTankActualLevels);
			logToInsertList.add(log);
		}
	}

	private void validateFields(Timestamp businessDate, Map<Validation, Object> validations) {
		if (!logbook2LetterH32CheckBox.isSelected() || logbook2LetterHIdentifyOfTanksJFXChipView.getChips().isEmpty()) {
			validations.put(Validation.H32_EMPTY_TANKS, null);
		}
		if (!logbook2LetterH33CheckBox.isSelected() && !logbook2LetterH40CheckBox.isSelected()) {
			validations.put(Validation.H33_EMPTY, null);
		}
		if (logbook2LetterH33CheckBox.isSelected() && (logbook2LetterH33LatTextField.getText().equals("00°00.00'N") || logbook2LetterH33LongTextField.getText().equals("000°00.00'W"))) {

			validations.put(Validation.H33_LAT_LON_LAT_LON_ZERO, null);
		}
		if (!logbook2LetterH34CheckBox.isSelected() && !logbook2LetterH40CheckBox.isSelected()) {
			validations.put(Validation.H34_EMPTY, null);
		}
		if (logbook2LetterH34CheckBox.isSelected() && (logbook2LetterH34LatTextField.getText().equals("00°00.00'N") || logbook2LetterH34LongTextField.getText().equals("000°00.00'W"))) {

			validations.put(Validation.H34_LAT_LON_LAT_LON_ZERO, null);
		}
		if (logbook2LetterH33CheckBox.isSelected() && logbook2LetterH33TimeTextField.getLocalTime() == null) {
			validations.put(Validation.H33_TIME_EMPTY, null);
		}
		if (logbook2LetterH34CheckBox.isSelected() && logbook2LetterH34TimeTextField.getLocalTime() == null) {
			validations.put(Validation.H34_TIME_EMPTY, null);
		}
		if ((logbook2LetterH33TimeTextField.getLocalTime()!=null && logbook2LetterH34TimeTextField.getLocalTime()!=null) && logbook2LetterH33TimeTextField.getLocalTime().isAfter(logbook2LetterH34TimeTextField.getLocalTime())) {
			validations.put(Validation.H33_TIME_AT_START_AFTER_TIME_AT_STOP, null);
		}
//		if (businessDate != null && logbook2LetterH33CheckBox.isSelected() && logbook2LetterH33TimeTextField.getLocalDateTime() != null) {
//			LocalDateTime registrationLocalDateTime = Instant.ofEpochMilli(businessDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime().plusDays(1);
//			if (logbook2LetterH33TimeTextField.getLocalDateTime().isAfter(registrationLocalDateTime)) {
//				validations.put(Validation.H33_TIME_AFTER_REGISTRATION_DATE, null);
//			}
//		}

//		if (businessDate != null && logbook2LetterH34CheckBox.isSelected() && logbook2LetterH34TimeTextField.getLocalDateTime() != null) {
//			LocalDateTime registrationLocalDateTime = Instant.ofEpochMilli(businessDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime().plusDays(1);
//			if (logbook2LetterH34TimeTextField.getLocalDateTime().isAfter(registrationLocalDateTime)) {
//				validations.put(Validation.H34_TIME_AFTER_REGISTRATION_DATE, null);
//			}
//		}

		if (!logbook2LetterH35CheckBox.isSelected() && !logbook2LetterH40CheckBox.isSelected()) {
			validations.put(Validation.H35_EMPTY, null);
		}
		if (logbook2LetterH35CheckBox.isSelected() && logbook2LetterH35DoubleField.getText().isEmpty()) {
			validations.put(Validation.H35_EMPTY, null);
		}
		if (!logbook2LetterH36CheckBox.isSelected() && !logbook2LetterH40CheckBox.isSelected() && logbook2LetterH36DoubleField.getText().isEmpty()) {
			validations.put(Validation.H36_EMPTY, null);
		}
		if (logbook2LetterH36CheckBox.isSelected() && logbook2LetterH36DoubleField.getText().isEmpty()) {
			validations.put(Validation.H36_EMPTY, null);
		}
		if (!logbook2LetterH37YESCheckBox.isSelected() && !logbook2LetterH37NOTCheckBox.isSelected() && !logbook2LetterH40CheckBox.isSelected()) {
			validations.put(Validation.H37_EMPTY, null);
		}
		if (logbook2LetterH37NOTCheckBox.isSelected() && remarks37TextField.getText().isEmpty()) {
			validations.put(Validation.H37_EMPTY, null);
		}
		if (!logbook2LetterH38YESCheckBox.isSelected() && !logbook2LetterH38NOTCheckBox.isSelected() && !logbook2LetterH40CheckBox.isSelected()) {
			validations.put(Validation.H38_EMPTY, null);
		}
		if (logbook2LetterH38NOTCheckBox.isSelected() && remarks38TextField.getText().isEmpty()) {
			validations.put(Validation.H38_EMPTY, null);
		}
		if (logbook2LetterH39CheckBox.isSelected() && logbook2LetterH39IdentifyOfTanksJFXChipView.getChips().isEmpty()) {
			validations.put(Validation.H39_EMPTY_TANKS, null);
		}
		
		if (logbook2LetterH40CheckBox.isSelected() && (logbook2LetterH40TextField.getText().isEmpty() || logbook2LetterH40DoubleField.getText().isEmpty() ||
			logbook2LetterH40ReceptionFacilitiesTextField.getText().isEmpty())) {
			validations.put(Validation.H40_EMPTY, null);
		}
	}

	public void showBalloon(MouseEvent event) {
		ballonMessage.showGeneralBalloon((ImageViewButton) event.getTarget(), label1);
	}

	public void clearFilledInfo() {
		logbook2LetterH32CheckBox.setSelected(false);
		logbook2LetterH33CheckBox.setSelected(false);
		logbook2LetterH34CheckBox.setSelected(false);
		logbook2LetterH35CheckBox.setSelected(false);
		logbook2LetterH36CheckBox.setSelected(false);
		logbook2LetterH37CheckBox.setSelected(false);
		logbook2LetterH38CheckBox.setSelected(false);
		logbook2LetterH39CheckBox.setSelected(false);
		logbook2LetterH40CheckBox.setSelected(false);
	}
}
