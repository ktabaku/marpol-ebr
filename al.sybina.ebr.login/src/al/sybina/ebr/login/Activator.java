package al.sybina.ebr.login;

import al.sybina.ebr.commons.utils.DatabaseCredentials;
import java.net.InetAddress;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.derby.drda.NetworkServerControl;
import org.apache.derby.jdbc.EmbeddedDriver;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.di.InjectorFactory;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.internal.jpa.EntityManagerImpl;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.oceanpro.core.security.entities.User;
import com.opcoach.e4.preferences.E4PreferencePage;

import al.sybina.ebr.commons.utils.EnvironmentalEntityManagerFactory;
import al.sybina.ebr.core.tools.PluginTools;

public class Activator implements BundleActivator{
	public static final String PLUGIN_ID = "al.sybina.ebr.login";
	
	public static final String SERVER_SHUTDOWN_STARTED_EVENT_TOPIC = "SERVER_SHUTDOWN_STARTED_EVENT_TOPIC";
	public static final String LOGIN_NEEDED_EVENT_TOPIC = "LOGIN_NEEDED_EVENT_TOPIC";
	public static final String LOGGED_IN_EVENT_TOPIC = "LOGGED_IN_EVENT_TOPIC";
	public static final String USER_CHANGED_EVENT_TOPIC = "USER_CHANGED_EVENT_TOPIC";
	public static final String USER_IDS= "USER_IDS";
	
	public static final String RUNTIME_LOCATION_FOLDER_NAME = "logbookdb";
	public static final String DATABASE_CONNECTION_LOST = "DATABASE_CONNECTION_LOST";
	
	public static final String PERMISSION_CREATE_USER = "createUser";
	public static final String PERMISSION_CREATE_ADMIN_USER = "createAdminUser";
	public static final String PERMISSION_INSERT_LOGS = "insertLogs";
	public static final String PERMISSION_MASTER_SIGN = "masterSign";
	public static final String PERMISSION_EDIT_LOGS = "editLogs";
	public static final String PERMISSION_MASTER_EDIT_LOGS = "masterEditLogs";

	
	
	
	public static BundleContext context;
	public static Activator plugin;
	private static EntityManagerFactory emf;
    private static EntityManager em;
    
    private static boolean dbServerPresent = false;
    
    public void start(BundleContext bundleContext) {
    	plugin = this;
    	this.context = bundleContext;
    	String serverIp = Platform.getPreferencesService().getString(E4PreferencePage.PLUGIN_ID, "mainInstallationIp", "localhost", null);
    	String serverPort = Platform.getPreferencesService().getString(E4PreferencePage.PLUGIN_ID, "mainInstallationPort", "1527", null);
    	String serverDb = Platform.getPreferencesService().getString(E4PreferencePage.PLUGIN_ID, "mainInstallationDb", PluginTools.getFileLocalToRuntime(al.sybina.ebr.login.Activator.RUNTIME_LOCATION_FOLDER_NAME + "/db"), null);
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
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	if(dbServerPresent) {
	    	try {
	    		org.apache.derby.jdbc.ClientDriver clientDriver = (org.apache.derby.jdbc.ClientDriver)Class.forName("org.apache.derby.jdbc.ClientDriver", true, User.class.getClassLoader()).newInstance();
				DriverManager.registerDriver(clientDriver);
				DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
			} catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				e.printStackTrace();
			}
	
			InjectorFactory.getDefault().addBinding(EntityManager.class).implementedBy(EntityManagerImpl.class);
	
			Map propertiesMap = new HashMap<String, Object>();
			propertiesMap.put(PersistenceUnitProperties.CLASSLOADER, User.class.getClassLoader());
			propertiesMap.put("javax.persistence.jdbc.url", "jdbc:derby://" + serverIp + ":" + serverPort + "/" + serverDb + ";create=false;dataEncryption=true;bootPassword=" + DatabaseCredentials.bootPassword());
			propertiesMap.put("javax.persistence.jdbc.user", DatabaseCredentials.user());
			propertiesMap.put("javax.persistence.jdbc.password", DatabaseCredentials.password());
			propertiesMap.put("eclipselink.ddl-generation", "none");
			EnvironmentalEntityManagerFactory.setEnvironmentVariables(propertiesMap);
			emf = EnvironmentalEntityManagerFactory.createEntityManagerFactory("al.sybina.ebr.commons.e4jpa", propertiesMap);
			em = emf.createEntityManager();
			// IEclipseContext context = EclipseContextFactory.create();
			// //ContextInjectionFactory.inject(emf, context);
			// ContextInjectionFactory.inject(em, context);
			// context.get(EntityManagerImpl.class);
			// context.get(EntityManager.class);
    	}
    }

	@Override
	public void stop(BundleContext context) throws Exception {
		if(em!=null) em.close();
		if(emf!=null) emf.close();
		em = null;
		emf = null;
	}
	
	public static BundleContext getContext() {
		return context;
	}
	
	public static EntityManager getEntityManager() {
		return em;
	}

	public static boolean isDbServerPresent() {
		return dbServerPresent;
	}

}
