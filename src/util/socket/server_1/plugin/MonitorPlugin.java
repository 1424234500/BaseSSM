package util.socket.server_1.plugin;

import util.Bean;
import util.socket.server_1.Msg;
import util.socket.server_1.netty.handler.SessionHandler;

/**
 * 监控 数据测试
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
		msg.setType("echo");
		msg.setData(res);
		pub.publish(msg.getFrom(), msg);
	}

}
