package com.walker.socket.client;

public class ClientTest {

	public static void main(String[] args) throws Exception {
//		new ClientUI(new ClientIO("127.0.0.1", 8090), "io-io");
//		new ClientUI(new ClientIO("127.0.0.1", 8091), "io-nio");
//		new ClientUI(new ClientNIO("127.0.0.1", 8090), "nio-io");
//		new ClientUI(new ClientNIO("127.0.0.1", 8091), "nio-nio-server");
//		new ClientUI(new ClientNIO("127.0.0.1", 8091), "nio-nio-client");
//		new ClientUI(new ClientNIO("127.0.0.1", 8092), "nio-netty-client");
		new ClientUI(new ClientNetty("127.0.0.1", 8092), "netty-netty-client");

	}

}
