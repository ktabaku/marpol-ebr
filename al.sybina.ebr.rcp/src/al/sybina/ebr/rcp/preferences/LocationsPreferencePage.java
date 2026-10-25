package al.sybina.ebr.rcp.preferences;

import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;

import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.Group;
import com.dlsc.preferencesfx.model.Setting;
import com.opcoach.e4.preferences.E4PreferencePage;

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
public class LocationsPreferencePage extends E4PreferencePage {
	
	private static Logger logger = LoggerCreator.createLogger(LocationsPreferencePage.class);
	
	StringProperty eracleaPath = new SimpleStringProperty("C:\\Program Files (x86)\\Eraclea\\LiveSea.exe");
	StringProperty ecdisUpPath = new SimpleStringProperty("C:\\Program Files (x86)\\Ecdis Up\\EcdisUp.exe");
	StringProperty euMrvPath = new SimpleStringProperty("");
	StringProperty imoReaderPath = new SimpleStringProperty("C:\\Program Files (x86)\\Seamanship E-book Reader\\eBook Reader.exe");
	StringProperty enpReaderPath = new SimpleStringProperty("C:\\Program Files (x86)\\UKHO\\ADMIRALTY e-NP Reader\\UKHO.EnpReader.Shell.exe");
	StringProperty adpAdllPath = new SimpleStringProperty("C:\\Program Files (x86)\\ADMIRALTY Digital Publications\\ADMIRALTY Digital List of Lights\\Lights.exe");
	StringProperty adpAdrs1345Path = new SimpleStringProperty("C:\\Program Files (x86)\\ADMIRALTY Digital Publications\\ADMIRALTY Digital Radio Signals 1345\\adrs1345.exe");
	StringProperty adpAdrs2Path = new SimpleStringProperty("C:\\Program Files (x86)\\ADMIRALTY Digital Publications\\ADMIRALTY Digital Radio Signals 2\\adrs2.exe");
	StringProperty adpAdrs6Path = new SimpleStringProperty("C:\\Program Files (x86)\\ADMIRALTY Digital Publications\\ADMIRALTY Digital Radio Signals 6\\adrs6.exe");
	StringProperty adpAttPath = new SimpleStringProperty("C:\\Program Files (x86)\\ADMIRALTY Digital Publications\\ADMIRALTY TotalTide\\TotalTide.exe");
	StringProperty adpRunKeyWizPath = new SimpleStringProperty("C:\\Program Files (x86)\\Common Files\\CherSoft\\RunKeyWiz.exe");
	StringProperty adpUpdateWizPath = new SimpleStringProperty("C:\\Program Files (x86)\\Common Files\\CherSoft\\UpdateWiz.exe");

	public LocationsPreferencePage() {
		setCategory(Category.of("Locations", Group.of("General",
				Setting.of("Sybina Eraclea:", eracleaPath),
//				Setting.of("Ecdis Up:", ecdisUpPath),
//				Setting.of("Eu Mrv:", euMrvPath),
//				Setting.of("Seamanship E-book Reader:", imoReaderPath),
				Setting.of("ADMIRALTY e-NP Reader:", enpReaderPath),
				Setting.of("ADP - ADMIRALTY Digital List of Lights:", adpAdllPath),
				Setting.of("ADP - ADMIRALTY Digital Radio Signals 1,3,4,5:", adpAdrs1345Path),
				Setting.of("ADP - ADMIRALTY Digital Radio Signals 2:", adpAdrs2Path),
				Setting.of("ADP - ADMIRALTY Digital Radio Signals 6:", adpAdrs6Path),
				Setting.of("ADP - ADMIRALTY TotalTide:", adpAttPath),
				Setting.of("ADP - Data Updating Wizard:", adpRunKeyWizPath),
				Setting.of("ADP - Licence Key Wizard:", adpUpdateWizPath)
		)));

	}

	public StringProperty getEracleaPath() {
		return eracleaPath;
	}

	public void setEracleaPath(StringProperty eracleaPath) {
		this.eracleaPath = eracleaPath;
	}

	public StringProperty getEcdisUpPath() {
		return ecdisUpPath;
	}

	public void setEcdisUpPath(StringProperty ecdisUpPath) {
		this.ecdisUpPath = ecdisUpPath;
	}

	public StringProperty getEuMrvPath() {
		return euMrvPath;
	}

	public void setEuMrvPath(StringProperty euMrvPath) {
		this.euMrvPath = euMrvPath;
	}

	public StringProperty getImoReaderPath() {
		return imoReaderPath;
	}

	public void setImoReaderPath(StringProperty imoReaderPath) {
		this.imoReaderPath = imoReaderPath;
	}

	public StringProperty getEnpReaderPath() {
		return enpReaderPath;
	}

	public void setEnpReaderPath(StringProperty enpReaderPath) {
		this.enpReaderPath = enpReaderPath;
	}

	public StringProperty getAdpAdllPath() {
		return adpAdllPath;
	}

	public void setAdpAdllPath(StringProperty adpAdllPath) {
		this.adpAdllPath = adpAdllPath;
	}

	public StringProperty getAdpAdrs1345Path() {
		return adpAdrs1345Path;
	}

	public void setAdpAdrs1345Path(StringProperty adpAdrs1345Path) {
		this.adpAdrs1345Path = adpAdrs1345Path;
	}

	public StringProperty getAdpAdrs2Path() {
		return adpAdrs2Path;
	}

	public void setAdpAdrs2Path(StringProperty adpAdrs2Path) {
		this.adpAdrs2Path = adpAdrs2Path;
	}

	public StringProperty getAdpAdrs6Path() {
		return adpAdrs6Path;
	}

	public void setAdpAdrs6Path(StringProperty adpAdrs6Path) {
		this.adpAdrs6Path = adpAdrs6Path;
	}

	public StringProperty getAdpAttPath() {
		return adpAttPath;
	}

	public void setAdpAttPath(StringProperty adpAttPath) {
		this.adpAttPath = adpAttPath;
	}

	public StringProperty getAdpRunKeyWizPath() {
		return adpRunKeyWizPath;
	}

	public void setAdpRunKeyWizPath(StringProperty adpRunKeyWizPath) {
		this.adpRunKeyWizPath = adpRunKeyWizPath;
	}

	public StringProperty getAdpUpdateWizPath() {
		return adpUpdateWizPath;
	}

	public void setAdpUpdateWizPath(StringProperty adpUpdateWizPath) {
		this.adpUpdateWizPath = adpUpdateWizPath;
	}


}