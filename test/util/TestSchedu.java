package util;

import org.junit.Test;

import com.walker.core.scheduler.Scheduler;
import com.walker.core.scheduler.SchedulerMgr;
import com.walker.core.scheduler.Task;

public class TestSchedu {
	
	@Test
	public void makeDay() throws Exception{
		Task task = new Task("util.scheduler.job.JobTest", "this is a schdeler", "*/2 * * * * ?");
		Scheduler sch = SchedulerMgr.getInstance();
		sch.add(task);
		sch.start();
		
		while(true) {
			
		}
		
	}
}
