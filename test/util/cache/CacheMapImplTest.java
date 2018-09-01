package util.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.Map;

import org.junit.Test;

import util.Bean;
import util.Tools;
import util.cache.CacheMapImpl;
import util.cache.CacheRedisImpl;
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
	}

}
