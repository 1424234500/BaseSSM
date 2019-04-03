package com.walker.socket.server_1.session;

/**
 * socket包装类
 * 组合对象 
 * 提供key 发送
 *
 * @param <T>
 */
public abstract class Socket<T> {
	/**
	 * 实际的socket连接
	 */
	public T socket;
	
	public Socket(T socket) {
		this.socket = socket;
	}
	public abstract String key();
	public abstract void send(Object obj);
	@Override
	public String toString() {
		return "Socket[" + key() + "]";
	}
	
	
	
	
	
	
}
