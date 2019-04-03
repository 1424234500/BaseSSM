package com.walker.core.pipe;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.walker.common.util.Fun;
import com.walker.core.pipe.PipeMgr.Type;

/**
 * LinkedBlockingQueue实现 
 * 
 * 常用于单java程序单进程 多线程生产 多线程消费 上下文隔离场景
 * 只被消费一次 抢占处理
 * 
 * 避免上下级影响
 * 并提供缓冲功能
 * 
 */
public class PipeListImpl<T> implements Pipe<T>{
	private static Logger log = Logger.getLogger(PipeListImpl.class);

	private LinkedBlockingDeque<T> list;
	
	/**
	 * 线程池消费 每个线程都去消费
	 */
	private ExecutorService threadPool;
	
	@Override
	public void start(String key) {
		list = new LinkedBlockingDeque<T>();
	}

	@Override
	public void stop() {
		log.info("stop");		
		list.clear();
	}

	@Override
	public boolean remove(T obj) {
		return list.remove(obj);
	}

	@Override
	public T get() {
		return list.poll();
	}

	@Override
	public boolean put(Collection<T> objs) {
		return list.addAll(objs);
	}

	@Override
	public boolean put(T obj) {
		list.add(obj);
		return true;
	}

	@Override
	public boolean putHead(Collection<T> objs) {
		for(T t : objs) {
			list.push(t);
		}
		return true;
	}

	@Override
	public boolean putHead(T obj) {
		list.push(obj);
		return true;
	}

	@Override
	public long size() {
		return list.size();
	}

	@Override
	public void startConsumer(int threadSize, final Fun<T> executer) {
		log.warn("StartConsumer");
		if(threadSize <= 0)return;

		threadPool = Executors.newFixedThreadPool(threadSize);
		for(int i = 0; i < threadSize; i++) {
			final int now = i;
			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					log.warn("Start thread " + now);
					while(! Thread.interrupted()) {
						//！！！！！！消费 加锁 互斥问题
						T obj = get();
						if(obj != null) {
							log.debug("Comsumer get " + obj.toString());
							executer.make(obj);
						}else {
							try {
//								log.debug("sleep");
								Thread.sleep(Pipe.SLEEP_THREAD);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
					log.warn("Stop thread " + now);
				}
			});
		}		
	}

	@Override
	public void stopConsumer() {
		if(threadPool != null && !threadPool.isShutdown()) {
			threadPool.shutdown();
		}
	}

	@Override
	public void await(long timeout, TimeUnit unit) {
		if(threadPool != null && !threadPool.isShutdown()) {
			try {
				threadPool.awaitTermination(timeout, unit);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	

	public static void main(String argv[]) {
		Pipe<String> pipe = PipeMgr.getPipe(Type.PIPE, "test");
		pipe.startConsumer(10, new Fun<String>() {
			@Override
			public <T> T make(String obj) {
				try {
					Thread.sleep((long) (Math.random() * 1000));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return null;
			}
			
		});
		
		for(int i = 0; i < 40; i++) {
			pipe.put("s" + i);
		}
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for(int i = 0; i < 10; i++) {
			pipe.putHead("00000s" + i);
		}
		
		pipe.stop();
		log.error("aaaaaaaaaaa");
	}

	
	
	
	
	
}
