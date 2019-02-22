package util.database;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import util.Tools;
import util.cache.Cache;
import util.cache.CacheMgr;

/**
 * redis工具数据库
 * @author walker
 *
 */
public class RedisMgr   { 

	public interface Fun<T>{
		public T make(Jedis obj) ;
	} 
	/**
	 * Redis数据结构 ---- 数据库结构[ id:01, name: walker, age: 18 ]
	 * set get key - value
	 * 1.	id:01 - {name:walker, age:18}
	 * 2.	id:01:name - walker
	 * 2.	id:01:age  - 18
	 * 3.	id:01 -  map{
	 * 						id:01,
	 * 						name:walker,
	 * 						age:18
	 * 					} 
	 */
	
	/**
	 * 存储所有 添加到redis的map的key值集合
	 */
	private Set<String> keys;
	public Set<String> getKeys(){
		return keys;
	}
	public void clearKeys(){ 
		Jedis jedis = this.getJedis();
		for(String key : keys){
			jedis.del(key);
		}
		close(jedis);
		this.keys.clear();
	}
	/**
	 * 添加一个指定key/id的map
	 */
	public String setMap(String key, Map<String, String> map){
		Jedis jedis = this.getJedis();
		String res = jedis.hmset(key, map);
		close(jedis);
		keys.add(key);
		return res;
		
	}
	/**
	 * 移除一个指定key 
	 */
	public long del(String key){
		Jedis jedis = this.getJedis();
		keys.remove(key);
		long res = jedis.del(key);
		close(jedis);
		return res;
	}
	/**
	 * 获取指定key的map 
	 */
	public Map<String, String> getMap(String key){
		Jedis jedis = this.getJedis();
		Map<String, String> res = jedis.hgetAll(key);
		close(jedis);
		return res;
	}
	/**
	* 添加一个list 通过byte 序列化 lpush头插入 rpush尾插入
	*/
	public void listRPush(String keyName, Collection<? extends String> c){ 
		Jedis jedis = this.getJedis();
		for(String item : c){
			jedis.rpush(keyName, item);
		}
		close(jedis);
	}
	/**
	* 添加一个list 通过byte 序列化 lpush头插入 rpush尾插入
	*/
	public void listLPush(String keyName, Collection<? extends String> c){ 
		Jedis jedis = this.getJedis();
		for(String item : c){
			jedis.lpush(keyName, item.toString());
		}
		close(jedis);
	}
	/**
	* 添加 通过byte 序列化 lpush头插入 rpush尾插入
	*/
	public void listRpush(String keyName, String obj){ 
		Jedis jedis = this.getJedis();
		jedis.rpush(keyName, obj);
		close(jedis);
	}
	/**
	* 添加 通过byte 序列化 lpush头插入 rpush尾插入
	*/
	public void listLpush(String keyName, String obj){ 
		Jedis jedis = this.getJedis();
		jedis.lpush(keyName, obj);
		close(jedis);
	}
	 
	public String lpop(final String key){
		Jedis jedis = this.getJedis();
		String res = jedis.lpop(key);
		close(jedis);
		return res;
	}
	/**
	 * 获取对象 自动解析键值类型
	 */
	public Object get(final String key){
		return doJedis(new Fun<Object>() {
			@Override
			public Object make(Jedis obj) {
				return get(obj, key);
			}
		});
	}
	
	private Object get(Jedis jedis, String key){
		Object res = null;
		if(jedis.exists(key)){
			String type = jedis.type(key);
			if(type.equals("string")){
				res = jedis.get(key);
			}else if(type.equals("list")){
				res = jedis.lrange(key, 0, -1);
			}else if(type.equals("hash")){
				res = (jedis.hgetAll(key));
			}else if(type.equals("set")){
				res = (jedis.smembers(key));
			}else if(type.equals("zset")){
				res = (jedis.zrange(key, 0, -1)); 
			}else{
				res = jedis.get(key); 
			}
		}
		return res;
	}
	public boolean exists(String key){
		Jedis jedis = this.getJedis();
		boolean res = jedis.exists(key);
		close(jedis);
		return res;
	}
	public long size(String key) {
		Jedis jedis = this.getJedis();
		long res = 0;
		if(jedis.type(key).equals("list")) {
			res = jedis.llen(key);
		}else {
			res = jedis.hlen(key);
		}
		close(jedis);
		return res;
	}
	
	/**
	 * 显示redis所有数据
	 */
	public void show(){
		out("-----------Redis show-----------------");
		Jedis jedis = this.getJedis();
		//获取所有key 各种类型
		Set<String> set = jedis.keys("*");
		for(String key : set){
			String type = jedis.type(key);
			out("key:" + key + ", type:" + type + "  ");
			if(type.equals("string")){
				out(jedis.get(key));
			}else if(type.equals("list")){
				out(jedis.lrange(key, 0, -1));
			}else if(type.equals("hash")){
				out(jedis.hgetAll(key));
			}else if(type.equals("set")){
				out(jedis.smembers(key));
			}else if(type.equals("zset")){
				out(jedis.zrange(key, 0, -1)); 
			}
			out("#############");
		}
		close(jedis);
		out("--------------------------------------");
	}
	
	public void showHash(){
		out("-----------Redis showHash-----------------");
		Jedis jedis = this.getJedis();
		//获取所有key 各种类型
		Set<String> set = jedis.keys("*"); 
		for(String key : set){
			String type = jedis.type(key);
			//out("key:" + key + ", type:" + type + "  "); 
			if(type.equals("hash")){	
				out(key, jedis.hgetAll(key));
			}  
		} 
		close(jedis);
		out("--------------------------------------");
	}
	
	/**
	 * 回调环绕执行redis 操作
	 */
	private <T> T doJedis(Fun<T> fun){
		T res = null;
		Jedis jedis = this.getJedis();
		if(fun != null){
			res = fun.make(jedis);
		}
		close(jedis);
		return res;
	}
	
	
	public Jedis getJedis(){
		return pool.getResource();
	}
	public void close(Jedis jedis){
		if(jedis != null){
			jedis.close();
		}
	}
	public RedisMgr(){ 
		Cache<String> cache = CacheMgr.getInstance();
		
		JedisPoolConfig config = new JedisPoolConfig();
        // 设置最大连接数
		config.setMaxTotal(cache.get("redis.maxTotal", 10));
		//设置最大等待时间
		config.setMaxWaitMillis(cache.get("redis.maxWaitMillis", 1000)); 
        // 设置空闲连接
        config.setMaxIdle(cache.get("redis.maxIdle", 3));
        
		pool = new JedisPool(config, cache.get("redis.host", "localhost"));
 
		keys = new HashSet<String>();
		out("redis init -----------------------" + cc++);
	}
	static int cc = 0;
	private JedisPool pool;
	private static RedisMgr instance; 
	public static  RedisMgr getInstance() {
		if (instance == null) {
			instance = new RedisMgr();
		}
		return instance;
	}

	public void out(Object...objs){
		Tools.out(objs);
	}
}	
