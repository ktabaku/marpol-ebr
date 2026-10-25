package al.sybina.ebr.logbook.commons.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;

import al.sybina.ebr.logbook.dialogs.databackup.DataBackupDialog;
import javafx.stage.Stage;


public class DataBackupHandler {
	@Inject private DataBackupDialog dataBackupDialog;
	
	@Execute
	public void execute(Stage stage) {
	  if(dataBackupDialog != null) {
		  dataBackupDialog.showDialog(stage);
	  }
	}
	
	@CanExecute
	public boolean canExecute() {
		return dataBackupDialog != null;
	}
}
