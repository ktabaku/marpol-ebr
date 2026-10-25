package al.sybina.ebr.server;


import java.io.File;
import java.io.IOException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import al.sybina.ebr.server.services.DerbyServerService;
import al.sybina.ebr.server.services.impl.DerbyServerServiceImpl;


/**
 * The activator class controls the plug-in life cycle
 */
public class Activator implements BundleActivator {
	// The plug-in ID
	public static final String PLUGIN_ID = "al.sybina.ebr.server";

	

	private static Activator activator;

	private static BundleContext context;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		activator = this;
		this.context = context;
	}

	public void stop(BundleContext context) throws Exception {

	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return activator;
	}

	public static BundleContext getContext() {
		return context;
	}
	public static Path getBundleBaseDirPath() {
		File file;
		try {
			file = FileLocator.getBundleFile(context.getBundle());
			return new Path(file.getCanonicalPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
}
