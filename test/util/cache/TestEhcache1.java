package util.cache;

import org.junit.Test;

import com.walker.common.util.ThreadUtil;
import com.walker.common.util.Tools;
import com.walker.core.cache.Cache;
import com.walker.core.cache.CacheMgr;
import com.walker.core.cache.Type;

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
