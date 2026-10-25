package al.sybina.ebr.logbook.signature;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.fx.core.di.LocalInstance;
import org.eclipse.fx.ui.services.theme.ThemeManager;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;

import al.sybina.ebr.commons.utils.JavaFXUtils;
import al.sybina.ebr.commons.utils.ResizeHelper;
import al.sybina.ebr.logbook.domain.reladomo.OilLog;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

@Creatable
@Singleton
public class Signature {


	private Scene dialogScene;
	private AnchorPane dialogAnchorPane;
	private SignatureController signatureController;
	private ThemeManager themeManager;
	private JFXButton closeButton;
	
	@Inject
	public Signature(ThemeManager themeManager, @LocalInstance FXMLLoader loader) throws IOException {
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
		this.themeManager = themeManager;
		loader.setLocation(getClass().getResource("Signature.fxml"));
		
		dialogAnchorPane = (AnchorPane) loader.load();
		dialogAnchorPane.setId("paneId");
		dialogScene = new Scene(dialogAnchorPane);
		themeManager.registerScene(dialogScene);
		signatureController = (SignatureController)loader.getController();
	}


	public void showSignature(Stage stage, List<OilLog> oilLogs) {
		Stage dialogStage = new Stage();
		dialogStage.setTitle("Insert Signature");
		dialogStage.initModality(Modality.NONE);
		dialogStage.setResizable(false);
		dialogStage.initOwner(stage);
		dialogStage.setScene(dialogScene);
		signatureController.setDialogStage(dialogStage);
		 //Show the dialog and wait until the user closes it
		JavaFXUtils.undecorate(dialogStage, closeButton, themeManager);
		ResizeHelper.addResizeListener(dialogStage);
		dialogStage.showAndWait();
	}
	

	public SignatureController getSignatureController() {
		return signatureController;
	}


}
