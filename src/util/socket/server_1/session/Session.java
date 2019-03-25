package util.socket.server_1.session;

import java.util.*;

import org.apache.log4j.Logger;

import util.Bean;
import util.JsonUtil;
import util.route.SubPub;
import util.route.SubPub.OnSubscribe;
import util.route.SubPubMgr;
import util.socket.server_1.Msg;

public class Session<T> implements OnSubscribe<Msg> {
	private static Logger log = Logger.getLogger(Session.class); 

	/**
	 * 会话信息
	 */
	String id;	//登录用户
	Set<String> subscribeKeys;	//订阅keys

    /**
     * 路由 发布订阅
     */
    private SubPub<Msg> sub = SubPubMgr.getSubPub("msg_route", 0);
	
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
	public String getKey() {
		return this.socket.key();
	}
	public void send(Object obj) {
		this.socket.send(obj);
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
	 * 登录成功后 订阅消息 单聊群聊特殊规则
	 */
	public void onLogin(Object msg) {
		sub.subscribe(id, this);
		sub.subscribe("all", this);
		sub.subscribe("online", this);
		
	}
	/**
 	 * session负责自己处理业务
	 */
	@Override
	public Type onSubscribe(Msg object) {
		log.info(object);
		send(object);
		return Type.DEFAULT;
	}
	
	
	
}
