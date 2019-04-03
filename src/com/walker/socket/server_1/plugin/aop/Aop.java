package com.walker.socket.server_1.plugin.aop;

import org.apache.log4j.Logger;

import com.walker.common.util.Bean;
import com.walker.socket.server_1.*;
import com.walker.socket.server_1.session.*;

public abstract class Aop<T> {
	protected Logger log = Logger.getLogger(Aop.class); 

	Bean params;
	Aop(Bean params){
		this.params = params;
	}
	public abstract Boolean doAop(final Msg msg);
}
