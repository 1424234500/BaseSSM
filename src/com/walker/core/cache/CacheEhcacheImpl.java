package com.walker.core.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.walker.common.util.Bean;
import com.walker.common.util.LangUtil;
import com.walker.common.util.Page;
import com.walker.common.util.SortUtil;
import com.walker.common.util.Tools;

import java.util.Set;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;


/**
 * 缓存服务实现类
 * ehcache实现
 */
class CacheEhcacheImpl implements Cache<String> {
	static int ALL_COUNT = 0; //所有缓存访问get次数
	private CacheManager cacheMgr = null;
	private net.sf.ehcache.Cache cache = null;
	/**
	 * ehcache 存储以 type-key形式 这里只用一个type元
	 */
	private final static String TYPE = "CACHE_EHCACHE_IMPL_TYPE";
	public CacheEhcacheImpl(){
//		cacheMgr = CacheManager.create("ehcache.xml");
		cacheMgr = CacheManager.create();
		cacheMgr.addCache(TYPE);
		cache = cacheMgr.getCache(TYPE);
		out("CacheEhcacheImpl init");
	}
	@Override
	public int size() {
		return cache.getSize();
	}

	@Override
	public boolean isStart() {
		return !cache.isDisabled();
	}

	@Override
	public boolean isEmpty() {
		return keySet().size() <= 0;
	}
	@Override
	public boolean containsKey(final String key) {
		return cache.get(key) != null;
	}

	@Override
	public boolean containsValue(Object value) {
		return false;
	}

	@Override
	public void putAll(final Map map) {
		
	}

	@Override
	public Map getAll() {
		Map<String, Object> res = new HashMap<>();
		Set<String> keys = keySet();
		for(String item : keys){
			res.put(item, get(item));
		}
		return res;
	}

	@Override
	public void clear() {
		cache.removeAll();
	}

	@Override
	public Set<String> keySet() {
		Set<String> res = new HashSet<>();
		for(Object item : cache.getKeys()){
			res.add((String)item);
		}
		return res;
	}

	@Override
	public Collection<Object> values() {
		Collection<Object> res = new ArrayList<>();
		cache.getAll(res);
		return res;
	}

