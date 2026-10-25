package al.sybina.ebr.logbook2.views;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
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
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jfxtras.scene.control.ImageViewButton;
import jidefx.scene.control.field.DoubleField;

public class OilRecordBookPart2CodeLetterJController implements Initializable {

	private Label label1 = new Label(" Notes:\r\n" + "The Master should obtain from the operator of \r\n" + "the reception facilities, which includes barges \r\n" + "and tank trucks, a receipt or certificate detailing \r\n"
			+ "the quantity of the slops disposed, together with \r\n" + "the time and date of the transfer. This receipt or\r\n" + "certificate should be kept together with the ORB \r\n" + "- Part II\r\n" + "");

	private Label label2 = new Label(" Any arrangement provided for machinery space oily bilge water transfer to\r\n" + "slop tanks should incorporate\r\n" + "adequate means to prevent any backflow of liquid cargo and gases into\r\n"
			+ "the machinery spaces.\r\n" + "\r\n" + "For such an entry to be made, it should be explicitly stated in item 3.2.3 of \r\n" + "the Supplement to the IOPPC\r\n"
			+ "That this method is allowed. (In such cases where a vessel has a waiver for \r\n" + "oil filtering equipment\r\n" + "(15ppm) under MARPOL Regulation 14, item 2.5.3 of the Supplement\r\n"
			+ "to the IOPPC may be applicable.)\r\n" + "\r\n" + "Prior to any such transfer the Company's agreement should be requested.\r\n" + "An entry in the ORB - Part I (Machinery space operations) must also be \r\n"
			+ "made using code D.13/14/15.3.\r\n" + "The identity of the oily bilge water holding tank recorded in this example \r\n" + "should be in strict compliance\r\n"
			+ "with item 3.3 of the supplement of the IOPPC. (In such cases where a \r\n" + "vessel has a waiver for oil filtering\r\n" + "equipment (15ppm) under MARPOL Regulation 14, the identity of the \r\n"
			+ "holding lank should comply with the Table of item 2.5.2 of the Supplement 10 the IOPPC.)\r\n" + "");

