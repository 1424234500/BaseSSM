package com.walker.socket.client;

import com.walker.common.setting.Setting;
import com.walker.common.util.Tools;
import com.walker.socket.server_0.InterfaceOut;
import com.walker.socket.server_0.TaskMake;
import com.walker.socket.server_0.TaskMake.TaskInterface;

public abstract class ClientFrame implements Client, InterfaceOut{

	
	
	int reconnect = 1;	//重连次数
	long sleeptime = 50;//重连间隔
	
	UiCall clientUi;
	public ClientFrame(){
		reconnect = Setting.get("reconnect_count", 20);
		sleeptime = Setting.get("reconnect_sleep", 1000);
	}
	public void setUI(UiCall cui){
		this.clientUi = cui;
	}
	public void out(Object...objects){
		clientUi.out(objects);
//		Tools.out(objects);
	}
 
	//开始连接任务
	@Override
	public void start() throws Exception{
		TaskMake task = new TaskMake(new TaskInterface() {
			@Override
			public void onTrue() {
				out("连接成功");
//				read();
			}
			@Override
			public void onFalse() {
				out("启动多次后依然失败 -----");
			}
			@Override
			public void doTask() throws Exception {
				startImpl();
			}
			@Override
			public void tip(Object... objects) {
				out(objects);
			}
		}, "连接服务器", sleeptime, reconnect);
//		task.startTask();
		startImpl();
	}  
	//开始读取任务
	protected void read() throws Exception{
		TaskMake taskMake = new TaskMake(new TaskInterface() {
			@Override
			public void onTrue() {
//				read();	//一个task(20延迟,最多连续两次异常读取)
			}
			@Override
			public void onFalse() {	//多次异常读取后就认为失联
				out("失联", show());
//				start();
			}
			@Override
			public void doTask() throws Exception {
				String readLine = readImpl();
				if(Tools.notNull(readLine)){
					clientUi.onReceive(readLine);
				}
			}
			@Override
			public void tip(Object... objects) {
				out(objects);
			}
		}, "读取消息",1000, 5);	//最多5次读取
//		taskMake.startTask();
		String readLine = readImpl();
		if(Tools.notNull(readLine)){
			clientUi.onReceive(readLine);
		}
	}  
	//开启发送任务
	@Override
	public void send(final String jsonstr) throws Exception{
		TaskMake task = new TaskMake(new TaskInterface() {
			@Override
			public void onTrue() {
				out("发送", jsonstr);
			}
			@Override
			public void onFalse() {
				out("发送失败", jsonstr);
			}
			@Override
			public void doTask() throws Exception {
				sendImpl(jsonstr);
			}
			@Override
			public void tip(Object... objects) {
				out(objects);
			}
		});
//		task.startTask();
		sendImpl(jsonstr);
		out("发送", jsonstr);

	} 
	
	  
	
	
	

	/**
	 * 关闭 底层socket 关闭循环引用
	 */
	public void stop(){
		this.stopImpl();
	}

	
	/**
	 * 开启监听 读取 循环 轮循 阻塞
	 */
	protected abstract void startRead()  throws Exception;

	/**
	 * 读取数据line 底层实现
	 */
	protected abstract String readImpl()  throws Exception;
	/**
	 * 发送数据 底层实现
	 */
	protected abstract void sendImpl(String jsonstr)  throws Exception;
	/**
	 * 启动服务器 底层实现
	 */
	protected abstract void startImpl()   throws Exception;

	/**
	 * 关闭服务器 底层实现
	 */
	protected abstract void stopImpl();

}