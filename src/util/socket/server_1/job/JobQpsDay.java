package util.socket.server_1.job;

import org.quartz.JobExecutionContext;

import util.Tools;
import util.scheduler.TaskJob;

public class JobQpsDay extends TaskJob{

	@Override
	public void run() {
		Tools.out("scheduler quartz run test");
		
	}

	
}
