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

public  class SocketIO extends SocketFrame<Socket>  {

	static ServerSocket serverSocket;
	 
	static String  serverHostName = "walker";				//服务器hostname
	static boolean boolIfOn = false;	//是否开启监听线程
	 
 
	 

	@Override
	protected void startImpl() throws Exception{
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
				onNewConnection(tempSocket);
				//frame底层负责启动监听任务 并传递给上层处理
				startRead(tempSocket);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		out("关闭服务器监听线程");
		
	} 
	//读取数据
	@Override
	public String readImpl(Socket socket) throws Exception{
		String res = "";
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		res = reader.readLine();
		return res;
	}
	//发送数据
	@Override
	public void sendImpl(Socket socket, String jsonstr) throws Exception {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter( socket.getOutputStream()));
		writer.write(jsonstr); 
		writer.newLine();
		writer.flush();
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
