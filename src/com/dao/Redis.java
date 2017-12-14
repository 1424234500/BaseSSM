package com.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import util.Fun;
import util.MapListHelp;
import util.Tools;
 
@SuppressWarnings({ "rawtypes", "unchecked" })
public class Redis   { 
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
		String res = jedis.hmset(key, (map));
		close(jedis);
		keys.add(key);
		return res;
		
	}
	/**
	 * 移除一个指定key 
	 */
	public long removeMap(String key){
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
	* 添加一个list，指定list中的map的keyName/id列值为键值
	*/
	public void setList(String keyName, List<Map> list){ 
		for(int i = 0; i < list.size(); i++){
			setMap(MapListHelp.getList(list, i, keyName), list.get(i));
		}  
	}
	/**
	 * 获取所有存储的map  list<map>
	 */
	public List getList(){
		Jedis jedis = this.getJedis();

		List res = new ArrayList();
		for(String key : keys){
			res.add(jedis.hgetAll(key));
		}
		close(jedis);
		return res;
	}
	
	public boolean existsMap(String key){
		Jedis jedis = this.getJedis();
		boolean res = jedis.exists(key);
		close(jedis);
		return res;
	}
	
	@SuppressWarnings("deprecation")
	public void close(Jedis jedis){
		if(jedis != null){
			jedis.close();
		}
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

		out("--------------------------------------");
	}
	
	
	public void doJedis(Fun<Jedis> fun){
		Jedis jedis = this.getJedis();
		if(fun != null){
			fun.make(jedis);
		}
		close(jedis);
	}
	public Jedis getJedis(){
		return pool.getResource();
	}
 
	public Redis(){ 
		JedisPoolConfig config = new JedisPoolConfig();
        // 设置最大连接数
		config.setMaxTotal(100);
		config.setMaxWaitMillis(1000); 
        // 设置空闲连接
        config.setMaxIdle(10); 
        
		pool = new JedisPool(config, "localhost");
 
		keys = new HashSet<String>();
		out("redis init -----------------------" + cc++);
	}
	static int cc = 0;
	private JedisPool pool;
	private static Redis instance; 
	public static  Redis getInstance() {
		if (instance == null) {
			instance = new Redis();
		}
		return instance;
	}

	public void out(Object...objs){
		Tools.out(objs);
	}
}	
