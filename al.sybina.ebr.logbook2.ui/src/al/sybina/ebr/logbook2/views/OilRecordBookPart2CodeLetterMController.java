package al.sybina.ebr.logbook2.views;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.inject.Inject;

import org.eclipse.e4.tools.resources.WorkbenchResourceProvider;
import org.eclipse.e4.tools.services.IResourceProviderService;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;
import org.eclipse.fx.ui.services.resources.ImageProvider;

import com.oceanpro.utils.EasyDateUtils;

import al.sybina.ebr.commons.utils.JavaFXUtils;
import al.sybina.ebr.logbook.dialogs.SelectTankerDialog;
import al.sybina.ebr.logbook.dialogs.help.HelpCodeLetterDialog;
import al.sybina.ebr.logbook.domain.reladomo.OilLog;
import al.sybina.ebr.logbook2.views.OilRecordBookPart2ViewController.Validation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import jfxtras.scene.control.ImageViewButton;
import jfxtras.scene.control.LocalTimeTextField;

public class OilRecordBookPart2CodeLetterMController implements Initializable {

	private static final Logger logger = LoggerCreator.createLogger(OilRecordBookPart2CodeLetterMController.class);

	@Inject
	private IResourceProviderService resourcesService;
	@Inject
	private ImageProvider imageProvider;
	@Inject
	private SelectTankerDialog selectTankerDialog;
	@Inject
	private HelpCodeLetterDialog helpCodeLetterDialog;
	@FXML
	private CheckBox logbook2LetterM70CheckBox;
	@FXML
	private LocalTimeTextField logbook2LetterM70LocalTimeTextField;
	@FXML
	private CheckBox logbook2LetterM71CheckBox;
	@FXML
	private LocalTimeTextField logbook2LetterM71LocalTimeTextField;
	@FXML
	private CheckBox logbook2LetterM72CheckBox;
	@FXML
	private TextField logbook2LetterM72TextField;
	@FXML
	private ImageViewButton infoLetterAImageViewButton;
	@FXML
	public AnchorPane codeLetterContentAnchorPane;
	private DateTimeFormatter timeFormatter = EasyDateUtils.logbookTimeFormatter;
	private ObservableList<Image> helpImageList = FXCollections.observableArrayList();
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {

			Image helpImageLetterM2 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPM2));
			
			Image helpImageLetterM1 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPM1));
			
			Image helpImageLetterM3 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPM3));
			
			Image helpImageLetterM4 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPM4));
			
			helpImageList.addAll(helpImageLetterM4, helpImageLetterM1,helpImageLetterM2, helpImageLetterM3);
			
			Image infoIconImageInfoLetterA = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
			infoLetterAImageViewButton.setImage(infoIconImageInfoLetterA);
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		logbook2LetterM70LocalTimeTextField.setDisable(true);
		logbook2LetterM71LocalTimeTextField.setDisable(true);
		logbook2LetterM72TextField.setDisable(true);
		logbook2LetterM72TextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		JavaFXUtils.addFilledPseudoClass(logbook2LetterM72TextField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterM70LocalTimeTextField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterM71LocalTimeTextField);
		logbook2LetterM70LocalTimeTextField.setDateTimeFormatter(timeFormatter);
		logbook2LetterM71LocalTimeTextField.setDateTimeFormatter(timeFormatter);
		logbook2LetterM70CheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterM70LocalTimeTextField.setDisable(false);

			} else {
				logbook2LetterM70LocalTimeTextField.setDisable(true);
				logbook2LetterM70LocalTimeTextField.setLocalTime(null);
			}

		});
		logbook2LetterM71CheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterM71LocalTimeTextField.setDisable(false);

			} else {
				logbook2LetterM71LocalTimeTextField.setDisable(true);
				logbook2LetterM71LocalTimeTextField.setLocalTime(null);
			}

		});
		logbook2LetterM72CheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterM72TextField.setDisable(false);

			} else {
				logbook2LetterM72TextField.setDisable(true);
				logbook2LetterM72TextField.setText("");
			}

		});

	}

	public void processM(OilLog logToInsert, List<OilLog> logToInsertList, Map<Validation, Object> validations) {
		validateFields(logToInsert.getBusinessDate(), validations);
		if (logbook2LetterM70CheckBox.isSelected() && logbook2LetterM70LocalTimeTextField.getLocalTime()!=null) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("70");
			log.setOrderInGroup("ZZZE");
			log.setContent("TIME: " + logbook2LetterM70LocalTimeTextField.getLocalTime().format(timeFormatter));
			logToInsertList.add(log);
		}
		if (logbook2LetterM71CheckBox.isSelected() && logbook2LetterM71LocalTimeTextField.getLocalTime()!=null) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("71");
			log.setOrderInGroup("ZZZF");
			log.setContent("TIME: " + logbook2LetterM71LocalTimeTextField.getLocalTime().format(timeFormatter));
			logToInsertList.add(log);
		}
		if (logbook2LetterM72CheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("72");
			log.setOrderInGroup("ZZZG");
			log.setContent(logbook2LetterM72TextField.getText());
			logToInsertList.add(log);
		}

	}

	private void validateFields(Timestamp businessDate, Map<Validation, Object> validations) {
		if (!logbook2LetterM70CheckBox.isSelected() || logbook2LetterM70LocalTimeTextField.getLocalTime()==null) {
			validations.put(Validation.M70_EMPTY_TIME, null);
		}
		if (!logbook2LetterM71CheckBox.isSelected() || logbook2LetterM71LocalTimeTextField.getLocalTime()==null) {
			validations.put(Validation.M71_EMPTY_TIME, null);
		}
		if (!logbook2LetterM72CheckBox.isSelected() || logbook2LetterM72TextField.getText().isEmpty()) {
			validations.put(Validation.M72_EMPTY_REASON, null);
		}

	}

	public void letterM(MouseEvent event) {
		helpCodeLetterDialog.showDialog((Stage)((ImageViewButton)event.getSource()).getScene().getWindow(),helpImageList);
	}

	public void openSelectTanksBalastedName(ActionEvent e) {
		logbook2LetterM71CheckBox.arm();

	}

	public void clearFilledInfo() {
		logbook2LetterM70CheckBox.setSelected(false);
		logbook2LetterM71CheckBox.setSelected(false);
		logbook2LetterM72CheckBox.setSelected(false);
	}
}
