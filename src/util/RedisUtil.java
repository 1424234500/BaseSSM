package util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import util.Fun;
import util.MapListUtil;
import util.SerializeUtil;
import util.Tools;
import util.cache.Cache;
 
@SuppressWarnings({ "rawtypes", "unchecked" })
public class RedisUtil   { 

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
	
	public <V> void put(final Jedis jedis, final String key, final V value, final long expire) {
		// NX是不存在时才set， XX是存在时才set， EX是秒，PX是毫秒 SET if Not eXists
		if(value instanceof String){ // type.equals("string")
			jedis.set(key, (String)value, "NX", "PX", expire);
		}else if(value instanceof List ){//type.equals("list")
			List list = (List)value;
			for(Object item : list){
				jedis.rpush(key.getBytes(), SerializeUtil.serialize(item));
			}
		}else if(value instanceof Map){ //type.equals("hash")
			jedis.hmset(key, (Map<String, String>)value);
		}else{
			jedis.set(key, value.toString());
		}
		//后置设定过期时间
		jedis.expire(key, (int)Math.ceil(expire / 1000));

	}
	
	public static <V> V get(Jedis jedis, String key){
		return get(jedis, key, null);
	}
	public static <V> V get(Jedis jedis, String key, V defaultValue){
		V res = defaultValue;
		String type = jedis.type(key);
		if(type.equals("string")){
			res = (V) jedis.get(key);
		}else if(type.equals("list")){ //list采取 序列化 子元素策略
			List<Object> list = new ArrayList<>();
			List<byte[]> listBytes= jedis.lrange(key.getBytes(), 0, -1);
			for(byte[] item : listBytes){
				list.add(SerializeUtil.deserialize(item));
			}
			res = (V) list;
		}else if(type.equals("hash")){
			res = (V) (jedis.hgetAll(key));
		}else if(type.equals("set")){
			res = (V) (jedis.smembers(key));
		}else if(type.equals("zset")){
			res = (V) (jedis.zrange(key, 0, -1)); 
		}else{
			res = (V) jedis.get(key); 
		}
		return res;
	}
	
	
	
	public void out(Object...objs){
		Tools.out(objs);
	}
}	
