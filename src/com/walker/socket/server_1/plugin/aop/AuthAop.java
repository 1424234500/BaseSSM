package com.walker.socket.server_1.plugin.aop;

import com.walker.common.util.Bean;
import com.walker.socket.server_1.Msg;
import com.walker.socket.server_1.session.Session;

public class AuthAop<T> extends Aop<T>{

	AuthAop(Bean params) {
		super(params);
	}

	@Override
	public Boolean doAop(Msg msg) {
		Boolean res = true;
		if(msg.getUserFrom().length() == 0) {
			log.warn(this.params.get("tip", "") + msg.getData());
			res = false;
		}
		return res;
	}

}
