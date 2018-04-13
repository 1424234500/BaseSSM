package util;

/**
 * 重试任务 接口
 *
 */
public interface TaskInterface{
	public void onTrue() ;	//操作成功
	public void onFalse();	//操作重试都失败
	public void doTask() throws Exception;	//执行任务
	
} 