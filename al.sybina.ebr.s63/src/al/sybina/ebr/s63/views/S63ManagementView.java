package al.sybina.ebr.s63.views;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.eclipse.fx.core.di.LocalInstance;
import org.eclipse.osgi.service.datalocation.Location;

import al.sybina.ebr.s63.Activator;
import al.sybina.ebr.s63.S63Constants;
import al.sybina.ebr.s63.preferences.S63PreferencePage;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

public class S63ManagementView {

	public static final String ID = "al.sybina.ebr.shared.views.S63ManagementView";
	public static final String SSE05_CERT_FILE_NOT_AVAILABLE = "SSE 05 - SA Digital Certificate (X509) file is not available. A valid certificate can be obtained from the IHO website or your data supplier";
	public static final String SSE06_SA_SIGNED_DATA_SERVER_CERT_INVALID = "SSE 06 - The SA Signed Data Server Certificate is invalid. The SA may have issued a new public key or the ENC may originate from another service. A new SA public key can be obtained from the IHO website or from your data supplier";
	public static final String SSE08_FILE_INCORRECT_FORMAT_MSG = "SSE 08 - SA Digital Certificate (X509) file incorrect format. A valid certificate can be obtained from the IHO website or your data supplier";
	public static final String SSE09_ENC_SIGNATURE_INVALID = "SSE 09 - WARNING ENC SIGNATURE INVALID";
	public static final String SSE11_CELL_PERMIT_NOT_FOUND = "SSE 11 - Cell permit not found";
	public static final String SSE12_CELL_PERMIT_FORMAT_INCORRECT = "SSE 12 - Cell permit format is incorrect";
	public static final String SSE13_CELL_PERMIT_INVALID = "SSE 13 Cell Permit is invalid (checksum is incorrect) or the Cell Permit is for a different system";
	public static final String SSE15_SUBSCRIPTION_SERVICE_EXPIRED = "SSE 15 - Subscription service has expired. Please contact your data supplier to renew the subscription licence.";
	public static final String SSE16_ENC_CRC_VALUE_INCORRECT = "SSE 16 - ENC CRC value is incorrect. Contact your data supplier as ENC(s) may be corrupted or missing data.";
	public static final String SSE20_SUBSCRIPTION_SERVICE_NEAR_EXPIRE = "SSE 20 - Subscription service will expire in less than 30 days. Please contact your data supplier to renew the subscription licence.";
	public static final String SSE21_NO_VALID_CELL_PERMIT="SSE 21 - Decryption failed no valid cell permit found. Permits may be for another system or new permits may be required, please contact your supplier to obtain a new licence";
	public static final String SSE22_CERT_EXPIRED_MSG = "SSE 22 - SA Digital Certificate (X509) has expired. A new SA public key can be obtained from the IHO website or from your data supplier.";
	public static final String SSE24_INVALID_ENC_SIGNATURE_FORMAT = "SSE 24 - INVALID ENC SIGNATURE FORMAT";
	public static final String SSE26_ENC_NOT_AUTHENTICATED_BY_SA = "SSE 26 - This ENC is not authenticated by the IHO acting as the Scheme Administrator";
	
	@Inject
	@Named(E4Workbench.INSTANCE_LOCATION)
	private Location instanceLocation;
	@Inject
	private S63PreferencePage s63PreferencePage;
	
	private BorderPane borderPane;
	private FXMLLoader loader;

	@PostConstruct
	public void createPartControl(BorderPane borderPane, @LocalInstance FXMLLoader loader) {
		this.borderPane = borderPane;
		this.loader = loader;
		loader.setLocation(getClass().getResource("S63ManagementView.fxml"));
		try {
			borderPane.setCenter(loader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
		borderPane.getStylesheets().add(getClass().getResource("lisStyles.css").toExternalForm());
		
	}

	@Inject
	@Optional
	private void subscribePreferencesUpdated(
			@UIEventTopic(S63Constants.CHARTS_PREFERENCES_CHANGED_S63) String msg) {
		if (loader != null && borderPane != null) {
			loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("S63ManagementView.fxml"));
			try {
				borderPane.getChildren().clear();
				borderPane.setCenter(loader.load());
				S63ManagementViewController s63ManagementViewController = ((S63ManagementViewController) loader.getController());
				s63ManagementViewController.setRootChart(s63PreferencePage.getChartsPathsStr().getValue().getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
