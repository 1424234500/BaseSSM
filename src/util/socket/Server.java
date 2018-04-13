package util.socket;


/**
 * 一个socket服务器
 * 负责维护和很多客户端保持连接
 * 同时维护和很多服务端保持连接
 * socket信息 中转 转发站
 * 
 */

public interface Server <Arg> {
//	? extends Test
	/**
	 * socket 写入 发送
	 * 收到一个 服务端服务请求  转发给对应对象的 客户端
	 * 把msg信息系统 用户转发出去
	 */
	public void send(Arg obj, String str);
	
	/**
	 * socket 接收 到消息 回调 key客户发了msg消息过来
	 */
	public void onReceive(Arg obj, String str);
	
	/**
	 * socket 新连接
	 */
	public void onNewConnection(Arg obj);
	
	 
	/**
	 * socket 断开了连接
	 */
	public void onDisConnection(Arg obj);
	
	 
	
	
	
	
	public boolean start();
	public boolean pause();
	public boolean stop();

	
	
	public void setSendCallback(SendCallback sendCallback);


	
	
	
	
} 