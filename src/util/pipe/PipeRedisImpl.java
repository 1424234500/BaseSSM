package util.pipe;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
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
 * 使用redis list作为存储结构
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
	public boolean putL(Collection<String> objs) {
		redisPool.listLPush(this.key, objs);
		return true;
	}

	@Override
	public boolean putL(String obj) {
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
		threadPool = Executors.newFixedThreadPool(threadSize);
		for(int i = 0; i < threadSize; i++) {
			final int now = i;
			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					String keyJedis = key + "-" + now;
					log.warn("Start thread " + now);
					while(! Thread.interrupted()) {
						String obj = redisPool.getJedis(keyJedis).lpop(key);//get();
						if(obj != null) {
//							log.error("Get pipe " + obj);
//							threadPool.notify();
							executer.make(obj);
						}else {
							try {
								//按照序号 依次等待 50 + now * 1000
								long tt = 100;
//								log.info("Wait " + tt);
								Thread.sleep(tt);
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
	
	
	
	
}
