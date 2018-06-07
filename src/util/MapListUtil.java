package util;

import org.json.JSONArray;

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

		Map map = getMap().put("key1", "value1").put(null, "value null").put("value null", null).build();
		Map map2 = getMap().put("key2", "value2").build();

		Map map22 = getMap().put("key2", "value2").build();
		Map map3 = map;
		Tools.out("map.keySet 键集合",map.keySet());
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
		Tools.out(list);

		HashSet<Map> set = new HashSet<Map>();
		set.add(map);
		set.add(map2);
		Tools.out("set 不同map", set);
		set.add(map);
		set.add(map3);
		set.add(map22);
		Tools.out("set 相同引用map equals判断不重复", set);

		HashMap<String, Object> hmap = new HashMap<>();
		hmap.put(null, "valueNull");
		hmap.put("keynull", null);
		hmap.put("key1", "value1");

		Tools.out("hash", hmap);


		Hashtable<String, Object> htable = new Hashtable<>();
//		htable.put(null, "valueNull");
//		htable.put("keynull", null);
		htable.put("key1", "value1");

		Tools.out("table 键值", htable);

		Tools.out("-----------");

		List<Map<String, Object>> li = new ArrayList<Map<String, Object>>();
		for(int i = 0; i < 2; i++){
			Map mm = getMap()
					.put("id", "id-" + i)
					.put("name", "name-" + i)
					.build();
			li.add(0, mm);
		}
		Tools.out(li);
		li = turnListMap(li);
		Tools.out(li);




	}

	public static int getMapSize(Map map){
		return map.keySet().size();
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
			return getMap(list.get(i), "name");
		}
	}
	public static String getMap(Map map, String name){
		return getMap(map, name, "");
	}

	public static <T> T getMap(Map map, Object key, T defaultValue){
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
				Map col = getLinkMap().build();
				for(int i = 0; i < rowSize; i++){
					col.put("col"+i, getList(list, i, ""+key));
				}
				res.add(0, col);
				cc++;
			}

		}

		return res;
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