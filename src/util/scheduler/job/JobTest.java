package util.scheduler.job;

import org.quartz.JobExecutionContext;

import util.Tools;
import util.scheduler.TaskJob;

public class JobTest extends TaskJob{

	@Override
	public void run() {
		Tools.out("scheduler quartz run test");
		
	}

	@Override
	public void execute(JobExecutionContext context, Class<?> clz, String about) {
		Tools.out("scheduler quartz execute test ", clz, about);
		
	}
	
}
