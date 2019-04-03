package com.walker.core.scheduler;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.walker.common.util.Tools;

public abstract class TaskJob implements Job,Runnable {
	private static Logger log = Logger.getLogger(TaskJob.class);

	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();
		log.info("Scheduler quartz execute " + this.getClass().toString());
		log.info(jobDetail.getClass().getName() + " " + jobDetail.getDescription());
		
		this.run();
		
	}

	
}
