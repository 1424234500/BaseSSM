package util.scheduler;

import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import util.ClassUtil;
import util.service.webservice.Provider;

import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

/**
 * quartz实现
 *
 */
class SchedulerQuartzImpl implements util.scheduler.Scheduler {
	private static Logger log = Logger.getLogger(SchedulerQuartzImpl.class);

	SchedulerFactory schedulerFactory;
	Scheduler scheduler;
	
	private Scheduler getScheduler() throws SchedulerException{
		if (schedulerFactory == null) {
			log.info(" * init scheduler quartz SchedulerFactory");
			schedulerFactory = new StdSchedulerFactory();
		}
		if (scheduler == null) {
			log.info(" * init scheduler quartz Scheduler");
			scheduler = schedulerFactory.getScheduler();
		}
		return scheduler;
	} 
	
	private JobDetail makeJobDetail(final Task task){
		String name = task.toString();
//		Job job = new Job(){
//			@Override
//			public void execute(JobExecutionContext arg0) throws JobExecutionException {
//				log.info(" # scheduler quartz execute " + task.toString());
//				ClassUtil.doClassMethod(task.className, task.methodName, task.args);
//				log.info(" # scheduler quartz execute over ");
//			}
//		};
		Class clz = ClassUtil.loadClass(task.className);
		
		JobDetail jobDetail = JobBuilder.newJob(clz)
				.withIdentity(name)
				.build();
		
		return jobDetail;
	}
	
	private Trigger makeTrigger(final Task task){
//		Trigger trigger = TriggerBuilder.newTrigger()
//		.withIdentity("trigger1", "group3")
//		.withSchedule(SimpleScheduleBuilder.simpleSchedule()
//		.withIntervalInSeconds(3)
//		.repeatForever()).build();
		TriggerBuilder triggerBuilder = TriggerBuilder.newTrigger();

		List<String> trr = task.pattern;
		for(String cron : trr){
			triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
		}
		
		return triggerBuilder.build();
	}
	
	@Override
	public void start() throws Exception {
		getScheduler().start();
	}
	@Override
	public void pause() throws Exception {
		getScheduler().pauseAll();
	}
	@Override
	public void shutdown() throws Exception {
		getScheduler().shutdown();
	}
	@Override
	public void add(Task task) throws Exception {
		Scheduler scheduler = getScheduler();
		JobDetail jobDetail = makeJobDetail(task);
		Trigger trigger = makeTrigger(task);
		// 将任务及其触发器放入调度器
		scheduler.scheduleJob(jobDetail, trigger);		
	}
	@Override
	public void remove(Task task) throws Exception {
		JobDetail job = (JobDetail)(task.make);
		getScheduler().deleteJob(job.getKey());
	}
	@Override
	public void update(Task task) throws Exception {

		
	} 

}
