package util.cache;

import org.junit.Test;

import util.ThreadUtil;
import util.Tools;

public class TestEhcache1 {
	@Test
	public void test() {
		Cache<String> cache = CacheMgr.getInstance(Type.EHCACHE);
		
//		cache.put("key", "value1");
		
		while(true) {
			Tools.out("before", cache.get("key"));
//			cache.put("key", "value-"+this.getClass().getName() + "-" + Tools.getNowTimeLS());
//			Tools.out("after", cache.get("key"));
			ThreadUtil.sleep(5000);
		}
		
	}
	
	
}
