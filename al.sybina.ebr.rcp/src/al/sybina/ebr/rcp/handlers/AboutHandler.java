package al.sybina.ebr.rcp.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.tools.services.IResourceProviderService;
import org.eclipse.fx.core.di.LocalInstance;
import org.eclipse.fx.ui.services.resources.ImageProvider;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AboutHandler {
		
	@Execute
	public void execute(Stage stage, @LocalInstance FXMLLoader loader, IResourceProviderService resourcesService, ImageProvider imageProvider) {

		AnchorPane page = null;
		AboutDialogController aboutDialogController = null;
		try {
			loader.setLocation(getClass().getResource("AboutDialog.fxml"));
			page = (AnchorPane) loader.load();
			aboutDialogController = (AboutDialogController)loader.getController();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		
		if (page == null || aboutDialogController == null) {
			return;
		}
		Scene scene = new Scene(page);
//		scene.getStylesheets().addAll(stage.getScene().getStylesheets());
		Stage aboutStage = new Stage();
		aboutStage.setScene(scene);
		aboutStage.initOwner(stage);
		aboutStage.initModality(Modality.APPLICATION_MODAL);
		aboutDialogController.setDialogStage(aboutStage);
		aboutStage.setTitle("About MARPOL ERB");
		aboutStage.showAndWait();	
	}
}
