package com.walker.socket.server_0;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import com.walker.common.setting.Setting;

/**
 * 底层 socket 基本实现
 * 负责实现socket通信 的 连接建立 并调用上层server的 事件 send receive
 * 再由上层server决定调用底层的 send receive...
 *
 */

public  class SocketNIO extends SocketFrame<SocketChannel>  {

	static ServerSocketChannel serverSocketChannel;
	 
	static String  serverHostName;			//服务器hostname
	static boolean boolIfOn;				//是否开启监听线程
	static String  serverIp;				//服务器ip
	static int	   serverPort;				//服务器port
	
 
	public SocketNIO(){
		serverPort = Setting.get("socket_port_nio", 8091);
	}

	@Override
	protected void startImpl() throws Exception{
		serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.bind(new InetSocketAddress(serverPort));
		
		out("启动服务器成功：服务器信息如下");
		serverIp = InetAddress.getLocalHost().getHostAddress();
		serverHostName = InetAddress.getLocalHost().getHostName();
		out(serverIp, serverPort, serverHostName);
		out("等待客户端.....");
		boolIfOn = true;
		while (boolIfOn) {
			try{
				SocketChannel tempSocket = serverSocketChannel.accept();
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

	@Override
	protected void startRead(SocketChannel socket) throws Exception {
		read(socket);
	}

	@Override
	protected String readImpl(SocketChannel socket) throws Exception {
		return SocketUtil.readImpl(socket, this);
	}

	@Override
	protected void sendImpl(SocketChannel socket, String jsonstr) throws Exception {
		SocketUtil.sendImpl(socket, jsonstr, this);
	}

	@Override
	protected void stopImpl() {
		// TODO Auto-generated method stub
		
	}  
	
	
}
