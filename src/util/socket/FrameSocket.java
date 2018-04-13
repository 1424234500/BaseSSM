package util.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import util.TaskInterface;
import util.TaskMake;
import util.Tools;

/**
 * 底层 socket 基本实现
 * 负责实现socket通信 的 连接建立 并调用上层server的 事件 send receive
 * 再由上层server决定调用底层的 send receive...
 *
 */

public  class FrameSocket implements SendCallback<Socket>{

	static ServerSocket serverSocket;
	 
	static String  serverIp = "127.0.0.1";				//服务器ip
	static int	   serverPort = 8090;				//服务器port
	static String  serverHostName = "walker";				//服务器hostname
	static boolean boolIfOn = false;	//是否开启监听线程
	 

	int reconnect = 998;	//重连次数
	long sleeptime = 1000;//重连间隔
	
	@SuppressWarnings("rawtypes")
	Server server;
	@SuppressWarnings("rawtypes")
	public FrameSocket(Server serverImpl){
		this.server = serverImpl;
		this.server.setSendCallback(this);
	}
	public boolean start(){
		this.startConnectTask();
		return true;
	}
	public boolean startServer() throws IOException {
		serverSocket = new ServerSocket(serverPort);
		out("启动服务器成功：服务器信息如下");
		serverIp = InetAddress.getLocalHost().getHostAddress();
		serverPort = serverSocket.getLocalPort();
		serverHostName = InetAddress.getLocalHost().getHostName();
		out(serverIp, serverPort, serverHostName);
		out("等待客户端.....");
		boolIfOn = true;
		while (boolIfOn) {
			try{
				Socket tempSocket = serverSocket.accept();
				//获取到一个连接客户,放入连接客户集合
				out("检测到连接:" + tempSocket.toString());
				//上层负责管理转发调用逻辑  
				server.onNewConnection(tempSocket);
				//frame底层负责启动监听任务 并传递给上层处理
				startReadTask(tempSocket);
			}catch(Exception e){
				e.printStackTrace();
				
			}
		}
		out("关闭服务器监听线程");
		
		return false;
	}
	private void out(Object...objects){
		Tools.out(objects);
	}
 

	
	//开始连接任务
	private void startConnectTask(){
		TaskMake task = new TaskMake(new TaskInterface() {
			@Override
			public void onTrue() {
				Tools.out("启动服务器成功");
			}
			@Override
			public void onFalse() {
				Tools.out("启动多次后依然失败 -----");
			}
			@Override
			public void doTask() throws Exception {
				startServer();
			}
		}, "启动服务器", sleeptime, reconnect);
		task.startTask();
	}  
	//开始读取任务
	private void startReadTask(final Socket socket){
		TaskMake taskMake = new TaskMake(new TaskInterface() {
			@Override
			public void onTrue() {
				startReadTask(socket);	//一个task(20延迟,最多连续两次异常读取)
			}
			@Override
			public void onFalse() {	//多次异常读取后就认为失联
				Tools.out("失联", socket);
				server.onDisConnection(socket);
			}
			@Override
			public void doTask() throws Exception {
				String readLine = read(socket);
				if(Tools.isNull(readLine)){
					server.onReceive(socket, readLine);
				}
			}
		}, "读取消息",1000, 5);	//最多5次读取
		taskMake.startTask();
	}  
	//开启发送任务
	public void startSendTask(final Socket socket, final String jsonstr){
		TaskMake task = new TaskMake(new TaskInterface() {
			@Override
			public void onTrue() {
//				Tools.out("发送成功", jsonstr);
			}
			@Override
			public void onFalse() {
				Tools.out("发送失败", jsonstr);
			}
			@Override
			public void doTask() throws Exception {
				send(socket, jsonstr);
			}
		});
		task.startTask();
	}
	//读取数据
	private String read(Socket socket) throws Exception{
		String res = "";
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		res = reader.readLine();
		return res;
	}
	//发送数据
	public void send(Socket socket, String jsonstr) throws Exception {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter( socket.getOutputStream()));
		writer.write(jsonstr); 
		writer.newLine();
		writer.flush();
	} 
	 
	public boolean stop() {
		try {
			boolIfOn = false;	//停止接收用户线程
			serverSocket.close();
			this.server.setSendCallback(null);
			this.server = null;
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}


	@Override
	public void sendCallback(Socket socket, String jsonstr) {
		this.startSendTask(socket, jsonstr);;
	}
}
