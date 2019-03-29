package util.socket.client;

public class ClientTest {

	public static void main(String[] args) throws InterruptedException {
//		new ClientUI(new ClientIO("127.0.0.1", 8090), "io-io");
//		new ClientUI(new ClientIO("127.0.0.1", 8091), "io-nio");
//		new ClientUI(new ClientNIO("127.0.0.1", 8090), "nio-io");
//		new ClientUI(new ClientNIO("127.0.0.1", 8091), "nio-nio-server");
//		new ClientUI(new ClientNIO("127.0.0.1", 8091), "nio-nio-client");
//		new ClientUI(new ClientNIO("127.0.0.1", 8092), "nio-netty-client");
		for(int i = 0; i < 1; i++) {
			ClientUI ui = new ClientUI(new ClientNetty("127.0.0.1", 8092), "netty-netty-client");
			if(i == 10)continue;

//			ui.btLogin.doClick();
			Thread.sleep(1000);
//			ui.jbtest.doClick();
		}
	}

}
