package com.walker.socket.server_1.plugin;

import com.walker.common.util.Bean;
import com.walker.socket.server_1.Msg;
import com.walker.socket.server_1.netty.handler.SessionHandler;

/**
 * 数据监控工具
 * @author walker
 *
 * @param <T>
 */
public class MonitorPlugin<T> extends Plugin<T>{

	MonitorPlugin(Bean params) {
		super(params);
	}

	@Override
	public void onData(Msg msg) {
		Bean bean = (Bean)msg.getData();
		Object res = msg;
//		session.send(new Bean().set("plugin", "echo").set("params", params).set("data", msg)
//				.set("time", TimeUtil.getTimeYmdHmss()));
		if(bean.get("type", "").equals("show")) {
			res = SessionHandler.sessionService.show();
		}
		msg.setData(res);
		
		//模拟存储耗时
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		pub.publish(msg.getFrom(), msg);
	}

}
