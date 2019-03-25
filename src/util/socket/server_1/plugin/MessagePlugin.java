package util.socket.server_1.plugin;

import util.Bean;
import util.route.SubPub;
import util.route.SubPubMgr;
import util.socket.server_1.Msg;
import util.socket.server_1.session.Session;

public  class MessagePlugin<T> extends Plugin<T>{

    MessagePlugin(Bean params) {
		super(params);
	}

	public void onData(Session<T> session, Msg bean) {
		//存储
		
		
		pub.publish("touser", bean);
		
		
	}
     

}
