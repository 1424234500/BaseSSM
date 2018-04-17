package util.socket.client;

import util.TaskInterface;
import util.TaskMake;
import util.Tools;
import util.setting.Setting;
import util.socket.InterfaceOut;

public abstract class ClientFrame implements Client, InterfaceOut{

	
	
	int reconnect = 20;	//重连次数
	long sleeptime = 1000;//重连间隔
	
	ClientUI clientUi;
	public ClientFrame(){
		reconnect = Setting.getInt("reconnect_count", 20);
		sleeptime = Setting.getLong("reconnect_sleep", 1000);
	}
	@Override
	public void setUI(ClientUI cui){
		this.clientUi = cui;
	}
	public void out(Object...objects){
		clientUi.out(objects);
	}
 
	//开始连接任务
	@Override
	public void start(){
		TaskMake task = new TaskMake(new TaskInterface() {
			@Override
			public void onTrue() {
				out("连接成功");
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
		task.startTask();
	}  
	//开始读取任务
	protected void read(){
		TaskMake taskMake = new TaskMake(new TaskInterface() {
			@Override
			public void onTrue() {
				read();	//一个task(20延迟,最多连续两次异常读取)
			}
			@Override
			public void onFalse() {	//多次异常读取后就认为失联
				out("失联");
				start();
			}
			@Override
			public void doTask() throws Exception {
				String readLine = readImpl();
				if(Tools.isNull(readLine)){
					clientUi.onReceive(readLine);
				}
			}
			@Override
			public void tip(Object... objects) {
				out(objects);
			}
		}, "读取消息",1000, 5);	//最多5次读取
		taskMake.startTask();
	}  
	//开启发送任务
	@Override
	public void send(final String jsonstr){
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
				sendImpl(jsonstr);
			}
			@Override
			public void tip(Object... objects) {
				out(objects);
			}
		});
		task.startTask();
	} 
	
	  
	
	
	

	/**
	 * 关闭 底层socket 关闭循环引用
	 */
	public void stop(){
		this.stopImpl();
		this.clientUi = null;
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