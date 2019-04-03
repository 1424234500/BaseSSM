package util.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.walker.common.util.Bean;
import com.walker.common.util.MapListUtil;
import com.walker.common.util.Tools;
import com.walker.core.cache.Cache;
import com.walker.core.cache.CacheMapImpl;
import com.walker.core.cache.CacheMgr;
import com.walker.core.cache.CacheRedisImpl;
import com.walker.core.cache.Type;
public class CacheMapImplTest extends CacheRedisImpl {
//	static Cache<String> cache = new CacheEhcacheImpl();
//	static{
//		for(int i = 0; i < 3; i++){
//			put("k" + i, "v" + i);
//		}
//	}
	/**
	 * 每次执行test函数 都会调用构造  测完就丢弃
	 */
	public CacheMapImplTest(){
		for(int i = 0; i < 3; i++){
			put("k" + i, "v" + i);
		}
	}
//	
//	@TestObjectCreate static JunitTester create(){
//		return new JunitTester("create");
//	}
//	

//	@TestProperty static List<String> input = Arrays.asList("a,b,c".split(","));
	
	
	@Test
	public void testSize() {
		assertEquals(3, size());
	}

	@Test
	public void testIsStart() {
		assertEquals(true, isStart());;
	}

	@Test
	public void testIsEmpty() {
		assertEquals(false, isEmpty());;
	}

	@Test
	public void testContainsKey() {
		assertEquals(true, containsKey("k1"));
		assertEquals(false, containsKey("k999"));
	}

	@Test
	public void testContainsValue() {
		assertEquals(true, containsValue(Bean.getBean().put("k1", "v1")));
		assertEquals(false, containsValue(Bean.getBean().put("k1", "v9991")));
	}

	@Test
	public void testPutAll() {
		Bean bean = Bean.getBean().put("p1", "pv1").put("p2", "pv2");
		putAll(bean);
		assertEquals(true, containsKey("p1"));
		assertEquals(true, containsKey("p2"));
	}

	@Test
	public void testGetAll() {
		Map map = getAll();
		Tools.out(map);
		assertEquals(3, map.size());
	}

	@Test
	public void testClear() {
		clear();
		assertEquals(0, size());
	}

	@Test
	public void testKeySet() {
		put("kkk", "vvv");
		Tools.out(keySet());
		assertEquals(4, size());
	}

	@Test
	public void testValues() {
		Collection<Object> objs = values();
		Tools.out(objs);
		assertEquals(3, objs.size());
	}

	@Test
	public void testGetString() {
		assertEquals("vvv", get("kkk"));
		assertEquals(null, get("kkdajdflk"));
		
	}

	@Test
	public void testGetStringV() {
		assertEquals("vvv", get("kkk", ""));
		assertEquals("bbb", get("askdfjals", "bbb"));
	}

	@Test
	public void testPutStringV() {
		assertEquals("putsvalue", put("puts", "putsvalue").get("puts", ""));
	}

	@Test
	public void testPutStringVLong() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemove() {
		assertEquals(true, remove("puts"));
		assertEquals(false, remove("putsadfjk"));
	}

	@Test
	public void testShutdown() {
	}

	@Test
	public void testStartup() { 
		Cache cache = CacheMgr.getInstance(Type.REDIS);
		cache.put("str01", "000", 3600 * 1000);
		cache.put("str01", "001", 60 * 1000);
		cache.put("str02", "000", 100);
		cache.put("str03", "000", 10);
		cache.put("str04", "000", 1);
		cache.put("mmm.bbb", "000", 1);
		cache.put("map", Bean.getBean().put("b1", "bk1").put("b2", "bk2"), 186 * 1000);
		List<Object> list = new ArrayList<>();
		list.add("list str1");
		list.add("list str2");
		list.add(Bean.getBean().put("b1", "bk1").put("b2", "bk2"));
		list.add(Bean.getBean().put("listStr", "bk1").put("listMap", Bean.getBean().put("listMapStr", "bk1").put("b2", "bk2")));
		cache.put("list", list, 186 * 1000);
		
		for(int i = 0; i < 100; i ++){
			cache.get(cache.keySet().toArray()[(int) (Math.random() * cache.keySet().size())]);
		}
		
		
		Tools.out("--------------全");
//		Tools.formatOut(cache.findCacheList(new Bean().put("URL", "")).get(key));
//		Tools.out("--------------基本数据");
//		Tools.formatOut(cache.findCacheList(new Bean().put("URL", "str01")));
//		Tools.out("--------------map");
//		Tools.formatOut(cache.findCacheList(new Bean().put("URL", "map")));
//		Tools.out("--------------list");
//		Tools.formatOut(cache.findCacheList(new Bean().put("URL", "list")));
//		Tools.out("--------------list[099]不存在");
//		Tools.formatOut(cache.findCacheList(new Bean().put("URL", "list[099]")));
//		Tools.out("--------------list[03]存在");
//		Tools.formatOut(cache.findCacheList(new Bean().put("URL", "list[03]")));
//
//		Tools.out("--------------list[03].listMap");
//		Tools.formatOut(cache.findCacheList(new Bean().put("URL", "list[03].listMap")));

		Tools.out("--------------list[03].listMap.listMapStr");
//		Tools.formatOut(cache.findCacheList(new Bean().put("URL", "list[03].listMap.listMapStr")));
		String str = "..aaa.bbb.ccc.";
		Tools.out(str.split("\\."));
		
		Tools.out(MapListUtil.getMapUrl(cache.getAll(), "list[03].listMap"));
		Tools.out(MapListUtil.getMapUrl(cache.getAll(), "list[03].listMap.listMapStr"));
		
	}
}
