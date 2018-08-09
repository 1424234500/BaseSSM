package util.cache;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Map;

import org.junit.Test;

import util.Bean;
import util.Tools;

public class CacheMapImplTest {
	static CacheMapImpl cache = new CacheMapImpl();
	static{
		for(int i = 0; i < 3; i++){
			cache.put("k" + i, "v" + i);
		}
	}
	
	@Test
	public void testSize() {
		assertEquals(3, cache.size());
	}

	@Test
	public void testIsStart() {
		assertEquals(true, cache.isStart());;
	}

	@Test
	public void testIsEmpty() {
		assertEquals(false, cache.isEmpty());;
	}

	@Test
	public void testContainsKey() {
		assertEquals(true, cache.containsKey("k1"));
		assertEquals(false, cache.containsKey("k999"));
	}

	@Test
	public void testContainsValue() {
		assertEquals(true, cache.containsValue(Bean.getBean().put("k1", "v1")));
		assertEquals(false, cache.containsValue(Bean.getBean().put("k1", "v9991")));
	}

	@Test
	public void testPutAll() {
		Bean bean = Bean.getBean().put("p1", "pv1").put("p2", "pv2");
		cache.putAll(bean);
		assertEquals(true, cache.containsKey("p1"));
		assertEquals(true, cache.containsKey("p2"));
	}

	@Test
	public void testGetAll() {
		Map map = cache.getAll();
		Tools.out(map);
		assertEquals(5, map.size());
	}

	@Test
	public void testClear() {
		cache.clear();
		assertEquals(0, cache.size());
	}

	@Test
	public void testKeySet() {
		cache.put("kkk", "vvv");
		Tools.out(cache.keySet());
		assertEquals(1, cache.size());
	}

	@Test
	public void testValues() {
		Collection<Object> objs = cache.values();
		Tools.out(objs);
		assertEquals(1, objs.size());
	}

	@Test
	public void testGetString() {
		assertEquals("vvv", cache.get("kkk"));
		assertEquals(null, cache.get("kkdajdflk"));
		
	}

	@Test
	public void testGetStringV() {
		assertEquals("vvv", cache.get("kkk", ""));
		assertEquals("bbb", cache.get("askdfjals", "bbb"));
	}

	@Test
	public void testPutStringV() {
		assertEquals("putsvalue", cache.put("puts", "putsvalue").get("puts", ""));
	}

	@Test
	public void testPutStringVLong() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemove() {
		assertEquals(true, cache.remove("puts"));
		assertEquals(false, cache.remove("putsadfjk"));
	}

	@Test
	public void testShutdown() {
	}

	@Test
	public void testStartup() {
	}

}
