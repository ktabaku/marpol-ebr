package al.sybina.ebr.s63;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;
import org.osgi.framework.FrameworkUtil;

import ca.gc.chs.ihossv1.Ihossv1CellPermit;
import ca.gc.chs.ihossv1.Ihossv1CellPermitChecksumException;
import ca.gc.chs.ihossv1.Ihossv1EncryptionSupport;
import ca.gc.chs.ihossv1.Ihossv1HardwareId;
import ca.gc.chs.ihossv1.Ihossv1InstallPermit;
import ca.gc.chs.ihossv1.Ihossv1MetaPermitFileEntry;
import ca.gc.chs.ihossv1.Ihossv1MiscellaneousSupport;
import ca.gc.chs.ihossv1.Ihossv1PublicKey;
import ca.gc.chs.ihossv1.Ihossv1SystemIdentificationKey;
import ca.gc.chs.ihossv1.Ihossv1UserPermit;
import ca.gc.chs.ihossv1.Ihossv1UserPermitChecksumException;
import al.sybina.ebr.s63.preferences.InstallPermitPreferencePage;
import al.sybina.ebr.s63.preferences.S63PreferencePage;
import al.sybina.ebr.core.tools.Log;
import al.sybina.ebr.core.tools.PluginTools;

public class S63Utils {
	private static final String PUBLIC_KEY_ALIAS_EBR_WEB = "ebr_web_cert";
	private static final String PRIVATE_KEY_ALIAS = "ebr_system_key";
    private static final String PRIVATE_KEY_PASS = "ebr_secret_pass123";
    private static final String KEY_STORE_PASS = "ebr_secret_pass123";
    private static final String KEY_STORE_TYPE = "JKS";
    private static final String KEY_STORE_PATH = "/ebr_system.jks";

    public static String sencExtensionPrefix = "senc";
    
    private static Logger logger = LoggerCreator.createLogger(InstallPermitPreferencePage.class);
    
    static byte[] encryptMachineUuid(String machineUuidHexString) throws Exception {
    	byte[] machineUuidByteArray = machineUuidHexString.getBytes();
    	byte[] signedMachineUuidByteArray = encryptMachineUuid(machineUuidByteArray);
    	return signedMachineUuidByteArray;
    }
    private static byte[] encryptMachineUuid(byte[] machineUuidByteArray) throws Exception {
    	URL resource = S63PreferencePage.class.getResource(KEY_STORE_PATH);
		final KeyStore keyStore = loadKeyStore(resource);
        final PrivateKey privateKey = (PrivateKey)keyStore.getKey(PRIVATE_KEY_ALIAS, PRIVATE_KEY_PASS.toCharArray());
        final X509Certificate webPubCert = (X509Certificate)keyStore.getCertificate(PUBLIC_KEY_ALIAS_EBR_WEB);
        
        Cipher cipher1 = Cipher.getInstance("RSA");
        cipher1.init(Cipher.ENCRYPT_MODE, webPubCert.getPublicKey());
        byte[] signedFirstPass = cipher1.doFinal(machineUuidByteArray);
        
        Cipher cipher2 = Cipher.getInstance("RSA");
        cipher2.init(Cipher.ENCRYPT_MODE, privateKey);
        int chunkSize =  ((RSAPrivateKey)privateKey).getModulus().bitLength() / 8 - 11; // 2048 / 8 - 11(padding) = 245
        int encSize = (int) (Math.ceil(signedFirstPass.length/new Double(chunkSize))*((RSAPrivateKey)privateKey).getModulus().bitLength() / 8); // 2048 / 8 = 256
        int idx = 0;
        ByteBuffer buf = ByteBuffer.allocate(encSize);
        while (idx < signedFirstPass.length) {
            int len = Math.min(signedFirstPass.length-idx, chunkSize);
            byte[] encChunk = cipher2.doFinal(signedFirstPass, idx, len);
            buf.put(encChunk);
            idx += len;
        }

        // fully encrypted data     
        byte[] signedMachineUuidByteArray = buf.array();
        return signedMachineUuidByteArray;
	}
    private static KeyStore loadKeyStore(URL resource) throws Exception {
		try (final InputStream fileInputStream = resource.openStream()) {
			final KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE);
			keyStore.load(fileInputStream, KEY_STORE_PASS.toCharArray());
			return keyStore;
		}
	}
