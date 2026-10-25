package al.sybina.ebr.logbook.core.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.compress.archivers.zip.ZipUtil;
import org.apache.commons.io.FilenameUtils;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.filter.ITableFilter;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import al.sybina.ebr.commons.utils.ArchiveUtils;
import al.sybina.ebr.commons.utils.AuditLogger;
import al.sybina.ebr.commons.utils.CryptoUtils;
import al.sybina.ebr.commons.utils.CryptoUtils.CryptoException;
import al.sybina.ebr.core.Activator;
import al.sybina.ebr.core.tools.PluginTools;

public class BackupDatabaseJob implements Job {

	public static final String DATABASE_PRODUCT_NAME_APACHE_DERBY = "Apache Derby";
    public static final String ARCHIVE_SUFFIX = ".zip";
    public static final String ENCRYPTED_SUFFIX = ".encrypted";

    private static final Logger LOGGER = LoggerCreator.createLogger(BackupDatabaseJob.class);
    private static final Logger auditLogger = LoggerCreator.createLogger(AuditLogger.class);
    
    protected String databaseName = "marpol-erb";
    
   
    
	public BackupDatabaseJob() {

	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap mergedJobDataMap = context.getMergedJobDataMap();
		try {
			execute(mergedJobDataMap);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw(new JobExecutionException(e));
		}
	}

	public void execute(JobDataMap mergedJobDataMap) throws Exception {
		String databaseProductName = null;
		Connection connection = null;
		String backupFile = "";
		String exportFormat = DerbyDatabaseBackupServiceImpl.STANDARD_FORMAT;
		try {
			String connectionUrl = (String) mergedJobDataMap.get(DerbyDatabaseBackupServiceImpl.CONNECTION_URL);
			String user = (String) mergedJobDataMap.get(DerbyDatabaseBackupServiceImpl.USER);
			String password = (String) mergedJobDataMap.get(DerbyDatabaseBackupServiceImpl.PASSWORD);
			String backupDir = (String) mergedJobDataMap.get(DerbyDatabaseBackupServiceImpl.OUTPUT_DIR);
			exportFormat = (String) mergedJobDataMap.get(DerbyDatabaseBackupServiceImpl.EXPORT_FORMAT);
			backupFile = createBackupURL(backupDir, exportFormat);
			DataSource dataSource = getDataSource(connectionUrl, user, password);
			connection = dataSource.getConnection();
			databaseProductName = connection.getMetaData().getDatabaseProductName();
		} catch (SQLException e) {
			LOGGER.error("Cannot read databaseProductName from Connection!"
					+ DerbyDatabaseBackupServiceImpl.class.getCanonicalName() + " cannot continue!");
			throw e;
		}
		if (DATABASE_PRODUCT_NAME_APACHE_DERBY.equals(databaseProductName)) {
			if(exportFormat.equals(DerbyDatabaseBackupServiceImpl.STANDARD_FORMAT)) {
				backupDerbyDatabase(connection, backupFile);
			} else if(exportFormat.equals(DerbyDatabaseBackupServiceImpl.XML_FORMAT)) {
				fullDatabaseExport(connection, backupFile);
			}
		} else {
			throw new RuntimeException(
					"Scheduled database backup for unsupported database type " + databaseProductName);
		}
		auditLogger.info("user: system scheduler performed database backup/export.");
	}



    /**
     * Backup the On-Line Derby database. This temporarily locks the db in
     * readonly mode
     * 
     * Invokes SYSCS_BACKUP_DATABASE and dumps the database to the temporary
     * directory Use {@link ZipUtil#pack(File, File)} to zip the directory
     * Deletes the temporary directory
     * @throws SQLException 
     * @throws IOException 
     * 
     * @see #createBackupURL(String)
     */
    private void backupDerbyDatabase(Connection connection, String backupFilePath) throws SQLException, IOException {
        CallableStatement cs = null;
        try {
            cs = connection.prepareCall("CALL SYSCS_UTIL.SYSCS_BACKUP_DATABASE(?)");
            cs.setString(1, backupFilePath);
            cs.execute();
        } catch (SQLException e) {
            LOGGER.error("Cannot perform database backup!", e);
            throw(e);
        } finally {
            try {
                cs.close();
            } catch (SQLException e) {
                LOGGER.error("Error closing backup connection ", e);
                throw(e);
            }
        }

        File backupFile = new File(backupFilePath);
        // zip the contents and delete the dir
        
        // delete the backup directory that we just zipped
        try {
    		File outputZipFile = new File(backupFile + ARCHIVE_SUFFIX);
    		ArchiveUtils.zipFile(backupFile, outputZipFile);
        	
        	org.apache.commons.io.FileUtils.deleteDirectory(backupFile);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw(e);
        }

        LOGGER.info("Backed up database to " + backupFilePath + ARCHIVE_SUFFIX);
    }
    
    
	
