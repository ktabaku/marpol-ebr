package al.sybina.ebr.rcp.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class RestartHandler {
	
	@Execute
	public boolean execute(EPartService partService, IWorkbench workbench, Stage stage, @Optional @Named("al.sybina.ebr.rcp.commandparameter.restartmessage") String restartMessage) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.initOwner(stage);
		alert.setTitle("Restart");
		if (restartMessage==null) {
			alert.setHeaderText("Restart application?");
		} else {
			alert.setHeaderText("Restart application?");
			alert.setContentText(restartMessage);
		}
		java.util.Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			return workbench.restart();
		} else {
			return false;
		}
	}
}
