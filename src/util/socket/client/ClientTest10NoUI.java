package util.socket.client;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import util.Tools;
import util.socket.server_1.Msg;

public class ClientTest10NoUI implements UiCall{
	int dtime = 100;	//每次连接 登录间隔延时
	int dtimesend = 1000;	//每批次发送间隔
	int dtimenewconnection = 30000;	//每批次连接添加间隔
	int num = 20;	//每批次添加连接
	int maxNum = 2000;	//最大连接数
	List<Client> cc = new ArrayList<Client>();
	AtomicLong count = new AtomicLong(0L);
	ClientTest10NoUI() throws Exception{
		startMakeClient();
		startSend();
		
	}
	public void startSend() {
		//开启循环发送 每个连接 发送  一批次  每个都发送 每秒每个连接发送10次
		ThreadUtilClient.scheduleWithFixedDelay(new Thread() {
			public void run() {
				Tools.out("开始发送批次" + cc.size());
				for(int i = 0; i < cc.size(); i++) {
					Client client = cc.get(i);
					sendAllUser(client);
				}
				Tools.out("开始发送批次" + cc.size(), "完成");
			}
		}, 3000, dtimesend, TimeUnit.MILLISECONDS);
	}
	public void startMakeClient() {
		//开启增长一个批次的连接
		ThreadUtilClient.scheduleWithFixedDelay(new Thread() {
			public void run() {
				Tools.out("当前连接数" + cc.size());
				if(cc.size() < maxNum - num) {
					Tools.out("创建连接数" + num);
					for(int i = 0; i < num; i++) {
						cc.add(newConnect());
						try {
							Thread.sleep(dtime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				Tools.out("当前连接数" + cc.size(), "完成");

			}
		}, 3000, dtimenewconnection, TimeUnit.MILLISECONDS);
	}
	
	private Client newConnect() {
		Client res = new ClientNetty("127.0.0.1", 8092);
		res.setUI(this);
		res.start();
		return res;
	}
	public void show(Client client) {

	}
	public void login(Client client, String key) {
		client.send("{type:login,data:{user:" + key + ",pwd:123456} }");
	}
	public void sendAllUser(Client client) {
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
		Tools.out(readLine);
	}

	@Override
	public void out(Object... objects) {
//		Tools.out(objects);
	}
	

}
