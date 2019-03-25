package util.socket.server_1.plugin;

import util.Bean;
import util.socket.server_1.*;
import util.socket.server_1.session.*;

public class LoginPlugin<T> extends Plugin<T>{

	LoginPlugin(Bean params) {
		super(params);
	}

	@Override
	public void onData(Session<T> session, Msg msg) {
		Bean data = msg.getData();
		String userId = data.get("user", "");
		String pwd = data.get("pwd", "");
		
		session.onLogin(data);
		
	}

}
