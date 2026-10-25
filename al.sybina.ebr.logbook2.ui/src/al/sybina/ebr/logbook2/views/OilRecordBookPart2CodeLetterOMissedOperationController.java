package al.sybina.ebr.logbook2.views;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.eclipse.e4.tools.resources.WorkbenchResourceProvider;
import org.eclipse.e4.tools.services.IResourceProviderService;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;
import org.eclipse.fx.ui.services.resources.ImageProvider;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;

import al.sybina.ebr.commons.utils.JavaFXUtils;
import al.sybina.ebr.logbook.commons.BallonMessage;
import al.sybina.ebr.logbook.domain.reladomo.OilLog;
import al.sybina.ebr.logbook2.views.OilRecordBookPart2ViewController.Validation;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import jfxtras.scene.control.ImageViewButton;

public class OilRecordBookPart2CodeLetterOMissedOperationController implements Initializable {

	private Label label1 = new Label(" Notes:\r\n" + "This entry should be made only in exceptional cases\r\n" + "Date ‘1’ to be the date when the original operation was carried out.\r\n"
			+ "Date '2’ to be the current date i.e. the date when the entry is made.\r\n" + "Signed ‘1’:Signature of Officer making the “O” entry\r\n" + "Signed ‘2’:Signature of Officer making the missed entry\r\n" + "");

	private Label label2 = new Label("Note \r\n" + "Date ‘1’ to be the date when the original operation \r\n" + "was carried out\r\n" + "");

	private static final String option2 = "Pre-discharge pressure";
	private static final String option3 = "Leakage test of the cow piping system";
	private static final String option4 = "Other";
	private static final Logger logger = LoggerCreator.createLogger(OilRecordBookPart2CodeLetterOMissedOperationController.class);
	private BallonMessage ballonMessage;
	private IResourceProviderService resourcesService;
	private ImageProvider imageProvider;
	@FXML
	private ImageViewButton helpLetterOImageViewButton;
	@FXML
	private ImageView imageViewHelpO1;
	@FXML
	private ImageView imageViewHelpO2;
	@FXML
	private ImageView imageViewHelpO3;
	@FXML
	private ImageView imageViewHelpO4;
	@FXML
	private ImageView imageViewHelpO5;

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
	private static JFXButton closeButton;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
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
		Platform.runLater(() -> {
			try {
				Image infoIconImageInfoLetterA = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_INFO));
				helpLetterOImageViewButton.setImage(infoIconImageInfoLetterA);} catch (IOException e) {
				e.printStackTrace();
			}
		});
		remarksTextField.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		logbook2LetterOComboBox.getItems().addAll(option2, option3, option4);
		logbook2LetterOComboBox.setValue("");
		logbook2LetterOComboBox.setDisable(true);
		mainVBox.getChildren().remove(case1VBox);
		mainVBox.getChildren().remove(case234VBox);
		selectOperationCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				logbook2LetterOComboBox.setDisable(false);
			} else {
				logbook2LetterOComboBox.setDisable(true);
				logbook2LetterOComboBox.setValue("");
			}

		});

		logbook2LetterOComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
			 if (newValue.equals(option2)) {
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
			OilLog log = (OilLog) logToInsert.clone();
			log.setOrderInGroup("ZR");
			log.setCodeNumber("");
			log.setContent(remarksTextField.getText());
			logToInsertList.add(log);
		}
	}

	private void validateFields(Timestamp businessDate, Map<Validation, Object> validations) {
		if (!selectOperationCheckBox.isSelected() || logbook2LetterOComboBox.getValue().isEmpty()) {
			validations.put(Validation.O_EMPTY_OPERATION, null);
		}
		if (!logbook2LetterOComboBox.getValue().isEmpty() && remarksTextField.getText().isEmpty()) {
			validations.put(Validation.O_EMPTY_REMARKS, null);
		}
	}
	public BallonMessage getBallonMessage() {
		return ballonMessage;
	}

	public void setBallonMessage(BallonMessage ballonMessage) {
		this.ballonMessage = ballonMessage;
	}

	public IResourceProviderService getResourcesService() {
		return resourcesService;
	}

	public void setResourcesService(IResourceProviderService resourcesService) {
		this.resourcesService = resourcesService;
	}

	public ImageProvider getImageProvider() {
		return imageProvider;
	}

	public void setImageProvider(ImageProvider imageProvider) {
		this.imageProvider = imageProvider;
	}
}
