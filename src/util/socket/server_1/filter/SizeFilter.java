package util.socket.server_1.filter;

import util.Bean;
import util.socket.server_1.Msg;
import util.socket.server_1.session.Session;

public class SizeFilter<T> extends Filter<T>{
	
	SizeFilter(Bean params) {
		super(params);
	}

	@Override
	public Boolean doFilter(Session<T> session, Msg msg) {
		boolean res = true;
		int length = msg.toString().length();
		int maxSize = this.params.get("size", 0);
		if(maxSize > 0 && length > maxSize ) {
			log.warn(this.params.get("tip", "") + " " + maxSize);
			res = false;
		}
		return res;
	}

	
	
}
