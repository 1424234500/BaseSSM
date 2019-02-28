package util.pipe;

import java.util.Collection;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import util.Fun;
import util.database.RedisMgr;

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
 * 	redis
 *
 * 使用redis publish subscribe 传递
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
	public boolean putL(Collection<String> objs) {
		return true;
	}

	@Override
	public boolean putL(String obj) {
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
		Jedis jedis = redisPool.getJedis(this.key);
		jedis.subscribe(new JedisPubSub() {
			public void onMessage(String channel, String message) {
				log.warn("onMessage channel:" + channel + " message:" + message);
				executer.make(message);
			}
			
			public void onSubscribe(String channel, int subscribedChannels) {
				log.warn("onSubscribe channel:" + channel + " subscribedChannels:" + subscribedChannels);
			}
			
			public void onUnsubscribe(String channel, int subscribedChannels) {
				log.warn("onUnsubscribe channel:" + channel + " subscribedChannels:" + subscribedChannels);
			}
		}, this.key);
		
	}
	@Override
	public void stopConsumer() {
		redisPool.close(this.key);
	}
	
	
	
}
