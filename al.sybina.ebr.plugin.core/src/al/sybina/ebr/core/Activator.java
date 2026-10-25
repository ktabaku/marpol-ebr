package al.sybina.ebr.core;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import al.sybina.ebr.core.tools.PluginTools;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator implements BundleActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "al.sybina.ebr.core";

	// The shared instance
	static Activator activator;

	private static Bundle bundle;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		activator = this;
		bundle = context.getBundle();
		System.setProperty("logdir", PluginTools.getFileLocalToRuntime(""));
	}

	@Override
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

	/**
	 * @return the bundle
	 */
	public static Bundle getBundle() {
		return bundle;
	}


}
