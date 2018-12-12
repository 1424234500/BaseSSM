package util.scheduler.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import util.Tools;

public class JobTest implements Job{
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		Tools.out("scheduler quartz test");
	}
	
}
