package al.sybina.ebr.logbook2;


import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

//import com.opcoach.e4.preferences.ScopedPreferenceStore;


/**
 * The activator class controls the plug-in life cycle
 */
public class Activator implements BundleActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "al.sybina.ebr.logbook2.ui";
	
	private static BundleContext context;
	private static Activator plugin;

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
		Activator.context = context;
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
	public BundleContext getContext() {
		return context;
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
}
