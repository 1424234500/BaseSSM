package com.walker.core.route;

/**
 * 订阅发布模型
 *
 */
public interface SubPub<T> {
	void init(Integer threadSize);
	
	/**
	 * 发布者 承担 负责调用订阅执行
	 * @param channel
	 * @param object
	 * @return
	 */
	Boolean publish(String channel, T object);
	
	/**
	 * 订阅 提供回调供发布者调用
	 * @param channel
	 * @param onSubscribe
	 * @return
	 */
	Boolean subscribe(String channel, OnSubscribe<T> onSubscribe);
	Boolean unSubscribe(String channel, OnSubscribe<T> onSubscribe);
	
	/**
	 * 订阅处理回调
	 * @param <T>
	 */
	interface OnSubscribe<T>{
		Type onSubscribe(T object);
		
		enum Type{
			/**
			 * 停止其他订阅
			 */
			STOP, 
			/**
			 * 不管
			 */
			DEFAULT,
			/**
			 * 已处理过
			 */
			DONE,
		}
	}

}






