package al.sybina.ebr.login.services.impl;


import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.ecf.core.ContainerConnectException;
import org.eclipse.ecf.core.ContainerCreateException;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.IContainerFactory;
import org.eclipse.ecf.core.IContainerManager;
import org.eclipse.ecf.core.identity.IDCreateException;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.sharedobject.ISharedObjectContainer;
import org.eclipse.ecf.core.sharedobject.SharedObjectAddException;
import org.eclipse.ecf.remoteservice.eventadmin.DistributedEventAdmin;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

import com.opcoach.e4.preferences.E4PreferencePage;

import al.sybina.ebr.commons.utils.NetworkUtils;
import al.sybina.ebr.login.Activator;
import al.sybina.ebr.login.preferences.LoginPreferencePage;
import al.sybina.ebr.login.services.RemoteEventAdminService;

@Creatable("RemoteEventAdminService")
@Singleton
public class RemoteEventAdminServiceImpl implements RemoteEventAdminService {
	
	public static final String DEFAULT_TOPIC = "defaultTopic";
	
	private static final Logger logger = LoggerCreator.createLogger(RemoteEventAdminServiceImpl.class);
	
	private DistributedEventAdmin distributedEventAdmin;
	
	protected IContainer container;
	protected ServiceTracker containerManagerTracker;
	protected String containerType = "ecf.generic.client";
	protected String targetId = "ecftcp://localhost:3787/server";
//	protected String[] topics = {DEFAULT_TOPIC, "anotherTopic", "anotherTopic/subTopic"};
	
	protected void activate() throws ContainerCreateException, SharedObjectAddException, ContainerConnectException {
//		while(Activator.getEntityManager()==null) {
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//			}
//		}
		String targetIp = Platform.getPreferencesService().getString(E4PreferencePage.PLUGIN_ID, "mainInstallationIp", "localhost", null);
		//NetworkUtils.isReachable(targetIp, 3787, 500)
		if(Activator.isDbServerPresent()) {
			BundleContext bundleContext = FrameworkUtil.getBundle(RemoteEventAdminServiceImpl.class).getBundleContext();
	        distributedEventAdmin = new DistributedEventAdmin(bundleContext);
	        createConfigureAndConnectContainer(bundleContext, targetIp);
			distributedEventAdmin.start();
		}
	}

    protected void deactivate() {
    	distributedEventAdmin.stop();
    	distributedEventAdmin = null;
    }

	@Override
	public DistributedEventAdmin getDistributedEventAdmin(String targetIp) {
		String temp = targetId.replace("ecftcp://", "");
		String oldTargetIp = temp.substring(0, temp.indexOf(":"));
		if(oldTargetIp.equals(targetIp)) {
			if(container.getConnectedID()==null) {
				try {
					container.connect(IDFactory.getDefault().createID(container.getConnectNamespace(), "ecftcp://"+targetIp+":3787/server"), null);
				} catch (ContainerConnectException | IDCreateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return distributedEventAdmin;
		} else {
			targetId = "ecftcp://"+targetIp+":3787/server";
			if(container.getConnectedID()!=null) {
				container.disconnect();
			}
			try {
				container.connect(IDFactory.getDefault().createID(container.getConnectNamespace(), "ecftcp://"+targetIp+":3787/server"), null);
			} catch (ContainerConnectException | IDCreateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return distributedEventAdmin;
		}
	}
	
	
	
	
	
	protected void createConfigureAndConnectContainer(BundleContext bundleContext, String targetIp) throws ContainerCreateException, SharedObjectAddException, ContainerConnectException {
		// get container factory and create container
		IContainerFactory containerFactory = getContainerManager(bundleContext).getContainerFactory();
		String publicIp = NetworkUtils.getSiteLocalIPv4();
		String containerId = publicIp!=null? "ecftcp://" + publicIp + ":3787/client" : null;
		container = (containerId == null) ? containerFactory.createContainer(containerType) : containerFactory.createContainer(containerType, new Object[] { containerId });

		// Get socontainer
		ISharedObjectContainer soContainer = (ISharedObjectContainer) container.getAdapter(ISharedObjectContainer.class);
		// Add to soContainer, with topic as name
		soContainer.getSharedObjectManager().addSharedObject(IDFactory.getDefault().createStringID(DEFAULT_TOPIC), distributedEventAdmin,null);

		// then connect to target Id
		targetId = "ecftcp://"+targetIp+":3787/server";
		try {
			container.connect(IDFactory.getDefault().createID(container.getConnectNamespace(), "ecftcp://"+targetIp+":3787/server"), null);
		} catch (ContainerConnectException | IDCreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected IContainerManager getContainerManager(BundleContext bundleContext) {
		if (containerManagerTracker == null) {
			containerManagerTracker = new ServiceTracker(bundleContext, IContainerManager.class.getName(), null);
			containerManagerTracker.open();
		}
		return (IContainerManager) containerManagerTracker.getService();
	}
}
