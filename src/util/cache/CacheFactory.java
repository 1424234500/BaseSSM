package util.cache;

import java.io.File;

import util.FileUtil;
import util.Tools;
import util.setting.SettingUtil;

/**
 * 缓存构造工具 单例并发交由具体实现控制 此处不做单例控制
 *
 */
public class CacheFactory {
	
	private static Cache<?> cache = null;
	
	private CacheFactory() {
	}
	
	public static Cache<?> getInstance() {
		if(cache == null){
			cache = getInstance(CacheType.MAP);
			init(cache);
		}
		return cache;
	}

	public static Cache<?> getInstance(CacheType type) {
		Cache<?> cache = null;
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
	 * 初始化cache 系统级数据 
	 * 环境设置读取
	 * 词典加载
	 * 额外配置项
	 */
	
	private static void init(Cache<?> cache){
		String classRoot = CacheFactory.class.getResource("/").getPath();
		File dir = new File(classRoot);
//		Tools.out(dir.getPath(), dir.getAbsolutePath(), dir.getName(), dir.getPath(),dir.list());
		for(String item : dir.list()){
			String path = classRoot + item;
			if(FileUtil.check(path) == 0 && path.endsWith(".properties")){
				cache.putAll(SettingUtil.getSetting(path));
			}
		}
	}

}

enum CacheType {
	MAP, EHCACHE, REDIS,
}
