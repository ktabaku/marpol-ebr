package al.sybina.ebr.logbook.dialogs.help;

import java.net.URL;
import java.util.ResourceBundle;

import javax.inject.Inject;

import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;
import org.eclipse.fx.ui.services.theme.ThemeManager;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class HelpCodeLetterDialogController implements Initializable {

	private static final Logger logger = LoggerCreator.createLogger(HelpCodeLetterDialogController.class);
	@Inject
	private ThemeManager themeManager;
	@FXML
	private AnchorPane anchorPane;
	@FXML
	private VBox imageHolderVBox;
	private JFXButton closeButton;
	private Stage dialogStage;

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

		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		anchorPane.setMaxHeight(screenBounds.getHeight() - 80);
		anchorPane.setMaxWidth(screenBounds.getWidth() - 40);

	}

	@FXML
	private void handleClose() {
		dialogStage.close();
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public VBox getImageHolderVBox() {
		return imageHolderVBox;
	}

	public void setImageHolderVBox(VBox imageHolderVBox) {
		this.imageHolderVBox = imageHolderVBox;
	}

	public void insertImage(ObservableList<Image> helpImageList) {
		imageHolderVBox.getChildren().clear();
		for (Image helpImage : helpImageList) {
		    ImageView img = new ImageView();
            img.setImage(helpImage);
		    img.fitWidthProperty().bind(imageHolderVBox.widthProperty());
		    imageHolderVBox.getChildren().add(img);
		}
		
	}

}
