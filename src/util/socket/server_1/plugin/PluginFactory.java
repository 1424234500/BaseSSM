package util.socket.server_1.plugin;


import util.Bean;
import util.ClassUtil;

public class PluginFactory {
	private static Bean plugins;
	
	
	public static Plugin getPlugin(String type) {
		Bean bean = (Bean) plugins.get(type);
//		login:{
//			class:util.plugin.Login,
//			on:false,
//			limit:200,
//		}
		String clz = bean.get("class", "");
		return (Plugin) ClassUtil.newInstance(clz);
	}

	public static void init(Bean plugins) {
		PluginFactory.plugins = plugins;
		//插件限流控制初始化
		
		
	}

}
