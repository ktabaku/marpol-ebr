package al.sybina.ebr.s63;


import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

//import com.opcoach.e4.preferences.ScopedPreferenceStore;

import al.sybina.ebr.core.tools.PluginTools;


/**
 * The activator class controls the plug-in life cycle
 */
public class Activator implements BundleActivator {

	// The plug-in ID : it's the package name
	public static final String PLUGIN_ID = "al.sybina.ebr.s63";

	// The shared instance
	public static Activator plugin;
	
	public BundleContext context;
	
//	private volatile ScopedPreferenceStore  preferenceStore;
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
		plugin = this;
		this.context = context;
		PluginTools.installPluginDataInRuntimeLocation(FrameworkUtil.getBundle(this.getClass()), "s63");
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
	
//	public ScopedPreferenceStore getPreferenceStore() {
//        if (null == preferenceStore) {
//            synchronized (this) {
//                if (null == preferenceStore) { 
//                    preferenceStore = new  ScopedPreferenceStore(InstanceScope.INSTANCE, PLUGIN_ID); 
//                } 
//            } 
//        } 
//        return preferenceStore;
//    }

	public BundleContext getContext() {
		return context;
	}
	
	
}
