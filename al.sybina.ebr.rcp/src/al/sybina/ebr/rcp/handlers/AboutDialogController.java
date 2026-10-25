package al.sybina.ebr.rcp.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.inject.Inject;

import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.tools.resources.WorkbenchResourceProvider;
import org.eclipse.e4.tools.services.IResourceProviderService;
import org.eclipse.fx.ui.services.resources.ImageProvider;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import al.sybina.ebr.rcp.Activator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class AboutDialogController implements Initializable {
	

	
	@Inject
	private IResourceProviderService resourcesService;
	@Inject
	private ImageProvider imageProvider;

	private Stage aboutStage;
	
	@FXML private ImageView logoImageView;
	@FXML private Label versionLabel;
	@FXML private Label dateLabel;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			Image verticalLogoImage = imageProvider.getImage(((WorkbenchResourceProvider)resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_VERTICAL_LOGO));
			logoImageView.setImage(verticalLogoImage);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// version.properties is produced by the Maven build (resource filtering of the version folder).
		// When running from the IDE it is missing or unfiltered, so fall back to the bundle version.
		Properties prop = loadVersionProperties();
		String version = filledValue(prop, "releasedVersion.version");
		String buildDate = filledValue(prop, "build.timestamp");
		versionLabel.setText(version != null ? version : bundleVersion());
		dateLabel.setText(buildDate != null ? buildDate : "dev build");
	}

	private static Properties loadVersionProperties() {
		Properties prop = new Properties();
		try (InputStream in = AboutDialogController.class.getResourceAsStream("/version.properties")) {
			if (in != null) {
				prop.load(in);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}

	/** The property value, or null when it is missing or still an unfiltered ${...} placeholder. */
	private static String filledValue(Properties prop, String key) {
		String value = prop.getProperty(key);
		if (value == null || value.trim().isEmpty() || value.contains("${")) {
			return null;
		}
		return value.trim();
	}

	private static String bundleVersion() {
		Bundle bundle = FrameworkUtil.getBundle(AboutDialogController.class);
		return bundle != null ? bundle.getVersion().toString() : "";
	}


	@FXML
	private void handleClose() {
		aboutStage.close();
	}


	public void setDialogStage(Stage aboutStage) {
		this.aboutStage = aboutStage;
	}

}
