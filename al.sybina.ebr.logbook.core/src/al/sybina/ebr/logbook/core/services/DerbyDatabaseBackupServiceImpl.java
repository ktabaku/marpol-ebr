package al.sybina.ebr.logbook.core.services;

import al.sybina.ebr.commons.utils.DatabaseCredentials;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.net.InetAddress;
import java.sql.SQLNonTransientConnectionException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.derby.drda.NetworkServerControl;
import org.eclipse.core.runtime.Platform;
//import org.apache.commons.io.FileUtils;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;
import org.osgi.service.event.EventConstants;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdScheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.listeners.SchedulerListenerSupport;

import com.opcoach.e4.preferences.E4PreferencePage;

import al.sybina.ebr.logbook.core.Activator;
import al.sybina.ebr.core.tools.PluginTools;

/**
 * @author mpostelnicu Provides built-in backup services. Defaults to the
 *         database location derby.system.home. Currently works only for Derby.
 *         Runs 9PM daily (good backup time for both EST and CET)
 */
@Creatable
@Singleton
public class DerbyDatabaseBackupServiceImpl implements DerbyDatabaseBackupService {

    private static final Logger LOGGER = LoggerCreator.createLogger(DerbyDatabaseBackupServiceImpl.class);


    public static final String CONNECTION_URL = "connectionUrl";
    public static final String USER = "user";
    public static final String PASSWORD = "password";
    public static final String OUTPUT_DIR = "outputDir";
    public static final String RESTORE_FILE = "restoreFile";
	public static final String EXPORT_FORMAT = "exportFormat";
	public static final String RESTORE_FORMAT = "restoreFormat";
	public static final String STANDARD_FORMAT = "standard";
	public static final String XML_FORMAT = "xml";
	public static final String ENCRYPTION_KEY = "F)J@NcRfUjXn2r5u";


	
	
	private EventAdmin eventAdmin;

	public void setEventAdmin(EventAdmin eventAdmin) {
		this.eventAdmin = eventAdmin;
	}
	public void unsetEventAdmin() {
		this.eventAdmin = null;
	}
	
    private SchedulerFactory schedulerFactory;
    
	protected void activate() {
		String serverIp = Platform.getPreferencesService().getString(E4PreferencePage.PLUGIN_ID, "mainInstallationIp", "localhost", null);
		String serverPort = Platform.getPreferencesService().getString(E4PreferencePage.PLUGIN_ID, "mainInstallationPort", "1527", null);
    	String serverDb = Platform.getPreferencesService().getString(E4PreferencePage.PLUGIN_ID, "mainInstallationDb", PluginTools.getFileLocalToRuntime(al.sybina.ebr.logbook.domain.Activator.RUNTIME_LOCATION_FOLDER_NAME + "/db"), null);

    	boolean dbServerPresent = false;
    	try {
			NetworkServerControl server = new NetworkServerControl(InetAddress.getByName(serverIp), Integer.valueOf(serverPort));
		    for(int i=0; i<1; i++) {
		        try {
		            server.ping();
		            dbServerPresent = true;
		            break;
		        } catch (Exception e) {
		        }
		        Thread.sleep(100);
		    }
		} catch (Exception e1) {
			LOGGER.error(e1.getMessage(), e1);
		}
    	if(dbServerPresent) {
			Map<String, String> connectionAndJobData = new HashMap<String, String>();
			connectionAndJobData.put(CONNECTION_URL, "jdbc:derby://" + serverIp + ":" + serverPort + "/" + serverDb + ";create=false;dataEncryption=true;bootPassword=" + DatabaseCredentials.bootPassword());
			connectionAndJobData.put(USER, DatabaseCredentials.user());
			connectionAndJobData.put(PASSWORD, DatabaseCredentials.password());
			try {
				schedulerFactory = createStandardSchedulerFactory(connectionAndJobData);
				Scheduler scd = getStandardScheduler();
				scd.getListenerManager().addSchedulerListener(new SchedulerListenerSupport() {
					@Override
					public void schedulerError(String msg, SchedulerException cause) {
						if(cause.getUnderlyingException() instanceof SQLNonTransientConnectionException) {
							LOGGER.error(msg, cause.getUnderlyingException());
							Map<String, Object> eventMap = new HashMap<String, Object>();
							eventMap.put(EventConstants.EVENT_TOPIC, al.sybina.ebr.logbook.domain.Activator.DATABASE_CONNECTION_LOST);
							eventMap.put(IEventBroker.DATA, new HashMap<String, String>());
							eventAdmin.postEvent(new Event(al.sybina.ebr.logbook.domain.Activator.DATABASE_CONNECTION_LOST, eventMap));
						}
					}
				});
				connectionAndJobData.put(OUTPUT_DIR, createBackupURL());
		        if(!serverIp.equals("localhost") && !serverIp.equals("127.0.0.1")) {
		        	connectionAndJobData.put(EXPORT_FORMAT, XML_FORMAT);
		        } else {
		        	connectionAndJobData.put(EXPORT_FORMAT, STANDARD_FORMAT);
		        }
				scheduleBackupDatabase(scd, connectionAndJobData);
			} catch (SchedulerException e) {
				LOGGER.error("Cannot schedule backup database.", e);
			}
    	}
	}



	protected void deactivate() {

    }
    
