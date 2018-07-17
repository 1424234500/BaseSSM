package util.cache;

import java.util.ArrayList;
import java.util.List;


import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;


/**
 * EhCache的缓存服务实现类
 * @author Jerry Li
 *
 */
public class EhCacheProvider implements ICacheProvider {

    protected static CacheManager cacheMgr = null;
    
    /**
     * 构建体方法
     */
    public EhCacheProvider() {
        String ehcacheConf = "ehcache.xml";
        cacheMgr = CacheManager.create(ehcacheConf);
    }

    @Override
    public boolean set(String type, String key, Object val) {
        return set(type, key, val, -1);
    }


    @Override
    public boolean set(String type, String key, Object val, int expiry) {
        Cache cache = this.getCache(type);
        Element elem = new Element(key, val);
        if (0 < expiry) {
            elem.setTimeToLive(expiry);
        }
        cache.put(elem);
        return true;
    }
    
    @Override
    public Object get(String type, String key) {
        if (!cacheMgr.cacheExists(type)) {
            return null;
        }
        Cache cache = cacheMgr.getCache(type);
        Element element = cache.get(key);
        if (element != null) {
            return element.getObjectValue();
        }
        return null;
    }

    @Override
    public boolean delete(String type, String key) {
        if (cacheMgr.cacheExists(type)) {
            return cacheMgr.getCache(type).remove(key);
        }
        return false;
    }

    @Override
    public boolean clear(String type) {
        if (cacheMgr.cacheExists(type)) {
            cacheMgr.removeCache(type);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean exists(String type, String key) {
        boolean exists = false;
        if (cacheMgr.cacheExists(type)) {
            Cache cache = cacheMgr.getCache(type);
            exists = cache.isKeyInCache(key);
        }
        return exists;
    }
    
    /**
     * 取得指定名称的缓存对象
     * @param cacheType 缓存名称
     * @return 指定名称的缓存对象
     */
    private Cache getCache(String cacheType) {
        synchronized (cacheMgr) {
            if (!cacheMgr.cacheExists(cacheType)) {
                cacheMgr.addCache(cacheType);
            }
        }
        return cacheMgr.getCache(cacheType);
    }
    
    /**
     * 关闭缓存
     */
    public void shutdown() {
        cacheMgr.shutdown();
    }

    @Override
    public int getSize(String type) {
        int count = 0;
        if (cacheMgr.cacheExists(type)) {
            count = cacheMgr.getCache(type).getSize();
        }
        return count;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getKeyList(String type) {
        List<String> keyList = null;
        if (cacheMgr.cacheExists(type)) {
            Cache cache = cacheMgr.getCache(type);
            keyList = cache.getKeys();
        }
        if (keyList == null) {
            keyList = new ArrayList<String>();
        }
        return keyList;
    }

    @Override
    public List<Object> getValueList(String type, int offset, int size) {
        List<String> list = getKeyList(type);
        if (offset >= list.size()) {
            return new ArrayList<Object>();
        }
        Cache cache = cacheMgr.getCache(type);
        int len = size + offset;
        if (size <= 0) {
            len = list.size();
        } else if (len >= list.size()) {
            len = list.size();
        }

        List<Object> rtnList = new ArrayList<Object>();
        for (int i = offset; i < len; i++) {
            String key = list.get(i);
            Element element = cache.get(key);
            rtnList.add(element.getObjectValue());
        }
        return rtnList;
    }
    
    @Override
    public Object getCacheMgr() {
        return cacheMgr;
    }

}
