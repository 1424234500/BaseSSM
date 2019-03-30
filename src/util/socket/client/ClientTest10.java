package util.socket.client;

public class ClientTest10 {

	public static void main(String[] args) throws InterruptedException {
//		new ClientUI(new ClientIO("127.0.0.1", 8090), "io-io");
//		new ClientUI(new ClientIO("127.0.0.1", 8091), "io-nio");
//		new ClientUI(new ClientNIO("127.0.0.1", 8090), "nio-io");
//		new ClientUI(new ClientNIO("127.0.0.1", 8091), "nio-nio-server");
//		new ClientUI(new ClientNIO("127.0.0.1", 8091), "nio-nio-client");
//		new ClientUI(new ClientNIO("127.0.0.1", 8092), "nio-netty-client");
		int num = 10;
		ClientUI.clientNum = num;
		ClientUI[] cc = new ClientUI[num];
		for(int i = 0; i < num; i++) {
			cc[i] = new ClientUI(new ClientNetty("127.0.0.1", 8092), "netty-netty-client");
			Thread.sleep(1000);
		}
		for(int i = 0; i < num; i++) {
			if(i == num - 1)continue;
			cc[i].btLogin.doClick();
			cc[i].jbtest.doClick();
			Thread.sleep(1000);
		}
	}

}
