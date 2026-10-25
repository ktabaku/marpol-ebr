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

import al.sybina.ebr.commons.utils.JavaFXUtils;
import al.sybina.ebr.logbook.commons.BallonMessage;
import al.sybina.ebr.logbook.dialogs.SelectTankerDialog;
import al.sybina.ebr.logbook.dialogs.help.HelpCodeLetterDialog;
import al.sybina.ebr.logbook.domain.reladomo.OilLog;
import al.sybina.ebr.logbook.views.OilRecordBookPart1ViewController.Validation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
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

public class OilRecordBookPart1CodeLetterFController implements Initializable {

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

	// codeLetter F
	@FXML
	private ImageViewButton imageViewButtonInfoLetterF;
	@FXML
	private CheckBox timeOfSystemFailureCheckBox;
	@FXML
	private LocalTimeTextField timeOfSystemFailureTimeField;
	@FXML
	private CheckBox timeWhenSystemHasBeenMadeOperationalCheckBox;
	@FXML
	private LocalTimeTextField meWhenSystemHasBeenMadeOperationalTimeField;
	@FXML
	private CheckBox reasonsForFailureCheckBox;
	@FXML
	private TextField reasonsForFailureTextField;
	@FXML
	public AnchorPane codeLetterContentAnchorPane;
	private ObservableList<Image> helpImageList = FXCollections.observableArrayList();
	private DateTimeFormatter dateTimeFormatter = EasyDateUtils.logbookPdfDateTimeFormatter;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		timeOfSystemFailureTimeField.setDateTimeFormatter(EasyDateUtils.logbookTimeFormatter);
		meWhenSystemHasBeenMadeOperationalTimeField.setDateTimeFormatter(EasyDateUtils.logbookTimeFormatter);
		reasonsForFailureTextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		JavaFXUtils.addFilledPseudoClass(reasonsForFailureTextField);
		JavaFXUtils.addFilledPseudoClass(timeOfSystemFailureTimeField);
		JavaFXUtils.addFilledPseudoClass(timeOfSystemFailureTimeField);
		try {

			Image helpImageLetterF1 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_HELPF1));
			
			Image helpImageLetterF2 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_HELPF2));
			
			Image helpImageLetterF3 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_HELPF3));
			 
			helpImageList.addAll(helpImageLetterF1, helpImageLetterF2, helpImageLetterF3);
			
			Image infoImageLetterF = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
			imageViewButtonInfoLetterF.setImage(infoImageLetterF);

		} catch (IOException e) {
			e.printStackTrace();
		}
		timeOfSystemFailureCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					timeOfSystemFailureTimeField.setDisable(false);
					timeWhenSystemHasBeenMadeOperationalCheckBox.setSelected(true);
					reasonsForFailureCheckBox.setSelected(true);
				} else {
					timeOfSystemFailureTimeField.setDisable(true);
					timeWhenSystemHasBeenMadeOperationalCheckBox.setSelected(false);
					reasonsForFailureCheckBox.setSelected(false);
				}
				timeOfSystemFailureTimeField.setLocalTime(null);
			}

		});
		timeWhenSystemHasBeenMadeOperationalCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {	
				if(newValue) {
					meWhenSystemHasBeenMadeOperationalTimeField.setDisable(false);
				} else {
					meWhenSystemHasBeenMadeOperationalTimeField.setDisable(true);
				}
				meWhenSystemHasBeenMadeOperationalTimeField.setLocalTime(null);
			}
		});
		reasonsForFailureCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					reasonsForFailureTextField.setDisable(false);
				} else {
					reasonsForFailureTextField.setDisable(true);
				}
				reasonsForFailureTextField.setText("");
			}
		});
	}

	public void processF(OilLog logToInsert, List<OilLog> logToInsertList, Map<Validation, Object> validations) {
		validateFields(logToInsert.getBusinessDate(), validations);
		
		if (timeOfSystemFailureCheckBox.isSelected() && timeOfSystemFailureTimeField.getLocalTime()!=null) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("19");
			log.setContent(timeOfSystemFailureTimeField.getLocalTime().format(EasyDateUtils.logbookTimeFormatter));
			log.setOrderInGroup("ZE");
			logToInsertList.add(log);

		}
		if (timeWhenSystemHasBeenMadeOperationalCheckBox.isSelected() && meWhenSystemHasBeenMadeOperationalTimeField.getLocalTime()!=null) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("20");
			log.setContent(meWhenSystemHasBeenMadeOperationalTimeField.getLocalTime().format(EasyDateUtils.logbookTimeFormatter));
			log.setOrderInGroup("ZF");
			logToInsertList.add(log);

		}
		if (reasonsForFailureCheckBox.isSelected()) {
			OilLog log = (OilLog) logToInsert.clone();
			log.setCodeNumber("21");
			log.setContent(reasonsForFailureTextField.getText());
			log.setOrderInGroup("ZG");
			logToInsertList.add(log);

		}
	}

	private void validateFields(Date registrationDate, Map<Validation, Object> validations) {
		if (reasonsForFailureCheckBox.isSelected() && reasonsForFailureTextField.getText().trim().isEmpty()) {
			validations.put(Validation.F21_EMPTY, null);
		}
		if(timeOfSystemFailureCheckBox.isSelected() && timeOfSystemFailureTimeField.getLocalTime()==null) {
			validations.put(Validation.F19_EMPTY, null);
		}
		if(!timeOfSystemFailureCheckBox.isSelected() && !timeWhenSystemHasBeenMadeOperationalCheckBox.isSelected()) {
			validations.put(Validation.F19_OR_F20_REQUIRED, null);
		}
		if(timeWhenSystemHasBeenMadeOperationalCheckBox.isSelected() && !reasonsForFailureCheckBox.isSelected()) {
			validations.put(Validation.F21_REQUIRED, null);
		}
		if(timeOfSystemFailureCheckBox.isSelected() && !reasonsForFailureCheckBox.isSelected()) {
			validations.put(Validation.F21_REQUIRED, null);
		}
//		if(registrationDate != null && timeOfSystemFailureCheckBox.isSelected() && timeOfSystemFailureTimeField.getLocalTime()!=null) {
//			LocalDateTime registrationLocalDateTime = Instant.ofEpochMilli(registrationDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime().plusDays(1);
//			if(registrationLocalDateTime.isBefore(timeOfSystemFailureTimeField.getLocalTime())) {
//				validations.put(Validation.F19_TIME_AFTER_REGISTRATION_DATE, null);
//			}
//		}
//		if(registrationDate != null && timeWhenSystemHasBeenMadeOperationalCheckBox.isSelected() && meWhenSystemHasBeenMadeOperationalTimeField.getLocalTime()!=null) {
//			LocalDateTime registrationLocalDateTime = Instant.ofEpochMilli(registrationDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime().plusDays(1);
//			if(registrationLocalDateTime.isBefore(meWhenSystemHasBeenMadeOperationalTimeField.getLocalTime())) {
//				validations.put(Validation.F20_TIME_AFTER_REGISTRATION_DATE, null);
//			}
//		}
	}

	public void actionPerformedF(MouseEvent event) {
		helpCodeLetterDialog.showDialog((Stage)((ImageViewButton)event.getSource()).getScene().getWindow(),helpImageList);
	}

	public void restoreLogGroup(List<OilLog> logGroup) {
		if(logGroup==null || logGroup.isEmpty()) return;
		List<OilLog> codeBOilLogs = logGroup.stream().filter(c -> c.getCodeLetter().equals("F")).collect(Collectors.toList());
		for (OilLog oilLog : codeBOilLogs) {
			if(oilLog.getCorrectionNote()!=null && oilLog.getCorrectionNote().equals("Corrected")) continue;
			String value;
			switch (oilLog.getCodeNumber()) {
				case "19":
					timeOfSystemFailureCheckBox.setSelected(true);
					value = oilLog.getContent();
					timeOfSystemFailureTimeField.setLocalTime(LocalTime.parse(value, EasyDateUtils.logbookTimeFormatter));
//					log.setContent(timeOfSystemFailureTimeField.getLocalTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
					break;
				case "20":
					timeWhenSystemHasBeenMadeOperationalCheckBox.setSelected(true);
					value = oilLog.getContent();
					meWhenSystemHasBeenMadeOperationalTimeField.setLocalTime(LocalTime.parse(value, EasyDateUtils.logbookTimeFormatter));
//					log.setContent(meWhenSystemHasBeenMadeOperationalTimeField.getLocalTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
					break;
				case "18":
					reasonsForFailureCheckBox.setSelected(true);
					value = oilLog.getContent();
					reasonsForFailureTextField.setText(value);
//					log.setContent(reasonsForFailureTextField.getText());
					break;
				default:
					break;
			}
		}
	}

	public void clearFilledInfo() {
		timeOfSystemFailureCheckBox.setSelected(false);
		timeWhenSystemHasBeenMadeOperationalCheckBox.setSelected(false);
		reasonsForFailureCheckBox.setSelected(false);
		
		timeOfSystemFailureTimeField.setLocalTime(null);
		meWhenSystemHasBeenMadeOperationalTimeField.setLocalTime(null);
		reasonsForFailureTextField.clear();
	}

}
