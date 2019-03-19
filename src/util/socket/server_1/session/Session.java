package util.socket.server_1.session;

import java.util.*;

import util.Bean;
import util.JsonUtil;
import util.socket.server_1.Msg;

public class Session<T> {
	/**
	 * 会话信息
	 */
	String id;	//登录用户
	Set<String> subscribeKeys;	//订阅keys
	
	
	/**
	 * socket实体以及 key send实现回调
	 */
	Socket<T> socket;	
	
	public Session(Socket<T> socket) {
		this.socket = socket;
		subscribeKeys = new HashSet<>();
	}
	public void setSession(Socket<T> socket) {
		this.socket = socket;
	}
	private void send(Msg msg) {
		
		session.socket.send( );
	}
	public String getKey() {
		return this.socket.key();
	}

	/**
	 * 判定session是否相同
	 */
	@Override
	public boolean equals(Object obj) {
		@SuppressWarnings("unchecked")
		Session<T> to = (Session<T>) obj;
		return this.socket.key().equals(to.socket.key());
		//return super.equals(obj);
	}

	@Override
	public String toString() {
		return "Session[key=" + getKey() + " socket=" + socket + "]";
	}
	
	
	/**
	 * session负责自己处理业务
	 * @param msg
	 */
	public void onData(Object msg) {
		
		

		
		
		
	}
	
	public void onLogin(Object msg) {
		Bean bean = JsonUtil.get(msg.toString());
		String user = bean.get("id", "");
		String pwd = bean.get("pwd", "");
		
		Msg res = new Msg();
		res.setTo(id);
		res.setInfo("hello login");
		res.setData(msg);
		
		send(res);
		
	}
	
	
	
}
