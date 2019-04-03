package com.walker.socket.server_1.session;

import org.apache.log4j.Logger;

import com.walker.common.util.Bean;
import com.walker.common.util.ThreadUtil;
import com.walker.core.route.SubPub;
import com.walker.core.route.SubPubMgr;
import com.walker.core.route.SubPub.OnSubscribe;
import com.walker.socket.server_1.Msg;
import com.walker.socket.server_1.MsgBuilder;

/**
 * 会话 关联socket user
 * 
 * 订阅模式管理分发
 * 
 * 建立连接 订阅socket
 * 		登录成功 订阅user
 * 			订阅到消息 写入socket
 * 		退出登录	取消订阅user
 * 断开连接 取消订阅socket
 * 
 */
public class Session<T> implements OnSubscribe<Msg> {
	private static Logger log = Logger.getLogger(Session.class); 

	/**
	 * ip
	 */
	String id = "";
	/**
	 * mac
	 */
	String key = "";
	
    /**
     * 路由 发布订阅
     */
    SubPub<Msg> sub = SubPubMgr.getSubPub("msg_route", 0);
	
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
	public Session<T> setUser(String id) {
		this.id = id;
		return this;
	}
	/**
	 * 判定session是否相同
	 */
	@Override
	public boolean equals(Object obj) {
		@SuppressWarnings("unchecked")
		Session<T> to = (Session<T>) obj;
		return getKey().equals(to.getKey());
		//return super.equals(obj);
	}

	@Override
	public String toString() {
		return "Session[" + getKey() + " " + getUser() + "]";
	}
	
	 
	public Boolean isLogin() {
		return getUser().length() != 0;
	}
	/**
	 * 长连接成功后 订阅socket消息
	 */
	public void onConnect() {
		sub.subscribe(getKey(), this); 	//订阅当前socket
		sub.subscribe("all_socket", this);		//订阅所有socket
	}
	public void onUnConnect() {
		sub.unSubscribe(getKey(), this); 	//订阅当前socket
		sub.unSubscribe("all_socket", this);	//订阅所有socket
	}
	/**
	 * 登录成功后 订阅用户消息 单聊群聊特殊规则
	 * 注册Rarp ip -> session
	 */
	public void onLogin(Bean bean) {
		this.onUnLogin(bean);
		this.id = bean.get("user", "");
		sub.subscribe(getUser(), this);	//订阅当前登录用户userid
		sub.subscribe("all_user", this);		//订阅所有登录用户
		send(MsgBuilder.makeOnLogin(this, bean));
		log.info("login ok " + this.toString() );
	}
	public void onUnLogin(Bean bean) {
		sub.unSubscribe(getUser(), this);	//订阅当前登录用户userid
		sub.unSubscribe("all_user", this);		//订阅所有登录用户
		setUser("");
//		send(MsgBuilder.makeOnUnLogin(this, bean));
		log.info("unlogin ok " + this.toString() );
	}
	/**
 	 * session负责自己处理业务
	 */
	@Override
	public Type onSubscribe(Msg msg) {

//		log.debug("onSubscribe " + msg);
		if(msg.getType().equals("login")) {
			this.onLogin((Bean) msg.getData());
		}else if(msg.getType().equals("monitor")) {
			send(msg.getData());
		}else {
			msg.setTo(getKey());
			send(msg);
		}
		//模拟写入socket耗时
//		ThreadUtil.sleep(20);
		
		return Type.DEFAULT;
	}
	public void send(Object obj) {
		this.socket.send(obj.toString());
	}
	
	
	
}
