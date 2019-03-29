package util.socket.server_1.plugin.aop;

import util.Bean;
import util.socket.server_1.Msg;
import util.socket.server_1.session.Session;

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