    public void runBackupDatabaseJob(String backupDir) {
    	String serverIp = Platform.getPreferencesService().getString(E4PreferencePage.PLUGIN_ID, "mainInstallationIp", "localhost", null);
    	String serverPort = Platform.getPreferencesService().getString(E4PreferencePage.PLUGIN_ID, "mainInstallationPort", "1527", null);
    	String serverDb = Platform.getPreferencesService().getString(E4PreferencePage.PLUGIN_ID, "mainInstallationDb", PluginTools.getFileLocalToRuntime(al.sybina.ebr.logbook.domain.Activator.RUNTIME_LOCATION_FOLDER_NAME + "/db"), null);

		Map<String, String> jobData = new HashMap<String, String>();
		jobData.put(CONNECTION_URL, "jdbc:derby://" + serverIp + ":" + serverPort + "/" + serverDb);
		jobData.put(USER, DatabaseCredentials.user());
		jobData.put(PASSWORD, DatabaseCredentials.password());
		jobData.put(OUTPUT_DIR, backupDir);
		jobData.put(EXPORT_FORMAT, STANDARD_FORMAT);
		try {
			Scheduler scheduler = getStandardScheduler();
			scheduler.start();
			JobKey jobKey = JobKey.jobKey("backupDatabase", "jobDetailGroup");
			JobDetail job = newJob(BackupDatabaseJob.class)
					.usingJobData(new JobDataMap(jobData))
					.withIdentity(jobKey)
					.storeDurably(true)
					.build();
			scheduler.addJob(job, true);
			scheduler.triggerJob(jobKey);
		} catch (SchedulerException e) {
			LOGGER.error("Cannot schedule backup database.", e);
		}
    }
    private void scheduleBackupDatabase(Scheduler scheduler, Map<String, String> jobData) throws SchedulerException {
		// define the job and tie it to our HelloJob class
		JobDetail job = newJob(BackupDatabaseJob.class)
				.usingJobData(new JobDataMap(jobData))
				.withIdentity("dailyBackupDatabase", "jobDetailGroup")
				.requestRecovery(true)
				.build();

		// Trigger the job to run now, and then repeat every day at 23:00 seconds
		Trigger  trigger = newTrigger()
				.withIdentity("cronTrigger23UTC", "triggerGroup")
				.withSchedule(cronSchedule("0 0 23 * * ?").inTimeZone(TimeZone.getTimeZone("UTC")).withMisfireHandlingInstructionFireAndProceed())
				.forJob(job)
				.build();

		scheduler.start();
		
		if (!scheduler.checkExists(job.getKey()))	{
			scheduler.scheduleJob(job, trigger);
		}
		else {
			job = job.getJobBuilder().storeDurably(true).build();
			scheduler.addJob(job, true);
//			scheduler.scheduleJob(trigger);
		}
    }
    
	private SchedulerFactory createStandardSchedulerFactory(Map<String, String> connectionData) throws SchedulerException {
		Properties prop = new Properties();

		// //RMI configuration to make the client to connect to the Quartz server
		// prop.put("org.quartz.scheduler.rmi.export", "true");
		// prop.put("org.quartz.scheduler.rmi.createRegistry", "true");
		// prop.put("org.quartz.scheduler.rmi.registryHost", "localhost");
		// prop.put("org.quartz.scheduler.rmi.registryPort", "1099");
		prop.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
		prop.put("org.quartz.threadPool.threadCount", "2");

		// Quartz Server Properties
		prop.put("quartz.scheduler.instanceName", "ServerScheduler");
		prop.put("org.quartz.scheduler.instanceId", "AUTO");
		prop.put("org.quartz.scheduler.skipUpdateCheck", "true");
		prop.put("org.quartz.scheduler.instanceId", "CLUSTERED");
		prop.put("org.quartz.scheduler.jobFactory.class", "org.quartz.simpl.SimpleJobFactory");
		prop.put("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
		prop.put("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.StdJDBCDelegate");
		prop.put("org.quartz.jobStore.dataSource", "quartzDataSource");
		prop.put("org.quartz.jobStore.tablePrefix", "QRTZ_");
		prop.put("org.quartz.jobStore.isClustered", "false");

		// Database properties
		prop.put("org.quartz.dataSource.quartzDataSource.driver", "org.apache.derby.jdbc.ClientDriver");
		prop.put("org.quartz.dataSource.quartzDataSource.URL", connectionData.get(CONNECTION_URL));
		prop.put("org.quartz.dataSource.quartzDataSource.user", connectionData.get(USER));
		prop.put("org.quartz.dataSource.quartzDataSource.password", connectionData.get(PASSWORD));
		prop.put("org.quartz.dataSource.quartzDataSource.maxConnections", "2");

		/* Configure standard scheduler */
		SchedulerFactory stdSchedulerFactory = new StdSchedulerFactory(prop);
		// SchedulerFactory schedulerFactory = new StdSchedulerFactory();

		return stdSchedulerFactory;
	}
    
    public Scheduler getStandardScheduler() throws SchedulerException {
		// Quartz loads the job classes stored in the database by name. Its class load helper keeps the
		// context class loader of the thread that creates the scheduler, so make that the loader of this
		// bundle: the Quartz bundle itself cannot see BackupDatabaseJob.
		Thread thread = Thread.currentThread();
		ClassLoader contextClassLoader = thread.getContextClassLoader();
		thread.setContextClassLoader(BackupDatabaseJob.class.getClassLoader());
		try {
			return schedulerFactory.getScheduler();
		} finally {
			thread.setContextClassLoader(contextClassLoader);
		}
	}
	/**
	 * Use backup.home system variable, if exists, as homedir for backups If
	 * backup.home does not exist try using derby.system.home If that is also null,
	 * use user.dir
	 * 
	 * @return the backupURL
	 */
	private String createBackupURL() {
		String backupHomeString = PluginTools.getFileLocalToRuntime(al.sybina.ebr.logbook.domain.Activator.BACK_UP_FOLDER_NAME);
		if (backupHomeString == null) {
			backupHomeString = System.getProperty("derby.system.home") != null ? System.getProperty("derby.system.home")
					: System.getProperty("user.dir");
		}
		return backupHomeString;
	}
}