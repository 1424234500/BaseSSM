package com.walker.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;



/**
 * map list常用工具
 * @author Walker
 *
 */
public class MapListUtil {

	public static void main(String[] argc){
		testListSetMap();
	}

	/**
	 *  测试list set map相关区别 争议点
	 */
	public static void testListSetMap(){
		Tools.out("-------测试list set map相关区别 争议点");

		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = getMap().put("key1", "value1").put(null, "value null").put("value null", null).build();
		Map<String, Object> map2 = getMap().put("key2", "value2").build();

		Map<String, Object> map22 = getMap().put("key2", "value2").build();
		Map<String, Object> map3 = map;
		Tools.out("map.keySet null键唯一 键集合",map.keySet());
		Tools.out("map.entrySet 键值集合",map.entrySet());
		//for (Map.Entry<String, Object> entry : map.entrySet())

		list.add(map);
		list.add(map2);
		Tools.out("hash map键值可null 无序",list);
		Tools.out("map==map", map==map, "map.equals(map)", map.equals(map),
				"map==map2", map==map2, "map.equals(map2)", map.equals(map2),
				"map==map3", map==map3, "map.equals(map3)", map.equals(map3),
				"map2==map22", map2==map22, "map2.equals(map22) equals有效值判断=", map2.equals(map22)
		);
		list.add(map);
		list.add(map3);
		Tools.out("list一种数组 可重复键值 可null add对象的引用唯一", list);

		HashSet<Map> set = new HashSet<Map>();
		set.add(map);
		set.add(map2);
//		Tools.out("set 不同map", set);
		set.add(map);
		set.add(map3);
		set.add(map22);
		set.add(null);
		Tools.out("set 一种数组(list) 允许一个null 相同引用(map) add对象的value equals判断不重复  重复覆盖", set);

		HashMap<String, Object> hmap = new HashMap<>();
		hmap.put(null, "valueNull");
		hmap.put("keynull", null);
		hmap.put("key1", "value1");

		Tools.out("hash map 键值可null HashMap去掉了HashTable的contains方法，但是加上了containsValue()和containsKey()方法", hmap);

		Hashtable<String, Object> htable = new Hashtable<>();
//		htable.put(null, "valueNull");
//		htable.put("keynull", null);
		htable.put("key1", "value1");

		Tools.out("hash table 键值不能null 。HashTable sync 锁", htable);

		Tools.out("-----------");

		List<Map<String, Object>> li = new ArrayList<Map<String, Object>>();
		for(int i = 0; i < 2; i++){
			Map<String, Object> mm = getMap()
					.put("id", "id-" + i)
					.put("name", "name-" + i)
					.build();
			li.add(0, mm);
		}
		Tools.out(li);
		li = turnListMap(li);
		Tools.out(li);




	}
	public static Map copy(Map<Object, Object> map){
		return copy(map, map.keySet().toArray());
	}
	public static Map copy(Map map, Object...keys){
		Map res = new HashMap<>();
		for(Object key : keys){
			res.put(key, map.get(key));
		}
		return res;
	}
	/**
	 * 获取Map工厂build模式
	 * @return
	 */
	public static MakeMap getMap(){
		return new MakeMap();
	}
	public static MakeMap map(){
		return new MakeMap();
	}
	public static MakeLinkMap getLinkMap(){
		return new MakeLinkMap();
	}
	/**
	 * 获取ArrayList
	 * @return
	 */
	public static MakeList getList(){
		return new MakeList();
	}
	public static MakeList array(){
		return new MakeList();
	}
	/**
	 * 获取ArrayList
	 * @return
	 */
	public static List<Map> getListMap(){
		return new ArrayList<Map>();
	}
	/**
	 * 获取某键值索引第一个
	 */
	public static int getCountListByName(List<Map<String,Object>> list, String name, String value){
		if(list == null)return -1;
		for(int i = 0; i < list.size(); i++){
			if(list.get(i).get(name).toString().equals(value)){
				return i;
			}
		}
		return -1;

	}

	/**
	 * List<Map> 转换为 List<Map<String, String>
	 * @param list
	 * @return
	 */
	public static List<Map<String, String>> getList(List<Map> list){
		List<Map<String, String>> res = new ArrayList<Map<String, String>>();
		if(list != null && list.size() > 0){
			for(int i = 0; i < list.size(); i++){
				Map<String,String> map =new HashMap<String,String>(list.get(i));
				res.add(map);
			}
		}
		return res;
	}

