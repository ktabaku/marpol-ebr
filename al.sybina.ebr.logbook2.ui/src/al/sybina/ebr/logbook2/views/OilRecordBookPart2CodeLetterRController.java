package al.sybina.ebr.logbook2.views;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.inject.Inject;

import org.eclipse.e4.tools.resources.WorkbenchResourceProvider;
import org.eclipse.e4.tools.services.IResourceProviderService;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;
import org.eclipse.fx.ui.services.resources.ImageProvider;

import al.sybina.ebr.commons.utils.JavaFXUtils;
import al.sybina.ebr.commons.utils.MathUtils;
import al.sybina.ebr.logbook.commons.BallonMessage;
import al.sybina.ebr.logbook.dialogs.help.HelpCodeLetterDialog;
import al.sybina.ebr.logbook.domain.reladomo.OilLog;
import al.sybina.ebr.logbook2.views.OilRecordBookPart2ViewController.Validation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import jidefx.scene.control.field.DoubleField;

public class OilRecordBookPart2CodeLetterRController implements Initializable {

	private Label label1 = new Label(" ATTACH  BDN WITH STAMP AND SIGNATURE\r\n" + "");

	private static final Logger logger = LoggerCreator.createLogger(OilRecordBookPart2CodeLetterRController.class);

	@Inject
	private BallonMessage ballonMessage;
	@Inject
	private IResourceProviderService resourcesService;
	@Inject
	private ImageProvider imageProvider;
	@Inject
	private HelpCodeLetterDialog helpCodeLetterDialog;
	@FXML
	private CheckBox logbook2LetterR82CheckBox;
	@FXML
	private TextField logbook2LetterR82TextField;
	@FXML
	private CheckBox logbook2LetterR83CheckBox;
	@FXML
	private TextField logbook2LetterR83TextField;
	@FXML
	private CheckBox logbook2LetterR84CheckBox;
	@FXML
	private DoubleField logbook2LetterR84DoubleField;
	@FXML
	private CheckBox logbook2LetterR85CheckBox;
	@FXML
	private TextField logbook2LetterR85TextField;
	@FXML
	private ImageViewButton infoLetterAImageViewButton;
	@FXML
	private ImageViewButton uploadReceiptImageViewButton;
	@FXML
	private ImageViewButton logbook2LetterRHelpImageViewButton;
	protected String CACHE_FILE_NAME = System.getProperty("user.home") + "/.ebr/caches/caches.properties";
	@FXML
	public AnchorPane codeLetterContentAnchorPane;
	private ObservableList<Image> helpImageList = FXCollections.observableArrayList();
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MathUtils.setStringConverter(logbook2LetterR84DoubleField);
		try {

			Image helpImageLetterR2 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPR2));
			
			Image helpImageLetterR1 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPR1));
			
			Image helpImageLetterR3 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPR3));
			
			helpImageList.addAll(helpImageLetterR2, helpImageLetterR1, helpImageLetterR3);
			
			Image infoIconImageInfoLetterA = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
			infoLetterAImageViewButton.setImage(infoIconImageInfoLetterA);
			uploadReceiptImageViewButton.setImage(infoIconImageInfoLetterA);
			logbook2LetterRHelpImageViewButton.setImage(infoIconImageInfoLetterA);

		} catch (IOException e) {
			e.printStackTrace();
		}
		logbook2LetterR82TextField.setDisable(true);
		logbook2LetterR83TextField.setDisable(true);
		logbook2LetterR84DoubleField.setDisable(true);
		logbook2LetterR84DoubleField.setText("");
		logbook2LetterR85TextField.setDisable(true);
		uploadReceiptImageViewButton.setDisable(true);
		logbook2LetterR82TextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		logbook2LetterR83TextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		JavaFXUtils.addFilledPseudoClass(logbook2LetterR83TextField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterR84DoubleField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterR82TextField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterR85TextField);
		MathUtils.setStringConverter(logbook2LetterR84DoubleField);
		MathUtils.setMinMaxValues(logbook2LetterR84DoubleField, 0.0, null);
		logbook2LetterR82CheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterR82TextField.setDisable(false);
			} else {
				logbook2LetterR82TextField.setDisable(true);
				logbook2LetterR82TextField.setText("");

			}

		});

		logbook2LetterR83CheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterR83TextField.setDisable(false);
			} else {
				logbook2LetterR83TextField.setDisable(true);
				logbook2LetterR83TextField.setText("");

			}

		});
		logbook2LetterR84CheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterR84DoubleField.setDisable(false);
			} else {
				logbook2LetterR84DoubleField.setDisable(true);
				logbook2LetterR84DoubleField.setText("");

			}

		});
		logbook2LetterR85CheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				uploadReceiptImageViewButton.setDisable(false);
			} else {
				logbook2LetterR85TextField.setDisable(true);
				logbook2LetterR85TextField.setText("");
				uploadReceiptImageViewButton.setDisable(true);

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
				logbook2LetterR85TextField.setText(logbook2LetterR85TextField.getText() + "  " + selectedFile.getName());
				logbook2LetterR85TextField.setUserData(selectedFile);
			}
		});

	}

	public void letterR(MouseEvent event) {
		helpCodeLetterDialog.showDialog((Stage)((ImageViewButton)event.getSource()).getScene().getWindow(),helpImageList);
	}

	public void processR(OilLog logToInsert, List<OilLog> logToInsertList, Map<Validation, Object> validations) {
		validateFields(logToInsert.getBusinessDate(), validations);
		if (logbook2LetterR82CheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("82");
			log.setOrderInGroup("ZZZQ");
			log.setContent(logbook2LetterR82TextField.getText());
			logToInsertList.add(log);
		}
		if (logbook2LetterR83CheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("83");
			log.setOrderInGroup("ZZZR");
			log.setContent(logbook2LetterR83TextField.getText());
			logToInsertList.add(log);
		}
		if (logbook2LetterR84CheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("84");
			log.setOrderInGroup("ZZZS");
			log.setContent(logbook2LetterR84DoubleField.getText() + "M3");
			logToInsertList.add(log);
		}
		if (logbook2LetterR85CheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("85");
			log.setOrderInGroup("ZZZT");
			log.setContent(logbook2LetterR85TextField.getText());
			if (logbook2LetterR85TextField.getUserData() != null && ((File) logbook2LetterR85TextField.getUserData()).exists()) {
				log.setAttachmentName(((File) logbook2LetterR85TextField.getUserData()).getName());
				byte[] attachmentBytes = null;
				try {
					attachmentBytes = Files.readAllBytes(((File) logbook2LetterR85TextField.getUserData()).toPath());
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
				log.setAttachmentData(attachmentBytes);
			}
			logToInsertList.add(log);
		}

	}

	private void validateFields(Timestamp businessDate, Map<Validation, Object> validations) {
		if (!logbook2LetterR82CheckBox.isSelected() || logbook2LetterR82TextField.getText().isEmpty()) {
			validations.put(Validation.R82_EMPTY_PORT, null);
		}
		if (!logbook2LetterR83CheckBox.isSelected() || logbook2LetterR83TextField.getText().isEmpty()) {
			validations.put(Validation.R83_EMPTY_RECEPTION, null);
		}
		if (!logbook2LetterR84CheckBox.isSelected() || logbook2LetterR84DoubleField.getValue()==null) {
			validations.put(Validation.R84_EMPTY_QUANTITY, null);
		}
		if (!logbook2LetterR85CheckBox.isSelected() || logbook2LetterR85TextField.getText().isEmpty()) {
			validations.put(Validation.R85_EMPTY_FILE, null);
		}
	}

	public void showBalloon(MouseEvent event) {
		ballonMessage.showGeneralBalloon((ImageViewButton) event.getTarget(), label1);
	}

	public void clearFilledInfo() {
		logbook2LetterR82CheckBox.setSelected(false);
		logbook2LetterR83CheckBox.setSelected(false);
		logbook2LetterR84CheckBox.setSelected(false);
		logbook2LetterR85CheckBox.setSelected(false);

	}
}
