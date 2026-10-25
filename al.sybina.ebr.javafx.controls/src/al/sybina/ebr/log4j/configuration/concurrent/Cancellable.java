package al.sybina.ebr.log4j.configuration.concurrent;

/**
 * A task handle that can be used to handle a scheduled task.
 */
public interface Cancellable {

    /**
     * Cancel the task.
     */
    void cancel();

}
