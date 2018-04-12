package util.socket;


/**
 * 一个socket服务器
 * 负责维护和很多客户端保持连接
 * 同时维护和很多服务端保持连接
 * socket信息 中转 转发站
 * 
 */

public interface Server{
//	? extends Test

	/**
	 * socket 写入 发送
	 * 收到一个 服务端服务请求  转发给对应key的 客户端
	 * 把msg信息系统 用户转发出去
	 */
	public void send(String sysKey, String key, Msg msg);
	
	/**
	 * socket 接收 到消息 回调 key客户发了msg消息过来
	 */
	public void onReceive(String sysKey, String key, Msg msg);
	
	/**
	 * socket 新连接
	 */
	public <Arg> void onNewConnection(Arg obj);
	
	/**
	 * socket 启动完成
	 */
	public <Arg> void onStart(Arg obj);
	
	/**
	 * socket 关闭完成
	 */
	public <Arg> void onStop(Arg obj);
	
	
	
	
	public boolean start();
	public boolean pause();
	public boolean stop();
	
	
	
	
	public <T> void make(T obj) ;
} 