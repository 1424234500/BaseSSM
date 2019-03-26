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
 * server发给某用户一条消息  session:socket:key
 * 
 * type	消息类型
 * data 消息体
 * 
 * type:event
 * data:{
 * 		name: onlogin
 * 		user: 000
 * 		token: xxxx
 * }
 * 			
 * 						type:message
 * 						data.to		发给目标用户	u_123,u_2323,g_xxx,s_all,s_online
 *						data.from	发送方来源	u_123,s_admin
 *						data.type	具体消息类型	text,image,voice,video,map
 *						data.body
 * 					
 *
 */
public class MsgDown extends Bean{
	private static final long serialVersionUID = 1L;

	final private static String KEY_TYPE = "type";
	final private static String KEY_DATA = "data";

	final private static String KEY_DATA_TYPE = "type";
	final private static String KEY_DATA_BODY = "body";
	final private static String KEY_DATA_FROM = "from";
	final private static String KEY_DATA_TO = "to";
	
	public MsgDown() {
		setData(new Bean());
	}
	public MsgDown setType(String type) {
		this.put(KEY_TYPE, type);
		return this;
	}
	public String getType(String type) {
		return this.get(KEY_TYPE, "");
	}
	
	public MsgDown setData(Bean bean) {
		this.put(KEY_DATA, bean);
		return this;
	}
	public Bean getData() {
		return (Bean) this.get(KEY_DATA);
	}
	public MsgDown setDataFrom(String from) {
		getData().set(KEY_DATA_FROM, from);
		return this;
	}
	public MsgDown setDataTo(String to) {
		getData().set(KEY_DATA_TO, to);
		return this;
	}
	public MsgDown setDataType(String type) {
		getData().set(KEY_DATA_TYPE, type);
		return this;
	}
	public String getDataTo() {
		return getData().get(KEY_DATA_TO, "");
	}
	public String getDataType() {
		return getData().get(KEY_DATA_TYPE, "");
	}
	
	public MsgDown setDataBody(Object body) {
		getData().set(KEY_DATA_BODY, body);
		return this;
	}
	public Object seDataBody() {
		return getData().get(KEY_DATA_BODY);
	}
	
}
