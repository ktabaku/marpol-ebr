package al.sybina.ebr.rcp;

import java.io.IOException;

import org.eclipse.core.internal.runtime.InternalPlatform;
import org.eclipse.osgi.internal.debug.Debug;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;

import com.jfoenix.svg.SVGGlyphLoader;

//import com.opcoach.e4.preferences.ScopedPreferenceStore;


/**
 * The activator class controls the plug-in life cycle
 */
@SuppressWarnings("restriction")
public class Activator implements BundleActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "al.sybina.ebr.rcp";
	public static final String PERSPECTIVE_ID = "al.sybina.ebr.perspectives.home";
	public static final String MENU_RIGHT_HBOX_REQUEST = "MENU_RIGHT_HBOX_REQUEST";


	// The shared singleton instance
	private static Activator plugin;
	private static BundleContext bundleContext;

//	private volatile ScopedPreferenceStore  preferenceStore;
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		plugin = this;
		bundleContext = context;
        new Thread(() -> {
            try {
                SVGGlyphLoader.loadGlyphsFont(Activator.class.getResourceAsStream("icomoon.svg"),
                    "icomoon.svg");
            } catch (IOException ioExc) {
                ioExc.printStackTrace();
            }
        }).start();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		bundleContext = null;
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
	


	public void startIbrPlugins(){
				Bundle[] bundles = InternalPlatform.getDefault().getBundleContext().getBundles();
				for (Bundle b : bundles) {
					if (b.getSymbolicName().contains("ibr")) {
						if (b.getState() != Bundle.ACTIVE){
							try {
								b.start();
							} catch (BundleException e) {
								e.printStackTrace();
							}
						}
					}
				}
	}


	public void printPluginServices(){

		Bundle[] bundles = InternalPlatform.getDefault().getBundleContext().getBundles();
		for (Bundle b : bundles) {
			if (b.getSymbolicName().contains("ibr")) {
				Debug.print(b.getSymbolicName());
				switch (b.getState()) {
				case Bundle.ACTIVE:
					Debug.print(" ACTIVE");
					break;
				case Bundle.UNINSTALLED:
					Debug.print(" NOT INSTALLED");
					break;
				case Bundle.INSTALLED:
					Debug.print(" INSTALLED");
					break;
				case Bundle.STARTING:
					Debug.print(" STARTING");
					break;
				case Bundle.STOPPING:
					Debug.print(" STOPPING");
					break;
				case Bundle.RESOLVED:
					Debug.print(" RESOLVED");
					break;
				}
				ServiceReference<?>[]  services = b.getRegisteredServices();
				if (services != null){
					Debug.print(" ( ");
					for(ServiceReference<?> rs : services){
						Debug.print(rs.getProperty("description") + " ");
					}
					Debug.print(")");
				}
				Debug.println("");
			}
		}
	}

	public static BundleContext getBundleContext() {
		return bundleContext;
	}

}
