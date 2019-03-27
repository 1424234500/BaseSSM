package util.socket.server_1.filter;

import util.Bean;
import util.socket.server_1.Msg;
import util.socket.server_1.session.Session;

public class MonitorFilter<T> extends Filter<T>{

	MonitorFilter(Bean params) {
		super(params);
	}

	@Override
	public Boolean doFilter(Session<T> session, Msg msg) {
		
		log.debug("Monitor " + session.toString() + " " + msg.toString());
		
		return true;
	}

}
