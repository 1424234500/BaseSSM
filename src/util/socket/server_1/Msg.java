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
	final private static String KEY_ID = "id";
	final private static String KEY_FROM = "from";	//发送方 自动附加from
	final private static String KEY_TO = "to";		//接收方 不需要知道to 
	final private static String KEY_DATA = "data";
	final private static String KEY_INFO = "info";
	final private static String KEY_RES = "ok";
	
//	final private static String KEY_SESSION = "session";	//发送方 消息绑定session
//	Object fromSession;
	
	String id = "";
	String from = "";
	Set<String> to = new HashSet<>();
	Object data = "";
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
		setData(bean.get(KEY_DATA, null));
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
	public Object getData() {
		return data;
	}
	public Msg setData(Object data) {
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

//	public Msg setFromSession(Object obj) {
//		this.fromSession = obj;
//		return this;
//	}
//	@SuppressWarnings("unchecked")
//	public <T> T getFromSession() {
//		return (T)this.fromSession;
//	}
	
}
