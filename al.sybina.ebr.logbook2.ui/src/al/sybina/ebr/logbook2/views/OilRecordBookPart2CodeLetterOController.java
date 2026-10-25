package al.sybina.ebr.logbook2.views;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.inject.Inject;

import org.eclipse.e4.tools.resources.WorkbenchResourceProvider;
import org.eclipse.e4.tools.services.IResourceProviderService;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;
import org.eclipse.fx.ui.services.resources.ImageProvider;
import org.eclipse.fx.ui.services.theme.ThemeManager;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import com.oceanpro.utils.EasyDateUtils;

import al.sybina.ebr.commons.utils.JavaFXUtils;
import al.sybina.ebr.logbook.commons.BallonMessage;
import al.sybina.ebr.logbook.dialogs.SelectTankerDialog;
import al.sybina.ebr.logbook.dialogs.help.HelpCodeLetterDialog;
import al.sybina.ebr.logbook.domain.reladomo.OilLog;
import al.sybina.ebr.logbook.signature.Signature;
import al.sybina.ebr.logbook.signature.SignatureLetterMissedOperation;
import al.sybina.ebr.logbook2.views.OilRecordBookPart2ViewController.Validation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import jfxtras.scene.control.ImageViewButton;

public class OilRecordBookPart2CodeLetterOController implements Initializable {

	private Label label1 = new Label(" Notes:\r\n" + "This entry should be made only in exceptional cases\r\n" + "Date ‘1’ to be the date when the original operation was carried out.\r\n"
			+ "Date '2’ to be the current date i.e. the date when the entry is made.\r\n" + "Signed ‘1’:Signature of Officer making the “O” entry\r\n" + "Signed ‘2’:Signature of Officer making the missed entry\r\n" + "");

	private Label label2 = new Label("Note \r\n" + "Date ‘1’ to be the date when the original operation \r\n" + "was carried out\r\n" + "");

	private static final String option1 = "Entry pertaining to an earlier missed operational";
	private static final String option2 = "Pre-discharge pressure";
	private static final String option3 = "Leakage test of the cow piping system";
	private static final String option4 = "Other";

	private static final String optionA = "A. Loading of oil cargo";
	private static final String optionB = "B. Internal transfer of oil cargo during voyage";
	private static final String optionC = "C. Unloading of oil cargo";
	private static final String optionD = "D. Crude oil washing (COW tankers only)";
	private static final String optionE = "E. Ballasting of cargo tanks";
	private static final String optionF = "F. Ballasting of dedicated clean ballast tanks (CBT tankers only)";
	private static final String optionG = "G. Cleaning of cargo tanks";
	private static final String optionH = "H. Discharge of dirty ballast";
	private static final String optionI = "I. Discharge of water from slop tanks into the sea";
	private static final String optionJ = "J. Collection, transfer and disposal of residues and oily mixtures not otherwise dealt with";
	private static final String optionK = "K. Discharge of clean ballast contained in cargo tanks";
	private static final String optionL = "L. Discharge of ballast from dedicated clean ballast tanks (CBT tankers only)";
	private static final String optionM = "M. Condition of oil discharge monitoring and control system";
	private static final String optionN = "N. Accidental or other exceptional discharges of oil";
	private static final String optionO = "O. Additional operational procedures and general remarks";
	private static final String optionP = "P. Loading of ballast water";
	private static final String optionQ = "Q. Re-allocation of ballast water within the ship";
	private static final String optionR = "R. Ballast water discharge to reception facility";

	private static final Logger logger = LoggerCreator.createLogger(OilRecordBookPart2CodeLetterOController.class);
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
	private ImageViewButton infoLetterAImageViewButton;
	@FXML
	private ImageViewButton helpLetterOImageViewButton;
	@FXML
	private ImageViewButton helpLetterOImageViewButton1;
	@FXML
	private ComboBox<String> logbook2LetterOComboBox;
	@FXML
	private Label operationTypeLabel;
	@FXML
	private CheckBox selectOperationCheckBox;
	@FXML
	private VBox mainVBox;
	@FXML
	private VBox case234VBox;
	@FXML
	private TextField remarksTextField;
	@FXML
	private VBox case1VBox;
	@FXML
	public ComboBox<String> logbook2LetterOEntryPertainingToAnEarlierMissedOperationalComboBox;
	@FXML
	public AnchorPane missedCodeLetterCodeLetterContentAnchorPane;
	@FXML
	public DatePicker compileDateCodeLetterO;
	@FXML
	private Button signatureMissedCodeLetterButton;
	private OilRecordBookPart2ViewController oilRecordBookPart2ViewController;
	@Inject
	private Signature signature;
	@Inject
	private SignatureLetterMissedOperation signatureLetterMissedOperation;

