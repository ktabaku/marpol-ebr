package al.sybina.ebr.s63.preferences;

import java.io.File;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.StringField;
import com.dlsc.formsfx.model.validators.CustomValidator;
import com.dlsc.preferencesfx.formsfx.view.controls.SimpleChooserGenerateFingerprintControl;
import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.Group;
import com.dlsc.preferencesfx.model.Setting;
import com.opcoach.e4.preferences.E4PreferencePage;

import al.sybina.ebr.s63.License3j;
import al.sybina.ebr.s63.S63Utils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

@Creatable
@Singleton
public class InstallPermitPreferencePage extends E4PreferencePage {
	private static Logger logger = LoggerCreator.createLogger(InstallPermitPreferencePage.class);
	
	private StringProperty userPermit = new SimpleStringProperty("");
	private ObjectProperty<File> fingerPrintFileObj = new SimpleObjectProperty<File>();
	private ObjectProperty<File> licenseFileObj = new SimpleObjectProperty<File>();
	
	public InstallPermitPreferencePage() {
		CustomValidator<String> userPermitValidator = CustomValidator.forPredicate(new Predicate<String>() {
			@Override
			public boolean test(String userPermit) {
				if(userPermit!=null && userPermit.equals("")) {
					logger.debug("UserPermit empty!");
					return false;
				}
				try {
					return S63Utils.validateUserPermit(userPermit);
				} catch(Exception ex) {
					logger.debug("Error validating userPermit." + ex.getMessage());
					return false;
				}
			}
		}, "User Permit not valid!");
		CustomValidator<File> licenseValidator = CustomValidator.forPredicate(new Predicate<File>() {
			@Override
			public boolean test(File licenseFile) {
				if(licenseFile != null && licenseFile.getPath().equals("")) {
					logger.debug("LicenseFile path empty!");
					return false;
				}
				try {
					return License3j.validateLicenseFile(licenseFile)!=null;
				} catch(Exception ex) {
					logger.debug("Error validating license." + ex.getMessage());
					return false;
				}
			}
		}, "License not valid!");
		
//		FileField fingerPrintFileField = FileField.ofFileType(fingerPrintFileObj).render(new GenerateFingerprintFileControl());
//		FileField licenseFileField = FileField.ofFileType(licenseFileObj).render(new LicenseFileControl());
		setCategory(Category.of("License", Group.of("General",
			Setting.of("User Permit:", userPermit).validate(userPermitValidator),
//			Setting.of("Machine Identifier:", fingerPrintFileField, fingerPrintFileObj),
//			Setting.of("License:", licenseFileField, licenseFileObj).validate(licenseValidator),
			of("Machine Identifier", fingerPrintFileObj, "Browse", "Create System Identifier File...", null, false),
			Setting.of("License:", licenseFileObj, false)//.validate(licenseValidator)
		)));
	}

	public StringProperty getUserPermit() {
		return userPermit;
	}

	public ObjectProperty<File> getFingerPrintFileObj() {
		return fingerPrintFileObj;
	}

	public ObjectProperty<File> getLicenseFileObj() {
		return licenseFileObj;
	}
	
	public static Setting of(String description, ObjectProperty<File> fileProperty, String buttonText, String generateButtonText,
			File initialDirectory, boolean directory) {
		StringProperty stringProperty = new SimpleStringProperty();
		stringProperty.bindBidirectional(fileProperty, new StringConverter<File>() {
			@Override
			public String toString(File file) {
				if (Objects.isNull(file)) {
					return "";
				}
				return file.getAbsolutePath();
			}

			@Override
			public File fromString(String value) {
				return new File(value);
			}
		});

		StringField stringField = Field.ofStringType(stringProperty).label(description).render(
				new SimpleChooserGenerateFingerprintControl(buttonText, generateButtonText, initialDirectory, directory) {
					@Override
					public void onGenerate(TextField editableField) {
						Alert alert = new Alert(AlertType.INFORMATION);
				        alert.setTitle("Info");
				        alert.setHeaderText("Generate fingerprint file?");
						String message = "To obtain an Install Permit, you must generate a unique System Identification File.\n"
								+ "This file il also known as a \"fingerprint\" file.\n"
								+ "The fingerprint file contains information to uniquely identify this computer.\n"
								+ "\n"
								+ "After creating this file, you need it to generate your InstallPermit at the sybina.al web site.\n"
								+ "Proceed to create Fingerprint file?";
				        alert.setContentText(message);
				        Optional<ButtonType> optButtonType = alert.showAndWait();
				        if(optButtonType.isPresent() && optButtonType.get().equals(ButtonType.OK)) {
							File fingerPrintFile;
							try {
								fingerPrintFile = S63Utils.generateFingerPrintFile();
								editableField.setText(fingerPrintFile.getAbsolutePath());
							} catch (Exception e1) {
								e1.printStackTrace();
							}
				        }
					}
				}
		);
		
		return new Setting<StringField, StringProperty>(description, stringField, stringProperty);
	}
}
