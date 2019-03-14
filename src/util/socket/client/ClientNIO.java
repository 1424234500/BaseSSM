package util.socket.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;

import util.Tools;
import util.setting.Setting;
import util.socket.server_0.SocketUtil;

public class ClientNIO extends ClientFrame {
 
	SocketChannel socket = null;
	String serverIp = "127.0.0.1";
	int serverPort = 8091; 
	
	ClientNIO(){
		serverPort = Setting.getInt("socket_port_nio", 8091);
		try {
			serverIp = Setting.getString("socket_ip", Tools.getServerIp(InetAddress.getLocalHost().getHostAddress()));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		serverIp = Setting.getString("socket_ip", "127.0.0.1");
	}
	ClientNIO(String ip, int port){
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
		out("NIO", serverIp, serverPort);
		socket = SocketChannel.open();
		socket.connect(new InetSocketAddress(serverIp, serverPort));
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
 
}
