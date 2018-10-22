package util.scheduler;

import util.Call;

/**
 * 管理器
 *
 */
public class SchedulerMgr implements Call{

	private static Scheduler scheduler = null;

	public SchedulerMgr() {
	}
	public static Scheduler getInstance() {
		if (scheduler == null) {
			scheduler = getInstance(Type.QUARTZ);
			reload(scheduler);
		}
		return scheduler;
	}

	public static Scheduler getInstance(Type type) {
		Scheduler scheduler = null;
		switch (type) {
		case QUARTZ:
			scheduler = new SchedulerQuartzImpl();
			break;
		default:
			scheduler = new SchedulerQuartzImpl();
		}
		return scheduler;
	}

	/**
	 * 初始化scheduler 系统级数据 环境设置读取 词典加载 额外配置项
	 * 1.加载配置文件
	 * 2.加载数据库 
	 */
	public static void reload(Scheduler scheduler){
		scheduler.add(new Task());
	}
	
	public void call(){
		Scheduler scheduler = getInstance();
		scheduler.start();
	}

}

enum Type {
	QUARTZ,
}
