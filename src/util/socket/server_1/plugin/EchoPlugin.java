package util.socket.server_1.plugin;

import util.Bean;
import util.TimeUtil;
import util.socket.server_1.*;
import util.socket.server_1.session.*;

public class EchoPlugin<T> extends Plugin<T>{

	EchoPlugin(Bean params) {
		super(params);
	}

	@Override
	public void onData(Session<T> session, MsgUp msg) {
		session.send(new Bean().set("plugin", "echo").set("params", params).set("data", msg)
				.set("time", TimeUtil.getTimeYmdHmss()));
	}

}
