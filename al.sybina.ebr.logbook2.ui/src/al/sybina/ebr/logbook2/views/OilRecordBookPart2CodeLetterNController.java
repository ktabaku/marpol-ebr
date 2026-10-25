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

import al.sybina.ebr.commons.utils.CoordinateUIUtils;
import al.sybina.ebr.commons.utils.JavaFXUtils;
import al.sybina.ebr.commons.utils.MathUtils;
import al.sybina.ebr.logbook.dialogs.help.HelpCodeLetterDialog;
import al.sybina.ebr.logbook.domain.reladomo.OilLog;
import al.sybina.ebr.logbook2.views.OilRecordBookPart2ViewController.Validation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import jidefx.scene.control.field.DoubleField;
import jidefx.scene.control.field.FormattedTextField;

public class OilRecordBookPart2CodeLetterNController implements Initializable {

	private static final Logger logger = LoggerCreator.createLogger(OilRecordBookPart2CodeLetterNController.class);

	@Inject
	private IResourceProviderService resourcesService;
	@Inject
	private ImageProvider imageProvider;
	@Inject
	private HelpCodeLetterDialog helpCodeLetterDialog;
	@FXML
	private CheckBox logbook2LetterN73TimeCheckBox;
	@FXML
	private LocalTimeTextField logbook2LetterN73LocalTimeTextField;
	@FXML
	private CheckBox logbook2LetterN74PositionCheckBox;
	@FXML
	private FormattedTextField logbook2LetterN74LatTextField;
	@FXML
	private FormattedTextField logbook2LetterN74LongTextField;
	@FXML
	private TextField logbook2LetterN74PortTextField;
	@FXML
	private CheckBox logbook2LetterN75QuantityCheckBox;
	@FXML
	private DoubleField logbook2LetterN75QuantityDoubleField;
	@FXML
	private CheckBox logbook2LetterN76RemarksCheckBox;
	@FXML
	private TextField logbook2LetterN76RemarksTextField;
	@FXML
	private ImageViewButton infoLetterAImageViewButton;
	@FXML
	public AnchorPane codeLetterContentAnchorPane;
	private DateTimeFormatter timeFormatter = EasyDateUtils.logbookTimeFormatter;
	private ObservableList<Image> helpImageList = FXCollections.observableArrayList();
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MathUtils.setStringConverter(logbook2LetterN75QuantityDoubleField);
		MathUtils.setMinMaxValues(logbook2LetterN75QuantityDoubleField, 0.0, null);
		try {

			Image helpImageLetterN2 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPN2));
			
			Image helpImageLetterN1 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPN1));
			
			Image helpImageLetterN3 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPN3));
			
			helpImageList.addAll(helpImageLetterN2, helpImageLetterN1, helpImageLetterN3);
			
			Image infoIconImageInfoLetterA = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
			infoLetterAImageViewButton.setImage(infoIconImageInfoLetterA);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logbook2LetterN73LocalTimeTextField.setDisable(true);
		logbook2LetterN74LatTextField.setDisable(true);
		logbook2LetterN74LongTextField.setDisable(true);
		CoordinateUIUtils.initializeLatitudeField(logbook2LetterN74LatTextField);
		CoordinateUIUtils.initializeLongitudeField(logbook2LetterN74LongTextField);
		logbook2LetterN74PortTextField.setDisable(true);
		logbook2LetterN75QuantityDoubleField.setDisable(true);
		logbook2LetterN75QuantityDoubleField.setText("");
		logbook2LetterN76RemarksTextField.setDisable(true);
		logbook2LetterN74PortTextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		logbook2LetterN76RemarksTextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		JavaFXUtils.addFilledPseudoClass(logbook2LetterN76RemarksTextField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterN74PortTextField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterN75QuantityDoubleField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterN74LatTextField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterN74LongTextField);
		JavaFXUtils.addFilledPseudoClass(logbook2LetterN73LocalTimeTextField);
		logbook2LetterN73LocalTimeTextField.setDateTimeFormatter(timeFormatter);
		logbook2LetterN73TimeCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterN73LocalTimeTextField.setDisable(false);

			} else {
				logbook2LetterN73LocalTimeTextField.setDisable(true);
				logbook2LetterN73LocalTimeTextField.setLocalTime(null);
			}

		});
		logbook2LetterN74PositionCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterN74LatTextField.setDisable(false);
				logbook2LetterN74LongTextField.setDisable(false);
				logbook2LetterN74PortTextField.setDisable(false);

			} else {
				logbook2LetterN74LatTextField.setDisable(true);
				logbook2LetterN74LongTextField.setDisable(true);
				logbook2LetterN74PortTextField.setDisable(true);
				logbook2LetterN74LatTextField.setText("00°00.00'N");
				logbook2LetterN74LongTextField.setText("000°00.00'W");
				logbook2LetterN74PortTextField.setText("");
			}

		});
		logbook2LetterN75QuantityCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterN75QuantityDoubleField.setDisable(false);

			} else {
				logbook2LetterN75QuantityDoubleField.setDisable(true);
				logbook2LetterN75QuantityDoubleField.setText("");
			}

		});
		logbook2LetterN76RemarksCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterN76RemarksTextField.setDisable(false);

			} else {
				logbook2LetterN76RemarksTextField.setDisable(true);
				logbook2LetterN76RemarksTextField.setText("");
			}

		});
	}

	public void processN(OilLog logToInsert, List<OilLog> logToInsertList, Map<Validation, Object> validations) {
		validateFields(logToInsert.getBusinessDate(), validations);
		if (logbook2LetterN73TimeCheckBox.isSelected()&&logbook2LetterN73LocalTimeTextField.getLocalTime()!=null) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("73");
			log.setOrderInGroup("ZZZH");
			log.setContent("TIME: " + logbook2LetterN73LocalTimeTextField.getLocalTime().format(timeFormatter));
			logToInsertList.add(log);
		}
		if (logbook2LetterN74PositionCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("74");
			log.setOrderInGroup("ZZZI");
			log.setContent(logbook2LetterN74PortTextField.getText() + " LAT:" + logbook2LetterN74LatTextField.getText() + " LONG:" + logbook2LetterN74LongTextField.getText());
			logToInsertList.add(log);
		}
		if (logbook2LetterN75QuantityCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("75");
			log.setOrderInGroup("ZZZJ");
			log.setContent(logbook2LetterN75QuantityDoubleField.getText() + "M3");
			logToInsertList.add(log);
		}
		if (logbook2LetterN76RemarksCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("76");
			log.setOrderInGroup("ZZZK");
			log.setContent(logbook2LetterN76RemarksTextField.getText());
			logToInsertList.add(log);
		}

	}

	private void validateFields(Timestamp businessDate, Map<Validation, Object> validations) {
		if (!logbook2LetterN73TimeCheckBox.isSelected() || logbook2LetterN73LocalTimeTextField.getLocalTime()==null) {
			validations.put(Validation.N73_EMPTY_TIME, null);
		}
		if (!logbook2LetterN74PositionCheckBox.isSelected() || logbook2LetterN74PortTextField.getText().isEmpty()) {
			validations.put(Validation.N74_EMPTY_POSITION, null);
		}
		if (logbook2LetterN74PositionCheckBox.isSelected() && (logbook2LetterN74LatTextField.getText().equals("00°00.00'N") || logbook2LetterN74LongTextField.getText().equals("000°00.00'W"))) {

			validations.put(Validation.N74_LAT_LON_LAT_LON_ZERO, null);
		}
		if (!logbook2LetterN75QuantityCheckBox.isSelected() || logbook2LetterN75QuantityDoubleField.getText().isEmpty()) {
			validations.put(Validation.N75_EMPTY_QUANTITY, null);
		}
		if (!logbook2LetterN76RemarksCheckBox.isSelected() || logbook2LetterN76RemarksTextField.getText().isEmpty()) {
			validations.put(Validation.N76_EMPTY_REMARKS, null);
		}

	}

	public void letterN(MouseEvent event) {
		helpCodeLetterDialog.showDialog((Stage)((ImageViewButton)event.getSource()).getScene().getWindow(),helpImageList);
	}

	public void clearFilledInfo() {
		logbook2LetterN73TimeCheckBox.setSelected(false);
		logbook2LetterN74PositionCheckBox.setSelected(false);
		logbook2LetterN75QuantityCheckBox.setSelected(false);
		logbook2LetterN76RemarksCheckBox.setSelected(false);

	}

}
