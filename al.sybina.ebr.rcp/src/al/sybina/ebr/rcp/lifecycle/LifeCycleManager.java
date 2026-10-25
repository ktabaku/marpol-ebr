package al.sybina.ebr.rcp.lifecycle;

import java.util.HashMap;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;


public class LifeCycleManager {
	
	@Inject private IEventBroker eventBroker;
	
	@PostContextCreate
	public void postContextCreate(IEclipseContext context) {

//		boolean loggedIn = eventBroker.send("LOGIN_NEEDED_EVENT_TOPIC", new HashMap<String, String>());
//		if(!loggedIn) return;
	}


}
