package util.socket.server_1.session;

import java.util.*;

import org.apache.log4j.Logger;

import util.Bean;
import util.JsonUtil;
import util.route.SubPub;
import util.route.SubPub.OnSubscribe;
import util.route.SubPubMgr;
import util.socket.server_1.MsgUp;
import util.socket.server_1.MsgBuilder;
import util.socket.server_1.MsgDown;

/**
 * Arp ip:user mac:key转换绑定元
 *
 * @param <T>
 */
public class Session<T> implements OnSubscribe<MsgDown> {
	private static Logger log = Logger.getLogger(Session.class); 

	/**
	 * ip
	 */
	String id;
	/**
	 * mac
	 */
	String key;
	
    /**
     * 路由 发布订阅
     */
    SubPub<MsgDown> sub = SubPubMgr.getSubPub("msg_route", 0);
	
	/**
	 * socket实体以及 key send实现回调
	 */
	Socket<T> socket;	
	
	public Session(Socket<T> socket) {
		setSession(socket);
	}
	public void setSession(Socket<T> socket) {
		this.socket = socket;
		this.key = socket.key();
	}
	public String getKey() {
		return this.key;
	}
	public String getUser() {
		return this.id;
	}

	/**
	 * 判定session是否相同
	 */
	@Override
	public boolean equals(Object obj) {
		@SuppressWarnings("unchecked")
		Session<T> to = (Session<T>) obj;
		return this.key.equals(to.key);
		//return super.equals(obj);
	}

	@Override
	public String toString() {
		return "Session[key=" + this.key + " user=" + this.id + "]";
	}
	
	 
	public Boolean isLogin() {
		return this.id.length() != 0;
	}
	/**
	 * 长连接成功后 订阅socket消息
	 */
	public void onConnect() {
		sub.subscribe(this.key, this); 	//订阅当前socket
		sub.subscribe("all_socket", this);		//订阅所有socket
	}
	public void onUnConnect() {
		sub.unSubscribe(this.key, this); 	//订阅当前socket
		sub.unSubscribe("all_socket", this);	//订阅所有socket
	}
	/**
	 * 登录成功后 订阅用户消息 单聊群聊特殊规则
	 * 注册Rarp ip -> session
	 */
	public void onLogin(Bean bean) {
		this.id = bean.get("user", "");
		sub.subscribe(this.id, this);	//订阅当前登录用户userid
		sub.subscribe("all_user", this);		//订阅所有登录用户
		send(MsgBuilder.makeOnLogin(this, bean));
		log.info("login ok " + this.toString() );
	}
	public void onUnLogin(Bean bean) {
		sub.unSubscribe(this.id, this);	//订阅当前登录用户userid
		sub.unSubscribe("all_user", this);		//订阅所有登录用户
		this.id = "";
		send(MsgBuilder.makeOnUnLogin(this, bean));
		log.info("unlogin ok " + this.toString() );
	}
	/**
 	 * session负责自己处理业务
	 */
	@Override
	public Type onSubscribe(MsgDown msg) {
		log.debug("onSubscribe " + msg);
		send(msg);
		return Type.DEFAULT;
	}
	public void send(Object obj) {
		if(this.socket != null) {
			this.socket.send(obj.toString());
			log.debug(this.toString() + "send " + obj.toString() + " ");
		}else {
			log.error("socket is null ? " + obj.toString());
		}
	}
	
	
	
}