	/**
	 * 获取list<map>的第i行name列
	 */
	public static String getList(List<Map<String, Object>> list, int i, String name){
		return getList(list, i, name, "null");
	}
	public static String getList(List<Map<String, Object>> list, int i, String name, String defaultValue){
		if(list == null) return "list is null";
		if(i < 0)return "i < 0";
		if(i >= list.size())return "i > list size";
		if(list.get(i).get(name) == null){
			return defaultValue;
		}else{
			return getMap(list.get(i), name);
		}
	}
	public static String getMap(Map map, String name){
		return getMap(map, name, "");
	}

	public static <T> T getMap(Map map, Object key, T defaultValue){
		if(map == null) return defaultValue;
		
		Object obj = map.get(key);
		T res = null;
		if(obj == null)obj = map.get(key.toString().toLowerCase());
		if(obj == null)obj = map.get(key.toString().toUpperCase());

		if(obj == null) {
			res = defaultValue;
		}else{
			if (defaultValue instanceof String) {
				res = (T)(obj.toString());
			} else if (obj instanceof String) {
				if (defaultValue instanceof Integer) {
					res = (T)(new Integer(Tools.parseInt(obj.toString())));
				} else if (defaultValue instanceof Double) {
					res = (T)(new Double(Tools.parseDouble(obj.toString())));
				}else{
					res = (T)obj;
				}
			}else{
				res = (T)obj;
			}
		}

		return res;
	}


	/**
	 * 转换Map<String, Object> -> Map<String, Double>
	 * @param map
	 * @return
	 */
	public static Map<String, Double> map2sdmap(Map<String, Object> map){
		Map<String, Double> res = new HashMap<String, Double>();
		for(String key : map.keySet()){
			res.put(key, (Double)(map.get(key)) );
		}
		return res;
	}
	/**
	 * 转换Map<String, Object> -> Map<String, String>
	 * @param map
	 * @return
	 */
	public static Map<String, String> map2ssmap(Map<String, Object> map){
		Map<String, String> res = new HashMap<String, String>();
		for(String key : map.keySet()){
			res.put(key, map.get(key).toString());
		}
		return res;
	}
	/**
	 * 转换Map<String, Object> <- Map<String, String>
	 * @param map
	 * @return
	 */
	public static Map<String, Object> map2map(Map<String, String> map){
		Map<String, Object> res = new HashMap<String, Object>();
		for(String key : map.keySet()){
			res.put(key, map.get(key));
		}
		return res;
	}

	/**
	 * List<Map> 转换为 可读的String
	 * @param list
	 * @return
	 */
	public static String list2string(List<Map> list){
		String res = "[ \n";
		for(Map<String,Object> map: list){
			res += map.toString();
			res += "\n";
		}
		res += " ]";
		return res;
	}

	/**
	 * 有序合并List
	 * @param list1
	 * @param list2
	 * @return
	 */
	public static List  listAdd( List  list1, List  list2) {
		if(list1 != null ){
			if(list2 != null){
				for( int i = 0;i < list2.size(); i++){
					list1.add(list2.get(i));
				}
			}
		}else{
			list1 = list2;
		}

		return list1;
	}

	public static List<List<String>> toArrayAndTurn(List<Map<String, Object>> list){
		return turnListString(toArray(list));
	}

