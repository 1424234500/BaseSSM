package util.socket.server_1.filter;

import org.apache.log4j.Logger;

import util.Bean;
import util.socket.server_1.*;
import util.socket.server_1.session.*;

public abstract class Filter<T> {
	protected Logger log = Logger.getLogger(Filter.class); 

	Bean params;
	Filter(Bean params){
		this.params = params;
	}
	abstract Boolean doFilter(Session<T> session, MsgUp msg);
}
