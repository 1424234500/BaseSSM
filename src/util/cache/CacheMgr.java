package util.cache;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.Bean;
import util.Call;
import util.FileUtil;
import util.MapListUtil;
import util.Tools;
import util.database.Dao;
import util.setting.SettingUtil;

/**
 * 缓存构造 构造完毕后并初始化缓存 
 * 单例并发交由具体实现控制 
 * 此处不做单例控制
 *
 */
public class CacheMgr implements Call{

	/**
	 * 用以保证默认的cache只初始化一次
	 */
	private static Cache<String> cache = null;

	public CacheMgr() {
	}

	public static Cache<String> getInstance() {
		if (cache == null) {
			cache = getInstance(Type.MAP);
			reload(cache);
		}
		return cache;
	}

	public static Cache<String> getInstance(Type type) {
		Cache<String> cache = null;
		switch (type) {
		case MAP:
			cache = new CacheMapImpl();
			break;
		case EHCACHE:
			cache = new CacheEhcacheImpl();
			break;
		case REDIS:
			cache = new CacheRedisImpl();
			break;
		default:
			cache = new CacheMapImpl();
		}
		return cache;
	}

	/**
	 * 初始化cache 系统级数据 环境设置读取 词典加载 额外配置项
	 * 1.加载配置文件
	 * 2.加载数据库 
	 */
	public static void reload(Cache<String> cache){
		String classRoot = CacheMgr.class.getResource("/").getPath();
		File dir = new File(classRoot);
		Tools.out(dir.getPath(), dir.getAbsolutePath(), dir.getName(), dir.getPath(),dir.list());
		for (String item : dir.list()) {
			String path = classRoot + item;
			if (FileUtil.check(path) == 0 && path.endsWith(".properties")) {
				cache.putAll(SettingUtil.getSetting(path));
			}
		}
		
//		Dao dao = new Dao();
//		//key value info time
//		for(Map<String, Object> keyValue : dao.queryList("select * from sys_config")){
//			String key = (String) keyValue.get("KEY");
//			Object value = keyValue.get("VALUE");
//			cache.put(key, value);
//		}
	}
	
	public void call(){
		Cache<String> cache = getInstance();
		if(cache == null) return ;
		
		List<Object> list = new ArrayList<>();
		list.add("string item");
		list.add(1111111);
		list.add(Bean.getBean().put("key of list map", "aldkjfakljf").put("keyint", 2222));
		Bean map = Bean.getBean().put("key1", 111).put("key2", 222);
		Bean bean = Bean.getBean().put("key1", 111).put("key2", map);
		Bean bean2 = Bean.getBean().put("key1", 333).put("key2", map).put("key3", bean);
		list.add(bean);
		list.add(bean2);
		list.add(map);
		
		cache.put("int", 1);
		cache.put("long", 998);
		cache.put("string", "the is a string");
		cache.put("map", 
			Bean.getBean().put("key-int", 2)
				.put("key-map", Bean.getBean().put("key-map-key-int", 3))
				.put("key-list", list)
				.put("list", list)
				.put("map1", map)
				.put("map2", bean)
				.put("map3", bean2)
				);
		cache.put("list", list);
		
	}

}

enum Type {
	MAP, EHCACHE, REDIS,
}
