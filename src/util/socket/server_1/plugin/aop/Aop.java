package util.socket.server_1.plugin.aop;

import org.apache.log4j.Logger;

import util.Bean;
import util.socket.server_1.*;
import util.socket.server_1.session.*;

public abstract class Aop<T> {
	protected Logger log = Logger.getLogger(Aop.class); 

	Bean params;
	Aop(Bean params){
		this.params = params;
	}
	public abstract Boolean doAop(final Msg msg);
}
