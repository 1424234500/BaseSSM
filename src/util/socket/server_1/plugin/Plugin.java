package util.socket.server_1.plugin;

import org.apache.log4j.Logger;

import util.Bean;
import util.route.SubPub;
import util.route.SubPubMgr;
import util.socket.server_1.Msg;

/**
 * 业务分类处理插件
 *
 */
public abstract class Plugin<T> {
	protected Logger log = Logger.getLogger(Plugin.class); 

	/**
     * 路由 发布订阅
     * pub发布 key socket定向消息 或者 发布user频道消息
     * 不需要session
     */
    SubPub<Msg> pub = SubPubMgr.getSubPub("msg_route", 0);
    
	Bean params;
	Plugin(Bean params){
		this.params = params;
	}
	abstract void onData(Msg bean);
	
}
