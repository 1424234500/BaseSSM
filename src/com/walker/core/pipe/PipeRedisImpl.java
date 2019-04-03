package com.walker.core.pipe;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.walker.common.util.Fun;
import com.walker.core.database.RedisMgr;

import redis.clients.jedis.Jedis;

/**
 * 
 * 使用redis list作为存储结构
 * 
 * 常用于多进程 多线程生产 多线程消费 上下文隔离场景
 * 只被消费一次 抢占处理
 * 
 * 避免上下级影响
 * 并提供缓冲功能
 * 
 */
public class PipeRedisImpl implements Pipe<String>{
	private static Logger log = Logger.getLogger(PipeRedisImpl.class);

	/**
	 * 簇列
	 */
	private String key;
	/**
	 * redis连接池
	 */
	private RedisMgr redisPool;

	/**
	 * 线程池消费 每个线程都去消费
	 */
	private ExecutorService threadPool;
	
	@Override
	public void start(String key){
		this.key = key;

		redisPool = RedisMgr.getInstance();
		Jedis jedis = redisPool.getJedis();
		if(jedis == null) {
			log.error("Start error " + key);
//			throw new PipeException("start error");
		}else {
			log.info("Start ok " + key);
		}
		redisPool.close(jedis);
	}

	@Override
	public void stop(){
		log.info("stop");		
	}

	@Override
	public boolean remove(String obj) {
		redisPool.del(this.key);
		return false;
	}

	@Override
	public String get() {
		return redisPool.lpop(this.key);
	}

	@Override
	public boolean put(Collection<String> objs) {
		redisPool.listRPush(this.key, objs);
		return true;
	}

	@Override
	public boolean put(String obj) {
		redisPool.listRPush(this.key, Arrays.asList(obj));
		return true;
	}

	@Override
	public boolean putHead(Collection<String> objs) {
		redisPool.listLPush(this.key, objs);
		return true;
	}

	@Override
	public boolean putHead(String obj) {
		redisPool.listLPush(this.key, Arrays.asList(obj));
		return true;
	}

	@Override
	public long size() {
		return redisPool.size(this.key);
	}

	/**
	 * 单线程 定时轮询  拿到资源 存入队列 新建线程处理     多拿存下来慢慢用
	 * 多线程 各自轮询  拿到资源 消费处理 继续拿资源	  量力获取
	 */
	@Override
	public void startConsumer(int threadSize, final Fun<String> executer) {
		log.warn("StartConsumer");
		if(threadSize <= 0)return;
		threadPool = Executors.newFixedThreadPool(threadSize);
		for(int i = 0; i < threadSize; i++) {
			final int now = i;
			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					String keyJedis = key + "-" + now;
					log.warn("Start thread " + now);
					while(! Thread.interrupted()) {
						//！！！！！！消费 加锁 互斥问题
						//:Todo
						String obj = redisPool.getJedis(keyJedis).lpop(key);//get();
						if(obj != null) {
							log.debug("Comsumer get " + obj.toString());
							executer.make(obj);
						}else {
							try {
								Thread.sleep(Pipe.SLEEP_THREAD);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
					
					redisPool.close(keyJedis);
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
	
	
}
