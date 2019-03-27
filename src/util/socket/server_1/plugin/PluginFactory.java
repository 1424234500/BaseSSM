package util.socket.server_1.plugin;


import org.apache.log4j.Logger;

import util.Bean;
import util.ClassUtil;
import util.socket.server_1.Msg;
import util.socket.server_1.SocketException;
import util.socket.server_1.session.Session;

public class PluginFactory {
	private static Logger log = Logger.getLogger(PluginFactory.class); 

	private static Bean plugins;
	
	public static <T> void doPlugin(Session<T> session, Msg msg) throws SocketException {
		//按类别处理 业务 plugin  存储 加工 发送socket
		Plugin<T> plugin = PluginFactory.getPlugin(msg.getType());
		plugin.onData(msg);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Plugin<T> getPlugin(String type) throws SocketException {
		Bean bean = (Bean) plugins.get(type);
		if(bean == null) {
			throw new SocketException("该插件不存在", type);
		}
		if( ! bean.get("on", true)) {
			throw new SocketException("该插件已经关闭", type);
		}
		int limit = bean.get("limit", 0);
		if(limit > 0) {
			throw new SocketException("该插件限流", type, limit);
		}
//		login:{
//			class:util.plugin.Login,
//			on:false,
//			limit:200,
//		}
		String clz = bean.get("class", "");
		Bean params = bean.get("params", new Bean());
		Plugin<T> plugin = (Plugin<T>) ClassUtil.newInstance(clz, params);
		return plugin;
	}

	public static void init(Bean plugins) {
		PluginFactory.plugins = plugins;
		//插件限流控制初始化
		
		
	}

}
