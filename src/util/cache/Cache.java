package util.cache;

import java.util.Collection;
import java.util.Map;
import java.util.Set;


/**
 * 仿造Map接口
 * 实现缓存抽象接口
 * by Walker
 */
public interface Cache {
    
    int size();
    boolean isStart();
    boolean isEmpty();
    boolean containsKey(Object key);
    boolean containsValue(Object value);
    /**
     * 全量添加map作为缓存key-value
     */
//    <K,V> void putAll(Map<?,?> m); 
	void putAll(Map<String, Object> map);

    /**
     * 全量获取缓存key-value
     */
    Map<?,?> getAll();
    
    void clear();
    /**
     * key集合
     */
    Set<String> keySet();
    /**
     * 值集合
     */
    Collection<Object> values();
   
    <V> V get(Object key);
    <V> V get(Object key, V defaultValue);
    <K,V> boolean put(K key, V value);
    /**
     * 缓存时间
     */
    <K,V> boolean put(K key, V value, long expiry);
    <K> boolean remove(K key); 
    /**
     * 关闭缓存
     */
    void shutdown();
    /**
     * 开启缓存
     */
    void startup();
}
