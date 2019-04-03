package com.walker.socket.server_1;

import com.walker.common.util.Bean;
import com.walker.common.util.JsonUtil;
import com.walker.socket.server_1.session.Session;

/**
 * socket 传递 消息结构 
 * 
 * who 发来了一条消息  session:socket:key
 * {type:login,data:{user:walker,pwd:1234,device:xxxxx} }	
 * {type:message,data:{user:walker,pwd:1234} }		
 * 
 * 客户端写入数据
 * 	type:login	登录请求
 * 	data:{}		登录请求的参数
 * 
 * 	time_client	客户端发送时间
 * 	
 * 		
 */
@SuppressWarnings("unchecked")
public class Msg extends Bean{
	private static final long serialVersionUID = 1L;
	final public static String SPLIT = ",";
	
	//系统上下文 函数调用控制

	//记录关键时间节点 统计计算
	//time_client - 网络传输耗时 - time_receive - 队列等待耗时 - time_do - 业务处理耗时 - time_send
	final private static String KEY_TIME_CLIENT = "time_client";	//client send time
	final private static String KEY_TIME_RECEIVE = "time_reveive";	//server receive time to pipe
	final private static String KEY_TIME_DO = "time_do";			//server consumer time
	final private static String KEY_TIME_SEND = "time_send";		//server send time
	
	final private static String KEY_WAIT_SIZE = "wait_size";		//pipe 队列等待深度
	final private static String KEY_INFO = "info";					//about

	
	//记录socket收发ip port key
	final private static String KEY_FROM = "sfrom";	//socket from
	final private static String KEY_TO = "sto";		//socket to
	//记录业务 类型 参数
	final private static String KEY_TYPE = "type";	//plugin type
	final private static String KEY_USER_FROM = "from";	//user from
	final private static String KEY_USER_TO = "to";		//user to
	
	final private static String KEY_DATA = "data";	//msg data bean
	
	public Msg() {}
	public Msg(String json) {
		int t = JsonUtil.getType(json);
		if(t == 1){
			Bean bean = (Bean)JsonUtil.get(json);
			this.putAll(bean);	//是否过滤非必须字段 
//			this.setType(bean.get(KEY_TYPE, ""));//确保type
//			this.setUserTo(bean.get(KEY_USER_TO, ""));
//			this.setUserFrom(bean.get(KEY_USER_FROM, ""));
//			this.setData(bean.get(KEY_DATA));
			
			if(this.getTimeClient() == 0) {//避免不传导致 计算异常
				this.setTimeClient(System.currentTimeMillis());//确保type
			}
		}else {
			this.setType("echo");
			this.setData(new Bean().set("json", json));
		}
	}
	public Msg(String json, Session<?> session) {
		this(json);
		
		//设置来源socket key session<T> 
		this.setFrom(session.getKey());
		
		//设置userFrom当前用户 若消息包含了from 则不设置 允许顶替发消息
		if(this.getUserFrom().length() == 0) {
			this.setUserFrom(session.getUser());
		}
		
	}
	
	
	public Msg setFrom(String from) {
		this.set(KEY_FROM, from);
		return this;
	}
	public Msg setTo(String to) {
		this.set(KEY_TO, to);
		return this;
	}
	public Msg setType(String type) {
		this.set(KEY_TYPE, type);
		return this;
	}
	public Msg setData(Object data) {
		this.set(KEY_DATA, data);
		return this;
	}
	public String getFrom() {
		return this.get(KEY_FROM, "");
	}
	public String getTo() {
		return this.get(KEY_TO, "");
	}
	public String getType() {
		return this.get(KEY_TYPE, "");
	}
	public <T> T getData() {
		return (T)this.get(KEY_DATA);
	}
	
	

	public Msg setUserFrom(String from) {
		this.set(KEY_USER_FROM, from);
		return this;
	}
	public Msg setUserTo(String to) {
		this.set(KEY_USER_TO, to);
		return this;
	}
	public String[] getUserTo() {
		return this.get(KEY_USER_TO, "").split(SPLIT);
	}
	public String getUserFrom() {
		return this.get(KEY_USER_FROM, "");
	}
	

	public Msg setTimeClient(long timeMill) {
		this.set(KEY_TIME_CLIENT, timeMill);
		return this;
	}
	public Long getTimeClient() {
		return this.get(KEY_TIME_CLIENT, 0L);
	}
	public Msg setTimeReceive(long timeMill) {
		this.set(KEY_TIME_RECEIVE, timeMill);
		return this;
	}
	public Long getTimeReceive() {
		return this.get(KEY_TIME_RECEIVE, 0L);
	}
	public Msg setTimeDo(long timeMill) {
		this.set(KEY_TIME_DO, timeMill);
		return this;
	}
	public Long getTimeDo() {
		return this.get(KEY_TIME_DO, 0L);
	}
	public Msg setTimeSend(long timeMill) {
		this.set(KEY_TIME_SEND, timeMill);
		return this;
	}
	public Long getTimeSend() {
		return this.get(KEY_TIME_SEND, 0L);
	}
	
	public Msg setWaitSize(Long size) {
		this.set(KEY_WAIT_SIZE, size);
		return this;
	}
	public Long getWaitSize() {
		return this.get(KEY_WAIT_SIZE, 0L);
	}
	public Msg setInfo(Object info) {
		this.set(KEY_INFO, info);
		return this;
	}
	public Object getInfo() {
		return this.get(KEY_INFO);
	}
}
