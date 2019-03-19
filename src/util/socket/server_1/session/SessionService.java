package util.socket.server_1.session;

/**
 * Netty负责 接收连接 读取消息 写入消息
 * 		提供 socket key send
 *	
 * SessionService 会话管理 订阅 路由 转发
 * 		需要 socket-key()
 * 		List<Session>
 * 			自己负责业务处理 调用写入消息
 * 			需要socket-send()
 * 
 * 
 * session 
 * socket
 * ip:port
 *
 * @param <T>
 */
public interface SessionService<T> {

	/**
	 * 添加连接
	 * @param socket
	 */
	void sessionAdd(Socket<T> socket);
	/**
	 * 移除连接
	 * @param socket
	 */
	void sessionRemove(Socket<T> socket);
	/**
	 * 读取到消息
	 * @param socket
	 * @param msg
	 */
	void sessionOnRead(Socket<T> socket, Object msg);

}

