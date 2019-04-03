package com.walker.core.scheduler;


/**
 * 定时器任务调度工具
 *
 * 初始化任务 db cache
 * 
 * 加入任务
 * 移除任务
 * 修改任务
 * 
 * 策略 异常上抛 用者处理
 */
public interface Scheduler{
	
	 /**
     * 启动
     */
    public void start() throws Exception;

	 /**
     * 暂停
     */
    public void pause() throws Exception;

    
    /**
     * 关闭
     */
    public void shutdown() throws Exception;

    /**
     * 添加任务
     */
    public void add(Task task) throws Exception;
    
    
    /**
     * 移除任务
     */
    public void remove(Task task) throws Exception;
    
    /**
     * 修改任务
     */
    public void update(Task task) throws Exception;
    
    
    
	
}
