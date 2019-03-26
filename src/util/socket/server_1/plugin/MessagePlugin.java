package util.socket.server_1.plugin;

import util.Bean;
import util.route.SubPub;
import util.route.SubPubMgr;
import util.socket.server_1.MsgDown;
import util.socket.server_1.MsgUp;
import util.socket.server_1.session.Session;

public  class MessagePlugin<T> extends Plugin<T>{

    MessagePlugin(Bean params) {
		super(params);
	}
	/**
	 * {type:message,data:{to:123,from:222,type:txt,body:hello} }	
	 */
	public void onData(Session<T> session, MsgUp bean) {
		//存储
//		 *					{type:message,data:{to:123,from:222,type:txt,body:hello} }		
//		 *					message 发给user/group 请求转发
//		 *						data.to		发给目标用户	u_123,u_2323,g_xxx,s_all,s_online
//		 *						data.from	发送方来源	u_123,s_admin
//		 *						data.type	具体消息类型	text,image,voice,video,map
//		 *						data.body
		Bean data = (Bean) bean.getData();
		MsgDown msg = new MsgDown();
		msg.setData(data);
		msg.setType(bean.getType());
		String[] tos = msg.getDataTo().split(",");
		for(String to : tos) {
			msg.setDataTo(to);
			pub.publish(to, msg);
		}
		
	}
     

}
