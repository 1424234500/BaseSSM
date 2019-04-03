package com.walker.socket.server_0;

import com.walker.common.util.ThreadUtil;
import com.walker.common.util.Tools;


public class TaskMake{
	/**
	 * 重试任务 接口
	 */
	public interface TaskInterface{
		public void onTrue() ;	//操作成功
		public void onFalse();	//操作重试都失败
		public void doTask() throws Exception;	//执行任务
		public void tip(Object...objects);	//提示信息输出
	} 
	
	TaskInterface taskResult = null;
	int threadType = ThreadUtilServer.DefaultThread;
	long time = 1000;
	int maxRetryCount = 5;	//最大重试次数
	String doName = "操作";
	Task task = null;
	public TaskMake(TaskInterface taskResult){
		this.taskResult = taskResult;
		this.task = new Task(this.taskResult, this.doName, this.time, this.maxRetryCount);
	}
	public TaskMake(TaskInterface taskResult, String doName){
		this.taskResult = taskResult;
		this.doName = doName;
		this.task = new Task(this.taskResult, this.doName, this.time, this.maxRetryCount);
	}
	public TaskMake(TaskInterface taskResult, String doName, long time, int maxRetryCount){
		this.taskResult = taskResult;
		this.doName = doName;
		this.time = time;
		this.maxRetryCount = maxRetryCount;
		this.task = new Task(this.taskResult, this.doName, this.time, this.maxRetryCount);
	}
	public void startTask(){
		ThreadUtilServer.execute(threadType,  task);
	}
	class Task implements Runnable{
		int count = 0;			//操作失败次数
		int maxRetryCount = 5;	//最大重试次数
		long time = 1000;
		String doName;
		TaskInterface result = null;
		public Task(TaskInterface taskResult, String doName, long time, int maxRetryCount){
			this.result = taskResult;
			this.doName = doName;
			this.time = time;
			this.maxRetryCount = maxRetryCount;
		} 
		public void out(Object...objects){
//			result.tip(objects);
			Tools.out("Task", Tools.objects2string(objects));
		}
		@Override
		public void run() {
			try{
				if(count > 0 && count <= maxRetryCount){
					out("尝试重新" + doName, count);
					result.doTask();
					result.onTrue();
				}else if(count > maxRetryCount){
					result.onFalse();
					count = 0;
				}else{
					result.doTask();
					count = 0;
					result.onTrue();
				}
			}catch(Exception e){
				e.printStackTrace();
				out(TaskMake.this.doName + "异常," + Tools.calcTime(time) + "后重新读取", count++, e.toString());
				ThreadUtil.sleep(time);
				TaskMake.this.startTask();
			}
		}
	}
}
