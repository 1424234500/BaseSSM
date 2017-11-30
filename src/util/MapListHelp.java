package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * map list常用工具
 * @author Walker
 *
 */
public class MapListHelp {

	
	/**
	 * 获取Map工厂build模式
	 * @return
	 */
	public static MakeMap getMap(){
		return new MakeMap();
	}
	 
	/**
	 * 获取ArrayList
	 * @return
	 */
	public static List<Map> getList(){
		return new ArrayList<Map>();
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
	 * @param list
	 * @param i
	 * @param name
	 * @return
	 */
	public static String getList(List<Map> list, int i, String name){
		if(list == null) return "list is null";
		if(i < 0)return "i < 0";
		if(i >= list.size())return "i > list size";
		if(list.get(i).get(name) == null){
			return "null";
		}else{
			return list.get(i).get(name).toString();
		}
	}
	/**
	 * 获取map的name列
	 * @param map
	 * @param name
	 * @return
	 */
	public static String getMap(Map map, String name){
		if(map == null)return "map is null";
		if(map.get(name) == null){
			return "";
		}else{
			return map.get(name).toString();
		}
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
	public static List<Map> listAdd( List<Map> list1, List<Map> list2) {
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
 
	
	
	
	
	
}