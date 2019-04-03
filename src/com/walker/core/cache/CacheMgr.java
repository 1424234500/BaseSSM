package com.walker.core.cache;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.walker.common.setting.SettingUtil;
import com.walker.common.util.Bean;
import com.walker.common.util.Call;
import com.walker.common.util.FileUtil;
import com.walker.core.database.Dao;

/**
 * 缓存构造 构造完毕后并初始化缓存 
 * 单例并发交由具体实现控制 
 * 此处不做单例控制
 *
 */
public class CacheMgr implements Call{
	private static Logger log = Logger.getLogger("Cache"); 
	static public Type DEFAULT_TYPE = Type.MAP;
	
	/**
	 * 用以保证默认的cache只初始化一次
	 */
	private static Cache<String> cache = null;

	public CacheMgr() {
	}
    private static class SingletonFactory{           
        private static Cache<String> cache;
        static {
        	System.out.println("静态内部类初始化" + SingletonFactory.class);
			cache = getInstance(Type.MAP);
			reload(cache);
        }
    }
	public static Cache<String> getInstance() {
		return SingletonFactory.cache;
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
		}
		return cache;
	}

	/**
	 * 初始化cache 系统级数据 环境设置读取 词典加载 额外配置项
	 * 1.加载配置文件
	 * 2.加载数据库 
	 */
	public static void reload(Cache<String> cache){
		log.info("***初始化缓存");
		
		String classRoot = CacheMgr.class.getResource("/").getPath();
		File dir = new File(classRoot);
//		log.info(dir.getPath());
//		log.info(dir.getAbsolutePath());
//		log.info(dir.getName());
//		log.info(Arrays.toString(dir.list()));

		cache.put("cache_dir_getPath", dir.getPath());
		cache.put("cache_dir_getAbsolutePath", dir.getAbsolutePath());
		cache.put("cache_dir_getName", dir.getName());
		cache.put("files", Arrays.asList(dir.list()));
		
		for (String item : dir.list()) {
			String path = classRoot + item;
			if (FileUtil.check(path) == 0 && path.endsWith(".properties")) {
				cache.putAll(SettingUtil.getSetting(path));
			}
		}
		
		//initOracle(cache);
		log.info("**!初始化完毕------------------ ");

	}
	private static void initOracle(Cache<String> cache) {
		try {
			Dao dao = new Dao();
			//key value info time
			for(Map<String, Object> keyValue : dao.find("select * from sys_config")){
				String key = (String) keyValue.get("KEY");
				Object value = keyValue.get("VALUE");
				cache.put(key, value);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void call(){
		Cache<String> cache = getInstance();
		if(cache == null){
			log.error(" cache 初始化 异常 xxxxxxxxxxxxxxxxxxx");
			return;
		}
		
		log.info("** 开始测试 附加缓存--------");

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
		
		if(cache.get("int", 0) == 1){
			log.info(" save and read ok ");
		}else{
			log.error(" save and read error xxxxxxxxxxxxxxxx");
		}
		log.info("**! 附加缓存----------------");
	}

}

enum Type {
	MAP, EHCACHE, REDIS,
}



