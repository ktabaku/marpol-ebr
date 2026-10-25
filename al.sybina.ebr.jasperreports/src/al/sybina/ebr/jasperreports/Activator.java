package al.sybina.ebr.jasperreports;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;


import al.sybina.ebr.core.tools.PluginTools;

public class Activator implements BundleActivator {
	
	public static final String PLUGIN_ID = "al.sybina.ebr.jasperreports";
	private static BundleContext bundleContext;
	public static final String RUNTIME_LOCATION_FOLDER_NAME = "reports";
	
    public void start(BundleContext bundleContext) {
    	this.bundleContext = bundleContext;
//    	PluginTools.installPluginDataInRuntimeLocation(FrameworkUtil.getBundle(this.getClass()), RUNTIME_LOCATION_FOLDER_NAME);
    }
	@Override
	public void stop(BundleContext context) throws Exception {
	}
	
	
	public static BundleContext getBundleContext() {
		return bundleContext;
	}
	
}