	public void fullDatabaseExport(Connection connection, String backupFilePath)
			throws ClassNotFoundException, DatabaseUnitException, DataSetException, IOException, SQLException, CryptoException {
		IDatabaseConnection iDbConnection = new DatabaseConnection(connection);
		
		File backupFile = new File(backupFilePath);
		FileOutputStream fos = new FileOutputStream(backupFile);
		ITableFilter filter = new DatabaseSequenceFilter(iDbConnection);
		IDataSet dataset = new FilteredDataSet(filter, iDbConnection.createDataSet());
		FlatXmlDataSet.write(dataset, fos);
		fos.close();
		
		File outputZipFile = new File(backupFile + ARCHIVE_SUFFIX);
		ArchiveUtils.zipFile(backupFile, outputZipFile);

		File outputEncryptedFile = new File(backupFile + ENCRYPTED_SUFFIX);
        CryptoUtils.encrypt(DerbyDatabaseBackupServiceImpl.ENCRYPTION_KEY, outputZipFile, outputEncryptedFile);
            
        org.apache.commons.io.FileUtils.forceDelete(backupFile);
        org.apache.commons.io.FileUtils.forceDelete(outputZipFile);
        
        LOGGER.info("Backed up database to " + backupFile + ARCHIVE_SUFFIX);
	}

	public void restore(JobDataMap mergedJobDataMap) throws Exception {
		String databaseProductName = null;
		Connection connection = null;
		String restoreFilePath = "";
		String connectionUrl = "";
		String user = "";
		String password = "";
		String restoreFormat = DerbyDatabaseBackupServiceImpl.STANDARD_FORMAT;
		try {
			connectionUrl = (String) mergedJobDataMap.get(DerbyDatabaseBackupServiceImpl.CONNECTION_URL);
			user = (String) mergedJobDataMap.get(DerbyDatabaseBackupServiceImpl.USER);
			password = (String) mergedJobDataMap.get(DerbyDatabaseBackupServiceImpl.PASSWORD);
			restoreFilePath = (String) mergedJobDataMap.get(DerbyDatabaseBackupServiceImpl.RESTORE_FILE);
			restoreFormat = (String) mergedJobDataMap.get(DerbyDatabaseBackupServiceImpl.RESTORE_FORMAT);
			DataSource dataSource = getDataSource(connectionUrl, user, password);
			connection = dataSource.getConnection();
			databaseProductName = connection.getMetaData().getDatabaseProductName();
		} catch (SQLException e) {
			LOGGER.error("Cannot read databaseProductName from Connection!"
					+ DerbyDatabaseBackupServiceImpl.class.getCanonicalName() + " cannot continue!" + e);
			throw(e);
		}
		if (DATABASE_PRODUCT_NAME_APACHE_DERBY.equals(databaseProductName)) {
			if(restoreFormat.equals(DerbyDatabaseBackupServiceImpl.STANDARD_FORMAT)) {
				File restoreFile = new File(restoreFilePath);
				ArchiveUtils.extractFolder(restoreFile, restoreFile.getParent());
		        String nsURL1 = connectionUrl+";shutdown=true";
		        Properties connectionProps = new Properties();
		        connectionProps.setProperty("user", user);
		        connectionProps.setProperty("password", password);
				try {
					DriverManager.getConnection(nsURL1, connectionProps);
				} catch (SQLNonTransientConnectionException ex) {
					LOGGER.debug(ex.getMessage());
				}
				org.apache.commons.io.FileUtils.forceDelete(new File(PluginTools.getFileLocalToRuntime(al.sybina.ebr.logbook.domain.Activator.RUNTIME_LOCATION_FOLDER_NAME+"/db")));
				PluginTools.copyDirectory(Activator.getBundle(), restoreFile.getParentFile()+"/"+FilenameUtils.removeExtension(restoreFile.getName()).replace(" ", "%20"), new URI("file:///"+PluginTools.getFileLocalToRuntime(al.sybina.ebr.logbook.domain.Activator.RUNTIME_LOCATION_FOLDER_NAME).replaceAll("\\\\", "/").replace(" ", "%20")), null);
		        org.apache.commons.io.FileUtils.forceDelete(new File(restoreFile.getParentFile()+"/"+FilenameUtils.removeExtension(restoreFile.getName())));
			} else if(restoreFormat.equals(DerbyDatabaseBackupServiceImpl.XML_FORMAT)) {
				fullDatabaseRestore(connection, restoreFilePath);
			}
		} else {
			throw new RuntimeException(
					"Scheduled database backup for unsupported database type " + databaseProductName);
		}
		
	}
	

