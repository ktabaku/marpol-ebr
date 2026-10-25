package al.sybina.ebr.server.services.impl;


import java.net.InetAddress;

import org.apache.derby.drda.NetworkServerControl;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.ecf.core.ContainerConnectException;
import org.eclipse.ecf.core.ContainerCreateException;
import org.eclipse.ecf.core.sharedobject.SharedObjectAddException;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;
import org.osgi.framework.FrameworkUtil;

import com.opcoach.e4.preferences.E4PreferencePage;

import al.sybina.ebr.server.services.DerbyServerService;
import al.sybina.ebr.core.tools.PluginTools;


@Creatable("DerbyServerService")
public class DerbyServerServiceImpl implements DerbyServerService {
	
	public static final String RUNTIME_LOCATION_FOLDER_NAME = "logbookdb";
	
	private static final Logger logger = LoggerCreator.createLogger(DerbyServerServiceImpl.class);

	private NetworkServerControl server;
	
	public void activate() throws ContainerCreateException, SharedObjectAddException, ContainerConnectException {
		PluginTools.installPluginDataInRuntimeLocation(FrameworkUtil.getBundle(this.getClass()), RUNTIME_LOCATION_FOLDER_NAME);
		String serverPort = Platform.getPreferencesService().getString(E4PreferencePage.PLUGIN_ID, "mainInstallationPort", "1527", null);
		try {
			logger.info("Using database from " + PluginTools.getFileLocalToRuntime(RUNTIME_LOCATION_FOLDER_NAME + "/db"));
			server = new NetworkServerControl(InetAddress.getByName("0.0.0.0"), Integer.valueOf(serverPort));
			server.start(null);
		    while(true) {
		        try {
		            server.ping();
		            break;
		        } catch (Exception e) {
		        }
		        Thread.sleep(100);
		    }
			logger.info("Database server started and is available from http://localhost:" + serverPort);
		} catch (Exception e) {
			logger.error("Cannot start derby server!", e);
		}
	}

    public void deactivate() {
    	try {
			server.shutdown();
		} catch (Exception e) {
			logger.error("Cannot shutdown derby server!", e);
		}
    }

}
