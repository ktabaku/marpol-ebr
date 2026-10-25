package al.sybina.ebr.logbook.dialogs.databackup;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.tools.services.IResourceProviderService;
import org.eclipse.fx.core.di.LocalInstance;
import org.eclipse.fx.ui.services.resources.ImageProvider;
import org.eclipse.fx.ui.services.theme.ThemeManager;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;

import al.sybina.ebr.commons.utils.JavaFXUtils;
import al.sybina.ebr.commons.utils.ResizeHelper;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

@Creatable
@Singleton
public class DataBackupDialog {
	
	private IResourceProviderService resourcesService;
	private ImageProvider imageProvider;
	private AnchorPane dialogPage;
	private DataBackupDialogController dataBackupDialogController;
	private ThemeManager themeManager;
	private static JFXButton closeButton;
	
	@Inject
	public DataBackupDialog(ThemeManager themeManager, @LocalInstance FXMLLoader loader, IResourceProviderService resourcesService, ImageProvider imageProvider) throws IOException {
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
		
		loader.setLocation(getClass().getResource("DataBackupDialog.fxml"));
		
		dialogPage = (AnchorPane) loader.load();
		dialogPage.setId("paneId");
		
		dataBackupDialogController = (DataBackupDialogController)loader.getController();
	}


	public void showDialog(Stage stage) {
		Stage dialogStage = new Stage(StageStyle.UNDECORATED);
		dialogStage.setTitle("Data Backup/Restore");
		dialogStage.initModality(Modality.WINDOW_MODAL);
//		dialogStage.setResizable(false);
		dialogStage.initOwner(stage);
		dataBackupDialogController.setDialogStage(dialogStage);
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
	

	public DataBackupDialogController getDataBackupDialogController() {
		return dataBackupDialogController;
	}

}