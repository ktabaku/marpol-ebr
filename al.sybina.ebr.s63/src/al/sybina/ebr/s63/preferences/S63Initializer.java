package al.sybina.ebr.s63.preferences;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.openpgp.PGPException;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;

import com.verhas.licensor.ExtendedLicense;

import ca.gc.chs.ihossv1.Ihossv1InstallPermit;
import ca.gc.chs.ihossv1.Ihossv1ManufacturerKey;
import ca.gc.chs.ihossv1.Ihossv1MiscellaneousSupport;
import ca.gc.chs.ihossv1.Ihossv1SystemIdentificationKey;
import ca.gc.chs.ihossv1.Ihossv1UserPermit;
import ca.gc.chs.ihossv1.Ihossv1UserPermitChecksumException;
import al.sybina.ebr.s63.S63Utils;

/**
 * Class used to initialize default preference values.
 */
//@Creatable
//@Singleton
public class S63Initializer {//extends AbstractPreferenceInitializer {

	private static Logger logger = LoggerCreator.createLogger(S63Initializer.class);
//	
	private static String mkey = "10121";
//	private static String mid = "10";
//	/*
//	 * (non-Javadoc)
//	 *
//	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
//	 */
//	@Override
//	public void initializeDefaultPreferences() {
//		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
//		
//		String certPaths = store.getString(S63Constants.CHARTS_S63_CERT_FILES_PATHS_STR);
//		if(certPaths==null || certPaths.isEmpty()) {
//			String certPath = PluginTools.getFileLocalToRuntime("s63/IHO.CRT")+";"+PluginTools.getFileLocalToRuntime("s63/IHO.PUB");
//			store.setValue(S63Constants.CHARTS_S63_CERT_FILES_PATHS_STR, certPath);
//		}
//		
//		String userPermitOdyssey = store.getString(S63Constants.CHARTS_S63_USER_PERMIT_STR);
//		if(userPermitOdyssey==null || userPermitOdyssey.isEmpty()) {
//			userPermitOdyssey = "66B5CBFDF7E4139D5B6086C23130";
//			store.setValue(S63Constants.CHARTS_S63_USER_PERMIT_STR, userPermitOdyssey);
//		}
//		
//		String fingerPrintFilePath = store.getString(S63Constants.CHARTS_S63_SYSTEM_IDENTIFICATION_STR);
//		if(fingerPrintFilePath==null || fingerPrintFilePath.isEmpty()) {
//			try {
//				File fingerPrintFile = S63Utils.generateFingerPrintFile();
//				fingerPrintFilePath = fingerPrintFile.getAbsolutePath();
//				store.setValue(S63Constants.CHARTS_S63_SYSTEM_IDENTIFICATION_STR, fingerPrintFilePath);
//			} catch (Exception e) {
//				logger.error(e.getMessage(), e);
//			}
//		}
//		
//		String theInstallPermitString = store.getString(S63Constants.CHARTS_S63_INSTALL_PERMIT_STR);
//		if (theInstallPermitString == null || theInstallPermitString.isEmpty()) {
//			try {
//				String machineUuidHexString = S63Utils.getMachineId();
//				theInstallPermitString = getInstallPermit(userPermitOdyssey, machineUuidHexString);
//				store.setValue(S63Constants.CHARTS_S63_INSTALL_PERMIT_STR, theInstallPermitString);
//			} catch (UnsupportedEncodingException | SocketException | UnknownHostException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | Ihossv1UserPermitChecksumException e) {
//				logger.error(e.getMessage(), e);
//			}
//		}
//		
//		
//		String chartsPathString = store.getString(S63Constants.CHARTS_PATHS_STR);
//		if (chartsPathString == null || chartsPathString.isEmpty()) {
//			store.setValue(S63Constants.CHARTS_PATHS_STR, PluginTools.getFileLocalToRuntime("s63/s63charts"));
//		}
//		
//		
//		String rootChartsPathString = store.getString(S63Constants.ROOT_CHARTS_PATH);
//		if (rootChartsPathString == null || rootChartsPathString.isEmpty()) {
//			store.setValue(S63Constants.ROOT_CHARTS_PATH, "charts");
//		}
//		
//		
//		try {
//			((ScopedPreferenceStore)store).save();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	
//	
	public static void main(String [] args) {
		String userPermitOdyssey = "66B5CBFDF7E4139D5B6086C23130";
		try {
			String installPermit = getInstallPermit(userPermitOdyssey, S63Utils.getMachineId());
			System.out.println("Userpermit: " + userPermitOdyssey + " mkey: " + mkey + " installPermit: " + installPermit);
			String license = generateLicense(installPermit);
			FileWriter fw = new FileWriter(System.getProperty("user.home")+"/license-"+ installPermit +".lic");
            fw.write(license);
            fw.close();
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException
				| Ihossv1UserPermitChecksumException | InterruptedException | IOException | NoSuchAlgorithmException | NoSuchProviderException | SignatureException | PGPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static String generateLicense(String theInstallPermitString) throws NoSuchAlgorithmException, NoSuchProviderException, SignatureException, IOException, PGPException {
		URL resource = Paths.get("C:/repositories/ebr/al.sybina.ebr.s63/pgpkeyring.asc").toUri().toURL();
		String license = null;
		try (final InputStream fileInputStream = resource.openStream()) {
			ExtendedLicense extendedLicense = new ExtendedLicense();
			extendedLicense.loadKey(fileInputStream, "licenseKey");
			extendedLicense.setFeature("issueDate", new Date());
			extendedLicense.setFeature("email", "kadritabaku@gmail.com");
			extendedLicense.setFeature("installPermit", theInstallPermitString);
			extendedLicense.setFeature("features", "marpol, navigationWarnings");
			Date maxValidUntilDate = Date.from((LocalDate.now().plusYears(2)).atStartOfDay(ZoneId.of( "Europe/Rome")).toInstant());
			extendedLicense.setExpiry(maxValidUntilDate);
			license = extendedLicense.encodeLicense("lic3nc3k3y");
		}
		return license;
	}
	public static String getInstallPermit(String userPermitString, String machineUuidHexString) throws UnsupportedEncodingException, SocketException, UnknownHostException, Ihossv1UserPermitChecksumException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Ihossv1UserPermit theIhossv1UserPermit = new Ihossv1UserPermit(userPermitString);
		theIhossv1UserPermit.decryptIhossv1HardwareId(ihossv1ManufacturerKey());
		String randomlyAssignedHardwareId = theIhossv1UserPermit.getIhossv1HardwareId().getString();
		
		Ihossv1InstallPermit theIhossv1InstallPermit = new Ihossv1InstallPermit(randomlyAssignedHardwareId, null);
		Ihossv1SystemIdentificationKey theIhossv1SystemIdentificationKey = new Ihossv1SystemIdentificationKey(machineUuidHexString);
		try {
			theIhossv1InstallPermit.encrypt(theIhossv1SystemIdentificationKey);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException e) {
			e.printStackTrace();
			return null;
		}
		return theIhossv1InstallPermit.getEncryptedString();
	}
	private static Ihossv1ManufacturerKey ihossv1ManufacturerKey() {
		String mkeyHexString = Ihossv1MiscellaneousSupport.hexString(mkey.getBytes());
		return new Ihossv1ManufacturerKey(mkeyHexString);
	}
}
