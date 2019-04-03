package com.walker.socket.client;


/**
 * 一个socket客户端
 * 逻辑处理接口!!!!!!!!!!!!
 */

public interface Client{
	/**
	 * socket 写入 发送
	 * 收到一个 服务端服务请求  转发给对应对象的 客户端
	 * 把msg信息系统 用户转发出去
	 */
	public void send(String str) throws Exception;
	
	public boolean isStart();
	public void start() throws Exception;
	public void stop();

	
	public void setUI(UiCall cui);

	public String show();

	
} 