	public static void fullDatabaseRestore(Connection connection, String encyptedRestoreFilePath)
			throws ClassNotFoundException, DatabaseUnitException, IOException, SQLException, CryptoException {
		
		File encryptedFile = new File(encyptedRestoreFilePath);
		File decryptedZipFile = new File(FilenameUtils.removeExtension(encyptedRestoreFilePath) + ARCHIVE_SUFFIX);
		CryptoUtils.decrypt(DerbyDatabaseBackupServiceImpl.ENCRYPTION_KEY, encryptedFile, decryptedZipFile);
		
		ArchiveUtils.extractFolder(decryptedZipFile, encryptedFile.getParent());
		
		File xmlFile = new File(FilenameUtils.removeExtension(encyptedRestoreFilePath));
		
		IDataSet dataSet = new FlatXmlDataSet(xmlFile, true);
		IDatabaseConnection iDbConnection = new DatabaseConnection(connection);
		iDbConnection.getConfig().setProperty(DatabaseConfig.FEATURE_ALLOW_EMPTY_FIELDS, Boolean.TRUE);
		DatabaseOperation.CLEAN_INSERT.execute(iDbConnection, dataSet);
		
        org.apache.commons.io.FileUtils.forceDelete(decryptedZipFile);
        org.apache.commons.io.FileUtils.forceDelete(xmlFile);
	}

	
	
	
	
	
	
	
	public static DataSource getDataSource(String databaseConnectionString, String user, String password) throws SQLException {
		String cleanURI = databaseConnectionString.substring(5).replaceAll("\\\\", "/");
		cleanURI = cleanURI.replace(" ", "%20");
		URI uri = URI.create(cleanURI);
		org.apache.derby.jdbc.ClientDataSource ds = new org.apache.derby.jdbc.ClientDataSource();
		ds.setServerName(uri.getHost());
		ds.setPortNumber(uri.getPort());
		ds.setDatabaseName(uri.getPath().replaceFirst("/", ""));

		if (user != null)
			ds.setUser(user);
		if (password != null)
			ds.setPassword(password);
		
		return ds;
	}
	
	/**
	 * Gets the URL (directory/file) of the backupPath. Adds as prefixes the last
	 * leaf of backup's location parent directory + {@link #databaseName} If the
	 * backupPath does not have a parent, it uses the host name from
	 * {@link InetAddress#getLocalHost()}
	 * 
	 * @param backupPath
	 *            the parent directory for the backup
	 * @return the backup url to be used by the backup procedure
	 * @throws UnknownHostException
	 */
	private String createBackupURL(final String backupPath, final String exportFormat) {
		java.text.SimpleDateFormat todaysDate = new java.text.SimpleDateFormat("yyyyMMdd-HHmmss");
		String backupURL = backupPath + "/" + databaseName + "-" + exportFormat + "-"
				+ todaysDate.format((java.util.Calendar.getInstance()).getTime());
		if(exportFormat.equals(DerbyDatabaseBackupServiceImpl.XML_FORMAT)) backupURL = backupURL+".xml";
		return backupURL;
	}


}