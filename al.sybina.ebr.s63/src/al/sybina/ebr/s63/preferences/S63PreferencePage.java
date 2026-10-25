package al.sybina.ebr.s63.preferences;

import java.io.File;
import java.util.function.Predicate;

import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;

import com.dlsc.formsfx.model.validators.CustomValidator;
import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.Group;
import com.dlsc.preferencesfx.model.Setting;
import com.opcoach.e4.preferences.E4PreferencePage;

import al.sybina.ebr.s63.License3j;
import al.sybina.ebr.s63.S63Utils;
import al.sybina.ebr.core.tools.PluginTools;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */
@Creatable
@Singleton
public class S63PreferencePage extends E4PreferencePage {
	private static Logger logger = LoggerCreator.createLogger(S63PreferencePage.class);
	
	private ObjectProperty<File> chartsS63CertFilesPathsStr = new SimpleObjectProperty<File>(new File(PluginTools.getFileLocalToRuntime("s63/IHO.CRT")));
	private ObjectProperty<File> chartsPathsStr = new SimpleObjectProperty<File>(new File(PluginTools.getFileLocalToRuntime("s63/s63charts")));
	private ObjectProperty<File> rootChartsPathsStr = new SimpleObjectProperty<File>(new File(PluginTools.getFileLocalToRuntime("charts")));

	
	public S63PreferencePage() {

		setCategory(Category.of("S63", Group.of("General",
			Setting.of("S63 IHO Certificate:", chartsS63CertFilesPathsStr, false), //.validate(userPermitValidator),
			Setting.of("S63 Charts Path:", chartsPathsStr, true),
			Setting.of("S63 Root Charts Path:", rootChartsPathsStr, true))));
	}


	public ObjectProperty<File> getChartsS63CertFilesPathsStr() {
		return chartsS63CertFilesPathsStr;
	}


	public void setChartsS63CertFilesPathsStr(ObjectProperty<File> chartsS63CertFilesPathsStr) {
		this.chartsS63CertFilesPathsStr = chartsS63CertFilesPathsStr;
	}


	public ObjectProperty<File> getChartsPathsStr() {
		return chartsPathsStr;
	}


	public void setChartsPathsStr(ObjectProperty<File> chartsPathsStr) {
		this.chartsPathsStr = chartsPathsStr;
	}


	public ObjectProperty<File> getRootChartsPathsStr() {
		return rootChartsPathsStr;
	}


	public void setRootChartsPathsStr(ObjectProperty<File> rootChartsPathsStr) {
		this.rootChartsPathsStr = rootChartsPathsStr;
	}
	
	
}

//	@Inject
//	private IEventBroker eventBroker;
//	
//	
//	StringProperty path = new SimpleStringProperty("Company Name");
//	StringProperty shipName = new SimpleStringProperty("Ship Name");
//	StringProperty supportEmail = new SimpleStringProperty("info@sybina.al");
//	
//	public S63PreferencePage() {
//		setCategory(Category.of("S63", Group.of("General",
//				Setting.of("Installed S63 Certificates/Keys", path),
//				Setting.of("Ship Name", shipName),
//				Setting.of("Charts update request receiver email address", supportEmail))));
//	}
//
//	@Override
//	public void createFieldEditors() {
//
//		
//		FileListEditor fileListEditor = new FileListEditor(S63Constants.CHARTS_S63_CERT_FILES_PATHS_STR, "Installed S63 Certificates/Keys", parent);
//		String[] filterExtensions = new String[]{"*.CRT;*.PUB", "*.*"};
//		String[] filterNames = new String[]{"CRT, PUB files", "All files"};
//		fileListEditor.setFilterExtensionsAndNames(filterExtensions, filterNames);
//		List<String> certPaths = S63Utils.getCertFiles();
//		for(String certPath : certPaths) {
//			if(certPath != null && new File(certPath).exists()) {
//				fileListEditor.addFile(certPath);
//			}
//		}
//		addField(fileListEditor);
//		
//		StringButtonFieldEditor sbfeUserPermit = new StringButtonFieldEditor(S63Constants.CHARTS_S63_USER_PERMIT_STR, "User Permit", parent) {
//			@Override
//			protected String changePressed() {
//				String userPermit = this.getStringValue();
//				if(S63Utils.validateUserPermit(userPermit)) {
//					MessageBox messageBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_INFORMATION);
//					messageBox.setText("User Permit");
//					messageBox.setMessage("User Permit OK");
//					messageBox.open();
//					return userPermit;
//				} else {
//					MessageBox messageBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_ERROR);
//					messageBox.setText("User Permit");
//					messageBox.setMessage("User Permit invalid");
//					messageBox.open();
//					return userPermit;
//				}
//			}
//		};
//		sbfeUserPermit.setChangeButtonText("Test");
//		addField(sbfeUserPermit);
//		
//		StringButtonFieldEditor sbfeInstallPermit = new StringButtonFieldEditor(S63Constants.CHARTS_S63_INSTALL_PERMIT_STR, "Install Permit", parent) {
//			@Override
//			protected String changePressed() {
//				String installPermit = this.getStringValue();
//				if(S63Utils.validateInstallPermit(installPermit)) {
//					MessageBox messageBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_INFORMATION);
//					messageBox.setText("Install Permit");
//					messageBox.setMessage("Install Permit OK");
//					messageBox.open();
//					return installPermit;
//				} else {
//					MessageBox messageBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_ERROR);
//					messageBox.setText("Install Permit");
//					messageBox.setMessage("Install Permit invalid");
//					messageBox.open();
//					return installPermit;
//				}
//			}
//		};
//		sbfeInstallPermit.setChangeButtonText("Test");
//		addField(sbfeInstallPermit);
//		
//		StringButtonFieldEditor sbfeSystemIdentification = new StringButtonFieldEditor(S63Constants.CHARTS_S63_SYSTEM_IDENTIFICATION_STR, "System Identification", parent) {
//			@Override
//			protected String changePressed() {
//				String systemIdPath = this.getStringValue();
//				MessageBox messageBox = new MessageBox(Display.getCurrent().getActiveShell(),
//						SWT.ICON_INFORMATION | SWT.YES | SWT.NO);
//				messageBox.setText("System Identification");
//				messageBox.setMessage(
//						"To obtain an Install Permit, you must generate a unique System Identification File.\n"
//								+ "This file il also known as a \"fingerprint\" file.\n"
//								+ "The fingerprint file contains information to uniquely identify this computer.\n"
//								+ "\n"
//								+ "After creating this file, you need it to generate your InstallPermit at the sybina.al web site.\n"
//								+ "Proceed to create Fingerprint file?");
//				if (messageBox.open() == SWT.YES) {
//					File fingerPrintFile;
//					try {
//						fingerPrintFile = S63Utils.generateFingerPrintFile();
//						return fingerPrintFile.getAbsolutePath();
//					} catch (Exception e1) {
//						e1.printStackTrace();
//						return "Error! " + e1.getMessage();
//					}
//					
//				}
//				return "";
//			}
//		};
//		sbfeSystemIdentification.setChangeButtonText("Create System Identifier File...");
//		addField(sbfeSystemIdentification);
//	}
//
//	
//	
//
//	@Override
//	public boolean performOk() {
//		boolean res = super.performOk();
//		if (res) {
//			eventBroker.post(S63Constants.CHARTS_PREFERENCES_CHANGED_S63, "reloadView");
//			eventBroker.post(S63Constants.CHARTS_REPAINT_REQUESTED, "reloadView");
//		}
//		return res;
//	}

