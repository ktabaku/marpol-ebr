package al.sybina.ebr.logbook.views.audit;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.fx.core.di.LocalInstance;

import al.sybina.ebr.log4j.configuration.Dialog;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AuditView {

	
	@PostConstruct
	public void createPartControl(BorderPane borderPane, @LocalInstance FXMLLoader loader, Stage stage, MPart thisPart) {
		Dialog.setPrimaryStage(stage);
		loader.setLocation(getClass().getResource("Audit.fxml"));
		try {
//			borderPane.setBackground(new Background(new BackgroundFill(Color.rgb(00,00,33), CornerRadii.EMPTY, Insets.EMPTY)));
			borderPane.setCenter(loader.load());
			((AuditController)loader.getController()).setPart(thisPart);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
