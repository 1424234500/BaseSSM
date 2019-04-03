package com.walker.socket.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.walker.common.setting.Setting;
import com.walker.common.util.Tools;
import com.walker.socket.server_0.SocketUtil;

public class ClientIO extends ClientFrame {
 
	Socket socket = null;
	String serverIp = "127.0.0.1";
	int serverPort = 8090; 
	
	ClientIO(){
		serverPort = Setting.get("socket_port_io", 8090);
		try {
			serverIp = Setting.get("socket_ip", Tools.getServerIp(InetAddress.getLocalHost().getHostAddress()));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		serverIp = Setting.get("socket_ip", "127.0.0.1");
	}
	ClientIO(String ip, int port){
		this.serverIp = ip;
		this.serverPort = port;
	}
	
	
	@Override
	public String show() {
		return socket.toString();
	}

	@Override
	protected void startRead() throws Exception {
		read();
	}

	@Override
	protected String readImpl() throws Exception {
		return SocketUtil.readImpl(socket, this);
	}

	@Override
	protected void sendImpl(String jsonstr) throws Exception {
		SocketUtil.sendImpl(socket, jsonstr, this);
	}

	@Override
	protected void startImpl() throws Exception {
		Tools.out("IO", serverIp, serverPort);
		this.socket = new Socket(serverIp, serverPort);
		if(this.socket == null){
			throw new Exception("socket连接失败");
		}
	}

	@Override
	protected void stopImpl() {
		try {
			if(this.socket != null){
				this.socket.close();
				this.socket = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public boolean isStart() {
		return socket != null && socket.isConnected() && !socket.isClosed();
	} 
 
}