	@Override
	public <V> V get(final String key) {
		return get(key, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <V> V get(final String key, final V defaultValue) {
		V res = defaultValue;
		Element ele = cache.get(key);
		if(ele != null){
			res = (V) (LangUtil.turn(ele.getObjectValue(), defaultValue));
		}
		return res;
	}
	
	@Override
	public <V> Cache put(String key, V value) {
		put(key, value, TIME_DEFAULT_EXPIRE);
		return this;
	}

	
	@Override
	public <V> Cache put(final String key, final V value, final long expire) {
		Element ele = new Element(key, value);
		if(expire > 0){
			ele.setTimeToLive((int) Math.ceil(expire / 1000));
		}
		cache.put(ele);
		return this;
	}
	public <V> String put(String url, String key, V value) {
		return put(url, key, value, TIME_DEFAULT_EXPIRE);
	}
	@Override
	public <V> String put(String url, String key, V value, long expire) {
		return "false";
	}
	/**
	 * 根据url 来移除key
	 * @param url
	 * @param key
	 * @return true 'false and reason'
	 */
	@Override
	public String remove(String url, String key) {
		return "false";
	}
	
	@Override
	public boolean remove(final String key) {
		return cache.remove(key);
	}

	@Override
	public void shutdown() {
		cacheMgr.shutdown();
	}

	@Override
	public void startup() {
	}

	@Override
	public Bean findCacheList(Bean bean) {
//		String[] table = {"HASHCODE", "KEY", "VALUE", "MTIME", "EXPIRE", "COUNT", "TOURL"};
		String urls = bean.get("URL", ""); //树形选择url
		String key = bean.get("KEY", ""); //过滤key
		String value = bean.get("VALUE", "");
		int expire = bean.get("EXPIRE", 0); //0, 1未过期, 2已过期 下次获取remove
		int type = bean.get("TYPE", -1);
		Page page = Page.getPage(bean); //分页
		
		Object obj = getAll(); 
		Object temp = null;
		String rootKey = "";
		String toUrl = ""; //实际路径
		String itemCopy = "";
		int oftype = 0;	//结果集来源于类型 用于前端判定生成url
		
		if(urls.length() > 0){	//非查询root
			if(urls.charAt(0) == '.') urls = urls.substring(1);
			String[] arr = urls.split("\\."); // map.list[0].map.aaa   map.list
			int cc = -1;
			for(String item : arr){
				if(item.length() <= 0)break;
				itemCopy = item;
				//list[0] -> list 0
				cc = -1;
				if(item.charAt(item.length() - 1) == ']'){ //数组
					item = item.substring(0, item.length() - 1); //去除]  -> ssk ey[02
					String[] ss = item.split("\\[");
					if(ss.length >= 2){
						item = ss[0];
						cc = Tools.parseInt(ss[1], -1);
					}
				}
				if(obj instanceof List){ //数组 list 不出现该情况
					oftype = 0;
					break;
				}else if(obj instanceof Map){//最后查询层级应该是此 
					Map objMap = (Map)obj;
					temp = objMap.get(item); //预读取取出值为 map list ? 否则中断跳出
					if(temp == null) break;
					if(temp instanceof Map){	//取出对象为map
						obj = temp;
						oftype = 1;
					}else if(temp instanceof List){ //输出对象为list
						List tempList = (List)temp;
						if(cc >= 0 && cc < tempList.size()){ //后续判定是否有选中某项 list[2]
							Object objTemp = tempList.get(cc);
							if(objTemp instanceof Map){ //list[2] = map
								obj = objTemp;
								oftype = 1;
							} else if(objTemp instanceof List){ //list[2] = list
								obj = objTemp;
								oftype = 2;
							}else{ //基本类型 则返回所有list
								obj = temp;
								oftype = 0;
							}
						}else{//list
							obj = temp;
							oftype = 2;
						}
					}else{ //基本类型
						break;
					}
				}else{ //已经是基本类型则 不再继续子层级查询 理应不存在访问此
					break;
				}
				toUrl += itemCopy + ".";
				if(rootKey.length() <= 0)
					rootKey = item;
			}
			if(toUrl.length() > 0)
				toUrl = toUrl.substring(0, toUrl.length() - 1);
		}
		List<Map<?,?>> res = new ArrayList<>();
		int size = 0;
		if(obj instanceof Map){
			res = mapToList((Map<?,?>)obj, page, rootKey, toUrl, key, value, expire, type);
			size = ((Map<?,?>)obj).size();
		}else if(obj instanceof List){
			res = listToList((List)obj, page, rootKey, toUrl, key, value, expire, type);
			size = ((List)obj).size();
		}else{
			res = new ArrayList<>();
		}
		SortUtil.sort(res, page.getDESC().length()==0, page.getORDER(), "TYPE", "COUNT", "KEY", "EXPIRE");
		return new Bean().put("ok", toUrl==urls).put("urls", toUrl).put("list", res).put("oftype", oftype).put("size", size);
	}
	public List mapToList(Map theMap, Page page, String rootKey, String toUrl, String key, String value, int expire, int type){
		List<Map> res = new ArrayList<>();
		Set<Entry<String, Object>> set = theMap.entrySet();
		int start = page.start();
		int stop = page.stop();
		int count = 0;
//		boolean ffExpire = false;
		for(Entry<String, Object> item : set){
			String ikey = item.getKey();
			int ihash = ikey.hashCode();
			Object ivalue = item.getValue();
			int itype = Tools.getType(ivalue);
			Bean temp = new Bean();
			temp.set("HASHCODE", ihash);
			if(key.length() > 0 && ikey.indexOf(key) < 0) continue;
			temp.set("KEY", ikey);
			if(itype == 0){ //只对基本类型做值查询
				if(value.length() > 0  && ivalue != null && ivalue.toString().indexOf(key) < 0) continue;
				temp.set("VALUE", ivalue);
			}
			if(type != -1 && type != itype) continue;
			temp.set("TYPE", itype);
			temp.set("MTIME", "");
//			if(expire == 1 && ffExpire) continue;
//			if(expire == 2 && !ffExpire) continue;
			temp.set("EXPIRE", "");
			temp.set("COUNT", "");
//			temp.set("TOURL", toUrl);
			if(count >= start){
				if(count < stop){
					res.add(temp);
				}else{
					break;
				}
			}
			count++;
		}
		return res;
	}
	public List listToList(List theList, Page page, String rootKey, String toUrl, String key, String value, int expire, int type){
		List<Map> res = new ArrayList<>();
		int start = page.start();
		int stop = page.stop();
		int count = 0;
		boolean ffExpire = false;
		for(int i = 0; i < theList.size(); i++){
			Object ivalue = theList.get(i);
			String ikey = "[" + i + "]";
			int ihash = ikey.hashCode();
			int itype = Tools.getType(ivalue);
			Bean temp = new Bean();
			temp.set("HASHCODE", ihash);
			if(key.length() > 0 && ikey.indexOf(key) < 0) continue;
			temp.set("KEY", ikey);
			if(itype == 0){ //只对基本类型做值查询
				if(value.length() > 0  && ivalue != null && ivalue.toString().indexOf(key) < 0) continue;
				temp.set("VALUE", ivalue);
			}
			if(type != -1 && type != itype) continue;
			temp.set("TYPE", itype);
			temp.set("MTIME", "");
			if(expire == 1 && ffExpire) continue;
			if(expire == 2 && !ffExpire) continue;
			temp.set("EXPIRE", "");
			temp.set("COUNT", "");
//			temp.set("TOURL", toUrl);

			if(count >= start){
				if(count < stop){
					res.add(temp);
				}else{
					break;
				}
			}
			count++;
		}
		return res;
	}
	
	private void out(Object...objects){
		Tools.out(objects);
	}
	@Override
	public Type getType() {
		return Type.EHCACHE;
	}
}
