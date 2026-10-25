package al.sybina.ebr.logbook.views;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.e4.tools.resources.WorkbenchResourceProvider;
import org.eclipse.e4.tools.services.IResourceProviderService;
import org.eclipse.fx.ui.services.resources.ImageProvider;

import com.oceanpro.utils.EasyDateUtils;

import al.sybina.ebr.commons.utils.CoordinateUIUtils;
import al.sybina.ebr.commons.utils.JavaFXUtils;
import al.sybina.ebr.commons.utils.MathUtils;
import al.sybina.ebr.logbook.commons.BallonMessage;
import al.sybina.ebr.logbook.dialogs.SelectTankerDialog;
import al.sybina.ebr.logbook.dialogs.help.HelpCodeLetterDialog;
import al.sybina.ebr.logbook.domain.reladomo.OilLog;
import al.sybina.ebr.logbook.views.OilRecordBookPart1ViewController.Validation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import jfxtras.scene.control.ImageViewButton;
import jfxtras.scene.control.LocalDateTimeTextField;
import jfxtras.scene.control.LocalTimeTextField;
import jidefx.scene.control.field.DoubleField;
import jidefx.scene.control.field.FormattedTextField;

public class OilRecordBookPart1CodeLetterGController implements Initializable {

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
	// codeLetter G
	@FXML
	private ImageViewButton imageViewButtonInfoLetterG;
	@FXML
	private CheckBox timeOfOccurrenceCheckBox;
	@FXML
	private LocalTimeTextField timeOfOccurrenceTimeField;
	@FXML
	private CheckBox placeOrPositionOfShipAtTimeOfOccurrenceCheckBox;
	@FXML
	private FormattedTextField placeOrPositionOfShipAtTimeOfOccurrenceLatTextField;
	@FXML
	private FormattedTextField placeOrPositionOfShipAtTimeOfOccurrenceLongTextField;
	@FXML
	private CheckBox approximateAuantityAndTypeOfOilCheckBox;
	@FXML
	private TextField typeOfOilTextField;
	@FXML
	private DoubleField approximateQuantityAndTypeOfOilDoubleField;
	@FXML
	private CheckBox circumstancesOfDischargeOrEscapeCheckBox;
	@FXML
	private TextField circumstancesOfDischargeOrEscapeTextField;
	@FXML
	public AnchorPane codeLetterContentAnchorPane;
	
	private ObservableList<Image> helpImageList = FXCollections.observableArrayList();
	
	private TextField currentSelectedTextField;

