package al.sybina.ebr.s63.views;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.tools.services.IResourceProviderService;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;
import org.eclipse.fx.ui.services.resources.ImageProvider;
import org.eclipse.osgi.service.datalocation.Location;

import ca.gc.chs.ihossv1.Ihossv1Cell;
import ca.gc.chs.ihossv1.Ihossv1CellKey;
import ca.gc.chs.ihossv1.Ihossv1CellName;
import ca.gc.chs.ihossv1.Ihossv1CellPermit;
import ca.gc.chs.ihossv1.Ihossv1CellPermitChecksumException;
import ca.gc.chs.ihossv1.Ihossv1CellSignatureFileEntryVerification;
import ca.gc.chs.ihossv1.Ihossv1ChecksumSupport;
import ca.gc.chs.ihossv1.Ihossv1HardwareId;
import ca.gc.chs.ihossv1.Ihossv1MetaPermitFile;
import ca.gc.chs.ihossv1.Ihossv1MetaPermitFileDate;
import ca.gc.chs.ihossv1.Ihossv1MetaPermitFileEntry;
import ca.gc.chs.ihossv1.Ihossv1MiscellaneousSupport;
import ca.gc.chs.ihossv1.Ihossv1PublicKey;
import al.sybina.ebr.s63.InstalledPermitsSingletonHolder;
import al.sybina.ebr.s63.License3j;
import al.sybina.ebr.s63.PublicKeyWithProperties;
import al.sybina.ebr.s63.S63CatalogEntry;
import al.sybina.ebr.s63.S63Constants;
import al.sybina.ebr.s63.S63SerialEncRecord;
import al.sybina.ebr.s63.S63Utils;
import al.sybina.ebr.s63.preferences.InstallPermitPreferencePage;
import al.sybina.ebr.s63.preferences.S63PreferencePage;
import al.sybina.ebr.S57Library.fiedsRecords.S57FieldCATD;
import al.sybina.ebr.core.tools.PluginTools;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

public class S63ManagementViewController implements Initializable {
	
	@FXML private Button importCellPermitsButton;
	@FXML private Button removePermitsButton;
	@FXML private Button importChartsUpdatesButton;
	@FXML private Button refreshChartsButton;
	@FXML private TextArea logMessagesTextArea;
	@FXML private TableView<Ihossv1MetaPermitFileEntry> cellPermitsTableView;
	@FXML private TableColumn<Ihossv1MetaPermitFileEntry, String> cellNameTableColumn;
	@FXML private TableColumn<Ihossv1MetaPermitFileEntry, String> dataServerIdTableColumn;
	@FXML private TableColumn<Ihossv1MetaPermitFileEntry, Date> expirationDateTableColumn;
	@FXML private ProgressBar progressBar;
	
	private static Logger logger = LoggerCreator.createLogger(S63ManagementViewController.class);
	
	private FileChooser fileChooser;
	private DirectoryChooser folderChooser;
	
	@Inject
	private IResourceProviderService resourcesService;
	@Inject
	private ImageProvider imageProvider;
	@Inject
	private IEventBroker eventBroker;
	
	@Inject
	@Named(E4Workbench.INSTANCE_LOCATION)
	private Location instanceLocation;
	
	@Inject
	private InstallPermitPreferencePage installPermitPreferencePage;
	@Inject
	private S63PreferencePage s63PreferencePage;
	
	private String chartPathStr;
	private String rootChartPath;
	private static String installPermitString;// = Activator.getDefault().getPreferenceStore().getString(ChartsConstants.CHARTS_S63_INSTALL_PERMIT_STR);;
	

	private static String permitsLastVisitedDirectory = System.getProperty("user.home");
	private static String chartsLastVisitedDirectory = System.getProperty("user.home");
	
	private static List<String> installedCertsPaths;
	private static Ihossv1HardwareId serverAssignedHardwareId;
	private static SecretKey secretKey;
	private ObservableList<Ihossv1MetaPermitFileEntry> allInstalledPermitsObsList;
	@Inject
	private InstalledPermitsSingletonHolder installedPermitsSingletonHolder;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		chartPathStr = s63PreferencePage.getChartsPathsStr().getValue().getAbsolutePath();
		rootChartPath = s63PreferencePage.getRootChartsPathsStr().getValue().getAbsolutePath();
		installPermitString = License3j.getInstallPermit();
		
