package util.socket.client;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import util.ThreadUtil;
import util.Tools;
import util.socket.server_1.Msg;
/**
 * 保持每个连接发送频率
 * 逐步扩大连接数,当连接数最大
 * 逐步扩大每个连接发送频率
 * 观察发送qps
 * @author walker
 *
 */
public class ClientTest10NoUI implements UiCall{
	int dtime = 100;	//每次连接 登录间隔延时
	int dtimesend = 100;	//每批次发送间隔
	int dtimesendDeta = 0;	//减少量
	int dtimenewconnection = 2000;	//每批次连接添加间隔 或者发送频率变更间隔
	int num = 10;	//每批次添加连接
	int countFalse = 0; 	//添加连接批次异常次数 5次之后停止 新连接 而开始加大发送消息评频率
	int countFalseMax = 2;
	int maxNum = 101;	//最大连接数161
	List<Client> cc = new ArrayList<Client>();
	AtomicLong count = new AtomicLong(0L);
	ClientTest10NoUI() throws Exception{
		startMakeClient();
		startSend();
		startMakeSend();
		
	}
	public void startSend() {
		//开启循环发送 每个连接 发送  一批次  每个都发送 每秒每个连接发送10次
		new Thread() {
			public void run() {
				while(!Thread.interrupted()) {
					new Thread() {
						public void run() {
							Tools.out("开始发送批次", cc.size(), dtimesend);
							for(int i = cc.size() - 1; i >= 0; i--) {
								Client client = cc.get(i);
								if(client.isStart()) {
									try {
										sendAllUser(client);
									} catch (Exception e) {
			//							e.printStackTrace();
										
									}
								}else {
									Tools.out( "移除断开连接", i, client.toString());
									cc.remove(i);
								}
							}
			//				Tools.out("开始发送批次" + cc.size(), "完成");
						}
					}.start();
					ThreadUtil.sleep(dtimesend);
				}
			}
		}.start();
	}
	int before = 0;	//上次添加完连接后的数量 若有减少
	public void startMakeClient() {
		//开启增长一个批次的连接
		ThreadUtilClient.scheduleWithFixedDelay(new Thread() {
			public void run() {
				Tools.out("当前连接数" + cc.size(), countFalse);
//				if(countFalse > countFalseMax)return;
				
				if(cc.size() < maxNum - num ) {
					Tools.out("创建连接数" + num);
					for(int i = 0; i < num; i++) {
						Client client;
						try {
							client = newConnect();
							cc.add(client);
							ThreadUtil.sleep(dtime);
							login(client);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
				int after = cc.size();
				Tools.out("连接数", before, after, "完成", countFalse);
				if(after < before + num || after >= maxNum) {	
					countFalse ++;
					Tools.out("有部分失败新连接或者到了最大连接数", before, after, countFalse);
				}else if(countFalse > 0){	//连续计数5次标示失败
					countFalse --;
				}
				before = cc.size();

			}
		}, 3000, dtimenewconnection, TimeUnit.MILLISECONDS);
	}
	public void startMakeSend() {
		//开启增长一个批次的连接
		ThreadUtilClient.scheduleWithFixedDelay(new Thread() {
			public void run() {
				int to = dtimesend - dtimesendDeta;
				if(to > 10 && countFalse > countFalseMax) {
					Tools.out("当前发送间隔",dtimesend,"降低间隔至", to);
					dtimesend = to;
				}
			}
		}, 3000, dtimenewconnection, TimeUnit.MILLISECONDS);
	}

	private Client newConnect() throws Exception {
		Client res = new ClientNetty("127.0.0.1", 8092);
		res.setUI(this);
		res.start();
		Tools.out("创建新连接", res.toString());
		return res;
	}
	public void show(Client client) {

	}
	public void login(Client client) throws Exception {
		client.send("{type:login,data:{user:" + Tools.getRandomNum(10, 99, 2) + ",pwd:123456} }");
	}
	public void sendAllUser(Client client) throws Exception {
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
		while(true) {}
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
