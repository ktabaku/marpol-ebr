package al.sybina.ebr.logbook.dialogs.databackup;

import al.sybina.ebr.commons.utils.DatabaseCredentials;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.inject.Inject;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.tools.services.IResourceProviderService;
import org.eclipse.fx.core.di.Service;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;
import org.eclipse.fx.ui.services.resources.ImageProvider;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.quartz.JobDataMap;

import com.opcoach.e4.preferences.E4PreferencePage;
import com.sun.javafx.tk.FileChooserType;

import al.sybina.ebr.commons.utils.JavaFXUtils;
import al.sybina.ebr.commons.utils.ViewPerspectiveHelper;
import al.sybina.ebr.logbook.commons.Activator;
import al.sybina.ebr.logbook.core.services.BackupDatabaseJob;
import al.sybina.ebr.logbook.core.services.DerbyDatabaseBackupService;
import al.sybina.ebr.logbook.core.services.DerbyDatabaseBackupServiceImpl;
import al.sybina.ebr.login.preferences.LoginPreferencePage;
import al.sybina.ebr.login.services.RemoteEventAdminService;
import al.sybina.ebr.core.tools.PluginTools;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class DataBackupDialogController implements Initializable {
	
	protected String CACHE_FILE_NAME = System.getProperty("user.home") + "/.ebr/caches/caches.properties";
	
	private static Logger logger = LoggerCreator.createLogger(DataBackupDialogController.class);

	@Inject
	private IResourceProviderService resourcesService;
	@Inject
	private ImageProvider imageProvider;
	@Inject
	private IEventBroker eventBroker;
	@Inject
	@Service
	private RemoteEventAdminService remoteEventAdminService;
	@Inject
	private EventAdmin eventAdmin;
	@Inject
	private LoginPreferencePage loginPreferencePage;
	@Inject
	private DerbyDatabaseBackupService derbyDatabaseBackupService;
	@Inject
	private ViewPerspectiveHelper viewPerspectiveHelper;
	
	@FXML private Button backupBrowseButton;
	@FXML private Button restoreBrowseButton;
	@FXML private Button backupOkButton;
	@FXML private Button restoreOkButton;
	@FXML private ProgressBar backupProgressBar;
	@FXML private ProgressBar restoreProgressBar;
	@FXML private TextField backupFolderTextField;
	@FXML private TextField restoreFileTextField;
	
	@FXML private ToggleGroup backupDataTypeToggleGroup;
	@FXML private RadioButton backupStandardRadioButton;
	@FXML private RadioButton backupXmlRadioButton;
	
	@FXML private ToggleGroup restoreDataTypeToggleGroup;
	@FXML private RadioButton restoreStandardRadioButton;
	@FXML private RadioButton restoreXmlRadioButton;
	

	private Properties properties;
	private DirectoryChooser directoryChooser;
	private FileChooser fileChooser;
	private File backupDataFolder;
	private File restoreDataFile;
	private Stage dialogStage;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		JavaFXUtils.addFilledPseudoClass(backupFolderTextField);
		JavaFXUtils.addFilledPseudoClass(restoreFileTextField);
//		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
//		File routesFolder = new File(marpolPreferencePage.getRoutesFolder().getValue());
		directoryChooser = new DirectoryChooser();
		fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Backup File", "*.zip"), new ExtensionFilter("Xml Backup File", "*.encrypted"), new ExtensionFilter("All Files", "*.*"));
		
		properties = new Properties();
		String userInitialDirectory = null;
        try {
            File cacheFile = new File(CACHE_FILE_NAME);
            if (cacheFile.isFile()) {
            	try (FileInputStream cacheIn = new FileInputStream(cacheFile)) {
            		properties.load(cacheIn);
            	}
            }
            userInitialDirectory = properties.getProperty("dataBackupDir");
        } catch (Exception ex) {
        	logger.error(ex.getMessage(), ex);
        }
        
        if (userInitialDirectory == null) {
            this.directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            this.fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        } else {
            this.directoryChooser.setInitialDirectory(new File(userInitialDirectory));
            this.fileChooser.setInitialDirectory(new File(userInitialDirectory));
        }
        String serverIp = loginPreferencePage.getMainInstallationIp().getValue();
        if(!serverIp.equals("localhost") && !serverIp.equals("127.0.0.1")) {
        	restoreStandardRadioButton.setSelected(false);
        	restoreStandardRadioButton.setDisable(true);
        	restoreXmlRadioButton.setSelected(true);
        	backupStandardRadioButton.setSelected(false);
        	backupStandardRadioButton.setDisable(true);
        	backupXmlRadioButton.setSelected(true);
        }
	}

	@FXML
	private void handleBackupBrowse() {
		try {
			backupDataFolder = directoryChooser.showDialog(backupBrowseButton.getScene().getWindow());
		} catch (IllegalArgumentException e) {
			directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
			backupDataFolder = directoryChooser.showDialog(backupBrowseButton.getScene().getWindow());
		}
		if(backupDataFolder!=null) {
			backupFolderTextField.setText(backupDataFolder.getAbsolutePath());
			backupOkButton.setDisable(false);
		}
	}
	@FXML
	private void handleRestoreBrowse() {
		try {
			restoreDataFile = fileChooser.showOpenDialog(restoreBrowseButton.getScene().getWindow());
		} catch (IllegalArgumentException e) {
			fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
			restoreDataFile = fileChooser.showOpenDialog(restoreBrowseButton.getScene().getWindow());
		}
		if(restoreDataFile!=null) {
			restoreFileTextField.setText(restoreDataFile.getAbsolutePath());
			restoreOkButton.setDisable(false);
		}
	}
	@FXML
	private void handleRestoreOk() {
		DataType dataType = null;
		if(restoreDataTypeToggleGroup.getSelectedToggle().equals(restoreStandardRadioButton)) {
			dataType = DataType.STANDARD;
		} else if(restoreDataTypeToggleGroup.getSelectedToggle().equals(restoreXmlRadioButton)) {
			dataType = DataType.XML;
		}
		final DataType finalDataType = dataType;
		if(restoreDataFile!=null && restoreDataFile.canRead()) {
			Task task = new Task<Void>() {
				@Override
				public Void call() {
					try {
						synchronousDataRestore(restoreDataFile.getAbsolutePath(), finalDataType);
						remoteEventAdminService.getDistributedEventAdmin(loginPreferencePage.getMainInstallationIp().getValue()).postEvent(new Event(al.sybina.ebr.logbook.commons.Activator.MARPOL_DETAILS_CHANGED, new HashMap<String, String>()));
						remoteEventAdminService.getDistributedEventAdmin(loginPreferencePage.getMainInstallationIp().getValue()).postEvent(new Event(al.sybina.ebr.logbook.commons.Activator.LOGS_CHANGED_EVENT_TOPIC, new HashMap<String, String>()));
						remoteEventAdminService.getDistributedEventAdmin(loginPreferencePage.getMainInstallationIp().getValue()).postEvent(new Event(al.sybina.ebr.login.Activator.USER_CHANGED_EVENT_TOPIC, new HashMap<String, List<Long>>()));
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
						Platform.runLater(() -> showErrorMessage("Error", "An error happend during restore, please contact the administrator."));
					}
					Platform.runLater(new Runnable() {
						public void run() {
							restoreProgressBar.progressProperty().unbind();
							restoreProgressBar.setProgress(0);
							handleClose();
							if(finalDataType.equals(DataType.STANDARD)) {
								viewPerspectiveHelper.restart();
							}
						}
					});
					
					return null;
				}
			};
			restoreProgressBar.progressProperty().bind(task.progressProperty());
			new Thread(task).start();
	    }
		properties.setProperty("dataBackupDir", restoreDataFile.getParent());
		File f = new File(CACHE_FILE_NAME);
		if(!f.exists()) f.getParentFile().mkdirs();
		OutputStream out;
		try {
			out = new FileOutputStream(f);
			properties.store(out, "Last directory choosed by user");
			out.close();
		} catch (IOException ex) {
			logger.error("Last directory choosed by user " + ex.toString());
		}
	}


	@FXML
	private void handleBackupOk() {
		DataType dataType = null;
		if(backupDataTypeToggleGroup.getSelectedToggle().equals(backupStandardRadioButton)) {
			dataType = DataType.STANDARD;
		} else if(backupDataTypeToggleGroup.getSelectedToggle().equals(backupXmlRadioButton)) {
			dataType = DataType.XML;
		}
		final DataType finalDataType = dataType;
		if(backupDataFolder!=null && backupDataFolder.canWrite()) {
			Task task = new Task<Void>() {
				@Override
				public Void call() {
					try {
						synchronousDataBackup(backupDataFolder.getAbsolutePath(), finalDataType);
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
						Platform.runLater(() -> showErrorMessage("Error", "An error happend during backup, please contact the administrator."));
					}
					Platform.runLater(() -> {
							backupProgressBar.progressProperty().unbind();
							backupProgressBar.setProgress(0);
							handleClose();
					});

					return null;
				}
			};
			backupProgressBar.progressProperty().bind(task.progressProperty());
			new Thread(task).start();
	    }
		properties.setProperty("dataBackupDir", backupDataFolder.getPath());
		File f = new File(CACHE_FILE_NAME);
		if(!f.exists()) f.getParentFile().mkdirs();
		OutputStream out;
		try {
			out = new FileOutputStream(f);
			properties.store(out, "Last directory choosed by user");
			out.close();
		} catch (IOException ex) {
			logger.error("Last directory choosed by user " + ex.toString());
		}
	}
	private final void synchronousDataBackup(String backupDir, DataType dataType) throws Exception {
		dialogStage.getScene().setCursor(Cursor.WAIT);
		
    	String serverIp = org.eclipse.core.runtime.Platform.getPreferencesService().getString(E4PreferencePage.PLUGIN_ID, "mainInstallationIp", "localhost", null);
    	String serverPort = org.eclipse.core.runtime.Platform.getPreferencesService().getString(E4PreferencePage.PLUGIN_ID, "mainInstallationPort", "1527", null);
    	String serverDb = org.eclipse.core.runtime.Platform.getPreferencesService().getString(E4PreferencePage.PLUGIN_ID, "mainInstallationDb", PluginTools.getFileLocalToRuntime(al.sybina.ebr.login.Activator.RUNTIME_LOCATION_FOLDER_NAME + "/db"), null);

		Map<String, String> jobData = new HashMap<String, String>();
		jobData.put(DerbyDatabaseBackupServiceImpl.CONNECTION_URL, "jdbc:derby://" + serverIp + ":" + serverPort + "/" + serverDb);
		jobData.put(DerbyDatabaseBackupServiceImpl.USER, DatabaseCredentials.user());
		jobData.put(DerbyDatabaseBackupServiceImpl.PASSWORD, DatabaseCredentials.password());
		jobData.put(DerbyDatabaseBackupServiceImpl.OUTPUT_DIR, backupDir);
		if(dataType.equals(DataType.STANDARD)) {
			jobData.put(DerbyDatabaseBackupServiceImpl.EXPORT_FORMAT, DerbyDatabaseBackupServiceImpl.STANDARD_FORMAT);
		} else if(dataType.equals(DataType.XML)) {
			jobData.put(DerbyDatabaseBackupServiceImpl.EXPORT_FORMAT, DerbyDatabaseBackupServiceImpl.XML_FORMAT);
		}

		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.putAll(jobData);
		BackupDatabaseJob backupDatabaseJob = new BackupDatabaseJob();
		backupDatabaseJob.execute(jobDataMap);
		
        dialogStage.getScene().setCursor(Cursor.DEFAULT);
    }
	private final void asynchronousDataBackup(String backupDir) {
		dialogStage.getScene().setCursor(Cursor.WAIT);
		derbyDatabaseBackupService.runBackupDatabaseJob(backupDir);
        dialogStage.getScene().setCursor(Cursor.DEFAULT);
	}
	


	private void synchronousDataRestore(String restoreFilePath, DataType dataType) throws Exception {
		dialogStage.getScene().setCursor(Cursor.WAIT);
		
    	String serverIp = org.eclipse.core.runtime.Platform.getPreferencesService().getString(E4PreferencePage.PLUGIN_ID, "mainInstallationIp", "localhost", null);
    	String serverPort = org.eclipse.core.runtime.Platform.getPreferencesService().getString(E4PreferencePage.PLUGIN_ID, "mainInstallationPort", "1527", null);
    	String serverDb = org.eclipse.core.runtime.Platform.getPreferencesService().getString(E4PreferencePage.PLUGIN_ID, "mainInstallationDb", PluginTools.getFileLocalToRuntime(al.sybina.ebr.login.Activator.RUNTIME_LOCATION_FOLDER_NAME + "/db"), null);
		
		Map<String, String> jobData = new HashMap<String, String>();
		jobData.put(DerbyDatabaseBackupServiceImpl.CONNECTION_URL, "jdbc:derby://" + serverIp + ":" + serverPort + "/" + serverDb);
		jobData.put(DerbyDatabaseBackupServiceImpl.USER, DatabaseCredentials.user());
		jobData.put(DerbyDatabaseBackupServiceImpl.PASSWORD, DatabaseCredentials.password());
		jobData.put(DerbyDatabaseBackupServiceImpl.RESTORE_FILE, restoreFilePath);
		if(dataType.equals(DataType.STANDARD)) {
			jobData.put(DerbyDatabaseBackupServiceImpl.RESTORE_FORMAT, DerbyDatabaseBackupServiceImpl.STANDARD_FORMAT);
		} else if(dataType.equals(DataType.XML)) {
			jobData.put(DerbyDatabaseBackupServiceImpl.RESTORE_FORMAT, DerbyDatabaseBackupServiceImpl.XML_FORMAT);
		}

		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.putAll(jobData);
		BackupDatabaseJob backupDatabaseJob = new BackupDatabaseJob();
		backupDatabaseJob.restore(jobDataMap);
		
        dialogStage.getScene().setCursor(Cursor.DEFAULT);
	}

	
	private void showErrorMessage(String header, String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(dialogStage.getOwner());
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.setTitle("Error");
		alert.setHeaderText(header);
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	
	@FXML
	private void handleClose() {
		dialogStage.close();
	}


	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	private enum DataType {
		STANDARD,
		XML
	}
}
