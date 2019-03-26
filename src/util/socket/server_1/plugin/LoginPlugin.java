package util.socket.server_1.plugin;

import util.Bean;
import util.socket.server_1.*;
import util.socket.server_1.session.*;

public class LoginPlugin<T> extends Plugin<T>{

	LoginPlugin(Bean params) {
		super(params);
	}

	/**
	 * {type:login,data:{user:123,pwd:123456} }	
	 */
	@Override
	public void onData(Session<T> session, MsgUp msg) {
		Bean data = (Bean) msg.getData();
		String userId = data.get("user", "");
		String pwd = data.get("pwd", "");
		
		session.onLogin(data);
		
	}

}
