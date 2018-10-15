package util.cache;

import org.junit.Test;

import util.Tools;

/**
 * 测试多线程下 各种缓存性能
 * 随机模拟 存取
 *
 */
public class TestCache{
	//10个线程
	int maxThread = 3;
	int nowThread = 0;
	//存取共次数
	int maxDo = 3000;
	int nowDo = 0;
	long lastTime = 0;
	@Test
	public void test1(){
		Cache<String> cache = null;
		cache = CacheMgr.getInstance(CacheType.MAP);
		testThread(cache);
	}
	@Test
	public void test2(){
		Cache<String> cache = null;
		cache = CacheMgr.getInstance(CacheType.EHCACHE);
		testThread(cache);
	}
	@Test
	public void test3(){
		Cache<String> cache = null;
		cache = CacheMgr.getInstance(CacheType.REDIS);
		testThread(cache);
	}
	public void testThread(final Cache<String> cache){
		//10个线程
		maxThread = 1;
		nowThread = 0;
		//存取共次数
		maxDo = 30;
		nowDo = 0;
		lastTime = System.currentTimeMillis();
		for(int i = 0; i < maxThread; i++){
			new Thread(cache.getType() + "-T" + i){
				public void run(){
					testCache(this.getName(), cache);
				}
			}.start();
		}
	}
	
	public int getRandom(int stop){
		return (int) (Math.random() * stop);
	}
	public void testCache(String name, Cache<String> cache){
		while(nowDo < maxDo){
			if(nowDo % 2 == 0){
				cache.put("key" + getRandom(100), name + "-value-" + getRandom(100));
			}else{
				cache.get("key" + getRandom(100), name + "-default");
			}
			nowDo ++;
		}
		Tools.out("End-" + name + "-" + nowThread++);
		if(nowThread >= maxThread){
		}
		Tools.out(Tools.calcTime(System.currentTimeMillis() - lastTime));

		Tools.out(cache.getAll());

	}
	
	
	
	
}
