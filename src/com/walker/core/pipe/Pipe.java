package com.walker.core.pipe;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import com.walker.common.util.Fun;

/**
 * 管道 跨 进程通信工具
 * 行字符串-存取  对象存取-编码解码
 * 同步  阻塞 阻塞队列 
 * LinkedBlockingQueue
 * ArrayBlockingQueue
 * 
 * 内存实现
 * redis list
 * redis subscribe
 * 系统管道实现
 * 文件实现
 * 数据库实现
 */
public interface Pipe<T>{
	/**
	 * 多线程消费 空闲间隔
	 */
	long SLEEP_THREAD = 50;
	/**
	 * 初始化管道
	 * @param key
	 */
	void start(String key);
	/**
	 * 销毁管道
	 * 清空队列
	 */
	void stop();
	/**
	 * 停止管道 
	 * 等待执行完毕
	 */
	void await(long timeout, TimeUnit unit);
	/**
	 * 移除元素
	 * @param obj
	 * @return
	 */
	boolean remove(T obj);
	/**
	 * 获取队首 弹出
	 */
	T get();
	
	/**
	 * 存入 排队 
	 * @param objs
	 * @return
	 */
	boolean put(Collection<T> objs);
	/**
	 * 存入 排队
	 * @param obj
	 * @return
	 */
	boolean put(T obj);
	/**
	 * 存入队首 优先
	 * @param objs
	 * @return
	 */
	boolean putHead(Collection<T> objs);
	/**
	 * 存入队首 优先
	 * @param obj
	 * @return
	 */
	boolean putHead(T obj);
	/**
	 * 队列大小
	 * @return
	 */
	long size() ;
	
	
	/**
	 * 开启消费者线程
	 * 线程数 执行器
	 */
	void startConsumer(int threadSize, Fun<T> executer) ;

	/**
	 * 关闭消费者线程
	 */
	void stopConsumer() ;
	
}


	
