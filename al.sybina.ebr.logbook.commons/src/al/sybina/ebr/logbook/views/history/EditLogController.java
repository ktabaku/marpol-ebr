package al.sybina.ebr.logbook.views.history;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.inject.Inject;

import org.apache.shiro.SecurityUtils;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.fx.core.di.Service;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;
import org.eclipse.fx.ui.services.theme.ThemeManager;
import org.osgi.service.event.Event;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import com.oceanpro.core.security.entities.User;
import com.oceanpro.core.services.OperationLogReladomoService;

import al.sybina.ebr.commons.utils.JavaFXUtils;
import al.sybina.ebr.jasperreports.JasperReportsService;
import al.sybina.ebr.logbook.commons.Activator;
import al.sybina.ebr.logbook.domain.reladomo.OilLog;
import al.sybina.ebr.logbook.signature.Signature;
import al.sybina.ebr.logbook.views.marpoldetail.MarpolBean;
import al.sybina.ebr.login.preferences.LoginPreferencePage;
import al.sybina.ebr.login.services.RemoteEventAdminService;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jfxtras.scene.control.LocalDateTimeTextField;

public class EditLogController implements Initializable {
	
	private static final Logger logger = LoggerCreator.createLogger(EditLogController.class);
	
	@Inject
	private IEventBroker eventBroker;
	@Inject
	private static ThemeManager themeManager;
	@Inject
	@Service
	private JasperReportsService jasperReportsService;
	@Inject
	private MarpolBean marpolBean;
	@Inject
	@Service
	private RemoteEventAdminService remoteEventAdminService;
	@Inject
	private LoginPreferencePage loginPreferencePage;
	@Inject
	@Service
	private OperationLogReladomoService operationLogReladomoService;
	@Inject
	private Signature signature;

	
	
	@FXML
	private AnchorPane mainAnchorPane;
	@FXML
	private TextField typeTextField;
	@FXML
	private LocalDateTimeTextField dateTextField;
	@FXML
	private TextField codeLetterTextField;
	@FXML
	private TextField codeNumberTextField;
	@FXML
	private TextArea contentTextArea;
	@FXML
	private TextField signerRankTextField;
	@FXML
	private TextField signerNameTextField;
	@FXML
	private TextField signerSurnameTextField;
	@FXML
	private ImageView signatureImageView;
	@FXML
	private TextField correctionNoteTextField;
		
	@FXML
	private Button saveButton;
	@FXML
	private Button cancelButton;
	
	private static JFXButton closeButton;
	
	private Stage stage;

	private OilLog oilLogToEdit;

	private LocalDateTime businessDate;


	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			SVGGlyph timesGlyph = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg.times-circle");
			closeButton = new JFXButton();
			closeButton.getStyleClass().add("closeDialogButton");
			closeButton.setMinWidth(40); closeButton.setPrefWidth(40); closeButton.setMaxWidth(40);
			closeButton.setMinHeight(32); closeButton.setPrefHeight(32); closeButton.setMaxHeight(32);
			closeButton.setGraphic(timesGlyph);
			closeButton.ripplerFillProperty().setValue(Color.LIGHTSALMON);
			closeButton.setOnAction(event -> {
				((Stage)((Button)event.getSource()).getScene().getWindow()).close();
	        });
	        Tooltip.install(closeButton, new Tooltip("Exit"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		typeTextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		codeLetterTextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		codeNumberTextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		contentTextArea.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		correctionNoteTextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		
		JavaFXUtils.addFilledPseudoClass(typeTextField);
		JavaFXUtils.addFilledPseudoClass(codeLetterTextField);
		JavaFXUtils.addFilledPseudoClass(codeNumberTextField);
		JavaFXUtils.addFilledPseudoClass(signerNameTextField);
		JavaFXUtils.addFilledPseudoClass(signerRankTextField);
		JavaFXUtils.addFilledPseudoClass(signerSurnameTextField);
		JavaFXUtils.addFilledPseudoClass(correctionNoteTextField);
		JavaFXUtils.addFilledPseudoClass(contentTextArea);
		JavaFXUtils.addFilledPseudoClass(dateTextField);
		
		eventBroker.subscribe(Activator.LOGS_CHANGED_EVENT_TOPIC, new org.osgi.service.event.EventHandler() {
			@Override
			public void handleEvent(Event event) {
				ArrayList<Long> ids = (ArrayList<Long>) event.getProperty(Activator.LOG_IDS);
    			if(ids==null || (oilLogToEdit!=null && ids.contains(oilLogToEdit.getId()))) {
    				if(signature.getSignatureController()!=null) signature.getSignatureController().cancel();
    				cancel();
    			}
			}
		});
	}
	

	
	@FXML
	public void saveLog() {
		if(oilLogToEdit.getCorrectionNote()==null) oilLogToEdit.setCorrectionNote("");

		oilLogToEdit.setMasterSigDate(null);
		oilLogToEdit.setMasterSignature(null);
		oilLogToEdit.setMasterSignerName(null);
		oilLogToEdit.setMasterSignerSurname(null);

		List<OilLog> sameGroupOilLogList = operationLogReladomoService.getOil1LogListByGroupUUID(oilLogToEdit.getGroupUuid());
		sameGroupOilLogList.removeIf(oilLog -> oilLog.getId() == oilLogToEdit.getId());
		for(OilLog sameGroupOilLog : sameGroupOilLogList) {
			sameGroupOilLog.setMasterSigDate(null);
			sameGroupOilLog.setMasterSignature(null);
			sameGroupOilLog.setMasterSignerName(null);
			sameGroupOilLog.setMasterSignerSurname(null);
		}
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		operationLogReladomoService.editSignOil1Log(businessDate, oilLogToEdit, sameGroupOilLogList);
		logger.info("Start modified log:");
		logger.info("User: " + user.getRoles().stream().map(Object::toString).collect(Collectors.joining(", ")) + " " + user.getFirstName() + " " + user.getLastName());
		logger.info(oilLogToEdit.toString());
		logger.info("End modified log.");
		
		List<Long> updatedLogIds = new ArrayList<Long>();
		updatedLogIds.add(oilLogToEdit.getId());
		Map<String, List<Long>> idsParameter = new HashMap<String, List<Long>>();
		idsParameter.put(Activator.LOG_IDS, updatedLogIds);
		remoteEventAdminService.getDistributedEventAdmin(loginPreferencePage.getMainInstallationIp().getValue()).postEvent(new Event(al.sybina.ebr.logbook.commons.Activator.LOGS_CHANGED_EVENT_TOPIC, idsParameter));
//		eventBroker.post(Activator.LOGS_CHANGED_EVENT_TOPIC, new HashMap<String, String>());
		showInformation("Log Saved", "Log saved successfully.");
		stage.close();
	}
	
	@FXML
	public void cancel() {
		
		stage.close();
	}
	
	public static void showInformation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Information");
        alert.setHeaderText(title);
        alert.setContentText(message);
        JavaFXUtils.undecorate(alert, closeButton, themeManager);
        alert.showAndWait();
    }
	
	

