package com.walker.socket.server_1.plugin.aop;

import com.walker.common.util.Bean;
import com.walker.socket.server_1.Msg;
import com.walker.socket.server_1.session.Session;

public class SizeAop<T> extends Aop<T>{
	
	SizeAop(Bean params) {
		super(params);
	}

	@Override
	public Boolean doAop(Msg msg) {
		boolean res = true;
		int length = msg.toString().length();
		int maxSize = this.params.get("size", 0);
		if(maxSize > 0 && length > maxSize ) {
			log.warn(this.params.get("tip", "") + " " + maxSize + " " + msg.getData().toString().substring(0, 20));
			res = false;
		}
		return res;
	}

	
	
}
