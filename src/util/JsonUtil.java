package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;







import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/*
 * msg json {"cmd":"conn","value":"connok" }
 * Json map list 工具类
 */
public class JsonUtil {

	 
	public static String getString(String jsonstr, String name) {
		String res = "";
		try {
			JSONObject jb = JSONObject.fromObject(jsonstr);
			res = jb.getString(name);
		} catch (Exception e) {
			out("json get error from " + jsonstr + " exception：" + e.toString());
		}
		return res;
	}

	public static int getInt(String jsonstr, String name) {
		int res = -1;
		try {
			JSONObject jb = JSONObject.fromObject(jsonstr);
			res = jb.getInt(name);
		} catch (Exception e) {
			out("json get error from " + jsonstr + " exception：" + e.toString());
		}
		return res;
	}

	public static List<Map<String, Object>> getList(String jsonstr, String name){
		try {
			JSONObject jo = JSONObject.fromObject(jsonstr);
			return JsonUtil.jsonArrayToListMap(jo.getJSONArray(name));
		} catch (Exception e) {
			out("json get list error from " + jsonstr + " exception：" + e.toString());
		}
		return null;
	}
	 
	public static Map<String, Object> getMap(String jsonstr){
		try {
			JSONObject jo = JSONObject.fromObject(jsonstr);
			
			Iterator<String> nameItr = jo.keys();
			Map<String, Object> map = new HashMap<String, Object>();
			while (nameItr.hasNext()) {
				String name = nameItr.next();
				map.put(name, jo.get(name));
			}
			return map;
		} catch (Exception e) {
			out("json get map error from " + jsonstr + " exception：" + e.toString());
		}
		return null;
	} 
	public static JSONArray listToJSONArray(Object[] list){
		JSONArray ja = new JSONArray();
		if(list == null)return ja;
		for(Object map: list){
			 ja.add(map);
		} 
		return ja;
	}	
	public static JSONArray listMapToJSONArray(List<Object> list){
		JSONArray ja = new JSONArray();
		if(list == null)return ja;
		for(Object map: list){
			 ja.add(  JSONObject.fromObject(map));
//			for (Map.Entry<String, Object> entry : map.entrySet()) {   }
		} 
		return ja;
	}	
	public static List<Map<String, Object>>   jsonArrayToListMap(JSONArray jsonArray){
		List<Map<String, Object>> list = new  ArrayList<Map<String, Object>>();
		String name;

		for(int i = 0; i < jsonArray.size(); i++){
			JSONObject jo = jsonArray.getJSONObject(i);
			
			Iterator<String> nameItr = jo.keys();
			Map<String, Object> map = new HashMap<String, Object>();
			while (nameItr.hasNext()) {
				name = nameItr.next();
				map.put(name, jo.getString(name));
			}
			list.add(map);
		}
		 
		return list;
	}	

	
	public static String makeJson(String key, Object[] list){
		String res = "";
		try { 
			JSONObject jo = new JSONObject();
			jo.put(key, JsonUtil.listToJSONArray(list));
			res = jo.toString();
		} catch (Exception e) {
			e.printStackTrace(); 
		}

		return res;
	}
	public static String makeJson(Object[] list){
		String res = "";
		try { 
			res = JsonUtil.listToJSONArray(list).toString();
		} catch (Exception e) {
			e.printStackTrace(); 
		}

		return res;
	}
	public static String makeJson(List<Object> list){
		String res = "";
		try { 
			res = JsonUtil.listMapToJSONArray(list).toString();
		} catch (Exception e) {
			e.printStackTrace(); 
		}

		return res;
	}
	public static String makeJson(String key, List<Object> list){
		String res = "";
		try {
			JSONObject jo = new JSONObject();
			jo.put(key, JsonUtil.listMapToJSONArray(list));
			res = jo.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

 
	
	public static String makeJson(String key, Map<String, Object> map){
		String res = "";
		try {
			JSONObject jomap = new JSONObject();
			jomap.putAll(map);
			
			JSONObject jo = new JSONObject();
			jo.put(key, jomap);
			res = jo.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	} 
	public static String makeJson(Map<String, Object> map){
		String res = "";
		try {
			JSONObject jomap = new JSONObject();
			jomap.putAll(map);
			res = jomap.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}


	public static void out(String str) {
		
	}
	

}
