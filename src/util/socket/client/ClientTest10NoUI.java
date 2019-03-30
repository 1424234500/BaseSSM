package util.socket.client;

import java.util.concurrent.atomic.AtomicLong;

import util.Tools;
import util.socket.server_1.Msg;

public class ClientTest10NoUI implements UiCall{
	ClientTest10NoUI() throws Exception{
//		new ClientUI(new ClientIO("127.0.0.1", 8090), "io-io");
//		new ClientUI(new ClientIO("127.0.0.1", 8091), "io-nio");
//		new ClientUI(new ClientNIO("127.0.0.1", 8090), "nio-io");
//		new ClientUI(new ClientNIO("127.0.0.1", 8091), "nio-nio-server");
//		new ClientUI(new ClientNIO("127.0.0.1", 8091), "nio-nio-client");
//		new ClientUI(new ClientNIO("127.0.0.1", 8092), "nio-netty-client");
		int num = 3;
		Client[] cc = new Client[num];
		for(int i = 0; i < num; i++) {
			cc[i] = newConnect();
			Thread.sleep(1000);
		}
		for(int i = 0; i < num; i++) {
			if(i == num - 1)continue;
			login(cc[i], Tools.getRandomNum(10, 99, 2));
			Thread.sleep(1000);
		}
		

		
	}
	private Client newConnect() {
		Client res = new ClientNetty("127.0.0.1", 8092);
		res.setUI(this);
		res.start();
		return res;
	}
	AtomicLong count = new AtomicLong(0L);
	public void show(Client client) {

	}
	public void login(Client client, String key) {
		client.send("{type:login,data:{user:" + key + ",pwd:123456} }");
	}
	public void sendTest(Client client) {
		count.addAndGet(1L);
		Msg msg = new Msg();
		msg.setType("message");
		msg.setData("{type:txt,body:" + count.get() + "-up}");
		msg.setUserTo("all_user");
		msg.setTimeClient(System.currentTimeMillis());
		client.send(msg.toString());
	}
	
	
	public static void main(String[] args) throws Exception {
		new ClientTest10NoUI();
	}

	@Override
	public void onReceive(String readLine) {
		out(readLine);
	}

	@Override
	public void out(Object... objects) {
		Tools.out(objects);
	}

}
