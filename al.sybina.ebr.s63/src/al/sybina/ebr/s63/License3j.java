package al.sybina.ebr.s63;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import org.bouncycastle.openpgp.PGPException;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.Logger.Level;
import org.eclipse.fx.core.log.LoggerCreator;

import com.verhas.licensor.ExtendedLicense;

import al.sybina.ebr.s63.preferences.InstallPermitPreferencePage;
import al.sybina.ebr.s63.preferences.S63PreferencePage;

public class License3j {
	private static final String KEY_RING_PATH = "/pgppublickeyring.asc";
	private static final String MARPOL = "marpol";
	private static final String NAVIGATION_WARNINGS = "navigationWarnings";
	private static final String LOGBOOK1 = "logbook1";
	private static final String LOGBOOK2 = "logbook2";
	private static final String ECHART = "echart";
	private static final String SMARTLOAD = "smartload";
	
	
	
	private static Logger logger = LoggerCreator.createLogger(InstallPermitPreferencePage.class);
	
	// KEY DIGEST START
	public static byte[] digest = new byte[] { -15, 118, -34, 95, -45, 75, 119, 45, -114, -103, -95, 41, 23, 120, -59,
			-90, 48, 39, -3, 119, 105, 53, 88, -58, 84, -96, -48, 79, -80, 8, -78, 79, 110, 57, -46, -37, 66, -123, -14,
			35, 10, 71, 82, -91, 63, 53, -3, 94, 65, -5, 14, -82, 126, 104, 20, 109, 101, 49, -102, -50, 44, 60, 36,
			82 };
	
	
	private static String installPermit;


	public static boolean validateMarpolLicense(File licenseFile) throws FileNotFoundException, IOException, PGPException, ParseException {
		ExtendedLicense extendedLicense = validateLicenseFile(licenseFile);
		if(extendedLicense!=null) {
			String features = extendedLicense.getFeature("feature");
		    List<String> featuresList = Arrays.asList(features.split("\\s*,\\s*"));
		    if(featuresList.contains(MARPOL)) {
		    	return true;
		    } else {
		    	return false;
		    }
		} else {
			return false;
		}
	}
	
	public static boolean validateNavigationWarningsLicense(File licenseFile) throws FileNotFoundException, IOException, PGPException, ParseException {
		ExtendedLicense extendedLicense = validateLicenseFile(licenseFile);
		if(extendedLicense!=null) {
			String features = extendedLicense.getFeature("features");
		    List<String> featuresList = Arrays.asList(features.split("\\s*,\\s*"));
		    if(featuresList.contains(NAVIGATION_WARNINGS)) {
		    	return true;
		    } else {
		    	return false;
		    }
		} else {
			return false;
		}
	}
	
	public static boolean validateLogbook1License(File licenseFile) throws FileNotFoundException, IOException, PGPException, ParseException {
		ExtendedLicense extendedLicense = validateLicenseFile(licenseFile);
		if(extendedLicense!=null) {
			String features = extendedLicense.getFeature("features");
		    List<String> featuresList = Arrays.asList(features.split("\\s*,\\s*"));
		    if(featuresList.contains(LOGBOOK1)) {
		    	return true;
		    } else {
		    	return false;
		    }
		} else {
			return false;
		}
	}
	
	public static boolean validateLogbook2License(File licenseFile) throws FileNotFoundException, IOException, PGPException, ParseException {
		ExtendedLicense extendedLicense = validateLicenseFile(licenseFile);
		if(extendedLicense!=null) {
			String features = extendedLicense.getFeature("features");
		    List<String> featuresList = Arrays.asList(features.split("\\s*,\\s*"));
		    if(featuresList.contains(LOGBOOK1)) {
		    	return true;
		    } else {
		    	return false;
		    }
		} else {
			return false;
		}
	}
	
	public static boolean validateEchartLicense(File licenseFile) throws FileNotFoundException, IOException, PGPException, ParseException {
		ExtendedLicense extendedLicense = validateLicenseFile(licenseFile);
		if(extendedLicense!=null) {
			String features = extendedLicense.getFeature("features");
		    List<String> featuresList = Arrays.asList(features.split("\\s*,\\s*"));
		    if(featuresList.contains(ECHART)) {
		    	return true;
		    } else {
		    	return false;
		    }
		} else {
			return false;
		}
	}
	
	public static boolean validateSmartloadLicense(File licenseFile) throws FileNotFoundException, IOException, PGPException, ParseException {
		ExtendedLicense extendedLicense = validateLicenseFile(licenseFile);
		if(extendedLicense!=null) {
			String features = extendedLicense.getFeature("features");
		    List<String> featuresList = Arrays.asList(features.split("\\s*,\\s*"));
		    if(featuresList.contains(SMARTLOAD)) {
		    	return true;
		    } else {
		    	return false;
		    }
		} else {
			return false;
		}
	}
	
	public static ExtendedLicense validateLicenseFile(File licenseFile)
			throws FileNotFoundException, IOException, PGPException, ParseException {
		ExtendedLicense extendedLicense = loadLicense(new FileInputStream(licenseFile));
		String installPermit = extendedLicense.getFeature("installPermit");
		String email = extendedLicense.getFeature("email");
		boolean installPermitValid = S63Utils.validateInstallPermit(installPermit);
		if(logger.isEnabled(Level.DEBUG) && extendedLicense.isExpired()) logger.debug("License expired!");
		if(logger.isEnabled(Level.DEBUG) && !installPermitValid) logger.debug("Install Permit not valid!");
		if(!extendedLicense.isExpired() && installPermitValid) {
			License3j.installPermit = installPermit;
			return extendedLicense;
		} else {
			return null;
		}
	}
	
	private static ExtendedLicense loadLicense(FileInputStream licenseInputStream) throws IOException, PGPException {
		URL resource = S63PreferencePage.class.getResource(KEY_RING_PATH);
		logger.debug("Loading public key");
		try (final InputStream fileInputStream = resource.openStream()) {
			ExtendedLicense extendedLicense = new ExtendedLicense();
			extendedLicense.loadKeyRing(fileInputStream, digest);
			logger.debug("Loaded public key.");
			extendedLicense.setLicenseEncoded(licenseInputStream);
			logger.debug("Added encoded license.");
			return extendedLicense;
		}
	}

	public static String getInstallPermit() {
		return installPermit;
	}


}
