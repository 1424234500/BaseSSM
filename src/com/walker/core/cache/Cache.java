package com.walker.core.cache;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.walker.common.util.Bean;


/**
 * 仿造Map接口
 * 实现缓存抽象接口
 * 缓存监控
 * by Walker
 */
public interface Cache<K> {
	/**
	 * 缓存监控接口
	 * 每一个缓存的键作为一条sql记录  hash, 键, 值, 修改时间, 过期时间, 命中次数, 值类型 map 1/list 2/base 0
	 */
	String[] table = {"HASHCODE", "KEY", "VALUE", "MTIME", "EXPIRE", "COUNT", "TYPE"};
	/**
	 * 查询键值缓存时间计数 list urls
	 */
	Bean findCacheList(Bean args);
	
	
	/**
	 * 默认接口成员变量 static final
	 */
	public static final int interfaceDefaultStaticFinalInt = 0;
	
	/**
	 * 默认的缓存周期
	 */
	long TIME_DEFAULT_EXPIRE = 0 * 600 * 1000L;
	
	
//具体缓存实现
	
    int size();
    boolean isStart();
    boolean isEmpty();
    boolean containsKey(K key);
    boolean containsValue(Object value);
    /**
     * 全量添加map作为缓存key-value
     */
//    <K,V> void putAll(Map<?,?> m); 
	void putAll(Map<?, ?> map);

    /**
     * 全量获取缓存key-value
     */
    Map<?, ?> getAll();
    
    void clear();
    /**
     * key集合
     */
    Set<K> keySet();
    /**
     * 值集合
     */
    Collection<Object> values();
   
    <V> V get(K key);
    <V> V get(K key, V defaultValue);
    <V> Cache<?> put(K key, V value);
    /**
     * 缓存时间
     */
    <V> Cache<?> put(K key, V value, long expiry);

    /**
     *  map1.list1[01].map2 
     *  key2 value2
     * url添加缓存 return "true" "error info"
     */
    <V> String put(String url, K key, V value);
    
    /**
     * map1.list1[01].map2 key2
     * url添加缓存 return "true" "error info"
     */
    <V> String put(String url, K key, V value, long expiry);
    
    /**
     * map1.list1[01].map2 key2
     * url移除key return "true" "error info"
     */
    String remove(String url, K key); 
    
    boolean remove(K key); 
    /**
     * 关闭缓存
     */
    void shutdown();
    /**
     * 开启缓存
     */
    void startup();
    
    Type getType();
}