	private FXMLLoader loaderMissedCodeLetterO;

	private OilRecordBookPart2CodeLetterOMissedOperationController oilRecordBookPart2CodeLetterOMissedOperationController;
	private static JFXButton closeButton;
	@Inject
	private static ThemeManager themeManager;
	private ObservableList<Image> helpImageList = FXCollections.observableArrayList();
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		JavaFXUtils.addFilledPseudoClass(compileDateCodeLetterO);
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
		try {

			Image helpImageLetterO1 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPO1));
			
			Image helpImageLetterO2 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPO2));
			
			Image helpImageLetterO3 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPO3));
			
			Image helpImageLetterO4 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPO4));
			
			Image helpImageLetterO5 = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK2_HELPO5));
			
			helpImageList.addAll(helpImageLetterO2, helpImageLetterO1, helpImageLetterO3, helpImageLetterO4, helpImageLetterO5);
			
			Image addImageIdentityOfTanks = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_ADD));

			Image infoIconImageInfoLetterA = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
			infoLetterAImageViewButton.setImage(infoIconImageInfoLetterA);
			helpLetterOImageViewButton.setImage(infoIconImageInfoLetterA);
			helpLetterOImageViewButton1.setImage(infoIconImageInfoLetterA);
			helpLetterOImageViewButton1.setDisable(false);
			compileDateCodeLetterO.setDisable(false);
			remarksTextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		} catch (IOException e) {
			e.printStackTrace();
		}
		logbook2LetterOComboBox.getItems().addAll(option1, option2, option3, option4);
		logbook2LetterOComboBox.setValue("");
		logbook2LetterOComboBox.setDisable(true);
		mainVBox.getChildren().remove(case1VBox);
		mainVBox.getChildren().remove(case234VBox);
		logbook2LetterOEntryPertainingToAnEarlierMissedOperationalComboBox.setDisable(false);
		selectOperationCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterOComboBox.setDisable(false);
				logbook2LetterOEntryPertainingToAnEarlierMissedOperationalComboBox.setValue("");
			} else {
				logbook2LetterOComboBox.setDisable(true);
				logbook2LetterOComboBox.setValue("");
				logbook2LetterOEntryPertainingToAnEarlierMissedOperationalComboBox.setValue("");
			}

		});

		logbook2LetterOComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
			logbook2LetterOEntryPertainingToAnEarlierMissedOperationalComboBox.setValue("");
			signatureLetterMissedOperation.getSignatureLetterMissedOperationController().resetSignature(null);
			compileDateCodeLetterO.setValue(null);
			if (newValue.equals(option1)) {
				showInformation("Please insert  the current date i.e. the date when the entry is made", "Signed ‘1’:Signature of Officer making the “O”\r\n" + "\r\n" + "After signature proceed to step 2\r\n" + "");
				if (!mainVBox.getChildren().contains(case1VBox)) {
					mainVBox.getChildren().add(case1VBox);
				}
				if (mainVBox.getChildren().contains(case234VBox)) {
					mainVBox.getChildren().remove(case234VBox);
				}
				remarksTextField.setText("");
			} else if (newValue.equals(option2)) {
				remarksTextField.setText("");
				if (mainVBox.getChildren().contains(case1VBox)) {
					mainVBox.getChildren().remove(case1VBox);
				}
				if (!mainVBox.getChildren().contains(case234VBox)) {
					mainVBox.getChildren().add(case234VBox);
				}
				operationTypeLabel.setText("  Pre-discharge pressure");

			} else if (newValue.equals(option3)) {
				remarksTextField.setText("");
				if (mainVBox.getChildren().contains(case1VBox)) {
					mainVBox.getChildren().remove(case1VBox);
				}
				if (!mainVBox.getChildren().contains(case234VBox)) {
					mainVBox.getChildren().add(case234VBox);
				}
				operationTypeLabel.setText("  Leakage test of the cow piping system");

			} else if (newValue.equals(option4)) {
				remarksTextField.setText("");
				if (mainVBox.getChildren().contains(case1VBox)) {
					mainVBox.getChildren().remove(case1VBox);
				}
				if (!mainVBox.getChildren().contains(case234VBox)) {
					mainVBox.getChildren().add(case234VBox);
				}
				operationTypeLabel.setText("  Other");
			} else {

				if (mainVBox.getChildren().contains(case1VBox)) {
					mainVBox.getChildren().remove(case1VBox);
				}
				if (mainVBox.getChildren().contains(case234VBox)) {
					mainVBox.getChildren().remove(case234VBox);
				}
				operationTypeLabel.setText("");
			}
		});
		missedCodeLetterCodeLetterContentAnchorPane.setVisible(false);
		compileDateCodeLetterO.setConverter(new StringConverter<LocalDate>() {
			private DateTimeFormatter dateTimeFormatter = EasyDateUtils.logbookSimpleDateTimeFormatter;

			@Override
			public String toString(LocalDate localDate) {
				if (localDate == null)
					return "";
				return dateTimeFormatter.format(localDate);
			}

			@Override
			public LocalDate fromString(String dateString) {
				if (dateString == null || dateString.trim().isEmpty()) {
					return null;
				}
				return LocalDate.parse(dateString, dateTimeFormatter);
			}
		});
		compileDateCodeLetterO.valueProperty().addListener((observable, oldDate, newDate) -> {
			if (compileDateCodeLetterO.getValue() != null) {
				missedCodeLetterCodeLetterContentAnchorPane.setVisible(true);
			} else {
				missedCodeLetterCodeLetterContentAnchorPane.setVisible(false);
			}

		});
		logbook2LetterOEntryPertainingToAnEarlierMissedOperationalComboBox.getItems().addAll(optionA, optionB, optionC, optionD, optionE, optionF, optionG, optionH, optionI, optionJ, optionK, optionL, optionM, optionN,  optionO, optionP, optionQ,
				optionR);
		logbook2LetterOEntryPertainingToAnEarlierMissedOperationalComboBox.setValue("");

		logbook2LetterOEntryPertainingToAnEarlierMissedOperationalComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
			compileDateCodeLetterO.setValue(null);
			ObservableList<Node> oldCodeLetter = missedCodeLetterCodeLetterContentAnchorPane.getChildren();
			if (oldValue.equals(optionA)) {
				oilRecordBookPart2ViewController.setOldCodLetterBackListNode(oldCodeLetter, "A");
			} else if (oldValue.equals(optionB)) {
				oilRecordBookPart2ViewController.setOldCodLetterBackListNode(oldCodeLetter, "B");
			} else if (oldValue.equals(optionC)) {
				oilRecordBookPart2ViewController.setOldCodLetterBackListNode(oldCodeLetter, "C");
			} else if (oldValue.equals(optionD)) {
				oilRecordBookPart2ViewController.setOldCodLetterBackListNode(oldCodeLetter, "D");
			} else if (oldValue.equals(optionE)) {
				oilRecordBookPart2ViewController.setOldCodLetterBackListNode(oldCodeLetter, "E");
			} else if (oldValue.equals(optionF)) {
				oilRecordBookPart2ViewController.setOldCodLetterBackListNode(oldCodeLetter, "F");
			} else if (oldValue.equals(optionG)) {
				oilRecordBookPart2ViewController.setOldCodLetterBackListNode(oldCodeLetter, "G");
			} else if (oldValue.equals(optionH)) {
				oilRecordBookPart2ViewController.setOldCodLetterBackListNode(oldCodeLetter, "H");
			} else if (oldValue.equals(optionI)) {
				oilRecordBookPart2ViewController.setOldCodLetterBackListNode(oldCodeLetter, "I");
			} else if (oldValue.equals(optionJ)) {
				oilRecordBookPart2ViewController.setOldCodLetterBackListNode(oldCodeLetter, "J");
			} else if (oldValue.equals(optionK)) {
				oilRecordBookPart2ViewController.setOldCodLetterBackListNode(oldCodeLetter, "K");
			} else if (oldValue.equals(optionL)) {
				oilRecordBookPart2ViewController.setOldCodLetterBackListNode(oldCodeLetter, "L");
			} else if (oldValue.equals(optionM)) {
				oilRecordBookPart2ViewController.setOldCodLetterBackListNode(oldCodeLetter, "M");
			} else if (oldValue.equals(optionN)) {
				oilRecordBookPart2ViewController.setOldCodLetterBackListNode(oldCodeLetter, "N");
			} else if (oldValue.equals(optionO)) {
				//
			} else if (oldValue.equals(optionP)) {
				oilRecordBookPart2ViewController.setOldCodLetterBackListNode(oldCodeLetter, "P");
			} else if (oldValue.equals(optionQ)) {
				oilRecordBookPart2ViewController.setOldCodLetterBackListNode(oldCodeLetter, "Q");
			} else if (oldValue.equals(optionR)) {
				oilRecordBookPart2ViewController.setOldCodLetterBackListNode(oldCodeLetter, "R");
			} 
			
			missedCodeLetterCodeLetterContentAnchorPane.getChildren().removeAll(missedCodeLetterCodeLetterContentAnchorPane.getChildren());

			if (newValue.equals(optionA)) {
				oilRecordBookPart2ViewController.setAllCodeLetterAtO("A");
			} else if (newValue.equals(optionB)) {
				oilRecordBookPart2ViewController.setAllCodeLetterAtO("B");
			} else if (newValue.equals(optionC)) {
				oilRecordBookPart2ViewController.setAllCodeLetterAtO("C");
			} else if (newValue.equals(optionD)) {
				oilRecordBookPart2ViewController.setAllCodeLetterAtO("D");
			} else if (newValue.equals(optionE)) {
				oilRecordBookPart2ViewController.setAllCodeLetterAtO("E");
			} else if (newValue.equals(optionF)) {
				oilRecordBookPart2ViewController.setAllCodeLetterAtO("F");
			} else if (newValue.equals(optionG)) {
				oilRecordBookPart2ViewController.setAllCodeLetterAtO("G");
			} else if (newValue.equals(optionH)) {
				oilRecordBookPart2ViewController.setAllCodeLetterAtO("H");
			} else if (newValue.equals(optionI)) {
				oilRecordBookPart2ViewController.setAllCodeLetterAtO("I");
			} else if (newValue.equals(optionJ)) {
				oilRecordBookPart2ViewController.setAllCodeLetterAtO("J");
			} else if (newValue.equals(optionK)) {
				oilRecordBookPart2ViewController.setAllCodeLetterAtO("K");
			} else if (newValue.equals(optionL)) {
				oilRecordBookPart2ViewController.setAllCodeLetterAtO("L");
			} else if (newValue.equals(optionM)) {
				oilRecordBookPart2ViewController.setAllCodeLetterAtO("M");
			} else if (newValue.equals(optionN)) {
				oilRecordBookPart2ViewController.setAllCodeLetterAtO("N");
			}else if (newValue.equals(optionO)) {
				try {
					loaderMissedCodeLetterO = new FXMLLoader(getClass().getResource("OilRecordBookPart2CodeLetterOMissedOperation.fxml"));
					loaderMissedCodeLetterO.load();
					oilRecordBookPart2CodeLetterOMissedOperationController = (OilRecordBookPart2CodeLetterOMissedOperationController) loaderMissedCodeLetterO.getController();
					oilRecordBookPart2CodeLetterOMissedOperationController.setBallonMessage(ballonMessage);
					oilRecordBookPart2CodeLetterOMissedOperationController.setImageProvider(imageProvider);
					oilRecordBookPart2CodeLetterOMissedOperationController.setResourcesService(resourcesService);
					missedCodeLetterCodeLetterContentAnchorPane.getChildren().add(loaderMissedCodeLetterO.getRoot());
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (newValue.equals(optionP)) {
				oilRecordBookPart2ViewController.setAllCodeLetterAtO("P");
			} else if (newValue.equals(optionQ)) {
				oilRecordBookPart2ViewController.setAllCodeLetterAtO("Q");
			} else if (newValue.equals(optionR)) {
				oilRecordBookPart2ViewController.setAllCodeLetterAtO("R");
			}
		});

	}

	public void letterA(MouseEvent event) {
		helpCodeLetterDialog.showDialog((Stage)((ImageViewButton)event.getSource()).getScene().getWindow(),helpImageList);
	}

	public void showBalloon(MouseEvent event) {
		ballonMessage.showGeneralBalloon((ImageViewButton) event.getTarget(), label1);
	}

	public void showBalloon1(MouseEvent event) {
		ballonMessage.showGeneralBalloon((ImageViewButton) event.getTarget(), label2);
	}

	public void processO(OilLog logToInsert, List<OilLog> logToInsertList, Map<Validation, Object> validations) {
		validateFields(logToInsert.getBusinessDate(), validations);
		if (selectOperationCheckBox.isSelected()) {
			if (logbook2LetterOComboBox.getValue().equals(option1)) {
				processOAndOtherLogsInsideCodeLetterO(logToInsert, logToInsertList, validations);	
			}else {
			OilLog log = (OilLog) logToInsert.clone();
			log.setOrderInGroup("A");
			//log.setCodeNumber(logbook2LetterOComboBox.getValue().equals(option2)?"2":logbook2LetterOComboBox.getValue().equals(option3)?"3":logbook2LetterOComboBox.getValue().equals(option4)?"4":"");
			log.setCodeNumber("");
			log.setContent(remarksTextField.getText());
			logToInsertList.add(log);
			}
		}
	}
	
	public void processOAndOtherLogsInsideCodeLetterO(OilLog logToInsertCodeLetterI, List<OilLog> logToInsertList, Map<Validation, Object> validations) {
		// process other logs inside code letter O
		LocalDate compileMissedOperationLocalDate = compileDateCodeLetterO.getValue();
		Timestamp compileMissedOperationTimestamp = null;
		String encodedMissedSignatureString = null;
		if (compileMissedOperationLocalDate == null) {
			validations.put(Validation.DATE_MISSING_CODE_LETTERO, null);
		} else {
			ZonedDateTime nowUTC = ZonedDateTime.now(ZoneOffset.UTC);
			LocalDateTime ldt = compileMissedOperationLocalDate.atTime(nowUTC.toLocalTime());
			ZonedDateTime utcZoned = ldt.atZone(ZoneId.of("UTC"));
			if (utcZoned.isAfter(nowUTC)) {
				validations.put(Validation.MISSED_DATE_AFTER_NOW, null);
			}
			compileMissedOperationTimestamp = Timestamp.valueOf(utcZoned.toLocalDateTime());
			WritableImage signatureMissedImage = signatureLetterMissedOperation.getSignatureLetterMissedOperationController().getSignatureImage();
			if (signatureMissedImage == null) {
				validations.put(Validation.SIGNATURE_MISSING_CODE_LETTERO, null);
			} else {
				try {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(SwingFXUtils.fromFXImage(signatureMissedImage, null), "png", baos);
					byte[] bytes = baos.toByteArray();
					encodedMissedSignatureString = Base64.getEncoder().encodeToString(bytes);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		if(oilRecordBookPart2ViewController.getGroupUUID()==null)
			oilRecordBookPart2ViewController.setGroupUUID(UUID.randomUUID().toString());
		
		String rank = signatureLetterMissedOperation.getSignatureLetterMissedOperationController().getRank();
		if(rank==null) {
			validations.put(Validation.SIGNER_RANK_MISSING, null);
		}
		String name = signatureLetterMissedOperation.getSignatureLetterMissedOperationController().getName();
		if(name==null) {
			validations.put(Validation.SIGNER_NAME_MISSING, null);
		}
		String surname = signatureLetterMissedOperation.getSignatureLetterMissedOperationController().getSurname();
		if(surname==null) {
			validations.put(Validation.SIGNER_SURNAME_MISSING, null);
		}
		
		List<OilLog> logToInsertCodeLetterIList = new ArrayList<OilLog>();
		
		OilLog missedLogInsert = new OilLog(compileMissedOperationTimestamp);
		missedLogInsert.setType("OIL2");
		Long shipId = oilRecordBookPart2ViewController.getShipId();
		if (shipId != null) missedLogInsert.setShip(shipId);
		missedLogInsert.setGroupUuid(oilRecordBookPart2ViewController.getGroupUUID());
		missedLogInsert.setNote("Missed Operation");
		missedLogInsert.setSignature(encodedMissedSignatureString);
		missedLogInsert.setOrderInGroup("");
		missedLogInsert.setSignerRank(rank);
		missedLogInsert.setSignerName(name);
		missedLogInsert.setSignerSurname(surname);
		
		processOtherLogInsideCodeLetterO(missedLogInsert, logToInsertList, validations);

		Comparator<OilLog> orderInGroupComparator = Comparator.comparing(oilLog -> oilLog.getOrderInGroup());
		logToInsertList.sort(orderInGroupComparator);
		for(OilLog missedLogToInsert : logToInsertList) {
			OilLog log = (OilLog) logToInsertCodeLetterI.clone();
			log.setCodeNumber("");
			log.setContent("ENTRY PERTAINING TO AN EARLIER MISSED OPERATIONAL ENTRY (CODE LETTER " + missedLogToInsert.getCodeLetter() + "." + missedLogToInsert.getCodeNumber() + ")");
			log.setOrderInGroup(missedLogToInsert.getOrderInGroup());
			missedLogToInsert.setOrderInGroup(missedLogToInsert.getOrderInGroup()+"A");
			logToInsertCodeLetterIList.add(log);
		}
		logToInsertList.addAll(logToInsertCodeLetterIList);
	}

	public void processOtherLogInsideCodeLetterO(OilLog logToInsert, List<OilLog> logToInsertList, Map<Validation, Object> validations) {
		if (!logbook2LetterOEntryPertainingToAnEarlierMissedOperationalComboBox.getSelectionModel().getSelectedItem().isEmpty()) {

			if (logbook2LetterOEntryPertainingToAnEarlierMissedOperationalComboBox.getValue().startsWith("A")) {
				logToInsert.setCodeLetter("A");
				oilRecordBookPart2ViewController.getOilRecordBookPart2CodeLetterAController().processA(logToInsert, logToInsertList, validations);
			} else if (logbook2LetterOEntryPertainingToAnEarlierMissedOperationalComboBox.getValue().startsWith("B")) {
				logToInsert.setCodeLetter("B");
				oilRecordBookPart2ViewController.getOilRecordBookPart2CodeLetterBController().processB(logToInsert, logToInsertList, validations);
			} else if (logbook2LetterOEntryPertainingToAnEarlierMissedOperationalComboBox.getValue().startsWith("C")) {
				logToInsert.setCodeLetter("C");
				oilRecordBookPart2ViewController.getOilRecordBookPart2CodeLetterCController().processC(logToInsert, logToInsertList, validations);
			} else if (logbook2LetterOEntryPertainingToAnEarlierMissedOperationalComboBox.getValue().startsWith("D")) {
				logToInsert.setCodeLetter("D");
				oilRecordBookPart2ViewController.getOilRecordBookPart2CodeLetterDController().processD(logToInsert, logToInsertList, validations);
			} else if (logbook2LetterOEntryPertainingToAnEarlierMissedOperationalComboBox.getValue().startsWith("E")) {
				logToInsert.setCodeLetter("E");
				oilRecordBookPart2ViewController.getOilRecordBookPart2CodeLetterEController().processE(logToInsert, logToInsertList, validations);
			} else if (logbook2LetterOEntryPertainingToAnEarlierMissedOperationalComboBox.getValue().startsWith("F")) {
				logToInsert.setCodeLetter("F");
				oilRecordBookPart2ViewController.getOilRecordBookPart2CodeLetterFController().processF(logToInsert, logToInsertList, validations);
			} else if (logbook2LetterOEntryPertainingToAnEarlierMissedOperationalComboBox.getValue().startsWith("G")) {
				logToInsert.setCodeLetter("G");
				oilRecordBookPart2ViewController.getOilRecordBookPart2CodeLetterGController().processG(logToInsert, logToInsertList, validations);
			} else if (logbook2LetterOEntryPertainingToAnEarlierMissedOperationalComboBox.getValue().startsWith("H")) {
				logToInsert.setCodeLetter("H");
				oilRecordBookPart2ViewController.getOilRecordBookPart2CodeLetterHController().processH(logToInsert, logToInsertList, validations);
			} else if (logbook2LetterOEntryPertainingToAnEarlierMissedOperationalComboBox.getValue().startsWith("I")) {
				logToInsert.setCodeLetter("I");
				oilRecordBookPart2ViewController.getOilRecordBookPart2CodeLetterIController().processI(logToInsert, logToInsertList, validations);
			} else if (logbook2LetterOEntryPertainingToAnEarlierMissedOperationalComboBox.getValue().startsWith("J")) {
				logToInsert.setCodeLetter("J");
				oilRecordBookPart2ViewController.getOilRecordBookPart2CodeLetterJController().processJ(logToInsert, logToInsertList, validations);
			} else if (logbook2LetterOEntryPertainingToAnEarlierMissedOperationalComboBox.getValue().startsWith("K")) {
				logToInsert.setCodeLetter("K");
				oilRecordBookPart2ViewController.getOilRecordBookPart2CodeLetterKController().processK(logToInsert, logToInsertList, validations);
			} else if (logbook2LetterOEntryPertainingToAnEarlierMissedOperationalComboBox.getValue().startsWith("L")) {
				logToInsert.setCodeLetter("L");
				oilRecordBookPart2ViewController.getOilRecordBookPart2CodeLetterLController().processL(logToInsert, logToInsertList, validations);
			} else if (logbook2LetterOEntryPertainingToAnEarlierMissedOperationalComboBox.getValue().startsWith("M")) {
				logToInsert.setCodeLetter("M");
				oilRecordBookPart2ViewController.getOilRecordBookPart2CodeLetterMController().processM(logToInsert, logToInsertList, validations);
			} else if (logbook2LetterOEntryPertainingToAnEarlierMissedOperationalComboBox.getValue().startsWith("N")) {
				logToInsert.setCodeLetter("N");
				oilRecordBookPart2ViewController.getOilRecordBookPart2CodeLetterNController().processN(logToInsert, logToInsertList, validations);
			} else if (logbook2LetterOEntryPertainingToAnEarlierMissedOperationalComboBox.getValue().startsWith("O")) {
				logToInsert.setCodeLetter("O");
				if (oilRecordBookPart2CodeLetterOMissedOperationController!=null) {
					oilRecordBookPart2CodeLetterOMissedOperationController.processO(logToInsert, logToInsertList, validations);	
				}
			} else if (logbook2LetterOEntryPertainingToAnEarlierMissedOperationalComboBox.getValue().startsWith("P")) {
				logToInsert.setCodeLetter("P");
				oilRecordBookPart2ViewController.getOilRecordBookPart2CodeLetterPController().processP(logToInsert, logToInsertList, validations);
			} else if (logbook2LetterOEntryPertainingToAnEarlierMissedOperationalComboBox.getValue().startsWith("Q")) {
				logToInsert.setCodeLetter("Q");
				oilRecordBookPart2ViewController.getOilRecordBookPart2CodeLetterQController().processQ(logToInsert, logToInsertList, validations);
			} else if (logbook2LetterOEntryPertainingToAnEarlierMissedOperationalComboBox.getValue().startsWith("R")) {
				logToInsert.setCodeLetter("R");
				oilRecordBookPart2ViewController.getOilRecordBookPart2CodeLetterRController().processR(logToInsert, logToInsertList, validations);
			}

		}
	}

	private void validateFields(Timestamp businessDate, Map<Validation, Object> validations) {
		if (!selectOperationCheckBox.isSelected() || logbook2LetterOComboBox.getValue().isEmpty()) {
			validations.put(Validation.O_EMPTY_OPERATION, null);
		}
		if (!logbook2LetterOComboBox.getValue().equals(option1) && remarksTextField.getText().isEmpty()) {
			validations.put(Validation.O_EMPTY_REMARKS, null);
		}
		if (logbook2LetterOEntryPertainingToAnEarlierMissedOperationalComboBox.getSelectionModel().isEmpty() && logbook2LetterOComboBox.getValue().equals(option1)) {
			validations.put(Validation.O_EMPTY_MISSED_CODE_LETTER, null);
		}
	}

	public void setController(OilRecordBookPart2ViewController oilRecordBookPart2ViewController) {
		this.oilRecordBookPart2ViewController = oilRecordBookPart2ViewController;
	}

	public void clearFilledInfo() {
		selectOperationCheckBox.setSelected(false);
		signatureLetterMissedOperation.getSignatureLetterMissedOperationController().resetSignature(null);

	}

	public void openSignatureMissedCodeLetter(ActionEvent e) {
		//signatureLetterMissedOperation.getSignatureLetterMissedOperationController().resetSignature(null);
		signatureLetterMissedOperation.showSignatureCodeLetterO((Stage) missedCodeLetterCodeLetterContentAnchorPane.getScene().getWindow());
	}

	public void showInformation(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.initStyle(StageStyle.UTILITY);
		alert.setTitle("Information");
		alert.setHeaderText(title);
		alert.setContentText(message);
		JavaFXUtils.undecorate(alert, closeButton, themeManager);
		alert.showAndWait();
		openSignatureMissedCodeLetter(null);
	}
}