	public static List<List<String>> toArray(List<Map<String, Object>> list){
		List<List<String>> res = new ArrayList<List<String>>();

		if(list != null && list.size() > 0){
			Set set = list.get(0).keySet();
			int colSize = set.size();
			int rowSize = list.size();

			for(int i = 0; i < rowSize; i++){
				List<String> ll = new ArrayList<String>();
				for (Object key : set) {
					ll.add(getList(list, i, ""+key));
				}
				res.add(ll);
			}
		}

		return res;
	}
	public static List<List<String>> turnListString(List<List<String>> list){

		List<List<String>> res = new ArrayList<List<String>>();

		if(list != null && list.size() > 0){
			int colSize = list.get(0).size();
			int rowSize = list.size();
			for(int i = 0; i < colSize; i++){
				List<String> ll = new ArrayList<String>();
				for(int j = 0; j < rowSize; j++){
					ll.add( list.get(j).get(i));
				}
				res.add(ll);
			}
		}

		return res;
	}
	/**
	 * List<Map> 行列转换
	 * row1: col11, col12, col13
	 * row2: col21, col22, col23
	 * row3: col31, col32, col33
	 * row4: col41, col42, col43
	 * ->
	 * 		col11, col21, col31, col41
	 * 		col12, col22, col32, col42
	 * 		col13, col23, col33, col43
	 */
	public static List<Map<String, Object>> turnListMap(List<Map<String, Object>> list){

		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();

		if(list != null && list.size() > 0){
			Set set = list.get(0).keySet();
			int colSize = set.size();
			int rowSize = list.size();
			int cc = 0;
			for (Object key : set) {
//				Tools.out(key);
				Map<String, Object> col = getLinkMap().build();
				for(int i = 0; i < rowSize; i++){
					col.put("col"+i, getList(list, i, ""+key));
				}
				res.add(0, col);
				cc++;
			}

		}

		return res;
	}
	/**
	 * 根据url 获取对象 map.key1.listcc[0].list[2].key3
	 * @param map
	 * @param urls
	 * @return null/obj
	 */
	public static <T> T getMapUrl(Map map, String urls){
		return getMapUrl(map, urls, null);
	}
	public static <T> T getMapUrl(Map map, String urls, T defaultValue){
		T res = defaultValue;
		Object obj = map;
		Object temp = null;
		String toUrl = ""; //实际路径
		String itemCopy = "";
		
		if(urls.length() <= 0){	//非查询root
			res = (T)map;
		} else{
			String[] arr = urls.split("\\."); // map.list[0].map.aaa   map.list
			int cc = -1;
			for(int i = 0; i < arr.length; i++){
				String item = arr[i];
				itemCopy = item;
				//list[0] -> list 0
				cc = -1;
				if(item.charAt(item.length() - 1) == ']'){ //数组
					item = item.substring(0, item.length() - 1); //去除]
					item = item.replace('[', ' ');
					String[] ss = item.split(" ");
					if(ss.length >= 2){
						item = ss[0];
						cc = Tools.parseInt(ss[1], -1);
					}
				}
				if(obj instanceof List){ //数组 list 不出现该情况
					break;
				}else if(obj instanceof Map){//最后查询层级应该是此 
					Map objMap = (Map)obj;
					temp = objMap.get(item); //预读取取出值为 map list ? 否则中断跳出
					if(temp == null) break;
					if(temp instanceof Map){	//取出对象为map
						obj = temp;
					}else if(temp instanceof List){ //输出对象为list
						List tempList = (List)temp;
						if(cc >= 0 && cc < tempList.size()){ //list[2]
							obj = tempList.get(cc);
						}else{ //list
							obj = temp;
						}
					}else{ //基本类型
						obj = temp;
					}
				}else{ //已经是基本类型则 不再继续子层级查询 理应不存在访问此
					break;
				}
				toUrl += itemCopy + ".";
			}
			if(toUrl.length() > 0)
				toUrl = toUrl.substring(0, toUrl.length() - 1);
			if(toUrl.equals(urls)){ 
				res = (T)obj;
			}
		} 
		return res;
	} 

	/**
	 * 按照url添加
	 * put map1.map11.cc test
	 * @return 
	 */
	public static String putMapUrl(Map<String, Object> map, String urls, Object value){
		if(urls.length() == 0) return null;
		String key = "";
		String[] keys = urls.split("\\."); //map1,   map1 map11
		key = keys[0];
		Object make = value;
		for(int i = keys.length - 1; i >= 0; i--){
			urls = urls.substring(0, urls.length() - Math.min(urls.length(),(keys[i].length() + 1))); //map1.map11 -> map1 map11-value
			if(i == 0){//
				map.put(keys[i], make);
				break;
			}else{
				Object temp = getMapUrl(map, urls);
				if(temp == null){
					make = new Bean().put(keys[i], make); //map{map11:value}
				}else{ //必须为map
					if(temp instanceof Map)
						((Map<String, Object>)temp).put(keys[i], make);
					else//找到上层url 替换为新map
						putMapUrl(map, urls, value);
					break;
				}
			}
		}
		return key;
	}










	public static List<Map> testList(){
		List<Map> res = new ArrayList<Map>();

		for(int i = 0; i < 4; i++){
			res.add(
					map()
							.put("id", i)
							.put("name", "name-" + i)
							.build()
			);
		}

		return res;
	}

	public static Map<String, String> testSSMap(){
		Map<String, String> res = new LinkedHashMap<>();
		for(int i = 0; i < 3; i++){
			res.put("id", "id" + i);
			res.put("name", "name" + i);
		}
		return res;
	}
	public static Map<String, Object> testSOMap(){
		Map<String, Object> res = new LinkedHashMap<>();
		for(int i = 0; i < 3; i++){
			res.put("id", "id" + i);
			res.put("name", "name" + i);
		}
		return res;
	}
	public static Map<String, Double> testSDMap(){
		Map<String, Double> res = new LinkedHashMap<>();
		for(int i = 0; i < 3; i++){
			res.put("id",  i * 1.0);
			res.put("name", i * 2.0);
		}
		return res;
	}

}