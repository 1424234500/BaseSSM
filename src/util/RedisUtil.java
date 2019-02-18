package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
	
	
	/**
	 * 处理list采用rpush结构 否则 全使用序列化 string byte[]
	 * @param jedis
	 * @param key
	 * @param value
	 * @param expire
	 */
	public static <V> void put(final Jedis jedis, final String key, final V value, final long expire) {
		if(value instanceof List ){
			List list = (List)value;
			for(Object item : list){
//				jedis.rpush(key.getBytes(), SerializeUtil.serialize(item));
			}
		}else{
			jedis.set(key, value.toString());
//			jedis.set(key.getBytes(), SerializeUtil.serialize(value));
		}
		//后置设定过期时间
		jedis.expire(key, (int)Math.ceil(expire / 1000));

	}
	/**
	 * 处理list采用rpush结构 否则 全使用序列化 string byte[]
	 * @param jedis
	 * @param key
	 * @return
	 */
	public static <V> V get(Jedis jedis, String key){
		return get(jedis, key, null);
	}
	/**
	 * 处理list采用rpush结构 否则 全使用序列化 string byte[]
	 * @param jedis
	 * @param key
	 * @return
	 */
	public static <V> V get(Jedis jedis, String key, V defaultValue){
		V res = defaultValue;
		String type = jedis.type(key);

		if(type.equals("list")){ //list采取 序列化 子元素策略
			List<Object> list = new ArrayList<>();
			List<byte[]> listBytes= jedis.lrange(key.getBytes(), 0, -1);
			for(byte[] item : listBytes){
				list.add(SerializeUtil.deserialize(item));
			}
			res = (V) list;
		}else{
			res = (V) SerializeUtil.deserialize(jedis.get(key.getBytes())); 
		}
		return res;
	}
	
	
	
	public void out(Object...objs){
		Tools.out(objs);
	}
}	
