package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * org.json.tools
 */


public class JsonUtil {
	/**
	 * map转json
	 * @param obj
	 */
	public static String makeJson(Map obj) {
        String res = "";
        try {
            JSONObject jo = new JSONObject(obj);
            res = jo.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    } 
	/**
	 * list转json
	 * @param list
	 */
    public static String makeJson(List list) {
        String res = "";
        try {
            JSONArray ja = new JSONArray(list);
            res = ja.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
    public static String makeJson(Object cmd, Map obj) {
        String res = "";
        try {
            JSONObject jomap = new JSONObject(obj);

            JSONObject jo = new JSONObject();
            jo.put("cmd", cmd);
            jo.put("result", jomap);
            res = jo.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
    public static String makeJson(Object cmd, List<Object> list) {
        String res = "";
        try {
            JSONObject jo = new JSONObject();
            jo.put("cmd", cmd);
            jo.put("result", toJSONArray(list));
            res = jo.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    // msg json {"cmd":"conn","value":"connok" }
    public static String makeJson(Object cmd, Object... values) {
        String res = "";
        try {
            JSONObject jo = new JSONObject();
            jo.put("cmd", cmd);
            int i = 0;
            for (Object value : values) {
                jo.put("value" + i++, value);
            }
            res = jo.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    
    /**
     * 递归 map 编码 为 JSONObject
     */
    private static JSONObject toJSONObject(Map map){
    	JSONObject res = new JSONObject(map);
    	return res;
    }
    /**
     * 递归 list 编码 为 JSONArray
     */
    private static JSONArray toJSONArray(List list){
    	JSONArray res = new JSONArray(list);
    	return res;
    }
    /**
     * 递归 解析JSONArray为list
     */
    private static List toList(JSONArray ja) throws JSONException{
    	List list = new ArrayList<>();
    	for(int i = 0; i < ja.length(); i++){
        	Object object = ja.get(i);
            if (object instanceof JSONArray) {  
                JSONArray jaa = (JSONArray) object;  
                list.add(toList(jaa));
            }  
            else if (object instanceof JSONObject) {  
            	JSONObject joo = (JSONObject)object;
                list.add(toMap(joo));
            }
            else{ // 普通 类型 int string double ...
            	list.add(object);
            }
        }
    	return list;
    }
    /**
     * 递归 解析JSONObject为map
     */
    private static Map toMap(JSONObject jo) throws JSONException{
    	Map map = new HashMap();
        //迭代多有的Key值  
        Iterator it = jo.keys();  
        //遍历每个Key值  
        while (it.hasNext()) {  
            //将key值转换为字符串  
            String key = it.next().toString();  
            Object object = jo.get(key);  
            if (object instanceof JSONArray) {  
                JSONArray jaa = (JSONArray) object;  
                map.put(key, toList(jaa));
            }  
            else if (object instanceof JSONObject) {  
            	JSONObject joo = (JSONObject)object;
            	map.put(key, toMap(joo));
            }
            else{ // 普通 类型 int string double ...
            	map.put(key, object);
            }
        }  
    	return map;
    }
    /**
     * 解析json为map 
     * @param jsonstr
     */
    public static Map<String, Object> getMap(String jsonstr) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            JSONObject jo = new JSONObject(jsonstr);
            map = toMap(jo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
    /**
     * 解析json为list
     * @param jsonstr
     */
    public static List<Map<String, Object>> getList(String jsonstr) {
        List<Map<String, Object>> res = new ArrayList<>();
        try {
        	JSONArray ja = new JSONArray(jsonstr);
            res = toList(ja);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    } 

    public static void out(String str) {

    }
}
