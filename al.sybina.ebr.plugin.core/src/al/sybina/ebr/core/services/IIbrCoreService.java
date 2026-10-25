package al.sybina.ebr.core.services;

public interface IIbrCoreService {

	/**
	 * terminate the service.
	 * May be overridden by the specialized service if resources must be destroyed.
	 * called only by the ibr service manager at the end of the service
	 */
	public void terminate();
}
