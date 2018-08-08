package util.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 简单缓存服务实现类，直接采用内存做缓存服务
 * 缓存池支持
 * 缓存时间支持
 */
public class CacheMapImpl implements Cache {
	
    /** 存储缓存 */
    private Map<String, Object> map = new ConcurrentHashMap<String, Object>();
    
	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean isStart() {
		return true;
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public void putAll(Map<String, Object> map) {
		map.putAll(map);
	}

	@Override
	public Map<?, ?> getAll() {
		return map;
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public Set<String> keySet() {
		return map.keySet();
	}

	@Override
	public Collection<Object> values() {
		return map.values();
	}

	@Override
	public <V> V get(Object key) {
		if(map.containsKey(key)){
			return (V)key;
		}
		return null;
	}

	@Override
	public <V> V get(Object key, V defaultValue) {
		
		return null;
	}

	@Override
	public <K, V> boolean put(K key, V value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <K, V> boolean put(K key, V value, long expiry) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <K> boolean remove(K key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startup() {
		// TODO Auto-generated method stub
		
	}


}
