package al.sybina.ebr.server.services.impl;


import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.ecf.core.ContainerConnectException;
import org.eclipse.ecf.core.ContainerCreateException;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.IContainerFactory;
import org.eclipse.ecf.core.IContainerManager;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.sharedobject.ISharedObjectContainer;
import org.eclipse.ecf.core.sharedobject.SharedObjectAddException;
import org.eclipse.ecf.remoteservice.eventadmin.DistributedEventAdmin;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

import al.sybina.ebr.server.Activator;
import al.sybina.ebr.server.services.RemoteEventAdminServerService;


@Creatable("RemoteEventAdminServerService")
public class RemoteEventAdminServerServiceImpl implements RemoteEventAdminServerService {
	
	public static final String DEFAULT_TOPIC = "defaultTopic";
	
	private static final Logger logger = LoggerCreator.createLogger(RemoteEventAdminServerServiceImpl.class);
	
	private DistributedEventAdmin distributedEventAdmin;
	
	protected IContainer container;
	protected ServiceTracker containerManagerTracker;
	protected String containerType = "ecf.generic.server";
	protected String containerId = "ecftcp://localhost:3787/server";
//	protected String targetId = "ecftcp://localhost:3787/server";
//	protected String[] topics = {DEFAULT_TOPIC, "anotherTopic", "anotherTopic/subTopic"};
	
	protected void activate() throws ContainerCreateException, SharedObjectAddException, ContainerConnectException {
		BundleContext bundleContext = FrameworkUtil.getBundle(RemoteEventAdminServerServiceImpl.class).getBundleContext();
        distributedEventAdmin = new DistributedEventAdmin(bundleContext);
        createConfigureAndConnectContainer(bundleContext);
		distributedEventAdmin.start();
	}

    protected void deactivate() {
    	distributedEventAdmin.stop();
    	distributedEventAdmin = null;
    }

	@Override
	public DistributedEventAdmin getDistributedEventAdmin() {
		return distributedEventAdmin;
	}
	
	
	
	
	
	protected void createConfigureAndConnectContainer(BundleContext bundleContext) throws ContainerCreateException, SharedObjectAddException, ContainerConnectException {
		// get container factory and create container
		IContainerFactory containerFactory = getContainerManager(bundleContext).getContainerFactory();
		container = (containerId == null) ? containerFactory.createContainer(containerType) : containerFactory.createContainer(containerType, new Object[] { containerId });

		// Get socontainer
//		ISharedObjectContainer soContainer = (ISharedObjectContainer) container.getAdapter(ISharedObjectContainer.class);
		// Add to soContainer, with topic as name
//		soContainer.getSharedObjectManager().addSharedObject(IDFactory.getDefault().createStringID(DEFAULT_TOPIC), distributedEventAdmin,null);

		// then connect to target Id
//		if (targetId != null)
//			container.connect(IDFactory.getDefault().createID(container.getConnectNamespace(), targetId), null);
	}

	protected IContainerManager getContainerManager(BundleContext bundleContext) {
		if (containerManagerTracker == null) {
			containerManagerTracker = new ServiceTracker(bundleContext, IContainerManager.class.getName(), null);
			containerManagerTracker.open();
		}
		return (IContainerManager) containerManagerTracker.getService();
	}
}
