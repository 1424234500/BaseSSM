package util.socket.server_1.plugin;

import util.Bean;

/**
 * 业务分类处理插件
 *
 */
public interface Plugin {
	void onData(Bean bean);
	
}
