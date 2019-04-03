package com.walker.socket.server_0;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.walker.common.setting.Setting;

/**
 * 底层 socket 基本实现
 * 负责实现socket通信 的 连接建立 并调用上层server的 事件 send receive
 * 再由上层server决定调用底层的 send receive...
 *
 */

public  class SocketIO extends SocketFrame<Socket>  {

	static ServerSocket serverSocket;
	 
	static String  serverHostName;			//服务器hostname
	static boolean boolIfOn;				//是否开启监听线程
	static String  serverIp;				//服务器ip
	static int	   serverPort;				//服务器port

 
	public SocketIO(){
		serverPort = Setting.get("socket_port_io", 8090);
	}

	@Override
	protected void startImpl() throws Exception{
		serverSocket = new ServerSocket(serverPort);
		out("启动服务器成功：服务器信息如下");
		serverIp = InetAddress.getLocalHost().getHostAddress();
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
				onNewConnection(tempSocket);
				//frame底层负责启动监听任务 并传递给上层处理
				startRead(tempSocket);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		out("关闭服务器监听线程");
		
	} 
	/**
	 * io阻塞方式 行读取模式  解离长度头 假设最多发送不会超过一行
	 * line:  int<28>{"id":0010,"name":"asdfj"}
	 */
	@Override
	public String readImpl(Socket socket) throws Exception{
		return SocketUtil.readImpl(socket, this);
	}
	/**
	 * io阻塞方式 对应的行发送模式  添加长度头
	 * line:  int<28>{"id":0010,"name":"asdfj"}
	 */
	@Override
	public void sendImpl(Socket socket, String jsonstr) throws Exception {
		SocketUtil.sendImpl(socket, jsonstr, this);
	}

	@Override
	protected void stopImpl() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void startRead(Socket socket) throws Exception {
		read(socket);
	} 
	  
	
	
	
}