//    public static void main(String [] args) {
//    	String result = "";
//    	for(int i=0; i<100; i++) {
//    		try {
//    			String machineId = getMachineId();
//				if(!machineId.equals(result)) {
//					System.out.println("machineId: "+machineId+"\nresult: "+result);
//					result = machineId;
//				}
//			} catch (UnsupportedEncodingException | SocketException | UnknownHostException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//    	}
//    }
    public static String getMachineId() throws UnsupportedEncodingException, SocketException, UnknownHostException, InterruptedException, IOException  {
//		HardwareBinder hb = new HardwareBinder();
//		hb.ignoreHostName();
//		hb.ignoreArchitecture();
//		UUID machineUuid = hb.getMachineId();
    	logger.debug("Getting serial.");
		UUID machineUuid = getSerialNumber();
		logger.debug("Getted serial.");
		String machineUuidString = Ihossv1MiscellaneousSupport.asHexString(machineUuid);
		return machineUuidString;
	}
    public static UUID getSerialNumber() throws IOException, InterruptedException {
		ProcessBuilder pb = new ProcessBuilder("wmic", "baseboard", "get", "serialnumber");
		Process process = pb.start();
		process.waitFor();
		String serialNumber = "";
		try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
			for (String line = br.readLine(); line != null; line = br.readLine()) {
				if (line.length() < 1 || line.startsWith("SerialNumber")) {
					continue;
				}
				serialNumber = line;
				break;
			}
		}
		return UUID.nameUUIDFromBytes(serialNumber.getBytes(Charset.forName("UTF-8")));
    }
	public static SecretKey loadSecretKey() {
		return Ihossv1EncryptionSupport.blowfishSecretKey("e91ee28ae2ab6f79ebf6");
	}
	public static boolean validateUserPermit(String userPermit) {
		try {
			Ihossv1UserPermit anIhossv1UserPermit = new Ihossv1UserPermit(userPermit);
			anIhossv1UserPermit.verifyIhossv1UserPermitChecksum();
			return true;
		} catch (Ihossv1UserPermitChecksumException e) {
			return false;
		}
	}
	
	public static boolean validateInstallPermit(String installPermit) {
		String serverAssignedMachineId;
		try {
			serverAssignedMachineId = getServerAssignedMachineId(installPermit);
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InterruptedException | IOException e) {
			return false;
		}
		return serverAssignedMachineId.length()==10 && isHexNumber(serverAssignedMachineId);
	}
	public static String getServerAssignedMachineId(String installPermit) throws UnsupportedEncodingException, SocketException, UnknownHostException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InterruptedException, IOException {
		String machineUuidString = getMachineId();
		Ihossv1SystemIdentificationKey theIhossv1SystemIdentificationKey = new Ihossv1SystemIdentificationKey(machineUuidString);
		Ihossv1InstallPermit theIhossv1InstallPermit = new Ihossv1InstallPermit(null, installPermit);
		theIhossv1InstallPermit.decrypt(theIhossv1SystemIdentificationKey);
		return theIhossv1InstallPermit.getString();
	}
	private static boolean isHexNumber (String cadena) {
		  try {
		    Long.parseLong(cadena, 16);
		    return true;
		  }
		  catch (NumberFormatException ex) {
		    // Error handling code...
		    return false;
		  }
	}
	
	
	public static Ihossv1CellPermit verifyIhossv1CellPermit(Ihossv1CellPermit anIhossv1CellPermit, Ihossv1HardwareId anIhossv1HardwareId) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, Ihossv1CellPermitChecksumException {
		
		anIhossv1CellPermit.decryptIhossv1CellPermitChecksum(anIhossv1HardwareId);
		anIhossv1CellPermit.decryptCurrentIhossv1CellKey(anIhossv1HardwareId);
		anIhossv1CellPermit.decryptNextIhossv1CellKey(anIhossv1HardwareId);
		anIhossv1CellPermit.verifyIhossv1CellPermitChecksum();
		
		return anIhossv1CellPermit;
	}
	
	public static File generateFingerPrintFile() throws Exception {
		String absoluteFilePath = PluginTools.getFileLocalToRuntime("s63");
		File s63Dir = new File(absoluteFilePath);
		s63Dir.mkdir();
		String machineUuidHexString = S63Utils.getMachineId();
    	byte[] signedMachineUuidByteArray = S63Utils.encryptMachineUuid(machineUuidHexString);
		File fingerprintFile = new File(absoluteFilePath, "ebr_fpr_"+new Date().getTime()+".efpr");
		try {
			fingerprintFile.createNewFile();
		} catch (IOException e) {
			Log.warning(FrameworkUtil.getBundle(S63Utils.class), e.getMessage());
		}
    	FileOutputStream fprfos = new FileOutputStream(fingerprintFile);
    	fprfos.write(signedMachineUuidByteArray);
    	fprfos.close();
        return fingerprintFile;
	}
	
	public static File generateChartPermitFile(Ihossv1MetaPermitFileEntry encIhossv1MetaPermitFileEntry) throws Exception {
		String absoluteFilePath = PluginTools.getFileLocalToRuntime("s63/s63charts/"+encIhossv1MetaPermitFileEntry.getDataServerId());
		Files.createDirectories(Paths.get(absoluteFilePath));
		File chartPermitFile = new File(absoluteFilePath, encIhossv1MetaPermitFileEntry.getIhossv1CellPermit().getIhossv1CellName().getCellDataFileName()+".s63");
		try {
			chartPermitFile.createNewFile();
		} catch (IOException e) {
			Log.warning(FrameworkUtil.getBundle(S63Utils.class), e.getMessage());
		}
    	FileOutputStream chpfos = new FileOutputStream(chartPermitFile);
    	chpfos.write(("cellpermit:"+encIhossv1MetaPermitFileEntry.getString()).getBytes());
    	chpfos.close();
        return chartPermitFile;
	}
	
	public static List<Ihossv1MetaPermitFileEntry> getAllInstalledPermits() {
		String absoluteFilePath = PluginTools.getFileLocalToRuntime("s63/s63charts");
		try {
			Files.createDirectories(Paths.get(absoluteFilePath));
		} catch (IOException e1) {
			Log.warning(FrameworkUtil.getBundle(S63Utils.class), e1.getMessage());
		}
		File chartPermitsFolder = new File(absoluteFilePath);
		List<Ihossv1MetaPermitFileEntry> allInstalledPermitsList = new ArrayList<Ihossv1MetaPermitFileEntry>();
		for(File dataServerFolder : chartPermitsFolder.listFiles()) {
			try {
				allInstalledPermitsList.addAll(getInstalledPermits(dataServerFolder));
			} catch (FileNotFoundException | ParseException | NullPointerException e) {
				Log.warning(FrameworkUtil.getBundle(S63Utils.class), e.getMessage());
			}
		}
		return allInstalledPermitsList;
	}
	
	public static List<Ihossv1MetaPermitFileEntry> getInstalledPermits(File dataServerFolder) throws FileNotFoundException, ParseException {
		List<Ihossv1MetaPermitFileEntry> installedPermitsList = new ArrayList<Ihossv1MetaPermitFileEntry>();
		for(File file : dataServerFolder.listFiles()) {
			Ihossv1MetaPermitFileEntry aIhossv1MetaPermitFileEntry = getIhossv1MetaPermitFileEntryFromFile(file);
			installedPermitsList.add(aIhossv1MetaPermitFileEntry);
		}
		return installedPermitsList;
	}
	public static Ihossv1MetaPermitFileEntry getIhossv1MetaPermitFileEntryFromFile(File file) throws FileNotFoundException, ParseException {
		Ihossv1MetaPermitFileEntry aIhossv1MetaPermitFileEntry = null;
		FileInputStream fis = new FileInputStream(file);
		Scanner scanner = new Scanner(fis);
		if(scanner.hasNextLine()){
		    String line = scanner.nextLine();
		    if(line.startsWith("cellpermit:")) {
		    	String cellPermitString = line.substring("cellpermit:".length());
		    	aIhossv1MetaPermitFileEntry = new Ihossv1MetaPermitFileEntry(cellPermitString);
		    }
		}
		scanner.close();
		return aIhossv1MetaPermitFileEntry;
	}
	
	public static File getInstalledPermitFile(Ihossv1MetaPermitFileEntry aIhossv1MetaPermitFileEntry) {
		String chartPermitsFolderPath = PluginTools.getFileLocalToRuntime("s63/s63charts/");
		String filePath = chartPermitsFolderPath + aIhossv1MetaPermitFileEntry.getDataServerId() + "/" + aIhossv1MetaPermitFileEntry.getIhossv1CellPermit().getIhossv1CellName().getCellDataFileName() + ".s63";
		File chartPermitFile = new File(filePath);
		if(chartPermitFile.exists()) {
			return chartPermitFile;
		}
		return null;
	}
	public static File getInstalledPermitFile(String dataServerId, String cellDataFileName) {
		String chartPermitsFolderPath = PluginTools.getFileLocalToRuntime("s63/s63charts/");
		String filePath = chartPermitsFolderPath + dataServerId + "/" + cellDataFileName + ".s63";
		File chartPermitFile = new File(filePath);
		if(chartPermitFile.exists()) {
			return chartPermitFile;
		}
		return null;
	}
	public static void updatePermitFile(File installedPermitFile, String comt, String s63FilePath) throws IOException {
		S63ChartUpdatesBean s63ChartUpdatesBean = new S63ChartUpdatesBean(installedPermitFile);
		if(s63ChartUpdatesBean.getCellbase()==null && comt.split(",").length==5) {
			s63ChartUpdatesBean.setCellbase(s63FilePath + ";" + comt);
		} else {
			s63ChartUpdatesBean.getCellupdates().add(s63FilePath + ";" + comt);
		}
		s63ChartUpdatesBean.save();
	}
	public static File getInstalledChartsFolder() throws IOException {
		String chartsFolderPath = PluginTools.getFileLocalToRuntime("s63/s63SENC/");
		Files.createDirectories(Paths.get(chartsFolderPath));
		File chartsFolder = new File(chartsFolderPath);
		return chartsFolder;
	}
	public static List<S63SerialEncRecord> parseSerialEnc(File serialEnc) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		List<S63SerialEncRecord> records = new ArrayList<>();
		Scanner scanner;
		try {
			scanner = new Scanner(serialEnc);
		} catch (FileNotFoundException e) {
			return records;
		}
		scanner.useDelimiter("0x0B0D0A");
		try {
			while (scanner.hasNext()) {
				String record = scanner.next();
				S63SerialEncRecord objectRecord = new S63SerialEncRecord();
				objectRecord.setDataServerId(record.substring(0, 2));
				objectRecord.setWeekOfIssue(record.substring(2, 12).trim());
				objectRecord.setDateOfPublication(dateFormat.parse(record.substring(12, 20).trim()));
				objectRecord.setCdType(record.substring(20, 30).trim());
				objectRecord.setFormatVersion(NumberFormat.getInstance().parse(record.substring(30, 35).trim()).doubleValue());
				objectRecord.setExchangeSetNumber(record.substring(35, 41).trim());
				records.add(objectRecord);
			}
		} catch (ParseException e) {
			return records;
		} finally {
			scanner.close();
		}
		return records;
	}
	
	
	
	public static List<PublicKeyWithProperties> getIhossv1PublicKeyFromCertPaths(List<String> installedCertsPaths) {
		List<PublicKeyWithProperties> ihossviPublicKeyList = new ArrayList<>();
		PublicKeyWithProperties publicKeyWithProperties = null;
		try {
			for(String certPath : installedCertsPaths) {
	        	if(certPath.endsWith("IHO.PUB")) {
	        		publicKeyWithProperties = new PublicKeyWithProperties(new Ihossv1PublicKey(new File(certPath)), true);
	        	} else if(certPath.endsWith(".PUB")) {
	        		publicKeyWithProperties = new PublicKeyWithProperties(new Ihossv1PublicKey(new File(certPath)), false);
	        	} else if(certPath.endsWith("IHO.CRT")) {
	        		FileInputStream in = new FileInputStream(new File(certPath));
					CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
		        	X509Certificate cert = (X509Certificate)certFactory.generateCertificate(in);
		        	publicKeyWithProperties = new PublicKeyWithProperties(new Ihossv1PublicKey((DSAPublicKey)cert.getPublicKey()), true);
	        	} else if(certPath.endsWith(".CRT")) {
	        		FileInputStream in = new FileInputStream(new File(certPath));
					CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
		        	X509Certificate cert = (X509Certificate)certFactory.generateCertificate(in);
		        	publicKeyWithProperties = new PublicKeyWithProperties(new Ihossv1PublicKey((DSAPublicKey)cert.getPublicKey()), false);
	        	}
	        	if(publicKeyWithProperties!=null)ihossviPublicKeyList.add(publicKeyWithProperties);
	        }
		} catch (InvalidKeySpecException | IOException | CertificateException e) {
			
		}
		Collections.sort(ihossviPublicKeyList);
		return ihossviPublicKeyList;
	}
	
	private static List<String> getFiles(String preferenceEntry) {
		List<String> files = new ArrayList<String>();
		if(preferenceEntry != "") {
			String[] items = parseString(preferenceEntry);
			if(items.length > 0) {
				for(String item : items) {
					files.add(item);
				}
			}
		}
		Collections.sort(files);
		return files;
	}
	private static String[] parseString(String stringList) {
		String SEPARATOR_TOKEN = ";";
		String[] decodedArray;
		if(stringList.contains(SEPARATOR_TOKEN)) {
			decodedArray = stringList.split(SEPARATOR_TOKEN);
		} else {
			decodedArray = new String[0];
		}
		return decodedArray;
	}
}
