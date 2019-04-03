package com.walker.core.scheduler;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 * quartz实现
 *
 */
class SchedulerQuartzImpl implements com.walker.core.scheduler.Scheduler {
	private static Logger log = Logger.getLogger(SchedulerQuartzImpl.class);

	SchedulerFactory schedulerFactory;
	Scheduler scheduler;
	
	private Scheduler getScheduler() throws SchedulerException{
		if (schedulerFactory == null) {
			log.warn(" * init scheduler quartz SchedulerFactory");
			schedulerFactory = new StdSchedulerFactory();
		}
		if (scheduler == null) {
			log.warn(" * init scheduler quartz Scheduler");
			scheduler = schedulerFactory.getScheduler();
		}
		return scheduler;
	} 

	@Override
	public void start() throws Exception {
		if(!getScheduler().isStarted())
			getScheduler().start();
	}
	@Override
	public void pause() throws Exception {
		getScheduler().pauseAll();
	}
	@Override
	public void shutdown() throws Exception {
		if(!getScheduler().isShutdown())
			getScheduler().shutdown();
	}
	@Override
	public void add(Task task) throws Exception {
		Scheduler scheduler = getScheduler();
		JobDetail jobDetail = task.getJobDetail();//makeJobDetail(task);
		Trigger trigger = task.getTrigger();//makeTrigger(task);
		// 将任务及其触发器放入调度器
		scheduler.scheduleJob(jobDetail, trigger);	
		start();
	}
	@Override
	public void remove(Task task) throws Exception {
		JobDetail job = task.getJobDetail();
		getScheduler().deleteJob(job.getKey());
	}
	/**
	 * 只修改触发器 
	 * 以className methodName args[] 为键修改
	 */
	@Override
	public void update(Task task) throws Exception {
		remove(task);
		add(task);
	} 

}
