package util.socket.server_1;

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
	final private static String KEY_ID = "id";
	final private static String KEY_FROM = "from";
	final private static String KEY_TO = "to";
	final private static String KEY_DATA = "data";
	final private static String KEY_INFO = "info";
	
	String id;
	String from;
	Set<String> to = new HashSet<>();
	Object data;
	String info;
	
	@Override
	public String toString() {
		Bean bean = new Bean()
				.set(KEY_ID, "msg_"+LangUtil.getUUID())
				.set(KEY_FROM, from)
				.set(KEY_TO, to)
				.set(KEY_DATA, data)
				.set(KEY_INFO, info);
		return bean.toString();
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public Set<String> getTo() {
		return to;
	}
	public void setTo(String...strings) {
		for(String str : strings) {
			this.to.add(str);
		}
	}
	public void setTo(Collection<? extends String> to) {
		this.to.addAll(to);
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}

	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}


}
