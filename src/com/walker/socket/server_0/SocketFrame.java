package com.walker.socket.server_0;

import com.walker.common.setting.Setting;
import com.walker.common.util.Tools;
import com.walker.socket.server_0.TaskMake.TaskInterface;

/**
 * 底层 socket 架构调用抽象 
 * 负责实现socket通信 的 连接建立 并调用上层server的 事件 send receive
 * 再由上层server决定调用底层的 send receive...
 * @param <SOCK>
 *
 */

public abstract class SocketFrame<SOCK> implements InterfaceOut {

	 

	int reconnect = 20;	//重连次数
	long sleeptime = 1000;//重连间隔
	
	Server<SOCK> server;	//父级引用 循环引用
	
	public SocketFrame(){
		reconnect = Setting.get("reconnect_count", 20);
		sleeptime = Setting.get("reconnect_sleep", 1000);
	}
	
	public void setServer(Server<SOCK> server){
		this.server = server;
	}
	public void out(Object...objects){
		Tools.out(this.getClass().getName(), Tools.objects2string(objects));
	}
 
	//开始连接任务
	protected void start(){
		TaskMake task = new TaskMake(new TaskInterface() {
			@Override
			public void onTrue() {
				out("启动服务器成功");
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
		}, "启动服务器", sleeptime, reconnect);
		task.startTask();
	}  
	//开始读取任务
	protected void read(final SOCK socket){
		TaskMake taskMake = new TaskMake(new TaskInterface() {
			@Override
			public void onTrue() {
				read(socket);	//一个task(20延迟,最多连续两次异常读取)
			}
			@Override
			public void onFalse() {	//多次异常读取后就认为失联
				out("失联", socket);
				onDisConnection(socket);
			}
			@Override
			public void doTask() throws Exception {
				String readLine = readImpl(socket);
				if(Tools.notNull(readLine)){
					onReceive(socket, readLine);
				}
			}
			@Override
			public void tip(Object... objects) {
				out(objects);
			}
		}, "读取消息",1000, 3);	//最多5次读取
		taskMake.startTask();
	}  
	//开启发送任务
	protected void send(final SOCK socket, final String jsonstr){
		TaskMake task = new TaskMake(new TaskInterface() {
			@Override
			public void onTrue() {
//				out("发送成功", jsonstr);
			}
			@Override
			public void onFalse() {
				out("发送失败", jsonstr);
			}
			@Override
			public void doTask() throws Exception {
				sendImpl(socket, jsonstr);
			}
			@Override
			public void tip(Object... objects) {
				out(objects);
			}
		});
		task.startTask();
	} 
	
	/**
	 * 获取到新连接传递给上级管理
	 */
	protected void onNewConnection(SOCK socket){
		server.onNewConnection(socket);
	}
	/**
	 * 断开连接传递给上级管理
	 */
	protected void onDisConnection(SOCK socket){
		server.onDisConnection(socket);
	}
	/**
	 * 获取到数据转交上级
	 */
	protected void onReceive(SOCK socket, String readLine) {
		server.onReceive(socket, readLine);
	}
	/**
	 * 关闭 底层socket 关闭循环引用
	 */
	protected void stop(){
		this.stopImpl();
		this.setServer(null);
	}

	
	/**
	 * 开启监听 读取 循环 轮循 阻塞
	 */
	protected abstract void startRead(SOCK socket)  throws Exception;

	/**
	 * 读取数据line 底层实现
	 */
	protected abstract String readImpl(SOCK socket)  throws Exception;
	/**
	 * 发送数据 底层实现
	 */
	protected abstract void sendImpl(SOCK socket, String jsonstr)  throws Exception;
	/**
	 * 启动服务器 底层实现
	 */
	protected abstract void startImpl()   throws Exception;

	/**
	 * 关闭服务器 底层实现
	 */
	protected abstract void stopImpl();

	
}
