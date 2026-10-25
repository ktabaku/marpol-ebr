package al.sybina.ebr.core.services;

public interface IServiceListener {

	/**
	 * the service as defined in event has just being registered.
	 * @param event
	 */
	void serviceStarted(ServiceEventCreated event);

}
