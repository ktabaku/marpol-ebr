package al.sybina.ebr.logbook.signature;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

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
import al.sybina.ebr.commons.utils.JavaFXUtils;
import al.sybina.ebr.logbook.commons.Activator;
import al.sybina.ebr.logbook.domain.reladomo.OilLog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class SignatureController implements Initializable {

	private static final Logger logger = LoggerCreator.createLogger(SignatureController.class);
	
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
	private String name;
	private String surname;
	private String note;
	
	private WritableImage signatureImage;
	private Stage dialogStage;
	
	private List<OilLog> oilLogList;
	
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
				User user = (User) SecurityUtils.getSubject().getPrincipal();
				rank = ((Role)user.getRoles().toArray()[0]).getDescription();
				name = user.getFirstName();
				surname = user.getLastName();
				rankComboBox.setValue(rank);
				nameTextField.setText(name);
				surnameTextField.setText(surname);
				rankComboBox.setDisable(true);
				nameTextField.setDisable(true);
				surnameTextField.setDisable(true);
			}
		});
		eventBroker.subscribe(Activator.MARPOL_DETAILS_RELOADED, new org.osgi.service.event.EventHandler() {
			@Override
			public void handleEvent(Event event) {
				cancel();
			}
		});
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
		this.name = nameTextField.getText();
		this.surname = surnameTextField.getText();
		SnapshotParameters params = new SnapshotParameters();
		params.setFill(Color.TRANSPARENT);
		signatureImage = canvasSignature.snapshot(params, null);
		dialogStage.close();
	}
	public void cancel() {
		if(dialogStage!=null) dialogStage.close();
	}

	public void restoreSignature() {
		rankComboBox.setValue(this.rank);
		nameTextField.setText(this.name);
		surnameTextField.setText(this.surname);
		//canvasSignature.getGraphicsContext2D().drawImage(signatureImage, 0, 0);
	}
	
	public void resetSignature(ActionEvent e) {
		signatureImage = null;
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

	public boolean checkChanged(List<OilLog> oilLogs) {
		if(this.oilLogList == null) {
			this.oilLogList = oilLogs;
			resetSignature(null);
			return false;
		} else {
			Collections.sort(this.oilLogList);
			if(oilLogs!=null) Collections.sort(oilLogs);
			for(OilLog ol : oilLogList) {
				if (ol.getNote()!=null && !ol.getNote().contains("Missed Operation")) {
				ol.setSignerRank(this.rank);
				ol.setSignerName(this.name);
				ol.setSignerSurname(this.surname);
				ol.setNote(note);
			}
			}
			if(!areEqualIgnoringDate(this.oilLogList, oilLogs)) {
				this.oilLogList = oilLogs;
				resetSignature(null);
				return true;
			}
		}
		return false;
	}
	
	

	
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

	public WritableImage getSignatureImage() {
		return signatureImage;
	}

	public String getRank() {
		return rank;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public void setSignatureImage(WritableImage signatureImage) {
		this.signatureImage = signatureImage;
	}

	public void setOilLogList(List<OilLog> oilLogList) {
		this.oilLogList = oilLogList;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getNote() {
		return note;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static <T> boolean areEqualIgnoringDate(List<OilLog> list1, List<OilLog> list2) {

		Comparator<OilLog> comparator = new Comparator<OilLog>() {

	        @Override
	        public int compare(OilLog obj1, OilLog obj2) {
	    		if (obj1 == obj2)
	    			return 0;
	    		if (obj2 == null)
	    			return 1;
	    		if (obj1.getClass() != obj2.getClass())
	    			return 1;
	    		OilLog other = (OilLog) obj2;
	    		if (obj1.getCodeLetter() == null) {
	    			if (other.getCodeLetter() != null)
	    				return 1;
	    		} else if (!obj1.getCodeLetter().equals(other.getCodeLetter()))
	    			return 1;
	    		if (obj1.getCodeNumber() == null) {
	    			if (other.getCodeNumber() != null)
	    				return 1;
	    		} else if (!obj1.getCodeNumber().equals(other.getCodeNumber()))
	    			return 1;
	    		if (obj1.getContent() == null) {
	    			if (other.getContent() != null)
	    				return 1;
	    		} else if (!obj1.getContent().equals(other.getContent()))
	    			return 1;
	    		if (obj1.getId() != other.getId())
	    			return 1;
	    		if (obj1.getSignerRank() == null) {
	    			if (other.getSignerRank() != null)
	    				return 1;
	    		} else if (!obj1.getSignerRank().equals(other.getSignerRank()))
	    			return 1;
	    		if (obj1.getSignerName() == null) {
	    			if (other.getSignerName() != null)
	    				return 1;
	    		} else if (!obj1.getSignerName().equals(other.getSignerName()))
	    			return 1;
	    		if (obj1.getSignerSurname() == null) {
	    			if (other.getSignerSurname() != null)
	    				return 1;
	    		} else if (!obj1.getSignerSurname().equals(other.getSignerSurname()))
	    			return 1;
	    		return 0;
	        }
	    };
	    if(list1==null) {
	        if(list2==null) {
	            //Here 2 null lists are equivelent. You may want to change this.
	            return true;
	        } else {
	            return false;
	        }
	    }
	    if(list2==null) {
	        return false;
	    }
	    // if not the same size, lists are not equal
	    if (list1.size() != list2.size()) {
	        return false;
	    }

	    // create sorted copies to avoid modifying the original lists
	    List<OilLog> copy1 = new ArrayList<OilLog>(list1);
	    List<OilLog> copy2 = new ArrayList<OilLog>(list2);

	    Collections.sort(copy1);
	    Collections.sort(copy2);

	    // iterate through the elements and compare them one by one using
	    // the provided comparator.
	    Iterator<OilLog> it1 = copy1.iterator();
	    Iterator<OilLog> it2 = copy2.iterator();
	    while (it1.hasNext()) {
	    	OilLog t1 = it1.next();
	    	OilLog t2 = it2.next();
	        if (comparator.compare(t1, t2) != 0) {
	            // as soon as a difference is found, stop looping
	            return false;
	        }
	    }
	    return true;
	}
}