	private static final Logger logger = LoggerCreator.createLogger(OilRecordBookPart2CodeLetterJController.class);

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
	private ToggleGroup transferType;
	@FXML
	private Toggle dischargedOrDisposedToggle;
	@FXML
	private Toggle transferredToggle;
	@FXML
	private CheckBox logbook2LetterJ55CheckBox;
	@FXML
	private ImageViewButton identityOfTanksImageViewButton;
	@FXML
	private JFXChipView<String> identifyOfTanksDisposedJFXChipView;
	@FXML
	private JFXChipView<String> identifyOfTanksTransferedJFXChipView;
	@FXML
	private Label textTypeLabel;
	@FXML
	private HBox tankerTransferedOrDisposedQuantityHBox;
	@FXML
	private HBox tankerRetainedQuantityHBox;
	@FXML
	private CheckBox logbook2LetterJ56QuantityCheckBox;
	@FXML
	private CheckBox logbook2LetterJ57CheckBox;
	@FXML
	private CheckBox logbook2LetterJ57DisposedCheckBox;
	@FXML
	private TextField logbook2LetterJ57DisposedPortTextField;
	@FXML
	private DoubleField logbook2LetterJ57DisposedQuantityDoubleField;
	@FXML
	private TextField logbook2LetterJ57DisposedBDNTextField;
	@FXML
	private TextField logbook2LetterJMixedWithCargoTextField;
	@FXML
	private ImageViewButton uploadReceiptImageViewButton;
	@FXML
	private CheckBox logbook2LetterJ57MixedCheckBox;
	@FXML
	private DoubleField logbook2LetterJ57MixedQuantityDoubleField;
	@FXML
	private CheckBox logbook2LetterJ57TransferedCheckBox;
	@FXML
	private ImageViewButton identityOfTanksJ57ImageViewButton;
	@FXML
	private JFXChipView<String> logbook2LetterJ57TransferedJFXChipView;
	@FXML
	private DoubleField logbook2LetterJ57TransferedQuantityDoubleField;
	@FXML
	private DoubleField logbook2LetterJ57RetainedQuantityDoubleField;
	@FXML
	private CheckBox logbook2LetterJ57MixedOneCheckBox;
	@FXML
	private DoubleField logbook2LetterJ57MixedOneQuantityDoubleField;
	@FXML
	private TextField totalQuantityOfOilLoadedTextField;
	@FXML
	private TextField totalContentOfTanksTextField;
	@FXML
	private ImageViewButton infoLetterAImageViewButton;
	@FXML
	private ImageViewButton infoLetterJImageViewButton;
	@FXML
	private ImageViewButton infoLetterJ2ImageViewButton;
	protected String CACHE_FILE_NAME = System.getProperty("user.home") + "/.ebr/caches/caches.properties";
	@FXML
	public AnchorPane codeLetterContentAnchorPane;
	private ObservableList<Image> helpImageList = FXCollections.observableArrayList();
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MathUtils.setStringConverter(logbook2LetterJ57DisposedQuantityDoubleField);
		MathUtils.setStringConverter(logbook2LetterJ57MixedQuantityDoubleField);
		MathUtils.setStringConverter(logbook2LetterJ57TransferedQuantityDoubleField);
		MathUtils.setStringConverter(logbook2LetterJ57RetainedQuantityDoubleField);
		MathUtils.setStringConverter(logbook2LetterJ57MixedOneQuantityDoubleField);
		MathUtils.setMinMaxValues(logbook2LetterJ57DisposedQuantityDoubleField, 0.0, null);
		MathUtils.setMinMaxValues(logbook2LetterJ57MixedQuantityDoubleField, 0.0, null);
		MathUtils.setMinMaxValues(logbook2LetterJ57TransferedQuantityDoubleField, 0.0, null);
		MathUtils.setMinMaxValues(logbook2LetterJ57RetainedQuantityDoubleField, 0.0, null);
		MathUtils.setMinMaxValues(logbook2LetterJ57MixedOneQuantityDoubleField, 0.0, null);
		try {

			Image helpImageLetterJ2 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPJ2));
			
			Image helpImageLetterJ1 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPJ1));
			
			Image helpImageLetterJ3 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPJ3));
			
			Image helpImageLetterJ4 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPJ4));
			
			Image helpImageLetterJ5 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPJ5));
			
			helpImageList.addAll(helpImageLetterJ5, helpImageLetterJ1, helpImageLetterJ2, helpImageLetterJ3, helpImageLetterJ4);
			
			Image addImageIdentityOfTanks = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_ADD));
			identityOfTanksImageViewButton.setImage(addImageIdentityOfTanks);

			identityOfTanksJ57ImageViewButton.setImage(addImageIdentityOfTanks);

			Image infoIconImageInfoLetterA = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
			infoLetterAImageViewButton.setImage(infoIconImageInfoLetterA);
			infoLetterJImageViewButton.setImage(infoIconImageInfoLetterA);
			infoLetterJ2ImageViewButton.setImage(infoIconImageInfoLetterA);
		} catch (IOException e) {
			e.printStackTrace();
		}
		JavaFXUtils.addFilledPseudoClass(logbook2LetterJ57DisposedPortTextField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterJ57DisposedQuantityDoubleField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterJ57MixedQuantityDoubleField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterJ57TransferedQuantityDoubleField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterJ57RetainedQuantityDoubleField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterJ57DisposedBDNTextField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterJ57MixedOneQuantityDoubleField);
		
		
		TankUsageContext tankUsageContextMultiTanks = new TankUsageContext(Stream.of(TankType.BALLAST, TankType.CARGO, TankType.FUELOIL, TankType.OILYBILGE, TankType.SLOP, TankType.SLUDGE).collect(Collectors.toList()), false, true);

		identityOfTanksImageViewButton.setDisable(true);
		identityOfTanksImageViewButton.setVisible(false);
		identifyOfTanksDisposedJFXChipView.setDisable(true);
		identifyOfTanksDisposedJFXChipView.setUserData(tankUsageContextMultiTanks);
		addRemoveListener(identifyOfTanksDisposedJFXChipView);
		identifyOfTanksDisposedJFXChipView.setVisible(false);

		identifyOfTanksTransferedJFXChipView.setDisable(true);
		identifyOfTanksTransferedJFXChipView.setUserData(tankUsageContextMultiTanks);
		addRemoveListener(identifyOfTanksTransferedJFXChipView);
		identifyOfTanksTransferedJFXChipView.setVisible(false);
		logbook2LetterJ57DisposedPortTextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		logbook2LetterJMixedWithCargoTextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());

		transferType.getToggles().forEach(toggle -> {
			toggle.setSelected(false);
			((RadioButton) toggle).setDisable(true);
		});
		logbook2LetterJ55CheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					transferType.getToggles().forEach(toggle -> ((RadioButton) toggle).setDisable(false));
				} else {
					dischargedOrDisposedToggle.setSelected(false);
					transferredToggle.setSelected(false);
					identityOfTanksImageViewButton.setDisable(true);
					identifyOfTanksDisposedJFXChipView.setDisable(true);
					identifyOfTanksDisposedJFXChipView.getChips().clear();

					identifyOfTanksTransferedJFXChipView.setDisable(true);
					identifyOfTanksTransferedJFXChipView.getChips().clear();
					transferType.getToggles().forEach(toggle -> {
						toggle.setSelected(false);
						((RadioButton) toggle).setDisable(true);
					});
				}
			}

		});

		transferType.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				if (newValue == (dischargedOrDisposedToggle)) {
					if (logbook2LetterJ55CheckBox.isSelected()) {
						identifyOfTanksTransferedJFXChipView.getChips().clear();

						identifyOfTanksDisposedJFXChipView.getChips().clear();
						identityOfTanksImageViewButton.setDisable(false);
						identityOfTanksImageViewButton.setVisible(true);
						identifyOfTanksDisposedJFXChipView.setDisable(false);
						identifyOfTanksDisposedJFXChipView.setVisible(true);
						textTypeLabel.setText("Disposed");
						identifyOfTanksTransferedJFXChipView.setVisible(false);

						logbook2LetterJ56QuantityCheckBox.setSelected(true);
						logbook2LetterJ57CheckBox.setSelected(true);

						logbook2LetterJ57DisposedCheckBox.setDisable(false);
						logbook2LetterJ57DisposedCheckBox.setSelected(true);

						logbook2LetterJ57MixedCheckBox.setDisable(true);
						logbook2LetterJ57MixedCheckBox.setSelected(false);
						logbook2LetterJ57TransferedCheckBox.setDisable(true);
						logbook2LetterJ57TransferedCheckBox.setSelected(false);
						logbook2LetterJ57MixedOneCheckBox.setDisable(true);
						logbook2LetterJ57MixedOneCheckBox.setSelected(false);
					}
				} else if (newValue == (transferredToggle)) {
					if (logbook2LetterJ55CheckBox.isSelected()) {
						identifyOfTanksDisposedJFXChipView.getChips().clear();

						identifyOfTanksTransferedJFXChipView.getChips().clear();
						identityOfTanksImageViewButton.setDisable(false);
						identityOfTanksImageViewButton.setVisible(true);
						identifyOfTanksTransferedJFXChipView.setDisable(false);
						identifyOfTanksTransferedJFXChipView.setVisible(true);
						textTypeLabel.setText("Transfered");
						identifyOfTanksDisposedJFXChipView.setVisible(false);

						logbook2LetterJ56QuantityCheckBox.setSelected(true);
						logbook2LetterJ57CheckBox.setSelected(true);

						logbook2LetterJ57DisposedCheckBox.setDisable(true);
						logbook2LetterJ57DisposedCheckBox.setSelected(false);

						logbook2LetterJ57MixedCheckBox.setDisable(false);
						logbook2LetterJ57MixedCheckBox.setSelected(false);
						logbook2LetterJ57TransferedCheckBox.setDisable(false);
						logbook2LetterJ57TransferedCheckBox.setSelected(true);
						logbook2LetterJ57MixedOneCheckBox.setDisable(false);
						logbook2LetterJ57MixedOneCheckBox.setSelected(false);
					}
				} else {

					logbook2LetterJ56QuantityCheckBox.setSelected(false);
					logbook2LetterJ57CheckBox.setSelected(false);
					logbook2LetterJ57DisposedCheckBox.setDisable(true);
					logbook2LetterJ57DisposedCheckBox.setSelected(false);
					logbook2LetterJ57MixedCheckBox.setDisable(true);
					logbook2LetterJ57MixedCheckBox.setSelected(false);
					logbook2LetterJ57TransferedCheckBox.setDisable(true);
					logbook2LetterJ57TransferedCheckBox.setSelected(false);
					logbook2LetterJ57MixedOneCheckBox.setDisable(true);
					logbook2LetterJ57MixedOneCheckBox.setSelected(false);
				}
			}
		});

		identifyOfTanksDisposedJFXChipView.getChips().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> change) {
				if (logbook2LetterJ55CheckBox.isSelected()) {
					Double totalUserTankQuantities = ((TankUsageContext) identifyOfTanksDisposedJFXChipView.getUserData()).getTotalUserTankQuantities();
					if (logbook2LetterJ57DisposedCheckBox.isSelected()) {
						logbook2LetterJ57DisposedQuantityDoubleField.setValue(totalUserTankQuantities);
					}
				}
				while (change.next()) {
					if (change.wasAdded()) {
						for (String item : change.getAddedSubList()) {
							addJ57TransferedRetained(item);
						}

					} else if (change.wasRemoved()) {
						for (String item : change.getRemoved()) {
							tankerTransferedOrDisposedQuantityHBox.getChildren().removeIf(childNode -> childNode.getId().equals(item));
							tankerRetainedQuantityHBox.getChildren().removeIf(childNode -> childNode.getId().equals(item));

						}
					}
				}
				JavaFXUtils.addTooltip(identifyOfTanksDisposedJFXChipView);
			}
		});

		identifyOfTanksTransferedJFXChipView.getChips().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> change) {
				while (change.next()) {
					for (String item : change.getAddedSubList()) {
						if (identifyOfTanksTransferedJFXChipView.getChips().size() > 1) {
							String oldChip = identifyOfTanksTransferedJFXChipView.getChips().get(0);
							Platform.runLater(() -> identifyOfTanksTransferedJFXChipView.getChips().remove(oldChip));
						}
					}
					if (change.wasAdded()) {
						for (String item : change.getAddedSubList()) {
							addJ57TransferedRetained(item);
						}

					} else if (change.wasRemoved()) {
						for (String item : change.getRemoved()) {
							tankerTransferedOrDisposedQuantityHBox.getChildren().removeIf(childNode -> childNode.getId().equals(item));
							tankerRetainedQuantityHBox.getChildren().removeIf(childNode -> childNode.getId().equals(item));
						}
					}
				}

				Platform.runLater(() -> {
					Double totalUserTankQuantities = ((TankUsageContext) identifyOfTanksTransferedJFXChipView.getUserData()).getTotalUserTankQuantities();
					if (logbook2LetterJ57TransferedCheckBox.isSelected()) {
						logbook2LetterJ57TransferedQuantityDoubleField.setValue(totalUserTankQuantities);
					}
				});
				JavaFXUtils.addTooltip(identifyOfTanksTransferedJFXChipView);
			}

		});
		logbook2LetterJ56QuantityCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
