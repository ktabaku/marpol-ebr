package al.sybina.ebr.logbook.core.services;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;

public interface DerbyDatabaseBackupService {
	
	public void runBackupDatabaseJob(String backupDir);

	public Scheduler getStandardScheduler() throws SchedulerException;
	
	
}