	private DateTimeFormatter dateTimeFormatter = EasyDateUtils.logbookPdfDateTimeFormatter;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MathUtils.setStringConverter(approximateQuantityAndTypeOfOilDoubleField);
		MathUtils.setMinMaxValues(approximateQuantityAndTypeOfOilDoubleField, 0.0, null);
		timeOfOccurrenceTimeField.setDateTimeFormatter(EasyDateUtils.logbookTimeFormatter);
		typeOfOilTextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		circumstancesOfDischargeOrEscapeTextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		JavaFXUtils.addFilledPseudoClass(placeOrPositionOfShipAtTimeOfOccurrenceLatTextField);
		JavaFXUtils.addFilledPseudoClass(placeOrPositionOfShipAtTimeOfOccurrenceLongTextField);
		JavaFXUtils.addFilledPseudoClass(approximateQuantityAndTypeOfOilDoubleField);
		JavaFXUtils.addFilledPseudoClass(typeOfOilTextField);
		JavaFXUtils.addFilledPseudoClass(circumstancesOfDischargeOrEscapeTextField);
		JavaFXUtils.addFilledPseudoClass(timeOfOccurrenceTimeField);
		try {
			Image helpImageLetterG1 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_HELPG1));
			
			Image helpImageLetterG2 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_HELPG2));
			
			Image helpImageLetterG3 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_HELPG3));
			
			helpImageList.addAll(helpImageLetterG1, helpImageLetterG2, helpImageLetterG3);
			
			Image infoImageLetterG = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
			imageViewButtonInfoLetterG.setImage(infoImageLetterG);

			CoordinateUIUtils.initializeLatitudeField(placeOrPositionOfShipAtTimeOfOccurrenceLatTextField);
			CoordinateUIUtils.initializeLongitudeField(placeOrPositionOfShipAtTimeOfOccurrenceLongTextField);

		} catch (IOException e) {
			e.printStackTrace();
		}
		timeOfOccurrenceCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					timeOfOccurrenceTimeField.setDisable(false);
					placeOrPositionOfShipAtTimeOfOccurrenceCheckBox.setSelected(true);
					approximateAuantityAndTypeOfOilCheckBox.setSelected(true);
					circumstancesOfDischargeOrEscapeCheckBox.setSelected(true);
				} else {
					timeOfOccurrenceTimeField.setDisable(true);
					placeOrPositionOfShipAtTimeOfOccurrenceCheckBox.setSelected(false);
					approximateAuantityAndTypeOfOilCheckBox.setSelected(false);
					circumstancesOfDischargeOrEscapeCheckBox.setSelected(false);
				}
				timeOfOccurrenceTimeField.setLocalTime(null);
			}

		});
		placeOrPositionOfShipAtTimeOfOccurrenceCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					placeOrPositionOfShipAtTimeOfOccurrenceLatTextField.setDisable(false);
					placeOrPositionOfShipAtTimeOfOccurrenceLongTextField.setDisable(false);
				} else {
					placeOrPositionOfShipAtTimeOfOccurrenceLatTextField.setDisable(true);
					placeOrPositionOfShipAtTimeOfOccurrenceLongTextField.setDisable(true);
				}
				placeOrPositionOfShipAtTimeOfOccurrenceLatTextField.setText("00°00.00'N");
				placeOrPositionOfShipAtTimeOfOccurrenceLongTextField.setText("000°00.00'W");
			}

		});
		approximateAuantityAndTypeOfOilCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					approximateQuantityAndTypeOfOilDoubleField.setDisable(false);
					typeOfOilTextField.setDisable(false);
				} else {
					approximateQuantityAndTypeOfOilDoubleField.setDisable(true);
					typeOfOilTextField.setDisable(true);
				}
				approximateQuantityAndTypeOfOilDoubleField.setValue(null);
				typeOfOilTextField.setText("");
			}

		});
		circumstancesOfDischargeOrEscapeCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					circumstancesOfDischargeOrEscapeTextField.setDisable(false);
				} else {
					circumstancesOfDischargeOrEscapeTextField.setDisable(true);
				}
				circumstancesOfDischargeOrEscapeTextField.setText("");
			}

		});
	}

	public void processG(OilLog logToInsert, List<OilLog> logToInsertList, Map<Validation, Object> validations) {
		validateFields(logToInsert.getBusinessDate(), validations);
		
		if (timeOfOccurrenceCheckBox.isSelected() && timeOfOccurrenceTimeField.getLocalTime()!=null) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("22");
			log.setContent(timeOfOccurrenceTimeField.getLocalTime().format(EasyDateUtils.logbookTimeFormatter));
			log.setOrderInGroup("ZH");
			logToInsertList.add(log);
		}
		if (placeOrPositionOfShipAtTimeOfOccurrenceCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("23");
			log.setContent(placeOrPositionOfShipAtTimeOfOccurrenceLatTextField.getText() + " LAT " + placeOrPositionOfShipAtTimeOfOccurrenceLongTextField.getText() + " LONG");
			log.setOrderInGroup("ZI");
			logToInsertList.add(log);
		}
		if (approximateAuantityAndTypeOfOilCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("24");
			log.setContent(approximateQuantityAndTypeOfOilDoubleField.getValue() + " M3 AND TYPE OF OIL " + typeOfOilTextField.getText());
			log.setOrderInGroup("ZJ");
			logToInsertList.add(log);
		}
		if (circumstancesOfDischargeOrEscapeCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("25");
			log.setContent(circumstancesOfDischargeOrEscapeTextField.getText());
			log.setOrderInGroup("ZK");
			logToInsertList.add(log);
		}
	}

	private void validateFields(Date registrationDate, Map<Validation, Object> validations) {
		if (approximateAuantityAndTypeOfOilCheckBox.isSelected() && approximateQuantityAndTypeOfOilDoubleField.getValue()==null) {
			validations.put(Validation.G24_EMPTY, null);
		}
		if (approximateAuantityAndTypeOfOilCheckBox.isSelected() && typeOfOilTextField.getText().trim().isEmpty()) {
			validations.put(Validation.G24_TYPE_OIL_EMPTY, null);
		}
		if (circumstancesOfDischargeOrEscapeCheckBox.isSelected() && circumstancesOfDischargeOrEscapeTextField.getText().trim().isEmpty()) {
			validations.put(Validation.G25_EMPTY, null);
		}
		
		if(!timeOfOccurrenceCheckBox.isSelected()) {
			validations.put(Validation.G22_REQUIRED, null);
		}
		if(!placeOrPositionOfShipAtTimeOfOccurrenceCheckBox.isSelected()) {
			validations.put(Validation.G23_REQUIRED, null);
		}
		if(!approximateAuantityAndTypeOfOilCheckBox.isSelected()) {
			validations.put(Validation.G24_REQUIRED, null);
		}
		if(!circumstancesOfDischargeOrEscapeCheckBox.isSelected()) {
			validations.put(Validation.G25_REQUIRED, null);
		}
//		if(registrationDate != null && timeOfOccurrenceCheckBox.isSelected() && timeOfOccurrenceTimeField.getLocalTime()!=null) {
//			LocalDateTime registrationLocalDateTime = Instant.ofEpochMilli(registrationDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime().plusDays(1);
//			if(registrationLocalDateTime.isBefore(timeOfOccurrenceTimeField.getLocalTime())) {
//				validations.put(Validation.G22_TIME_AFTER_REGISTRATION_DATE, null);
//			}
//		}
		if(placeOrPositionOfShipAtTimeOfOccurrenceCheckBox.isSelected()) {
			if(placeOrPositionOfShipAtTimeOfOccurrenceLatTextField.getText().equals("00°00.00'N") || placeOrPositionOfShipAtTimeOfOccurrenceLongTextField.getText().equals("000°00.00'W")) {
				validations.put(Validation.G23_LAT_LON_ZERO, null);
			}
		}
	}

	public void actionPerformedG(MouseEvent event) {
		
		helpCodeLetterDialog.showDialog((Stage)((ImageViewButton)event.getSource()).getScene().getWindow(),helpImageList);

	}

	public void restoreLogGroup(List<OilLog> logGroup) {
		if(logGroup==null || logGroup.isEmpty()) return;
		List<OilLog> codeBOilLogs = logGroup.stream().filter(c -> c.getCodeLetter().equals("G")).collect(Collectors.toList());
		for (OilLog oilLog : codeBOilLogs) {
			if(oilLog.getCorrectionNote()!=null && oilLog.getCorrectionNote().equals("Corrected")) continue;
			String value;
			switch (oilLog.getCodeNumber()) {
				case "22":
					timeOfOccurrenceCheckBox.setSelected(true);
					value = oilLog.getContent();
					timeOfOccurrenceTimeField.setLocalTime(LocalTime.parse(value, EasyDateUtils.logbookTimeFormatter));
//					log.setContent(timeOfOccurrenceTimeField.getLocalTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
					break;
				case "23":
					placeOrPositionOfShipAtTimeOfOccurrenceCheckBox.setSelected(true);
					value = oilLog.getContent();
					String lat = value.substring(0, value.indexOf(" Lat "));
					value = value.replace(lat+" Lat ", "");
					String lon = value.substring(0, value.indexOf(" Long"));
					placeOrPositionOfShipAtTimeOfOccurrenceLatTextField.setText(lat);
					placeOrPositionOfShipAtTimeOfOccurrenceLongTextField.setText(lon);
//					log.setContent(placeOrPositionOfShipAtTimeOfOccurrenceLatTextField.getText() + " Lat " + placeOrPositionOfShipAtTimeOfOccurrenceLongTextField.getText() + " Long");
					break;
				case "24":
					approximateAuantityAndTypeOfOilCheckBox.setSelected(true);
					value = oilLog.getContent();
					String approximateQuantityValue = value.substring(0, value.indexOf(" M3 and type of oil "));
					String typeOfOilValue = value.replace(approximateQuantityValue+" M3 and type of oil ", "");
					approximateQuantityAndTypeOfOilDoubleField.setValue(Double.parseDouble(approximateQuantityValue));
					typeOfOilTextField.setText(typeOfOilValue);
//					log.setContent(approximateAuantityAndTypeOfOilTextField.getText() + " M3 and type of oil " + typeOfOilTextField.getText());
					break;
				case "25":
					circumstancesOfDischargeOrEscapeCheckBox.setSelected(true);
					value = oilLog.getContent();
					circumstancesOfDischargeOrEscapeTextField.setText(value);
//					log.setContent(circumstancesOfDischargeOrEscapeTextField.getText());
					break;
				default:
					break;
			}
		}
	}

	public void clearFilledInfo() {
		timeOfOccurrenceCheckBox.setSelected(false);
		placeOrPositionOfShipAtTimeOfOccurrenceCheckBox.setSelected(false);
		approximateAuantityAndTypeOfOilCheckBox.setSelected(false);
		circumstancesOfDischargeOrEscapeCheckBox.setSelected(false);
		
		timeOfOccurrenceTimeField.setLocalTime(null);
		placeOrPositionOfShipAtTimeOfOccurrenceLatTextField.setText("00°00.00'N");
		placeOrPositionOfShipAtTimeOfOccurrenceLongTextField.setText("000°00.00'W");
		approximateQuantityAndTypeOfOilDoubleField.clear();
		typeOfOilTextField.clear();
		circumstancesOfDischargeOrEscapeTextField.clear();
	}

}
