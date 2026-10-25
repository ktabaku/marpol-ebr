package al.sybina.ebr.login.services;

import org.eclipse.ecf.remoteservice.eventadmin.DistributedEventAdmin;

public interface RemoteEventAdminService {

	public DistributedEventAdmin getDistributedEventAdmin(String targetIp);

}
