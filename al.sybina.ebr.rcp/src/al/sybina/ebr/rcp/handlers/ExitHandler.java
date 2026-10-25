package al.sybina.ebr.rcp.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class ExitHandler {
	@Execute
	public boolean execute(EPartService partService, IWorkbench workbench, Stage stage) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.initOwner(stage);
		alert.setTitle("Close");
		if (partService.getDirtyParts().size() > 0) {
			alert.setHeaderText("Unsaved data");
			alert.setContentText("Really close?");
		} else {
			alert.setHeaderText("Close application?");
		}
		java.util.Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			workbench.close();
			Thread.getAllStackTraces().keySet().forEach((t) -> {
				if(t.isDaemon() && t.getName().startsWith("ComThread")) {
					try {
						synchronized(t){
							t.stop();
						}
			    	}catch(ThreadDeath td) {
			    		if(td.getMessage()!=null) {
			    			System.out.println(td.getMessage());
			    		};
			    	}catch(Exception e) {
			    		if(e.getMessage()!=null) {
			    			System.out.println(e.getMessage());
			    		};
			    	}
				}
			});
			return true;
		} else {
			return false;
		}
	}
}
