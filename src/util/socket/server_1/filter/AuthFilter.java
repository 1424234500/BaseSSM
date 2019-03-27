package util.socket.server_1.filter;

import util.Bean;
import util.socket.server_1.Msg;
import util.socket.server_1.session.Session;

public class AuthFilter<T> extends Filter<T>{

	AuthFilter(Bean params) {
		super(params);
	}

	@Override
	public Boolean doFilter(Session<T> session, Msg msg) {
		Boolean res = true;
		if(!session.isLogin()) {
			log.warn(this.params.get("tip", "") + " ");
			res = false;
		}
		return res;
	}

}
