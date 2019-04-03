package com.walker.core.pipe;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.walker.common.util.Fun;
import com.walker.core.database.RedisMgr;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/**
 * 
 * 使用redis publish subscribe 传递
 * 
 * 常用于多进程 多线程发布 多线程订阅 上下文隔离场景
 * 实现一对多 一人发布 多人复制处理
 * 
 * 避免上下级影响
 * 只提供订阅后的 弱缓冲功能
 * 
 * 
 */
public class PipeRedisBroadcastImpl implements Pipe<String>{
	private static Logger log = Logger.getLogger(PipeRedisBroadcastImpl.class);

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
		return false;
	}

	@Override
	public String get() {
		return null;
	}

	@Override
	public boolean put(Collection<String> objs) {
		Jedis jedis = redisPool.getJedis();
		for(String obj : objs) {
			jedis.publish(this.key, obj);
		}
		redisPool.close(jedis);
		return true;
	}

	@Override
	public boolean put(String obj) {
		Jedis jedis = redisPool.getJedis();
		jedis.expire(obj, 3);
		jedis.publish(this.key, obj);
		redisPool.close(jedis);
		return true;
	}

	@Override
	public boolean putHead(Collection<String> objs) {
		return true;
	}

	@Override
	public boolean putHead(String obj) {
		return true;
	}

	@Override
	public long size() {
		return -1;
	}

	/**
	 * 1线程 定时轮询  拿到资源 新建线程处理
	 * 多线程 各自轮询 消费 消费完后继续拿资源
	 */
	@Override
	public void startConsumer(int threadSize, final Fun<String> executer) {
		log.warn("StartConsumer");
		if(threadSize <= 0)return;

		threadPool = Executors.newFixedThreadPool(threadSize);
		Jedis jedis = redisPool.getJedis(this.key);
		jedis.subscribe(new JedisPubSub() {
			public void onMessage(String channel, final String message) {
				log.debug("Consumer subcribe [" + channel + "] " + message);
				threadPool.execute(new Runnable() {
					public void run() {
						executer.make(message);
					}
				});
			}
			
			public void onSubscribe(String channel, int subscribedChannels) {
				log.debug("onSubscribe channel:" + channel + " subscribedChannels:" + subscribedChannels);
			}
			
			public void onUnsubscribe(String channel, int subscribedChannels) {
				log.debug("onUnsubscribe channel:" + channel + " subscribedChannels:" + subscribedChannels);
			}
		}, this.key);
		
	}
	@Override
	public void stopConsumer() {
		redisPool.close(this.key);
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
