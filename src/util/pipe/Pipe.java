package util.pipe;

import java.util.Collection;

import util.Fun;

/**
 * 管道 跨 进程通信工具
 * 行字符串-存取  对象存取-编码解码
 * 同步  阻塞 阻塞队列 
 * LinkedBlockingQueue
 * ArrayBlockingQueue
 * 
 * 系统管道实现
 * 文件实现
 * 数据库实现
 * redis list
 * redis subscribe
 */
public interface Pipe<T>{
	/**
	 * 初始化管道
	 * @param key
	 */
	void start(String key);
	/**
	 * 销毁
	 */
	void stop();
	
	boolean remove(T obj);
	/**
	 * 队尾 弹出
	 */
	T get();
	
	/**
	 * 存入
	 * @param objs
	 * @return
	 */
	boolean put(Collection<T> objs);
	/**
	 * 存入
	 * @param obj
	 * @return
	 */
	boolean put(T obj);
	/**
	 * 存入队首
	 * @param objs
	 * @return
	 */
	boolean putL(Collection<T> objs);
	/**
	 * 存入队首
	 * @param obj
	 * @return
	 */
	boolean putL(T obj);
	/**
	 * 队列大小
	 * @return
	 */
	long size() ;
	
	
	/**
	 * 开启消费者线程
	 */
	void startConsumer(int threadSize, Fun<T> executer) ;

	/**
	 * 关闭消费者线程
	 */
	void stopConsumer() ;
	
}


	