//				for (Node childNode : tankerRetainedQuantityHBox.getChildren()) {
//					if (childNode instanceof DoubleField) {
//						((DoubleField) childNode).setDisable(false);
//					}
//				}
//				for (Node childNode : tankerTransferedOrDisposedQuantityHBox.getChildren()) {
//					if (childNode instanceof DoubleField) {
//						((DoubleField) childNode).setDisable(false);
//					}
//
//				}

			} else {
				for (Node childNode : tankerRetainedQuantityHBox.getChildren()) {
					if (childNode instanceof DoubleField) {
						((DoubleField) childNode).setDisable(true);
					}

				}
				for (Node childNode : tankerTransferedOrDisposedQuantityHBox.getChildren()) {
					if (childNode instanceof DoubleField) {
						((DoubleField) childNode).setDisable(true);
					}

				}
			}

		});

		logbook2LetterJ57DisposedCheckBox.setDisable(true);
		logbook2LetterJ57MixedCheckBox.setDisable(true);
		logbook2LetterJ57TransferedCheckBox.setDisable(true);
		logbook2LetterJ57MixedOneCheckBox.setDisable(true);

		logbook2LetterJ57CheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterJ57DisposedCheckBox.setDisable(false);
				logbook2LetterJ57MixedCheckBox.setDisable(false);
				logbook2LetterJ57TransferedCheckBox.setDisable(false);
				logbook2LetterJ57MixedOneCheckBox.setDisable(false);

			} else {
				logbook2LetterJ57DisposedCheckBox.setSelected(false);
				logbook2LetterJ57MixedCheckBox.setSelected(false);
				logbook2LetterJ57TransferedCheckBox.setSelected(false);
				logbook2LetterJ57MixedOneCheckBox.setSelected(false);

				logbook2LetterJ57DisposedCheckBox.setDisable(true);
				logbook2LetterJ57MixedCheckBox.setDisable(true);
				logbook2LetterJ57TransferedCheckBox.setDisable(true);
				logbook2LetterJ57MixedOneCheckBox.setDisable(true);
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
				logbook2LetterJ57DisposedBDNTextField.setText(selectedFile.getName());
				logbook2LetterJ57DisposedBDNTextField.setUserData(selectedFile);
			}
		});
		logbook2LetterJ57DisposedPortTextField.setDisable(true);
		logbook2LetterJ57DisposedQuantityDoubleField.setDisable(true);
		uploadReceiptImageViewButton.setDisable(true);
		logbook2LetterJ57DisposedBDNTextField.setDisable(true);
		logbook2LetterJMixedWithCargoTextField.setDisable(true);

		logbook2LetterJ57DisposedCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterJ57DisposedPortTextField.setDisable(false);
				// logbook2LetterJ57DisposedQuantityDoubleField.setDisable(false);
				uploadReceiptImageViewButton.setDisable(false);
				//logbook2LetterJ57DisposedBDNTextField.setDisable(false);
				Double totalUserTankQuantities = ((TankUsageContext) identifyOfTanksDisposedJFXChipView.getUserData()).getTotalUserTankQuantities();
				if (logbook2LetterJ57DisposedCheckBox.isSelected()) {
					logbook2LetterJ57DisposedQuantityDoubleField.setValue(totalUserTankQuantities);
				}

			} else {
				logbook2LetterJ57DisposedPortTextField.setDisable(true);
				logbook2LetterJ57DisposedQuantityDoubleField.setDisable(true);
				uploadReceiptImageViewButton.setDisable(true);
				logbook2LetterJ57DisposedBDNTextField.setDisable(true);

				logbook2LetterJ57DisposedPortTextField.setText("");
				logbook2LetterJ57DisposedQuantityDoubleField.setText("");
				logbook2LetterJ57DisposedBDNTextField.setText("");
			}

		});

		logbook2LetterJ57MixedQuantityDoubleField.setDisable(true);

		logbook2LetterJ57MixedCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterJ57MixedQuantityDoubleField.setDisable(false);

			} else {
				logbook2LetterJ57MixedQuantityDoubleField.setDisable(true);
				logbook2LetterJ57MixedQuantityDoubleField.setText("");
			}

		});

		identityOfTanksJ57ImageViewButton.setDisable(true);
		logbook2LetterJ57TransferedJFXChipView.setDisable(true);
		logbook2LetterJ57TransferedQuantityDoubleField.setDisable(true);
		logbook2LetterJ57RetainedQuantityDoubleField.setDisable(true);

		logbook2LetterJ57TransferedCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				identityOfTanksJ57ImageViewButton.setDisable(false);
				logbook2LetterJ57TransferedJFXChipView.setDisable(false);
				// logbook2LetterJ57TransferedQuantityDoubleField.setDisable(false);
				// logbook2LetterJ57RetainedQuantityDoubleField.setDisable(false);
				Platform.runLater(() -> {
					Double totalUserTankQuantities = ((TankUsageContext) identifyOfTanksTransferedJFXChipView.getUserData()).getTotalUserTankQuantities();
					if (logbook2LetterJ57TransferedCheckBox.isSelected()) {
						logbook2LetterJ57TransferedQuantityDoubleField.setValue(totalUserTankQuantities);
					}
				});
				Platform.runLater(() -> {
					Double totalUserTankQuantities = ((TankUsageContext) logbook2LetterJ57TransferedJFXChipView.getUserData()).getTotalUserTankActualLevel();
					logbook2LetterJ57RetainedQuantityDoubleField.setValue(totalUserTankQuantities - Optional.ofNullable(logbook2LetterJ57TransferedQuantityDoubleField.getValue()).orElse(0.0));
				});

			} else {
				identityOfTanksJ57ImageViewButton.setDisable(true);
				logbook2LetterJ57TransferedJFXChipView.setDisable(true);
				logbook2LetterJ57TransferedQuantityDoubleField.setDisable(true);
				logbook2LetterJ57RetainedQuantityDoubleField.setDisable(true);
				logbook2LetterJ57TransferedJFXChipView.getChips().clear();
				logbook2LetterJ57TransferedQuantityDoubleField.setText("");
				logbook2LetterJ57RetainedQuantityDoubleField.setText("");
			}

		});

		logbook2LetterJ57MixedOneQuantityDoubleField.setDisable(true);

		logbook2LetterJ57MixedOneCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterJ57MixedOneQuantityDoubleField.setDisable(false);
				logbook2LetterJMixedWithCargoTextField.setDisable(false);

			} else {
				logbook2LetterJ57MixedOneQuantityDoubleField.setDisable(true);
				logbook2LetterJ57MixedOneQuantityDoubleField.setText("");
				logbook2LetterJMixedWithCargoTextField.setDisable(true);
				logbook2LetterJMixedWithCargoTextField.setText("");
			}

		});
		TankUsageContext tankUsageContext1 = new TankUsageContext(Stream.of(TankType.BALLAST, TankType.CARGO, TankType.FUELOIL, TankType.OILYBILGE, TankType.SLOP, TankType.SLUDGE).collect(Collectors.toList()), false, true);
		tankUsageContext1.setNoNeedForUserTankQuantities(true);
		logbook2LetterJ57TransferedJFXChipView.setUserData(tankUsageContext1);
		addRemoveListener(logbook2LetterJ57TransferedJFXChipView);

		logbook2LetterJ57TransferedJFXChipView.getChips().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> change) {
				while (change.next()) {
					for (String item : change.getAddedSubList()) {
						if (logbook2LetterJ57TransferedJFXChipView.getChips().size() > 1) {
							String oldChip = logbook2LetterJ57TransferedJFXChipView.getChips().get(0);
							// logbook2LetterJ57TransferedJFXChipView.getChips().remove(oldChip);
							Platform.runLater(() -> logbook2LetterJ57TransferedJFXChipView.getChips().remove(oldChip));
						}
					}
				}
				Platform.runLater(() -> {
					Double totalUserTankQuantities = ((TankUsageContext) logbook2LetterJ57TransferedJFXChipView.getUserData()).getTotalUserTankActualLevel();
					logbook2LetterJ57RetainedQuantityDoubleField.setValue(totalUserTankQuantities - Optional.ofNullable(logbook2LetterJ57TransferedQuantityDoubleField.getValue()).orElse(0.0));
				});
				JavaFXUtils.addTooltip(logbook2LetterJ57TransferedJFXChipView);
			}
		});

		eventBroker.subscribe(Activator.MARPOL_DETAILS_RELOADED, new org.osgi.service.event.EventHandler() {
			@Override
			public void handleEvent(org.osgi.service.event.Event event) {
				ArrayList<Long> changedTankIds = (ArrayList<Long>) event.getProperty(Activator.TANK_IDS);
				if (changedTankIds == null) {
					identifyOfTanksDisposedJFXChipView.getChips().clear();
					logbook2LetterJ57TransferedJFXChipView.getChips().clear();
				} else if (changedTankIds != null && !changedTankIds.isEmpty()) {
					Activator.removeChangedTanksFromJFXChipView(changedTankIds, identifyOfTanksDisposedJFXChipView);
					Activator.removeChangedTanksFromJFXChipView(changedTankIds, logbook2LetterJ57TransferedJFXChipView);
				}
			}
		});

	}

	private void addJ57TransferedRetained(String item) {
		DoubleField tankerTransferedOrDisposedDoubleField = new DoubleField();
		MathUtils.setStringConverter(tankerTransferedOrDisposedDoubleField);
		tankerTransferedOrDisposedDoubleField.setPrefWidth(70);
		tankerTransferedOrDisposedDoubleField.setId(item);
		tankerTransferedOrDisposedDoubleField.getStyleClass().add("logbook-text-field");
		JavaFXUtils.addFilledPseudoClass(tankerTransferedOrDisposedDoubleField);
		tankerTransferedOrDisposedDoubleField.setDisable(true);
		Map<Tank, Double> userTankQuantities = ((TankUsageContext) identifyOfTanksDisposedJFXChipView.getUserData()).getUserTankQuantities();
		userTankQuantities.entrySet().stream().filter(tank -> tank.getKey().getName() == item).forEach(tank -> tankerTransferedOrDisposedDoubleField.setValue(tank.getValue()));
		Label labelQuantity = new Label("M3");
		labelQuantity.getStyleClass().add("logbookOtherLabel");
		labelQuantity.setId(item);
		tankerTransferedOrDisposedQuantityHBox.getChildren().addAll(tankerTransferedOrDisposedDoubleField, labelQuantity);

		DoubleField tankerRetainedDoubleField = new DoubleField();
		MathUtils.setStringConverter(tankerRetainedDoubleField);
		tankerRetainedDoubleField.setPrefWidth(70);
		tankerRetainedDoubleField.setId(item);
		tankerRetainedDoubleField.getStyleClass().add("logbook-text-field");
		JavaFXUtils.addFilledPseudoClass(tankerRetainedDoubleField);
		tankerRetainedDoubleField.setDisable(true);
		Map<Tank, Double> userTankActualLevels = ((TankUsageContext) identifyOfTanksDisposedJFXChipView.getUserData()).getUserTankActualLevels();
		userTankActualLevels.entrySet().stream().filter(tank -> tank.getKey().getName() == item).forEach(tank -> tankerRetainedDoubleField.setValue(tank.getValue()));
		Label labelRetained = new Label("M3");
		labelRetained.getStyleClass().add("logbookOtherLabel");
		labelRetained.setId(item);
		tankerRetainedQuantityHBox.getChildren().addAll(tankerRetainedDoubleField, labelRetained);
	}

	
	public void openIdentifyOfTanksJ55(Event e) {
		if (identifyOfTanksDisposedJFXChipView.isVisible()) {
			selectTankerDialog.showDialog((Stage) ((ImageViewButton) e.getSource()).getScene().getWindow(), identifyOfTanksDisposedJFXChipView);
		} else if (identifyOfTanksTransferedJFXChipView.isVisible()) {
			selectTankerDialog.showDialog((Stage) ((ImageViewButton) e.getSource()).getScene().getWindow(), identifyOfTanksTransferedJFXChipView);
		}

	}

	public void openIdentifyOfTanksJ57(Event e) {
		selectTankerDialog.showDialog((Stage) ((ImageViewButton) e.getSource()).getScene().getWindow(), logbook2LetterJ57TransferedJFXChipView);
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

	public void letterJ(MouseEvent event) {
		helpCodeLetterDialog.showDialog((Stage)((ImageViewButton)event.getSource()).getScene().getWindow(),helpImageList);
	}
	public void processJ(OilLog logToInsert, List<OilLog> logToInsertList, Map<Validation, Object> validations) {
		validateFields(logToInsert.getBusinessDate(), validations);
		if (logbook2LetterJ55CheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("55");
			log.setOrderInGroup("ZZH");
			if (dischargedOrDisposedToggle.isSelected()) {
				log.setContent("" + String.join(", ", identifyOfTanksDisposedJFXChipView.getChips()));
				log.setUserTankActualLevels(((TankUsageContext) identifyOfTanksDisposedJFXChipView.getUserData()).getUserTankActualLevels());
			} else if (transferredToggle.isSelected()) {
				log.setContent("" + String.join(", ", identifyOfTanksTransferedJFXChipView.getChips()));
				log.setUserTankActualLevels(((TankUsageContext) identifyOfTanksTransferedJFXChipView.getUserData()).getUserTankActualLevels());
			}
			logToInsertList.add(log);
		}
		if (logbook2LetterJ56QuantityCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("56");
			log.setOrderInGroup("ZZI");
			StringBuilder transferedDisposedRetainedText = new StringBuilder();
			if (dischargedOrDisposedToggle.isSelected()) {
				for (String chip : identifyOfTanksDisposedJFXChipView.getChips()) {
					int i = 0;
					for (Node childNode : tankerTransferedOrDisposedQuantityHBox.getChildren()) {
						if (childNode.getId() == chip && !childNode.getStyleClass().contains("logbookOtherLabel")) {
							DoubleField transferedOrDisposedDoubleField = (DoubleField) tankerTransferedOrDisposedQuantityHBox.getChildren().get(i);
							DoubleField retainedDoubleField = (DoubleField) tankerRetainedQuantityHBox.getChildren().get(i);
							if (transferedOrDisposedDoubleField != null && retainedDoubleField != null) {

								transferedDisposedRetainedText.append(transferedOrDisposedDoubleField.getValue());
								transferedDisposedRetainedText.append(" M3 DISPOSED ");

								transferedDisposedRetainedText.append(retainedDoubleField.getValue());
								transferedDisposedRetainedText.append(" M3 RETAINED IN  ");

								transferedDisposedRetainedText.append(chip);
								transferedDisposedRetainedText.append(System.lineSeparator());
							}
						}
						i++;
					}
				}
				log.setContent(transferedDisposedRetainedText.toString());
			} else if (transferredToggle.isSelected()) {
				for (String chip : identifyOfTanksTransferedJFXChipView.getChips()) {
					int i = 0;
					for (Node childNode : tankerTransferedOrDisposedQuantityHBox.getChildren()) {
						if (childNode.getId() == chip && !childNode.getStyleClass().contains("logbookOtherLabel")) {
							DoubleField transferedOrDisposedDoubleField = (DoubleField) tankerTransferedOrDisposedQuantityHBox.getChildren().get(i);
							DoubleField retainedDoubleField = (DoubleField) tankerRetainedQuantityHBox.getChildren().get(i);
							if (transferedOrDisposedDoubleField != null && retainedDoubleField != null) {

								transferedDisposedRetainedText.append(transferedOrDisposedDoubleField.getValue());
								transferedDisposedRetainedText.append(" M3 DISPOSED ");

								transferedDisposedRetainedText.append(retainedDoubleField.getValue());
								transferedDisposedRetainedText.append(" M3 RETAINED IN  ");

								transferedDisposedRetainedText.append(chip);
								transferedDisposedRetainedText.append(System.lineSeparator());
							}
						}
						i++;
					}
				}
				log.setContent(transferedDisposedRetainedText.toString());
			}
			logToInsertList.add(log);
		}
		if (logbook2LetterJ57DisposedCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("57.1");
			log.setOrderInGroup("ZZJ");
			StringBuilder sb = new StringBuilder();
			if (sb.length() > 0)
				sb.append("\n");
			sb.append(logbook2LetterJ57DisposedPortTextField.getText() + " ");
			sb.append(logbook2LetterJ57DisposedQuantityDoubleField.getText() + " M3 DISPOSED ");
			sb.append(logbook2LetterJ57DisposedBDNTextField.getText());
			log.setContent(sb.toString());
			if (logbook2LetterJ57DisposedBDNTextField.getUserData() != null && ((File) logbook2LetterJ57DisposedBDNTextField.getUserData()).exists()) {
				log.setAttachmentName(((File) logbook2LetterJ57DisposedBDNTextField.getUserData()).getName());
				byte[] attachmentBytes = null;
				try {
					attachmentBytes = Files.readAllBytes(((File) logbook2LetterJ57DisposedBDNTextField.getUserData()).toPath());
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
				log.setAttachmentData(attachmentBytes);
			}
			logToInsertList.add(log);
		}
		if (logbook2LetterJ57MixedCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("57.2");
			log.setOrderInGroup("ZZK");
			log.setContent(logbook2LetterJ57MixedQuantityDoubleField.getValue() + " M3");
			logToInsertList.add(log);
		}
		if (logbook2LetterJ57TransferedCheckBox.isSelected()) {
			StringBuilder sb = new StringBuilder();
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("57.3");
			log.setOrderInGroup("ZZL");
			sb.append(logbook2LetterJ57TransferedQuantityDoubleField.getValue() + " M3 TRANSFRED FROM" + logbook2LetterJ57TransferedJFXChipView.getChips());
			sb.append(System.lineSeparator());
			sb.append(logbook2LetterJ57RetainedQuantityDoubleField.getValue() + " M3 RETAINED TO" + logbook2LetterJ57TransferedJFXChipView.getChips());
			// TankUsageContext tanks =
			// (TankUsageContext)logbook2LetterJ57TransferedJFXChipView.getUserData();
			Set<Tank> userTanks = ((TankUsageContext) ((JFXChipView<String>) logbook2LetterJ57TransferedJFXChipView).getUserData()).getUserTankQuantities().keySet();
			Tank tank = null;
			if (userTanks != null && !userTanks.isEmpty()) {
				tank = (Tank) userTanks.iterator().next();
			}
			Map<Tank, Double> userTankActualLevels = new HashMap<Tank, Double>();
			userTankActualLevels.put(tank, logbook2LetterJ57RetainedQuantityDoubleField.getValue());
			log.setUserTankActualLevels(userTankActualLevels);
			log.setContent(sb.toString());
			logToInsertList.add(log);
		}
		if (logbook2LetterJ57MixedOneCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("57.4");
			log.setOrderInGroup("ZZM");
			log.setContent(logbook2LetterJ57MixedOneQuantityDoubleField.getValue() + " M3");
			logToInsertList.add(log);
		}
	}

	private void validateFields(Timestamp businessDate, Map<Validation, Object> validations) {
		if (!logbook2LetterJ55CheckBox.isSelected() || (identifyOfTanksDisposedJFXChipView.getChips().isEmpty() && identifyOfTanksTransferedJFXChipView.getChips().isEmpty())) {
			validations.put(Validation.J55_EMPTY_TANKS, null);
		}
		if (!logbook2LetterJ56QuantityCheckBox.isSelected()) {
			validations.put(Validation.J56_EMPTY, null);
		}
		if (!logbook2LetterJ57DisposedCheckBox.isSelected() && dischargedOrDisposedToggle.isSelected()) {
			validations.put(Validation.J57_EMPTY, null);
		}
		if (logbook2LetterJ57DisposedCheckBox.isSelected() && logbook2LetterJ57DisposedPortTextField.getText().isEmpty()) {
			validations.put(Validation.J57_EMPTY_PORT, null);
		}
		if (logbook2LetterJ57DisposedCheckBox.isSelected() && logbook2LetterJ57DisposedBDNTextField.getText().isEmpty()) {
			validations.put(Validation.J57_EMPTY_RECEPTION, null);
		}
		if (logbook2LetterJ57MixedCheckBox.isSelected() && logbook2LetterJ57MixedQuantityDoubleField.getText().isEmpty()) {
			validations.put(Validation.J57_EMPTY_QUANTITY, null);
		}
		if (!logbook2LetterJ57TransferedCheckBox.isSelected() && transferredToggle.isSelected()) {
			validations.put(Validation.J57_EMPTY_TRANSFERED, null);
		}
		if (logbook2LetterJ57TransferedCheckBox.isSelected() && transferredToggle.isSelected() && logbook2LetterJ57TransferedJFXChipView.getChips().isEmpty()) {
			validations.put(Validation.J57_EMPTY_TANKS, null);
		}
		if (logbook2LetterJ57MixedOneCheckBox.isSelected() && logbook2LetterJ57MixedOneQuantityDoubleField.getText().isEmpty()) {
			validations.put(Validation.J57_EMPTY_QUANTITY1, null);
		}	
		if (logbook2LetterJ57MixedOneCheckBox.isSelected() && logbook2LetterJMixedWithCargoTextField.getText().isEmpty()) {
			validations.put(Validation.J57_EMPTY_QUANTITY1, null);
		}
	}

	public void showBalloon(MouseEvent event) {
		ballonMessage.showGeneralBalloon((ImageViewButton) event.getTarget(), label1);
	}

	public void showBalloon1(MouseEvent event) {
		ballonMessage.showGeneralBalloon((ImageViewButton) event.getTarget(), label2);
	}

	public void clearFilledInfo() {
		logbook2LetterJ55CheckBox.setSelected(false);
		logbook2LetterJ56QuantityCheckBox.setSelected(false);
		logbook2LetterJ57CheckBox.setSelected(false);
		logbook2LetterJ57DisposedCheckBox.setSelected(false);
		logbook2LetterJ57TransferedCheckBox.setSelected(false);
		logbook2LetterJ57MixedCheckBox.setSelected(false);
		logbook2LetterJ57MixedOneCheckBox.setSelected(false);
		identifyOfTanksDisposedJFXChipView.getChips().clear();
		identifyOfTanksTransferedJFXChipView.getChips().clear();
		logbook2LetterJ57TransferedJFXChipView.getChips().clear();
		TankUsageContext tankUsageContextMultiTanks = new TankUsageContext(Stream.of(TankType.BALLAST, TankType.CARGO, TankType.FUELOIL, TankType.OILYBILGE, TankType.SLOP, TankType.SLUDGE).collect(Collectors.toList()), false, true);
		identifyOfTanksDisposedJFXChipView.setUserData(tankUsageContextMultiTanks);
		identifyOfTanksTransferedJFXChipView.setUserData(tankUsageContextMultiTanks);
		TankUsageContext tankUsageContext1 = new TankUsageContext(Stream.of(TankType.BALLAST, TankType.CARGO, TankType.FUELOIL, TankType.OILYBILGE, TankType.SLOP, TankType.SLUDGE).collect(Collectors.toList()), false, true);
		tankUsageContext1.setNoNeedForUserTankQuantities(true);
		logbook2LetterJ57TransferedJFXChipView.setUserData(tankUsageContext1);
	}

}
