package util.socket.server_1.plugin;

import util.Bean;
import util.TimeUtil;
import util.socket.server_1.Msg;

public class EchoPlugin<T> extends Plugin<T>{

	EchoPlugin(Bean params) {
		super(params);
	}

	@Override
	public void onData(Msg msg) {
//		session.send(new Bean().set("plugin", "echo").set("params", params).set("data", msg)
//				.set("time", TimeUtil.getTimeYmdHmss()));
		msg.setType("echo");
		msg.setInfo("from echo");
		pub.publish(msg.getFrom(), msg);
	}

}
