package com.walker.core.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.walker.common.util.Bean;
import com.walker.common.util.LangUtil;
import com.walker.common.util.MapListUtil;
import com.walker.common.util.Page;
import com.walker.common.util.SortUtil;
import com.walker.common.util.Tools;


/**
 * 缓存服务实现类，直接采用内存做缓存服务
 * 缓存池支持 ?
 * 缓存时间支持
 */
class CacheMapImpl implements Cache<String> {
	static int ALL_COUNT = 0; //所有缓存访问get次数

	/**
	 * 附加数据记录
	 */
	class Index{
		int count;	//命中次数
		long expire;	//过期时间
		long mtime;	//修改时间
		public boolean isExpire(){ //是否过期
			if(expire <= 0) return false;
			return System.currentTimeMillis() > mtime + expire;
		}
	}
	
	
    /** 存储缓存 */
    private static Map<String, Object> map = new ConcurrentHashMap<>();
    private static Map<String, Index> mapIndex = new ConcurrentHashMap<>();
    CacheMapImpl(){
		out("CacheMapImpl init");
    }
    
    
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
	public boolean containsKey(String key) {

		if(map.containsKey(key) && mapIndex.containsKey(key)){
			Index index = mapIndex.get(key);
			if(index.isExpire()){
				out("expire." + key + "." + map.get(key) + "." + index.expire);
				remove(key);
				return false;
			}
		}else{
			remove(key);
			return false;
		}
		return true;
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public void putAll(Map map) {
		for(Object key : map.keySet()){
			put(key.toString(), map.get(key));
		}
//		map.putAll(map);
	}

	@Override
	public Map getAll() {
		return map;
	}

	@Override
	public void clear() {
		map.clear();
		mapIndex.clear();
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
	public <V> V get(String key) {
		return get(key, null);
	}

	@Override
	public <V> V get(String key, V defaultValue) {
		ALL_COUNT++;
		if(containsKey(key)){
			Index index = mapIndex.get(key);
			index.count += 1;
			return (V)(LangUtil.turn(map.get(key), defaultValue));
		}
		return defaultValue;
	}

	@Override
	public <V> Cache put(String key, V value) {
		put(key, value, TIME_DEFAULT_EXPIRE);
		return this;
	}

	@Override
	public <V> Cache put(String key, V value, long expire) {
		map.put(key, value);
		Index index;
		if(mapIndex.containsKey(key)){
			index = mapIndex.get(key);
		}else{
			index = new Index();
			mapIndex.put(key, index);
		}
		index.mtime = System.currentTimeMillis();
		index.expire = expire;
//		index.count += 1;
		return this;
	}
	public <V> String put(String url, String key, V value) {
		return put(url, key, value, TIME_DEFAULT_EXPIRE);
	}
	@Override
	public <V> String put(String url, String key, V value, long expire) {
		if(Tools.isNull(url)) {
			put(key, value, expire);
			return "true";
		}
		String res = "false";
		Object obj = MapListUtil.getMapUrl(map, url);
		if(obj == null){
//			res = "寻址失败" + url;
			key =  url + "." + key;
			key = MapListUtil.putMapUrl(map, key, value);
			Index index = new Index();
			index.mtime = System.currentTimeMillis();
			index.expire = expire;
			mapIndex.put(key, index);
			res = "true";
//			out(res);
		}else{
			if(obj instanceof List){
				((List)obj).add(value);
				res = "true";
			}else if(obj instanceof Map){
				((Map)obj).put(key, value);
				res = "true";
			}else{
				res = "基本数据类型,无子对象";
				out(res);
			}
			//添加子对象后 刷新root key的生命? 不重置次数
			key = url.split("\\.")[0]; //取出root
			Index index;
			if(mapIndex.containsKey(key)){
				index = mapIndex.get(key);
			}else{
				index = new Index();
				mapIndex.put(key, index);
			}
			index.mtime = System.currentTimeMillis();
			index.expire = expire;
//			index.count = 0;
		}
		
		return res;
	}
	/**
	 * 根据url 来移除key
	 * @param url
	 * @param key
	 * @return true 'false and reason'
	 */
	@Override
	public String remove(String url, String key) {
		if(Tools.isNull(url)) {
			return "" + remove(key);
		}
		String res = "false";
		Object obj = MapListUtil.getMapUrl(map, url);
		if(obj == null){
			res = "寻址失败" + url;
			out(res);
		}else{
			if(obj instanceof List){
				int cc = Tools.parseInt(key, -1);
				List list = (List)obj;
				if(cc >= 0 && cc < list.size()){
					(list.remove(cc)).toString();
					res = "true";
				}else{
					res = "List越界," + cc + "->[0," + list.size() + "]";
				}
			}else if(obj instanceof Map){
				Map mapp = (Map)obj;
				if(mapp.containsKey(key)){
					(mapp.remove(key)).toString();
					res = "true";
				}else{
					res = "Map中无键" + key;
				}
			}else{
				res = "基本数据类型,无子对象";
			}
		}
		
		return res;
	}
	
	@Override
	public boolean remove(String key) {
		map.remove(key);
		mapIndex.remove(key);
		return true;
	}

	@Override
	public void shutdown() {
		
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
		
		Object obj = map;
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
		Index index = null;
		int start = page.start();
		int stop = page.stop();
		int count = 0;
		boolean ffExpire = false;
		if(rootKey.length() > 0){
			index = mapIndex.get(rootKey);
			ffExpire = index.isExpire();
		}
		for(Entry<String, Object> item : set){
			String ikey = item.getKey();
			if(rootKey.length() <= 0){
				index = mapIndex.get(ikey);
				ffExpire = index.isExpire();
			}
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
			temp.set("MTIME", index.mtime);
			if(expire == 1 && ffExpire) continue;
			if(expire == 2 && !ffExpire) continue;
			temp.set("EXPIRE", index.expire);
			temp.set("COUNT", index.count);
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
		Index index = null;
		int start = page.start();
		int stop = page.stop();
		int count = 0;
		boolean ffExpire = false;
		if(rootKey.length() > 0){
			index = mapIndex.get(rootKey);
			ffExpire = index.isExpire();
		}else{
			index = new Index();
		}
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
			temp.set("MTIME", index.mtime);
			if(expire == 1 && ffExpire) continue;
			if(expire == 2 && !ffExpire) continue;
			temp.set("EXPIRE", index.expire);
			temp.set("COUNT", index.count);
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
		return Type.MAP;
	}

}
