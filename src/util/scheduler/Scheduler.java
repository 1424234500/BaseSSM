package util.scheduler;


/**
 * 定时器任务调度工具
 *
 * 初始化任务 db cache
 * 
 * 加入任务
 * 移除任务
 * 修改任务
 * 
 */
public interface Scheduler{
	
	 /**
     * 启动
     */
    public Boolean start();

	 /**
     * 暂停
     */
    public Boolean pause();

    
    /**
     * 关闭
     */
    public Boolean shutdown();

    /**
     * 添加任务
     */
    public Boolean add(Task task);
    
    
    /**
     * 移除任务
     */
    public Boolean remove(String id);
    
    /**
     * 修改任务
     */
    public Boolean update(Task task);
    
    
    
	
}
