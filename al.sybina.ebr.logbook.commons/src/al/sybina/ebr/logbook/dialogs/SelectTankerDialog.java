package al.sybina.ebr.logbook.dialogs;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.tools.resources.WorkbenchResourceProvider;
import org.eclipse.e4.tools.services.IResourceProviderService;
import org.eclipse.fx.core.di.LocalInstance;
import org.eclipse.fx.ui.services.resources.ImageProvider;
import org.eclipse.fx.ui.services.theme.ThemeManager;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXChipView;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;

import al.sybina.ebr.commons.utils.JavaFXUtils;
import al.sybina.ebr.commons.utils.ResizeHelper;
import javafx.application.Platform;
import javafx.beans.binding.Binding;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

@Creatable
@Singleton
public class SelectTankerDialog {

	private IResourceProviderService resourcesService;
	private ImageProvider imageProvider;
	private AnchorPane dialogPage;
	private SelectTankerDialogController selectTankerDialogController;
	private ThemeManager themeManager;
	private static JFXButton closeButton;
	
	@Inject
	public SelectTankerDialog(ThemeManager themeManager, @LocalInstance FXMLLoader loader, IResourceProviderService resourcesService, ImageProvider imageProvider) throws IOException {
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
		
		this.resourcesService = resourcesService;
		this.imageProvider = imageProvider;
		this.themeManager = themeManager;
		
		loader.setLocation(getClass().getResource("SelectTankerDialog.fxml"));
		
		dialogPage = (AnchorPane) loader.load();
//		Image backgroundImage = imageProvider.getImage(((WorkbenchResourceProvider)resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_RCP_TEXTURE));
//		dialogPage.setBackground(new Background(new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(100,100,true, true, true, true))));
		dialogPage.setId("paneId");
		
		selectTankerDialogController = (SelectTankerDialogController)loader.getController();
		
//		//Needed only to initiate stage for the triangle control to function properly
//		Stage dStage = new Stage();
//		selectTankerDialogController.setDialogStage(dStage);
//		dStage.sizeToScene();
//		dStage.setScene(new Scene(dialogPage));
//		dStage.show();
//		dStage.close();
		
	}


	public void showDialog(Stage stage, JFXChipView jFXChipView) {
		Stage dialogStage = new Stage(StageStyle.UNDECORATED);
		dialogStage.setTitle("Select Tanker");
		dialogStage.initModality(Modality.WINDOW_MODAL);
//		dialogStage.setResizable(false);
//		dialogStage.getIcons().add(catalogDownload24);
		dialogStage.initOwner(stage);
		selectTankerDialogController.setJFXChipView(jFXChipView);
		selectTankerDialogController.setDialogStage(dialogStage);
		selectTankerDialogController.initTankButtons();
		dialogStage.sizeToScene();
		if(dialogPage.getScene()==null) {
			Scene scene = new Scene(dialogPage);
			themeManager.registerScene(scene);
			dialogStage.setScene(scene);
			JavaFXUtils.undecorate(dialogStage, closeButton, themeManager);
			ResizeHelper.addResizeListener(dialogStage);
		} else {
			Stage oldStage = (Stage)dialogPage.getScene().getWindow();
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					oldStage.close();
				}
			});
			dialogStage.setScene(dialogPage.getScene());
			ResizeHelper.addResizeListener(dialogStage);
		}
		dialogStage.showAndWait();
		
		// Show the dialog and wait until the user closes it
	}
	

	public SelectTankerDialogController getSelectTankerDialogController() {
		return selectTankerDialogController;
	}

}