		installedCertsPaths = new ArrayList<String>();
		installedCertsPaths.add(s63PreferencePage.getChartsS63CertFilesPathsStr().getValue().getAbsolutePath());
		
		secretKey = S63Utils.loadSecretKey();
		try {
			String serverAssignedMachineId = S63Utils.getServerAssignedMachineId(installPermitString);
			serverAssignedHardwareId = new Ihossv1HardwareId(serverAssignedMachineId, null);
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | IllegalArgumentException | InterruptedException | IOException e) {
			logger.warning(e.getMessage());
			importCellPermitsButton.setDisable(true);
			removePermitsButton.setDisable(true);
			importChartsUpdatesButton.setDisable(true);
			refreshChartsButton.setDisable(true);
			logMessagesTextArea.appendText("Userpermit or install permit is not set or is incorrect!"+"\n");
		}
		
//		allInstalledPermitsObsList = FXCollections.observableList(S63Utils.getAllInstalledPermits());
		allInstalledPermitsObsList = installedPermitsSingletonHolder.getAllInstalledPermitsObsList();
		cellPermitsTableView.setItems(allInstalledPermitsObsList);
		cellPermitsTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		cellNameTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIhossv1CellPermit().getIhossv1CellName().getCellDataFileName()));
		dataServerIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("dataServerId"));
		expirationDateTableColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Date>() {
			final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			@Override
            public String toString(Date t) {
                if (t==null) {
                    return "" ;
                } else {
                    return dateFormat.format(t);
                }
            }
            @Override
            public Date fromString(String string) {
                try {
					return dateFormat.parse(string);
				} catch (ParseException e) {
					return null;
				}
            }
        }));
		expirationDateTableColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<Date>(cellData.getValue().getIhossv1CellPermit().getIhossv1CellPermitDate().getDate()));
		
		fileChooser = new FileChooser();
		FileChooser.ExtensionFilter permitFilesFilter = new FileChooser.ExtensionFilter("Permit files", "PERMIT.TXT");
		FileChooser.ExtensionFilter allFilesFilter = new FileChooser.ExtensionFilter("All Files", "*.*");
		fileChooser.getExtensionFilters().addAll(permitFilesFilter, allFilesFilter);
		
		folderChooser = new DirectoryChooser();
		
		logMessagesTextArea.setEditable(false);
		ContextMenu contextMenu = new ContextMenu();
	    contextMenu.getItems().addAll(createDefaultMenuItems(logMessagesTextArea));
	    logMessagesTextArea.setContextMenu(contextMenu);
	    
	    if(serverAssignedHardwareId != null) {
			String defaultPermitPath = PluginTools.getFileLocalToRuntime("s63/PERMIT.TXT");
			File defaultPermitFile = new File(defaultPermitPath);
			if(defaultPermitFile!=null && defaultPermitFile.isFile()) {
				String result = importPermits(defaultPermitFile);
		        allInstalledPermitsObsList.clear();
		        allInstalledPermitsObsList.addAll(S63Utils.getAllInstalledPermits());
			}
	    }
		
	}
	private List<MenuItem> createDefaultMenuItems(TextInputControl t) {
	    MenuItem cut = new MenuItem("Cut");
	    cut.setOnAction(e -> t.cut());
	    MenuItem copy = new MenuItem("Copy");
	    copy.setOnAction(e -> t.copy());
//	    MenuItem paste = new MenuItem("Paste");
//	    paste.setOnAction(e -> t.paste());
	    MenuItem delete = new MenuItem("Delete");
	    delete.setOnAction(e -> t.deleteText(t.getSelection()));
	    MenuItem selectAll = new MenuItem("Select All");
	    selectAll.setOnAction(e -> t.selectAll());

	    BooleanBinding emptySelection = Bindings.createBooleanBinding(() ->
	        t.getSelection().getLength() == 0,
	        t.selectionProperty());

	    cut.disableProperty().bind(emptySelection);
	    copy.disableProperty().bind(emptySelection);
	    delete.disableProperty().bind(emptySelection);

	    return Arrays.asList(cut, copy, /**paste,**/ delete, new SeparatorMenuItem(), selectAll);
	}
	
	@FXML
	public void handleImportCellPermits(final ActionEvent e) {
		fileChooser.setInitialDirectory(new  File(permitsLastVisitedDirectory));
        List<File> list = fileChooser.showOpenMultipleDialog(importCellPermitsButton.getScene().getWindow());
        permitsLastVisitedDirectory = (list!=null && list.size()>=1)?list.get(0).getParent():System.getProperty("user.home");
        if (list != null) {
            for (File file : list) {
            	logMessagesTextArea.appendText("Importing permits from : " + file.getAbsolutePath()+"\n");
            	String result = importPermits(file);
            	logMessagesTextArea.appendText(result+"\n");
            	logMessagesTextArea.appendText("Finished importing permits from : " + file.getAbsolutePath()+"\n");
            }
        }
        allInstalledPermitsObsList.clear();
        allInstalledPermitsObsList.addAll(S63Utils.getAllInstalledPermits());
    }

	private String importPermits(File file) {
		StringBuffer messages = new StringBuffer();
		if(!file.getName().equals("PERMIT.TXT")) {
			return S63ManagementView.SSE11_CELL_PERMIT_NOT_FOUND;
		}
		
		Ihossv1MetaPermitFile anIhossv1MetaPermitFile = new Ihossv1MetaPermitFile(file);
		Ihossv1MetaPermitFileDate anIhossv1MetaPermitFileDate = null;
		Ihossv1MetaPermitFileEntry[] encIhossv1MetaPermitFileEntryArray = null;
		Ihossv1MetaPermitFileEntry[] ecsIhossv1MetaPermitFileEntryArray = null;
		try {
			anIhossv1MetaPermitFileDate = anIhossv1MetaPermitFile.getIhossv1MetaPermitFileDate();
			encIhossv1MetaPermitFileEntryArray = anIhossv1MetaPermitFile.getEncIhossv1MetaPermitFileEntries();
			ecsIhossv1MetaPermitFileEntryArray = anIhossv1MetaPermitFile.getEcsIhossv1MetaPermitFileEntries();
			if(encIhossv1MetaPermitFileEntryArray.length==0 && ecsIhossv1MetaPermitFileEntryArray.length==0) {
				return S63ManagementView.SSE11_CELL_PERMIT_NOT_FOUND;
			}
		} catch (Exception e1) {
			return S63ManagementView.SSE12_CELL_PERMIT_FORMAT_INCORRECT;
		}
		for(Ihossv1MetaPermitFileEntry encIhossv1MetaPermitFileEntry : encIhossv1MetaPermitFileEntryArray) {
			Ihossv1CellPermit aIhossv1CellPermit = encIhossv1MetaPermitFileEntry.getIhossv1CellPermit();
			if(aIhossv1CellPermit==null) {
				String error = encIhossv1MetaPermitFileEntry.getComment();
				logger.info(error);
				String cellNameOfInvalidCellPermit = error.length()>=8?error.substring(0, 8):error;
				messages.append(cellNameOfInvalidCellPermit + ", " + S63ManagementView.SSE12_CELL_PERMIT_FORMAT_INCORRECT + "\n");
				continue;
			}
			try {
				S63Utils.verifyIhossv1CellPermit(aIhossv1CellPermit, serverAssignedHardwareId);
			} catch (Ihossv1CellPermitChecksumException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
				String invalidCellPermit = aIhossv1CellPermit.getString();
				logger.info("Invalid Cell Permit: " + invalidCellPermit);
				String cellNameOfInvalidCellPermit = invalidCellPermit.length()>=8?invalidCellPermit.substring(0, 8):invalidCellPermit;
				messages.append(cellNameOfInvalidCellPermit + ", " + S63ManagementView.SSE13_CELL_PERMIT_INVALID + "\n");
				continue;
			}
			
			GregorianCalendar gregorianCalendar = (GregorianCalendar) Calendar.getInstance();
		    gregorianCalendar.setTime(aIhossv1CellPermit.getIhossv1CellPermitDate().getDate());
		    ZonedDateTime zonedDateTime = gregorianCalendar.toZonedDateTime();
			LocalDate expiryDate = zonedDateTime.toLocalDate();
			LocalDate today = LocalDate.now();
			if(expiryDate.isBefore(today)) {
				messages.append(aIhossv1CellPermit.getIhossv1CellName().getCellDataFileName() + ", " + S63ManagementView.SSE15_SUBSCRIPTION_SERVICE_EXPIRED + "\n");
			}
			if(expiryDate.isAfter(today) && expiryDate.isBefore(today.plusDays(30))) {
				messages.append(aIhossv1CellPermit.getIhossv1CellName().getCellDataFileName() + ", " + S63ManagementView.SSE20_SUBSCRIPTION_SERVICE_NEAR_EXPIRE + "\n");
			}
			try {
				S63Utils.generateChartPermitFile(encIhossv1MetaPermitFileEntry);
			} catch (Exception e) {
				messages.append("Error: Can't install "+aIhossv1CellPermit.getIhossv1CellName().getCellDataFileName()+"\n");
				continue;
			}
			messages.append(aIhossv1CellPermit.getIhossv1CellName().getCellDataFileName());
			messages.append(", valid to ");
			messages.append(new SimpleDateFormat("dd/MM/yyyy").format(aIhossv1CellPermit.getIhossv1CellPermitDate().getDate()));
			messages.append(" installed OK\n");
		}
		return messages.length()!=0 ? messages.toString().trim() : "";
	}
	
	@FXML
	public void handleRemovePermits(final ActionEvent e) {
		ObservableList<Ihossv1MetaPermitFileEntry> selectedItems = cellPermitsTableView.getSelectionModel().getSelectedItems();
		for(Ihossv1MetaPermitFileEntry aIhossv1MetaPermitFileEntry : selectedItems) {
			File file = S63Utils.getInstalledPermitFile(aIhossv1MetaPermitFileEntry);
			File dataServerFolder = file.getParentFile();
			if(file!=null) {
				file.delete();
			}
			if(dataServerFolder.list().length==0) {
				dataServerFolder.delete();
			}
		}
		allInstalledPermitsObsList.removeAll(selectedItems);
	}
	@FXML
	public void handleImportChartsUpdates(final ActionEvent e) {
		folderChooser.setInitialDirectory(new File(chartsLastVisitedDirectory));
		File chartsUpdatesFolser = folderChooser.showDialog(importChartsUpdatesButton.getScene().getWindow());
		if(chartsUpdatesFolser==null) return;
        chartsLastVisitedDirectory = (chartsUpdatesFolser!=null)?chartsUpdatesFolser.getAbsolutePath():System.getProperty("user.home");
        File encRootDir = null;
        File serialEnc = null;
        for(File file : chartsUpdatesFolser.listFiles()) {
        	if(file.isDirectory() && file.getName().endsWith("ENC_ROOT")) {
        		encRootDir = file;
        	}
        	if(file.getName().toUpperCase().endsWith("SERIAL.ENC")) {
        		serialEnc = file;
        	}
        }
        if(encRootDir==null || !encRootDir.exists()) {
        	Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Error");
        	alert.setHeaderText("Cannot find \"ENC_ROOT\" directory");
        	alert.showAndWait();
        	handleImportChartsUpdates(null);
        	return;
        }
        
        String verificationResult = verifySACert(installedCertsPaths);
        if(verificationResult!=null && !verificationResult.equals("")) {
        	logMessagesTextArea.appendText(verificationResult+"\n");
        	return;
        }
        
        File catalog031File = new File(encRootDir, "CATALOG.031");
        S63CatalogEntry aS63CatalogEntry;
		try {
			aS63CatalogEntry = new S63CatalogEntry(catalog031File.getAbsolutePath());
			if(aS63CatalogEntry.getElements().size()==0) {
				logMessagesTextArea.appendText("Can not find any chart in CATALOG.031 in ENC_ROOT folder!"+"\n");
	        	return;
			}
		} catch (Exception e2) {
			logMessagesTextArea.appendText("Can not find CATALOG.031 in ENC_ROOT folder!"+"\n");
        	return;
		}
		S57FieldCATD firstS57FieldCATD = aS63CatalogEntry.getElements().get(0);
		Ihossv1Cell fistIhossv1Cell = getCell(encRootDir, firstS57FieldCATD);
		
		
		PublicKeyWithProperties thePublicKeyWithProperties;
		try {
			thePublicKeyWithProperties = findRightInstalledPublicKey(fistIhossv1Cell);
		} catch (IOException e1) {
			logMessagesTextArea.appendText("Cannot read file. " + e1.getMessage() + "\n");
			return;
		}
		if(thePublicKeyWithProperties==null) {
			logMessagesTextArea.appendText(S63ManagementView.SSE06_SA_SIGNED_DATA_SERVER_CERT_INVALID + "\n");
			return;
		}
		if(!thePublicKeyWithProperties.isSAPublicKey())logMessagesTextArea.appendText(S63ManagementView.SSE26_ENC_NOT_AUTHENTICATED_BY_SA + "\n");
		
		
		final File finalEncRootDir = encRootDir;
		final File finalSerialEnc = serialEnc;
		Task task = new Task<Void>() {
			@Override
			public Void call() {
				List<S57FieldCATD> elementList = aS63CatalogEntry.getElements();
				final int size = elementList.size();
				for (int i = 0; i < size; i++) {
					S57FieldCATD s57FieldCATD = elementList.get(i);
					validateExtract(finalEncRootDir, finalSerialEnc, s57FieldCATD, thePublicKeyWithProperties);
					updateProgress(i+1, size);
				}
				if(rootChartPath!=null && chartPathStr!=null) {
					Object[] param = new Object[] {rootChartPath, chartPathStr, true, null};
					eventBroker.post(S63Constants.BUILD_CATALOG_REQUESTED, param);
					eventBroker.post(S63Constants.CHARTS_REPAINT_REQUESTED, "reloadView");
				} else {
					logMessagesTextArea.appendText("Please set the value of preference Charts Path!");
				}
				return null;
			}
		};
		progressBar.progressProperty().bind(task.progressProperty());
		new Thread(task).start();
		
	}
	
	private void validateExtract(File encRootDir, File serialEnc, S57FieldCATD s57FieldCATD, PublicKeyWithProperties thePublicKeyWithProperties) {
		Ihossv1CellSignatureFileEntryVerification[] ihossv1CSFEVA = null;
       	Ihossv1Cell aIhossv1Cell = getCell(encRootDir, s57FieldCATD);
       	List<S63SerialEncRecord> s63SerialEncRecords = S63Utils.parseSerialEnc(serialEnc);
        if(s63SerialEncRecords.isEmpty()) {
        	Platform.runLater(new Runnable() {
    		    public void run() {
    		    	logMessagesTextArea.appendText(aIhossv1Cell.getIhossv1CellName().getCellDataFileName() + ": " + "ERROR Reading SERIAL_ENC!\n");
    		    }
    		});
        	return;
        }
        S63SerialEncRecord s63SerialEncRecord = s63SerialEncRecords.get(0);
        File installedPermitFile = S63Utils.getInstalledPermitFile(s63SerialEncRecord.getDataServerId(), aIhossv1Cell.getIhossv1CellName().getCellDataFileName());
        if(installedPermitFile==null) {
        	Platform.runLater(new Runnable() {
    		    public void run() {
    		    	logMessagesTextArea.appendText(aIhossv1Cell.getIhossv1CellName().getCellDataFileName() + ": " + S63ManagementView.SSE21_NO_VALID_CELL_PERMIT+"\n");
    		    }
    		});
        	return;
        }
        Ihossv1MetaPermitFileEntry aIhossv1MetaPermitFileEntry = null;
		try {
			aIhossv1MetaPermitFileEntry = S63Utils.getIhossv1MetaPermitFileEntryFromFile(installedPermitFile);
		} catch (FileNotFoundException | ParseException e) {
			logger.warning(e.getMessage());
			return;
		}
		Date permitExpiryDate = aIhossv1MetaPermitFileEntry.getIhossv1CellPermit().getIhossv1CellPermitDate().getDate();
		GregorianCalendar gregorianCalendar = (GregorianCalendar) Calendar.getInstance();
	    gregorianCalendar.setTime(permitExpiryDate);
	    ZonedDateTime zonedDateTime = gregorianCalendar.toZonedDateTime();
		LocalDate expiryDate = zonedDateTime.toLocalDate();
		LocalDate today = LocalDate.now();
		if(s63SerialEncRecord.getDateOfPublication().after(permitExpiryDate)) {
			Platform.runLater(new Runnable() {
    		    public void run() {
    		    	logMessagesTextArea.appendText(aIhossv1Cell.getIhossv1CellName().getCellDataFileName() + ": " + S63ManagementView.SSE15_SUBSCRIPTION_SERVICE_EXPIRED + "\n");
    		    }
    		});
			return;
		}
		if(expiryDate.isAfter(today) && expiryDate.isBefore(today.plusDays(30))) {
			Platform.runLater(new Runnable() {
    		    public void run() {
    		    	logMessagesTextArea.appendText(aIhossv1Cell.getIhossv1CellName().getCellDataFileName() + ": " + S63ManagementView.SSE20_SUBSCRIPTION_SERVICE_NEAR_EXPIRE + "\n");
    		    }
    		});
		}
        
		
       	try {
       		ihossv1CSFEVA = aIhossv1Cell.verify(thePublicKeyWithProperties.getTheIhossv1PublicKey());
		} catch (InvalidKeyException | InvalidKeySpecException | IOException e1) {
			return;
    	} catch (SignatureException e1) {
    		Platform.runLater(new Runnable() {
    		    public void run() {
    		    	logMessagesTextArea.appendText(aIhossv1Cell.getIhossv1CellName().getCellSignatureFileName() + ": " + S63ManagementView.SSE24_INVALID_ENC_SIGNATURE_FORMAT + "\n");
    		    }
    		});
    		return;
    	}
        if(!ihossv1CSFEVA[0].getIhossv1CertificateVerification()) {
        	Platform.runLater(new Runnable() {
    		    public void run() {
    		    	logMessagesTextArea.appendText(aIhossv1Cell.getIhossv1CellName().getCellSignatureFileName() + ": " + S63ManagementView.SSE06_SA_SIGNED_DATA_SERVER_CERT_INVALID + "\n");
    		    }
    		});
        	return;
        }
        if(!ihossv1CSFEVA[0].getIhossv1CellDataFileVerification()) {
        	Platform.runLater(new Runnable() {
    		    public void run() {
    		    	logMessagesTextArea.appendText(aIhossv1Cell.getIhossv1CellName().getCellSignatureFileName() + ": " + S63ManagementView.SSE09_ENC_SIGNATURE_INVALID + "\n");
    		    }
    		});
        	return;
        }
        Ihossv1Cell decryptedIhossv1Cell = null;
		try {
			Ihossv1CellKey currentIhossv1CellKey = aIhossv1MetaPermitFileEntry.getIhossv1CellPermit().getCurrentIhossv1CellKey();
			currentIhossv1CellKey.decrypt(serverAssignedHardwareId);
			decryptedIhossv1Cell = aIhossv1Cell.decrypt(S63Utils.getInstalledChartsFolder(), currentIhossv1CellKey, secretKey, S63Utils.sencExtensionPrefix);
		} catch (InvalidKeyException | SecurityException | IOException | IllegalBlockSizeException | BadPaddingException e1) {
			try {
				Ihossv1CellKey currentIhossv1CellKey = aIhossv1MetaPermitFileEntry.getIhossv1CellPermit().getNextIhossv1CellKey();
				currentIhossv1CellKey.decrypt(serverAssignedHardwareId);
				decryptedIhossv1Cell = aIhossv1Cell.decrypt(S63Utils.getInstalledChartsFolder(), currentIhossv1CellKey, secretKey, S63Utils.sencExtensionPrefix);
			} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | SecurityException | IOException e) {
				Platform.runLater(new Runnable() {
	    		    public void run() {
	    		    	logMessagesTextArea.appendText(aIhossv1Cell.getIhossv1CellName().getCellDataFileName() + ": " + S63ManagementView.SSE21_NO_VALID_CELL_PERMIT+"\n");
	    		    }
	    		});
	        	return;
			}
		}
		
		try {
			FileInputStream sencInputStream = new FileInputStream(decryptedIhossv1Cell.getIhossv1CellDataFile().getFile());
			byte[] catalog31CrcByteArray = Ihossv1MiscellaneousSupport.byteArray(s57FieldCATD.crcs);
			boolean crcOk = Ihossv1ChecksumSupport.verifyDecryptedCrc32Checksum(sencInputStream, secretKey, catalog31CrcByteArray);
			if(!crcOk) {
				Platform.runLater(new Runnable() {
	    		    public void run() {
	    		    	logMessagesTextArea.appendText(aIhossv1Cell.getIhossv1CellName().getCellSignatureFileName() + ": " + S63ManagementView.SSE16_ENC_CRC_VALUE_INCORRECT + "\n");
	    		    }
	    		});
	        	return;
			}
			
			S63Utils.updatePermitFile(installedPermitFile, s57FieldCATD.comt, aIhossv1Cell.getIhossv1CellDataFile().getFile().getPath());
			
		} catch (IOException e) {
			logger.warning(e.getMessage());
			return;
		} finally {
//			decryptedIhossv1Cell.getIhossv1CellDataFile().getFile().delete();
		}
		
		Platform.runLater(new Runnable() {
		    public void run() {
		    	logMessagesTextArea.appendText(aIhossv1Cell.getIhossv1CellName().getCellSignatureFileName() + ": installed\n");
		    }
		});
	}
	
	@FXML
	public void handleRefreshCharts(final ActionEvent e) {
		Job job = new Job("updating catalog"){
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				if(rootChartPath!=null && chartPathStr!=null) {
					Object[] param = new Object[] {rootChartPath, chartPathStr, true, monitor};
					eventBroker.post(S63Constants.BUILD_CATALOG_REQUESTED, param);
					eventBroker.post(S63Constants.CHARTS_REPAINT_REQUESTED, "reloadView");
				} else {
					logMessagesTextArea.appendText("Please set the value of preference Charts Path!");
				}
				return Status.OK_STATUS;
			}
		};
		job.setUser(true);
		job.schedule();
	}
	
	private static String verifySACert(List<String> installedCertsPaths) {
		Ihossv1PublicKey ihoSAIhossv1PublicKey = null;
		String result = S63ManagementView.SSE05_CERT_FILE_NOT_AVAILABLE;
        for(String certPath : installedCertsPaths) {
        	File ihoSACertOrPub =  new File(certPath);
        	if(ihoSACertOrPub==null || !ihoSACertOrPub.exists() || !ihoSACertOrPub.canRead()) {
        		continue;
        	}
        	if(certPath.toUpperCase().endsWith("IHO.PUB")) {
        		try {
            		ihoSAIhossv1PublicKey = new Ihossv1PublicKey(ihoSACertOrPub);
            		result = "";
    			} catch (InvalidKeySpecException | IOException e1) {
    				result = S63ManagementView.SSE08_FILE_INCORRECT_FORMAT_MSG;
    			}
        	}
        	if(certPath.toUpperCase().endsWith("IHO.CRT")) {
    			try {
    				FileInputStream in = new FileInputStream(ihoSACertOrPub);
    				CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
    	        	X509Certificate cert = (X509Certificate)certFactory.generateCertificate(in);
    	        	ihoSAIhossv1PublicKey = new Ihossv1PublicKey((DSAPublicKey)cert.getPublicKey());
    	        	cert.checkValidity();
    	        	result = "";
    			} catch (IOException e1) {
    				logger.warning(e1.getMessage());
    			} catch (CertificateException e1) {
    				result = S63ManagementView.SSE22_CERT_EXPIRED_MSG; 
    			}
        	}
        }
        return result;
	}
	private static PublicKeyWithProperties findRightInstalledPublicKey(Ihossv1Cell fistIhossv1Cell) throws IOException {
		List<PublicKeyWithProperties> publicKeyWithPropertiesList = S63Utils.getIhossv1PublicKeyFromCertPaths(installedCertsPaths);
		if(publicKeyWithPropertiesList.size()==0) return null;
		Ihossv1CellSignatureFileEntryVerification[] ihossv1CSFEVA1 = null;
		PublicKeyWithProperties thePublicKeyWithProperties = null;
		for(PublicKeyWithProperties aPublicKeyWithProperties : publicKeyWithPropertiesList) {
			try {
	       		ihossv1CSFEVA1 = fistIhossv1Cell.verify(aPublicKeyWithProperties.getTheIhossv1PublicKey());
	       		if(ihossv1CSFEVA1[0].getIhossv1CertificateVerification()) {
	       			thePublicKeyWithProperties = aPublicKeyWithProperties;
		       		break;
	       		}
			} catch (InvalidKeyException | InvalidKeySpecException | SignatureException e1) {
        	} catch (IOException e) {
        		throw e;
        	}
		}
		return thePublicKeyWithProperties;
	}
	
	private static Ihossv1Cell getCell(File encRootDir, S57FieldCATD s57FieldCATD) {
		File entryFile = new File(encRootDir, s57FieldCATD.fileName);
		File entryFolder = entryFile.getParentFile();
		Ihossv1CellName aIhossv1CellName = new Ihossv1CellName(entryFile.getName().substring(0, entryFile.getName().lastIndexOf(".")));
		String entryExtension = entryFile.getName().substring(entryFile.getName().lastIndexOf(".")+1);
		Ihossv1Cell aIhossv1Cell = new Ihossv1Cell(entryFolder, aIhossv1CellName, entryExtension);
		return aIhossv1Cell;
	}
	
	public void setRootChart(String rootChart) {
		this.rootChartPath = rootChart;
	}
}
