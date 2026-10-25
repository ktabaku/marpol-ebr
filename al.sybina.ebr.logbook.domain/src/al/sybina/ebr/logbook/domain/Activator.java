package al.sybina.ebr.logbook.domain;

import al.sybina.ebr.commons.utils.DatabaseCredentials;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.derby.drda.NetworkServerControl;
import org.apache.derby.jdbc.ClientDriver;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.di.InjectorFactory;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.internal.jpa.EntityManagerImpl;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.gs.fw.common.mithra.MithraManager;
import com.gs.fw.common.mithra.MithraManagerProvider;
import com.opcoach.e4.preferences.E4PreferencePage;

import al.sybina.ebr.commons.utils.EnvironmentalEntityManagerFactory;
import al.sybina.ebr.core.tools.PluginTools;

public class Activator implements BundleActivator {
	
	public static final String PLUGIN_ID = "al.sybina.ebr.logbook.domain";
	public static final String RUNTIME_LOCATION_FOLDER_NAME = "logbookdb";
	public static final String BACK_UP_FOLDER_NAME = "backup";
	public static final String DATABASE_CONNECTION_LOST = "DATABASE_CONNECTION_LOST";
	
	private static final Logger logger = LoggerCreator.createLogger(Activator.class);
	
	private EntityManagerFactory emf;
	private EntityManager em;

	
	
	public void start(BundleContext bundleContext) {
    	String serverIp = Platform.getPreferencesService().getString(E4PreferencePage.PLUGIN_ID, "mainInstallationIp", "localhost", null);
    	String serverPort = Platform.getPreferencesService().getString(E4PreferencePage.PLUGIN_ID, "mainInstallationPort", "1527", null);
    	String serverDb = Platform.getPreferencesService().getString(E4PreferencePage.PLUGIN_ID, "mainInstallationDb", PluginTools.getFileLocalToRuntime(RUNTIME_LOCATION_FOLDER_NAME + "/db"), null);

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
			PluginTools.createPluginDataInRuntimeLocation(BACK_UP_FOLDER_NAME);
	
			InjectorFactory.getDefault().addBinding(EntityManager.class).implementedBy(EntityManagerImpl.class);
	
			Map propertiesMap = new HashMap<String, Object>();
			propertiesMap.put(PersistenceUnitProperties.CLASSLOADER, getClass().getClassLoader());
			propertiesMap.put("javax.persistence.jdbc.url", "jdbc:derby://" + serverIp + ":" + serverPort + "/" + serverDb + ";create=false;dataEncryption=true;bootPassword=" + DatabaseCredentials.bootPassword());
			propertiesMap.put("javax.persistence.jdbc.user", DatabaseCredentials.user());
			propertiesMap.put("javax.persistence.jdbc.password", DatabaseCredentials.password());
	
			EnvironmentalEntityManagerFactory.setEnvironmentVariables(propertiesMap);
			emf = EnvironmentalEntityManagerFactory.createEntityManagerFactory("al.sybina.ebr.logbook.e4jpa", propertiesMap);
			em = emf.createEntityManager();
	//				IEclipseContext context = EclipseContextFactory.create();
	//				//ContextInjectionFactory.inject(emf, context);
	//				ContextInjectionFactory.inject(em, context);
	//				context.get(EntityManagerImpl.class);
	//				context.get(EntityManager.class);
	//				System.setProperty("derby.drda.startNetworkServer", "true");
					
			try {
				initReladomo();
			} catch (Exception e) {
				logger.error("Cannot start Reladomo!", e);
			}
    	}
    }

	@Override
	public void stop(BundleContext context) throws Exception {
		if(em!=null) em.close();
		if(emf!=null) emf.close();
		em = null;
		emf = null;
	}
	
	
	
    private void initReladomo() throws Exception {
        MithraManager mithraManager = MithraManagerProvider.getMithraManager();
        mithraManager.setTransactionTimeout(60 * 1000);
        loadReladomoXML("reladomo/LogBookRuntimeConfiguration.xml");
    }

    private void loadReladomoXML(String fileName) throws Exception {
		Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
		URL fileURL = bundle.getEntry(fileName);
        InputStream stream = fileURL.openStream();
        if (stream == null) {
            throw new Exception("Failed to locate " + fileName + " in classpath");
        }
        ClientDriver a;
        MithraManagerProvider.getMithraManager().readConfiguration(stream);
        stream.close();
    }

}
