package util.socket.server_1;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import util.Bean;
import util.JsonUtil;
import util.LangUtil;
import util.MapListUtil;
import util.Tools;

/**
 * socket 传递 消息结构 
 *
 */
public class Msg{
	final private static String SPLIT = ",";
	
	String type = "";
	
	
	/**
	 * 需要写入 socket 传递的业务信息
	 */
	final private static String KEY_ID = "id";		//消息id 没有则生成
	final private static String KEY_FROM = "from";	//业务发送方 user_id
	final private static String KEY_TO = "to";		//业务接收方 user_id
	final private static String KEY_DATA = "data";	//具体消息body
	final private static String KEY_INFO = "info";	//说明
	final private static String KEY_RES = "ok";		//是否成功 true false
	
	final private static String KEY_TYPE = "type";	//消息类型 业务处理分类plugin
	final private static String TYPE_EVENT = "event";

	/**
	 * msg上下文处理相关信息
	 */
	final private static String CONTEXT_FROM = "_from";	//socket发送方 key
	final private static String CONTEXT_TO = "_to";		//socket来源 key
	final private static String CONTEXT = "aaa";
	String _from = "";
	String _to = "";
	public Msg setKeyFrom(String key) {
		this._from = key;
		return this;
	}
	public Msg setKeyTo(String key) {
		this._to = key;
		return this;
	}
	public String getKeyFrom() {
		return this._from;
	}
	public String getKeyTo() {
		return this._to;
	}
	
	
//	final private static String KEY_SESSION = "session";	//发送方 消息绑定session
//	Object fromSession;
	
	String id = "";
	String from = "";
	Set<String> to = new HashSet<>();
	Bean data = new Bean();
	String info = "";
	boolean ok = false;
	
	@Override
	public String toString() {
		if(id.length() == 0) {
			id = "msg_"+LangUtil.getUUID();
		}
		StringBuilder tt = new StringBuilder();
		for(String tid : to) {
			tt.append(SPLIT).append(tid);
		}
		String ttt = tt.toString().substring(SPLIT.length());
		Bean bean = new Bean()
				.set(KEY_ID, id)
				.set(KEY_FROM, from)
				.set(KEY_TO, ttt)
				.set(KEY_DATA, data)
				.set(KEY_RES, ok)
				.set(KEY_INFO, info);
		return bean.toString();
	}
	public Msg(){}
	public Msg(String json){
		Bean bean = JsonUtil.get(json);
		setId(bean.get(KEY_ID, ""));
		setFrom(bean.get(KEY_FROM, ""));
		setTo(bean.get(KEY_TO, "").split(SPLIT));
		setData(bean.get(KEY_DATA, new Bean()));
		setOk(bean.get(KEY_RES, false));
		setInfo(bean.get(KEY_INFO, ""));

	}
	public Msg setId(String id) {
		this.id = id;
		return this;
	}
	public String getId() {
		return id;
	}
	public String getFrom() {
		return from;
	}
	public Msg setFrom(String from) {
		this.from = from;
		return this;
	}
	public Set<String> getTo() {
		return to;
	}
	public Msg setTo(String...strings) {
		for(String str : strings) {
			this.to.add(str);
		}
		return this;
	}
	public Msg setTo(Collection<? extends String> to) {
		this.to.addAll(to);
		return this;
	}
	public Bean getData() {
		return data;
	}
	public Msg setData(Bean data) {
		this.data = data;
		return this;
	}

	public String getInfo() {
		return info;
	}
	public Msg setInfo(String info) {
		this.info = info;
		return this;
	}
	public boolean isOk() {
		return ok;
	}
	public Msg setOk(Boolean ok) {
		this.ok = ok;
		return this;
	}

	public String getType() {
		return type;
	}
	public Msg setType(String type) {
		this.type = type;
		return this;
	}
//	public Msg setFromSession(Object obj) {
//		this.fromSession = obj;
//		return this;
//	}
//	@SuppressWarnings("unchecked")
//	public <T> T getFromSession() {
//		return (T)this.fromSession;
//	}
	
}
