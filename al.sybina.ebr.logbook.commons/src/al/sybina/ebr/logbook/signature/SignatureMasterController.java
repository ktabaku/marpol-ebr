package al.sybina.ebr.logbook.signature;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.inject.Inject;

import org.apache.shiro.SecurityUtils;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.tools.services.IResourceProviderService;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;
import org.eclipse.fx.ui.services.resources.ImageProvider;
import org.eclipse.fx.ui.services.theme.ThemeManager;
import org.osgi.service.event.Event;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import com.jfoenix.validation.RequiredFieldValidator;
import com.oceanpro.core.security.entities.Role;
import com.oceanpro.core.security.entities.User;

import de.jensd.fx.glyphs.GlyphIcon;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import al.sybina.ebr.commons.utils.ImageCell;
import al.sybina.ebr.commons.utils.JavaFXUtils;
import al.sybina.ebr.logbook.domain.reladomo.OilLog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SignatureMasterController implements Initializable {

	private static final Logger logger = LoggerCreator.createLogger(SignatureMasterController.class);
	
	@Inject
	private IResourceProviderService resourcesService;
	@Inject
	private ImageProvider imageProvider;
	@Inject
	private IEventBroker eventBroker;
	@Inject
	private static ThemeManager themeManager;
	@FXML
	private AnchorPane anchorPaneSignature;
	@FXML
	private Canvas canvasSignature;
	@FXML
	private Button buttonReset;
	@FXML
	private Button buttonAccept;
	@FXML
	private HBox hBoxSignature;
	@FXML
	private Label insertSignature;
	@FXML
	private JFXComboBox<String> rankComboBox;
	@FXML
	private JFXTextField nameTextField;
	@FXML
	private JFXTextField surnameTextField;
	@FXML
	private HBox canvasValidationHBox;
	
	private static JFXButton closeButton;
	
	private String rank;
	private String masterName;
	private String masterSurname;
	private String note;
	

	private Stage dialogStage;
	
	private Map<String, List<OilLog>> logGroupsMap;
	private Label nameLabel;
	private Label surnameLabel;
	private ImageCell signatureImageView;
	private Label sigDateLabel;
	
	private boolean canvasDirty = false;
	
	
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
		
		GraphicsContext gc;
		gc = canvasSignature.getGraphicsContext2D();

		ColorPicker cpLine = new ColorPicker(Color.BLACK);

		canvasSignature.setOnMousePressed(e -> {
			gc.setStroke(cpLine.getValue());
			gc.setLineWidth(6.0);
			gc.beginPath();
			gc.lineTo(e.getX(), e.getY());
		});

		canvasSignature.setOnMouseDragged(e -> {
			gc.lineTo(e.getX(), e.getY());
			gc.stroke();
			canvasDirty=true;
			canvasValidationHBox.getChildren().clear();
		});

		canvasSignature.setOnMouseReleased(e -> {
			gc.lineTo(e.getX(), e.getY());
			gc.stroke();
			gc.closePath();
		});

		
		
        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Input Required");
        validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
            .glyph(FontAwesomeIcon.WARNING)
            .size("1em")
            .styleClass("error")
            .build());
        
        nameTextField.getValidators().add(validator);
        nameTextField.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
            	nameTextField.validate();
            }
        });
        
        surnameTextField.getValidators().add(validator);
        surnameTextField.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
            	surnameTextField.validate();
            }
        });

        rankComboBox.setItems(getRankItems());
        rankComboBox.getValidators().add(validator);
        rankComboBox.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
            	rankComboBox.validate();
            }
        });
        
		anchorPaneSignature.setVisible(true);
		
		eventBroker.subscribe(al.sybina.ebr.login.Activator.LOGGED_IN_EVENT_TOPIC, new org.osgi.service.event.EventHandler() {
			@Override
			public void handleEvent(Event event) {
				initUser();
			}
		});
		initUser();
	}

	private void initUser() {
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		rank = ((Role)user.getRoles().toArray()[0]).getDescription();
		masterName = user.getFirstName();
		masterSurname = user.getLastName();
		rankComboBox.setValue(rank);
		nameTextField.setText(user.getFirstName());
		surnameTextField.setText(user.getLastName());
		rankComboBox.setDisable(true);
		nameTextField.setDisable(true);
		surnameTextField.setDisable(true);
	}
	
	private boolean validateCanvas() {
		if (!canvasDirty) {
			GlyphIcon icon = GlyphsBuilder.create(FontAwesomeIconView.class).glyph(FontAwesomeIcon.WARNING).size("1em")
					.styleClass("error").build();
			icon.setFill(Color.valueOf("#D34336"));
			Label inputRequiredLabel = new Label("Input Required");
			inputRequiredLabel.setTextFill(Color.valueOf("#D34336"));
			Region middleSpace = new Region();
			HBox.setHgrow(middleSpace, Priority.ALWAYS);
			canvasValidationHBox.getChildren().clear();
			canvasValidationHBox.getChildren().addAll(inputRequiredLabel, middleSpace, icon);
			return false;
		} else {
			canvasValidationHBox.getChildren().clear();
			return true;
		}
	}
	
	public void acceptSignature(ActionEvent e) {
		boolean signatureCanvasValid = validateCanvas();
		if(!nameTextField.validate() || !surnameTextField.validate() || !rankComboBox.validate() || !signatureCanvasValid) return;
		this.rank = rankComboBox.getValue();

		this.masterName = nameTextField.getText();
		this.masterSurname = surnameTextField.getText();
		
		Date nowDate = new Date();
		sigDateLabel.setText(new SimpleDateFormat("dd-MM-yyyy").format(nowDate));
		SnapshotParameters params = new SnapshotParameters();
		params.setFill(Color.TRANSPARENT);
		WritableImage signatureImage = canvasSignature.snapshot(params, null);
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
		String signatureGroupUuid = UUID.randomUUID().toString();
		for(List<OilLog> oilLogList : logGroupsMap.values()) {
			for(OilLog oilLog : oilLogList) {
				oilLog.setMasterSignerName(masterName);
				oilLog.setMasterSignerSurname(masterSurname);
				oilLog.setMasterSignature(encodedSignatureString);
				oilLog.setMasterSigDate(nowDate);
				oilLog.setSignatureGroupUuid(signatureGroupUuid);
			}
		}
		
		nameLabel.setText(nameTextField.getText());
		surnameLabel.setText(surnameTextField.getText());
		signatureImageView.setImage(signatureImage);
		signatureImageView.layout();
		
		dialogStage.close();
	}
	public void restoreSignature() {
		rankComboBox.setValue(this.rank);

		nameTextField.setText(this.masterName);
		surnameTextField.setText(this.masterSurname);
		
		//canvasSignature.getGraphicsContext2D().drawImage(signatureImage, 0, 0);
	}
	
	public void resetSignature(ActionEvent e) {
		canvasSignature.getGraphicsContext2D().clearRect(0, 0, canvasSignature.getWidth(), canvasSignature.getHeight());
		canvasDirty=false;
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

	public void openInfoMessage(ActionEvent e) {
		anchorPaneSignature.setVisible(true);
	}



//	public ObservableList<String> getRankItems() {
//		return FXCollections.observableArrayList(
//				"MASTER / SSO",
//				"CH/ENG"
//		);
//	}
	public ObservableList<String> getRankItems() {
		return FXCollections.observableArrayList(
				"admin",
				"MASTER / SSO",
				"CH/OFF",
				"2/OFF",
				"3/OFF",
				"DK/CADET",
				"CH/ENG",
				"1/ENG",
				"2/ENG",
				"3/ENG",
				"ELEC. OFF",
				"PUMPMAN",
				"BOSUN",
				"A.B - 1",
				"A.B - 2",
				"A.B - 3",
				"O.S - 1",
				"FITTER",
				"OILER 1",
				"CH.COOK",
				"M/MAN",
				"OILER 2",
				"Other",
				"MASTER",
				"SSO",
				"CH/OFF SSO",
				"O.S - 2",
				"O.S - 3"
		);
	}
	public void setDialogStage(Stage dialogStage2) {
		this.dialogStage = dialogStage2;
	}


	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getNote() {
		return note;
	}

	
	


	public String getMasterName() {
		return masterName;
	}

	public void setMasterName(String masterName) {
		this.masterName = masterName;
	}

	public String getMasterSurname() {
		return masterSurname;
	}

	public void setMasterSurname(String masterSurname) {
		this.masterSurname = masterSurname;
	}

	public void setOilLogListMap(Map<String, List<OilLog>> logGroupsMap) {
		this.logGroupsMap = logGroupsMap;
	}

	public void setNameLabel(Label nameLabel) {
		this.nameLabel = nameLabel;
	}

	public void setSurnameLabel(Label surnameLabel) {
		this.surnameLabel = surnameLabel;
	}

	public void setSignatureImageView(ImageCell signatureImageView) {
		this.signatureImageView = signatureImageView;
	}

	public void setSigDateLabel(Label sigDateLabel) {
		this.sigDateLabel = sigDateLabel;
	}

	public Label getSigDateLabel() {
		return sigDateLabel;
	}
	
}
