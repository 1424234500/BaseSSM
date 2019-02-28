package util.scheduler;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import util.Tools;

public abstract class TaskJob implements Job,Runnable {
	
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();
		Tools.out("Scheduler quartz execute " + this.getClass().toString());
		Tools.out(jobDetail.getClass().getName(), jobDetail.getDescription());
		
		this.run();
		
	}

	public abstract void execute(JobExecutionContext context, Class clz, String about);
	
}
