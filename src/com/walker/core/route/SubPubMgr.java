package com.walker.core.route;

import java.util.concurrent.*;

import com.walker.common.util.Call;

/**
 * 发布订阅控制器 
 *
 */
public class SubPubMgr implements Call{
	private static ConcurrentHashMap<String, SubPub<?>> index = new ConcurrentHashMap<>();
	
	private SubPubMgr() {}
 
	/**
	 * 指定通道 共用
	 * @param key
	 * @param threadCoreSize
	 * @return
	 */
	public static <T> SubPub<T> getSubPub(String key, Integer threadCoreSize) {
		@SuppressWarnings("unchecked")
		SubPub<T> subPub =  (SubPub<T>) index.get(key);
		if(subPub == null) {
			subPub = new SubPubMapImpl<T>();
			index.put(key, subPub);
			subPub.init(threadCoreSize);
		}
		return subPub;
	}
	
	/**
	 * 中转消费线程数量
	 * @param threadCoreSize
	 * @return
	 */
	public static <T> SubPub<T> getSubPub(Integer threadCoreSize) {
		SubPub<T> subPub =  new SubPubMapImpl<T>();
		subPub.init(threadCoreSize);
		return subPub;
	}
	
	
	public void call(){
		
	}

	


}
