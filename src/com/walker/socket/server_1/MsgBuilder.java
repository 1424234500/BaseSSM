package com.walker.socket.server_1;

import com.walker.common.util.Bean;
import com.walker.socket.server_1.session.Session;

/**
 * 构造各种消息格式
 * @author walker
 *
 */
public class MsgBuilder {
	
	
	
	
	public static <T> Object makeException(Session<T> session, Msg msg, Exception e) {
		return new Msg().setType("exception").setData(new Bean().set("msg", msg).set("info", e.toString()));
	}

	public static <T> Object makeOnLogin(Session<T> session, Bean bean) {
//		return new Bean().set(TYPE, TYPE_EVENT).set(DATA, bean.set("type", "onlogin"));
		return new Msg().setType("onlogin").setData(bean.set("session", session));
	}

	public static <T> Object makeOnUnLogin(Session<T> session, Bean bean) {
//		return new Bean().set(TYPE, TYPE_EVENT).set(DATA, bean.set("type", "onunlogin"));
		return new Msg().setType("onunlogin").setData(bean.set("session", session));
	}

	
	
	
}
