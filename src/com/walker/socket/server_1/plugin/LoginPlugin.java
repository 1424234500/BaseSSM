package com.walker.socket.server_1.plugin;

import com.walker.common.util.Bean;
import com.walker.socket.server_1.Msg;

public class LoginPlugin<T> extends Plugin<T>{

	LoginPlugin(Bean params) {
		super(params);
	}

	/**
	 * {type:login,sto:,sfrom:128.2.3.1\:9080,from:,to:,data:{user:123,pwd:123456} }	
	 */
	@Override
	public void onData(Msg msg) {
		Bean data = (Bean) msg.getData();
		String userId = data.get("user", "");
		String pwd = data.get("pwd", "");
		
//		msg.setType("onlogin");
//		session.onLogin(data);
		pub.publish(msg.getFrom(), msg);
		
	}

}
