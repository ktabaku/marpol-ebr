package al.sybina.ebr.logbook.core;

import al.sybina.ebr.commons.utils.DatabaseCredentials;
import java.net.InetAddress;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.derby.drda.NetworkServerControl;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.di.InjectorFactory;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.internal.jpa.EntityManagerImpl;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.opcoach.e4.preferences.E4PreferencePage;

import al.sybina.ebr.commons.utils.EnvironmentalEntityManagerFactory;
import al.sybina.ebr.core.tools.PluginTools;

public class Activator implements BundleActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "al.sybina.ebr.logbook.core";
	public BundleContext context;
	public static Activator plugin;
	private static EntityManagerFactory emf;
    private static EntityManager em;
    
    public void start(BundleContext bundleContext) {
    	plugin = this;
    	this.context = bundleContext;
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
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	if(dbServerPresent) {
			try {
				DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
			} catch (SQLException e) {
				e.printStackTrace();
			}
	
			InjectorFactory.getDefault().addBinding(EntityManager.class).implementedBy(EntityManagerImpl.class);
	
			Map propertiesMap = new HashMap<String, Object>();
			propertiesMap.put(PersistenceUnitProperties.CLASSLOADER, al.sybina.ebr.logbook.domain.Activator.class.getClassLoader());
			propertiesMap.put("javax.persistence.jdbc.url", "jdbc:derby://" + serverIp + ":" + serverPort + "/" + serverDb + ";create=false;dataEncryption=true;bootPassword=" + DatabaseCredentials.bootPassword());
			propertiesMap.put("javax.persistence.jdbc.user", DatabaseCredentials.user());
			propertiesMap.put("javax.persistence.jdbc.password", DatabaseCredentials.password());
			propertiesMap.put("eclipselink.ddl-generation", "none");
			EnvironmentalEntityManagerFactory.setEnvironmentVariables(propertiesMap);
			emf = EnvironmentalEntityManagerFactory.createEntityManagerFactory("al.sybina.ebr.logbook.e4jpa", propertiesMap);
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
	
	public BundleContext getContext() {
		return context;
	}
	
	public static EntityManager getEntityManager() {
		return em;
	}
	

}
