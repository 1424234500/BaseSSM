package com.walker.socket.server_1.plugin;

import com.walker.common.util.Bean;
import com.walker.common.util.ThreadUtil;
import com.walker.socket.server_1.Msg;

public  class MessagePlugin<T> extends Plugin<T>{

    MessagePlugin(Bean params) {
		super(params);
	}
	/**
	 * {type:message,sto:,sfrom:128.2.3.1\:9080,to:123,from:222,data:{type:txt,body:hello} }	
	 */
	public void onData(Msg msg) {
		//存储
//		 *					{type:message,data:{to:123,from:222,type:txt,body:hello} }		
//		 *					message 发给user/group 请求转发
//		 *						data.to		发给目标用户	u_123,u_2323,g_xxx,s_all,s_online
//		 *						data.from	发送方来源	u_123,s_admin
//		 *						data.type	具体消息类型	text,image,voice,video,map
//		 *						data.body
		//发送方设置去向 接收方只看到发送给自己
		String[] tos = msg.getUserTo();
		for(String to : tos) {
			msg.setUserTo(to);
			pub.publish(to, msg);
		}

	}
     

}
