package util;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import util.database.RedisMgr;

public class TestRedis {

	@Test
	public void makeDay(){


		for(int i = 0;i < 100; i++) {
			
			if(i % 2 == 0)
		RedisMgr.getInstance().doJedis(new RedisMgr.Fun<Object>() {
			@Override
			public Object make(Jedis obj) {
				Tools.out(obj.set("sss", 22222222 + ""));
				return null;
			}
		});
			else
		RedisMgr.getInstance().doJedis(new RedisMgr.Fun<Object>() {
			@Override
			public Object make(Jedis obj) {
				Tools.out(obj.get("sss"));
				return null;
			}
		});
		}
		
	}
}