	public void setStage(Stage stage) {
		this.stage = stage;
	}



	public void setLogToEdit(OilLog oilLogToEdittt) {
		this.oilLogToEdit = oilLogToEdittt.getOriginalPersistentObject().getDetachedCopy();
		typeTextField.setText(oilLogToEdit.getType());
		dateTextField.setLocalDateTime(oilLogToEdit.getBusinessDate().toLocalDateTime());
		businessDate = dateTextField.getLocalDateTime();
		codeLetterTextField.setText(oilLogToEdit.getCodeLetter());
		codeNumberTextField.setText(oilLogToEdit.getCodeNumber());
		contentTextArea.setText(oilLogToEdit.getContent());
		signerRankTextField.setText(oilLogToEdit.getSignerRank());
		signerNameTextField.setText(oilLogToEdit.getSignerName());
		signerSurnameTextField.setText(oilLogToEdit.getSignerSurname());
		correctionNoteTextField.setText(oilLogToEdit.getCorrectionNote());
		byte[] signatureBytes = Base64.getDecoder().decode(oilLogToEdit.getSignature());
		BufferedImage bImage;
		try {
			bImage = ImageIO.read(new ByteArrayInputStream(signatureBytes));
			WritableImage image = SwingFXUtils.toFXImage(bImage, null);
			signatureImageView.setImage(image);
			signatureImageView.setFitWidth(50);
			signatureImageView.setFitHeight(30);
		} catch (IOException e) {
			e.printStackTrace();
		}
//		typeTextField.setDisable(false);
		dateTextField.setDisable(false);
		codeLetterTextField.setDisable(false);
		codeNumberTextField.setDisable(false);
		contentTextArea.setDisable(false);
		correctionNoteTextField.setDisable(false);
		saveButton.setDisable(true);
	}

	
	public void openSignature(ActionEvent e) {
//		oilLogToEdit.setType(typeTextField.getText());
		oilLogToEdit.setCodeLetter(codeLetterTextField.getText());
		oilLogToEdit.setCodeNumber(codeNumberTextField.getText());
		oilLogToEdit.setContent(contentTextArea.getText());
		oilLogToEdit.setCorrectionNote(correctionNoteTextField.getText());
		businessDate = dateTextField.getLocalDateTime();
		List<OilLog> list = new ArrayList<OilLog>();
		list.add(oilLogToEdit);
		signature.getSignatureController().resetSignature(null);
		signature.showSignature((Stage)((Button)e.getSource()).getScene().getWindow(), list);
		
		if(signature.getSignatureController().getName()!=null) {
			oilLogToEdit.setSignerRank(signature.getSignatureController().getRank());
			oilLogToEdit.setSignerName(signature.getSignatureController().getName());
			oilLogToEdit.setSignerSurname(signature.getSignatureController().getSurname());
			WritableImage signatureImage = signature.getSignatureController().getSignatureImage();
			String encodedSignatureString = null;
			if(signatureImage==null) {
				return;
			} else {
				try {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(SwingFXUtils.fromFXImage(signatureImage, null), "png", baos);
					byte[] bytes = baos.toByteArray();
					encodedSignatureString = Base64.getEncoder().encodeToString(bytes);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			oilLogToEdit.setSignature(encodedSignatureString);
			
//			typeTextField.setDisable(true);
			dateTextField.setDisable(true);
			codeLetterTextField.setDisable(true);
			codeNumberTextField.setDisable(true);
			contentTextArea.setDisable(true);
			correctionNoteTextField.setDisable(true);
			saveButton.setDisable(false);
			
			signerRankTextField.setText(oilLogToEdit.getSignerRank());
			signerNameTextField.setText(oilLogToEdit.getSignerName());
			signerSurnameTextField.setText(oilLogToEdit.getSignerSurname());
			correctionNoteTextField.setText(oilLogToEdit.getCorrectionNote());
			signatureImageView.setImage(signatureImage);
		}
	}
}
