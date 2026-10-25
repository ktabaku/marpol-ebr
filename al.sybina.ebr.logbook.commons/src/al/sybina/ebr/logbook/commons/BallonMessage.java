package al.sybina.ebr.logbook.commons;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.fx.ui.services.theme.ThemeManager;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;

import al.sybina.ebr.commons.utils.JavaFXUtils;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jfxtras.scene.control.ImageViewButton;
import jidefx.scene.control.popup.BalloonPopupOutline;
import jidefx.scene.control.popup.ShapedPopup;

@Creatable
@Singleton
public class BallonMessage {
	
	@Inject
	private static ThemeManager themeManager;
	
	private ShapedPopup _shapedPopup;
	private static JFXButton closeButton;
	
	public void showGeneralBalloon(Node imageViewButton, Label label) {
		if (_shapedPopup != null && _shapedPopup.isShowing()) {
			_shapedPopup.hide();
			_shapedPopup = null;
			return;
		} else if(_shapedPopup != null) {
			_shapedPopup = null;
		}
		
		_shapedPopup = new ShapedPopup();
		_shapedPopup.setAutoHide(true);
		_shapedPopup.setCloseButtonVisible(false);
		_shapedPopup.setInsets(new javafx.geometry.Insets(20));
		_shapedPopup.setPopupContent(new javafx.scene.Group(label));
		
		BalloonPopupOutline outline = new BalloonPopupOutline();
		outline.arrowSideProperty().setValue(Side.LEFT);
		outline.arrowPositionProperty().setValue(0.9);
		outline.arrowBasePositionProperty().setValue(0.5);
		outline.arrowHeightProperty().setValue(30);
		outline.arrowWidthProperty().setValue(30);
//		outline.roundedRadiusProperty().setValue(30);;

		_shapedPopup.setPopupOutline(outline);
		if (_shapedPopup.isShowing()) {
			_shapedPopup.adjustPopup(imageViewButton, Pos.TOP_RIGHT, 0, 0);
		} else {
			_shapedPopup.showPopup(imageViewButton, Pos.TOP_RIGHT, 0, 0);
		}
	}

	public void closeBalloon() {
		if (_shapedPopup != null && _shapedPopup.isShowing()) {
			_shapedPopup.hide();
			_shapedPopup = null;
			return;
		} else if(_shapedPopup != null) {
			_shapedPopup = null;
		}
	}
	public static void showInformation(String title, String message) {
		try {
			SVGGlyph timesGlyph = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg.times-circle");
			closeButton = new JFXButton();
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
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.initStyle(StageStyle.UTILITY);
		alert.setTitle("Information");
		alert.setHeaderText(title);
		alert.setContentText(message);
		JavaFXUtils.undecorate(alert, closeButton, themeManager);
		alert.showAndWait();
	}
}
