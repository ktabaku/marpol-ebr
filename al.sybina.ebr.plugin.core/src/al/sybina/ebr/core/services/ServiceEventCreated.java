package al.sybina.ebr.core.services;

/**
 * event thrown when a service is created.
 * @author crosay
 */
public class ServiceEventCreated {
	public Object service;
	public String id;

	public ServiceEventCreated(String id, Object service) {
		super();
		this.id  = id;
		this.service = service;
	}

}
