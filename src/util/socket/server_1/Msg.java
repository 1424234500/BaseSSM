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
import util.socket.server_1.session.Session;

/**
 * socket 传递 消息结构 
 * 
 * who 发来了一条消息  session:socket:key
 * 			   msg
 * 					//由服务端设置
 * 					from key	来源			socket:key
 * 					to	 key	去向			socket:key
 * 					//由客户端发送
 * 					type plugin 函数调用		login,message
 * 					data onData 函数调用参数	bean
 * 
 * 					//plugin调用
 * 					login 登录操作  未登录只能调用此plugin 此时没有self userId
 * 						bean.user:000900
 * 						bean.pwd:123456
 * 						bean.device:asjfkajxkjakjdf
 *							
 *					message 发给user/group 请求转发
 *						bean.to		发给目标用户	u_123,u_2323,g_xxx,s_all,s_online
 *						bean.from	发送方来源	u_123,s_admin
 *						bean.type	具体消息类型	text,image,voice,video,map
 *
 */
public class Msg extends Bean{
	private static final long serialVersionUID = 1L;
	final private static String SPLIT = ",";
	final private static String KEY_FROM = "from";
	final private static String KEY_TO = "to";
	
	final private static String KEY_TYPE = "type";
	final private static String KEY_DATA = "data";
	
	public Msg(String json) {
		Bean bean = JsonUtil.get(json);
		this.setType(bean.get(KEY_TYPE, ""));
		this.setData(bean.get(KEY_DATA, new Bean()));
	}
	public Msg(String json, Session<?> session) {
		this(json);
		this.setFrom(session.getKey());//设置来源socket key session<T>
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
	public Msg setData(Bean data) {
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
	public Bean getData() {
		return this.get(KEY_DATA, new Bean());
	}
	

	
}
