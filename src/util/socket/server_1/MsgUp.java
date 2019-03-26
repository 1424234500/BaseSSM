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
 *					{type:login,data:{user:walker,pwd:1234,device:xxxxx} }		
 * 					login 登录操作  未登录只能调用此plugin 此时没有self userId
 * 						bean.user:000900
 * 						bean.pwd:123456
 * 						bean.device:asjfkajxkjakjdf
 * 
 *					{type:message,data:{user:walker,pwd:1234} }		
 *					message 发给user/group 请求转发
 *						data.to		发给目标用户	u_123,u_2323,g_xxx,s_all,s_online
 *						data.from	发送方来源	u_123,s_admin
 *						data.type	具体消息类型	text,image,voice,video,map
 *						data.body
 *
 *	ip mac路由模拟
 *	ip		user:id		login 			用户收发消息都用于user:id
 *	Arp Rarp  ip/id <-> mac/key
 *  mac		socket:key	onConnection	而机器转发都用于 socket:key
 *	
 *
 */
public class MsgUp extends Bean{
	private static final long serialVersionUID = 1L;
	final private static String SPLIT = ",";
	
	//系统上下文 函数调用控制
	final private static String KEY_FROM = "from";
	final private static String KEY_TO = "to";
	
	final private static String KEY_TYPE = "type";
	final private static String KEY_DATA = "data";
	
	//业务上下文 
	
	
	public MsgUp(String json) {
		int t = JsonUtil.getType(json);
		if(t == 1){
			Bean bean = (Bean)JsonUtil.get(json);
			this.setType(bean.get(KEY_TYPE, ""));
			this.setData(bean.get(KEY_DATA));
		}else {
			this.setType("echo");
			this.setData(new Bean().set("json", json));
		}
	}
	public MsgUp(String json, Session<?> session) {
		this(json);
		this.setFrom(session.getKey());//设置来源socket key session<T>
	}
	
	
	public MsgUp setFrom(String from) {
		this.set(KEY_FROM, from);
		return this;
	}
	public MsgUp setTo(String to) {
		this.set(KEY_TO, to);
		return this;
	}
	public MsgUp setType(String type) {
		this.set(KEY_TYPE, type);
		return this;
	}
	public MsgUp setData(Object data) {
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
	public Object getData() {
		return this.get(KEY_DATA);
	}
	

	
}
